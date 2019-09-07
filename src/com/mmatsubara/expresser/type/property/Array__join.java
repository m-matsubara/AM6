package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.IArray;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._String;

/**
 * Array型の join() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/05/08
 * 
 * @author m.matsubara
 */

public class Array__join extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("join");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Array__join() throws ExpException {
        super("join", 2);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        IArray value = (IArray)thisObject;
        String separater = ",";
        if (arguments.length >= 1)
            separater = arguments[0].toString();
            
        StringBuffer sb = new StringBuffer();
        Object obj;
        int max = value.length();
        if (max != 0) {
            obj = value.getProperty(0);
            if (obj != Expresser.UNDEFINED)
                sb.append(obj);
            for (int idx = 1; idx < max; idx++) {
                sb.append(separater);
                obj = value.getProperty(idx);
                if (obj != Expresser.UNDEFINED) {
                    sb.append(obj);
                }
            }
        }
        return new _String(sb.toString());
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Array__join();
        return instance;
    }
}
