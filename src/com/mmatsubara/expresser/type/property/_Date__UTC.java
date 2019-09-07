package com.mmatsubara.expresser.type.property;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Double;


/**
 * Dateオブジェクトの UTC() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/05/08
 * 
 * @author m.matsubara
 */

public class _Date__UTC extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("UTC");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private _Date__UTC() throws ExpException {
        super("UTC", 7);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        //  パラメータの解析
        int year = 1900;
        try {
            if (arguments.length >= 1)
                year = ((ExpNumberBase)(arguments[0])).intValue();
        } catch (ClassCastException cce) {
            year = runtime.getTypeConverter().toNumber(runtime, arguments[0]).intValue();
        }
        int month = 0;
        try {
            if (arguments.length >= 2)
                month = ((ExpNumberBase)(arguments[1])).intValue();
        } catch (ClassCastException cce) {
            month = runtime.getTypeConverter().toNumber(runtime, arguments[0]).intValue();
        }
        int day = 1;
        try {
            if (arguments.length >= 3)
                day = ((ExpNumberBase)(arguments[2])).intValue();
        } catch (ClassCastException cce) {
            day = runtime.getTypeConverter().toNumber(runtime, arguments[2]).intValue();
        }
        int hours = 0;
        try {
            if (arguments.length >= 4)
                hours = ((ExpNumberBase)(arguments[3])).intValue();
        } catch (ClassCastException cce) {
            hours = runtime.getTypeConverter().toNumber(runtime, arguments[3]).intValue();
        }
        int minutes = 0;
        try {
            if (arguments.length >= 5)
                minutes = ((ExpNumberBase)(arguments[4])).intValue();
        } catch (ClassCastException cce) {
            minutes = runtime.getTypeConverter().toNumber(runtime, arguments[4]).intValue();
        }
        int seconds = 0;
        try {
            if (arguments.length >= 6)
                seconds = ((ExpNumberBase)(arguments[5])).intValue();
        } catch (ClassCastException cce) {
            seconds = runtime.getTypeConverter().toNumber(runtime, arguments[5]).intValue();
        }
        int milliSeconds = 0;
        try {
            if (arguments.length >= 7)
                milliSeconds = ((ExpNumberBase)(arguments[6])).intValue();
        } catch (ClassCastException cce) {
            milliSeconds = runtime.getTypeConverter().toNumber(runtime, arguments[6]).intValue();
        }

        //  グレゴリオ暦でカレンダーオブジェクトを作成(GMT)
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        cal.set(Calendar.YEAR,         year);
        cal.set(Calendar.MONTH,        month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY,  hours);
        cal.set(Calendar.MINUTE,       minutes);
        cal.set(Calendar.SECOND,       seconds);
        cal.set(Calendar.MILLISECOND,  milliSeconds);
        
        
        //  戻り値にエポックからの経過ミリ秒で時刻を設定する
        return new _Double(cal.getTime().getTime() + milliSeconds);
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new _Date__UTC();
        return instance;
    }
}
