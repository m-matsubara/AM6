package com.mmatsubara.expresser.parsedexpression;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Boolean;
import com.mmatsubara.expresser.type._String;

/**
 * ラップされた定数値です。<br/>
 * 値として IVariable が渡されることは前提としていません。 <br/>
 * <br/>
 * 作成日: 2005/02/05
 * 
 * @author m.matsubara
 */
public class WrappedExpression implements IParsedExpression {
    private IExpObject value;
    
    public WrappedExpression(IExpObject value) {
        this.value = value;
    }

    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.expresser.IParsedExpression#isUsingVariable(Integer)
     */
    public int isUsingVariable(Integer id) throws ExpException {
        //  何もしない
        return 0;
    }
    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.expresser.IParsedExpression#evaluate(com.mmatsubara.expresser.RuntimeData)
     */
    public IExpObject evaluate(RuntimeData runtime) {
        return value;
    }
    
    /* (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#evalRef(com.mmatsubara.expresser.RuntimeData)
     */
    public IVariable evalRef(RuntimeData runtime) throws ExpException {
        throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), -1);
    }
    
    /* (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#evalTargetObject(com.mmatsubara.expresser.RuntimeData)
     */
    public IExpObject evalTargetObject(RuntimeData runtime) throws ExpException {
        return Expresser.UNDEFINED;
    }
    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.expresser.IParsedExpression#isConst()
     */
    public boolean isConst() {
        //  プリミティブ型の定数型のみ true とする
        if (value instanceof ExpNumberBase)
            return true;
        else if (value instanceof _Boolean)
            return true;
        else if (value instanceof _String)
            return true;
        else
            return false;
    }
}
