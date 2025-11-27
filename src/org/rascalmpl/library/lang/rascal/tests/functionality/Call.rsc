@license{
 Copyright (c) 2009-2014 CWI
 All rights reserved. This program and the accompanying materials
 are made available under the terms of the Eclipse License v1.0
 which accompanies this distribution, and is available at
 http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl - CWI}
module lang::rascal::tests::functionality::Call
 
import List;

import lang::rascal::tests::functionality::CallAux;

// voidFun
  
test bool voidFun() {void f(){ } f(); return true;}
  		
int fac(int n){ return (n <= 0) ? 1 : (n * fac(n - 1));}

test bool testFac() = fac(0) == 1 && fac(1) == 1 && fac(2) == 2 && 
                      fac(3) == 6 && fac(4) == 24;
  	
int facNT(int n)  { 
    if(n == 0) { return 1;} 
    int z = facNT(n-1); return z*n; 
}
  
// facNotTailRec
  
test bool facNotTailRec() = facNT(0) == 1 && facNT(1) == 1 && facNT(2) == 2 && 
  		                    facNT(3) == 6 && facNT(4) == 24;	      
  		      
// formalsAreLocal
  
test bool formalsAreLocal(){
    int fac(int n) { if (n == 0) { return 1; } int z = n; int m = fac(n - 1); return z * m; }
	return fac(0) == 1 && fac(1) == 1 && fac(2) == 2 && 
           fac(3) == 6 && fac(4) == 24;
}
  
// higherOrder
  		
test bool higherOrder() {
	int add(int a, int b) { return a + b; };
	int doSomething(int (int a, int b) F) { return F(1,2); }; 
	int sub(int a, int b) { return a - b; }
	if (doSomething(add) != 3) return false;
	if (doSomething(sub) != -1) return false;
	return true;
}
  	
// closures
  
test bool closures1() {
	int f(int (int i) g, int j) { return g(j);}
	if (f(int (int i) { return i + 1; }, 0) != 1) return false;
	return true;
}

test bool closures2() {
    int x = 1;
    int f(int (int i) g, int j) { return g(j);}
    if (f(int (int i) { x = x * 2; return i + x; }, 1) != 3 || (x != 2))
        return false;
    return true;
}
  	    
// closuresVariables

bool() x = bool() { return false; } ;

void changeX(bool() newX) { x = newX; }
 
bool getX() = x();
 
test bool closureVariables() {
    x = bool() { return false; } ;
    b1 = getX() == false;
    changeX(bool() { return true; });
    return b1 && getX();
}   
  	
// varArgs0
   
test bool varArgs01(){
	int add(int i...) { return 0; }
	return add() == 0;
}

test bool varArgs02(){
	int add(int i...) { return 0; }
	return add([]) == 0;
}

test bool varArgs03(){
    int add(int i...) { return 0; }
    return add(0) == 0;
}

test bool varArgs04(){
    int add(int i...) { return 0; }
    return add([0]) == 0;
}

test bool varArgs05(){
    int add(int i...) { return 0; }
    return add(0,1,2) == 0;
}

test bool varArgs06(){
    int add(int i...) { return 0; }
    return add([0,1,2]) == 0;
}
   		
// varArgs1
   		
test bool varArgs11(){
    int add(int i...) { return i[0]; }
    return add(0) == 0;
}

test bool varArgs12(){
    int add(int i...) { return i[0]; }
    return add([0]) == 0;
}

test bool varArgs13(){
    int add(int i...) { return i[0]; }
    return add(0,1,2) == 0;
}

test bool varArgs14(){
    int add(int i...) { return i[0]; }
    return add([0,1,2]) == 0;
}
   		
// varArgs2
   	
test bool varArgs21(){
    int add(int i, int j...) { return i + j[0]; }
    return add(1,2) == 3;
}
test bool varArgs22(){
    int add(int i, int j...) { return i + j[0]; }
    return add(1,[2]) == 3;
}
test bool varArgs23(){
    int add(int i, int j...) { return i + j[0]; }
    return add(1,2,3) == 3;
}
   		
test bool varArgs24(){
    int add(int i, int j...) { return i + j[0]; }
    return add(1,[2,3]) == 3;
}
   		
// varArgs3
   		
test bool varArgs31(){
    list[int] add(int i, int j...) = j;
    return add(1) == [];
}
        
test bool varArgs32(){
    list[int] add(int i, int j...) = j;
    return add(1, 2) == [2];
}
        
test bool varArgs33(){
    list[int] add(int i, int j...) = j;
    return add(1, 2, 3) == [2, 3];
}
  
// sideEffect
  	
test bool sideEffect1() {
    int called = 0;  								// changed order, since forward refs are no longer allowed
    void One() { called = called + 1; return; }
    One(); 
    One(); 
    One(); 
    return called == 3;
}
  	
// max1
  
test bool max1() { 
    int max(int a, int b) { return a > b ? a : b; } 
    return max(3,4) == 4;
}

test bool max2() { 
    int max(int a, int b) { return a > b ? a : b; } 
    real max(real a, real b) { return a > b ? a : b; }
    return max(3,4) == 4 && max(3.0,4.0) == 4.0;
}
  		    
  		 /*changed: overloading is ambiguous */   
  		//test bool max3() { 
  		//    int max(int a, int b) { return a > b ? a : b; } 
  		//    real max(real a, real b) { return a > b ? a : b; }
  		//    &T max(&T a, &T b) { return a > b ? a : b; }
  		//    return max(3,4) == 4 && max(3.0,4.0) == 4.0
  		//    && max("abc","def")=="def";
  		//    }

  	
// ident
  		
test bool ident1() {
    &T ident(&T x){ return x; }
    return ident(true) == true;
}    
    
test bool ident2() {
    &T ident(&T x){ return x; }
    return ident(4) == 4;
}

test bool ident3() {
    &T ident(&T x){ return x; }
    return ident(4.5) == 4.5;
}

test bool ident4() {
    &T ident(&T x){ return x; }
    return ident("abc") == "abc";
}
  		  
data DATA = f(int n);
  		            
test bool ident5() {
    &T ident(&T x){ return x; }
    return ident(f(1)) == f(1);
}

test bool ident6() {
    &T ident(&T x){ return x; }
    return ident([1,2,3]) == [1,2,3];
}

test bool ident7() {
    &T ident(&T x){ return x; }
    return ident({1,2,3}) == {1,2,3};
}

test bool ident8() {
    &T ident(&T x){ return x; }
    return ident((1:10,2:20,3:30)) == (1:10,2:20,3:30);
}
  
// map
  
test bool  map1() {
    map[&K,&V] put(map[&K,&V] m, &K k, &V v) { m[k] = v; return m; }
    return put((),1,"1") == (1:"1");
}
  
data X = xx() | yy() | zz();	
  
// dispatchTest1
  
test bool  dispatchTest1() { 
    int f1(xx()) = 1;
    int f1(yy()) = 2;
    int f1(zz()) = 3;
  	return [f1(xx()),f1(yy()),f1(zz())] == [1,2,3];
}
  	
test bool  dispatchTest2() { 
    int f2(xx()) = 1;
    int f2(yy()) = 2;
    int f2(zz()) = 3;
    default int f2(int x) = x;
  	return [f2(xx()),f2(yy()),f2(zz()), f2(4)] == [1,2,3,4];
}


test bool  dispatchTest3() { 
	int f3(/[a-z]+/) = 1;
    int f3(/[0-9]+/) = 2;
    return f3("abc") == 1 && f3("123") == 2;
}

test bool  dispatchTest4() { 
    str f4(/X<v:[a-z]+>Y/) = v;
    str f4(/X<v:[0-9]+>Y/) = v;
    return f4("XabcY") == "abc" && f4("X123Y") == "123";
}

// Indirect calls

@ignoreInterpreter
test bool indirect1(){
	bool isLF(int c) = c == 0x000A;
    l = [ isLF ];
    elem = l[0];
    return !elem(0);
}
 
//  keywordTest
   
test bool keywordTest1() { 
    int incr(int x, int delta=1) = x + delta;
    return incr(3) == 4 && incr(3, delta=2) == 5;
}
  	
test bool  keywordTest2() { 
    int sum(int x = 0, int y = 0) = x + y;
    return sum() == 0 && sum(x=5, y=7) == 5+7 &&
    sum(y=7,x=5)== 5+7;
}
  
test bool keywordTest3(){
    list[int] varargs(int x, int y ..., int z = 0, str q = "a") = y;
    return varargs(1,2,3,4) == [2,3,4];
}
test bool keywordTest4(){
    list[int] varargs(int x, int y ..., int z = 0, str q = "a") = y;
    return varargs(1,2,3,4,q="b") == [2,3,4];
}
test bool keywordTest5(){
    list[int] varargs(int x, int y ..., int z = 0, str q = "a") = y;
    return varargs(1,2,3,4,z=5) == [2,3,4];
}
test bool keywordTest6(){
    list[int] varargs(int x, int y ..., int z = 0, str q = "a") = y;
    return varargs(1,2,3,4,q="b",z=5) == [2,3,4];
}
test bool keywordTest7(){
    int vol(int x, int y, int z, int area = x * y, int volume = area * z) = volume;
    return vol(1,2,3) == 6; 
}

data Figure (real shrink = 1.0, str fillColor = "white", str lineColor = "black")  =  emptyFigure() 
  | ellipse(Figure inner = emptyFigure()) 
  | box(Figure inner = emptyFigure())
  | volume(int width, int height, int depth, int area = width * height, int volume = area * depth)
  ;
  
test bool keywordTest8() = emptyFigure().fillColor == "white";

test bool keywordTest9() = emptyFigure(shrink=0.5).fillColor == "white";

test bool keywordTest10() = emptyFigure(lineColor="red").fillColor == "white";

test bool keywordTest11() = emptyFigure(lineColor="red", shrink=0.5).fillColor == "white";

test bool keywordTest12() = emptyFigure(fillColor="red").fillColor == "red";

test bool keywordTest13() = emptyFigure(shrink=0.5,fillColor="red").fillColor == "red";

test bool keywordTest14() = emptyFigure(shrink=0.5,fillColor="red", lineColor="black").fillColor == "red";

test bool keywordTest15() = emptyFigure(lineColor="red", shrink=0.5).fillColor == "white";

test bool keywordTest16() = ellipse().fillColor == "white";
 
test bool keywordTest17() = volume(2,3,4).area == 6 && volume(2,3,4).volume == 24;

test bool keywordTest18() = volume(2,3,4,area=0).volume == 0;

test bool keywordTest19() = volume(2,3,4,volume=0).area == 6;

test bool keywordTest20() = ellipse(inner=emptyFigure(fillColor="red")).fillColor == "white";

test bool keywordTest21() = ellipse(inner=emptyFigure(fillColor="red")).inner.fillColor == "red";

data D = d(int x, int y = 3);
  
data POINT = point(int x, int y, str color = "red");
  
// keywordMatchTest1
  	
test bool keywordMatchTest1() = point(_,_,color=_) := point(1,2);
test bool keywordMatchTest2() = point(_,_,color="red") := point(1,2);
test bool keywordMatchTest3() = point(_,_,color="green") !:= point(1,2, color="red");
test bool keywordMatchTest4() = point(_,_,color="green") := point(1,2, color="green");
test bool keywordMatchTest5() = point(1,2) := point(1,2);
test bool keywordMatchTest6() = point(1,2) !:= point(1,3);
test bool keywordMatchTest7() = point(1,2) := point(1,2,color="red");
test bool keywordMatchTest8() = point(1,2,color="red") := point(1,2,color="red");
test bool keywordMatchTest9() = point(1,2,color="green") !:= point(1,2);
test bool keywordMatchTest10() =point(1,2,color="green") !:= point(1,2);
  
data POINT1 = point1(int x, int y, int z = 3, list[str] colors = []);	
  
// keywordMatchTest2
  
test bool keywordMatchTest11() = point1(_, _, colors=["blue"]) := point1(1,2, colors=["blue"]);
  		
test bool keywordMatchTest12() =point1(_, _, colors=[*_,"blue",*_]) := point1(1,2, colors=["red","green","blue"]);

test bool keywordMatchTest13() =point1(_, _, colors=[*_,*X,*_,*X, *_]) := point1(1,2, colors=["red","blue","green","blue"]);
 
data Expr(int depth = 0) = id(str x);
data Expr(int width = 1) = number(num n);

test bool genericKwParams1() = number(1).depth == 0;

test bool genericKwParams2() = id("tommie").width == 1;

test bool genericKwParamsBack1() = number(1).q == 4;

data Expr(int p = 2, int q = 2 * p) = a(Expr l, Expr r, int z = p * q);

test bool genericKwParams3() = a(id("x"), id("y")).z == 8;

test bool genericKwParams4() = a(id("x"),id("y"),p = 3).z == 18;

// defaults

int f01n(0) = 10;
int f01n(1) = 11;
default int f01n(int n) = 100;

test bool f01n1() = f01n(0) == 10;
test bool f01n2() = f01n(1) == 11;
test bool f01n3() = f01n(2) == 100;

data E = e0() | e1(int n);

E trans("e0", []) = e0();
default E trans(str _, list[value] vals) = e1(0);

test bool trans1() = trans("e0", []) == e0();
test bool trans2() = trans("abc", []) == e1(0);
test bool trans3() = trans("abc", [1,2]) == e1(0);

int translateConstantCall(str name, list[value] args) =
    tcc(name, args);

private int tcc("value", []) = 0;
private int tcc("value", list[int] L) = 1 when size(L) == 1;
private int tcc("value", list[int] L) = 2 when size(L) == 2;

private default int tcc(str name, list[value] args) { return -1;}

test bool tcc1() = translateConstantCall("value", []) == 0;
test bool tcc2() = translateConstantCall("value", [1]) == 1;
test bool tcc3() = translateConstantCall("value", [1, 2]) == 2;
test bool tcc4() = translateConstantCall("xxx", []) == -1;
test bool tcc5() = translateConstantCall("xxx", [1]) == -1;

// backtracking tests, also uses an alternative from CallTestsAux

C c(int i) {
  if (i == 0 || i mod 3 != 0) 
    fail c;
  else
    return c(i / 3);
}

C c(int i) {
  if (i == 0 || i mod 2 != 0) 
    fail c;
  else
    return c(i / 2);
}

C c(int i) = c(i / 7) when i mod 7 == 0, i != 0;

test bool bt1() = c(7 * 5 * 3 * 2) == c(1);
test bool bt2() = c(5 * 3 * 2) == c(1);
test bool bt3() = c(3 * 2) == c(1);
test bool bt(int i) = (j := i mod 100) && c(xxx) := c(j) && xxx <= j;

// when clauses

int fw(int n) = 10 when 0 !:= n;
default int fw(int n) = -1;

test bool negativeMatch1() = fw(0) == -1;
test bool negativeMatch2() = fw(1) == 10;