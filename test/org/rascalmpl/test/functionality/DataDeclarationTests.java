/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.test.functionality;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredField;
import org.rascalmpl.test.infrastructure.TestFramework;


public class DataDeclarationTests extends TestFramework {

	@Test
	public void bool() {

		prepare("data Bool = btrue() | bfalse() | band(Bool left, Bool right) | bor(Bool left, Bool right);");

		assertTrue(runTestInSameEvaluator("{Bool b = btrue(); b == Bool::btrue();}"));
		assertTrue(runTestInSameEvaluator("{Bool b = bfalse(); b == Bool::bfalse();}"));
		assertTrue(runTestInSameEvaluator("{Bool b = band(btrue(),bfalse());  b == Bool::band(Bool::btrue(),Bool::bfalse());}"));
		assertTrue(runTestInSameEvaluator("{Bool b = bor(btrue(),bfalse()); b == bor(btrue(),bfalse());}"));
		assertTrue(runTestInSameEvaluator("band(btrue(),bfalse()).left == btrue();"));
		assertTrue(runTestInSameEvaluator("band(btrue(),bfalse()).right == bfalse();"));
		assertTrue(runTestInSameEvaluator("bor(btrue(),bfalse()).left == btrue();"));
		assertTrue(runTestInSameEvaluator("bor(btrue(),bfalse()).right == bfalse();"));
		assertTrue(runTestInSameEvaluator("{Bool b = band(btrue(),bfalse()).left; b == btrue();}"));
		assertTrue(runTestInSameEvaluator("{Bool b = band(btrue(),bfalse()).right; b == bfalse();}"));
	}
	
	@Test(expected=StaticError.class)
	public void boolUndefinedValue(){
		prepare("data Bool = btrue() | bfalse() | band(Bool left, Bool right) | bor(Bool left, Bool right);");
		runTestInSameEvaluator("{Bool b; b.left;}");
	}
	
	@Test(expected=StaticError.class)
	public void boolUnitializedVariable1(){
		prepare("data Bool = btrue() | bfalse() | band(Bool left, Bool right) | bor(Bool left, Bool right);");
		runTestInSameEvaluator("{Bool b; b.left = btrue();}");
	}
	
	@Test
	public void boolFieldUpdate() {

		prepare("data Bool = btrue() | bfalse() | band(Bool left, Bool right) | bor(Bool left, Bool right);");

		assertTrue(runTestInSameEvaluator("{Bool b = bor(btrue(),bfalse()); b[left=bfalse()] == bor(bfalse(),bfalse());}"));
		assertTrue(runTestInSameEvaluator("{Bool b = bor(btrue(),bfalse()); b[right=btrue()] == bor(btrue(),btrue());}"));
		assertTrue(runTestInSameEvaluator("{Bool b = bor(btrue(),bfalse()); b.left=bfalse(); b == bor(bfalse(),bfalse());}"));
		assertTrue(runTestInSameEvaluator("{Bool b = bor(btrue(),bfalse()); b.right=btrue(); b == bor(btrue(),btrue());}"));
		assertTrue(runTestInSameEvaluator("{Bool b = bor(bfalse(),bfalse()); b.left=btrue(); b.right=btrue(); b == bor(btrue(),btrue());}"));
	}
	
	@Test(expected=StaticError.class)
	public void boolUnitializedVariable2(){
		prepare("data Bool = btrue() | bfalse() | band(Bool left, Bool right) | bor(Bool left, Bool right);");
		runTestInSameEvaluator("{Bool b; b[left = btrue()];}");
	}

	@Test
	public void let1() {
		prepare("data Exp = let(str name, Exp exp1, Exp exp2) | var(str name) | \\int(int intVal);");
		
		assertTrue(runTestInSameEvaluator("{Exp e = \\int(1); e == \\int(1);}"));
		assertTrue(runTestInSameEvaluator("{Exp e = var(\"a\"); e == var(\"a\");}"));
		assertTrue(runTestInSameEvaluator("{Exp e = let(\"a\",\\int(1),var(\"a\")); e ==  let(\"a\",\\int(1),var(\"a\"));}"));
	}
	
	@Test
	public void parameterized() {
		prepare("data Exp[&T] = tval(&T tval) | tval2(&T tval1, &T tval2) | ival(int x);");
		
//		assertTrue(runTestInSameEvaluator("{a = tval(1); a == tval(1);}"));
//		assertTrue(runTestInSameEvaluator("{b = tval(\"abc\"); b == tval(\"abc\");}"));
//		assertTrue(runTestInSameEvaluator("{c = {tval(\"abc\")}; c == {tval(\"abc\")};}"));
		
		assertTrue(runTestInSameEvaluator("{Exp[int] e = tval(1); e == tval(1);}"));
		assertTrue(runTestInSameEvaluator("{Exp[str] f = tval(\"abc\"); f == tval(\"abc\");}"));
		assertTrue(runTestInSameEvaluator("{set[Exp[value]] g = {tval(1),tval(\"abc\")}; g == {tval(1), tval(\"abc\")};}"));
		
		// if the parameter is not bound by a constructor, the instantiated type equals the bound of the parameter, 
		// any smaller types, like Exp[int] would result in a type error
		assertTrue(runTestInSameEvaluator("{Exp[value] h = ival(3); h == ival(3);}"));
		
		assertTrue(runTestInSameEvaluator("{j = tval2(\"abc\", \"def\"); j == tval2(\"abc\", \"def\");}"));
		assertTrue(runTestInSameEvaluator("{k = tval2(\"abc\", \"def\"); k.tval1 == \"abc\";}"));
		assertTrue(runTestInSameEvaluator("{l = tval2(\"abc\", \"def\"); l.tval2 == \"def\";}"));
		assertTrue(runTestInSameEvaluator("{m = tval2(\"abc\", \"def\"); str s2 = m.tval2; s2 == \"def\";}"));	
	}
	
	@Test
	public void parameterizedErrorTest() {
		prepare("data Exp[&T] = tval(&T tval) | tval2(&T tval1, &T tval2) | ival(int x);");
		assertTrue(runTestInSameEvaluator("{Exp[int] h = ival(3); h == ival(3);}"));
	}
	
	public void unboundTypeVar() {
		prepare("data Maybe[&T] = None() | Some(&T t);");
		assertTrue(runTestInSameEvaluator("{ Maybe[void] x = None(); x == None();}"));
		assertTrue(runTestInSameEvaluator("{ x = None(); x = Some(0); x == Some(0);}"));
	}
	
	public void unequalParameterType(){
		prepare("data Exp[&T] = tval(&T tval) | tval2(&T tval1, &T tval2);");
		// T becomes lub of int and str
		runTestInSameEvaluator("Exp[value] x = tval2(3, \"abc\");");
	}
	
	
	@Test(expected=StaticError.class)
	public void letWrongTypeViaAlias(){
		prepare("alias Var2 = str;");
		prepareMore("data Exp2 = let(Var2 var, Exp2 exp1, Exp2 exp2) | var(Var2 var) | \\int(int intVal);");
		assertTrue(runTestInSameEvaluator("Var2 varx !:= let(\"a\",\\int(1),var(\"a\"));"));
	}

	@Test
	public void let2() {
		prepare("alias Var2 = str;");
		prepareMore("data Exp2 = let(Var2 var, Exp2 exp1, Exp2 exp2) | var(Var2 var) | \\int(int intVal);");

		assertTrue(runTestInSameEvaluator("{Exp2 e = \\int(1); e == \\int(1);}"));
		assertTrue(runTestInSameEvaluator("{Exp2 e = var(\"a\"); e == var(\"a\");}"));
		assertTrue(runTestInSameEvaluator("{Exp2 e = let(\"a\",\\int(1),var(\"a\")); e ==  let(\"a\",\\int(1),var(\"a\"));}"));
		assertTrue(runTestInSameEvaluator("Var2 var := \"a\";"));
	}
	
	@Test(expected=org.rascalmpl.interpreter.control_exceptions.Throw.class) 
	public void boolError() {
		prepare("data Bool = btrue() | bfalse() | band(Bool left, Bool right) | bor(Bool left, Bool right);");
		assertTrue(runTestInSameEvaluator("{Bool b = btrue(); b.left == btrue();}"));
	}
	
	public void exactDoubleFieldIsAllowed() throws StaticError {
		runTest("data D = d | d;");
		assertTrue(true);
	}
	
	@Test(expected=StaticError.class)
	public void doubleFieldError2() throws StaticError {
		runTest("data D = d(int n) | d(value v);");
	}
	
	@Test(expected=StaticError.class)
	public void doubleFieldError3() throws StaticError {
		runTest("data D = d(int n) | d(int v);");
	}
	
	@Test(expected=StaticError.class)
	public void doubleFieldError4() throws StaticError {
		prepare("alias INTEGER = int;");
		runTest("data D = d(int n) | d(INTEGER v);");
	}
	
	public void exactDoubleDataDeclarationIsAllowed() throws StaticError {
		prepare("data D = d(int n) | e;");
		runTestInSameEvaluator("data D = d(int n);");
		assertTrue(true);
	}
	
	@Test(expected=StaticError.class)
	public void undeclaredTypeError1() throws UndeclaredField {
		runTest("data D = anE(E e);");
	}
}
