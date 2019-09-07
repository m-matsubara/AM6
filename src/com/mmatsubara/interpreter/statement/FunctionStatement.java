package com.mmatsubara.interpreter.statement;


import java.util.Map;

import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.interpreter.ecmascript.type.EcmaFunction;


/**
 * function ステートメントです。<br/>
 * 関数定義を行う
 * 
 * 作成日: 2005/02/03
 * 
 * @author m.matsubara
 */
public class FunctionStatement implements IStatement {
    private String functionName;
    private Integer functionId;
    private Integer[] argIds;
    private IStatement blockStatement = null;
    
    /**
     * 関数ステートメントの生成をします。
     * @param functionName 関数名
     * @param functionId 関数名ID
     * @param argIds 引数IDの配列
     */
    public FunctionStatement(String functionName, Integer functionId, Integer[] argIds) throws ExpException {
        this.functionName = functionName;
        this.functionId = functionId;
        this.argIds = argIds;
        blockStatement = null;
    }
    
    public IStatement getStatement() {
        return blockStatement;
    }

    
    /**
     * ステートメントを設定します。<br/>
     * 既にステートメントがある場合は置き換えられます。
     * @param statement
     */
    public void setStatement(IStatement statement) {
        this.blockStatement = statement;
    }
    
    /**
     * ステートメントを追加します。<br/>
     * 設定済みのステートメントの後ろに追加されます。
     * @param statement ステートメント
     */
    public void add(IStatement statement) {
        if (blockStatement == null)
            blockStatement = statement;
        else if (blockStatement instanceof BlockStatement)
            ((BlockStatement)blockStatement).add(statement);
        else {
            IStatement workStatement = blockStatement;
            blockStatement = new BlockStatement();
            ((BlockStatement)blockStatement).add(workStatement);
        }
    }

    
    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#isUsingVariable(Integer)
     */
    public int isUsingVariable(Integer id) throws ExpException {
        int count = 0;
        if (functionId == id)
            count++;
        if (blockStatement != null)
            count += blockStatement.isUsingVariable(id);
        return count++;
    }
    
    
    
    /*
     * ステートメントの実行<br/>
     * 関数の登録<br/>
     *  (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#execute(com.mmatsubara.expresser.RuntimeData)
     */
    public StatementResult execute(RuntimeData runtime) throws ExpException {
        if (runtime != null && functionId != null) {
            EcmaFunction function = new EcmaFunction(argIds, blockStatement, runtime.getCurrentScope());    //  グローバルスコープではなくカレントスコープが nextScope に設定されるところがミソ
            
            IVariable functionVar = runtime.getVariable(functionId, true);
            functionVar.setValue(function);
        }
        return null;
    }

    
    /* (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#registLocalVariable(java.util.HashMap)
     */
    public void registLocalVariable(Map varMap) {
        //  ステートメントがスコープ内で変数を定義する必要があるときは varMapにキーをセットしなければなりません。
        if (functionId != null)
            varMap.put(functionId, this);
    }
    
    /**
     * 引数IDの配列を返します。
     * @return 引数IDの配列 
     */
    public Integer[] getArgIds() {
        return argIds;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }
}
