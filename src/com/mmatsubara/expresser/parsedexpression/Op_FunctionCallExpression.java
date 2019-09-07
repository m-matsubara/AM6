package com.mmatsubara.expresser.parsedexpression;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type.IFunction;

/**
 * 関数呼び出しの式を表現します。<br/>
 * 関数呼び出し演算子はOperatorSetではなく専用のIParsedExpressionオブジェクトによって処理されます。<br/> 
 * <br/>
 * 作成日: 2005/07/29
 * 
 * @author m.matsubara
 */
public class Op_FunctionCallExpression implements IParsedExpression {
    private IParsedExpression functionExpression;
    private IParsedExpression[] argumentsExpression;
    private int opeIdx;
    
    
    public Op_FunctionCallExpression(IParsedExpression functionExpression, IParsedExpression[] argumentsExpression, int opeIdx) {
        this.functionExpression = functionExpression;
        this.argumentsExpression = argumentsExpression;
        this.opeIdx = opeIdx;
    }
    
    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#isUsingVariable(java.lang.Integer)
     */
    public int isUsingVariable(Integer id) throws ExpException {
        int count = functionExpression.isUsingVariable(id);
        int max = argumentsExpression.length;
        for (int idx = 0; idx < max; idx++)
            count += argumentsExpression[idx].isUsingVariable(id);
        
        return count;
    }

    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#evaluate(com.mmatsubara.expresser.RuntimeData)
     */
    public IExpObject evaluate(RuntimeData runtime) throws ExpException {
        try {
            //  関数オブジェクト取り出し
            IExpObject functionObject = functionExpression.evaluate(runtime);
            if (functionObject instanceof IFunction == false)
                throw new ExpException(Expresser.getResString("NotCallableObject"), opeIdx);
            IExpObject thisObject = functionExpression.evalTargetObject(runtime);
            
            IFunction function = (IFunction)functionObject;
            //  引数を計算する
            int max = argumentsExpression.length;
            IExpObject[] arguments = new IExpObject[max];
            for (int idx = 0; idx < max; idx++)
                arguments[idx] = argumentsExpression[idx].evaluate(runtime);

            //  関数の呼び出し
            IExpObject result = function.callFunction(runtime, thisObject, arguments);
            return result;
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
        throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), -1);
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
