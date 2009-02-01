package test;

import junit.framework.TestCase;
import java.io.IOException;

public class PatternTests extends TestCase {
	
	private static TestFramework tf = new TestFramework();
	
	public void testMatchList1() throws IOException {
		
		tf = new TestFramework();
		
		assertTrue(tf.runTest("[1] := [1];"));
		assertTrue(tf.runTest("[1,2] := [1,2];"));
		
		assertFalse(tf.runTest("[1] := [2];"));
		assertFalse(tf.runTest("[1,2] := [1,2, 3];"));
		
		assertTrue(tf.runTest("([int n] := [1]) && (n == 1);"));
		assertTrue(tf.runTest("([int n, 2, int m] := [1,2,3]) && (n == 1) && (m==3);"));
		
		assertTrue(tf.runTest("[1, [2, 3], 4] := [1, [2, 3], 4];"));
		assertFalse(tf.runTest("[1, [2, 3], 4] := [1, [2, 3, 4], 4];"));
		
		assertTrue(tf.runTest("([list[int] L] := []) && (L == []);"));
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
		
		//assertTrue(tf.runTest("([1, list[int] L, [10, list[int] M, 100], list[int] N, 1000] := [1, [10,100],1000]);"));
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
	
	public void testMatchSet() throws IOException {
		
		tf = new TestFramework();
		
		assertTrue(tf.runTest("{} := {};"));
		assertTrue(tf.runTest("{1} := {1};"));
		assertTrue(tf.runTest("{1, 2} := {1, 2};"));
		
		assertFalse(tf.runTest("{} := {1};"));
		assertFalse(tf.runTest("{1} := {2};"));
		assertFalse(tf.runTest("{1,2} := {1,3};"));
		
		assertTrue(tf.runTest("{ {set[int] X} := {}; X == {};}"));
		assertTrue(tf.runTest("{ {set[int] X} := {1}; X == {1};}"));
		assertTrue(tf.runTest("{ {set[int] X} := {1,2}; X == {1,2};}"));
		
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
	}
	
	public void testMatchTuple() throws IOException {
		
		tf = new TestFramework();
		
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
