package com.mmatsubara.expresser.type;

import java.util.List;

import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.exception.ExpException;


/**
 * すべてのネイティブオブジェクトのインターフェイスです。（プリミティブ型も含む）<br/>
 * <br/>
 * IExpObjectにメソッドを追加するときはプリミティブ型 (_ から始まる型)よりも先に
 * 対応するオブジェクト型の方へメソッドを追加した方がよいです。<br/>
 * <br/>
 * プリミティブ型に先に追加すると、オブジェクト型のエラーが消えるため修正しなけれ
 * ばいけないことがわからなくなります。<br/>
 * <br/>  
 * 作成日: 2005/02/05
 * 
 * @author m.matsubara
 */
public interface IExpObject {
    /**
     * プロパティを取得します。
     * @param id プロパティID
     * @return 値
     */
    public IExpObject getProperty(Integer id) throws ExpException;

    /**
     * プロパティを設定します。
     * @param id プロパティID
     * @param value 値
     */
    public void putProperty(Integer id, IExpObject value) throws ExpException;

    /**
     * プロパティを取得します。
     * @param index 添え字
     * @return 値
     */
    public IExpObject getProperty(int index) throws ExpException;

    /**
     * プロパティを設定します。
     * @param index 添え字
     * @param value 値
     */
    public void putProperty(int index, IExpObject value) throws ExpException;

    /**
     * プロパティがあるかどうか判断します。
     * @param id プロパティ名
     * @param searchPrototype プロトタイプのプロパティまで探すか指定します。
     * @return プロパティがある場合 true
     */
    public boolean isProperty(Integer id, boolean searchPrototype) throws ExpException;
    
    /**
     * プロパティを削除します。
     * @param id プロパティID
     */
    public IExpObject deleteProperty(Integer id) throws ExpException;
    
    /**
     * コンストラクタのリストです。<br/>
     * クラスが継承されている場合は後ろに追加される
     * @return コンストラクタのリスト(Classオブジェクトを格納)
     */
    public List getConstructorList();
    
    /**
     * プロパティの変数を取得します。
     * @param id プロパティID
     * @return 変数
     */
    public IVariable getVariable(Integer id);

    /**
     * 配列プロパティの変数を取得します。
     * @param index 添え字
     * @return 変数
     */
    public IVariable getVariable(int index);
    
    
    /**
     * プロパティを列挙します。
     * @return プロパティIDの配列
     */
    public Integer[] enumProperty();
    
    
    /*
     * IExpObjectにメソッドを追加するときはプリミティブ型 (_ から始まる型)よりも先に
     * 対応するオブジェクト型の方へメソッドを追加した方がよいです。
     * 
     * プリミティブ型に先に追加すると、オブジェクト型のエラーが消えるため修正しなけれ
     * ばいけないことがわからなくなります。
     */
}
