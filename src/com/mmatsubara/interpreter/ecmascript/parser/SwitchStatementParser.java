package com.mmatsubara.interpreter.ecmascript.parser;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.parsedexpression.IParsedExpression;
import com.mmatsubara.expresser.parsedexpression.WrappedExpression;
import com.mmatsubara.interpreter.ScriptEngine;
import com.mmatsubara.interpreter.statement.BlockStatement;
import com.mmatsubara.interpreter.statement.CaseBlockStatement;

/**
 * switchステートメントパーサーです。<br/>
 * 
 * 作成日: 2005/02/26
 * 
 * @author m.matsubara
 */
public class SwitchStatementParser implements IStatementParser {
    /* (非 Javadoc)
     * @see com.mmatsubara.interpreter.ecmascript.parser.IStatementParser#targetStatement()
     */
    public String targetStatement() {
        return "switch";
    }

    /* (Javadoc なし)
     * @see com.mmatsubara.interpreter.ecmascript.parser.IStatementParser#parseStatement(com.mmatsubara.interpreter.ScriptEngine, com.mmatsubara.interpreter.statement.BlockStatement, java.lang.String, char[], int, int, java.lang.Integer)
     */
    public int parseStatement(ScriptEngine se, BlockStatement blockStatement, String statementWord, char[] script, int begin, int end, Integer labelID) throws ExpException {
        int idx = begin;
        int statementLength = statementWord.length();

        int caseValueBeginIdx = Expresser.skipWhitespace(script, idx + statementLength, end);
        if (script[caseValueBeginIdx] != '(')
            throw new ExpException(ScriptEngine.getResString("NecessaryParentheses"), caseValueBeginIdx);
        int caseValueEndIdx = Expresser.skipParenthesis(script, caseValueBeginIdx, end);
        IParsedExpression caseExpression = se.getExpresser().parse(script, caseValueBeginIdx + 1, caseValueEndIdx - 1);
        
        idx = caseValueEndIdx;
        idx = Expresser.skipWhitespace(script, idx, end);
        if (script[idx] != '{') {
            throw new ExpException(ScriptEngine.getResString("CaseBlockNotFound"), idx);
        }
        int caseBlockBeginIdx = idx;
        int caseBlockEndIdx = Expresser.skipParenthesis(script, caseBlockBeginIdx, end);
        
        CaseBlockStatement caseBlockStatement = new CaseBlockStatement(caseExpression);

        parseCaseBlock(se, caseBlockStatement, script, caseBlockBeginIdx + 1, caseBlockEndIdx - 1);
        
        blockStatement.add(caseBlockStatement);
        
        idx = caseBlockEndIdx + 1;
        
        return idx;
    }
    
    
    /**
     * ケースブロックを解析する
     * @param se スクリプトエンジン
     * @param caseBlockStatement ブロックステートメント
     * @param script スクリプト
     * @param begin 開始位置
     * @param end 終了位置
     * @throws ExpException
     * @throws ExpException
     */
    public void parseCaseBlock(ScriptEngine se, CaseBlockStatement caseBlockStatement, char[] script, int begin, int end) throws ExpException, ExpException {
        while (begin < end) {
            begin = Expresser.skipWhitespace(script, begin, end);
            
            String word = se.getExpresser().nextToken(script, begin, end);
            if (word.equals("case")) {
                int caseIdx = begin + word.length();
                caseIdx = Expresser.skipWhitespace(script, caseIdx, end);
                int caseValueBeginIdx = caseIdx;
                while (caseIdx < end && script[caseIdx] != ':' && script[caseIdx] != '\n')
                    caseIdx++;
                if (script[caseIdx] != ':')
                    throw new ExpException(ScriptEngine.getResString("ColonNotFound"), caseIdx);
                IParsedExpression caseExpresion = se.getExpresser().parse(script, caseValueBeginIdx, caseIdx);
                if ((caseExpresion instanceof WrappedExpression) == false)
                    throw new ExpException(ScriptEngine.getResString("NecessaryConstant"), caseIdx);
                Expresser expresser = se.getExpresser();
                RuntimeData runtime = expresser.getDefaultRuntimeData(); 
                Object caseValue = Expresser.evaluate(runtime, caseExpresion);
                caseBlockStatement.addCaseLabel(caseValue);
                begin = caseIdx + 1;
            } else if (word.equals("default")) {
                int caseIdx = begin + word.length();
                caseIdx = Expresser.skipWhitespace(script, caseIdx, end);
                if (script[caseIdx] != ':')
                    throw new ExpException(ScriptEngine.getResString("ColonNotFound"), caseIdx);
                caseBlockStatement.addCaseLabel(null);
                begin = caseIdx + 1;
            } else {
                begin = se.parseStatement1(caseBlockStatement, script, begin, end);
            }
        }
        return;
        
    }
}
