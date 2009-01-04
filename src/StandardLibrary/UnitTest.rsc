module UnitTest

private int nTests = 0;
private int nAssertions = 0;
private int nErrors = 0;
private int nFailures = 0;

public void assertTrue(bool outcome)
{
	nAssertions = nAssertions + 1;
	if(!outcome){
		nFailures = nFailures + 1;
	}
}

private void runTest(void () test)
{
	try {
		test();
	} catch(tree Any){
		nErrors = nErrors + 1;
	}
}

private list[void()] java getTests()
{
}

public void runAllTests()
{
	for(Test : getTests()){
		nTests = nTests + 1;
		Test();
	}
}

public void report()
{
	println("<nTests> tests, <nAssertions> assertions, <nFailures> failures, <nErrors> errors");
}

