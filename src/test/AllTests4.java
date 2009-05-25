package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.StandardLibraryTests.NodeTests;
import test.StandardLibraryTests.RealTests;
import test.StandardLibraryTests.RelationTests;
import test.StandardLibraryTests.SetTests;
import test.StandardLibraryTests.StringTests;
import test.StandardLibraryTests.ValueIOTests;

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
	VisitTests.class
})

public class AllTests4 {

}
