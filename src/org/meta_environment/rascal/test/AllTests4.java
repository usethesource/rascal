package org.meta_environment.rascal.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.meta_environment.rascal.test.StandardLibraryTests.NodeTests;
import org.meta_environment.rascal.test.StandardLibraryTests.RealTests;
import org.meta_environment.rascal.test.StandardLibraryTests.RelationTests;
import org.meta_environment.rascal.test.StandardLibraryTests.SetTests;
import org.meta_environment.rascal.test.StandardLibraryTests.StringTests;
import org.meta_environment.rascal.test.StandardLibraryTests.ValueIOTests;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	NodeTests.class,
	RealTests.class,
	RelationTests.class,
	RuleTests.class,
	SetTests.class,
	StringTests.class,
	SubscriptTests.class,
	StatementTests.class,
	TryCatchTests.class,
	ValueIOTests.class,
	VisitTests.class,
	StrategyTests.class
})

public class AllTests4 {
// Empty
}
