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
 * Global型の parseInt() メソッドです。<br/>
 * このメソッドはECMA262規格にはありません<br/>
 * <br/>
 * 作成日: 2005/07/24
 * 
 * @author m.matsubara
 */

public class Global__parseInt extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("parseInt");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Global__parseInt() throws ExpException {
        super("parseInt", 1);
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
        //  符号判定
        boolean minusFlag = false;
        if (string.charAt(0) == '-') {
            minusFlag = true;
            string = string.substring(1);
        }
        if (string.length() == 0)
            return new _Double(Double.NaN);

        //  基数を判定
        if (string.charAt(0) == '0') {
            if (string.length() == 1)
                return new _Double(0);
            if (string.length() >= 2 && (string.charAt(1) == 'x' || string.charAt(1) == 'X')) {
                if (radix != -1 && radix != 16)
                    return new _Double(0);  //  NaNではないの？ (rhinoにあわせている)
                radix = 16;
                if (string.length() == 2)
                    return new _Double(Double.NaN);
                string = string.substring(2);
            } else { 
                radix = 8;
            }
        }
        if (radix == -1) {
            radix = 10;
        }
        
        //  数字のデコード
        int result = 0;
        int max = string.length();
        if (max == 0)
            return new _Double(Double.NaN);
        for (int idx = 0; idx < max; idx++) {
            int val;
            char ch = string.charAt(idx);
            if (ch >= '0' && ch <= '9')
                val = (ch - '0');
            else if (ch >= 'a' && ch <= 'z')
                val = (10 + (ch - 'a'));
            else if (ch >= 'A' && ch <= 'Z')
                val = (10 + (ch - 'A'));
            else
                break;
            if (val >= radix)
                return new _Double(Double.NaN);
            result *= radix;
            result += val;
        }
        
        if (minusFlag)
            return new _Double(result * -1);
        else
            return new _Double(result);
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Global__parseInt();
        return instance;
    }
}
