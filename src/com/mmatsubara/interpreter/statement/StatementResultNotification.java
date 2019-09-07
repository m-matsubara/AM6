package com.mmatsubara.interpreter.statement;

import com.mmatsubara.expresser.exception.ExpException;

/**
 * SpecialStatementの実行を例外として通知します。<br/>
 * EmbeddedScriptはbreak,continue,return,throwのステートメントの実行をStatementResultとして戻しますが、Expresserではその方法が使えないため、ExpExceptionに乗せて親環境に通知します。
 *  
 * 作成日: 2005/02/27
 * 
 * @author matsubara
 */
public class StatementResultNotification extends ExpException {
    /**
     * シリアルバージョンUIDです。（Eclipse3.1だと明示的に定義しないと怒られるので（？））<br/>
     * シリアライズしない（ハズ）なので本来必要ないはずです。 
     */
    private static final long serialVersionUID = 1703464079054172060L;

    private StatementResult statementResult;
    
    public StatementResultNotification(StatementResult statementResult) {
        super("Statement result notification", -1);    
        this.statementResult = statementResult;
    }

    public StatementResultNotification(String message, int position, StatementResult statementResult) {
        super(message, position);
        this.statementResult = statementResult;
    }
    
    /**
     * @return statementResult を戻します。
     */
    public StatementResult getStatementResult() {
        return statementResult;
    }
}
