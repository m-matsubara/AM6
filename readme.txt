■プログラムの説明
  Ignis ECMAScript Engine は ECMAScript Language 3rd edition に基づくJavaで実装されたインタープリタです。
  Javaで開発されたアプリケーションの組み込み用として開発されています。
  同種のソフトウェアとしては rhino や FESI があります。
  -- ECMAScript とは --
    ECMAScript は JavaScript からブラウザ依存の機能を取り除きによりECMAにより規格化されたスクリプト言語です。
      (ECMA : European Computer Manufacturer Association - ヨーロッパ電子計算機工業会)


■とりあえず実行する
  zip ファイルを展開すると am6.jar が含まれています。
  この jar ファイルに対して以下のコマンドを実行します。
  
  >java -jar am6.jar
  
  実行されて出てきたプロンプトからは以下のコマンドが実行できます。
      （[]は任意の内容で置き換える必要があります）
    @run [ファイル名]
      指定されたスクリプトファイルを実行します。
      実行が終了するまでプロンプトは戻りません。
    @start [ファイル名]
      指定されたスクリプトファイルが新しいスレッドで開始されます。
      実行が終了しなくてもプロンプトに戻ります。
    @exit
      プログラムを終了します。
    @mem
      Javaのメモリの状態を表示します。
    [計算式]
      計算式の評価を行います。
  
  引数にスクリプトファイル名を渡すことで実行と同時にスクリプトファイルを実行することもできます。
  
  >java -jar am6.jar [スクリプトファイル名]
  
  例）
  >java -jar am6.jar test/quick_sort.es
  

■ビルド方法
  ビルドを行うと am6.jar が作成されます。これはzip ファイルに添付される am6.jar と基本的に同じものです。（Javaやantのバージョンによる）
  ビルドにはJ2SDK1.2以降とjakarta-ant1.5以降が必要です。
  JAVA_HOME環境変数とANT_HOME環境変数を設定したあとantを実行します。
  ant_build.bat を用いてもビルドできます。
  Eclipse3.0.1以降を用いてもビルドできます。
  正規表現を利用するには jakarta-ORO を利用するか、J2SDK1.4以降を利用する必要があります。


■アプリケーションに組み込む
  このインタープリタをアプリケーションに組み込むには以下の手順が必要です。
  　1. ScriptEngine のインスタンスを作成します。
    2. ファイル・ストリームの何れかからスクリプトを読み込み char の配列を得ます。
    3. 2の結果から ParsedScript オブジェクトを作成します。
    3. ParsedScript オブジェクトを実行します。
  もっとも単純な例は以下のようになります。
    ScriptEngine scriptEngine = new ScriptEngine();
    char[] scriptChars = scriptEngine.loadFromFile(fileName);
    script = scriptEngine.parseScript(scriptChars);
    RuntimeData defaultRuntime = scriptEngine.getExpresser().getDefaultRuntimeData();
    script.execute(defaultRuntime);
  組み込み用途の場合、親アプリケーションを操作するためのオブジェクトをスクリプトから扱えるようにする必要がありますが、次のようにする事で実現します。
    ScriptEngine scriptEngine = new ScriptEngine();
    char[] scriptChars = scriptEngine.loadFromFile(fileName);
    script = scriptEngine.parseScript(scriptChars);
    RuntimeData defaultRuntime = scriptEngine.getExpresser().getDefaultRuntimeData();
    defaultRuntime.setValue("foo", yourApplicationObject);	//	この行！！ 
    script.execute(defaultRuntime);
    
    ※スクリプトからは foo として yourApplicationObject にアクセスできます。 
      yourApplicationObject は任意のオブジェクト型が使用可能です。
  マルチスレッドアプリケーションで使用する場合、ScriptEngineや、ParsedScript を複数作成する必要はありません。runtimeオブジェクトを複製することで対応します。
    //  全体で１回（最初に実行する）
    ScriptEngine scriptEngine = new ScriptEngine();
    char[] scriptChars = scriptEngine.loadFromFile(fileName);
    script = scriptEngine.parseScript(scriptChars);
    RuntimeData defaultRuntime = scriptEngine.getExpresser().getDefaultRuntimeData();
    defaultRuntime.setValue("foo", yourApplicationObject);
    
    //  各々のスレッド内で以下の処理を行います。 （servletのserviceメソッドの中とか…）
    RuntimeData defaultRuntime = scriptEngine.getExpresser().getDefaultRuntimeData();
    RuntimeData threadRuntime = new RuntimeData(script.getDefaultRuntime());
    script.execute(defaultRuntime);
  ScriptThread を使用してマルチスレッドを実現することもできます。
    //  全体で１回（最初に実行する）
    ScriptEngine scriptEngine = new ScriptEngine();
    char[] scriptChars = scriptEngine.loadFromFile(fileName);
    script = scriptEngine.parseScript(scriptChars);
    RuntimeData defaultRuntime = scriptEngine.getExpresser().getDefaultRuntimeData();
    defaultRuntime.setValue("foo", yourApplicationObject);
  
    //  新しいスレッドが作成され、その中でスクリプトが実行されます。
    script.executeNewThread();  //  自動的に新しいRuntimeDataが作成されます。
    
　※いずれの方法でもマルチスレッドの場合、グローバル変数が共有されるので注意が必要です。

  各クラスの簡単な説明
    com.mmatsubara.interpreter.ScriptEngine
      スクリプトを実行するためのフロントエンドとなります。
      通常はアプリケーションに１つあれば十分です。
    com.mmatsubara.interpreter.ParsedScript
      ソースから解析されたスクリプトを保持するオブジェクトです。実態は構文解析木です。
      ソース（スクリプト）１つに対して１つだけ必要です。
    com.mmatsubara.expresser.RuntimeData
      グローバル変数や標準入出力など、スレッド毎に異なる情報を保持します。
      実行を行うスレッド毎に必要です。
      シングルスレッドアプリケーションの場合は、自分で作成する必要はありません。


■互換性
  Ignis は現時点でまだ開発中であるため、ECMAScript Language 3rd edition に対して、いくつかの非互換性を持っています。
  
  ・for-inステートメントでa[0]などの配列は列挙されません

  ・行末の自動セミコロン挿入がサポートされません。
      （文末には必ずセミコロンが必要です）
      作業が大変なので実装されていないだけですが、おそらくサポートしない方が歓迎されそうなので当面このままとします。

  ・Object のコンストラクタに引数を渡してもラッパーオブジェクトとなりません。
  
  ・typeof new Boolean(false) は "object" を返すべきですが、"boolean"を返します。
      （他のラッパーオブジェクトも同様です）

  ・正規表現において、g, i, m の各フラグがサポートされません。

  ・正規表現はOROまたはJava1.4の正規表現ライブラリを利用します。このためJavaScriptの正規表現とは若干互換性に違いがあるようです（調査中）。
      正規表現について
        クラスパス内にJakarta-OROが存在すればそれを利用します。
        見つからなければJava1.4からサポートされた正規表現ライブラリが利用されます。
        いずれも利用できない場合は正規表現使用時に例外が発生します。

  ・実行時エラーについて以下のオブジェクトが存在しません
      EvalError, RangeError, ReferenceError, SyntaxError, TypeError, URIError

  ・ECMAScript にはない synchronized ステートメントがサポートされます。構文は以下の通りです。
      synchronized ( [オブジェクト] ) {
        [ステートメント];
      }

  ・グローバルオブジェクトの writeln, write, print メソッドにて標準出力へ出力することができます。
      writeln(a) 標準出力へ出力後、改行を行う。
      write(a)   標準出力へ出力する。改行は行わない。
      print(a)   標準出力へ出力後、改行を行う。

  ・グローバルオブジェクトの loadClass メソッドにてJavaのクラスをロードできます。使い方は以下の通りです。
      JavaDate = loadClass("java.util.Date");
      jdate = new JavaDate();	//	Javaの java.util.Date クラスのインスタンスを作成する
      writeln(jdate);
      jdate.setTime((0).longValue());    // ※ を参照
      writeln(jdate);
      
      ※Javaクラスのメソッドを呼び出す際、数値型を引数として渡すときは数値型に用意される各メソッドを使い、型変換する必要があります。
        Number.prototype.byteValue()
        Number.prototype.shortValue()
        Number.prototype.intValue()
        Number.prototype.longValue()
        Number.prototype.floatValue()
        Number.prototype.doubleValue()

  ・定数への代入は無視ではなく例外となります。
  
  ・このほかにも自分の気づいていない非互換性が多々あると思われます。


■今後の予定
  ・テスト項目数を増やす。 (test/test.es)
　・互換性を向上させる。
　・JDBC対応する。
  ・ファイル入出力対応する。
  ・GUI対応(Swing? SWT?)する。
  ・日本語リソースしかないので英語リソースを用意する。
  ・エラー周りがいい加減なのを何とかする。（コンパイルエラー・実行時エラーとも）
  ・セキュリティ考える。（考えてみる）
  ・デバッグ機能考える。（そのうち）


■参考資料
  ECMAScript仕様書 (英語・ECMAScript仕様書の仕様書(pdf)があります)
    http://www.ecma-international.org/publications/standards/Ecma-262.htm
  Dynamic Scripting (日本語・JScriptやその他のスクリプト言語のリファレンスなどがあります)
    http://www.interq.or.jp/student/exeal/dss/
  ECMAScript - on Surface of the Depth - (ECMAScriptの重箱の隅をつつくようなコードがたくさん載っています)
    http://www.kmonos.net/alang/etc/ecmascript.php


■著作権表示など
  Ignis interpreter
    ECMAScript Language 3rd Edition based
    Copyright (C) 2005 m.matsubara
    HP : http://www.wind.sannet.ne.jp/m_matsu/
    e-mail : m_matsu@wind.sannet.ne.jp
  
  このプログラムは現状有姿かつ無保証、損害に関して免責という条件下で提供されます。
  現時点でこのプログラムはアルファ版のため、今後仕様が大きく変更される可能性があります。
  
  
