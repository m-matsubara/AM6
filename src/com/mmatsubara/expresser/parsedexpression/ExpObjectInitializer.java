package com.mmatsubara.expresser.parsedexpression;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpObject;
import com.mmatsubara.expresser.type.IExpObject;

/**
 * オブジェクトを初期化します。（新しいオブジェクトを生成します）<br/>
 * <br/>
 * 作成日: 2005/03/06
 * 
 * @author m.matsubara
 */
public class ExpObjectInitializer implements IParsedExpression {
    private Integer[] propertyIds;
    private IParsedExpression[] propertyExpressions;
    
    public ExpObjectInitializer(Integer[] propertyIds, IParsedExpression[] propertyExpressions) {
        this.propertyIds = propertyIds;
        this.propertyExpressions = propertyExpressions;
    }
    

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.IParsedExpression#isUsingVariablejava.lang.Integer)
     */
    public int isUsingVariable(Integer id) throws ExpException {
        int result = 0;
        int max = propertyExpressions.length;
        for (int idx = 0; idx < max; idx++) {
            result += propertyExpressions[idx].isUsingVariable(id);
        }
        return result;
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.IParsedExpression#evaluate(com.mmatsubara.expresser.RuntimeData)
     */
    public IExpObject evaluate(RuntimeData runtime) throws ExpException {
        ExpObject result = new ExpObject();
        int max = propertyIds.length;
        for (int idx = 0; idx < max; idx++) {
            IExpObject object = propertyExpressions[idx].evaluate(runtime);
            result.putProperty(propertyIds[idx], object);
        }
        return result;
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
     * @see com.mmatsubara.expresser.IParsedExpression#isConst()
     */
    public boolean isConst() {
        return false;
    }

}
