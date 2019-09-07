package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpDate;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Double;

/**
 * Date型の getTime() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/05/01
 * 
 * @author m.matsubara
 */

public class Date__getTime extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("getTime");
    private static IExpObject instance; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Date__getTime() throws ExpException {
        super("getTime", 0);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        
        ExpDate date = (ExpDate)thisObject;
        if (date.isNaN())
            return new _Double(Expresser.NaN);
        return new _Double(date.getDate().getTime());
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Date__getTime();
        return instance;
    }
}
