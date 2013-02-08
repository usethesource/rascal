/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.test.forking;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredVariable;
import org.rascalmpl.test.infrastructure.ConcurrentTestFramework;

public class ComprehensionTests extends ConcurrentTestFramework {
	
	@Test
	public void emptySetGeneratorError1(){
		assertTrue(runTest("{ X | int X <- {} } == {};"));
	}
	
	@Test
	public void emptySetGeneratorError2(){
		assertTrue(runTest("{ X | int X <- [] } == {};"));
	}
	
	@Test public void setComprehension1() {
		
		assertTrue(runTest("{ X | int X <- {1}} == {1};"));
		assertTrue(runTest("{ X | int X <- [1]} == {1};"));
		
		assertTrue(runTest("{ X | X <- {1}} == {1};"));
		assertTrue(runTest("{ X | X <- [1]} == {1};"));
		
		assertTrue(runTest("{ X | int X <- {1, 2}} == {1,2};"));
		assertTrue(runTest("{ X | int X <- [1, 2]} == {1,2};"));
		
		assertTrue(runTest("{ X | X <- {1, 2}} == {1,2};"));
		assertTrue(runTest("{ X | X <- [1, 2]} == {1,2};"));
		
		assertTrue(runTest("{ X | int X <- {1, 1, 1}} == {1};"));
		assertTrue(runTest("{ X | int X <- [1, 1, 1]} == {1};"));
		
		assertTrue(runTest("{ 1 | int X <- {1,2,3}} == {1};"));
		assertTrue(runTest("{ 1 | int X <- [1,2,3]} == {1};"));
		
		assertTrue(runTest("{ 1 | int X <- {1,2,3}, true } == {1};"));
		assertTrue(runTest("{ 1 | int X <- [1,2,3], true } == {1};"));
		
		assertTrue(runTest("{ 1 | int X <- {1,2,3}, false} 	== {};"));
		assertTrue(runTest("{ 1 | int X <- [1,2,3], false} 	== {};"));
		
		assertTrue(runTest("{ X | int X <- {1,2,3}} == {1,2,3};"));
		assertTrue(runTest("{ X | int X <- [1,2,3]} == {1,2,3};"));
		
		assertTrue(runTest("{  X | int X <- {1,2,3}, true} == {1,2,3};"));
		assertTrue(runTest("{  X | int X <- [1,2,3], true} == {1,2,3};"));
		
		assertTrue(runTest("{  X | int X <- {1,2,3}, false} 	== {};"));
		assertTrue(runTest("{  X | int X <- [1,2,3], false} 	== {};"));
		
		assertTrue(runTest("{  X | int X <- {1,2,3}, X >= 2, X < 3} == {2};"));
		assertTrue(runTest("{  X | int X <- [1,2,3], X >= 2, X < 3} == {2};"));
		
		assertTrue(runTest("{  X, 10*X | int X <- [1,2,3]} == {1,2,3,10,20,30};"));
		assertTrue(runTest("{  X, 10*X, 100*X | int X <- [1,2,3]} == {1,2,3,10,20,30, 100,200,300};"));
	}
	
	@Test public void setComprehension2() {	
		
		assertTrue(runTest("{  {} | int X <- {1,2,3}} == {{}};"));
		assertTrue(runTest("{  {} | int X <- [1,2,3]} == {{}};"));
		
		assertTrue(runTest("{  {} | int X <- {1,2,3}, true} == {{}};"));
		assertTrue(runTest("{  {} | int X <- [1,2,3], true} == {{}};"));
		
		assertTrue(runTest("{  {} | int X <- {1,2,3}, false} == {};"));
		assertTrue(runTest("{  {} | int X <- [1,2,3], false} == {};"));
		
		assertTrue(runTest("{ <1,2,3> | int X <- {1,2,3}} 	== {<1,2,3>};"));
		assertTrue(runTest("{ <1,2,3> | int X <- [1,2,3]} 	== {<1,2,3>};"));
		
		assertTrue(runTest("{ <1,2,3> | int X <- {1,2,3}, true} 	== {<1,2,3>};"));
		assertTrue(runTest("{ <1,2,3> | int X <- [1,2,3], true} 	== {<1,2,3>};"));
		
		assertTrue(runTest("{ <1,2,3> | int X <- {1,2,3}, true, true} == {<1,2,3>};"));
		assertTrue(runTest("{ <1,2,3> | int X <- [1,2,3], true, true} == {<1,2,3>};"));
		
		assertTrue(runTest("{ <1,2,3> | int X <- {1,2,3}, false}	== {} ;"));
		assertTrue(runTest("{ <1,2,3> | int X <- [1,2,3], false}	== {} ;"));
		
		assertTrue(runTest("{ Y | list[int] Y <- [[1,2,3],[10,20,30],[100,200,300]] } == { [1,2,3],[10,20,30],[100,200,300]};"));
		assertTrue(runTest("{1 | 3 > 2} == {1} ;"));
		assertTrue(runTest("{1 | 2 > 3} == {} ;"));
	}
	

	
	@Test(expected=StaticError.class)
	public void testGen1() {
		runTest("{x | 5};");
	}
	
	@Test(expected=StaticError.class)
	public void testVoidFunctionPredicate() {
		runTest("{ void f() { } { x | int x <- {1,2,3}, f() }; }");
	}
	
	@Test(expected=StaticError.class)
	public void testUndefinedValue() {
		runTest("{ y | int x <- {1,2,3}};");
	}
	
//	@Test(expected=StaticError.class)
//	public void WrongStrategyError1(){
//		runTest("innermost int X <- {1,2,3};");
//	}
//	
//	@Test(expected=StaticError.class)
//	public void WrongStrategyError2(){
//		runTest("outermost int X <- {1,2,3};");
//	}
//	
//	@Test(expected=StaticError.class)
//	public void WrongStrategyError3(){
//		runTest("bottom-up-break int X <- {1,2,3};");
//	}
//	
//	@Test(expected=StaticError.class)
//	public void WrongStrategyError4(){
//		runTest("top-down-break int X <- {1,2,3};");
//	}
	
	@Test(expected=StaticError.class)
	public void WrongListType(){
		runTest("str S <- [1,2,3];");
	}
	
	@Test(expected=StaticError.class)
	public void WrongSetType(){
		runTest("str S <- {1,2,3};");
	}
	
	@Test(expected=StaticError.class)
	public void WrongMapType(){
		runTest("str S <- (1:10,2:20);");
	}
	
	@Test(expected=StaticError.class)
	public void WrongStringType(){
		runTest("int N <- \"abc\";");
	}
	
	@Test(expected=StaticError.class)
	public void WrongADTType(){
		prepare("data Bool = btrue() | bfalse() | band(Bool lhs, Bool rhs) | bor(Bool lhs, Bool rhs);");
		runTest("int N <- [true, true, false];");
	}
	
	@Test public void any()  {
		
		assertTrue(runTest("any(int X <- {1,2,3}, X > 2);"));
		assertTrue(runTest("any(    X <- {1,2,3}, X > 2);"));
		assertTrue(runTest("any(int X <- {1,2,3}, X > 2, X <10);"));
		assertTrue(runTest("any(int X <- {1,2,3}, X > 2 && X <10);"));
		assertTrue(runTest("any(    X <- {1,2,3}, X > 2 && X <10);"));
		
		assertTrue(runTest("any(int X <- [1,2,3], X > 2);"));
		assertTrue(runTest("any(int X <- [1,2,3], X > 2, X < 10);"));
		assertTrue(runTest("any(int X <- [1,2,3], X > 2 && X < 10);"));
		
		assertFalse(runTest("any(int X <- {1,2,3}, X > 10);"));
		assertFalse(runTest("any(int X <- [1,2,3], X > 10);"));
		
		assertTrue(runTest("any(<int X, int Y> <- {<1,10>,<30,3>,<2,20>}, X > Y);"));
		assertTrue(runTest("any(<int X, int Y> <- [<1,10>,<30,3>,<2,20>], X > Y);"));
		
		assertFalse(runTest("any(<int X, int Y> <- {<1,10>,<30,3>,<2,20>}, X > 100*Y);"));
		assertFalse(runTest("any(<int X, int Y> <- [<1,10>,<30,3>,<2,20>], X > 100*Y);"));
	}
	
	
	@Test(expected=StaticError.class)
	public void anyError() {
		runTest("any(x <- [1,2,3], \"abc\");");
	}
	
	@Test 
	public void all() {
		
		assertTrue(runTest("all(int X <- {1,2,3}, X >= 1);"));
		assertTrue(runTest("all(int X <- {1,2,3}, X >= 1, X < 10);"));
		assertTrue(runTest("all(int X <- {1,2,3}, X >= 1 && X < 10);"));
		assertTrue(runTest("all(int X <- [1,2,3], X >= 1);"));
		assertTrue(runTest("all(int X <- {1,2,3}, X >= 1, X < 10);"));
		assertTrue(runTest("all(int X <- {1,2,3}, X >= 1 && X < 10);"));
		
		assertFalse(runTest("all(int X <- {1,2,3}, X >= 2);"));
		assertFalse(runTest("all(int X <- {1,2,3}, X >= 2, X <=2);"));
		assertFalse(runTest("all(int X <- {1,2,3}, X >= 2 && X <=2);"));
		assertFalse(runTest("all(int X <- [1,2,3], X >= 2);"));
		assertFalse(runTest("all(int X <- [1,2,3], X >= 2, X <= 2);"));
		assertFalse(runTest("all(int X <- [1,2,3], X >= 2 && X <= 2);"));
		
		assertTrue(runTest("all(<int X, int Y> <- {<1,10>,<3,30>,<2,20>}, X < Y);"));
		assertTrue(runTest("all(<int X, int Y> <- [<1,10>,<3,30>,<2,20>], X < Y);"));
		
		assertFalse(runTest("all(<int X, int Y> <- {<1,10>,<30,3>,<2,20>}, X < Y);"));
		assertFalse(runTest("all(<int X, int Y> <- [<1,10>,<30,3>,<2,20>], X < Y);"));
		
		assertTrue(runTest("all(int i <- [0, 1] && [0, 1][i] == i);"));
	}
	
	@Test(expected=StaticError.class)
	public void noLeaking() {
		assertTrue(runTest("{{ X | int X <- [1,2,3] }; X == 3; }"));
	}
	
	@Test(expected=StaticError.class)
	public void allError() {
		runTest("all(x <- [1,2,3], \"abc\");");
	}
	
	@Test public void setComprehension3() {	
		
		assertTrue(runTest("{X + 1 | int X <- {1,2,3}} == {2,3,4};"));
		assertTrue(runTest("{X + 1 | int X <- [1,2,3]} == {2,3,4};"));
		
		assertTrue(runTest("{X | int X <- {1,2,3}, X + 1 < 3} == {1};"));
		assertTrue(runTest("{X | int X <- [1,2,3], X + 1 < 3} == {1};"));
		
		assertTrue(runTest("{X - 1 | int X <- {1,2,3}} == {0,1,2};"));
		assertTrue(runTest("{X - 1 | int X <- [1,2,3]} == {0,1,2};"));
		
		assertTrue(runTest("{X | int X <- {1,2,3}, X - 1 < 3} == {1,2,3};"));
		assertTrue(runTest("{X | int X <- [1,2,3], X - 1 < 3} == {1,2,3};"));
		
		assertTrue(runTest("{X * 2 | int X <- {1,2,3}} == {2,4,6};"));
		assertTrue(runTest("{X * 2 | int X <- [1,2,3]} == {2,4,6};"));
		
		
		assertTrue(runTest("{*[X * 2] | int X <- {1,2,3}} == {2,4,6};"));
		assertTrue(runTest("{*[X * 2, X * 2 + 1] | int X <- {1,2,3}} == {2,3,4,5,6,7};"));
	}
	
	@Test
	public void setComprehension4(){
		prepare("set[int] f(int n) { return {n, 3*n}; }");
		assertTrue(runTestInSameEvaluator("{f(n) | n <- [ 1 .. 4 ]} == {{1,3},{2,6},{3,9}};"));
		assertTrue(runTestInSameEvaluator("{*f(n) | n <- [ 1 .. 4 ]} == {1,3,2,6,3,9};"));
		
		assertTrue(runTestInSameEvaluator("{{n, 3*n} | n <- [ 1 .. 4 ]} == {{1,3},{2,6},{3,9}};"));
		assertTrue(runTestInSameEvaluator("{*{n, 3*n} | n <- [ 1 .. 4 ]} == {1,3,2,6,3,9};"));
		assertTrue(runTestInSameEvaluator("{n, 3*n | n <- [ 1 .. 4 ]} == {1,3,2,6,3,9};"));
	
		assertTrue(runTestInSameEvaluator("{{5*n, f(n)} | n <- [ 1 .. 4 ]} == {{5,{1,3}},{10,{2,6}},{15,{3,9}}};"));
		assertTrue(runTestInSameEvaluator("{{5*n, *f(n)} | n <- [ 1 .. 4 ]} == {{5,1,3},{10,2,6},{15,3,9}};"));
		assertTrue(runTestInSameEvaluator("{5*n, f(n) | n <- [ 1 .. 4 ]} == {5,{1,3},10,{2,6},15,{3,9}};"));
		assertTrue(runTestInSameEvaluator("{5*n, *f(n) | n <- [ 1 .. 4 ]} == {5,1,3,10,2,6,15,3,9};"));
		
		assertTrue(runTestInSameEvaluator("{{5*n, f(n)} | n <- [ 1 .. 4 ]} == {{5,{1,3}},{10,{2,6}},{15,{3,9}}};"));
		assertTrue(runTestInSameEvaluator("{{5*n, *f(n)} | n <- [ 1 .. 4 ]} == {{5,1,3},{10,2,6},{15,3,9}};"));
		assertTrue(runTestInSameEvaluator("{5*n, f(n) | n <- [ 1 .. 4 ]} == {5,{1,3},10,{2,6},15,{3,9}};"));
		assertTrue(runTestInSameEvaluator("{5*n, *f(n) | n <- [ 1 .. 4 ]} == {5,1,3,10,2,6,15,3,9};"));
	}
	
	@Test
	public void setComprehensionNested() {
		assertTrue(runTest("{ {X + y | int y <- [1..X+1]} | int X <- {1,2,3}} == {{2}, {3,4}, {4,5,6}};"));
		assertTrue(runTest("{ *{X + y | int y <- [1..X+1]} | int X <- {1,2,3}} == {2, 3, 4, 5, 6};"));
		assertTrue(runTest("{ {X + y | int y <- [1..X+1], X < 2} | int X <- [1,2,3]} == {{2}, {}};"));
		assertTrue(runTest("{ *{X + y | int y <- [1..X+1], X < 2} | int X <- [1,2,3]} == {2};"));
		assertTrue(runTest("{ {X + y | int y <- [1..X+1], X > 2} | int X <- [1,2,3]} == {{}, {4,5,6}};"));
		assertTrue(runTest("{ *{X + y | int y <- [1..X+1], X > 2} | int X <- [1,2,3]} == {4, 5, 6};"));
	}
	
	@Test
	public void emptySetGeneratorError(){
		assertTrue(runTest("[ X | int X <- {} ] == [];"));
	}
	
	@Test
	public void emptyListGeneratorError1(){
		assertTrue(runTest("[ X | int X <- [] ] == [];"));
	}
	
	@Test
	public void emptyListGeneratorError2(){
		assertTrue(runTest("[ X |     X <- [] ] == [];"));
	}
	
	@Test public void listComprehension1()  {
		
		assertTrue(runTest("[ X | int X <- {1}] == [1];"));
		assertTrue(runTest("[ X | int X <- [1]] == [1];"));
		assertTrue(runTest("[ X |     X <- [1]] == [1];"));
		
		assertTrue(runTest("{L = [ X | int X <- {1, 2}]; (L == [1,2]) || (L == [2, 1]);}"));
		assertTrue(runTest("[ X | int X <- [1, 2]] == [1,2];"));
		assertTrue(runTest("[ X |     X <- [1, 2]] == [1,2];"));
		
		assertTrue(runTest("[ X | int X <- {1, 1, 1}] == [1];"));
		assertTrue(runTest("[ X | int X <- [1, 1, 1]] == [1, 1, 1];"));
		
		assertTrue(runTest("[ 1 | int X <- {1,2,3}] == [1, 1, 1];"));
		assertTrue(runTest("[ 1 | int X <- [1,2,3]] == [1, 1, 1];"));
		
		assertTrue(runTest("[ 1 | int X <- {1,2,3}, true ] == [1, 1, 1];"));
		assertTrue(runTest("[ 1 | int X <- [1,2,3], true ] == [1, 1, 1];"));
		
		assertTrue(runTest("[ 1 | int X <- {1,2,3}, false] 	== [];"));
		assertTrue(runTest("[ 1 | int X <- [1,2,3], false] 	== [];"));
		
		assertTrue(runTest("{L = [ X | int X <- {1,2}]; (L == [1,2]) || (L == [2, 1]);}"));
		assertTrue(runTest("[ X | int X <- [1,2,3]] == [1,2,3];"));
		
		assertTrue(runTest("{L = [  X | int X <- {1,2}, true]; (L == [1,2]) || (L == [2, 1]);}"));
		assertTrue(runTest("[  X | int X <- [1,2,3], true] == [1,2,3];"));
		
		assertTrue(runTest("[  X | int X <- {1,2,3}, false] == [];"));
		assertTrue(runTest("[  X | int X <- [1,2,3], false] == [];"));
		
		assertTrue(runTest("[  X | int X <- {1,2,3}, X >= 2, X < 3] == [2];"));
		assertTrue(runTest("[  X | int X <- [1,2,3], X >= 2, X < 3] == [2];"));
		
		assertTrue(runTest("[  X, 10*X | int X <- [1,2,3]] == [1,10,2,20,3,30];"));
		assertTrue(runTest("[  X, 10*X, 100*X | int X <- [1,2,3]] == [1,10,100,2,20,200,3,30,300];"));
	}
	
	@Test public void listComprehension2() {
		
		assertTrue(runTest("[  [] | int X <- {1,2,3}] == [[], [], []];"));
		assertTrue(runTest("[  [] | int X <- [1,2,3]] == [[], [], []];"));
		
		assertTrue(runTest("[  [] | int X <- {1,2,3}, true] == [[], [], []];"));
		assertTrue(runTest("[  [] | int X <- [1,2,3], true] == [[], [], []];"));
		
		assertTrue(runTest("[  [] | int X <- {1,2,3}, false] == [];"));
		assertTrue(runTest("[  [] | int X <- [1,2,3], false] == [];"));
		
		assertTrue(runTest("[ <1,2,3> | int X <- {1,2,3}] == [<1,2,3>, <1,2,3>, <1,2,3>];"));
		assertTrue(runTest("[ <1,2,3> | int X <- [1,2,3]] == [<1,2,3>, <1,2,3>, <1,2,3>];"));
		
		assertTrue(runTest("[ <1,2,3> | int X <- {1,2,3}, true] == [<1,2,3>, <1,2,3>, <1,2,3>];"));
		assertTrue(runTest("[ <1,2,3> | int X <- [1,2,3], true] == [<1,2,3>, <1,2,3>, <1,2,3>];"));
		
		assertTrue(runTest("[ <1,2,3> | int X <- {1,2,3}, true, true] == [<1,2,3>, <1,2,3>, <1,2,3>];"));
		assertTrue(runTest("[ <1,2,3> | int X <- [1,2,3], true, true] == [<1,2,3>, <1,2,3>, <1,2,3>];"));
		
		assertTrue(runTest("[ <1,2,3> | int X <- {1,2,3}, false]	== [] ;"));
		assertTrue(runTest("[ <1,2,3> | int X <- [1,2,3], false]	== [] ;"));
	}
	
	@Test public void listComprehension3()  {
		
		assertTrue(runTest("[ [Y] | list[int] Y <- [[1,2,3],[10,20,30],[100,200,300]] ] == [ [[1,2,3]], [[10,20,30]],[[100,200,300]]];"));
		assertTrue(runTest("[ Y | list[int] Y <- [[1,2,3],[10,20,30],[100,200,300]] ] == [ [1,2,3], [10,20,30],[100,200,300]];"));
		assertTrue(runTest("[ *Y | list[int] Y <- [[1,2,3],[10,20,30],[100,200,300]] ] == [ 1,2,3, 10,20,30,100,200,300];"));
		
		assertTrue(runTest("[1 | 3 > 2] == [1] ;"));
		assertTrue(runTest("[1 | 2 > 3] == [] ;"));
		
		assertTrue(runTest("{L = [X + 1 | int X <- {1,2}]; (L == [2,3]) || (L == [3,2]);}"));
		assertTrue(runTest("[X + 1 | int X <- [1,2,3]] == [2,3,4];"));
		
		assertTrue(runTest("[X | int X <- {1,2,3}, X + 1 < 3] == [1];"));
		assertTrue(runTest("[X | int X <- [1,2,3], X + 1 < 3] == [1];"));
		
		assertTrue(runTest("{L = [X - 1 | int X <- {1,2}]; (L == [0,1]) || (L == [1,0]);}"));
		assertTrue(runTest("[X - 1 | int X <- [1,2,3]] == [0,1,2];"));
		
		assertTrue(runTest("{L = [X | int X <- {2,3}, X - 1 < 3]; (L == [2,3]) || (L == [3,2]);}"));
		assertTrue(runTest("[X | int X <- [1,2,3], X - 1 < 3] == [1,2,3];"));
		
		assertTrue(runTest("{ L = [X * 2 | int X <- {2,3}]; (L == [4,6]) || (L == [6,4]);}"));
		assertTrue(runTest("[X * 2 | int X <- [1,2,3]] == [2,4,6];"));
		
		assertTrue(runTest("[*{X * 2} | int X <- [1,2,3]] == [2,4,6];"));
		prepare("import List;");
		assertTrue(runTestInSameEvaluator("toSet([*{X * 2, X * 2 + 1} | int X <- [1,2,3]]) == {2,3,4,5, 6, 7};"));
	}
	
	@Test
	public void listComprehension4(){
		prepare("list[int] f(int n) { return [n, 3*n]; }");
		assertTrue(runTestInSameEvaluator("[f(n) | n <- [ 1 .. 4 ]] == [[1,3],[2,6],[3,9]];"));
		assertTrue(runTestInSameEvaluator("[*f(n) | n <- [ 1 .. 4 ]] == [1,3,2,6,3,9];"));

		assertTrue(runTestInSameEvaluator("[[n, 3*n] | n <- [ 1 .. 4 ]] == [[1,3],[2,6],[3,9]];"));
		
		assertTrue(runTestInSameEvaluator("[5*n, f(n) | n <- [ 1 .. 4 ]] == [5,[1,3],10,[2,6],15,[3,9]];"));
		assertTrue(runTestInSameEvaluator("[5*n, *f(n) | n <- [ 1 .. 4 ]] == [5,1,3,10,2,6,15,3,9];"));
		
		assertTrue(runTestInSameEvaluator("[[5*n, f(n)] | n <- [ 1 .. 4 ]] == [[5,[1,3]],[10,[2,6]],[15,[3,9]]];"));
		assertTrue(runTestInSameEvaluator("[[5*n, *f(n)] | n <- [ 1 .. 4 ]] == [[5,1,3],[10,2,6],[15,3,9]];"));
	}
	
	@Test
	public void listComprehensionNested() {
		assertTrue(runTest("[  [y | int y <- [0..X+1]] | int X <- [1,2,3]] == [[0,1], [0,1,2], [0,1,2,3]];"));
		assertTrue(runTest("[ *[y | int y <- [0..X+1]] | int X <- [1,2,3]] == [0,1, 0,1,2, 0,1,2,3];"));
		assertTrue(runTest("[ [y | int y <- [0..X+1], X < 2] | int X <- [1,2,3]] == [[0,1], [], []];"));
		assertTrue(runTest("[ *[y | int y <- [0..X+1], X < 2] | int X <- [1,2,3]] == [0,1];"));
		assertTrue(runTest("[ [y | int y <- [0..X+1], X > 2] | int X <- [1,2,3]] == [[], [], [0,1,2,3]];"));
		assertTrue(runTest("[ *[y | int y <- [0..X+1], X > 2] | int X <- [1,2,3]] == [0,1,2,3];"));
		
	}
	
	@Test
	public void emptyTupleGeneratorError1(){
		assertTrue(runTest("{<X,Y> | <int X, int Y> <- {}} == {} ;"));
	}
	
	@Test
	public void emptyTupleGeneratorError2(){
		assertTrue(runTest("{<X,Y> | <int X, int Y> <- []} == {} ;"));
	}
	
	@Test
	public void emptyTupleGeneratorError3(){
		assertTrue(runTest("{<X,Y> | int X <- {}, int Y <- {}} == {};"));
	}
	
	@Test
	public void emptyTupleGeneratorError4(){
		assertTrue(runTest("{<X,Y> | int X <- [], int Y <- []} == {};"));
	}
	
	@Test public void relationComprehension() {	
		
		assertTrue(runTest("{<X,Y> | int X <- {1}, int Y <- {2}} == {<1,2>};"));
		assertTrue(runTest("{<X,Y> | int X <- [1,1,1], int Y <- [2,2,2]} == {<1,2>};"));
		
		assertTrue(runTest("{<1,2> | int X <- {1,2,3}} == {<1,2>};"));
		assertTrue(runTest("{<1,2> | int X <- [1,2,3]} == {<1,2>};"));
		
		assertTrue(runTest("{<X,Y> | int X <- {1,2,3}, int Y <- {2,3,4}} ==  {<1, 2>, <1, 3>, <1, 4>, <2, 2>, <2, 3>, <2, 4>, <3, 2>, <3, 3>, <3, 4>};"));
		assertTrue(runTest("{<X,Y> | int X <- [1,2,3], int Y <- [2,3,4]} ==  {<1, 2>, <1, 3>, <1, 4>, <2, 2>, <2, 3>, <2, 4>, <3, 2>, <3, 3>, <3, 4>};"));
		
		assertTrue(runTest("{<X,Y> | int X <- {1,2,3}, int Y <- {2,3,4}, true} ==	{<1, 2>, <1, 3>, <1, 4>, <2, 2>, <2, 3>, <2, 4>, <3, 2>, <3, 3>, <3, 4>};"));
		assertTrue(runTest("{<X,Y> | int X <- [1,2,3], int Y <- [2,3,4], true} ==	{<1, 2>, <1, 3>, <1, 4>, <2, 2>, <2, 3>, <2, 4>, <3, 2>, <3, 3>, <3, 4>};"));
		
		
		assertTrue(runTest("{<X,Y> | int X <- {1,2,3}, int Y <- {2,3,4}, false} == {};"));
		assertTrue(runTest("{<X,Y> | int X <- [1,2,3], int Y <- [2,3,4], false} == {};"));
		
		assertTrue(runTest("{<X,Y> | int X <- {1,2,3}, int Y <- {2,3,4}, X >= Y} =={<2, 2>, <3, 2>, <3, 3>};"));
		assertTrue(runTest("{<X,Y> | int X <- [1,2,3], int Y <- [2,3,4], X >= Y} =={<2, 2>, <3, 2>, <3, 3>};"));
		
		assertTrue(runTest("{<X,Y> | int X <- {1,2,3}, <X, int Y> <- {<1,10>, <7,70>, <3,30>,<5,50>}} == {<1, 10>, <3, 30>};"));
		assertTrue(runTest("{<X,Y> | int X <- [1,2,3], <X, int Y> <- [<1,10>, <7,70>, <3,30>,<5,50>]} == {<1, 10>, <3, 30>};"));
		
		assertTrue(runTest("{<X,Y> | int X <- {1,2,3}, <X, str Y> <- {<1,\"a\">, <7,\"b\">, <3,\"c\">,<5,\"d\">}} == {<1, \"a\">, <3, \"c\">};"));
		assertTrue(runTest("{<X,Y> | int X <- [1,2,3], <X, str Y> <- [<1,\"a\">, <7,\"b\">, <3,\"c\">,<5,\"d\">]} == {<1, \"a\">, <3, \"c\">};"));
		
		}
	
	@Test
	public void emptyMapGeneratorError1(){
		assertTrue(runTest("( X : 2 * X | int X <- {} ) == ();"));
	}
	
	@Test
	public void emptyMapGeneratorError2(){
		assertTrue(runTest("( X : 2 * X | int X <- [] ) == ();"));
	}
	
	@Test public void mapComprehension()  {
		
		assertTrue(runTest("( X : 2 * X | int X <- {1}) == (1:2);"));
		assertTrue(runTest("( X : 2 * X | int X <- [1]) == (1:2);"));
		
		assertTrue(runTest("( X : 2 * X | int X <- {1, 2}) == (1:2,2:4);"));
		assertTrue(runTest("( X : 2 * X | int X <- [1, 2]) == (1:2,2:4);"));
		
		assertTrue(runTest("( X: 2 * X| int X<- [1,2,3] ) == (1:2,2:4,3:6);"));
	}
	
	@Test
	public void mapComprehensionNested() {
		assertTrue(runTest("( X: (2 * X + y : y | int y <- [1..X+1]) | int X <- [1,2,3] ) == (1:(3:1),2:(5:1,6:2),3:(7:1,8:2,9:3));"));
		assertTrue(runTest("( X: (2 * X + y : y | int y <- [1..X+1], X < 2) | int X <- [1,2,3] ) == (1:(3:1), 2:(), 3:());"));
		assertTrue(runTest("( X: (2 * X + y : y | int y <- [1..X+1], X > 2) | int X <- [1,2,3] ) == (1:(),2:(),3:(7:1,8:2,9:3));"));
	}
	
	@Test public void nodeGenerator()  {
		prepare("data TREE = i(int N) | f(TREE a,TREE b) | g(TREE a, TREE b);");
		
		assertTrue(runTestInSameEvaluator("[ X | /int X <- f(i(1),g(i(2),i(3))) ] == [1,2,3];"));
		
		assertTrue(runTestInSameEvaluator("[ X | /value X <- f(i(1),g(i(2),i(3))) ] == [1,i(1),2,i(2),3,i(3),g(i(2),i(3))];"));
		assertTrue(runTestInSameEvaluator("[ X | value X <- f(i(1),g(i(2),i(3))) ] == [i(1),g(i(2),i(3))];"));

		assertTrue(runTestInSameEvaluator("[N | /value N <- f(i(1),i(2))] == [1,i(1),2,i(2)];"));
		assertTrue(runTestInSameEvaluator("[N | value N <- f(i(1),i(2))] == [i(1), i(2)];"));
		
		assertTrue(runTestInSameEvaluator("[N | /TREE N <- f(i(1),i(2))] == [i(1),i(2)];"));
		assertTrue(runTestInSameEvaluator("[N | TREE N <- f(i(1),i(2))] == [i(1),i(2)];"));
		
		assertTrue(runTestInSameEvaluator("[N | /int N <- f(i(1),i(2))] == [1,2];"));
		
		assertTrue(runTestInSameEvaluator("[N | /value N <- f(i(1),g(i(2),i(3)))] == [1,i(1),2,i(2),3,i(3),g(i(2),i(3))];"));
		assertTrue(runTestInSameEvaluator("[N | value N <- f(i(1),g(i(2),i(3)))] == [i(1),g(i(2),i(3))];"));
		
		assertTrue(runTestInSameEvaluator("[N | /TREE N <- f(i(1),g(i(2),i(3)))] == [i(1),i(2),i(3),g(i(2),i(3))];"));
		assertTrue(runTestInSameEvaluator("[N | TREE N <- f(i(1),g(i(2),i(3)))] == [i(1),g(i(2),i(3))];"));
		
		assertTrue(runTestInSameEvaluator("[N | /int N <- f(i(1),g(i(2),i(3)))] == [1,2,3];"));
	}
	
	@Test(expected=StaticError.class)
	public void nodeGeneratorTypeError(){
		prepare("data TREE = i(int N) | f(TREE a,TREE b) | g(TREE a, TREE b);");
		assertTrue(runTestInSameEvaluator("[N | int N <- f(i(1),g(i(2),i(3)))] == [];"));
	}
	
	@Test public void regularGenerators() {
		
		assertTrue(runTest("[S | /@<S:[a-z]+>@/ <- [\"@abc@\", \"@def@\"]] == [\"abc\",\"def\"];"));
		assertTrue(runTest("{S | /@<S:[a-z]+>@/ <- [\"@abc@\", \"@def@\"]} == {\"abc\", \"def\"};"));
		assertTrue(runTest("{S | /@<S:[a-z]+>@/ <- {\"@abc@\", \"@def@\"}} == {\"abc\", \"def\"};"));
	}
	
	@Test(expected=UndeclaredVariable.class)
	public void NoLeakFromNextGenerator1(){
		assertTrue(runTest("[<N,M> | int N <- [1 .. 3], ((N==1) ? true : M > 0), int M <- [10 .. 12]] == [<1,10>,<1,11>,<2,10><2,11>];"));
	}
	
	@Test(expected=UndeclaredVariable.class)
	public void NoLeakFromNextGenerator2(){
		assertTrue(runTest("[<N,M> | int N <- [1 .. 3], ((N==1) ? true : M > 0), int M := N] == [<1,1>,<2,2>];"));
	}
}
