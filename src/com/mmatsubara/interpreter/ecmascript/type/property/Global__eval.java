package com.mmatsubara.interpreter.ecmascript.type.property;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.ITypeConverter;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.interpreter.ParsedScript;
import com.mmatsubara.interpreter.ScriptEngine;

/**
 * Globalオブジェクトの eval() メソッドです。<br/>
 * このメソッドはScriptEngineに依存するため、expresserの下ではなく、ここにある。
 * <br/>
 * 作成日: 2005/07/28
 * 
 * @author m.matsubara
 */
public class Global__eval extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("eval");
    private static IExpObject instance = null;  

    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Global__eval() throws ExpException {
        super("eval", 1);
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        ITypeConverter typeConverter = runtime.getTypeConverter();
        if (arguments.length < 1)
            throw new ExpException(Expresser.getResString("ArgumentsNumberError"), -1);

        String script = typeConverter.toString(runtime, arguments[0]).toString();
        ScriptEngine scriptEngine = new ScriptEngine();
        scriptEngine.getExpresser().setDefaultRuntimeData(runtime);
        ParsedScript parcedScript = scriptEngine.parse(script);
        parcedScript.execute(runtime);
        
        return runtime.getLastResultValue();
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Global__eval();
        return instance;
    }

    public void putProperty(Integer id, IExpObject value) throws ExpException {
        // TODO Auto-generated method stub
    	super.putProperty(id, value);
        
    }

    public void putProperty(int index, IExpObject value) throws ExpException {
        // TODO Auto-generated method stub
    	super.putProperty(index, value);
    }
}
