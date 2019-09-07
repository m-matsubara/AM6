package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._String;

/**
 * String型の slice() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/04/03
 * 
 * @author m.matsubara
 */

public class String__slice extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("slice");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private String__slice() throws ExpException {
        super("slice", 2);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        String value = ((IExpObject)thisObject).toString();
        if (arguments.length < 1)
            throw new ExpException(Expresser.getResString("ArgumentsNumberError"), -1);

        //  from パラメータの取り出し
        int from;
        try {
            from = ((ExpNumberBase)(arguments[0])).intValue();
        } catch (ClassCastException cce) {
            from = runtime.getTypeConverter().toNumber(runtime, arguments[0]).intValue();
        }
        if (from < 0)
            from = value.length() + from;
        
        //  to パラメータの取り出し（有れば）
        int to = value.length();
        if (arguments.length >= 2) {
            try {
                to = ((ExpNumberBase)(arguments[0])).intValue();
            } catch (ClassCastException cce) {
                to = runtime.getTypeConverter().toNumber(runtime, arguments[0]).intValue();
            }
            if (to < 0)
                to = value.length() + to;
        }
        
        //  演算
        value = value.substring(from, to);
        return new _String(value);
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new String__slice();
        return instance;
    }
}
