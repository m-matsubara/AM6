package com.mmatsubara.expresser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 文字列から対応するユニークなID（整数値）を得ます<br/>
 * またその逆の動作も行います。<br/>
 * 同じIDを戻す場合は必ずインスタンス自体が同じものとなります。<br/>
 * <br/>
 * 作成日: 2005/02/19
 * 
 * @author m.matsubara
 */
public class StringToId {
    private static HashMap map = new HashMap();
    private static List idList  = new ArrayList();
    
    private StringToId() {
        
    }

    /**
     * 文字列から対応するユニークな整数値を得る<br/>
     * 文字列とIDのセットが登録されていない場合、新たにIDが発行される
     * @param string IDを取得する文字列
     * @return 対応するID
     */
    public static Integer toId(String string) {
        synchronized (map) {
            Integer id = (Integer)map.get(string);
            if (id == null) {
                synchronized (idList) {
                    id = new Integer(idList.size());
                    map.put(string, id);
                    idList.add(string);
                }
            }
            return id;
        }
    }
    
    
    /**
     * IDから文字列を取得する
     * @param id 文字列を得るID
     * @return 対応する文字列
     */
    public static String toString(Integer id) {
        synchronized (idList) {
            return (String)idList.get(id.intValue());
        }
    }

}
