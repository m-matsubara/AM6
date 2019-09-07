package com.mmatsubara.expresser.type;

import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;

public class ExpTypeError extends ExpError {
    public static IExpObject constructor = null; 

    private static final Integer property$message = StringToId.toId("message");

    public ExpTypeError(Object message) throws ExpException {
        super("TypeError");
        getConstructorList().add(constructor);
        
        putProperty(property$message, new _String(message));
        setThisPrototype(getPrototype());
    }

}
