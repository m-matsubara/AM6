package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Boolean;

/**
 * Global型の isNaN() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/07/24
 * 
 * @author m.matsubara
 */

public class Global__isNaN extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("isNaN");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Global__isNaN() throws ExpException {
        super("isNaN", 1);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        if (arguments.length < 1)
            throw new ExpException(Expresser.getResString("ArgumentsNumberError"), -1);

        if (arguments[0] instanceof ExpNumberBase) {
            double value = ((ExpNumberBase)(arguments[0])).doubleValue();
            
            return new _Boolean(Double.isNaN(value));
        }
        return new _Boolean(false);
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Global__isNaN();
        return instance;
    }
}
