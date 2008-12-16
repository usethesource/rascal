package test;

import junit.framework.TestCase;
import java.io.IOException;

public class CallTests extends TestCase{
	
	private TestFramework tf = new TestFramework();
	
	public void testFac() throws IOException {
		String fac = "int fac(int n){ return (n <= 0) ? 1 : (n * fac(n - 1));}";
		
		assertTrue(tf.runTest("{" + fac + " fac(0) == 1;}"));
		//assertTrue(tf.runTest("{ public " + fac + " fac(0) == 1;}"));
		assertTrue(tf.runTest("{ private " + fac + " fac(0) == 1;}"));
		
		assertTrue(tf.runTest("{" +  fac + " fac(1) == 1;}"));
		assertTrue(tf.runTest("{" + fac + " fac(2) == 2;}"));
		assertTrue(tf.runTest("{" + fac + " fac(3) == 6;}"));
		assertTrue(tf.runTest("{" + fac + " fac(4) == 24;}"));
	}
	
	public void testHigherOrder() throws IOException  {
		String add = "int add(int a, int b) { return a + b; }";
		String sub = "int sub(int a, int b) { return a - b; }";
		String doSomething = "int doSomething(int (int a, int b) F) { return #F(1,2); }";

		assertTrue(tf.runTest("{" + add + " " + doSomething + " " + "doSomething(#add) == 3;}"));
		assertTrue(tf.runTest("{" + add + " " + sub + " " + doSomething + " " + "doSomething(#sub) == -1;}"));
	}
	
	public void testSideEffect() throws IOException {
		String one = "void One() { called = called + 1; return; }";
		
		assertTrue(tf.runTest("{ int called = 0; " + one + " One(); One(); One(); called == 3;}"));
	}
	
	public void testMax1() throws IOException {
		String maxInt = "int max(int a, int b) { return a > b ? a : b; }";
		String maxDouble = "double max(double a, double b) { return a > b ? a : b; }";
		assertTrue(tf.runTest("{" + maxInt + " max(3,4) == 4;}"));
		assertTrue(tf.runTest("{" + maxInt + maxDouble + " (max(3,4) == 4) && (max(3.0,4.0) == 4.0);}"));
	}
	
	public void testMax2() throws IOException {
		String max = "&T max(&T a, &T b) { return a > b ? a : b; }";
		assertTrue(tf.runTest("{" + max + " max(3,4) == 4;}"));
		assertTrue(tf.runTest("{" + max + " max(3.0,4.0) == 4.0;}"));
		assertTrue(tf.runTest("{" + max + " max(\"abc\",\"def\") == \"def\";}"));
	}
	
	public void testIdent() throws IOException {
		String ident = "&T ident(&T x){ return x; }";
		assertTrue(tf.runTest("{" + ident + " ident(true) == true;}"));
		assertTrue(tf.runTest("{" + ident + " ident(4) == 4;}"));
		assertTrue(tf.runTest("{" + ident + " ident(4.5) == 4.5;}"));
		assertTrue(tf.runTest("{" + ident + " ident(\"abc\") == \"abc\";}"));
		assertTrue(tf.runTest("{" + ident + " ident(f(1)) == f(1);}"));
		assertTrue(tf.runTest("{" + ident + " ident([1,2,3]) == [1,2,3];}"));
		assertTrue(tf.runTest("{" + ident + " ident({1,2,3}) == {1,2,3};}"));
		assertTrue(tf.runTest("{" + ident + " ident((1:10,2:20,3:30)) == (1:10,2:20,3:30);}"));
	}
	
	public void testMap() throws IOException {
		String put = "map[&K,&V] put(map[&K,&V] m, &K k, &V v) { m[k] = v; return m; }";
		
		assertTrue(tf.runTest("{" + put + " put((),1,\"1\") == (1:\"1\"); }"));
	}
	
	public void testAdd() throws IOException {
		String add = "list[&T] java add(&T elm, list[&T] lst) { return lst.insert(elm); }";
		
		assertTrue(tf.runTest("{" + add + " add(3, [1,2]) == [1,2,3];}"));
		assertTrue(tf.runTest("{ public " + add + " add(3, [1,2]) == [1,2,3];}"));
		assertTrue(tf.runTest("{" + add + " add(\"c\", [\"a\",\"b\"]) == [\"a\",\"b\", \"c\"];}"));
	}
}

