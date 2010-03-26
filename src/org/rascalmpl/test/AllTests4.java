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
	StatementTests.class,
	TryCatchTests.class,
	ValueIOTests.class,
	VisitTests.class,
	StrategyTests.class
})

public class AllTests4 {
// Empty
}
