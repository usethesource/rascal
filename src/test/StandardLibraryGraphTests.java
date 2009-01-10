package test;

import java.io.IOException;

import junit.framework.TestCase;

public class StandardLibraryGraphTests extends TestCase {
	
	private static TestFramework tf = new TestFramework("import Graph;");
	
	public void testGraphBottom() throws IOException {
		
		tf = new TestFramework("import Graph;");
		assertTrue(tf.runTestInSameEvaluator("bottom({}) == {};"));
		assertTrue(tf.runTestInSameEvaluator("bottom({<1,2>, <1,3>, <2,4>, <3,4>}) == {4};"));
	}
	
	public void testGraphTop() throws IOException {
		
		tf = new TestFramework("import Graph;");
		assertTrue(tf.runTestInSameEvaluator("top({}) == {};"));
		assertTrue(tf.runTestInSameEvaluator("top({<1,2>, <1,3>, <2,4>, <3,4>}) == {1};"));
	}
	
	public void testGraphReachR() throws IOException {
		
		tf = new TestFramework("import Graph;");

		assertTrue(tf.runTestInSameEvaluator("reachR({}, {}, {}) == {};"));
		assertTrue(tf.runTestInSameEvaluator("reachR({1}, {}, {<1,2>, <1,3>, <2,4>, <3,4>}) =={};")); 
		assertTrue(tf.runTestInSameEvaluator("reachR({1}, {1,2}, {<1,2>, <1,3>, <2,4>, <3,4>}) =={2};")); 
		assertTrue(tf.runTestInSameEvaluator("reachR({1}, {1,2,3}, {<1,2>, <1,3>, <2,4>, <3,4>}) =={2,3};")); 
		assertTrue(tf.runTestInSameEvaluator("reachR({1}, {1,2,4}, {<1,2>, <1,3>, <2,4>, <3,4>}) =={2, 4};")); 
	}
	
	public void testGraphReachX() throws IOException {
		
		tf = new TestFramework("import Graph;");
	
		assertTrue(tf.runTestInSameEvaluator("reachX({}, {}, {}) == {};"));
//		assertTrue(tf.runTestInSameEvaluator("reachX({1}, {}, {<1,2>, <1,3>, <2,4>, <3,4>}) =={2, 3, 4};")); 
		assertTrue(tf.runTestInSameEvaluator("reachX({1}, {2}, {<1,2>, <1,3>, <2,4>, <3,4>}) =={3, 4};")); 
		//assertTrue(tf.runTestInSameEvaluator("reachX({1}, {2,3}, {<1,2>, <1,3>, <2,4>, <3,4>}) =={};")); 
		assertTrue(tf.runTestInSameEvaluator("reachX({1}, {4}, {<1,2>, <1,3>, <2,4>, <3,4>}) =={2, 3};")); 
	}
	
	
}
