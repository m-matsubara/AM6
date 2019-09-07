package com.mmatsubara.expresser.parsedexpression;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.IExpObject;

/**
 * 代入式を表現します。<br/>
 * 代入演算子はOperatorSetではなく専用のIParsedExpressionオブジェクトによって処理されます。<br/> 
 * <br/>
 * 作成日: 2005/07/29
 * 
 * @author m.matsubara
 */
public class Op_SubstituteExpression implements IParsedExpression {
    private IParsedExpression variableExpression;
    private IParsedExpression valueExpression;
    private int opeIdx;
    
    public Op_SubstituteExpression(IParsedExpression variableExpression, IParsedExpression valueExpression, int opeIdx) {
        this.variableExpression = variableExpression;
        this.valueExpression = valueExpression;
        this.opeIdx = opeIdx;
    }
    
    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#isUsingVariable(java.lang.Integer)
     */
    public int isUsingVariable(Integer id) throws ExpException {
        try {
            int count = variableExpression.isUsingVariable(id);
            count += valueExpression.isUsingVariable(id);
            return count;
        } catch (Exception e) {
            throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), opeIdx);
        }
    }

    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#evaluate(com.mmatsubara.expresser.RuntimeData)
     */
    public IExpObject evaluate(RuntimeData runtime) throws ExpException {
        try {
            IExpObject value = this.valueExpression.evaluate(runtime);
            if (value instanceof IVariable)
                value = ((IVariable)value).getValue();
            IVariable variable = variableExpression.evalRef(runtime);

            variable.setValue(value);
            return value;
        } catch (ExpException ee) {
            if (ee.getPosition() < 0)
                ee.setPosition(opeIdx);
            throw ee;
        }
    }

    /* (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#evalRef(com.mmatsubara.expresser.RuntimeData)
     */
    public IVariable evalRef(RuntimeData runtime) throws ExpException {
        throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), -1);
    }
    
    /* (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#evalTargetObject(com.mmatsubara.expresser.RuntimeData)
     */
    public IExpObject evalTargetObject(RuntimeData runtime) throws ExpException {
        return Expresser.UNDEFINED;
    }
    
    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#isConst()
     */
    public boolean isConst() {
        return false;
    }
}
