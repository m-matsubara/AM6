package com.mmatsubara.interpreter.ecmascript.parser;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.parsedexpression.IParsedExpression;
import com.mmatsubara.interpreter.ScriptEngine;
import com.mmatsubara.interpreter.statement.BlockStatement;
import com.mmatsubara.interpreter.statement.IStatement;
import com.mmatsubara.interpreter.statement.VariableDefineStatement;

/**
 * var ステートメントです。<br/> 
 * 
 * 作成日: 2005/03/07
 * 
 * @author m.matsubara
 */
public class VarStatementParser implements IStatementParser {
    public String targetStatement() {
        return "var";
    }
    
    /* (Javadoc なし)
     * @see com.mmatsubara.interpreter.ecmascript.parser.IStatementParser#parseStatement(com.mmatsubara.interpreter.ScriptEngine, com.mmatsubara.interpreter.statement.BlockStatement, java.lang.String, char[], int, int, java.lang.Integer)
     */
    public int parseStatement(ScriptEngine se, BlockStatement blockStatement, String statementWord, char[] script, int begin, int end, Integer labelID) throws ExpException {
        int idx = begin;
        int nStatementLength = statementWord.length();
        Integer id;
        IParsedExpression initialExpression = null;
        idx = Expresser.skipWhitespace(script, idx + nStatementLength, end);
        
        while (idx < end) {
            //  識別子（変数名）の取得
            int beginIdx = idx;
            int endIdx;
            String identifier = se.getExpresser().nextToken(script, beginIdx, end);
            idx = idx + identifier.length(); 
            id = StringToId.toId(identifier);
            idx = Expresser.skipWhitespace(script, idx, end);
    
            //  初期化式
            if (idx + 1 < end && script[idx] == '=') {
                idx = Expresser.skipWhitespace(script, idx + 1, end);
                beginIdx = idx;
                idx = skipExpression(script, beginIdx, end);
                endIdx = idx - 1;
                initialExpression = se.getExpresser().parse(script, beginIdx, endIdx);
                idx--;
            }
            IStatement statement = new VariableDefineStatement(id, initialExpression);
            blockStatement.add(statement);
            if (script[idx] != ',')
                break;
            idx++;
            idx = Expresser.skipWhitespace(script, idx, end);
        }
        if (idx < end) {
            if (script[idx] != ';')
                throw new ExpException(ScriptEngine.getResString("NecessarySemicolon"), idx);
            else
                idx++;
        }
        
        return idx;
    }
    
    /**
     * その式の終わりまで読み飛ばします。<br/>
     * カンマでも式の終わりと判断することに注意しなければなりません。
     * @param script スクリプト
     * @param begin 開始位置
     * @param end 終了位置
     * @return セミコロンまたはカンマの位置+1
     * @throws ExpException
     */
    public static int skipExpression(char[] script, int begin, int end) throws ExpException {
        int idx = begin;
        while (idx < end) {
            char ch = script[idx];
            if (ch == '{' || ch == '(' || ch == '[')
                idx = Expresser.skipParenthesis(script, idx, end);
            else if (ch == '\"' || ch == '\'')
                idx = Expresser.skipString(script, idx, end);
            else if (ch == ';' || ch == ',')
                return idx + 1;
            else {
                idx++;
            }
        }
        return end;
    }
}
