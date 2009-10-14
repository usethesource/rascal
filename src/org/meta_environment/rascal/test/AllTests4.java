package org.meta_environment.rascal.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.meta_environment.rascal.test.library.NodeTests;
import org.meta_environment.rascal.test.library.RealTests;
import org.meta_environment.rascal.test.library.RelationTests;
import org.meta_environment.rascal.test.library.SetTests;
import org.meta_environment.rascal.test.library.StringTests;
import org.meta_environment.rascal.test.library.ValueIOTests;


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
