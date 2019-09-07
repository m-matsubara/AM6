package com.mmatsubara.expresser.type.property;

import java.io.UnsupportedEncodingException;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._String;


/**
 * Global型の encodeURI() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/07/30
 * 
 * @author m.matsubara
 */

public class Global__encodeURIComponent extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("encodeURIComponent");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Global__encodeURIComponent() throws ExpException {
        super("encodeURI", 1);
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
            if (ch > 256 || ch == ' ' || ch == '"' || ch == '%' || ch == '^' ||  
                    ch == '\\' || ch == '|' || ch == '`' || ch == '[' ||  
                    ch == '{' || ch == '}' || ch == ']' || ch == '<' || ch == '>' || 
                    ch == '#' || ch == '$' || ch == '+' || ch == '/' || ch == ':' ||	//	encodeURIComponent のみで処理される（encodeURIでは処理されない） 
                    ch == ';' || ch == '=' || ch == '?' || ch == '@' ) 					//	encodeURIComponent のみで処理される（encodeURIでは処理されない）
            {
                try {
                    byte[] bytes = (String.valueOf(ch)).getBytes("UTF-8");
                    for (int byteIdx = 0; byteIdx < bytes.length; byteIdx++) {
                        int hex = bytes[byteIdx];
                        result.append('%');
                        result.append(toHexString(hex));
                    }
                } catch (UnsupportedEncodingException uee) {
                    //  あり得ない
                }
            } else {
                result.append(ch);
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
            instance = new Global__encodeURIComponent();
        return instance;
    }
    
    /**
     * 数値を２桁又は４桁の１６進数に置き換えます。
     * @param hex
     * @return
     */
    public static String toHexString(int hex) {
        StringBuffer result = new StringBuffer(2);
        char[] hexChar = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        int hi;
        int low;
        if (hex >= 256) {
            hi =  (hex >> 12) & 0x0f;
            low = (hex >>  8) & 0x0f;
            result.append(hexChar[hi]);
            result.append(hexChar[low]);
        }
        hi = (hex >> 4) & 0x0f;
        low = hex & 0x0f;
        result.append(hexChar[hi]);
        result.append(hexChar[low]);
        return result.toString();
    }
}
