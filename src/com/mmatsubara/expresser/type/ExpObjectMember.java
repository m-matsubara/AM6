package com.mmatsubara.expresser.type;

import java.util.List;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;

/**
 * オブジェクトのメンバー名を保持します。<br/>
 * <br/>
 * 作成日: 2005/02/06
 * 
 * @author m.matsubara
 */
public class ExpObjectMember implements IExpObject, IVariable, IFunction {
    private IExpObject object;
    private Integer memberId;
    private int index;
    
    public ExpObjectMember(IExpObject object, Integer memberId) {
//      if (object == null)
//          throw new ExpException(Expresser.getResString("NullObject"), -1);
        this.object = object;
        this.memberId = memberId;
        this.index = 0;
    }
        
    public ExpObjectMember(IExpObject object, int index) {
//      if (object == null)
//          throw new ExpException(Expresser.getResString("NullObject"), -1);
        this.object = object;
        this.memberId = null;
        this.index = index;
    }

    /**
     * @return object を戻します。
     */
    public IExpObject getObject() {
        return object;
    }
    /**
     * @param object object を設定。
     */
    public void setObject(IExpObject object) {
        this.object = object;
    }
    
    /**
     * メソッドの呼び出しをします。（メンバがメソッドである場合）
     * 
     * @param runtime ランタイムデータ
     * @param thisObject thisオブジェクト(nullの時は内部のobjectを使用します)
     * @param arguments 引数IDの配列（Integer）
     * @return 関数の実行結果
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments) throws ExpException {

        Object memberObj;
        if (memberId != null)
            memberObj = object.getProperty(memberId);
        else
            memberObj = object.getProperty(index);
        
        try {
            IFunction function = (IFunction)memberObj;    //  ClassCastException を起こす可能性がある
            if (thisObject != Expresser.UNDEFINED)
                return function.callFunction(runtime, thisObject, arguments);   //  コンストラクタ
            else
                return function.callFunction(runtime, object, arguments);   //  メソッド呼び出し
        } catch (ClassCastException cce) {
            throw new ExpException(Expresser.getResString("NotCallableObject"), -1); 
        }
    }
    
    /**
     * このメンバがフィールドである場合、値を取得する
     */
    public IExpObject getValue() throws ExpException {
        if (memberId != null)
            return object.getProperty(memberId);
        else
            return object.getProperty(index);
    }
    
    /**
     * このメンバがフィールドである場合、値を設定する
     */
    public void setValue(IExpObject value) throws ExpException {
        if (memberId != null)
            object.putProperty(memberId, value);
        else
            object.putProperty(index, value);
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IVariable#isConst()
     */
    public boolean isConst() throws ExpException {
        return false;
    }
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IVariable#setConst(boolean)
     */
    public void setConst(boolean bConst) throws ExpException {
        throw new ExpException(Expresser.getResString("NotSetConst"), -1);
    }
    
    /**
     * 指定されたメンバーを削除する
     * @return 削除されたプロパティの保持していた値
     * @throws ExpException
     */
    public IExpObject deleteMember() throws ExpException {
        if (memberId != null)
            return object.deleteProperty(memberId);
        else {
            IExpObject result = object.getProperty(index);
            object.putProperty(index, null);
            return result;
        }
    }

    public IExpObject callFunction(RuntimeData runtime, Object thisObject, Object[] arguments) throws ExpException {
        // TODO Auto-generated method stub
        return null;
    }

    public IExpObject getProperty(Integer id) throws ExpException {
        // TODO Auto-generated method stub
        return null;
    }

    public void putProperty(Integer id, IExpObject value) throws ExpException {
        // TODO Auto-generated method stub
        
    }

    public IExpObject getProperty(int index) throws ExpException {
        // TODO Auto-generated method stub
        return null;
    }

    public void putProperty(int index, IExpObject value) throws ExpException {
        // TODO Auto-generated method stub
        
    }

    public boolean isProperty(Integer id, boolean searchPrototype) throws ExpException {
        // TODO Auto-generated method stub
        return false;
    }

    public IExpObject deleteProperty(Integer id) throws ExpException {
        // TODO Auto-generated method stub
        return null;
    }

    public List getConstructorList() {
        // TODO Auto-generated method stub
        return null;
    }

    public IVariable getVariable(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    public IVariable getVariable(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    public Integer[] enumProperty() {
        // TODO Auto-generated method stub
        return null;
    }
}
