/*
 * �N�C�b�N�\�[�g�A���S���Y���̎���
 * �}���`�X���b�h�Ή��łł��B
 * for Ignis
 */
count = 0;

function qsort_(array, minIdx, maxIdx) {
    var minSearch;
    var maxSearch;
    var swap;
    var base;
    var baseIdx;
    var baseIdx1;
    var baseIdx2;
    var baseIdx3;
    if (minIdx + 1 == maxIdx) {
        if (array[minIdx] > array[maxIdx]) {
            //  �l�̌���
            swap = array[minIdx];
            array[minIdx] = array[maxIdx];
            array[maxIdx] = swap;
        }
        return 0;
    }

    //  ��̃C���f�b�N�X����i�R�̃��f�B�A���j
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
        return 0;	//	�v�f���R�̂Ƃ����̎��_�Ŋ��ɕ���ł��邱�ƂɂȂ�̂Ŗ߂�
    baseIdx = baseIdx2;
	minSearch = minIdx + 1;
	maxSearch = maxIdx - 1;
    
	base = array[baseIdx];    //  ��l
	while (minSearch <= maxSearch) {
        //  �z��̑O�������l���傫�Ȓl��T��
		count++;
		while (array[minSearch] < base) {
			minSearch = minSearch + 1;
    		count++;
		}
        //  �z��̌�둤�����l��菬���Ȓl��T��
		count++;
		while (array[maxSearch] > base) {
			maxSearch = maxSearch - 1;
    		count++;
		}
        //  �K�v�Ȃ�l����������
		if (minSearch <= maxSearch) {
            if (minSearch < maxSearch) {
				//	�l�̌���
				swap = array[minSearch];
				array[minSearch] = array[maxSearch];
				array[maxSearch] = swap;
            }
			
			minSearch = minSearch + 1; 
			maxSearch = maxSearch - 1;
		}
	}
	if (minIdx < maxSearch)
        qsort_(array, minIdx, maxSearch);  // �O�����\�[�g
    if (minSearch < maxIdx)
        qsort_(array, minSearch, maxIdx);  // �㔼���\�[�g
}

function qsort(array) {
	if (array.length > 1)
		qsort_(array, 0, array.length - 1);
}

function main() {
	var a = new Array(100000);
	var max = a.length;
	var i;
	var j;
	for (j = 0; j < 3; j++) {
		for (i = 0; i < max; i++) {
			a[i] = Math.random();
		//	a[i] = 0;
		}
		count = 0;
		
		//	�\�[�g
		var startTime = new Date();
		
		//a.sort();
		qsort(a);
		
		var endTime = new Date();
		
		//	�G���[�`�F�b�N
		var errorFlag = false;
		for (i = 0; i < max - 1; i++) {
			if (a[i] > a[i + 1]) {
	//			writeln("error : " + i);
	//			writeln("  a[i]     : " + a[i]);
	//			writeln("  a[i + 1] : " + a[i + 1]);
				errorFlag = true;
			}
		}
		if (errorFlag == false)
			writeln("sort successful (time : " + (endTime - startTime) / 1000.0 + "s, count : "+ count +")");
		else
			writeln("sort failure.");
	}
}

main();


