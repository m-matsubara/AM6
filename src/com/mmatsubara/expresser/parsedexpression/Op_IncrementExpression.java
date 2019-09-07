package com.mmatsubara.expresser.parsedexpression;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Byte;
import com.mmatsubara.expresser.type._Double;
import com.mmatsubara.expresser.type._Float;
import com.mmatsubara.expresser.type._Integer;
import com.mmatsubara.expresser.type._Long;
import com.mmatsubara.expresser.type._Short;

/**
 * 後置インクリメント演算子を表現します。<br/>
 * 後置インクリメント演算子はOperatorSetではなく専用のIParsedExpressionオブジェクトによって処理されます。<br/> 
 * <br/>
 * 作成日: 2005/07/29
 * 
 * @author m.matsubara
 */
public class Op_IncrementExpression implements IParsedExpression {
    private IParsedExpression variableExpression;
    private int opeIdx;
    private int incNumber;
    
    public Op_IncrementExpression(IParsedExpression variableExpression, int opeIdx, int incNumber) {
        this.variableExpression = variableExpression;
        this.opeIdx = opeIdx;
        this.incNumber = incNumber;
    }

    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#isUsingVariable(java.lang.Integer)
     */
    public int isUsingVariable(Integer id) throws ExpException {
        return variableExpression.isUsingVariable(id);
    }

    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#evaluate(com.mmatsubara.expresser.RuntimeData)
     */
    public IExpObject evaluate(RuntimeData runtime) throws ExpException {
        try {
            IVariable variable = variableExpression.evalRef(runtime);
            IExpObject result = variable.getValue();
            if (result instanceof _Double) {
                variable.setValue(new _Double(((_Double)result).doubleValue() + incNumber));
            } else if (result instanceof _Integer) {
                variable.setValue(new _Integer(((_Integer)result).intValue() + incNumber));
            } else if (result instanceof _Byte) {
                variable.setValue(new _Byte((byte)(((_Byte)result).byteValue() + incNumber)));
            } else if (result instanceof _Short) {
                variable.setValue(new _Short((short)(((_Short)result).shortValue() + incNumber)));
            } else if (result instanceof _Long) {
                variable.setValue(new _Long(((_Long)result).longValue() + incNumber));
            } else if (result instanceof _Float) {
                variable.setValue(new _Float(((_Float)result).floatValue() + incNumber));
            } else
                return new _Double(Double.NaN);    //  対応しない型
            return result;
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
