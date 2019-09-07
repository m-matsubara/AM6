package com.mmatsubara.expresser.operatorset;

import com.mmatsubara.expresser.*;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpDate;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Boolean;
import com.mmatsubara.expresser.type._Double;


/**
 * 日付型に対する演算子セットです。<br/>
 * <br/>
 * 作成日: 2004/12/31
 * 
 * @author m.matsubara
 */
public class DateOperatorSet implements IOperatorSet {
    public static final DateOperatorSet operatorSet = new DateOperatorSet();
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.operatorset.IOperatorSet#targetType()
     */
    public Class[] targetType() {
        Class[] result = { ExpDate.class };
        
        return result;
    }

    /* (非 Javadoc)
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
	        switch (operator) {
	        case OperatorData.OP_ARI_ADD:
	            if (operand1 instanceof ExpDate && operand2 instanceof ExpNumberBase) {
	                java.util.Date dateOperand = ((ExpDate)operand1).getDate();
	                double operand2value = ((ExpNumberBase)operand2).doubleValue();
	                
	                return new ExpDate((long)(dateOperand.getTime() + operand2value)); 
	            }
	            else if (operand1 instanceof ExpNumberBase && operand2 instanceof ExpDate) {
	                java.util.Date dateOperand = ((ExpDate)operand2).getDate();
	                double operand2value = ((ExpNumberBase)operand1).doubleValue();
	                
	                return new ExpDate((long)(dateOperand.getTime() + operand2value)); 
	            }
	            break;
	        case OperatorData.OP_ARI_SUB:
	            if (operand1 instanceof ExpDate && operand2 instanceof ExpNumberBase) {
	                java.util.Date dateOperand = ((ExpDate)operand1).getDate();
	                double operand2value = ((ExpNumberBase)operand2).doubleValue();
	                
	                return new ExpDate((long)(dateOperand.getTime() + operand2value)); 
	            }
	            else if (operand1 instanceof ExpDate && operand2 instanceof ExpDate) {
	                java.util.Date dateOperand1 = ((ExpDate)operand1).getDate();
	                java.util.Date dateOperand2 = ((ExpDate)operand2).getDate();
	                
	                return new _Double(dateOperand1.getTime() - dateOperand2.getTime()); 
	            }
	            break;
	        default:
	            if (operand1 instanceof java.util.Date && operand2 instanceof java.util.Date) {
	                //  左右オペランドが java.util.Date の場合
	                java.util.Date dateOperand1 = ((ExpDate)operand1).getDate();
	                java.util.Date dateOperand2 = ((ExpDate)operand2).getDate();
	                //  比較演算子系
	                switch (operator) {
	                case OperatorData.OP_CMP_EQ:
	                    return new _Boolean(dateOperand1.equals(dateOperand2)); 
	                case OperatorData.OP_CMP_NE:
	                    return new _Boolean(!dateOperand1.equals(dateOperand2)); 
	                case OperatorData.OP_CMP_GE:
	                    return new _Boolean(dateOperand1.compareTo(dateOperand2) >= 0); 
	                case OperatorData.OP_CMP_GT:
	                    return new _Boolean(dateOperand1.compareTo(dateOperand2) > 0); 
	                case OperatorData.OP_CMP_LE:
	                    return new _Boolean(dateOperand1.compareTo(dateOperand2) <= 0); 
	                case OperatorData.OP_CMP_LT:
	                    return new _Boolean(dateOperand1.compareTo(dateOperand2) < 0);
	                default:
	                    return _DefaultOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
	                }
	            } else {
	                return _DefaultOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
	            }
	        }
        }
        return Expresser.UNSUPPORTED_OPERATOR;
    }

}
