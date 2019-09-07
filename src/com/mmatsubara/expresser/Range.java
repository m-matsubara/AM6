package com.mmatsubara.expresser;

/**
 * 実行範囲を保持します。<br/>
 * <br/>
 * 作成日: 2005/01/16
 * 
 * @author m.matsubara
 */
public class Range {
    private int begin;
    private int end;
    
    /**
     * 範囲オブジェクトを初期化します
     * @param begin 開始位置
     * @param end 終了位置
     */
    public Range(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }
    
    /**
     * @return begin を戻します。
     */
    public int getBegin() {
        return begin;
    }
    /**
     * @param begin begin を設定します。
     */
    public void setBegin(int begin) {
        this.begin = begin;
    }
    /**
     * @return end を戻します。
     */
    public int getEnd() {
        return end;
    }
    /**
     * @param end end を設定します。
     */
    public void setEnd(int end) {
        this.end = end;
    }

}
