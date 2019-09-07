package com.mmatsubara.expresser.type;

import java.util.ArrayList;
import java.util.List;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.Variable;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.property.String__charAt;
import com.mmatsubara.expresser.type.property.String__charCodeAt;
import com.mmatsubara.expresser.type.property.String__concat;
import com.mmatsubara.expresser.type.property.String__indexOf;
import com.mmatsubara.expresser.type.property.String__lastIndexOf;
import com.mmatsubara.expresser.type.property.String__localeCompare;
import com.mmatsubara.expresser.type.property.String__match;
import com.mmatsubara.expresser.type.property.String__replace;
import com.mmatsubara.expresser.type.property.String__search;
import com.mmatsubara.expresser.type.property.String__slice;
import com.mmatsubara.expresser.type.property.String__split;
import com.mmatsubara.expresser.type.property.String__substring;
import com.mmatsubara.expresser.type.property.String__toChar;
import com.mmatsubara.expresser.type.property.String__toCharArray;
import com.mmatsubara.expresser.type.property.String__toLowerCase;
import com.mmatsubara.expresser.type.property.String__toUpperCase;


/**
 * プリミティブ型の文字列型です。<br/>
 * <br/>
 * 作成日: 2005/02/06
 * 
 * @author m.matsubara
 *
 */
public class _String implements IExpObject {
    private static ExpObject prototype = null;
    private IExpObject _description = Expresser.UNDEFINED;	//	独自拡張
    
    public int propertyId$length = StringToId.toId("length").intValue();

    private String value;
    private int hashCode;
    
    public _String(Object o) throws ExpException {
        if (o != null)  
            this.value = o.toString();
        else
            this.value = "";
        hashCode = this.value.hashCode();
        
        getPrototype();
    }
    
    public boolean equals(Object object) {
        if (this == object)
            return true;
        else if (object instanceof _String)
            return value.equals(((_String)object).value);
        else
            return value.equals(object);
    }
    
    public int length() {
        return value.length();
    }
    
    public String toString() {
        return value;
    }
    
    public int hashCode() {
        return hashCode;
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
        int idValue = id.intValue();
        if (idValue == propertyId$length)
            return new _Integer(value.length());
        else {
        	if (StringToId.toId("_description") == id)
        		return _description;
            return prototype.getProperty(id);
        }
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
        if (id.intValue() == propertyId$length)
        	return true;
        else if (searchPrototype)
            return prototype.isProperty(id, searchPrototype);
        else
            return false;
    }
    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getConstructorList()
     */
    public List getConstructorList() {
        return new ArrayList(0);
    }

    /* (non-Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#enumProperty()
     */
    public Integer[] enumProperty() {
    	Integer[] properties = new Integer[1];
    	properties[0] = new Integer(propertyId$length);
        return properties;
    }

    /**
     * prototypeを取得します。
     * @return prototype 
     * @throws ExpException
     */
    public static synchronized ExpObject getPrototype() throws ExpException {
        if (prototype == null) {
            prototype = new ExpObject(ExpObject.getPrototype());
            prototype.putProperty(String__charAt.ID,        String__charAt.getInstance());
            prototype.putProperty(String__charCodeAt.ID,    String__charCodeAt.getInstance());
            prototype.putProperty(String__concat.ID,        String__concat.getInstance());
            prototype.putProperty(String__indexOf.ID,       String__indexOf.getInstance());
            prototype.putProperty(String__lastIndexOf.ID,   String__lastIndexOf.getInstance());
            prototype.putProperty(String__localeCompare.ID, String__localeCompare.getInstance());
            prototype.putProperty(String__toLowerCase.ID,   String__toLowerCase.getInstance());
            prototype.putProperty(String__toLowerCase.toLocaleLowerCaseID,   String__toLowerCase.getInstance());
            prototype.putProperty(String__toUpperCase.ID,   String__toUpperCase.getInstance());
            prototype.putProperty(String__toUpperCase.toLocaleUpperCaseID,   String__toUpperCase.getInstance());
            prototype.putProperty(String__match.ID,         String__match.getInstance());
            prototype.putProperty(String__search.ID,        String__search.getInstance());
            prototype.putProperty(String__replace.ID,       String__replace.getInstance());
            prototype.putProperty(String__slice.ID,         String__slice.getInstance());
            prototype.putProperty(String__split.ID,         String__split.getInstance());
            prototype.putProperty(String__substring.ID,     String__substring.getInstance());

            prototype.putProperty(String__toChar.ID,        String__toChar.getInstance());
            prototype.putProperty(String__toCharArray.ID,   String__toCharArray.getInstance());
        }
        return prototype;
    }
}
