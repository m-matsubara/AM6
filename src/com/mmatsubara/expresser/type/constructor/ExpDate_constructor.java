package com.mmatsubara.expresser.type.constructor;

import java.util.Date;
import java.util.GregorianCalendar;

import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpDate;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._String;
import com.mmatsubara.expresser.type.property._Date__UTC;
import com.mmatsubara.expresser.type.property._Date__parse;


/**
 * Date型のコンストラクタです。<br/>
 * <br/>
 * 作成日: 2005/05/01
 * 
 * @author m.matsubara
 */

public class ExpDate_constructor extends ExpFunctionBase {
    /**
     * Date型のコンストラクタを生成します。 
     * @throws ExpException
     */
    public ExpDate_constructor() throws ExpException {
        super("Date", 7);
        putProperty(_Date__UTC.ID,   _Date__UTC.getInstance());
        putProperty(_Date__parse.ID, _Date__parse.getInstance());
        putProperty(StringToId.toId("prototype"), ExpDate.getPrototype());
    }


    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        if (arguments.length == 1) {
            ExpDate date = new ExpDate();
            if (arguments[0] instanceof ExpNumberBase) {
                long time = ((ExpNumberBase)arguments[0]).longValue(); 
                date.getDate().setTime(time);
            } else if (arguments[0] instanceof _String) {
                Date dateObj = ExpDate.parse(arguments[0].toString());
                if (dateObj != null)
                    date.getDate().setTime(dateObj.getTime());
                else
                    date.setNaN(true);
            } else if (arguments[0] instanceof ExpDate) {
                ExpDate arg = (ExpDate)arguments[0];
                long time = arg.getDate().getTime();
                date.getDate().setTime(time);
                date.setNaN(arg.isNaN());
            }
            return date;
        } else if (arguments.length >= 2) {
            //  パラメータの解析
            int year;
            try {
                year = ((ExpNumberBase)(arguments[0])).intValue();
            } catch (ClassCastException cce) {
                year = runtime.getTypeConverter().toNumber(runtime, arguments[0]).intValue();
            }
            int month;
            try {
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

            //  グレゴリオ暦でカレンダーオブジェクトを作成
            GregorianCalendar cal = new GregorianCalendar(year, month, day, hours, minutes, seconds);
            
            //  戻り値にエポックからの経過ミリ秒で時刻を設定する
            return new ExpDate(cal.getTime().getTime() + milliSeconds);
            
        } else {
            return new ExpDate();
        }
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getProperty(java.lang.Integer)
     */
    public IExpObject getProperty(Integer id) throws ExpException {
        if (id.intValue() == ExpObject_constructor.propertyId$prototype.intValue())
            return ExpDate.getPrototype();
        IExpObject obj = super.getProperty(id);
       	return obj;
    }
}
