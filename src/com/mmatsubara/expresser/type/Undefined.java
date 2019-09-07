package com.mmatsubara.expresser.type;

import java.util.ArrayList;
import java.util.List;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.exception.ExpException;


/**
 * undefined を定義する型です。
 * @author matsubara
 *
 */
public class Undefined implements IExpObject {
    
    public Undefined() {
        
    }

    public IExpObject getProperty(Integer id) throws ExpException {
        throw new ExpException(Expresser.getResString("OperationToUndefined"), -1);
    }

    public void putProperty(Integer id, IExpObject value) throws ExpException {
        throw new ExpException(Expresser.getResString("OperationToUndefined"), -1);
    }

    public IExpObject getProperty(int index) throws ExpException {
        throw new ExpException(Expresser.getResString("OperationToUndefined"), -1);
    }

    public void putProperty(int index, IExpObject value) throws ExpException {
        throw new ExpException(Expresser.getResString("OperationToUndefined"), -1);
        
    }

    public boolean isProperty(Integer id, boolean searchPrototype) throws ExpException {
        throw new ExpException(Expresser.getResString("OperationToUndefined"), -1);
    }

    public IExpObject deleteProperty(Integer id) throws ExpException {
        throw new ExpException(Expresser.getResString("OperationToUndefined"), -1);
    }

    public List getConstructorList() {
        return new ArrayList(0);
    }

    public IVariable getVariable(Integer id) {
        return null;
    }

    public IVariable getVariable(int index) {
        return null;
    }
    
    public Integer[] enumProperty() {
        return new Integer[0];
    }

    public String toString() { 
        return "undefined"; 
    }

    public boolean equals(Object object) {
        return (this == object);    //  同じオブジェクトインスタンスの時のみtrueとなる
    }
}
