package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpRegExp;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._String;

/**
 * String型の replace() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/07/24
 * 
 * @author m.matsubara
 */

public class String__search extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("search");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private String__search() throws ExpException {
        super("replace", 1);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        String value = ((IExpObject)thisObject).toString();
        if (arguments.length < 1)
            throw new ExpException(Expresser.getResString("ArgumentsNumberError"), -1);

        ExpRegExp regExp = null;
        if (arguments[0] instanceof ExpRegExp) {
            regExp = (ExpRegExp)(arguments[0]);
        } else if (arguments[0] instanceof _String) {
            regExp = new ExpRegExp(((_String)arguments[0]).toString(), "");
        } else {
            throw new ExpException(Expresser.getResString("ArgumentError"), -1);
        }
        return regExp.exec(value).getProperty(0);
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new String__search();
        return instance;
    }
}
