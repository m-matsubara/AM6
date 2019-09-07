package com.mmatsubara.expresser.type.constructor;

import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpObject;
import com.mmatsubara.expresser.type.IExpObject;

/**
 * Object型のコンストラクタです。<br/>
 * <br/>
 * 作成日: 2005/05/01
 * 
 * @author m.matsubara
 */

public class ExpObject_constructor extends ExpFunctionBase {
    public static final Integer propertyId$prototype = StringToId.toId("prototype");

    /**
     * Object型のコンストラクタを生成します。 
     * @throws ExpException
     */
    public ExpObject_constructor() throws ExpException {
        super("Object", 1);
        putProperty(StringToId.toId("prototype"), ExpObject.getPrototype());
    }


    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        return new ExpObject();
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
