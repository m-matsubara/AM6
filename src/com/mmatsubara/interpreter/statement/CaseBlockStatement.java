package com.mmatsubara.interpreter.statement;

import java.util.HashMap;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.parsedexpression.IParsedExpression;


/**
 * caseブロックステートメントです。
 * 
 * 作成日: 2005/02/26
 * 
 * @author m.matsubara
 */
public class CaseBlockStatement extends BlockStatement {
    private HashMap caseLabelMap = new HashMap();
    private int defaultCaseValuePosition = Integer.MAX_VALUE;
    private IParsedExpression caseExpression;
    
    
    /**
     * caseブロックの生成をします。
     * （内容は初期化した後に追加しなければならない）
     * 
     * @param caseExpression 分岐条件式
     */
    public CaseBlockStatement(IParsedExpression caseExpression) {
        this.caseExpression = caseExpression;
    }
    
    
    /* (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#execute(com.mmatsubara.expresser.RuntimeData)
     */
    public StatementResult execute(RuntimeData runtime) throws ExpException {
        Object caseValue = Expresser.evaluate(runtime, caseExpression);
        StatementResult receive = execute(runtime, caseValue);
        if (receive != null) {
            int statementType = receive.getStatementType();
            if (statementType == StatementResult.ST_BREAK)
                return null;
            return receive;
        }
        
        return null;
    }
    
    /**
     * 指定されたcase値に対応する位置から実行します。
     * @param runtime ランタイムデータ
     * @param caseValue case値
     * @throws ExpException
     * @throws SpecialStatement
     */
    public StatementResult execute(RuntimeData runtime, Object caseValue) throws ExpException {
        prepareBlockArray();
        
        int max = blockArray.length;
        for (int idx = getCaseLabelPos(caseValue); idx < max; idx++) {
            StatementResult receive = blockArray[idx].execute(runtime);
            if (receive != null) {
                if (receive.getStatementType() == StatementResult.ST_BREAK)
                    return null;
                return receive;
            }
        }
        return null;
    }
    
    /**
     * 最後に追加されたステートメントの後ろにラベルを追加します。
     * @param caseValue case値、default:を登録するときはnullを指定する
     * @return 設定に成功した場合 true すでに登録済みの時は失敗し、falseを返す
     */
    public boolean addCaseLabel(Object caseValue) {
        if (caseValue != null) {
            if (caseLabelMap.get(caseValue) != null)
                return false;
            caseLabelMap.put(caseValue, new Integer(size()));
        }
        else {
            if (defaultCaseValuePosition != Integer.MAX_VALUE)
                return false;
            defaultCaseValuePosition = size();
        }
        return true;
    }
    
    /**
     * 指定されたIDのラベル位置を取得します。
     * @param caseValue case値
     * @return ラベル位置
     */
    public int getCaseLabelPos(Object caseValue) {
        Integer pos = (Integer)caseLabelMap.get(caseValue);
        if (pos != null)
            return pos.intValue();
        else
            return defaultCaseValuePosition;
    }
}
