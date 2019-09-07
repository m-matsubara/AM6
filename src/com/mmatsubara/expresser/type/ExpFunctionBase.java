package com.mmatsubara.expresser.type;

import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.property.Function__apply;
import com.mmatsubara.expresser.type.property.Function__call;

/**
 * 関数オブジェクトの基底クラスです。<br/>
 * 抽象クラスになっているので、派生したクラスはIFunctionで定義されるメソッドを実装しなければなりません。<br/>
 * <br/>
 * 作成日: 2005/05/24
 * 
 * @author m.matsubara
 */

public abstract class ExpFunctionBase extends ExpObject implements IFunction {
    /** toStringで使用される関数の名称です */
    protected String functionName;
    /** toStringで使用される関数の本体です */
    protected String functionBody;
    /** length プロパティの値 */
    protected int length;
    
    private static ExpObject prototype = null;
    
    private static final Integer propertyId$length = StringToId.toId("length");

    /**
     * 関数オブジェクトを構築します。
     * @param functionName 関数名
     * @param functionBody 関数本体
     * @param length 引数の数
     * @throws ExpException
     */
    public ExpFunctionBase(String functionName, String functionBody, int length) throws ExpException {
        super();
        this.functionName = functionName;
        this.functionBody = functionBody;
        this.length = length;
        //  super.constructorList.add();    //  追加すべきクラスがない…
        setThisPrototype(getPrototype());
    }
    
    /**
     * 関数オブジェクトを構築します。
     * @param functionName 関数名
     * @param length 引数の数
     * @throws ExpException
     */
    public ExpFunctionBase(String functionName, int length) throws ExpException {
        this(functionName, "", length);
        this.functionBody = "[Native code: " + this.getClass().getName() + "]";
    }
    
    /*
     *  (非 Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "function " + functionName + "([" + getLength() + " arguments]) { " + functionBody + " }";
    }
    
    public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getFunctionBody() {
		return functionBody;
	}

	public void setFunctionBody(String functionBody) {
		this.functionBody = functionBody;
	}

	/**
     * 引数の数を返します。 
     * @return 引数の数
     */
    public int getLength() {
        return this.length;
    }
    
	public void setLength(int length) {
		this.length = length;
	}


    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#getProperty(java.lang.Integer)
     */
    public IExpObject getProperty(Integer id) throws ExpException {
        if (id.intValue() == propertyId$length.intValue())
            return new _Double(getLength());
        return super.getProperty(id);
    }
    
    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.expresser.type.IExpObject#isProperty(java.lang.Integer, boolean)
     */
    public boolean isProperty(Integer id, boolean searchPrototype) throws ExpException {
        if (id.intValue() == propertyId$length.intValue())
            return true;
        else
            return super.isProperty(id, searchPrototype);
    }
    
    
    
    /**
     * prototypeを取得します。
     * @return prototype 
     * @throws ExpException
     */
    public static synchronized ExpObject getPrototype() throws ExpException {
        if (prototype == null) {
            prototype = new ExpObject(ExpObject.getPrototype());
            prototype.putProperty(Function__apply.ID, Function__apply.getInstance());
            prototype.putProperty(Function__call.ID,  Function__call.getInstance());
        }
        return prototype;
    }
}
