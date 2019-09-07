package com.mmatsubara.expresser.parsedexpression;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.ITypeConverter;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Boolean;

/**
 * 構文解析済みの計算式です。<br/>
 * <br/>
 * 作成日: 2005/02/05
 * 
 * @author m.matsubara
 */
public class Op_LogicalOrExpression implements IParsedExpression {
    private IParsedExpression operand1Expression;
    private IParsedExpression operand2Expression;
    private int operatorIdx;

    public Op_LogicalOrExpression(IParsedExpression operand1, IParsedExpression operand2, int operatorIdx) throws ExpException {
        this.operand1Expression = operand1;
        this.operand2Expression = operand2;
        this.operatorIdx = operatorIdx;
    }

    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.expresser.IParsedExpression#isUsingVariable(Integer id)
     */
    public int isUsingVariable(Integer id) throws ExpException {
        int result = 0;
        result += ((IParsedExpression)operand1Expression).isUsingVariable(id);
        result += ((IParsedExpression)operand2Expression).isUsingVariable(id);
        return result;
    }
    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.expresser.Op_LogicalOrExpression.IParsedExpression#evaluate(com.mmatsubara.expresser.RuntimeData)
     */
    public IExpObject evaluate(RuntimeData runtime) throws ExpException {
        IExpObject operand1 = ((IParsedExpression)this.operand1Expression).evaluate(runtime);
        IExpObject operand2 = ((IParsedExpression)this.operand2Expression).evaluate(runtime);

        try {
            //  左オペランドが false のとき、右オペランドを評価せず、false を返す。
            ITypeConverter typeConverter = runtime.getTypeConverter();
            if (typeConverter.toBoolean(runtime, operand1).booleanValue())
                return new _Boolean(true);
            if (typeConverter.toBoolean(runtime, operand2).booleanValue())
                return new _Boolean(true);
            return new _Boolean(false);
        } catch (ExpException ee) {
            //  position を保持しない例外を補足し、positionを再設定してスローする
            if (ee.getPosition() < 0)
                ee.setPosition(this.operatorIdx);
               throw ee;        //  キャッチした例外が位置を保持していない場合、演算子位置をエラー位置として再スローする
        }
    }
    
    /* (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#evalRef(com.mmatsubara.expresser.RuntimeData)
     */
    public IVariable evalRef(RuntimeData runtime) throws ExpException {
        throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), -1);
    }
    
    public boolean isConst() {
        return ((operand1Expression instanceof IParsedExpression) == false) && ((operand2Expression instanceof IParsedExpression) == false);
    }
    
    /* (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#evalTargetObject(com.mmatsubara.expresser.RuntimeData)
     */
    public IExpObject evalTargetObject(RuntimeData runtime) throws ExpException {
        return Expresser.UNDEFINED;
    }
    
    public Object getOperand1() {
        return operand1Expression;
    }

    public Object getOperand2() {
        return operand2Expression;
    }
}
