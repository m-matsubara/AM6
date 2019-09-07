package com.mmatsubara.expresser.type;

import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.scope.IScope;

/**
 * スクリプトを使って定義される関数呼び出しのインターフェイスです。<br/>
 * <br/>
 * 作成日: 2005/05/14
 * 
 * @author m.matsubara
 */
public interface IScriptFunction extends IFunction {
    /**
     * このメソッドの中でローカル変数スコープの生成を行います。
     * @param runtime ランタイムデータ
     */
    public IScope newLocalScope(RuntimeData runtime);
}
