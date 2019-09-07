package com.mmatsubara.expresser.type.constructor;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._String;

/**
 * String型の fromCharCode() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/04/03
 * 
 * @author m.matsubara
 */
public class ExpString_constructor_fromCharCode extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("fromCharCode");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private ExpString_constructor_fromCharCode() throws ExpException {
        super("fromCharCode", 1);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        //String value = ((IExpObject)thisObject).toString();
        if (arguments.length < 1)
            throw new ExpException(Expresser.getResString("ArgumentsNumberError"), -1);
        int[] params = new int[arguments.length];
        for (int idx = 0; idx < arguments.length; idx++) {
	        try {
	            params[idx] = ((ExpNumberBase)(arguments[idx])).intValue();
	        } catch (ClassCastException cce) {
	        	params[idx] = runtime.getTypeConverter().toNumber(runtime, arguments[idx]).intValue();
	        }
        }
        String result = new String(params, 0, params.length);
        return new _String(result);
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new ExpString_constructor_fromCharCode();
        return instance;
    }
}
