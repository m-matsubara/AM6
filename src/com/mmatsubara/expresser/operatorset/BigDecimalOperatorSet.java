package com.mmatsubara.expresser.operatorset;

import java.math.BigDecimal;
import java.math.MathContext;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.OperatorData;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._BigDecimal;
import com.mmatsubara.expresser.type._Boolean;
import com.mmatsubara.expresser.type._Double;
import com.mmatsubara.expresser.type._String;

/**
 * BigDecimalの演算子セットです。<br/>
 * <br/>
 * 作成日: 2011/05/30
 * 
 * @author m.matsubara
 *
 */
public class BigDecimalOperatorSet implements IOperatorSet {
    public static final BigDecimalOperatorSet operatorSet = new BigDecimalOperatorSet();
    private _Boolean trueValue;
    private _Boolean falseValue;
//	private BigDecimal fltEpsilon;
    
    public BigDecimalOperatorSet() {
        try {
            trueValue = new _Boolean(true);
            falseValue = new _Boolean(false);
            
//            fltEpsilon = new BigDecimal(5E-33);	//	MathContext.DECIMAL128 を基準（精度34桁）としている
        } catch (ExpException ee) {
            //  本来起こりえないはず
            throw new RuntimeException(Expresser.getResString("InternalError", "DoubleOperatorSet#DoubleOperatorSet()", "unknown error"));
        }
    }
    
    public static boolean equals(BigDecimal o1, BigDecimal o2) {
    	return o1.compareTo(o2) == 0;
/*
    	if (o1.compareTo(o2) >= 0) {
    		return (o1.subtract(o2).compareTo(operatorSet.fltEpsilon) < 0);    			
    	} else {
    		return (o2.subtract(o1).compareTo(operatorSet.fltEpsilon) < 0);    			
    	}
*/
    }
    
	public IExpObject operation(int operator, IExpObject operand1, IExpObject operand2, RuntimeData runtime) throws ExpException {
        switch (operator) {
        case OperatorData.OP_CMP_EQ_STRICT:
        	try {
        		if (operand1.getClass().equals(operand2.getClass()) == false)
        			return falseValue;
        		return operation(OperatorData.OP_CMP_EQ, operand1, operand2, runtime);
        	} catch (ClassCastException e) {
        		//  データ型が合わなかった
        		return falseValue;
        	}
        case OperatorData.OP_CMP_NE_STRICT:
        	try {
        		if (operand1.getClass().equals(operand2.getClass()) == false)
        			return falseValue;
        		return operation(OperatorData.OP_CMP_EQ, operand1, operand2, runtime);
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
	        if (operand1 != null) {
	        	double doubleValue = ((ExpNumberBase)operand1).doubleValue();
	        	if (Double.isNaN(doubleValue))
	        		return DoubleOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
	        	else if (Double.isInfinite(doubleValue))
	        		return DoubleOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
	        }
	        if (operand2 != null) {
	        	double doubleValue = ((ExpNumberBase)operand2).doubleValue();
	        	if (Double.isNaN(doubleValue))
	        		return DoubleOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
	        	else if (Double.isInfinite(doubleValue))
	        		return DoubleOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
	        }
	        
	        BigDecimal operand1value = null;
	        try {
	            operand1value = (operand1 != null) ? ((_BigDecimal)operand1).bigDecimalValue() : null;
	        } catch (ClassCastException cce) {
		        try {
		            operand1value = new BigDecimal(String.valueOf(((ExpNumberBase)operand1).doubleValue()));
		        } catch (Exception cce2) {
		            operand1value = BigDecimal.ZERO; 
		        }
	        }
	        BigDecimal operand2value = null;
	        try {
	            operand2value = (operand2 != null) ? ((_BigDecimal)operand2).bigDecimalValue() : null;
	        } catch (ClassCastException cce) {
		        try {
		            operand2value = new BigDecimal(String.valueOf(((ExpNumberBase)operand2).doubleValue()));
		        } catch (Exception cce2) {
		            operand2value = BigDecimal.ZERO; 
		        }
	        }
	        
	        switch (operator) {
	        //  比較演算子
	        case OperatorData.OP_CMP_EQ:
	            return (equals(operand1value, operand2value) != false) ? trueValue : falseValue; 
	        case OperatorData.OP_CMP_NE:
	            return (equals(operand1value, operand2value) == false) ? trueValue : falseValue; 
	        case OperatorData.OP_CMP_GE:
//	            return (operand1value.add(fltEpsilon).compareTo(operand2value) >= 0) ? trueValue : falseValue; 
	            return (operand1value.compareTo(operand2value) >= 0) ? trueValue : falseValue; 
	        case OperatorData.OP_CMP_GT:
//	            return (operand1value.subtract(fltEpsilon).compareTo(operand2value) > 0) ? trueValue : falseValue; 
	            return (operand1value.compareTo(operand2value) > 0) ? trueValue : falseValue; 
	        case OperatorData.OP_CMP_LE:
//	            return (operand1value.subtract(fltEpsilon).compareTo(operand2value) <= 0) ? trueValue : falseValue; 
	            return (operand1value.compareTo(operand2value) <= 0) ? trueValue : falseValue; 
	        case OperatorData.OP_CMP_LT:
//	            return (operand1value.add(fltEpsilon).compareTo(operand2value) < 0) ? trueValue : falseValue; 
	            return (operand1value.compareTo(operand2value) < 0) ? trueValue : falseValue; 
	        //  算術演算子
	        case OperatorData.OP_ARI_ADD:
	            return new _BigDecimal(operand1value.add(operand2value, MathContext.DECIMAL128)); 
	        case OperatorData.OP_ARI_SUB:
	            if (operand1 != null) {
	                return new _BigDecimal(operand1value.subtract(operand2value, MathContext.DECIMAL128)); 
	            } else {
	                return new _BigDecimal(BigDecimal.ZERO.subtract(operand2value, MathContext.DECIMAL128)); 
	            }
	        case OperatorData.OP_ARI_MUL:
	            return new _BigDecimal(operand1value.multiply(operand2value, MathContext.DECIMAL128)); 
	        case OperatorData.OP_ARI_DIV:
	            if (operand2value.compareTo(BigDecimal.ZERO) == 0) {
	                if (operand1value.compareTo(BigDecimal.ZERO) == 0)
	                    return new _Double(Double.NaN);
	                else if (operand1value.compareTo(BigDecimal.ZERO) > 0)
	                    return new _Double(Double.POSITIVE_INFINITY);
	                else
	                    return new _Double(Double.NEGATIVE_INFINITY);
	            }
	            return new _BigDecimal(operand1value.divide(operand2value, MathContext.DECIMAL128)); 
	        case OperatorData.OP_ARI_REM:
	            if (operand2value.compareTo(BigDecimal.ZERO) == 0)
	                return new _BigDecimal(new BigDecimal(Double.NaN));
	            return new _BigDecimal(operand1value.remainder(operand2value)); 
	        //  正符号
	        case OperatorData.OP_PLUS:
	            return operand2; 
	        //  負符号
	        case OperatorData.OP_MINUS:
	            return new _BigDecimal(BigDecimal.ZERO.subtract(operand2value)); 
	        //  ビット演算子
	        case OperatorData.OP_BIT_NOT:
	            return new _BigDecimal(~ operand2value.longValue()); 
	        case OperatorData.OP_BIT_AND:
	            return new _BigDecimal(operand1value.longValue() & operand2value.longValue()); 
	        case OperatorData.OP_BIT_XOR:
	            return new _BigDecimal(operand1value.longValue() ^ operand2value.longValue()); 
	        case OperatorData.OP_BIT_OR:
	            return new _BigDecimal(operand1value.longValue() | operand2value.longValue());
	        //  シフト演算子
	        case OperatorData.OP_SFT_LEFT:
	            return new _BigDecimal(operand1value.intValue() << operand2value.intValue()); 
	        case OperatorData.OP_SFT_RIGHT:
	            return new _BigDecimal(operand1value.intValue() >> operand2value.intValue()); 
	        case OperatorData.OP_SFT_RIGHT_UNSIGNED:
	            return new _BigDecimal(operand1value.intValue() >>> operand2value.intValue()); 
	        default:
	            return _DefaultOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
	        }
        }
	}

	public Class[] targetType() {
		// TODO 自動生成されたメソッド・スタブ
        Class[] result = { _BigDecimal.class, _Double.class };
        
        return result;
	}

}
