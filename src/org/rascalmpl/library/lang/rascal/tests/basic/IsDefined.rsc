module lang::rascal::tests::basic::IsDefined

import Exception;
import util::Math;
import List;

test bool isDefined0(list[int] L) = L == [] || L[arbInt(size(L))]?;

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

test bool isAnnoDefined() = (f3()[@pos=1])@pos?;

test bool isDefined3(){
    X = f3(); 
    X @ pos ?= 3;
    if(X @ pos != 3) return false;
    return true;
}

test bool isDefined4() = "aap"(noot=1).noot?;
test bool isDefined5() = !("aap"(boot=1).noot?);

//test bool isDefined6() = "aap"(noot=1) has noot;
//test bool isDefined7() = !("aap"(boot=1) has noot);


// e has f : e is of an ADT type and its constructor has a positional or keyword field f.
// e[k]?   : list or map contains given index k
// e.f?    :  e is a node or constructor that has a keyword field f with an explicitly set value
// e?      : debatbale whther we allow the general case

data F = z(int l = 2) | u();

test bool isDefined8() = !(u() has l);
test bool isDefined9() = !(u().l?);
test bool isDefined10() = !(z().l?);
test bool isDefined11() = z() has l;
test bool isDefined12() = z(l=1).l?;

test bool isDefined13() {
  e = z();
  e.l?=3; // set l to 3 if the field is not set, otherwise leave it
  return e.l == 3;
}

data D = d1() | d2(int n) | d3(int n, str s = "abc");

@expected{NoSuchField}
test bool getField1() = d1().n == 0;

@expected{NoSuchField}
test bool getField2() = d1().s == "abc";

test bool getField3() = d2(10).n == 10;

@expected{NoSuchField}
test bool getField4() = d2(10).s == "abc";

test bool getField5() = d3(20).n == 20;
test bool getField6() = d3(20).s == "abc";
test bool getField7() = d3(20, s = "def").n == 20;
test bool getField8() = d3(20, s = "def").s == "def";
test bool getField9() = d3(20, s = "abc").n == 20;
test bool getField10() = d3(20, s = "abc").s == "abc";

test bool has1() = !(d1() has n);
test bool has2() = !(d1() has s);
test bool has3() = d2(10) has n;
test bool has4() = !(d2(10) has s);
test bool has5() = d3(20) has n;
test bool has6() = d3(20) has s;
test bool has7() = d3(20, s="def") has n;
test bool has8() = d3(20, s="def") has s;
test bool has9() = d3(20, s="abc") has n;
test bool has10() = d3(20, s="abc") has s;

test bool isDef1() = !d1().n?;
test bool isDef2() = !d1().s?;
test bool isDef3() = d2(10).n?;
test bool isDef4() = !d2(10).s?;
test bool isDef5() = d3(20).n?;
test bool isDef6() = !d3(20).s?;
test bool isDef7() = d3(20, s = "def").n?;
test bool isDef8() = d3(20, s = "def").s?;
test bool isDef9() = d3(20, s = "abc").n?;
test bool isDef10() = d3(20, s = "abc").s?;

test bool ifDefOtherwise1() = 13 == (d1().n ? 13);
test bool ifDefOtherwise2() = "xyz" == (d1().s ? "xyz");
test bool ifDefOtherwise3() = 10 == (d2(10).n ? 13);
test bool ifDefOtherwise4() = "xyz" == (d2(10).s ? "xyz");
test bool ifDefOtherwise5() = 20 == (d3(20).n ? 13);
test bool ifDefOtherwise6() = "xyz" == (d3(20).s ? "xyz");
test bool ifDefOtherwise7() = 20 == (d3(20, s = "def").n ? 13);
test bool ifDefOtherwise8() = "def" == (d3(20, s = "def").s ? "xyz");
test bool ifDefOtherwise9() = 20 == (d3(20, s = "abc").n ? 13);
test bool ifDefOtherwise10() = "abc" == (d3(20, s = "abc").s ? "xyz");

// Potential generic rules to check:
// e has f => e.f is well defined
// !(e has f) => e.f. gives error
// e.f? => e has f
// !(e.f?) && !(e has f) => e.f gives error
// !(e.f?) && (e has field) => e.f. gives default value