package com.mmatsubara.expresser.type.property;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpDate;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Double;


/**
 * Date型の setMonth() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/06/04
 * 
 * @author m.matsubara
 */

public class Date__setMonth extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("setMonth");
    private static IExpObject instance; 
    private TimeZone timeZone = null;
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Date__setMonth() throws ExpException {
        super("setMonth", 2);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        
        ExpDate date = (ExpDate)thisObject;
        if (arguments.length < 1)
            date.setNaN(true);
        if (date.isNaN())
            return new _Double(Expresser.NaN);
        
        //  引数の解析
        int month = -1;
        try {
            if (arguments.length >= 1)
                month = ((ExpNumberBase)(arguments[0])).intValue();
        } catch (ClassCastException cce) {
            month = runtime.getTypeConverter().toNumber(runtime, arguments[0]).intValue();
        }
        int day = -1;
        try {
            if (arguments.length >= 2)
                day = ((ExpNumberBase)(arguments[1])).intValue();
        } catch (ClassCastException cce) {
            day = runtime.getTypeConverter().toNumber(runtime, arguments[1]).intValue();
        }
        
        Calendar calendar = new GregorianCalendar();
        if (timeZone != null)
            calendar.setTimeZone(timeZone);
        calendar.setTime(date.getDate());
        calendar.set(Calendar.MONTH, month);
        if (arguments.length >= 2)
            calendar.set(Calendar.DAY_OF_MONTH, day);
        long result = calendar.getTime().getTime(); 
        date.getDate().setTime(result);
        
        return new _Double(result);
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Date__setMonth();
        return instance;
    }

    /**
     * デフォルト以外のタイムゾーンを持つインスタンスを作成します。<br/>
     * シングルトンパターンにはなりません
     * @param timeZone タイムゾーン
     * @return IExpObject 型のインスタンス
     * @throws ExpException
     */
    public static IExpObject getInstance(TimeZone timeZone) throws ExpException {
        Date__setMonth instance = new Date__setMonth();
        instance.timeZone = timeZone;
        return instance;
    }
}
