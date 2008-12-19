package test;

import java.io.IOException;

import junit.framework.TestCase;

public class StandardLibraryGraphTests extends TestCase {
	
	private static TestFramework tf = new TestFramework("import Graph;");
	
	public void testGraphTop() throws IOException {
		//assertTrue(tf.runTestInSameEvaluator("top({}) == {};"));
		//assertTrue(tf.runTestInSameEvaluator("top({<1,2>, <1,3>, <2,4>, <3,4>}) == {1};"));
		assertTrue(tf.runTestInSameEvaluator("gtop({<1,2>, <1,3>, <2,4>, <3,4>}) == {1};"));
	}
	
	public void testGraphBottom() throws IOException {
		//assertTrue(tf.runTestInSameEvaluator("bottom({}) == {};"));
		assertTrue(tf.runTestInSameEvaluator("bottom({<1,2>, <1,3>, <2,4>, <3,4>}) == {4};"));
		assertTrue(tf.runTestInSameEvaluator("gbottom({<1,2>, <1,3>, <2,4>, <3,4>}) == {4};"));
	}
	
}
