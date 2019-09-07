package com.mmatsubara.expresser.type;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.property.RegExp__exec;
import com.mmatsubara.expresser.type.property.RegExp__test;
import com.mmatsubara.regexp.IRegExpCore;
import com.mmatsubara.regexp.RegExpCoreFactory;
import com.mmatsubara.regexp.RegMatchResult;

/**
 * 正規表現を表現します。<br/>
 * <br/>
 * 作成日: 2005/05/03
 * 
 * @author m.matsubara
 */
public class ExpRegExp extends ExpObject {
    public static IExpObject constructor = null; 

    private IRegExpCore regExpCore;
    
    
    private static ExpObject prototype = null;

    /**
     * 正規表現オブジェクトを生成します。 
     * @param pattern パターン
     * @param flags 動作を指定するフラグ
     * @throws ExpException
     */
    public ExpRegExp(String pattern, String flags) throws ExpException {
        super("RegExp", constructor);
        try {
            regExpCore = RegExpCoreFactory.create();
        } catch (Throwable e) {
            throw new ExpException(Expresser.getResString("RegExpFailureCreate", e.getMessage()), -1);
        }
        
        try {
            regExpCore.init(pattern, flags);
        } catch (Exception e) {
            throw new ExpException(Expresser.getResString("InvalidRegExp"), -1);
        }
        
        boolean ignoreFlag = false;
        boolean globalFlag = false;
        boolean multilineFlag = false;
        
        char[] flagsChars = flags.toCharArray();
        for (int idx = 0; idx < flagsChars.length; idx++) {
    		char ch = flagsChars[idx];
    		if (ch == 'i') {
    			ignoreFlag = true; 
    		} else if (ch == 'g') {
    			globalFlag = true; 
    		} else if (ch == 'm') {
    			multilineFlag = true; 
    		}
    	}
        putConstProperty(StringToId.toId("source"), new _String(pattern));
        putConstProperty(StringToId.toId("ignoreCase"), new _Boolean(ignoreFlag));
        putConstProperty(StringToId.toId("global"), new _Boolean(globalFlag));
        putConstProperty(StringToId.toId("multiline"), new _Boolean(multilineFlag));

        setThisPrototype(getPrototype());
    }
    
    /**
     * パターンが見つかった場合にその文字列を返します。<br>
     * @param source 検索する文字列
     * @return パターンの見つかった文字列配列・見つからなかった場合はnull
     */
    public ExpArray exec(String source) throws ExpException {
        RegMatchResult[] receive = regExpCore.match(source);
        if (receive != null) {
            ExpArray result = new ExpArray(receive.length);
            for (int idx = 0; idx < receive.length; idx++) {
                if (receive[idx] != null)
                    result.putProperty(idx, new _String(source.substring(receive[idx].getBegin(), receive[idx].getEnd())));
                else
                    result.putProperty(idx, Expresser.UNDEFINED);
            }
            return result;
        } else
            return null;
    }
    
    /**
     * パターンが見つかるか検査します。
     * @param source 検索する文字列
     * @return 見つかる場合 true
     */
    public boolean test(String source) {
        return regExpCore.test(source);
    }

    /**
     * 正規表現を利用して文字列の置き換えを行います。
     * @param source 元文字列（検索される文字列）
     * @param replace 置き換えられる文字列
     * @return 文字列の置き換えを行った結果
     * @throws ExpException
     */
    public String replace(String source, String replace) throws ExpException {
        RegMatchResult[] matchs = regExpCore.match(source);
        if (matchs == null)
            return source;

        //  replace中の $ をエスケープする
        int max = replace.length();
        StringBuffer replaseStrBuf = new StringBuffer(max);
        int idx = 0; 
        while (idx < max) {
            char ch = replace.charAt(idx);
            if (ch == '$') {
                if (idx >= max - 1)
                    throw new ExpException(Expresser.getResString("InvalidRegExp"), -1);
                char ch2 = replace.charAt(idx + 1);
                switch (ch2) {
                case '$':   //  $ 自体
                    replaseStrBuf.append('$');
                    break;
                case '&':   //  一致した部分文字列
                    {
                        String str = source.substring(matchs[0].getBegin(), matchs[0].getEnd());
                        replaseStrBuf.append(str);
                    }
                    break;  
                case '`':   //  マッチした部分より前方の文字列
                    {
                        String str = source.substring(0, matchs[0].getBegin());
                        replaseStrBuf.append(str);
                    }
                    break;
                case '\'':  //  マッチした部分より後方の文字列
                    {
                        String str = source.substring(matchs[0].getEnd());
                        replaseStrBuf.append(str);
                    }
                    break;  
                default:
                    //  n 番目の括弧で囲まれたグループにマッチした文字列
                    if (Character.isDigit(ch2)) {   
                        int num = ch2 - '0';
                        String str = source.substring(matchs[num].getBegin(), matchs[num].getEnd());
                        replaseStrBuf.append(str);
                    } else {
                        throw new ExpException(Expresser.getResString("InvalidRegExp"), -1);
                    }
                }
                idx += 2;
            } else {
                replaseStrBuf.append(ch);
                idx++;
            }
            
        }
        replace = replaseStrBuf.toString();
        
        //  置き換え処理
        int begin = matchs[0].getBegin();
        int end = matchs[0].getEnd();
        String preStr = source.substring(0, begin);
        String postStr = source.substring(end);
        
        return preStr + replace + postStr;
    }

    /**
     * prototypeを取得します。
     * @return prototype 
     * @throws ExpException
     */
    public static ExpObject getPrototype() throws ExpException {
        if (prototype == null) {
            prototype = new ExpObject(ExpObject.getPrototype());
            synchronized (prototype) {
                prototype.putProperty(StringToId.toId("exec"), RegExp__exec.getInstance());
                prototype.putProperty(StringToId.toId("test"), RegExp__test.getInstance());
            }
        }
        return prototype;
    }
}
