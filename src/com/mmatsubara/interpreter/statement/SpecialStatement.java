/*
 *
 */
package com.mmatsubara.interpreter.statement;

import java.util.Map;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.parsedexpression.IParsedExpression;
import com.mmatsubara.expresser.type.IExpObject;


/**
 * 特殊ステートメント　break, continue, return, exception を表します。<br/>
 * executeメソッドにて StatementResult オブジェクトを生成します。<br/>
 * 他のステートメントは StatementResult によって処理を切り替えなければなりません。<br/>
 * 
 * 作成日: 2005/01/30
 * 
 * @author m.matsubara
 *
 */
public class SpecialStatement implements IStatement {
    public static final int SS_TYPE_BREAK = StatementResult.ST_BREAK;
    public static final int SS_TYPE_CONTINUE = StatementResult.ST_CONTINUE;
    public static final int SS_TYPE_EXCEPTION = StatementResult.ST_EXCEPTION;
    public static final int SS_TYPE_RETURN = StatementResult.ST_RETURN;
    
    private int type;
    private IParsedExpression expression;
    private IExpObject value;    //  executeメソッドで、expressionを計算したもの
    private Integer labelId;
    
    
    public SpecialStatement(int type, IParsedExpression expression) {
        this.type = type;
        this.expression = expression;
        this.value = Expresser.UNDEFINED;
        this.labelId = null;
    }

    public SpecialStatement(int type, IExpObject value) {
        this.type = type;
        this.expression = null;
        this.value = value;
        this.labelId = null;
    }

    public SpecialStatement(int type, Integer labelId) {
        this.type = type;
        this.expression = null;
        this.value = null;
        this.labelId = labelId;
    }

    /**
     * 特殊ステートメントの種類を返します。 SS_TYPE_BREAK, SS_TYPE_RETURN, SS_TYPE_EXCEPTION  
     * @return 特殊ステートメントの種類
     */
    public int getType() {
        return type;
    }

    /**
     * オプションの値がある場合はその値です。
     * @return オプション値
     */
    public Object getValue() {
        return value;
    }

    /**
     * ラベルIDを取得します。
     * @return
     */
    public Integer getLabelId() {
        return labelId;
    }

    /**
     * ラベルIDを設定します。
     * @param labelId
     */
    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
    }

    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#isUsingVariable(Integer)
     */
    public int isUsingVariable(Integer id) throws ExpException {
        if (expression != null)
            return expression.isUsingVariable(id);
        return 0;
    }

    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#execute(com.mmatsubara.expresser.RuntimeData)
     */
    public StatementResult execute(RuntimeData runtime) throws ExpException {
        if (expression != null) {
            IExpObject value = expression.evaluate(runtime);
            return new StatementResult(type, value, labelId);
        } else { 
            return new StatementResult(type, this.value, labelId);
        }
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#registLocalVariable(java.util.Map)
     */
    public void registLocalVariable(Map varMap) {
        //  何もしない
    }
}
