print("start");

function sosuu(nMax) {
	count = 0;

	a = 3;
	while (a < nMax) {
		b = 3;
		flag = false;
		while (b * b <= a) {
			if (a % b == 0) {
				flag = true;
				break;
			}
			b += 2;
		}
		if (flag == false)
			count++;
		a += 2;
	}

	return count;
}

startTime = new Date();
total = 0;

total = sosuu(500000);

print("total = " + total);
print("time : " + ((new Date()) - startTime) / 1000.0 + "s");
