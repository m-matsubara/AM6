package com.mmatsubara.expresser.operatorset;

import com.mmatsubara.expresser.*;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Boolean;
import com.mmatsubara.expresser.type._Double;
import com.mmatsubara.expresser.type._String;

/**
 * 文字列型に対する演算子セットです。<br/>
 * <br/>
 * 作成日: 2004/12/31
 * 
 * @author m.matsubara
 */
public class StringOperatorSet implements IOperatorSet {
    public static final StringOperatorSet operatorSet = new StringOperatorSet(); 
    public Class[] targetType() {
        Class[] result = { _String.class, String.class };
        return result;
    }

    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.expresser.operatorset.IOperatorSet#operation(int, java.lang.Object, java.lang.Object, com.mmatsubara.expresser.RuntimeData)
     */
    public IExpObject operation(int operator, IExpObject operand1, IExpObject operand2, RuntimeData runtime)    throws ExpException {
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
        case OperatorData.OP_ARRAY_ACCESS:
            try {
                int index = ((ExpNumberBase)operand2).intValue();
            	return new _String(operand1.toString().substring(index, index + 1));
            } catch (Exception e) {
                return Expresser.UNDEFINED;
            }
        default:
	        if (operand1 == null || operand1 == Expresser.UNDEFINED)
	            operand1 = runtime.getTypeConverter().toString(runtime, operand1);
	        if (operand2 == null || operand2 == Expresser.UNDEFINED)
	            operand2 = runtime.getTypeConverter().toString(runtime, operand2);
	        
	        //  文字列の結合
	        if (operator == OperatorData.OP_ARI_ADD)
	            return new _String(operand1.toString() + operand2.toString());
	        if (operator == OperatorData.OP_ARI_SUB)
	            return new _Double(Double.parseDouble(operand1.toString()) - Double.parseDouble(operand2.toString()));
	        if (operator == OperatorData.OP_PLUS)
	            return new _Double(0 + Double.parseDouble(operand2.toString()));
	        if (operator == OperatorData.OP_MINUS)
	            return new _Double(0 - Double.parseDouble(operand2.toString()));
	        if (operand1 instanceof _String && operand2 instanceof _String) {
	            String operand1value = operand1.toString();
	            String operand2value = operand2.toString();
	            //  比較演算子関係
	            switch (operator) {
	            case OperatorData.OP_CMP_EQ:
	                return new _Boolean(operand1value.equals(operand2value));
	            case OperatorData.OP_CMP_NE:
	                return new _Boolean(!operand1value.equals(operand2value));
	            case OperatorData.OP_CMP_GE:
	                return new _Boolean(operand1value.compareTo(operand2value) >= 0);
	            case OperatorData.OP_CMP_GT:
	                return new _Boolean(operand1value.compareTo(operand2value) > 0);
	            case OperatorData.OP_CMP_LE:
	                return new _Boolean(operand1value.compareTo(operand2value) <= 0);
	            case OperatorData.OP_CMP_LT:
	                return new _Boolean(operand1value.compareTo(operand2value) < 0);
	            default:
	                return _DefaultOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
	            }
	        } else {
	            return _DefaultOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
	        }
        }
    }
}
