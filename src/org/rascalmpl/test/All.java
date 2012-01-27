/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Bas Basten - Bas.Basten@cwi.nl (CWI)
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
import org.rascalmpl.test.library.NodeTests;
import org.rascalmpl.test.library.NumberTests;
import org.rascalmpl.test.library.RealTests;
import org.rascalmpl.test.library.RelationTests;
import org.rascalmpl.test.library.SetTests;
import org.rascalmpl.test.library.StringTests;
import org.rascalmpl.test.library.ValueIOTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	
	AccumulatingTests.class,
	AliasTests.class,
	AnnotationTests.class,
	AssignmentTests.class,
	
	BackTrackingTests.class,
	BooleanTests.class,
	
	CallTests.class,
	ComprehensionTests.class,

	
	DataDeclarationTests.class,
	DataTypeTests.class,
	DeclarationTests.class,
	
	GraphTests.class,
	
	ImportTests.class,
	IntegerTests.class,
	InterpolationTests.class,
	IOTests.class,
	
	ListTests.class,
	
	MapTests.class,
	
	NodeTests.class,
	NumberTests.class,
	
	PatternTests.class,
	ProjectionTests.class,
	
	RangeTests.class,
	RealTests.class,
	RecoveryTests.class,
	RegExpTests.class,
	RelationTests.class,
	
	ScopeTests.class,
	SetTests.class,
	StatementTests.class,
	StrategyTests.class,
	StringTests.class,
	SubscriptTests.class,

	TryCatchTests.class,
	
	ValueIOTests.class,
	VisitTests.class,
	
	PreBootstrapTests.class,
	
	AllBenchmarks.class,
	AllDemoTests.class,
	
	ConcreteSyntaxTests.class
        })
public class All {
// Empty
}
