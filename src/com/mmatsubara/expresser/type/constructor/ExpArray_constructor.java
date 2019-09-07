package com.mmatsubara.expresser.type.constructor;

import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpArray;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.IExpObject;

/**
 * Array型のコンストラクタです。<br/>
 * <br/>
 * 作成日: 2005/05/01
 * 
 * @author m.matsubara
 */

public class ExpArray_constructor extends ExpFunctionBase {
    /**
     * Array型のコンストラクタを生成します。 
     * @throws ExpException
     */
    public ExpArray_constructor() throws ExpException {
        super("Array", 1);
        putProperty(StringToId.toId("prototype"), ExpArray.getPrototype());
    }


    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        if (arguments.length >= 1) {
            if (arguments.length == 1) {
                ExpNumberBase arg1 = (ExpNumberBase)arguments[0];
                if (arg1 == null)
                    return new ExpArray();
                else
                	return new ExpArray(arg1.intValue());
            } else {
                return new ExpArray(arguments);
            }
        } else {
            return new ExpArray();
        }
        
    }
    
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getProperty(java.lang.Integer)
     */
    public IExpObject getProperty(Integer id) throws ExpException {
        if (id.intValue() == ExpObject_constructor.propertyId$prototype.intValue())
            return ExpArray.getPrototype();
        IExpObject obj = super.getProperty(id);
      	return obj;
    }
}
