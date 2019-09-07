package com.mmatsubara.expresser;

import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.parsedexpression.IParsedExpression;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Boolean;

/**
 * ３項演算子（op1 ? op2 : op3）<br/>
 * <br/>
 * 作成日: 2005/06/04
 * 
 * @author m.matsubara
 */
class SelectParsedExpression implements IParsedExpression {
    private IParsedExpression operand1;
    private IParsedExpression operand2;
    private IParsedExpression operand3;
    
    public SelectParsedExpression(IParsedExpression operand1, IParsedExpression operand2, IParsedExpression operand3) {
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.operand3 = operand3;
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#evaluate(com.mmatsubara.expresser.RuntimeData)
     */
    public IExpObject evaluate(RuntimeData runtime) throws ExpException {
        IExpObject value = operand1.evaluate(runtime);
        _Boolean bool;
        if (value instanceof _Boolean)
            bool = (_Boolean)value;
        else
            bool = runtime.getTypeConverter().toBoolean(runtime, value);
        if (bool.booleanValue()) {
            return operand2.evaluate(runtime);
        } else {
            return operand3.evaluate(runtime);
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
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#isConst()
     */
    public boolean isConst() {
        //  本当はoperand1の値に応じてoperand2, operand3は片方でよいのだが、RuntimeDataオブジェクトがないのでチェックできない
        //  operand1, operand2, operand3 全てが定数の書き方なんて誰もしないと思うが…。
        return operand1.isConst() && operand2.isConst() && operand3.isConst(); 
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#isUsingVariable(java.lang.Integer)
     */
    public int isUsingVariable(Integer id) throws ExpException {
        return operand1.isUsingVariable(id) + operand2.isUsingVariable(id) + operand3.isUsingVariable(id);
    }
}



/**
 * ３項演算子（op1 ? op2 : op3）のパーサー
 * 
 * 作成日: 2005/05/29
 * 
 * @author m.matsubara
 */
public class SelectOperatorParser implements IOperatorParser {

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.IOperatorParser#parseOperand2(Expresser expresser, char[], int, int)
     */
    public IParsedExpression parse(Expresser expresser, char[] statement, int begin,
            int end, int opeIdx, String opeWord) throws ExpException {
        IParsedExpression operand1 = expresser.parse(statement, begin, opeIdx);
        
        Range[] selectValues = Expresser.split(':', statement, opeIdx + opeWord.length(), end);
        if (selectValues.length != 2)
            throw new ExpException(Expresser.getResString("SyntaxError"), opeIdx);

        IParsedExpression operand2 = expresser.parse(statement, selectValues[0]);
        IParsedExpression operand3 = expresser.parse(statement, selectValues[1]);
        
        return new SelectParsedExpression(operand1, operand2, operand3);
    }

}
