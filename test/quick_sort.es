/*
 * �N�C�b�N�\�[�g�A���S���Y���̎���
 * �}���`�X���b�h��Ή��ł��B
 * for Ignis & rhino
 */

//  print = writeln;    //  ���̍s�̃R�����g�����������FESI�ł����삵�܂��B 
count = 0;


//	�N�C�b�N�\�[�g�A���S���Y��
function qsort_(array, minIdx, maxIdx) {
    var minSearch, maxSearch;
    var base;
    var baseIdx, baseIdx1, baseIdx2, baseIdx3;
    var swap;
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
			minSearch++;
    		count++;
		}
        //  �z��̌�둤�����l��菬���Ȓl��T�� 
		count++;
		while (array[maxSearch] > base) {
			maxSearch--;
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
			
			minSearch++; 
			maxSearch--;
		}
	}
	if (minIdx < maxSearch)
        qsort_(array, minIdx, maxSearch);  // �O�����\�[�g 
    if (minSearch < maxIdx)
        qsort_(array, minSearch, maxIdx);  // �㔼���\�[�g 
}

function qsort() {
	if (this.length > 1)
		qsort_(this, 0, this.length - 1);
}


//	�\�[�g����z��̏����� 
a = new Array(1000000);
max = a.length;
for (i = 0; i < max; i++) {
	a[i] = Math.random();
//	a[i] = 0;
}

//	Array�^��sort()���\�b�h���㏑������
a.sort = qsort;


//	�\�[�g 
print("array size : " + a.length);
startTime = new Date();
a.sort();

print("time : " + ((new Date()) - startTime) / 1000.0 + "s");

//	�G���[�`�F�b�N 
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
