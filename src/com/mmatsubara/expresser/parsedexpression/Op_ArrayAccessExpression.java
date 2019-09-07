package com.mmatsubara.expresser.parsedexpression;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.ExpObjectMember;
import com.mmatsubara.expresser.type.IExpObject;

/**
 * 配列アクセスを表現します。<br/>
 * 配列アクセス演算子はOperatorSetではなく専用のIParsedExpressionオブジェクトによって処理されます。<br/> 
 * <br/>
 * 作成日: 2005/07/29
 * 
 * @author m.matsubara
 */
public class Op_ArrayAccessExpression implements IParsedExpression {
    private IParsedExpression arrayExpression;
    private IParsedExpression indexExpression;
    private int opeIdx;
    
    public Op_ArrayAccessExpression(IParsedExpression arrayExpression, IParsedExpression indexExpression, int opeIdx) {
        this.arrayExpression = arrayExpression;
        this.indexExpression = indexExpression;
        this.opeIdx = opeIdx;
    }
    
    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#isUsingVariable(java.lang.Integer)
     */
    public int isUsingVariable(Integer id) throws ExpException {
        return arrayExpression.isUsingVariable(id) + indexExpression.isUsingVariable(id);
    }

    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#evaluate(com.mmatsubara.expresser.RuntimeData)
     */
    public IExpObject evaluate(RuntimeData runtime) throws ExpException {
        return evalRef(runtime).getValue();
    }

    /* (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#evalRef(com.mmatsubara.expresser.RuntimeData)
     */
    public IVariable evalRef(RuntimeData runtime) throws ExpException {
        try {
            IExpObject array = arrayExpression.evaluate(runtime);
            IExpObject indexObject = indexExpression.evaluate(runtime);
            if (indexObject instanceof ExpNumberBase) {
                double arrayIdx;
                arrayIdx = ((ExpNumberBase)indexObject).doubleValue();
                if (arrayIdx >= 0 && arrayIdx % 1.0 == 0)
                    return array.getVariable((int)arrayIdx);
            }
            // 添え字が正の整数ではなかった場合、文字列化してメンバアクセスとする
            String identifier = runtime.getTypeConverter().toString(runtime, indexObject).toString();
            return new ExpObjectMember(array, StringToId.toId(identifier));
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
        return Expresser.UNDEFINED;
    }
    
    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#isConst()
     */
    public boolean isConst() {
        return false;
    }
}
