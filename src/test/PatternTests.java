package test;

import junit.framework.TestCase;
import java.io.IOException;

public class PatternTests extends TestCase {

	private static TestFramework tf = new TestFramework();

	public void testMatchList1() throws IOException {

		tf = new TestFramework();
		
		assertFalse(tf.runTest("[1] := \"a\";"));

		assertTrue(tf.runTest("[] := [];"));
		assertTrue(tf.runTest("[1] := [1];"));
		assertTrue(tf.runTest("[1,2] := [1,2];"));

		assertFalse(tf.runTest("[1] := [2];"));
		assertFalse(tf.runTest("[1,2] := [1,2, 3];"));

		assertTrue(tf.runTest("([int N] := [1]) && (N == 1);"));
		
		assertTrue(tf.runTest("([int N, 2, int M] := [1,2,3]) && (N == 1) && (M==3);"));
		
		assertTrue(tf.runTest("([int N, 2, N] := [1,2,1]) && (N == 1);"));
		
		assertFalse(tf.runTest("([int N, 2, N] := [1,2,3]);"));
		assertFalse(tf.runTest("([int N, 2, N] := [1,2,\"a\"]);"));
		
		assertTrue(tf.runTest("{int N = 1; ([N, 2, int M] := [1,2,3]) && (N == 1) && (M==3);}"));
		assertFalse(tf.runTest("{int N = 1; ([N, 2, int M] := [4,2,3]);}"));
		
		assertTrue(tf.runTest("{list[int] L = [3]; [1,2,L] := [1,2,3];}"));
		assertTrue(tf.runTest("{list[int] L = [2, 3]; [1, L] := [1,2,3];}"));

		assertTrue(tf.runTest("[1, [2, 3], 4] := [1, [2, 3], 4];"));
		assertFalse(tf.runTest("[1, [2, 3], 4] := [1, [2, 3, 4], 4];"));


		assertTrue(tf.runTest("([list[int] L] := []) && (L == []);"));
		assertTrue(tf.runTest("{ list[int] X = []; ([list[int] L] := X) && (L == []); }"));
		assertTrue(tf.runTest("([list[int] L] := ([1] - [1])) && (L == []);"));
		assertTrue(tf.runTest("([list[int] L] := [1]) && (L == [1]);"));
		assertTrue(tf.runTest("([list[int] L] := [1,2]) && (L == [1,2]);"));

		assertTrue(tf.runTest("([1, list[int] L] := [1]) && (L == []);"));
		assertTrue(tf.runTest("([1, list[int] L] := [1, 2]) && (L == [2]);"));
		assertTrue(tf.runTest("([1, list[int] L] := [1, 2, 3]) && (L == [2, 3]);"));

		assertTrue(tf.runTest("([list[int] L, 10] := [10]) && (L == []);"));
		assertTrue(tf.runTest("([list[int] L, 10] := [1,10]) && (L == [1]);"));
		assertTrue(tf.runTest("([list[int] L, 10] := [1,2,10]) && (L == [1,2]);"));


		assertTrue(tf.runTest("([1, list[int] L, 10] := [1,10]) && (L == []);"));
		assertTrue(tf.runTest("([1, list[int] L, 10] := [1,2,10]) && (L == [2]);"));
		assertTrue(tf.runTest("([1, list[int] L, 10, list[int] M, 20] := [1,10,20]) && (L == []) && (M == []);"));
		assertTrue(tf.runTest("([1, list[int] L, 10, list[int] M, 20] := [1,2,10,20]) && (L == [2]) && (M == []);"));
		assertTrue(tf.runTest("([1, list[int] L, 10, list[int] M, 20] := [1,2,10,3,20]) && (L == [2]) && (M==[3]);"));
		assertTrue(tf.runTest("([1, list[int] L, 10, list[int] M, 20] := [1,2,3,10,4,5,20]) && (L == [2,3]) && (M==[4,5]);"));
		
		assertTrue(tf.runTest("([1, list[int] L, 10, L, 20] := [1,2,3,10,2,3,20]) && (L == [2,3]);"));
		assertFalse(tf.runTest("([1, list[int] L, 10, L, 20] := [1,2,3,10,2,4,20]);"));	
		
//		assertTrue(tf.runTest("([1, list[int] L, [10, list[int] M, 100], list[int] N, 1000] := [1, [10,100],1000]);"));
	}

	public void testMatchList2() throws IOException {
		tf = new TestFramework("import ListMatchingTests;");

		assertTrue(tf.runTestInSameEvaluator("hasOrderedElement([]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("hasOrderedElement([1]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("hasOrderedElement([1,2]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("hasOrderedElement([1,2,1]) == true;"));
		assertTrue(tf.runTestInSameEvaluator("hasOrderedElement([1,2,3,4,3,2,1]) == true;"));

		assertTrue(tf.runTestInSameEvaluator("hasDuplicateElement([]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("hasDuplicateElement([1]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("hasDuplicateElement([1,2]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("hasDuplicateElement([1,1]) == true;"));
		assertTrue(tf.runTestInSameEvaluator("hasDuplicateElement([1,2,3]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("hasDuplicateElement([1,2,3,1]) == true;"));
		assertTrue(tf.runTestInSameEvaluator("hasDuplicateElement([1,2,3,2]) == true;"));
		assertTrue(tf.runTestInSameEvaluator("hasDuplicateElement([1,2,3,3]) == true;"));

		assertTrue(tf.runTestInSameEvaluator("isDuo1([]) == true;"));
		assertTrue(tf.runTestInSameEvaluator("isDuo1([1]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isDuo1([1,1]) == true;"));
		assertTrue(tf.runTestInSameEvaluator("isDuo1([1,2]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isDuo1([1,2, 1]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isDuo1([1,2, 1,2]) == true;"));
		assertTrue(tf.runTestInSameEvaluator("isDuo1([1,2,3, 1,2]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isDuo1([1,2,3, 1,2, 3]) == true;"));

		assertTrue(tf.runTestInSameEvaluator("isDuo2([]) == true;"));
		assertTrue(tf.runTestInSameEvaluator("isDuo2([1]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isDuo2([1,1]) == true;"));
		assertTrue(tf.runTestInSameEvaluator("isDuo2([1,2]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isDuo2([1,2, 1]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isDuo2([1,2, 1,2]) == true;"));
		assertTrue(tf.runTestInSameEvaluator("isDuo2([1,2,3, 1,2]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isDuo2([1,2,3, 1,2, 3]) == true;"));

		assertTrue(tf.runTestInSameEvaluator("isDuo3([]) == true;"));
		assertTrue(tf.runTestInSameEvaluator("isDuo3([1]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isDuo3([1,1]) == true;"));
		assertTrue(tf.runTestInSameEvaluator("isDuo3([1,2]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isDuo3([1,2, 1]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isDuo3([1,2, 1,2]) == true;"));
		assertTrue(tf.runTestInSameEvaluator("isDuo3([1,2,3, 1,2]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isDuo3([1,2,3, 1,2, 3]) == true;"));

		assertTrue(tf.runTestInSameEvaluator("isTrio1([]) == true;"));
		assertTrue(tf.runTestInSameEvaluator("isTrio1([1]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isTrio1([1,1]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isTrio1([1,1,1]) == true;"));
		assertTrue(tf.runTestInSameEvaluator("isTrio1([2,1,1]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isTrio1([1,2,1]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isTrio1([1,1,2]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isTrio1([1,2, 1,2, 1,2]) == true;"));

		assertTrue(tf.runTestInSameEvaluator("isTrio2([]) == true;"));
		assertTrue(tf.runTestInSameEvaluator("isTrio2([1]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isTrio2([1,1]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isTrio2([1,1,1]) == true;"));
		assertTrue(tf.runTestInSameEvaluator("isTrio2([2,1,1]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isTrio2([1,2,1]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isTrio2([1,1,2]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isTrio2([1,2, 1,2, 1,2]) == true;"));

		assertTrue(tf.runTestInSameEvaluator("isTrio3([]) == true;"));
		assertTrue(tf.runTestInSameEvaluator("isTrio3([1]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isTrio3([1,1]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isTrio3([1,1,1]) == true;"));
		assertTrue(tf.runTestInSameEvaluator("isTrio3([2,1,1]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isTrio3([1,2,1]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isTrio3([1,1,2]) == false;"));
		assertTrue(tf.runTestInSameEvaluator("isTrio3([1,2, 1,2, 1,2]) == true;"));
	}

	public void testMatchList3() throws IOException {

		tf = new TestFramework("data DATA = a | b | c | d | e(int N) | f(list[DATA] S);");

		assertTrue(tf.runTestInSameEvaluator("[a, b] := [a, b];"));
		assertTrue(tf.runTestInSameEvaluator("([DATA X, b] := [a, b]) && (X == a);"));

		assertFalse(tf.runTestInSameEvaluator("([DATA X, DATA Y, c] := [a, b]);"));

		assertTrue(tf.runTestInSameEvaluator("([e(int X), b] := [e(3), b]) && (X == 3);"));
		assertTrue(tf.runTestInSameEvaluator("([e(int X)] := [e(3)]) && (X == 3);"));
		assertFalse(tf.runTestInSameEvaluator("([e(int X)] := [a]);"));

		assertTrue(tf.runTestInSameEvaluator("([a, f([a, b, DATA X])] := [a, f([a,b,c])]) && (X == c);"));

		assertTrue(tf.runTestInSameEvaluator("([a, f([a, b, DATA X]), list[DATA] Y] := [a, f([a,b,c]), b]) && (X == c && Y == [b]);"));
		assertTrue(tf.runTestInSameEvaluator("([DATA A, f([A, b, DATA X])] := [a, f([a,b,c])]) && (A == a);"));

		assertTrue(tf.runTestInSameEvaluator("([DATA A, f([A, b, list[DATA] SX]), SX] := [a, f([a,b,c]), c]) && (A == a) && (SX ==[c]);"));

		assertFalse(tf.runTestInSameEvaluator("([DATA A, f([A, b, list[DATA] SX]), SX] := [d, f([a,b,c]), a]);"));
		assertFalse(tf.runTestInSameEvaluator("([DATA A, f([A, b, list[DATA] SX]), SX] := [c, f([a,b,c]), d]);"));

	}
	
	public void testMatchList4() throws IOException {
		tf = new TestFramework();
		
		assertTrue(tf.runWithError("[1, list[int] L, 2, list[int] L] := [1,2,3];", "Double"));
		assertTrue(tf.runWithError("[1, list[str] L, 2] := [1,2,3];", "is incompatible"));
		assertTrue(tf.runWithError("[1, str S, 2] := [1,2,3];", "not allowed"));
		assertTrue(tf.runWithError("{str S = \"a\"; [1, S, 2] := [1,2,3];}", "not allowed"));
		assertTrue(tf.runWithError("{list[str] S = [\"a\"]; [1, S, 2] := [1,2,3];}", "not allowed"));
		assertTrue(tf.runWithError("{list[int] S; [1, S, 2] := [1,2,3];}", "Uninitialized"));
		
	}

	public void testMatchListSet() throws IOException {

		tf = new TestFramework("data DATA = a | b | c | d | e(int N) | f(list[DATA] S) | f(set[DATA] S);");

		assertTrue(tf.runTestInSameEvaluator("[a, b] := [a, b];"));
		assertTrue(tf.runTestInSameEvaluator("([DATA X, b] := [a, b]) && (X == a);"));

		assertFalse(tf.runTestInSameEvaluator("([DATA X, DATA Y, c] := [a, b]);"));

		assertTrue(tf.runTestInSameEvaluator("([e(int X), b] := [e(3), b]) && (X == 3);"));
		assertTrue(tf.runTestInSameEvaluator("([e(int X)] := [e(3)]) && (X == 3);"));
		assertFalse(tf.runTestInSameEvaluator("([e(int X)] := [a]);"));

		assertTrue(tf.runTestInSameEvaluator("([a, f({a, b, DATA X})] := [a, f({a,b,c})]) && (X == c);"));
		assertTrue(tf.runTestInSameEvaluator("({a, f([a, b, DATA X])} := {a, f([a,b,c])}) && (X == c);"));

		assertTrue(tf.runTestInSameEvaluator("([a, f({a, b, DATA X}), list[DATA] Y] := [a, f({a,b,c}), b]) && (X == c && Y == [b]);"));
		assertTrue(tf.runTestInSameEvaluator("({a, f([a, b, DATA X]), set[DATA] Y} := {a, f([a,b,c]), b}) && (X == c && Y == {b});"));

		assertTrue(tf.runTestInSameEvaluator("([DATA A, f({A, b, DATA X})] := [a, f({a,b,c})]) && (A == a);"));
		assertTrue(tf.runTestInSameEvaluator("({DATA A, f([A, b, DATA X])} := {a, f([a,b,c])}) && (A == a);"));

	}

	public void testMatchLiteral() throws IOException {

		tf = new TestFramework();

		assertTrue(tf.runTest("true     := true;"));
		assertFalse(tf.runTest("true    := false;"));
		assertTrue(tf.runTest("true     !:= false;"));
		assertFalse(tf.runTest("true    := 1;"));
		assertTrue(tf.runTest("true     !:= 1;"));
		assertFalse(tf.runTest("\"abc\" := true;"));
		assertTrue(tf.runTest("\"abc\"  !:= true;"));

		assertTrue(tf.runTest("1        := 1;"));
		assertFalse(tf.runTest("2       := 1;"));
		assertTrue(tf.runTest("2        !:= 1;"));
		assertFalse(tf.runTest("true    := 1;"));
		assertTrue(tf.runTest("true     !:= 1;"));
		assertFalse(tf.runTest("1.0     := 1;"));
		assertTrue(tf.runTest("1.0      !:= 1;"));
		assertFalse(tf.runTest("\"abc\" := 1;"));
		assertTrue(tf.runTest("\"abc\"  !:= 1;"));

		assertTrue(tf.runTest("1.5      := 1.5;"));
		assertFalse(tf.runTest("2.5     := 1.5;"));
		assertTrue(tf.runTest("2.5      !:= 1.5;"));
		assertFalse(tf.runTest("true    := 1.5;"));
		assertTrue(tf.runTest("true     !:= 1.5;"));
		assertFalse(tf.runTest("2       := 1.5;"));
		assertTrue(tf.runTest("2        !:= 1.5;"));
		assertFalse(tf.runTest("1.0     := 1.5;"));
		assertTrue(tf.runTest("1.0      !:= 1.5;"));
		assertFalse(tf.runTest("\"abc\" := 1.5;"));
		assertTrue(tf.runTest("\"abc\"  !:= 1.5;"));

		assertTrue(tf.runTest("\"abc\"  := \"abc\";"));
		assertFalse(tf.runTest("\"def\" := \"abc\";"));
		assertTrue(tf.runTest("\"def\"  !:= \"abc\";"));
		assertFalse(tf.runTest("true    := \"abc\";"));
		assertTrue(tf.runTest("true     !:= \"abc\";"));
		assertFalse(tf.runTest("1       := \"abc\";"));
		assertTrue(tf.runTest("1        !:= \"abc\";"));
		assertFalse(tf.runTest("1.5     := \"abc\";"));
		assertTrue(tf.runTest("1.5      !:= \"abc\";"));
	}

	public void testMatchNode() throws IOException {

		tf = new TestFramework("data F = f(int N) | f(int N, int M) | f(int N, value f, bool B) | g(str S);");
		
		assertFalse(tf.runTestInSameEvaluator("f(1)                   := \"a\";"));
		assertTrue(tf.runTestInSameEvaluator("f(1)                   := f(1);"));
		assertTrue(tf.runTestInSameEvaluator("f(1, g(\"abc\"), true) := f(1, g(\"abc\"), true);"));
		assertFalse(tf.runTestInSameEvaluator("1                     := f(1);"));
		assertTrue(tf.runTestInSameEvaluator("1                      !:= f(1);"));
		assertFalse(tf.runTestInSameEvaluator("1.5                   := f(1);"));
		assertTrue(tf.runTestInSameEvaluator("1.5                    !:= f(1);"));
		assertFalse(tf.runTestInSameEvaluator("\"abc\"               := f(1);"));
		assertTrue(tf.runTestInSameEvaluator("\"abc\"                !:= f(1);"));
		assertFalse(tf.runTestInSameEvaluator("g(1)                  := f(1);"));
		assertTrue(tf.runTestInSameEvaluator("g(1)                   !:= f(1);"));
		assertFalse(tf.runTestInSameEvaluator("f(1, 2)               := f(1);"));
		assertTrue(tf.runTestInSameEvaluator("f(1, 2)                !:= f(1);"));
	}

	public void testMatchSet1() throws IOException {

		tf = new TestFramework();
		
		assertFalse(tf.runTest("{1} := \"a\";"));

		assertTrue(tf.runTest("{} := {};"));
		assertTrue(tf.runTest("{1} := {1};"));
		assertTrue(tf.runTest("{1, 2} := {1, 2};"));

		assertFalse(tf.runTest("{} := {1};"));
		assertFalse(tf.runTest("{1} := {2};"));
		assertFalse(tf.runTest("{1,2} := {1,3};"));

		assertTrue(tf.runTest("{ {set[int] X} := {}; X == {};}"));
		assertTrue(tf.runTest("{ {set[int] X} := {1}; X == {1};}"));
		assertTrue(tf.runTest("{ {set[int] X} := {1,2}; X == {1,2};}"));
	
		assertTrue(tf.runTest("({int N, 2, N} := {1,2}) && (N == 1);"));
		
		assertFalse(tf.runTest("({int N, 2, N} := {1,2,3});"));
		assertFalse(tf.runTest("({int N, 2, N} := {1,2,\"a\"});"));
		
		assertTrue(tf.runTest("{int N = 3; {N, 2, 1} := {1,2,3};}"));
		assertTrue(tf.runTest("{set[int] S = {3}; {S, 2, 1} := {1,2,3};}"));
		assertTrue(tf.runTest("{set[int] S = {2, 3}; {S, 1} := {1,2,3};}"));

		assertTrue(tf.runTest("{ {1, set[int] X, 2} := {1,2}; X == {};}"));
		assertFalse(tf.runTest("{ {1, set[int] X, 3} := {1,2};}"));

		assertTrue(tf.runTest("{ {1, set[int] X, 2} := {1,2,3}; X == {3};}"));
		assertTrue(tf.runTest("{ {1, set[int] X, 2} := {1,2,3,4}; X == {3,4};}"));

		assertTrue(tf.runTest("{ {set[int] X, set[int] Y} := {}; X == {} && Y == {};}"));
		assertTrue(tf.runTest("{ {1, set[int] X, set[int] Y} := {1}; X == {} && Y == {};}"));
		assertTrue(tf.runTest("{ {set[int] X, 1, set[int] Y} := {1}; X == {} && Y == {};}"));
		assertTrue(tf.runTest("{ {set[int] X, set[int] Y, 1} := {1}; X == {} && Y == {};}"));

		assertFalse(tf.runTest("{ {set[int] X, set[int] Y, 1} := {2};}"));

		assertTrue(tf.runTest("{ {set[int] X, set[int] Y} := {1}; (X == {} && Y == {1}) || (X == {1} && Y == {});}"));

		assertTrue(tf.runTest("{ {set[int] X, set[int] Y, set[int] Z} := {}; X == {} && Y == {} && Z == {};}"));
		assertTrue(tf.runTest("{ {set[int] X, set[int] Y, set[int] Z} := {1}; (X == {1} && Y == {} && Z == {}) || (X == {} && Y == {1} && Z == {}) || (X == {} && Y == {} && Z == {1});}"));

		assertTrue(tf.runTest("{ {int X, set[int] Y} := {1}; X == 1 && Y == {};}"));
		assertTrue(tf.runTest("{ {set[int] X, int Y} := {1}; X == {} && Y == 1;}"));

		assertTrue(tf.runTest("{ {set[int] X, int Y} := {1, 2}; (X == {1} && Y == 2) || (X == {2} && Y == 1);}"));
		
		assertTrue(tf.runTest("{ {set[int] X, set[real] Y} := { 1, 5.5, 2, 6.5}; (X == {1,2} && Y == {5.5, 6.5});}"));
		
	}	

	public void testMatchSet2() throws IOException {

		tf = new TestFramework("data DATA = a | b | c | d | e(int N) | f(set[DATA] S);");

		assertTrue(tf.runTestInSameEvaluator("{a, b} := {a, b};"));
		assertTrue(tf.runTestInSameEvaluator("({DATA X, b} := {a, b}) && (X == a);"));

		assertFalse(tf.runTestInSameEvaluator("({DATA X, DATA Y, c} := {a, b});"));

		assertTrue(tf.runTestInSameEvaluator("({e(int X), b} := {e(3), b}) && (X == 3);"));
		assertTrue(tf.runTestInSameEvaluator("({e(int X)} := {e(3)}) && (X == 3);"));
		assertFalse(tf.runTestInSameEvaluator("({e(int X)} := {a});"));

		assertTrue(tf.runTestInSameEvaluator("({a, f({a, b, DATA X})} := {a, f({a,b,c})}) && (X == c);"));
		assertTrue(tf.runTestInSameEvaluator("({f({a, b, DATA X}), a} := {a, f({a,b,c})}) && (X == c);"));

		assertTrue(tf.runTestInSameEvaluator("({a, f({a, b, DATA X}), set[DATA] Y} := {a, b, f({a,b,c})}) && (X == c && Y == {b});"));
		assertTrue(tf.runTestInSameEvaluator("({DATA A, f({A, b, DATA X})} := {a, f({a,b,c})}) && (A == a);"));
		assertTrue(tf.runTestInSameEvaluator("({DATA A, f({A, b, DATA X})} := {f({a,b,c}), a}) && (A == a);"));

		assertTrue(tf.runTestInSameEvaluator("({DATA A, f({A, b, set[DATA] SX}), SX} := {a, f({a,b,c}), c}) && (A == a) && (SX =={c});"));
		assertTrue(tf.runTestInSameEvaluator("({DATA A, f({A, b, set[DATA] SX}), SX} := {f({a,b,c}), a, c}) && (A == a) && (SX =={c});"));
		assertTrue(tf.runTestInSameEvaluator("({DATA A, f({A, b, set[DATA] SX}), SX} := {c, f({a,b,c}), a}) && (A == a) && (SX =={c});"));

		assertFalse(tf.runTestInSameEvaluator("({DATA A, f({A, b, set[DATA] SX}), SX} := {d, f({a,b,c}), a});"));
		assertFalse(tf.runTestInSameEvaluator("({DATA A, f({A, b, set[DATA] SX}), SX} := {c, f({a,b,c}), d});"));
	}	
	
	public void testMatchSet3() throws IOException {
		tf = new TestFramework();
		
		assertTrue(tf.runWithError("{1, set[int] L, 2, set[int] L} := {1,2,3};", "Double"));
		assertTrue(tf.runWithError("{1, \"a\", 2, set[int] L} := {1,2,3};", "not allowed"));
		
		assertTrue(tf.runWithError("{1, set[str] L, 2} := {1,2,3};", "not allowed"));
		assertTrue(tf.runWithError("{1, str S, 2} := {1,2,3};", "not allowed"));
		assertTrue(tf.runWithError("{set[str] S = {\"a\"}; {1, S, 2} := {1,2,3};}", "not allowed"));
		assertTrue(tf.runWithError("{set[int] S; {1, S, 2} := {1,2,3};}", "Uninitialized"));
		
	}

	public void testMatchTuple() throws IOException {

		tf = new TestFramework();

		assertFalse(tf.runTest("<1>           := \"a\";"));
		assertTrue(tf.runTest("<1>           := <1>;"));
		assertTrue(tf.runTest("<1, \"abc\">  := <1, \"abc\">;"));
		assertFalse(tf.runTest("<2>          := <1>;"));
		assertTrue(tf.runTest("<2>           !:= <1>;"));
		assertFalse(tf.runTest("<1,2>        := <1>;"));
		assertTrue(tf.runTest("<1,2>         !:= <1>;"));
		assertFalse(tf.runTest("<1, \"abc\"> := <1, \"def\">;"));
		assertTrue(tf.runTest("<1, \"abc\">  !:= <1, \"def\">;"));
	}

	public void testMatchVariable() throws IOException {

		tf = new TestFramework("data F = f(int N);");

		assertTrue(tf.runTestInSameEvaluator("(n := 1) && (n == 1);"));
		assertTrue(tf.runTestInSameEvaluator("{int n = 1; (n := 1) && (n == 1);}"));
		assertTrue(tf.runTestInSameEvaluator("{int n = 1; (n !:= 2) && (n == 1);}"));
		assertTrue(tf.runTestInSameEvaluator("{int n = 1; (n !:= \"abc\") && (n == 1);}"));

		assertTrue(tf.runTestInSameEvaluator("(f(n) := f(1)) && (n == 1);"));
		assertTrue(tf.runTestInSameEvaluator("{int n = 1; (f(n) := f(1)) && (n == 1);}"));
	}

}
