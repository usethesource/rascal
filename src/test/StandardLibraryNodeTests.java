package test;

import org.junit.Test;
import static org.junit.Assert.*;

public class StandardLibraryNodeTests extends TestFramework {

	@Test
	public void testNodeArity() {

		prepare("import Node;").prepareMore(
				"data NODE = f | f(int) | f(int,int) | f(int,int,int);");

		assertTrue(runTestInSameEvaluator("arity(f()) == 0;"));
		assertTrue(runTestInSameEvaluator("arity(f(1)) == 1;"));
		assertTrue(runTestInSameEvaluator("arity(f(1,2)) == 2;"));
	}

	@Test
	public void testNodeGetChildren() {

		prepare("import Node;").prepareMore(
				"data NODE = f | f(int) | f(int,int) | f(int,int,int);");

		assertTrue(runTestInSameEvaluator("getChildren(f()) == [];"));
		assertTrue(runTestInSameEvaluator("getChildren(f(1)) == [1];"));
		assertTrue(runTestInSameEvaluator("getChildren(f(1,2)) == [1,2];"));
	}

	@Test
	public void testNodeGetName() {

		prepare("import Node;").prepareMore(
				"data NODE = f | f(int) | f(int,int) | f(int,int,int);");

		assertTrue(runTestInSameEvaluator("getName(f()) == \"f\";"));
		assertTrue(runTestInSameEvaluator("getName(f(1,2,3)) == \"f\";"));
	}

	@Test
	public void testNodeMakeNode() {
		prepare("import Node;");

		assertTrue(runTestInSameEvaluator("{node n = makeNode(\"f\"); getName(n) == \"f\" && arity(n) == 0 && getChildren(n) == []; }"));
		assertTrue(runTestInSameEvaluator("{node n = makeNode(\"f\", 1); getName(n) == \"f\" && arity(n) == 1 && getChildren(n) == [1];}"));
		assertTrue(runTestInSameEvaluator("{node n = makeNode(\"f\", 1, 2); getName(n) == \"f\" && arity(n) == 2 && getChildren(n) == [1,2];}"));
		assertTrue(runTestInSameEvaluator("{node n = makeNode(\"f\", 1, 2, 3); getName(n) == \"f\" && arity(n) == 3 && getChildren(n) == [1,2,3];}"));
	}

}
