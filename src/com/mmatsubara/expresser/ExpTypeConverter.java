package com.mmatsubara.expresser;

import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.exception.ExpTypeError;
import com.mmatsubara.expresser.type.ExpBoolean;
import com.mmatsubara.expresser.type.ExpNumber;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.ExpString;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type.IFunction;
import com.mmatsubara.expresser.type.JavaClass;
import com.mmatsubara.expresser.type.JavaObject;
import com.mmatsubara.expresser.type.JavaObjectMember;
import com.mmatsubara.expresser.type._Boolean;
import com.mmatsubara.expresser.type._Double;
import com.mmatsubara.expresser.type._String;

/**
 * 型変換の動作を規定します。
 * 
 * 作成日: 2005/05/22
 * 
 * @author m.matsubara
 */

public class ExpTypeConverter implements ITypeConverter {
    
    /**
     * プリミティブ方かどうか判定します。
     * @param value 判定する値
     * @return プリミティブ方の時 true
     */
    public boolean isPrimitive(Object value) {
        if (value instanceof _Boolean)
            return true;
        else if (value instanceof ExpNumberBase)
            return true;
        else if (value instanceof _String)
            return true;
        return false;
    }
    

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.ITypeConverter#toPrimitive(RuntimeData, java.lang.Object)
     */
    public IExpObject toPrimitive(RuntimeData runtime, IExpObject value) throws ExpException {
        if (value == Expresser.UNDEFINED || value == null)
            return value;
        else if (value instanceof _Boolean)
            return value;
        else if (value instanceof ExpNumberBase)
            return value;
        else if (value instanceof _String)
            return value;
        else if (value instanceof JavaObject)
            return new _String(value.toString());
        else if (value instanceof JavaClass)
            return new _String(value.toString());
        else if (value instanceof JavaObjectMember)
            return new _String(value.toString());
        else if (value instanceof IExpObject) {
            //  toString() でまず変換できるか
            IExpObject object = (IExpObject)value;
            IExpObject property = object.getProperty(StringToId.toId("toString"));
            if (property instanceof IFunction) {
                IFunction function = (IFunction)property;
                IExpObject newValue = function.callFunction(runtime, object, new IExpObject[] {});
                if (isPrimitive(newValue))
                    return newValue;
            }
            //  valueOf() で変換できるか
            property = object.getProperty(StringToId.toId("valueOf"));
            if (property instanceof IFunction) {
                IFunction function = (IFunction)property;
                IExpObject newValue = function.callFunction(runtime, object, new IExpObject[] {});
                if (isPrimitive(newValue))
                    return newValue;
            }
        }
        throw new ExpTypeError(Expresser.getResString("ConvertError_Primitive"), -1);
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.ITypeConverter#toBoolean(RuntimeData runtime, java.lang.Object)
     */
    public _Boolean toBoolean(RuntimeData runtime, IExpObject value) throws ExpException {
        if (value == Expresser.UNDEFINED || value == null)
            return new _Boolean(false);
        else if (value.getClass() == _Boolean.class)
            return (_Boolean)value;
        else if (value instanceof ExpNumberBase) {
            double doubleValue = ((ExpNumberBase)value).doubleValue();
            return new _Boolean(doubleValue != 0 && Double.isNaN(doubleValue) == false);
        }
        else if (value instanceof _String)
            if (value.toString().equals(""))
                return new _Boolean(false);
            else
                return new _Boolean(true);
        else if (value instanceof IExpObject)
            //  (new Boolean(false)) をコンバートすると true が返ります ECMA-262 の仕様
            return new _Boolean(true);
        else
            return new _Boolean(true);
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.ITypeConverter#toNumber(RuntimeData runtime, java.lang.Object)
     */
    public _Double toNumber(RuntimeData runtime, IExpObject value) throws ExpException {
        if (value == null)
            return new _Double(Double.NaN);
        else if (value == Expresser.UNDEFINED)
            return new _Double(Double.NaN);
        else if (value instanceof _Boolean)
            return new _Double(((_Boolean)value).booleanValue() ? 1 : 0);
        else if (value instanceof ExpNumberBase)
            return new _Double(((ExpNumberBase)value).doubleValue());
        else if (value instanceof _String)
            try {
                String string = ((_String)value).toString();
                return Expresser.toValue(string.toCharArray(), 0, string.length());
            } catch (Exception e) {
                return new _Double(Double.NaN);
            }
        else if (value instanceof IExpObject) {
            value = toPrimitive(runtime, value);
            return toNumber(runtime, value);
        }
        else {
//            throw new ExpTypeError(Expresser.getResString("ConvertError_Number"), -1);
            return new _Double(Double.NaN);
        }
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.ITypeConverter#toString(RuntimeData runtime, java.lang.Object)
     */
    public _String toString(RuntimeData runtime, IExpObject value) throws ExpException {
        if (value == Expresser.UNDEFINED)
            return new _String("undefined");
        else if (value == null)
            return new _String("null");
        else if (value instanceof _Boolean)
            return new _String(value.toString());
        else if (value instanceof ExpNumberBase)
            return new _String(value.toString());
        else if (value instanceof _String)
            return (_String)value;
        else if (value instanceof IExpObject)
            return toString(runtime, toPrimitive(runtime, value));
        else
            return new _String(value.toString());
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.ITypeConverter#toObject(RuntimeData runtime, java.lang.Object)
     */
    public IExpObject toObject(RuntimeData runtime, IExpObject value) throws ExpException {
        if (value == Expresser.UNDEFINED || value == null)
            throw new ExpTypeError(Expresser.getResString("ConvertError_Object"), -1);
        else if (value instanceof _Boolean)
            return new ExpBoolean(((_Boolean)value).booleanValue());
        else if (value instanceof ExpNumberBase)
            return new ExpNumber(((ExpNumberBase)value).doubleValue());
        else if (value instanceof _String)
            return new ExpString(((_String)value).toString());
        else if (value instanceof IExpObject) {
            return (IExpObject)value;
        }
        else
            throw new ExpTypeError(Expresser.getResString("ConvertError_Object"), -1);
    }
}
