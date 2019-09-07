package com.mmatsubara.expresser.type;

import com.mmatsubara.expresser.exception.*;

/**
 * 配列のインターフェイスです。<br/>
 * <br/>
 * 作成日: 2005/01/22
 * 
 * @author m.matsubara
 */
public interface IArray extends IExpObject {
    /**
     * 配列のサイズを返します。
     * @return 配列のサイズ
     */
    public int length();
    
    /**
     * 配列のサイズを設定します。
     * @param length
     */
    public void setLength(int length) throws ExpException;
    
    /**
     * Javaの配列を返します。
     * @return Javaの配列 
     * @throws ExpException
     */
    public IExpObject[] toArray() throws ExpException;

}
