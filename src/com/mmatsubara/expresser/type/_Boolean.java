package com.mmatsubara.expresser.type;

import java.util.ArrayList;
import java.util.List;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.Variable;
import com.mmatsubara.expresser.exception.ExpException;


/**
 * プリミティブ型の論理値型です。<br/>
 * <br/>
 * 作成日: 2005/01/16
 * 
 * @author m.matsubara
 *
 */
public class _Boolean implements IExpObject {
    private boolean value;
    private IExpObject _description = Expresser.UNDEFINED;	//	独自拡張

    private static ExpObject prototype = null;
    
    public _Boolean(boolean value) throws ExpException {
        this.value = value;
        getPrototype();
    }
    
    public boolean booleanValue() {
        return value;
    }
    
    public String toString() {
        return String.valueOf(value);
    }
    
    public boolean equals(Object object) {
        Class classObject = object.getClass();
        if (classObject == _Boolean.class) {
            return ((_Boolean)object).booleanValue() == value;
        } else if (classObject == Boolean.class) {
            return ((Boolean)object).booleanValue() == value;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return value ? 1 : 0;
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
        IExpObject obj = prototype.getProperty(id);
        return obj;
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
        }
        return prototype;
    }
}
