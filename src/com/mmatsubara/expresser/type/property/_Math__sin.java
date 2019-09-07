package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Double;

/**
 * Mathオブジェクトの sin() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/05/08
 * 
 * @author m.matsubara
 */

public class _Math__sin extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("sin");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private _Math__sin() throws ExpException {
        super("sin", 1);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        if (arguments.length < 1)
            return new _Double(Double.NaN);
        double number;
        try {
            number = ((ExpNumberBase)(arguments[0])).doubleValue();
        } catch (ClassCastException cce) {
            number = runtime.getTypeConverter().toNumber(runtime, arguments[0]).doubleValue();
        }
        return new _Double(sin(number));
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new _Math__sin();
        return instance;
    }
    
    /**
     * sin関数（三角関数）です。
     * @param number 角度（ラジアン）
     * @return 計算結果
     */
    public static double sin(double number) {
        //  Javaの三角関数は PI / 2 を超えると誤差が発生するみたい（？）なので 0 ～ PI / 2 の範囲内に変換して計算
        number %= (2 * Math.PI);
        if (number < 0)
            number += (2 * Math.PI);
        if (number < Math.PI * 0.5)
            return Math.sin(number);
        else if (number < Math.PI)
            return Math.sin(Math.PI - number);
        else if (number < Math.PI * 1.5)
            return Math.sin(number - Math.PI) * -1.0;
        else
            return Math.sin(2 * Math.PI - number) * -1.0;
    }
}
