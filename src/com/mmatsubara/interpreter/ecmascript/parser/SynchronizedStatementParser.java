package com.mmatsubara.interpreter.ecmascript.parser;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.parsedexpression.IParsedExpression;
import com.mmatsubara.interpreter.ScriptEngine;
import com.mmatsubara.interpreter.statement.BlockStatement;
import com.mmatsubara.interpreter.statement.SynchronizedStatement;

/**
 * synchronized ステートメントです。<br/>
 * <br/>
 * 作成日: 2005/05/17
 * 
 * @author m.matsubara
 *
 */
public class SynchronizedStatementParser implements IStatementParser {
    public String targetStatement() {
        return "synchronized";
    }
    
    /* (Javadoc なし)
     * @see com.mmatsubara.interpreter.ecmascript.parser.IStatementParser#parseStatement(com.mmatsubara.interpreter.ScriptEngine, com.mmatsubara.interpreter.statement.BlockStatement, java.lang.String, char[], int, int, java.lang.Integer)
     */
    public int parseStatement(ScriptEngine se, BlockStatement blockStatement, String statementWord, char[] script, int begin, int end, Integer labelID) throws ExpException {
        int idx = begin;
        int statementLength = statementWord.length();
        
        int objectBeginIdx = Expresser.skipWhitespace(script, idx + statementLength, end);
        if (script[objectBeginIdx] != '(')
            throw new ExpException(ScriptEngine.getResString("NecessaryParentheses"), objectBeginIdx);
        int objectEndIdx = Expresser.skipParenthesis(script, objectBeginIdx, end);
        IParsedExpression synchronizedExpression = se.getExpresser().parse(script, objectBeginIdx + 1, objectEndIdx - 1);

        idx = objectEndIdx;
        BlockStatement loopStatement = new BlockStatement();
        idx = Expresser.skipWhitespace(script, idx, end);
        idx = se.parseStatement1(loopStatement, script, idx, end);
        SynchronizedStatement statement = new SynchronizedStatement(synchronizedExpression, loopStatement.getStatement(0));
        blockStatement.add(statement);
        
        return idx;
    }
}
