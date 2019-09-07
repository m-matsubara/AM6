package com.mmatsubara.regexp;

/**
 * 正規表現をサポートするインターフェイスです。<br/>
 * <br/>
 * 作成日: 2005/05/03
 * 
 * @author m.matsubara
 */
public interface IRegExpCore {
    /**
     * 正規表現コアオブジェクトを初期化します。
     * @param pattern パターン
     * @param flags 動作を決定するためのフラグ
     */
    public void init(String pattern, String flags) throws Exception;
    
    /**
     * パターンに合致する範囲を検索します。
     * @param source 検索する文字列
     * @return 検索結果
     */
    public RegMatchResult[] match(String source);

    /**
     * パターンが見つかるか検査します。
     * @param source 検索する文字列
     * @return 見つかる場合 true
     */
    public boolean test(String source);
}
