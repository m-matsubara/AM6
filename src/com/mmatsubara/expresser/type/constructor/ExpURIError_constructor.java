package com.mmatsubara.expresser.type.constructor;

import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpError;

/**
 * URIError型のコンストラクタです。<br/>
 * <br/>
 * 作成日: 2011/05/29
 * @author matsubara
 *
 */
public class ExpURIError_constructor extends ExpError_constructor {
    /**
     * Error型のコンストラクタを生成します。 
     * @throws ExpException
     */
    public ExpURIError_constructor() throws ExpException {
        super();
        putProperty(StringToId.toId("prototype"), ExpError.getPrototype());
        functionBody = "URIError";
    }
}
