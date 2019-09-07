package com.mmatsubara.expresser.operatorset;

import java.util.List;

import com.mmatsubara.expresser.*;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.*;


/**
 * デフォルトの演算子セットです。<br/>
 * 各々の型に特化していない演算を行います。<br/>
 * または、ExpObjectなど各オブジェクトの基底オブジェクトの演算を行います。
 * <br/>
 * 作成日: 2005/02/06
 * 
 * @author m.matsubara
 */
public class _DefaultOperatorSet implements IOperatorSet {
    public static final _DefaultOperatorSet operatorSet = new _DefaultOperatorSet();  
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.operatorset.IOperatorSet#targetType()
     */
    public Class[] targetType() {
        Class[] result = { Object.class };
        return result;
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.operatorset.IOperatorSet#operation(int, java.lang.Object, java.lang.Object, com.mmatsubara.expresser.RuntimeData)
     */
    public IExpObject operation(int operator, IExpObject operand1, IExpObject operand2, RuntimeData runtime) throws ExpException {

    	switch (operator) {
/*        
        case OperatorData.OP_MEMBER_ACCESS:
            if ((operand1 instanceof IVariable) && ((operand1 instanceof IExpObject) == false)) {
                operand1 = ((IVariable)operand1).getValue();
            }
            if (operand1 == null)
                throw new ExpException(Expresser.getResString("NullObject"), -1);
            if (operand1 instanceof IExpObject) {
                return new ExpObjectMember((IExpObject)operand1, (Integer)operand2);
            } else {
                return new JavaObjectMember(operand1, StringToId.toString((Integer)operand2));
            }
*/            
        case OperatorData.OP_CMP_EQ:
            return new _Boolean(equal(operand1, operand2, runtime));
            
        case OperatorData.OP_CMP_NE:
            return new _Boolean(! equal(operand1, operand2, runtime));
        
        case OperatorData.OP_CMP_EQ_STRICT:
            if (operand1 == null)
                return new _Boolean(operand2 == null);
            if (operand2 == null)
                return new _Boolean(false);    //  operand1はnullではないので false
            if (operand1.getClass() != operand2.getClass())
                return new _Boolean(false);
            return new _Boolean(operand1.equals(operand2));
            
        case OperatorData.OP_CMP_NE_STRICT:
            if (operand1 == null)
                return new _Boolean(operand2 != null);
            if (operand2 == null)
                return new _Boolean(true);    //  operand1はnullではないので true
            if (operand1.getClass() != operand2.getClass())
                return new _Boolean(false);
            return new _Boolean(operand1.equals(operand2) == false);

        case OperatorData.OP_SEQUENSE:
            return operand2;
            
        case OperatorData.OP_VOID:
            return Expresser.UNDEFINED;

        case OperatorData.OP_TYPEOF:
            //  TODO この実装だと "typeof new Boolean(false)" が "object" を返さない
            //  TODO そもそも ExpBoolean, ExpNumber, ExpString って単純にプリミティブ型を継承するだけでよいのかな？
            if (operand2 == Expresser.UNDEFINED)
                return new _String("undefined");
            else if (operand2 == null)
                return new _String("object");
            else if (operand2 instanceof _Boolean)
                return new _String("boolean");
            else if (operand2 instanceof ExpNumberBase)
                return new _String("number");
            else if (operand2 instanceof _String)
                return new _String("string");
            else if (operand2 instanceof IFunction)
                return new _String("function");
            else if (operand2 instanceof IExpObject)
                return new _String("object");
            else
                return new _String("[host object]");

        case OperatorData.OP_DELETE_MEMBER:
            if (operand2 instanceof ExpObjectMember)
                return ((ExpObjectMember)operand2).deleteMember();
            break;

        case OperatorData.OP_INSTANCEOF:
            if (operand1 instanceof IExpObject && operand2 != null) {
                IExpObject expObject = (IExpObject)operand1;
                List list = expObject.getConstructorList();
                int max = list.size();
                for (int idx = 0; idx < max; idx++) {
                    System.out.println(list.get(idx));
                    if (operand2 == list.get(idx))
                        return new _Boolean(true);
                }
                return new _Boolean(false);
            }
            break;
        case OperatorData.OP_ARI_ADD:
        	if (Expresser.UNDEFINED.equals(operand1))
        		return new _Double(Expresser.NaN);
        	if (Expresser.UNDEFINED.equals(operand2))
        		return new _Double(Expresser.NaN);
            operand1 = runtime.getTypeConverter().toPrimitive(runtime, operand1);
            operand2 = runtime.getTypeConverter().toPrimitive(runtime, operand2);
            if (operand1 instanceof _Boolean)
                operand1 = runtime.getTypeConverter().toNumber(runtime, operand1);
            if (operand2 instanceof _Boolean)
                operand2 = runtime.getTypeConverter().toNumber(runtime, operand2);
            if (operand1 instanceof ExpNumberBase && operand2 instanceof ExpNumberBase)
                return DoubleOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
            else {
                operand1 = runtime.getTypeConverter().toString(runtime, operand1);
                operand2 = runtime.getTypeConverter().toString(runtime, operand2);
                return StringOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
            }
        case OperatorData.OP_ARI_SUB:
        case OperatorData.OP_ARI_MUL:
        case OperatorData.OP_ARI_DIV:
        case OperatorData.OP_ARI_REM:
        case OperatorData.OP_BIT_NOT:
        case OperatorData.OP_BIT_AND:
        case OperatorData.OP_BIT_XOR:
        case OperatorData.OP_BIT_OR:
        case OperatorData.OP_SFT_LEFT:
        case OperatorData.OP_SFT_RIGHT:
        case OperatorData.OP_SFT_RIGHT_UNSIGNED:
        	if (Expresser.UNDEFINED.equals(operand1))
        		return new _Double(Expresser.NaN);
        	if (Expresser.UNDEFINED.equals(operand2))
        		return new _Double(Expresser.NaN);
            operand1 = runtime.getTypeConverter().toNumber(runtime, operand1);
            operand2 = runtime.getTypeConverter().toNumber(runtime, operand2);
            return DoubleOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
        case OperatorData.OP_LOG_NOT:
            {
                ITypeConverter typeConverter = runtime.getTypeConverter();
                boolean bool = typeConverter.toBoolean(runtime, operand2).booleanValue();
                return new _Boolean(!bool);
            }
            
        }
        return Expresser.UNSUPPORTED_OPERATOR;
    }
    
    
    /**
     * データ型の比較を行う
     * @param operand1 オペランド１
     * @param operand2 オペランド２
     * @param runtime ランタイムデータ
     * @return 等しいとき true
     * @throws ExpException
     */
    public boolean equal(IExpObject operand1, IExpObject operand2, RuntimeData runtime) throws ExpException {
        ITypeConverter converter = runtime.getTypeConverter();
        if (operand1 == null)
            return operand2 == null;
        if (operand1 instanceof ExpNumberBase) {
            if (operand2 instanceof ExpNumberBase) {
                return operand2.equals(operand2);
            } else if (operand2 instanceof _String) {
                return ((ExpNumberBase)operand1).doubleValue() == converter.toNumber(runtime, operand2).doubleValue(); 
            } else if (operand2 instanceof _Boolean) {
                return ((ExpNumberBase)operand1).doubleValue() == converter.toNumber(runtime, operand2).doubleValue(); 
            } else if (operand2 instanceof ExpDate) {
                return ((ExpNumberBase)operand1).doubleValue() == converter.toNumber(runtime, operand2).doubleValue(); 
            } else {
                return operand2.equals(operand2);
            }
        } else if (operand1 instanceof _String) {
            if (operand2 instanceof ExpNumberBase) {
                return converter.toNumber(runtime, operand1).doubleValue() == ((ExpNumberBase)operand2).doubleValue(); 
            } else if (operand2 instanceof _String) {
                return operand2.equals(operand2);
            } else if (operand2 instanceof _Boolean) {
                return (operand1.equals(converter.toString(runtime, operand2))); 
            } else if (operand2 instanceof ExpDate) {
                return (operand1.equals(converter.toString(runtime, operand2))); 
            } else {
                return operand2.equals(operand2);
            }
        } else if (operand1 instanceof _Boolean) {
            if (operand2 instanceof ExpNumberBase) {
                return converter.toNumber(runtime, operand1).doubleValue() == ((ExpNumberBase)operand2).doubleValue(); 
            } else if (operand2 instanceof _String) {
                return converter.toString(runtime, operand1).equals(operand2); 
            } else if (operand2 instanceof _Boolean) {
                return operand2.equals(operand2);
            } else if (operand2 instanceof ExpDate) {
                return converter.toString(runtime, operand1).equals(converter.toString(runtime, operand2)); 
            } else {
                return operand2.equals(operand2);
            }
        } else if (operand1 instanceof ExpDate) {
            if (operand2 instanceof ExpNumberBase) {
                return converter.toNumber(runtime, operand1).doubleValue() == ((ExpNumberBase)operand2).doubleValue(); 
            } else if (operand2 instanceof _String) {
                return converter.toString(runtime, operand1).equals(operand2); 
            } else if (operand2 instanceof _Boolean) {
                return converter.toString(runtime, operand1).equals(converter.toString(runtime, operand2)); 
            } else if (operand2 instanceof ExpDate) {
                return operand2.equals(operand2);
            } else {
                return operand2.equals(operand2);
            }
        } else {
            return operand1.equals(operand2);
        }
    }

}
