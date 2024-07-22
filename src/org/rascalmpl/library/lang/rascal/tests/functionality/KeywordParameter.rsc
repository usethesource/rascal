@license{
  Copyright (c) 2009-2020 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module lang::rascal::tests::functionality::KeywordParameter

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

// delta is unused on purpose for testing purposes
int f4(int _, int delta = 0) = g4();

int g4() = h4();

int h4(int delta = 1) {
    return delta; 
}

test bool keywordParam4(){ 
    return f4(0,delta = 5) == 1;
}

data Point = point(int i, str color = "red");

// color and print are unused on purpose for testing
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

// delta is unused on purpose
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

test bool keywordParam91() {
    return f10(0,delta=1).delta == 1;
}

test bool keywordParam92(){
	bool f11(bool c = false){
		bool g11(){
			return c;
		}
		return g11();
	}
	return f11() == false;
}

test bool keywordParam93(){
	bool f12(bool c = false){
		void g12(){
			c = true;
		}
		g12();
		return c;
	}
	return f12() == true;
}

test bool keywordParam101(){
    int f13(int c = 10){
        int g13(int d = 100){
            return c + d;
        }
        return g13();
    }
    return f13() == 110;
}

test bool keywordParam102(){
    int f14(int c = 10){
        int g14(int d = 100){
            return c + d;
        }
        return g14();
    }
    return f14(c=11) == 111;
}

test bool keywordParam103(){
    int f15(int c = 10){
        int g15(int d = 100){
            return c + d;
        }
        return g15();
    }
    return f15(c=11) == 111;
}

test bool keywordParam104(){
    int f16(int c = 10){
        int g16(int d = 100){
            return c + d;
        }
        return g16(d=200);
    }
    return f16(c=11) == 211;
}

test bool keywordParam105(){
    int f17(int c = 10){
        int g17(int c = 100){
            return c;
        }
        return g17(c=200);
    }
    return f17(c=11) == 200;
}

test bool keywordParam106(){
    int f18(int c = 10){
        int h18(){
            int g18(int d = 100){
                return c + d;
            }
            return g18(d=200);
        }
        return h18();
    }
    return f18(c=11) == 211;
}

test bool keywordParam107(){
    int f19(int c = 10){
        int h19(){
            int g19(int c = 1, int d = 100){
                return c + d;
            }
            return g19(d=200);
        }
        return h19();
    }
    return f19(c=11) == 201;
}

test bool keywordParam108(){
    int f20(int c = 10){
        int h20(){
            int g20(int c = 1, int d = c * 100){
                return c + d;
            }
            return g20(c=2);
        }
        return h20();
    }
    return f20(c=11) == 202;
}

data X(int y = 1) = xx(int z = 0);
data X(int yy = 2) = xx(int u);
data X(int yyy = 3);

test bool sharedKWParams1() = xx().y == 1;
test bool sharedKWParams2() = xx().yy == 2;
test bool sharedKWParams3() = xx().yyy == 3;

// has 
data F11(int y = 1) = d11(int n, real r = 1.5) | d11(str s, bool b = true) | d11(int n, str s);

test bool has1() = d11(0) has n;
test bool has2() = d11(0) has r;
test bool has3() = d11(0) has y;
test bool has4() = !(d11(0) has s);
test bool has5() = !(d11(0) has b);

test bool has6() = d11("abc") has s;
test bool has7() = d11("abc") has b;
test bool has8() = d11("abc") has y;
test bool has9() = !(d11("abc") has n);
test bool has10() = !(d11("abc") has r);

test bool has11() = d11(0, "abc") has n;
test bool has12() = d11(0, "abc") has s;
test bool has13() = d11(0, "abc") has y;
test bool has14() = !(d11(0, "abc") has r);
test bool has15() = !(d11(0, "abc") has b);

// when

int f13(int n, str s = "") = n when s == "";
int f13(int n, str s = "") = -n when s != "";

test bool when1() = f13(10) == 10;
test bool when2() = f13(10, s="a") == -10;

data E[&T] = e(&T t, int j = 0);

test bool parametrizedDataTypeWithKwParam() = e(1).j == 0 && e(1,j=2).j == 2;

// static types

data GGG = ggg(rel[int a, int b] r = {<1,2>});

test bool fieldsNamesOfKeywordParametersIssue1851() {
    ax = ggg();
    return ax.r.a == {1};
}