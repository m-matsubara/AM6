package com.mmatsubara.expresser.type.property;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._String;


/**
 * Global型の decodeURI() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/07/30
 * 
 * @author m.matsubara
 */

public class Global__decodeURI extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("decodeURI");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Global__decodeURI() throws ExpException {
        super("decodeURI", 1);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        if (arguments.length < 1)
            throw new ExpException(Expresser.getResString("ArgumentsNumberError"), -1);
        
        String uri = runtime.getTypeConverter().toString(runtime, arguments[0]).toString();
        
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        int idx = 0;
        int max = uri.length();
        while (idx < max) {
            char ch = uri.charAt(idx);
            if (ch == '%') {
                if (idx + 2 >= max)
                    throw new ExpException("URIError", -1); //  TODO URIErrorを通知するべき
                int hex = hex(uri.charAt(idx + 1)) * 16 + hex(uri.charAt(idx + 2));
                try {
                    byteArray.write(new byte[] { (byte)hex });
                } catch (IOException ioe) {
                    throw new ExpException("URIError", -1); // TODO URIError を通知するべき
                }
                idx += 3;
            } else {
                try {
                    byteArray.write(String.valueOf(ch).getBytes("UTF-8"));
                } catch (Exception e) {
                    throw new ExpException("URIError", -1); // TODO URIError を通知するべき
                }
                idx++;
            }
        }
        try {
            return new _String(new String(byteArray.toByteArray(), "UTF-8"));
        } catch (UnsupportedEncodingException uee) {
            //  ありえない
            return new _String("");
        }
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Global__decodeURI();
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
