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
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Double;


/**
 * Date型の getHours() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/06/04
 * 
 * @author m.matsubara
 */

public class Date__getHours extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("getHours");
    private static IExpObject instance; 
    private TimeZone timeZone = null;
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Date__getHours() throws ExpException {
        super("getHours", 0);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        
        ExpDate date = (ExpDate)thisObject;
        if (date.isNaN())
            return new _Double(Expresser.NaN);
        Calendar calendar = new GregorianCalendar();
        if (timeZone != null)
            calendar.setTimeZone(timeZone);
        calendar.setTime(date.getDate());
        
        return new _Double(calendar.get(Calendar.HOUR_OF_DAY));
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Date__getHours();
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
        Date__getHours instance = new Date__getHours();
        instance.timeZone = timeZone;
        return instance;
    }
}
