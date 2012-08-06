/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.test.functionality;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.test.TestFramework;

public class VisitTests extends TestFramework {
	
	@Test
	public void Cnt()  {
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
	public void When()  {
		String cnt =
		"NODE1 cnt(NODE1 T) {" +
		"   return visit(T) {" +
		"      case int N => x when int x := N * 2, x >= 1" +
		"    };" +
		"}";
		
		prepare("data NODE1 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2);");
		
		assertTrue(runTestInSameEvaluator("{" + cnt + "cnt(f(3)) == f(6);}"));
		assertTrue(runTestInSameEvaluator("{" + cnt + "cnt(f(1,2,3)) == f(2,4,6);}"));
		assertTrue(runTestInSameEvaluator("{" + cnt + "cnt(f(1,g(2,3))) == f(2, g(4, 6));}"));
		assertTrue(runTestInSameEvaluator("{" + cnt + "cnt(f(1,g(2,[3,4,5]))) == f(2, g(4, [6, 8, 10]));}"));
	}
	
	@Test
	public void NewTreeVisibleBottomUp() {
		prepare("data T = knot(int i, T l, T r) | tip(int i);");
		assertTrue(runTestInSameEvaluator("{visit(knot(0,tip(0),tip(0))) { case tip(int i) => tip(i+1) case knot(int i, T l, T r) => knot(i + l.i + r.i, l, r) } == knot(2,tip(1),tip(1)); }"));
	}
	
	@Test
	public void Drepl()  {
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
		// The following test used to work, but now that we are using more and more static types it fails.
		// Explanation: [g(2,3),4,5] has as type list[value] and the elements have static type valeu as well.
		// In particular g(2,3) has type value.
		// As a result the node pattern g(value T1, value T2) in the case does not match.
		// assertTrue(runTestInSameEvaluator("{" + drepl + "drepl(g(1,f([g(2,3),4,5]))) == g(1,f([h(2,3),4,5]));}"));
	}
	
	@Test
	public void FrepA()  {
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
	public void FrepB()  {
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
	public void FrepG2H3a()  {
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
	public void FrepG2H3b()  {
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
	public void Inc()  {
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
	public void IncAndCount()  {
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
	public void Srepl()  {
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
	public void Order()  {
		String order =
		// Extract integers from structure 

		"list[int] order(NODE10 T) {" +
		"    res = [];" +
		"    visit (T) {" +
		"       case int N:  res += N;" +
		"    };" +
		"    return res;" +
		"}";
		
		prepare("data NODE10 = f(int I) | g(list[NODE10] L) | h(NODE10 N1, NODE10 N2);");
	
		assertTrue(runTestInSameEvaluator("{" + order + "order(f(3)) == [3];}"));
		assertTrue(runTestInSameEvaluator("{" + order + "order(g([f(1),f(2)])) == [1,2];}"));
		assertTrue(runTestInSameEvaluator("{" + order + "order(h(f(1),h(f(2),f(3)))) == [1,2,3];}"));
		assertTrue(runTestInSameEvaluator("{" + order + "order(h(f(1),g([h(f(2),f(3)),f(4),f(5)]))) == [1,2,3,4,5];}"));
	}
	
	@Test
	public void StringVisit1a()  {

		assertTrue(runTestInSameEvaluator("visit(\"\"){ case /b/: insert \"B\";} == \"\";"));
		assertTrue(runTestInSameEvaluator("visit(\"a\"){ case /b/: insert \"B\";} == \"a\";"));
		assertTrue(runTestInSameEvaluator("visit(\"b\"){ case /b/: insert \"B\";} == \"B\";"));
		assertTrue(runTestInSameEvaluator("visit(\"abc\"){ case /b/: insert \"B\";} == \"aBc\";"));
		assertTrue(runTestInSameEvaluator("visit(\"abcabc\"){ case /b/: insert \"B\";} == \"aBcaBc\";"));
	}
	
	@Test
	public void StringVisit1b()  {
		
		assertTrue(runTestInSameEvaluator("visit(\"\"){ case /b/ => \"B\"} == \"\";"));
		assertTrue(runTestInSameEvaluator("visit(\"a\"){ case /b/ => \"B\"} == \"a\";"));
		assertTrue(runTestInSameEvaluator("visit(\"b\"){ case /b/ => \"B\"} == \"B\";"));
		assertTrue(runTestInSameEvaluator("visit(\"abc\"){ case /b/ => \"B\"} == \"aBc\";"));
		assertTrue(runTestInSameEvaluator("visit(\"abcabc\"){ case /b/ =>\"B\"} == \"aBcaBc\";"));
	}
	
	@Test
	public void StringVisit2()  {
		
		assertTrue(runTestInSameEvaluator("visit(\"\"){ case /b/: insert \"BB\";} == \"\";"));
		assertTrue(runTestInSameEvaluator("visit(\"a\"){ case /b/: insert \"BB\";} == \"a\";"));
		assertTrue(runTestInSameEvaluator("visit(\"b\"){ case /b/: insert \"BB\";} == \"BB\";"));
		assertTrue(runTestInSameEvaluator("visit(\"abc\"){ case /b/: insert \"B\";} == \"aBc\";"));
		assertTrue(runTestInSameEvaluator("visit(\"abcabc\"){ case /b/: insert \"BB\";} == \"aBBcaBBc\";"));
	}
	
	@Test
	public void StringVisit3()  {
		
		assertTrue(runTestInSameEvaluator("visit(\"\"){ case /^a/: insert \"AA\"; case /^b/: insert \"BB\";} == \"\";"));
		assertTrue(runTestInSameEvaluator("visit(\"a\"){ case /^a/: insert \"AA\"; case /^b/: insert \"BB\";} == \"AA\";"));
		assertTrue(runTestInSameEvaluator("visit(\"b\"){ case /^a/: insert \"AA\"; case /^b/: insert \"BB\";} == \"BB\";"));
		assertTrue(runTestInSameEvaluator("visit(\"abcabc\"){ case /^a/: insert \"AA\"; case /^b/: insert \"BB\";} == \"AABBcAABBc\";"));
		assertTrue(runTestInSameEvaluator("visit(\"abcabca\"){ case /^a/: insert \"AA\"; case /^b/: insert \"BB\";} == \"AABBcAABBcAA\";"));

	}
	
	@Test
	public void StringVisit4()  {
		
		assertTrue(runTestInSameEvaluator("visit(\"\"){ case \"a\": insert \"AA\"; case /b/: insert \"BB\";} == \"\";"));
		assertTrue(runTestInSameEvaluator("visit(\"a\"){ case \"a\": insert \"AA\"; case /b/: insert \"BB\";} == \"AA\";"));
		assertTrue(runTestInSameEvaluator("visit(\"b\"){ case \"a\": insert \"AA\"; case /b/: insert \"BB\";} == \"BB\";"));
		assertTrue(runTestInSameEvaluator("visit(\"abcabc\"){ case \"a\": insert \"AA\"; case /b/: insert \"BB\";} == \"aBBcaBBc\";"));
		assertTrue(runTestInSameEvaluator("visit(\"abcabca\"){ case \"a\": insert \"AA\"; case /b/: insert \"BB\";} == \"aBBcaBBcAA\";"));

	}
	
	@Test(expected=UnexpectedTypeError.class)
	public void WrongInsert()  {
		String vs = "visit ([1,2,3]) {case 1: insert \"abc\";}";
		assertTrue(runTestInSameEvaluator(vs + " == [\"abc\", 2, 3];"));
	}
}


