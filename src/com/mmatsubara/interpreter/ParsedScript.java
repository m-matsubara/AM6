package com.mmatsubara.interpreter;

import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.interpreter.statement.IStatement;
import com.mmatsubara.interpreter.statement.StatementResult;

/**
 * 解析済みスクリプトを表すクラスです。<br/>
 * 
 * 作成日: 2005/05/21
 * 
 * @author m.matsubara
 */

public class ParsedScript {
    /** 解析済みのトップレベルステートメント（通常はBlockStatement） */
    private IStatement topLevelStatement;
    /** ソース */
    private char[] source; 
    
    /** 
     * 解析時に使用された RuntimeData<br/> 
     * 実行時のランタイムデータは defaultRuntime とグローバル変数エリアを共有している必要がある
     */
    private RuntimeData defaultRuntime;
    
    /**
     * 解析済みスクリプトを作成する
     * @param topLevelStatement 実行されるステートメント(通常はBlockStatement)
     * @param defaultRuntime 
     */
    public ParsedScript(IStatement topLevelStatement, RuntimeData defaultRuntime) {
        this.topLevelStatement = topLevelStatement;
        this.defaultRuntime = defaultRuntime;
    }
    
    
    /**
     * スクリプトの実行を行う。<br/>
     * シングルスレッド環境用
     * @return 実行結果（戻り値）
     * @throws ExpException
     */
    public Object execute() throws ExpException {
        return execute(defaultRuntime);
    }
    
    /**
     * スクリプトの実行を行う。<br/>
     * マルチスレッド環境対応
     * @param runtime ランタイムデータ
     * @return 実行結果（戻り値）
     * @throws ExpException
     */
    public Object execute(RuntimeData runtime) throws ExpException {
        StatementResult result = topLevelStatement.execute(runtime);
        if (result != null) {
            switch (result.getStatementType()) {
            case StatementResult.ST_BREAK:
                throw new ExpException(ScriptEngine.getResString("NotProcessedBreak"), -1);
            case StatementResult.ST_CONTINUE:
                throw new ExpException(ScriptEngine.getResString("NotProcessedContinue"), -1);
            case StatementResult.ST_EXCEPTION:
                {
                    IExpObject error = (IExpObject)result.getResultValue();
                    String message = error.getProperty(StringToId.toId("message")).toString();
                    int position = -1;
                    try {
                        position = ((ExpNumberBase)error.getProperty(StringToId.toId("position"))).intValue();
                    } catch (Exception e) {
                    }
                    throw new ExpException(message, position);
                }
            case StatementResult.ST_RETURN:
            default:
                return result.getResultValue(); 
            }
        }
        return null;
    }
    
    /**
     * 新しいスレッドを用意してスクリプトの実行を行う。
     * @throws ExpException
     */
    public void executeNewThread() {
        ScriptThread thread = new ScriptThread(this);
        thread.start();
    }
    
    /**
     * ソースファイルのテキストを返します。
     * @return ソースファイルのテキスト
     */
    public char[] getSource() {
        return source;
    }
    
    /**
     * ソースファイルのテキストを設定します。
     * @param source ソースファイルのテキスト
     */
    public void setSource(char[] source) {
        this.source = source;
    }
    
    /**
     * デフォルトのランタイムオブジェクトを返します。
     * @return デフォルトのランタイムオブジェクト
     */
    public RuntimeData getDefaultRuntime() {
        return defaultRuntime;
    }
    
    /** 
     * デフォルトのランタイムオブジェクトを設定します。
     * @param defaultRuntime デフォルトのランタイムオブジェクト
     */
    public void setDefaultRuntime(RuntimeData defaultRuntime) {
        this.defaultRuntime = defaultRuntime;
    }
}
