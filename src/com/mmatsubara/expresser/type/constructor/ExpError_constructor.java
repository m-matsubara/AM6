package com.mmatsubara.expresser.type.constructor;

import com.mmatsubara.expresser.ITypeConverter;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpError;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpObject;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._String;

/**
 * Error型のコンストラクタです。<br/>
 * <br/>
 * 作成日: 2005/05/01
 * 
 * @author m.matsubara
 */

public class ExpError_constructor extends ExpFunctionBase {
    private static final Integer property$message = StringToId.toId("message");
    private static final Integer property$name = StringToId.toId("name");
    
    
    /**
     * Error型のコンストラクタを生成します。 
     * @throws ExpException
     */
    public ExpError_constructor() throws ExpException {
        super("Error", 1);
        putProperty(StringToId.toId("prototype"), ExpError.getPrototype());
    }


    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        IExpObject message = new _String("");
        if (arguments.length >= 1) 
            message = arguments[0];
        ExpObject result = new ExpError(message);
        ITypeConverter typeConverter = runtime.getTypeConverter();
        result.putProperty(property$message, typeConverter.toString(runtime, message));
        result.putProperty(property$name, new _String(getFunctionName()));
        
        return result;
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getProperty(java.lang.Integer)
     */
    public IExpObject getProperty(Integer id) throws ExpException {
        if (id.intValue() == ExpObject_constructor.propertyId$prototype.intValue())
            return ExpObject.getPrototype();
        IExpObject obj = super.getProperty(id);
       	return obj;
    }
}
