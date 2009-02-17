package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.StandardLibraryTests.MapTests;
import test.StandardLibraryTests.NodeTests;
import test.StandardLibraryTests.RelationTests;
import test.StandardLibraryTests.SetTests;
import test.StandardLibraryTests.StringTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	MapTests.class,
	NodeTests.class,
	RelationTests.class,
	SetTests.class,
	StringTests.class,
	SubscriptTests.class,
	StatementTests.class,
	TryCatchTests.class,
	VisitTests.class
})

public class AllTests4 {

}
