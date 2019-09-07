package com.mmatsubara.interpreter.ecmascript.parser;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.parsedexpression.IParsedExpression;
import com.mmatsubara.expresser.parsedexpression.WrappedExpression;
import com.mmatsubara.interpreter.ScriptEngine;
import com.mmatsubara.interpreter.statement.BlockStatement;
import com.mmatsubara.interpreter.statement.SpecialStatement;

/**
 * return ステートメントです。<br/>
 * 
 * 作成日: 2005/02/06
 * 
 * @author m.matsubara
 *
 */
public class ReturnStatementParser implements IStatementParser {
    public String targetStatement() {
        return "return";
    }
    
    /* (Javadoc なし)
     * @see com.mmatsubara.interpreter.ecmascript.parser.IStatementParser#parseStatement(com.mmatsubara.interpreter.ScriptEngine, com.mmatsubara.interpreter.statement.BlockStatement, java.lang.String, char[], int, int, java.lang.Integer)
     */
    public int parseStatement(ScriptEngine se, BlockStatement blockStatement, String statementWord, char[] script, int begin, int end, Integer labelID) throws ExpException {
        int idx = begin;
        int statementLength = statementWord.length();

        idx = Expresser.skipWhitespace(script, idx + statementLength, end);
        
        IParsedExpression expression;
        
        if (script[idx] == ';') {
            expression = new WrappedExpression(Expresser.UNDEFINED);
        } else {
            int beginIdx = idx;
            idx = ScriptEngine.skipStatement(script, idx, end);
            int endIdx = idx - 1;
            expression = se.getExpresser().parse(script, beginIdx, endIdx);
        }
        
        SpecialStatement statement = new SpecialStatement(SpecialStatement.SS_TYPE_RETURN, expression);
        blockStatement.add(statement);
        return idx;
    }
}
