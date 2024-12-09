/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Davy Landman  - Davy.Landman@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.test;

import org.junit.runner.RunWith;
import org.rascalmpl.test.infrastructure.RecursiveTest;
import org.rascalmpl.test.infrastructure.RecursiveTestSuite;

@RunWith(RecursiveTestSuite.class)
@RecursiveTest({"basic", "concrete", "extend", "functionality", "imports", "library", "parser", "recovery", "syntax", "demo", "benchmark"})
public class AllSuite {
}
