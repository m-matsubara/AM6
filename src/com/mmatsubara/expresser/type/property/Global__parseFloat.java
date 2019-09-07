package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.ITypeConverter;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Double;

/**
 * Global型の parseFloat() メソッドです。<br/>
 * このメソッドはECMA262規格にはありません<br/>
 * <br/>
 * 作成日: 2005/07/24
 * 
 * @author m.matsubara
 */

public class Global__parseFloat extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("parseFloat");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Global__parseFloat() throws ExpException {
        super("parseFloat", 1);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        if (arguments.length < 1)
            throw new ExpException(Expresser.getResString("ArgumentsNumberError"), -1);
        
        ITypeConverter typeConverter = runtime.getTypeConverter(); 
        //  基数を判定
        int radix = -1;
        if (arguments.length >= 2) {
            radix = typeConverter.toNumber(runtime, arguments[1]).intValue();
            if (radix < 0 || radix > 36) {
                return new _Double(Double.NaN);
            }
        }
        String string = typeConverter.toString(runtime, arguments[0]).toString().trim();
        
        return new _Double(Double.parseDouble(string));
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Global__parseFloat();
        return instance;
    }
}
