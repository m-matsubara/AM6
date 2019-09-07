package com.mmatsubara.interpreter.ecmascript.parser;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.Range;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.parsedexpression.IParsedExpression;
import com.mmatsubara.interpreter.ScriptEngine;
import com.mmatsubara.interpreter.statement.BlockStatement;
import com.mmatsubara.interpreter.statement.IStatement;
import com.mmatsubara.interpreter.statement.IfStatement;

/**
 * if ステートメントです。
 * 
 * 作成日: 2005/01/31
 * 
 * @author m.matsubara
 *
 */
public class IfStatementParser implements IStatementParser {
    public String targetStatement() {
        return "if";
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
        BlockStatement thenWorkStatement = null;
        BlockStatement elseWorkStatement = null;
        IStatement thenStatement = null;
        IStatement elseStatement = null;
        idx = Expresser.skipWhitespace(script, idx, end);
        if ("else".equals(se.getExpresser().nextToken(script, idx, end)) == false) {    //  thenの内容が無く、いきなり else が来ることがある。
            thenWorkStatement = new BlockStatement();
            idx = se.parseStatement1(thenWorkStatement, script, idx, end);
            thenStatement = thenWorkStatement.getStatement(0);
        }
        
        idx = Expresser.skipWhitespace(script, idx, end);
        String sElse = se.getExpresser().nextToken(script, idx, end);
        if (sElse.equals("else")) {
            elseWorkStatement = new BlockStatement();
            idx = Expresser.skipWhitespace(script, idx + 4, end);
            idx = se.parseStatement1(elseWorkStatement, script, idx, end);
            elseStatement = elseWorkStatement.getStatement(0);
        }
        IfStatement statement = new IfStatement(condition, conditionRange, thenStatement, elseStatement);
        blockStatement.add(statement);
        
        return idx;
    }
}
