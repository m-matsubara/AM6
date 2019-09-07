package com.mmatsubara.expresser;


/**
 * 演算子の情報を保持するクラスです。<br/>
 * 演算子は各々 int 型のIDを持ちます。<br/>
 * <br/>
 * 作成日: 2005/02/05
 * 
 * @author m.matsubara
 *
 */
public class OperatorData {
    public static final int OP_CLS_BIN = 1;            //  二項演算子
    public static final int OP_CLS_FRONT_UNITY = 2;    //  前置単項演算子
    public static final int OP_CLS_BACK_UNITY = 3;    //  後置単項演算子
    public static final int OP_CLS_PARENTHESIS = 4;    //  括り型

    /** 外部定義（originalParserを使用する） */
    public static final int OP_EXTERNAL_PARSED=0;    

    /** = 代入 */
    public static final int OP_SUBSTITUTE=101;    
    /** - 負記号 */
    public static final int OP_PLUS=102;    
    /** - 負記号 */
    public static final int OP_MINUS=103;    
    /** . メンバアクセス */
    public static final int OP_MEMBER_ACCESS=104;    
    /** ( [ 括弧 */
    public static final int OP_PARENTHESIS=105;
    /** ( 関数呼び出し */
    public static final int OP_CALL_FUNCTION=106;
    /** [ 配列呼び出し */
    public static final int OP_ARRAY_ACCESS=107;
    /** new オブジェクト生成 */
    public static final int OP_CREATE_OBJECT=108;
    /** delete 演算子 */
    public static final int OP_DELETE_MEMBER=109;
    /** void 演算子 */
    public static final int OP_VOID=110;
    /** delete 演算子 */
    public static final int OP_TYPEOF=111;
    /** , 演算子 */
    public static final int OP_SEQUENSE=112;
    /** , 演算子 */
    public static final int OP_INSTANCEOF=113;

    /** + 算術加算 */
    public static final int OP_ARI_ADD=201;
    /** - 算術減算 */
    public static final int OP_ARI_SUB=202;    
    /** * 算術乗算 */
    public static final int OP_ARI_MUL=203;    
    /** / 算術除算 */
    public static final int OP_ARI_DIV=204;    
    /** % 算術剰余 */
    public static final int OP_ARI_REM=205;    

    /** || 論理和 */
    public static final int OP_LOG_OR=301;
    /** && 論理積 */
    public static final int OP_LOG_AND=302;
    /** !  論理否定 */
    public static final int OP_LOG_NOT=303;

    /** == 比較等価 */
    public static final int OP_CMP_EQ=401;
    /** != 比較非等価 */
    public static final int OP_CMP_NE=402;
    /** >= 比較大なりイコール */
    public static final int OP_CMP_GE=403;
    /** >  比較大なり */
    public static final int OP_CMP_GT=404;
    /** <= 比較小なりイコール */
    public static final int OP_CMP_LE=405;
    /** < 比較小なり */
    public static final int OP_CMP_LT=406;
    /** === 比較等価 */
    public static final int OP_CMP_EQ_STRICT=407;
    /** === 比較等価 */
    public static final int OP_CMP_NE_STRICT=408;
    
    /** | ビット和 */
    public static final int OP_BIT_OR=501;
    /** & ビット積 */
    public static final int OP_BIT_AND=502;
    /** ~ ビット反転 */
    public static final int OP_BIT_NOT=503;
    /** ^ ビット排他的論理和 */
    public static final int OP_BIT_XOR=504;
    
    /** <<  ビットシフト 左 */
    public static final int OP_SFT_LEFT=601;
    /** >>  ビットシフト 右 */
    public static final int OP_SFT_RIGHT=602;
    /** >>> ビットシフト 右（符号なし） */
    public static final int OP_SFT_RIGHT_UNSIGNED=603;
    
    /** ++ 前置代入インクリメント */
    public static final int OP_ARI_INC_F=701;
    /** -- 前置代入デクリメント */
    public static final int OP_ARI_DEC_F=702;
    /** ++ 後置代入インクリメント */
    public static final int OP_ARI_INC_B=703;
    /** -- 後置代入デクリメント */
    public static final int OP_ARI_DEC_B=704;
    /** += 代入加算 */
    public static final int OP_ARI_ADD_SUBS=705;
    /** -= 代入減算 */
    public static final int OP_ARI_SUB_SUBS=706;
    /** *= 代入乗算 */
    public static final int OP_ARI_MUL_SUBS=707;
    /** /= 代入除算 */
    public static final int OP_ARI_DIV_SUBS=708;
    /** %= 代入剰余 */
    public static final int OP_ARI_REM_SUBS=709;
    /** &= 代入ビット積 */
    public static final int OP_BIT_AND_SUBS=710;
    /** |= 代入ビット和 */
    public static final int OP_BIT_OR_SUBS=711;
    /** ^= 代入ビット排他的論理和 */
    public static final int OP_BIT_XOR_SUBS=712;
    /** <<=  代入ビットシフト 左 */
    public static final int OP_SFT_LEFT_SUBS=713;
    /** >>=  代入ビットシフト 右 */
    public static final int OP_SFT_RIGHT_SUBS=714;
    /** >>>= 代入ビットシフト 右（符号なし） */
    public static final int OP_SFT_RIGHT_UNSIGNED_SUBS=715;

    
    private char[] operatorChars;                //  演算子
    private int operatorValue;                        //  演算子(OperatorData.OP_*)
    private int priority;                        //  優先度
    private int opeClass;                        //  二項演算子・前置単項演算子・後置単項演算子・括り方
    private boolean leftVariable;                //  左オペランドが変数であるべき時true
    private boolean rightVariable;            //  右オペランドが変数であるべき時true
    private IOperatorParser originalParser;    //  独自のパーサー（有れば指定可能）
    
    public OperatorData(String operator, int operatorValue, int priority, int opeClass, boolean leltVariable, boolean rightVariable, IOperatorParser originalParser) {
        initialize(operator, operatorValue, priority, opeClass, leltVariable, rightVariable, originalParser);
    }

    public OperatorData(String operator, int operatorValue, int priority, int opeClass, boolean leftVariable, boolean rightVariable) {
        initialize(operator, operatorValue, priority, opeClass, leftVariable, rightVariable, null);
    }
    
    private void initialize(String operator, int operatorValue, int priority, int opeClass, boolean leftVariable, boolean rightVariable, IOperatorParser originalParser) {
        this.operatorChars = operator.toCharArray();
        this.operatorValue = operatorValue;
        this.priority = priority;
        this.opeClass = opeClass;
        this.leftVariable = leftVariable;
        this.rightVariable = rightVariable;
        this.originalParser = originalParser;
    }
    
    /**
     * ステートメントの指定位置が自分自身のオペレータと一致するか調べる
     * @param statements ステートメント
     * @param opeIdx 評価位置
     * @param end 終了位置
     * @return 一致する場合 true
     */
    public boolean compare(char[] statements, int opeIdx, int end) {
        int max = operatorChars.length;
        //  ステートメントの長さチェック
        if (opeIdx + max > end)
            return false;
        //  あっているかチェック
        for (int idx = 0; idx < max; idx++) {
            if (operatorChars[idx] != statements[opeIdx + idx])
                return false;
        }
        //  アルファベット（または$や_）から始まる演算子は次の文字が文字や数字であってはいけない
        char firstChar = operatorChars[0];
        if (Character.isUnicodeIdentifierStart(firstChar) || Character.isJavaIdentifierStart(firstChar)) {
            if (opeIdx + max < end) {
                char chNext = statements[opeIdx + max];
                if (Character.isUnicodeIdentifierPart(chNext) || Character.isJavaIdentifierPart(chNext)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    
    /**
     * 演算子文字列の char 型配列を戻す
     * 
     * @return 演算子文字列の char 型配列 
     */
    public char[] getOperatorChars() {
        return operatorChars;
    }
    
    /**
     * 演算子の種類
     *  OperatorData.OP_CLS_BIN : 二項演算子
     *  OperatorData.OP_CLS_FRONT_UNITY : 前置単項演算子
     *  OperatorData.OP_CLS_BACK_UNITY : 後置単項演算子
     *  OperatorData.OP_CLS_PARENTHESIS : 括り型
     * 
     * @return opeClass を戻します。
     */
    public int getOpeClass() {
        return opeClass;
    }
    /**
     * 演算子の番号を返す
     * 
     * @return 演算子の番号
     */
    public int getOperator() {
        return operatorValue;
    }
    /**
     * 独自定義パーサーを返す
     * 
     * @return 独自定義パーサーがあればそのオブジェクト、なければ null
     */
    public IOperatorParser getOriginalParser() {
        return originalParser;
    }
    /**
     * 演算子の優先度
     * 
     * @return 演算子の優先度
     */
    public int getPriority() {
        return priority;
    }

    /**
     * 左オペランドが変数である必要があるか
     * 
     * @return 左オペランドが変数である必要がある場合 ture
     */
    public boolean isLeftVariable() {
        return leftVariable;
    }

    /**
     * 右オペランドが変数である必要があるか
     * 
     * @return 右オペランドが変数である必要がある場合 ture
     */
    public boolean isRightVariable() {
        return rightVariable;
    }

    public String toString() {
        return new String(operatorChars); 
    }
    
    /**
     * 演算子の文字列表現での長さを返します。
     * @return 演算子の文字列表現での長さ 
     */
    public int length() {
        return operatorChars.length;
    }
}
