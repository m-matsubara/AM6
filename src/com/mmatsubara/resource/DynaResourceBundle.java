package com.mmatsubara.resource;

import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * 動的メッセージ付きのリソースバンドルです。
 * 
 * 作成日: 2005/05/18
 * 
 * @author m.matsubara
 */

public class DynaResourceBundle extends ResourceBundle {
    
    private ResourceBundle source;
    
    public DynaResourceBundle(ResourceBundle source) {
        this.source = source; 
    }

    /* (非 Javadoc)
     * @see java.util.ResourceBundle#getKeys()
     */
    public Enumeration getKeys() {
        return source.getKeys();
    }

    /* (非 Javadoc)
     * @see java.util.ResourceBundle#handleGetObject(java.lang.String)
     */
    protected Object handleGetObject(String key) {
        return source.getObject(key);
    }
    
    public String getString(String key, Object[] params) {
        String resource = source.getString(key);
        StringBuffer result = new StringBuffer();
        
        int max = resource.length();
        for (int idx = 0; idx < max; idx++) {
            char ch = resource.charAt(idx);
            switch (ch) {
            case '{':
                //  動的置き換えパラメータ
                if (idx + 2 < max) {
                    int startIdx = idx + 1;
                    while (idx < max) {
                        if (resource.charAt(idx) == '}')
                            break;
                        idx++;
                    }
                    try {
                        int paramIdx = Integer.parseInt(resource.substring(startIdx, idx));
                        if (paramIdx < params.length) {
                            if (params[paramIdx] != null)
                                result.append(params[paramIdx].toString());
                            else
                                result.append("null");
                        }
                    } catch (Exception e) {
                        //  数字じゃなかった
                        result.append(resource.substring(startIdx, idx));
                    }
                } else
                    result.append(ch);
                break;
            case '\\':
                //  エスケープシーケンス
                if (idx + 1 < max) {
                    char ch2 = resource.charAt(idx + 1);
                    switch (ch2) {
                    case 'b':
                        result.append('\b');
                        break;
                    case 't':
                        result.append('\t');
                        break;
                    case 'n':
                        result.append('\n');
                        break;
                    case 'f':
                        result.append('\f');
                        break;
                    case 'r':
                        result.append('\r');
                    case '0':
                        result.append('\0');
                        break;
                    case '\\':
                        result.append('\\');
                        break;
                    case '\"':
                        result.append('\"');
                        break;
                    case '\'':
                        result.append('\'');
                        break;
                    case '{':
                        result.append('{');
                        break;
                    }
                    idx++;
                } else
                    result.append(ch);
                break;
            default:
                //  普通の文字
                result.append(ch);
            }
        }
        return result.toString();
    }
    
    public String getString(String key, Object param1) {
        return getString(key, new Object[] {param1});
    }
    
    public String getString(String key, Object param1, Object param2) {
        return getString(key, new Object[] {param1, param2});
    }
    
    public String getString(String key, Object param1, Object param2, Object param3) {
        return getString(key, new Object[] {param1, param2, param3});
    }
    
    public String getString(String key, Object param1, Object param2, Object param3, Object param4) {
        return getString(key, new Object[] {param1, param2, param3, param4});
    }
    
}
