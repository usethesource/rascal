module cobra::tests::monadicgenerators

import cobra::monadicgenerators;
import Prelude;

test bool testIsZero(){
	return (iszero(0) == true && iszero(1) == false); 
}

test bool testIsZeroShouldRaiseExceptionWithBelowZero(){
	try 
		iszero(-1);
	catch IllegalArgument(value a, str s):
		return a == -1 && s == "Depth below zero";
	finally;
		return false;
}

test bool testUnit(){
	int unused = 999;
	return (
		unit(true)(unused) == true &&
		unit(1)(unused) == 1 &&
		unit("test")(unused) == "test" &&
		unit([])(unused) == []
	);
}

test bool testLift0(){
	int () myConst = int (){ return 13;};
	int unused = 999;
	return lift( myConst )()(unused) == 13;
}

test bool testLift1(){
	int unused = 999;
	return lift( int (str a) { return toInt(a);})( unit("2"))(unused) == 2;
}

test bool testLift2(){
	int (int, int) myAdd = int (int a, int b){ return a+b;};
	int unused = 999;
	return lift( myAdd )(unit(2), unit(3))(unused) == 5;
}

test bool testLift3(){
	int (int, int, int) myAdd = int (int a, int b, int c){ return a+b+c;};
	int unused = 999;
	return lift( myAdd )(unit(2), unit(3), unit(4))(unused) == 9;
}

test bool testLift4(){
	int (int, int, int, int) myAdd = int (int a, int b, int c, int d){ return a+b+c+d;};
	int unused = 999;
	return lift( myAdd )(unit(2), unit(3), unit(4), unit(5))(unused) == 14;
}

test bool testLift5(){
	str (str, str, str, str, str) myAdd = str (str a, str b, str c, str d, str e){ return a+b+c+d+e;};
	int unused = 999;
	return lift( myAdd )(unit("t"), unit("e"), unit("s"), unit("t"), unit("!"))(unused) == "test!";
}

test bool testLiftedFunctionDoesNotChangeDepth(){
	Gen[int] genDepth = int (int d){ return d;};
	Gen[int] (Gen[int]) lifted = lift( int (int a){ return a;} );
	return lifted(genDepth)(12) == 12;	
}

test bool testNumber(){
	int unused = 999;
	return number(1)(unused) == 0;
}

test bool testChoose(){
	int unused = 999;
	return choose(0,1)(unused) == 0;
}

test bool testBind(){
	Gen[int] (bool) func = Gen[int] (bool a) {
		if(a){
			return unit(1);
		} else {
			return unit(2);
		}
	};
	
	return (bind(iszero, func )(0) == 1 && bind(iszero, func)(1) == 2);
	
}


test bool testBind(){
	
	Gen[int] (bool) func = Gen[int] (bool a) {
		if(a){
			return unit(1);
		} else {
			return unit(2);
		}
	}; 
	
	int notUsed = 999;
	
	return (bind(unit(true), func )(notUsed) == 1 &&
		 	bind(unit(false), func )(notUsed) == 2);
	
}

test bool shouldGenZeroElements(){
	return (genList( unit(0), unit(1))(2) == []);
}

test bool shouldGenTwoElements(){
	return (size(genList( unit(2), unit(1))(2)) == 2);
}

test bool chooseShouldExcludeLimit(){
	return choose(1, 2)(3) == 1;
}



