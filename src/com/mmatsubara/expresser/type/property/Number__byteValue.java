package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Byte;

/**
 * Number型の byteValue() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/04/03
 * 
 * @author m.matsubara
 */
public class Number__byteValue extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("byteValue");
    private static IExpObject instance = null;  

    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Number__byteValue() throws ExpException {
        super("byteValue", 0);
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        ExpNumberBase number = (ExpNumberBase)thisObject; 
        return new _Byte(number.byteValue());
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Number__byteValue();
        return instance;
    }
}
