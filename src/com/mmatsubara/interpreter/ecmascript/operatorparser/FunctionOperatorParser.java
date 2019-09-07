package com.mmatsubara.interpreter.ecmascript.operatorparser;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.IOperatorParser;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.parsedexpression.IParsedExpression;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.interpreter.ScriptEngine;
import com.mmatsubara.interpreter.ecmascript.parser.FunctionStatementParser;
import com.mmatsubara.interpreter.ecmascript.type.EcmaFunction;
import com.mmatsubara.interpreter.statement.BlockStatement;
import com.mmatsubara.interpreter.statement.FunctionStatement;
import com.mmatsubara.interpreter.statement.IStatement;

/**
 * 匿名関数パーサーです。<br/>
 * 
 * 作成日: 2005/02/13
 * 
 * @author m.matsubara
 *
 */
public class FunctionOperatorParser implements IOperatorParser {
    private ScriptEngine scriptEngine;
    
    public FunctionOperatorParser(ScriptEngine se) {
        this.scriptEngine = se;
    }
    

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.IOperatorParser#parse(Expresser expresser, char[], int, int, int, String)
     */
    public IParsedExpression parse(Expresser expresser, char[] statement, int begin, int end, int opeIdx, String opeWord)  throws ExpException {
        if (begin != opeIdx)
            throw new ExpException(ScriptEngine.getResString("FunctionDefineError"), begin);
        
        FunctionStatementParser functionStatementParser = new FunctionStatementParser();
        BlockStatement workBlockStatement = new BlockStatement(); 
        functionStatementParser.parseStatement(scriptEngine, workBlockStatement, "", statement, opeIdx + opeWord.length(), end, null);
        FunctionStatement functionStatement = (FunctionStatement)workBlockStatement.getStatement(0);
        
        //  関数を利用可能にする
        //functionStatement.execute(null);
        
        return new FunctionOperatorExpression(functionStatement.getArgIds(), functionStatement.getStatement());
    }
}

class FunctionOperatorExpression implements IParsedExpression {
    /**
     * 引数IDリスト
     */
    private Integer[] argIds;
    /**
     * 関数呼び出し時に呼び出されるステートメント
     */
    private IStatement functionBody;
    
    public FunctionOperatorExpression(Integer[] argIds, IStatement functionBody) {
        this.argIds = argIds;
        this.functionBody = functionBody;
    }
    

	/* (非 Javadoc)
	 * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#isUsingVariable(java.lang.Integer)
	 */
	public int isUsingVariable(Integer id) throws ExpException {
		//return functionBody.isUsingVariable(id);
        return 0;
	}

	/* (非 Javadoc)
	 * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#evaluate(com.mmatsubara.expresser.RuntimeData)
	 */
	public IExpObject evaluate(RuntimeData runtime) throws ExpException {
		return new EcmaFunction(argIds, functionBody, runtime.getCurrentScope()); //  グローバルスコープではなくカレントスコープが nextScope に設定されるところがミソ
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
	 * @see com.mmatsubara.expresser.parsedexpression.IParsedExpression#isConst()
	 */
	public boolean isConst() {
		return false;
	}
}
