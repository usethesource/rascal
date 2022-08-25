@license{
 Copyright (c) 2009-2015 CWI
 All rights reserved. This program and the accompanying materials
 are made available under the terms of the Eclipse Public License v1.0
 which accompanies this distribution, and is available at
 http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module lang::rascal::tests::functionality::FunctionComposition

/*
 * The 'o' function composition operator
 */
 
int twice (int n) = 2 * n;
int triple (int n) = 3 * n;

int dup (int n) = n + n;
str dup (str s) = s + s;

int trip(int n) = n + n + n;
str trip(str s) = s + s + s;

test bool twiceTriple1(){
    return (twice o triple)(5) == twice(triple(5));
}

test bool twiceTriple2(){
    c = twice o triple;
    return c(5) == twice(triple(5));
}

test bool dupTriple1(){
    return (dup o triple)(5) == dup(triple(5));
}

test bool tripleDup1(){
    return (triple o dup)(5) == triple(dup(5));
}

test bool dupTrip1(){
    return (dup o trip)(5) == dup(trip(5));
}

test bool dupTrip2(){
    c = dup o trip;
    return c(5) == dup(trip(5));
}

test bool dupTrip3(){
    c = dup o trip;
    return c("abc") == dup(trip("abc"));
}        

int fib(0) = 0;
int fib(1) = 1;
default int fib(int n) = fib(n-1) + fib(n-2);

int fact(0) = 1;
int fact(1) = 1;
default int fact(int n) = n*fact(n-1);
str printResult(int n) = " <n>; ";
str printResult(str s) = s + s;
	
int f(0) = 0; 
int f(1) = 1;
default int f(int n) = n + 1; 
	
int g(0) { fail; }
int g(1) = 1; 
default int g(int n) = n + 2;

test bool factorialFibonacci() {
    list[int] inputs = [0,1,2,3,4,5,6,7,8,9]; 
    list[int] outputs1 = [ fact(fib(i)) | int i <- inputs ]; 
	list[int] outputs2 = [ (fact o fib)(i) | int i <- inputs ];
	return outputs1 == outputs2;
}
	
test bool factorialFibonacciPrint1() {
    list[int] inputs = [0,1,2,3,4,5,6,7,8,9];
    list[str] outputs1 = [ printResult(fact(fib(i))) | int i <- inputs ];
    list[str] outputs2 = [ (printResult o fact o fib)(i) | int i <- inputs ];
    
    return outputs1 == outputs2; 
}

test bool factorialFibonacciPrint2() {
    list[int] inputs = [0,1,2,3,4,5,6,7,8,9];
    list[str] outputs1 = [ printResult(fact(fib(i))) | int i <- inputs ];
    
    // associativity check of the 'o' operator
    list[str] outputs3 = [ ( (printResult o fact) o fib)(i) | int i <- inputs ]; 
    return outputs1 == outputs3; 
}

test bool factorialFibonacciPrint3() {
    list[int] inputs = [0,1,2,3,4,5,6,7,8,9];
    list[str] outputs1 = [ printResult(fact(fib(i))) | int i <- inputs ];
   
    // associativity check of the 'o' operator
 
    list[str] outputs4 = [ (printResult o (fact o fib))(i) | int i <- inputs ];
    return outputs1 == outputs4; 
}		
		
test bool anonymousFunctionComposition() {
    list[int] inputs = [0,1,2,3,4,5,6,7,8,9]; 
    list[int] outputs1 = [ int (int n) { switch(n) { case 0: return 1; case 1: return 1; case int m: return m*(m-1); default: return -1;} }  			/* renamed n to m*/
                           ( int (int n) { switch(n) { case 0: return 0; case 1: return 1; case int m: return (m-1) + (m-2); default: return -1;} }  	/* renamed n to m*/
						   (i)) 
						 | int i <- inputs ]; 
    list[int] outputs2 = [ (int (int n) { switch(n) { case 0: return 1; case 1: return 1; case int m: return m*(m-1); default: return -1;} } 			/* renamed n to m*/
						  o int (int n) { switch(n) { case 0: return 0; case 1: return 1; case int m: return (m-1) + (m-2); default: return -1;} }) 	/* renamed n to m*/
							(i) 
						 | int i <- inputs ]; 
    return outputs1 == outputs2; 
} 

test bool composedOverloadedFunctions1() {
    return (g o f)(0) == g(f(0)); 
}

test bool composedOverloadedFunctions2() {
	return (g o f)(0) == 2; 
}

/*
 * The '+' function composition operator
 */

str h(0) = "0"; 
str h(1) = "1"; 
default str h(int n) { fail; } 

str i(0) = "1"; 
str i(1) = "2"; 
default str i(int n) = "<n + 1>"; 
	
int j0(0) = 0;
int j1(1) = 1; 
default int j3(int n) = 2*n; 
	
default int j4(int n) = 2*n - 1; 

int k(int n) {
    if(n%2 == 0){
         fail k;
    } else {
        return 2*n; 
    }
} 

int l(int n) {
    if(n%2 == 0){
        return n*(n-1);
    } else { 
        fail l;
    };
}
	
test bool nonDeterministicChoiceAndNormalComposition11() {
    list[int] inputs = [2,3];
    list[str] outputs1 = [ i(n) | int n <- inputs ];
    list[str] outputs2 = [ (h + i)(n) | int n <- inputs ]; 
    return outputs1 == outputs2;
}


test bool nonDeterministicChoiceAndNormalComposition12() {
    list[int] inputs = [2,3];
    list[str] outputs1 = [ i(n) | int n <- inputs ]; 
    list[str] outputs3 = [ (i + h)(n) | int n <- inputs ]; 
    return outputs1 == outputs3;    
}

test bool nonDeterministicChoiceAndNormalComposition13() =
    (h + i)(0) == "0" || (h + i)(0) == "1";
            
test bool nonDeterministicChoiceAndNormalComposition14() =           
    (h + i)(1) == "1" || (h + i)(1) == "2";
            
test bool nonDeterministicChoiceAndNormalComposition15() =                 
    (i + h)(0) == "0" || (i + h)(0) == "1";
            
test bool nonDeterministicChoiceAndNormalComposition16() =             
    (i + h)(1) == "1" || (i + h)(1) == "2"; 
    
test bool nonDeterministicChoiceAndNormalComposition21() {
    list[int] inputs = [0,1,2,3,4,5,6,7,8,9,10]; 
    list[int] outputs = [ (n%2 == 0) ? n*(n - 1) : 2*n | int n <- inputs ]; 
    list[int] outputs1 = [ (k + l)(n) | int n <- inputs ]; 
    
    return outputs == outputs1;
   }

test bool nonDeterministicChoiceAndNormalComposition22() {
    list[int] inputs = [0,1,2,3,4,5,6,7,8,9,10]; 
    list[int] outputs = [ (n%2 == 0) ? n*(n - 1) : 2*n | int n <- inputs ]; 
    list[int] outputs2 = [ (l + k)(n) | int n <- inputs ]; 
    
    return  outputs == outputs2;
}

test bool nonDeterministicChoiceAndNormalComposition23() {
    list[int] inputs = [0,1,2,3,4,5,6,7,8,9,10]; 
    list[int] outputs = [ (n%2 == 0) ? n*(n - 1) : 2*n | int n <- inputs ]; 
    list[int] outputs3 = [ ( (k + l) o (l + k) )(n) | int n <- inputs ]; 
    list[int] outputs4 = [ n*(n - 1) | int n <- outputs ]; 
    
    return outputs3 == outputs4;
}

test bool nonDeterministicChoiceAndNormalComposition24() {
    list[int] inputs = [0,1,2,3,4,5,6,7,8,9,10]; 

    list[int] outputs5 = [ (j0 + j1 + (k + l) o j3)(n) | int n <- inputs ]; 
    list[int] outputs7 = [0,1] + [ 2*n*(2*n - 1) | int n <- inputs - [0,1] ]; 
    list[int] outputs9 = [ 2*n*(2*n - 1) | int n <- inputs ];
    
    return outputs5 == outputs7 || outputs5 == outputs9 ;
}

test bool nonDeterministicChoiceAndNormalComposition25() {
    list[int] inputs = [0,1,2,3,4,5,6,7,8,9,10]; 
    
    list[int] outputs6 = [ ((k + l) o j4 + j0 + j1)(n) | int n <- inputs ]; 
    list[int] outputs8 = [0,1] + [ 2*(2*n-1) | int n <- inputs - [0,1] ];
    list[int] outputs10 = [ 2*(2*n-1) | int n <- inputs ]; 

    return outputs6 == outputs8 || outputs6 == outputs10 ;
}

int twiceNotEven(int n) { 
    if(n%2 == 0){
        fail twiceNotEven; 
    } else {
        return 2*n;
    }
}
 
test bool nonDeterministicChoiceAndNormalComposition26() {
    list[int] inputs = [0,1,2,3,4,5,6,7,8,9,10]; 
    
    list[int] outputs8 = [0,1] + [ 2*(2*n-1) | int n <- inputs - [0,1] ];
    list[int] outputs10 = [ 2*(2*n-1) | int n <- inputs ]; 
    list[int] outputs11 = [ (( twiceNotEven + l) o (int (int n) { return 2*n - 1; }) + j0 + j1)(n) | int n <- inputs ]; 
    
    return outputs11 == outputs8 || outputs11 == outputs10;             
}

test bool nonDeterministicChoiceAndNormalComposition27() {
    list[int] inputs = [0,1,2,3,4,5,6,7,8,9,10]; 
    
    list[int] outputs8 = [0,1] + [ 2*(2*n-1) | int n <- inputs - [0,1] ];
    list[int] outputs10 = [ 2*(2*n-1) | int n <- inputs ]; 
    list[int] outputs11 = [ (( l + twiceNotEven ) o (int (int n) { return 2*n - 1; }) + j0 + j1)(n) | int n <- inputs ]; 
    
    return outputs11 == outputs8 || outputs11 == outputs10;             
}
