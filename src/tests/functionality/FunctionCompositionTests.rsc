module tests::functionality::FunctionCompositionTests
/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
public int fib(0) = 0;
public int fib(1) = 1;
public default int fib(int n) = fib(n-1) + fib(n-2);

public int fact(0) = 1;
public int fact(1) = 1;
public default int fact(int n) = n*fact(n-1);
public str printResult(int n) = " <n>; ";
public str printResult(str s) = s + s;
	
public int f(0) = 0; 
public int f(1) = 1;
public default int f(int n) = n + 1; 
	
public int g(0) { fail; }
public int g(1) = 1; 
public default int g(int n) = n + 2;

	
public test bool factorialFibonacci() {
		list[int] inputs = [0,1,2,3,4,5,6,7,8,9]; 
		list[int] outputs1 = [ fact(fib(i)) | int i <- inputs ]; 
	    list[int] outputs2 = [ (fact o fib)(i) | int i <- inputs ];
	    return outputs1 == outputs2;
	}
	

public test bool factorialFibonacciPrint() {
				list[int] inputs = [0,1,2,3,4,5,6,7,8,9];
				list[str] outputs1 = [ printResult(fact(fib(i))) | int i <- inputs ];
				list[str] outputs2 = [ (printResult o fact o fib)(i) | int i <- inputs ];
				// associativity check of the 'o' operator
				list[str] outputs3 = [ ( (printResult o fact) o fib)(i) | int i <- inputs ]; 
				list[str] outputs4 = [ (printResult o (fact o fib))(i) | int i <- inputs ];
				return (outputs1 == outputs2) && (outputs1 == outputs3) && (outputs1 == outputs4); 
				}		
		
public test bool anonymousFunctionComposition() {
				list[int] inputs = [0,1,2,3,4,5,6,7,8,9]; 
				list[int] outputs1 = [ int (int n) { switch(n) { case 0: return 1; case 1: return 1; case int m: return m*(m-1); } }  				/* renamed n to m*/
				 							( int (int n) { switch(n) { case 0: return 0; case 1: return 1; case int m: return (m-1) + (m-2); } }  	/* renamed n to m*/
												(i)) | int i <- inputs ]; 
			     list[int] outputs2 = [ (int (int n) { switch(n) { case 0: return 1; case 1: return 1; case int m: return m*(m-1); } } 				/* renamed n to m*/
											o int (int n) { switch(n) { case 0: return 0; case 1: return 1; case int m: return (m-1) + (m-2); } }) 	/* renamed n to m*/
										(i) | int i <- inputs ]; 
				return outputs1 == outputs2; 
				} 

	public test bool composedOverloadedFunctions() {
				return (g o f)(0) == 2; 
	}

	/*
	 * Tests of the '+' composition operator
	 */


     public str h(0) = "0"; 
	 public str h(1) = "1"; 
	public default str h(int n) { fail; } 

	public str i(0) = "1"; 
	public str i(1) = "2"; 
	public default str i(int n) = "<n + 1>"; 
	
	public int j0(0) = 0;
	public int j1(1) = 1; 
	public default int j3(int n) = 2*n; 
	
	public default int j4(int n) = 2*n - 1; 
		
	public int k(int n) = (n%2 == 0) ? { fail; } : 2*n; 
	public int l(int n) = (n%2 == 0) ? n*(n-1) : { fail; }; 
	
	public test bool nonDeterministicChoiceAndNormalComposition1() {
				list[int] inputs = [2,3];
				list[str] outputs1 = [ i(n) | int n <- inputs ];
				list[str] outputs2 = [ (h + i)(n) | int n <- inputs ]; 
				list[str] outputs3 = [ (i + h)(n) | int n <- inputs ]; 
				return outputs1 == outputs2 && outputs1 == outputs3 && 
			 ((h + i)(0) == "0" || (h + i)(0) == "1" ) &&
				( (h + i)(1) == "1" || (h + i)(1) == "2" ) &&
				( (i + h)(0) == "0" || (i + h)(0) == "1" ) &&
				( (i + h)(1) == "1" || (i + h)(1) == "2" ); 
	
			 }
	
	public test bool nonDeterministicChoiceAndNormalComposition2() {
				list[int] inputs = [0,1,2,3,4,5,6,7,8,9,10]; 
				list[int] outputs = [ (n%2 == 0) ? n*(n - 1) : 2*n | int n <- inputs ]; 
				list[int] outputs1 = [ (k + l)(n) | int n <- inputs ]; 
				list[int] outputs2 = [ (l + k)(n) | int n <- inputs ]; 
				list[int] outputs3 = [ ( (k + l) o (l + k) )(n) | int n <- inputs ]; 
				list[int] outputs4 = [ n*(n - 1) | int n <- outputs ]; 
				list[int] outputs5 = [ (j0 + j1 + (k + l) o j3)(n) | int n <- inputs ]; 
				list[int] outputs6 = [ ((k + l) o j4 + j0 + j1)(n) | int n <- inputs ]; 
				list[int] outputs7 = [0,1] + [ 2*n*(2*n - 1) | int n <- inputs - [0,1] ]; 
				list[int] outputs8 = [0,1] + [ 2*(2*n-1) | int n <- inputs - [0,1] ]; 
				list[int] outputs9 = [ 2*n*(2*n - 1) | int n <- inputs ]; 
				list[int] outputs10 = [ 2*(2*n-1) | int n <- inputs ]; 
				list[int] outputs11 = [ (( int (int n) { return (n%2 == 0) ? { fail; } : 2*n; } + l) o (int (int n) { return 2*n - 1; }) + j0 + j1)(n) | int n <- inputs ]; 
				return outputs == outputs1 
				&& outputs == outputs2 
				&& outputs3 == outputs4 
				&& ( outputs5 == outputs7 || outputs5 == outputs9 ) 
				&& ( outputs6 == outputs8 || outputs6 == outputs10 ) 
				&& ( outputs11 == outputs8 || outputs11 == outputs10 ); 			
				}
	


