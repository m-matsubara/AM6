package com.mmatsubara.expresser;

import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.IExpObject;

/**
 * 変数を実現するためのインターフェイスです。<br/>
 * <br/>
 * @author m.matsubara
 * 
 * 2005/01/09
 */
public interface IVariable extends IExpObject {
    public IExpObject getValue() throws ExpException;
    
    public void setValue(IExpObject value) throws ExpException;
    
    public void setConst(boolean constFlag) throws ExpException;
    
    public boolean isConst() throws ExpException;
}
