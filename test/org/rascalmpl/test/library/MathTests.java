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

import java.math.BigDecimal;

import org.junit.Test;
import org.rascalmpl.test.infrastructure.TestFramework;


public class MathTests extends TestFramework {

	@Test
	public void absInt() {

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("{abs(0) == 0;}"));
		assertTrue(runTestInSameEvaluator("{abs(-1) == 1;}"));
		assertTrue(runTestInSameEvaluator("{abs(1) == 1;}"));
	}
	
	@Test
	public void absReal() {

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("{abs(0.0) == 0.0;}"));
		assertTrue(runTestInSameEvaluator("{abs(-1.0) == 1.0;}"));
		assertTrue(runTestInSameEvaluator("{abs(1.0) == 1.0;}"));
	}
	
	@Test
	public void absRat() {

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("{abs(0r) == 0r;}"));
		assertTrue(runTestInSameEvaluator("{abs(-1r1) == 1r1;}"));
		assertTrue(runTestInSameEvaluator("{abs(1r1) == 1r1;}"));
	}

	@Test
	public void arbInt() {

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("{int N = util::Math::arbInt(10); (N >= 0) && (N < 10);}"));
		assertTrue(runTestInSameEvaluator("{int N = arbInt(10); (N >= 0) && (N < 10);}"));

		assertTrue(runTestInSameEvaluator("{int N = arbInt(); true;}"));
	}
	
	@Test
	public void arbReal() {

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("{real D = util::Math::arbReal(); (D >= 0.0) && (D <= 1.0);}"));
		assertTrue(runTestInSameEvaluator("{real D = arbReal(); (D >= 0.0) && (D <= 1.0);}"));
	}
	
	@Test
	public void arbRat() {

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("{rat R = util::Math::arbRat(); true;}"));
		assertTrue(runTestInSameEvaluator("{rat R = arbRat(10, 100); (R >= 0) && (R <= 10);}"));
	}
	
	@Test
	public void cos(){
		prepare("import util::Math;");
		
		assertTrue(runTestInSameEvaluator("{real D = cos(0);        abs(D - 1) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = cos(PI()/2);   abs(D)     < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = cos(PI());     abs(D + 1) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = cos(3*PI()/2); abs(D)     < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = cos(2*PI());   abs(D - 1) < 0.000001;}"));
	}
	
	@Test
	public void cosExtensive() {
		prepare("import util::Math;");
		
		BigDecimal start = BigDecimal.valueOf(-100);
		BigDecimal stop = start.negate();
		BigDecimal increments = BigDecimal.ONE.divide(BigDecimal.valueOf(10));
		for (BigDecimal param = start; stop.compareTo(param) > 0; param = param.add(increments)) {
			assertTrue(runTestInSameEvaluator("{ real D = cos(" + param.toString() + "); abs(D - (" + Double.toString(Math.cos(param.doubleValue())) + ")) < 0.00001; }"));
		}
	}	
	
	@Test
	public void denominator(){
		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("{denominator(2r3)== 3;}"));
		assertTrue(runTestInSameEvaluator("{denominator(4r6)== 3;}"));
		assertTrue(runTestInSameEvaluator("{denominator(-2r3)== 3;}"));
	}
	
	@Test
	public void E(){
		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("{E() > 2.7;}"));
		assertTrue(runTestInSameEvaluator("{E() < 2.8;}"));
	}

	@Test
	public void exp(){
		prepare("import util::Math;");
		
		assertTrue(runTestInSameEvaluator("{real D = exp(0); abs(D - 1) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = exp(1); abs(D - E()) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = exp(2); abs(D - E() * E()) < 0.000001;}"));
	}
	
	@Test
	public void floor(){
		prepare("import util::Math;");
		
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
		prepare("import util::Math;");
		assertTrue(runTestInSameEvaluator("{real D = ln(exp(2)); abs(D - 2) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = ln(exp(3.5)); abs(D - 3.5) < 0.000001;}"));
	}
	
	@Test
	public void log(){
		prepare("import util::Math;");
		assertTrue(runTestInSameEvaluator("{real D = log(9,3); abs(D - 2) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = log(81,9); abs(D - 2) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = log(343,7); abs(D - 3) < 0.000001;}"));
	}
	
	@Test
	public void log10(){
		prepare("import util::Math;");
		assertTrue(runTestInSameEvaluator("{real D = log10(10); abs(D - 1) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = log10(100); abs(D - 2) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = log10(pow(10,5)); abs(D - 5) < 0.000001;}"));
	}
	
	@Test
	public void log2(){
		//TODO: arg <= 0
		prepare("import util::Math;");
		assertTrue(runTestInSameEvaluator("{real D = log2(4); abs(D - 2) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = log2(16); abs(D - 4) < 0.000001;}"));
	}

	@Test
	public void maxInt() {

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("util::Math::max(3, 10) == 10;"));
		assertTrue(runTestInSameEvaluator("max(3, 10) == 10;"));
		assertTrue(runTestInSameEvaluator("util::Math::max(10, 10) == 10;"));
	}
	
	@Test
	public void maxRat() {

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("util::Math::max(2r3, 2r4) == 2r3;"));
		assertTrue(runTestInSameEvaluator("max(2r3, 2r4) == 2r3;"));
		assertTrue(runTestInSameEvaluator("max(2r3, 2r3) == 2r3;"));
		assertTrue(runTestInSameEvaluator("max(-2r3, 2r4) == 2r4;"));
	}
	
	@Test
	public void maxReal() {

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("util::Math::max(3.0, 10.0) == 10.0;"));
		assertTrue(runTestInSameEvaluator("max(3.0, 10.0) == 10.0;"));
		assertTrue(runTestInSameEvaluator("util::Math::max(10.0, 10.0) == 10.0;"));
	}


	@Test
	public void minInt() {

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("util::Math::min(3, 10) == 3;"));
		assertTrue(runTestInSameEvaluator("min(3, 10) == 3;"));
		assertTrue(runTestInSameEvaluator("util::Math::min(10, 10) == 10;"));
	}
	
	@Test
	public void minRat() {

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("util::Math::min(2r3, 2r4) == 2r4;"));
		assertTrue(runTestInSameEvaluator("min(2r3, 2r4) == 2r4;"));
		assertTrue(runTestInSameEvaluator("min(2r3, 2r3) == 2r3;"));
		assertTrue(runTestInSameEvaluator("min(-2r3, 2r4) == -2r3;"));
	}
	
	@Test
	public void minReal() {

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("util::Math::min(3.0, 10.0) == 3.0;"));
		assertTrue(runTestInSameEvaluator("min(3.0, 10.0) == 3.0;"));
		assertTrue(runTestInSameEvaluator("util::Math::min(10.0, 10.0) == 10.0;"));
	}
	
	@Test
	public void numerator(){
		prepare("import util::Math;");
		
		assertTrue(runTestInSameEvaluator("{numerator(2r3)== 2;}"));
		assertTrue(runTestInSameEvaluator("{numerator(4r6)== 2;}"));
		assertTrue(runTestInSameEvaluator("{numerator(-2r3)== -2;}"));
		assertTrue(runTestInSameEvaluator("{numerator(-4r6)== -2;}"));
	}
	
	@Test
	public void nroot(){
		prepare("import util::Math;");
		assertTrue(runTestInSameEvaluator("{real D = nroot(10,1);        abs(D - 10)     < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = nroot(10,2);        abs(D*D - 10)   < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = nroot(10,3);        abs(D*D*D - 10) < 0.000001;}"));
	}
	
	@Test
	public void PI(){
		prepare("import util::Math;");
		
		assertTrue(runTestInSameEvaluator("{PI() > 3.14;}"));
		assertTrue(runTestInSameEvaluator("{PI() < 3.15;}"));
	}
	
	@Test
	public void pow(){
		prepare("import util::Math;");
		
		assertTrue(runTestInSameEvaluator("{real D = pow(7,0);        abs(D - 1)      < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = pow(7,1);        abs(D - 7)      < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = pow(7,2);        abs(D - 7*7)    < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = pow(7,3);        abs(D - 7*7*7)  < 0.000001;}"));
	}
	
	@Test
	public void remainder(){
		prepare("import util::Math;");
		assertTrue(runTestInSameEvaluator("{remainder(2r3)== 2;}"));
		assertTrue(runTestInSameEvaluator("{remainder(3r2)== 1;}"));
		assertTrue(runTestInSameEvaluator("{remainder(4r2)== 0;}"));
		assertTrue(runTestInSameEvaluator("{remainder(-2r3)== -2;}"));
	}
	
	@Test
	public void sin(){
		prepare("import util::Math;");
		assertTrue(runTestInSameEvaluator("{real D = sin(0);        abs(D)     < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = sin(PI()/2);   abs(D - 1) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = sin(PI());     abs(D)     < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = sin(3*PI()/2); abs(D + 1) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = sin(2*PI());   abs(D)     < 0.000001;}"));
	}
	
	@Test
	public void sinExtensive() {
		prepare("import util::Math;");
		
		BigDecimal start = BigDecimal.valueOf(-100);
		BigDecimal stop = start.negate();
		BigDecimal increments = BigDecimal.ONE.divide(BigDecimal.valueOf(10));
		for (BigDecimal param = start; stop.compareTo(param) > 0; param = param.add(increments)) {
			assertTrue(runTestInSameEvaluator("{ real D = sin(" + param.toString() + "); abs(D - (" + Double.toString(Math.sin(param.doubleValue())) + ")) < 0.00001; }"));
		}
	}	
	
	
	@Test
	public void sqrt(){
		prepare("import util::Math;");
		//assertTrue(runTestInSameEvaluator("{real D = sqrt(0);        abs(D)     < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = sqrt(1);        abs(D - 1) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = sqrt(2);        abs(D*D - 2) < 0.000001;}"));
	}
	
	@Test
	public void tan(){
		prepare("import util::Math;");
		// TODO: arg < -pi/2 or > pi/2
		assertTrue(runTestInSameEvaluator("{real D = tan(0);        abs(D)     < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = tan(PI()/4);   abs(D - 1) < 0.000001;}"));
		assertTrue(runTestInSameEvaluator("{real D = tan(-PI()/4);  abs(D + 1) < 0.000001;}"));
	}
	
	@Test
	public void tanExtensive() {
		prepare("import util::Math;");
		
		BigDecimal start = BigDecimal.valueOf(Math.PI).divide(BigDecimal.valueOf(2)).negate();
		BigDecimal stop = start.negate();
		BigDecimal increments = BigDecimal.ONE.divide(BigDecimal.valueOf(100));
		
		// around pi/2 tan is undefined so we skip checking around that.
		start = start.add(increments);
		stop = stop.subtract(increments);
		for (BigDecimal param = start; stop.compareTo(param) > 0; param = param.add(increments)) {
			assertTrue(runTestInSameEvaluator("{ real D = tan(" + param.toString() + "); abs(D - (" + Double.toString(Math.tan(param.doubleValue())) + ")) < 0.00001; }"));
		}
	}
	
	
	@Test
	public void toRat() {

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("util::Math::toRat(3, 4) == 3r4;"));
		assertTrue(runTestInSameEvaluator("toRat(3, 4) == 3r4;"));
	}

	@Test
	public void toRealInt() {

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("util::Math::toReal(3) == 3.0;"));
		assertTrue(runTestInSameEvaluator("toReal(3) == 3.0;"));
	}

	@Test
	public void toIntReal() {

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("util::Math::toInt(3.14) == 3;"));
		assertTrue(runTestInSameEvaluator("toInt(3.14) == 3;"));
	}
	
	@Test
	public void toStringInt() {

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("util::Math::toString(314) == \"314\";"));
		assertTrue(runTestInSameEvaluator("toString(314) == \"314\";"));
	}

	@Test
	public void toStringReal() {

		prepare("import util::Math;");

		assertTrue(runTestInSameEvaluator("util::Math::toString(3.14) == \"3.14\";"));
		assertTrue(runTestInSameEvaluator("toString(3.14) == \"3.14\";"));

	}

}
