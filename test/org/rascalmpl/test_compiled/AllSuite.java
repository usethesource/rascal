/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Davy Landman  - Davy.Landman@cwi.nl - CWI
 *   * Paul Klint     - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.test_compiled;

import org.junit.runner.RunWith;
import org.rascalmpl.test.infrastructure.RecursiveTest;
import org.rascalmpl.test.infrastructure.RecursiveTestSuite;

@RunWith(RecursiveTestSuite.class)
@RecursiveTest({"basic", 
                "compiler",
                "demo", 
                "extend", 
                "format",
                "functionality",
                "grammar",
                "imports", 
                "library", 
                "meta",
                "parser",
                "syntax",
                "types"
                })
public class AllSuite { }
