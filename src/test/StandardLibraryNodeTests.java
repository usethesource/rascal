package test;

import java.io.IOException;

import junit.framework.TestCase;

public class StandardLibraryNodeTests extends TestCase {
	private static TestFramework tf;

	public void testNodeArity() throws IOException {
		
		tf = new TestFramework("import Node;").
		prepareMore("data NODE = f | f(int) | f(int,int) | f(int,int,int);");
		
		assertTrue(tf.runTestInSameEvaluator("arity(f()) == 0;"));
		assertTrue(tf.runTestInSameEvaluator("arity(f(1)) == 1;"));
		assertTrue(tf.runTestInSameEvaluator("arity(f(1,2)) == 2;"));
	}
	
	public void testNodeGetChildren() throws IOException {
		
		tf = new TestFramework("import Node;").
		prepareMore("data NODE = f | f(int) | f(int,int) | f(int,int,int);");
		
		assertTrue(tf.runTestInSameEvaluator("getChildren(f()) == [];"));
		assertTrue(tf.runTestInSameEvaluator("getChildren(f(1)) == [1];"));
		assertTrue(tf.runTestInSameEvaluator("getChildren(f(1,2)) == [1,2];"));
	}
	
	public void testNodeGetName() throws IOException {
		
		tf = new TestFramework("import Node;").
		prepareMore("data NODE = f | f(int) | f(int,int) | f(int,int,int);");
		
		assertTrue(tf.runTestInSameEvaluator("getName(f()) == \"f\";"));
		assertTrue(tf.runTestInSameEvaluator("getName(f(1,2,3)) == \"f\";"));
	}
	
	public void testNodeMakeNode() throws IOException {
		
		tf = new TestFramework("import Node;").
		prepareMore("data NODE = f | f(int) | f(int,int) | f(int,int,int);");
		
		assertTrue(tf.runTestInSameEvaluator("makeNode(\"f\") == f;"));
		assertTrue(tf.runTestInSameEvaluator("makeNode(\"f\", 1) == f(1);"));
		assertTrue(tf.runTestInSameEvaluator("makeNode(\"f\", 1, 2) == f(1, 2);"));
		assertTrue(tf.runTestInSameEvaluator("makeNode(\"f\", 1, 2, 3) == f(1, 2, 3);"));
	}
		
}
