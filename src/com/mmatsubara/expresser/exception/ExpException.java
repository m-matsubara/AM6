package com.mmatsubara.expresser.exception;

/**
 * com.mmatsubara.expresser.Expresser クラス用の例外オブジェクトです。<br/>
 * <br/>
 * 作成日: 2004/12/31
 * 
 * @author m.matsubara
 *
 */
public class ExpException extends Exception {
    /**
     * シリアルバージョンUIDです。（Eclipse3.1だと明示的に定義しないと怒られるので（？））<br/>
     * シリアライズしない（ハズ）なので本来必要ないはずです。 
     */

    private static final long serialVersionUID = 7005639773529406729L;
    /**
     * エラー位置
     */
    private int position = 0; 
    
    /**
     * com.mmatsubara.expresser.Expresser クラス用の例外オブジェクトを初期化します
     * @param sMessage エラーメッセージ
     * @param position エラー位置
     */
    public ExpException(String sMessage, int position) {
        super(sMessage);
        this.position = position;
    }
    
    /**
     * エラー位置を取得します
     * @return エラー位置
     */
    public int getPosition() {
        return position;
    }

    /**
     * エラー位置を設定します
     * 
     * @param position エラー位置
     */
    public void setPosition(int position) {
        this.position = position;
    }
    
    /* (非 Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getMessage() + " : " + getPosition();
    }
}
