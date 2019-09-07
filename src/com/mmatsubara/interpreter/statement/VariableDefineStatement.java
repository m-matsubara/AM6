package com.mmatsubara.interpreter.statement;

import java.util.Map;

import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.parsedexpression.IParsedExpression;
import com.mmatsubara.expresser.type.IExpObject;


/**
 * 変数定義ステートメントです。<br/>
 * 
 * 作成日: 2005/03/06
 * 
 * @author m.matsubara
 */
public class VariableDefineStatement implements IStatement {
    private Integer id;
    private int variableOffset = 0;
    private IParsedExpression initialExpression;
    
    public VariableDefineStatement(Integer id, IParsedExpression initialExpression) {
        this.id = id;
        this.initialExpression = initialExpression;
    }
    

    /* (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#isUsingVariable(java.lang.Integer)
     */
    public int isUsingVariable(Integer id) throws ExpException {
        if (initialExpression != null)
            return initialExpression.isUsingVariable(id);
        else
            return 0;
    }

    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#execute(com.mmatsubara.expresser.RuntimeData)
     */
    public StatementResult execute(RuntimeData runtime) throws ExpException {
        if (initialExpression != null) {
            if (variableOffset == 0)
                variableOffset = runtime.getVariableOffset(id);
            
            IExpObject value = initialExpression.evaluate(runtime);
            if (value instanceof IVariable)
                value = ((IVariable)value).getValue();
            if (variableOffset != 0)
                runtime.setValue(id, variableOffset, value);
            else
                runtime.setValue(id, value);
        }
        return null;
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#registLocalVariable(java.util.Map)
     */
    public void registLocalVariable(Map varMap) {
        varMap.put(id, this);
    }
}
