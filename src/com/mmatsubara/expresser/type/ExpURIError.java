package com.mmatsubara.expresser.type;

import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;

public class ExpURIError extends ExpError {
    public static IExpObject constructor = null; 

    private static final Integer property$message = StringToId.toId("message");

    public ExpURIError(Object message) throws ExpException {
        super("URIError");
        getConstructorList().add(constructor);
        
        putProperty(property$message, new _String(message));
        setThisPrototype(getPrototype());
    }

}
