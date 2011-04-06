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
import org.rascalmpl.test.library.NodeTests;
import org.rascalmpl.test.library.NumberTests;
import org.rascalmpl.test.library.RealTests;
import org.rascalmpl.test.library.RelationTests;
import org.rascalmpl.test.library.SetTests;
import org.rascalmpl.test.library.StringTests;
import org.rascalmpl.test.library.ValueIOTests;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	NodeTests.class,
	NumberTests.class,
	RealTests.class,
	RelationTests.class,
	RuleTests.class,
	SetTests.class,
	StringTests.class,
	SubscriptTests.class,
	ProjectionTests.class,
	StatementTests.class,
	TryCatchTests.class,
	ValueIOTests.class,
	VisitTests.class,
	StrategyTests.class
})

public class AllTests4 {
// Empty
}
