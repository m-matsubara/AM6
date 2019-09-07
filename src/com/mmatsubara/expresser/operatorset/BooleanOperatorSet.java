package com.mmatsubara.expresser.operatorset;

import com.mmatsubara.expresser.*;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.*;

/**
 * 論理値型に対する演算子セットです。<br/>
 * <br/>
 * 作成日: 2004/12/26
 * 
 * @author m.matsubara
 *
 */
public class BooleanOperatorSet implements IOperatorSet {
    public static final BooleanOperatorSet operatorSet = new BooleanOperatorSet();
    public Class[] targetType() {
        Class[] result = { _Boolean.class };
        
        return result;
    }

    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.expresser.operatorset.IOperatorSet#operation(int, java.lang.Object, java.lang.Object, com.mmatsubara.expresser.RuntimeData)
     */
    public IExpObject operation(int operator, IExpObject operand1, IExpObject operand2, RuntimeData runtime) throws ExpException {
        switch (operator) {
        case OperatorData.OP_CMP_EQ_STRICT:
        	try {
        		return new _Boolean(operand1.equals(operand2));
        	} catch (ClassCastException e) {
        		//  データ型が合わなかった
        		return new _Boolean(false);
        	}
        case OperatorData.OP_CMP_NE_STRICT:
        	try {
        		return new _Boolean(! operand1.equals(operand2));
        	} catch (ClassCastException e) {
        		//  データ型が合わなかった
        		return new _Boolean(false);
        	}
        default:	
	    	if (!(operand2 instanceof _Boolean)) {
                operand2 = runtime.getTypeConverter().toPrimitive(runtime, operand2);
	            if (operand2 instanceof _String) {
	                return StringOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
                } else if (operand2 instanceof ExpNumberBase) {
	                operand1 = runtime.getTypeConverter().toNumber(runtime, operand1);
	                return DoubleOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
	            } else {
                    return _DefaultOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
                }
	        }
	        
	        switch (operator) {
	        case OperatorData.OP_CMP_EQ:
	            return new _Boolean(((_Boolean)operand1).booleanValue() == ((_Boolean)operand2).booleanValue()); 
	        case OperatorData.OP_CMP_NE:
	            return new _Boolean(((_Boolean)operand1).booleanValue() != ((_Boolean)operand2).booleanValue()); 
	        case OperatorData.OP_LOG_NOT:
	            return new _Boolean(! ((_Boolean)operand2).booleanValue());
	        default:
	            return _DefaultOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
	        }
        }
    }
}
