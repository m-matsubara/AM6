package com.mmatsubara.expresser.operatorset;

import com.mmatsubara.expresser.*;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.*;

/**
 * 倍精度実数の演算子セットです。<br/>
 * <br/>
 * 作成日: 2004/12/26
 * 
 * @author m.matsubara
 *
 */
public class DoubleOperatorSet implements IOperatorSet {
    public static final DoubleOperatorSet operatorSet = new DoubleOperatorSet();
    private _Boolean trueValue;
    private _Boolean falseValue;
    
    public DoubleOperatorSet() {
        try {
            trueValue = new _Boolean(true);
            falseValue = new _Boolean(false);
        } catch (ExpException ee) {
            //  本来起こりえないはず
            throw new RuntimeException(Expresser.getResString("InternalError", "DoubleOperatorSet#DoubleOperatorSet()", "unknown error"));
        }
    }
    
    

    public Class[] targetType() {
        Class[] result = { _Double.class };
        
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
        		return (operand1.equals(operand2)) ? trueValue : falseValue;
        	} catch (ClassCastException e) {
        		//  データ型が合わなかった
        		return falseValue;
        	}
        case OperatorData.OP_CMP_NE_STRICT:
        	try {
        		return (! operand1.equals(operand2)) ? trueValue : falseValue;
        	} catch (ClassCastException e) {
        		//  データ型が合わなかった
        		return falseValue;
        	}
        default:
	        if (operand1 != null && !(operand1 instanceof ExpNumberBase))
	            operand1 = runtime.getTypeConverter().toNumber(runtime, operand1);
	        if (!(operand2 instanceof ExpNumberBase)) {
                operand2 = runtime.getTypeConverter().toPrimitive(runtime, operand2);
                if (operand2 instanceof ExpNumberBase)
                    ;
                else if (operand2 instanceof _String)
                    return StringOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
                else if (operand2 instanceof _Boolean)
                    operand2 = runtime.getTypeConverter().toNumber(runtime, operand2);
                else
                    return _DefaultOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
	        }
	        double operand1value;
	        try {
	            operand1value = (operand1 != null) ? ((ExpNumberBase)operand1).doubleValue() : Double.NaN;
	        } catch (ClassCastException cce) {
	            operand1value = runtime.getTypeConverter().toNumber(runtime, operand1).doubleValue(); 
	        }
	        double operand2value;
	        try {
	            operand2value = (operand2 != null) ? ((ExpNumberBase)operand2).doubleValue() : Double.NaN;
	        } catch (ClassCastException cce) {
	            operand2value = runtime.getTypeConverter().toNumber(runtime, operand2).doubleValue(); 
	        }
	        
	        switch (operator) {
	        //  比較演算子
	        case OperatorData.OP_CMP_EQ:
	            return (operand1value == operand2value) ? trueValue : falseValue; 
	        case OperatorData.OP_CMP_NE:
	            return (operand1value != operand2value) ? trueValue : falseValue; 
	        case OperatorData.OP_CMP_GE:
	            return (operand1value >= operand2value) ? trueValue : falseValue; 
	        case OperatorData.OP_CMP_GT:
	            return (operand1value > operand2value) ? trueValue : falseValue; 
	        case OperatorData.OP_CMP_LE:
	            return (operand1value <= operand2value) ? trueValue : falseValue; 
	        case OperatorData.OP_CMP_LT:
	            return (operand1value < operand2value) ? trueValue : falseValue; 
	        //  算術演算子
	        case OperatorData.OP_ARI_ADD:
	            return new _Double(operand1value + operand2value); 
	        case OperatorData.OP_ARI_SUB:
	            if (operand1 != null) {
	                return new _Double(operand1value - operand2value); 
	            } else {
	                return new _Double(0 - operand2value); 
	            }
	        case OperatorData.OP_ARI_MUL:
	            return new _Double(operand1value * operand2value); 
	        case OperatorData.OP_ARI_DIV:
	            if (operand2value == 0) {
	                if (Double.isNaN((operand1value)))
	                    return new _Double(Double.NaN);
	                else if (operand1value == 0)
	                    return new _Double(Double.NaN);
	                else if (operand1value > 0)
	                    return new _Double(Double.POSITIVE_INFINITY);
	                else
	                    return new _Double(Double.NEGATIVE_INFINITY);
	            }
	            return new _Double(operand1value / operand2value); 
	        case OperatorData.OP_ARI_REM:
	            if (operand2value == 0)
	                return new _Double(Double.NaN);
	            return new _Double(operand1value % operand2value); 
	        //  正符号
	        case OperatorData.OP_PLUS:
	            return operand2; 
	        //  負符号
	        case OperatorData.OP_MINUS:
	            return new _Double(0 - operand2value); 
	        //  ビット演算子
	        case OperatorData.OP_BIT_NOT:
	            return new _Double(~ (long)operand2value); 
	        case OperatorData.OP_BIT_AND:
	            return new _Double((long)operand1value & (long)operand2value); 
	        case OperatorData.OP_BIT_XOR:
	            return new _Double((long)operand1value ^ (long)operand2value); 
	        case OperatorData.OP_BIT_OR:
	            return new _Double((long)operand1value | (long)operand2value);
	        //  シフト演算子
	        case OperatorData.OP_SFT_LEFT:
	            return new _Double((int)operand1value << (int)operand2value); 
	        case OperatorData.OP_SFT_RIGHT:
	            return new _Double((int)operand1value >> (int)operand2value); 
	        case OperatorData.OP_SFT_RIGHT_UNSIGNED:
	            return new _Double((int)operand1value >>> (int)operand2value); 
	        default:
	            return _DefaultOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
	        }
        }
    }
}
