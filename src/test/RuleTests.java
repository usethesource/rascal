package test;

import org.junit.Test;
import static org.junit.Assert.*;

public class RuleTests extends TestFramework {
	
	@Test
	public void testBool() {
		
		prepare("import BoolAbstractRules;");
		
		assertTrue(runTestInSameEvaluator("btrue == btrue;"));
		assertTrue(runTestInSameEvaluator("bfalse == bfalse;"));
		
		assertFalse(runTestInSameEvaluator("btrue == bfalse;"));
		
		assertTrue(runTestInSameEvaluator("band(btrue,bfalse) == bfalse;"));	
		assertTrue(runTestInSameEvaluator("band(band(btrue,btrue),band(btrue, bfalse)) == bfalse;"));
		
		assertTrue(runTestInSameEvaluator("bor(btrue,bfalse) == btrue;"));
		
		assertTrue(runTestInSameEvaluator("bor(bor(btrue,btrue),bor(btrue, bfalse)) == btrue;"));
		assertTrue(runTestInSameEvaluator("bor(bor(bfalse,bfalse),bor(bfalse, bfalse)) == bfalse;"));
		
		assertTrue(runTestInSameEvaluator("bor(band(btrue,btrue),band(btrue, bfalse)) == btrue;"));
		
		assertTrue(runTestInSameEvaluator("band(bor(btrue,btrue),band(btrue, bfalse)) == bfalse;"));
		
	}
	
	@Test
	public void testInteger() {
		
		prepare("import IntegerAbstractRules;");
		
		assertTrue(runTestInSameEvaluator("add(s(s(z)), s(s(s(z)))) == s(s(s(s(s(z)))));"));
		assertTrue(runTestInSameEvaluator("mul(s(s(z)), s(s(s(z)))) == s(s(s(s(s(s(z))))));"));
		assertTrue(runTestInSameEvaluator("exp(s(s(z)), s(s(s(z)))) == s(s(s(s(s(s(s(s(z))))))));"));
		
		assertTrue(runTestInSameEvaluator("eq(s(s(z)), s(s(s(z)))) == bfalse;"));
		assertTrue(runTestInSameEvaluator("eq(s(s(s(z))), s(s(s(z)))) == btrue;"));
		
	}
}
	
