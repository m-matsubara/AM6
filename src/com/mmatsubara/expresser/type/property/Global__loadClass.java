package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type.JavaClass;

/**
 * Global型の loadClass() メソッドです。<br/>
 * このメソッドはECMA262規格にはありません<br/>
 * <br/>
 * 作成日: 2005/07/24
 * 
 * @author m.matsubara
 */

public class Global__loadClass extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("loadClass");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Global__loadClass() throws ExpException {
        super("loadClass", 1);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        if (arguments.length != 1)
            throw new ExpException(Expresser.getResString("ArgumentsNumberError"), -1);
        try {
            return new JavaClass(Class.forName(arguments[0].toString()));
        } catch (ClassNotFoundException e) {
            throw new ExpException(Expresser.getResString("FailureLoadClass"), -1);
        }
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Global__loadClass();
        return instance;
    }
}
