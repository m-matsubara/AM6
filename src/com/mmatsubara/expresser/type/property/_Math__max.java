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
 * Mathオブジェクトの max() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/05/08
 * 
 * @author m.matsubara
 */

public class _Math__max extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("max");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private _Math__max() throws ExpException {
        super("max", 2);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
        throws ExpException {
        if (arguments.length < 1)
            throw new ExpException(Expresser.getResString("ArgumentsNumberError"), -1);

        double maxnumber = Double.NEGATIVE_INFINITY;
        int max = arguments.length;
        for (int idx = 0; idx < max; idx++) {
            if (arguments[idx] != null && arguments[idx] != Expresser.UNDEFINED) {
                double number;
                try {
                    number = ((ExpNumberBase)(arguments[idx])).doubleValue();
                } catch (ClassCastException cce) {
                    number = runtime.getTypeConverter().toNumber(runtime, arguments[idx]).doubleValue();
                }
                if (Double.isNaN(number))
                    return new _Double(Double.NaN);
                if (number > maxnumber)
                    maxnumber = number;
            }
        }
        return new _Double(maxnumber);
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new _Math__max();
        return instance;
    }
}
