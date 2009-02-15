package test;

import org.junit.Test;

public class StandardLibraryIntegerTests extends TestFramework {

	@Test
	public void testIntegerAbs() {

		prepare("import Integer;");

		assertTrue(runTestInSameEvaluator("{abs(0) == 0;}"));
		assertTrue(runTestInSameEvaluator("{abs(-1) == 1;}"));
		assertTrue(runTestInSameEvaluator("{abs(1) == 1;}"));
	}

	@Test
	public void testIntegerArbInt() {

		prepare("import Integer;");

		assertTrue(runTestInSameEvaluator("{int N = Integer::arbInt(10); (N >= 0) && (N < 10);}"));
		assertTrue(runTestInSameEvaluator("{int N = arbInt(10); (N >= 0) && (N < 10);}"));

		assertTrue(runTestInSameEvaluator("{int N = arbInt(); true;}"));
	}

	@Test
	public void testIntegerMax() {

		prepare("import Integer;");

		assertTrue(runTestInSameEvaluator("Integer::max(3, 10) == 10;"));
		assertTrue(runTestInSameEvaluator("max(3, 10) == 10;"));
		assertTrue(runTestInSameEvaluator("Integer::max(10, 10) == 10;"));
	}

	@Test
	public void testIntegerMin() {

		prepare("import Integer;");

		assertTrue(runTestInSameEvaluator("Integer::min(3, 10) == 3;"));
		assertTrue(runTestInSameEvaluator("min(3, 10) == 3;"));
		assertTrue(runTestInSameEvaluator("Integer::min(10, 10) == 10;"));
	}

	@Test
	public void testIntegerToReal() {

		prepare("import Integer;");

		assertTrue(runTestInSameEvaluator("Integer::toReal(3) == 3.0;"));
		assertTrue(runTestInSameEvaluator("toReal(3) == 3.0;"));
	}

	@Test
	public void testIntegerToString() {

		prepare("import Integer;");

		assertTrue(runTestInSameEvaluator("Integer::toString(314) == \"314\";"));
		assertTrue(runTestInSameEvaluator("toString(314) == \"314\";"));
	}

}
