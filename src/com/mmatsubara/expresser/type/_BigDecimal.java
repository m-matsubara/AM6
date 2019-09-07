package com.mmatsubara.expresser.type;

import java.math.BigDecimal;
import java.math.MathContext;

import com.mmatsubara.expresser.exception.ExpException;

public class _BigDecimal extends _Double {
	BigDecimal bd; 
	
	public _BigDecimal(BigDecimal bd) throws ExpException {
		super (bd.doubleValue());
		this.bd = bd;
	}

	public _BigDecimal(String str) throws ExpException {
		super (Double.parseDouble(str));
		this.bd = new BigDecimal(str, MathContext.DECIMAL128);
	}

	public _BigDecimal(double d) throws ExpException {
		super (d);
		if ((Double.isNaN(d) == false) && (Double.isInfinite(d) == false)) {
			String str = String.valueOf(d);
			this.bd = new BigDecimal(str);
		} else {
			this.bd = null;
		}
	}
	
    public byte byteValue() {
    	if (bd != null) 
    		return bd.byteValue();
    	else
    		return super.byteValue();
    }
    
    public short shortValue() {
    	if (bd != null) 
    		return bd.shortValue();
    	else
    		return super.shortValue();
    }
    
    public int intValue() {
    	if (bd != null) 
    		return bd.intValue();
    	else
    		return super.intValue();
    }
    
    public long longValue() {
    	if (bd != null) 
    		return bd.longValue();
    	else
    		return super.longValue();
    }

    public float floatValue() {
    	if (bd != null) 
    		return bd.floatValue();
    	else
    		return super.floatValue();
    }

    public double doubleValue() {
    	if (bd != null) 
    		return bd.doubleValue();
    	else
    		return super.doubleValue();
    }
    
    public BigDecimal bigDecimalValue() {
    	return bd;
    }
    
    public String toString() {
    	if (bd != null) {
    		if (BigDecimal.ZERO.compareTo(bd) == 0)
    			return "0";		//	BigDecimalのtoString()は 0E+33 とか訳分らん値を返すことがあるのでその対処
    		else
    			return bd.toString();
    	} else {
    		return super.toString();
    	}
    }
    
    public boolean equals(Object object) {
    	if (bd != null) {
	    	if (object instanceof _BigDecimal) {
	    		return bd.equals(((_BigDecimal)object).bigDecimalValue());
	    	} else if (object instanceof ExpNumberBase) {
	            return bd.doubleValue() == ((ExpNumberBase)object).doubleValue();
	        } else {
	            return false;
	        }
    	} else {
    		return super.equals(object);
    	}
    }
    
    public int hashCode() {
    	if (bd != null)
    		return bd.hashCode();
    	else
    		return super.hashCode();
    }
}
