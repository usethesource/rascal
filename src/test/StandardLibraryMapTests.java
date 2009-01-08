package test;

import java.io.IOException;

import junit.framework.TestCase;

public class StandardLibraryMapTests extends TestCase {
	private static TestFramework tf = new TestFramework("import Map;");
	
	public void testMapArb() throws IOException {
		
		tf = new TestFramework("import Map;");
		
		assertTrue(tf.runTestInSameEvaluator("arb((1:10)) == 1;"));
		assertTrue(tf.runTestInSameEvaluator("{int N = arb((1:10, 2:20)); (N == 1) || (N ==2);}"));
	}
	
	// mapper
	
	public void testMapMapper() throws IOException {
		
		tf = new TestFramework("import Map;");
		
		String inc = "int inc(int n) {return n + 1;} ";
		String dec = "int dec(int n) {return n - 1;} ";
		
		assertTrue(tf.runTestInSameEvaluator("{" + inc + "mapper((), #inc, #inc) == ();}"));
		assertTrue(tf.runTestInSameEvaluator("{" + inc + "mapper((1:10,2:20), #inc, #inc) == (2:11,3:21);}"));
		
		assertTrue(tf.runTestInSameEvaluator("{" + inc + dec + "mapper((), #inc, #dec) == ();}"));
		assertTrue(tf.runTestInSameEvaluator("{" + inc + dec + "mapper((1:10,2:20), #inc, #dec) == (2:9,3:19);}"));
	}
	
	
	// size
	
	public void testMapSize() throws IOException {
		
		tf = new TestFramework("import Map;");
		
		assertTrue(tf.runTestInSameEvaluator("size(()) == 0;"));
		assertTrue(tf.runTestInSameEvaluator("size((1:10)) == 1;"));
		assertTrue(tf.runTestInSameEvaluator("size((1:10,2:20)) == 2;"));
	}
	
	// toList
	public void testMapToList() throws IOException {
		
		tf = new TestFramework("import Map;");
		
		assertTrue(tf.runTestInSameEvaluator("toList(()) == [];"));
		assertTrue(tf.runTestInSameEvaluator("toList((1:10)) == [<1,10>];"));
		assertTrue(tf.runTestInSameEvaluator("{list[tuple[int,int]] L = toList((1:10, 2:20)); (L == [<1,10>,<2,20>]) || (L == [<2,20>,<1,10>]);}"));
	}
	
	// toRel
	public void testMapToRel() throws IOException {
		
		tf = new TestFramework("import Map;");
		
		assertTrue(tf.runTestInSameEvaluator("toRel(()) == {};"));
		assertTrue(tf.runTestInSameEvaluator("toRel((1:10)) == {<1,10>};"));
		assertTrue(tf.runTestInSameEvaluator("{rel[int,int] R = toRel((1:10, 2:20)); R == {<1,10>,<2,20>};}"));
	}
	
	// toString
	
	public void testMapToString() throws IOException {
		
		tf = new TestFramework("import Map;");
		
		assertTrue(tf.runTestInSameEvaluator("toString(()) == \"()\";"));
		assertTrue(tf.runTestInSameEvaluator("toString((1:10)) == \"(1:10)\";"));
	}
	
}
