package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.ITypeConverter;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.IArray;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._String;

/**
 * Array型の toString() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/05/08
 * 
 * @author m.matsubara
 */

public class Array__toString extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("toString");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Array__toString() throws ExpException {
        super("toString", 2);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        IArray value = (IArray)thisObject;
        
        int length = value.length();
        if (length <= 0) {
            return new _String("");
        }
        
        ITypeConverter typeConverter = runtime.getTypeConverter();
        StringBuffer result = new StringBuffer();
        result.append(typeConverter.toString(runtime, value.getProperty(0)));
        for (int idx = 1; idx < length; idx++) {
            result.append(",");
            result.append(typeConverter.toString(runtime, value.getProperty(idx)));
        }
        return new _String(result.toString());
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Array__toString();
        return instance;
    }
}
