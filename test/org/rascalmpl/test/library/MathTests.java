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
	
	
}
