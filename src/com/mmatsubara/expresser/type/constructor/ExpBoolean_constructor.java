package com.mmatsubara.expresser.type.constructor;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.ITypeConverter;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpBoolean;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Boolean;

/**
 * Boolean型のコンストラクタです。<br/>
 * <br/>
 * 作成日: 2005/05/03
 * 
 * @author m.matsubara
 */

public class ExpBoolean_constructor extends ExpFunctionBase {
    /**
     * Boolean型のコンストラクタを生成します。 
     * @throws ExpException
     */
    public ExpBoolean_constructor() throws ExpException {
        super("Boolean", 1);
        putProperty(StringToId.toId("prototype"), ExpBoolean.getPrototype());
    }


    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        if (arguments.length >= 1) {
            if (thisObject != Expresser.UNDEFINED) {  //  コンストラクタ呼び出しの判定
            	if (arguments[0] == null)
            		return new ExpBoolean(false);
            	else if (arguments[0].getClass() == _Boolean.class)
                    return new ExpBoolean(((_Boolean)arguments[0]).booleanValue());
                else {
                    ITypeConverter typeConverter = runtime.getTypeConverter();
                    _Boolean bool = typeConverter.toBoolean(runtime, arguments[0]) ;
                    return new ExpBoolean(bool.booleanValue());
                }
            } else {
                ITypeConverter typeConverter = runtime.getTypeConverter();
                return typeConverter.toBoolean(runtime, arguments[0]);
            }
        } else {
            return new ExpBoolean(false);
        }
        
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getProperty(java.lang.Integer)
     */
    public IExpObject getProperty(Integer id) throws ExpException {
        if (id.intValue() == ExpObject_constructor.propertyId$prototype.intValue())
            return ExpBoolean.getPrototype();
        IExpObject obj = super.getProperty(id);
      	return obj;
    }
}
