package test;

import java.io.IOException;

import junit.framework.TestCase;

public class StandardLibraryTreeTests extends TestCase {
	private static TestFramework tf;

	public void testTreeArity() throws IOException {
		
		tf = new TestFramework("import Node;").
		prepareMore("data NODE f | f(int) | f(int,int) | f(int,int,int);");
		
		assertTrue(tf.runTestInSameEvaluator("arity(f()) == 0;"));
		assertTrue(tf.runTestInSameEvaluator("arity(f(1)) == 1;"));
		assertTrue(tf.runTestInSameEvaluator("arity(f(1,2)) == 2;"));
	}
	
	public void testTreeGetChildren() throws IOException {
		
		tf = new TestFramework("import Node;").
		prepareMore("data NODE f | f(int) | f(int,int) | f(int,int,int);");
		
		assertTrue(tf.runTestInSameEvaluator("getChildren(f()) == [];"));
		assertTrue(tf.runTestInSameEvaluator("getChildren(f(1)) == [1];"));
		assertTrue(tf.runTestInSameEvaluator("getChildren(f(1,2)) == [1,2];"));
	}
	
	public void testTreeGetName() throws IOException {
		
		tf = new TestFramework("import Node;").
		prepareMore("data NODE f | f(int) | f(int,int) | f(int,int,int);");
		
		assertTrue(tf.runTestInSameEvaluator("getName(f()) == \"f\";"));
		assertTrue(tf.runTestInSameEvaluator("getName(f(1,2,3)) == \"f\";"));
	}
	
	public void testTreeMakeTree() throws IOException {
		
		tf = new TestFramework("import Node;").
		prepareMore("data NODE f | f(int) | f(int,int) | f(int,int,int);");
		
		assertTrue(tf.runTestInSameEvaluator("makeTree(\"f\") == f;"));
		assertTrue(tf.runTestInSameEvaluator("makeTree(\"f\", 1) == f(1);"));
		assertTrue(tf.runTestInSameEvaluator("makeTree(\"f\", 1, 2) == f(1, 2);"));
		assertTrue(tf.runTestInSameEvaluator("makeTree(\"f\", 1, 2, 3) == f(1, 2, 3);"));
	}
		
}
