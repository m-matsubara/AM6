package com.mmatsubara.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * J2SDK1.4 で導入された正規表現をサポートするオブジェクトです。<br/>
 * <br/>
 * 作成日: 2005/05/03
 * 
 * @author m.matsubara
 */
public class JavaRegExpCore implements IRegExpCore {
    Pattern pattern;
    
    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.regexp.IRegExpCore#init(java.lang.String, java.lang.String)
     */
    public void init(String pattern, String flags) throws Exception {
    	int flagsInt = 0;
    	char[] flagsChars = flags.toCharArray();
    	for (int idx = 0; idx < flagsChars.length; idx++) {
    		char ch = flagsChars[idx];
    		if (ch == 'i') {
    			flagsInt = flagsInt | Pattern.CASE_INSENSITIVE; 
    		} else if (ch == 'g') {
    			//flagsInt = flagsInt | Pattern.; 
    		} else if (ch == 'm') {
    			flagsInt = flagsInt | Pattern.MULTILINE; 
    		}
    	}
    	
    	
        this.pattern = Pattern.compile(pattern, flagsInt);
    }

    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.regexp.IRegExpCore#match(java.lang.String)
     */
    public RegMatchResult[] match(String source) {
        //  TODO 意図した値を返してくれない（ことがある）
        Matcher matcher = pattern.matcher(source);
        if (matcher.find()) {
            int max = matcher.groupCount() + 1;
            RegMatchResult[] result = new RegMatchResult[max];
            for (int idx = 0; idx < max; idx++) {
                int begin = matcher.start(idx);
                int end = matcher.end(idx);
                if (begin != -1 && end != -1)
                    result[idx] = new RegMatchResult(begin, end);
                else
                    result[idx] = null;
            }
            return result;
        }
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see com.mmatsubara.regexp.IRegExpCore#test(java.lang.String)
     */
    public boolean test(String source) {
        Matcher matcher = pattern.matcher(source);
        return matcher.matches();
    }

    
    
}
