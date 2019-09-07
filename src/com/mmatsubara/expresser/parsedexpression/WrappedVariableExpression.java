package com.mmatsubara.expresser.parsedexpression;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.IExpObject;

/**
 * ラップされた変数オブジェクトです。<br/>
 * <br/>
 * 作成日: 2005/02/05
 * 
 * @author m.matsubara
 */
public class WrappedVariableExpression implements IParsedExpression {
    private String variableName;    //  TODO 使わないかも？（デバッグ機能を実装したら使うかも）
    private Integer variableId;
    
    private int offset = 0;    //  変数へのインデックス（RuntimeDataで変数へのアクセスに使う（高速化））
    
    private int position;
    
    public WrappedVariableExpression(String variableName, Integer variableId, int position) {
        this.variableName = variableName;
        this.variableId = variableId;
        this.position = position;
    }
    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.expresser.IParsedExpression#isUsingVariable(Integer)
     */
    public int isUsingVariable(Integer id) throws ExpException {
        if (variableId.intValue() == id.intValue()) {
            return 1;
        } else {
            return 0;
        }
    }

    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.expresser.IParsedExpression#evaluate(com.mmatsubara.expresser.RuntimeData)
     */
    public IExpObject evaluate(RuntimeData runtime) throws ExpException {
        try {
            if (offset != 0) {
                if (offset > 0)
                    return runtime.getValue(variableId, offset);
                else
                    return runtime.getValue(variableId);  //  offsetなしで変数を取得する。
            }
            else {
                offset = runtime.getVariableOffset(variableId);
                if (offset != 0)
                    return runtime.getValue(variableId, offset);
                else {
                    offset = -1;
                    IVariable variable = runtime.getVariable(variableId, false);  //  offsetなしで変数を取得する。変数がない場合はエラー
                    if (variable != null)
                        return variable.getValue();
                    else
                        throw new ExpException(Expresser.getResString("UndefinedVariable"), position);
                }
            }
        } catch (ExpException ee) {
            if (ee.getPosition() < 0)
                ee.setPosition(position);
            throw ee;
        }
    }

    /* (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#evalRef(com.mmatsubara.expresser.RuntimeData)
     */
    public IVariable evalRef(RuntimeData runtime) throws ExpException {
        try {
            if (offset != 0) {
                if (offset > 0)
                    return runtime.getVariable(variableId, offset);
                else
                    return runtime.getVariable(variableId, false);  //  offsetなしで変数を取得する。
            }
            else {
                offset = runtime.getVariableOffset(variableId);
                if (offset != 0)
                    return runtime.getVariable(variableId, offset);
                else {
                    offset = -1;
                    return runtime.getVariable(variableId, true);  //  offsetなしで変数を取得する。変数がない場合は作成される
                }
            }
        } catch (ExpException ee) {
            if (ee.getPosition() < 0)
                ee.setPosition(position);
            throw ee;
        }
    }
    
    /* (non-Javadoc)
     * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#evalTargetObject(com.mmatsubara.expresser.RuntimeData)
     */
    public IExpObject evalTargetObject(RuntimeData runtime) throws ExpException {
        return Expresser.UNDEFINED;
    }
    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.expresser.IParsedExpression#isConst()
     */
    public boolean isConst() {
        return false;
    }
    
    /**
     * 変数名の取得
     * @return 変数名
     */
    public String getVariableName() {
        return variableName;
    }
}
