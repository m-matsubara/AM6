package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.ITypeConverter;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.IArray;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type.IFunction;
import com.mmatsubara.expresser.type._String;

/**
 * Array型の toLocaleString() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/05/08
 * 
 * @author m.matsubara
 */

public class Array__toLocaleString extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("toLocaleString");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Array__toLocaleString() throws ExpException {
        super("toLocaleString", 2);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        IArray value = (IArray)thisObject;
        
        int length = value.length();
        if (length <= 0) {
            return new _String("");
        }
        
        ITypeConverter typeConverter = runtime.getTypeConverter();
        StringBuffer result = new StringBuffer();
        result.append(typeConverter.toString(runtime, toLocaleString(value.getProperty(0), runtime)));
        for (int idx = 1; idx < length; idx++) {
            result.append(",");
            result.append(typeConverter.toString(runtime, toLocaleString(value.getProperty(idx), runtime)));
        }
        return new _String(result.toString());
    }
    
    /**
     * オブジェクトの持つ toLocaleString() によって地域化された文字列を得ます。
     * @param object 対象のオブジェクト
     * @param runtime ランタイムデータ
     * @return 地域化された文字列
     * @throws ExpException
     */
    public IExpObject toLocaleString(IExpObject object, RuntimeData runtime) throws ExpException {
        if (object instanceof IExpObject) {
            IExpObject toLocaleStringObj = ((IExpObject)object).getProperty(ID);
            if (toLocaleStringObj instanceof IFunction) {
                return ((IFunction)toLocaleStringObj).callFunction(runtime, object, new IExpObject[] {});
            }
        }
        return object;  //  _Stringを戻さないけど仕方ない。
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Array__toLocaleString();
        return instance;
    }
}
