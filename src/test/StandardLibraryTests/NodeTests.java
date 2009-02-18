package test.StandardLibraryTests;

import org.junit.Test;

import test.TestFramework;
import static org.junit.Assert.*;

public class NodeTests extends TestFramework {

	@Test
	public void arity() {

		prepare("import Node;").prepareMore(
				"data XNODE = xf | xf(int) | xf(int,int) | xf(int,int,int);");

		assertTrue(runTestInSameEvaluator("arity(xf()) == 0;"));
		assertTrue(runTestInSameEvaluator("arity(xf(1)) == 1;"));
		assertTrue(runTestInSameEvaluator("arity(xf(1,2)) == 2;"));
	}

	@Test
	public void getChildren() {

		prepare("import Node;").prepareMore(
				"data YNODE = yf | yf(int) | yf(int,int) | yf(int,int,int);");

		assertTrue(runTestInSameEvaluator("getChildren(yf()) == [];"));
		assertTrue(runTestInSameEvaluator("getChildren(yf(1)) == [1];"));
		assertTrue(runTestInSameEvaluator("getChildren(yf(1,2)) == [1,2];"));
	}

	@Test
	public void getName() {

		prepare("import Node;").prepareMore(
				"data ZNODE = zf | zf(int) | zf(int,int) | zf(int,int,int);");

		assertTrue(runTestInSameEvaluator("getName(zf()) == \"f\";"));
		assertTrue(runTestInSameEvaluator("getName(zf(1,2,3)) == \"f\";"));
	}

	@Test
	public void makeNode() {
		prepare("import Node;");

		assertTrue(runTestInSameEvaluator("{node n = makeNode(\"f\"); getName(n) == \"f\" && arity(n) == 0 && getChildren(n) == []; }"));
		assertTrue(runTestInSameEvaluator("{node n = makeNode(\"f\", 1); getName(n) == \"f\" && arity(n) == 1 && getChildren(n) == [1];}"));
		assertTrue(runTestInSameEvaluator("{node n = makeNode(\"f\", 1, 2); getName(n) == \"f\" && arity(n) == 2 && getChildren(n) == [1,2];}"));
		assertTrue(runTestInSameEvaluator("{node n = makeNode(\"f\", 1, 2, 3); getName(n) == \"f\" && arity(n) == 3 && getChildren(n) == [1,2,3];}"));
	}

}
