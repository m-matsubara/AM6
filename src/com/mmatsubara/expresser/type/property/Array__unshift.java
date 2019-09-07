package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.IArray;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Integer;

/**
 * Array型の unshift() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/05/10
 * 
 * @author m.matsubara
 */

public class Array__unshift extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("unshift");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Array__unshift() throws ExpException {
        super("unshift", 2);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        IArray array = (IArray)thisObject;
        int length = array.length();
        int argLength = arguments.length;
        length += argLength;
        array.setLength(length);
        
        for (int idx = length - 1; idx >= argLength; idx--) {
            IExpObject value = array.getProperty(idx - argLength);
            array.putProperty(idx, value);
        }
        for (int idx = 0; idx < argLength; idx++) {
            array.putProperty(idx, arguments[idx]);
        }
        return new _Integer(length);
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Array__unshift();
        return instance;
    }
}
