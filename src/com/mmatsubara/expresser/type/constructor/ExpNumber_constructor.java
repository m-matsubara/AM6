package com.mmatsubara.expresser.type.constructor;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.ITypeConverter;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpNumber;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Double;

/**
 * Number型のコンストラクタです。<br/>
 * <br/>
 * 作成日: 2005/05/03
 * 
 * @author m.matsubara
 */

public class ExpNumber_constructor extends ExpFunctionBase {
    /**
     * Number型のコンストラクタを生成します。 
     * @throws ExpException
     */
    public ExpNumber_constructor() throws ExpException {
        super("Number", 1);
        putConstProperty(StringToId.toId("prototype"), ExpNumber.getPrototype());
        putConstProperty(StringToId.toId("MIN_VALUE"),         new _Double(Double.MIN_VALUE));
        putConstProperty(StringToId.toId("MAX_VALUE"),         new _Double(Double.MAX_VALUE));
        putConstProperty(StringToId.toId("NaN"),               new _Double(Double.NaN));
        putConstProperty(StringToId.toId("NEGATIVE_INFINITY"), new _Double(Double.NEGATIVE_INFINITY));
        putConstProperty(StringToId.toId("POSITIVE_INFINITY"), new _Double(Double.POSITIVE_INFINITY));
    }


    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        if (arguments.length >= 1) {
            if (thisObject != Expresser.UNDEFINED) {  //  コンストラクタ呼び出しの判定
            	if (arguments[0] == null)
            		return new ExpNumber(0);
            	else if (arguments[0] instanceof ExpNumberBase)
                    return new ExpNumber(((ExpNumberBase)arguments[0]).doubleValue());
                else {
                    ITypeConverter typeConverter = runtime.getTypeConverter();
                    return new ExpNumber(typeConverter.toNumber(runtime, arguments[0]).doubleValue());
                }
            } else {
                ITypeConverter typeConverter = runtime.getTypeConverter();
                return typeConverter.toNumber(runtime, arguments[0]);
            }
        } else {
            return new ExpNumber(0);
        }
        
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getProperty(java.lang.Integer)
     */
    public IExpObject getProperty(Integer id) throws ExpException {
        if (id.intValue() == ExpObject_constructor.propertyId$prototype.intValue())
            return ExpNumber.getPrototype();
        IExpObject obj = super.getProperty(id);
       	return obj;
    }
}
