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
package org.rascalmpl.test.functionality;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.test.TestFramework;

public class ProjectionTests extends TestFramework {

	@Test
	public void empty() {
		assertTrue(runTest("{}<0> == {};"));
		assertTrue(runTest("{}<1> == {};"));
	}
	
	@Test
	public void nonEmpty() {
		assertTrue(runTest("{<1,2>}<0> == {1}"));
		assertTrue(runTest("{<1,2>}<1> == {2}"));
	}
	
	@Test(expected=Throw.class)
	public void outOfBounds() {
		assertTrue(runTest("{<1,2>}<2> == {2}"));
	}
	
}
