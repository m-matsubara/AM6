package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Integer;

/**
 * String型の charCodeAt() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/04/03
 * 
 * @author m.matsubara
 */

public class String__charCodeAt extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("charCodeAt");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private String__charCodeAt() throws ExpException {
        super("charCodeAt", 1);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        String value = ((IExpObject)thisObject).toString();
        if (arguments.length < 1)
            throw new ExpException(Expresser.getResString("ArgumentsNumberError"), -1);
        int idx;
        try {
            idx = ((ExpNumberBase)(arguments[0])).intValue();
        } catch (ClassCastException cce) {
            idx = runtime.getTypeConverter().toNumber(runtime, arguments[0]).intValue();
        }
        int result = value.charAt(idx);
        return new _Integer(result);
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new String__charCodeAt();
        return instance;
    }
}
