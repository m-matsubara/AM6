package com.mmatsubara.expresser.type;

/**
 * 数字型を規定するオブジェクトです。<br/>
 * Number インターフェイスとほぼ等価な役割をします。
 * <br/>
 * 作成日: 2005/07/24
 * 
 * @author m.matsubara
 */
public abstract class ExpNumberBase {
    public abstract int intValue();
    public abstract long longValue();
    public abstract float floatValue();
    public abstract double doubleValue();
    public byte byteValue() {
        return (byte)intValue();
    }
    public short shortValue() {
        return (short)intValue();
    }

}
