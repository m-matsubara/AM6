package com.mmatsubara.expresser.type;

import java.util.List;

import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.exception.ExpException;


/**
 * 数字オブジェクト（倍精度実数）です。<br/>
 * <br/>
 * 作成日: 2005/05/03
 * 
 * @author m.matsubara
 */

public class ExpNumber extends _Double {
    public static IExpObject constructor = null; 

    private ExpObject object = null;
    
    private static ExpObject prototype = null;

    public ExpNumber(double value) throws ExpException {
        super(value);
        object = new ExpObject("Number", constructor);
        object.setThisPrototype(getPrototype());
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#deleteProperty(java.lang.Integer)
     */
    public IExpObject deleteProperty(Integer id) throws ExpException {
        return object.deleteProperty(id);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getConstructorList()
     */
    public List getConstructorList() {
        return object.getConstructorList();
    }
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getProperty(int)
     */
    public IExpObject getProperty(int index) throws ExpException {
        return object.getProperty(index);
    }
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getProperty(java.lang.Integer)
     */
    public IExpObject getProperty(Integer id) throws ExpException {
        return object.getProperty(id);
    }
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#putProperty(int, java.lang.Object)
     */
    public void putProperty(int index, IExpObject value) throws ExpException {
        object.putProperty(index, value);
    }
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#putProperty(java.lang.Integer, java.lang.Object)
     */
    public void putProperty(Integer id, IExpObject value) throws ExpException {
        object.putProperty(id, value);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getVariable(int)
     */
    public IVariable getVariable(int index) {
        return object.getVariable(index);  
    }
    
    /* (non-Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#enumProperty()
     */
    public Integer[] enumProperty() {
        return object.enumProperty();
    }
    
    /**
     * prototypeを取得します。
     * @return prototype 
     * @throws ExpException
     */
    public static ExpObject getPrototype() throws ExpException {
        if (prototype == null) {
            prototype = new ExpObject(_Double.getPrototype());
        }
        return prototype;
    }
}
