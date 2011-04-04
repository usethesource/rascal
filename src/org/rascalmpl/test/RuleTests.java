/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.test;

import org.junit.Test;


public class RuleTests extends TestFramework{
	
	
	@Test
	public void useOfVariableInRuleLhs(){
		prepare("int j = 0;");
		prepareMore("data Int = i(int i);");
		prepareMore("rule \test i(j) => i(1);");
		runTestInSameEvaluator("i(0) == i(1);");
	}
	
	@Test
	public void useOfGlobalVariableInRuleLhs(){
		prepareModule("A", "module A public int j = 0;\n" + 
				      "data Int = i(int i);\n" +
		              "rule \test i(j) => i(1);"
				      );
		prepareMore("import A;");
		runTestInSameEvaluator("i(0) == i(1);");
	}
	
	@Test
	public void useOfGlobalVariableInRuleLhsNested(){
		prepareModule("A", "module A public int j = 0;\n" + 
				      "data Int = i(int i) | i(Int j);\n" +
				      "public Int Example = i(0);\n" +
		              "rule \test i(Example) => i(1);"
				      );
		prepareMore("import A;");
		runTestInSameEvaluator("i(i(0)) == i(1);");
	}
	
	@Test
	public void useOfGlobalimportedVariableInRuleLhs(){
		prepare("import Mbase;");
		prepareMore("data Int = i(int i);");
		prepareMore("rule \test i(n) => i(1);");
		runTestInSameEvaluator("i(2) == i(1);");
	}
	
	@Test 
	public void rulesInModuleEnvironmentNotLocal() {
		prepare("data Flip = flip(int i, int j);");
		prepareMore("rule flip flip(int a, int b) => flip(b,a) when a > b;");
		runTestInSameEvaluator("{int a = 0; flip(3,2) == flip(2,3);}");
	}
	
	@Test 
	public void rulesInModuleEnvironmentNotFunction() {
		prepare("data Flip = flip(int i, int j);");
		prepareMore("rule flip flip(int a, int b) => flip(b,a) when a > b;");
		prepareMore("Flip f(int a, int b) { return flip(3,2); }");
		runTestInSameEvaluator("{f(1,0) == flip(2,3);}");
	}
}

