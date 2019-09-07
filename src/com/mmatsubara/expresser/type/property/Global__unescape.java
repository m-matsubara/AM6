package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._String;

/**
 * Global型の unescape() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/07/30
 * 
 * @author m.matsubara
 */

public class Global__unescape extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("unescape");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Global__unescape() throws ExpException {
        super("unescape", 1);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        if (arguments.length < 1)
            throw new ExpException(Expresser.getResString("ArgumentsNumberError"), -1);
        
        String uri = runtime.getTypeConverter().toString(runtime, arguments[0]).toString();
        
        StringBuffer result = new StringBuffer();
        int idx = 0;
        int max = uri.length();
        while (idx < max) {
            char ch = uri.charAt(idx);
            if (ch == '%') {
                if (idx + 1 >= max)
                    throw new ExpException("URIError", -1);     //  TODO URIErrorを通知するべき
                if (uri.charAt(idx + 1) == 'u') {
                    if (idx + 5 >= max)
                        throw new ExpException("URIError", -1); //  TODO URIErrorを通知するべき
                    ch = (char)(
                        hex(uri.charAt(idx + 2)) * 16 * 16 * 16 +
                        hex(uri.charAt(idx + 3)) * 16 * 16 +
                        hex(uri.charAt(idx + 4)) * 16 +
                        hex(uri.charAt(idx + 5)));
                    result.append((char)ch);
                    idx += 6;
                } else {
                    if (idx + 2 >= max)
                        throw new ExpException("URIError", -1); //  TODO URIErrorを通知するべき
                    int hex = 
                        hex(uri.charAt(idx + 1)) * 16 + 
                        hex(uri.charAt(idx + 2));
                    result.append((char)hex);
                    idx += 3;
                }
            } else {
                try {
                    result.append(ch);
                } catch (Exception e) {
                    throw new ExpException("URIError", -1); // TODO URIError を通知するべき
                }
                idx++;
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
            instance = new Global__unescape();
        return instance;
    }
    
    /**
     * 数値を２桁の１６進数に置き換えます。
     * @param hex
     * @return
     */
    public int hex(char hexChar) throws ExpException {
        if (hexChar >= '0' && hexChar <= '9')
            return hexChar - '0';
        if (hexChar >= 'A' && hexChar <= 'F')
            return hexChar - 'A' + 10;
        if (hexChar >= 'a' && hexChar <= 'f')
            return hexChar - 'a' + 10;
        throw new ExpException("URIError", -1); //  TODO URIErrorを通知するべき
    }
}
