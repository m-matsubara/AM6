package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.IArray;
import com.mmatsubara.expresser.type.IExpObject;


/**
 * Array型の shift() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/05/10
 * 
 * @author m.matsubara
 */

public class Array__shift extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("shift");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Array__shift() throws ExpException {
        super("shift", 0);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        IArray array = (IArray)thisObject;
        int length = array.length();
        if (length == 0)
            return Expresser.UNDEFINED;
        
        IExpObject result = array.getProperty(0);
        
        
        for (int idx = 0; idx < length - 1; idx++) {
            IExpObject value = array.getProperty(idx + 1);
            array.putProperty(idx, value);
        }
        array.setLength(length - 1);
        return result;
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Array__shift();
        return instance;
    }
}
