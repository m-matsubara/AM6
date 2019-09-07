package com.mmatsubara.expresser.parsedexpression;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpObjectMember;
import com.mmatsubara.expresser.type.IExpObject;

/**
 * メンバーアクセスを表現します。<br/>
 * メンバーアクセス演算子はOperatorSetではなく専用のIParsedExpressionオブジェクトによって処理されます。<br/> 
 * <br/>
 * 作成日: 2005/07/29
 * 
 * @author m.matsubara
 */
public class Op_MemberAccessExpression implements IParsedExpression {
    private IParsedExpression objectExpression;
    private Integer memberID;
    private int opeIdx;
    
    public Op_MemberAccessExpression(IParsedExpression arrayExpression, Integer memberID, int opeIdx) {
        this.objectExpression = arrayExpression;
        this.memberID = memberID;
        this.opeIdx = opeIdx;
    }
    
    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#isUsingVariable(java.lang.Integer)
     */
    public int isUsingVariable(Integer id) throws ExpException {
        return objectExpression.isUsingVariable(id);
    }

    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#evaluate(com.mmatsubara.expresser.RuntimeData)
     */
    public IExpObject evaluate(RuntimeData runtime) throws ExpException {
        try {
            IExpObject object = objectExpression.evaluate(runtime);
            if (object == null)
                throw new ExpException(Expresser.getResString("NullObject"), -1);
            return object.getProperty(memberID);
        } catch (ExpException ee) {
            if (ee.getPosition() < 0)
                ee.setPosition(opeIdx);
            throw ee;
        }
    }

    /* (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#evalRef(com.mmatsubara.expresser.RuntimeData)
     */
    public IVariable evalRef(RuntimeData runtime) throws ExpException {
        try {
            IExpObject object = objectExpression.evaluate(runtime);
            if (object == null)
                throw new ExpException(Expresser.getResString("NullObject"), -1);
            return new ExpObjectMember(object, memberID);
        } catch (ExpException ee) {
            if (ee.getPosition() < 0)
                ee.setPosition(opeIdx);
            throw ee;
        }
    }
    
    /* (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#evalTargetObject(com.mmatsubara.expresser.RuntimeData)
     */
    public IExpObject evalTargetObject(RuntimeData runtime) throws ExpException {
        IExpObject object = objectExpression.evaluate(runtime);
        return object;
    }
    
    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#isConst()
     */
    public boolean isConst() {
        return false;
    }
}
