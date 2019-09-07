package com.mmatsubara.interpreter.ecmascript.parser;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.Range;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.parsedexpression.IParsedExpression;
import com.mmatsubara.interpreter.ScriptEngine;
import com.mmatsubara.interpreter.statement.BlockStatement;
import com.mmatsubara.interpreter.statement.WhileStatement;

/**
 * do-while ステートメントです。<br/>
 * 
 * 作成日: 2005/03/27
 * 
 * @author m.matsubara
 *
 */
public class DoWhileStatementParser implements IStatementParser {
    public String targetStatement() {
        return "do";
    }
    
    /* (Javadoc なし)
     * @see com.mmatsubara.interpreter.ecmascript.parser.IStatementParser#parseStatement(com.mmatsubara.interpreter.ScriptEngine, com.mmatsubara.interpreter.statement.BlockStatement, java.lang.String, char[], int, int, java.lang.Integer)
     */
    public int parseStatement(ScriptEngine se, BlockStatement blockStatement, String statementWord, char[] script, int begin, int end, Integer labelID) throws ExpException {
        int idx = begin;
        int nStatementLength = statementWord.length();
        idx = Expresser.skipWhitespace(script, idx + nStatementLength, end);

        //  ループ本体
        BlockStatement loopStatement = new BlockStatement();
        idx = se.parseStatement1(loopStatement, script, idx, end);
        
        //  while キーワード
        idx = Expresser.skipWhitespace(script, idx, end);
        String s = se.getExpresser().nextToken(script, idx, end);
        if ("while".equals(s) == false) {
            throw new ExpException(ScriptEngine.getResString("NecessaryIdentifier", "while"), idx);
        }
        idx += s.length();

        //  条件式
        int conditionBeginIdx = Expresser.skipWhitespace(script, idx, end);
        if (script[conditionBeginIdx] != '(')
            throw new ExpException(ScriptEngine.getResString("NecessaryParentheses"), conditionBeginIdx);
        int conditionEndIdx = Expresser.skipParenthesis(script, conditionBeginIdx, end);
        Range conditionRange = new Range(conditionBeginIdx + 1, conditionEndIdx - 1);
        IParsedExpression condition = se.getExpresser().parse(script, conditionBeginIdx + 1, conditionEndIdx - 1);

        idx = conditionEndIdx;
        
        
        WhileStatement statement = new WhileStatement(condition, conditionRange, loopStatement.getStatement(0), labelID);
        statement.setDoWhile(true);
        blockStatement.add(statement);
        
        return idx;
    }
}
