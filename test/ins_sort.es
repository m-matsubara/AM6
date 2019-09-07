/*
 * クイックソートアルゴリズムの実装
 * マルチスレッド非対応です。
 * for Ignis & rhino
 */

//  print = writeln;    //  この行のコメントを解除するとFESIでも動作します。 
count = 0;


//	インサートソートアルゴリズム
function insSort_(array, minIdx, maxIdx) {
	for (var currentIdx = minIdx; currentIdx <= maxIdx; currentIdx++) {
		var base = array[currentIdx];
		for (var idx = minIdx; base > array[idx]; idx++)
			;
		if (idx < currentIdx) {
			for (var swapIdx = currentIdx; swapIdx > idx; swapIdx--)
				array[swapIdx] = array[swapIdx - 1];
			array[idx] = base;
		}
	}
}


function insSort() {
	if (this.length > 1)
		insSort_(this, 0, this.length - 1);
}


//	ソートする配列の初期化 
a = new Array(10000);
max = a.length;
for (i = 0; i < max; i++) {
	a[i] = Math.random();
//	a[i] = 0;
}

//	Array型のsort()メソッドを上書きする
a.sort = insSort;


//	ソート 
print("array size : " + a.length);
startTime = new Date();
a.sort();

print("time : " + ((new Date()) - startTime) / 1000.0 + "s");

//	エラーチェック 
errorFlag = false;
for (i = 0; i < max - 1; i++) {
	if (a[i] > a[i + 1]) {
		print("error : " + i);
		print("  a[i]     : " + a[i]);
		print("  a[i + 1] : " + a[i + 1]);
		errorFlag = true;
	}
}
if (errorFlag == false)
	print("sort successful.");
else
	print("sort failure.");
print("compare count : " + count);
