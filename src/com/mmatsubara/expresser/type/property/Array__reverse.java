package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.IArray;
import com.mmatsubara.expresser.type.IExpObject;


/**
 * Array型の reverse() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/05/08
 * 
 * @author m.matsubara
 */

public class Array__reverse extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("reverse");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Array__reverse() throws ExpException {
        super("reverse", 0);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        IArray value = (IArray)thisObject;
        int minIdx = 0;
        int maxIdx = value.length() - 1;
        while (minIdx < maxIdx) {
            IExpObject swap = value.getProperty(minIdx);
            value.putProperty(minIdx, value.getProperty(maxIdx));
            value.putProperty(maxIdx, swap);
            
            minIdx++;
            maxIdx--;
        }
        return value;
    }

    /**
     * インスタンスの生成
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Array__reverse();
        return instance;
    }
}
