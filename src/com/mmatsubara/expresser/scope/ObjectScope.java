package com.mmatsubara.expresser.scope;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpObjectMember;
import com.mmatsubara.expresser.type.IExpObject;

/**
 * スコープを実装する
 * 
 * 作成日: 2005/06/04
 * 
 * @author m.matsubara
 */

public class ObjectScope implements IScope {
    IExpObject object;  //  スコープの元となるオブジェクト
    private IScope scopeChain = null;
    
    public ObjectScope(IExpObject object) {
        this.object = object;
    }

	/* (非 Javadoc)
	 * @see com.mmatsubara.expresser.IScope#getValue(java.lang.Integer)
	 */
	public IExpObject getValue(Integer id) throws ExpException {
        if (object.isProperty(id, true))
        	return object.getProperty(id);
        else if (scopeChain != null)
            return getValue(id);
        else
        	throw new ExpException(Expresser.getResString("UndefinedVariable"), -1);
        
	}
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.IScope#getValue(java.lang.Integer, int)
     */
    public IExpObject getValue(Integer id, int offset) throws ExpException {
        if (object.isProperty(id, true))
            return object.getProperty(id);
        else if (scopeChain != null)
            return getValue(id, offset);
        else
            throw new ExpException(Expresser.getResString("UndefinedVariable"), -1);
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.IScope#setValue(java.lang.Integer, java.lang.Object)
     */
    public void setValue(Integer id, IExpObject value) throws ExpException {
        if (object.isProperty(id, true))
        	object.putProperty(id, value);
        else if (scopeChain != null)
            setValue(id, value);
        else
            throw new ExpException(Expresser.getResString("UndefinedVariable"), -1);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.IScope#setValue(java.lang.Integer, java.lang.Object)
     */
    public void setValue(Integer id, int offset, IExpObject value) throws ExpException {
        if (object.isProperty(id, true))
            object.putProperty(id, value);
        else if (scopeChain != null)
            setValue(id, offset, value);
        else
            throw new ExpException(Expresser.getResString("UndefinedVariable"), -1);
    }

	/* (非 Javadoc)
	 * @see com.mmatsubara.expresser.IScope#getVariable(java.lang.Integer)
	 */
	public IVariable getVariable(Integer id) throws ExpException {
		return new ExpObjectMember(object, id);
	}
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.IScope#getVariable(java.lang.Integer)
     */
    public IVariable getVariable(Integer id, int offset) throws ExpException {
        if (object.isProperty(id, true))
        	return new ExpObjectMember(object, id);
        else if (scopeChain != null)
            return getVariable(id, offset);
        else
            throw new ExpException(Expresser.getResString("UndefinedVariable"), -1);
        
    }
    
	/* (非 Javadoc)
	 * @see com.mmatsubara.expresser.IScope#defineVariable(java.lang.Integer)
	 */
    public IVariable defineVariable(Integer id) throws ExpException {
    	object.putProperty(id, Expresser.UNDEFINED);
        return getVariable(id);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.IScope#setScopeChain(com.mmatsubara.expresser.IScope)
     */
    public void setScopeChain(IScope nextScope) {
        this.scopeChain = nextScope;
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.IScope#getScopeChain()
     */
    public IScope getScopeChain() {
        return scopeChain;
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.IScope#getOffset(java.lang.Integer)
     */
    public int getOffset(Integer id) {
    	return 0;
    }
    
    public Integer[] getObjectIdList() {
    	return object.enumProperty();
    }
    
    public IExpObject getObject() {
    	return object;
    }
}
