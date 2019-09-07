package com.mmatsubara.interpreter.statement;

import java.util.Map;

import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.parsedexpression.IParsedExpression;


/**
 * synchronized ステートメントです。<br/>
 * 
 * 作成日: 2005/05/17
 * 
 * @author m.matsubara
 *
 */
public class SynchronizedStatement implements IStatement {
    /** 同期化を行うための式です。 */
    private IParsedExpression synchronizedExpression;
    /** 同期中に実行されるステートメントです。 */
    private IStatement statement;
    
    
    public SynchronizedStatement(IParsedExpression synchronizedExpression, IStatement statement) {
        this.synchronizedExpression = synchronizedExpression;
        this.statement = statement;
    }


    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#isUsingVariable(Integer)
     */
    public int isUsingVariable(Integer id) throws ExpException {
        int result = 0;
        if (synchronizedExpression != null)
            result += synchronizedExpression.isUsingVariable(id);
        if (statement != null)
            result += statement.isUsingVariable(id);
        return result;
    }

    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#execute(com.mmatsubara.expresser.RuntimeData)
     */
    public StatementResult execute(RuntimeData runtime) throws ExpException {
        if (statement != null) {
            Object object = synchronizedExpression.evaluate(runtime);
            if (object instanceof IVariable)
                object = ((IVariable)object).getValue();
            
            StatementResult result;
            synchronized (object) {
                result = statement.execute(runtime);
            }
            return result;
        }
        return null;
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#registLocalVariable(java.util.Map)
     */
    public void registLocalVariable(Map varMap) {
        statement.registLocalVariable(varMap);
    }
}
