package com.mmatsubara.expresser;

import java.io.InputStream;
import java.io.PrintStream;

import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.scope.IScope;
import com.mmatsubara.expresser.scope.ObjectScope;
import com.mmatsubara.expresser.scope.SequentialScope;
import com.mmatsubara.expresser.type.IExpObject;


/**
 * ランタイム情報です。<br/>
 * 変数のスコープや標準入出力、型変換オブジェクトを管理します。<br/>
 * スレッドごとに作成する必要があります。<br/>
 * 各スレッド用にはExpresserの持つ、defaultRuntimeを利用してグローバルエリアを共有したRuntimeDataを作成する必要があります。<br/>
 * <br/> 
 * 作成日: 2005/05/14
 * 
 * @author m.matsubara
 */
public class RuntimeData {
	/** グローバルレベルのスコープです。通常はスコープチェインの最後に位置します。 */
    IScope globalScope;
    /** 現在使用されるスコープチェインの先頭です */
    IScope currentScope;
    
    /** 標準出力です */
    private PrintStream stdout;
    /** 標準エラー出力です */
    private PrintStream stderr;
    /** 標準入力です */
    private InputStream stdin;
    
    /**
     * 最後に演算が行われたときの結果を保持します。
     */
    IExpObject lastResultValue = null;
    
    /**
     * 型変換オブジェクトです。<br/>
     * コンストラクタで初期化されないので明示的にsetterにて設定する必要があります。
     */
    ITypeConverter typeConverter = null;
    
    /**
     * オリジナルのランタイムデータとグローバルエリアを共有したランタイムデータを作成します。<br/>
     * @param originalRuntime オリジナルのランタイムデータ 
     */
    public RuntimeData(RuntimeData originalRuntime) {
        globalScope = originalRuntime.globalScope;
        currentScope = globalScope;

        stdout = originalRuntime.stdout;
        stderr = originalRuntime.stderr;
        stdin  = originalRuntime.stdin;
    }
    
    
    /**
     * ランタイムデータを作成します。<br/>
     * グローバル変数は他のRuntimeDataと共有されません。
     */
    public RuntimeData() {
        globalScope = new SequentialScope(256);
        currentScope = globalScope;

        stdout = System.out;
        stderr = System.err;
        stdin  = System.in;
    }
    
    /**
     * 変数オフセット値を取得します。
     * 
     * @param id 変数ID
     * @return 変数オブジェクト
     */
    public int getVariableOffset(Integer id) {
        int result = currentScope.getOffset(id);
        //System.out.println(StringToId.toString(id) + ":" + String.valueOf(result));
        return result;
    }

    /**
     * 変数を取得します。
     * 
     * @param id 変数ID
     * @param create 変数がない場合作成するか
     * @return 変数オブジェクト
     */
    public IVariable getVariable(Integer id, boolean create) throws ExpException {
        IVariable variable = currentScope.getVariable(id);
        if (variable == null && create) {
            variable = globalScope.defineVariable(id);
          variable.setValue(Variable.UNINITIALIZED);
//          variable.setValue(Expresser.UNDEFINED);
        }
        return variable;
    }
    
    /**
     * 変数を取得する
     * 
     * @param id 変数ID
     * @param offset 変数アクセスのオフセット値
     * @return 変数オブジェクト
     */
    public IVariable getVariable(Integer id, int offset) throws ExpException {
        if (offset > 0)
        	return currentScope.getVariable(id, offset);
        else
            return currentScope.getVariable(id);
    }
    
    /**
     * 変数に対して値を設定する<br/>
     * 指定されたIDを持つ変数が見つからない場合グローバルエリアに登録される
     * @param id 変数ID
     * @param object 値
     */
    public void setValue(Integer id, IExpObject object) throws ExpException {
        try {
        	currentScope.setValue(id, object);
        } catch (ExpException ee) {
        	IVariable variable = globalScope.defineVariable(id);
            variable.setValue(object);
        }
    }
    
    /**
     * 変数に対して値を設定する<br/>
     * 指定されたIDを持つ変数が見つからない場合グローバルエリアに登録される
     * @param name 変数名
     * @param object 値
     */
    public void setValue(String name, IExpObject object) throws ExpException {
        setValue(StringToId.toId(name), object);
    }

    /**
     * 変数に対して値を設定する<br/>
     * 指定されたIDを持つ変数が見つからない場合グローバルエリアに登録される
     * 
     * @param id 変数ID
     * @param offset 変数へのオフセット値
     * @param object 値
     */
    public void setValue(Integer id, int offset, IExpObject object) throws ExpException {
        if (offset > 0)
        	currentScope.setValue(id, offset, object);
        else
            currentScope.setValue(id, object);
    }
    
    /**
     * 変数値を取得する<br/>
     * 指定されたIDを持つ変数が見つからない場合 undefined を返す
     * @param id 変数ID
     * @param offset 変数へのオフセット値
     * @exception ExpException
     */
    public IExpObject getValue(Integer id, int offset) throws ExpException {
        if (offset > 0)
        	return currentScope.getValue(id, offset);
        else
            return currentScope.getValue(id);
    }

    /**
     * 変数値を取得する<br/>
     * 指定されたIDを持つ変数が見つからない場合 undefined を返す
     * @param id 変数ID
     * @exception ExpException
     */
    public IExpObject getValue(Integer id) throws ExpException {
        return currentScope.getValue(id);
    }
    
    /**
     * 定数を設定する
     * @param id 定数ID
     * @param object 設定する値
     * @exception ExpException
     */
    public void setConst(Integer id, IExpObject object) throws ExpException {
        IVariable variable = getVariable(id, true);
        variable.setValue(object);
        variable.setConst(true);
    }
    
    /**
     * 定数を設定する
     * @param name 定数名
     * @param object 設定する値
     * @exception ExpException
     */
    public void setConst(String name, IExpObject object) throws ExpException {
        setConst(StringToId.toId(name), object);
    }
    
    /**
     * グローバルスコープを取得します。
     * @return カレントスコープ
     */
    public IScope getGlobalScope() {
        return globalScope;
    }
    
    /**
     * カレントスコープを取得します。
     * @return カレントスコープ
     */
    public IScope getCurrentScope() {
    	return currentScope;
    }
    
    /**
     * 新しいカレントスコープを設定します。<br/>
     * 新しいカレントスコープはスコープチェインも設定されていなければなりません。<br/>
     * もしスコープチェインが設定されていない場合はグローバル変数などへのアクセスができなくなります。（意図的にできなくすることもできます。）
     * @param newCurrentScope 新しいカレントスコープ
     */
    public void setCurrentScope(IScope newCurrentScope) {
        this.currentScope = newCurrentScope;
    }
    
    
    /**
     * with オブジェクトの追加をします。
     * @param object オブジェクト
     */
    public void addWithObject(IExpObject object) {
        IScope nextChain = currentScope;
        currentScope = new ObjectScope(object);
        currentScope.setScopeChain(nextChain);
    }
    
    /**
     * with オブジェクトを除去します。</br>
     * 誤ってaddWithObjectより多く呼び出すとカレントスコープがnullとなり、NullPointerException が発生する原因となります。
     */
    public void removeWithObject() {
        currentScope = currentScope.getScopeChain();
    }
    
    /**
     * @return タイプコンバーターを戻します。
     */
    public ITypeConverter getTypeConverter() {
        return typeConverter;
    }
    /**
     * @param typeConverter タイプコンバーターを設定します。
     */
    public void setTypeConverter(ITypeConverter typeConverter) {
        this.typeConverter = typeConverter;
    }
    
    /**
     * 標準出力を返します。 
     * @return 標準出力
     */
    public PrintStream getStdout() {
        return stdout;
    }
    /**
     * 標準出力を設定します。
     * @param stderr 標準出力
     */
    public void setStdout(PrintStream stdout) {
        this.stdout = stdout;
    }

    /**
     * 標準エラー出力を返します。 
     * @return 標準エラー出力
     */
    public PrintStream getStderr() {
        return stderr;
    }
    /**
     * 標準エラー出力を設定します。
     * @param stderr 標準エラー出力
     */
    public void setStderr(PrintStream stderr) {
        this.stderr = stderr;
    }

    /**
     * 標準入力を返します。 
     * @return 標準入力
     */
    public InputStream getStdin() {
        return stdin;
    }
    /**
     * 標準入力を設定します。
     * @param stderr 標準入力
     */
    public void setStdin(InputStream stdin) {
        this.stdin = stdin;
    }


    /**
     * 現時点での最後の演算結果を取得します。
     * @return 最後の演算結果
     */
    public IExpObject getLastResultValue() {
        return lastResultValue;
    }


    /**
     * 現時点での最後の演算結果を設定します。
     * @param lastResultValue 演算結果
     */
    public void setLastResultValue(IExpObject lastResultValue) {
        this.lastResultValue = lastResultValue;
    }
}
