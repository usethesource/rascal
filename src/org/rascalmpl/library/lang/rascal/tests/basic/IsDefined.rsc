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

test bool isDefined4() = "aap"(noot=1).noot?;
test bool isDefined5() = !("aap"(boot=1).noot?);

data F = z(int l = 2) | u();

// ? is not the same as has, has returns whether or not in principle (statically) the current dynamic value has the field or could have the field (in case of keyword parameter)
// while ? computes whether or not currently dynamically the field is set (which is always true in case of named parameters of the right constructor and sometimes true for keyword parameters)
// for constructors it is true that ? implies has, but not for nodes.
test bool isDefined6() = !(u() has l);
test bool isDefined7() = !(u().l?);
test bool isDefined8() = !(z().l?);
test bool isDefined9() = z() has l;
test bool isDefined10() = z(l=1).l?;
test bool isDefined11() {
  e = z();
  e.l?=3;
  return e.l == 3;
}

