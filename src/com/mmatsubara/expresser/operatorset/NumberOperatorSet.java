package com.mmatsubara.expresser.operatorset;

import com.mmatsubara.expresser.*;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.*;

/**
 * 数字型の演算子セットです。<br/>
 * int, long, double については専用の演算子セットが提供されるのでそちらを使うようにしてください。<br/>
 * <br/>
 * 作成日: 2005/05/04
 * 
 * @author m.matsubara
 *
 */
public class NumberOperatorSet implements IOperatorSet {
    public static final NumberOperatorSet operatorSet = new NumberOperatorSet();
    
    private static final int TYPE_BYTE = 0;
    private static final int TYPE_SHORT = 1;
    private static final int TYPE_INT = 2;
    private static final int TYPE_LONG = 3;
    private static final int TYPE_FLOAT = 4;
    private static final int TYPE_DOUBLE = 5;

    public Class[] targetType() {
        Class[] result = { ExpNumberBase.class };
        
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
	        if (!(operand1 instanceof ExpNumberBase))
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
	        double result = 0;
	        
	        //  戻り型の判定
	        int resultType = TYPE_DOUBLE;
	        if (operand1 != Expresser.UNDEFINED) {
	            int type1 = resultType(operand1);
	            int type2 = resultType(operand2);
	            if (type1 < type2)
	                resultType = type2;
	            else
	                resultType = type1;
	        } else {
	            resultType = resultType(operand2);
	        }
	
	        //  いったんdouble型に変換して計算
	        double operand1value = (operand1 != null) ? ((ExpNumberBase)operand1).doubleValue() : Double.NaN;    //  オペランド１はNOT_RESULTのことがある
	        double operand2value = ((ExpNumberBase)operand2).doubleValue();
	        
	        //  演算
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
	            result = operand1value + operand2value;
	            break;
	        case OperatorData.OP_ARI_SUB:
	            if (operand1 != null) {
	                result = operand1value - operand2value; 
	            } else {
	                result = 0 - operand2value; 
	            }
	            break;
	        case OperatorData.OP_ARI_MUL:
	            result = operand1value * operand2value;
	            break;
	        case OperatorData.OP_ARI_DIV:
	            if (operand2value == 0) {
	                if (operand1value == 0)
	                    return new _Double(Double.NaN);
	                else if (operand1value > 0)
	                    return new _Double(Double.POSITIVE_INFINITY);
	                else
	                    return new _Double(Double.NEGATIVE_INFINITY);
	            }
	            result = operand1value / operand2value;
	            break;
	        case OperatorData.OP_ARI_REM:
	            if (operand2value == 0)
	                return new _Double(Double.NaN);
	            result = operand1value % operand2value;
	            break;
	        //  正符号
	        case OperatorData.OP_PLUS:
	            return operand2;
	        //  負符号
	        case OperatorData.OP_MINUS:
	            result = 0 - operand2value;
	            break;
	        //  ビット演算子
	        case OperatorData.OP_BIT_NOT:
	            result = ~ (long)operand2value;
	            break;
	        case OperatorData.OP_BIT_AND:
	            result = (long)operand1value & (long)operand2value;
	            break;
	        case OperatorData.OP_BIT_XOR:
	            result = (long)operand1value ^ (long)operand2value;
	            break;
	        case OperatorData.OP_BIT_OR:
	            result = (long)operand1value | (long)operand2value;
	            break;
	        //  シフト演算子
	        case OperatorData.OP_SFT_LEFT:
	            result = (long)operand1value << (long)operand2value;
	            break;
	        case OperatorData.OP_SFT_RIGHT:
	            result = (long)operand1value >> (long)operand2value;
	            break;
	        case OperatorData.OP_SFT_RIGHT_UNSIGNED:
	            result = (long)operand1value >>> (long)operand2value;
	            break;
	        default:
	            return _DefaultOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
	        }
	
	        //  戻り値の型を判別してリターンする
	        switch (resultType) {
	        case TYPE_BYTE:
	            return new _Byte((byte)result);
	        case TYPE_SHORT:
	            return new _Short((short)result);
	        case TYPE_INT:
	            return new _Integer((int)result);
	        case TYPE_LONG:
	            return new _Long((long)result);
	        case TYPE_FLOAT:
	            return new _Float((float)result);
	        case TYPE_DOUBLE:
	            return new _Double((double)result);
	        default:
	            return new _Double((double)result);
	        }
        }
    }
    
    
    /**
     * オペランドの型を判別します。
     * @param operand 判定する型
     * @return 定数 TYPE_BYTE ～ TYPE_DOUBLE
     */
    private int resultType(Object operand) {
        if (operand instanceof _Byte)
            return TYPE_BYTE;
        else if (operand instanceof _Short)
            return TYPE_SHORT;
        else if (operand instanceof _Integer)
            return TYPE_INT;
        else if (operand instanceof _Long)
            return TYPE_LONG;
        else if (operand instanceof _Float)
            return TYPE_FLOAT;
        else {
            return TYPE_DOUBLE;
        }
    }

}
