package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._String;

/**
 * String型の charAt() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/04/03<br/>
 * 
 * @author m.matsubara
 */

public class String__toLowerCase extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("toLowerCase");
    public static final Integer toLocaleLowerCaseID = StringToId.toId("toLocaleLowerCase");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private String__toLowerCase() throws ExpException {
        super("toLowerCase", 0);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        String value = ((IExpObject)thisObject).toString();
        return new _String(value.toLowerCase());
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new String__toLowerCase();
        return instance;
    }
}
