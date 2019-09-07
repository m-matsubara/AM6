package com.mmatsubara.interpreter.ecmascript.parser;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.parsedexpression.IParsedExpression;
import com.mmatsubara.expresser.parsedexpression.WrappedExpression;
import com.mmatsubara.interpreter.ScriptEngine;
import com.mmatsubara.interpreter.statement.BlockStatement;
import com.mmatsubara.interpreter.statement.Statement;

/**
 * 式ステートメントです。<br/>
 * 
 * 作成日: 2005/01/31
 * 
 * @author m.matsubara
 *
 */
public class ExpressionStatementParser implements IStatementParser {
    public String targetStatement() {
        return "";
    }
    
    /* (Javadoc なし)
     * @see com.mmatsubara.interpreter.ecmascript.parser.IStatementParser#parseStatement(com.mmatsubara.interpreter.ScriptEngine, com.mmatsubara.interpreter.statement.BlockStatement, java.lang.String, char[], int, int, java.lang.Integer)
     */
    public int parseStatement(ScriptEngine se, BlockStatement blockStatement, String statementWord, char[] script, int begin, int end, Integer labelID) throws ExpException {
        boolean lastTokenOperator = true; 
        int idx = begin;

        int stBeginIdx = idx;
        while (idx < end) {
            char ch = script[idx];
            if (ch == ';') {
                break;
            } else if (ch == ' ' || ch == '\t' || ch == '\n') {
                idx++;
            } else    if (ch == '(' || ch == '{' || ch == '[') {
                idx = Expresser.skipParenthesis(script, idx, end);
                lastTokenOperator = false;
            } else if (ch == '\"' || ch == '\'') {
                idx = Expresser.skipString(script, idx, end);
                lastTokenOperator = false;
            } else if (ch == '/' && lastTokenOperator) {
                idx = Expresser.skipRegExp(script, idx, end);
                lastTokenOperator = false;
            } else {
                lastTokenOperator = se.getExpresser().isOperator(script, idx, end);
                int nextIdx = se.getExpresser().skipToken(script, idx, end);
                if (idx != nextIdx)
                    idx = nextIdx;
                else {
                    idx++;  //  予想しない文字…ここでは簡易判定という扱いなため、エラーにはしない（しても良い気がする）
                }
            }
        }
        int stEndIdx = idx;
        idx++;
        if (stBeginIdx < stEndIdx) {
            IParsedExpression parsedExpression = se.getExpresser().parse(script, stBeginIdx, stEndIdx);
            if (parsedExpression instanceof WrappedExpression)
                throw new ExpException(ScriptEngine.getResString("MeaninglessStatement"), stBeginIdx);
            Statement statement = new Statement(parsedExpression, stBeginIdx);
            blockStatement.add(statement);
        }

        return idx;
    }
}
