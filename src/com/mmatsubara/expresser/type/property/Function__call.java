package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type.IFunction;


/**
 * EcmaScript Functionオブジェクトのcallメソッドです。<br/>
 * 
 * 作成日: 2005/03/05
 * 
 * @author m.matsubara
 */
public class Function__call extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("call");
    private static IExpObject instance = null;
    
    /**
     * Functionオブジェクトのcallメソッドを定義します。
     * 
     */
    private Function__call() throws ExpException {
        super("call", 1);
    }
    
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments) throws ExpException {
        IFunction superFunction = (IFunction)thisObject;    //  継承元オブジェクトの関数
        IExpObject callThisObject = (IExpObject)arguments[0];
        if (arguments.length < 1)
            throw new ExpException(Expresser.getResString("ArgumentsNumberError"), -1);
        
        IExpObject[] callArguments = new IExpObject[arguments.length - 1];
        int max = callArguments.length;
        for (int idx = 0; idx < max; idx++) {
            callArguments[idx] = arguments[idx + 1];
        }
            
        return superFunction.callFunction(runtime, callThisObject, callArguments);
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Function__call();
        return instance;
    }
}
