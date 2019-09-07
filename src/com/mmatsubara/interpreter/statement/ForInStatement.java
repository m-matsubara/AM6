package com.mmatsubara.interpreter.statement;

import java.util.Map;

import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.parsedexpression.IParsedExpression;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._String;


/**
 * for-in ステートメントです。<br/>
 * 
 * 作成日: 2006/04/15
 * 
 * @author m.matsubara
 */
public class ForInStatement implements IStatement {
    /** 制御変数ID */
    private Integer variableID;
    /** 操作対象オブジェクトID */
    /** 操作対象オブジェクトを表す式 */
    private IParsedExpression objectExpression;
    /** ラベルID */
    private Integer labelID;
    /** ループ本体 */
    private IStatement statement = null;
    
    
    
    /**
     * for-in ステートメントの生成をします。
     * @param variableID 制御用変数ID
     * @param objectExpression　操作対象オブジェクトID 
     * @param statement ループ本体
     * @param labelID ラベルを持つ場合
     */
    public ForInStatement(Integer variableID, IParsedExpression objectExpression, IStatement statement, Integer labelID) {
    	this.variableID = variableID;
        this.objectExpression = objectExpression;
        this.statement = statement;
        this.labelID = labelID;
    }
    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#isUsingVariable(Integer)
     */
    public int isUsingVariable(Integer id) throws ExpException {
        int result = 0;
        if (variableID.equals(id))
            result++;
        result += objectExpression.isUsingVariable(id);
        return result;
    }

    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#execute(com.mmatsubara.expresser.RuntimeData runtime)
     */
    public StatementResult execute(RuntimeData runtime) throws ExpException {
        objectExpression.evaluate(runtime);
        IExpObject object = objectExpression.evaluate(runtime);
        if (object == null)
            return null;
        Integer[] properties = object.enumProperty();
        for (int idx = 0; idx < properties.length; idx++) {
            runtime.setValue(variableID, new _String(StringToId.toString(properties[idx])));
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
}
