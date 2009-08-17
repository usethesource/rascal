package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AllDemoTests extends TestFramework {

	@Test
	public void AbstractBool() {
		prepare("import demo::Rules::AbstractBool;");
		assertTrue(runTestInSameEvaluator("demo::Rules::AbstractBool::test();"));
	}
	
	@Test
	public void AbstractBoolVisit() {
		prepare("import demo::Rules::AbstractBoolVisit;");
		assertTrue(runTestInSameEvaluator("demo::Rules::AbstractBoolVisit::test();"));
	}
	
	@Test
	public void AbstractInteger() {
		prepare("import demo::Rules::AbstractInteger;");
		assertTrue(runTestInSameEvaluator("demo::Rules::AbstractInteger::testInt();"));
	}
	
	@Test
	public void AbstractPicoAssembly(){
		prepare("import demo::AbstractPico::Assembly;");
		assertTrue(runTestInSameEvaluator("demo::AbstractPico::Assembly::test();"));
	}

	@Test
	public void AbstractPicoCommonSubexpression() {
		prepare("import demo::AbstractPico::CommonSubexpression;");
		assertTrue(runTestInSameEvaluator("demo::AbstractPico::CommonSubexpression::test();"));
	}
	
	@Test
	public void AbstractPicoConstantPropagation() {
		prepare("import demo::AbstractPico::ConstantPropagation;");
		assertTrue(runTestInSameEvaluator("demo::AbstractPico::ConstantPropagation::test();"));
	}
	
	@Test
	public void AbstractPicoControlflow() {
		prepare("import demo::AbstractPico::Controlflow;");
		assertTrue(runTestInSameEvaluator("demo::AbstractPico::Controlflow::test();"));
	}
	
	@Test
	public void AbstractPicoEval() {
		prepare("import demo::AbstractPico::Eval;");
		assertTrue(runTestInSameEvaluator("demo::AbstractPico::Eval::test();"));
	}

	@Test
	public void AbstractPicoPrograms() {
		prepare("import demo::AbstractPico::Programs;");
		assertTrue(runTestInSameEvaluator("demo::AbstractPico::Programs::test();"));
	}

	@Test
	public void AbstractPicoTypecheck() {
		prepare("import demo::AbstractPico::Typecheck;");
		assertTrue(runTestInSameEvaluator("demo::AbstractPico::Typecheck::test();"));
	}

	@Test
	public void AbstractPicoUninit() {
		prepare("import demo::AbstractPico::Uninit;");
		assertTrue(runTestInSameEvaluator("demo::AbstractPico::Uninit::test();"));
	}

	@Test
	public void AbstractPicoUseDef() {
		prepare("import demo::AbstractPico::UseDef;");
		assertTrue(runTestInSameEvaluator("demo::AbstractPico::UseDef::test();"));
	}

	@Test
	public void Ackermann() {
		prepare("import demo::Ackermann;");
		assertTrue(runTestInSameEvaluator("demo::Ackermann::test();"));
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
	public void ColoredTrees() {
		prepare("import demo::ColoredTrees;");
		assertTrue(runTestInSameEvaluator("demo::ColoredTrees::test();"));
	}

	@Test
	public void ConcreteBool() {
		prepare("import demo::Rules::ConcreteBool;");
		assertTrue(runTestInSameEvaluator("demo::Rules::ConcreteBool::test();"));
	}
	
	@Test
	public void ConcreteBoolVisit() {
		prepare("import demo::Rules::ConcreteBoolVisit;");
		assertTrue(runTestInSameEvaluator("demo::Rules::ConcreteBoolVisit::test();"));
	}


	@Test
	public void ConcretePicoTypecheck() {
		prepare("import demo::ConcretePico::Typecheck;");
		assertTrue(runTestInSameEvaluator("demo::ConcretePico::Typecheck::test();"));
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
	public void Factorial() {
		prepare("import demo::Factorial;");
		assertTrue(runTestInSameEvaluator("demo::Factorial::test();"));
	}

	@Test
	public void FunAbstract() {
		prepare("import demo::Fun::FunAbstract;");
		assertTrue(runTestInSameEvaluator("demo::Fun::FunAbstract::test();"));
	}
	
	@Test
	public void GenericFeatherweightJava() {
		prepare("import demo::GenericFeatherweightJava::Examples;");
		assertTrue(runTestInSameEvaluator("demo::GenericFeatherweightJava::Examples::test();"));
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
	public void Lift() {
		prepare("import demo::Lift;");
		assertTrue(runTestInSameEvaluator("demo::Lift::test();"));
	}
	
	@Test
	public void LRGen() {
		prepare("import experiments::Parsing::LRGen;");
		assertTrue(runTestInSameEvaluator("experiments::Parsing::LRGen::test();"));
	}
	
	@Test
	public void McCabe() {
		prepare("import demo::McCabe;");
		assertTrue(runTestInSameEvaluator("demo::McCabe::test();"));
	}

	@Test
	public void ModelTransformationsBook2Publication() {
		prepare("import experiments::ModelTransformations::Book2Publication;");
		assertTrue(runTestInSameEvaluator("test();"));
	}

	@Test
	public void ModelTransformationsFamilies2Persons() {
		prepare("import experiments::ModelTransformations::Families2Persons;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void ModelTransformationsTree2List() {
		prepare("import experiments::ModelTransformations::Tree2List;");
		assertTrue(runTestInSameEvaluator("test();"));
	}

	@Test
	public void ParsingGRD(){
		prepare("import experiments::Parsing::GRD;");
		assertTrue(runTestInSameEvaluator("experiments::Parsing::GRD::test();"));
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
	public void Slicing() {
		prepare("import demo::Slicing;");
		assertTrue(runTestInSameEvaluator("demo::Slicing::test();"));
	}
	
	@Test
	public void Squares() {
		prepare("import demo::Squares;");
		assertTrue(runTestInSameEvaluator("demo::Squares::test();"));
	}

	@Test
	public void StateMachine(){
		prepare("import demo::StateMachine::CanReach;");
		assertTrue(runTestInSameEvaluator("demo::StateMachine::CanReach::test();"));
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
	public void Uninit() {
		prepare("import demo::Uninit;");
		assertTrue(runTestInSameEvaluator("demo::Uninit::test();"));
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
