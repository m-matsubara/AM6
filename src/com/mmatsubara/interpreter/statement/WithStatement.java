package com.mmatsubara.interpreter.statement;

import java.util.Map;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.parsedexpression.IParsedExpression;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.interpreter.ScriptEngine;


/**
 * with ステートメントです。<br/>
 * 
 * 作成日: 2005/01/16
 * 
 * @author m.matsubara
 */
public class WithStatement implements IStatement {
    private IParsedExpression withExpression;
    private IStatement withStatement = null;
    private int position;
    
    public WithStatement(IParsedExpression withExpression, IStatement withStatement, int position) {
        this.withExpression = withExpression;
        this.withStatement = withStatement;
        this.position = position;
    }
    
    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#isUsingVariable(Integer id)
     */
    public int isUsingVariable(Integer id) throws ExpException {
        int result = 0;
        if (withStatement != null)
            result += withStatement.isUsingVariable(id);
        return result;
    }

    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#execute(com.mmatsubara.expresser.RuntimeData)
     */
    public StatementResult execute(RuntimeData runtime) throws ExpException {
        Object withObject = Expresser.evaluate(runtime, withExpression);
        
        if ((withObject instanceof IExpObject) == false)
            throw new ExpException(ScriptEngine.getResString("IllegalWithObject"), position);
        runtime.addWithObject((IExpObject)withObject);
        try {
            StatementResult receive = withStatement.execute(runtime);
            if (receive != null)
                return receive;
        } finally {
            runtime.removeWithObject();
        }
        return null;
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#registLocalVariable(java.util.Map)
     */
    public void registLocalVariable(Map varMap) {
        withStatement.registLocalVariable(varMap);
    }
}
