package com.mmatsubara.interpreter.ecmascript.parser;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.interpreter.ScriptEngine;
import com.mmatsubara.interpreter.statement.BlockStatement;
import com.mmatsubara.interpreter.statement.SpecialStatement;

/**
 * continue ステートメントです。<br/>
 * 
 * 作成日: 2005/02/20
 * 
 * @author m.matsubara
 *
 */
public class ContinueStatementParser implements IStatementParser {
    /* (Javadoc なし)
     * @see com.mmatsubara.interpreter.ecmascript.parser.IStatementParser#targetStatement()
     */
    public String targetStatement() {
        return "continue";
    }
    
    /* (Javadoc なし)
     * @see com.mmatsubara.interpreter.ecmascript.parser.IStatementParser#parseStatement(com.mmatsubara.interpreter.ScriptEngine, com.mmatsubara.interpreter.statement.BlockStatement, java.lang.String, char[], int, int, java.lang.Integer)
     */
    public int parseStatement(ScriptEngine se, BlockStatement blockStatement, String statementWord, char[] script, int begin, int end, Integer labelID) throws ExpException {
        int idx = begin;
        int nStatementLength = statementWord.length();
        labelID = null; //渡されたlabelIDは無視

        idx = Expresser.skipWhitespace(script, idx + nStatementLength, end);
        if (script[idx] != ';') {
            String label = se.getExpresser().nextToken(script, idx, end);
            if (se.getExpresser().isIdentifier(label) == false)
                throw new ExpException(ScriptEngine.getResString("IllegalIdentifier", label), idx);
            labelID = StringToId.toId(label);
            idx += label.length();    
            if (script[idx] != ';')
                throw new ExpException(ScriptEngine.getResString("SyntaxError"), idx);
        }
        idx++;
        SpecialStatement statement = new SpecialStatement(SpecialStatement.SS_TYPE_CONTINUE, labelID);
        blockStatement.add(statement);
        return idx;
    }
}
