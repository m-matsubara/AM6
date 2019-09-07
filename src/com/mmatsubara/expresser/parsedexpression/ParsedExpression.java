package com.mmatsubara.expresser.parsedexpression;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.OperatorData;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.operatorset.IOperatorSet;
import com.mmatsubara.expresser.type.IExpObject;

/**
 * 構文解析済みの計算式です。<br/>
 * <br/>
 * 作成日: 2005/02/05
 * 
 * @author m.matsubara
 */
public class ParsedExpression implements IParsedExpression {
    /**  オペランド１、左オペランドを表します。左オペランドのない単項演算子は null となります */
    private IParsedExpression operand1Expression;
    /**  オペランド２、右オペランドを表します。右オペランドのない単項演算子は null となります */
    private IParsedExpression operand2Expression;
    /**  演算子を表します */
    private OperatorData operatorData;
    /**  ソース中での演算子位置を表します */
    private int operatorIdx;
    /**  演算器です */
    private Expresser expresser;
    /**  前回演算の評価を行ったときの主オペランドのクラスです */
    private Class lastPrimaryOperandClass = null;
    /**  前回演算の評価を行ったときの演算子セットです */
    private IOperatorSet lastOperatorSet = null;
    
    public ParsedExpression(IParsedExpression operand1Expression, IParsedExpression operand2Expression, OperatorData operatorData, int operatorIdx, Expresser expresser) throws ExpException {
        this.operand1Expression = operand1Expression;
        this.operand2Expression = operand2Expression;
        this.operatorData = operatorData;
        this.operatorIdx = operatorIdx;
        this.expresser = expresser;
    }

    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.expresser.IParsedExpression#isUsingVariable(Integer id)
     */
    public int isUsingVariable(Integer id) throws ExpException {
        int result = 0;
        if (operand1Expression != null)
            result += operand1Expression.isUsingVariable(id);
        if (operand2Expression != null)
            result += operand2Expression.isUsingVariable(id);
        return result;
    }
    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#evaluate(com.mmatsubara.expresser.RuntimeData)
     */
    public IExpObject evaluate(RuntimeData runtime) throws ExpException {
        IExpObject operand1 = null;
        IExpObject operand2 = null;
        try {
            if (this.operand1Expression != null) {
                if (operatorData.isLeftVariable() == false)
                    operand1 = this.operand1Expression.evaluate(runtime);
                else
                    operand1 = this.operand1Expression.evalRef(runtime);
            }
            if (this.operand2Expression != null) {
                if (operatorData.isRightVariable() == false)
                    operand2 = this.operand2Expression.evaluate(runtime);
                else
                    operand2 = this.operand2Expression.evalRef(runtime);
            }

            Object primaryOperand = ((operand1 != null) && (operand1 != null)) ? operand1 : operand2;        //  主体となる演算対象
            Class primaryOperandClass = primaryOperand != null ? primaryOperand.getClass() : Object.class;
            
            //  計算対象が前回と同じ型だったら、それを使う。
            if (primaryOperandClass == lastPrimaryOperandClass)
                return lastOperatorSet.operation(operatorData.getOperator(), operand1, operand2, runtime);
            
            //  expresserを用いて対応する演算子を検索
            IOperatorSet opeSet = expresser.searchOperatorSet(primaryOperand);
            if (opeSet != null) {
                IExpObject value = opeSet.operation(operatorData.getOperator(), operand1, operand2, runtime);
                if (value != Expresser.UNSUPPORTED_OPERATOR) {
                    lastPrimaryOperandClass = primaryOperandClass;
                    lastOperatorSet = opeSet;
                    return value;
                }    
            }
        } catch (ExpException ee) {
            //  position を保持しない例外を補足し、positionを再設定してスローする
            if (ee.getPosition() < 0)
                ee.setPosition(this.operatorIdx);
               throw ee;        //  キャッチした例外が位置を保持していない場合、演算子位置をエラー位置として再スローする
        }
        throw new ExpException(Expresser.getResString("UnsupportedOperator", operand1, operatorData.toString(), operand2), operatorIdx);
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
    
    /* (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#isConst()
     */
    public boolean isConst() {
        if (operand1Expression != null && operand1Expression.isConst() == false)
            return false;
        if (operand2Expression != null && operand2Expression.isConst() == false)
            return false;
        return true;
    }
    
    public OperatorData getOperatorData() {
        return operatorData;
    }
    
    public Object getOperand1() {
        return operand1Expression;
    }

    public Object getOperand2() {
        return operand2Expression;
    }
}
