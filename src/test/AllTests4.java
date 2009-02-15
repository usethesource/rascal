package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	StandardLibraryMapTests.class,
	StandardLibraryNodeTests.class,
	StandardLibraryRelationTests.class,
	StandardLibrarySetTests.class,
	StandardLibraryStringTests.class,
	SubscriptTests.class,
	StatementTests.class,
	TryCatchTests.class,
	VisitTests.class
})

public class AllTests4 {

}
