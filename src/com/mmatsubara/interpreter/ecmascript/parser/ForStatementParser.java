package com.mmatsubara.interpreter.ecmascript.parser;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.Range;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.parsedexpression.IParsedExpression;
import com.mmatsubara.expresser.parsedexpression.WrappedExpression;
import com.mmatsubara.interpreter.ScriptEngine;
import com.mmatsubara.interpreter.statement.BlockStatement;
import com.mmatsubara.interpreter.statement.ForInStatement;
import com.mmatsubara.interpreter.statement.ForStatement;
import com.mmatsubara.interpreter.statement.VariableDefineStatement;

/**
 * for ステートメントです。<br/>
 * 
 * 作成日: 2005/01/31
 * 
 * @author m.matsubara
 *
 */
public class ForStatementParser implements IStatementParser {
    public String targetStatement() {
        return "for";
    }
    
    /* (Javadoc なし)
     * @see com.mmatsubara.interpreter.ecmascript.parser.IStatementParser#parseStatement(com.mmatsubara.interpreter.ScriptEngine, com.mmatsubara.interpreter.statement.BlockStatement, java.lang.String, char[], int, int, java.lang.Integer)
     */
    public int parseStatement(ScriptEngine se, BlockStatement blockStatement, String statementWord, char[] script, int begin, int end, Integer labelID) throws ExpException {
        int idx = begin;
        int statementLength = statementWord.length();

        int controlBeginIdx = Expresser.skipWhitespace(script, idx + statementLength, end);
        if (script[controlBeginIdx] != '(')
            throw new ExpException(ScriptEngine.getResString("NecessaryParentheses"), controlBeginIdx);
        int controlEndIdx = Expresser.skipParenthesis(script, controlBeginIdx, end);
        
        Range[] control = Expresser.split(';', script, controlBeginIdx + 1, controlEndIdx - 1);
        if (control.length != 3) {
            return parseForInStatement(se, blockStatement, statementWord, script, begin, end, labelID);
            //throw new ExpException(ScriptEngine.getResString("NecessarySemicolon"), controlBeginIdx + 1);
        }

        //  for 文実行部の解析
        idx = controlEndIdx;
        BlockStatement loopStatement = new BlockStatement();
        idx = Expresser.skipWhitespace(script, idx, end);
        idx = se.parseStatement1(loopStatement, script, idx, end);

        //  ループの初期化
        IParsedExpression initialExpression = null;
        if (control[0].getBegin() < control[0].getEnd()) {
            String word = se.getExpresser().nextToken(script, control[0].getBegin(), control[0].getEnd());    
            if ("var".equals(word)) {
                VarStatementParser varParser = new VarStatementParser();
                varParser.parseStatement(se, blockStatement, statementWord, script, control[0].getBegin(), control[1].getBegin(), null);    //  control[0].getEnd() でないのははセミコロンを認識させるため
            } else
                initialExpression = se.getExpresser().parse(script, control[0]);
        }
        //  ループの継続条件
        IParsedExpression condition = null;
        if (control[1].getBegin() < control[1].getEnd())
            condition = se.getExpresser().parse(script, control[1]); 
        //  ループ１回ごとに実行する式
        IParsedExpression loopEndExpression = null;
        if (control[2].getBegin() < control[2].getEnd())
            loopEndExpression = se.getExpresser().parse(script, control[2]); 
        ForStatement statement = new ForStatement(initialExpression, condition, loopEndExpression, loopStatement.getStatement(0), control[1].getBegin(), labelID);
        blockStatement.add(statement);
        
        return idx;
    }
    
    /**
     * For-in ステートメントを解析する
     * @param se スクリプトエンジン
     * @param block このブロックステートメントの最後に解析されたステートメントが追加される
     * @param statementWird 構文解析するステートメントの最初の単語
     * @param script ステートメント
     * @param begin 構文解析開始位置
     * @param end 構文解析終了位置
     * @param labelID ステートメントにラベルが付く場合そのID・つかない場合は null
     * @return 構文解析の次の開始位置
     * @throws ExpException 構文エラー
     */
    public int parseForInStatement(ScriptEngine se, BlockStatement blockStatement, String statementWord, char[] script, int begin, int end, Integer labelID) throws ExpException {
        Expresser expresser = se.getExpresser();
        int idx = begin;
        int statementLength = statementWord.length();

        int controlBeginIdx = Expresser.skipWhitespace(script, idx + statementLength, end);
        if (script[controlBeginIdx] != '(')
            throw new ExpException(ScriptEngine.getResString("NecessaryParentheses"), controlBeginIdx);
        int controlEndIdx = Expresser.skipParenthesis(script, controlBeginIdx, end);
        idx = controlBeginIdx + 1;
        
        //  For-in は for (variable in object) ... 形式をとる
        
        boolean useVar = false;
        idx = Expresser.skipWhitespace(script, idx, controlEndIdx - 1);
        String variable = expresser.nextToken(script, idx, controlEndIdx - 1);
        
        if ((expresser.isIdentifier(variable) == false) && (variable.equals("var") == false)) 
            throw new ExpException(ScriptEngine.getResString("IllegalIdentifier", variable), idx);
        idx += variable.length();
        idx = Expresser.skipWhitespace(script, idx, controlEndIdx - 1);
        if (variable.equals("var")) {
        	useVar = true;
            variable = expresser.nextToken(script, idx, controlEndIdx - 1);
            idx += variable.length();
            idx = Expresser.skipWhitespace(script, idx, controlEndIdx - 1);
        }
        
        String inWord = expresser.nextToken(script, idx, controlEndIdx - 1);
        if (inWord.equals("in") == false)
            throw new ExpException(ScriptEngine.getResString("SyntaxError", variable), idx);
        idx += inWord.length();
        idx = Expresser.skipWhitespace(script, idx, controlEndIdx);
        
        IParsedExpression objectExpression = se.getExpresser().parse(script, idx, controlEndIdx - 1);
        Integer variableID = StringToId.toId(variable);
        
        //  for 文実行部の解析
        idx = controlEndIdx;
        BlockStatement loopStatement = new BlockStatement();
        idx = Expresser.skipWhitespace(script, idx, end);
        idx = se.parseStatement1(loopStatement, script, idx, end);

        //  ループの初期化
        if (useVar) {
        	VariableDefineStatement varStatement = new VariableDefineStatement(variableID, new WrappedExpression(Expresser.UNDEFINED));
            blockStatement.add(varStatement);
        }
        ForInStatement statement = new ForInStatement(variableID, objectExpression, loopStatement, labelID);
        blockStatement.add(statement);
        
        return idx;
        
        
    }
}
