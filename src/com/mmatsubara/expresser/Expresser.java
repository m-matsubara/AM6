package com.mmatsubara.expresser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.mmatsubara.expresser.exception.ExpException;
import com.mmatsubara.expresser.operatorset.*;
import com.mmatsubara.expresser.parsedexpression.ExpArrayInitializer;
import com.mmatsubara.expresser.parsedexpression.ExpObjectInitializer;
import com.mmatsubara.expresser.parsedexpression.IParsedExpression;
import com.mmatsubara.expresser.parsedexpression.Op_ArrayAccessExpression;
import com.mmatsubara.expresser.parsedexpression.Op_FunctionCallExpression;
import com.mmatsubara.expresser.parsedexpression.Op_IncrementExpression;
import com.mmatsubara.expresser.parsedexpression.Op_LogicalAndExpression;
import com.mmatsubara.expresser.parsedexpression.Op_LogicalOrExpression;
import com.mmatsubara.expresser.parsedexpression.Op_MemberAccessExpression;
import com.mmatsubara.expresser.parsedexpression.Op_SubstituteExpression;
import com.mmatsubara.expresser.parsedexpression.ParsedExpression;
import com.mmatsubara.expresser.parsedexpression.WrappedExpression;
import com.mmatsubara.expresser.parsedexpression.WrappedVariableExpression;
import com.mmatsubara.expresser.type.*;
import com.mmatsubara.expresser.type.constructor.ExpArray_constructor;
import com.mmatsubara.expresser.type.constructor.ExpBoolean_constructor;
import com.mmatsubara.expresser.type.constructor.ExpDate_constructor;
import com.mmatsubara.expresser.type.constructor.ExpError_constructor;
import com.mmatsubara.expresser.type.constructor.ExpEvalError_constructor;
import com.mmatsubara.expresser.type.constructor.ExpNumber_constructor;
import com.mmatsubara.expresser.type.constructor.ExpObject_constructor;
import com.mmatsubara.expresser.type.constructor.ExpRangeError_constructor;
import com.mmatsubara.expresser.type.constructor.ExpReferenceError_constructor;
import com.mmatsubara.expresser.type.constructor.ExpRegExp_constructor;
import com.mmatsubara.expresser.type.constructor.ExpString_constructor;
import com.mmatsubara.expresser.type.constructor.ExpSyntaxError_constructor;
import com.mmatsubara.expresser.type.constructor.ExpTypeError_constructor;
import com.mmatsubara.expresser.type.constructor.ExpURIError_constructor;
import com.mmatsubara.expresser.type.property.Global__decodeURI;
import com.mmatsubara.expresser.type.property.Global__decodeURIComponent;
import com.mmatsubara.expresser.type.property.Global__encodeURI;
import com.mmatsubara.expresser.type.property.Global__encodeURIComponent;
import com.mmatsubara.expresser.type.property.Global__escape;
import com.mmatsubara.expresser.type.property.Global__isFinite;
import com.mmatsubara.expresser.type.property.Global__isNaN;
import com.mmatsubara.expresser.type.property.Global__loadClass;
import com.mmatsubara.expresser.type.property.Global__parseFloat;
import com.mmatsubara.expresser.type.property.Global__parseInt;
import com.mmatsubara.expresser.type.property.Global__unescape;
import com.mmatsubara.expresser.type.property.Global__write;
import com.mmatsubara.expresser.type.property.Global__writeln;
import com.mmatsubara.expresser.type.property._Math__abs;
import com.mmatsubara.expresser.type.property._Math__acos;
import com.mmatsubara.expresser.type.property._Math__asin;
import com.mmatsubara.expresser.type.property._Math__atan;
import com.mmatsubara.expresser.type.property._Math__atan2;
import com.mmatsubara.expresser.type.property._Math__ceil;
import com.mmatsubara.expresser.type.property._Math__cos;
import com.mmatsubara.expresser.type.property._Math__exp;
import com.mmatsubara.expresser.type.property._Math__floor;
import com.mmatsubara.expresser.type.property._Math__log;
import com.mmatsubara.expresser.type.property._Math__max;
import com.mmatsubara.expresser.type.property._Math__min;
import com.mmatsubara.expresser.type.property._Math__pow;
import com.mmatsubara.expresser.type.property._Math__random;
import com.mmatsubara.expresser.type.property._Math__round;
import com.mmatsubara.expresser.type.property._Math__sin;
import com.mmatsubara.expresser.type.property._Math__sqrt;
import com.mmatsubara.expresser.type.property._Math__tan;
import com.mmatsubara.resource.DynaResourceBundle;

/**
 * 演算器<br/>
 * 計算式から計算結果を得るクラスです。<br/>
 * <br/>
 * 作成日: 2004/12/23
 * 
 * @author m.matsubara
 * 
 */
public class Expresser {
    private static final int LAST_CHAR_NONE = 0;        //  前の文字はない（最初の文字）
    private static final int LAST_CHAR_OPERATOR = 1;    //  前の文字は演算子
    private static final int LAST_CHAR_VALUE = 2;       //  前の文字は何らかの値（関数呼び出しも含む）
    private static final int LAST_CHAR_NUMBER = 3;      //  前の文字は数字
    private static final int LAST_CHAR_VARIABLE = 5;    //  前の文字は変数
    
    public static String RESOURCE_NAME = "com.mmatsubara.expresser.ExpresserRes";
    public static DynaResourceBundle resource = null;

    /** EcmaScriptの undefined */
    public static final IExpObject UNDEFINED = new Undefined();

    /** OperatorSetで対応できない演算子であった場合返す値（エラー） */
    public static final IExpObject UNSUPPORTED_OPERATOR = new Undefined();	//	UNDEFINEDと同じインスタンスであってはいけない
    
    /** Number型のNaNを表します。 */
    public static final double NaN = Double.NaN;
    
    /**
     *  デフォルトのランタイムデータ(parse時に使います)<br/>
     *  マルチスレッドでない場合、このランタイムデータをそのまま使っても良いが、 
     *  マルチスレッド環境で使用する場合、このデフォルトランタイムデータを引数として新しいランタイムデータを生成する必要があります。
     */
    private RuntimeData defaultRuntimeData;
    
    /** BigDecimalを利用するか */
    private static boolean usingBigDecimal = false;
    
    /** 
     * 演算子セット<br/>
     * ０番目はデフォルト演算子セットとして対応する演算子セットの特定されない型の検索に用いられます。<br/>
     * １番目から対応するクラスに特化した演算子セットを登録していきます。<br/>
     * ある型から継承したクラス用の特化した演算子セットを登録する場合は、継承元のクラス用演算子セットより跡に登録します。<br/>
     */
    private List operatorSetList = new ArrayList();
    /** 型と演算子セットを結びつけます */
    private HashMap operatorSetTable = new HashMap();
    
    /** 演算子の情報 */
    private List opeDataList = new ArrayList();
    
    /** 括弧演算子の識別に使用 */
    private OperatorData opeParenthesis = null;  
    
    /** 予約語を定義する */
    private HashMap reservedWords = new HashMap();
    
    /**
     * Expresser クラスを初期化します。<br/>
     * 演算子を登録し、デフォルトランタイムを作成します。
     * 
     * @exception ExpException
     */
    public Expresser() throws ExpException {
        //  ランタイムデータの作成
        defaultRuntimeData = new RuntimeData();
        defaultRuntimeData.setTypeConverter(new ExpTypeConverter());
        
        //  演算子を登録
        int priority = 0;
        addOpeData(new OperatorData(".",   OperatorData.OP_MEMBER_ACCESS, priority, OperatorData.OP_CLS_BIN, false, false)); 
        opeParenthesis = new OperatorData("(", OperatorData.OP_PARENTHESIS,    priority, OperatorData.OP_CLS_PARENTHESIS, false, false);  
        addOpeData(new OperatorData("new", OperatorData.OP_CREATE_OBJECT, priority, OperatorData.OP_CLS_FRONT_UNITY, false, false)); 
        priority++;

        addOpeData(new OperatorData("++",  OperatorData.OP_ARI_INC_F, priority, OperatorData.OP_CLS_FRONT_UNITY, false, true)); 
        addOpeData(new OperatorData("--",  OperatorData.OP_ARI_DEC_F, priority, OperatorData.OP_CLS_FRONT_UNITY, false, true)); 
        addOpeData(new OperatorData("++",  OperatorData.OP_ARI_INC_B, priority, OperatorData.OP_CLS_BACK_UNITY, true, false)); 
        addOpeData(new OperatorData("--",  OperatorData.OP_ARI_DEC_B, priority, OperatorData.OP_CLS_BACK_UNITY, true, false)); 
        addOpeData(new OperatorData("!",   OperatorData.OP_LOG_NOT, priority, OperatorData.OP_CLS_FRONT_UNITY, false, false)); 
        addOpeData(new OperatorData("~",   OperatorData.OP_BIT_NOT, priority, OperatorData.OP_CLS_FRONT_UNITY, false, false));
        priority++;

        addOpeData(new OperatorData("+",   OperatorData.OP_PLUS,  priority, OperatorData.OP_CLS_FRONT_UNITY, false, false));
        addOpeData(new OperatorData("-",   OperatorData.OP_MINUS, priority, OperatorData.OP_CLS_FRONT_UNITY, false, false));
        priority++;

        addOpeData(new OperatorData("*",   OperatorData.OP_ARI_MUL, priority, OperatorData.OP_CLS_BIN, false, false)); 
        addOpeData(new OperatorData("/",   OperatorData.OP_ARI_DIV, priority, OperatorData.OP_CLS_BIN, false, false)); 
        addOpeData(new OperatorData("%",   OperatorData.OP_ARI_REM, priority, OperatorData.OP_CLS_BIN, false, false)); 
        priority++;

        addOpeData(new OperatorData("+",   OperatorData.OP_ARI_ADD, priority, OperatorData.OP_CLS_BIN, false, false)); 
        addOpeData(new OperatorData("-",   OperatorData.OP_ARI_SUB, priority, OperatorData.OP_CLS_BIN, false, false));
        priority++;

        addOpeData(new OperatorData("<<",  OperatorData.OP_SFT_LEFT, priority, OperatorData.OP_CLS_BIN, false, false)); 
        addOpeData(new OperatorData(">>",  OperatorData.OP_SFT_RIGHT, priority, OperatorData.OP_CLS_BIN, false, false)); 
        addOpeData(new OperatorData(">>>",  OperatorData.OP_SFT_RIGHT_UNSIGNED, priority, OperatorData.OP_CLS_BIN, false, false)); 
        priority++;
        
        addOpeData(new OperatorData(">=",  OperatorData.OP_CMP_GE,  priority, OperatorData.OP_CLS_BIN, false, false)); 
        addOpeData(new OperatorData("<=",  OperatorData.OP_CMP_LE,  priority, OperatorData.OP_CLS_BIN, false, false)); 
        addOpeData(new OperatorData(">",   OperatorData.OP_CMP_GT,  priority, OperatorData.OP_CLS_BIN, false, false)); 
        addOpeData(new OperatorData("<",   OperatorData.OP_CMP_LT,  priority, OperatorData.OP_CLS_BIN, false, false)); 
        priority++;
        
        addOpeData(new OperatorData("instanceof",OperatorData.OP_INSTANCEOF,  priority, OperatorData.OP_CLS_BIN, false, false)); 
        priority++;

        addOpeData(new OperatorData("==",  OperatorData.OP_CMP_EQ,  priority, OperatorData.OP_CLS_BIN, false, false)); 
        addOpeData(new OperatorData("!=",  OperatorData.OP_CMP_NE,  priority, OperatorData.OP_CLS_BIN, false, false)); 
        addOpeData(new OperatorData("===", OperatorData.OP_CMP_EQ_STRICT, priority, OperatorData.OP_CLS_BIN, false, false)); 
        addOpeData(new OperatorData("!==", OperatorData.OP_CMP_NE_STRICT, priority, OperatorData.OP_CLS_BIN, false, false)); 
        priority++;

        addOpeData(new OperatorData("&",   OperatorData.OP_BIT_AND, priority, OperatorData.OP_CLS_BIN, false, false)); 
        priority++;
        
        addOpeData(new OperatorData("^",   OperatorData.OP_BIT_XOR, priority, OperatorData.OP_CLS_BIN, false, false)); 
        priority++;
        
        addOpeData(new OperatorData("|",   OperatorData.OP_BIT_OR,  priority, OperatorData.OP_CLS_BIN, false, false)); 
        priority++;
        
        addOpeData(new OperatorData("&&",  OperatorData.OP_LOG_AND, priority, OperatorData.OP_CLS_BIN, false, false)); 
        priority++;

        addOpeData(new OperatorData("||",  OperatorData.OP_LOG_OR, priority, OperatorData.OP_CLS_BIN, false, false)); 
        priority++;
        
        addOpeData(new OperatorData("?", OperatorData.OP_EXTERNAL_PARSED, priority, OperatorData.OP_CLS_BIN, false, false, new SelectOperatorParser()));

/*        
        addOpeData(new OperatorData(":",  OperatorData.OP_SEL_VALUES, priority, OperatorData.OP_CLS_BIN, false, false)); 
        priority++;
        addOpeData(new OperatorData("?",  OperatorData.OP_SEL_SELECT, priority, OperatorData.OP_CLS_BIN, false, false)); 
        priority++;
*/
        addOpeData(new OperatorData("+=",  OperatorData.OP_ARI_ADD_SUBS, priority, OperatorData.OP_CLS_BIN, true, false)); 
        addOpeData(new OperatorData("-=",  OperatorData.OP_ARI_SUB_SUBS, priority, OperatorData.OP_CLS_BIN, true, false)); 
        addOpeData(new OperatorData("*=",  OperatorData.OP_ARI_MUL_SUBS, priority, OperatorData.OP_CLS_BIN, true, false)); 
        addOpeData(new OperatorData("/=",  OperatorData.OP_ARI_DIV_SUBS, priority, OperatorData.OP_CLS_BIN, true, false)); 
        addOpeData(new OperatorData("%=",  OperatorData.OP_ARI_REM_SUBS, priority, OperatorData.OP_CLS_BIN, true, false)); 
        addOpeData(new OperatorData("&=",  OperatorData.OP_BIT_AND_SUBS, priority, OperatorData.OP_CLS_BIN, true, false)); 
        addOpeData(new OperatorData("|=",  OperatorData.OP_BIT_OR_SUBS,  priority, OperatorData.OP_CLS_BIN, true, false)); 
        addOpeData(new OperatorData("^=",  OperatorData.OP_BIT_XOR_SUBS, priority, OperatorData.OP_CLS_BIN, true, false)); 
        addOpeData(new OperatorData("<<=", OperatorData.OP_SFT_LEFT_SUBS, priority, OperatorData.OP_CLS_BIN, true, false)); 
        addOpeData(new OperatorData(">>=", OperatorData.OP_SFT_RIGHT_SUBS,priority, OperatorData.OP_CLS_BIN, true, false)); 
        addOpeData(new OperatorData(">>>=",OperatorData.OP_SFT_RIGHT_UNSIGNED_SUBS, priority, OperatorData.OP_CLS_BIN, true, false)); 
        addOpeData(new OperatorData("=",   OperatorData.OP_SUBSTITUTE, priority, OperatorData.OP_CLS_BIN, true, false)); 
        priority++;
        
        addOpeData(new OperatorData("delete", OperatorData.OP_DELETE_MEMBER, priority, OperatorData.OP_CLS_FRONT_UNITY, false, true)); 
        addOpeData(new OperatorData("typeof", OperatorData.OP_TYPEOF,        priority, OperatorData.OP_CLS_FRONT_UNITY, false, false)); 
        addOpeData(new OperatorData("void", OperatorData.OP_VOID,            priority, OperatorData.OP_CLS_FRONT_UNITY, false, false)); 
        priority++;
        
        addOpeData(new OperatorData(",",   OperatorData.OP_SEQUENSE, priority, OperatorData.OP_CLS_BIN, false, false)); 
        priority++;
        
        //  各型に対応した演算子セットを登録（後から登録したものが優先して使用される）
        operatorSetList.add(_DefaultOperatorSet.operatorSet);
        operatorSetList.add(VariableAndFunctionOperatorSet.operatorSet);
        operatorSetList.add(DateOperatorSet.operatorSet);
        operatorSetList.add(StringOperatorSet.operatorSet);
        operatorSetList.add(BooleanOperatorSet.operatorSet);
        operatorSetList.add(NumberOperatorSet.operatorSet);
        operatorSetList.add(DoubleOperatorSet.operatorSet);
        operatorSetList.add(LongOperatorSet.operatorSet);
        operatorSetList.add(IntegerOperatorSet.operatorSet);
        if (Expresser.usingBigDecimal)
        	operatorSetList.add(BigDecimalOperatorSet.operatorSet);

        //  規定の定数＆変数を設定
        //  プリミティブ定数
        defaultRuntimeData.setConst(StringToId.toId("null"),     null);
        defaultRuntimeData.setConst(StringToId.toId("true"),     new _Boolean(true));
        defaultRuntimeData.setConst(StringToId.toId("false"),    new _Boolean(false));
        defaultRuntimeData.setValue(StringToId.toId("undefined"),Expresser.UNDEFINED);
        defaultRuntimeData.setValue(StringToId.toId("NaN"),      new _Double(Double.NaN));
        defaultRuntimeData.setValue(StringToId.toId("Infinity"), new _Double(Double.POSITIVE_INFINITY));
        
        //  グローバル定数
        ExpObject mathClass = new ExpObject();
        mathClass.putConstProperty(_Math__abs.ID,               _Math__abs.getInstance());
        mathClass.putConstProperty(_Math__acos.ID,              _Math__acos.getInstance());
        mathClass.putConstProperty(_Math__asin.ID,              _Math__asin.getInstance());
        mathClass.putConstProperty(_Math__atan.ID,              _Math__atan.getInstance());
        mathClass.putConstProperty(_Math__atan2.ID,             _Math__atan2.getInstance());
        mathClass.putConstProperty(_Math__ceil.ID,              _Math__ceil.getInstance());
        mathClass.putConstProperty(_Math__cos.ID,               _Math__cos.getInstance());
        mathClass.putConstProperty(_Math__exp.ID,               _Math__exp.getInstance());
        mathClass.putConstProperty(_Math__floor.ID,             _Math__floor.getInstance());
        mathClass.putConstProperty(_Math__log.ID,               _Math__log.getInstance());
        mathClass.putConstProperty(_Math__max.ID,               _Math__max.getInstance());
        mathClass.putConstProperty(_Math__min.ID,               _Math__min.getInstance());
        mathClass.putConstProperty(_Math__pow.ID,               _Math__pow.getInstance());
        mathClass.putConstProperty(_Math__random.ID,            _Math__random.getInstance());
        mathClass.putConstProperty(_Math__round.ID,             _Math__round.getInstance());
        mathClass.putConstProperty(_Math__sin.ID,               _Math__sin.getInstance());
        mathClass.putConstProperty(_Math__sqrt.ID,              _Math__sqrt.getInstance());
        mathClass.putConstProperty(_Math__tan.ID,               _Math__tan.getInstance());
        if (Expresser.usingBigDecimal) {
            BigDecimal one = new BigDecimal("1.0");
        	BigDecimal sqrt2 = new BigDecimal("1.41421356237309504880168872420969807856967187537694", MathContext.DECIMAL128);					//	http://www.h2.dion.ne.jp/~dra/suu/chi2/heihoukon/2.html
            BigDecimal ln10  = new BigDecimal("2.30258509299404568401799145468436420760110148862877", MathContext.DECIMAL128);					//	http://www.finetune.jp/~lyuka/technote/fract/fract.html
            BigDecimal ln2   = new BigDecimal("0.69314718055994530941723212145817656807550013436025", MathContext.DECIMAL128);					//	http://www.h2.dion.ne.jp/~dra/suu/chi2/shizentaisuu/200.html
            mathClass.putConstProperty(StringToId.toId("E"),        new _BigDecimal("2.71828182845904523536028747135266249775724709369995"));	//	http://www.finetune.jp/~lyuka/technote/fract/fract.html
            mathClass.putConstProperty(StringToId.toId("PI"),       new _BigDecimal("3.14159265358979323846264338327950288419716939937510"));	//	http://www.h2.dion.ne.jp/~dra/suu/enshuritsu/atai/1.html
            mathClass.putConstProperty(StringToId.toId("LN10"),     new _BigDecimal(ln10));
            mathClass.putConstProperty(StringToId.toId("LN2"),      new _BigDecimal(ln2));
            mathClass.putConstProperty(StringToId.toId("LOG10E"),   new _BigDecimal(one.divide(ln10, MathContext.DECIMAL128)));
            mathClass.putConstProperty(StringToId.toId("LOG2E"),    new _BigDecimal(one.divide(ln2, MathContext.DECIMAL128)));
            mathClass.putConstProperty(StringToId.toId("SQRT1_2"),  new _BigDecimal(one.divide(sqrt2, MathContext.DECIMAL128)));
            mathClass.putConstProperty(StringToId.toId("SQRT2"),    new _BigDecimal(sqrt2));
        } else {
            mathClass.putConstProperty(StringToId.toId("E"),        new _Double(Math.E));
            mathClass.putConstProperty(StringToId.toId("PI"),       new _Double(Math.PI));
            mathClass.putConstProperty(StringToId.toId("LN10"),     new _Double(2.302585092994046));
            mathClass.putConstProperty(StringToId.toId("LN2"),      new _Double(0.6931471805599453));
            mathClass.putConstProperty(StringToId.toId("LOG10E"),   new _Double(0.43429448190325176));
            mathClass.putConstProperty(StringToId.toId("LOG2E"),    new _Double(1.4426950408889634));
            mathClass.putConstProperty(StringToId.toId("SQRT1_2"),  new _Double(Math.sqrt(0.5)));
            mathClass.putConstProperty(StringToId.toId("SQRT2"),    new _Double(Math.sqrt(2)));
        }
        defaultRuntimeData.setValue(StringToId.toId("Math"),    mathClass);
        
        //  オブジェクトのコンストラクタ
        //    コンストラクタの準備
        ExpArray.constructor = new ExpArray_constructor();
        ExpBoolean.constructor = new ExpBoolean_constructor();
        ExpDate.constructor = new ExpDate_constructor();
        ExpError.constructor = new ExpError_constructor();
        ExpEvalError.constructor = new ExpEvalError_constructor();
        ExpRangeError.constructor = new ExpRangeError_constructor();
        ExpReferenceError.constructor = new ExpReferenceError_constructor();
        ExpSyntaxError.constructor = new ExpSyntaxError_constructor();
        ExpTypeError.constructor = new ExpTypeError_constructor();
        ExpURIError.constructor = new ExpURIError_constructor();
        ExpNumber.constructor = new ExpNumber_constructor();
        ExpObject.constructor = new ExpObject_constructor();
        ExpRegExp.constructor = new ExpRegExp_constructor();
        ExpString.constructor = new ExpString_constructor();
        defaultRuntimeData.setValue(StringToId.toId("Array"),          ExpArray.constructor);
        defaultRuntimeData.setValue(StringToId.toId("Boolean"),        ExpBoolean.constructor);
        defaultRuntimeData.setValue(StringToId.toId("Date"),           ExpDate.constructor);
        defaultRuntimeData.setValue(StringToId.toId("Error"),          ExpError.constructor);
        defaultRuntimeData.setValue(StringToId.toId("EvalError"),      ExpEvalError.constructor);
        defaultRuntimeData.setValue(StringToId.toId("RangeError"),     ExpRangeError.constructor);
        defaultRuntimeData.setValue(StringToId.toId("ReferenceError"), ExpReferenceError.constructor);
        defaultRuntimeData.setValue(StringToId.toId("SyntaxError"),    ExpSyntaxError.constructor);
        defaultRuntimeData.setValue(StringToId.toId("TypeError"),      ExpTypeError.constructor);
        defaultRuntimeData.setValue(StringToId.toId("URIError"),       ExpURIError.constructor);
        defaultRuntimeData.setValue(StringToId.toId("Number"),         ExpNumber.constructor);
        defaultRuntimeData.setValue(StringToId.toId("Object"),         ExpObject.constructor);
        defaultRuntimeData.setValue(StringToId.toId("RegExp"),         ExpRegExp.constructor);
        defaultRuntimeData.setValue(StringToId.toId("String"),         ExpString.constructor);

        //  Global メソッド
        defaultRuntimeData.setValue(Global__decodeURI.ID,           Global__decodeURI.getInstance());
        defaultRuntimeData.setValue(Global__encodeURI.ID,           Global__encodeURI.getInstance());
        defaultRuntimeData.setValue(Global__decodeURIComponent.ID,  Global__decodeURIComponent.getInstance());
        defaultRuntimeData.setValue(Global__encodeURIComponent.ID,  Global__encodeURIComponent.getInstance());
        defaultRuntimeData.setValue(Global__escape.ID,              Global__escape.getInstance());
        defaultRuntimeData.setValue(Global__isFinite.ID,            Global__isFinite.getInstance());
        defaultRuntimeData.setValue(Global__isNaN.ID,               Global__isNaN.getInstance());
        defaultRuntimeData.setValue(Global__parseFloat.ID,          Global__parseFloat.getInstance());
        defaultRuntimeData.setValue(Global__parseInt.ID,            Global__parseInt.getInstance());
        defaultRuntimeData.setValue(Global__unescape.ID,            Global__unescape.getInstance());
        //  非標準 Global メソッド
        defaultRuntimeData.setValue(StringToId.toId("print"),       Global__writeln.getInstance());
        defaultRuntimeData.setValue(Global__writeln.ID,             Global__writeln.getInstance());
        defaultRuntimeData.setValue(Global__write.ID,               Global__write.getInstance());
        defaultRuntimeData.setValue(Global__loadClass.ID,           Global__loadClass.getInstance());
    }
    
    /**
     * 計算式から計算結果を得ます。<br/>
     * 計算結果は IVariable である場合があります。<br/>
     * この場合、最終的な計算結果を得る場合は IVariable#getValue() を呼び出す必要があります。
     * <br/>
     * 演算子の優先順位<br/>
     * 1 :  . [] () new <br/>
     * 2 : ++ -- ! ~ <br/>
     * 3 : + - (キャストするための型) (+ -は符号としての単項演算子)<br/>
     * 4 : * / %<br/>
     * 5 : + - (加算・減算)<br/>
     * 6 : &lt;&lt; &gt;&gt; &gt;&gt;&gt;<br/>
     * 7 : &gt;= &gt; &lt;= &lt; <br/>
     * 8 : == != === !== <br/>
     * 9 : &<br/>
     * 10: ^<br/>
     * 11: |<br/>
     * 12: &&<br/>
     * 13: ||<br/>
     * 14: ?:<br/>
     * 15: = += -= *= /= %= &= ^= |= &lt;&lt;= &gt;&gt;= &gt;&gt;&gt;=<br/>
     * 16: delete typeof void<br/>
     * 17: ,
     * 
     * @param runtime ランタイムデータ
     * @param sStatement 計算式
     * @return 計算結果です。ただし、計算結果は IVariable である場合があります。
     * @throws ExpException 計算式の誤り
     */
    public IExpObject evaluate(RuntimeData runtime, String sStatement) throws ExpException {
        IParsedExpression parsedExpression = parse(sStatement.toCharArray(), 0, sStatement.length());
        return evaluate(runtime, parsedExpression);
    }
    
    /**
     * 構文解析済み計算式を計算します。
     * 
     * @param runtime ランタイムデータ
     * @param parsedExpression 解析済み計算式
     * @return 計算結果
     * @throws ExpException 不正な計算
     */
    public static IExpObject evaluate(RuntimeData runtime, IParsedExpression parsedExpression) throws ExpException {
        //  演算
        IExpObject result = parsedExpression.evaluate(runtime);
        //  最終演算結果を更新
        runtime.lastResultValue = result;   //  TODO まじめに setter を使った方が早いという噂　
        return result;
    }
    
    /**
     * 計算式の構文解析を行います。
     * @param statement 計算式 
     * @return 構文解析を行った計算式オブジェクト(IParsedExpression)
     * @throws ExpException
     */
    public IParsedExpression parse(String statement) throws ExpException {
        return parse(statement.toCharArray(), 0, statement.length());
    }

    /**
     * 計算式の構文解析を行います。
     * @param statement 計算式
     * @param range 範囲
     * @return 構文解析を行った計算式オブジェクト(IParsedExpression)
     * @throws ExpException
     */
    public IParsedExpression parse(char [] statement, Range range) throws ExpException {
        return parse(statement, range.getBegin(), range.getEnd());
    }
    
    /**
     * 計算式の構文解析を行います。
     * 
     * @param statement 計算式
     * @param begin 開始位置
     * @param end 終了位置
     * @return 構文解析を行った計算式オブジェクト(IParsedExpression)
     * @throws ExpException 計算式の誤り
     */
    public IParsedExpression parse(char [] statement, int begin, int end) throws ExpException {
//      System.out.println(String.valueOf(statement, begin, end - begin));
        
        int parenthesisDepth = 0;    //  括弧の深さ
        //  前後の空白除去(あれば)
        char beginChar = statement[begin];
        while (begin < end && (Character.isSpaceChar(beginChar) || beginChar == '\t' || beginChar == '\r' || beginChar == '\n')) {
            begin++;
            beginChar = statement[begin];
        }
        char endChar = beginChar;
        if (begin != end - 1) {
            endChar = statement[end - 1];
            while (begin < end && (Character.isSpaceChar(endChar) || endChar == '\t' || endChar == '\r' || endChar == '\n')) {
                end--;
                endChar = statement[end - 1];
            }
        }
        if (begin >= end)
            throw new ExpException(getResString("EnptyExpression"), begin);

        //
        //  計算式中で最低の優先順位の演算子を捜す
        //
        int idx = begin;
        OperatorData operator = null;
        int opeIdx = -1;    //  見つかった演算子の位置
        int opePri = -1;    //  見つかった演算子の優先度
        int lastCharType = Expresser.LAST_CHAR_NONE;
        while (idx < end) {
            char ch = statement[idx]; 
            if (ch == '(' || ch == '[' || ch == '{') {
                //  括弧
                if (opePri <= opeParenthesis.getPriority()) {
                    operator = opeParenthesis;
                    opeIdx = idx;
                    opePri = opeParenthesis.getPriority();
                }
                idx = skipParenthesis(statement, idx, end);
                lastCharType = Expresser.LAST_CHAR_VALUE;   //  括弧が閉じているので値が括弧で括られているか、関数呼び出しの終わり
            }
            else if (ch == '\"' || ch == '\'') {
                idx = skipString(statement, idx, end);
                lastCharType = Expresser.LAST_CHAR_VALUE;    //  文字列なので値
            }
            else if ((lastCharType == Expresser.LAST_CHAR_NONE || lastCharType == Expresser.LAST_CHAR_OPERATOR) && ch == '/') {
                idx = skipRegExp(statement, idx, end);
                lastCharType = Expresser.LAST_CHAR_VALUE;   //  正規表現なので値
            }
            /*
             * 括弧の深さが 0 の場合、演算子を検索
             */
            else if (parenthesisDepth == 0) {
                //  一番優先順位の低い演算子を探す
                if ('0' <= ch && ch <= '9' || 
                		(((lastCharType == Expresser.LAST_CHAR_NUMBER) || (lastCharType == Expresser.LAST_CHAR_NONE) || (lastCharType == Expresser.LAST_CHAR_OPERATOR)) && ch == '.')) {
                    idx = skipToken(statement, idx, end);
                    lastCharType = Expresser.LAST_CHAR_NUMBER;
                }
                else if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n') {
                    idx++;
                }
                else {
                    int nOpIdx;
                    int nOpMax = opeDataList.size();
                    boolean findOperator = false;
                    //  評価位置の文字から演算子が開始しているか、チェックする
                    for (nOpIdx = 0; nOpIdx < nOpMax; nOpIdx++) {
                        OperatorData opeData = (OperatorData)opeDataList.get(nOpIdx); 
                        if (opeData.compare(statement, idx, end)) { //  演算子の文字列が一致した
                            if (((lastCharType == Expresser.LAST_CHAR_NUMBER || lastCharType == Expresser.LAST_CHAR_VALUE || lastCharType == Expresser.LAST_CHAR_VARIABLE) && opeData.getOpeClass() != OperatorData.OP_CLS_FRONT_UNITY) ||
                                    ((lastCharType == Expresser.LAST_CHAR_NONE || lastCharType == Expresser.LAST_CHAR_OPERATOR) && opeData.getOpeClass() == OperatorData.OP_CLS_FRONT_UNITY)) {
                                //  評価位置の前の文字が、(数字 or 変数) and 演算子が前置単項演算子でない
                                //  あるいは
                                //  評価位置の前の文字が、(無い or 演算子) and 演算子が前置単項演算子である
                                if (opePri < opeData.getPriority()) {
                                    //  今見つかった演算子の方が優先順位が低い！！
                                    operator = opeData;
                                    opeIdx = idx;
                                    opePri = opeData.getPriority();
                                } else if (opePri == opeData.getPriority() && (opeData.getOpeClass() != OperatorData.OP_CLS_FRONT_UNITY) && opeData.getOperator() != OperatorData.OP_SUBSTITUTE) {
                                    //  前見つかった演算子と優先順位が同じだが後にあるので実質優先順位低い
                                    //  （前置単項演算子はのぞく）
                                    operator = opeData;
                                    opeIdx = idx;
                                    opePri = opeData.getPriority();
                                }
                                if (opeData.getOpeClass() == OperatorData.OP_CLS_BACK_UNITY)
                                    lastCharType = Expresser.LAST_CHAR_VALUE;       //  演算子が見つかった（後置単項演算子以外）
                                else
                                    lastCharType = Expresser.LAST_CHAR_OPERATOR;    //  演算子が特定できたが後置単項演算子なので「値が見つかった」と見なす。
                                idx += opeData.getOperatorChars().length;
                                findOperator = true;
                                break;
                            }
                        }
                    }
                    if (findOperator == false) {
                        if (Character.isUnicodeIdentifierStart(ch) || Character.isJavaIdentifierStart(ch)) {
                            idx = skipToken(statement, idx, end);
                            lastCharType = Expresser.LAST_CHAR_VARIABLE;
                        } else {
                            throw new ExpException(getResString("IllegalCharacter"), opeIdx);
                        }
                    }
                }
            }
            else
                idx++;
        }
        if (parenthesisDepth != 0) {
            throw new ExpException(getResString("UnmatchParenthesis"), idx);
        }
        
        //
        //  見つけた演算子から IParsedExpression のインスタンスを作成する
        //
        if (operator == null) {
            //
            //  演算子がない
            //
            
            //  変数または定数
            if (('0' <= beginChar && beginChar <= '9') || beginChar == '.') {
                //  数字定数
                return new WrappedExpression(toValue(statement, begin, end));
            }
            if (beginChar == '\"' || beginChar == '\'') {
                //  文字列定数
                return new WrappedExpression(toStrValue(statement, begin, end));
            }
            if (beginChar == '/') {
                //  正規表現
                return new WrappedExpression(toRegExpValue(statement, begin, end));
            }
            else {
                //  変数
                String variableName = trim(substring(statement, begin, end));
                if (isIdentifier(variableName) == false)
                    throw new ExpException(getResString("IllegalIdentifier", variableName), begin);
                Integer variableId = StringToId.toId(variableName);
                IVariable variable = defaultRuntimeData.getVariable(variableId, false);
                if (variable != null && variable.isConst())        //  変数が事前に定義された定数である場合、parse時に計算してしまう
                    return new WrappedExpression(variable.getValue());
                return new WrappedVariableExpression(variableName, variableId, begin);
            }
        }
        else {
            //
            //  変数や定数ではなく計算式（演算子がある）
            //
            if (operator.getOriginalParser() != null)
                return operator.getOriginalParser().parse(this, statement, begin, end, opeIdx, operator.toString());
            
            //  演算対象を確定
            IParsedExpression operand1Expression = null;
            IParsedExpression operand2Expression = null;
            
            //  左オペランド（第一オペランド）
            if (operator.getOperator() == OperatorData.OP_PARENTHESIS) {
                ;
            } else if (operator.getOpeClass() == OperatorData.OP_CLS_BIN || operator.getOpeClass() == OperatorData.OP_CLS_BACK_UNITY) {
                if (begin < opeIdx)
                    operand1Expression = parse(statement, begin, opeIdx);
            }

            //  右オペランド（第二オペランド）
            if (operator.getOperator() == OperatorData.OP_MEMBER_ACCESS) {
                int opeLen = operator.getOperatorChars().length;
                String identifier = substring(statement, opeIdx + opeLen, end);
                if (isIdentifier(identifier) == false)
                    throw new ExpException(getResString("IllegalIdentifier", identifier), opeIdx + opeLen);
                Integer memberID = StringToId.toId(identifier);
                return new Op_MemberAccessExpression((IParsedExpression)operand1Expression, memberID, opeIdx);
            } else {
                if (operator.getOpeClass() == OperatorData.OP_CLS_BIN || operator.getOpeClass() == OperatorData.OP_CLS_FRONT_UNITY) {
                    int opeLen = operator.getOperatorChars().length;
                    if (opeIdx + opeLen < end) {
                        operand2Expression = parse(statement, opeIdx + opeLen, end);
                    } else {
                        throw new ExpException(getResString("OperandNotFound"), opeIdx + opeLen);
                    }
                    
                }
            }

            //  単項演算子のチェック
            if (begin != opeIdx) {
                if (operator.getOpeClass() == OperatorData.OP_CLS_FRONT_UNITY)
                    throw new ExpException(getResString("UnnecessaryLeftOperand"), opeIdx);
            }
            if (end != opeIdx + operator.toString().length()) {
                if (operator.getOpeClass() == OperatorData.OP_CLS_BACK_UNITY)
                    throw new ExpException(getResString("UnnecessaryRightOperand"), opeIdx);
            }
                
            if (operator.getOperator() == OperatorData.OP_PARENTHESIS) {
                if (statement[opeIdx] == '(') {
                    //  不要な括弧で括られているので、はずして再計算する
                    if (opeIdx == begin) {
                        return parse(statement, begin + 1, end - 1);
                    }

                    //  関数（メソッド）呼び出し
                    IParsedExpression function = parse(statement, begin, opeIdx);
                    int argStart = opeIdx + operator.getOperatorChars().length;
                    int argEnd = skipParenthesis(statement, opeIdx, end) - 1;
                    IParsedExpression[] arguments = splitFunctionArgs(statement, argStart, argEnd);    //  引数をカンマで分解
                    //  関数呼び出し演算子はOperatorSetではなく専用のIParsedExpressionオブジェクトによって処理されます。
                    return new Op_FunctionCallExpression(function, arguments, opeIdx);
                } else if (statement[opeIdx] == '[') {
                    if (begin != opeIdx) {
                        //  オペレータの位置は式の開始位置ではない→配列へのアクセス
                        IParsedExpression object = parse(statement, begin, opeIdx);
                        IParsedExpression objIndex = parse(statement, opeIdx + operator.getOperatorChars().length, end - 1);
                        //  配列アクセス演算子はOperatorSetではなく専用のIParsedExpressionオブジェクトによって処理されます。
                        return new Op_ArrayAccessExpression(object, objIndex, opeIdx);
                    } else {
                        //  オペレータの位置は式の開始位置→配列の定義
                        return defineArray(statement, opeIdx + 1, end - 1);
                    }
                } else if (statement[opeIdx] == '{') {
                    //  オブジェクトの初期化
                    return defineObject(statement, opeIdx + 1, end - 1);
                }
                throw new ExpException(getResString("InternalError", "Expresser#parse()", "unknown parenthesis"), opeIdx);    //  実装に誤りがある
            } else {
                if (operand1Expression == null && operand2Expression == null)
                    throw new ExpException(getResString("OperandNotFound"), opeIdx);
                
                //  定数はあらかじめ計算してしまう
                if (operand1Expression != null && operand1Expression.isConst()) {
                    IExpObject object = ((IParsedExpression)operand1Expression).evaluate(defaultRuntimeData); 
                    operand1Expression = new WrappedExpression(object);
                }
                if (operand2Expression != null && operand2Expression.isConst()) {
                    IExpObject object = ((IParsedExpression)operand2Expression).evaluate(defaultRuntimeData); 
                    operand2Expression = new WrappedExpression(object);
                }
                
                //  変数が要求されているのに定数になっていないかチェック
                /*
                if (operator.isLeftVariable() && (operand1 instanceof IParsedExpression == false))
                    throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), opeIdx);
                if (operator.isRightVariable() && (operand2 instanceof IParsedExpression == false))
                    throw new ExpException(Expresser.getResString("NotSubstituteToConstant"), opeIdx);
                */
                
                IParsedExpression result;
                if (operator.getOperator() == OperatorData.OP_ARI_INC_B) {
                    //後置インクリメント演算子はOperatorSetではなく専用のIParsedExpressionオブジェクトによって処理されます。
                    result = new Op_IncrementExpression(operand1Expression, opeIdx, 1);
                } else if (operator.getOperator() == OperatorData.OP_ARI_DEC_B) {
                    //後置デクリメント演算子はOperatorSetではなく専用のIParsedExpressionオブジェクトによって処理されます。
                    result = new Op_IncrementExpression(operand1Expression, opeIdx, -1);
                } else if (operator.getOperator() == OperatorData.OP_SUBSTITUTE) {
                    //代入演算子はOperatorSetではなく専用のIParsedExpressionオブジェクトによって処理されます。
                    result = new Op_SubstituteExpression((IParsedExpression)operand1Expression, operand2Expression, opeIdx);
                } else if (operator.getOperator() == OperatorData.OP_LOG_AND) {
                    //代入演算子はOperatorSetではなく専用のIParsedExpressionオブジェクトによって処理されます。
                    result = new Op_LogicalAndExpression(operand1Expression, operand2Expression, opeIdx);
                } else if (operator.getOperator() == OperatorData.OP_LOG_OR) {
                    //代入演算子はOperatorSetではなく専用のIParsedExpressionオブジェクトによって処理されます。
                    result = new Op_LogicalOrExpression(operand1Expression, operand2Expression, opeIdx);
                } else {
                    result = new ParsedExpression(operand1Expression, operand2Expression, operator, opeIdx, this);
                }
                return result;
            }
        }
    }
    
    /**
     * {} (中括弧)に書かれた内容からオブジェクトを生成します<br/>
     * 例)<br/>
     *   {aaa:0, bbb:"text"}
     * 
     * @param string 対象
     * @param begin 開始位置
     * @param end 終了位置
     * @exception ExpException
     */
    public IParsedExpression defineObject(char[] string, int begin, int end) throws ExpException {
        Range[] properties = split(',', string, begin, end);
        int max = properties.length;
        //  最後の項目が空なら削除する
        if (max >= 1 && (properties[max - 1].getBegin() == properties[max - 1].getEnd()))
            max--;
        
        //  プロパティとして持つべきIDのリスト
        List propertyIdList = new ArrayList();
        //  プロパティの式のリスト
        List propertyExpressionList = new ArrayList();
        
        //  プロパティとするべき名前（のID）と式を列挙する
        for (int idx = 0; idx < max; idx++) {
            Range property = properties[idx];
            if (property.getBegin() != property.getEnd()) {
                Range propertyRange[] = split(':', string, property.getBegin(), property.getEnd());
                if (propertyRange.length != 2)
                    throw new ExpException(getResString("SyntaxError"), property.getBegin());
                Range propertyNameRange = propertyRange[0]; 
                Range propertyExpressionRange = propertyRange[1];
                String propertyName = substring(string, propertyNameRange.getBegin(), propertyNameRange.getEnd());
                if (isIdentifier(propertyName) == false)
                    throw new ExpException(getResString("IllegalIdentifier", propertyName), property.getBegin());
                IParsedExpression propertyExpression = parse(string, propertyExpressionRange);
                
                propertyIdList.add(StringToId.toId(propertyName));
                propertyExpressionList.add(propertyExpression);
            }
        }
        //  propertyIdListとpropertyExpressionListを使ってオブジェクトを初期化
        max = propertyIdList.size();
        Integer[] propertyIds = new Integer[max]; 
        IParsedExpression[] propertyExpressions = new IParsedExpression[max];
        
        propertyIdList.toArray(propertyIds);
        propertyExpressionList.toArray(propertyExpressions);
        
        return new ExpObjectInitializer(propertyIds, propertyExpressions);
    }
    
    /**
     * [] (大括弧)に書かれた内容から配列を生成します<br/>
     * 例)<br/>
     *   [0, "text"]
     * 
     * @param string 対象
     * @param begin 開始位置
     * @param end 終了位置
     * @exception ExpException
     */
    public IParsedExpression defineArray(char[] string, int begin, int end) throws ExpException {
        Range[] elements = split(',', string, begin, end);
        int max = elements.length;
        //  最後の項目が空なら削除する
        if (max >= 1 && (elements[max - 1].getBegin() == elements[max - 1].getEnd()))
            max--;
        
        //  各要素の式のリスト
        List elementsExpressionList = new ArrayList();
        //  各要素を列挙しリストに格納する
        for (int idx = 0; idx < max; idx++) {
            Range element = elements[idx];
            if (element.getBegin() != element.getEnd()) {
                IParsedExpression propertyExpression = parse(string, element);
                elementsExpressionList.add(propertyExpression);
            } else {
                elementsExpressionList.add(new WrappedExpression(UNDEFINED));
            }
        }
        
        max = elementsExpressionList.size();
        IParsedExpression[] elementsExpressions = new IParsedExpression[max];
        
        elementsExpressionList.toArray(elementsExpressions);
        
        return new ExpArrayInitializer(elementsExpressions);
    }
    
    /**
     * char配列のある範囲から文字列を生成します
     * @param string 対象
     * @param begin 開始位置
     * @param end 終了位置
     * @return 対象に対する、開始位置から終了位置までの文字列
     */
    public static String substring(char[] string, int begin, int end) {
        //  前後の空白除去(あれば)
    	if (begin >= end)
    		return "";
        char beginChar = string[begin];
        while (begin < end && (Character.isSpaceChar(beginChar) || beginChar == '\t' || beginChar == '\r' || beginChar == '\n')) {
            begin++;
            beginChar = string[begin];
        }
        char endChar = beginChar;
        if (begin != end - 1) {
            endChar = string[end - 1];
            while (begin < end && (Character.isSpaceChar(endChar) || endChar == '\t' || endChar == '\r' || endChar == '\n')) {
                end--;
                endChar = string[end - 1];
            }
        }
        return new String(string, begin, end - begin);
        
    }
    
    
    /**
     * 文字列の前後の空白文字を読み飛ばします
     * @param src 元文字列
     * @return 前後の空白を取り除いた文字列
     */
    public static String trim(String src) {
        char[] chars = src.toCharArray();
        int idx = 0;
        int max = src.length();
        while (idx < max && (Character.isSpaceChar(chars[idx]) || chars[idx] == '\t' || chars[idx] == '\r' || chars[idx] == '\n'))
            idx++;
        while (idx < max && (Character.isSpaceChar(chars[max - 1]) || chars[max - 1] == '\t' || chars[max - 1] == '\r' || chars[max - 1] == '\n' ))
            max--;
        return String.valueOf(chars, idx, max - idx);
    }
    
    
    /**
     * ステートメントの中で、空白文字を読み飛ばします（スペース, タブ, 改行…）
     * @param script ステートメント
     * @param begin 開始位置
     * @param end 終了位置
     * @return nBeginから空白を読み飛ばし、空白でない最初の文字
     */
    public static int skipWhitespace(char[] script, int begin, int end) {
        int idx = begin;
        while (idx < end) {
            if (Character.isSpaceChar(script[idx]) == false && (script[idx] != '\t') && (script[idx] != '\r') && (script[idx] != '\n')) {
                return idx;
            }
            idx++;
        }
        return end;
    }
    
    
    /**
     * ステートメントの中で、クォートでくくられた範囲を読み飛ばします
     * @param script ステートメント
     * @param begin 文字列リテラルの開始位置（必ず " または ' であること）
     * @param end 終了位置
     * @return 文字列リテラルの終わり位置 " + 1
     * @exception ExpException 
     */
    public static int skipString(char[] script, int begin, int end) throws ExpException {
        char beginChar = script[begin]; 
        if (beginChar != '\"' && beginChar != '\'') {
            throw new ExpException(getResString("InternalError", "Expresser#skipString()"), begin);
        }
        
        int idx = begin + 1;
        while (idx < end) {
            char ch = script[idx];
            if (ch == '\n') {
                throw new ExpException(getResString("StringHasNotEnded"), idx);
            }
            else if (ch == beginChar) {
                return idx + 1;
            }
            else if (ch == '\\')
                idx += 2;
            else
                idx++;
        }
        throw new ExpException(getResString("StringHasNotEnded"), begin);
    }
    
    /**
     * ステートメントの中で、正規表現を読み飛ばします
     * @param script
     * @param begin 開始位置（必ず / であること）
     * @param end 終了位置
     * @return 正規表現の終わりを示す / の位置 + 1
     * @throws ExpException
     */
    public static int skipRegExp(char[] script, int begin, int end) throws ExpException {
        char beginChar = script[begin]; 
        if (beginChar != '/') {
            throw new ExpException(getResString("InternalError", "Expresser#skipRegExp()"), begin);
        }
        
        int idx = begin + 1;
        while (idx < end) {
            char ch = script[idx];
            if (ch == '\n') {
                throw new ExpException(getResString("RegexHasNotEnded"), idx);
            }
            else if (ch == '/') {
            	idx++;
            	if (idx < end) {
            		ch = script[idx];
            		if (ch == 'i')
                        return idx;
            		else if (ch == 'g')
                        return idx;
            		else if (ch == 'm')
                        return idx;
            	}
                return idx;
            }
            else if (ch == '\\')
                idx += 2;
            else
                idx++;
        }
        throw new ExpException(getResString("RegexHasNotEnded"), begin);
    }

    /**
     * ステートメントの中で、括弧でくくられた範囲を読み飛ばします
     * @param script
     * @param begin 開始位置（必ず (, [, { であること）
     * @param end 終了位置
     * @return 括弧の終わり位置 + 1
     */
    public static int skipParenthesis(char[] script, int begin, int end) throws ExpException {
        char endChar;
        switch (script[begin]) {
        case '(':
            endChar = ')';
            break;
        case '[':
            endChar = ']';
            break;
        case '{':
            endChar = '}';
            break;
        default:
            throw new ExpException(getResString("InternalError", "Expresser#skipParenthesis()", "unknown parenthesis"), begin);
        }

        int idx = begin + 1;
        while (idx < end) {
            char ch = script[idx];
            if (ch == '(' || ch == '[' || ch == '{')
                idx = skipParenthesis(script, idx, end);
            else if (ch == endChar)
                return idx + 1;
            else if (ch == '\"' || ch == '\'')
                idx = skipString(script, idx, end);
//          else if (ch == '/')
//              idx = skipRegExp(script, idx, end);
            else
                idx++;
        }
        throw new ExpException(getResString("UnmatchParenthesis"), begin);
    }

    /**
     * ステートメントの中で、１単語読み飛ばします
     * @param script
     * @param begin 開始位置
     * @param end 単語の終わり位置 + 1
     * @return 括弧の終わり位置 + 1
     */
    public int skipToken(char[] script, int begin, int end) {
        char firstChar = script[begin];
        if (firstChar < '0' || (firstChar > '9' && firstChar < 'A') ||  (firstChar > 'Z' && firstChar < 'a') || (firstChar > 'z' && firstChar < 0x80)) {
            if (firstChar != '$' && firstChar != '_') { //  $ と _ は記号として扱わない
                //  記号は演算子として扱う。アルファベットから始まる演算子はここでは通常のトークンと同じように扱う
                int nOpIdx;
                int nOpMax = opeDataList.size();
                for (nOpIdx = 0; nOpIdx < nOpMax; nOpIdx++) {
                    OperatorData opeData = (OperatorData)opeDataList.get(nOpIdx); 
                    if (opeData.compare(script, begin, end)) {
                        return begin + opeData.length();
                    }
                }
            }
        }
        //  (, [, {, ", 'も読み飛ばすべきかも？
        
        //  数字の判定は簡易です。途中で変な文字が入っていてもスルーします。この場合、後に呼び出される toValue() メソッドでエラーとなります。
        boolean isNumber = ((firstChar >= '0' && firstChar <= '9') || firstChar == '.');
        int idx = begin;
        while (idx < end) {
            char ch = script[idx];
            if (isNumber && (ch == 'e' || ch == 'E') && (idx + 1 < end))
                idx += 2;
            else if (Character.isUnicodeIdentifierPart(ch) || Character.isJavaIdentifierPart(ch))    
                idx++;
            else if (ch > 0xFF)    
                idx++;
            else if (ch == '.') {
                if (isNumber == false)
                    return idx;
                idx++;
            } else
                return idx;
        }
        return idx;
    }
    
    
    /**
     * begin 位置からのからの単語を一つ返します。
     * @param script スクリプト
     * @param begin 開始位置
     * @param end 終了位置
     * @return 単語
     */
    public String nextToken(char[] script, int begin, int end) {
        int tokenEnd = skipToken(script, begin, end);
        return new String(script, begin, tokenEnd - begin); 
    }
    
    /**
     * 判定位置の文字列が演算子と一致するか検査します。
     * @param script スクリプト
     * @param begin 開始位置
     * @param end 終了位置
     * @return 演算子であった場合 true
     */
    public boolean isOperator(char[] script, int begin, int end) {
        int nOpIdx;
        int nOpMax = opeDataList.size();
        for (nOpIdx = 0; nOpIdx < nOpMax; nOpIdx++) {
            OperatorData opeData = (OperatorData)opeDataList.get(nOpIdx); 
            if (opeData.compare(script, begin, end)) {
                return true;
            }
        }
        return false;
    }
    

    /**
     * 指定された文字で指定されたスクリプトの範囲を分割します
     * @param splitChar 分割する文字
     * @param script スクリプト
     * @param begin 開始位置
     * @param end 終了位置
     * @return 分割された範囲オブジェクトの配列
     * @throws ExpException
     */
    public static Range[] split(char splitChar, char[] script, int begin, int end) throws ExpException {
        List resultList = new ArrayList();
        int idx = begin;
        int beginIdx = idx;
        int endIdx;
        while (idx < end) {
            char ch = script[idx];
            if (ch == splitChar) {
                endIdx = idx;
                //  前後の空白を削除
                while (Character.isSpaceChar(script[beginIdx]) && beginIdx < endIdx)
                    beginIdx++;
                while (Character.isSpaceChar(script[endIdx - 1]) && beginIdx < endIdx)
                    endIdx--;

                resultList.add(new Range(beginIdx, endIdx));
                idx++;
                beginIdx = idx;
            } else if (ch == '(' || ch == '{' || ch == '[') {
                idx = skipParenthesis(script, idx, end);
            } else if (ch == '"' || ch == '\'') {
                idx = skipString(script, idx, end);
            } else {
                idx++;
            }
        }
        endIdx = idx;
        //  前後の空白を削除
        while (Character.isSpaceChar(script[beginIdx]) && beginIdx < endIdx)
            beginIdx++;
        while (Character.isSpaceChar(script[endIdx - 1]) && beginIdx < endIdx)
            endIdx--;

        if (beginIdx <= endIdx)
            resultList.add(new Range(beginIdx, endIdx));

        //  List から配列（Range[]）に変換
        Range[] result = new Range[resultList.size()];
        for (idx = 0; idx < result.length; idx++)
            result[idx] = (Range)(resultList.get(idx)); 
        return result;
    }
    
    /**
     * 文字列の引数をカンマで分解します
     * 
     * @param arguments 引数群
     * @param begin 開始位置
     * @param end 終了位置
     * @return 分解された引数（計算済み）
     * @throws Exception 計算の実行時エラー
     */
    public IParsedExpression[] splitFunctionArgs(char[] arguments, int begin, int end) throws ExpException {
        List list = new ArrayList();
        int argBegin = begin;
        
        int max = end;
        int idx = begin;
        while (idx < max) {
            char ch = arguments[idx];
            if (ch == '(' || ch == '[' || ch == '{')
                idx = skipParenthesis(arguments, idx, end); //  括弧は読み飛ばす
            else if (ch == '\"' || ch == '\'')
                idx = skipString(arguments, idx, end);      //  文字列も読み飛ばす
            else {
                if (arguments[idx] == ',') {
                    //  引数を１つずつ評価する
                    Object oArg = parse(arguments, argBegin, idx);
                    list.add(oArg);
                    argBegin = idx + 1;
                }
                idx++;
            }
            
        }
        if (argBegin != max) {
            Object oArg = parse(arguments, argBegin, max);
            list.add(oArg);
        }
        //  IParsedExpressionの配列へ
        max = list.size();
        IParsedExpression[] result = new IParsedExpression[max];
        for (idx = 0; idx < max; idx++) {
            result[idx] = (IParsedExpression)list.get(idx);
        }
        return result;
    }
    
    /**
     * ステートメントの一部を整数型または実数型に変換します
     * @param sStatement ステートメント
     * @param begin 開始位置
     * @param end 終了位置
     * @return 変換されたオブジェクト
     * @throws ExpException 数字として認識できない例外
     */
    public static _Double toValue(char[] statement, int begin, int end) throws ExpException {
        long longValue = 0;
        double doubleValue = 0;
        double baseValue = 1;
//      boolean doubleFlag = false;
        boolean doubleFlag = true;
        boolean minusFlag = false;
        boolean periodFlag = false;
        int radix = 10;
        int idx = begin;
        
        //  先頭文字が 0 の場合基数変更
        if (statement[idx] == '0' && end > idx + 1) {
            if ((statement[idx + 1] == 'x' || statement[idx + 1] == 'X')) {
                //  １６進数
                radix = 16;
                idx += 2;
            } else if ( statement[idx + 1] == '.') {
                //  やっぱり１０進数（ただし実数）
            } else {
                //  ８進数
                radix = 8;
            }
        }
        if ((radix == 10) && (usingBigDecimal)) {
        	String str = String.copyValueOf(statement, begin, end - begin).trim();
        	if (str.charAt(0) == '.')
        		str = "0" + str;
        	try {
        		BigDecimal bd = new BigDecimal(str, MathContext.DECIMAL128);
        		return new _BigDecimal(bd);
        	} catch (RuntimeException ex) {
                throw new ExpException(getResString("IllegalNumericRepresentation", new String(statement, begin, end - begin)), idx);
        	}
        }
        for (; idx < end; idx++) {
            char ch = statement[idx];
            if ('0' <= ch && ch <= '9') {
            	if ((longValue >= 100000000) && (doubleFlag == false)) {
            		doubleFlag = true;
                    doubleValue = longValue;
            	}
            	if (doubleFlag == false) {
                    int value = (ch - '0');
                    if (value >= radix)
                        throw new ExpException(getResString("IllegalNumericRepresentation", new String(statement, begin, end - begin)), idx);
                        
                    longValue *= radix;
                    longValue += (ch - '0');
                }
                else {
                    doubleValue *= radix;
                    doubleValue += (ch - '0');
                    if (periodFlag)
                    	baseValue *= radix;
                }
            } else if ('a' <= ch && ch <= 'f' && radix == 16) {
                longValue *= radix;
                longValue += (10 + ch - 'a');
            } else if ('A' <= ch && ch <= 'F' && radix == 16) {
                longValue *= radix;
                longValue += (10 + ch - 'A');
            } else if (ch == '.') {
                if (periodFlag)
                    throw new ExpException(getResString("IllegalNumericRepresentation", new String(statement, begin, end - begin)), idx);
                if (radix != 10)
                    throw new ExpException(getResString("IllegalNumericRepresentation", new String(statement, begin, end - begin)), idx);
                if (doubleFlag == false)
                	doubleValue = longValue;
                doubleFlag = true;
                periodFlag = true;
            }
            else if (ch == '-') {
                if (idx == begin)
                    minusFlag = true;
                else
                    throw new ExpException(getResString("IllegalNumericRepresentation", new String(statement, begin, end - begin)), idx);
            }
            else if (ch == 'e' || ch == 'E') {
                //  指数つき実数
                try {
                    int exp = ((_Double)toValue(statement, idx + 1, end)).intValue();
                    if (doubleFlag)
                        return new _Double(doubleValue / baseValue * Math.pow(radix, exp));
                    else
                        return new _Double(longValue * Math.pow(radix, exp));
                }
                catch (Exception e) {
                    throw new ExpException(getResString("IllegalNumericRepresentation", new String(statement, begin, end - begin)), idx + 1);
                }
            }
            else {
                throw new ExpException(getResString("IllegalNumericRepresentation", new String(statement, begin, end - begin)), idx);
            }
        }
        if (minusFlag) {
            longValue = -longValue;
            doubleValue = -doubleValue;
        }
        if (doubleFlag) {
            if (doubleValue == 0 && minusFlag)
                return new _Double(-0.0);
            return new _Double(doubleValue / baseValue);
        } else {
            if (longValue == 0 && minusFlag)
                return new _Double(-0.0);
            return new _Double(longValue);
/*            
            if (Integer.MIN_VALUE <= longValue && longValue <= Integer.MAX_VALUE)
                return new _Integer((int)longValue);
            else
                return new _Long(longValue);
*/                
        }
    }
    
    
    /**
     * 文字列表現から文字列オブジェクトを生成します
     * @param sStatement ステートメント
     * @param begin 開始位置
     * @param end 終了位置
     * @return 変換されたオブジェクト
     * @throws ExpException
     */
    private IExpObject toStrValue(char[] statements, int begin, int end) throws ExpException {
        if (begin >= end)
            throw new ExpException(getResString("StringHasNotEnded"), begin);
        if (statements[begin] != '\"' && statements[begin] != '\'')
            throw new ExpException(getResString("InternalError", "Expresser#toStrValue()"), begin);
        if (statements[begin] != statements[end - 1])
            throw new ExpException(getResString("StringHasNotEnded"), begin);
        
        StringBuffer result = new StringBuffer();
        int idx = begin + 1;
        int max = end - 1;
        while (idx < max) {
            char ch = statements[idx];
            if (ch != '\\') {
                result.append(ch);
                idx++;
            } else {
                if (idx == max - 1)
                    throw new ExpException(getResString("StringHasNotEnded"), begin);
                char ch2 = statements[idx + 1];
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
                case 'u':
                    if (idx + 6 > end)
                        throw new ExpException(getResString("InvalidEscapeSequence"), idx);
                    result.append(hexToChar(statements, idx + 2));
                    idx += 4;
                    break;
                    
                default:
                    throw new ExpException(getResString("InvalidEscapeSequence"), idx);
                }
                idx += 2;
            }
        }
        
        return new _String(result.toString());
    }

    /**
     * 正規表現から正規表現オブジェクトを生成します
     * @param sStatement ステートメント
     * @param begin 開始位置
     * @param end 終了位置
     * @return 変換されたオブジェクト
     * @throws ExpException
     */
    private IExpObject toRegExpValue(char[] statements, int begin, int end) throws ExpException {
        boolean ignoreFlag = false;
        boolean globalFlag = false;
        boolean multilineFlag = false;
    	if (begin >= end)
            throw new ExpException(getResString("RegexHasNotEnded"), begin);
        if (statements[begin] != '/')
            throw new ExpException(getResString("InternalError", "Expresser#toRegExpValue()"), begin);

        while ((statements[end - 1] == 'i') || (statements[end - 1] == 'g') || (statements[end - 1] == 'm')) {
        	if (statements[end - 1] == 'i')
        		ignoreFlag = true;
        	else if (statements[end - 1] == 'g')
        		ignoreFlag = true;
        	else if (statements[end - 1] == 'm')
        		ignoreFlag = true;
        	end--;
        }
        if (statements[end - 1] != '/')
            throw new ExpException(getResString("RegexHasNotEnded"), begin);
        
        StringBuffer result = new StringBuffer();
        int idx = begin + 1;
        int max = end - 1;
        while (idx < max) {
            char ch = statements[idx];
            if (ch != '\\') {
                result.append(ch);
                idx++;
            } else {
                if (idx == max - 1)
                    throw new ExpException(getResString("StringHasNotEnded"), begin);
                char ch2 = statements[idx + 1];
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
                case 'u':
                    if (idx + 6 > end)
                        throw new ExpException(getResString("InvalidEscapeSequence"), idx);
                    result.append(hexToChar(statements, idx + 2));
                    idx += 4;
                    break;
                    
                default:
                    throw new ExpException(getResString("InvalidEscapeSequence"), idx);
                }
                idx += 2;
            }
        }
        String flagStr = "";
        if (ignoreFlag)
        	flagStr = flagStr + "i"; 
        if (globalFlag)
        	flagStr = flagStr + "g"; 
        if (multilineFlag)
        	flagStr = flagStr + "m"; 
        
        return new ExpRegExp(result.toString(), flagStr);
    }
    
    
    /**
     * 0～9, a～f, A～F で構成される４文字から char 型を生成します
     * @param statements ステートメント
     * @param begin 開始位置
     * @return 生成された１文字
     * @throws ExpException
     */
    public char hexToChar(char[] statements, int begin) throws ExpException {
        char result = 0;
        int max = begin + 4;
        for (int idx = begin; idx < max; idx++) {
            byte by;
            if ('0' <= statements[idx] && statements[idx] <= '9')
                by = (byte)(statements[idx] - '0');
            else if ('a' <= statements[idx] && statements[idx] <= 'f')
                by = (byte)(statements[idx] - 'a' + 10);
            else if ('A' <= statements[idx] && statements[idx] <= 'F')
                by = (byte)(statements[idx] - 'A' + 10);
            else
                throw new ExpException(getResString("IllegalCharacter"), idx);
            
            result <<= 4;
            result |= by; 
        }
        return result;
    }
    
    
    
    /**
     * 識別子が変数名や関数名として成立するかチェックします
     * 
     * @param identifier 識別子
     * @return 識別子として成立する場合 true
     */
    public boolean isIdentifier(String identifier) {
        int max = identifier.length(); 
        if (max == 0)
            return false;
        if (reservedWords.get(identifier) != null)
            return false;
        char ch = identifier.charAt(0);
        if (!Character.isUnicodeIdentifierStart(ch) && !Character.isJavaIdentifierStart(ch))
            return false;
        for (int idx = 1; idx < max; idx++) {
            ch = identifier.charAt(idx);
            if (!Character.isUnicodeIdentifierPart(ch) && !Character.isJavaIdentifierPart(ch))
                return false;
        }
        return true;
    }
    
    /**
     * プリミティブタイプ(int, doubleとか…)をExpInteger, ExpDouble等に変換します<br>
     * プリミティブタイプ型以外はそのまま値を返す
     * 
     * @param value JavaのリフレクションAPIから戻ってきた値
     * @param type JavaのリフレクションAPIから戻ってきた型
     * @return Expresser で対応する型に変換されたオブジェクト
     */
    public static IExpObject convertPrimitiveType(Object value, Class type) throws ExpException {
        if (type == int.class)
            return new _Integer(((Integer)value).intValue());
        else if (type == double.class)
            return new _Double(((Double)value).doubleValue());
        else if (type == long.class)
            return new _Long(((Long)value).longValue());
        else if (type == byte.class)
            return new _Byte(((Byte)value).byteValue());
        else if (type == short.class)
            return new _Short(((Double)value).shortValue());
        else if (type == double.class)
            return new _Float(((Float)value).floatValue());
        else if (type == char[].class)  //  char の配列は文字列扱いにしてしまう
            return new ExpString(String.valueOf((char[])value));
        else if (type == char.class) {
            return new ExpString(String.valueOf(value));
        } else
            return new JavaObject(value);
    }
    
    
    /**
     * 演算子セットを戻します
     * 
     * @param idx 演算子セットのインデックス
     * @return 演算子セット
     */
    public IOperatorSet getOperatorSet(int idx) {
        return (IOperatorSet)operatorSetList.get(idx);
    }

    /**
     * 演算子セットのリストを戻します。
     * @return 演算子セットのリスト
     */
    public List getOperatorSetList() {
        return operatorSetList;
    }
    
    /**
     * Expresser に登録された演算子セットの中からオペランドに対応した演算子セットを検索します。
     * @param operand 演算対象
     * @return 演算対象に対応した演算子セット
     */
    public IOperatorSet searchOperatorSet(Object operand) {
        IOperatorSet opeSet;
        //  高速化用ハッシュテーブルに登録されたものから検索
        if (operand != null)
            opeSet = (IOperatorSet)operatorSetTable.get(operand.getClass());
        else
            return (IOperatorSet)operatorSetList.get(0);
            
        if (opeSet != null)
            return opeSet;
        
        //  operatorSetList から検索
        int setMax = operatorSetList.size();
        for (int setIdx = setMax - 1; setIdx >= 0; setIdx--) {
            opeSet = (IOperatorSet)operatorSetList.get(setIdx);
            Class[] types = opeSet.targetType();
            int typeMax = types.length;
            for (int typeIdx = 0; typeIdx < typeMax; typeIdx++) {
                if (types[typeIdx].isInstance(operand)) {
                    operatorSetTable.put(operand.getClass(), opeSet);    //高速化用ハッシュテーブルへ登録    
                    return opeSet;
                }
            }
        }
        return null;
    }
    
    /**
     * 新しい演算子セットを追加します
     * 
     * @param operatorSet 新しい演算子セット
     */
    public void setOperatorSet(IOperatorSet operatorSet) {
        operatorSetList.add(operatorSet);
    }

    /**
     * デフォルトのランタイムデータを返します
     * @return デフォルトのランタイムデータ
     */
    public RuntimeData getDefaultRuntimeData() {
        return defaultRuntimeData;
    }
    
    /**
     * デフォルトのランタイムデータを設定します
     * @param runtime ランタイムデータ
     * 
     */
    public void setDefaultRuntimeData(RuntimeData runtime) {
        defaultRuntimeData = runtime;
    }
    
    public static boolean getUsingBigDecimal() {
    	return usingBigDecimal;
    }
    
    public static void setUsingBigDecimal(boolean value) {
    	usingBigDecimal = value;
    }
    
    /**
     * オペレータデータを追加します
     * @param operatorData 演算子データ
     */
    public void addOpeData(OperatorData operatorData) {
        String opeString = String.valueOf(operatorData.getOperatorChars()); 
        
        /* 識別子として認識できる文字から始まる演算子は予約語として登録*/
        if (Character.isUnicodeIdentifierStart(opeString.charAt(0)) || Character.isJavaIdentifierPart(opeString.charAt(0)))
            addReservedWord(opeString);

        /* 演算子として登録 */
        int max = opeDataList.size();
        for (int idx = 0; idx < max; idx++) {
            OperatorData workOperatorData = (OperatorData)opeDataList.get(idx);
            if (workOperatorData.getOperatorChars().length < operatorData.getOperatorChars().length) {
                opeDataList.add(idx, operatorData);
                return;
            }
        }
        opeDataList.add(operatorData);
        
    }
    
    /**
     * 登録されている演算子の中から指定のIDをもつ演算子データを検索します
     * @param operator OperatorData.OPE_**
     * @return オペレータデータ
     */
    public OperatorData findOpeData(int operator) {
        int max = opeDataList.size();
        for (int idx = 0; idx < max; idx++) {
            OperatorData opeData = (OperatorData)opeDataList.get(idx);
            if (opeData.getOperator() == operator) {
                return opeData;
            }
        }
        return null;
    }
    
    /**
     * 単語をを予約語として登録します<br/>
     * isIdentifier() でfalseを返すようになります。 
     * @param reservedWord 予約語として登録する単語
     */
    public void addReservedWord(String reservedWord) {
        reservedWords.put(reservedWord, "");
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
     * Expresser クラス用のドライバーメソッド
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println("jcalc version 0.5.0 - copyright(C) 2005-2011 m.matsubara");
/*
        Package[] packages = Package.getPackages();
        for (int n = 0; n < packages.length; n++) {
            System.out.println(packages[n].getName());
        }
*/         
        Expresser expresser = new Expresser();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String statement = reader.readLine();
        while (statement.equals(".") == false) {
            try {
                if (statement.length() != 0)
                    System.out.println(" = " + expresser.evaluate(expresser.getDefaultRuntimeData(), statement));
            } catch (ExpException ce) {
                System.out.println(statement);
                int max = ce.getPosition();
                for (int idx = 0; idx < max; idx++)
                    System.out.print("-");
                System.out.println("^");
                System.out.println(ce.getMessage());
                
                ce.printStackTrace();
            }
            statement = reader.readLine();
        }
    }
}
