package com.mmatsubara.interpreter.statement;

import java.util.Map;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.parsedexpression.IParsedExpression;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Boolean;


/**
 * for ステートメントです。<br/>
 * 
 * 作成日: 2005/01/30
 * 
 * @author m.matsubara
 */
public class ForStatement implements IStatement {
    /** ループの初期化式 */
    private IParsedExpression initialExpression; 
    /** 繰り返し条件式 */
    private IParsedExpression condition;
    /** ループの終わりに実行する式 */
    private IParsedExpression loopEndExpression;
    /** ループ本体 */
    private IStatement statement = null;
    /** 繰り返し条件式の位置 */
    private int conditionIdx;   //  現在使用されていないが将来使うかも？（エラー位置の検出）
    /** ラベルID */
    private Integer labelID;
    
    
    /**
     * for ステートメントの生成をします。
     * @param initialExpression 初期化計算式
     * @param condition 繰り返し条件式
     * @param loopEndExpression ループエンドの式
     * @param statement ループ本体
     * @param conditionIdx 繰り返し条件式位置
     */
    public ForStatement(IParsedExpression initialExpression, IParsedExpression condition, IParsedExpression loopEndExpression, IStatement statement, int conditionIdx, Integer labelID) {
        this.initialExpression = initialExpression;
        this.condition = condition;
        this.loopEndExpression = loopEndExpression;
        this.statement = statement;
        this.conditionIdx = conditionIdx;
        this.labelID = labelID;
    }
    
    /**
     * 条件式を返します。
     * 
     * @return 解析済みの条件式
     */
    public IParsedExpression getCondition() {
        return condition;
    }
    
    /**
     * 条件式を評価します。
     * @param runtime ランタイムデータ
     * @return 条件式の評価結果
     * @throws ExpException
     */
    public boolean condition(RuntimeData runtime) throws ExpException {
        if (condition == null)
            return true;    //  条件式が指定されていない場合はそのままループ内を実行するためtrue扱いとしてしまう
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
        if (initialExpression != null)
            result += initialExpression.isUsingVariable(id); 
        if (condition != null)
            result += condition.isUsingVariable(id);
        if (loopEndExpression != null)
            result += loopEndExpression.isUsingVariable(id);
        if (statement != null)
            result += statement.isUsingVariable(id);
        return result;
    }

    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#execute(com.mmatsubara.expresser.RuntimeData runtime)
     */
    public StatementResult execute(RuntimeData runtime) throws ExpException {
        if (initialExpression != null)
            Expresser.evaluate(runtime, initialExpression);

        while (condition(runtime)) {
            if (statement != null) {
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
            //  次のループの準備
            if (loopEndExpression != null)
                Expresser.evaluate(runtime, loopEndExpression);
        }
        return null;
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#registLocalVariable(java.util.Map)
     */
    public void registLocalVariable(Map varMap) {
        if (statement != null)
            statement.registLocalVariable(varMap);
    }

    public int getConditionIdx() {
        return conditionIdx;
    }

    public void setConditionIdx(int conditionIdx) {
        this.conditionIdx = conditionIdx;
    }
}
