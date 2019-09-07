package com.mmatsubara.interpreter.ecmascript.parser;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.Range;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.interpreter.ScriptEngine;
import com.mmatsubara.interpreter.statement.BlockStatement;
import com.mmatsubara.interpreter.statement.FunctionStatement;
import com.mmatsubara.interpreter.statement.IStatement;

/**
 * Function ステートメント（関数の定義）です。<br/>
 * 
 * 作成日: 2005/02/03
 * 
 * @author m.matsubara
 *
 */
public class FunctionStatementParser implements IStatementParser {
    public String targetStatement() {
        return "function";
    }
    
    /* (Javadoc なし)
     * @see com.mmatsubara.interpreter.ecmascript.parser.IStatementParser#parseStatement(com.mmatsubara.interpreter.ScriptEngine, com.mmatsubara.interpreter.statement.BlockStatement, java.lang.String, char[], int, int, java.lang.Integer)
     */
    public int parseStatement(ScriptEngine se, BlockStatement blockStatement, String statementWord, char[] script, int begin, int end, Integer labelID) throws ExpException {
        int idx = begin;
        int statementLength = statementWord.length();

        idx = Expresser.skipWhitespace(script, idx + statementLength, end);
        String functionName = "";
        
        //  関数名を取得
        if (script[idx] != '(') {
            functionName = se.getExpresser().nextToken(script, idx, end);
            idx += functionName.length();
            idx = Expresser.skipWhitespace(script, idx, end);
        }
        
        //  関数仮引数を解析する
        if (script[idx] != '(')
            throw new ExpException(ScriptEngine.getResString("NecessaryParentheses"), idx);
        int argBegin = idx;
        idx = Expresser.skipParenthesis(script, idx, end);
        int argEnd = idx; 
        Range[] range = Expresser.split(',', script, argBegin + 1, argEnd - 1);
        int max = range.length; 

        //  唯一の項目が空なら削除する
        if (max == 1 && (range[0].getBegin() == range[0].getEnd()))
            max = 0;
        
        Integer[] argIds = new Integer[max];
        for (int argIdx = 0; argIdx < max; argIdx++) {
            int beginIdx = range[argIdx].getBegin();
            int endIdx   = range[argIdx].getEnd();
            String argName = new String(script, beginIdx, endIdx - beginIdx);
            if (se.getExpresser().isIdentifier(argName) == false)
                throw new ExpException(ScriptEngine.getResString("IllegalIdentifier", argName), beginIdx); 
            argIds[argIdx] = StringToId.toId(argName);
        }
        idx = Expresser.skipWhitespace(script, idx, end);
        
        //  関数仮引数を解析する
        if (script[idx] != '{')
            throw new ExpException(ScriptEngine.getResString("FunctionBodyNotFound"), idx);
        int funcBodyBegin = idx + 1;
        idx = Expresser.skipParenthesis(script, idx, end);
        int funcBodyEnd = idx - 1;
        IStatement functionBody = se.parseStatement(script, funcBodyBegin, funcBodyEnd);
        FunctionStatement functionStatement;
        if ("".equals(functionName) == false)
            functionStatement = new FunctionStatement(functionName, StringToId.toId(functionName), argIds);
        else
            functionStatement = new FunctionStatement(functionName, null, argIds);
            
        //  関数本体の追加
        int functionBodySize = ((BlockStatement)functionBody).size();
        if (functionBodySize == 1)
            functionStatement.setStatement(((BlockStatement)functionBody).getStatement(0));
        else
            functionStatement.setStatement(functionBody);    
        
        blockStatement.add(functionStatement);
        
        return idx;
    }
}
