package com.mmatsubara.expresser.type.property;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._BigDecimal;
import com.mmatsubara.expresser.type._String;

/**
 * Number型の intValue() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/05/04
 * 
 * @author m.matsubara
 */
public class Number__toFixed extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("toFixed");
    private static IExpObject instance = null;  

    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Number__toFixed() throws ExpException {
        super("toFixed", 0);
    }
    
    /**
     * 数値を小数点以下の桁数を指定して文字列化する
     * @param number 対象の数値
     * @param fractionDigits 小数点以下の桁数
     * @return number の文字列表現
     */
    public static String toFixed(double number, int fractionDigits, RoundingMode roundingMode) {
		if (Double.isNaN(number))
			return "NaN";
		else if (Double.isInfinite(number)) {
			if (number > 0)
				return "Infinity";
			else
				return "-Infinity";
		}
		return toFixed(new BigDecimal(number), fractionDigits, roundingMode);
/*
		DecimalFormat df = new DecimalFormat();
		df.setRoundingMode(roundingMode);
        df.applyPattern("0");
        df.setMaximumFractionDigits(fractionDigits);
        df.setMinimumFractionDigits(fractionDigits);        
        
        return df.format(number);
*/
    }
    
    /**
     * 数値を小数点以下の桁数を指定して文字列化する
     * @param number 対象の数値
     * @param fractionDigits 小数点以下の桁数
     * @return number の文字列表現
     */
    public static String toFixed(BigDecimal number, int fractionDigits, RoundingMode roundingMode) {
       	number = number.setScale(fractionDigits, roundingMode);
    	
    	DecimalFormat df = new DecimalFormat();
        df.applyPattern("0");
//        df.setRoundingMode(roundingMode);
        df.setMaximumFractionDigits(fractionDigits);
        df.setMinimumFractionDigits(fractionDigits);        
        
        return df.format(number);
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        int fractionDigits = 0;
    	if (arguments.length > 0) {
	        try {
	        	fractionDigits = ((ExpNumberBase)(arguments[0])).intValue();
	        } catch (ClassCastException cce) {
	        	fractionDigits = runtime.getTypeConverter().toNumber(runtime, arguments[0]).intValue();
	        }
    	}
    	
    	if (thisObject instanceof _BigDecimal) {
    		double d = ((_BigDecimal)thisObject).doubleValue();
    		if (Double.isNaN(d))
    			return new _String("NaN");
    		else if (Double.isInfinite(d)) {
    			if (d > 0)
    				return new _String("Infinity");
    			else
    				return new _String("-Infinity");
    		}
    		BigDecimal bd = ((_BigDecimal)thisObject).bigDecimalValue();
    		return new _String(toFixed(bd, fractionDigits, RoundingMode.HALF_UP));
    	} else {
    		double number = ((ExpNumberBase)thisObject).doubleValue();
    		return new _String(toFixed(number, fractionDigits, RoundingMode.HALF_UP));
    	}
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Number__toFixed();
        return instance;
    }
}
