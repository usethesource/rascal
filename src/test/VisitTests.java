package test;

import java.io.IOException;

import junit.framework.TestCase;

public class VisitTests extends TestCase {
	private static TestFramework tf = new TestFramework();
	
	public void testCnt() throws IOException {
		String cnt =
		"int cnt(NODE1 T) {" +
		"   int C = 0;" +
		"   visit(T) {" +
		"      case int N: C = C + 1;" +
		"    };" +
		"    return C;" +
		"}";
		
		tf = new TestFramework("data NODE1 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2);");
		
		assertTrue(tf.runTestInSameEvaluator("{" + cnt + "cnt(f(3)) == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{" + cnt + "cnt(f(1,2,3)) == 3;}"));
		assertTrue(tf.runTestInSameEvaluator("{" + cnt + "cnt(f(1,g(2,3))) == 3;}"));
		assertTrue(tf.runTestInSameEvaluator("{" + cnt + "cnt(f(1,g(2,[3,4,5]))) == 5;}"));
		assertTrue(tf.runTestInSameEvaluator("{" + cnt + "cnt(f(1,g(2,{3,4,5}))) == 5;}"));
		assertTrue(tf.runTestInSameEvaluator("{" + cnt + "cnt(f(1,g(2,<3,4,5>))) == 5;}"));
		assertTrue(tf.runTestInSameEvaluator("{" + cnt + "cnt(f(1,g(2,{<1,10>,<2,20>}))) == 6;}"));
		assertTrue(tf.runTestInSameEvaluator("{" + cnt + "cnt(f(1,g(2,(1:10,2:20)))) == 6;}"));
	}
	
	public void testDrepl() throws IOException {
		String drepl =
			
		// Deep replacement of g by h
			
		"NODE2 drepl(NODE2 T) {" +
		"    return bottom-up-break visit (T) {" +
		"      case g(value T1, value T2) =>  h(T1, T2)" +
		"    };" +
		"}";
		
		tf = new TestFramework("data NODE2 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2);");

		
		assertTrue(tf.runTestInSameEvaluator("{" + drepl + "drepl(f(3)) == f(3);}"));
		assertTrue(tf.runTestInSameEvaluator("{" + drepl + "drepl(g(1,2)) == h(1,2);}"));
		assertTrue(tf.runTestInSameEvaluator("{" + drepl + "drepl(g(1,f(g(2,3)))) == g(1,f(h(2,3)));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + drepl + "drepl(g(1,f([g(2,3),4,5]))) == g(1,f([h(2,3),4,5]));}"));
	}
	
	public void testFrepA() throws IOException {
		String frepa =
		// Replace all nodes g(_,_) by h(_,_)
		// Using insert

		"NODE3 frepa(NODE3 T) { " +
		"    return visit (T) {" +
		"      case g(value T1, value T2):" +
		"           insert h(T1, T2);" +
		"    };" +
		"}";
		
		tf = new TestFramework("data NODE3 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2);");

		
		assertTrue(tf.runTestInSameEvaluator("{" + frepa + "frepa(f(3)) == f(3);}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepa + "frepa(f(1,2,3)) == f(1,2,3);}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepa + "frepa(f(1,g(2,3))) == f(1,h(2,3));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepa + "frepa(f(1,g(2,[3,4,5]))) == f(1,h(2,[3,4,5]));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepa + "frepa(f(1,g(2,{3,4,5}))) == f(1,h(2,{3,4,5}));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepa + "frepa(f(1,g(2,<3,4,5>))) == f(1,h(2,<3,4,5>));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepa + "frepa(f(1,g(2,{<1,10>,<2,20>}))) == f(1,h(2,{<1,10>,<2,20>}));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepa + "frepa(f(1,g(2,(1:10,2:20)))) == f(1,h(2,(1:10,2:20)));}"));
	}
	
	public void testFrepB() throws IOException {
		String frepb =
		// Replace all nodes g(_,_) by h(_,_)
		// Using replacement rule

		"NODE4 frepb(NODE4 T) {" +
		"    return visit (T) {" +
		"      case g(value T1, value T2) => h(T1, T2)" +
		"    };" +
		"}";
		
		tf = new TestFramework("data NODE4 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2);");

		
		assertTrue(tf.runTestInSameEvaluator("{" + frepb + "frepb(f(3)) == f(3);}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepb + "frepb(f(1,2,3)) == f(1,2,3);}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepb + "frepb(f(1,g(2,3))) == f(1,h(2,3));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepb + "frepb(f(1,g(2,[3,4,5]))) == f(1,h(2,[3,4,5]));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepb + "frepb(f(1,g(2,{3,4,5}))) == f(1,h(2,{3,4,5}));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepb + "frepb(f(1,g(2,<3,4,5>))) == f(1,h(2,<3,4,5>));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepb + "frepb(f(1,g(2,{<1,10>,<2,20>}))) == f(1,h(2,{<1,10>,<2,20>}));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepb + "frepb(f(1,g(2,(1:10,2:20)))) == f(1,h(2,(1:10,2:20)));}"));

	}
	
	public void testFrepG2H3a() throws IOException {
		String frepG2H3a =
		// Replace all nodes g(_,_) by h(_,_,_)
		// Using insert

		"NODE5 frepG2H3a(NODE5 T) {" +
		"    return visit (T) {" +
		"      case g(value T1, value T2):" +
		"           insert h(T1, T2, 0);" +
		"    };" +
		"}";
		
		tf = new TestFramework("data NODE5 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2) | h(value V1, value V2, value V3);");

		
		assertTrue(tf.runTestInSameEvaluator("{" + frepG2H3a + "frepG2H3a(f(3)) == f(3);}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepG2H3a + "frepG2H3a(f(1,2,3)) == f(1,2,3);}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepG2H3a + "frepG2H3a(f(1,g(2,3))) == f(1,h(2,3,0));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepG2H3a + "frepG2H3a(f(1,g(2,[3,4,5]))) == f(1,h(2,[3,4,5],0));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepG2H3a + "frepG2H3a(f(1,g(2,{3,4,5}))) == f(1,h(2,{3,4,5},0));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepG2H3a + "frepG2H3a(f(1,g(2,<3,4,5>))) == f(1,h(2,<3,4,5>,0));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepG2H3a + "frepG2H3a(f(1,g(2,{<1,10>,<2,20>}))) == f(1,h(2,{<1,10>,<2,20>},0));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepG2H3a + "frepG2H3a(f(1,g(2,(1:10,2:20)))) == f(1,h(2,(1:10,2:20), 0));}"));
	}
	
	public void testFrepG2H3b() throws IOException {
		String frepG2H3b =
		// Replace all nodes g(_,_) by h(_,_,_)
		// Using replacement rule
			
		"NODE6 frepG2H3b(NODE6 T) {" +
		"   return visit (T) {" +
		"      case g(value T1, value T2) => h(T1, T2, 0)" +
		"    };" +
		"}";
		
		tf = new TestFramework("data NODE6 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2) | h(value V1, value V2, value V3);");

		
		assertTrue(tf.runTestInSameEvaluator("{" + frepG2H3b + "frepG2H3b(f(3)) == f(3);}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepG2H3b + "frepG2H3b(f(1,2,3)) == f(1,2,3);}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepG2H3b + "frepG2H3b(f(1,g(2,3))) == f(1,h(2,3,0));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepG2H3b + "frepG2H3b(f(1,g(2,[3,4,5]))) == f(1,h(2,[3,4,5],0));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepG2H3b + "frepG2H3b(f(1,g(2,{3,4,5}))) == f(1,h(2,{3,4,5},0));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepG2H3b + "frepG2H3b(f(1,g(2,<3,4,5>))) == f(1,h(2,<3,4,5>,0));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepG2H3b + "frepG2H3b(f(1,g(2,{<1,10>,<2,20>}))) == f(1,h(2,{<1,10>,<2,20>},0));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + frepG2H3b + "frepG2H3b(f(1,g(2,(1:10,2:20)))) == f(1,h(2,(1:10,2:20), 0));}"));
	}
	
	public void testInc() throws IOException {
		String inc =
		"	NODE7 inc(NODE7 T) {" +
		"    return visit(T) {" +
		"      case int N: insert N + 1;" +
		"    };" + 
		"}";
		
		tf = new TestFramework("data NODE7 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2);");

		
		assertTrue(tf.runTestInSameEvaluator("{" + inc + "inc(f(3)) == f(4);}"));
		assertTrue(tf.runTestInSameEvaluator("{" + inc + "inc(f(1,2,3)) == f(2,3,4);}"));
		assertTrue(tf.runTestInSameEvaluator("{" + inc + "inc(f(1,g(2,3))) == f(2,g(3,4));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + inc + "inc(f(1,g(2,[3,4,5]))) == f(2,g(3,[4,5,6]));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + inc + "inc(f(1,g(2,{3,4,5}))) == f(2,g(3,{4,5,6}));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + inc + "inc(f(1,g(2,<3,4,5>))) == f(2,g(3,<4,5,6>));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + inc + "inc(f(1,g(2,{<1,10>,<2,20>}))) == f(2,g(3,{<2,11>,<3,21>}));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + inc + "inc(f(1,g(2,(1:10,2:20)))) == f(2,g(3,(2:11,3:21)));}"));
	}
	
	public void testIncAndCount() throws IOException {
		String inc_and_count =
		// Accumulating transformer that increments integer leaves with 
		// amount D and counts them as well.

		"tuple[int, NODE8] inc_and_count(NODE8 T, int D) {" +
		"    int C = 0;" +  
		"    T = visit (T) {" +
		"        case int N: { C = C + 1; " +
		"                      insert N + D;" +
		"                    }" +
		"        };" +
		"    return <C, T>;" +
		"}";
		
		tf = new TestFramework("data NODE8 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2);");

		
		assertTrue(tf.runTestInSameEvaluator("{" + inc_and_count + "inc_and_count(f(3),10)                       == <1,f(13)>;}"));
		assertTrue(tf.runTestInSameEvaluator("{" + inc_and_count + "inc_and_count(f(1,2,3), 10)                  == <3,f(11,12,13)>;}"));
		assertTrue(tf.runTestInSameEvaluator("{" + inc_and_count + "inc_and_count(f(1,g(2,3)), 10)               == <3, f(11,g(12,13))>;}"));
		assertTrue(tf.runTestInSameEvaluator("{" + inc_and_count + "inc_and_count(f(1,g(2,[3,4,5])), 10)         == <5,f(11,g(12,[13,14,15]))>;}"));
		assertTrue(tf.runTestInSameEvaluator("{" + inc_and_count + "inc_and_count(f(1,g(2,{3,4,5})), 10)         == <5,f(11,g(12,{13,14,15}))>;}"));
		assertTrue(tf.runTestInSameEvaluator("{" + inc_and_count + "inc_and_count(f(1,g(2,<3,4,5>)), 10)         == <5,f(11,g(12,<13,14,15>))>;}"));
		assertTrue(tf.runTestInSameEvaluator("{" + inc_and_count + "inc_and_count(f(1,g(2,{<1,10>,<2,20>})), 10) == <6,f(11,g(12,{<11,20>,<12,30>}))>;}"));
		assertTrue(tf.runTestInSameEvaluator("{" + inc_and_count + "inc_and_count(f(1,g(2,(1:10,2:20))),10)      == <6, f(11,g(12,(11:20,12:30)))>;}"));
	}
	
	public void testSrepl() throws IOException {
		String srepl =
		// Ex6: shallow replacement of g by h (i.e. only outermost 
		// g's are replaced); 

		"NODE9 srepl(NODE9 T) {" +
		"    return top-down-break visit (T) {" +
		"       case g(value T1, value T2) =>  h(T1, T2)" +
		"    };" +
		"}";
		
		tf = new TestFramework("data NODE9 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2);");
	
		assertTrue(tf.runTestInSameEvaluator("{" + srepl + "srepl(f(3)) == f(3);}"));
		assertTrue(tf.runTestInSameEvaluator("{" + srepl + "srepl(g(1,2)) == h(1,2);}"));
		assertTrue(tf.runTestInSameEvaluator("{" + srepl + "srepl(g(1,f(g(2,3)))) == h(1,f(g(2,3)));}"));
		assertTrue(tf.runTestInSameEvaluator("{" + srepl + "srepl(g(1,f([g(2,3),4,5]))) == h(1,f([g(2,3),4,5]));}"));
	}
	
	public void testStringVisit1a() throws IOException {
		tf = new TestFramework();

		assertTrue(tf.runTestInSameEvaluator("visit(\"\"){ case /b/: insert \"B\";} == \"\";"));
		assertTrue(tf.runTestInSameEvaluator("visit(\"a\"){ case /b/: insert \"B\";} == \"a\";"));
		assertTrue(tf.runTestInSameEvaluator("visit(\"b\"){ case /b/: insert \"B\";} == \"B\";"));
		assertTrue(tf.runTestInSameEvaluator("visit(\"abc\"){ case /b/: insert \"B\";} == \"aBc\";"));
		assertTrue(tf.runTestInSameEvaluator("visit(\"abcabc\"){ case /b/: insert \"B\";} == \"aBcaBc\";"));
	}
	
	public void testStringVisit1b() throws IOException {
		
		tf = new TestFramework();
		
		assertTrue(tf.runTestInSameEvaluator("visit(\"\"){ case /b/ => \"B\"} == \"\";"));
		assertTrue(tf.runTestInSameEvaluator("visit(\"a\"){ case /b/ => \"B\"} == \"a\";"));
		assertTrue(tf.runTestInSameEvaluator("visit(\"b\"){ case /b/ => \"B\"} == \"B\";"));
		assertTrue(tf.runTestInSameEvaluator("visit(\"abc\"){ case /b/ => \"B\"} == \"aBc\";"));
		assertTrue(tf.runTestInSameEvaluator("visit(\"abcabc\"){ case /b/ =>\"B\"} == \"aBcaBc\";"));
	}
	
	public void testStringVisit2() throws IOException {
		
		tf = new TestFramework();
		
		assertTrue(tf.runTestInSameEvaluator("visit(\"\"){ case /b/: insert \"BB\";} == \"\";"));
		assertTrue(tf.runTestInSameEvaluator("visit(\"a\"){ case /b/: insert \"BB\";} == \"a\";"));
		assertTrue(tf.runTestInSameEvaluator("visit(\"b\"){ case /b/: insert \"BB\";} == \"BB\";"));
		assertTrue(tf.runTestInSameEvaluator("visit(\"abc\"){ case /b/: insert \"B\";} == \"aBc\";"));
		assertTrue(tf.runTestInSameEvaluator("visit(\"abcabc\"){ case /b/: insert \"BB\";} == \"aBBcaBBc\";"));
	}
	
	public void testStringVisit3() throws IOException {
		
		tf = new TestFramework();
		
		assertTrue(tf.runTestInSameEvaluator("visit(\"\"){ case /^a/: insert \"AA\"; case /^b/: insert \"BB\";} == \"\";"));
		assertTrue(tf.runTestInSameEvaluator("visit(\"a\"){ case /^a/: insert \"AA\"; case /^b/: insert \"BB\";} == \"AA\";"));
		assertTrue(tf.runTestInSameEvaluator("visit(\"b\"){ case /^a/: insert \"AA\"; case /^b/: insert \"BB\";} == \"BB\";"));
		assertTrue(tf.runTestInSameEvaluator("visit(\"abcabc\"){ case /^a/: insert \"AA\"; case /^b/: insert \"BB\";} == \"AABBcAABBc\";"));
		assertTrue(tf.runTestInSameEvaluator("visit(\"abcabca\"){ case /^a/: insert \"AA\"; case /^b/: insert \"BB\";} == \"AABBcAABBcAA\";"));

	}
	
	public void testStringVisit4() throws IOException {
		
		tf = new TestFramework();
		
		assertTrue(tf.runTestInSameEvaluator("visit(\"\"){ case \"a\": insert \"AA\"; case /b/: insert \"BB\";} == \"\";"));
		assertTrue(tf.runTestInSameEvaluator("visit(\"a\"){ case \"a\": insert \"AA\"; case /b/: insert \"BB\";} == \"AA\";"));
		assertTrue(tf.runTestInSameEvaluator("visit(\"b\"){ case \"a\": insert \"AA\"; case /b/: insert \"BB\";} == \"BB\";"));
		assertTrue(tf.runTestInSameEvaluator("visit(\"abcabc\"){ case \"a\": insert \"AA\"; case /b/: insert \"BB\";} == \"aBBcaBBc\";"));
		assertTrue(tf.runTestInSameEvaluator("visit(\"abcabca\"){ case \"a\": insert \"AA\"; case /b/: insert \"BB\";} == \"aBBcaBBcAA\";"));

	}
}


