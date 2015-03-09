module lang::rascal::tests::functionality::KeywordParameterTests

str f(int i, str k = "empty", int j = 0) {
    k = k + "; bbb";
    j = j - 1;
    return "<i>, <j>, <k>";
}

test bool keywordParam10() = f(0) == "0, -1, empty; bbb";
test bool keywordParam11() = f(0, k = "aaa") == "0, -1, aaa; bbb";
test bool keywordParam12() = f(0, j = 100) == "0, 99, empty; bbb";
test bool keywordParam13() = f(0, j = 100, k = "aaa") == "0, 99, aaa; bbb";

test bool keywordParam2(){ 
  	int incr(int x, int delta = 1) = x + delta;
  	return incr(3) == 4 && incr(3, delta = 2) == 5;
}

test bool keywordParam3(){ 
	int sum(int x = 0, int y = 0) = x + y;
  	return sum() == 0 
  	       && sum(x = 5, y = 7) == 5 + 7 
  	       && sum(y = 7, x = 5) == 5 + 7;
}

int f4(int i, int delta = 0) = g4();

int g4() = h4();

int h4(int delta = 1) {
    return delta; 
}

test bool keywordParam4(){ 
    return f4(0,delta = 5) == 1;
}

data Point = point(int i, str color = "red");

public tuple[Point,Point] f5(int i, str color = "green", bool print = false) = <point(i),point(i + 1,color = "blue")>;

test bool keywordParam5() {
    return <f5(0,print = true), f5(1,color = "grey")> == 
     <<point(0),point(1,color="blue")>,<point(1),point(2,color="blue")>>;
}

data Figure (str fillColor = "white")  =  emptyFigure();

test bool keywordParam6() = emptyFigure().fillColor == "white";

str f7(int i, int j, str k = "<i>, <j>") = k;

test bool keywordParam71() = f7(1,2) == "1, 2";
test bool keywordParam72() = f7(3,4) == "3, 4";
test bool keywordParam73() = f7(1,3, k = "1 + 3") == "1 + 3";

int f8(int i, int delta = 100 + i) = g8();

int g8() = h8(665);

int h8(int step, int delta = 1 + step) {
    return delta; 
}
test bool keywordParam81() = f8(0,delta = 999) == 666;
test bool keywordParam82() = f8(100) == 666;

data F9 = f9(int i, int delta = 100);

test bool keywordParam9() {
    return f9(0).delta == 100;
}

data F10 = f10(int i, int delta = 100);

test bool keywordParam10() {
    return f10(0,delta=1).delta == 1;
}

test bool keywordParam11(){
	bool f(bool c = false){
		bool g(){
			return c;
		}
		return g();
	}
	return f() == false;
}


