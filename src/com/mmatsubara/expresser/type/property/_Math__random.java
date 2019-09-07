package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Double;

/**
 * Mathオブジェクトの random() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/05/08
 * 
 * @author m.matsubara
 */

public class _Math__random extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("random");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private _Math__random() throws ExpException {
        super("random", 0);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        return new _Double(Math.random());
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new _Math__random();
        return instance;
    }
}
