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
public class Number__toExponential extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("toExponential");
    private static IExpObject instance = null;  

    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Number__toExponential() throws ExpException {
        super("toExponential", 0);
    }
    
    /**
     * 数値を指数形式で表現した文字列を返す
     * @param number 対象の数値
     * @param fractionDigits 小数点以下の桁数
     * @return number の文字列表現
     */
    public static String toExponential(double number, int fractionDigits, RoundingMode roundingMode) {
		if (Double.isNaN(number))
			return "NaN";
		else if (Double.isInfinite(number)) {
			if (number > 0)
				return "Infinity";
			else
				return "-Infinity";
		}
		return toExponential(new BigDecimal(number), fractionDigits, roundingMode);
/*    	
        DecimalFormat df = new DecimalFormat();
        String format = "0.";
        for (int idx = 0; idx < fractionDigits; idx++)
        	format = format + "0";
        format = format + "E0";
        df.applyPattern(format);
        df.setMaximumFractionDigits(fractionDigits);
        df.setMinimumFractionDigits(fractionDigits);        
        String result = df.format(number);
        
        if (result.indexOf("E-") >= 0)
        	result = result.replace("E-", "e-");
        else if (result.indexOf("E") >= 0)
        	result = result.replace("E", "e+"); 
        
        return result;
*/
    }
    
    /**
     * 数値を指数形式で表現した文字列を返す
     * @param number 対象の数値
     * @param fractionDigits 小数点以下の桁数
     * @return number の文字列表現
     */
    public static String toExponential(BigDecimal number, int fractionDigits, RoundingMode roundingMode) {
    	DecimalFormat df = new DecimalFormat();
        //	formatで小数点以下の桁数を指定しても何故か全桁で返してきてしまう（Java or Dolvikの不具合？）
        String format = "0.0";
        for (int idx = 0; idx < fractionDigits; idx++)
        	format = format + "0";
        format = format + "E0";
        df.applyPattern(format);
//        df.setRoundingMode(roundingMode);
        df.setMaximumFractionDigits(fractionDigits);
        df.setMinimumFractionDigits(fractionDigits);        
        String result = df.format(number);

        //	指数部と仮数部に分ける
        String exp = "";
    	int pos = result.indexOf("E");
    	if (pos >= 0) {
    		exp = result.substring(pos);
    		result = result.substring(0, pos);
    	}

        format = "0.";
        for (int idx = 0; idx < fractionDigits; idx++)
        	format = format + "0";
        df.applyPattern(format);
    	
		number = new BigDecimal(result);
		number = number.setScale(fractionDigits, roundingMode);
		result = df.format(number) + exp;
		number = new BigDecimal(result);
        
		//	指数部分のフォーマットをECMAScriptの仕様に合わせる
        if (result.indexOf("E-") >= 0)
        	result = result.replace("E-", "e-");
        else if (result.indexOf("E") >= 0)
        	result = result.replace("E", "e+"); 
        
        return result;
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
    		return new _String(toExponential(bd, fractionDigits, RoundingMode.HALF_UP));
    	} else {
    		double number = ((ExpNumberBase)thisObject).doubleValue();
    		return new _String(toExponential(number, fractionDigits, RoundingMode.HALF_UP));
    	}
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Number__toExponential();
        return instance;
    }
}
