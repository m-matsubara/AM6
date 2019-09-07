package com.mmatsubara.interpreter.ecmascript.parser;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.interpreter.ScriptEngine;
import com.mmatsubara.interpreter.statement.*;

/**
 * while ステートメントです。<br/>
 * 
 * 作成日: 2005/02/15
 * 
 * @author m.matsubara
 *
 */
public class TryStatementParser implements IStatementParser {
    public String targetStatement() {
        return "try";
    }
    
    /* (Javadoc なし)
     * @see com.mmatsubara.interpreter.ecmascript.parser.IStatementParser#parseStatement(com.mmatsubara.interpreter.ScriptEngine, com.mmatsubara.interpreter.statement.BlockStatement, java.lang.String, char[], int, int, java.lang.Integer)
     */
    public int parseStatement(ScriptEngine se, BlockStatement blockStatement, String statementWord, char[] script, int begin, int end, Integer labelID) throws ExpException {
        int idx = begin;
        int statementLength = statementWord.length();
        
        idx = Expresser.skipWhitespace(script, idx + statementLength, end);
        
        //  try ブロックの処理
        BlockStatement workBlockStatement = new BlockStatement();
        int nTryBlockIdx = idx;
        idx = se.parseStatement1(workBlockStatement, script, idx, end);
        IStatement tryStatement = workBlockStatement.getStatement(0);
        Integer catchObjectId = null;
        IStatement catchStatement = null;
        IStatement finallyStatement = null;
        if (tryStatement instanceof BlockStatement == false)
            throw new ExpException(ScriptEngine.getResString("NecessaryBlock"), nTryBlockIdx);
        
        idx = Expresser.skipWhitespace(script, idx, end);
        String nextWord = se.getExpresser().nextToken(script, idx, end);
        
        if (nextWord.equals("catch")) {
            idx += nextWord.length();
            idx = Expresser.skipWhitespace(script, idx, end);

            //  catch の後ろの 「(identifier)」を処理する
            int catchBeginIdx = idx;
            if (script[catchBeginIdx] != '(')
                throw new ExpException(ScriptEngine.getResString("NecessaryParentheses"), catchBeginIdx);
            int catchEndIdx = Expresser.skipParenthesis(script, catchBeginIdx, end);
            // Range catchObjectNameRange = new Range(nCatchBeginIdx + 1, nCatchEndIdx - 1);
            String catchObjectName = new String(script, catchBeginIdx + 1, catchEndIdx - catchBeginIdx - 2);
            catchObjectName = Expresser.trim(catchObjectName);
            if (se.getExpresser().isIdentifier(catchObjectName) == false)
                throw new ExpException(ScriptEngine.getResString("IllegalIdentifier", catchObjectName), catchBeginIdx + 1);
            catchObjectId = StringToId.toId(catchObjectName);
            idx = catchEndIdx;
            idx = Expresser.skipWhitespace(script, idx, end);
            
            //  catch ブロックを処理
            workBlockStatement = new BlockStatement();
            idx = se.parseStatement1(workBlockStatement, script, idx, end);
            catchStatement = workBlockStatement.getStatement(0);
            if (catchStatement instanceof BlockStatement == false)
                throw new ExpException(ScriptEngine.getResString("NecessaryBlock"), nTryBlockIdx);

            idx = Expresser.skipWhitespace(script, idx, end);
            nextWord = se.getExpresser().nextToken(script, idx, end);
        }
        if (nextWord.equals("finally")) {
            idx += nextWord.length();
            idx = Expresser.skipWhitespace(script, idx, end);

            //  finally ブロックを処理
            workBlockStatement = new BlockStatement();
            idx = se.parseStatement1(workBlockStatement, script, idx, end);
            finallyStatement = workBlockStatement.getStatement(0);
            if (catchStatement instanceof BlockStatement == false)
                throw new ExpException(ScriptEngine.getResString("NecessaryBlock"), nTryBlockIdx);
        }
        //  catch または finally があること！！
        if ((catchStatement == null) && (finallyStatement == null))
            throw new ExpException(ScriptEngine.getResString("NecessaryIdentifier", "catch, finally"), idx);
        
        //  try-catch-finallyを登録
        IStatement statement = new TryStatement(tryStatement, catchObjectId, catchStatement, finallyStatement);
        blockStatement.add(statement);

        return idx;
    }
}
