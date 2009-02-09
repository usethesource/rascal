package test;

import junit.framework.TestCase;
import java.io.IOException;

public class CallTests extends TestCase{
	
	public void testFac() throws IOException {
		TestFramework tf = new TestFramework();
		
		String fac = "int fac(int n){ return (n <= 0) ? 1 : (n * fac(n - 1));}";
		
		assertTrue(tf.runTest("{" + fac + " fac(0) == 1;}"));
		//assertTrue(tf.runTest("{ public " + fac + " fac(0) == 1;}"));
		//assertTrue(tf.runTest("{ private " + fac + " fac(0) == 1;}"));
		
		assertTrue(tf.runTest("{" +  fac + " fac(1) == 1;}"));
		assertTrue(tf.runTest("{" + fac + " fac(2) == 2;}"));
		assertTrue(tf.runTest("{" + fac + " fac(3) == 6;}"));
		assertTrue(tf.runTest("{" + fac + " fac(4) == 24;}"));
	}
	
	public void testNotTailRecFac() throws IOException {
		TestFramework tf = new TestFramework();
		
		String fac = "int fac(int n) { if (n == 0) { return 1; } int z = fac(n - 1); return z * n; }";
		
		assertTrue(tf.runTest("{" + fac + " fac(0) == 1;}"));
		assertTrue(tf.runTest("{" + fac + " fac(1) == 1;}"));
		assertTrue(tf.runTest("{" + fac + " fac(2) == 2;}"));
		assertTrue(tf.runTest("{" + fac + " fac(3) == 6;}"));
		assertTrue(tf.runTest("{" + fac + " fac(4) == 24;}"));
	}
	
	public void testFormalsAreLocal() throws IOException {
		TestFramework tf = new TestFramework();
		
		String fac = "int fac(int n) { if (n == 0) { return 1; } int z = n; int n = fac(n - 1); return z * n; }";
		
		assertTrue(tf.runTest("{" + fac + " fac(0) == 1;}"));
		assertTrue(tf.runTest("{" + fac + " fac(1) == 1;}"));
		assertTrue(tf.runTest("{" + fac + " fac(2) == 2;}"));
		assertTrue(tf.runTest("{" + fac + " fac(3) == 6;}"));
		assertTrue(tf.runTest("{" + fac + " fac(4) == 24;}"));
	}
	
	public void testHigherOrder() throws IOException  {
		TestFramework tf = new TestFramework();
		
		String add = "int add(int a, int b) { return a + b; }";
		String sub = "int sub(int a, int b) { return a - b; }";
		String doSomething = "int doSomething(int (int a, int b) F) { return #F(1,2); }";

		assertTrue(tf.runTest("{" + add + " " + doSomething + " " + "doSomething(#add) == 3;}"));
		assertTrue(tf.runTest("{" + add + " " + sub + " " + doSomething + " " + "doSomething(#sub) == -1;}"));
	}
	
	public void testClosures() throws IOException {
		TestFramework tf = new TestFramework();
		
		String doSomething = "int f(int (int i) g, int j) { return #g(j); }";
		
	    assertTrue(tf.runTest("{ " + doSomething + " f(int (int i) { return i + 1; }, 0) == 1; }"));
	    assertTrue(tf.runTest("{ int x = 1; " + doSomething + " (f(int (int i) { x = x * 2; return i + x; }, 1) == 3) && (x == 2); }"));
	}
	
	public void testVarArgs() throws IOException {
		TestFramework tf = new TestFramework();
		
		String add0 = "int add(int i...) { return 0; }";
		String add1 = "int add(int i...) { return i[0]; }";
		String add2 = "int add(int i, int j...) { return i + j[0]; }";
		
		assertTrue(tf.runTest("{" + add0 + " add() == 0; }"));
		assertTrue(tf.runTest("{" + add0 + " add([]) == 0; }"));
		assertTrue(tf.runTest("{" + add0 + " add(0) == 0; }"));
		assertTrue(tf.runTest("{" + add0 + " add([0]) == 0; }"));
		assertTrue(tf.runTest("{" + add0 + " add(0,1,2) == 0; }"));
		assertTrue(tf.runTest("{" + add0 + " add([0,1,2]) == 0; }"));
		
		assertTrue(tf.runTest("{" + add1 + " add(0) == 0; }"));
		assertTrue(tf.runTest("{" + add1 + " add([0]) == 0; }"));
		assertTrue(tf.runTest("{" + add1 + " add(0,1,2) == 0; }"));
		assertTrue(tf.runTest("{" + add1 + " add([0,1,2]) == 0; }"));
		
		assertTrue(tf.runTest("{" + add2 + " add(1,2) == 3; }"));
		assertTrue(tf.runTest("{" + add2 + " add(1,[2]) == 3; }"));
		assertTrue(tf.runTest("{" + add2 + " add(1,2,3) == 3; }"));
		assertTrue(tf.runTest("{" + add2 + " add(1,[2,3]) == 3; }"));
	}
	
	public void testSideEffect() throws IOException {
		TestFramework tf = new TestFramework();
		
		String one = "void One() { called = called + 1; return; }";
		
		assertTrue(tf.runTest("{ int called = 0; " + one + " One(); One(); One(); called == 3;}"));
	}
	
	public void testMax1() throws IOException {
		TestFramework tf = new TestFramework();
		
		String maxInt = "int max(int a, int b) { return a > b ? a : b; }";
		String maxReal = "real max(real a, real b) { return a > b ? a : b; }";
		assertTrue(tf.runTest("{" + maxInt + " max(3,4) == 4;}"));
		assertTrue(tf.runTest("{" + maxInt + maxReal + " (max(3,4) == 4) && (max(3.0,4.0) == 4.0);}"));
	}
	
	public void testMax2() throws IOException {
		TestFramework tf = new TestFramework();
		
		String max = "&T max(&T a, &T b) { return a > b ? a : b; }";
		assertTrue(tf.runTest("{" + max + " max(3,4) == 4;}"));
		assertTrue(tf.runTest("{" + max + " max(3.0,4.0) == 4.0;}"));
		assertTrue(tf.runTest("{" + max + " max(\"abc\",\"def\") == \"def\";}"));
	}
	
	public void testIdent() throws IOException {
		TestFramework tf = new TestFramework();
		
		String ident = "&T ident(&T x){ return x; }";
		assertTrue(tf.runTest("{" + ident + " ident(true) == true;}"));
		assertTrue(tf.runTest("{" + ident + " ident(4) == 4;}"));
		assertTrue(tf.runTest("{" + ident + " ident(4.5) == 4.5;}"));
		assertTrue(tf.runTest("{" + ident + " ident(\"abc\") == \"abc\";}"));
//		assertTrue(tf.runTest("{" + ident + " ident(f(1)) == f(1);}"));
		assertTrue(tf.runTest("{" + ident + " ident([1,2,3]) == [1,2,3];}"));
		assertTrue(tf.runTest("{" + ident + " ident({1,2,3}) == {1,2,3};}"));
		assertTrue(tf.runTest("{" + ident + " ident((1:10,2:20,3:30)) == (1:10,2:20,3:30);}"));
	}
	
	public void testMap() throws IOException {
		TestFramework tf = new TestFramework();
		
		String put = "map[&K,&V] put(map[&K,&V] m, &K k, &V v) { m[k] = v; return m; }";
		
		assertTrue(tf.runTest("{" + put + " put((),1,\"1\") == (1:\"1\"); }"));
	}
	
	public void testAdd() throws IOException {
		TestFramework tf = new TestFramework();
		
		String add = "list[&T] java add(&T elm, list[&T] lst) { return lst.insert(elm); }";
		
		assertTrue(tf.runTest("{" + add + " add(1, [2,3]) == [1,2,3];}"));
		assertTrue(tf.runTest("{" + add + " add(\"a\", [\"b\",\"c\"]) == [\"a\",\"b\", \"c\"];}"));
	}
	
	public void testPutAt() throws IOException {
		TestFramework tf = new TestFramework();
		
		String putAt = "list[&T] java putAt(&T elm, int n, list[&T] lst){return lst.put(n.getValue(), elm);}";
		
		assertTrue(tf.runTest("{" + putAt + " putAt(1, 0, [2,3]) == [1,3];}"));
	}
	
}

