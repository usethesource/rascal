/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.test.functionality;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.interpreter.staticErrors.RedeclaredVariableError;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredTypeError;
import org.rascalmpl.test.infrastructure.TestFramework;

public class DeclarationTests extends TestFramework {
	@Test
	public void localTypeInferenceBottomScope() {
		runTest("x = 1;");
		runTestInSameEvaluator("x == 1;");
	}
	
	@Test
	public void localTypeInferenceNestedScope() {
		runTest("{ x = 1; x == 1; }");
	}
	
	public void localTypeInferenceNoEscape() {
		runTest("{ x = 1; x == 1; }");
		runTestInSameEvaluator("{ x = \"1\"; x == \"1\";}");
	}
	
	@Test(expected=StaticError.class)
	public void localTypeInferenceNoEscape2() {
		runTest("{ x = 1; x == 1; }");
		runTestInSameEvaluator("x;");
	}
	
	@Test(expected=UndeclaredTypeError.class)
	public void undeclaredType1(){
		runTest("X N;");
	}
	
	@Test
	public void shadowingDeclaration1(){
		assertTrue(runTest("{int N = 1; {int N = 2;}; N == 1;}"));
	}
	
	@Test
	public void shadowingDeclaration2(){
		assertTrue(runTest("{N = 1; {int N = 2;}; N == 1;}"));
	}
	
	@Test(expected=RedeclaredVariableError.class)
	public void doubleDeclaration3(){
		assertTrue(runTest("{int f(int N){int N = 1; return N;} f(3) == 1;}"));
	}
	
	@Test
	public void shadowingDeclaration4(){
		assertTrue(runTest("{int N = 3; int N := 3;}"));
	}
}
