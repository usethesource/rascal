package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AllDemoTests extends TestFramework {

	@Test
	public void Ackermann() {
		prepare("import demo::Ackermann::Ackermann;");
		assertTrue(runTestInSameEvaluator("demo::Ackermann::Ackermann::test();"));
	}

	@Test
	public void BoolAbstractRules() {
		prepare("import demo::Booleans::BoolAbstractRules;");
		assertTrue(runTestInSameEvaluator("demo::Booleans::BoolAbstractRules::test();"));
	}

	@Test
	public void BoolAbstractVisit() {
		prepare("import demo::Booleans::BoolAbstractVisit;");
		assertTrue(runTestInSameEvaluator("demo::Booleans::BoolAbstractVisit::test();"));
	}

	@Test
	public void Bubble() {
		prepare("import demo::Bubble;");
		assertTrue(runTestInSameEvaluator("demo::Bubble::test();"));
	}

	@Test
	public void Calls() {
		prepare("import demo::Calls;");
		assertTrue(runTestInSameEvaluator("demo::Calls::test();"));
	}

	@Test
	public void CarFDL() {
		prepare("import demo::CarFDL;");
		assertTrue(runTestInSameEvaluator("demo::CarFDL::test();"));
	}

	@Test
	public void Cycles() {
		prepare("import demo::Cycles;");
		assertTrue(runTestInSameEvaluator("demo::Cycles::test();"));
	}

	@Test
	public void Dominators() {
		prepare("import demo::Dominators;");
		assertTrue(runTestInSameEvaluator("demo::Dominators::test();"));
	}

	@Test
	public void FunAbstract() {
		prepare("import demo::Fun::FunAbstract;");
		assertTrue(runTestInSameEvaluator("demo::Fun::FunAbstract::test();"));
	}

	@Test
	public void GraphDataType() {
		prepare("import demo::GraphDataType;");
		assertTrue(runTestInSameEvaluator("demo::GraphDataType::test();"));
	}
	
	@Test
	public void Hello() {
		prepare("import demo::Hello;");
		assertTrue(runTestInSameEvaluator("demo::Hello::test();"));
	}

	@Test
	public void Innerproduct() {
		prepare("import demo::Innerproduct;");
		assertTrue(runTestInSameEvaluator("demo::Innerproduct::test();"));
	}

	@Test
	public void IntegerAbstractRules() {
		prepare("import demo::Integers::IntegerAbstractRules;");
		assertTrue(runTestInSameEvaluator("demo::Integers::IntegerAbstractRules::testInt();"));
	}

	@Test
	public void Lift() {
		prepare("import demo::Lift;");
		assertTrue(runTestInSameEvaluator("demo::Lift::test();"));
	}
	
	@Test
	public void PicoEval() {
		prepare("import demo::PicoAbstract::PicoEval;");
		assertTrue(runTestInSameEvaluator("demo::PicoAbstract::PicoEval::test();"));
	}

	@Test
	public void PicoTypecheck() {
		prepare("import demo::PicoAbstract::PicoTypecheck;");
		assertTrue(runTestInSameEvaluator("demo::PicoAbstract::PicoTypecheck::test();"));
	}
	
	@Test
	public void PicoControlflow() {
		prepare("import demo::PicoAbstract::PicoControlflow;");
		assertTrue(runTestInSameEvaluator("demo::PicoAbstract::PicoControlflow::test();"));
	}
	
	@Test
	public void PicoUseDef() {
		prepare("import demo::PicoAbstract::PicoUseDef;");
		assertTrue(runTestInSameEvaluator("demo::PicoAbstract::PicoUseDef::test();"));
	}
	
	@Test
	public void PicoUninit() {
		prepare("import demo::PicoAbstract::PicoUninit;");
		assertTrue(runTestInSameEvaluator("demo::PicoAbstract::PicoUninit::test();"));
	}


	@Test
	public void Queens() {
		prepare("import demo::Queens;");
		assertTrue(runTestInSameEvaluator("demo::Queens::test();"));
	}

	@Test
	public void ReachingDefs() {
		prepare("import demo::ReachingDefs;");
		assertTrue(runTestInSameEvaluator("demo::ReachingDefs::test();"));
	}

	@Test
	public void Squares() {
		prepare("import demo::Squares;");
		assertTrue(runTestInSameEvaluator("demo::Squares::test();"));
	}

	@Test
	public void Trans() {
		prepare("import demo::Trans;");
		assertTrue(runTestInSameEvaluator("demo::Trans::test();"));
	}
	
	@Test
	public void TreeTraversals() {
		prepare("import demo::TreeTraversals;");
		assertTrue(runTestInSameEvaluator("demo::TreeTraversals::test();"));
	}

	@Test
	public void UPTR() {
		prepare("import demo::UPTR;");
		assertTrue(runTestInSameEvaluator("demo::UPTR::test();"));
	}

	@Test
	public void WordCount() {
		prepare("import demo::WordCount;");
		assertTrue(runTestInSameEvaluator("demo::WordCount::test();"));
	}

	@Test
	public void WordReplacement() {
		prepare("import demo::WordReplacement;");
		assertTrue(runTestInSameEvaluator("demo::WordReplacement::test();"));
	}

}
