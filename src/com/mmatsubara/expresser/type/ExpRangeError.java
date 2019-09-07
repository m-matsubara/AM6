package com.mmatsubara.expresser.type;

import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;

public class ExpRangeError extends ExpError {
    public static IExpObject constructor = null; 

    private static final Integer property$message = StringToId.toId("message");

    public ExpRangeError(Object message) throws ExpException {
        super("RangeError");
        getConstructorList().add(constructor);
        
        putProperty(property$message, new _String(message));
        setThisPrototype(getPrototype());
    }

}
