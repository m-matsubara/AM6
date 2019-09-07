package com.mmatsubara.expresser.type.property;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.ITypeConverter;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._Boolean;

/**
 * Object型の hasOwnProperty() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/04/03
 * 
 * @author m.matsubara
 */
public class Object__propertyIsEnumerable extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("propertyIsEnumerable");
    private static IExpObject instance = null;  

    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Object__propertyIsEnumerable() throws ExpException {
        super("propertyIsEnumerable", 0);
    }
    
    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        if (arguments.length < 1)
            return new _Boolean(false);
        if (arguments[0] == null || arguments[0] == Expresser.UNDEFINED)
            return new _Boolean(false);

        ITypeConverter typeConverter = runtime.getTypeConverter();
        String propertyName = typeConverter.toString(runtime, arguments[0]).toString();
        Integer propertyId = StringToId.toId(propertyName);
        if (thisObject instanceof IExpObject) {
            IExpObject object = (IExpObject)thisObject;
            boolean result = object.isProperty(propertyId, false);  //  一応絞り込み
            if (result) {
                //  TODO パフォーマンスが良くないはず
                Integer[] ids = object.enumProperty();
                int max = ids.length;
                for (int idx = 0; idx < max; idx++) {
                    if (ids[idx] == propertyId) {
                        return new _Boolean(true);
                    }
                }
            }
            return new _Boolean(false);
        } else {
            //  このメソッドはホストオブジェクトを this として扱うことはできない（実装上の制限より）
            return new _Boolean(false);
        }
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Object__propertyIsEnumerable();
        return instance;
    }
}
