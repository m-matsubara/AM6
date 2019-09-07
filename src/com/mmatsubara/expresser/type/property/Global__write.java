package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.ITypeConverter;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.IExpObject;


/**
 * Global型の write() メソッドです。<br/>
 * このメソッドはECMA262規格にはありません<br/>
 * <br/>
 * 作成日: 2005/07/24
 * 
 * @author m.matsubara
 */

public class Global__write extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("write");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Global__write() throws ExpException {
        super("write", 1);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        int max = arguments.length;
        ITypeConverter typeConverter = runtime.getTypeConverter();
        for (int idx = 0; idx < max; idx++) {
            if (arguments[idx] != null) {
                Object object = typeConverter.toString(runtime, arguments[idx]);
                runtime.getStdout().print(object);
            } else {
                runtime.getStdout().print("(null)");
            }
        }
        return Expresser.UNDEFINED;
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Global__write();
        return instance;
    }
}
