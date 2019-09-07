package com.mmatsubara.expresser.type;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.exception.ExpException;

/**
 * プリミティブ型の文字型です。<br/>
 * このプログラム内では文字列の構成要素というより、１文字のみの文字列として扱われます。<br/>
 * <br/>
 * 作成日: 2005/07/24
 * 
 * @author m.matsubara
 *
 */
public class _Character extends _String {
    public _Character(String s) throws ExpException {
        super((s != null && s.length() >= 1) ? s.substring(0, 1) : " ");
        if (s == null || s.length() == 0)
            throw new ExpException(Expresser.getResString("FailureCreate_Character_EmptyString"), -1);
    }
    
    public char toChar() {
        return toString().charAt(0);
    }
}
