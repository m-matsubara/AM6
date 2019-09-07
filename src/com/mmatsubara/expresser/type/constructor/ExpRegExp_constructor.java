package com.mmatsubara.expresser.type.constructor;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.ITypeConverter;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpRegExp;
import com.mmatsubara.expresser.type.IExpObject;

/**
 * RegExp型のコンストラクタです。<br/>
 * <br/> 
 * 作成日: 2005/05/03
 * 
 * @author m.matsubara
 */

public class ExpRegExp_constructor extends ExpFunctionBase {
    /**
     * RegExp型のコンストラクタを生成します。 
     * @throws ExpException
     */
    public ExpRegExp_constructor() throws ExpException {
        super("RegExp", 2);
        putProperty(StringToId.toId("prototype"), ExpRegExp.getPrototype());
    }


    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        ITypeConverter typeConverter = runtime.getTypeConverter();
        if (arguments.length >= 1) {
            if (arguments[0] instanceof ExpRegExp) {
                return (ExpRegExp)(arguments[0]);
            } else {
                String pattern = typeConverter.toString(runtime, arguments[0]).toString();
                String flags = "";
                if (arguments.length >= 2)
                    arguments[1].toString();
                return new ExpRegExp(pattern, flags);
            } 
        } else {
            throw new ExpException(Expresser.getResString("ArgumentsNumberError"), -1);
        }
        
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getProperty(java.lang.Integer)
     */
    public IExpObject getProperty(Integer id) throws ExpException {
        if (id.intValue() == ExpObject_constructor.propertyId$prototype.intValue())
            return ExpRegExp.getPrototype();
        IExpObject obj = super.getProperty(id);
       	return obj;
    }
    
}
