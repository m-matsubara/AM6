package com.mmatsubara.interpreter.statement;

import java.util.Map;

import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;


/**
 * すべてのステートメントの親インターフェイスです。<br/>
 *
 * 作成日: 2005/01/29
 * 
 * @author m.matsubara
 */
public interface IStatement {
    /**
     * 変数が使われているかどうか判別します。
     * @param id 変数ID
     * @return 変数が使用されている場合その数、使用されていない場合は0
     * @throws ExpException
     */
    public int isUsingVariable(Integer id) throws ExpException;

    /**
     * ステートメントを実行します。
     * @param runtime ランタイムデータ
     * @return ステートメントの実行結果。ただし特に何もないときは null が返る。break, continue, return, throw のみが値を返す。
     * @throws ExpException 実行時エラー
     * @throws SpecialStatement 特殊ステートメントが実行されたとき
     */
    public StatementResult execute(RuntimeData runtime) throws ExpException;
    
    
    /**
     * ステートメント内でローカル変数の宣言がある場合、varMap 引数に対してID を登録しなければなりません。
     * @param varMap ローカル変数を登録するための Map
     */
    public void registLocalVariable(Map varMap);
}
