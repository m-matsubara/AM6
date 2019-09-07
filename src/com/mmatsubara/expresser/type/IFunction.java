package com.mmatsubara.expresser.type;

import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;

/**
 * 関数呼び出しのインターフェイスです。<br/>
 * <br/>
 * 作成日: 2005/01/16
 * 
 * @author m.matsubara
 */
public interface IFunction {
    /**
     * 関数の呼び出しを定義します。<br/>
     * このクラスで対応しない関数の場合は Expresser.UNDIFINED を返します。
     * @param runtime ランタイムデータ
     * @param thisObject this オブジェクト(nullでもよい)
     * @param arguments 引数の配列
     * @return 戻り値 
     * @throws ExpException
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments) throws ExpException;
}
