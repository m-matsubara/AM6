package com.mmatsubara.expresser.scope;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.Variable;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.IExpObject;

/**
 * スコープの実装の一種です。<br/>
 * 変数に対してシーケンシャルアクセスを行います。<br/>
 * offset値なしでのアクセスの場合、Scopeオブジェクトよりもおそくなりますが、
 * fixedプロパティを持たないため、任意の時点でoffsetを取得することができます。<br/>
 * <br/> 
 * 作成日: 2005/06/05
 * 
 * @author m.matsubara
 */

public class SequentialScope implements IScope {
    private int[]  idArray;
    private IVariable[] varArray;
    private int count = 0;

    private IScope scopeChain = null;
    
    public SequentialScope() {
        this(10);
    }
    
    public SequentialScope(int initialSize) {
        idArray = new int[initialSize];
        varArray = new IVariable[initialSize];
    }
    
    
	/* (非 Javadoc)
	 * @see com.mmatsubara.expresser.IScope#getVariable(java.lang.Integer)
	 */
	public IVariable getVariable(Integer id) throws ExpException {
        int idValue = id.intValue();
        for (int idx = 0; idx < count; idx++) {
            if (idArray[idx] == idValue)
                return varArray[idx];
        }
        if (scopeChain != null)
            return scopeChain.getVariable(id);
        return null;
	}

	/* (非 Javadoc)
	 * @see com.mmatsubara.expresser.IScope#getVariable(java.lang.Integer, int)
	 */
	public IVariable getVariable(Integer id, int offset) throws ExpException {
        if (offset <= count && idArray[offset - 1] == id.intValue())
            return varArray[offset - 1];
        else if (scopeChain != null)
            return scopeChain.getVariable(id, offset);
        else
            return null;
	}

	/* (非 Javadoc)
	 * @see com.mmatsubara.expresser.IScope#getValue(java.lang.Integer)
	 */
	public IExpObject getValue(Integer id) throws ExpException {
        int offset = getOffset(id);
        if (offset <= 0) {
            if (scopeChain != null)
                return getValue(id);
            else
                return Expresser.UNDEFINED;
        }
		IVariable variable = getVariable(id, offset);
        if (variable == null)
            throw new ExpException(Expresser.getResString("UndefinedVariable"), -1);
		return variable.getValue();
	}

	/* (非 Javadoc)
	 * @see com.mmatsubara.expresser.IScope#getValue(java.lang.Integer, int)
	 */
	public IExpObject getValue(Integer id, int offset) throws ExpException {
        IVariable variable = getVariable(id, offset);
        if (variable == null)
            throw new ExpException(Expresser.getResString("UndefinedVariable"), -1);
        return variable.getValue();
	}

	/* (非 Javadoc)
	 * @see com.mmatsubara.expresser.IScope#setValue(java.lang.Integer, java.lang.Object)
	 */
	public void setValue(Integer id, IExpObject value) throws ExpException {
        IVariable variable = getVariable(id);
        if (variable == null)
            throw new ExpException(Expresser.getResString("UndefinedVariable"), -1);
        variable.setValue(value);
	}

	/* (非 Javadoc)
	 * @see com.mmatsubara.expresser.IScope#setValue(java.lang.Integer, int, java.lang.Object)
	 */
	public void setValue(Integer id, int offset, IExpObject value)
			throws ExpException {
        IVariable variable = getVariable(id, offset);
        if (variable == null)
            throw new ExpException(Expresser.getResString("UndefinedVariable"), -1);
        variable.setValue(value);
	}

	/* (非 Javadoc)
	 * @see com.mmatsubara.expresser.IScope#defineVariable(java.lang.Integer)
	 */
	public IVariable defineVariable(Integer id) throws ExpException {
        //  配列がオーバーフローするときの対処
        if (count >= idArray.length) {
            int[] idArrayWork = new int[count * 2];
            IVariable[] varArrayWork = new IVariable[count * 2];
            System.arraycopy(idArray, 0, idArrayWork, 0, count);
            System.arraycopy(varArray, 0, varArrayWork, 0, count);
            idArray = idArrayWork;
            varArray = varArrayWork;
        }
        IVariable variable = new Variable(Expresser.UNDEFINED);
        idArray[count] = id.intValue();
        varArray[count] = variable;
        count++;
        return variable;
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
		return this.scopeChain;
	}

	/* (非 Javadoc)
	 * @see com.mmatsubara.expresser.IScope#getOffset(java.lang.Integer)
	 */
	public int getOffset(Integer id) {
        int idValue = id.intValue();
		for (int idx = 0; idx < count; idx++) {
            if (idArray[idx] == idValue)
                return idx + 1;
        }
        if (scopeChain != null)
            return scopeChain.getOffset(id);
        return 0;
	}
	
	/*
	 * 
	 */
	public int[] getVarIdArray() {
		return idArray;
	}
	
	/*
	 * 
	 */
	public int getVarCount() {
		return count;
	}
	
}
