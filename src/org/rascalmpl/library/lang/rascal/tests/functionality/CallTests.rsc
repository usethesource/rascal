 module lang::rascal::tests::functionality::CallTests
 /*******************************************************************************
   * Copyright (c) 2009-2011 CWI
   * All rights reserved. This program and the accompanying materials
   * are made available under the terms of the Eclipse License v1.0
   * which accompanies this distribution, and is available at
   * http://www.eclipse.org/legal/epl-v10.html
   *
   * Contributors:
  
   *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
   *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
   *   * Paul Klint - Paul.Klint@cwi.nl - CWI
   *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
   *   * Bert Lisser - Bert.Lisser@cwi.nl - CWI
  *******************************************************************************/

import ParseTree;

import  lang::rascal::tests::functionality::CallTestsAux;

syntax XYZ = "x" | "y" | "z";

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
  
test bool closures() {
	int x = 1;
	int f(int (int i) g, int j) { return g(j);}
	if (f(int (int i) { return i + 1; }, 0) != 1) return false;
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

test bool varArgs02(){
    int add(int i...) { return 0; }
    return add(0) == 0;
}

test bool varArgs03(){
    int add(int i...) { return 0; }
    return add([0]) == 0;
}

test bool varArgs04(){
    int add(int i...) { return 0; }
    return add(0,1,2) == 0;
}

test bool varArgs05(){
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

test bool varArgs11(){
    int add(int i...) { return i[0]; }
    return add(0,1,2) == 0;
}

test bool varArgs11(){
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
    int f(xx()) = 1;
    int f(yy()) = 2;
    int f(zz()) = 3;
  	return [f(xx()),f(yy()),f(zz())] == [1,2,3];
}
  	
test bool  dispatchTest2() { 
    int f(xx()) = 1;
    int f(yy()) = 2;
    int f(zz()) = 3;
    default int f(int x) = x;
  	return [f(xx()),f(yy()),f(zz()), f(4)] == [1,2,3,4];
}
  
test bool  dispatchTest3() { 
    int f((XYZ) `x`) = 1;
    int f((XYZ) `y`) = 2;
    int f((XYZ) `z`) = 3;
  		
    return [f((XYZ)`x`),f((XYZ)`y`),f((XYZ)`z`)] == [1,2,3];
}	
 
//  keywordTest
   
test bool keywordTest11() { 
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
  
test bool keywordTest7() = emptyFigure().fillColor == "white";

test bool keywordTest8() = emptyFigure(shrink=0.5).fillColor == "white";

test bool keywordTest9() = emptyFigure(lineColor="red").fillColor == "white";

test bool keywordTest10() = emptyFigure(lineColor="red", shrink=0.5).fillColor == "white";

test bool keywordTest11() = emptyFigure(fillColor="red").fillColor == "red";

test bool keywordTest12() = emptyFigure(shrink=0.5,fillColor="red").fillColor == "red";

test bool keywordTest13() = emptyFigure(shrink=0.5,fillColor="red", lineColor="black").fillColor == "red";

test bool keywordTest14() = emptyFigure(lineColor="red", shrink=0.5).fillColor == "white";

test bool keywordTest15() = ellipse().fillColor == "white";
 
test bool keywordTest16() = volume(2,3,4).area == 6 && volume(2,3,4).volume == 24;

test bool keywordTest17() = volume(2,3,4,area=0).volume == 0;

test bool keywordTest18() = volume(2,3,4,volume=0).area == 6;



/* The following give NoSuchKey errors: TC info is missing or not found in the compiler */
/*TODO:TC*///test bool keywordTest16() = ellipse(inner=emptyFigure(fillColor="red")).fillColor == "white";

/*TODO:TC*///test bool keywordTest17() = ellipse(inner=emptyFigure(fillColor="red")).inner.fillColor == "red";


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
/*TODO:TC+COMP*///test bool keywordMatchTest13() =point1(_, _, colors=[*_,*X,*_,*X, *_]) := point1(1,2, colors=["red","blue","green","blue"]);
 