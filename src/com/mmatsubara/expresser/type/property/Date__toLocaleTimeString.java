package com.mmatsubara.expresser.type.property;

import java.text.DateFormat;

import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpDate;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._String;


/**
 * Date型の toLocaleTimeString() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/04/03
 * 
 * @author m.matsubara
 */

public class Date__toLocaleTimeString extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("toLocaleTimeString");
    private static IExpObject instance; 
    private static DateFormat formatter = null;
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Date__toLocaleTimeString() throws ExpException {
        super("toLocaleTimeString", 0);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        ExpDate date = (ExpDate)thisObject;
        if (date.isNaN())
            return new _String("Invalid Date");
        if (formatter == null)
            formatter = DateFormat.getTimeInstance(DateFormat.LONG);
        _String result = new _String(formatter.format(date.getDate())); 
        return result;
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Date__toLocaleTimeString();
        return instance;
    }
}
