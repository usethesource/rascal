package test;

import java.io.IOException;

import junit.framework.TestCase;

public class StandardLibraryRelationTests extends TestCase {
	private static TestFramework tf = new TestFramework("import Relation;");
	
	public void testRelCarrier() throws IOException {
		assertTrue(tf.runTestInSameEvaluator("carrier({<1,10>,<2,20>}) == {1,2,10,20};"));
		assertTrue(tf.runTestInSameEvaluator("carrier({<1,10,100>,<2,20,200>}) == {1,2,10,20,100,200};"));
		assertTrue(tf.runTestInSameEvaluator("carrier({<1,10,100,1000>,<2,20,200,2000>}) == {1,2,10,20,100,200,1000,2000};"));
		assertTrue(tf.runTestInSameEvaluator("carrier({<1,10,100,1000,10000>,<2,20,200,2000,20000>}) == {1,2,10,20,100,200,1000,2000,10000,20000};"));

	}
	
	public void testRelCarrierR() throws IOException {
		assertTrue(tf.runTestInSameEvaluator("carrierR({<1,10>,<2,20>}, {} ) == {};"));
		assertTrue(tf.runTestInSameEvaluator("carrierR({<1,10>,<2,20>}, {2,3} ) == {};"));
		assertTrue(tf.runTestInSameEvaluator("carrierR({<1,10>,<2,20>}, {2,20} ) == {<2,20>};"));
		assertTrue(tf.runTestInSameEvaluator("carrierR({<1,10,100>,<2,20,200>}, {2, 20,200}) == {<2,20,200>};"));
		assertTrue(tf.runTestInSameEvaluator("carrierR({<1,10,100>,<2,20,200>}, {1,2,10,20,100,200}) == {<1,10,100>,<2,20,200>};"));
		assertTrue(tf.runTestInSameEvaluator("carrierR({<1,10,100,1000>,<2,20,200,2000>}, {1,10,100,1000}) == {<1,10,100,1000>};"));
		assertTrue(tf.runTestInSameEvaluator("carrierR({<1,10,100,1000>,<2,20,200,2000>}, {2,20,200,2000}) == {<2,20,200,2000>};"));
	}
	
	public void testRelCarrierX() throws IOException {
		assertTrue(tf.runTestInSameEvaluator("carrierX({<1,10>,<2,20>}, {} ) == {<1,10>,<2,20>};"));
		assertTrue(tf.runTestInSameEvaluator("carrierX({<1,10>,<2,20>}, {2,3} ) == {<1,10>};"));
		assertTrue(tf.runTestInSameEvaluator("carrierX({<1,10,100>,<2,20,200>}, {20}) == {<1,10,100>};"));
		assertTrue(tf.runTestInSameEvaluator("carrierX({<1,10,100>,<2,20,200>}, {20,100}) == {};"));
		assertTrue(tf.runTestInSameEvaluator("carrierX({<1,10,100,1000>,<2,20,200,2000>}, {1000}) == {<2,20,200,2000>};"));
		assertTrue(tf.runTestInSameEvaluator("carrierX({<1,10,100,1000>,<2,20,200,2000>}, {2}) == {<1,10,100,1000>};"));
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
		assertTrue(tf.runTestInSameEvaluator("domain({<1,10,100,1000,10000>,<2,20,200,2000,20000>}) == {1,2};"));
	}
	
	public void testRelDomainR() throws IOException {
		assertTrue(tf.runTestInSameEvaluator("domainR({<1,10>,<2,20>}, {}) == {};"));
		assertTrue(tf.runTestInSameEvaluator("domainR({<1,10>,<2,20>}, {2}) == {<2,20>};"));
		assertTrue(tf.runTestInSameEvaluator("domainR({<1,10,100>,<2,20,200>}, {2,5}) == {<2,20,200>};"));
		assertTrue(tf.runTestInSameEvaluator("domainR({<1,10,100,1000>,<2,20,200,2000>}, {1,3}) == {<1,10,100,1000>};"));
		assertTrue(tf.runTestInSameEvaluator("domainR({<1,10,100,1000,10000>,<2,20,200,2000,20000>},{2,5}) == {<2,20,200,2000,20000>};"));
	}
	
	public void testRelDomainX() throws IOException {
		assertTrue(tf.runTestInSameEvaluator("domainX({<1,10>,<2,20>}, {}) == {<1,10>,<2,20>};"));
		assertTrue(tf.runTestInSameEvaluator("domainX({<1,10>,<2,20>}, {2}) == {<1,10>};"));
		assertTrue(tf.runTestInSameEvaluator("domainX({<1,10,100>,<2,20,200>}, {2,5}) == {<1,10,100>};"));
		assertTrue(tf.runTestInSameEvaluator("domainX({<1,10,100,1000>,<2,20,200,2000>}, {1,3}) == {<2,20,200,2000>};"));
		assertTrue(tf.runTestInSameEvaluator("domainX({<1,10,100,1000,10000>,<2,20,200,2000,20000>},{2,5}) == {<1,10,100,1000,10000>};"));

	}
	
	public void testRelInvert() throws IOException {
		assertTrue(tf.runTestInSameEvaluator("invert({<1,10>,<2,20>}) == {<10,1>,<20,2>};"));
		assertTrue(tf.runTestInSameEvaluator("invert({<1,10,100>,<2,20,200>}) == {<100,10,1>,<200,20,2>};"));
		assertTrue(tf.runTestInSameEvaluator("invert({<1,10,100,1000>,<2,20,200,2000>}) == {<1000,100,10,1>,<2000,200,20,2>};"));
		assertTrue(tf.runTestInSameEvaluator("invert({<1,10,100,1000,10000>,<2,20,200,2000,20000>}) == {<10000,1000,100,10,1>,<20000,2000,200,20,2>};"));
	}
	
	public void testRelRange() throws IOException {
		assertTrue(tf.runTestInSameEvaluator("range({<1,10>,<2,20>}) == {10,20};"));
		assertTrue(tf.runTestInSameEvaluator("range({<1,10,100>,<2,20,200>}) == {<10,100>,<20,200>};"));
		assertTrue(tf.runTestInSameEvaluator("range({<1,10,100,1000>,<2,20,200,2000>}) == {<10,100,1000>,<20,200,2000>};"));
		assertTrue(tf.runTestInSameEvaluator("range({<1,10,100,1000,10000>,<2,20,200,2000,20000>}) == {<10,100,1000,10000>,<20,200,2000,20000>};"));
	}
	
	public void testRelRangeR() throws IOException {
		//assertTrue(tf.runTestInSameEvaluator("rangeR({<1,10>,<2,20>}, {}) == {};"));
		assertTrue(tf.runTestInSameEvaluator("rangeR({<1,10>,<2,20>}, {20}) == {<2,20>};"));
		//assertTrue(tf.runTestInSameEvaluator("rangeR({<1,10,100>,<2,20,200>}) == {<10,100>,<20,200>};"));
		//assertTrue(tf.runTestInSameEvaluator("rangeR({<1,10,100,1000>,<2,20,200,2000>}) == {<10,100,1000>,<20,200,2000>};"));
		//assertTrue(tf.runTestInSameEvaluator("rangeR({<1,10,100,1000,10000>,<2,20,200,2000,20000>}) == {<10,100,1000,10000>,<20,200,2000,20000>};"));

	}
	
	public void testRelRangeX() throws IOException {
		//assertTrue(tf.runTestInSameEvaluator("rangeX({<1,10>,<2,20>}, {}) == {<1,10>,<2,20>};"));
		assertTrue(tf.runTestInSameEvaluator("rangeX({<1,10>,<2,20>}, {20}) == {<1,10>};"));
		//assertTrue(tf.runTestInSameEvaluator("rangeX({<1,10,100>,<2,20,200>}) == {<10,100>,<20,200>};"));
		//assertTrue(tf.runTestInSameEvaluator("rangeX({<1,10,100,1000>,<2,20,200,2000>}) == {<10,100,1000>,<20,200,2000>};"));
		//assertTrue(tf.runTestInSameEvaluator("rangeX({<1,10,100,1000,10000>,<2,20,200,2000,20000>}) == {<10,100,1000,10000>,<20,200,2000,20000>};"));

	}

}
