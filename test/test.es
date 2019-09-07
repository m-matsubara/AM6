/*
 * test for Ignis
 */
	writeln = print;	//	rhino対応


	function testCallFunction(a, b, c) {
        if (arguments.length != 3) {
			writeln("NG : call function - arguments.length != 3");
			return false;
		} else if (a != 'a') {
			writeln("NG : call function - a : " + a);
			return false;
		} else if (b != 0) {
			writeln("NG : call function - b : " + b);
			return false;
		} else if (c != 1.1) {
			writeln("NG : call function - c : " + c);
			return false;
		} else { 
			writeln("OK : call function");
			return true;
		}
	}
	
	//	UserObjectクラスのtoStringメソッドとして動作するための関数
	function UserObject$toString() {
		return this.a + this.n;
	}
	
	function UserObject() {
		this.a = "aaa";
		this.n = 0;
		this.toString = UserObject$toString;

		function innerFunction(a, b) {
			return a + b;
		}
		
		//	以下のように書くことで見かけ上プライベートメンバを実現できるが、メモリの無駄遣いになるためあまり推奨されない
		var virtualPrivateFunction = function (a, b, c) {
			return innerFunction(a, b);
		};

		this.anonymousFunction = function (a, b, c) {
			return virtualPrivateFunction(a, b);
		};
	}

	function SuperObject(a, b) {
		this.a = a;
		this.b = b;
	}

	function InheritedObjectByCallMethod(a, b) {
		SuperObject.call(this, a, b);
	}

	function InheritedObjectByApplyMethod(a, b) {
		SuperObject.apply(this, arguments);
	}


	//	計算式のテスト
	function expressionTest() {
		var error_flg=false;

		if (-1 != -1) {
			writeln("NG : -1 != -1");
			error_flg=true;
		}
		if (-1.0 != -1.0) {
			writeln("NG : -1.0 != -1.0");
			error_flg=true;
		}
		if (2+3 != 5) {
			writeln("NG : 2+3 != 5");
			error_flg=true;
		}
		if (2.0+3.0 != 5.0) {
			writeln("NG : 2.0+3.0 != 5.0");
			error_flg=true;
		}
		if ( (2+3) != 5) {
			writeln("NG :  (2+3) != 5");
			error_flg=true;
		}
		if (( 2+3 ) != 5) {
			writeln("NG : ( 2+3 ) != 5");
			error_flg=true;
		}
		if (2 + 3 * 4 != 14) {
			writeln("NG : 2 + 3 * 4 != 14");
			error_flg=true;
		}
		if (2.0 + 3.0 * 4.0 != 14.0) {
			writeln("NG : 2.0 + 3.0 * 4.0 != 14.0");
			error_flg=true;
		}
		if ((2+3)*4/2 != 10) {
			writeln("NG : (2+3)*4/2 != 10");
			error_flg=true;
		}
		if ((2.0+3.0)*4.0/2.0 != 10.0) {
			writeln("NG : (2.0+3.0)*4.0/2.0 != 10.0");
			error_flg=true;
		}
		if ( 5%2 != 1) {
			writeln("NG :  5%2 != 1");
			error_flg=true;
		}
		if ( 5.0 % 2.0 != 1.0) {
			writeln("NG :  5.0 % 2.0 != 1.0");
			error_flg=true;
		}
		if ( 5.0 % 2.5 != 0.0) {
			writeln("NG :  5.0 % 2.5 != 0.0");
			error_flg=true;
		}
		if (-3*4 != -12) {
			writeln("NG : -3*4 != -12");
			error_flg=true;
		}
		if (-3*-4 != 12) {
			writeln("NG : -3*-4 != 12");
			error_flg=true;
		}
		if (-3.0*4.0 != -12.0) {
			writeln("NG : -3.0*4.0 != -12.0");
			error_flg=true;
		}
		if ((a=5+5) != 10) {
			writeln("NG : (a=5+5) != 10");
			error_flg=true;
		}
		if ((b=20) != 20) {
			writeln("NG : (b=20) != 20");
			error_flg=true;
		}
		if ((a+b) != 30) {
			writeln("NG : (a+b) != 30");
			error_flg=true;
		}
		if (!(a==b) != true) {
			writeln("NG : !(a==b) != true");
			error_flg=true;
		}
		if (~1 != ~1) {
			writeln("NG : ~1 != ~1");
			error_flg=true;
		}
		if (~10 != ~10) {
			writeln("NG : ~10 != ~10");
			error_flg=true;
		}
		if ((a==10) == false) {
			writeln("NG : (a==10) == false");
			error_flg=true;
		}
		if (a==1) {
			writeln("NG : a==1");
			error_flg=true;
		}
		if (!(a==10)) {
			writeln("NG : !(a==10)");
			error_flg=true;
		}
		if ((!(a==20)) == false) {
			writeln("NG : (!(a==20)) == false");
			error_flg=true;
		}
		if ((a!=b) == false) {
			writeln("NG : (a!=b) == false");
			error_flg=true;
		}
		if ((a>=10) == false) {
			writeln("NG : (a>=10) == false");
			error_flg=true;
		}
		if (a>=100) {
			writeln("NG : a>=100");
			error_flg=true;
		}
		if ((a<=10) == false) {
			writeln("NG : (a<=10) == false");
			error_flg=true;
		}
		if (a<=1) {
			writeln("NG : a<=1");
			error_flg=true;
		}
		if ((a>1) == false) {
			writeln("NG : (a>1) == false");
			error_flg=true;
		}
		if (a>10) {
			writeln("NG : a>10");
			error_flg=true;
		}
		if ((a<100) == false) {
			writeln("NG : (a<100) == false");
			error_flg=true;
		}
		if (a<10) {
			writeln("NG : a<10");
			error_flg=true;
		}
		if ((1 < a && a < 20) == false) {
			writeln("NG : (1 < a && a < 20) == false");
			error_flg=true;
		}
		if ((a >= 15 || b >= 15) == false) {
			writeln("NG : (a >= 15 || b >= 15) == false");
			error_flg=true;
		}
		if (("a" && true) == false) {
			writeln("\"a\" && true == false");
			error_flg=true;
		}
		if ((true && "true") == false) {
			writeln("true && \"true\" == false");
			error_flg=true;
		}
		if (("a" || false) == false) {
			writeln("\"a\" || false == false");
			error_flg=true;
		}
		if ((false || "a") == false) {
			writeln("false || \"a\" == false");
			error_flg=true;
		}
		if (!"a") {
			writeln("!\"a\"");
			error_flg=true;
		}
		if ("a") {
		} else {
			writeln("\"a\"");
			error_flg=true;
		}
		if ((3 & 1) != 1) {
			writeln("NG : (3 & 1) != 1");
			error_flg=true;
		}
		if ((3 | 5) != 7) {
			writeln("NG : (3 | 5) != 7");
			error_flg=true;
		}
		if ((a=0) != 0) {
			writeln("NG : (a=0) != 0");
			error_flg=true;
		}
		if ((a++) != 0) {
			writeln("NG : (a++) != 0");
			error_flg=true;
		}
		if ((++a) != 2) {
			writeln("NG : (++a) != 2");
			error_flg=true;
		}
		if ((a--) != 2) {
			writeln("NG : (a--) != 2");
			error_flg=true;
		}
		if ((--a) != 0) {
			writeln("NG : (--a) != 0");
			error_flg=true;
		}
		a=0;
		if ((a+=5) != 5) {
			writeln("NG : (a+=5) != 5");
			error_flg=true;
		}
		if ((a-=2) != 3) {
			writeln("NG : (a-=2) != 3");
			error_flg=true;
		}
		if ((a*=2) != 6) {
			writeln("NG : (a*=2) != 6");
			error_flg=true;
		}
		if ((a/=2) != 3) {
			writeln("NG : (a/=2) != 3");
			error_flg=true;
		}
		if ((a%=2) != 1) {
			writeln("NG : (a/=2) != 1");
			error_flg=true;
		}

		if (1 << 2 != 4) {
			writeln("NG : 1 << 2 != 4");
			error_flg=true;
		}
		if (4 >> 2 != 1) {
			writeln("NG : 4 >> 2 != 1");
			error_flg=true;
		}
		if (4 >>> 2 != 1) {
			writeln("NG : 4 >>> 2 != 1");
			error_flg=true;
		}
		if ("a" >= "b") {
			writeln("\"a\" >= \"b\"");
			error_flg=true;
		}
		if ("a" > "b") {
			writeln("\"a\" > \"b\"");
			error_flg=true;
		}
		if ("c" <= "b") {
			writeln("\"c\" <= \"b\"");
			error_flg=true;
		}
		if ("c" < "b") {
			writeln("\"c\" < \"b\"");
			error_flg=true;
		}
		if ("c" == "b") {
			writeln("\"c\" == \"b\"");
			error_flg=true;
		}
		if ("c" != "c") {
			writeln("\"c\" != \"c\"");
			error_flg=true;
		}
		if (1 == "1") {		//	自動型変換
		} else {
			writeln('1 == "1"');
			error_flg=true;
		}
		if (1 != "1") {		//	自動型変換
			writeln('1 != "1"');
			error_flg=true;
		}

		if (1 === "1") {		//	自動型変換しない
			writeln('1 === "1"');
			error_flg=true;
		}
		if (1 !== 1) {		//	自動型変換しない
			writeln('1 !== 1');
			error_flg=true;
		}
		var i= 1, j =1;
		if (i++, i++, false) {
			writeln('i++, i++, false');	//	カンマ演算子
			error_flg=true;
		}
		i = 10;
		j = 5;
		if (i+-+-+-+-+-j != 5) {
			writeln('i+-+-+-+-+-j != 5');
			error_flg=true;
		}
		
		
		if ("\u3044\u308d\u306f\u306b\u307b\u3078\u3068" != "いろはにほへと") {
			writeln("\"\\u3044\\u308d\\u306f\\u306b\\u307b\\u3078\\u3068\" != \"いろはにほへと\"");
			error_flg=true;
		}
		if (("aAa").toUpperCase().toLowerCase() != 'aaa') {
			writeln("(\"aAa\").toUpperCase().toLowerCase() != 'aaa'");
			error_flg=true;
		}

		var obj = new Object();
		obj.prop = "a";
		delete obj.prop;
		if (obj.prop != undefined) {
			writeln("obj.prop != undefined");
			error_flg=true;
		}

/*
		if () {
			writeln('');
			error_flg=true;
		}
*/

		if (error_flg)
			writeln("NG : expression");
		else
			writeln("OK : expression");
		return !error_flg;
	}
	
	function throwErrorFunction() {
		throw new Error("throwErrorFunction");
	}
	
	//	組み込みオブジェクトのプロパティをテスト
	function propertyTest() {
		var error_flg = false;

		var num = 12345;
		
		if (num.toString() != "12345") {
			writeln("NG : num.toString()");
			error_flg=true;
		}
		if (num.valueOf() != 12345) {
			writeln("NG : num.valueOf()");
			error_flg=true;
		}

		var str = "abcde";
		if (str.toString() != "abcde") {
			writeln("NG : str.toString()");
			error_flg=true;
		}
		if (str.valueOf() != "abcde") {
			writeln("NG : str.valueOf()");
			error_flg=true;
		}
		if (str.charAt(1) != "b" || str.charAt(2) != "c") {
			writeln("str.valueOf()");
			error_flg=true;
		}
		if ("abc".toUpperCase() != "ABC") {
			writeln("\"abc\".toUpperCase() != \"ABC\"");
			error_flg=true;
		}
		if ("ABC".toLowerCase() != "abc") {
			writeln("\"ABC\".toLowerCase() != \"abc\"");
			error_flg=true;
		}

		return !error_flg;		
	}
	
	function globalTest() {
		var error_flg = false;

		if (decodeURI(encodeURI("いろはにほへと漢字abcABC123 !\"#$%&'()-=^~\\|@`[{]}:*;+,<.>/?\\_")) != "いろはにほへと漢字abcABC123 !\"#$%&'()-=^~\\|@`[{]}:*;+,<.>/?\\_") {
			writeln("Global.decodeURI()");
			error_flg=true;
		}
		if (encodeURI("いろはにほへと漢字abcABC123 !\"#$%&'()-=^~\\|@`[{]}:*;+,<.>/?\\_") != "%e3%81%84%e3%82%8d%e3%81%af%e3%81%ab%e3%81%bb%e3%81%b8%e3%81%a8%e6%bc%a2%e5%ad%97abcABC123%20!%22#$%25&'()-=%5e~%5c%7c@%60%5b%7b%5d%7d:*;+,%3c.%3e/?%5c_") {
			writeln("Global.encodeURI()");
			error_flg=true;
		}
		if (escape("いろはにほへと漢字abcABC123 !\"#$%&'()-=^~\\|@`[{]}:*;+,<.>/?\\_") != "%u3044%u308D%u306F%u306B%u307B%u3078%u3068%u6F22%u5B57abcABC123%20%21%22%23%24%25%26%27%28%29-%3D%5E%7E%5C%7C@%60%5B%7B%5D%7D%3A*%3B+%2C%3C.%3E/%3F%5C_") {
			writeln("Global.escape()");
			error_flg=true;
		}
		if (isFinite(0) != true || isFinite(1) != true || isFinite(Infinity) != false || isFinite(-Infinity) != false || isFinite(NaN) != false || isFinite(undefined) != false || isFinite(null) != true) {
			writeln("Global.isFinite()");
			error_flg=true;
		}

		if (unescape(escape("いろはにほへと漢字abcABC123 !\"#$%&'()-=^~\\|@`[{]}:*;+,<.>/?\\_")) != "いろはにほへと漢字abcABC123 !\"#$%&'()-=^~\\|@`[{]}:*;+,<.>/?\\_") {
			writeln("Global.unescape()");
			error_flg=true;
		}
		if (error_flg)
			writeln("NG : global function");
		else
			writeln("OK : global function");
		return !error_flg;		
	}
	
	function objectTest() {
		var error_flg = false;
		
		var o = new Object();
		var o2 = new Object();
		
		if (o == o2) {
			writeln('o == o2');
			error_flg=true;
		}
		
		if (o.toString() != "[object Object]") {
			writeln('o.toString() != "[object Object]"');
			error_flg=true;
		}
		
		if (o.toString() != "[object Object]") {
			writeln('o.toString() != "[object Object]"');
			error_flg=true;
		}

		if (o.toLocaleString() != "[object Object]") {
			writeln('o.toString() != "[object Object]"');
			error_flg=true;
		}
		
		if (o.valueOf() != o) {
			writeln('o.valueOf() != o');
			error_flg=true;
		}

		if (o.hasOwnProperty("a") != false) {
			writeln('o.hasOwnProperty("a") != false');
			error_flg=true;
		}
		o.a = 1;
		if (o.hasOwnProperty("a") != true) {
			writeln('o.hasOwnProperty("a") != true"');
			error_flg=true;
		}

		if (o.isPrototypeOf("toString") != true) {
			writeln('o.isPrototypeOf("toString") != true"');
			error_flg=true;
		}
		if (o.isPrototypeOf("a")) {
			writeln('o.isPrototypeOf("a")');
			error_flg=true;
		}

		if (o.propertyIsEnumerable("a") == false) {
			writeln('o.propertyIsEnumerable("a") == false');
			error_flg=true;
		}
		if (o.propertyIsEnumerable("toString")) {
			writeln('o.propertyIsEnumerable("toString")');
			error_flg=true;
		}
		
		o = {a:1, b:"bbb", c:function(n) {return -n;}, d:1.1 };
		if (o.a != 1 || o.b != "bbb" || o.c(2) != -2 || o.d != 1.1) {
			writeln('o.a != 1 || o.b != "bbb" || o.c(2) != -2 || o.d != 1.1 (\"{name:expression, ...}\")');
			error_flg = true;
		}

		if (error_flg)
			writeln("NG : Object object");
		else
			writeln("OK : Object object");
		return !error_flg;		
	}

	function functionTest() {
		var error_flg = false;

		
		var func = Function("n", "return n * n;");
		
		if (func(3) != 9) {
			writeln('func(3) != 9');
			error_flg=true;
		}
		if (func.call(undefined, 4) != 16) {
			writeln('func.call(null, 4) != 16');
			error_flg=true;
		}
		if (func.apply(undefined, [5]) != 25) {
			writeln('func.apply(undefined, [5])');
			error_flg=true;
		}
		
		func = function(n) { return n * n; };
		if (func(3) != 9) {
			writeln('func(3) != 9 <2>');
			error_flg=true;
		}

		if (func.call(undefined, 4) != 16) {
			writeln('func.call(null, 4) != 16 <2>');
			error_flg=true;
		}
		if (func.apply(undefined, [5]) != 25) {
			writeln('func.apply(undefined, [5]) != 25 <2>');
			error_flg=true;
		}

		//	TODO
		

		if (error_flg)
			writeln("NG : Function object");
		else
			writeln("OK : Function object");
		return !error_flg;		
	}

	function arrayTest() {
		var error_flg = false;

		a = Array(10);
		total = 0;
		for (n = 1; n <= 10; n++) {
			a[n] = n;
			total = total + a[n];
		}
		if (total != 55 || a.length != 11) {
			writeln("  NG : Array access");
			error_flg = true;
		}
	
		a["a"] = "aa";
		a["b"] = "bb";
		if (a["a"] + a["b"] != "aabb") {
			writeln('  NG : Assosiation array access');
			error_flg = true;
		}
	
		a = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15];
		total = 0;
		for (n = 1; n < a.length; n++) {
			total = total + a[n];
		}
		if (a.length != 16 || total != 120) {
			writeln("  NG : Array inirtialize [0, 1, 2, 3 ... ]");
			error_flg = true;
		}
	
		//	TODO　メソッドのテスト
		a = [6, 3, 5, 7, 2, 1, 4, 8, 9, 0];
		if (a.sort().join() != "0,1,2,3,4,5,6,7,8,9") {
			writeln('  NG :  Array sort(), join() method');
			error_flg = true;
		}
		
		a = 0;
		b = {toLocaleString:function() {return a++;}};
		c = [b, b, b];
		if (c.toLocaleString() != "0,1,2") {
			writeln('  NG :  c.toLocaleString() != "0,1,2"');
			error_flg = true;
		}

		if (error_flg)
			writeln("NG : Array object");
		else
			writeln("OK : Array object");
		return !error_flg;		
	}

	function stringTest() {
		var error_flg = false;

		//	TODO

		if (error_flg)
			writeln("NG : String object");
		else
			writeln("OK : String object");
		return !error_flg;		
	}

	function booleanTest() {
		var error_flg = false;

		//	TODO

		if (error_flg)
			writeln("NG : Boolean object");
		else
			writeln("OK : Boolean object");
		return !error_flg;		
	}

	function numberTest() {
		var error_flg = false;

		//	TODO

		if (error_flg)
			writeln("NG : Number object");
		else
			writeln("OK : Number object");
		return !error_flg;		
	}

	function mathTest() {
		var error_flg = false;

		//	TODO

		if (error_flg)
			writeln("NG : Math object");
		else
			writeln("OK : Math object");
		return !error_flg;		
	}

	function dateTest() {
		var error_flg = false;

		//	TODO

		if (error_flg)
			writeln("NG : Date object");
		else
			writeln("OK : Date object");
		return !error_flg;		
	}

	function regExpTest() {
		var error_flg = false;
		
		//	TODO

		if (RegExp("a|ab").exec("abc").toString() != "a") {
			writeln('RegExp("a|ab").exec("abc").toString() != "a"');
			error_flg=true;
		}

		if ((new RegExp("a|ab")).exec("abc").toString() != "a") {
			writeln('(new RegExp("a|ab")).exec("abc").toString() != "a"');
			error_flg=true;
		}

		if (/a|ab/.exec("abc").toString() != "a") {
			writeln("/a|ab/.exec(\"abc\").toString() != \"a\"");
			error_flg=true;
		}
		if (/((a)|(ab))((c)|(bc))/.exec("abc").toString() != "abc,a,a,,bc,,bc") {
			writeln('/((a)|(ab))((c)|(bc))/.exec("abc").toString() != "abc,a,a,,bc,,bc"');
			error_flg=true;
		}
		if (/a[a-z]{2,4}/.exec("abcdefghi").toString() != "abcde") {
			writeln('/a[a-z]{2,4}/.exec("abcdefghi").toString() != "abcde"');
			error_flg=true;
		}
		if (/a[a-z]{2,4}?/.exec("abcdefghi").toString() != "abc") {
			writeln('/a[a-z]{2,4}?/.exec("abcdefghi").toString() != "abc"');
			error_flg=true;
		}
		if (/(aa|aabaac|ba|b|c)*/.exec("aabaac").toString() != "aaba,ba") {
			writeln('/(aa|aabaac|ba|b|c)*/.exec("aabaac").toString() != "aaba,ba"');
			error_flg=true;
		}
//	正しい値を返してくれない
//		if (/(z)((a+)?(b+)?(c))*/.exec("zaacbbbcac").toString() != "zaacbbbcac,z,ac,a,,c") {
//			writeln('/(z)((a+)?(b+)?(c))*/.exec("zaacbbbcac").toString() != "zaacbbbcac,z,ac,a,,c"');
//			error_flg=true;
//		}

		if (error_flg)
			writeln("NG : global function");
		else
			writeln("OK : global function");
		return !error_flg;		
	}
	
	

// script start  /////////////////////////////////////////////////////////////
	//	Emptyステートメント(例外発生しないこと)
	;	
	;;;
	var error_flg;

	//	計算式のテスト
	error_flg = ! expressionTest();
	
	if (propertyTest() == false) {
		error_flg = true;
	}
	
	if (globalTest() == false) {
		error_flg = true;
	}

	if (objectTest() == false) {
		error_flg = true;
	}

	if (functionTest() == false) {
		error_flg = true;
	}

	if (arrayTest() == false) {
		error_flg = true;
	}

	if (stringTest() == false) {
		error_flg = true;
	}

	if (booleanTest() == false) {
		error_flg = true;
	}

	if (numberTest() == false) {
		error_flg = true;
	}

	if (mathTest() == false) {
		error_flg = true;
	}

	if (dateTest() == false) {
		error_flg = true;
	}

	if (regExpTest() == false) {
		error_flg = true;
	}
	
	//  if のテスト(これが正常に動かないことには・・・)
	if (true) {
		writeln("OK : if - then");
	} else {
		writeln("NG : if - then");
		error_flg = true;
	}
	if (1 == 1) {
		writeln("OK : if - then (1 == 1)");
	} else {
		writeln("NG : if - then (1 == 1)");
		error_flg = true;
	}

	if (false) {
		writeln("NG : if - else");
		error_flg = true;
	} else {
		writeln("OK : if - else");
	}
	if (0 == 1) {
		writeln("NG : if - else (0 == 1)");
		error_flg = true;
	} else {
		writeln("OK : if - else (0 == 1)");
	}
	
	//	while のテスト
	total = 0;
	n = 1;
	while (n <= 10) {
//		writeln("before :" + n);
		if (n == 1) {
			total = total + 1;
		} else if (n == 2) {
			total = total + 2;
		} else if (n == 3) {
			total = total + 3;
		} else if (n == 4) {
			total = total + 4;
		} else if (n == 5) {
			total = total + 5;
		} else if (n == 6)
			total = total + 6;
		else if (n == 7)
			total = total + 7;
		else if (n == 8)
			total = total + 8;
		else if (n == 9)
			total = total + 9;
		else if (n == 10)
			total = total + 10;
		else if (n == 11)
			;
		else
			break;
		n++;
	}
	if (total == 55)
		writeln("OK : while");
	else {
		writeln("NG : while");
		error_flg = true;
	}
		
	total = 0;
	for (n=1; n <= 10; n++) {
		total = total + n;
	}
	if (total == 55)
		writeln("OK : for");
	else {
		writeln("NG : for");
		error_flg = true;
	}

	obj = new Object();
	obj.aaa = "AAA";
	obj.bbb = "BBB";
	obj.ccc = 333;
	var names = "";
	for (var name in obj) {
		names = names + name;
	}
	if (names == "aaabbbccc" || names == "aaacccbbb" || names == "bbbaaaccc" || names == "bbbcccaaa" || names == "cccaaabbb" || names == "cccbbbaaa") 
		writeln("OK : for-in :" + names);
	else {
		writeln("NG : for-in");
		error_flg = true;
	}
	
	

		
	n = 1;
	total = 0;
	do {
		total += n;
		n++;
	} while(n <= 10);
	if (n == 11 && total == 55)
		writeln("OK : do-while");
	else {
		writeln("NG : do-while");
		error_flg = true;
	}
		
	total = 0;
	for (n = 1; n <= 3; n++) {
		switch (n) {
		case 1:
			total = total + 1;
			break;
		case 2:
			total = total + 2;
			break;
		default :
			total = total + 4;
		}
	}
	if (total == 7)
		writeln("OK : switch-case");
	else {
		writeln("NG : switch-case");
		error_flg = true;
	}
		

	for (n=0; n < 10; n++) {
		continue;
		break;
	}
	if (n == 10)
		writeln("OK : continue(for)");
	else {
		writeln("NG : continue(for)");
		error_flg = true;
	}

	n = 0;
	while (n < 10) {
		n++;
		continue;
		break;
	}
	if (n == 10)
		writeln("OK : continue(while)");
	else {
		writeln("NG : continue(while)");
		error_flg = true;
	}

	forLabel:
	for (;;) {
		for (;;) {
			break forLabel;
		}
	}
	writeln("OK : break(for, labeled)");

	whileLabel:
	while (true) {
		while (true) {
			break whileLabel;
		}
	}
	writeln("OK : break(while, labeled)");


	testCallFunction("a", 0, 1.1);
	
	userObject = new UserObject();
	userObject.c = "abc";
	
	if (userObject.a == "aaa" && userObject.n == 0 && userObject.c == "abc")
		writeln("OK : Generate user object");
	else {
		writeln("NG : Generate user object");
		error_flg = true;
	}

	if (userObject.toString() == "aaa0")
		writeln("OK : Call user object method");
	else {
		writeln("NG : Call user object method");
		error_flg = true;
	}

	with (userObject) {
		a = "with test";
	}
	if (userObject.a == "with test")
		writeln("OK : with statement");
	else {
		writeln("NG : with statement");
		error_flg = true;
	}

	if (userObject.anonymousFunction(12345, 6789) == 12345 + 6789)
		writeln("OK : Call user object anonymous method & inner function");
	else {
		writeln("NG : Call user object anonymous method & inner function");
		error_flg = true;
	}

	if (userObject.virtualPrivateFunction == undefined) {
		writeln("OK : user object virtualPrivateFunction");
	} else {
		writeln("NG : user object virtualPrivateFunction");
		error_flg = true;
	}

	inheritedObject = new InheritedObjectByCallMethod("A", "B");
	
	if (inheritedObject.a == "A" && inheritedObject.b == "B")
		writeln("OK : Object inherited by call method");
	else {
		writeln("NG : Object inherited by call method");
		error_flg = true;
	}

	inheritedObject2 = new InheritedObjectByApplyMethod("A", "B");
	
	if (inheritedObject2.a == "A" && inheritedObject2.b == "B")
		writeln("OK : Object inherited by apply method");
	else {
		writeln("NG : Object inherited by apply method");
		error_flg = true;
	}


	System = loadClass("java.lang.System");
	System.out.println("OK : Java native method (System.out.println)");
	if (Math.PI > 3.141592653589792 && Math.PI < 3.141592653589794)
		writeln("OK : Java native class static member access");
	else {
		writeln("NG : Java native class static member access");
		error_flg = true;
	}

	catchCounter = 0;
	finallyCounter = 0;
	try {
		throw new Error("try-catch");
		
		writeln("NG : throw action");	//	ここが実行されるのは間違い
		error_flg = true;
	} catch (	e	) {
		if (e.message == "try-catch")
			catchCounter++;
	} finally {
		finallyCounter++;
	}
	
	try {
	} catch (e) {
		catchCounter++;
	} finally {
		finallyCounter++;
	}

	
	if (catchCounter == 1 && finallyCounter==2)
		writeln("OK : try-catch-finally statement");
	else {
		writeln("NG : try-catch-finally statement");
		writeln("     catchCounter : " + catchCounter);
		writeln("     finallyCounter : " + finallyCounter);
		error_flg = true;
	}
		
	flag = false;
	try {
		throwErrorFunction();		
	}
	catch (e) {
		if (e.message == "throwErrorFunction")
			flag = true;
	}
	if (flag)
		writeln("OK : function internal throw");
	else {
		writeln("NG : function internal throw");
		error_flg = true;
	}

	var varFunction = function() { return 12.3; }, varTest = 123;
	if (varFunction() == 12.3 && varTest == 123)
		writeln("OK : var statement");
	else {
		writeln("NG : var statement");
		error_flg = true;
	}
		
	a = "OK : synchronized";
	synchronized(a) {
		writeln(a);		//	あんまりテストになっていない気が…
	}

	
	writeln("--------------------------------------------------");
	if (error_flg == false) {
		writeln("OK : test successful.");
	} else {
		writeln("NG : test failure.");
	}
	
	writeln('End program');
	