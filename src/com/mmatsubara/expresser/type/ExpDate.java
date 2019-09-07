package com.mmatsubara.expresser.type;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.property.Date__getDate;
import com.mmatsubara.expresser.type.property.Date__getDay;
import com.mmatsubara.expresser.type.property.Date__getFullYear;
import com.mmatsubara.expresser.type.property.Date__getHours;
import com.mmatsubara.expresser.type.property.Date__getMilliseconds;
import com.mmatsubara.expresser.type.property.Date__getMinutes;
import com.mmatsubara.expresser.type.property.Date__getMonth;
import com.mmatsubara.expresser.type.property.Date__getSeconds;
import com.mmatsubara.expresser.type.property.Date__getTime;
import com.mmatsubara.expresser.type.property.Date__getTimezoneOffset;
import com.mmatsubara.expresser.type.property.Date__setDate;
import com.mmatsubara.expresser.type.property.Date__setFullYear;
import com.mmatsubara.expresser.type.property.Date__setHours;
import com.mmatsubara.expresser.type.property.Date__setMilliseconds;
import com.mmatsubara.expresser.type.property.Date__setMinutes;
import com.mmatsubara.expresser.type.property.Date__setMonth;
import com.mmatsubara.expresser.type.property.Date__setSeconds;
import com.mmatsubara.expresser.type.property.Date__setTime;
import com.mmatsubara.expresser.type.property.Date__toDateString;
import com.mmatsubara.expresser.type.property.Date__toLocaleDateString;
import com.mmatsubara.expresser.type.property.Date__toLocaleString;
import com.mmatsubara.expresser.type.property.Date__toLocaleTimeString;
import com.mmatsubara.expresser.type.property.Date__toTimeString;
import com.mmatsubara.expresser.type.property.Date__toUTCString;
import com.mmatsubara.expresser.type.property.Primitive__valueOf;


/**
 * 日付保持クラスです。<br/>
 * <br/>
 * 作成日: 2005/05/01
 * 
 * @author m.matsubara
 */
public class ExpDate extends ExpObject {
    public static IExpObject constructor = null; 

    public static Locale DEFAULT_FORMAT_LOCALE = Locale.US;    //  really ?
    public static String DEFAULT_DATE_FORMAT = "E MMM dd yyyy";
    public static String DEFAULT_TIME_FORMAT = "HH:mm:ss 'GMT'Z '('z')'";
    public static String DEFAULT_DATE_TIME_FORMAT = DEFAULT_DATE_FORMAT + " " + DEFAULT_TIME_FORMAT;
    public static String UTC_DATE_TIME_FORMAT = "E, dd MMM yyyy HH:mm:ss z";
    
    
    private Date date = new Date();
    private boolean nanFlag = false;

    private static ExpObject prototype = null;
    /**  toString() 用のフォーマット */
    private static DateFormat formatter = null;
    /**  parse() 用のフォーマットリスト*/
    private static DateFormat[] formatterArray = null;
    
    public ExpDate() throws ExpException {
        super("Date", constructor);

        setThisPrototype(getPrototype());
    }
    
    public ExpDate(long time) throws ExpException {
        this();
        date.setTime(time);
    }
    
    /**
     * java.util.Dateを返します。
     * @return java.util.Date
     */
    public Date getDate() {
        return this.date;
    }
    
    public String toString() {
        if (nanFlag)
            return String.valueOf(Expresser.NaN);
        else {
            if (formatter == null) 
                formatter = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT, DEFAULT_FORMAT_LOCALE);
            return formatter.format(date);
        }
    }
    
    /**
     * 値が NaN の時trueとなります。 
     * @return
     */
    public boolean isNaN() {
        return nanFlag;
    }

    /**
     * NaNの状態を設定します。
     * @param nanFlag
     */
    public void setNaN(boolean nanFlag) {
        this.nanFlag = nanFlag;
    }
    
    /**
     * 文字列から日付型への変換をします。
     * @param source
     * @return 
     */
    public static Date parse(String source) {
        //  フォーマットの列挙
        if (formatterArray == null) {
            //  synchronized はしなくて良いかな…
            formatterArray = new DateFormat[] {
                    new SimpleDateFormat(ExpDate.DEFAULT_DATE_TIME_FORMAT, ExpDate.DEFAULT_FORMAT_LOCALE),  
                    new SimpleDateFormat(ExpDate.DEFAULT_DATE_FORMAT, ExpDate.DEFAULT_FORMAT_LOCALE),   
                    new SimpleDateFormat(ExpDate.DEFAULT_TIME_FORMAT, ExpDate.DEFAULT_FORMAT_LOCALE),
                    new SimpleDateFormat(ExpDate.UTC_DATE_TIME_FORMAT, ExpDate.DEFAULT_FORMAT_LOCALE),              
                    DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM),    
                    DateFormat.getDateInstance(DateFormat.SHORT),   
                    DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM), 
                    DateFormat.getDateInstance(DateFormat.LONG),    
            };
        }
        
        //  様々なフォーマットを試してみる
        Date date = null;
        for (int idx = 0; idx < formatterArray.length; idx++) {
            DateFormat formatter = formatterArray[idx];
            try {
                date = formatter.parse(source);
                return date;
            } catch (ParseException pe) {
                //  次のフォーマットへ
            }
        }
        return null;    //  すべてのフォーマットがダメだった
        
    }
    
    /**
     * prototypeを取得します。
     * @return prototype 
     * @throws ExpException
     */
    public static synchronized ExpObject getPrototype() throws ExpException {
        if (prototype == null) {
            prototype = new ExpObject(ExpObject.getPrototype());
            prototype.putProperty(Primitive__valueOf.ID,                Date__getTime.getInstance());
            prototype.putProperty(Date__getDate.ID,                     Date__getDate.getInstance());
            prototype.putProperty(Date__getDay.ID,                      Date__getDay.getInstance());
            prototype.putProperty(Date__getFullYear.ID,                 Date__getFullYear.getInstance());
            prototype.putProperty(Date__getHours.ID,                    Date__getHours.getInstance());
            prototype.putProperty(Date__getMilliseconds.ID,             Date__getMilliseconds.getInstance());
            prototype.putProperty(Date__getMinutes.ID,                  Date__getMinutes.getInstance());
            prototype.putProperty(Date__getMonth.ID,                    Date__getMonth.getInstance());
            prototype.putProperty(Date__getSeconds.ID,                  Date__getSeconds.getInstance());
            prototype.putProperty(Date__getTime.ID,                     Date__getTime.getInstance());
            prototype.putProperty(Date__getTimezoneOffset.ID,           Date__getTimezoneOffset.getInstance());
            prototype.putProperty(StringToId.toId("getUTCDate"),        Date__getDate.getInstance(TimeZone.getTimeZone("GMT")));
            prototype.putProperty(StringToId.toId("getUTCDay"),         Date__getDay.getInstance(TimeZone.getTimeZone("GMT")));
            prototype.putProperty(StringToId.toId("getUTCFullYear"),    Date__getFullYear.getInstance(TimeZone.getTimeZone("GMT")));
            prototype.putProperty(StringToId.toId("getUTCHours"),       Date__getHours.getInstance(TimeZone.getTimeZone("GMT")));
            prototype.putProperty(StringToId.toId("getUTCMilliseconds"),Date__getMilliseconds.getInstance(TimeZone.getTimeZone("GMT")));
            prototype.putProperty(StringToId.toId("getUTCMinutes"),     Date__getMinutes.getInstance(TimeZone.getTimeZone("GMT")));
            prototype.putProperty(StringToId.toId("getUTCMonth"),       Date__getMonth.getInstance(TimeZone.getTimeZone("GMT")));
            prototype.putProperty(StringToId.toId("getUTCSeconds"),     Date__getSeconds.getInstance(TimeZone.getTimeZone("GMT")));
            prototype.putProperty(StringToId.toId("getYear"),           Date__getFullYear.getInstance());
            prototype.putProperty(Date__setDate.ID,                     Date__setDate.getInstance());
            prototype.putProperty(Date__setFullYear.ID,                 Date__setFullYear.getInstance());
            prototype.putProperty(Date__setHours.ID,                    Date__setHours.getInstance());
            prototype.putProperty(Date__setMilliseconds.ID,             Date__setMilliseconds.getInstance());
            prototype.putProperty(Date__setMinutes.ID,                  Date__setMinutes.getInstance());
            prototype.putProperty(Date__setMonth.ID,                    Date__setMonth.getInstance());
            prototype.putProperty(Date__setSeconds.ID,                  Date__setSeconds.getInstance());
            prototype.putProperty(Date__setTime.ID,                     Date__setTime.getInstance());
            prototype.putProperty(StringToId.toId("setUTCDate"),        Date__setDate.getInstance(TimeZone.getTimeZone("GMT")));
            prototype.putProperty(StringToId.toId("setUTCFullYear"),    Date__setFullYear.getInstance(TimeZone.getTimeZone("GMT")));
            prototype.putProperty(StringToId.toId("setUTCHours"),       Date__setHours.getInstance(TimeZone.getTimeZone("GMT")));
            prototype.putProperty(StringToId.toId("setUTCMilliseconds"),Date__setMilliseconds.getInstance(TimeZone.getTimeZone("GMT")));
            prototype.putProperty(StringToId.toId("setUTCMinutes"),     Date__setMinutes.getInstance(TimeZone.getTimeZone("GMT")));
            prototype.putProperty(StringToId.toId("setUTCMonth"),       Date__setMonth.getInstance(TimeZone.getTimeZone("GMT")));
            prototype.putProperty(StringToId.toId("setUTCSeconds"),     Date__setSeconds.getInstance(TimeZone.getTimeZone("GMT")));
            prototype.putProperty(StringToId.toId("setYear"),           Date__setFullYear.getInstance());
            prototype.putProperty(Date__toDateString.ID,                Date__toDateString.getInstance());
            prototype.putProperty(StringToId.toId("toGMTString"),       Date__toUTCString.getInstance());
            prototype.putProperty(Date__toLocaleDateString.ID,          Date__toLocaleDateString.getInstance());
            prototype.putProperty(Date__toLocaleTimeString.ID,          Date__toLocaleTimeString.getInstance());
            prototype.putProperty(Date__toLocaleString.ID,              Date__toLocaleString.getInstance());
            prototype.putProperty(Date__toTimeString.ID,                Date__toTimeString.getInstance());
            prototype.putProperty(Date__toUTCString.ID,                 Date__toUTCString.getInstance());
        }
        return prototype;
    }
}
