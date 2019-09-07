package com.mmatsubara.expresser.type;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;


/**
 * Javaホストオブジェクトのメンバー名を保持します。<br/>
 * <br/>
 * 作成日: 2005/01/16
 * 
 * @author m.matsubara
 */
public class JavaObjectMember implements IExpObject, IVariable, IFunction {
    private Object object;
    private String member;
    
    public JavaObjectMember(Object object, String member) {
        this.object = object;
        this.member = member;
    }
        
    /**
     * @return member を戻します。
     */
    public String getMember() {
        return member;
    }
    /**
     * @param member member を設定。
     */
    public void setMember(String member) {
        this.member = member;
    }
    /**
     * @return object を戻します。
     */
    public Object getObject() {
        return object;
    }
    /**
     * @param object object を設定。
     */
    public void setObject(Object object) {
        this.object = object;
    }
    
    /**
     * メソッドの呼び出し
     * @param object オブジェクト
     * @param methodName メソッド名
     * @param params パラメータ
     * @return 戻り値
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ExpException
     */
    public static IExpObject invokeMethod(Object object, String methodName, Object[] params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ExpException {
        Class[] classParams = createParamTypes(params);
    
        Method method;
        if (object instanceof JavaClass) {
            JavaClass javaClass = (JavaClass)object;
            method = javaClass.getJavaClass().getMethod(methodName, classParams);
            return Expresser.convertPrimitiveType(method.invoke(null, params), method.getReturnType());
        } else {
            method = object.getClass().getMethod(methodName, classParams);
            return Expresser.convertPrimitiveType(method.invoke(object, params), method.getReturnType());
        }
    }

    
    /**
     * メソッドの呼び出し（メンバがメソッドである場合）
     * 
     * @param params 引数配列
     * @return メソッド戻り値
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ExpException
     */
    public Object invokeMethod(Object[] params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ExpException {
        Class[] classParams = createParamTypes(params);
    
        Method method;
        if (object instanceof JavaClass) {
            JavaClass javaClass = (JavaClass)object;
            method = javaClass.getJavaClass().getMethod(member, classParams);
            return Expresser.convertPrimitiveType(method.invoke(null, params), method.getReturnType());
        } else {
            method = object.getClass().getMethod(member, classParams);
            return Expresser.convertPrimitiveType(method.invoke(object, params), method.getReturnType());
        }
    }
    
    
    /**
     * パラメータの型配列を作成する（JavaMethodの呼び出し用）
     * @param params パラメータ
     * @return パラメータの型配列 
     */
    public static Class[] createParamTypes(Object[] params) {
        Class[] paramTypes = new Class[params.length];
        for (int idx = 0; idx < params.length; idx++) {
            if (params[idx] instanceof _Double) {
                paramTypes[idx] = double.class;
                params[idx] = new Double(((_Double)params[idx]).doubleValue());
            }
            else if (params[idx] instanceof _Integer) {
                paramTypes[idx] = int.class;
                params[idx] = new Integer(((_Integer)params[idx]).intValue());
            }
            else if (params[idx] instanceof _String) {
                paramTypes[idx] = String.class;
                params[idx] = ((_String)params[idx]).toString();
            }
            else if (params[idx] instanceof _Byte) {
                paramTypes[idx] = byte.class;
                params[idx] = new Byte(((_Byte)params[idx]).byteValue());
            }
            else if (params[idx] instanceof _Short) {
                paramTypes[idx] = short.class;
                params[idx] = new Short(((_Short)params[idx]).shortValue());
            }
            else if (params[idx] instanceof _Long) {
                paramTypes[idx] = long.class;
                params[idx] = new Long(((_Long)params[idx]).longValue());
            }
            else if (params[idx] instanceof _Float) {
                paramTypes[idx] = float.class;
                params[idx] = new Float(((_Float)params[idx]).floatValue());
            }
            else if (params[idx] instanceof _Character) {
                paramTypes[idx] = char.class;
                params[idx] = new Character(((_Character)params[idx]).toChar());
            }
            else
                paramTypes[idx] = params[idx].getClass();
        }
        return paramTypes;
    }
    
    /**
     * フィールドの取得（メンバがフィールドである場合）
     * 
     * @return フィールドの取得
     * @throws NoSuchFieldException
     */
    public Field getField() throws NoSuchFieldException {
        /*
        if (object instanceof JavaClass) {
            JavaClass javaClass = (JavaClass)object;
            return javaClass.getField(member);        
        } else {
            return object.getClass().getField(member);
        }
*/
        if (object instanceof Class) {
            Class classObj = (Class)object;
            return classObj.getField(member);        
        } else {
            return object.getClass().getField(member);
        }
    }
    
    /**
     * このメンバがフィールドである場合、値を取得する
     */
    public IExpObject getValue() throws ExpException {
        try {
            Field field = getField();
            return Expresser.convertPrimitiveType(field.get(object), field.getType());
        } catch (Exception e) {
            try {
                String accesserGet = "get" + String.valueOf(member.charAt(0)).toUpperCase() + member.substring(1);
                return invokeMethod(object, accesserGet, new Object[] {});
            } catch (Exception e2) {
                throw new ExpException(Expresser.getResString("NotField") +  " : " + object.getClass().toString() + "#" + member, -1);
            }
        }
    }
    
    /**
     * このメンバがフィールドである場合、値を設定する
     */
    public void setValue(Object value) throws ExpException {
        try {
            Field field = getField();
            field.set(object, value);
        } catch (Exception e) {
            try {
                String accesserSet = "set" + String.valueOf(member.charAt(0)).toUpperCase() + member.substring(1);
                invokeMethod(object, accesserSet, new Object[] { value });
            } catch (Exception e2) {
                throw new ExpException(Expresser.getResString("NotField") +  " : " + object.getClass().toString() + "#" + member, -1);
            }
        }
    }
    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments) throws ExpException {
        Object[] objectArguments = new Object[arguments.length];
        for (int idx = 0; idx < arguments.length; idx++) {
            objectArguments[idx] = arguments[idx]; 
        }
        try {
            return new JavaObject(invokeMethod(objectArguments));
        } catch (NoSuchMethodException nsme) {
            throw new ExpException(Expresser.getResString("NotCallableObjectMethod") + " : " + object.getClass().toString() + "#" + member, -1);
        } catch (InvocationTargetException ite) {
            Throwable e = ite.getTargetException();
            throw new ExpException(e.getClass().getName() + " : " + e.getMessage(), -1);
        } catch (Exception e) {
            throw new ExpException(e.getClass().getName() + " : " + e.getMessage(), -1);
        }
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
    
    public String toString() {
        return object.getClass().getName() + "#" + member; 
    }

    public void setValue(IExpObject value) throws ExpException {
        // TODO Auto-generated method stub
        
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
