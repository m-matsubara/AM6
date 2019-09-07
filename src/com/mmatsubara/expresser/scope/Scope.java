package com.mmatsubara.expresser.scope;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.Variable;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.IExpObject;

/**
 * 変数アクセスの際のスコープを定義します。<br/>
 * バイナリサーチアルゴリズムを用いて変数へアクセスするため、offset値を用いなくてもそこそこアクセスが速くなります。<br/>
 * 反面固定化（fixed()メソッドの呼び出し)を行わないと、 offset値を扱うことができません。<br/>
 * <br/>
 * 作成日: 2005/06/04
 * 
 * @author m.matsubara
 */
public class Scope implements IScope {
    private int[]  idArray;
    private IVariable[] varArray;
    private int count = 0;
    
    /** trueに設定されると新しい変数を定義できなくなるかわりに getOffset() が0以外を返すようになります。 */
    private boolean fixed = false;
    
    private IScope scopeChain = null;
    
    public Scope() {
        this(10);
    }
    
    public Scope(int initialSize) {
        idArray = new int[initialSize];
        varArray = new IVariable[initialSize];
    }
    

    /* (Javadoc なし)
     * @see com.mmatsubara.expresser.IScope#getVariable(java.lang.Integer)
     */
    public IVariable getVariable(Integer id) throws ExpException {
        int idValue = id.intValue();
        int min = 0;
        int max = count - 1;
        int idx = (min + max) / 2;
        while (idArray[idx] != idValue) {
            if (idValue < idArray[idx])
                max = idx - 1;
            else
                min = idx + 1;
            
            if (min > max) {
                if (scopeChain != null)
                    return scopeChain.getVariable(id);
                return null;
            }
            idx = (min + max) / 2;
        }
        return varArray[idx];
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
    
    /**
     * 変数を設定します。
     * @param id 変数ID
     * @param variable 変数
     * @throws ExpException
     */
    public void setVariable(Integer id, IVariable variable) throws ExpException {
        int idValue = id.intValue();
        int min = 0;
        int max = count - 1;
        int idx = (min + max) / 2;
        while (idArray[idx] != idValue) {
            if (idValue < idArray[idx])
                max = idx - 1;
            else
                min = idx + 1;
            
            if (min > max) {
                throw new ExpException(Expresser.getResString("UndefinedVariable"), -1);
            }
            idx = (min + max) / 2;
        }
        varArray[idx] = variable;
    }

    /* (Javadoc なし)
     * @see com.mmatsubara.expresser.IScope#getValue(java.lang.Integer)
     */
    public IExpObject getValue(Integer id) throws ExpException {
        IVariable variable = getVariable(id);
        if (variable == null)
            throw new ExpException(Expresser.getResString("UndefinedVariable"), -1);
        return variable.getValue();
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.IScope#getValue(java.lang.Integer, int)
     */
    public IExpObject getValue(Integer id, int offset) throws ExpException {
        if (offset <= count && idArray[offset - 1] == id.intValue())
            return varArray[offset - 1].getValue();
        else if (scopeChain != null)
            return scopeChain.getValue(id, offset);
        else
            throw new ExpException(Expresser.getResString("UndefinedVariable"), -1);
    }

    /* (Javadoc なし)
     * @see com.mmatsubara.expresser.IScope#setValue(java.lang.Integer, java.lang.Object)
     */
    public void setValue(Integer id, IExpObject value) throws ExpException {
        IVariable variable = getVariable(id);
        if (variable != null) {
            variable.setValue(value);
        } else {
            //  このスコープにもチェインされたスコープにも変数がない
            throw new ExpException(Expresser.getResString("UndefinedVariable"), -1);
        }
    }
    
    public void setValue(Integer id, int offset, IExpObject value) throws ExpException {
        if (offset <= count && idArray[offset - 1] == id.intValue())
            varArray[offset - 1].setValue(value);
        else if (scopeChain != null)
            scopeChain.setValue(id, offset, value);
        else
            throw new ExpException(Expresser.getResString("UndefinedVariable"), -1);
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.IScope#defineVariable(java.lang.Integer)
     */
    public IVariable defineVariable(Integer id) throws ExpException {
        if (fixed)
        	throw new ExpException(Expresser.getResString("InternalError", "Scope#defineVariable", "failure define variable"), -1);
        
        //  配列がオーバーフローするときの対処
        if (count >= idArray.length) {
            int[] idArrayWork = new int[count * 2];
            IVariable[] varArrayWork = new IVariable[count * 2];
            System.arraycopy(idArray, 0, idArrayWork, 0, count);
            System.arraycopy(varArray, 0, varArrayWork, 0, count);
            idArray = idArrayWork;
            varArray = varArrayWork;
        }
        //  配列に新しい変数を挿入する
        int idx;
        int idValue = id.intValue();
        for (idx = count - 1; idx >= 0; idx--) {
            if (idArray[idx] < idValue)
                break;
            idArray[idx + 1] = idArray[idx];  
            varArray[idx + 1] = varArray[idx];  
        }
        if (idx >= 0 && idArray[idx] == idValue) {
            return varArray[idx];   // すでに変数が定義されている
        }
            
        Variable variable = new Variable(Expresser.UNDEFINED); 
        idArray[idx + 1] = idValue;  
        varArray[idx + 1] = variable;
        count++;
        
        return variable;
    }
    

    /* (Javadoc なし)
     * @see com.mmatsubara.expresser.IScope#setScopeChain(com.mmatsubara.expresser.IScope)
     */
    public void setScopeChain(IScope nextScope) {
        this.scopeChain = nextScope;
    }

    /* (Javadoc なし)
     * @see com.mmatsubara.expresser.IScope#getScopeChain()
     */
    public IScope getScopeChain() {
        return this.scopeChain;
    }
    
    /**
     * あらかじめ、コピー元（自分自身）と同じ変数の定義されたスコープを作成します。<br/>
     * 変数自体は共有されず新しい変数が確保されます。<br/>
     * fixedプロパティの状態もコピーされます。
     * @return コピーされたスコープ
     */
    public Scope copyScope() {
        Scope dest = new Scope(idArray.length);
        dest.count = this.count;
        for (int idx = 0; idx < count; idx++) {
            dest.idArray[idx] = this.idArray[idx];
            dest.varArray[idx] = new Variable();
        }
        dest.fixed = this.fixed;
        return dest;
    }
    
    /**
     * このメソッドを呼び出すと新しい変数を定義できなくなる代わりにgetOffset()が0以外の値を返すようになります。
     */
    public void fixed() {
    	this.fixed = true;
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.IScope#getOffset(java.lang.Integer)
     */
    public int getOffset(Integer id) {
        if (fixed == false)
            return 0;
        int idValue = id.intValue();
        int min = 0;
        int max = count - 1;
        int idx = (min + max) / 2;
        while (idArray[idx] != idValue) {
            if (idValue < idArray[idx])
                max = idx - 1;
            else
                min = idx + 1;
            
            if (min > max) {
                if (scopeChain != null)
                    return scopeChain.getOffset(id);
                return 0;
            }
            idx = (min + max) / 2;
        }
        return idx + 1;
    }
}
