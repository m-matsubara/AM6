package com.mmatsubara.expresser.parsedexpression;

import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.IExpObject;

/**
 * 構文解析済みの式です。<br/> 
 * <br/>
 * 作成日: 2005/02/05
 * 
 * @author m.matsubara
 */
public interface IParsedExpression {
    /**
     * 変数が利用されているかどうか調べます。
     * @param id 変数ID
     * @return 変数が使用されている場合その数、使用されていない場合は0
     * @throws ExpException
     */
    public int isUsingVariable(Integer id) throws ExpException;
    
    /**
     * 構文解析済みの計算式を実際に計算します。
     * @param runtime ランタイムデータ 
     * @return 計算結果
     * @throws ExpException
     */
    public IExpObject evaluate(RuntimeData runtime) throws ExpException;
    
    /**
     * 可能であれば計算式の参照を得ます。<br/>
     * 参照とは代入可能な式のことです。具体的にこれは変数であったり、メンバアクセス演算子や、配列アクセス演算子です。
     * 参照を得ることができない場合は例外を生成します。
     * @param runtime ランタイムデータ 
     * @return 参照
     * @throws ExpException
     */
    public IVariable evalRef(RuntimeData runtime) throws ExpException;

    /**
     * 対象となるオブジェクトを返します。<br/>
     * これは xxx.yyy() のメソッド呼び出しにおける xxx を表します。
     * @param runtime ランタイムデータ
     * @return 対象となるオブジェクト
     * @throws ExpException
     */
    public IExpObject evalTargetObject(RuntimeData runtime) throws ExpException;
    
    
    /**
     * 構文解析された計算式が定数であるか判断します。
     * @return 構文解析された計算式が定数である場合 true
     */
    public boolean isConst();

}
