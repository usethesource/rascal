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
package org.rascalmpl.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredAnnotationError;
import org.rascalmpl.interpreter.staticErrors.UninitializedVariableError;
import org.rascalmpl.interpreter.staticErrors.UnsupportedOperationError;


public class AnnotationTests extends TestFramework{
	
	
	@Test(expected=UndeclaredAnnotationError.class)
	public void annotationNotAllowed(){
		prepare("data POS = pos(int n);");
		runTestInSameEvaluator("1 [@pos=3];");
	}
	
	@Test(expected=UnsupportedOperationError.class)
	public void annotationNotAllowed2(){
		runTest("1 @ pos;");
	}
	
	@Test(expected=StaticError.class)
	public void annotationNotAllowed3(){
		prepare("data F = f() | f(int n) | g(int n) | deep(F f);");
		prepareMore("anno int F @ pos;");
		runTestInSameEvaluator("f[@pos=true];");
	}
	
	@Test(expected=UndeclaredAnnotationError.class)
	public void annotationNotAllowed4(){
		prepare("data F = f() | f(int n) | g(int n) | deep(F f);");
		prepareMore("anno int F @ pos;");
		runTestInSameEvaluator("f() [@wrongpos=true];");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void UndefinedValueError1(){
		prepare("data F = f() | f(int n) | g(int n) | deep(F f);");
		prepareMore("anno int F @ pos;");
		runTestInSameEvaluator("{F someF; someF @ pos;}");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void UndefinedValueError2(){
		prepare("data F = f() | f(int n) | g(int n) | deep(F f);");
		prepareMore("anno int F @ pos;");
		runTestInSameEvaluator("{F someF; someF [@pos=3];}");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void UninitializedVariableError(){
		prepare("data F = f() | f(int n) | g(int n) | deep(F f);");
		prepareMore("anno int F @ pos;");
		runTestInSameEvaluator("{F someF; someF @ pos = 3;}");
	}
	
	@Test
	public void annotations(){
		prepare("data F = f() | f(int n) | g(int n) | deep(F f);");
		prepareMore("anno int F @ pos;");
		
		assertTrue(runTestInSameEvaluator("f() [@pos=1] == f();"));
		assertTrue(runTestInSameEvaluator("f() [@pos=1] @ pos == 1;"));
		assertTrue(runTestInSameEvaluator("f() [@pos=1] [@pos=2] @ pos == 2;"));
		
		assertTrue(runTestInSameEvaluator("f(5) [@pos=1] == f(5);"));
		assertTrue(runTestInSameEvaluator("f(5) [@pos=1] @ pos == 1;"));
		assertTrue(runTestInSameEvaluator("f(5) [@pos=1] [@pos=2] @ pos == 2;"));
		
		assertTrue(runTestInSameEvaluator("deep(f(5) [@pos=1]) == deep(f(5));"));
		assertTrue(runTestInSameEvaluator("f(5) [@pos=1] == f(5) [@pos=2];"));	
	}
	
	@Test
	public void annotationsInSets(){
		prepare("data F = f() | f(int n) | g(int n) | deep(F f);");
		prepareMore("anno int F @ pos;");
		
		assertTrue(runTestInSameEvaluator("{f() [@pos=1]} == {f()};"));
		assertTrue(runTestInSameEvaluator("{f() [@pos=1], g(2) [@pos=2]} == {f(), g(2)};"));
		assertTrue(runTestInSameEvaluator("{f() [@pos=1], g(2)} == {f(), g(2)[@pos=2]};"));		
		assertTrue(runTestInSameEvaluator("{deep(f(5) [@pos=1])} == {deep(f(5))};"));
		
		assertTrue(runTestInSameEvaluator("{f() [@pos=1]} + {g(2) [@pos=2]} == {f(), g(2)};"));
		
		assertTrue(runTestInSameEvaluator("{X = {f() [@pos=1]} + {f() [@pos=2]}; {F elem} := X && (elem@pos == 2 || elem@pos == 1);}"));
	}
}

