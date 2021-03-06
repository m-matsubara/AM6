package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpDate;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.IExpObject;


/**
 * Date型の getTime() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/05/01
 * 
 * @author m.matsubara
 */

public class Date__setTime extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("setTime");
    private static IExpObject instance; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Date__setTime() throws ExpException {
        super("setTime", 1);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        
        ExpDate date = (ExpDate)thisObject;
        long time;
        if (arguments.length < 1)
            throw new ExpException(Expresser.getResString("ArgumentsNumberError"), -1);
        try {
            time = ((ExpNumberBase)(arguments[0])).longValue();
        } catch (ClassCastException cce) {
            time = runtime.getTypeConverter().toNumber(runtime, arguments[0]).longValue();
        }
        
        date.getDate().setTime(time);
        
        return date;
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Date__setTime();
        return instance;
    }
}
