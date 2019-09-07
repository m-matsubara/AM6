package com.mmatsubara.regexp;

/**
 * 正規表現をでsearchメソッドの戻り型に使われます。<br/>
 * <br/>
 * 作成日: 2005/05/03
 * 
 * @author m.matsubara
 */
public class RegMatchResult {
    private int begin;
    private int end;
    
    public RegMatchResult(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }
    
    /**
     * 正規表現で検索結果の開始位置を返します。
     * @return 開始位置
     */
    public int getBegin() {
        return begin;
    }
    
    /**
     * 正規表現で検索結果の終了位置を返します。
     * @return 終了位置
     */
    public int getEnd() {
        return end;
    }

}
