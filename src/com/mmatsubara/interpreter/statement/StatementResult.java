package com.mmatsubara.interpreter.statement;

import com.mmatsubara.expresser.type.IExpObject;

/**
 * IStatementの実行結果として返されるオブジェクトです。<br/>
 * ただし、特に何もないときは StatementResult は返されません。<br/>
 * 通常、break, continue, return, throwなどの処理の流れを変えるステートメントが生成します。
 * 
 * 作成日: 2005/02/27
 * @author m.matsubara
 *
 */
public class StatementResult {
    public static final int ST_BREAK = 1;
    public static final int ST_CONTINUE = 2;
    public static final int ST_RETURN = 3;
    public static final int ST_EXCEPTION = 4;
    
    
    private int statementType;
    private IExpObject resultValue;
    private Integer labelId;
    
    /**
     * ステートメントの実行結果を生成します。
     * @param statementType
     * @param resultValue
     */
    public StatementResult(int statementType, IExpObject resultValue, Integer labelId) {
        this.statementType = statementType;
        this.resultValue = resultValue;
        this.labelId = labelId;
    }
    
    
    /**
     * @return resultValue を戻します。
     */
    public IExpObject getResultValue() {
        return resultValue;
    }
    /**
     * @param resultValue 戻り値を設定します。
     */
    public void setResultValue(IExpObject resultValue) {
        this.resultValue = resultValue;
    }
    /**
     * @return statementType を戻します。
     */
    public int getStatementType() {
        return statementType;
    }
    /**
     * @param statementType ステートメントの型を設定します。
     */
    public void setStatementType(int statementType) {
        this.statementType = statementType;
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
}
