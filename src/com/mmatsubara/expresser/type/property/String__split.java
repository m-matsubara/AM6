package com.mmatsubara.expresser.type.property;

import java.util.ArrayList;

import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpArray;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type._String;


/**
 * String型の split() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/04/03
 * 
 * @author m.matsubara
 */

public class String__split extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("split");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private String__split() throws ExpException {
        super("split", 2);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        String value = ((IExpObject)thisObject).toString();

        //  sep パラメータの取り出し
        String sep = null;
        if (arguments.length >= 1) {
            sep = arguments[0].toString();
        }
        
        //  limit パラメータの取り出し
        int limit = Integer.MAX_VALUE;
        if (arguments.length >= 2) {
            try {
                limit = ((ExpNumberBase)(arguments[0])).intValue();
            } catch (ClassCastException cce) {
                limit = runtime.getTypeConverter().toNumber(runtime, arguments[0]).intValue();
            }
        }
        
        //  演算
        if (sep != null) {
            IExpObject[] resultArray = split(value, sep.charAt(0), limit);
            ExpArray result = new ExpArray(resultArray);
            return result;
        } else {
            ExpArray result = new ExpArray(1);
            result.putProperty(0, thisObject);
            return result;
        }
    }
    
    
    /**
     * 文字列の分割をします
     * @param source 元文字列
     * @param ch 分割のセパレータ
     * @param limit 最大分割数
     * @return 分割された文字列の配列(ExpStringの配列)
     */
    protected IExpObject[] split(String source, char ch, int limit) throws ExpException {
        ArrayList resultList = new ArrayList();
        int begin = 0;
        int max = source.length();
        for (int idx = 0; idx < max; idx++) {
            if (source.charAt(idx) == ch && resultList.size() < limit - 1) {
                String str = source.substring(begin, idx);
                resultList.add(new _String(str));
                begin = idx + 1;
            }
        }
        if (begin != max) {
            String str = source.substring(begin, max);
            resultList.add(new _String(str));
        }
        return (IExpObject[])resultList.toArray(new IExpObject[resultList.size()]); // TODO このやり方で大丈夫か確認
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new String__split();
        return instance;
    }
}
