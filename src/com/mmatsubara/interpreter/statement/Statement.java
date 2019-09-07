package com.mmatsubara.interpreter.statement;

import java.util.Map;

import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.parsedexpression.IParsedExpression;
import com.mmatsubara.expresser.type.ExpObject;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Double;
import com.mmatsubara.expresser.type._String;


/**
 * 計算式の評価ステートメントです。<br/>
 * 
 * 作成日: 2005/01/16
 * 
 * @author m.matsubara
 */
public class Statement implements IStatement {
    private int position;
    private IParsedExpression parsedExpression;
    
    /**
     * 計算式ステートメントです。
     * @param parsedExpression 解析済みステートメント
     * @param position 計算式の位置
     * @throws ExpException
     */
    public Statement(IParsedExpression parsedExpression, int position) {
        this.parsedExpression = parsedExpression;
        this.position = position;
    }
    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#isUsingVariable(Integer)
     */
    public int isUsingVariable(Integer id) throws ExpException {
        if (parsedExpression != null)
            return parsedExpression.isUsingVariable(id);
        return 0;
    }

    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#execute(com.mmatsubara.expresser.RuntimeData)
     */
    public StatementResult execute(RuntimeData runtime) throws ExpException {
        try {
            //  演算
            IExpObject result = parsedExpression.evaluate(runtime);
            //  演算結果が変数の場合、その値を取得
            if (result instanceof IVariable)
                result = ((IVariable)result).getValue();
            //  最終演算結果を更新
            runtime.setLastResultValue(result);
        } catch (StatementResultNotification srn) {
            return srn.getStatementResult();
        } catch (ExpException ee) {
            if (ee.getPosition() < 0)
                ee.setPosition(position);
            ExpObject error = new ExpObject("Error", null);
            error.putProperty(StringToId.toId("message"), new _String(ee.getMessage()));
            error.putProperty(StringToId.toId("position"), new _Double(ee.getPosition()));
            StatementResult statementResult = new StatementResult(StatementResult.ST_EXCEPTION, error, null);    //  Error型を送出するようにしなければならない
            return statementResult;
//          throw ee;
        }
        return null;
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#registLocalVariable(java.util.Map)
     */
    public void registLocalVariable(Map varMap) {
        //  何もしない
    }
    
}
