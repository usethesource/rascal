package test;

import org.junit.Test;


public class RuleTests extends TestFramework{
	
	
	@Test
	public void useOfVariableInRuleLhs(){
		prepare("int j = 0;");
		prepareMore("data Int = i(int i);");
		prepareMore("rule test i(j) => i(1);");
		runTestInSameEvaluator("i(0) == i(1);");
	}
	
}

