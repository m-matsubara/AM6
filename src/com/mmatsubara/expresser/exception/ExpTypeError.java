package com.mmatsubara.expresser.exception;

/**
 * 型変換時のエラーです。
 * 
 * 作成日: 2005/05/22
 * 
 * @author m.matsubara
 */

public class ExpTypeError extends ExpException {
    /**
     * シリアルバージョンUIDです。（Eclipse3.1だと明示的に定義しないと怒られるので（？））<br/>
     * シリアライズしない（ハズ）なので本来必要ないはずです。 
     */
    private static final long serialVersionUID = -2572630933497737237L;

    public ExpTypeError(String message, int position) {
        super(message, position);
    }
}
