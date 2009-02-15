package test;

import org.junit.Test;

public class VisitTests extends TestFramework {
	
	@Test
	public void testCnt()  {
		String cnt =
		"int cnt(NODE1 T) {" +
		"   int C = 0;" +
		"   visit(T) {" +
		"      case int N: C = C + 1;" +
		"    };" +
		"    return C;" +
		"}";
		
		prepare("data NODE1 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2);");
		
		assertTrue(runTestInSameEvaluator("{" + cnt + "cnt(f(3)) == 1;}"));
		assertTrue(runTestInSameEvaluator("{" + cnt + "cnt(f(1,2,3)) == 3;}"));
		assertTrue(runTestInSameEvaluator("{" + cnt + "cnt(f(1,g(2,3))) == 3;}"));
		assertTrue(runTestInSameEvaluator("{" + cnt + "cnt(f(1,g(2,[3,4,5]))) == 5;}"));
		assertTrue(runTestInSameEvaluator("{" + cnt + "cnt(f(1,g(2,{3,4,5}))) == 5;}"));
		assertTrue(runTestInSameEvaluator("{" + cnt + "cnt(f(1,g(2,<3,4,5>))) == 5;}"));
		assertTrue(runTestInSameEvaluator("{" + cnt + "cnt(f(1,g(2,{<1,10>,<2,20>}))) == 6;}"));
		assertTrue(runTestInSameEvaluator("{" + cnt + "cnt(f(1,g(2,(1:10,2:20)))) == 6;}"));
	}
	
	@Test
	public void testDrepl()  {
		String drepl =
			
		// Deep replacement of g by h
			
		"NODE2 drepl(NODE2 T) {" +
		"    return bottom-up-break visit (T) {" +
		"      case g(value T1, value T2) =>  h(T1, T2)" +
		"    };" +
		"}";
		
		prepare("data NODE2 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2);");

		assertTrue(runTestInSameEvaluator("{" + drepl + "drepl(f(3)) == f(3);}"));
		assertTrue(runTestInSameEvaluator("{" + drepl + "drepl(g(1,2)) == h(1,2);}"));
		assertTrue(runTestInSameEvaluator("{" + drepl + "drepl(g(1,f(g(2,3)))) == g(1,f(h(2,3)));}"));
		assertTrue(runTestInSameEvaluator("{" + drepl + "drepl(g(1,f([g(2,3),4,5]))) == g(1,f([h(2,3),4,5]));}"));
	}
	
	@Test
	public void testFrepA()  {
		String frepa =
		// Replace all nodes g(_,_) by h(_,_)
		// Using insert

		"NODE3 frepa(NODE3 T) { " +
		"    return visit (T) {" +
		"      case g(value T1, value T2):" +
		"           insert h(T1, T2);" +
		"    };" +
		"}";
		
		prepare("data NODE3 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2);");

		
		assertTrue(runTestInSameEvaluator("{" + frepa + "frepa(f(3)) == f(3);}"));
		assertTrue(runTestInSameEvaluator("{" + frepa + "frepa(f(1,2,3)) == f(1,2,3);}"));
		assertTrue(runTestInSameEvaluator("{" + frepa + "frepa(f(1,g(2,3))) == f(1,h(2,3));}"));
		assertTrue(runTestInSameEvaluator("{" + frepa + "frepa(f(1,g(2,[3,4,5]))) == f(1,h(2,[3,4,5]));}"));
		assertTrue(runTestInSameEvaluator("{" + frepa + "frepa(f(1,g(2,{3,4,5}))) == f(1,h(2,{3,4,5}));}"));
		assertTrue(runTestInSameEvaluator("{" + frepa + "frepa(f(1,g(2,<3,4,5>))) == f(1,h(2,<3,4,5>));}"));
		assertTrue(runTestInSameEvaluator("{" + frepa + "frepa(f(1,g(2,{<1,10>,<2,20>}))) == f(1,h(2,{<1,10>,<2,20>}));}"));
		assertTrue(runTestInSameEvaluator("{" + frepa + "frepa(f(1,g(2,(1:10,2:20)))) == f(1,h(2,(1:10,2:20)));}"));
	}
	
	@Test
	public void testFrepB()  {
		String frepb =
		// Replace all nodes g(_,_) by h(_,_)
		// Using replacement rule

		"NODE4 frepb(NODE4 T) {" +
		"    return visit (T) {" +
		"      case g(value T1, value T2) => h(T1, T2)" +
		"    };" +
		"}";
		
		prepare("data NODE4 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2);");
		
		assertTrue(runTestInSameEvaluator("{" + frepb + "frepb(f(3)) == f(3);}"));
		assertTrue(runTestInSameEvaluator("{" + frepb + "frepb(f(1,2,3)) == f(1,2,3);}"));
		assertTrue(runTestInSameEvaluator("{" + frepb + "frepb(f(1,g(2,3))) == f(1,h(2,3));}"));
		assertTrue(runTestInSameEvaluator("{" + frepb + "frepb(f(1,g(2,[3,4,5]))) == f(1,h(2,[3,4,5]));}"));
		assertTrue(runTestInSameEvaluator("{" + frepb + "frepb(f(1,g(2,{3,4,5}))) == f(1,h(2,{3,4,5}));}"));
		assertTrue(runTestInSameEvaluator("{" + frepb + "frepb(f(1,g(2,<3,4,5>))) == f(1,h(2,<3,4,5>));}"));
		assertTrue(runTestInSameEvaluator("{" + frepb + "frepb(f(1,g(2,{<1,10>,<2,20>}))) == f(1,h(2,{<1,10>,<2,20>}));}"));
		assertTrue(runTestInSameEvaluator("{" + frepb + "frepb(f(1,g(2,(1:10,2:20)))) == f(1,h(2,(1:10,2:20)));}"));

	}
	
	@Test
	public void testFrepG2H3a()  {
		String frepG2H3a =
		// Replace all nodes g(_,_) by h(_,_,_)
		// Using insert

		"NODE5 frepG2H3a(NODE5 T) {" +
		"    return visit (T) {" +
		"      case g(value T1, value T2):" +
		"           insert h(T1, T2, 0);" +
		"    };" +
		"}";
		
		prepare("data NODE5 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2) | h(value V1, value V2, value V3);");

		
		assertTrue(runTestInSameEvaluator("{" + frepG2H3a + "frepG2H3a(f(3)) == f(3);}"));
		assertTrue(runTestInSameEvaluator("{" + frepG2H3a + "frepG2H3a(f(1,2,3)) == f(1,2,3);}"));
		assertTrue(runTestInSameEvaluator("{" + frepG2H3a + "frepG2H3a(f(1,g(2,3))) == f(1,h(2,3,0));}"));
		assertTrue(runTestInSameEvaluator("{" + frepG2H3a + "frepG2H3a(f(1,g(2,[3,4,5]))) == f(1,h(2,[3,4,5],0));}"));
		assertTrue(runTestInSameEvaluator("{" + frepG2H3a + "frepG2H3a(f(1,g(2,{3,4,5}))) == f(1,h(2,{3,4,5},0));}"));
		assertTrue(runTestInSameEvaluator("{" + frepG2H3a + "frepG2H3a(f(1,g(2,<3,4,5>))) == f(1,h(2,<3,4,5>,0));}"));
		assertTrue(runTestInSameEvaluator("{" + frepG2H3a + "frepG2H3a(f(1,g(2,{<1,10>,<2,20>}))) == f(1,h(2,{<1,10>,<2,20>},0));}"));
		assertTrue(runTestInSameEvaluator("{" + frepG2H3a + "frepG2H3a(f(1,g(2,(1:10,2:20)))) == f(1,h(2,(1:10,2:20), 0));}"));
	}
	
	@Test
	public void testFrepG2H3b()  {
		String frepG2H3b =
		// Replace all nodes g(_,_) by h(_,_,_)
		// Using replacement rule
			
		"NODE6 frepG2H3b(NODE6 T) {" +
		"   return visit (T) {" +
		"      case g(value T1, value T2) => h(T1, T2, 0)" +
		"    };" +
		"}";
		
		prepare("data NODE6 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2) | h(value V1, value V2, value V3);");
		
		assertTrue(runTestInSameEvaluator("{" + frepG2H3b + "frepG2H3b(f(3)) == f(3);}"));
		assertTrue(runTestInSameEvaluator("{" + frepG2H3b + "frepG2H3b(f(1,2,3)) == f(1,2,3);}"));
		assertTrue(runTestInSameEvaluator("{" + frepG2H3b + "frepG2H3b(f(1,g(2,3))) == f(1,h(2,3,0));}"));
		assertTrue(runTestInSameEvaluator("{" + frepG2H3b + "frepG2H3b(f(1,g(2,[3,4,5]))) == f(1,h(2,[3,4,5],0));}"));
		assertTrue(runTestInSameEvaluator("{" + frepG2H3b + "frepG2H3b(f(1,g(2,{3,4,5}))) == f(1,h(2,{3,4,5},0));}"));
		assertTrue(runTestInSameEvaluator("{" + frepG2H3b + "frepG2H3b(f(1,g(2,<3,4,5>))) == f(1,h(2,<3,4,5>,0));}"));
		assertTrue(runTestInSameEvaluator("{" + frepG2H3b + "frepG2H3b(f(1,g(2,{<1,10>,<2,20>}))) == f(1,h(2,{<1,10>,<2,20>},0));}"));
		assertTrue(runTestInSameEvaluator("{" + frepG2H3b + "frepG2H3b(f(1,g(2,(1:10,2:20)))) == f(1,h(2,(1:10,2:20), 0));}"));
	}
	
	@Test
	public void testInc()  {
		String inc =
		"	NODE7 inc(NODE7 T) {" +
		"    return visit(T) {" +
		"      case int N: insert N + 1;" +
		"    };" + 
		"}";
		
		prepare("data NODE7 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2);");

		assertTrue(runTestInSameEvaluator("{" + inc + "inc(f(3)) == f(4);}"));
		assertTrue(runTestInSameEvaluator("{" + inc + "inc(f(1,2,3)) == f(2,3,4);}"));
		assertTrue(runTestInSameEvaluator("{" + inc + "inc(f(1,g(2,3))) == f(2,g(3,4));}"));
		assertTrue(runTestInSameEvaluator("{" + inc + "inc(f(1,g(2,[3,4,5]))) == f(2,g(3,[4,5,6]));}"));
		assertTrue(runTestInSameEvaluator("{" + inc + "inc(f(1,g(2,{3,4,5}))) == f(2,g(3,{4,5,6}));}"));
		assertTrue(runTestInSameEvaluator("{" + inc + "inc(f(1,g(2,<3,4,5>))) == f(2,g(3,<4,5,6>));}"));
		assertTrue(runTestInSameEvaluator("{" + inc + "inc(f(1,g(2,{<1,10>,<2,20>}))) == f(2,g(3,{<2,11>,<3,21>}));}"));
		assertTrue(runTestInSameEvaluator("{" + inc + "inc(f(1,g(2,(1:10,2:20)))) == f(2,g(3,(2:11,3:21)));}"));
	}
	
	@Test
	public void testIncAndCount()  {
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
		
		prepare("data NODE8 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2);");

		assertTrue(runTestInSameEvaluator("{" + inc_and_count + "inc_and_count(f(3),10)                       == <1,f(13)>;}"));
		assertTrue(runTestInSameEvaluator("{" + inc_and_count + "inc_and_count(f(1,2,3), 10)                  == <3,f(11,12,13)>;}"));
		assertTrue(runTestInSameEvaluator("{" + inc_and_count + "inc_and_count(f(1,g(2,3)), 10)               == <3, f(11,g(12,13))>;}"));
		assertTrue(runTestInSameEvaluator("{" + inc_and_count + "inc_and_count(f(1,g(2,[3,4,5])), 10)         == <5,f(11,g(12,[13,14,15]))>;}"));
		assertTrue(runTestInSameEvaluator("{" + inc_and_count + "inc_and_count(f(1,g(2,{3,4,5})), 10)         == <5,f(11,g(12,{13,14,15}))>;}"));
		assertTrue(runTestInSameEvaluator("{" + inc_and_count + "inc_and_count(f(1,g(2,<3,4,5>)), 10)         == <5,f(11,g(12,<13,14,15>))>;}"));
		assertTrue(runTestInSameEvaluator("{" + inc_and_count + "inc_and_count(f(1,g(2,{<1,10>,<2,20>})), 10) == <6,f(11,g(12,{<11,20>,<12,30>}))>;}"));
		assertTrue(runTestInSameEvaluator("{" + inc_and_count + "inc_and_count(f(1,g(2,(1:10,2:20))),10)      == <6, f(11,g(12,(11:20,12:30)))>;}"));
	}
	
	@Test
	public void testSrepl()  {
		String srepl =
		// Ex6: shallow replacement of g by h (i.e. only outermost 
		// g's are replaced); 

		"NODE9 srepl(NODE9 T) {" +
		"    return top-down-break visit (T) {" +
		"       case g(value T1, value T2) =>  h(T1, T2)" +
		"    };" +
		"}";
		
		prepare("data NODE9 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2);");
	
		assertTrue(runTestInSameEvaluator("{" + srepl + "srepl(f(3)) == f(3);}"));
		assertTrue(runTestInSameEvaluator("{" + srepl + "srepl(g(1,2)) == h(1,2);}"));
		assertTrue(runTestInSameEvaluator("{" + srepl + "srepl(g(1,f(g(2,3)))) == h(1,f(g(2,3)));}"));
		assertTrue(runTestInSameEvaluator("{" + srepl + "srepl(g(1,f([g(2,3),4,5]))) == h(1,f([g(2,3),4,5]));}"));
	}
	
	@Test
	public void testStringVisit1a()  {

		assertTrue(runTestInSameEvaluator("visit(\"\"){ case /b/: insert \"B\";} == \"\";"));
		assertTrue(runTestInSameEvaluator("visit(\"a\"){ case /b/: insert \"B\";} == \"a\";"));
		assertTrue(runTestInSameEvaluator("visit(\"b\"){ case /b/: insert \"B\";} == \"B\";"));
		assertTrue(runTestInSameEvaluator("visit(\"abc\"){ case /b/: insert \"B\";} == \"aBc\";"));
		assertTrue(runTestInSameEvaluator("visit(\"abcabc\"){ case /b/: insert \"B\";} == \"aBcaBc\";"));
	}
	
	@Test
	public void testStringVisit1b()  {
		
		assertTrue(runTestInSameEvaluator("visit(\"\"){ case /b/ => \"B\"} == \"\";"));
		assertTrue(runTestInSameEvaluator("visit(\"a\"){ case /b/ => \"B\"} == \"a\";"));
		assertTrue(runTestInSameEvaluator("visit(\"b\"){ case /b/ => \"B\"} == \"B\";"));
		assertTrue(runTestInSameEvaluator("visit(\"abc\"){ case /b/ => \"B\"} == \"aBc\";"));
		assertTrue(runTestInSameEvaluator("visit(\"abcabc\"){ case /b/ =>\"B\"} == \"aBcaBc\";"));
	}
	
	@Test
	public void testStringVisit2()  {
		
		assertTrue(runTestInSameEvaluator("visit(\"\"){ case /b/: insert \"BB\";} == \"\";"));
		assertTrue(runTestInSameEvaluator("visit(\"a\"){ case /b/: insert \"BB\";} == \"a\";"));
		assertTrue(runTestInSameEvaluator("visit(\"b\"){ case /b/: insert \"BB\";} == \"BB\";"));
		assertTrue(runTestInSameEvaluator("visit(\"abc\"){ case /b/: insert \"B\";} == \"aBc\";"));
		assertTrue(runTestInSameEvaluator("visit(\"abcabc\"){ case /b/: insert \"BB\";} == \"aBBcaBBc\";"));
	}
	
	@Test
	public void testStringVisit3()  {
		
		assertTrue(runTestInSameEvaluator("visit(\"\"){ case /^a/: insert \"AA\"; case /^b/: insert \"BB\";} == \"\";"));
		assertTrue(runTestInSameEvaluator("visit(\"a\"){ case /^a/: insert \"AA\"; case /^b/: insert \"BB\";} == \"AA\";"));
		assertTrue(runTestInSameEvaluator("visit(\"b\"){ case /^a/: insert \"AA\"; case /^b/: insert \"BB\";} == \"BB\";"));
		assertTrue(runTestInSameEvaluator("visit(\"abcabc\"){ case /^a/: insert \"AA\"; case /^b/: insert \"BB\";} == \"AABBcAABBc\";"));
		assertTrue(runTestInSameEvaluator("visit(\"abcabca\"){ case /^a/: insert \"AA\"; case /^b/: insert \"BB\";} == \"AABBcAABBcAA\";"));

	}
	
	@Test
	public void testStringVisit4()  {
		
		assertTrue(runTestInSameEvaluator("visit(\"\"){ case \"a\": insert \"AA\"; case /b/: insert \"BB\";} == \"\";"));
		assertTrue(runTestInSameEvaluator("visit(\"a\"){ case \"a\": insert \"AA\"; case /b/: insert \"BB\";} == \"AA\";"));
		assertTrue(runTestInSameEvaluator("visit(\"b\"){ case \"a\": insert \"AA\"; case /b/: insert \"BB\";} == \"BB\";"));
		assertTrue(runTestInSameEvaluator("visit(\"abcabc\"){ case \"a\": insert \"AA\"; case /b/: insert \"BB\";} == \"aBBcaBBc\";"));
		assertTrue(runTestInSameEvaluator("visit(\"abcabca\"){ case \"a\": insert \"AA\"; case /b/: insert \"BB\";} == \"aBBcaBBcAA\";"));

	}
}


