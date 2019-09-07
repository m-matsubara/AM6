package com.mmatsubara.expresser.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.IVariable;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.Variable;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.property.Object__hasOwnProperty;
import com.mmatsubara.expresser.type.property.Object__isPrototypeOf;
import com.mmatsubara.expresser.type.property.Object__propertyIsEnumerable;
import com.mmatsubara.expresser.type.property.Primitive__toString;
import com.mmatsubara.expresser.type.property.Primitive__valueOf;


/**
 * Object型を規定します。<br/>
 * <br/>
 * 作成日: 2005/02/05
 * 
 * @author m.matsubara
 */
public class ExpObject implements IExpObject {
    public static IExpObject constructor = null;

    /** プロパティを保持するマップです。 */
    private Map properties;
    /** 配列プロパティを保持します。添え字は正の整数のみです。 */
    private IVariable[] varArray;
    /** varArray の中で実際に使用されている領域の長さです。 */
    private int varArrayLength;
    /** このオブジェクトのコンストラクタ */
    private ExpObject thisPrototype;    
    
    
    protected List constructorList;
    private String objectName;
    
    /**
     * arrayMapへの登録時にnull値が設定されているのか、値が設定されていないのか判別するための定数
     */
    static final Object NULL = new Object();
    
    /** コンストラクタ作成中フラグ（trueのときはプロトタイプの作成中なのでprototypeを再帰的に作成しない） */
    private static boolean createPrototype = false;
    /** ExpObjectのコンストラクタ */
    private static ExpObject prototype = null; 
    
    public ExpObject(String objectName, int initialSize) throws ExpException {
        this.properties = new HashMap();     //  IdentityHashMap 使いたい…
        this.objectName = objectName;
        this.thisPrototype = getPrototype();
        this.varArray = new IVariable[initialSize > 0 ? initialSize : 1];
        this.constructorList = new ArrayList();
        getConstructorList().add(constructor);
    }

    public ExpObject() throws ExpException {
        this("Object", 2);
    }

    public ExpObject(ExpObject copyFrom) throws ExpException {
        this("Object", copyFrom.varArray.length);
        //  プロパティのコピー
        Iterator ite = copyFrom.keys();
        while (ite.hasNext()) {
            Integer id = (Integer)ite.next();
            properties.put(id, new Variable(copyFrom.getProperty(id)));
        }
        //  配列プロパティのコピー
        varArrayLength = copyFrom.varArrayLength;
        for (int idx = 0; idx < varArrayLength; idx++) {
        	varArray[idx] = copyFrom.varArray[idx];
        }
    }

    public ExpObject(String objectName, IExpObject constructor) throws ExpException {
        this(objectName, 2);
        getConstructorList().add(constructor);
    }

    public ExpObject(String objectName, IExpObject constructor, int initialSize) throws ExpException {
        this(objectName, initialSize);
        getConstructorList().add(constructor);
    }
    
    private void arrayResize(int index) {
        //  配列の長さを変更
        synchronized (varArray) {
            int newSize = varArray.length * 2;
            while (newSize <= index)
                newSize *= 2;
            IVariable[] newVarArray = new Variable[newSize];
            System.arraycopy(varArray, 0, newVarArray, 0, varArray.length);
            varArray = newVarArray;
        }
    }
    
    

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#deleteProperty(java.lang.Integer)
     */
    public IExpObject deleteProperty(Integer id) throws ExpException {
        properties.remove(id);
        return new _Boolean(true);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getProperty(java.lang.Integer)
     */
    public IExpObject getProperty(Integer id) throws ExpException {
        IVariable variable = (IVariable)properties.get(id);
        if (variable != null)
            return variable.getValue();
        else if (thisPrototype != null)
            return thisPrototype.getProperty(id);
        else
            return Expresser.UNDEFINED;
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#putProperty(java.lang.String, java.lang.Integer)
     */
    public void putProperty(Integer id, IExpObject value) throws ExpException {
        IVariable variable = (IVariable)properties.get(id);
        if (variable != null)
            variable.setValue(value);
        else {
            variable = new Variable(value);
            properties.put(id, variable);
        }
            
    }

    /**
     * プロパティの変数自身を取得します。
     * @param id プロパティID
     * @return 変数
     */
    public IVariable getVariable(Integer id) {
        //  ハッシュからオブジェクトを取り出して返さないのは、派生クラスでgetPropertyをオーバーライドしている可能性があるため。
        return new ExpObjectMember(this, id);
    }
    
    /**
     * プロパティの変数自身を設定します。
     * @param id プロパティID
     * @param var 変数
     */
    protected void setVariable(Integer id, IVariable var) {
        properties.put(id, var);
    }
    
    /**
     * プロパティの変数自身を取得します。
     * @param index 配列インデックス
     * @return 変数
     */
    public IVariable getVariable(int index) {
        if (index >= varArray.length)
            arrayResize(index);
        IVariable variable = varArray[index];
        if (variable == null) {
            variable = new Variable(Expresser.UNDEFINED);
            varArray[index] = variable;
        }
        if (index >= varArrayLength)
            varArrayLength = index + 1;
        return variable;
    }
    
    /**
     * プロパティの変数自身を設定します。
     * @param id プロパティID
     * @param var 変数
     */
    protected void setVariable(int index, IVariable var) {
        if (index >= varArray.length)
            arrayResize(index);
        varArray[index] = var;
    }
    
    /**
     * 値を定数として登録します。
     * @param id ID
     * @param value 値
     * @throws ExpException
     */
    public void putConstProperty(Integer id, IExpObject value) throws ExpException {
        IVariable variable = (IVariable)properties.get(id);
        if (variable != null)
            variable.setValue(value);
        else {
            variable = new Variable(value);
            properties.put(id, variable);
        }
        variable.setConst(true);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getProperty(int)
     */
    public IExpObject getProperty(int index) throws ExpException {
        if (index >= varArray.length)
            return Expresser.UNDEFINED;
        
        IVariable variable = varArray[index];
        if (variable == null)
            return Expresser.UNDEFINED;
            
        IExpObject result = variable.getValue();
        return result;
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#putProperty(int, java.lang.Integer)
     */
    public void putProperty(int index, IExpObject value) throws ExpException {
        if (index >= varArray.length)
            arrayResize(index);
        if (index >= varArrayLength)
            varArrayLength = index + 1;
        IVariable variable = varArray[index];
        if (variable == null) {
        	variable = new Variable(value);
            varArray[index] = variable;
            return;
        }
        variable.setValue(value);
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#isProperty(java.lang.Integer, boolean)
     */
    public boolean isProperty(Integer id, boolean searchPrototype) throws ExpException {
        if (properties.get(id) != null)
            return true;
            
        if (searchPrototype) {
            if (thisPrototype != null && thisPrototype.isProperty(id, searchPrototype))
                return true;
        }
        return false;
    }
    
    /*
     *  (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getConstructorList()
     */
    public List getConstructorList() {
        return constructorList;
    }

    /* (non-Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#enumProperty()
     */
    public Integer[] enumProperty() {
        Iterator iterator = keys();
        List list = new ArrayList();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        
        int max = list.size();
        Integer[] result = new Integer[max];
        for (int idx = 0; idx < max; idx++) {
            result[idx] = (Integer)list.get(idx);
        }
        return result;
    }

    /**
     * リファレンスセットを返します。
     * @return リファレンスセット
     */
/*
    public ReferenceSet getReferenceSet() {
        return properties;
    }
*/    
    public String toString() {
        String result = "[object " + this.objectName + "]"; 
        return result;
    }
    
    
    /**
     * オブジェクト名を設定する toString() で使用されます。 
     * @param objectName オブジェクト名
     */
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
    
    /**
     * オブジェクト名を取得します。
     * @return オブジェクト名
     */
    public String getObjectName() {
        return this.objectName;
    }
    
    /**
     * キーIDの一覧を取得します。
     * @return キーIDの一覧
     */
    public Iterator keys() {
        return properties.keySet().iterator();
    }
    
    /**
     * prototypeを取得します。
     * @return prototype 
     * @throws ExpException
     */
    public static synchronized ExpObject getPrototype() throws ExpException {
        if (createPrototype)
            return null;
        if (prototype == null) {
            createPrototype = true;  //  コンストラクタ中で getPrototype() 呼び出しを行わない。
            prototype = new ExpObject();
            createPrototype = false;
            prototype.putProperty(Primitive__toString.ID,  Primitive__toString.getInstance());
            prototype.putProperty(StringToId.toId("toLocaleString"),  Primitive__toString.getInstance());
            prototype.putProperty(Primitive__valueOf.ID,   Primitive__valueOf.getInstance());

            prototype.putProperty(Object__hasOwnProperty.ID,       Object__hasOwnProperty.getInstance());
            prototype.putProperty(Object__isPrototypeOf.ID,        Object__isPrototypeOf.getInstance());
            prototype.putProperty(Object__propertyIsEnumerable.ID, Object__propertyIsEnumerable.getInstance());
        }
        return prototype;
    }
    
    
    /**
     * このオブジェクトのプロトタイプを返します。
     * @return このオブジェクトのプロトタイプ
     */
    public ExpObject getThisPrototype() {
        return thisPrototype;
    }
    /**
     * このオブジェクトのプロトタイプを設定します。
     * @param thisPrototype このオブジェクトのプロトタイプ
     */
    public void setThisPrototype(ExpObject thisPrototype) {
        this.thisPrototype = thisPrototype;
    }

    /**
     * 配列プロパティの長さを取得します。
     * @return 配列プロパティの長さ
     */
    public int getVarArrayLength() {
        return varArrayLength;
    }

    /**
     * 配列プロパティの長さを設定します。
     * @param varArrayLength 配列プロパティの長さ
     */
    public void setVarArrayLength(int varArrayLength) {
        if (this.varArrayLength == varArrayLength)
            return;
        this.varArrayLength = varArrayLength;
        int max = varArray.length;
        for (int idx = varArrayLength; idx < max; idx++) {
            varArray[idx] = null;
        }
    }
}
