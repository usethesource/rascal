package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.errors.*;

public class AliasTests extends TestFramework{
	
	
	@Test(expected=TypeError.class)
	public void doubleDeclarationError(){
		prepare("alias A = str;");
		runTestInSameEvaluator("alias A = int;");
	}
	
	@Test(expected=TypeError.class)
	public void circularDeclarationError(){
		runTest("alias A = A;");
	}
	
	@Test(expected=TypeError.class)
	public void undeclaredTYpeError(){
		runTest("alias A = B;");
	}
	
	@Test
	public void aliases1(){
		prepare("alias INTEGER = int;");
		
		assertTrue(runTestInSameEvaluator("{INTEGER I = 3; I == 3;}"));
		assertTrue(runTestInSameEvaluator("{INTEGER I = 3; int J = I; J == 3;}"));
		
		assertTrue(runTestInSameEvaluator("{list[INTEGER] LI = [1,2,3]; LI == [1,2,3];}"));
		assertTrue(runTestInSameEvaluator("{set[INTEGER] SI = {1,2,3}; SI == {1,2,3};}"));
		assertTrue(runTestInSameEvaluator("{map[INTEGER,INTEGER] MI = (1:10,2:20); MI == (1:10,2:20);}"));
		assertTrue(runTestInSameEvaluator("{rel[INTEGER,INTEGER] RI = {<1,10>,<2,20>}; RI == {<1,10>,<2,20>};}"));
	}
	
	@Test
	public void aliases2(){
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
	public void aliases3(){
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
	

}

