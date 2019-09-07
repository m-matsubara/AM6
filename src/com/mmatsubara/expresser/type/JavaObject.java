package com.mmatsubara.expresser.type;

import java.util.ArrayList;
import java.util.List;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;

public class JavaObject implements IExpObject {
    private Object object;
    
    public JavaObject(Object object) {
        this.object = object;
    }

    public IExpObject getProperty(Integer id) throws ExpException {
        JavaObjectMember member = new JavaObjectMember(object, StringToId.toString(id));
        try {
            //  TODO JavaObjectのメソッドをプロパティとして呼び出した場合の動作をもう少し考える
            //  TODO JavaObjectMember はIVariableを継承していて良いか考える。
            IExpObject memberVal = member.getValue();
            return memberVal;
        } catch (ExpException ee) {
            return member;
        }
    }

    public void putProperty(Integer id, IExpObject value) throws ExpException {
        JavaObjectMember member = new JavaObjectMember(object, StringToId.toString(id));
        member.setValue(value);
    }

    public IExpObject getProperty(int index) throws ExpException {
        return Expresser.UNDEFINED;
    }

    public void putProperty(int index, IExpObject value) throws ExpException {
        
    }

    public boolean isProperty(Integer id, boolean searchPrototype) throws ExpException {
        return true;    //  TODO まずい
    }

    public Integer[] enumProperty() {
        return new Integer[] {};    //  TODO これもまずい
    }
    
    public IExpObject deleteProperty(Integer id) throws ExpException {
        return new _Boolean(true);
    }

    public List getConstructorList() {
        return new ArrayList(0);
    }

    public IVariable getVariable(Integer id) {
        JavaObjectMember member = new JavaObjectMember(object, StringToId.toString(id));
        return member;
    }

    public IVariable getVariable(int Index) {
        return null;    // TODO これはまずいだろう。
    }

    public Object getObject() {
        return object;
    }
    
    public String toString() {
        return object.toString();
    }
}
