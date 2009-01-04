package test;

import java.io.IOException;

import junit.framework.TestCase;

public class StandardLibraryTreeTests extends TestCase {
	private static TestFramework tf = new TestFramework("import Tree;").
		prepare("data NODE f | f(int) | f(int,int) | f(int,int,int);");

	public void testTreeArity() throws IOException {
		assertTrue(tf.runTestInSameEvaluator("arity(f()) == 0;"));
		assertTrue(tf.runTestInSameEvaluator("arity(f(1)) == 1;"));
		assertTrue(tf.runTestInSameEvaluator("arity(f(1,2)) == 2;"));
	}
	
	public void testTreeGetChild() throws IOException {
		assertTrue(tf.runTestInSameEvaluator("getChild(f(1),0) == 1;"));
		assertTrue(tf.runTestInSameEvaluator("getChild(f(1,2),0) == 1;"));
		assertTrue(tf.runTestInSameEvaluator("getChild(f(1,2),1) == 2;"));
	}
	
	public void testTreeGetChildren() throws IOException {
		assertTrue(tf.runTestInSameEvaluator("getChildren(f()) == [];"));
		assertTrue(tf.runTestInSameEvaluator("getChildren(f(1)) == [1];"));
		assertTrue(tf.runTestInSameEvaluator("getChildren(f(1,2)) == [1,2];"));
	}
	
	public void testTreeGetName() throws IOException {
		assertTrue(tf.runTestInSameEvaluator("getName(f()) == \"f\";"));
		assertTrue(tf.runTestInSameEvaluator("getName(f(1,2,3)) == \"f\";"));
	}
	
	// makeTree
	
	public void testTreeSetChild() throws IOException {
		assertTrue(tf.runTestInSameEvaluator("setChild(f(1,2,3), 0, 10) == f(10,2,3);"));
		assertTrue(tf.runTestInSameEvaluator("setChild(f(1,2,3), 1, 20) == f(1,20,3);"));
		assertTrue(tf.runTestInSameEvaluator("setChild(f(1,2,3), 2, 30) == f(1,2,30);"));
	}
	
	
		
}
