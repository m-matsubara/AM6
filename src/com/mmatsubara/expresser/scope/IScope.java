package com.mmatsubara.expresser.scope;

import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.IExpObject;

/**
 * 変数アクセスの際のスコープを定義します。<br/>
 * <br/>
 * 作成日: 2005/06/04
 * 
 * @author m.matsubara
 */
public interface IScope {
    /**
     * 変数オブジェクトを取得します。
     * @param id 変数ID
     * @return 変数オブジェクト
     */
    public IVariable getVariable(Integer id) throws ExpException;
    
    /**
     * 変数オブジェクトを取得します。
     * @param id 変数ID
     * @param offset 変数へ高速にアクセスするためのパラメータ
     * @return 変数オブジェクト
     */
    public IVariable getVariable(Integer id, int offset) throws ExpException;

    /**
     * 変数の値を返します。
     * @param id 変数ID
     * @return 変数の値
     */
    public IExpObject getValue(Integer id) throws ExpException;

    /**
     * 変数の値を返します。
     * @param id 変数ID
     * @param offset 変数へ高速にアクセスするためのパラメータ
     * @return 変数の値
     */
    public IExpObject getValue(Integer id, int offset) throws ExpException;

    /**
     * 変数の値を設定します。
     * @param id 変数ID
     * @param value 値
     */
    public void setValue(Integer id, IExpObject value) throws ExpException;
    
    /**
     * 変数の値を設定します。
     * @param id 変数ID
     * @param offset 変数へ高速にアクセスするためのパラメータ
     * @param value 値
     */
    public void setValue(Integer id, int offset, IExpObject value) throws ExpException;
    
    /**
     * IDで示される新しい変数を作成し登録します。
     * @param id 変数ID
     * @return 登録された変数
     */
    public IVariable defineVariable(Integer id) throws ExpException; 

    /**
     * 自身で解決できないスコープの変数のためのスコープチェインを設定します。<br/>
     * 親スコープを設定します。<br/>
     * @param nextScope 親スコープ
     */
    public void setScopeChain(IScope nextScope);

    /**
     * 自身で解決できないスコープの変数のためのスコープチェインを取得します。
     * @return nextScope 親スコープ
     */
    public IScope getScopeChain();
    
    /**
     * 変数に高速にアクセスするためのオフセット値を取得します。<br/>
     * 変数に高速にアクセスするためのオフセット値をサポートしないスコープは0を返します。<br/>
     * @param id 変数ID
     * @return 変数に高速にアクセスするためのオフセット値
     */
    public int getOffset(Integer id);
}
