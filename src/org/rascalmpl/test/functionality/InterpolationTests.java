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
*******************************************************************************/
package org.rascalmpl.test.functionality;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.test.TestFramework;

public class InterpolationTests extends TestFramework {

	
	@Test
	public void interpolateWhile() {
		assertTrue(runTest("{ x = 10; \"<while (x > 0) {> <{x -= 1; x; }> <}>\" == " +
				" \" 9  8  7  6  5  4  3  2  1  0 \"; }"));
	}

	@Test
	public void interpolateDoWhile() {
		assertTrue(runTest("{ x = 10; \"<do {> <{x -= 1; x; }> <} while (x > 0)>\" == " +
				" \" 9  8  7  6  5  4  3  2  1  0 \"; }"));
	}

	@Test
	public void interpolateIfThenElse() {
		assertTrue(runTest("\"abc <if (1 > 0) {> GT <} else {> LT <}> cde\" == " +
				" \"abc  GT  cde\";"));
	}

	@Test
	public void interpolateIfThenTrue() {
		assertTrue(runTest("\"abc <if (1 > 0) {> GT <}> cde\" == " +
				" \"abc  GT  cde\";"));
	}

	@Test
	public void interpolateIfThenFalse() {
		assertTrue(runTest("\"abc <if (0 > 0) {> GT <}> cde\" == " +
				" \"abc  cde\";"));
	}
	
	@Test
	public void interpolateFor() {
		assertTrue(runTest("\"abc <for (i <- [1,2,3]) {> print <i> <}> cde\" == " +
				"\"abc  print 1  print 2  print 3  cde\";"));
	}
	
	@Test
	public void interpolateForNested() {
		assertTrue(runTest("\"<for (x <- [1,2,3]) {>outer <x> <for (y <- [4,5,6]) {>inner <x>,<y> <}><}>\" == " +
				"\"outer 1 inner 1,4 inner 1,5 inner 1,6 outer 2 inner 2,4 inner 2,5 inner 2,6 outer 3 inner 3,4 inner 3,5 inner 3,6 \";"));
		
	}
	
	@Test
	public void interpolatePreFor() {
		assertTrue(runTest("\"<for (i <- [1,2,3]) { j = i + 1;> <j> <}>\" == " +
			"\" 2  3  4 \";"));
	}

	@Test
	public void interpolatePostWhile() {
		assertTrue(runTest("{ x = 5; \"<while (x > 0) {> <x> < x -= 1; }>\" == " +
			"\" 5  4  3  2  1 \";}"));
	}

}
