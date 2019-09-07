package com.mmatsubara.expresser.type.constructor;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.ITypeConverter;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpString;
import com.mmatsubara.expresser.type.IExpObject;

/**
 * String型のコンストラクタです。<br/>
 * <br/> 
 * 作成日: 2005/05/01
 * 
 * @author m.matsubara
 */

public class ExpString_constructor extends ExpFunctionBase {
	
	/**
     * String型のコンストラクタを生成します。 
     * @throws ExpException
     */
    public ExpString_constructor() throws ExpException {
        super("String", 1);
        putProperty(StringToId.toId("prototype"), ExpString.getPrototype());
        putProperty(ExpString_constructor_fromCharCode.ID, ExpString_constructor_fromCharCode.getInstance());
    }


    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        ITypeConverter typeConverter = runtime.getTypeConverter();
        if (arguments.length >= 1) {
            if (thisObject != Expresser.UNDEFINED) {  //  コンストラクタ呼び出しの判定
                String value = typeConverter.toString(runtime, arguments[0]).toString();
                return new ExpString(value);
            } else {
                return typeConverter.toString(runtime, arguments[0]); 
            }
        } else {
            return new ExpString(null);
        }
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getProperty(java.lang.Integer)
     */
    public IExpObject getProperty(Integer id) throws ExpException {
        if (id.intValue() == ExpObject_constructor.propertyId$prototype.intValue())
            return ExpString.getPrototype();
        IExpObject obj = super.getProperty(id);
       	return obj;
    }
}
