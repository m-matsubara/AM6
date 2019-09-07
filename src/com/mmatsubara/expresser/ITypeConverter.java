package com.mmatsubara.expresser;

import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Boolean;
import com.mmatsubara.expresser.type._Double;
import com.mmatsubara.expresser.type._String;

/**
 * 型変換を行うインターフェイスです。<br/>
 * 
 * 作成日: 2005/05/22
 * 
 * @author m.matsubara
 */

public interface ITypeConverter {
    public IExpObject toPrimitive(RuntimeData runtime, IExpObject value) throws ExpException;
    public _Boolean toBoolean(RuntimeData runtime, IExpObject value) throws ExpException;
    public _Double toNumber(RuntimeData runtime, IExpObject value) throws ExpException;
    public _String toString(RuntimeData runtime, IExpObject value) throws ExpException;
    public IExpObject toObject(RuntimeData runtime, IExpObject value) throws ExpException;
}
