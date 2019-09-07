package com.mmatsubara.expresser.operatorset;

import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.IExpObject;

/**
 * 演算子セットのインターフェイスを規定します。<br/>
 * <br/>
 * 作成日: 2004/12/31
 * @author m.matsubara
 *
 */
public interface IOperatorSet {
    /**
     * 指定された演算子で演算を行います。
     * @param operator 演算子
     * @param operand1 演算対象１ 演算対象１がない場合は Expresser.UNDIFINED が渡される。二項演算子の場合は左オペランド
     * @param operand2 演算対象２ 演算対象２がない場合は Expresser.UNDIFINED が渡される。二項演算子の場合は右オペランド
     * @param runtime ランタイムデータ
     * @return 演算結果 このクラスで処理できない演算子や演算対象の場合、Expresser.UNDIFINED を返す。
     */
    public IExpObject operation(int operator, IExpObject operand1, IExpObject operand2, RuntimeData runtime) throws ExpException;
    
    /**
     * 演算子セットの対応するクラスを返します。
     * @return 対応するクラスの配列
     */
    public Class[] targetType();
}
