package com.mmatsubara.interpreter.ecmascript.parser;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.parsedexpression.IParsedExpression;
import com.mmatsubara.interpreter.ScriptEngine;
import com.mmatsubara.interpreter.statement.BlockStatement;
import com.mmatsubara.interpreter.statement.IStatement;
import com.mmatsubara.interpreter.statement.WithStatement;

/**
 * with ステートメントです。<br/>
 * 
 * 作成日: 2005/01/31
 * 
 * @author m.matsubara
 *
 */
public class WithStatementParser implements IStatementParser {
    public String targetStatement() {
        return "with";
    }
    
    /* (Javadoc なし)
     * @see com.mmatsubara.interpreter.ecmascript.parser.IStatementParser#parseStatement(com.mmatsubara.interpreter.ScriptEngine, com.mmatsubara.interpreter.statement.BlockStatement, java.lang.String, char[], int, int, java.lang.Integer)
     */
    public int parseStatement(ScriptEngine se, BlockStatement blockStatement, String statementWord, char[] script, int begin, int end, Integer labelID) throws ExpException {
        int idx = begin;
        int statementLength = statementWord.length();

        int withValueBeginIdx = Expresser.skipWhitespace(script, idx + statementLength, end);
        if (script[withValueBeginIdx] != '(')
            throw new ExpException(ScriptEngine.getResString("NecessaryParentheses"), withValueBeginIdx);
        int withValueEndIdx = Expresser.skipParenthesis(script, withValueBeginIdx, end);
        IParsedExpression withExpression = se.getExpresser().parse(script, withValueBeginIdx + 1, withValueEndIdx - 1);

        idx = withValueEndIdx;
        BlockStatement workStatement = null;
        IStatement withStatement = null;
        idx = Expresser.skipWhitespace(script, idx, end);
        if (script[idx] != '{')
            throw new ExpException(ScriptEngine.getResString("NecessaryBlock"), withValueBeginIdx);
            
        workStatement = new BlockStatement();
        idx = se.parseStatement1(workStatement, script, idx, end);
        withStatement = workStatement.getStatement(0);
        
        WithStatement statement = new WithStatement(withExpression, withStatement, begin);
        blockStatement.add(statement);
        
        return idx;
    }
}
