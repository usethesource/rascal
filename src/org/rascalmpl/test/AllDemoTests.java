package org.rascalmpl.test;

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
		assertTrue(runRascalTests("import demo::AbstractPico::Assembly;"));
	}

	@Test
	public void AbstractPicoCommonSubexpression() {
		assertTrue(runRascalTests("import demo::AbstractPico::CommonSubexpression;"));
	}
	
	@Test
	public void AbstractPicoConstantPropagation() {
		assertTrue(runRascalTests("import demo::AbstractPico::ConstantPropagation;"));
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
		assertTrue(runRascalTests("import demo::AbstractPico::Programs;"));
	}

	@Test
	public void AbstractPicoTypecheck() {
		assertTrue(runRascalTests("import demo::AbstractPico::Typecheck;"));
	}

	@Test
	public void AbstractPicoUninit() {
		assertTrue(runRascalTests("import demo::AbstractPico::Uninit;"));
	}

	@Test
	public void AbstractPicoUseDef() {
		assertTrue(runRascalTests("import demo::AbstractPico::UseDef;"));
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
		assertTrue(runRascalTests("import demo::Calls;"));
	}

	@Test
	public void CarFDL() {
		assertTrue(runRascalTests("import demo::CarFDL;"));
	}
	
	@Test
	public void ColoredTrees() {
		assertTrue(runRascalTests("import demo::ColoredTrees;"));
	}

	@Test
	public void ConcreteBool() {
		assertTrue(runRascalTests("import demo::Rules::ConcreteBool;"));
	}
	
	@Test
	public void ConcreteBoolVisit() {
		assertTrue(runRascalTests("import demo::Rules::ConcreteBoolVisit;"));
	}

	@Test
	public void ConcretePicoEval() {
		assertTrue(runRascalTests("import demo::ConcretePico::Eval;"));
	}
	
	@Test
	public void ConcretePicoTypecheck() {
		assertTrue(runRascalTests("import demo::ConcretePico::Typecheck;"));
	}

	@Test
	public void Cycles() {
		assertTrue(runRascalTests("import demo::Cycles;"));
	}
	
	@Test
	public void Dominators() {
		assertTrue(runRascalTests("import demo::Dominators;"));
	}
	
	@Test
	public void FactorialTest() {
		assertTrue(runRascalTests("import demo::FactorialTest;"));
	}

	@Test
	public void FunAbstract() {
		assertTrue(runRascalTests("import demo::Fun::FunAbstract;"));
	}
	
	@Test
	public void GenericFeatherweightJava() {
		assertTrue(runRascalTests("import demo::GenericFeatherweightJava::Examples;"));
	}
	
	@Test
	public void GrammarToolsGrammar() {
		assertTrue(runRascalTests("import experiments::GrammarTools::Grammar;"));
	}
	
	@Test
	public void GrammarToolsImportBNF() {
		assertTrue(runRascalTests("import experiments::GrammarTools::ImportBNF;"));
	}
	
	@Test
	public void GrammarToolsItemSet() {
		assertTrue(runRascalTests("import experiments::GrammarTools::ItemSet;"));
	}
	
	@Test
	public void GrammarToolsFirstFollow() {
		assertTrue(runRascalTests("import experiments::GrammarTools::FirstFollow;"));
	}
	
	@Test
	public void GraphDataType() {
		assertTrue(runRascalTests("import demo::GraphDataType;"));
	}
	
	@Test
	public void Hello() {
		assertTrue(runRascalTests("import demo::Hello;"));
	}
	
	@Test
	public void Innerproduct() {
		assertTrue(runRascalTests("import  demo::Innerproduct;"));
	}
	
	@Test
	public void Lift() {
		assertTrue(runRascalTests("import  demo::Lift;"));
	}
	
	@Test
	public void LRGen() {
		assertTrue(runRascalTests("import  experiments::Parsing::LRGen;"));
	}
	
	@Test
	public void McCabe() {
		assertTrue(runRascalTests("import   demo::McCabe;"));
	}

	@Test
	public void ModelTransformationsBook2Publication() {
		assertTrue(runRascalTests("import  experiments::ModelTransformations::Book2Publication;"));
	}

	@Test
	public void ModelTransformationsFamilies2Persons() {
		assertTrue(runRascalTests("import experiments::ModelTransformations::Families2Persons;"));
	}
	
	@Test
	public void ModelTransformationsTree2List() {
		assertTrue(runRascalTests("import experiments::ModelTransformations::Tree2List;"));
	}

	@Test
	public void ParsingGRD(){
		assertTrue(runRascalTests("import experiments::Parsing::GRD;"));
	}

	@Test
	public void Queens() {
		assertTrue(runRascalTests("import demo::Queens;"));
	}
	
	@Test
	public void ReachingDefs() {
		assertTrue(runRascalTests("import demo::ReachingDefs;"));
	}

	@Test
	public void Slicing() {
		assertTrue(runRascalTests("import demo::Slicing;"));
	}
	
	@Test
	public void StringTemplate() {
		assertTrue(runRascalTests("import demo::StringTemplate;"));
	}
	
	@Test
	public void Squares() {
		assertTrue(runRascalTests("import demo::Squares;"));
	}

	@Test
	public void StateMachine(){
		assertTrue(runRascalTests("import demo::StateMachine::CanReach;"));
	}
	
	@Test
	public void StateMachineOld(){
		assertTrue(runRascalTests("import demo::StateMachine::OldCanReach;"));
	}

	@Test
	public void Trans() {
		assertTrue(runRascalTests("import demo::Trans;"));
	}
	
	@Test
	public void TreeTraversals() {
		assertTrue(runRascalTests("import demo::TreeTraversals;"));
	}
	
	@Test
	public void Uninit() {
		assertTrue(runRascalTests("import demo::Uninit;"));
	}
	
	@Test
	public void WordCount() {
		assertTrue(runRascalTests("import demo::WordCount;"));
	}
	
	@Test
	public void WordReplacement() {
		assertTrue(runRascalTests("import demo::WordReplacement;"));
	}

}
