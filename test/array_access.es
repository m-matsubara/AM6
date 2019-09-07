a = new Array(100000);
max = a.length;
var test = true;
var rand;

startTime = new Date();
for (var idx2 = 0; idx2 < 2; idx2++) {
    for (var idx = 0; idx < max; idx++) {
    	rand = Math.random() * max;
    	rand = rand - rand % 1;
        a[rand] = idx;
        test = (a[rand] + 2) * 2;
        test = a[rand] * idx;
        test = a[rand] < 1;
        test = a[rand] * 3 + 2;
        test = a[rand] + test;

        test = a[rand] + test;
        test = a[rand] - test;
        test = a[rand] * test;
        test = a[rand] / test;
        test = a[rand] - a[idx];
    }
}

print("time : " + ((new Date()) - startTime) / 1000.0 + "s");
