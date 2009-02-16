module UnitTest

import IO;

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

public void assertEqual(value V1, value V2)
{
	nAssertions = nAssertions + 1;
	if(V1 != V2){
		nFailures = nFailures + 1;
		println("assertEqual fails: <V1> != <V2>");
	}
}

private void runTest(void () test)
{
	try {
		test();
	} catch node Any: {
		nErrors = nErrors + 1;
	}
}

/*
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
*/

public bool report()
{
	println("<nAssertions> assertions, <nFailures> failures, <nErrors> errors");
	return nFailures == 0 && nErrors == 0;
}

