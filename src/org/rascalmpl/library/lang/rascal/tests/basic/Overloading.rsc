module lang::rascal::tests::basic::Overloading

import List;
import Set;

test bool overloading1(){
	int f(0) = -1;
	default int f(int n) = n;
	
	int f("0") = -2;
	default int f(str s) = -3;
	
	int f(int n, str s) = -4;
	
	x = f(0);
	y = f(5);
	k = f("0");
	l = f("5");
	z = f(0,"1");
	return x == -1 && y == 5 && k == -2 && l == -3 && z == -4;
}

test bool overloading2(){
	default int f(int n) = n;
	default int f(str s) = -3;
	
	int f(0) = -1;
	int f("0") = -2;
	
	int f(int n, str s) = -4;

	x = f(0);
	y = f(5);
	k = f("0");
	l = f("5");
	z = f(0,"1");
	return x == -1 && y == 5 && k == -2 && l == -3 && z == -4;
}

data D = d(str s) | d(int n) | d();

test bool overloading3(){

	public D d(0) = d(-1);
	public D d("0") = d("-1");

	x = d(0);
	y = d("0");
	k = d(1);
	z = d("1");
	return <x,y,k,z> == <d(-1), d("-1"), d(1), d("1")>;
}

test bool overloadingDynamicCall(){
	int f(0) = -1;
	default int f(int i) = 100 + i;
	
	str f("0") = "- 1";
	default str f(str s) = "100 + <s>";

	x = f;
	y = x("arg");
	z = x(1);
	return <y, z> == <"100 + arg", 101>;
}

test bool overloadingMatch(){
	default D d(str s) = d();

	D d(0) = d(-1);
	D d("0") = d("-1");

	int n = 0;
	if( D::d(int v) := d(0) ) {
		n = v;
	} 
	return n == -1;
}

test bool overloadingPlusBacktracking1(){
	int f([*int x,*int y]) {
		if(size(x) == size(y)) {
			return -1000;
		}
		fail;
	}
	default int f(list[int] l) = 0;
	
	int g([1,2,3,4,5,6]) {
		return -2000;
	}
	default int g(list[int] l) = -3000;
	
	int h(list[int] _) {
		fail;
	}
	default int h(list[int] l) = -3000;
	
	return <f([1,2,3,4,5,6]), g([1,2,3,4,5,6]), g([1,2,3,4,5]), h([1,2,3,4,5,6]) > ==
		   <-1000,            -2000,            -3000,          -3000>;
}

test bool overloadingPlusBacktracking2(){
	list[int] f([*int x, *int y]) { if(size(x) == size(y)) return x; fail; }
	default list[int] f(list[int] l) = l;

    return f([1,2,3,4]) == [1, 2];
}

test bool overloadingPlusPolymorphism1(){
	int inc(int n) { return n + 1; } 

	return mapper([1, 2, 3], inc) == [2, 3, 4];
}

test bool overloadingPlusPolymorphism2(){
	bool similar(int a, int b) = a % 5 == b % 5;

	return group({1,2,3}, similar) == {{1}, {2}, {3}};
}	

test bool overloadingPlusVarArgs(){

	str f(500) = "500";
	str f(500,"0") = "5000";
	default str f(int n, str strs...) = "<n> + <strs>";

    return f(500) + "; " + f(0) + "; " + f(500,"0") + "; " + f(0,"0","0") + "; " + f(600) + "; " + f(600,"0") ==
           "500; 0 + []; 5000; 0 + [\"0\",\"0\"]; 600 + []; 600 + [\"0\"]";
}

test bool overloadingPlusVarArgsSpecialCase(){
	list[str] f(str strs...) = strs;

    return f(["0","0"]) + f("1","1") ==  ["0","0","1","1"];
}
