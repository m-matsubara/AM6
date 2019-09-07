package com.mmatsubara.expresser.type;

import java.util.ArrayList;
import java.util.List;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.Variable;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.property.Number__byteValue;
import com.mmatsubara.expresser.type.property.Number__doubleValue;
import com.mmatsubara.expresser.type.property.Number__floatValue;
import com.mmatsubara.expresser.type.property.Number__intValue;
import com.mmatsubara.expresser.type.property.Number__longValue;
import com.mmatsubara.expresser.type.property.Number__shortValue;
import com.mmatsubara.expresser.type.property.Number__toExponential;
import com.mmatsubara.expresser.type.property.Number__toFixed;
import com.mmatsubara.expresser.type.property.Number__toPrecision;


/**
 * プリミティブ型の倍精度実数型です。<br/>
 * <br/>
 * 作成日: 2005/01/16
 * 
 * @author m.matsubara
 *
 */
public class _Double extends ExpNumberBase implements IExpObject {
    private double value;
    private IExpObject _description = Expresser.UNDEFINED;	//	独自拡張

    private static ExpObject prototype = null;
    
    public _Double(double value) throws ExpException {
        this.value = value;
        getPrototype();
    }
    
    public byte byteValue() {
        return (byte)value;
    }
    
    public short shortValue() {
        return (short)value;
    }
    
    public int intValue() {
        return (int)value;
    }
    
    public long longValue() {
        return (long)value;
    }

    public float floatValue() {
        return (float)value;
    }

    public double doubleValue() {
        return value;
    }
    
    public String toString() {
        if (value >= Long.MIN_VALUE && value <= Long.MAX_VALUE && value % 1.0 == 0.0) {
            return String.valueOf((long)value);
        } else {
            return String.valueOf(value);
        }
    }
    
    public boolean equals(Object object) {
        if (object instanceof ExpNumberBase) {
            return value == ((ExpNumberBase)object).doubleValue();
        } else {
            return false;
        }
    }
    
    public int hashCode() {
        return (int)value;
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#deleteProperty(java.lang.Integer)
     */
    public IExpObject deleteProperty(Integer id) throws ExpException {
    	if (StringToId.toId("_description") == id)
    		_description = Expresser.UNDEFINED;
        return new _Boolean(true);
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getProperty(java.lang.Integer)
     */
    public IExpObject getProperty(Integer id) throws ExpException {
    	if (StringToId.toId("_description") == id)
    		return _description;
        return prototype.getProperty(id);
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#putProperty(java.lang.Integer, java.lang.Object)
     */
    public void putProperty(Integer id, IExpObject value) throws ExpException {
    	if (StringToId.toId("_description") == id)
    		_description = value;
        // 何もしない
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getProperty(int)
     */
    public IExpObject getProperty(int index) throws ExpException {
        return Expresser.UNDEFINED;
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#putProperty(int, java.lang.Integer)
     */
    public void putProperty(int index, IExpObject value) throws ExpException {
        // 何もしない
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getVariable(java.lang.Integer)
     */
    public IVariable getVariable(Integer id) {
        return new ExpObjectMember(this, id);
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getVariable(int)
     */
    public IVariable getVariable(int index) {
        return new Variable(Expresser.UNDEFINED);  //   アクセスしてもエラーにならないが、値は常にundefinedとなる  
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#isProperty(java.lang.Integer, boolean)
     */
    public boolean isProperty(Integer id, boolean searchPrototype) throws ExpException {
        if (searchPrototype)
            return prototype.isProperty(id, searchPrototype);
        else
            return false;
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getConstructorList()
     */
    public List getConstructorList() {
        return new ArrayList(0);
    }

    /* (non-Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#enumProperty()
     */
    public Integer[] enumProperty() {
        return new Integer[0];
    }

    /**
     * prototypeを取得します。
     * @return prototype 
     * @throws ExpException
     */
    public static synchronized ExpObject getPrototype() throws ExpException {
        if (prototype == null) {
            prototype = new ExpObject(ExpObject.getPrototype());
            prototype.putProperty(Number__byteValue.ID,     Number__byteValue.getInstance());
            prototype.putProperty(Number__shortValue.ID,    Number__shortValue.getInstance());
            prototype.putProperty(Number__intValue.ID,      Number__intValue.getInstance());
            prototype.putProperty(Number__longValue.ID,     Number__longValue.getInstance());
            prototype.putProperty(Number__floatValue.ID,    Number__floatValue.getInstance());
            prototype.putProperty(Number__doubleValue.ID,   Number__doubleValue.getInstance());
            prototype.putProperty(Number__toExponential.ID, Number__toExponential.getInstance());
            prototype.putProperty(Number__toFixed.ID,       Number__toFixed.getInstance());
            prototype.putProperty(Number__toPrecision.ID,   Number__toPrecision.getInstance());
        }
        return prototype;
    }
}
