package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AllDemoTests extends TestFramework {

	@Test
	public void testAckermann() {
		prepare("import Ackermann;");
		assertTrue(runTestInSameEvaluator("Ackermann::test();"));
	}

	@Test
	public void testBoolAbstractRules() {
		prepare("import BoolAbstractRules;");
		assertTrue(runTestInSameEvaluator("BoolAbstractRules::test();"));
	}

	@Test
	public void testBoolAbstractVisit() {
		prepare("import BoolAbstractVisit;");
		assertTrue(runTestInSameEvaluator("BoolAbstractVisit::test();"));
	}

	@Test
	public void testBubble() {
		prepare("import Bubble;");
		assertTrue(runTestInSameEvaluator("Bubble::test();"));
	}

	@Test
	public void testCalls() {
		prepare("import Calls;");
		assertTrue(runTestInSameEvaluator("Calls::test();"));
	}

	@Test
	public void testCarFDL() {
		prepare("import CarFDL;");
		assertTrue(runTestInSameEvaluator("CarFDL::test();"));
	}

	@Test
	public void testCycles() {
		prepare("import Cycles;");
		assertTrue(runTestInSameEvaluator("Cycles::test();"));
	}

	@Test
	public void testDominators() {
		prepare("import Dominators;");
		assertTrue(runTestInSameEvaluator("Dominators::test();"));
	}

	@Test
	public void testFunAbstract() {
		prepare("import FunAbstract;");
		assertTrue(runTestInSameEvaluator("FunAbstract::test();"));
	}

	@Test
	public void testGraphDataType() {
		prepare("import GraphDataType;");
		assertTrue(runTestInSameEvaluator("GraphDataType::test();"));
	}

	@Test
	public void testInnerproduct() {
		prepare("import Innerproduct;");
		assertTrue(runTestInSameEvaluator("Innerproduct::test();"));
	}

	@Test
	public void testIntegerAbstractRules() {
		prepare("import IntegerAbstractRules;");
		assertTrue(runTestInSameEvaluator("IntegerAbstractRules::testInt();"));
	}

	@Test
	public void testLift() {
		prepare("import Lift;");
		assertTrue(runTestInSameEvaluator("Lift::test();"));
	}

	@Test
	public void testPicoTypecheck() {
		prepare("import PicoTypecheck;");
		assertTrue(runTestInSameEvaluator("PicoTypecheck::test();"));
	}

	@Test
	public void testQueens() {
		prepare("import Queens;");
		assertTrue(runTestInSameEvaluator("Queens::test();"));
	}

	@Test
	public void testReachingDefs() {
		prepare("import ReachingDefs;");
		assertTrue(runTestInSameEvaluator("ReachingDefs::test();"));
	}

	@Test
	public void testSquares() {
		prepare("import Squares;");
		assertTrue(runTestInSameEvaluator("Squares::test();"));
	}

	@Test
	public void testTrans() {
		prepare("import Trans;");
		assertTrue(runTestInSameEvaluator("Trans::test();"));
	}

	@Test
	public void testUPTR() {
		prepare("import UPTR;");
		assertTrue(runTestInSameEvaluator("UPTR::test();"));
	}

	@Test
	public void testWordCount() {
		prepare("import WordCount;");
		assertTrue(runTestInSameEvaluator("WordCount::test();"));
	}

	@Test
	public void testWordReplacement() {
		prepare("import WordReplacement;");
		assertTrue(runTestInSameEvaluator("WordReplacement::test();"));
	}

}
