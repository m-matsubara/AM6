package com.mmatsubara.expresser;

import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.parsedexpression.IParsedExpression;

/**
 * 独自に定義される（演算子の）演算対象のパーサーです<br/>
 * 演算対象が通常の式ではない場合に独自にパーサーを用意できます<br/>
 * <br/>
 * 作成日: 2005/02/13
 * 
 * @author m.matsubara
 */
public interface IOperatorParser {
    
    /**
     * 独自定義の演算子を解釈します
     * 
     * @param expresser 呼び出し元 expresser
     * @param statement ステートメント
     * @param begin オペランド開始位置
     * @param end オペランド終了位置
     * @param opeIdx 演算子位置
     * @param opeWord 演算子の文字列表現
     * @return 解析された計算式
     * @throws ExpException
     */
    public IParsedExpression parse(Expresser expresser, char[] statement, int begin, int end, int opeIdx, String opeWord) throws ExpException;
}
