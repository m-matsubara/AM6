package com.mmatsubara.interpreter.ecmascript.parser;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.Range;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.parsedexpression.IParsedExpression;
import com.mmatsubara.interpreter.ScriptEngine;
import com.mmatsubara.interpreter.statement.BlockStatement;
import com.mmatsubara.interpreter.statement.WhileStatement;

/**
 * while ステートメントです。<br/>
 * 
 * 作成日: 2005/01/31
 * 
 * @author m.matsubara
 *
 */
public class WhileStatementParser implements IStatementParser {
    public String targetStatement() {
        return "while";
    }
    
    /* (Javadoc なし)
     * @see com.mmatsubara.interpreter.ecmascript.parser.IStatementParser#parseStatement(com.mmatsubara.interpreter.ScriptEngine, com.mmatsubara.interpreter.statement.BlockStatement, java.lang.String, char[], int, int, java.lang.Integer)
     */
    public int parseStatement(ScriptEngine se, BlockStatement blockStatement, String statementWord, char[] script, int begin, int end, Integer labelID) throws ExpException {
        int idx = begin;
        int statementLength = statementWord.length();
        
        int conditionBeginIdx = Expresser.skipWhitespace(script, idx + statementLength, end);
        if (script[conditionBeginIdx] != '(')
            throw new ExpException(ScriptEngine.getResString("NecessaryParentheses"), conditionBeginIdx);
        int conditionEndIdx = Expresser.skipParenthesis(script, conditionBeginIdx, end);
        Range conditionRange = new Range(conditionBeginIdx + 1, conditionEndIdx - 1);
        IParsedExpression condition = se.getExpresser().parse(script, conditionBeginIdx + 1, conditionEndIdx - 1);

        idx = conditionEndIdx;
        BlockStatement loopStatement = new BlockStatement();
        idx = Expresser.skipWhitespace(script, idx, end);
        idx = se.parseStatement1(loopStatement, script, idx, end);
        WhileStatement statement = new WhileStatement(condition, conditionRange, loopStatement.getStatement(0), labelID);
        blockStatement.add(statement);
        
        return idx;
    }
}
