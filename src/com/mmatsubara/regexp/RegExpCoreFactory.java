package com.mmatsubara.regexp;


/**
 * 正規表現コアクラスを生成するファクトリです。<br/>
 * <br/>
 * 作成日: 2005/08/06
 * 
 * @author m.matsubara
 */
public class RegExpCoreFactory {
    private static Class regExpCoreClass = null;
    private static Object mutex = new Object();
    
    /**
     * 正規表現コアクラスを生成する
     * @return
     * @throws Exception
     */
    public static IRegExpCore create() throws Exception {
        IRegExpCore regExpCore = null;
        //  正規表現コアクラスの一覧
        String[] regExpCoreClassNames = { 
                "com.mmatsubara.regexp.JavaRegExpCore",
//                "com.mmatsubara.regexp.ORORegExpCore", 
        };
        
        //  利用可能な正規表現コアクラスが特定されていなかったら特定する
        if (regExpCoreClass == null) {
            synchronized (mutex) {
                int max = regExpCoreClassNames.length;
                for (int idx = 0; idx < max; idx++) {
                    try {
                        regExpCoreClass = Class.forName(regExpCoreClassNames[idx]);
                        //  インスタンスの生成
                        regExpCore = (IRegExpCore)(regExpCoreClass.newInstance());
                        return regExpCore;
                    } catch (Throwable t) {
                        //  クラスが見つからなかった・リンケージエラー・初期化の失敗など
                        //  次の正規表現コアクラスを試す。
                    }
                }
                if (regExpCoreClass == null)
                    throw new Exception("RegExp class create failure.");
            }
        }
        
        //  インスタンスの生成
        regExpCore = (IRegExpCore)(regExpCoreClass.newInstance());
        return regExpCore;
    }
}
