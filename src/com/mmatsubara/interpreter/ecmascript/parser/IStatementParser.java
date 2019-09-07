package com.mmatsubara.interpreter.ecmascript.parser;

import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.interpreter.ScriptEngine;
import com.mmatsubara.interpreter.statement.BlockStatement;

/**
 * 各ステートメントの構文解析用インターフェイスです。<br/> 
 * 
 * 作成日: 2005/01/31
 * 
 * @author m.matsubara
 */
public interface IStatementParser {
    /**
     * ターゲットとなるステートメントを返します。
     * @return ターゲットとなるステートメント 
     */
    public String targetStatement();
    
    /**
     * ステートメントの構文解析をします。 
     * @param se スクリプトエンジン
     * @param block このブロックステートメントの最後に解析されたステートメントが追加される
     * @param statementWird 構文解析するステートメントの最初の単語
     * @param script ステートメント
     * @param begin 構文解析開始位置
     * @param end 構文解析終了位置
     * @param labelID ステートメントにラベルが付く場合そのID・つかない場合は null
     * @return 構文解析の次の開始位置
     * @throws ExpException 構文エラー
     */
    public int parseStatement(ScriptEngine se, BlockStatement block, String statementWird, char[] script, int begin, int end, Integer labelID) throws ExpException, ExpException;
}
