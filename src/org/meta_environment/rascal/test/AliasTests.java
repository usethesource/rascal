package org.meta_environment.rascal.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.staticErrors.StaticError;


public class AliasTests extends TestFramework{
	
	
	@Test(expected=StaticError.class)
	public void doubleDeclarationError(){
		prepare("alias A = str;");
		runTestInSameEvaluator("alias A = int;");
	}
	
	@Test(expected=StaticError.class)
	public void circularDeclarationError(){
		runTest("alias A = A;");
	}
	
	@Test(expected=StaticError.class)
	public void undeclaredTYpeError(){
		runTest("alias A = B;");
	}
	
	@Test
	public void usingAliases(){
		prepare("alias INTEGER = int;");
		
		assertTrue(runTestInSameEvaluator("{INTEGER I = 3; I == 3;}"));
		assertTrue(runTestInSameEvaluator("{INTEGER I = 3; int J = I; J == 3;}"));
		
		assertTrue(runTestInSameEvaluator("{list[INTEGER] LI = [1,2,3]; LI == [1,2,3];}"));
		assertTrue(runTestInSameEvaluator("{set[INTEGER] SI = {1,2,3}; SI == {1,2,3};}"));
		assertTrue(runTestInSameEvaluator("{map[INTEGER,INTEGER] MI = (1:10,2:20); MI == (1:10,2:20);}"));
		assertTrue(runTestInSameEvaluator("{rel[INTEGER,INTEGER] RI = {<1,10>,<2,20>}; RI == {<1,10>,<2,20>};}"));
	}
	
	@Test
	public void usingIndirectAliases(){
		prepare("alias INTEGER0 = int;");
		prepareMore("alias INTEGER = INTEGER0;");
		
		assertTrue(runTestInSameEvaluator("{INTEGER I = 3; I == 3;}"));
		assertTrue(runTestInSameEvaluator("{INTEGER I = 3; int J = I; J == 3;}"));
		
		assertTrue(runTestInSameEvaluator("{list[INTEGER] LI = [1,2,3]; LI == [1,2,3];}"));
		assertTrue(runTestInSameEvaluator("{set[INTEGER] SI = {1,2,3}; SI == {1,2,3};}"));
		assertTrue(runTestInSameEvaluator("{map[INTEGER,INTEGER] MI = (1:10,2:20); MI == (1:10,2:20);}"));
		assertTrue(runTestInSameEvaluator("{rel[INTEGER,INTEGER] RI = {<1,10>,<2,20>}; RI == {<1,10>,<2,20>};}"));
	}
	
	@Test
	public void usingVeryIndirectAliases(){
		prepare("alias INTEGER0 = int;");
		prepareMore("alias INTEGER1 = INTEGER0;");
		prepareMore("alias INTEGER = INTEGER1;");
		
		assertTrue(runTestInSameEvaluator("{INTEGER I = 3; I == 3;}"));
		assertTrue(runTestInSameEvaluator("{INTEGER I = 3; int J = I; J == 3;}"));
		
		assertTrue(runTestInSameEvaluator("{list[INTEGER] LI = [1,2,3]; LI == [1,2,3];}"));
		assertTrue(runTestInSameEvaluator("{set[INTEGER] SI = {1,2,3}; SI == {1,2,3};}"));
		assertTrue(runTestInSameEvaluator("{map[INTEGER,INTEGER] MI = (1:10,2:20); MI == (1:10,2:20);}"));
		assertTrue(runTestInSameEvaluator("{rel[INTEGER,INTEGER] RI = {<1,10>,<2,20>}; RI == {<1,10>,<2,20>};}"));
	}
	
	@Test
	public void aliasAndADT1() {
		prepareModule("Test", "module Test alias INTEGER0 = INTEGER1; data INTEGER1 = f(int);");
		prepareMore("import Test;");
		assertTrue(runTestInSameEvaluator("{ INTEGER0 x = f(0); x == f(0); }"));
	}
	
	@Test
	public void aliasAndADT2(){
		prepare("alias StateId = int;");
		prepareMore("alias Permutation = list[int];");
		prepareMore("alias StatedId = int;");
		prepareMore("alias Symbol = int;");
		prepareMore("map[list[Permutation], StateId] allStates = ();");
		prepareMore("rel[StateId from,StateId to,Symbol symbol] Transitions = {}; "); 
		assertTrue(runTestInSameEvaluator("{Transitions = {<1,2,3>}; true;}"));
		
	}
	
	@Test
	public void outofOrderDeclaration() {
		prepareModule("Test", "module Test alias INTEGER0 = INTEGER1; alias INTEGER1 = int;");
		prepareMore("import Test;");
		assertTrue(runTestInSameEvaluator("{ INTEGER0 x = 0; x == 0; }"));
	}

	@Test(expected=StaticError.class) 
	public void longCycle() {
		prepareModule("Test", "module Test alias INTEGER0 = INTEGER1; alias INTEGER1 = INTEGER2; alias INTEGER2 = INTEGER0;");
	}
	
	@Test(expected=StaticError.class) 
	public void undeclaredTypeInDefinition() {
		prepareModule("Test", "module Test alias INTEGER0 = INTEGER1;");
	}
	
	@Test(expected=StaticError.class)
	public void anotherCircularity() {
		prepareModule("Test", "module Test alias INTEGER0 = INTEGER1; alias INTEGER1 = INTEGER0;");
		prepareMore("import Test;");
		assertTrue(runTestInSameEvaluator("{ INTEGER0 x = 0; x == 0; }"));
	}
	
	@Test
	public void transitiveAliasAcrossTuplesBug() {
		prepareModule("B", "module B\n" +
				"alias trans = tuple[str, str, str];\n" +
				"/* alias trans = str; */\n" +
				"alias block = set[trans];\n" +
				"alias partition = set[block];\n");
		prepareMore("import B;");
		assertTrue(runTestInSameEvaluator("{block aBlock = {<\"a\", \"b\", \"c\">}; " +
				"aBlock == {<\"a\", \"b\", \"c\">} ; }"));
	}
	
}

