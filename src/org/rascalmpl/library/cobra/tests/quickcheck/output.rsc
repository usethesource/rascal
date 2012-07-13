module cobra::tests::quickcheck::output


test bool silentQuickcheckShouldBeSilent(){
	startLog();
	silentQuickcheck(bool (int a){return true;}, 10, 10 );
	return getLog() == "";
}


test bool silentQuickcheckShouldBeSilent(){
	startLog();
	silentQuickcheck(bool (){return true;} );
	return getLog() == "";
}

test bool failureShouldBeReported(){
	startLog();
	quickcheck(bool (int a){return false;}, 10, 10 );
	return /^FAILED with \[-*\d+\]$/ := getLog();
}

test bool successShouldBeReported(){
	startLog();
	quickcheck(bool (int a){return true;}, 5, 10 );
	return /^Not refuted after 10 tries with maximum depth 5$/ := getLog();
}


test bool failureWithExceptionShouldBeReported(){
	startLog();
	quickcheck(bool (int a){throw IllegalArgument("My exception");}, 10, 10 );
	list[str] lines = split("\n", getLog());
	return
		(size(lines) == 2) && 
		(/^FAILED with \[-*\d+\]$/ := lines[0]) &&
		(/\/output.rsc:\d+,\d+: IllegalArgument\(\"My exception\"\)/ := lines[1])
	; 
}


test bool failureWithNoArguments(){
	startLog();
	quickcheck(bool (){ return false;} );
	return /^failed$/ := getLog(); 
}

test bool failureWithExceptionAndNoException(){
	startLog();
	quickcheck(bool () {throw IllegalArgument("My exception");}, 10, 10 );
	list[str] lines = split("\n", getLog());
	return
		(size(lines) == 2) && 
		(/^failed$/ := lines[0]) &&
		(/\/output.rsc:\d+,\d+: IllegalArgument\(\"My exception\"\)/ := lines[1])
	; 
}


test bool verboseFailureWithNoArguments(){
	startLog();
	verboseQuickcheck(bool (){ return false;} );
	return /^failed$/ := getLog();  
}

test bool successWithNoArguments(){
	startLog();
	quickcheck(bool (){ return true;} );
	return /^succeeded$/ := getLog(); 
}

test bool verboseSuccessWithNoArguments(){
	startLog();
	verboseQuickcheck(bool (){ return true;} );
	return /^succeeded$/ := getLog(); 
}


test bool verboseShouldReportAll(){
	startLog();
	verboseQuickcheck(bool (int a){return true;}, 10, 10 );
	list[str] lines = split("\n", getLog());
	for( int n <- [1 .. 10]){
		if(! /^<n>: Checked with \[-*\d+\]: true$/ := lines[(n-1)]){
			return false;
		}
	}
	return lines[10] == "Not refuted after 10 tries with maximum depth 10"; 
}
