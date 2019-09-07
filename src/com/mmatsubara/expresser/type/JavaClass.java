package com.mmatsubara.expresser.type;

import java.lang.reflect.*;
import java.util.List;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.*;


/**
 * Javaホストオブジェクトのクラスを表現します。<br/>
 * <br/>
 * 作成日: 2005/01/16
 * 
 * @author m.matsubara
 */
public class JavaClass implements IExpObject, IFunction {
    private Class javaClass;
    
    public JavaClass(Class javaClass) {
        this.javaClass = javaClass;
    }
    
    public Field getField(String fieldName) throws NoSuchFieldException {
        return javaClass.getField(fieldName);
    }
    
    public Class getJavaClass() {
        return javaClass;
    }
    
    /**
     * 新しいインスタンスを生成します
     * 
     * @return 生成されたインスタンス
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public Object newInstance()  throws InstantiationException, IllegalAccessException {
        return javaClass.newInstance();
    }

    /**
     * 新しいインスタンスを生成します
     * @param params コンストラクタのパラメータ
     * @return 生成されたインスタンス
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public IExpObject newInstance(IExpObject[] params)  throws NoSuchMethodException, InstantiationException, InvocationTargetException, IllegalAccessException {
        Class[] paramTypes = JavaObjectMember.createParamTypes(params);
            
        Constructor constructor = javaClass.getConstructor(paramTypes);
        return new JavaObject(constructor.newInstance(params)); //  TODO 型変換しなければならないのでは？
    }
    
    /*
     * IFunctionの実装<br/>
     * クラスを関数として呼び出す場合はコンストラクタが呼び出される
     * 
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] params) throws ExpException {
        try {
            return newInstance(params);
        } catch (Exception e) {
            throw new ExpException(Expresser.getResString("FailureCreateObject") + " : " + javaClass.getName(), 0);
        }
    }
    
    public String toString() {
        return javaClass.toString();
    }

//  TODO 以下ダミーメソッド
    public IExpObject getProperty(Integer id) throws ExpException {
        JavaObjectMember member = new JavaObjectMember(javaClass, StringToId.toString(id));
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
        JavaObjectMember member = new JavaObjectMember(javaClass, StringToId.toString(id));
        member.setValue(value);
    }

    public IExpObject getProperty(int index) throws ExpException {
        // TODO Auto-generated method stub
        return null;
    }

    public void putProperty(int index, IExpObject value) throws ExpException {
        // TODO Auto-generated method stub
        
    }

    public boolean isProperty(Integer id) throws ExpException {
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

    public IVariable getVariable(int Index) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isProperty(Integer id, boolean searchPrototype) throws ExpException {
        // TODO Auto-generated method stub
        return false;
    }

    public Integer[] enumProperty() {
        // TODO Auto-generated method stub
        return null;
    }
}
