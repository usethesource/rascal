package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AllDemoTests extends TestFramework {

	@Test
	public void Ackermann() {
		prepare("import Ackermann;");
		assertTrue(runTestInSameEvaluator("Ackermann::test();"));
	}

	@Test
	public void BoolAbstractRules() {
		prepare("import BoolAbstractRules;");
		assertTrue(runTestInSameEvaluator("BoolAbstractRules::test();"));
	}

	@Test
	public void testBoolAbstractVisit() {
		prepare("import BoolAbstractVisit;");
		assertTrue(runTestInSameEvaluator("BoolAbstractVisit::test();"));
	}

	@Test
	public void Bubble() {
		prepare("import Bubble;");
		assertTrue(runTestInSameEvaluator("Bubble::test();"));
	}

	@Test
	public void Calls() {
		prepare("import Calls;");
		assertTrue(runTestInSameEvaluator("Calls::test();"));
	}

	@Test
	public void CarFDL() {
		prepare("import CarFDL;");
		assertTrue(runTestInSameEvaluator("CarFDL::test();"));
	}

	@Test
	public void Cycles() {
		prepare("import Cycles;");
		assertTrue(runTestInSameEvaluator("Cycles::test();"));
	}

	@Test
	public void Dominators() {
		prepare("import Dominators;");
		assertTrue(runTestInSameEvaluator("Dominators::test();"));
	}

	@Test
	public void FunAbstract() {
		prepare("import FunAbstract;");
		assertTrue(runTestInSameEvaluator("FunAbstract::test();"));
	}

	@Test
	public void GraphDataType() {
		prepare("import GraphDataType;");
		assertTrue(runTestInSameEvaluator("GraphDataType::test();"));
	}

	@Test
	public void Innerproduct() {
		prepare("import Innerproduct;");
		assertTrue(runTestInSameEvaluator("Innerproduct::test();"));
	}

	@Test
	public void IntegerAbstractRules() {
		prepare("import IntegerAbstractRules;");
		assertTrue(runTestInSameEvaluator("IntegerAbstractRules::testInt();"));
	}

	@Test
	public void Lift() {
		prepare("import Lift;");
		assertTrue(runTestInSameEvaluator("Lift::test();"));
	}
	
	@Test
	public void PicoEval() {
		prepare("import PicoEval;");
		assertTrue(runTestInSameEvaluator("PicoEval::test();"));
	}

	@Test
	public void PicoTypecheck() {
		prepare("import PicoTypecheck;");
		assertTrue(runTestInSameEvaluator("PicoTypecheck::test();"));
	}
	
	@Test
	public void PicoControlflow() {
		prepare("import PicoControlflow;");
		assertTrue(runTestInSameEvaluator("PicoControlflow::test();"));
	}

	@Test
	public void Queens() {
		prepare("import Queens;");
		assertTrue(runTestInSameEvaluator("Queens::test();"));
	}

	@Test
	public void ReachingDefs() {
		prepare("import ReachingDefs;");
		assertTrue(runTestInSameEvaluator("ReachingDefs::test();"));
	}

	@Test
	public void Squares() {
		prepare("import Squares;");
		assertTrue(runTestInSameEvaluator("Squares::test();"));
	}

	@Test
	public void Trans() {
		prepare("import Trans;");
		assertTrue(runTestInSameEvaluator("Trans::test();"));
	}

	@Test
	public void UPTR() {
		prepare("import UPTR;");
		assertTrue(runTestInSameEvaluator("UPTR::test();"));
	}

	@Test
	public void WordCount() {
		prepare("import WordCount;");
		assertTrue(runTestInSameEvaluator("WordCount::test();"));
	}

	@Test
	public void WordReplacement() {
		prepare("import WordReplacement;");
		assertTrue(runTestInSameEvaluator("WordReplacement::test();"));
	}

}
