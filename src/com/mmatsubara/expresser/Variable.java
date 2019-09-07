package com.mmatsubara.expresser;

import java.util.List;

import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpObject;
import com.mmatsubara.expresser.type.IExpObject;

/**
 * 変数クラスです。<br/>
 * <br/>
 * @author m.matsubara
 */
public class Variable implements IVariable {
    public static final IExpObject UNINITIALIZED = uninisializedValue();;
    
    //private Class clsType;
    private IExpObject value;
    private boolean isConstant;
    
    
    
    public Variable() {
        this.value = UNINITIALIZED;
        //this.clsType = Object.class;
        this.isConstant = false;
    }
    
    public Variable(IExpObject value) {
        this.value = value;
/*
        if (value != null)
            this.clsType = value.getClass();
        else
            this.clsType = Object.class;
*/            
        this.isConstant = false;
    }

    public Variable(IExpObject value, boolean isConstant) {
        this.value = value;
/*        
        if (value != null)
            this.clsType = value.getClass();
        else
            this.clsType = Object.class;
*/            
        this.isConstant = isConstant;
    }
    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IVariable#getClsType()
     */
/*    
    public Class getClsType() {
        return clsType;
    }
*/    
    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IVariable#setClsType(java.lang.Class)
     */
/*
    public void setClsType(Class clsType) {
        this.clsType = clsType;
    }
*/    
    /**
     * 初期化されていないオブジェクトを生成する
     */
    public static IExpObject uninisializedValue() {
        try {
            return new ExpObject();
        } catch (ExpException ee) {
            throw new RuntimeException("create failure uninitialized object.");
        }
    }

    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IVariable#getValue()
     */
    public IExpObject getValue() throws ExpException {
        if (value == UNINITIALIZED)
            throw new ExpException(Expresser.getResString("UndefinedVariable"), -1);
        return value;
    }
    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IVariable#setValue(java.lang.Object)
     */
    public void setValue(IExpObject oValue) throws ExpException {
        if (isConstant) 
            throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), -1);
        this.value = oValue;
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IVariable#isConst()
     */
    public boolean isConst() throws ExpException {
        return isConstant;
    }
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IVariable#setConst(boolean)
     */
    public void setConst(boolean bConst) throws ExpException {
        this.isConstant = bConst;
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
