/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.test.library;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.test.infrastructure.TestFramework;


public class ListTests extends TestFramework {

	@Test
	public void delete() {
		prepare("import List;");
		
		assertTrue(runTestInSameEvaluator("{delete([0,1,2], 0) == [1,2];}"));
		assertTrue(runTestInSameEvaluator("{delete([0,1,2], 1) == [0,2];}"));
		assertTrue(runTestInSameEvaluator("{delete([0,1,2], 2) == [0,1];}"));
	}

// domain has been removed from the library 
//	@Test
//	public void domain() {
//
//		prepare("import List;");
//
//		assertTrue(runTestInSameEvaluator("{domain([]) == {};}"));
//		assertTrue(runTestInSameEvaluator("{domain([1]) == {0};}"));
//		assertTrue(runTestInSameEvaluator("{domain([1, 2]) == {0, 1};}"));
//	}

	@Test
	public void getOneFrom() {

		prepare("import List;");

		assertTrue(runTestInSameEvaluator("{int N = List::getOneFrom([1]); N == 1;}"));
		assertTrue(runTestInSameEvaluator("{int N = getOneFrom([1]); N == 1;}"));
		assertTrue(runTestInSameEvaluator("{int N = List::getOneFrom([1,2]); (N == 1) || (N == 2);}"));
		assertTrue(runTestInSameEvaluator("{int N = List::getOneFrom([1,2,3]); (N == 1) || (N == 2) || (N == 3);}"));
		assertTrue(runTestInSameEvaluator("{real D = List::getOneFrom([1.0,2.0]); (D == 1.0) || (D == 2.0);}"));
		assertTrue(runTestInSameEvaluator("{str S = List::getOneFrom([\"abc\",\"def\"]); (S == \"abc\") || (S == \"def\");}"));
	}
	

	@Test(expected=Throw.class)
	public void getOneFromError() {
		runTest("import List;", "getOneFrom([]);");
	}

	@Test
	public void head() {

		prepare("import List;");

		assertTrue(runTestInSameEvaluator("{List::head([1]) == 1;}"));
		assertTrue(runTestInSameEvaluator("{head([1]) == 1;}"));
		assertTrue(runTestInSameEvaluator("{List::head([1, 2]) == 1;}"));

		assertTrue(runTestInSameEvaluator("{head([1, 2, 3, 4], 0) == [];}"));
		assertTrue(runTestInSameEvaluator("{head([1, 2, 3, 4], 1) == [1];}"));
		assertTrue(runTestInSameEvaluator("{head([1, 2, 3, 4], 2) == [1,2];}"));
		assertTrue(runTestInSameEvaluator("{head([1, 2, 3, 4], 3) == [1,2,3];}"));
		assertTrue(runTestInSameEvaluator("{head([1, 2, 3, 4], 4) == [1,2,3,4];}"));
	}
	
	@Test(expected=Throw.class)
	public void headError1() {
		runTest("import List;", "head([]);");
	}
	
	@Test(expected=Throw.class)
	public void headError2() {
		runTest("import List;", "head([],3);");
	}
	
	@Test(expected=Throw.class)
	public void testHead2() {
		runTest("import List;", "head([1,2,3], 4);");
	}

	@Test
	public void insertAt() {

		prepare("import List;");

		assertTrue(runTestInSameEvaluator("List::insertAt([], 0, 1) == [1];"));
		assertTrue(runTestInSameEvaluator("insertAt([], 0, 1) == [1];"));
		assertTrue(runTestInSameEvaluator("List::insertAt([2,3], 1, 1) == [2,1, 3];"));
		assertTrue(runTestInSameEvaluator("insertAt([2,3], 1, 1) == [2, 1, 3];"));
		assertTrue(runTestInSameEvaluator("List::insertAt([2,3], 2, 1) == [2,3,1];"));
		assertTrue(runTestInSameEvaluator("insertAt([2,3], 2, 1) == [2, 3, 1];"));
	}
	

	@Test(expected=Throw.class)
	public void testInsertAt() {
		runTest("import List;", "insertAt([1,2,3], 4, 5);");
	}
	
	@Test
	public void isEmpty(){
		prepare("import List;");
		
		assertTrue(runTestInSameEvaluator("isEmpty([]);"));
		assertTrue(runTestInSameEvaluator("isEmpty([1,2]) == false;"));
	}

	@Test
	public void mapper() {

		prepare("import List;");

		assertTrue(runTestInSameEvaluator("{int inc(int n) {return n + 1;} mapper([1, 2, 3], inc) == [2, 3, 4];}"));
	}

	@Test
	public void max() {

		prepare("import List;");

		assertTrue(runTestInSameEvaluator("{List::max([1, 2, 3, 2, 1]) == 3;}"));
		assertTrue(runTestInSameEvaluator("{max([1, 2, 3, 2, 1]) == 3;}"));
	}

	@Test
	public void min() {

		prepare("import List;");

		assertTrue(runTestInSameEvaluator("{List::min([1, 2, 3, 2, 1]) == 1;}"));
		assertTrue(runTestInSameEvaluator("{min([1, 2, 3, 2, 1]) == 1;}"));
	}

	@Test
	public void permutations() {

		prepare("import List;");

		assertTrue(runTestInSameEvaluator("permutations([]) == {[]};"));
		assertTrue(runTestInSameEvaluator("permutations([1]) == {[1]};"));
		assertTrue(runTestInSameEvaluator("permutations([1,2]) == {[1,2],[2,1]};"));
		assertTrue(runTestInSameEvaluator("permutations([1,2,3]) ==  {[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]};"));

	}
	
	@Test
	public void distribution() {

		prepare("import List;");

		assertTrue(runTestInSameEvaluator("distribution([]) == ();"));
		assertTrue(runTestInSameEvaluator("distribution([1]) == (1:1);"));
		assertTrue(runTestInSameEvaluator("distribution([1,2]) == (1:1, 2:1);"));
		assertTrue(runTestInSameEvaluator("distribution([1,2, 2]) == (1:1, 2:2);"));
	}

	@Test
	public void reducer() {

		prepare("import List;");
		String add = "int add(int x, int y){return x + y;}";

		assertTrue(runTestInSameEvaluator("{" + add
				+ "reducer([1, 2, 3, 4], add, 0) == 10;}"));
	}

	@Test
	public void reverse() {

		prepare("import List;");

		assertTrue(runTestInSameEvaluator("{List::reverse([]) == [];}"));
		assertTrue(runTestInSameEvaluator("{reverse([]) == [];}"));
		assertTrue(runTestInSameEvaluator("{List::reverse([1]) == [1];}"));
		assertTrue(runTestInSameEvaluator("{List::reverse([1,2,3]) == [3,2,1];}"));
	}

	@Test
	public void size() {

		prepare("import List;");

		assertTrue(runTestInSameEvaluator("{List::size([]) == 0;}"));
		assertTrue(runTestInSameEvaluator("{size([]) == 0;}"));
		assertTrue(runTestInSameEvaluator("{List::size([1]) == 1;}"));
		assertTrue(runTestInSameEvaluator("{List::size([1,2,3]) == 3;}"));
	}

	@Test
	public void slice() {

		prepare("import List;");

		assertTrue(runTestInSameEvaluator("{slice([1,2,3,4], 0, 0) == [];}"));
		assertTrue(runTestInSameEvaluator("{slice([1,2,3,4], 0, 1) == [1];}"));
		assertTrue(runTestInSameEvaluator("{slice([1,2,3,4], 0, 2) == [1,2];}"));
		assertTrue(runTestInSameEvaluator("{slice([1,2,3,4], 0, 3) == [1,2,3];}"));
		assertTrue(runTestInSameEvaluator("{slice([1,2,3,4], 0, 4) == [1,2,3,4];}"));
		assertTrue(runTestInSameEvaluator("{slice([1,2,3,4], 1, 0) == [];}"));
		assertTrue(runTestInSameEvaluator("{slice([1,2,3,4], 1, 1) == [2];}"));
		assertTrue(runTestInSameEvaluator("{slice([1,2,3,4], 1, 2) == [2,3];}"));
		assertTrue(runTestInSameEvaluator("{slice([1,2,3,4], 3, 0) == [];}"));
		assertTrue(runTestInSameEvaluator("{slice([1,2,3,4], 3, 1) == [4];}"));
	}

	@Test
	public void sort() {

		prepare("import List;");

		assertTrue(runTestInSameEvaluator("{List::sort([]) == [];}"));
		assertTrue(runTestInSameEvaluator("{sort([]) == [];}"));
		assertTrue(runTestInSameEvaluator("{List::sort([1]) == [1];}"));
		assertTrue(runTestInSameEvaluator("{sort([1]) == [1];}"));
		assertTrue(runTestInSameEvaluator("{List::sort([2, 1]) == [1,2];}"));
		assertTrue(runTestInSameEvaluator("{sort([2, 1]) == [1,2];}"));
		assertTrue(runTestInSameEvaluator("{List::sort([2,-1,4,-2,3]) == [-2,-1,2,3, 4];}"));
		assertTrue(runTestInSameEvaluator("{sort([2,-1,4,-2,3]) == [-2,-1,2,3, 4];}"));
		assertTrue(runTestInSameEvaluator("{sort([1,2,3,4,5,6]) == [1,2,3,4,5,6];}"));
		assertTrue(runTestInSameEvaluator("{sort([1,1,1,1,1,1]) == [1,1,1,1,1,1];}"));
		assertTrue(runTestInSameEvaluator("{sort([1,1,0,1,1]) == [0,1,1,1,1];}"));
	}
	
	@Test
	public void sum() {

		prepare("import List;");

		assertTrue(runTestInSameEvaluator("{sum([]) == 0;}"));
		assertTrue(runTestInSameEvaluator("{sum([1]) == 1;}"));
		assertTrue(runTestInSameEvaluator("{sum([1,2]) == 3;}"));
		assertTrue(runTestInSameEvaluator("{sum([1,2,3]) == 6;}"));
	}
	
	@Test
	public void sortWithCompareFunction() {
		prepare("import List;");
		prepareMore("import Exception;");
		assertTrue(runTestInSameEvaluator("{sort([1, 2, 3]) == [1,2,3];}"));
		assertTrue(runTestInSameEvaluator("{sort([1, 2, 3], bool(int a, int b){return a < b;}) == [1,2,3];}"));
		assertTrue(runTestInSameEvaluator("{sort([1, 2, 3], bool(int a, int b){return a > b;}) == [3,2,1];}"));
		assertTrue(runTestInSameEvaluator("{try { sort([1, 2, 3], bool(int a, int b){return a <= b;}); throw \"Should fail\"; } catch IllegalArgument(_,_): ;}"));
		assertTrue(runTestInSameEvaluator("{try { sort([1, 0, 1], bool(int a, int b){return a <= b;}); throw \"Should fail\"; } catch IllegalArgument(_,_): ;}"));
	}

	@Test
	public void tail() {

		prepare("import List;");

		assertTrue(runTestInSameEvaluator("{List::tail([1]) == [];}"));
		assertTrue(runTestInSameEvaluator("{tail([1]) == [];}"));
		assertTrue(runTestInSameEvaluator("{List::tail([1, 2]) == [2];}"));
		assertTrue(runTestInSameEvaluator("{tail([1, 2, 3]) + [4, 5, 6]  == [2, 3, 4, 5, 6];}"));
		assertTrue(runTestInSameEvaluator("{tail([1, 2, 3]) + tail([4, 5, 6])  == [2, 3, 5, 6];}"));

		assertTrue(runTestInSameEvaluator("{tail([1, 2, 3], 2) == [2,3];}"));
		assertTrue(runTestInSameEvaluator("{tail([1, 2, 3], 0) == [];}"));
		
		assertTrue(runTestInSameEvaluator("{tail(tail([1, 2])) == tail([3]);}"));
		
		assertTrue(runTestInSameEvaluator("{L = [1,2]; tail(tail(L)) == tail(tail(L));}"));
		assertTrue(runTestInSameEvaluator("{L1 = [1,2,3]; L2 = [2,3]; tail(tail(L1)) == tail(L2);}"));
		assertTrue(runTestInSameEvaluator("{L1 = [1,2]; L2 = [3]; tail(tail(L1)) == tail(L2);}"));
		
		assertTrue(runTestInSameEvaluator("{L1 = [1,2]; L2 = [3]; {tail(tail(L1)), tail(L2)} == {[]};}"));
		
	}
	

	@Test(expected=Throw.class)
	public void tailError1() {
		runTest("import List;", "tail([]);");
	}
	
	@Test(expected=Throw.class)
	public void tailError2() {
		runTest("import List;",  "tail([1,2,3], 4);");
	}

	@Test
	public void takeOneFrom() {

		prepare("import List;");

		assertTrue(runTestInSameEvaluator("{<E, L> = takeOneFrom([1]); (E == 1) && (L == []);}"));
		assertTrue(runTestInSameEvaluator("{<E, L> = List::takeOneFrom([1,2]); ((E == 1) && (L == [2])) || ((E == 2) && (L == [1]));}"));
	}
	

	@Test(expected=Throw.class)
	public void takeOneFromError() {
		runTest("import List;", "takeOneFrom([]);");
	}

	@Test
	public void toMapUnique() {

		prepare("import List;");

		assertTrue(runTestInSameEvaluator("{List::toMapUnique([]) == ();}"));
		assertTrue(runTestInSameEvaluator("{toMapUnique([]) == ();}"));
		assertTrue(runTestInSameEvaluator("{List::toMapUnique([<1,10>, <2,20>]) == (1:10, 2:20);}"));
	}
	
	@Test(expected=Throw.class)
	public void toMapUniqueError(){
		prepare("import List;");
		assertTrue(runTestInSameEvaluator("{List::toMapUnique([<1,10>, <1,20>]) == (1:10, 2:20);}"));
	}
	
	@Test
	public void toMap() {

		prepare("import List;");

		assertTrue(runTestInSameEvaluator("{List::toMap([]) == ();}"));
		assertTrue(runTestInSameEvaluator("{toMap([]) == ();}"));
		assertTrue(runTestInSameEvaluator("{List::toMap([<1,10>, <2,20>]) == (1:{10}, 2:{20});}"));
		assertTrue(runTestInSameEvaluator("{List::toMap([<1,10>, <2,20>, <1,30>]) == (1:{10,30}, 2:{20});}"));
	}

	@Test
	public void toSet() {

		prepare("import List;");

		assertTrue(runTestInSameEvaluator("{List::toSet([]) == {};}"));
		assertTrue(runTestInSameEvaluator("{toSet([]) == {};}"));
		assertTrue(runTestInSameEvaluator("{List::toSet([1]) == {1};}"));
		assertTrue(runTestInSameEvaluator("{toSet([1]) == {1};}"));
		assertTrue(runTestInSameEvaluator("{List::toSet([1, 2, 1]) == {1, 2};}"));
	}

	@Test
	public void testToString() {

		prepare("import List;");

		assertTrue(runTestInSameEvaluator("{List::toString([]) == \"[]\";}"));
		assertTrue(runTestInSameEvaluator("{toString([]) == \"[]\";}"));
		assertTrue(runTestInSameEvaluator("{List::toString([1]) == \"[1]\";}"));
		assertTrue(runTestInSameEvaluator("{List::toString([1, 2]) == \"[1,2]\";}"));
	}
	
	@Test(expected = UnexpectedType.class) 
	public void listExpressions1() {
		runTest("{ value n = 1; list[int] l = [ *[n, n] ]; }");
	}
	
	@Test(expected = UnexpectedType.class) 
	public void listExpressions2() {
		runTest("{ value n = 1; list[int] l = [ 1, *[n, n], 2 ]; }");
	}
	
	@Test
	public void listExpressions3() {
		assertTrue(runTest("{ value n = 1; value s = \"string\"; list[int] _ := [ n ] && list[str] _ := [ s, s, *[ s, s ] ]; }"));
	}
	
	// Tests related to the correctness of the dynamic types of lists produced by the library functions;
	// incorrect dynamic types make pattern matching fail;

	@Test
	public void testDynamicTypes() {
		prepare("import List;");
		assertTrue(runTestInSameEvaluator("{ list[value] lst = [\"1\",2,3]; list[int] _ := slice(lst, 1, 2); }"));
		assertTrue(runTestInSameEvaluator("{ list[value] lst = [\"1\",2,3]; list[int] _ := lst - \"1\"; }"));
		assertTrue(runTestInSameEvaluator("{ list[value] lst = [\"1\",2,3]; list[int] _ := lst - [\"1\"]; }"));
		assertTrue(runTestInSameEvaluator("{ list[value] lst = [\"1\",2,3]; list[int] _ := delete(lst, 0); }"));
		assertTrue(runTestInSameEvaluator("{ list[value] lst = [\"1\",2,3]; list[int] _ := drop(1, lst); }"));
		assertTrue(runTestInSameEvaluator("{ list[value] lst = [1,2,\"3\"]; list[int] _ := head(lst, 2); }"));
		assertTrue(runTestInSameEvaluator("{ list[value] lst = [1,2,\"3\"]; list[int] _ := prefix(lst); }"));
		assertTrue(runTestInSameEvaluator("{ list[value] lst = [\"1\",2,3]; list[int] _ := tail(lst); }"));
		assertTrue(runTestInSameEvaluator("{ list[value] lst = [1,2,\"3\"]; list[int] _ := take(2, lst); }"));
		
		assertTrue(runTestInSameEvaluator("{ [str _, *int _] := [\"1\",2,3]; }"));
	}
	
}
