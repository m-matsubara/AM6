package com.mmatsubara.interpreter;

import com.mmatsubara.expresser.ExpTypeConverter;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.exception.ExpException;

/**
 * スクリプトを実行するためのスレッドです。
 * 
 * 作成日: 2005/05/25
 * 
 * @author m.matsubara
 */

public class ScriptThread extends Thread {
    private ParsedScript script;
    private RuntimeData runtime;
    
    /**
     * スクリプトを実行するためのスレッドを作成します。
     * @param script 解析済みスクリプト
     */
    public ScriptThread(ParsedScript script) {
        this.script = script;
        runtime = new RuntimeData(script.getDefaultRuntime());
        runtime.setTypeConverter(new ExpTypeConverter());
    }

    /* (非 Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            script.execute(runtime);
        } catch (ExpException ee) {
            ee.printStackTrace();
        }
    }
}
