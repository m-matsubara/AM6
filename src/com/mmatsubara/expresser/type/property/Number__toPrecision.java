package com.mmatsubara.expresser.type.property;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._BigDecimal;
import com.mmatsubara.expresser.type._String;

/**
 * Number型の toPrecision() メソッドです。<br/>
 * 参照 : http://www2u.biglobe.ne.jp/~oz-07ams/prog/ecma262r3/15-7_Number_Objects.html#section-15.7.4.5
 * <br/>
 * 作成日: 2005/05/04
 * 
 * @author m.matsubara
 */
public class Number__toPrecision extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("toPrecision");
    private static IExpObject instance = null;  

    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Number__toPrecision() throws ExpException {
        super("toPrecision", 1);
    }
    
    public static String toPrecision(double number, int precision, RoundingMode roundingMode) {
        if (number == Expresser.NaN) {
            return "NaN";
        } else if ((Double.POSITIVE_INFINITY == number) || (Double.NEGATIVE_INFINITY == number)) {
        	return String.valueOf(number);
        } else {
        	if (number != 0) {
        		//	0以外
        		
        		String flag = "";
        		if (number < 0) {
        			number = 0 - number;
        			flag = "-";
        		}
        		
        		//	指定桁数の仮数部（の整数）とそれに対応する指数部を得る。（指定桁数で数値丸めするため）
        		//	(123.45).toPrecision(4)なら、仮数部 1234 と指数部 -1 のような
            	double max = Math.pow(10, precision);
            	double min = max / 10;
            	int exp = 0;
	        	while (number > max) {
	        		number /= 10;
	        		exp++;
	        	}
	        	while (number < min) {
	        		number *= 10;
	        		exp--;
	        	}
//	        	指定桁数の数値丸め
	        	if (roundingMode == RoundingMode.HALF_UP)
	        		number = Math.round(number);
	        	else
	        		number = Math.ceil(number);	

	        	if (exp > 0) {
	        		//	指数部が正なら仮数部が10未満になるよう指数部を調整
	            	while (number >= 10) {
	            		number /= 10;
	            		exp++;
	            	}
	        	} else if (exp < 0) {
	        		//	指数部が負
	        		
	        		//	指数部が0または仮数部が10未満になるよう調整
		        	while ((exp < 0) && (number >= 10)) {
		        		number /= 10;
		        		exp++;
		        	}
		        	//	指数部が -7以上(0以下)の場合は、指数部が0になるように調整
		        	if (exp > -7) {
		            	while (exp < 0) {
		            		number /= 10;
		            		exp++;
		            	}
		        	}
	        	}
	        	
	        	DecimalFormat df = new DecimalFormat();
	        	df.applyPattern("0.0000000000000000000000000000000000000");
	        	df.setRoundingMode(roundingMode);
	        	String numberStr = df.format(number);
	        	StringBuffer sb = new StringBuffer();
	        	boolean enableFlg = false;
	        	boolean periodFlg = false;
	        	int count = 0;
	        	for (int idx = 0; idx < numberStr.length() && ((count < precision) || (periodFlg == false)); idx++) {
	        		char ch = numberStr.charAt(idx);
	        		if ((ch >= '1') && (ch <= '9')) {
	        			enableFlg = true;
	        		} else if (ch == '.')
	        			periodFlg = true;
	        		
	        		if ((ch != '.') || (count < precision))
	        			sb.append(ch);
	        		if ((enableFlg) && (ch != '.')) {
	        			count++;
	        		}
	        	}
	        	numberStr = flag + sb.toString();
	        	if (exp > 0)
	        		numberStr = numberStr + "e+" + String.valueOf(exp);
	        	else if (exp < 0)
	        		numberStr = numberStr + "e-" + String.valueOf(-exp);
	            return numberStr;
        	} else {
        		//	0 の処理
        		DecimalFormat df = new DecimalFormat();
	        	df.applyPattern("0.0000000000000000000000000000000000000");
	        	String numberStr = df.format(number);
        	
        		numberStr = numberStr.substring(precision + 1);	//	+ 1 しているのはピリオドの分
                return numberStr;
        	}
        }
    }


    public static String toPrecisionBD(double numberDouble, int precision, RoundingMode roundingMode) {
        if (Double.isNaN(numberDouble)) {
            return "NaN";
        } else if ((Double.isInfinite(numberDouble))) {
        	if (numberDouble > 0)
        		return "Infinity";
        	else
        		return "-Infinity";
        } else {
        	String numberStr = String.valueOf(numberDouble);
        	return toPrecisionBD(new BigDecimal(Double.parseDouble(numberStr)), precision, roundingMode);
        }
    }
    
    public static String toPrecisionBD(BigDecimal numberBd, int precision, RoundingMode roundingMode) {
    	double numberDouble = numberBd.doubleValue();
        if (Double.isNaN(numberDouble)) {
            return "NaN";
        } else if (Double.isInfinite(numberDouble)) {
        	if (numberDouble > 0)
        		return "Infinity";
        	else
        		return "-Infinity";
        } else {
        	if (numberBd.compareTo(BigDecimal.ZERO) != 0) {
        		//	0以外
        		String numberStr;
        		BigDecimal number = numberBd;
        		String flag = "";
        		
        		//	符号を確定
        		if (number.compareTo(BigDecimal.ZERO) < 0) {
        			number = number.negate();
        			flag = "-";
        		}
        		
        		//	指定桁数の仮数部（の整数）とそれに対応する指数部を得る。（指定桁数で数値丸めするため）
        		//	(123.45).toPrecision(4)なら、仮数部 1234 と指数部 -1 のような
            	BigDecimal max = BigDecimal.TEN.pow(precision);
            	BigDecimal min = max.divide(BigDecimal.TEN);
            	int exp = 0;
	        	while (number.compareTo(max) > 0) {
	        		number = number.divide(BigDecimal.TEN);
	        		exp++;
	        	}
	        	while (number.compareTo(min) < 0) {
	        		number = number.multiply(BigDecimal.TEN);
	        		exp--;
	        	}
	        	number = number.setScale(0, roundingMode);

	        	if (exp > 0) {
	        		//	指数部が正なら仮数部が10未満になるよう指数部を調整
	            	while (number.compareTo(BigDecimal.TEN) >= 0) {
	            		number = number.divide(BigDecimal.TEN);
	            		exp++;
	            	}
	        	} else if (exp < 0) {
	        		//	指数部が負
	        		
	        		//	指数部が0または仮数部が10未満になるよう調整
		        	while ((exp < 0) && (number.compareTo(BigDecimal.TEN) >= 0)) {
	            		number = number.divide(BigDecimal.TEN);
		        		exp++;
		        	}
		        	//	指数部が -7以上(0以下)の場合は、指数部が0になるように調整
		        	if (exp > -7) {
		            	while (exp < 0) {
		            		number = number.divide(BigDecimal.TEN);
		            		exp++;
		            	}
		        	}
	        	}

	        	//	仮数部の文字列を生成
	        	DecimalFormat df = new DecimalFormat();
	        	df.applyPattern("0.0000000000000000000000000000000000000000000");
	        	numberStr = df.format(number);
	        	StringBuffer sb = new StringBuffer();
	        	boolean enableFlg = false;
	        	boolean periodFlg = false;
	        	int count = 0;
	        	for (int idx = 0; idx < numberStr.length() && ((count < precision) || (periodFlg == false)); idx++) {
	        		char ch = numberStr.charAt(idx);
	        		if ((ch >= '1') && (ch <= '9')) {
	        			enableFlg = true;
	        		} else if (ch == '.')
	        			periodFlg = true;
	        		
	        		if ((ch != '.') || (count < precision))
	        			sb.append(ch);
	        		if ((enableFlg) && (ch != '.')) {
	        			count++;
	        		}
	        	}
	        	
	        	//	符号と仮数部と指数部を結合
	        	numberStr = flag + sb.toString();
	        	if (exp > 0)
	        		numberStr = numberStr + "e+" + String.valueOf(exp);
	        	else if (exp < 0)
	        		numberStr = numberStr + "e-" + String.valueOf(-exp);
	            return numberStr;
        	} else {
        		//	0 の処理
        		String numberStr = "0.";
        		for (int idx = 0; idx < precision - 1; idx++)
        			numberStr = numberStr + "0";
                return numberStr;
        	}
        }
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        ExpNumberBase numberObj = (ExpNumberBase)thisObject;
    	double number = numberObj.doubleValue();
       
        if (arguments.length == 0) {
        	if (numberObj instanceof _BigDecimal)
        		return new _String(numberObj.toString());
        	else
        		return new _String(String.valueOf(number));
        } else if ((arguments[0]).equals(Expresser.UNDEFINED)) {
        	if (numberObj instanceof _BigDecimal)
        		return new _String(numberObj.toString());
        	else
        		return new _String(String.valueOf(number));
        } else {
            int precision = 0;
        	if (arguments.length > 0) {
    	        try {
    	        	precision = ((ExpNumberBase)(arguments[0])).intValue();
    	        } catch (ClassCastException cce) {
    	        	precision = runtime.getTypeConverter().toNumber(runtime, arguments[0]).intValue();
    	        }
        	}
      		return new _String(toPrecisionBD(number, precision, RoundingMode.HALF_UP));
        }
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Number__toPrecision();
        return instance;
    }
}
