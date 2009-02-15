package test;

import org.junit.Test;

public class StandardLibraryGraphTests extends TestFramework {

	@Test
	public void testGraphBottom() {

		prepare("import Graph;");
		assertTrue(runTestInSameEvaluator("bottom({}) == {};"));
		assertTrue(runTestInSameEvaluator("bottom({<1,2>, <1,3>, <2,4>, <3,4>}) == {4};"));
	}

	@Test
	public void testGraphTop() {

		prepare("import Graph;");
		assertTrue(runTestInSameEvaluator("top({}) == {};"));
		assertTrue(runTestInSameEvaluator("top({<1,2>, <1,3>, <2,4>, <3,4>}) == {1};"));
	}

	@Test
	public void testGraphReachR() {

		prepare("import Graph;");

		assertTrue(runTestInSameEvaluator("reachR({}, {}, {}) == {};"));
		assertTrue(runTestInSameEvaluator("reachR({1}, {}, {<1,2>, <1,3>, <2,4>, <3,4>}) =={};"));
		assertTrue(runTestInSameEvaluator("reachR({1}, {1,2}, {<1,2>, <1,3>, <2,4>, <3,4>}) =={2};"));
		assertTrue(runTestInSameEvaluator("reachR({1}, {1,2,3}, {<1,2>, <1,3>, <2,4>, <3,4>}) =={2,3};"));
		assertTrue(runTestInSameEvaluator("reachR({1}, {1,2,4}, {<1,2>, <1,3>, <2,4>, <3,4>}) =={2, 4};"));
	}

	@Test
	public void testGraphReachX() {

		prepare("import Graph;");

		assertTrue(runTestInSameEvaluator("reachX({}, {}, {}) == {};"));
		// assertTrue(runTestInSameEvaluator("reachX({1}, {}, {<1,2>, <1,3>, <2,4>, <3,4>}) =={2, 3, 4};"));
		assertTrue(runTestInSameEvaluator("reachX({1}, {2}, {<1,2>, <1,3>, <2,4>, <3,4>}) =={3, 4};"));
		// assertTrue(runTestInSameEvaluator("reachX({1}, {2,3}, {<1,2>, <1,3>, <2,4>, <3,4>}) =={};"));
		assertTrue(runTestInSameEvaluator("reachX({1}, {4}, {<1,2>, <1,3>, <2,4>, <3,4>}) =={2, 3};"));
	}

}
