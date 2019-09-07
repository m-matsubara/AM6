package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.ITypeConverter;
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
 * 作成日: 2005/04/03
 * 
 * @author m.matsubara
 */

public class String__replace extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("replace");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private String__replace() throws ExpException {
        super("replace", 2);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        String value = ((IExpObject)thisObject).toString();
        if (arguments.length < 2)
            throw new ExpException(Expresser.getResString("ArgumentsNumberError"), -1);

        //  TODO ExpRegExp に global プロパティを追加したらその処理も記述すること
        if (arguments[0] instanceof ExpRegExp) {
            ExpRegExp regExp = null;
            regExp = (ExpRegExp)(arguments[0]);
            ITypeConverter typeConverter = runtime.getTypeConverter();
            String result = regExp.replace(value, typeConverter.toString(runtime, arguments[1]).toString());
            return new _String(result);
        } else if (arguments[0] instanceof _String) {
            String before = arguments[0].toString(); 
            String after = arguments[1].toString();
            int begin = value.indexOf(before);
            if (begin < 0)
                return new _String(value);
            return new _String(value.substring(0, begin) + after + value.substring(begin + before.length())); 
        } else {
            throw new ExpException(Expresser.getResString("ArgumentError"), -1);
        }
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new String__replace();
        return instance;
    }
}
