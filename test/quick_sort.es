/*
 * クイックソートアルゴリズムの実装
 * マルチスレッド非対応です。
 * for Ignis & rhino
 */

//  print = writeln;    //  この行のコメントを解除するとFESIでも動作します。 
count = 0;


//	クイックソートアルゴリズム
function qsort_(array, minIdx, maxIdx) {
    var minSearch, maxSearch;
    var base;
    var baseIdx, baseIdx1, baseIdx2, baseIdx3;
    var swap;
    if (minIdx + 1 == maxIdx) {
        if (array[minIdx] > array[maxIdx]) {
            //  値の交換
            swap = array[minIdx];
            array[minIdx] = array[maxIdx];
            array[maxIdx] = swap;
        }
        return 0;
    }

    //  基準のインデックス判定（３つのメディアン） 
    baseIdx1 = minIdx;
    baseIdx2 = minIdx + Math.floor((maxIdx - minIdx) / 2);
    baseIdx3 = maxIdx;
	count++;
    if (array[baseIdx1] > array[baseIdx2]) {
        swap = array[baseIdx1];
        array[baseIdx1] = array[baseIdx2];
        array[baseIdx2] = swap;
    }
	count++;
    if (array[baseIdx2] > array[baseIdx3]) {
        swap = array[baseIdx2];
        array[baseIdx2] = array[baseIdx3];
        array[baseIdx3] = swap;
		count++;
        if (array[baseIdx1] > array[baseIdx2]) {
            swap = array[baseIdx1];
            array[baseIdx1] = array[baseIdx2];
            array[baseIdx2] = swap;
        }
    }
    if (maxIdx - minIdx == 2)
        return 0;	//	要素が３個のときこの時点で既に並んでいることになるので戻る 
    baseIdx = baseIdx2;
	minSearch = minIdx + 1;
	maxSearch = maxIdx - 1;

	base = array[baseIdx];    //  基準値 
	while (minSearch <= maxSearch) {
        //  配列の前側から基準値より大きな値を探す 
		count++;
		while (array[minSearch] < base) {
			minSearch++;
    		count++;
		}
        //  配列の後ろ側から基準値より小さな値を探す 
		count++;
		while (array[maxSearch] > base) {
			maxSearch--;
    		count++;
		}
        //  必要なら値を交換する 
		if (minSearch <= maxSearch) {
            if (minSearch < maxSearch) {
				//	値の交換
				swap = array[minSearch];
				array[minSearch] = array[maxSearch];
				array[maxSearch] = swap;
            }
			
			minSearch++; 
			maxSearch--;
		}
	}
	if (minIdx < maxSearch)
        qsort_(array, minIdx, maxSearch);  // 前半をソート 
    if (minSearch < maxIdx)
        qsort_(array, minSearch, maxIdx);  // 後半をソート 
}

function qsort() {
	if (this.length > 1)
		qsort_(this, 0, this.length - 1);
}


//	ソートする配列の初期化 
a = new Array(1000000);
max = a.length;
for (i = 0; i < max; i++) {
	a[i] = Math.random();
//	a[i] = 0;
}

//	Array型のsort()メソッドを上書きする
a.sort = qsort;


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
