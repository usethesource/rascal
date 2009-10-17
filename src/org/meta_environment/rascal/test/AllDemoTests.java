package org.meta_environment.rascal.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AllDemoTests extends TestFramework {

	@Test
	public void AbstractBool() {
		assertTrue(runRascalTests("import demo::Rules::AbstractBool;"));
	}
	
	@Test
	public void AbstractBoolVisit() {
		assertTrue(runRascalTests("import demo::Rules::AbstractBoolVisit;"));
	}
	
	@Test
	public void AbstractInteger() {
		assertTrue(runRascalTests("import demo::Rules::AbstractInteger;"));
	}
	
	@Test
	public void AbstractPicoAssembly(){
		prepare("import demo::AbstractPico::Assembly;");
		assertTrue(runTestInSameEvaluator("test();"));
	}

	@Test
	public void AbstractPicoCommonSubexpression() {
		assertTrue(runRascalTests("import demo::AbstractPico::CommonSubexpression;"));
	}
	
	@Test
	public void AbstractPicoConstantPropagation() {
		prepare("import demo::AbstractPico::ConstantPropagation;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void AbstractPicoControlflow() {
		assertTrue(runRascalTests("import demo::AbstractPico::Controlflow;"));
	}
	
	@Test
	public void AbstractPicoEval() {
		assertTrue(runRascalTests("import demo::AbstractPico::Eval;"));
	}

	@Test
	public void AbstractPicoPrograms() {
		prepare("import demo::AbstractPico::Programs;");
		assertTrue(runTestInSameEvaluator("test();"));
	}

	@Test
	public void AbstractPicoTypecheck() {
		prepare("import demo::AbstractPico::Typecheck;");
		assertTrue(runTestInSameEvaluator("test();"));
	}

	@Test
	public void AbstractPicoUninit() {
		prepare("import demo::AbstractPico::Uninit;");
		assertTrue(runTestInSameEvaluator("test();"));
	}

	@Test
	public void AbstractPicoUseDef() {
		prepare("import demo::AbstractPico::UseDef;");
		assertTrue(runTestInSameEvaluator("test();"));
	}

	@Test
	public void Ackermann() {
		assertTrue(runRascalTests("import demo::Ackermann;"));
	}

	@Test
	public void Bubble() {
		assertTrue(runRascalTests("import demo::Bubble;"));
	}

	@Test
	public void Calls() {
		prepare("import demo::Calls;");
		assertTrue(runTestInSameEvaluator("test();"));
	}

	@Test
	public void CarFDL() {
		prepare("import demo::CarFDL;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void ColoredTrees() {
		prepare("import demo::ColoredTrees;");
		assertTrue(runTestInSameEvaluator("test();"));
	}

	@Test
	public void ConcreteBool() {
		prepare("import demo::Rules::ConcreteBool;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void ConcreteBoolVisit() {
		prepare("import demo::Rules::ConcreteBoolVisit;");
		assertTrue(runTestInSameEvaluator("test();"));
	}

	@Test
	public void ConcretePicoEval() {
		prepare("import demo::ConcretePico::Eval;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	@Test
	public void ConcretePicoTypecheck() {
		prepare("import demo::ConcretePico::Typecheck;");
		assertTrue(runTestInSameEvaluator("test();"));
	}

	@Test
	public void Cycles() {
		prepare("import demo::Cycles;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void Dominators() {
		prepare("import demo::Dominators;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void Factorial() {
		prepare("import demo::Factorial;");
		assertTrue(runTestInSameEvaluator("test();"));
	}

	@Test
	public void FunAbstract() {
		prepare("import demo::Fun::FunAbstract;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void GenericFeatherweightJava() {
		prepare("import demo::GenericFeatherweightJava::Examples;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void GrammarToolsGrammar() {
		prepare("import experiments::GrammarTools::Grammar;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void GrammarToolsImportBNF() {
		prepare("import experiments::GrammarTools::ImportBNF;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void GrammarToolsItemSet() {
		prepare("import experiments::GrammarTools::ItemSet;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void GrammarToolsFirstFollow() {
		prepare("import experiments::GrammarTools::FirstFollow;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void GraphDataType() {
		prepare("import demo::GraphDataType;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void Hello() {
		prepare("import demo::Hello;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void Innerproduct() {
		prepare("import demo::Innerproduct;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void Lift() {
		prepare("import demo::Lift;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void LRGen() {
		prepare("import experiments::Parsing::LRGen;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void McCabe() {
		prepare("import demo::McCabe;");
		assertTrue(runTestInSameEvaluator("test();"));
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
		assertTrue(runTestInSameEvaluator("test();"));
	}

	@Test
	public void Queens() {
		prepare("import demo::Queens;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void ReachingDefs() {
		prepare("import demo::ReachingDefs;");
		assertTrue(runTestInSameEvaluator("test();"));
	}

	@Test
	public void Slicing() {
		prepare("import demo::Slicing;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void StringTemplate() {
		prepare("import demo::StringTemplate;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void Squares() {
		prepare("import demo::Squares;");
		assertTrue(runTestInSameEvaluator("test();"));
	}

	@Test
	public void StateMachine(){
		prepare("import demo::StateMachine::CanReach;");
		assertTrue(runTestInSameEvaluator("test();"));
	}

	@Test
	public void Trans() {
		prepare("import demo::Trans;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void TreeTraversals() {
		prepare("import demo::TreeTraversals;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void Uninit() {
		prepare("import demo::Uninit;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void WordCount() {
		prepare("import demo::WordCount;");
		assertTrue(runTestInSameEvaluator("test();"));
	}
	
	@Test
	public void WordReplacement() {
		prepare("import demo::WordReplacement;");
		assertTrue(runTestInSameEvaluator("test();"));
	}

}
