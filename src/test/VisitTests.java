package test;

import java.io.IOException;

import junit.framework.TestCase;

public class VisitTests extends TestCase {
	private static TestFramework tf = new TestFramework();
	
	public void testCnt() throws IOException {
		String cnt =
		"int cnt(tree T) {" +
		"   int C = 0;" +
		"   visit(T) {" +
		"      case int N: C = C + 1;" +
		"    };" +
		"    return C;" +
		"}";
		
		assertTrue(tf.runTest("{" + cnt + "cnt(f(3)) == 1;}"));
		assertTrue(tf.runTest("{" + cnt + "cnt(f(1,2,3)) == 3;}"));
		assertTrue(tf.runTest("{" + cnt + "cnt(f(1,g(2,3))) == 3;}"));
		assertTrue(tf.runTest("{" + cnt + "cnt(f(1,g(2,[3,4,5]))) == 5;}"));
		assertTrue(tf.runTest("{" + cnt + "cnt(f(1,g(2,{3,4,5}))) == 5;}"));
		assertTrue(tf.runTest("{" + cnt + "cnt(f(1,g(2,<3,4,5>))) == 5;}"));
		assertTrue(tf.runTest("{" + cnt + "cnt(f(1,g(2,{<1,10>,<2,20>}))) == 6;}"));
		//assertTrue(tf.runTest("{" + cnt + "cnt(f(1,g(2,(1:10,2:20)))) == 6;}"));
	}
	
	public void testInc() throws IOException {
		String inc =
		"	tree inc(tree T) {" +
		"    return visit(T) {" +
		"      case int N: insert N + 1;" +
		"    };" + 
		"}";
		
		assertTrue(tf.runTest("{" + inc + "inc(f(3)) == f(4);}"));
		assertTrue(tf.runTest("{" + inc + "inc(f(1,2,3)) == f(2,3,4);}"));
		assertTrue(tf.runTest("{" + inc + "inc(f(1,g(2,3))) == f(2,g(3,4));}"));
		assertTrue(tf.runTest("{" + inc + "inc(f(1,g(2,[3,4,5]))) == f(2,g(3,[4,5,6]));}"));
		assertTrue(tf.runTest("{" + inc + "inc(f(1,g(2,{3,4,5}))) == f(2,g(3,{4,5,6}));}"));
		assertTrue(tf.runTest("{" + inc + "inc(f(1,g(2,<3,4,5>))) == f(2,g(3,<4,5,6>));}"));
		assertTrue(tf.runTest("{" + inc + "inc(f(1,g(2,{<1,10>,<2,20>}))) == f(2,g(3,{<2,11>,<3,21>}));}"));
		//assertTrue(tf.runTest("{" + inc + "inc(f(1,g(2,(1:10,2:20)))) == 6;}"));
	}
	
	public void testFrepA() throws IOException {
		String frepa =
		// Replace all nodes g(_,_) by h(_,_)
		// Using insert

		"tree frepa(tree T) { " +
		"    return visit (T) {" +
		"      case g(value T1, value T2):" +
		"           insert h(T1, T2);" +
		"    };" +
		"}";
		
		assertTrue(tf.runTest("{" + frepa + "frepa(f(3)) == f(3);}"));
		assertTrue(tf.runTest("{" + frepa + "frepa(f(1,2,3)) == f(1,2,3);}"));
		assertTrue(tf.runTest("{" + frepa + "frepa(f(1,g(2,3))) == f(1,h(2,3));}"));
		assertTrue(tf.runTest("{" + frepa + "frepa(f(1,g(2,[3,4,5]))) == f(1,h(2,[3,4,5]));}"));
		assertTrue(tf.runTest("{" + frepa + "frepa(f(1,g(2,{3,4,5}))) == f(1,h(2,{3,4,5}));}"));
		assertTrue(tf.runTest("{" + frepa + "frepa(f(1,g(2,<3,4,5>))) == f(1,h(2,<3,4,5>));}"));
		assertTrue(tf.runTest("{" + frepa + "frepa(f(1,g(2,{<1,10>,<2,20>}))) == f(1,h(2,{<1,10>,<2,20>}));}"));
		//assertTrue(tf.runTest("{" + frepa + "frepa(f(1,g(2,(1:10,2:20)))) == 6;}"));
	}
	
	public void testFrepB() throws IOException {
		String frepb =
		// Replace all nodes g(_,_) by h(_,_)
		// Using replacement rule

		"tree frepb(tree T) {" +
		"    return visit (T) {" +
		"      case g(value T1, value T2) => h(T1, T2)" +
		"    };" +
		"}";
		
		assertTrue(tf.runTest("{" + frepb + "frepb(f(3)) == f(3);}"));
		assertTrue(tf.runTest("{" + frepb + "frepb(f(1,2,3)) == f(1,2,3);}"));
		assertTrue(tf.runTest("{" + frepb + "frepb(f(1,g(2,3))) == f(1,h(2,3));}"));
		assertTrue(tf.runTest("{" + frepb + "frepb(f(1,g(2,[3,4,5]))) == f(1,h(2,[3,4,5]));}"));
		assertTrue(tf.runTest("{" + frepb + "frepb(f(1,g(2,{3,4,5}))) == f(1,h(2,{3,4,5}));}"));
		assertTrue(tf.runTest("{" + frepb + "frepb(f(1,g(2,<3,4,5>))) == f(1,h(2,<3,4,5>));}"));
		assertTrue(tf.runTest("{" + frepb + "frepb(f(1,g(2,{<1,10>,<2,20>}))) == f(1,h(2,{<1,10>,<2,20>}));}"));
		//assertTrue(tf.runTest("{" + frepb + "frepb(f(1,g(2,(1:10,2:20)))) == 6;}"));
	}
	
	public void testFrepG2H3a() throws IOException {
		String frepG2H3a =
		// Replace all nodes g(_,_) by h(_,_,_)
		// Using insert

		"tree frepG2H3a(tree T) {" +
		"    return visit (T) {" +
		"      case g(value T1, value T2):" +
		"           insert h(T1, T2, 0);" +
		"    };" +
		"}";
		
		assertTrue(tf.runTest("{" + frepG2H3a + "frepG2H3a(f(3)) == f(3);}"));
		assertTrue(tf.runTest("{" + frepG2H3a + "frepG2H3a(f(1,2,3)) == f(1,2,3);}"));
		assertTrue(tf.runTest("{" + frepG2H3a + "frepG2H3a(f(1,g(2,3))) == f(1,h(2,3,0));}"));
		assertTrue(tf.runTest("{" + frepG2H3a + "frepG2H3a(f(1,g(2,[3,4,5]))) == f(1,h(2,[3,4,5],0));}"));
		assertTrue(tf.runTest("{" + frepG2H3a + "frepG2H3a(f(1,g(2,{3,4,5}))) == f(1,h(2,{3,4,5},0));}"));
		assertTrue(tf.runTest("{" + frepG2H3a + "frepG2H3a(f(1,g(2,<3,4,5>))) == f(1,h(2,<3,4,5>,0));}"));
		assertTrue(tf.runTest("{" + frepG2H3a + "frepG2H3a(f(1,g(2,{<1,10>,<2,20>}))) == f(1,h(2,{<1,10>,<2,20>},0));}"));
		//assertTrue(tf.runTest("{" + frepG2H3a + "frepG2H3a(f(1,g(2,(1:10,2:20)))) == 6;}"));

	}
	
	public void testFrepG2H3b() throws IOException {
		String frepG2H3b =
		// Replace all nodes g(_,_) by h(_,_,_)
		// Using replacement rule
			
		"tree frepG2H3b(tree T) {" +
		"   return visit (T) {" +
		"      case g(value T1, value T2) => h(T1, T2, 0)" +
		"    };" +
		"}";
		
		assertTrue(tf.runTest("{" + frepG2H3b + "frepG2H3b(f(3)) == f(3);}"));
		assertTrue(tf.runTest("{" + frepG2H3b + "frepG2H3b(f(1,2,3)) == f(1,2,3);}"));
		assertTrue(tf.runTest("{" + frepG2H3b + "frepG2H3b(f(1,g(2,3))) == f(1,h(2,3,0));}"));
		assertTrue(tf.runTest("{" + frepG2H3b + "frepG2H3b(f(1,g(2,[3,4,5]))) == f(1,h(2,[3,4,5],0));}"));
		assertTrue(tf.runTest("{" + frepG2H3b + "frepG2H3b(f(1,g(2,{3,4,5}))) == f(1,h(2,{3,4,5},0));}"));
		assertTrue(tf.runTest("{" + frepG2H3b + "frepG2H3b(f(1,g(2,<3,4,5>))) == f(1,h(2,<3,4,5>,0));}"));
		assertTrue(tf.runTest("{" + frepG2H3b + "frepG2H3b(f(1,g(2,{<1,10>,<2,20>}))) == f(1,h(2,{<1,10>,<2,20>},0));}"));
		//assertTrue(tf.runTest("{" + frepG2H3b + "frepG2H3b(f(1,g(2,(1:10,2:20)))) == 6;}"));

	}
	
}

