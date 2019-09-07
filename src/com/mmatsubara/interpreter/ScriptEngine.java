package com.mmatsubara.interpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.mmatsubara.expresser.Expresser;
import com.mmatsubara.expresser.ITypeConverter;
import com.mmatsubara.expresser.OperatorData;
import com.mmatsubara.expresser.RuntimeData;
import com.mmatsubara.expresser.StringToId;
import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.type.IExpObject;
import com.mmatsubara.interpreter.ecmascript.operatorparser.FunctionOperatorParser;
import com.mmatsubara.interpreter.ecmascript.parser.BreakStatementParser;
import com.mmatsubara.interpreter.ecmascript.parser.ContinueStatementParser;
import com.mmatsubara.interpreter.ecmascript.parser.DoWhileStatementParser;
import com.mmatsubara.interpreter.ecmascript.parser.ExpressionStatementParser;
import com.mmatsubara.interpreter.ecmascript.parser.ForStatementParser;
import com.mmatsubara.interpreter.ecmascript.parser.FunctionStatementParser;
import com.mmatsubara.interpreter.ecmascript.parser.IStatementParser;
import com.mmatsubara.interpreter.ecmascript.parser.IfStatementParser;
import com.mmatsubara.interpreter.ecmascript.parser.ReturnStatementParser;
import com.mmatsubara.interpreter.ecmascript.parser.SwitchStatementParser;
import com.mmatsubara.interpreter.ecmascript.parser.SynchronizedStatementParser;
import com.mmatsubara.interpreter.ecmascript.parser.ThrowStatementParser;
import com.mmatsubara.interpreter.ecmascript.parser.TryStatementParser;
import com.mmatsubara.interpreter.ecmascript.parser.VarStatementParser;
import com.mmatsubara.interpreter.ecmascript.parser.WhileStatementParser;
import com.mmatsubara.interpreter.ecmascript.parser.WithStatementParser;
import com.mmatsubara.interpreter.ecmascript.type.EcmaFunction;
import com.mmatsubara.interpreter.ecmascript.type.constructor.Function_constructor;
import com.mmatsubara.interpreter.ecmascript.type.property.Global__eval;
import com.mmatsubara.interpreter.statement.BlockStatement;
import com.mmatsubara.interpreter.statement.IStatement;
import com.mmatsubara.resource.DynaResourceBundle;




/**
 * あまのいわと６  Ignis interpreter<br/>
 * 組み込み用途向けスクリプト言語<br/>
 *
 * 作成日: 2005/01/15
 *
 * @author m.matsubara
 */
public class ScriptEngine {
    public static String RESOURCE_NAME = "com.mmatsubara.interpreter.ScriptEngineRes";
    public static DynaResourceBundle resource = null;

    /**  演算器（計算式評価クラス） */
    private Expresser expresser;

    /** 各ステートメントのパーサーを登録します。 */
    private HashMap statementParserMap = new HashMap();
    /** 特定のキーワードで始まらないステートメントを処理するパーサーです。計算式を処理するパーサーです。 */
    private IStatementParser defaultParseStatement;

    /** 予約語の一覧 */
    public final String[] RESERVED_WORDS = new String[] {
            "as", "break", "case", "catch", "class", "const", "continue", "default", "delete", "do", "else", "export", "extends", "finally", "for", "function", "if", "import", "in", "instanceof", "is", "namespace", "new", "package", "private", "public", "return", "super", "switch", "throw", "try", "typeof", "use", "var", "void", "while", "with",
            "abstract", "debugger", "enum", "goto", "implements", "interface", "native", "protected", "synchronized", "throws", "transient", "volatile",
    };

    /**
     * スクリプトファイルを読み込む際のキャラクタセットです。<br/>
     * null の時はシステムに基づくキャラクタセットが利用されます。
     */
    private String scriptCharacterSet = null;

    /**
     * スクリプトエンジンを構築します。
     * @throws Exception
     */
    public ScriptEngine() throws ExpException {
        //  計算オブジェクトの作成
        expresser = new Expresser();
        //  演算子の追加
        int priority = expresser.findOpeData(OperatorData.OP_LOG_NOT).getPriority();  //  ! 演算子の優先度を取得
        expresser.addOpeData(new OperatorData("function", OperatorData.OP_EXTERNAL_PARSED, priority, OperatorData.OP_CLS_FRONT_UNITY, false, false, new FunctionOperatorParser(this)));

        //  グローバルなプロパティ（関数）の追加
        EcmaFunction.constructor = new Function_constructor(this);

        RuntimeData runtime = expresser.getDefaultRuntimeData();
        runtime.setConst(Global__eval.ID, Global__eval.getInstance());
        runtime.setConst(StringToId.toId("Function"), EcmaFunction.constructor);

        //  パーサーの登録
        //  ECMAScript 互換パーサー
        addStatementParser(new BreakStatementParser());
        addStatementParser(new ContinueStatementParser());
        addStatementParser(new DoWhileStatementParser());
        addStatementParser(new ReturnStatementParser());
        addStatementParser(new ForStatementParser());
        addStatementParser(new FunctionStatementParser());
        addStatementParser(new IfStatementParser());
        addStatementParser(new SwitchStatementParser());
        addStatementParser(new ThrowStatementParser());
        addStatementParser(new TryStatementParser());
        addStatementParser(new VarStatementParser());
        addStatementParser(new WhileStatementParser());
        addStatementParser(new WithStatementParser());
        setDefaultStatementParser(new ExpressionStatementParser());

        //  独自拡張パーサー
        addStatementParser(new SynchronizedStatementParser());

        for (int idx = 0; idx < RESERVED_WORDS.length; idx++)
            expresser.addReservedWord(RESERVED_WORDS[idx]);
    }

    /**
     * expresser を取得します。
     * @return expresser
     */
    public Expresser getExpresser() {
        return expresser;
    }

    /**
     * スクリプトのキャラクタセットを取得します。
     * @return
     */
    public String getScriptCharacterSet() {
		return scriptCharacterSet;
	}

    /**
     * スクリプトのキャラクタセットを設定します。
     * @param scriptCharacterSet
     */
    public void setScriptCharacterSet(String scriptCharacterSet) {
		this.scriptCharacterSet = scriptCharacterSet;
	}

	/**
     * 構文解析用ステートメントを登録します。
     * @param parser パーサー
     */
    public void addStatementParser(IStatementParser parser) {
        statementParserMap.put(parser.targetStatement(), parser);
        expresser.addReservedWord(parser.targetStatement());
    }

    /**
     * デフォルトのステートメントパーサーです。
     *
     * @param defaultStatement defaultStatement を設定
     */
    public void setDefaultStatementParser(IStatementParser defaultStatement) {
        this.defaultParseStatement = defaultStatement;
    }

    /**
     * ステートメントパーサーを取得します。
     * @param statement ステートメント
     * @return 解析に使用するパーサー
     */
    public IStatementParser getStatementParser(String statement) {
        IStatementParser result = (IStatementParser)statementParserMap.get(statement);
        if (result != null)
            return result;
        else
            return defaultParseStatement;
    }

    /**
     * スクリプトの解析をします。
     * @param script スクリプト
     * @return 構文解析されたスクリプト
     * @throws ExpException
     */
    public ParsedScript parse(String script) throws ExpException {
        char[] scriptChars = script.toCharArray();
        return parse(scriptChars, 0, scriptChars.length);
    }

    /**
     * スクリプトの解析をします。
     * @param script スクリプト
     * @param begin 開始位置
     * @param end 終了位置
     * @return 構文解析されたスクリプト
     * @throws Exception
     */
    public ParsedScript parse(char[] script, int begin, int end) throws ExpException {
        BlockStatement parsedStatement = new BlockStatement();
        while (begin < end) {
            begin = parseStatement1(parsedStatement, script, begin, end);
        }
        ParsedScript result = new ParsedScript(parsedStatement, expresser.getDefaultRuntimeData());
        return result;
    }

    /**
     * 複数ステートメントの解析をします。<br/>
     * parse() メソッドの中から内部的に呼び出されます。通常呼び出す必要はありません。
     * @param script スクリプト
     * @param begin 開始位置
     * @param end 終了位置
     * @return 構文解析されたスクリプト
     * @throws Exception
     */
    public IStatement parseStatement(char[] script, int begin, int end) throws ExpException {
        BlockStatement parsedStatement = new BlockStatement();
        while (begin < end) {
            begin = parseStatement1(parsedStatement, script, begin, end);
        }
        return parsedStatement;
    }

    /**
     * ステートメントの解析をします(１ステートメントまたは１ブロックです)<br/>
     * parse() メソッドの中から内部的に呼び出されます。通常呼び出す必要はありません。
     * @param script スクリプト(ステートメント)
     * @param begin 開始位置
     * @param end 終了位置
     */
    public int parseStatement1(BlockStatement blockStatement, char[] script, int begin, int end) throws ExpException {
        //  前部分の空白除去(あれば)
        begin = Expresser.skipWhitespace(script, begin, end);
        if (begin >= end)
            return begin;

        if (script[begin] == '{') {
            //  ブロックステートメントを処理する
            int blockBegin = begin;
            begin = Expresser.skipParenthesis(script, begin, end);
            if (script[begin - 1] == '}') {
                blockStatement.add(parseStatement(script, blockBegin + 1, begin - 1));
            }
        } else {
            String statement = expresser.nextToken(script, begin, end);
            int labelIdx = begin + statement.length();
            labelIdx = Expresser.skipWhitespace(script, labelIdx, end);
            if (labelIdx >= end)
                return labelIdx;
            if (script[labelIdx] == ':') {
                //  ラベルを処理する
                Integer labelId = StringToId.toId(statement);
                begin = labelIdx + 1;
                begin = Expresser.skipWhitespace(script, begin, end);

                statement = expresser.nextToken(script, begin, end);
                IStatementParser statementParser = getStatementParser(statement);
                begin = statementParser.parseStatement(this, blockStatement, statement, script, begin, end, labelId);
            } else {
                //  通常のステートメントを処理する
                IStatementParser statementParser = getStatementParser(statement);
                begin = statementParser.parseStatement(this, blockStatement, statement, script, begin, end, null);
            }
        }
        return begin;
    }


    /**
     * そのステートメントの終わりまで読み飛ばします。
     * @param script スクリプト
     * @param begin 開始位置
     * @param end 終了位置
     * @return セミコロンの位置+1
     * @throws ExpException
     */
    public static int skipStatement(char[] script, int begin, int end) throws ExpException {
        while (begin < end) {
            char ch = script[begin];
            if (ch == '{' || ch == '(' || ch == '[')
                begin = Expresser.skipParenthesis(script, begin, end);
            else if (ch == '\"' || ch == '\'')
                begin = Expresser.skipString(script, begin, end);
            else if (ch == ';')
                return begin + 1;
            else {
                begin++;
            }
        }
        throw new ExpException(ScriptEngine.getResString("StatementNotFinished"), begin);
    }

    /**
     * その式の終わりまで読み飛ばします。<br/>
     * カンマでも式の終わりと判断することに注意しなければなりません。
     * @param script スクリプト
     * @param begin 開始位置
     * @param end 終了位置
     * @return セミコロンまたはカンマの位置+1
     * @throws ExpException
     */
    public static int skipExpression(char[] script, int begin, int end) throws ExpException {
        while (begin < end) {
            char ch = script[begin];
            if (ch == '{' || ch == '(' || ch == '[')
                begin = Expresser.skipParenthesis(script, begin, end);
            else if (ch == '\"' || ch == '\'')
                begin = Expresser.skipString(script, begin, end);
            else if (ch == ';')
                return begin + 1;
            else {
                begin++;
            }
        }
        throw new ExpException(ScriptEngine.getResString("StatementNotFinished"), begin);
    }


    /**
     * スクリプトを実行します。<br/>
     * シングルスレッド環境用です。<br/>
     * マルチスレッド環境では parse()メソッドの戻り値のParsedScriptのexecute()メソッドを、expresserのデフォルトランタイムから作成した新しいランタイムデータを引数として渡して呼び出す必要があります。
     * @param script スクリプト
     * @return 実行結果
     * @throws ExpException
     */
    public Object execute(String script) throws ExpException {
        ParsedScript parsedScript = parse(script);
        return parsedScript.execute();
    }

    /**
     * コメントを削除します。
     * @param script コメント付きのスクリプト
     * @param begin 開始位置
     * @param end 終了位置
     * @throws ExpException
     */
    public static void deleteComents(char[] script, int begin, int end) throws ExpException {
    	if (script.length > 0) {
    		if (script[0] == 0xFEFF)
    			script[0] = ' ';
    	}

        for (int idx = begin; idx < end - 1; idx++) {
            char ch = script[idx];
            if (ch == '/') {
                char ch2 = script[idx + 1];
                if (ch2 == '/') {
                    while (idx < end && script[idx] != '\n') {
                        script[idx] = ' ';
                        idx++;
                    }
                } else if (ch2 == '*') {
                    script[idx] = ' ';
                    idx++;
                    while (idx < end - 1 && !(script[idx] == '*' && script[idx + 1] == '/')) {
                        if (script[idx] != '\n')
                            script[idx] = ' ';
                        idx++;
                    }
                    if (idx >= end - 1)
                        throw new ExpException(ScriptEngine.getResString("CommentNotEnded"), idx);
                    script[idx] = ' ';
                    script[idx + 1] = ' ';
                }
            }
        }
    }

    /**
     * ソースを解析します。
     * @param scriptsChars 解析するべきソース
     * @return 解析済みスクリプト
     * @throws ExpException
     */
    public ParsedScript parseScript(char[] scriptsChars) throws ExpException {
        deleteComents(scriptsChars, 0, scriptsChars.length);
        //  解析
        ParsedScript parsedScript = parse(scriptsChars, 0, scriptsChars.length);
        parsedScript.setSource(scriptsChars);
        return parsedScript;
    }


    /**
     * ストリームからソースを読み込みます。
     * @param stream 読み込みストリーム
     * @param charsetName キャラクタセット名
     * @return ストリームから読み込まれたソース
     * @throws ExpException
     * @throws IOException
     */
    public char[] loadFromStream(InputStream stream, String charsetName) throws ExpException, IOException {
        //  ストリームの読み込み準備
        BufferedReader fileReader;
        if (charsetName != null)
            fileReader = new BufferedReader(new InputStreamReader(stream, charsetName));
        else
            fileReader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer scriptBuffer = new StringBuffer();
        //  ストリームの読み込み
        while (true) {
            String line = fileReader.readLine();
            if (line == null)
                break;
            scriptBuffer.append(line);
            scriptBuffer.append('\n');
        }
        //  解析準備
        char[] scriptsChars = new char[scriptBuffer.length()];
        scriptBuffer.getChars(0, scriptBuffer.length(), scriptsChars, 0);
        return scriptsChars;
    }

    /**
     * ファイルからソースを読み込みます。
     * @param scriptFileName 解析するファイル名
     * @param charsetName キャラクタセット名
     * @return ファイルから読み込まれたソース
     * @throws ExpException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public char[] loadFromFile(String scriptFileName, String charsetName) throws ExpException, FileNotFoundException, IOException {
        File file = new File(scriptFileName);
        InputStream stream = new FileInputStream(file);
        char[] result = loadFromStream(stream, charsetName);
        stream.close();
        return result;
    }

    /**
     * ファイルからスクリプトを読み込み、解析を行います。
     * @param scriptFileName 解析するファイル名
     * @return 解析済みスクリプト
     * @throws ExpException 解析に失敗した場合
     * @throws FileNotFoundException ファイルが見つからない場合
     * @throws IOException 読み込みに失敗した場合
     */
    public char[] loadFromFile(String scriptFileName) throws ExpException, FileNotFoundException, IOException {
        File file = new File(scriptFileName);
        InputStream stream = new FileInputStream(file);
        char[] result = loadFromStream(stream, scriptCharacterSet);
        stream.close();
        return result;
    }


    /**
     * スクリプトファイルを読み込んで実行します。
     * @param scriptFileName スクリプトファイル名
     * @param displayTime 時間表示を行う際 true
     * @throws ExpException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void runScriptFile(String scriptFileName, boolean displayTime) throws ExpException, FileNotFoundException, IOException {
        long startTime = System.currentTimeMillis();
        double scriptTime;
        ParsedScript parsedScript = null;
        char[] scriptChars = null;
        try {
            scriptChars = loadFromFile(scriptFileName);
            parsedScript = parseScript(scriptChars);
            if (displayTime) {
                scriptTime = (System.currentTimeMillis() - startTime) / 1000.0;
                System.out.println("スクリプト構文解析時間 : " + scriptTime + "s");
            }
            parsedScript.execute();
        } catch (ExpException ee) {
            int position = ee.getPosition();
            if (scriptChars != null && scriptChars.length > position) {
                int row = 1;
                int col = 1;
                for (int idx = 0; idx < position; idx++) {
                    if (scriptChars[idx] == '\n') {
                        row++;
                        col = 1;
                    }
                    else if (scriptChars[idx] == '\t') {
                        col += 4;   //便宜上
                    }
                    else
                        col++;
                }
                System.err.println(ee.getMessage());
                System.err.println("  row : " + row);
                System.err.println("  col : " + col);
            } else {
                ee.printStackTrace();
            }
        }
        if (displayTime) {
            scriptTime = (System.currentTimeMillis() - startTime) / 1000.0;
            System.out.println("スクリプト実行総計時間 : " + scriptTime + "s");
        }
    }


    /**
     * プログラムに関する情報を返します。
     * @return プログラムに関する情報
     */
    public static String aboutString() {
        return "Ignis ECMAScript Engine 0.4.1 - copyright(C) 2005-2019 m.matsubara";
    }


    /**
     * 動的メッセージに対応したリソースバンドルを取得します
     * @return リソースバンドル
     */
    public static DynaResourceBundle getResource() {
        if (resource == null)
            resource = new DynaResourceBundle(ResourceBundle.getBundle(RESOURCE_NAME));
        return resource;
    }

    /**
     * キーからローケルに対応したメッセージを取得します
     * @param key キー
     * @return メッセージ
     */
    public static String getResString(String key) {
        return getResource().getString(key);
    }

    /**
     * キーからローケルに対応したメッセージを取得します
     * @param key キー
     * @param params 置き換えパラメータの配列
     * @return メッセージ
     */
    public static String getResString(String key, Object[] params) {
        return getResource().getString(key, params);
    }

    /**
     * キーからローケルに対応したメッセージを取得します
     * @param key キー
     * @param param1 置き換えパラメータ1
     * @return メッセージ
     */
    public static String getResString(String key, Object param1) {
        return getResource().getString(key, param1);
    }

    /**
     * キーからローケルに対応したメッセージを取得します
     * @param key キー
     * @param param1 置き換えパラメータ1
     * @param param2 置き換えパラメータ2
     * @return メッセージ
     */
    public static String getResString(String key, Object param1, Object param2) {
        return getResource().getString(key, param1, param2);
    }

    /**
     * キーからローケルに対応したメッセージを取得します
     * @param key キー
     * @param param1 置き換えパラメータ1
     * @param param2 置き換えパラメータ2
     * @param param3 置き換えパラメータ3
     * @return メッセージ
     */
    public static String getResString(String key, Object param1, Object param2, Object param3) {
        return getResource().getString(key, param1, param2, param3);
    }

    /**
     * キーからローケルに対応したメッセージを取得します
     * @param key キー
     * @param param1 置き換えパラメータ1
     * @param param2 置き換えパラメータ2
     * @param param3 置き換えパラメータ3
     * @param param4 置き換えパラメータ4
     * @return メッセージ
     */
    public static String getResString(String key, Object param1, Object param2, Object param3, Object param4) {
        return getResource().getString(key, param1, param2, param3, param4);
    }

    /**
     * 実行ポイントです。<br/>
     * コンソールアプリケーションとして実行される
     * @param argv スクリプトファイル名
     * @throws Exception
     */
    public static void main(String[] argv) throws Exception {
        ScriptEngine scriptEngine = new ScriptEngine();
        ParsedScript script = null;

        if (argv.length == 0) {
            //  引数なしでの起動
            System.out.println(aboutString());
            System.out.println();

            Expresser expresser = scriptEngine.getExpresser();
            RuntimeData runtime = expresser.getDefaultRuntimeData();
            ITypeConverter typeConverter = runtime.getTypeConverter();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print(">");
            System.out.flush();
            String statement = reader.readLine();
            while (statement != null && statement.equals("@exit") == false && statement.equals(".") == false) {
                try {
                    statement = Expresser.trim(statement) + " ";
                    if (statement.length() != 1) {
                        if (statement.startsWith("@run ")) {
                            String fileName = statement.substring(5).trim();
                            //long startTime = System.currentTimeMillis();
                            //double scriptTime = 0;
                            if (fileName.equals("") == false) {
                                //System.out.println("-----------------------------------------------------");
                                char[] scriptChars = scriptEngine.loadFromFile(fileName);
                                script = scriptEngine.parseScript(scriptChars);
                                //scriptTime = ((System.currentTimeMillis() - startTime) / 1000.0);
                                //System.out.println(ScriptEngine.getResString("ScriptParseTime", String.valueOf(scriptTime)));
                            }
                            if (script != null) {
                                script.execute(scriptEngine.getExpresser().getDefaultRuntimeData());
                                //scriptTime = (System.currentTimeMillis() - startTime) / 1000.0;
                                //System.out.println(ScriptEngine.getResString("ScriptTotalRunTime", String.valueOf(scriptTime)));
                            }
                        } else if (statement.startsWith("@start ")) {
                            String fileName = statement.substring(7).trim();
                            if (fileName.equals("") == false) {
                                char[] scriptChars = scriptEngine.loadFromFile(fileName);
                                script = scriptEngine.parseScript(scriptChars);
                            }
                            if (script != null) {
                                script.executeNewThread();
                                System.out.println(ScriptEngine.getResString("StartThread"));

                            }
                        } else if (statement.startsWith("@load ")) {
                            String fileName = statement.substring(6).trim();
                            if (fileName.equals("") == false) {
                                char[] scriptChars = scriptEngine.loadFromFile(fileName);
                                script = scriptEngine.parseScript(scriptChars);
                            } else {
                                throw new ExpException(ScriptEngine.getResString("SpecifyFile"), 0);
                            }
                        } else if (statement.startsWith("@unload ")) {
                            script = null;
                        } else if (statement.startsWith("@mem ")) {
                            System.out.println("free memory  : " + Runtime.getRuntime().freeMemory());
                            System.out.println("total memory : " + Runtime.getRuntime().totalMemory());
//                            System.out.println("max memory   : " + Runtime.getRuntime().maxMemory());	//	JDK1.2だと無いメソッド…
                        } else if (statement.startsWith("@gc ")) {
                            System.gc();
                        } else if (statement.startsWith("@charset ")) {
                            String charSet = statement.substring(9).trim();
                            scriptEngine.setScriptCharacterSet(charSet);
                        } else if (statement.startsWith("@about")) {
                            System.out.println(aboutString());
                        } else {
                            try {
                                statement = statement.trim();
                                if ((statement.length() > 0) && (statement.charAt(statement.length() - 1) == ';'))
                                    statement = statement.substring(0, statement.length() - 1);
//                              ParsedScript parsedScript = scriptEngine.parse(statement);
//                              parsedScript.execute(expresser.getDefaultRuntimeData());
                                IExpObject object = expresser.evaluate(runtime, statement);

                                if (object != Expresser.UNDEFINED) { //  undefined でない場合のみ表示
                                    object = typeConverter.toString(runtime, object);
                                    System.out.println(" = " + object);
                                }
                            } catch (ExpException ee) {
                                System.out.println(statement);
                                int max = ee.getPosition();
                                for (int idx = 0; idx < max; idx++)
                                    System.out.print("-");
                                System.out.println("^");
                                System.out.println(ee.getMessage());
                            }
                        }
                    }
                } catch (ExpException ee) {
                    System.out.println(statement);
/*
                    int max = ee.getPosition();
                    for (int idx = 0; idx < max; idx++)
                        System.out.print("-");
                    System.out.println("^");
                    System.out.println(ee.getMessage());
*/
                    if (script != null) {
                        char[] source = script.getSource();
                        if (script != null) {
                            int nPosition = ee.getPosition();
                            int row = 1;
                            int col = 1;
                            for (int idx = 0; idx < nPosition; idx++) {
                                if (source[idx] == '\n') {
                                    row++;
                                    col = 1;
                                }
                                else if (source[idx] == '\t') {
                                    col += 4;   //便宜上
                                }
                                else
                                    col++;
                            }
                            System.err.println(ee.getMessage());
                            System.err.println("  row : " + row);
                            System.err.println("  col : " + col);
                        }
                    } else {
                        ee.printStackTrace();
                    }
                }
                System.out.print(">");
                System.out.flush();
                statement = reader.readLine();
            }
        } else {
            //  引数ありの起動（スクリプトファイルの実行）
            int fileIdx = 0;
            while (fileIdx < argv.length) {
                if (argv[fileIdx].charAt(0) == '-') {
                    if (argv[fileIdx].equals("-charset")) {
                        if (argv.length >= fileIdx + 1)
                            scriptEngine.scriptCharacterSet = argv[fileIdx + 1];
                        else {
                            System.err.println(ScriptEngine.getResString("CharactersetIsNotSpecified"));
                            return;
                        }
                        fileIdx += 2;
                    } else {
                        System.err.println(ScriptEngine.getResString("UnknownOption", argv[fileIdx]));
                        return;
                    }
                } else {
                    //System.out.println("-----------------------------------------------------");
                    //System.out.println("run : " + argv[fileIdx]);
                    //scriptEngine.runScriptFile(argv[fileIdx], true);
                    scriptEngine.runScriptFile(argv[fileIdx], false);
                    fileIdx++;
                }
            }
        }
    }
}
