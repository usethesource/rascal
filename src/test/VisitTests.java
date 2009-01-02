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
		assertTrue(tf.runTest("{" + cnt + "cnt(f(1,g(2,(1:10,2:20)))) == 6;}"));
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
		assertTrue(tf.runTest("{" + inc + "inc(f(1,g(2,(1:10,2:20)))) == f(2,g(3,(2:11,3:21)));}"));
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
		assertTrue(tf.runTest("{" + frepa + "frepa(f(1,g(2,(1:10,2:20)))) == f(1,h(2,(1:10,2:20)));}"));
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
		assertTrue(tf.runTest("{" + frepb + "frepb(f(1,g(2,(1:10,2:20)))) == f(1,h(2,(1:10,2:20)));}"));

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
		assertTrue(tf.runTest("{" + frepG2H3a + "frepG2H3a(f(1,g(2,(1:10,2:20)))) == f(1,h(2,(1:10,2:20), 0));}"));
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
		assertTrue(tf.runTest("{" + frepG2H3b + "frepG2H3b(f(1,g(2,(1:10,2:20)))) == f(1,h(2,(1:10,2:20), 0));}"));
	}
	
	public void testIncAndCount() throws IOException {
		String inc_and_count =
		// Accumulating transformer that increments integer leaves with 
		// amount D and counts them as well.

		"tuple[int, tree] inc_and_count(tree T, int D) {" +
		"    int C = 0;" +  
		"    T = visit (T) {" +
		"        case int N: { C = C + 1; " +
		"                      insert N + D;" +
		"                    }" +
		"        };" +
		"    return <C, T>;" +
		"}";
		
		assertTrue(tf.runTest("{" + inc_and_count + "inc_and_count(f(3),10)                       == <1,f(13)>;}"));
		assertTrue(tf.runTest("{" + inc_and_count + "inc_and_count(f(1,2,3), 10)                  == <3,f(11,12,13)>;}"));
		assertTrue(tf.runTest("{" + inc_and_count + "inc_and_count(f(1,g(2,3)), 10)               == <3, f(11,g(12,13))>;}"));
		assertTrue(tf.runTest("{" + inc_and_count + "inc_and_count(f(1,g(2,[3,4,5])), 10)         == <5,f(11,g(12,[13,14,15]))>;}"));
		assertTrue(tf.runTest("{" + inc_and_count + "inc_and_count(f(1,g(2,{3,4,5})), 10)         == <5,f(11,g(12,{13,14,15}))>;}"));
		assertTrue(tf.runTest("{" + inc_and_count + "inc_and_count(f(1,g(2,<3,4,5>)), 10)         == <5,f(11,g(12,<13,14,15>))>;}"));
		assertTrue(tf.runTest("{" + inc_and_count + "inc_and_count(f(1,g(2,{<1,10>,<2,20>})), 10) == <6,f(11,g(12,{<11,20>,<12,30>}))>;}"));
		assertTrue(tf.runTest("{" + inc_and_count + "inc_and_count(f(1,g(2,(1:10,2:20))),10)      == <6, f(11,g(12,(11:20,12:30)))>;}"));
	}
	
	public void testDrepl() throws IOException {
		String drepl =
			
		// Deep replacement of g by h
			
		"tree drepl(tree T) {" +
		"    return bottom-up-break visit (T) {" +
		"      case g(value T1, value T2) =>  h(T1, T2)" +
		"    };" +
		"}";
		
		assertTrue(tf.runTest("{" + drepl + "drepl(f(3)) == f(3);}"));
		assertTrue(tf.runTest("{" + drepl + "drepl(g(1,2)) == h(1,2);}"));
		assertTrue(tf.runTest("{" + drepl + "drepl(g(1,f(g(2,3)))) == g(1,f(h(2,3)));}"));
		assertTrue(tf.runTest("{" + drepl + "drepl(g(1,f([g(2,3),4,5]))) == g(1,f([h(2,3),4,5]));}"));
	}
	
	public void testSrepl() throws IOException {
		String srepl =
		// Ex6: shallow replacement of g by h (i.e. only outermost 
		// g's are replaced); 

		"tree srepl(tree T) {" +
		"    return top-down-break visit (T) {" +
		"       case g(value T1, value T2) =>  h(T1, T2)" +
		"    };" +
		"}";
		
		assertTrue(tf.runTest("{" + srepl + "srepl(f(3)) == f(3);}"));
		assertTrue(tf.runTest("{" + srepl + "srepl(g(1,2)) == h(1,2);}"));
		assertTrue(tf.runTest("{" + srepl + "srepl(g(1,f(g(2,3)))) == h(1,f(g(2,3)));}"));
		assertTrue(tf.runTest("{" + srepl + "srepl(g(1,f([g(2,3),4,5]))) == h(1,f([g(2,3),4,5]));}"));
	}
	
	public void testStringVisit1a() throws IOException {
		assertTrue(tf.runTest("visit(\"\"){ case /b/: insert \"B\";} == \"\";"));
		assertTrue(tf.runTest("visit(\"a\"){ case /b/: insert \"B\";} == \"a\";"));
		assertTrue(tf.runTest("visit(\"b\"){ case /b/: insert \"B\";} == \"B\";"));
		assertTrue(tf.runTest("visit(\"abc\"){ case /b/: insert \"B\";} == \"aBc\";"));
		assertTrue(tf.runTest("visit(\"abcabc\"){ case /b/: insert \"B\";} == \"aBcaBc\";"));
	}
	
	public void testStringVisit1b() throws IOException {
		assertTrue(tf.runTest("visit(\"\"){ case /b/ => \"B\"} == \"\";"));
		assertTrue(tf.runTest("visit(\"a\"){ case /b/ => \"B\"} == \"a\";"));
		assertTrue(tf.runTest("visit(\"b\"){ case /b/ => \"B\"} == \"B\";"));
		assertTrue(tf.runTest("visit(\"abc\"){ case /b/ => \"B\"} == \"aBc\";"));
		assertTrue(tf.runTest("visit(\"abcabc\"){ case /b/ =>\"B\"} == \"aBcaBc\";"));
	}
	
	public void testStringVisit2() throws IOException {
		assertTrue(tf.runTest("visit(\"\"){ case /b/: insert \"BB\";} == \"\";"));
		assertTrue(tf.runTest("visit(\"a\"){ case /b/: insert \"BB\";} == \"a\";"));
		assertTrue(tf.runTest("visit(\"b\"){ case /b/: insert \"BB\";} == \"BB\";"));
		assertTrue(tf.runTest("visit(\"abc\"){ case /b/: insert \"B\";} == \"aBc\";"));
		assertTrue(tf.runTest("visit(\"abcabc\"){ case /b/: insert \"BB\";} == \"aBBcaBBc\";"));
	}
	
	public void testStringVisit3() throws IOException {
		assertTrue(tf.runTest("visit(\"\"){ case /^a/: insert \"AA\"; case /^b/: insert \"BB\";} == \"\";"));
		assertTrue(tf.runTest("visit(\"a\"){ case /^a/: insert \"AA\"; case /^b/: insert \"BB\";} == \"AA\";"));
		assertTrue(tf.runTest("visit(\"b\"){ case /^a/: insert \"AA\"; case /^b/: insert \"BB\";} == \"BB\";"));
		assertTrue(tf.runTest("visit(\"abcabc\"){ case /^a/: insert \"AA\"; case /^b/: insert \"BB\";} == \"AABBcAABBc\";"));
		assertTrue(tf.runTest("visit(\"abcabca\"){ case /^a/: insert \"AA\"; case /^b/: insert \"BB\";} == \"AABBcAABBcAA\";"));

	}
	
	public void testStringVisit4() throws IOException {
		assertTrue(tf.runTest("visit(\"\"){ case \"a\": insert \"AA\"; case /b/: insert \"BB\";} == \"\";"));
		assertTrue(tf.runTest("visit(\"a\"){ case \"a\": insert \"AA\"; case /b/: insert \"BB\";} == \"AA\";"));
		assertTrue(tf.runTest("visit(\"b\"){ case \"a\": insert \"AA\"; case /b/: insert \"BB\";} == \"BB\";"));
		assertTrue(tf.runTest("visit(\"abcabc\"){ case \"a\": insert \"AA\"; case /b/: insert \"BB\";} == \"aBBcaBBc\";"));
		assertTrue(tf.runTest("visit(\"abcabca\"){ case \"a\": insert \"AA\"; case /b/: insert \"BB\";} == \"aBBcaBBcAA\";"));

	}
}


