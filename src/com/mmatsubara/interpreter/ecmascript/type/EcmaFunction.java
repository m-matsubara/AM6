package com.mmatsubara.interpreter.ecmascript.type;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.Variable;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.scope.IScope;
import com.mmatsubara.expresser.scope.Scope;
import com.mmatsubara.expresser.type.ExpArray;
import com.mmatsubara.expresser.type.ExpObject;
import com.mmatsubara.expresser.type.IArray;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type.IScriptFunction;
import com.mmatsubara.expresser.type.property.Function__apply;
import com.mmatsubara.expresser.type.property.Function__call;
import com.mmatsubara.interpreter.ScriptEngine;
import com.mmatsubara.interpreter.statement.IStatement;
import com.mmatsubara.interpreter.statement.StatementResult;
import com.mmatsubara.interpreter.statement.StatementResultNotification;

/**
 * Function型です。<br/>
 * <br/>
 * 作成日: 2005/08/13
 * 
 * @author m.matsubara
 */
public class EcmaFunction extends ExpObject implements IScriptFunction {
    public static IExpObject constructor = null;
    private static final Integer THIS_ID = StringToId.toId("this");            //  thisオブジェクトを登録するときのID
    private static final Integer ARGUMENTS_ID = StringToId.toId("arguments");    //  argumentsIdオブジェクトを登録するときのID

    /**
     * 引数IDリスト
     */
    private Integer[] argIds;
    /**
     * 関数呼び出し時に呼び出されるステートメント
     */
    private IStatement functionBody;
    /**
     * ローカルスコープで変数が見つからない場合に使用されるスコープ
     */
    private IScope nextScope;
    
    
    /**
     * ローカルスコープの雛形<br/>
     * 関数呼び出しごとにこの雛形から新しいスコープが作成される
     */
    private Scope defaultLocalScope;

    /**
     * プロトタイプ
     */
    private static ExpObject prototype = null;
    
    /**
     * コンストラクタ
     * @param argIds 引数IDリスト
     * @param nextScope 次のスコープチェイン
     * @throws ExpException
     */
    public EcmaFunction(Integer[] argIds, IStatement functionBody, IScope nextScope) throws ExpException {
        super("Function", constructor);
        this.argIds = argIds;
        this.functionBody = functionBody;
        this.nextScope = nextScope;
        setupLocalVariable();
        
        setThisPrototype(getPrototype());
    }

    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject,
            IExpObject[] arguments) throws ExpException {
        if (functionBody == null)
            return Expresser.UNDEFINED;

        IExpObject result;
        
        IScope backupCurrentScope = runtime.getCurrentScope();
        IScope scope = newLocalScope(runtime);
        runtime.setCurrentScope(scope);
        try {
            //  this オブジェクトの登録
            if (thisObject == Expresser.UNDEFINED)
                thisObject = this;  //  TODO ホントか？
            scope.setValue(THIS_ID, thisObject);
            result = thisObject;

            //  引数配列を変数セットに格納する

            IVariable[] argumentsVars = new IVariable[arguments.length];
            for (int idx = 0; idx < arguments.length; idx++)
                argumentsVars[idx] = new Variable(arguments[idx]);

            Arguments argumentsObject = new Arguments(argumentsVars, this);
            scope.setValue(ARGUMENTS_ID, argumentsObject);
            
            int max = (argIds.length < arguments.length) ? argIds.length : arguments.length;
            try {
                Scope localScope = (Scope)scope;
                for (int idx = 0; idx < max; idx++) {
                    //localScope.setVariable(argIds[idx], argArray.getVariable(idx));
                    localScope.setVariable(argIds[idx], argumentsVars[idx]);
                }
                for (int idx = max; idx < argIds.length; idx++)
                    localScope.setValue(argIds[idx], Expresser.UNDEFINED);
            } catch (ClassCastException cce) {
                throw new ExpException(ScriptEngine.getResString("InternalError", "FunctionStatement#callFunction", "set variable for scope object"), -1);
            }
            
            
            //  関数本体を実行
            StatementResult receive = functionBody.execute(runtime);
            if (receive != null) {
                int statementType = receive.getStatementType();
                if (statementType == StatementResult.ST_RETURN)
                    return receive.getResultValue();
                if (statementType == StatementResult.ST_BREAK)
                    throw new ExpException(ScriptEngine.getResString("NotProcessedBreak"), -1);
                if (statementType == StatementResult.ST_CONTINUE)
                    throw new ExpException(ScriptEngine.getResString("NotProcessedContinue"), -1);
                if (statementType == StatementResult.ST_EXCEPTION)
                    throw new StatementResultNotification("throw exception.", -1, receive);
            }
        } finally {
            runtime.setCurrentScope(backupCurrentScope);
        }
        return result;
    }

    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.type.IScriptFunction#newLocalScope(com.mmatsubara.expresser.RuntimeData)
     */
    public IScope newLocalScope(RuntimeData runtime) {
        Scope newScope = defaultLocalScope.copyScope();
        newScope.setScopeChain(nextScope);
        return newScope;
    }

    /**
     * 関数に対して、利用するローカル変数情報を初期化するなどして使用可能にします。
     *
     */
    public void setupLocalVariable() throws ExpException {
        //  スタックポインタに対するインデックスを登録する
        Map variableMap = new HashMap();    //  IdentityHashMap 使いたい…
        defaultLocalScope = new Scope();
        
        functionBody.registLocalVariable(variableMap);

        //  this, arguments を登録
        defaultLocalScope.defineVariable(THIS_ID);
        defaultLocalScope.defineVariable(ARGUMENTS_ID);
        
        //  var ステートメントの変数を登録
        Iterator iterator = variableMap.keySet().iterator();
        while (iterator.hasNext()) {
            Object id = iterator.next();
            defaultLocalScope.defineVariable((Integer)id);
        }
        //  引数を登録
        for (int idx = 0; idx < argIds.length; idx++) {
            defaultLocalScope.defineVariable(argIds[idx]);
        }
        //  この後に変数を追加しない
        defaultLocalScope.fixed();
    }
    

    /**
     * prototypeを取得します。
     * @return prototype 
     * @throws ExpException
     */
    public static synchronized ExpObject getPrototype() throws ExpException {
        if (prototype == null) {
            prototype = new ExpObject(ExpObject.getPrototype());
            prototype.putProperty(Function__apply.ID,   Function__apply.getInstance());
            prototype.putProperty(Function__call.ID,    Function__call.getInstance());
        }
        return prototype;
    }
}


/**
 * 関数呼び出し時のargumentsオブジェクトを表すクラスです。<br/>
 * Arrayオブジェクトの生成はコストが大きいため、実際に使用されたときに初めてArrayオブジェクトが生成されるようにしています。
 * @author m.matsubara
 */
class Arguments implements IExpObject, IArray {
    private static final Integer CALLEE_ID = StringToId.toId("callee");        //  argumentsのcalleeプロパティのID
    ExpArray array;
    IVariable[] arguments; 
    IExpObject callee;
    
    Arguments(IVariable[] arguments, IExpObject callee) {
        this.array = null;
        this.arguments = arguments;
        this.callee = callee;
    }
    
    /**
     * Argumentsオブジェクトの実体を生成します。
     * @return Argumentsオブジェクトの実体
     * @throws ExpException
     */
    public ExpArray getArray() {
        if (array == null) {
            try {
                array = new ExpArray(arguments);
                array.putProperty(CALLEE_ID, callee);
            } catch (ExpException e) {
                throw new RuntimeException(e.getMessage() + " : " + Integer.toString(e.getPosition()));
            }
        }
        return array;
    }
    
    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getProperty(java.lang.Integer)
     */
    public IExpObject getProperty(Integer id) throws ExpException {
        return getArray().getProperty(id);
    }

    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#putProperty(java.lang.Integer, java.lang.Object)
     */
    public void putProperty(Integer id, IExpObject value) throws ExpException {
        getArray().putProperty(id, value);
    }

    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getProperty(int)
     */
    public IExpObject getProperty(int index) throws ExpException {
        return getArray().getProperty(index);
    }

    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#putProperty(int, java.lang.Object)
     */
    public void putProperty(int index, IExpObject value) throws ExpException {
        getArray().putProperty(index, value);
    }

    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#isProperty(java.lang.Integer, boolean)
     */
    public boolean isProperty(Integer id, boolean searchPrototype) throws ExpException {
        return getArray().isProperty(id, searchPrototype);
    }

    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#deleteProperty(java.lang.Integer)
     */
    public IExpObject deleteProperty(Integer id) throws ExpException {
        return getArray().deleteProperty(id);
    }

    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getConstructorList()
     */
    public List getConstructorList() {
        return getArray().getConstructorList();
    }

    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getVariable(java.lang.Integer)
     */
    public IVariable getVariable(Integer id) {
        return getArray().getVariable(id);
    }

    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getVariable(int)
     */
    public IVariable getVariable(int index) {
        return getArray().getVariable(index);
    }

    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#enumProperty()
     */
    public Integer[] enumProperty() {
        return getArray().enumProperty();
    }

    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.type.IArray#length()
     */
    public int length() {
        return getArray().length();
    }
    
    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.type.IArray#setLength(int)
     */
    public void setLength(int length) throws ExpException {
        getArray().setLength(length);        
    }

    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.type.IArray#toArray()
     */
    public IExpObject[] toArray() throws ExpException {
        return getArray().toArray();
    }
    
}