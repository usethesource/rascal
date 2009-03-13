package test;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredFunctionError;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredModuleError;
import org.meta_environment.rascal.interpreter.staticErrors.UninitializedVariableError;

public class ImportTests extends TestFramework {
	

	@Test(expected=UndeclaredModuleError.class)
	public void importError() {
		runTest("import zap;");
	}
	
	@Test
	public void testFun() {
		
		prepareModule("module M" +
				         " public int f(int n) {return 2 * n;}" +
				         " private int g(int n) { return 2 * n;}");
		
		assertTrue(runTestInSameEvaluator("import M;"));
		assertTrue(runTestInSameEvaluator("M::f(3) == 6;"));
		assertTrue(runTestInSameEvaluator("f(3) == 6;"));
		assertTrue(runTestInSameEvaluator("{ int f(int n) {return 3 * n;} f(3) == 9;}"));
	}
	
	@Test
	public void testVar() {
		
		prepareModule("module M\n" +
				         "public int n = 3;\n" +
				         "private int m = 3;");
		assertTrue(runTestInSameEvaluator("import M;"));
		assertTrue(runTestInSameEvaluator("M::n == 3;"));
		assertTrue(runTestInSameEvaluator("n == 3;"));
		assertTrue(runTestInSameEvaluator("{ int n = 4; n == 4;}"));
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void UndefinedPrivateVar1(){
		prepareModule("module M\n" +
		         "private int m = 3;");
		runTestInSameEvaluator("m != 3;");
	}
	
	@Ignore @Test(expected=UninitializedVariableError.class)
	public void UndefinedPrivateVar2(){
		prepareModule("module M\n" +
		         "private int m = 3;");
		prepareMore("import M;");
		runTestInSameEvaluator("int n = m;");
	}
	
	@Test(expected=UndeclaredFunctionError.class)
	public void UndefinedPrivateFunction(){
		prepareModule("module M\n" +
		         "private int f() {return 3;}");
		runTestInSameEvaluator("f();");
	}
	
	@Test
	public void testMbase1() {
		
		prepare("import Mbase;");
		
		assertTrue(runTestInSameEvaluator("Mbase::n == 2;"));
		assertTrue(runTestInSameEvaluator("n == 2;"));
		assertTrue(runTestInSameEvaluator("Mbase::f(3) == 6;"));
		assertTrue(runTestInSameEvaluator("f(3) == 6;"));
		assertTrue(runTestInSameEvaluator("{ int n = 3; n == 3;}"));
	}
	
	@Test
	public void testMbase2() {
		
		prepareModule("module M " +
						 "import Mbase; " +
						 "public int m = n;" +
						 "public int f() { return n; }"	 +
						 "public int g() { return m; } "
		);
		
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("M::m == 2;"));
		assertTrue(runTestInSameEvaluator("M::f() == 2;"));
		assertTrue(runTestInSameEvaluator("M::g() == 2;"));
	}
	
	@Test
	public void testMbase3() {
		
		prepareModule("module M " +
						 "import Mbase;" +
						 "public int g(int n) {return 3 * n;}" +
						 "public int h(int n) {return f(n);}" +
						 "public int m = 3;"
		);
		
		prepareMore("import M;");
		prepareMore("import Mbase;");
		assertTrue(runTestInSameEvaluator("Mbase::n == 2;"));
		assertTrue(runTestInSameEvaluator("Mbase::f(3) == 6;"));
		
		assertTrue(runTestInSameEvaluator("M::m == 3;"));
		assertTrue(runTestInSameEvaluator("M::g(3) == 9;"));
		
		assertTrue(runTestInSameEvaluator("M::h(3) == 6;"));
	}
	
	@Test
	public void testSize() {
		
		prepareModule("module Msize \n" +
				         "import Set;\n" +
						 "public set[int] Procs = {1, 2, 3};\n" +
						 "public int f() {int nProcs = Set::size(Procs); return nProcs;}\n" +
						 "public int g() {int nProcs = size(Procs); return nProcs;}\n"
		);
		
		runTestInSameEvaluator("import Msize;");
		assertTrue(runTestInSameEvaluator("f() == 3;"));
		assertTrue(runTestInSameEvaluator("g() == 3;"));
	}
}
