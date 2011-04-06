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
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.rascalmpl.test.library.BooleanTests;
import org.rascalmpl.test.library.GraphTests;
import org.rascalmpl.test.library.IntegerTests;
import org.rascalmpl.test.library.ListTests;
import org.rascalmpl.test.library.MapTests;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	
	BooleanTests.class,
	GraphTests.class,
	IntegerTests.class,
	ListTests.class,
	MapTests.class
	
})

public class AllTests3 {
// Empty
}

