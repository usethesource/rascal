module openrecursion::E

import openrecursion::A;
import IO;


// 0 1 2 5 16 65 326 1957 13700 109601
public int fibW(0) = 0;
public int fibW(1) = 1;
public default int fibW(int n) = fibW(n-1) + factW(n-1);

// 1 1 3 11 49 261 1631 11743 95901 876809
public int factW(0) = 1;
public int factW(1) = 1;
public default int factW(int n) = n*factW(n-1) + fibW(n-1);


public test bool test1() {
	str output1 = "1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;65;1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;261;326;1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;261;1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;65;1631;1957;";
	str output2 = "1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;261;1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;65;1631;1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;65;1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;261;326;11743;";
	str result = "";
	int (int) printResult = int (int n) { result = result + "<n>;"; return n; };
	int (int) left = (<printResult, printResult> o <fibW, factW>)[0];
	int (int) right = (<printResult, printResult> o <fibW, factW>)[1];
	left(7); right(7);
	return result == (output1 + output2);
}

public test bool test2() {
	str output1 = "1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;65;1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;261;326;1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;261;1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;65;1631;1957;";
	str output2 = "1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;261;1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;65;1631;1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;65;1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;261;326;11743;";
	str result = "";
	int (int) printResult = int (int n) { result = result + "<n>;"; return n; };
	int (int) left = (printResult o <fibW, factW>)[0];
	int (int) right = (printResult o <fibW, factW>)[1];
	left(7); right(7);
	return result == (output1 + output2);
}

public test bool test3() {
	str output1 = "1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;65;1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;261;326;1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;261;1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;65;1631;1957;";
	str output2 = "1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;261;1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;65;1631;1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;65;1;1;3;1;1;2;11;1;1;2;1;1;3;5;49;1;1;2;1;1;3;5;1;1;3;1;1;2;11;16;261;326;11743;";
	str result = "";
	int (int) (int (int)) printMixin = 
		int (int) (int (int) f) { 
			return int (int n) { int res = f(n); result = result + "<res>;"; return res; }; };
	int (int) left = (printMixin o <fibW, factW>)[0];
	int (int) right = (printMixin o <fibW, factW>)[1];
	left(7); right(7);
	return result == (output1 + output2);
}

public str state0 = "";

// 1 2 4 7 12 20 33 54 143 
public int fibExt0(0) { state0 = state0 + "1;"; return 1; }
public int fibExt0(1) { state0 = state0 + "2;"; return 2; }
public default int fibExt0(int n) { int res = fibExt0(n-1) + fibExt0(n-2) + 1; state0 = state0 + "<res>;"; return res; }

public str state = "";

public int fibExt1(0) { state = state + "0;"; return 0; }
public int fibExt1(1) { state = state + "1;"; return 1; }
public default int fibExt1(int n) { int res = (n-1)*fibExt1(n-1) + (n-2)*fibExt1(n-2); state = state + "<res>;"; return res; }

public int (int) fExt0(int (int) f) = int (int n) { return f(n) + 1; };

public int (int) fExt1(int (int) f) = int (int n) { 
	switch(n) {
		case 0: return 0;
		case 1: return 1;
		case int _: return f(n) + (n-2)*fExt1(f)(n-1) + (n-3)*fExt1(f)(n-2); 
	} 
};

public test bool test4() {
	list[int] input = [0,1,2,3,4,5,6,7,8,9];
	list[int] output = [ fibExt1(n) | int n <- input ];
	list[int] result = [ (fExt1 o fib)(n) | int n <- input ];
	return output == result;
}

public test bool test5() {
	str output = "2;1;4;2;7;2;1;4;12;2;1;4;2;7;20;2;1;4;2;7;2;1;4;12;33;2;1;4;2;7;2;1;4;12;2;1;4;2;7;20;54;";
	str result = "";
	int (int) (int (int)) printMixin = 
		int (int) (int (int) f) { 
			return int (int n) { int res = f(n); result = result + "<res>;"; return res; }; };
	int res1 = ((printMixin o fExt0) o fib)(7);
	str result1 = result;
	result = "";
	int res2 = (printMixin o (fExt0 o fib))(7);
	str result2 = result;
	return (res1 == res2) && (res1 == 54) && (result1 == output) && (result2 == result1);
}

public test bool test6() {
	str result = "";
	int (int) (int (int)) printMixin = 
		int (int) (int (int) f) { 
			return int (int n) { int res = f(n); result = result + "<res>;"; return res; }; };
	int res1 = ((printMixin o. fExt1) o fib)(7);
	str result1 = result;
	result = "";
	int res2 = (printMixin o (fExt1 o fib))(7);
	str result2 = result;
	return (res1 == res2) && (result2 == result1);
}

