function foo(a, b) {
    return a + b;
}


startTime = new Date();
total = 0;
for (var idx = 0; idx < 2000000; idx++) {
	total += foo(idx, idx);
}

print("total = " + total);
print("time : " + ((new Date()) - startTime) / 1000.0 + "s");
