package test;

import java.io.IOException;

import junit.framework.TestCase;

public class StandardLibraryRelationTests extends TestCase {
	private static TestFramework tf = new TestFramework("import Relation;");
	
	public void testRelCarrier() throws IOException {
		assertTrue(tf.runTestInSameEvaluator("carrier({<1,10>,<2,20>}) == {1,2,10,20};"));
		assertTrue(tf.runTestInSameEvaluator("carrier({<1,10,100>,<2,20,200>}) == {1,2,10,20,100,200};"));
		assertTrue(tf.runTestInSameEvaluator("carrier({<1,10,100,1000>,<2,20,200,2000>}) == {1,2,10,20,100,200,1000,2000};"));
	}
	
	public void testRelComplement() throws IOException {
		assertTrue(tf.runTestInSameEvaluator("complement({<1,10>,<2,20>}) == {<2,10>,<1,20>};"));
		assertTrue(tf.runTestInSameEvaluator("complement({<1,10,100>,<2,20,200>}) == {<2,20,100>,<2,10,200>,<2,10,100>,<1,20,200>,<1,20,100>,<1,10,200>};"));
		assertTrue(tf.runTestInSameEvaluator("complement({<1,10,100,1000>,<2,20,200,2000>}) == {<2,20,200,1000>,<1,10,100,2000>,<1,10,200,1000>,<1,10,200,2000>,<1,20,100,1000>,<1,20,100,2000>,<1,20,200,1000>,<1,20,200,2000>,<2,10,100,1000>,<2,10,100,2000>,<2,10,200,1000>,<2,10,200,2000>,<2,20,100,1000>,<2,20,100,2000>};"));
	}
	
	public void testRelDomain() throws IOException {
		assertTrue(tf.runTestInSameEvaluator("domain({<1,10>,<2,20>}) == {1,2};"));
		assertTrue(tf.runTestInSameEvaluator("domain({<1,10,100>,<2,20,200>}) == {1,2};"));
		assertTrue(tf.runTestInSameEvaluator("domain({<1,10,100,1000>,<2,20,200,2000>}) == {1,2};"));
	}
	
	public void testRelInvert() throws IOException {
		assertTrue(tf.runTestInSameEvaluator("invert({<1,10>,<2,20>}) == {<10,1>,<20,2>};"));
	}
	
	public void testRelRange() throws IOException {
		assertTrue(tf.runTestInSameEvaluator("range({<1,10>,<2,20>}) == {10,20};"));
		assertTrue(tf.runTestInSameEvaluator("range({<1,10,100>,<2,20,200>}) == {<10,100>,<20,200>};"));
		assertTrue(tf.runTestInSameEvaluator("range({<1,10,100,1000>,<2,20,200,2000>}) == {<10,100,1000>,<20,200,2000>};"));
	}

}
