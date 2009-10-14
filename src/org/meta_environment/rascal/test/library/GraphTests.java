package org.meta_environment.rascal.test.library;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.meta_environment.rascal.test.TestFramework;


public class GraphTests extends TestFramework {

	@Test
	public void bottom() {

		prepare("import Graph;");
		assertTrue(runTestInSameEvaluator("bottom({}) == {};"));
		assertTrue(runTestInSameEvaluator("bottom({<1,2>, <1,3>, <2,4>, <3,4>}) == {4};"));
	}

	@Test
	public void predecessors(){
		prepare("import Graph;");
		assertTrue(runTestInSameEvaluator("predecessors({<1,2>, <1,3>, <2,4>, <3,4>}, 4) =={2, 3};"));
	}

	@Test
	public void reachR() {

		prepare("import Graph;");

		assertTrue(runTestInSameEvaluator("reachR({}, {}, {}) == {};"));
		assertTrue(runTestInSameEvaluator("reachR({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {}) =={};"));
		assertTrue(runTestInSameEvaluator("reachR({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {1,2}) =={2};"));
		assertTrue(runTestInSameEvaluator("reachR({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {1,2,3}) =={2,3};"));
		assertTrue(runTestInSameEvaluator("reachR({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {1,2,4}) =={2, 4};"));
	}

	@Test
	public void reachX() {

		prepare("import Graph;");

		assertTrue(runTestInSameEvaluator("reachX({}, {}, {}) == {};"));
		// assertTrue(runTestInSameEvaluator("reachX({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {}) =={2, 3, 4};"));
		assertTrue(runTestInSameEvaluator("reachX({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {2}) =={3, 4};"));
		// assertTrue(runTestInSameEvaluator("reachX({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {2,3}) =={};"));
		assertTrue(runTestInSameEvaluator("reachX({<1,2>, <1,3>, <2,4>, <3,4>}, {1}, {4}) =={2, 3};"));
	}
	
	@Test 
	public void reach(){
		
		prepare("import Graph;");
		
//		assertTrue(runTestInSameEvaluator("reach({}, {}, {}) == {};"));
		assertTrue(runTestInSameEvaluator("reach({<1,2>, <1,3>, <2,4>, <3,4>}, {1}) =={1,2, 3, 4};"));
		assertTrue(runTestInSameEvaluator("reach({<1,2>, <1,3>, <2,4>, <3,4>}, {2}) =={2, 4};"));
		assertTrue(runTestInSameEvaluator("reach({<1,2>, <1,3>, <2,4>, <3,4>}, {3}) =={3, 4};"));
		assertTrue(runTestInSameEvaluator("reach({<1,2>, <1,3>, <2,4>, <3,4>}, {4}) =={4};"));
		assertTrue(runTestInSameEvaluator("reach({<1,2>, <1,3>, <2,4>, <3,4>}, {3,4}) =={3,4};"));
		assertTrue(runTestInSameEvaluator("reach({<1,2>, <1,3>, <2,4>, <3,4>}, {2,3}) =={2, 3,4};"));
	}
	
	@Test
	public void successors(){
		prepare("import Graph;");
		assertTrue(runTestInSameEvaluator("successors({<1,2>, <1,3>, <2,4>, <3,4>}, 1) =={2, 3};"));
	}

	@Test
	public void top() {

		prepare("import Graph;");
		assertTrue(runTestInSameEvaluator("top({}) == {};"));
		assertTrue(runTestInSameEvaluator("top({<1,2>, <1,3>, <2,4>, <3,4>}) == {1};"));
	}
}
