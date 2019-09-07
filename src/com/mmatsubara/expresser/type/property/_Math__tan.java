package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Double;

/**
 * Mathオブジェクトの tan() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/05/08
 * 
 * @author m.matsubara
 */

public class _Math__tan extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("tan");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private _Math__tan() throws ExpException {
        super("tan", 1);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        if (arguments.length < 1)
            return new _Double(Double.NaN);
        double number;
        try {
            number = ((ExpNumberBase)(arguments[0])).doubleValue();
        } catch (ClassCastException cce) {
            number = runtime.getTypeConverter().toNumber(runtime, arguments[0]).doubleValue();
        }
        return new _Double(_Math__sin.sin(number) / _Math__cos.cos(number));
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new _Math__tan();
        return instance;
    }
}