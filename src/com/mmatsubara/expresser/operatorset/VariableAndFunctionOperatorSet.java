package com.mmatsubara.expresser.operatorset;

import com.mmatsubara.expresser.*;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.*;

/**
 * 代入関連の演算子セットです。<br/>
 * <br/>
 * 作成日: 2005/01/10
 * @author m.matsubara
 */
public class VariableAndFunctionOperatorSet implements IOperatorSet {
    public static final VariableAndFunctionOperatorSet operatorSet = new VariableAndFunctionOperatorSet();
    
    public Class[] targetType() {
        Class[] result = { IVariable.class, IFunction.class };
        
        return result;
    }

    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.expresser.operatorset.IOperatorSet#operation(int, java.lang.Object, java.lang.Object, com.mmatsubara.expresser.RuntimeData)
     */
    public IExpObject operation(int operator, IExpObject operand1, IExpObject operand2, RuntimeData runtime) throws ExpException {
        IVariable variable;
        IExpObject result;
        switch (operator) {
/*
        case OperatorData.OP_SUBSTITUTE:
            try {
            	variable = (IVariable)operand1;
            } catch (ClassCastException cce) {
                throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), -1);
            }
            variable.setValue(operand2);
            return operand2;
*/
        case OperatorData.OP_ARI_INC_F:
            try {
                variable = (IVariable)operand2;
            } catch (ClassCastException cce) {
                throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), -1);
            }
            result = variable.getValue();
            if (result instanceof _Double) {
                variable.setValue(new _Double(((_Double)result).doubleValue() + 1));
            } else if (result instanceof _Integer) {
                variable.setValue(new _Integer(((_Integer)result).intValue() + 1));
            } else if (result instanceof _Byte) {
                variable.setValue(new _Byte((byte)(((_Byte)result).byteValue() + 1)));
            } else if (result instanceof _Short) {
                variable.setValue(new _Short((short)(((_Short)result).shortValue() + 1)));
            } else if (result instanceof _Long) {
                variable.setValue(new _Long(((_Long)result).longValue() + 1));
            } else if (result instanceof _Float) {
                variable.setValue(new _Float(((_Float)result).floatValue() + 1));
            } else
                return new _Double(Double.NaN);    //  対応しない型
            result = variable.getValue();
            return result;
        case OperatorData.OP_ARI_INC_B:
            try {
                variable = (IVariable)operand1;
            } catch (ClassCastException cce) {
                throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), -1);
            }
            result = variable.getValue();
            if (result instanceof _Double) {
                variable.setValue(new _Double(((_Double)result).doubleValue() + 1));
            } else if (result instanceof _Integer) {
                variable.setValue(new _Integer(((_Integer)result).intValue() + 1));
            } else if (result instanceof _Byte) {
                variable.setValue(new _Byte((byte)(((_Byte)result).byteValue() + 1)));
            } else if (result instanceof _Short) {
                variable.setValue(new _Short((short)(((_Short)result).shortValue() + 1)));
            } else if (result instanceof _Long) {
                variable.setValue(new _Long(((_Long)result).longValue() + 1));
            } else if (result instanceof _Float) {
                variable.setValue(new _Float(((_Float)result).floatValue() + 1));
            } else
                return new _Double(Double.NaN);    //  対応しない型
            return result;
        case OperatorData.OP_ARI_DEC_F:
            try {
                variable = (IVariable)operand2;
            } catch (ClassCastException cce) {
                throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), -1);
            }
            result = variable.getValue();
            if (result instanceof _Double) {
                variable.setValue(new _Double(((_Double)result).doubleValue() - 1));
            } else if (result instanceof _Integer) {
                variable.setValue(new _Integer(((_Integer)result).intValue() - 1));
            } else if (result instanceof _Byte) {
                variable.setValue(new _Byte((byte)(((_Byte)result).byteValue() - 1)));
            } else if (result instanceof _Short) {
                variable.setValue(new _Short((short)(((_Short)result).shortValue() - 1)));
            } else if (result instanceof _Long) {
                variable.setValue(new _Long(((_Long)result).longValue() - 1));
            } else if (result instanceof _Float) {
                variable.setValue(new _Float(((_Float)result).floatValue() - 1));
            } else
                return new _Double(Double.NaN);    //  対応しない型
            result = variable.getValue();
            return result;
        case OperatorData.OP_ARI_DEC_B:
            try {
                variable = (IVariable)operand1;
            } catch (ClassCastException cce) {
                throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), -1);
            }
            result = variable.getValue();
            if (result instanceof _Double) {
                variable.setValue(new _Double(((_Double)result).doubleValue() - 1));
            } else if (result instanceof _Integer) {
                variable.setValue(new _Integer(((_Integer)result).intValue() - 1));
            } else if (result instanceof _Byte) {
                variable.setValue(new _Byte((byte)(((_Byte)result).byteValue() - 1)));
            } else if (result instanceof _Short) {
                variable.setValue(new _Short((short)(((_Short)result).shortValue() - 1)));
            } else if (result instanceof _Long) {
                variable.setValue(new _Long(((_Long)result).longValue() - 1));
            } else if (result instanceof _Float) {
                variable.setValue(new _Float(((_Float)result).floatValue() - 1));
            } else
                return new _Double(Double.NaN);    //  対応しない型
            return result;
        case OperatorData.OP_ARI_ADD_SUBS:
            try {
                variable = (IVariable)operand1;
            } catch (ClassCastException cce) {
                throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), -1);
            }
            result = variable.getValue();
            if (operand2 instanceof IVariable)
                operand2 = ((IVariable)operand2).getValue();
            if (result instanceof _Double) {
                variable.setValue(new _Double(((_Double)result).doubleValue() + ((ExpNumberBase)operand2).doubleValue()));
            } else if (result instanceof _Integer) {
                variable.setValue(new _Integer(((_Integer)result).intValue() + ((ExpNumberBase)operand2).intValue()));
            } else     if (result instanceof _Byte) {
                variable.setValue(new _Byte((byte)(((_Byte)result).byteValue() + ((ExpNumberBase)operand2).byteValue())));
            } else     if (result instanceof _Short) {
                variable.setValue(new _Short((short)(((_Short)result).shortValue() + ((ExpNumberBase)operand2).shortValue())));
            } else     if (result instanceof _Long) {
                variable.setValue(new _Long(((_Long)result).longValue() + ((ExpNumberBase)operand2).longValue()));
            } else     if (result instanceof _Float) {
                variable.setValue(new _Float(((_Float)result).floatValue() + ((ExpNumberBase)operand2).floatValue()));
            } else
                return new _Double(Double.NaN);    //  対応しない型
            result = variable.getValue();
            return result;
        case OperatorData.OP_ARI_SUB_SUBS:
            try {
                variable = (IVariable)operand1;
            } catch (ClassCastException cce) {
                throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), -1);
            }
            result = variable.getValue();
            if (operand2 instanceof IVariable)
                operand2 = ((IVariable)operand2).getValue();
            if (result instanceof _Double) {
                variable.setValue(new _Double(((_Double)result).doubleValue() - ((ExpNumberBase)operand2).doubleValue()));
            } else if (result instanceof _Integer) {
                variable.setValue(new _Integer(((_Integer)result).intValue() - ((ExpNumberBase)operand2).intValue()));
            } else     if (result instanceof _Byte) {
                variable.setValue(new _Byte((byte)(((_Byte)result).byteValue() - ((ExpNumberBase)operand2).byteValue())));
            } else     if (result instanceof _Short) {
                variable.setValue(new _Short((short)(((_Short)result).shortValue() - ((ExpNumberBase)operand2).shortValue())));
            } else     if (result instanceof _Long) {
                variable.setValue(new _Long(((_Long)result).longValue() - ((ExpNumberBase)operand2).longValue()));
            } else     if (result instanceof _Float) {
                variable.setValue(new _Float(((_Float)result).floatValue() - ((ExpNumberBase)operand2).floatValue()));
            } else
                return new _Double(Double.NaN);    //  対応しない型
            result = variable.getValue();
            return result;
        case OperatorData.OP_ARI_MUL_SUBS:
            try {
                variable = (IVariable)operand1;
            } catch (ClassCastException cce) {
                throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), -1);
            }
            result = variable.getValue();
            if (operand2 instanceof IVariable)
                operand2 = ((IVariable)operand2).getValue();
            if (result instanceof _Double) {
                variable.setValue(new _Double(((_Double)result).doubleValue() * ((ExpNumberBase)operand2).doubleValue()));
            } else if (result instanceof _Integer) {
                variable.setValue(new _Integer(((_Integer)result).intValue() * ((ExpNumberBase)operand2).intValue()));
            } else     if (result instanceof _Byte) {
                variable.setValue(new _Byte((byte)(((_Byte)result).byteValue() * ((ExpNumberBase)operand2).byteValue())));
            } else     if (result instanceof _Short) {
                variable.setValue(new _Short((short)(((_Short)result).shortValue() * ((ExpNumberBase)operand2).shortValue())));
            } else     if (result instanceof _Long) {
                variable.setValue(new _Long(((_Long)result).longValue() * ((ExpNumberBase)operand2).longValue()));
            } else     if (result instanceof _Float) {
                variable.setValue(new _Float(((_Float)result).floatValue() * ((ExpNumberBase)operand2).floatValue()));
            } else
                return new _Double(Double.NaN);    //  対応しない型
            result = variable.getValue();
            return result;
        case OperatorData.OP_ARI_DIV_SUBS:
            try {
                variable = (IVariable)operand1;
            } catch (ClassCastException cce) {
                throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), -1);
            }
            result = variable.getValue();
            if (operand2 instanceof IVariable)
                operand2 = ((IVariable)operand2).getValue();
            if (result instanceof _Double) {
                variable.setValue(new _Double(((_Double)result).doubleValue() / ((ExpNumberBase)operand2).doubleValue()));
            } else if (result instanceof _Integer) {
                variable.setValue(new _Integer(((_Integer)result).intValue() / ((ExpNumberBase)operand2).intValue()));
            } else     if (result instanceof _Byte) {
                variable.setValue(new _Byte((byte)(((_Byte)result).byteValue() / ((ExpNumberBase)operand2).byteValue())));
            } else     if (result instanceof _Short) {
                variable.setValue(new _Short((short)(((_Short)result).shortValue() / ((ExpNumberBase)operand2).shortValue())));
            } else     if (result instanceof _Long) {
                variable.setValue(new _Long(((_Long)result).longValue() / ((ExpNumberBase)operand2).longValue()));
            } else     if (result instanceof _Float) {
                variable.setValue(new _Float(((_Float)result).floatValue() / ((ExpNumberBase)operand2).floatValue()));
            } else
                return new _Double(Double.NaN);    //  対応しない型
            result = variable.getValue();
            return result;
        case OperatorData.OP_ARI_REM_SUBS:
            try {
                variable = (IVariable)operand1;
            } catch (ClassCastException cce) {
                throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), -1);
            }
            result = variable.getValue();
            if (operand2 instanceof IVariable)
                operand2 = ((IVariable)operand2).getValue();
            if (result instanceof _Double) {
                variable.setValue(new _Double(((_Double)result).doubleValue() % ((ExpNumberBase)operand2).doubleValue()));
            } else if (result instanceof _Integer) {
                variable.setValue(new _Integer(((_Integer)result).intValue() % ((ExpNumberBase)operand2).intValue()));
            } else     if (result instanceof _Byte) {
                variable.setValue(new _Byte((byte)(((_Byte)result).byteValue() % ((ExpNumberBase)operand2).byteValue())));
            } else     if (result instanceof _Short) {
                variable.setValue(new _Short((short)(((_Short)result).shortValue() % ((ExpNumberBase)operand2).shortValue())));
            } else     if (result instanceof _Long) {
                variable.setValue(new _Long(((_Long)result).longValue() % ((ExpNumberBase)operand2).longValue()));
            } else     if (result instanceof _Float) {
                variable.setValue(new _Float(((_Float)result).floatValue() % ((ExpNumberBase)operand2).floatValue()));
            } else
                return new _Double(Double.NaN);    //  対応しない型
            result = variable.getValue();
            return result;

        
        case OperatorData.OP_BIT_AND_SUBS:
            try {
                variable = (IVariable)operand1;
            } catch (ClassCastException cce) {
                throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), -1);
            }
            result = variable.getValue();
            if (operand2 instanceof IVariable)
                operand2 = ((IVariable)operand2).getValue();
            if (result instanceof _Double) {
                variable.setValue(new _Double(((_Double)result).longValue() & ((ExpNumberBase)operand2).longValue()));
            } else if (result instanceof _Integer) {
                variable.setValue(new _Integer(((_Integer)result).intValue() & ((ExpNumberBase)operand2).intValue()));
            } else     if (result instanceof _Byte) {
                variable.setValue(new _Byte((byte)(((_Byte)result).byteValue() & ((ExpNumberBase)operand2).byteValue())));
            } else     if (result instanceof _Short) {
                variable.setValue(new _Short((short)(((_Short)result).shortValue() & ((ExpNumberBase)operand2).shortValue())));
            } else     if (result instanceof _Long) {
                variable.setValue(new _Long(((_Long)result).longValue() & ((ExpNumberBase)operand2).longValue()));
            } else     if (result instanceof _Float) {
                variable.setValue(new _Float(((_Float)result).longValue() & ((ExpNumberBase)operand2).longValue()));
            } else
                return new _Double(Double.NaN);    //  対応しない型
            result = variable.getValue();
            return result;
        case OperatorData.OP_BIT_OR_SUBS:
            try {
                variable = (IVariable)operand1;
            } catch (ClassCastException cce) {
                throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), -1);
            }
            result = variable.getValue();
            if (operand2 instanceof IVariable)
                operand2 = ((IVariable)operand2).getValue();
            if (result instanceof _Double) {
                variable.setValue(new _Double(((_Double)result).longValue() | ((ExpNumberBase)operand2).longValue()));
            } else if (result instanceof _Integer) {
                variable.setValue(new _Integer(((_Integer)result).intValue() | ((ExpNumberBase)operand2).intValue()));
            } else     if (result instanceof _Byte) {
                variable.setValue(new _Byte((byte)(((_Byte)result).byteValue() | ((ExpNumberBase)operand2).byteValue())));
            } else     if (result instanceof _Short) {
                variable.setValue(new _Short((short)(((_Short)result).shortValue() | ((ExpNumberBase)operand2).shortValue())));
            } else     if (result instanceof _Long) {
                variable.setValue(new _Long(((_Long)result).longValue() | ((ExpNumberBase)operand2).longValue()));
            } else     if (result instanceof _Float) {
                variable.setValue(new _Float(((_Float)result).longValue() | ((ExpNumberBase)operand2).longValue()));
            } else
                return new _Double(Double.NaN);    //  対応しない型
            result = variable.getValue();
            return result;
        case OperatorData.OP_BIT_XOR_SUBS:
            try {
                variable = (IVariable)operand1;
            } catch (ClassCastException cce) {
                throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), -1);
            }
            result = variable.getValue();
            if (operand2 instanceof IVariable)
                operand2 = ((IVariable)operand2).getValue();
            if (result instanceof _Double) {
                variable.setValue(new _Double(((_Double)result).longValue() ^ ((ExpNumberBase)operand2).longValue()));
            } else if (result instanceof _Integer) {
                variable.setValue(new _Integer(((_Integer)result).intValue() ^ ((ExpNumberBase)operand2).intValue()));
            } else     if (result instanceof _Byte) {
                variable.setValue(new _Byte((byte)(((_Byte)result).byteValue() ^ ((ExpNumberBase)operand2).byteValue())));
            } else     if (result instanceof _Short) {
                variable.setValue(new _Short((short)(((_Short)result).shortValue() ^ ((ExpNumberBase)operand2).shortValue())));
            } else     if (result instanceof _Long) {
                variable.setValue(new _Long(((_Long)result).longValue() ^ ((ExpNumberBase)operand2).longValue()));
            } else     if (result instanceof _Float) {
                variable.setValue(new _Float(((_Float)result).longValue() ^ ((ExpNumberBase)operand2).longValue()));
            } else
                return new _Double(Double.NaN);    //  対応しない型
            result = variable.getValue();
            return result;
        
        case OperatorData.OP_SFT_LEFT_SUBS:
            try {
                variable = (IVariable)operand1;
            } catch (ClassCastException cce) {
                throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), -1);
            }
            result = variable.getValue();
            if (operand2 instanceof IVariable)
                operand2 = ((IVariable)operand2).getValue();
            if (result instanceof _Double) {
                variable.setValue(new _Double(((_Double)result).longValue() << ((ExpNumberBase)operand2).longValue()));
            } else if (result instanceof _Integer) {
                variable.setValue(new _Integer(((_Integer)result).intValue() << ((ExpNumberBase)operand2).intValue()));
            } else     if (result instanceof _Byte) {
                variable.setValue(new _Byte((byte)(((_Byte)result).byteValue() << ((ExpNumberBase)operand2).byteValue())));
            } else     if (result instanceof _Short) {
                variable.setValue(new _Short((short)(((_Short)result).shortValue() << ((ExpNumberBase)operand2).shortValue())));
            } else     if (result instanceof _Long) {
                variable.setValue(new _Long(((_Long)result).longValue() << ((ExpNumberBase)operand2).longValue()));
            } else     if (result instanceof _Float) {
                variable.setValue(new _Float(((_Float)result).longValue() << ((ExpNumberBase)operand2).longValue()));
            } else
                return new _Double(Double.NaN);    //  対応しない型
            result = variable.getValue();
            return result;
        case OperatorData.OP_SFT_RIGHT_SUBS:
            try {
                variable = (IVariable)operand1;
            } catch (ClassCastException cce) {
                throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), -1);
            }
            result = variable.getValue();
            if (operand2 instanceof IVariable)
                operand2 = ((IVariable)operand2).getValue();
            if (result instanceof _Double) {
                variable.setValue(new _Double(((_Double)result).longValue() >> ((ExpNumberBase)operand2).longValue()));
            } else if (result instanceof _Integer) {
                variable.setValue(new _Integer(((_Integer)result).intValue() >> ((ExpNumberBase)operand2).intValue()));
            } else     if (result instanceof _Byte) {
                variable.setValue(new _Byte((byte)(((_Byte)result).byteValue() >> ((ExpNumberBase)operand2).byteValue())));
            } else     if (result instanceof _Short) {
                variable.setValue(new _Short((short)(((_Short)result).shortValue() >> ((ExpNumberBase)operand2).shortValue())));
            } else     if (result instanceof _Long) {
                variable.setValue(new _Long(((_Long)result).longValue() >> ((ExpNumberBase)operand2).longValue()));
            } else     if (result instanceof _Float) {
                variable.setValue(new _Float(((_Float)result).longValue() >> ((ExpNumberBase)operand2).longValue()));
            } else
                return new _Double(Double.NaN);    //  対応しない型
            result = variable.getValue();
            return result;
        case OperatorData.OP_SFT_RIGHT_UNSIGNED_SUBS:
            try {
                variable = (IVariable)operand1;
            } catch (ClassCastException cce) {
                throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), -1);
            }
            result = variable.getValue();
            if (operand2 instanceof IVariable)
                operand2 = ((IVariable)operand2).getValue();
            if (result instanceof _Double) {
                variable.setValue(new _Double(((_Double)result).longValue() >>> ((ExpNumberBase)operand2).longValue()));
            } else if (result instanceof _Integer) {
                variable.setValue(new _Integer(((_Integer)result).intValue() >>> ((ExpNumberBase)operand2).intValue()));
            } else     if (result instanceof _Byte) {
                variable.setValue(new _Byte((byte)(((_Byte)result).byteValue() >>> ((ExpNumberBase)operand2).byteValue())));
            } else     if (result instanceof _Short) {
                variable.setValue(new _Short((short)(((_Short)result).shortValue() >>> ((ExpNumberBase)operand2).shortValue())));
            } else     if (result instanceof _Long) {
                variable.setValue(new _Long(((_Long)result).longValue() >>> ((ExpNumberBase)operand2).longValue()));
            } else     if (result instanceof _Float) {
                variable.setValue(new _Float(((_Float)result).longValue() >>> ((ExpNumberBase)operand2).longValue()));
            } else
                return new _Double(Double.NaN);    //  対応しない型
            result = variable.getValue();
            return result;
        
        case OperatorData.OP_ARRAY_ACCESS:
            if ((operand1 instanceof IExpObject) == false) {
                try {
                    operand1 = ((IVariable)operand1).getValue();
                } catch (ClassCastException cce) {
                    throw new ExpException(Expresser.getResString("NotArrayAccess"), -1);
                }
            }    
            return _DefaultOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);

        case OperatorData.OP_CREATE_OBJECT:
            //  operand1 は無し
            if (operand2 instanceof IFunction) {
                IFunction function = (IFunction)operand2;
                if (function instanceof ExpConstructor)
                    throw new ExpException(Expresser.getResString("IllegalOperator"), -1);    //  new が２つ重ねて記述されている
                return new ExpConstructor(function);
            }
            throw new ExpException(Expresser.getResString("NotConstructor"), -1); 
        default:
            return _DefaultOperatorSet.operatorSet.operation(operator, operand1, operand2, runtime);
        }
    }

}
