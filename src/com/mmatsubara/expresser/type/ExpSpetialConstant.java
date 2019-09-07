package com.mmatsubara.expresser.type;

import java.util.List;

import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.exception.ExpException;

/**
 * 特殊な定数を表します。<br/>
 * これはスクリプトで何かを表す定数値と言うより、処理系の中で処理を切り分ける判断材料として利用されます。
 * <br/>
 * 作成日: 2005/09/18
 * 
 * @author m.matsubara
 */
public class ExpSpetialConstant implements IExpObject {

    public IExpObject getProperty(Integer id) throws ExpException {
        return null;
    }

    public void putProperty(Integer id, IExpObject value) throws ExpException {
        
    }

    public IExpObject getProperty(int index) throws ExpException {
        return null;
    }

    public void putProperty(int index, IExpObject value) throws ExpException {
        
    }

    public boolean isProperty(Integer id, boolean searchPrototype) throws ExpException {
        return false;
    }

    public IExpObject deleteProperty(Integer id) throws ExpException {
        return null;
    }

    public List getConstructorList() {
        return null;
    }

    public IVariable getVariable(Integer id) {
        return null;
    }

    public IVariable getVariable(int index) {
        return null;
    }

    public Integer[] enumProperty() {
        return null;
    }

}
