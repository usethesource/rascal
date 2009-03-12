package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.meta_environment.rascal.interpreter.exceptions.TypeErrorException;
import org.meta_environment.rascal.interpreter.exceptions.UninitializedVariableException;

public class PatternTests extends TestFramework {

	@Test
	public void matchList1() {
		
		assertFalse(runTest("[1] := \"a\";"));

		assertTrue(runTest("[] := [];"));
		assertTrue(runTest("[1] := [1];"));
		assertTrue(runTest("[1,2] := [1,2];"));

		assertFalse(runTest("[] := [2];"));
		assertFalse(runTest("[1] := [];"));
		assertFalse(runTest("[1] := [2];"));
		assertFalse(runTest("[1,2] := [1,2,3];"));

		assertTrue(runTest("([int N] := [1]) && (N == 1);"));
		assertTrue(runTest("[ _ ] := [1];"));
		
		assertTrue(runTest("([int N, 2, int M] := [1,2,3]) && (N == 1) && (M==3);"));
		assertTrue(runTest("[ _, 2, _] := [1,2,3];"));
		
		assertTrue(runTest("([int N, 2, N] := [1,2,1]) && (N == 1);"));
		
		assertFalse(runTest("([int N, 2, N] := [1,2,3]);"));
		assertFalse(runTest("([int N, 2, N] := [1,2,\"a\"]);"));
		
		assertTrue(runTest("{int N = 1; ([N, 2, int M] := [1,2,3]) && (N == 1) && (M==3);}"));
		assertFalse(runTest("{int N = 1; ([N, 2, int M] := [4,2,3]);}"));
		
		assertTrue(runTest("{list[int] L = [3]; [1,2,L] := [1,2,3];}"));
		assertTrue(runTest("{list[int] L = [2, 3]; [1, L] := [1,2,3];}"));

		assertTrue(runTest("[1, [2, 3], 4] := [1, [2, 3], 4];"));
		assertFalse(runTest("[1, [2, 3], 4] := [1, [2, 3, 4], 4];"));


		assertTrue(runTest("([list[int] L] := []) && (L == []);"));
		assertTrue(runTest("{ list[int] X = []; ([list[int] L] := X) && (L == []); }"));
		assertTrue(runTest("([list[int] L] := ([1] - [1])) && (L == []);"));
		assertTrue(runTest("([list[int] L] := [1]) && (L == [1]);"));
		assertTrue(runTest("([list[int] L] := [1,2]) && (L == [1,2]);"));

		assertTrue(runTest("([1, list[int] L] := [1]) && (L == []);"));
		assertTrue(runTest("([1, list[int] L] := [1, 2]) && (L == [2]);"));
		assertTrue(runTest("([1, list[int] L] := [1, 2, 3]) && (L == [2, 3]);"));

		assertTrue(runTest("([list[int] L, 10] := [10]) && (L == []);"));
		assertTrue(runTest("([list[int] L, 10] := [1,10]) && (L == [1]);"));
		assertTrue(runTest("([list[int] L, 10] := [1,2,10]) && (L == [1,2]);"));


		assertTrue(runTest("([1, list[int] L, 10] := [1,10]) && (L == []);"));
		assertTrue(runTest("([1, list[int] L, 10] := [1,2,10]) && (L == [2]);"));
		assertTrue(runTest("([1, list[int] L, 10, list[int] M, 20] := [1,10,20]) && (L == []) && (M == []);"));
		assertTrue(runTest("([1, list[int] L, 10, list[int] M, 20] := [1,2,10,20]) && (L == [2]) && (M == []);"));
		assertTrue(runTest("([1, list[int] L, 10, list[int] M, 20] := [1,2,10,3,20]) && (L == [2]) && (M==[3]);"));
		assertTrue(runTest("([1, list[int] L, 10, list[int] M, 20] := [1,2,3,10,4,5,20]) && (L == [2,3]) && (M==[4,5]);"));
		
		assertTrue(runTest("([1, list[int] L, 10, L, 20] := [1,2,3,10,2,3,20]) && (L == [2,3]);"));
		assertFalse(runTest("([1, list[int] L, 10, L, 20] := [1,2,3,10,2,4,20]);"));
		
		assertTrue(runTest("[list[int] _] := [];"));
		assertTrue(runTest("[list[int] _] := [1];"));
		assertTrue(runTest("[list[int] _] := [1,2];"));
		assertTrue(runTest("([1, list[int] _, 10, list[int] _, 20] := [1,2,10,20]);"));
		
//		assertTrue(runTest("([1, list[int] L, [10, list[int] M, 100], list[int] N, 1000] := [1, [10,100],1000]);"));
	}
	
	@Test
	public void matchList2()  {
		prepare("import ListMatchingTests;");

		assertTrue(runTestInSameEvaluator("hasOrderedElement([]) == false;"));
		assertTrue(runTestInSameEvaluator("hasOrderedElement([1]) == false;"));
		assertTrue(runTestInSameEvaluator("hasOrderedElement([1,2]) == false;"));
		assertTrue(runTestInSameEvaluator("hasOrderedElement([1,2,1]) == true;"));
		assertTrue(runTestInSameEvaluator("hasOrderedElement([1,2,3,4,3,2,1]) == true;"));

		assertTrue(runTestInSameEvaluator("hasDuplicateElement([]) == false;"));
		assertTrue(runTestInSameEvaluator("hasDuplicateElement([1]) == false;"));
		assertTrue(runTestInSameEvaluator("hasDuplicateElement([1,2]) == false;"));
		assertTrue(runTestInSameEvaluator("hasDuplicateElement([1,1]) == true;"));
		assertTrue(runTestInSameEvaluator("hasDuplicateElement([1,2,3]) == false;"));
		assertTrue(runTestInSameEvaluator("hasDuplicateElement([1,2,3,1]) == true;"));
		assertTrue(runTestInSameEvaluator("hasDuplicateElement([1,2,3,2]) == true;"));
		assertTrue(runTestInSameEvaluator("hasDuplicateElement([1,2,3,3]) == true;"));

		assertTrue(runTestInSameEvaluator("isDuo1([]) == true;"));
		assertTrue(runTestInSameEvaluator("isDuo1([1]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo1([1,1]) == true;"));
		assertTrue(runTestInSameEvaluator("isDuo1([1,2]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo1([1,2, 1]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo1([1,2, 1,2]) == true;"));
		assertTrue(runTestInSameEvaluator("isDuo1([1,2,3, 1,2]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo1([1,2,3, 1,2, 3]) == true;"));

		assertTrue(runTestInSameEvaluator("isDuo2([]) == true;"));
		assertTrue(runTestInSameEvaluator("isDuo2([1]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo2([1,1]) == true;"));
		assertTrue(runTestInSameEvaluator("isDuo2([1,2]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo2([1,2, 1]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo2([1,2, 1,2]) == true;"));
		assertTrue(runTestInSameEvaluator("isDuo2([1,2,3, 1,2]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo2([1,2,3, 1,2, 3]) == true;"));

		assertTrue(runTestInSameEvaluator("isDuo3([]) == true;"));
		assertTrue(runTestInSameEvaluator("isDuo3([1]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo3([1,1]) == true;"));
		assertTrue(runTestInSameEvaluator("isDuo3([1,2]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo3([1,2, 1]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo3([1,2, 1,2]) == true;"));
		assertTrue(runTestInSameEvaluator("isDuo3([1,2,3, 1,2]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo3([1,2,3, 1,2, 3]) == true;"));

		assertTrue(runTestInSameEvaluator("isTrio1([]) == true;"));
		assertTrue(runTestInSameEvaluator("isTrio1([1]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio1([1,1]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio1([1,1,1]) == true;"));
		assertTrue(runTestInSameEvaluator("isTrio1([2,1,1]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio1([1,2,1]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio1([1,1,2]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio1([1,2, 1,2, 1,2]) == true;"));

		assertTrue(runTestInSameEvaluator("isTrio2([]) == true;"));
		assertTrue(runTestInSameEvaluator("isTrio2([1]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio2([1,1]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio2([1,1,1]) == true;"));
		assertTrue(runTestInSameEvaluator("isTrio2([2,1,1]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio2([1,2,1]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio2([1,1,2]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio2([1,2, 1,2, 1,2]) == true;"));

		assertTrue(runTestInSameEvaluator("isTrio3([]) == true;"));
		assertTrue(runTestInSameEvaluator("isTrio3([1]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio3([1,1]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio3([1,1,1]) == true;"));
		assertTrue(runTestInSameEvaluator("isTrio3([2,1,1]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio3([1,2,1]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio3([1,1,2]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio3([1,2, 1,2, 1,2]) == true;"));
	}

	@Test
	public void matchList3()  {

		prepare("data DATA = a | b | c | d | e(int N) | f(list[DATA] S);");

		assertTrue(runTestInSameEvaluator("[a, b] := [a, b];"));
		assertTrue(runTestInSameEvaluator("([DATA X, b] := [a, b]) && (X == a);"));

		assertFalse(runTestInSameEvaluator("([DATA X, DATA Y, c] := [a, b]);"));

		assertTrue(runTestInSameEvaluator("([e(int X), b] := [e(3), b]) && (X == 3);"));
		assertTrue(runTestInSameEvaluator("([e(int X)] := [e(3)]) && (X == 3);"));
		assertFalse(runTestInSameEvaluator("([e(int X)] := [a]);"));

		assertTrue(runTestInSameEvaluator("([a, f([a, b, DATA X])] := [a, f([a,b,c])]) && (X == c);"));

		assertTrue(runTestInSameEvaluator("([a, f([a, b, DATA X]), list[DATA] Y] := [a, f([a,b,c]), b]) && (X == c && Y == [b]);"));
		assertTrue(runTestInSameEvaluator("([DATA A, f([A, b, DATA X])] := [a, f([a,b,c])]) && (A == a);"));

		assertTrue(runTestInSameEvaluator("([DATA A, f([A, b, list[DATA] SX]), SX] := [a, f([a,b,c]), c]) && (A == a) && (SX ==[c]);"));

		assertFalse(runTestInSameEvaluator("([DATA A, f([A, b, list[DATA] SX]), SX] := [d, f([a,b,c]), a]);"));
		assertFalse(runTestInSameEvaluator("([DATA A, f([A, b, list[DATA] SX]), SX] := [c, f([a,b,c]), d]);"));

	}
	
	@Test(expected=TypeErrorException.class)
	public void matchListError1() {
		runTest("[1, list[int] L, 2, list[int] L] := [1,2,3];");
	}
	
	@Test(expected=TypeErrorException.class)
	public void matchListError2() {
		runTest("[1, list[str] L, 2] := [1,2,3];");
	}
	
	@Test(expected=TypeErrorException.class)
	public void matchListError3() {
		runTest("[1, str S, 2] := [1,2,3];");
	}
	
	@Test(expected=TypeErrorException.class)
	public void matchListError4() {
		runTest("{str S = \"a\"; [1, S, 2] := [1,2,3];}");
	}
	
	@Test(expected=TypeErrorException.class)
	public void matchListError5() {
		runTest("{list[str] S = [\"a\"]; [1, S, 2] := [1,2,3];}");
	}
	
	@Test(expected=UninitializedVariableException.class)
	public void matchListError6() {
		runTest("{list[int] S; [1, S, 2] := [1,2,3];}");
	}

	@Test
	public void matchListSet() {

		prepare("data DATA = a | b | c | d | e(int N) | f(list[DATA] S) | f(set[DATA] S);");

		assertTrue(runTestInSameEvaluator("[a, b] := [a, b];"));
		assertTrue(runTestInSameEvaluator("([DATA X, b] := [a, b]) && (X == a);"));

		assertFalse(runTestInSameEvaluator("([DATA X, DATA Y, c] := [a, b]);"));

		assertTrue(runTestInSameEvaluator("([e(int X), b] := [e(3), b]) && (X == 3);"));
		assertTrue(runTestInSameEvaluator("([e(int X)] := [e(3)]) && (X == 3);"));
		assertFalse(runTestInSameEvaluator("([e(int X)] := [a]);"));

		assertTrue(runTestInSameEvaluator("([a, f({a, b, DATA X})] := [a, f({a,b,c})]) && (X == c);"));
		assertTrue(runTestInSameEvaluator("({a, f([a, b, DATA X])} := {a, f([a,b,c])}) && (X == c);"));

		assertTrue(runTestInSameEvaluator("([a, f({a, b, DATA X}), list[DATA] Y] := [a, f({a,b,c}), b]) && (X == c && Y == [b]);"));
		assertTrue(runTestInSameEvaluator("({a, f([a, b, DATA X]), set[DATA] Y} := {a, f([a,b,c]), b}) && (X == c && Y == {b});"));

		assertTrue(runTestInSameEvaluator("([DATA A, f({A, b, DATA X})] := [a, f({a,b,c})]) && (A == a);"));
		assertTrue(runTestInSameEvaluator("({DATA A, f([A, b, DATA X])} := {a, f([a,b,c])}) && (A == a);"));

	}

	@Test
	public void matchLiteral() {

		assertTrue(runTest("true     := true;"));
		assertFalse(runTest("true    := false;"));
		assertTrue(runTest("true     !:= false;"));
		assertFalse(runTest("true    := 1;"));
		assertTrue(runTest("true     !:= 1;"));
		assertFalse(runTest("\"abc\" := true;"));
		assertTrue(runTest("\"abc\"  !:= true;"));

		assertTrue(runTest("1        := 1;"));
		assertFalse(runTest("2       := 1;"));
		assertTrue(runTest("2        !:= 1;"));
		assertFalse(runTest("true    := 1;"));
		assertTrue(runTest("true     !:= 1;"));
		assertFalse(runTest("1.0     := 1;"));
		assertTrue(runTest("1.0      !:= 1;"));
		assertFalse(runTest("\"abc\" := 1;"));
		assertTrue(runTest("\"abc\"  !:= 1;"));

		assertTrue(runTest("1.5      := 1.5;"));
		assertFalse(runTest("2.5     := 1.5;"));
		assertTrue(runTest("2.5      !:= 1.5;"));
		assertFalse(runTest("true    := 1.5;"));
		assertTrue(runTest("true     !:= 1.5;"));
		assertFalse(runTest("2       := 1.5;"));
		assertTrue(runTest("2        !:= 1.5;"));
		assertFalse(runTest("1.0     := 1.5;"));
		assertTrue(runTest("1.0      !:= 1.5;"));
		assertFalse(runTest("\"abc\" := 1.5;"));
		assertTrue(runTest("\"abc\"  !:= 1.5;"));

		assertTrue(runTest("\"abc\"  := \"abc\";"));
		assertFalse(runTest("\"def\" := \"abc\";"));
		assertTrue(runTest("\"def\"  !:= \"abc\";"));
		assertFalse(runTest("true    := \"abc\";"));
		assertTrue(runTest("true     !:= \"abc\";"));
		assertFalse(runTest("1       := \"abc\";"));
		assertTrue(runTest("1        !:= \"abc\";"));
		assertFalse(runTest("1.5     := \"abc\";"));
		assertTrue(runTest("1.5      !:= \"abc\";"));
	}

	@Test
	public void matchNode() {

		prepare("data F = f(int N) | f(int N, int M) | f(int N, value f, bool B) | g(str S);");
		
		assertFalse(runTestInSameEvaluator("f(1)                   := \"a\";"));
		assertTrue(runTestInSameEvaluator("f(1)                   := f(1);"));
		assertTrue(runTestInSameEvaluator("f(1, g(\"abc\"), true) := f(1, g(\"abc\"), true);"));
		assertFalse(runTestInSameEvaluator("1                     := f(1);"));
		assertTrue(runTestInSameEvaluator("1                      !:= f(1);"));
		assertFalse(runTestInSameEvaluator("1.5                   := f(1);"));
		assertTrue(runTestInSameEvaluator("1.5                    !:= f(1);"));
		assertFalse(runTestInSameEvaluator("\"abc\"               := f(1);"));
		assertTrue(runTestInSameEvaluator("\"abc\"                !:= f(1);"));
		assertFalse(runTestInSameEvaluator("g(1)                  := f(1);"));
		assertTrue(runTestInSameEvaluator("g(1)                   !:= f(1);"));
		assertFalse(runTestInSameEvaluator("f(1, 2)               := f(1);"));
		assertTrue(runTestInSameEvaluator("f(1, 2)                !:= f(1);"));
		
		assertTrue(runTestInSameEvaluator("f(_)                   := f(1);"));
		assertTrue(runTestInSameEvaluator("f(_,_)                 := f(1,2);"));
		assertTrue(runTestInSameEvaluator("f(_,_,_)               := f(1,2.5,true);"));
	}
	
	@Ignore @Test(expected=TypeErrorException.class)
	public void NoDataDecl(){
		runTest("f(1) := 1;");
	}

	@Test
	public void matchSet1() {
		
		assertFalse(runTest("{1} := \"a\";"));

		assertTrue(runTest("{} := {};"));
		assertTrue(runTest("{1} := {1};"));
		assertTrue(runTest("{1, 2} := {1, 2};"));
		
		assertTrue(runTest("{int _} := {1};"));
		assertTrue(runTest("{int _, int _} := {1, 2};"));
		
		assertTrue(runTest("{_} := {1};"));
		assertTrue(runTest("{_, _} := {1, 2};"));
 
		assertFalse(runTest("{} := {1};"));
		assertFalse(runTest("{1} := {2};"));
		assertFalse(runTest("{1,2} := {1,3};"));

		assertTrue(runTest("{ {set[int] X} := {}; X == {};}"));
		assertTrue(runTest("{ {set[int] X} := {1}; X == {1};}"));
		assertTrue(runTest("{ {set[int] X} := {1,2}; X == {1,2};}"));
		
		assertTrue(runTest("{ {set[int] _} := {1,2}; }"));
	
		assertTrue(runTest("({int N, 2, N} := {1,2}) && (N == 1);"));
		
		assertFalse(runTest("({int N, 2, N} := {1,2,3});"));
		assertFalse(runTest("({int N, 2, N} := {1,2,\"a\"});"));
		
		assertTrue(runTest("{int N = 3; {N, 2, 1} := {1,2,3};}"));
		assertTrue(runTest("{set[int] S = {3}; {S, 2, 1} := {1,2,3};}"));
		assertTrue(runTest("{set[int] S = {2, 3}; {S, 1} := {1,2,3};}"));

		assertTrue(runTest("{ {1, set[int] X, 2} := {1,2}; X == {};}"));
		assertFalse(runTest("{ {1, set[int] X, 3} := {1,2};}"));

		assertTrue(runTest("{ {1, set[int] X, 2} := {1,2,3}; X == {3};}"));
		assertTrue(runTest("{ {1, set[int] X, 2} := {1,2,3,4}; X == {3,4};}"));

		assertTrue(runTest("{ {set[int] X, set[int] Y} := {}; X == {} && Y == {};}"));
		assertTrue(runTest("{ {1, set[int] X, set[int] Y} := {1}; X == {} && Y == {};}"));
		assertTrue(runTest("{ {set[int] X, 1, set[int] Y} := {1}; X == {} && Y == {};}"));
		assertTrue(runTest("{ {set[int] X, set[int] Y, 1} := {1}; X == {} && Y == {};}"));

		assertFalse(runTest("{ {set[int] X, set[int] Y, 1} := {2};}"));

		assertTrue(runTest("{ {set[int] X, set[int] Y} := {1}; (X == {} && Y == {1}) || (X == {1} && Y == {});}"));

		assertTrue(runTest("{ {set[int] X, set[int] Y, set[int] Z} := {}; X == {} && Y == {} && Z == {};}"));
		assertTrue(runTest("{ {set[int] X, set[int] Y, set[int] Z} := {1}; (X == {1} && Y == {} && Z == {}) || (X == {} && Y == {1} && Z == {}) || (X == {} && Y == {} && Z == {1});}"));

		assertTrue(runTest("{ {int X, set[int] Y} := {1}; X == 1 && Y == {};}"));
		assertTrue(runTest("{ {set[int] X, int Y} := {1}; X == {} && Y == 1;}"));
		assertTrue(runTest("{ {set[int] _, int _} := {1}; }"));
		assertTrue(runTest("{ {set[int] _, _} := {1}; }"));

		assertTrue(runTest("{ {set[int] X, int Y} := {1, 2}; (X == {1} && Y == 2) || (X == {2} && Y == 1);}"));
		
		assertTrue(runTest("{ {set[int] X, set[real] Y} := { 1, 5.5, 2, 6.5}; (X == {1,2} && Y == {5.5, 6.5});}"));
		
	}	

	@Test
	public void matchSet2() {

		prepare("data DATA = a | b | c | d | e(int N) | f(set[DATA] S);");

		assertTrue(runTestInSameEvaluator("{a, b} := {a, b};"));
		assertTrue(runTestInSameEvaluator("({DATA X, b} := {a, b}) && (X == a);"));

		assertFalse(runTestInSameEvaluator("({DATA X, DATA Y, c} := {a, b});"));

		assertTrue(runTestInSameEvaluator("({e(int X), b} := {e(3), b}) && (X == 3);"));
		assertTrue(runTestInSameEvaluator("({e(int X)} := {e(3)}) && (X == 3);"));
		assertFalse(runTestInSameEvaluator("({e(int X)} := {a});"));

		assertTrue(runTestInSameEvaluator("({a, f({a, b, DATA X})} := {a, f({a,b,c})}) && (X == c);"));
		assertTrue(runTestInSameEvaluator("({f({a, b, DATA X}), a} := {a, f({a,b,c})}) && (X == c);"));

		assertTrue(runTestInSameEvaluator("({a, f({a, b, DATA X}), set[DATA] Y} := {a, b, f({a,b,c})}) && (X == c && Y == {b});"));
		assertTrue(runTestInSameEvaluator("({DATA A, f({A, b, DATA X})} := {a, f({a,b,c})}) && (A == a);"));
		assertTrue(runTestInSameEvaluator("({DATA A, f({A, b, DATA X})} := {f({a,b,c}), a}) && (A == a);"));

		assertTrue(runTestInSameEvaluator("({DATA A, f({A, b, set[DATA] SX}), SX} := {a, f({a,b,c}), c}) && (A == a) && (SX =={c});"));
		assertTrue(runTestInSameEvaluator("({DATA A, f({A, b, set[DATA] SX}), SX} := {f({a,b,c}), a, c}) && (A == a) && (SX =={c});"));
		assertTrue(runTestInSameEvaluator("({DATA A, f({A, b, set[DATA] SX}), SX} := {c, f({a,b,c}), a}) && (A == a) && (SX =={c});"));

		assertFalse(runTestInSameEvaluator("({DATA A, f({A, b, set[DATA] SX}), SX} := {d, f({a,b,c}), a});"));
		assertFalse(runTestInSameEvaluator("({DATA A, f({A, b, set[DATA] SX}), SX} := {c, f({a,b,c}), d});"));
	}	
	
	
	
	@Test(expected=TypeErrorException.class)
	public void matchSetDoubleDeclError() {
		runTest("{1, set[int] L, 2, set[int] L} := {1,2,3};");
	}
	
	@Test(expected=TypeErrorException.class)
	public void matchSetWrongElemError() {
		runTest("{1, \"a\", 2, set[int] L} := {1,2,3};");
	}	
	
	@Test(expected=TypeErrorException.class)
	public void matchSetWrongElemError2() {
		runTest("{1, set[str] L, 2} := {1,2,3};");
	}
	
	@Test(expected=TypeErrorException.class)
	public void matchSetWrongElemError3() {
		runTest("{1, str S, 2} := {1,2,3};");
	}
	
	@Test(expected=TypeErrorException.class)
	public void matchSetWrongElemError4() {
		runTest("{set[str] S = {\"a\"}; {1, S, 2} := {1,2,3};}");
	}
	
	@Test(expected=UninitializedVariableException.class)
	public void matchSetWrongElemError5() {
		runTest("{set[int] S; {1, S, 2} := {1,2,3};}");
	}

	@Test
	public void matchTuple() {

		assertFalse(runTest("<1>           := \"a\";"));
		assertTrue(runTest("<1>           := <1>;"));
		assertTrue(runTest("<1, \"abc\">  := <1, \"abc\">;"));
		assertFalse(runTest("<2>          := <1>;"));
		assertTrue(runTest("<2>           !:= <1>;"));
		assertFalse(runTest("<1,2>        := <1>;"));
		assertTrue(runTest("<1,2>         !:= <1>;"));
		assertFalse(runTest("<1, \"abc\"> := <1, \"def\">;"));
		assertTrue(runTest("<1, \"abc\">  !:= <1, \"def\">;"));
		
		assertTrue(runTest("<_, \"abc\">  := <1, \"abc\">;"));
		assertTrue(runTest("<1, _>        := <1, \"abc\">;"));
		assertTrue(runTest("<_, _>        := <1, \"abc\">;"));
	}

	@Test
	public void matchVariable() {

		prepare("data F = f(int N);");

		assertTrue(runTestInSameEvaluator("(n := 1) && (n == 1);"));
		assertTrue(runTestInSameEvaluator("{int n = 1; (n := 1) && (n == 1);}"));
		assertTrue(runTestInSameEvaluator("{int n = 1; (n !:= 2) && (n == 1);}"));
		assertTrue(runTestInSameEvaluator("{int n = 1; (n !:= \"abc\") && (n == 1);}"));

		assertTrue(runTestInSameEvaluator("(f(n) := f(1)) && (n == 1);"));
		assertTrue(runTestInSameEvaluator("{int n = 1; (f(n) := f(1)) && (n == 1);}"));
		
		assertTrue(runTestInSameEvaluator("(f(_) := f(1));"));
	}
	
	@Test(expected=TypeErrorException.class)
	public void UndeclaredTypeError(){
		runTest("STRANGE X := 123;");
	}
}
