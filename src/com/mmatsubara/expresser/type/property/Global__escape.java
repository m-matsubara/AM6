package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._String;

/**
 * Global型の escape() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/07/30
 * 
 * @author m.matsubara
 */

public class Global__escape extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("escape");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Global__escape() throws ExpException {
        super("escape", 1);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        if (arguments.length < 1)
            throw new ExpException(Expresser.getResString("ArgumentsNumberError"), -1);
        
        String uri = runtime.getTypeConverter().toString(runtime, arguments[0]).toString();
        StringBuffer result = new StringBuffer(uri.length() * 12 / 10);
        int max = uri.length();
        for (int idx = 0; idx < max; idx++) {
            char ch = uri.charAt(idx);
            if (ch > 256) {
            //  マルチバイト文字
            result.append("%u");
            result.append(Global__encodeURI.toHexString(ch).toUpperCase());
            } else if (Character.isLetterOrDigit(ch) || ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '.' || ch == '_' || ch == '@') {
                //  ASCII の アルファベット・数字・一部の記号
                result.append(ch);
            } else {
                //  その他の記号（ASCII）
                result.append('%');
                result.append(Global__encodeURI.toHexString(ch).toUpperCase());
            }
        }
        return new _String(result.toString());
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Global__escape();
        return instance;
    }
}
