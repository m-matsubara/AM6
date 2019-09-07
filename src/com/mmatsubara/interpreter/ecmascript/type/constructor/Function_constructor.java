package com.mmatsubara.interpreter.ecmascript.type.constructor;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.ITypeConverter;
import com.mmatsubara.expresser.Range;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpObject;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type.constructor.ExpObject_constructor;
import com.mmatsubara.interpreter.ScriptEngine;
import com.mmatsubara.interpreter.ecmascript.type.EcmaFunction;
import com.mmatsubara.interpreter.statement.IStatement;

/**
 * Function型のコンストラクタです。<br/>
 * <br/>
 * 作成日: 2005/05/03
 * 
 * @author m.matsubara
 */
public class Function_constructor extends ExpFunctionBase {
    /**
     * 関数を解析するためのスクリプトエンジンです。
     */
    ScriptEngine scriptEngine;
    
    /**
     * Function型のコンストラクタを生成します。 
     * @throws ExpException
     */
    public Function_constructor(ScriptEngine scriptEngine) throws ExpException {
        super("Function", 1);
        putProperty(StringToId.toId("prototype"), ExpObject.getPrototype());
        
        this.scriptEngine = scriptEngine;
    }


    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        Integer[] argIds; 
        char[] functionBody;
        ITypeConverter typeConverter = runtime.getTypeConverter();
        
        if (arguments.length == 0) {
            //  引数なし・ボディなし（空関数）
            argIds = new Integer[0];
            functionBody = "".toCharArray();
        } else if (arguments.length == 1) {
            //  引数なし・ボディあり
            argIds = new Integer[0];
            
            //  ボディの取得
            functionBody = typeConverter.toString(runtime, arguments[0]).toString().toCharArray();
            ScriptEngine.deleteComents(functionBody, 0, functionBody.length);
        } else {
            //  引数あり・ボディあり
            
            //  引数の解析
            char[] argIdChars = typeConverter.toString(runtime, arguments[0]).toString().toCharArray();
            ScriptEngine.deleteComents(argIdChars, 0, argIdChars.length);
            Range[] range = Expresser.split(',', argIdChars, 0, argIdChars.length);
            argIds = new Integer[range.length];
            for (int idx = 0; idx < argIds.length; idx++) {
                String argName = String.valueOf(argIdChars, range[idx].getBegin(), range[idx].getEnd() - range[idx].getBegin());
                argIds[idx] = StringToId.toId(argName);
            }
            
            //  ボディの取得
            functionBody = typeConverter.toString(runtime, arguments[1]).toString().toCharArray();
            ScriptEngine.deleteComents(functionBody, 0, functionBody.length);
        }
        try {
            IStatement functionBodyStatement = scriptEngine.parseStatement(functionBody, 0, functionBody.length);  
            EcmaFunction function = new EcmaFunction(argIds, functionBodyStatement, runtime.getCurrentScope());     //  グローバルスコープではなくカレントスコープが nextScope に設定されるところがミソ
            
            return function;
        } catch (ExpException ee) {
            ee.setPosition(-1); // ソースの文字列自体が違うので正しい値を示していない。このため -1で値をつぶし、上位の catch で改めて設定してもらう。
            throw ee;
        }
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getProperty(java.lang.Integer)
     */
    public IExpObject getProperty(Integer id) throws ExpException {
        if (id == ExpObject_constructor.propertyId$prototype)
            return ExpObject.getPrototype();
        return super.getProperty(id);
    }
}
