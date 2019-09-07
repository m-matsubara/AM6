package com.mmatsubara.interpreter.statement;

import java.util.Map;

import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;


/**
 * try ステートメントです。<br/>
 * 
 * 作成日: 2005/01/16
 * 
 * @author m.matsubara
 */
public class TryStatement implements IStatement {
    private IStatement tryStatement = null;
    private Integer catchObjectId = null;
    private IStatement catchStatement = null;
    private IStatement finallyStatement = null;
    
    public TryStatement(IStatement tryStatement, Integer catchObjectId, IStatement catchStatement, IStatement finallyStatement) {
        this.tryStatement = tryStatement;
        this.catchObjectId = catchObjectId;
        this.catchStatement = catchStatement;
        this.finallyStatement = finallyStatement;
    }
    
    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#isUsingVariable(Integer id)
     */
    public int isUsingVariable(Integer id) throws ExpException {
        int result = 0;
        if (tryStatement != null)
            result += tryStatement.isUsingVariable(id);
        if (catchStatement != null)
            result += catchStatement.isUsingVariable(id);
        if (finallyStatement != null)
            result += finallyStatement.isUsingVariable(id);
        return result;
    }

    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#execute(com.mmatsubara.expresser.RuntimeData)
     */
    public StatementResult execute(RuntimeData runtime) throws ExpException {
        StatementResult receive = null;
        try {
            receive = tryStatement.execute(runtime);
            if (receive != null) {
                int statementType = receive.getStatementType();
                if (statementType == StatementResult.ST_EXCEPTION) {
                    try {
                        runtime.setValue(catchObjectId, receive.getResultValue());
                        receive = catchStatement.execute(runtime);
                        if (receive != null) {
                            return receive;
                        }
                    } finally {
                    }
                    return null;
                }
                return receive;
            }
        } finally {
            if (finallyStatement != null) {
                receive = finallyStatement.execute(runtime);
                if (receive != null) {
                    return receive;
                }
            }
        }
        return null;
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#registLocalVariable(java.util.Map)
     */
    public void registLocalVariable(Map varMap) {
        if (tryStatement != null)
            tryStatement.registLocalVariable(varMap);
        if (catchStatement != null)
            catchStatement.registLocalVariable(varMap);
        if (finallyStatement != null)
            finallyStatement.registLocalVariable(varMap);
        if (catchObjectId != null) {
            varMap.put(catchObjectId, this);    // 登録する値は何でも良い
        }
    }
    
}
