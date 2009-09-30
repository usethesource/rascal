package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredVariableError;
import org.meta_environment.rascal.interpreter.staticErrors.StaticError;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredVariableError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;

public class PatternTests extends TestFramework {

	@Test(expected=StaticError.class)
	public void cannotMatchListStr(){
		assertFalse(runTest("[1] := \"a\";"));
	}
	
	@Test
	public void matchList1() {
		
		assertFalse(runTest("[] := [2];"));
		assertFalse(runTest("[1] := [];"));

		assertTrue(runTest("[] := [];"));
		assertTrue(runTest("[1] := [1];"));
		assertTrue(runTest("[1,2] := [1,2];"));
		
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
		
	//	assertTrue(runTest("([1, list[int] L, [10, list[int] M, 100], list[int] N, 1000] := [1, [10,100],1000]);"));
	}
	
	@Test
	public void matchExternalListVars(){
		assertTrue(runTest("{int n;  n := 3 && n == 3; }"));
		assertTrue(runTest("{list[int] L; ([1, L, 4, 5] := [1, 2, 3, 4, 5] && L == [2, 3]);}"));
	}
	
	@Test
	public void matchListMultiVars(){
		assertTrue(runTest("{[1, L*, 4, 5] := [1, 2, 3, 4, 5] && L == [2, 3];}"));
		assertTrue(runTest("{[1, _*, 4, 5] := [1, 2, 3, 4, 5];}"));
		assertTrue(runTest("{[1, L*, 4, L, 5] := [1, 2, 3, 4, 2, 3, 5] && L == [2, 3];}"));
	}
	
	@Test
	public void matchSetMultiVars(){
		assertTrue(runTest("{{1, S*, 4, 5}:= {1, 2, 3, 4, 5} && S == {2, 3};}"));
		assertTrue(runTest("{{1, _*, 4, 5} := {1, 2, 3, 4, 5};}"));
	}
	
	@Test(expected=UndeclaredVariableError.class) 
	public void unguardedMatchNoEscape() {
		// m should not be declared after the unguarded pattern match.
		assertTrue(runTest("{int n = 3; int m := n; m == n; }"));
	}
	
	@Test
	public void matchListHasOrderedElement()  {
		prepare("import ListMatchingTests;");

		assertTrue(runTestInSameEvaluator("hasOrderedElement([]) == false;"));
		assertTrue(runTestInSameEvaluator("hasOrderedElement([1]) == false;"));
		assertTrue(runTestInSameEvaluator("hasOrderedElement([1,2]) == false;"));
		assertTrue(runTestInSameEvaluator("hasOrderedElement([1,2,1]) == true;"));
		assertTrue(runTestInSameEvaluator("hasOrderedElement([1,2,3,4,3,2,1]) == true;"));
	}
	
	@Test
	public void matchListHasDuplicateElement()  {
		prepare("import ListMatchingTests;");

		assertTrue(runTestInSameEvaluator("hasDuplicateElement([]) == false;"));
		assertTrue(runTestInSameEvaluator("hasDuplicateElement([1]) == false;"));
		assertTrue(runTestInSameEvaluator("hasDuplicateElement([1,2]) == false;"));
		assertTrue(runTestInSameEvaluator("hasDuplicateElement([1,1]) == true;"));
		assertTrue(runTestInSameEvaluator("hasDuplicateElement([1,2,3]) == false;"));
		assertTrue(runTestInSameEvaluator("hasDuplicateElement([1,2,3,1]) == true;"));
		assertTrue(runTestInSameEvaluator("hasDuplicateElement([1,2,3,2]) == true;"));
		assertTrue(runTestInSameEvaluator("hasDuplicateElement([1,2,3,3]) == true;"));
	}
	
	@Test
	public void matchListIsDuo1()  {
		prepare("import ListMatchingTests;");
		assertTrue(runTestInSameEvaluator("isDuo1([]) == true;"));
		assertTrue(runTestInSameEvaluator("isDuo1([1]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo1([1,1]) == true;"));
		assertTrue(runTestInSameEvaluator("isDuo1([1,2]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo1([1,2, 1]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo1([1,2, 1,2]) == true;"));
		assertTrue(runTestInSameEvaluator("isDuo1([1,2,3, 1,2]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo1([1,2,3, 1,2, 3]) == true;"));
	}
		
	@Test
	public void matchListIsDuo2()  {
		prepare("import ListMatchingTests;");

		assertTrue(runTestInSameEvaluator("isDuo2([]) == true;"));
		assertTrue(runTestInSameEvaluator("isDuo2([1]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo2([1,1]) == true;"));
		assertTrue(runTestInSameEvaluator("isDuo2([1,2]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo2([1,2, 1]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo2([1,2, 1,2]) == true;"));
		assertTrue(runTestInSameEvaluator("isDuo2([1,2,3, 1,2]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo2([1,2,3, 1,2, 3]) == true;"));
	}
	
	@Test
	public void matchListIsDuo3()  {
		prepare("import ListMatchingTests;");

		assertTrue(runTestInSameEvaluator("isDuo3([]) == true;"));
		assertTrue(runTestInSameEvaluator("isDuo3([1]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo3([1,1]) == true;"));
		assertTrue(runTestInSameEvaluator("isDuo3([1,2]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo3([1,2, 1]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo3([1,2, 1,2]) == true;"));
		assertTrue(runTestInSameEvaluator("isDuo3([1,2,3, 1,2]) == false;"));
		assertTrue(runTestInSameEvaluator("isDuo3([1,2,3, 1,2, 3]) == true;"));
	}
	
	@Test
	public void matchListIsTrio1()  {
		prepare("import ListMatchingTests;");

		assertTrue(runTestInSameEvaluator("isTrio1([]) == true;"));
		assertTrue(runTestInSameEvaluator("isTrio1([1]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio1([1,1]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio1([1,1,1]) == true;"));
		assertTrue(runTestInSameEvaluator("isTrio1([2,1,1]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio1([1,2,1]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio1([1,1,2]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio1([1,2, 1,2, 1,2]) == true;"));
	}
	
	@Test
	public void matchListIsTrio2()  {
		prepare("import ListMatchingTests;");

		assertTrue(runTestInSameEvaluator("isTrio2([]) == true;"));
		assertTrue(runTestInSameEvaluator("isTrio2([1]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio2([1,1]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio2([1,1,1]) == true;"));
		assertTrue(runTestInSameEvaluator("isTrio2([2,1,1]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio2([1,2,1]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio2([1,1,2]) == false;"));
		assertTrue(runTestInSameEvaluator("isTrio2([1,2, 1,2, 1,2]) == true;"));
	}
	
	@Test
	public void matchListIsTrio3()  {
		prepare("import ListMatchingTests;");

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

		prepare("data DATA = a() | b() | c() | d() | e(int N) | f(list[DATA] S);");

		assertTrue(runTestInSameEvaluator("[a(), b()] := [a(), b()];"));
		assertTrue(runTestInSameEvaluator("([DATA X1, b()] := [a(), b()]) && (X1 == a());"));

		assertFalse(runTestInSameEvaluator("([DATA X2, DATA Y, c()] := [a(), b()]);"));

		assertTrue(runTestInSameEvaluator("([e(int X3), b()] := [e(3), b()]) && (X3 == 3);"));
		assertTrue(runTestInSameEvaluator("([e(int X4)] := [e(3)]) && (X4 == 3);"));
		assertFalse(runTestInSameEvaluator("([e(int X5)] := [a()]);"));

		assertTrue(runTestInSameEvaluator("([a(), f([a(), b(), DATA X6])] := [a(), f([a(),b(),c()])]) && (X6 == c());"));

		assertTrue(runTestInSameEvaluator("([a(), f([a(), b(), DATA X7]), list[DATA] Y7] := [a(), f([a(),b(),c()]), b()]) && (X7 == c() && Y7 == [b()]);"));
		assertTrue(runTestInSameEvaluator("([DATA A1, f([A1, b(), DATA X8])] := [a(), f([a(),b(),c()])]) && (A1 == a());"));

		assertTrue(runTestInSameEvaluator("([DATA A2, f([A2, b(), list[DATA] SX1]), SX1] := [a(), f([a(),b(),c()]), c()]) && (A2 == a()) && (SX1 ==[c()]);"));

		assertFalse(runTestInSameEvaluator("([DATA A3, f([A3, b(), list[DATA] SX2]), SX2] := [d(), f([a(),b(),c()]), a()]);"));
		assertFalse(runTestInSameEvaluator("([DATA A4, f([A4, b(), list[DATA] SX3]), SX3] := [c(), f([a(),b(),c()]), d()]);"));

	}
	
	@Ignore @Test(expected=StaticError.class)
	public void recursiveDataTypeNoPossibleMatchVertical() {
		prepare("data Bool = and(Bool, Bool) | t;");
		runTestInSameEvaluator("t := and(t,t);");
	}
	
	@Test(expected=StaticError.class)
	public void recursiveDataTypeNoPossibleMatchHorizontal() {
		prepare("data Bool = and(Bool, Bool) | t;");
		prepareMore("data Prop = or(Prop, Prop) | f;");
		runTestInSameEvaluator("Prop p := and(t,t);");
	}
	
	@Ignore @Test(expected=StaticError.class)
	public void recursiveDataTypeNoPossibleHiddenRecursion() {
		prepare("data Prop = f;");
		prepareMore("data Bool = and(list[Prop], list[Prop]) | t;");
		prepareMore("data Prop = or(Bool, Bool);");
		runTestInSameEvaluator("{p = or(t,t); and(t,t) := p;}");
	}
	
	@Test(expected=RedeclaredVariableError.class)
	public void matchListError12() {
		runTest("{list[int] x = [1,2,3]; [1, list[int] L, 2, list[int] L] := x;}");
	}
	
	public void matchListError1() {
		assertTrue(runTest("{list[int] x = [1,2,3]; [1, list[int] L, 2, list[int] M] := x;}"));
	}
	
	public void matchListError11() {
		assertFalse(runTest("[1, list[int] L, 2, list[int] L] := [1,2,3];"));
	}
	
	public void matchListError2() {
		assertFalse(runTest("[1, list[str] L, 2] := [1,2,3];"));
	}
	
	@Test(expected=UnexpectedTypeError.class)
	public void matchListError22() {
		runTest("{ list[int] l = [1,2,3]; [1, list[str] L, 2] := l; }");
	}
	
	@Test
	public void matchListFalse3() {
		assertFalse(runTest("{ list[value] l = [1,2,3]; [1, str S, 2] := l;}"));
	}
	
	@Test(expected=StaticError.class)
	public void matchListError3() {
		runTest("{ list[int] x = [1,2,3] ; [1, str S, 2] := x;}");
	}
	
	
	public void matchListError4() {
		assertFalse(runTest("{str S = \"a\"; [1, S, 2] := [1,2,3];}"));
	}
	
	@Test(expected=StaticError.class)
	public void matchListError42() {
		runTest("{str S = \"a\"; list[int] x = [1,2,3]; [1, S, 2] := x;}");
	}
	
	public void matchListError5() {
		assertFalse(runTest("{list[str] S = [\"a\"]; [1, S, 2] := [1,2,3];}"));
	}
	
	@Test(expected=StaticError.class)
	public void matchListError55() {
		runTest("{list[str] S = [\"a\"]; list[int] x = [1,2,3]; [1, S, 2] := x;}");
	}
	
	@Test
	public void matchListExternalVar() {
		runTest("{list[int] S; [1, S, 2] := [1,2,3] && S == [3];}");
	}

	@Test
	public void matchListSet() {

		prepare("data DATA = a() | b() | c() | d() | e(int N) | f(list[DATA] S) | f(set[DATA] S);");

		assertTrue(runTestInSameEvaluator("[a(), b()] := [a(), b()];"));
		assertTrue(runTestInSameEvaluator("([DATA X1, b()] := [a(), b()]) && (X1 == a());"));

		assertFalse(runTestInSameEvaluator("([DATA X2, DATA Y2, c()] := [a(), b()]);"));

		assertTrue(runTestInSameEvaluator("([e(int X3), b()] := [e(3), b()]) && (X3 == 3);"));
		assertTrue(runTestInSameEvaluator("([e(int X4)] := [e(3)]) && (X4 == 3);"));
		assertFalse(runTestInSameEvaluator("([e(int X5)] := [a()]);"));

		assertTrue(runTestInSameEvaluator("([a(), f({a(), b(), DATA X6})] := [a(), f({a(),b(),c()})]) && (X6 == c());"));
		assertTrue(runTestInSameEvaluator("({a(), f([a(), b(), DATA X7])} := {a(), f([a(),b(),c()])}) && (X7 == c());"));

		assertTrue(runTestInSameEvaluator("([a(), f({a(), b(), DATA X8}), list[DATA] Y8] := [a(), f({a(),b(),c()}), b()]) && (X8 == c() && Y8 == [b()]);"));
		assertTrue(runTestInSameEvaluator("({a(), f([a(), b(), DATA X9]), set[DATA] Y9} := {a(), f([a(),b(),c()]), b()}) && (X9 == c() && Y9 == {b()});"));

		assertTrue(runTestInSameEvaluator("([DATA A1, f({A1, b(), DATA X10})] := [a(), f({a(),b(),c()})]) && (A1 == a());"));
		assertTrue(runTestInSameEvaluator("({DATA A2, f([A2, b(), DATA X11])} := {a(), f([a(),b(),c()])}) && (A2 == a());"));

	}
	
	@Test(expected=StaticError.class)
	public void matchBoolIntError1(){
		assertFalse(runTest("true    := 1;"));
	}
	
	@Test(expected=StaticError.class)
	public void matchBoolIntError2(){
		assertFalse(runTest("1    := true;"));
	}
	
	@Test(expected=StaticError.class)
	public void noMatchBoolIntError1(){
		assertTrue(runTest("true     !:= 1;"));
	}
	
	@Test(expected=StaticError.class)
	public void noMatchBoolIntError2(){
		assertTrue(runTest("1     !:= true;"));
	}
	
	@Test(expected=StaticError.class)
	public void matchStringBoolError1(){
		assertFalse(runTest("\"abc\" := true;"));
	}
	
	@Test(expected=StaticError.class)
	public void matchStringBoolError2(){
		assertFalse(runTest("true := \"abc\";"));
	}
	
	@Test(expected=StaticError.class)
	public void noMatchStringBoolError1(){
		assertTrue(runTest("\"abc\"  !:= true;"));
	}
	
	@Test(expected=StaticError.class)
	public void noMatchStringBoolError2(){
		assertTrue(runTest("true !:= \"abc\";"));
	}
	
	@Test(expected=StaticError.class)
	public void matchStringIntError1(){
		assertFalse(runTest("\"abc\" := 1;"));
	}
	
	@Test(expected=StaticError.class)
	public void matchStringIntError2(){
		assertFalse(runTest("1 := \"abc\";"));
	}
	
	@Test(expected=StaticError.class)
	public void noMatchStringIntError1(){
		assertTrue(runTest("\"abc\"  !:= 1;"));
	}
	
	@Test(expected=StaticError.class)
	public void noMatchStringIntError2(){
		assertTrue(runTest("1 !:= \"abc\";"));
	}
	
	@Test(expected=StaticError.class)
	public void matchStringRealError1(){
		assertFalse(runTest("\"abc\" := 1.5;"));
	}
	
	@Test(expected=StaticError.class)
	public void matchStringRealError2(){
		assertFalse(runTest("1.5 := \"abc\";"));
	}
	
	@Test(expected=StaticError.class)
	public void noMatchStringRealError1(){
		assertTrue(runTest("\"abc\"  !:= 1.5;"));
	}
	
	@Test(expected=StaticError.class)
	public void noMatchStringRealError2(){
		assertTrue(runTest("1.5 !:= \"abc\";"));
	}
	
	@Test(expected=StaticError.class)
	public void matchIntRealError1(){
		assertFalse(runTest("2 := 1.5;"));
	}
	
	@Test(expected=StaticError.class)
	public void matchIntRealError2(){
		assertFalse(runTest("1.5 := 2;"));
	}
	
	@Test(expected=StaticError.class)
	public void noMatchIntRealError1(){
		assertTrue(runTest("2  !:= 1.5;"));
	}
	
	@Test(expected=StaticError.class)
	public void noMatchIntRealError2(){
		assertTrue(runTest("1.5 !:= 2;"));
	}

	@Test
	public void matchLiteral() {

		assertTrue(runTest("true     := true;"));
		assertFalse(runTest("true    := false;"));
		assertTrue(runTest("true     !:= false;"));

		assertTrue(runTest("1        := 1;"));
		assertFalse(runTest("2       := 1;"));
		assertTrue(runTest("2        !:= 1;"));

		assertTrue(runTest("1.5      := 1.5;"));
		assertFalse(runTest("2.5     := 1.5;"));
		assertTrue(runTest("2.5      !:= 1.5;"));
		
		assertFalse(runTest("1.0     := 1.5;"));
		assertTrue(runTest("1.0      !:= 1.5;"));

		assertTrue(runTest("\"abc\"  := \"abc\";"));
		assertFalse(runTest("\"def\" := \"abc\";"));
		assertTrue(runTest("\"def\"  !:= \"abc\";"));
	}
	
	@Test(expected=StaticError.class)
	public void matchADTStringError1(){
		prepare("data F = f(int N) | f(int N, int M) | f(int N, value f, bool B) | g(str S);");
		assertFalse(runTestInSameEvaluator("f(1) := \"abc\";"));
	}
	
	@Test(expected=StaticError.class)
	public void matchADTStringError2(){
		prepare("data F = f(int N) | f(int N, int M) | f(int N, value f, bool B) | g(str S);");
		assertFalse(runTestInSameEvaluator("\"abc\" := f(1);"));
	}
	
	@Test(expected=StaticError.class)
	public void noMatchADTStringError1(){
		prepare("data F = f(int N) | f(int N, int M) | f(int N, value f, bool B) | g(str S);");
		assertTrue(runTestInSameEvaluator("f(1) !:= \"abc\";"));
	}
	
	@Test(expected=StaticError.class)
	public void noMatchADTStringError2(){
		prepare("data F = f(int N) | f(int N, int M) | f(int N, value f, bool B) | g(str S);");
		assertTrue(runTestInSameEvaluator("\"abc\" !:= f(1);"));
	}

	@Test
	public void matchNode() {

		prepare("data F = f(int N) | f(int N, int M) | f(int N, value f, bool B) | g(str S);");
		
		assertTrue(runTestInSameEvaluator("f(1)                   := f(1);"));
		assertTrue(runTestInSameEvaluator("f(1, g(\"abc\"), true) := f(1, g(\"abc\"), true);"));
		assertFalse(runTestInSameEvaluator("g(1)                  := f(1);"));
		assertTrue(runTestInSameEvaluator("g(1)                   !:= f(1);"));
		assertFalse(runTestInSameEvaluator("f(1, 2)               := f(1);"));
		assertTrue(runTestInSameEvaluator("f(1, 2)                !:= f(1);"));
		
		assertTrue(runTestInSameEvaluator("f(_)                   := f(1);"));
		assertTrue(runTestInSameEvaluator("f(_,_)                 := f(1,2);"));
		assertTrue(runTestInSameEvaluator("f(_,_,_)               := f(1,2.5,true);"));
	}
	
	@Ignore @Test(expected=StaticError.class)
	public void NoDataDecl(){
		runTest("f(1) := 1;");
	}
	
	@Test(expected=StaticError.class)
	public void matchSetStringError(){
		assertFalse(runTest("{1} := \"a\";"));
	}

	@Test
	public void matchSet1() {
		
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

		assertTrue(runTest("{ {set[int] X} := {} && X == {};}"));
		assertTrue(runTest("{ {set[int] X} := {1} && X == {1};}"));
		assertTrue(runTest("{ {set[int] X} := {1,2} && X == {1,2};}"));
		
		assertTrue(runTest("{ {set[int] _} := {1,2}; }"));
	
		assertTrue(runTest("({int N, 2, N} := {1,2}) && (N == 1);"));
		
		assertFalse(runTest("({int N, 2, N} := {1,2,3});"));
		assertFalse(runTest("({int N, 2, N} := {1,2,\"a\"});"));
		
		assertTrue(runTest("{int N = 3; {N, 2, 1} := {1,2,3};}"));
		assertTrue(runTest("{set[int] S = {3}; {S, 2, 1} := {1,2,3};}"));
		assertTrue(runTest("{set[int] S = {2, 3}; {S, 1} := {1,2,3};}"));

		assertTrue(runTest("{ {1, set[int] X, 2} := {1,2} && X == {};}"));
		assertFalse(runTest("{ {1, set[int] X, 3} := {1,2};}"));

		assertTrue(runTest("{ {1, set[int] X, 2} := {1,2,3} && X == {3};}"));
		assertTrue(runTest("{ {1, set[int] X, 2} := {1,2,3,4} && X == {3,4};}"));

		assertTrue(runTest("{ {set[int] X, set[int] Y} := {} && X == {} && Y == {};}"));
		assertTrue(runTest("{ {1, set[int] X, set[int] Y} := {1} && X == {} && Y == {};}"));
		assertTrue(runTest("{ {set[int] X, 1, set[int] Y} := {1} && X == {} && Y == {};}"));
		assertTrue(runTest("{ {set[int] X, set[int] Y, 1} := {1} && X == {} && Y == {};}"));

		assertFalse(runTest("{ {set[int] X, set[int] Y, 1} := {2};}"));

		assertTrue(runTest("{ {set[int] X, set[int] Y} := {1} && ((X == {} && Y == {1}) || (X == {1} && Y == {}));}"));

		assertTrue(runTest("{ {set[int] X, set[int] Y, set[int] Z} := {} && X == {} && Y == {} && Z == {};}"));
		assertTrue(runTest("{ {set[int] X, set[int] Y, set[int] Z} := {1} && (X == {1} && Y == {} && Z == {}) || (X == {} && Y == {1} && Z == {}) || (X == {} && Y == {} && Z == {1});}"));

		assertTrue(runTest("{ {int X, set[int] Y} := {1} && X == 1 && Y == {};}"));
		assertTrue(runTest("{ {set[int] X, int Y} := {1} && X == {} && Y == 1;}"));
		assertTrue(runTest("{ {set[int] _, int _} := {1}; }"));
		assertTrue(runTest("{ {set[int] _, _} := {1}; }"));

		assertTrue(runTest("{ {set[int] X, int Y} := {1, 2} && (X == {1} && Y == 2) || (X == {2} && Y == 1);}"));
		
		assertTrue(runTest("{ {set[int] X, set[real] Y} := { 1, 5.5, 2, 6.5} && (X == {1,2} && Y == {5.5, 6.5});}"));
		
		assertTrue(runTest("{ set[int] x = {}; {} := x; }"));
		
	}	

	@Test
	public void matchSet2() {

		prepare("data DATA = a() | b() | c() | d() | e(int N) | f(set[DATA] S);");

		assertTrue(runTestInSameEvaluator("{a(), b()} := {a(), b()};"));
		assertTrue(runTestInSameEvaluator("({DATA X1, b()} := {a(), b()}) && (X1 == a());"));

		assertFalse(runTestInSameEvaluator("({DATA X2, DATA Y2, c()} := {a(), b()});"));

		assertTrue(runTestInSameEvaluator("({e(int X3), b()} := {e(3), b()}) && (X3 == 3);"));
		assertTrue(runTestInSameEvaluator("({e(int X4)} := {e(3)}) && (X4 == 3);"));
		assertFalse(runTestInSameEvaluator("({e(int X5)} := {a()});"));

		assertTrue(runTestInSameEvaluator("({a(), f({a(), b(), DATA X6})} := {a(), f({a(),b(),c()})}) && (X6 == c());"));
		assertTrue(runTestInSameEvaluator("({f({a(), b(), DATA X7}), a()} := {a(), f({a(),b(),c()})}) && (X7 == c());"));

		assertTrue(runTestInSameEvaluator("({a(), f({a(), b(), DATA X8}), set[DATA] Y8} := {a(), b(), f({a(),b(),c()})}) && (X8 == c() && Y8 == {b()});"));
		assertTrue(runTestInSameEvaluator("({DATA A1, f({A1, b(), DATA X9})} := {a(), f({a(),b(),c()})}) && (A1 == a());"));
		assertTrue(runTestInSameEvaluator("({DATA A2, f({A2, b(), DATA X10})} := {f({a(),b(),c()}), a()}) && (A2 == a());"));

		assertTrue(runTestInSameEvaluator("({DATA A3, f({A3, b(), set[DATA] SX1}), SX1} := {a(), f({a(),b(),c()}), c()}) && (A3== a()) && (SX1 =={c()});"));
		assertTrue(runTestInSameEvaluator("({DATA A4, f({A4, b(), set[DATA] SX2}), SX2} := {f({a(),b(),c()}), a(), c()}) && (A4== a()) && (SX2 =={c()});"));
		assertTrue(runTestInSameEvaluator("({DATA A5, f({A5, b(), set[DATA] SX3}), SX3} := {c(), f({a(),b(),c()}), a()}) && (A5 == a()) && (SX3 =={c()});"));

		assertFalse(runTestInSameEvaluator("({DATA A6, f({A6, b(), set[DATA] SX4}), SX4} := {d(), f({a(),b(),c()}), a()});"));
		assertFalse(runTestInSameEvaluator("({DATA A7, f({A7, b(), set[DATA] SX5}), SX5} := {c(), f({a(),b(),c()}), d()});"));
	}	
	
	@Ignore @Test
	public void matchConstructor1(){
		prepare("data Bool = btrue | bfalse | band(Bool left, Bool right) | bor(Bool left, Bool right);");

		//assertTrue(runTestInSameEvaluator("Bool::btrue := btrue;"));
		assertTrue(runTestInSameEvaluator("btrue := btrue;"));
		//assertTrue(runTestInSameEvaluator("Bool::band := band(btrue, bfalse);"));
		assertTrue(runTestInSameEvaluator("band := band(btrue, bfalse);"));
	}
	
	@Ignore @Test
	public void matchConstructor2(){
	
		prepareModule("Bool", "module Bool " +
				  "data Bool = btrue | bfalse | band(Bool left, Bool right) | bor(Bool left, Bool right);");

		assertTrue(runTestInSameEvaluator("import Bool;"));
		assertTrue(runTestInSameEvaluator("btrue := btrue;"));
		assertTrue(runTestInSameEvaluator("Bool::band := band(btrue, bfalse);"));
	}

	
	@Test(expected=StaticError.class)
	public void matchSetDoubleDeclError() {
		runTest("{1, set[int] L, 2, set[int] L} := {1,2,3};");
	}
	
	@Test(expected=StaticError.class)
	public void matchSetWrongElemError() {
		runTest("{1, \"a\", 2, set[int] L} := {1,2,3};");
	}	
	
	@Test(expected=StaticError.class)
	public void matchSetWrongElemError2() {
		runTest("{1, set[str] L, 2} := {1,2,3};");
	}
	
	@Test(expected=StaticError.class)
	public void matchSetWrongElemError3() {
		runTest("{1, str S, 2} := {1,2,3};");
	}
	
	@Test(expected=StaticError.class)
	public void matchSetWrongElemError4() {
		runTest("{set[str] S = {\"a\"}; {1, S, 2} := {1,2,3};}");
	}
	
	@Test
	public void matchSetExternalVar() {
		runTest("{set[int] S; {1, S, 2} := {1,2,3} && S == {3};}");
	}
	
	@Test(expected=StaticError.class)
	public void matchTupleStringError() {
		assertFalse(runTest("<1>           := \"a\";"));
	}
	
	@Test(expected=StaticError.class)
	public void matchTupleArityError() {
		assertFalse(runTest("<1,2>        := <1>;"));
	}
	
	@Test(expected=StaticError.class)
	public void noMatchTupleArityError(){
		assertTrue(runTest("<1> !:= <1,2>;"));
	}

	@Test
	public void matchTuple() {

		assertTrue(runTest("<1>           := <1>;"));
		assertTrue(runTest("<1, \"abc\">  := <1, \"abc\">;"));
		assertFalse(runTest("<2>          := <1>;"));
		assertTrue(runTest("<2>           !:= <1>;"));
		
		assertFalse(runTest("<1, \"abc\"> := <1, \"def\">;"));
		assertTrue(runTest("<1, \"abc\">  !:= <1, \"def\">;"));
		
		assertTrue(runTest("<_, \"abc\">  := <1, \"abc\">;"));
		assertTrue(runTest("<1, _>        := <1, \"abc\">;"));
		assertTrue(runTest("<_, _>        := <1, \"abc\">;"));
	}
	
	@Test
	public void matchTupleExternalVar(){
		assertTrue(runTest("{tuple[int,int] T; T := <1,2> && T[0] == 1 && T[1] == 2;}"));
	}

	@Test
	public void matchVariable() {

		prepare("data F = f(int N);");

		assertTrue(runTestInSameEvaluator("(n1 := 1) && (n1 == 1);"));
		assertTrue(runTestInSameEvaluator("{int n2 = 1; (n2 := 1) && (n2 == 1);}"));
		assertTrue(runTestInSameEvaluator("{int n3 = 1; (n3 !:= 2) && (n3 == 1);}"));

		assertTrue(runTestInSameEvaluator("(f(n5) := f(1)) && (n5 == 1);"));
		assertTrue(runTestInSameEvaluator("{int n6 = 1; (f(n6) := f(1)) && (n6 == 1);}"));
		
		assertTrue(runTestInSameEvaluator("(f(_) := f(1));"));
	}
	
	@Test
	public void matchTypedVariableBecomes() {
		assertTrue(runTest("{int N : 3 := 3 && N == 3;}"));
		assertTrue(runTest("{list[int] L1 : [int N, list[int] L2, int M] := [1,2,3] && L1 == [1,2,3] && N == 1 && L2 == [2] && M == 3;}"));
		assertTrue(runTest("{[1, list[int] L: [int N], 2] := [1,[2],2] && L == [2];}"));
		assertTrue(runTest("{[1, list[int] L1: [list[int] L2, int N], 5] := [1,[2,3,4],5] && L1 == [2,3,4] && L2==[2,3] && N ==4;}"));
		assertTrue(runTest("{[1, list[int] L1: [list[int] L2, int N], L1] := [1,[2,3,4],[2,3,4]] && L1 == [2,3,4] && L2==[2,3] && N ==4;}"));
	}
	
	@Test(expected=StaticError.class)
	public void typedVariableBecomesWrongType(){
		assertTrue(runTest("{str N : 3 := 3; N == 3;}"));
	}
	
	@Test
	public void redeclaredTypedVariableBecomesShadowsAnother(){
		assertTrue(runTest("{int N = 5; int N : 3 := 3 && N == 3;}"));
	}
	
	@Test(expected=StaticError.class)
	public void doubleTypedVariableBecomes(){
		assertTrue(runTest("{[int N : 3, int N : 4] := [3,4] && N == 3;}"));
	}
	
	@Test
	public void matchVariableBecomes() {
		assertTrue(runTest("{N : 3 := 3 && N == 3;}"));
		assertTrue(runTest("{L1 : [int N, list[int] L2, int M] := [1,2,3] && L1 == [1,2,3] && N == 1 && L2 == [2] && M == 3;}"));
		assertTrue(runTest("{[1, L: [int N], 2] := [1,[2],2] && L == [2];}"));
		assertTrue(runTest("{[1, L1: [list[int] L2, int N], 5] := [1,[2,3,4],5] && L1 == [2,3,4] && L2==[2,3] && N ==4;}"));
		assertTrue(runTest("{[1, L1: [list[int] L2, int N], L1] := [1,[2,3,4],[2,3,4]] && L1 == [2,3,4] && L2==[2,3] && N ==4;}"));
	}
	
	public void variableBecomesEquality(){
		assertFalse(runTest("{int N = 5; N : 3 := 3 && N == 3;}"));
		assertTrue(runTest("{int N = 3; N : 3 := 3 && N == 3;}"));
	}
	
	public void doubleVariableBecomes(){
		assertFalse(runTest("{[N : 3, N : 4] := [3,4] && N == 3;}"));
		assertTrue(runTest("{[N : 3, N : 3] := [3,3] && N == 3;}"));
	}
	
	@Test(expected=StaticError.class)
	public void UndeclaredTypeError(){
		runTest("STRANGE X := 123;");
	}
	
	@Test
	public void antiPattern(){
		assertTrue(runTest("{!4 := 3;}"));
		assertFalse(runTest("{!3 := 3;}"));
		
		assertTrue(runTest("{![1,2,3] := [1,2,4];}"));
		assertFalse(runTest("{![1,2,3] := [1,2,3];}"));
	}
	
	@Test(expected=UndeclaredVariableError.class)
	public void antiPatternDoesNotDeclare() {
		runTest("{![1,int X,3] := [1,2,4] && (X ? 10) == 10;}");
	}
	
	@Test
	public void descendant1(){
		assertTrue(runTest("/int N := 1 && N == 1;"));
		assertTrue(runTest("!/int N := true;"));
		
		assertFalse(runTest("/int N := [];"));
		assertTrue(runTest("/int N := [1] && N == 1;"));

		assertTrue(runTest("/int N := [1,2,3,2] && N > 2;"));
		assertTrue(runTest("!/4 := [1,2,3,2];"));
		assertTrue(runTest("/int N := (1 : 10) && (N == 1 || N == 10);"));
	
		assertFalse(runTest("/int N := {};"));
		assertTrue(runTest("/int N := {1} && N == 1;"));
		assertTrue(runTest("/int N := {<false,1>} && N == 1;"));
		
		assertTrue(runTest("/int N := (\"a\" : 1) && N == 1;"));
		assertTrue(runTest("/int N := <\"a\", 1> && N == 1;"));
		
		assertTrue(runTest("{[1, /int N, 3] := [1, [1,2,3,2], 3] && N == 1;}"));
		assertTrue(runTest("{[1, /int N, 3] := [1, [1,2,3,2], 3] && N == 2;}"));	
	}
	
	@Test
	public void descendant2(){
		prepare("data F = f(F left, F right) | g(int N);");
		assertTrue(runTestInSameEvaluator("/g(2) := f(g(1),f(g(2),g(3)));"));
		assertTrue(runTestInSameEvaluator("[1, /g(2), 3] := [1, f(g(1),f(g(2),g(3))), 3];"));
		assertTrue(runTestInSameEvaluator("[1, !/g(5), 3] := [1, f(g(1),f(g(2),g(3))), 3];"));
		
		assertTrue(runTestInSameEvaluator("[1, /f(/g(2), _), 3] := [1, f(g(1),f(g(2),g(3))), 3];"));
		assertTrue(runTestInSameEvaluator("[1, /f(/g(2),/g(3)), 3] := [1, f(g(1),f(g(2),g(3))), 3];"));
		assertTrue(runTestInSameEvaluator("[1, F outer: /f(/F inner: g(2), _), 3] := [1, f(g(1),f(g(2),g(3))), 3] && outer == f(g(1),f(g(2),g(3))) && inner == g(2);"));
			
		assertTrue(runTestInSameEvaluator("{[1, /g(int N1), 3] := [1, f(g(1),f(g(2),g(3))), 3] && N1 == 1;}"));
		assertTrue(runTestInSameEvaluator("{[1, /g(int N2), 3] := [1, f(g(1),f(g(2),g(3))), 3] && N2 == 2;}"));
		assertTrue(runTestInSameEvaluator("{[1, /g(int N3), 3] := [1, f(g(1),f(g(2),g(3))), 3] && N3 == 3;}"));
	}
	
	@Test
	public void descendant3(){
		assertTrue(runTestInSameEvaluator("[n | /int n <- [1,2,3]] == [1,2,3];"));
		assertTrue(runTestInSameEvaluator("[b | /bool b <- [true,false,true]] == [true,false,true];"));
		assertTrue(runTestInSameEvaluator("[s | /str s <- [\"a\",\"b\"]] == [\"a\",\"b\"];"));
		
		assertTrue(runTestInSameEvaluator("{n | /int n <- {1,2,3}} == {1,2,3};"));
		assertTrue(runTestInSameEvaluator("{n | /int n <- {<1,2,3>}} == {1,2,3};"));
		assertTrue(runTestInSameEvaluator("{v | /value v <- {<1,\"b\",true>}} == {1,\"b\",true, <1,\"b\",true>};"));
	}
	
	/*
	 * The following test requires deeper analysis of the data signature
	 */
	@Ignore @Test(expected=StaticError.class)
	public void descendantWrongType(){
		prepare("data F = f(F left, F right) | g(int N);");
		assertTrue(runTestInSameEvaluator("/true := f(g(1),f(g(2),g(3)));"));
	}
	
	@Test
	public void listCount1(){
		String cnt = 
		      "int cnt(list[int] L){" +
		      "  int count = 0;" +
		      "  while ([int N, list[int] Ns] := L) { " +
		      "         count = count + 1;" +
		      "         L = tail(L);" +
		      "  }" +
		      "  return count;" +
		      "}";
	
		prepare("import List;");
		assertTrue(runTestInSameEvaluator("{" + cnt + "cnt([1,2,3]) == 3;}"));
	}
	
	@Test
	public void listCount2(){
		String cnt = 
		      "int cnt(list[int] L){" +
		      "  int count = 0;" +
		      "  while ([int N, list[int] _] := L) { " +
		      "         count = count + 1;" +
		      "         L = tail(L);" +
		      "  }" +
		      "  return count;" +
		      "}";
	
		prepare("import List;");
		assertTrue(runTestInSameEvaluator("{" + cnt + "cnt([1,2,3]) == 3;}"));
	}
	
	@Test
	public void listCount3(){
		String cnt = 
		      "int cnt(list[int] L){" +
		      "  int count = 0;" +
		      "  while ([N, list[int] _] := L) { " +
		      "         count = count + 1;" +
		      "         L = tail(L);" +
		      "  }" +
		      "  return count;" +
		      "}";
	
		prepare("import List;");
		assertTrue(runTestInSameEvaluator("{" + cnt + "cnt([1,2,3]) == 3;}"));
	}
	
	@Test
	public void setCount1(){
		String cnt = 
		      "int cnt(set[int] S){" +
		      "  int count = 0;" +
		      "  while ({int N, set[int] Ns} := S) { " +
		      "         count = count + 1;" +
		      "         S = S - {N};" +
		      "  }" +
		      "  return count;" +
		      "}";
	
		assertTrue(runTestInSameEvaluator("{" + cnt + "cnt({1,2,3}) == 3;}"));
	}
	
	@Test
	public void setCount2(){
		String cnt = 
		      "int cnt(set[int] S){" +
		      "  int count = 0;" +
		      "  while ({int N, set[int] _} := S) { " +
		      "         count = count + 1;" +
		      "         S = S - {N};" +
		      "  }" +
		      "  return count;" +
		      "}";
	
		assertTrue(runTestInSameEvaluator("{" + cnt + "cnt({1,2,3}) == 3;}"));
	}
	
	@Test
	public void setCount3(){
		String cnt = 
		      "int cnt(set[int] S){" +
		      "  int count = 0;" +
		      "  while ({N, set[int] _} := S) { " +
		      "         count = count + 1;" +
		      "         S = S - {N};" +
		      "  }" +
		      "  return count;" +
		      "}";
	
		assertTrue(runTestInSameEvaluator("{" + cnt + "cnt({1,2,3}) == 3;}"));
	}
}
