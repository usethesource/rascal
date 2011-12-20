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
package org.rascalmpl.test.library;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.test.TestFramework;


public class MathTests extends TestFramework {

	@Test
	public void absInt() {

		prepare("import Math;");

		assertTrue(runTestInSameEvaluator("{abs(0) == 0;}"));
		assertTrue(runTestInSameEvaluator("{abs(-1) == 1;}"));
		assertTrue(runTestInSameEvaluator("{abs(1) == 1;}"));
	}
	
	@Test
	public void absReal() {

		prepare("import Math;");

		assertTrue(runTestInSameEvaluator("{abs(0.0) == 0.0;}"));
		assertTrue(runTestInSameEvaluator("{abs(-1.0) == 1.0;}"));
		assertTrue(runTestInSameEvaluator("{abs(1.0) == 1.0;}"));
	}
	
	@Test
	public void absRat() {

		prepare("import Math;");

		assertTrue(runTestInSameEvaluator("{abs(0r) == 0r;}"));
		assertTrue(runTestInSameEvaluator("{abs(-1r1) == 1r1;}"));
		assertTrue(runTestInSameEvaluator("{abs(1r1) == 1r1;}"));
	}

	@Test
	public void arbInt() {

		prepare("import Math;");

		assertTrue(runTestInSameEvaluator("{int N = Math::arbInt(10); (N >= 0) && (N < 10);}"));
		assertTrue(runTestInSameEvaluator("{int N = arbInt(10); (N >= 0) && (N < 10);}"));

		assertTrue(runTestInSameEvaluator("{int N = arbInt(); true;}"));
	}
	
	@Test
	public void arbReal() {

		prepare("import Math;");

		assertTrue(runTestInSameEvaluator("{real D = Math::arbReal(); (D >= 0.0) && (D <= 1.0);}"));
		assertTrue(runTestInSameEvaluator("{real D = arbReal(); (D >= 0.0) && (D <= 1.0);}"));
	}
	
	@Test
	public void arbRat() {

		prepare("import Math;");

		assertTrue(runTestInSameEvaluator("{rat R = Math::arbRat(); true;}"));
		assertTrue(runTestInSameEvaluator("{rat R = arbRat(10, 100); (R >= 0) && (R <= 10);}"));
	}
	
	@Test
	public void cos(){
		prepare("import Math;");
		
		assertTrue(runTestInSameEvaluator("{real D = cos(0);        abs(D - 1) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = cos(PI()/2);   abs(D)     < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = cos(PI());     abs(D + 1) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = cos(3*PI()/2); abs(D)     < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = cos(2*PI());   abs(D - 1) < 0.000001;}"));
	}
	
	@Test
	public void denominator(){
		prepare("import Math;");

		assertTrue(runTestInSameEvaluator("{denominator(2r3)== 3;}"));
		assertTrue(runTestInSameEvaluator("{denominator(4r6)== 3;}"));
		assertTrue(runTestInSameEvaluator("{denominator(-2r3)== 3;}"));
	}
	
	@Test
	public void E(){
		prepare("import Math;");

		assertTrue(runTestInSameEvaluator("{E() > 2.7;}"));
		assertTrue(runTestInSameEvaluator("{E() < 2.8;}"));
	}

	@Test
	public void exp(){
		prepare("import Math;");
		
		assertTrue(runTestInSameEvaluator("{real D = exp(0); abs(D - 1) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = exp(1); abs(D - E()) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = exp(2); abs(D - E() * E()) < 0.000001;}"));
	}
	
	@Test
	public void floor(){
		prepare("import Math;");
		
		assertTrue(runTestInSameEvaluator("{floor(0.0) == 0;}"));
		assertTrue(runTestInSameEvaluator("{floor(1.0) == 1;}"));
		assertTrue(runTestInSameEvaluator("{floor(1.1) == 1;}"));
		assertTrue(runTestInSameEvaluator("{floor(1.5) == 1;}"));
		assertTrue(runTestInSameEvaluator("{floor(1.9) == 1;}"));
		assertTrue(runTestInSameEvaluator("{floor(-1.0) == -1;}"));
		assertTrue(runTestInSameEvaluator("{floor(-1.1) == -2;}"));
		assertTrue(runTestInSameEvaluator("{floor(-1.5) == -2;}"));
		assertTrue(runTestInSameEvaluator("{floor(-1.9) == -2;}"));
	}
	
	@Test
	public void ln(){
		prepare("import Math;");
		assertTrue(runTestInSameEvaluator("{real D = ln(exp(2)); abs(D - 2) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = ln(exp(3.5)); abs(D - 3.5) < 0.000001;}"));
	}
	
	@Test
	public void log(){
		prepare("import Math;");
		assertTrue(runTestInSameEvaluator("{real D = log(9,3); abs(D - 2) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = log(81,9); abs(D - 2) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = log(343,7); abs(D - 3) < 0.000001;}"));
	}
	
	@Test
	public void log10(){
		prepare("import Math;");
		assertTrue(runTestInSameEvaluator("{real D = log10(10); abs(D - 1) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = log10(100); abs(D - 2) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = log10(pow(10,5)); abs(D - 5) < 0.000001;}"));
	}
	
	@Test
	public void log2(){
		//TODO: arg <= 0
		prepare("import Math;");
		assertTrue(runTestInSameEvaluator("{real D = log2(4); abs(D - 2) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = log2(16); abs(D - 4) < 0.000001;}"));
	}

	@Test
	public void maxInt() {

		prepare("import Math;");

		assertTrue(runTestInSameEvaluator("Math::max(3, 10) == 10;"));
		assertTrue(runTestInSameEvaluator("max(3, 10) == 10;"));
		assertTrue(runTestInSameEvaluator("Math::max(10, 10) == 10;"));
	}
	
	@Test
	public void maxRat() {

		prepare("import Math;");

		assertTrue(runTestInSameEvaluator("Math::max(2r3, 2r4) == 2r3;"));
		assertTrue(runTestInSameEvaluator("max(2r3, 2r4) == 2r3;"));
		assertTrue(runTestInSameEvaluator("max(2r3, 2r3) == 2r3;"));
		assertTrue(runTestInSameEvaluator("max(-2r3, 2r4) == 2r4;"));
	}
	
	@Test
	public void maxReal() {

		prepare("import Math;");

		assertTrue(runTestInSameEvaluator("Math::max(3.0, 10.0) == 10.0;"));
		assertTrue(runTestInSameEvaluator("max(3.0, 10.0) == 10.0;"));
		assertTrue(runTestInSameEvaluator("Math::max(10.0, 10.0) == 10.0;"));
	}


	@Test
	public void minInt() {

		prepare("import Math;");

		assertTrue(runTestInSameEvaluator("Math::min(3, 10) == 3;"));
		assertTrue(runTestInSameEvaluator("min(3, 10) == 3;"));
		assertTrue(runTestInSameEvaluator("Math::min(10, 10) == 10;"));
	}
	
	@Test
	public void minRat() {

		prepare("import Math;");

		assertTrue(runTestInSameEvaluator("Math::min(2r3, 2r4) == 2r4;"));
		assertTrue(runTestInSameEvaluator("min(2r3, 2r4) == 2r4;"));
		assertTrue(runTestInSameEvaluator("min(2r3, 2r3) == 2r3;"));
		assertTrue(runTestInSameEvaluator("min(-2r3, 2r4) == -2r3;"));
	}
	
	@Test
	public void minReal() {

		prepare("import Math;");

		assertTrue(runTestInSameEvaluator("Math::min(3.0, 10.0) == 3.0;"));
		assertTrue(runTestInSameEvaluator("min(3.0, 10.0) == 3.0;"));
		assertTrue(runTestInSameEvaluator("Math::min(10.0, 10.0) == 10.0;"));
	}
	
	@Test
	public void numerator(){
		prepare("import Math;");
		
		assertTrue(runTestInSameEvaluator("{numerator(2r3)== 2;}"));
		assertTrue(runTestInSameEvaluator("{numerator(4r6)== 2;}"));
		assertTrue(runTestInSameEvaluator("{numerator(-2r3)== -2;}"));
		assertTrue(runTestInSameEvaluator("{numerator(-4r6)== -2;}"));
	}
	
	@Test
	public void nroot(){
		prepare("import Math;");
	}
	
	@Test
	public void PI(){
		prepare("import Math;");
		
		assertTrue(runTestInSameEvaluator("{PI() > 3.14;}"));
		assertTrue(runTestInSameEvaluator("{PI() < 3.15;}"));
	}
	
	@Test
	public void pow(){
		prepare("import Math;");
	}
	
	@Test
	public void remainder(){
		prepare("import Math;");
		assertTrue(runTestInSameEvaluator("{remainder(2r3)== 2;}"));
		assertTrue(runTestInSameEvaluator("{remainder(3r2)== 1;}"));
		assertTrue(runTestInSameEvaluator("{remainder(4r2)== 0;}"));
		assertTrue(runTestInSameEvaluator("{remainder(-2r3)== -2;}"));
	}
	
	@Test
	public void sin(){
		prepare("import Math;");
		assertTrue(runTestInSameEvaluator("{real D = sin(0);        abs(D)     < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = sin(PI()/2);   abs(D - 1) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = sin(PI());     abs(D)     < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = sin(3*PI()/2); abs(D + 1) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = sin(2*PI());   abs(D)     < 0.000001;}"));
	}
	
	@Test
	public void sqrt(){
		prepare("import Math;");
		//assertTrue(runTestInSameEvaluator("{real D = sqrt(0);        abs(D)     < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = sqrt(1);        abs(D - 1) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = sqrt(2);        abs(D*D - 2) < 0.000001;}"));
	}
	
	@Test
	public void tan(){
		prepare("import Math;");
		// TODO: arg < -pi/2 or > pi/2
		assertTrue(runTestInSameEvaluator("{real D = tan(0);        abs(D)     < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = tan(PI()/4);   abs(D - 1) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = tan(-PI()/4);  abs(D + 1) < 0.000001;}"));
	}
	
	@Test
	public void toRat() {

		prepare("import Math;");

		assertTrue(runTestInSameEvaluator("Math::toRat(3, 4) == 3r4;"));
		assertTrue(runTestInSameEvaluator("toRat(3, 4) == 3r4;"));
	}

	@Test
	public void toRealInt() {

		prepare("import Math;");

		assertTrue(runTestInSameEvaluator("Math::toReal(3) == 3.0;"));
		assertTrue(runTestInSameEvaluator("toReal(3) == 3.0;"));
	}

	
	@Test
	public void toIntReal() {

		prepare("import Math;");

		assertTrue(runTestInSameEvaluator("Math::toInt(3.14) == 3;"));
		assertTrue(runTestInSameEvaluator("toInt(3.14) == 3;"));
	}
	
	@Test
	public void toStringInt() {

		prepare("import Math;");

		assertTrue(runTestInSameEvaluator("Math::toString(314) == \"314\";"));
		assertTrue(runTestInSameEvaluator("toString(314) == \"314\";"));
	}

	@Test
	public void toStringReal() {

		prepare("import Math;");

		assertTrue(runTestInSameEvaluator("Math::toString(3.14) == \"3.14\";"));
		assertTrue(runTestInSameEvaluator("toString(3.14) == \"3.14\";"));

	}

}
