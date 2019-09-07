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
 * while ステートメントです。<br/>
 * 
 * 作成日: 2005/01/16
 * 
 * @author m.matsubara
 *
 */
public class WhileStatement implements IStatement {
    /** while の条件式 */
    private Range conditionRange;	//	今使わないけど将来使うかも？
    private IParsedExpression condition;
    private IStatement statement = null;
    private boolean doWhile;
    /** ラベルID */
    private Integer labelID;
    
    
    public WhileStatement(IParsedExpression condition, Range conditionRange, IStatement statement, Integer labelID) {
        this.condition = condition;
        this.conditionRange = conditionRange;
        this.statement = statement;
        this.labelID = labelID;
        this.doWhile = false;
    }

    /**
     * do-while（後判定の時）trueに設定します。
     * @param doWhile do-while（後判定の時）trueに設定します。
     */
    public void setDoWhile(boolean doWhile) {
        this.doWhile = doWhile;
    }
    
    /**
     * do-while（後判定の時）trueを返します。
     * @return do-while（後判定の時）trueを返します。
     */
    public boolean isDoWhile() {
        return doWhile;
    }
    
    /**
     * 条件式を評価します。
     * @param runtime ランタイムデータ
     * @return 条件式の評価結果
     * @throws ExpException 実行時エラー
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
        if (statement != null)
            result += statement.isUsingVariable(id);
        return result;
    }

    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#execute(com.mmatsubara.expresser.RuntimeData)
     */
    public StatementResult execute(RuntimeData runtime) throws ExpException {
        if (statement != null) {
            if (doWhile) {
                //  DoWhileの時
                StatementResult receive = statement.execute(runtime);
                if (receive != null) {
                    int statementType = receive.getStatementType();
                    if (statementType == StatementResult.ST_BREAK) {
                        if (receive.getLabelId() == null || this.labelID == receive.getLabelId())   //  ラベルはインスタンス自体一致する
                            return null;
                        else
                            return receive;
                    } else if (statementType == StatementResult.ST_CONTINUE) {
                        if (receive.getLabelId() == null || this.labelID == receive.getLabelId())   //  ラベルはインスタンス自体一致する
                            ;
                        else
                            return receive;
                    } else
                        return receive;
                }
            }
            //  ループの実行
            while (condition(runtime)) {
                StatementResult receive = statement.execute(runtime);
                if (receive != null) {
                    int statementType = receive.getStatementType();
                    if (statementType == StatementResult.ST_BREAK) {
                        if (receive.getLabelId() == null || this.labelID == receive.getLabelId())   //  ラベルはインスタンス自体一致する
                            return null;
                        else
                            return receive;
                    } else if (statementType == StatementResult.ST_CONTINUE) {
                        if (receive.getLabelId() == null || this.labelID == receive.getLabelId())   //  ラベルはインスタンス自体一致する
                            ;
                        else
                            return receive;
                    } else
                        return receive;
                }
            }
        } else {
            //  ループ本体がない場合
            while (condition(runtime))
                ;
        }
        return null;
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#registLocalVariable(java.util.Map)
     */
    public void registLocalVariable(Map varMap) {
        statement.registLocalVariable(varMap);
    }

    public Range getConditionRange() {
        return conditionRange;
    }

    public void setConditionRange(Range conditionRange) {
        this.conditionRange = conditionRange;
    }
}
