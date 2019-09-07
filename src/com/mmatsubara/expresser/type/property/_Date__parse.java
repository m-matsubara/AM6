package com.mmatsubara.expresser.type.property;

import java.util.Date;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpDate;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Double;


/**
 * Dateオブジェクトの parse() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/06/03
 * 
 * @author m.matsubara
 */

public class _Date__parse extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("parse");
    private static IExpObject instance = null;
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private _Date__parse() throws ExpException {
        super("parse", 1);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        if (arguments.length < 1)
            return new _Double(Expresser.NaN);

        //  パラメータの解析
        String source = arguments[0].toString();
        
        Date date = ExpDate.parse(source);
        if (date != null)
            return new _Double(date.getTime());
        else
            return new _Double(Expresser.NaN);
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new _Date__parse();
        return instance;
    }
}
