package com.mmatsubara.expresser.type;

import com.mmatsubara.expresser.*;
import com.mmatsubara.expresser.exception.*;
import com.mmatsubara.expresser.type.property.Array__concat;
import com.mmatsubara.expresser.type.property.Array__join;
import com.mmatsubara.expresser.type.property.Array__pop;
import com.mmatsubara.expresser.type.property.Array__push;
import com.mmatsubara.expresser.type.property.Array__reverse;
import com.mmatsubara.expresser.type.property.Array__shift;
import com.mmatsubara.expresser.type.property.Array__sort;
import com.mmatsubara.expresser.type.property.Array__toLocaleString;
import com.mmatsubara.expresser.type.property.Array__toString;
import com.mmatsubara.expresser.type.property.Array__unshift;

/**
 * 配列オブジェクトです。<br/>
 * <br/>
 * 作成日: 2005/01/22
 * 
 * @author m.matsubara
 */
public class ExpArray extends ExpObject implements IArray {
    public static IExpObject constructor = null; 

    private static ExpObject prototype = null;
    
    
    private static final Integer propertyId$length = StringToId.toId("length");
    
    public ExpArray() throws ExpException {
        super("Array", constructor);
        setThisPrototype(getPrototype());
    }

    public ExpArray(int initialSize) throws ExpException {
        super("Array", constructor, initialSize);
        setThisPrototype(getPrototype());
        setLength(initialSize);
    }

    public ExpArray(IExpObject[] objects) throws ExpException {
        this(objects.length);
        int max = objects.length;
        for (int idx = 0; idx < max; idx++)
            putProperty(idx, objects[idx]);
    }

    public ExpArray(IVariable[] variables) throws ExpException {
        this(variables.length);
        int max = variables.length;
        for (int idx = 0; idx < max; idx++)
            setVariable(idx, variables[idx]);
    }

    public ExpArray(IArray array) throws ExpException {
        this();
        setVarArrayLength(array.length());
        int max = getVarArrayLength();
        for (int idx = 0; idx < max; idx++)
            putProperty(idx, array.getProperty(idx));
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IArray#length()
     */
    public int length() {
        return getVarArrayLength();
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IArray#setLength(int)
     */
    public void setLength(int length) throws ExpException {
        setVarArrayLength(length);
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IArray#toArray()
     */
    public IExpObject[] toArray() throws ExpException {
        IExpObject[] result = new IExpObject[getVarArrayLength()];
        for (int idx = 0; idx < result.length; idx++)
            result[idx] = getProperty(idx); 
        return result;
    }
    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getProperty(java.lang.Integer)
     */
    public IExpObject getProperty(Integer id) throws ExpException {
        if (id.intValue() == propertyId$length.intValue())
            return new _Double(getVarArrayLength());
        else
            return super.getProperty(id);
    }
    
    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#isProperty(java.lang.Integer, boolean)
     */
    public boolean isProperty(Integer id, boolean searchPrototype) throws ExpException {
        if (id.intValue() == propertyId$length.intValue())
            return true;
        else
            return super.isProperty(id, searchPrototype);
    }
    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#putProperty(java.lang.Integer, java.lang.Object)
     */
    public void putProperty(Integer id, IExpObject value) throws ExpException {
        if (id.intValue() == propertyId$length.intValue()) {
            int newLength = 0;
            if (value instanceof ExpNumberBase)
                newLength = ((ExpNumberBase)value).intValue();
            else if (value instanceof _String) {
                try {
                    newLength = Integer.parseInt(value.toString());
                } catch (Exception e) {
                    //  無視
                }
            } else if (value instanceof _Boolean) {
                try {
                    if (((_Boolean)value).booleanValue())
                        newLength = 1;
                    else
                        newLength = 0;
                } catch (Exception e) {
                    //  無視
                }
            }
            setVarArrayLength(newLength);
        } else
            super.putProperty(id, value);
    }
    /* (non-Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#enumProperty()
     */
    public Integer[] enumProperty() {
    	Integer[] orgProperties = super.enumProperty();
    	
    	Integer[] properties = new Integer[orgProperties.length + 1];
    	for (int idx = 0; idx < orgProperties.length; idx++) {
        	properties[idx] = orgProperties[idx];
    	}
    	properties[orgProperties.length] = StringToId.toId("length");
        return properties;
    }
    
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        Object obj;
        int length = getVarArrayLength();
        if (length != 0) {
            try {
                obj = getProperty(0);
                if (obj != Expresser.UNDEFINED)
                    sb.append(obj);
                for (int idx = 1; idx < length; idx++) {
                    sb.append(",");
                    obj = getProperty(idx);
                    if (obj != Expresser.UNDEFINED) {
                        sb.append(obj);
                    }
                }
            } catch (ExpException ee) {
                return ee.getMessage();
            }
        }
        return sb.toString();
    }

    /**
     * prototypeを取得します。
     * @return prototype 
     * @throws ExpException
     */
    public static ExpObject getPrototype() throws ExpException {
        if (prototype == null) {
            prototype = new ExpObject(ExpObject.getPrototype());
            synchronized (prototype) {
                prototype.putProperty(Array__concat.ID, Array__concat.getInstance());
                prototype.putProperty(Array__join.ID,   Array__join.getInstance());
                prototype.putProperty(Array__pop.ID,    Array__pop.getInstance());
                prototype.putProperty(Array__push.ID,   Array__push.getInstance());
                prototype.putProperty(Array__reverse.ID,Array__reverse.getInstance());
                prototype.putProperty(Array__shift.ID,  Array__shift.getInstance());
                prototype.putProperty(Array__sort.ID,   Array__sort.getInstance());
                prototype.putProperty(Array__toString.ID, Array__toString.getInstance());
                prototype.putProperty(Array__toLocaleString.ID, Array__toLocaleString.getInstance());
                prototype.putProperty(Array__unshift.ID,Array__unshift.getInstance());
            }
        }
        return prototype;
    }
}
