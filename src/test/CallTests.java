package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.errors.*;

public class CallTests extends TestFramework{
	
	
	@Test(expected=NoSuchFunctionError.class)
	public void callError1() {
		runTest("zap(1,2);");
	}
	
	@Test(expected=NoSuchModuleError.class)
	public void callError2() {
		runTest("zip::zap(1,2);");
	}
	
	@Test(expected=NoSuchFunctionError.class)
	public void callError3() {
		runTest("{zap = 10; zap(1,2);}");
	}
	
	@Test public void testFac() {
		String fac = "int fac(int n){ return (n <= 0) ? 1 : (n * fac(n - 1));}";
		
		assertTrue(runTest("{" + fac + " fac(0) == 1;}"));
		//assertTrue(tf.runTest("{ public " + fac + " fac(0) == 1;}"));
		//assertTrue(tf.runTest("{ private " + fac + " fac(0) == 1;}"));
		
		assertTrue(runTest("{" +  fac + " fac(1) == 1;}"));
		assertTrue(runTest("{" + fac + " fac(2) == 2;}"));
		assertTrue(runTest("{" + fac + " fac(3) == 6;}"));
		assertTrue(runTest("{" + fac + " fac(4) == 24;}"));
	}
	
	@Test public void testNotTailRecFac() {
		
		String fac = "int fac(int n) { if (n == 0) { return 1; } int z = fac(n - 1); return z * n; }";
		
		assertTrue(runTest("{" + fac + " fac(0) == 1;}"));
		assertTrue(runTest("{" + fac + " fac(1) == 1;}"));
		assertTrue(runTest("{" + fac + " fac(2) == 2;}"));
		assertTrue(runTest("{" + fac + " fac(3) == 6;}"));
		assertTrue(runTest("{" + fac + " fac(4) == 24;}"));
	}
	
	@Test public void testFormalsAreLocal() {
		
		String fac = "int fac(int n) { if (n == 0) { return 1; } int z = n; int n = fac(n - 1); return z * n; }";
		
		assertTrue(runTest("{" + fac + " fac(0) == 1;}"));
		assertTrue(runTest("{" + fac + " fac(1) == 1;}"));
		assertTrue(runTest("{" + fac + " fac(2) == 2;}"));
		assertTrue(runTest("{" + fac + " fac(3) == 6;}"));
		assertTrue(runTest("{" + fac + " fac(4) == 24;}"));
	}
	
	@Test public void testHigherOrder() {
		
		String add = "int add(int a, int b) { return a + b; }";
		String sub = "int sub(int a, int b) { return a - b; }";
		String doSomething = "int doSomething(int (int a, int b) F) { return #F(1,2); }";

		assertTrue(runTest("{" + add + " " + doSomething + " " + "doSomething(#add) == 3;}"));
		assertTrue(runTest("{" + add + " " + sub + " " + doSomething + " " + "doSomething(#sub) == -1;}"));
	}
	
	@Test public void testClosures() {
		
		String doSomething = "int f(int (int i) g, int j) { return #g(j); }";
		
	    assertTrue(runTest("{ " + doSomething + " f(int (int i) { return i + 1; }, 0) == 1; }"));
	    assertTrue(runTest("{ int x = 1; " + doSomething + " (f(int (int i) { x = x * 2; return i + x; }, 1) == 3) && (x == 2); }"));
	}
	
	@Test public void testVarArgs() {
		
		String add0 = "int add(int i...) { return 0; }";
		String add1 = "int add(int i...) { return i[0]; }";
		String add2 = "int add(int i, int j...) { return i + j[0]; }";
		
		assertTrue(runTest("{" + add0 + " add() == 0; }"));
		assertTrue(runTest("{" + add0 + " add([]) == 0; }"));
		assertTrue(runTest("{" + add0 + " add(0) == 0; }"));
		assertTrue(runTest("{" + add0 + " add([0]) == 0; }"));
		assertTrue(runTest("{" + add0 + " add(0,1,2) == 0; }"));
		assertTrue(runTest("{" + add0 + " add([0,1,2]) == 0; }"));
		
		assertTrue(runTest("{" + add1 + " add(0) == 0; }"));
		assertTrue(runTest("{" + add1 + " add([0]) == 0; }"));
		assertTrue(runTest("{" + add1 + " add(0,1,2) == 0; }"));
		assertTrue(runTest("{" + add1 + " add([0,1,2]) == 0; }"));
		
		assertTrue(runTest("{" + add2 + " add(1,2) == 3; }"));
		assertTrue(runTest("{" + add2 + " add(1,[2]) == 3; }"));
		assertTrue(runTest("{" + add2 + " add(1,2,3) == 3; }"));
		assertTrue(runTest("{" + add2 + " add(1,[2,3]) == 3; }"));
	}
	
	@Test public void testSideEffect() {
		
		String one = "void One() { called = called + 1; return; }";
		
		assertTrue(runTest("{ int called = 0; " + one + " One(); One(); One(); called == 3;}"));
	}
	
	@Test public void testMax1() {
		
		String maxInt = "int max(int a, int b) { return a > b ? a : b; }";
		String maxReal = "real max(real a, real b) { return a > b ? a : b; }";
		assertTrue(runTest("{" + maxInt + " max(3,4) == 4;}"));
		assertTrue(runTest("{" + maxInt + maxReal + " (max(3,4) == 4) && (max(3.0,4.0) == 4.0);}"));
	}
	
	@Test public void testMax2() {
		
		String max = "&T max(&T a, &T b) { return a > b ? a : b; }";
		assertTrue(runTest("{" + max + " max(3,4) == 4;}"));
		assertTrue(runTest("{" + max + " max(3.0,4.0) == 4.0;}"));
		assertTrue(runTest("{" + max + " max(\"abc\",\"def\") == \"def\";}"));
	}
	
	@Test public void testIdent() {
		
		String ident = "&T ident(&T x){ return x; }";
		assertTrue(runTest("{" + ident + " ident(true) == true;}"));
		assertTrue(runTest("{" + ident + " ident(4) == 4;}"));
		assertTrue(runTest("{" + ident + " ident(4.5) == 4.5;}"));
		assertTrue(runTest("{" + ident + " ident(\"abc\") == \"abc\";}"));
//		assertTrue(runTest("{" + ident + " ident(f(1)) == f(1);}"));
		assertTrue(runTest("{" + ident + " ident([1,2,3]) == [1,2,3];}"));
		assertTrue(runTest("{" + ident + " ident({1,2,3}) == {1,2,3};}"));
		assertTrue(runTest("{" + ident + " ident((1=>10,2=>20,3=>30)) == (1=>10,2=>20,3=>30);}"));
	}
	
	@Test public void testMap() {
		
		String put = "map[&K,&V] put(map[&K,&V] m, &K k, &V v) { m[k] = v; return m; }";
		
		assertTrue(runTest("{" + put + " put((),1,\"1\") == (1=>\"1\"); }"));
	}
	
	@Test public void testAdd() {
		
		String add = "list[&T] java add(&T elm, list[&T] lst) { return lst.insert(elm); }";
		
		assertTrue(runTest("{" + add + " add(1, [2,3]) == [1,2,3];}"));
		assertTrue(runTest("{" + add + " add(\"a\", [\"b\",\"c\"]) == [\"a\",\"b\", \"c\"];}"));
	}
	
	@Test public void testPutAt() {
		
		String putAt = "list[&T] java putAt(&T elm, int n, list[&T] lst){return lst.put(n.getValue(), elm);}";
		
		assertTrue(runTest("{" + putAt + " putAt(1, 0, [2,3]) == [1,3];}"));
	}
	
}

