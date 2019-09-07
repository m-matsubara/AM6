package com.mmatsubara.expresser.type;

import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;

/**
 * エラー保持クラスです。<br/>
 * <br/>
 * 作成日: 2005/05/01
 * 
 * @author m.matsubara
 */
public class ExpError extends ExpObject {
    public static IExpObject constructor = null; 

    private static ExpObject prototype = null;

    private static final Integer property$message = StringToId.toId("message");

    public ExpError(Object message) throws ExpException {
        super("Error", constructor);
        putProperty(property$message, new _String(message));
        setThisPrototype(getPrototype());
    }

    /**
     * prototypeを取得します。
     * @return prototype 
     * @throws ExpException
     */
    public static ExpObject getPrototype() throws ExpException {
        if (prototype == null) {
            prototype = new ExpObject(ExpObject.getPrototype());
        }
        return prototype;
    }
    
    public String toString() {
        try {
            Object message = getProperty(property$message);
            return getObjectName() + ": " + message.toString();
        } catch (ExpException e) {
            return "";
        }
    }
    
    
}
