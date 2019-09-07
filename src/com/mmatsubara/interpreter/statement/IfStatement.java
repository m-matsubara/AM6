package com.mmatsubara.interpreter.statement;

import java.util.Map;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.Range;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.parsedexpression.IParsedExpression;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Boolean;


/**
 * if ステートメントです。<br/>
 * 
 * 作成日: 2005/01/16
 * 
 * @author m.matsubara
 */
public class IfStatement implements IStatement {
    /** if の条件式 */
    private Range conditionRange;
    private IParsedExpression condition;
    private IStatement thenStatement = null;
    private IStatement elseStatement = null;
    
    /**
     * if ステートメントオブジェクトの構築
     * @param condition 条件式
     * @param conditionRange 条件式範囲
     * @param thenStatement 条件式が true の時実行されるステートメント
     * @param elseStatement 条件式が else の時実行されるステートメント
     * @throws ExpException
     * @throws ExpException
     */
    public IfStatement(IParsedExpression condition, Range conditionRange, IStatement thenStatement, IStatement elseStatement) {
        this.conditionRange = conditionRange;
        this.condition = condition;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }
    
    /**
     * if 文の条件式を返す
     * 
     * @return ステートメント内での条件式の範囲
     */
    public Range getConditionRange() {
        return conditionRange;
    }
    
    /**
     * 条件式を評価する
     * @param runtime ランタイムデータ
     * @return 評価結果
     * @throws ExpException
     */
    public boolean condition(RuntimeData runtime) throws ExpException {
        IExpObject result = Expresser.evaluate(runtime, condition);
        if (result instanceof _Boolean) {
            return ((_Boolean)result).booleanValue();
        } else {
            return runtime.getTypeConverter().toBoolean(runtime, result).booleanValue();
        }
    }
    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#isUsingVariable(Integer)
     */
    public int isUsingVariable(Integer id) throws ExpException {
        int result = 0;
        if (condition != null)
            result += condition.isUsingVariable(id);
        if (thenStatement != null)
            result += thenStatement.isUsingVariable(id);
        if (elseStatement != null)
            result += elseStatement.isUsingVariable(id);
        return result;
    }

    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#execute(com.mmatsubara.expresser.RuntimeData)
     */
    public StatementResult execute(RuntimeData runtime) throws ExpException {
        StatementResult receive = null;
        if (condition(runtime)) {
            if (thenStatement != null)
                receive = thenStatement.execute(runtime);
        } else {
            if (elseStatement != null)
                receive = elseStatement.execute(runtime);
        }
        return receive;
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#registLocalVariable(java.util.Map)
     */
    public void registLocalVariable(Map varMap) {
        if (thenStatement != null)
            thenStatement.registLocalVariable(varMap);
        if (elseStatement != null)
            elseStatement.registLocalVariable(varMap);
    }
}
