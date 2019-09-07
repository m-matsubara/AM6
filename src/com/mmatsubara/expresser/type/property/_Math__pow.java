package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Double;

/**
 * Mathオブジェクトの pow() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/05/08
 * 
 * @author m.matsubara
 */

public class _Math__pow extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("pow");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private _Math__pow() throws ExpException {
        super("pow", 2);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        if (arguments.length < 2)
            throw new ExpException(Expresser.getResString("ArgumentsNumberError"), -1);
        double number;
        try {
            number = ((ExpNumberBase)(arguments[0])).doubleValue();
        } catch (ClassCastException cce) {
            number = runtime.getTypeConverter().toNumber(runtime, arguments[0]).doubleValue();
        }
        double number2;
        try {
            number2 = ((ExpNumberBase)(arguments[1])).doubleValue();
        } catch (ClassCastException cce) {
            number2 = runtime.getTypeConverter().toNumber(runtime, arguments[1]).doubleValue();
        }
        return new _Double(Math.pow(number, number2));
        
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new _Math__pow();
        return instance;
    }
}
