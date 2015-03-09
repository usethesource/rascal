module lang::rascal::tests::basic::IsDefined

import Exception;

test bool isDefined1() {
	str trace = "";
	map[int,str] m = (0 : "0", 1 : "1", 2 : "2");
	try {
		m[3];
	} catch NoSuchKey(k): {
		trace += "Caught no such key: <k>;";
	}
	
	if(m[2]?) {
		trace += " <m[2]>;";
	} else {
		trace += " not found the key <2>;";
	}
	
	if(m[3]?) {
		trace += " <m[3]>;";
	} else {
		trace += " did not find the key <3>;";
	}
	
	return trace == "Caught no such key: 3; 2; did not find the key 3;";
}

test bool isDefined2() {

	str trace = "";
	
	map[int,str] m = (0 : "0", 1 : "1", 2 : "2");
	
	trace += m[2] ? " Not found the key <2>;";
	trace += m[3] ? " Did not find the key <3>;";
	
	int x;
	
	x = x ? 1;
	x = x ? 2;
	
	trace += " <x>;";
	
	try {
		x = [0,1,2,3][5] ? 3;
	} catch IndexOutOfBounds(index) : {
		x = x - 100;
	}
	
	trace += " <x>";
	
	return trace == "2 Did not find the key 3; 1; 3";
}

data F = f3() | f3(int n) | g(int n) | deep(F f);
anno int F @ pos;

test bool isDefined3(){
    X = f3(); 
    X @ pos ?= 3;
    if(X @ pos != 3) return false;
    return true;
}