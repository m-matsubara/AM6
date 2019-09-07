package com.mmatsubara.expresser.type;

import java.util.List;

import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;

/**
 * コンストラクタです。<br/>
 * new 演算子にて関数がコンストラクタ化されたときに使用されます。
 * <br/>
 * 作成日: 2005/05/18
 * 
 * @author m.matsubara
 */

public class ExpConstructor implements IExpObject, IFunction {
    private IFunction function;
    
    
    public ExpConstructor(IFunction function) {
        this.function = function; 
    }
    

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject,
            IExpObject[] arguments) throws ExpException {
        //  TODO エラーチェックした方がよいかも？ （functionはIExpObjectであるか）
        thisObject = new ExpObject("Object", (IExpObject)function);
        return function.callFunction(runtime, thisObject, arguments);
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
