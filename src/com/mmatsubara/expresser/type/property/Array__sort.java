package com.mmatsubara.expresser.type.property;

import java.util.Date;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.ExpDate;
import com.mmatsubara.expresser.type.ExpFunctionBase;
import com.mmatsubara.expresser.type.ExpNumberBase;
import com.mmatsubara.expresser.type.IArray;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.expresser.type.IFunction;



/**
 * Array型の sort() メソッドです。<br/>
 * <br/>
 * 作成日: 2005/05/08
 * 
 * @author m.matsubara
 */

public class Array__sort extends ExpFunctionBase {
    public static final Integer ID = StringToId.toId("sort");
    private static IExpObject instance = null; 
    
    /**
     * この型の生成には getInstance() を使用してください。
     *
     */
    private Array__sort() throws ExpException {
        super("sort", 1);
    }

    /* (非 Javadoc)
     * @see com.mmatsubara.expresser.type.IFunction#callFunction(com.mmatsubara.expresser.RuntimeData, java.lang.Object, java.lang.Object[])
     */
    public IExpObject callFunction(RuntimeData runtime, IExpObject thisObject, IExpObject[] arguments)
            throws ExpException {
        IArray value = (IArray)thisObject;
        if (arguments.length >= 1) {
            sort(runtime, value, (IFunction)(arguments[0]));
        } else {
            sort(runtime, value, null);
        }
        return value;
    }

    /**
     * 唯一のインスタンスを返します。
     * @return IExpObject 型のインスタンス
     */
    public static IExpObject getInstance() throws ExpException {
        if (instance == null)
            instance = new Array__sort();
        return instance;
    }
    /**
     * 配列のソートを行います。
     * @param runtime ランタイムデータ
     * @param array ソート対象の配列
     * @param compareFunction 比較用の関数(nullの場合デフォルト動作が使用される)
     */
    public void sort(RuntimeData runtime, IArray array, IFunction compareFunction) throws ExpException {
        int length = array.length();
        sort(runtime, array, 0, length - 1, compareFunction);
    }
    
    /**
     * 配列のソートを行います。
     * @param runtime ランタイムデータ
     * @param array ソート対象の配列
     * @param minIdx ソートを行う配列の最小添え字
     * @param maxIdx ソートを行う配列の最大添え字
     * @param compareFunction 比較用の関数(nullの場合デフォルト動作が使用される)
     * @throws ExpException
     */
    public void sort(RuntimeData runtime, IArray array, int minIdx, int maxIdx, IFunction compareFunction) throws ExpException {
        IExpObject swap;
        
        if (minIdx == maxIdx)
            return;
        
        if (minIdx + 1 == maxIdx) {
            if (compare(runtime, array.getProperty(minIdx), array.getProperty(maxIdx), compareFunction) > 0) {
                //  値の交換
                swap = array.getProperty(minIdx);
                array.putProperty(minIdx, array.getProperty(maxIdx));
                array.putProperty(maxIdx, swap);
            }
            return;
        }
        
        //  基準のインデックス判定（３つのメディアン）
        int baseIdx1 = minIdx;
        int baseIdx2 = minIdx + (maxIdx - minIdx) / 2;
        int baseIdx3 = maxIdx;
        if (compare(runtime, array.getProperty(baseIdx1), array.getProperty(baseIdx2), compareFunction) > 0) {
            swap = array.getProperty(baseIdx1);
            array.putProperty(baseIdx1, array.getProperty(baseIdx2));
            array.putProperty(baseIdx2, swap);
        }
        if (compare(runtime, array.getProperty(baseIdx2), array.getProperty(baseIdx3), compareFunction) > 0) {
            swap = array.getProperty(baseIdx2);
            array.putProperty(baseIdx2, array.getProperty(baseIdx3));
            array.putProperty(baseIdx3, swap);
            if (compare(runtime, array.getProperty(baseIdx1), array.getProperty(baseIdx2), compareFunction) > 0) {
                swap = array.getProperty(baseIdx1);
                array.putProperty(baseIdx1, array.getProperty(baseIdx2));
                array.putProperty(baseIdx2, swap);
            }
        }
        if (maxIdx - minIdx == 2)
            return;        //  要素が３個のときこの時点で既に並んでいることになるので戻る
        int baseIdx = baseIdx2;
        int minSearch = minIdx + 1;
        int maxSearch = maxIdx - 1;
        
        
        IExpObject base = array.getProperty(baseIdx);    //  基準値
        while (minSearch <= maxSearch) {
            //  配列の前側から基準値より大きな値を探す
            while (compare(runtime, array.getProperty(minSearch), base, compareFunction) < 0) 
                minSearch = minSearch + 1;
            //  配列の後ろ側から基準値より小さな値を探す
            while (compare(runtime, array.getProperty(maxSearch), base, compareFunction) > 0) 
                maxSearch = maxSearch - 1;
            //  必要なら値を交換する
            if (minSearch <= maxSearch) {
                if (minSearch < maxSearch) {
                    //  値の交換
                    swap = array.getProperty(minSearch);
                    array.putProperty(minSearch, array.getProperty(maxSearch));
                    array.putProperty(maxSearch, swap);
                }
                
                minSearch = minSearch + 1; 
                maxSearch = maxSearch - 1;
            }
        }
        if (minIdx < maxSearch)
            sort(runtime, array, minIdx, maxSearch, compareFunction);  // 前半をソート
        if (minSearch < maxIdx)
            sort(runtime, array, minSearch, maxIdx, compareFunction);  // 後半をソート
    }
    
    /**
     * オブジェクト同士の比較を行います。
     * @param runtime ランタイムデータ
     * @param a 比較対象1
     * @param b 比較対象2
     * @param compareFunction 比較用の関数(nullの場合デフォルト動作が使用される)
     * @return 比較結果（負の値:aが小さい, 0:aとbは等しい, 正の値:aが大きい）
     * @throws ExpException
     */
    public int compare(RuntimeData runtime, IExpObject a, IExpObject b, IFunction compareFunction) throws ExpException {
        if (compareFunction != null) {
            IExpObject compareObject = compareFunction.callFunction(runtime, null, new IExpObject[] {a, b});
            if (compareObject instanceof ExpNumberBase) {
                double compareNum = ((ExpNumberBase)compareObject).doubleValue();
                if (compareNum < 0)
                    return -1;
                else if (compareNum > 0)
                    return 1;
                else
                    return 0;
            } else {
                throw new ExpException(Expresser.getResString("CompareFunctionResultNotNumber"), -1);
            }
        }
        
        if (a instanceof ExpNumberBase && b instanceof ExpNumberBase) {
            double numA = ((ExpNumberBase)a).doubleValue();
            double numB = ((ExpNumberBase)b).doubleValue();
            
            if (numA < numB)
                return -1;
            else if (numA > numB)
                return 1;
            else
                return 0;
        } else if (a instanceof ExpDate && b instanceof ExpDate) {
            Date dateA = ((ExpDate)a).getDate();
            Date dateB = ((ExpDate)b).getDate();
            
            return dateA.compareTo(dateB);
        } else {
            String strA = null;// = a.toString();
            String strB = null;// = b.toString();
            Object toString;
            //   aを文字列化する
            if (a instanceof IExpObject) {
                toString = ((IExpObject)a).getProperty(StringToId.toId("toString"));
                if (toString != Expresser.UNDEFINED && toString != null) {
                    strA = ((IFunction)toString).callFunction(runtime, a, new IExpObject[] {}).toString();
                }
            }
            if (strA == null)
                strA = a.toString();
            //   bを文字列化する
            if (b instanceof IExpObject) {
                toString = ((IExpObject)b).getProperty(StringToId.toId("toString"));
                if (toString != Expresser.UNDEFINED && toString != null) {
                    strB = ((IFunction)toString).callFunction(runtime, b, new IExpObject[] {}).toString();
                }
            }
            if (strB == null)
                strB = b.toString();
                
            //   比較
            return strA.compareTo(strB);
        }
    }
}
