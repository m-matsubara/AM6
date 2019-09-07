package com.mmatsubara.interpreter.statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;


/**
 * {} で括られた実行ブロックです。<br/>
 * 
 * 作成日: 2005/01/29
 * 
 * @author m.matsubara
 *
 */
public class BlockStatement implements IStatement {
    protected List block = new ArrayList();
    protected IStatement[] blockArray = null;
    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#isUsingVariable(Integer)
     */
    public int isUsingVariable(Integer id) throws ExpException {
        prepareBlockArray();
        
        int result = 0;
        int max = blockArray.length;
        for (int idx = 0; idx < max; idx++) {
            result += blockArray[idx].isUsingVariable(id);
        }
        return result;
    }
    
    /**
     * blockArrayを準備します。
     *
     */
    public void prepareBlockArray() {
        if (blockArray == null) {
            blockArray = new IStatement[block.size()];
            for (int idx = 0; idx < blockArray.length; idx++)
                blockArray[idx] = (IStatement)block.get(idx);
        }
    }
    
    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#execute(com.mmatsubara.expresser.RuntimeData)
     */
    public StatementResult execute(RuntimeData runtime) throws ExpException {
        prepareBlockArray();

        int max = blockArray.length;
        for (int idx = 0; idx < max; idx++) {
            StatementResult receive = blockArray[idx].execute(runtime);
            if (receive != null) {
                return receive;
            }
        }
        return null;
    }

    /**
     * ステートメントを追加する<br>
     * 関数ステートメント（関数の登録を行うステートメント）はブロックの前側に纏めて登録する
     * @param statement
     */
    public void add(IStatement statement) {
        if (statement instanceof FunctionStatement) {
            int idx = 0;
            int max = block.size(); 
            for (idx = 0; idx < max; idx++) {
                if (block.get(idx) instanceof FunctionStatement == false)
                    break;
            }
            block.add(idx, statement);
        } else {
            block.add(statement);
        }
        blockArray = null;
    }
    
    /**
     * 指定された位置のステートメントを取得する<br/>
     * インデックスが範囲外の場合 null を返す
     * @param idx 位置
     * @return ステートメント
     */
    public IStatement getStatement(int idx) {
        if (idx < 0 || block.size() <= idx)
            return null;
        return (IStatement)block.get(idx);
    }
    
    /**
     * 指定された位置のステートメントを設定する<br/>
     * @param idx 位置
     * @param statement 置き換えるステートメント 
     */
    public void setStatement(int idx, IStatement statement) {
        block.set(idx, statement);
    }
    
    /**
     * 指定された位置のステートメントを削除する<br/>
     * @param idx 位置
     * @return 削除されたステートメント
     */
    public IStatement removeStatement(int idx) {
        return (IStatement)block.remove(idx);
    }
    
    /**
     * すべてのステートメントを削除する
     *
     */
    public void clearStatements() {
        block.clear();
    }
    
    /**
     * ステートメントの個数を得る 
     * @return ステートメントの個数
     */
    public int size() {
        return block.size();
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.interpreter.statement.IStatement#registLocalVariable(java.util.Map)
     */
    public void registLocalVariable(Map varMap) {
        prepareBlockArray();

        int max = blockArray.length;
        for (int idx = 0; idx < max; idx++)
            blockArray[idx].registLocalVariable(varMap);
        return;
    }
}
