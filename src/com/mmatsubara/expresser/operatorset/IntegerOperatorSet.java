package com.mmatsubara.expresser.operatorset;

import com.mmatsubara.expresser.*;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.*;

/**
 * 整数型に対応する演算子セットです。<br/>
 * <br/>
 * 作成日: 2004/12/26
 * 
 * @author m.matsubara
 */
public class IntegerOperatorSet implements IOperatorSet {
    public static final IntegerOperatorSet operatorSet = new IntegerOperatorSet();

    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.expresser.operatorset.IOperatorSet#targetType()
     */
    public Class[] targetType() {
        Class[] result = { _Integer.class };
        
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
	        if (!(operand2 instanceof _Integer)) {
                operand2 = runtime.getTypeConverter().toPrimitive(runtime, operand2);
                if (operand2 instanceof ExpNumberBase)
                    return DoubleOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
                else if (operand2 instanceof _String)
                    return StringOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
                else if (operand2 instanceof _Boolean)
                    operand2 = runtime.getTypeConverter().toNumber(runtime, operand2);
                else
                    return _DefaultOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
	        }
	
	        int operand1value = (operand1 != null) ? ((ExpNumberBase)operand1).intValue() : 0;    //  オペランド１はnullのことがある
	        int operand2value = ((ExpNumberBase)operand2).intValue();
	        switch (operator) {
	        //  比較演算子
	        case OperatorData.OP_CMP_EQ:
	            return new _Boolean(operand1value == operand2value); 
	        case OperatorData.OP_CMP_NE:
	            return new _Boolean(operand1value != operand2value); 
	        case OperatorData.OP_CMP_GE:
	            return new _Boolean(operand1value >= operand2value); 
	        case OperatorData.OP_CMP_GT:
	            return new _Boolean(operand1value > operand2value);
	        case OperatorData.OP_CMP_LE:
	            return new _Boolean(operand1value <= operand2value);
	        case OperatorData.OP_CMP_LT:
	            return new _Boolean(operand1value < operand2value);
	        //  算術演算子
	        case OperatorData.OP_ARI_ADD:
	            return new _Integer(operand1value + operand2value);
	        case OperatorData.OP_ARI_SUB:
	            return new _Integer(operand1value - operand2value);
	        case OperatorData.OP_ARI_MUL:
	            return new _Integer(operand1value * operand2value);
	        case OperatorData.OP_ARI_DIV:
	            if (operand2value == 0) {
	                if (operand1value == 0)
	                    return new _Double(Double.NaN);
	                else if (operand1value > 0)
	                    return new _Double(Double.POSITIVE_INFINITY);
	                else
	                    return new _Double(Double.NEGATIVE_INFINITY);
	            }
	            return new _Integer(operand1value / operand2value);
	        case OperatorData.OP_ARI_REM:
	            if (operand2value == 0)
	                return new _Double(Double.NaN);
	            return new _Integer(operand1value % operand2value);
	        //  正符号
	        case OperatorData.OP_PLUS:
	            return operand2; 
	        //  負符号
	        case OperatorData.OP_MINUS:
	            return new _Integer(0 - operand2value);
	        //  ビット演算子
	        case OperatorData.OP_BIT_NOT:
	            return new _Integer(~ operand2value); 
	        case OperatorData.OP_BIT_AND:
	            return new _Integer(operand1value & operand2value); 
	        case OperatorData.OP_BIT_XOR:
	            return new _Integer(operand1value ^ operand2value); 
	        case OperatorData.OP_BIT_OR:
	            return new _Integer(operand1value | operand2value);
	        //  シフト演算子
	        case OperatorData.OP_SFT_LEFT:
	            return new _Integer(operand1value << operand2value); 
	        case OperatorData.OP_SFT_RIGHT:
	            return new _Integer(operand1value >> operand2value); 
	        case OperatorData.OP_SFT_RIGHT_UNSIGNED:
	            return new _Integer(operand1value >>> operand2value); 
	        
	        default:
	            return _DefaultOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
	        }
        }
    }

}
