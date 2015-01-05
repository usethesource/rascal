@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Wietse Venema - wietsevenema@gmail.com - CWI}
module cobra::tests::quickcheck::quickcheck
import cobra::quickcheck;
import cobra::tests::quickcheck::imported;
import cobra::util::outputlogger;

import Prelude;

test bool testQCShouldFailWithIncorrectProperty(){
	return silentQuickcheck(bool (int a){return false;}, 10, 10) == false;
}

test bool testQCShouldFailWithCorrectProperty(){
	return silentQuickcheck(bool (int a){return true;}, 10, 10 );
}

test bool testQCShouldFailWhenThrownException(){
	return silentQuickcheck(bool (int a){throw IllegalArgument();}, 10, 10 ) == false;
}

test bool withZeroArgsShouldEvaluateOnce(){
	startLog();
	silentQuickcheck( bool(){ println("1"); return true; }, 5, 10);
	list[str] lines = split("\n", getLog());
	return size(lines) == 1;
}

test bool testQCWithZeroArgs(){
	return silentQuickcheck(bool (){return true;}, 10, 10 );
}

test bool testQCWithOneArg(){
	return silentQuickcheck(bool (int a){return true;}, 10, 10 );
}

test bool testQCWithTwoArgs(){
	return silentQuickcheck(bool (int a, str b){return false;}, 10, 10 ) == false;
}

test bool testQCWithThreeArgs(){
	return silentQuickcheck(bool (map[int, bool] a, rel[real, str] b, real c){return true;}, 10, 10);
}

test bool testQCWithFourArgs(){
	return silentQuickcheck(bool (list[set[int]] a, str b, real c, list[int] d){return true;}, 10, 10 );
}

test bool testQCWithFiveArgs(){
	return silentQuickcheck(bool (int a, str b, real c, list[int] d, set[str] e){return true;}, 10, 10 );
}

data myData = myData(int n);
test bool quickcheckShouldThrowIllegalArgument(){
	bool(myData) heightProp = bool(myData d) { return true; };
	try quickcheck(heightProp, 0, 10);
	catch IllegalArgument(0, _): return true;
	return false;  
}


test bool quickcheckShouldWorkWithImportedDataType(){
	return quickcheck( propImported, 10, 10);
}

test bool quickcheckShouldWorkWithOverloadedFunction(){
	startLog();
	bool result = verboseQuickcheck( propOverloaded, 10, 1);
	list[str] lines = split("\n", getLog());
	return 
		/^1: Checked with \[-*\d+\]: true$/ := lines[0] &&
		"Not refuted after 1 tries with maximum depth 10" == lines[1] &&
		/^1: Checked with \[\".*\"\]: true$/ := lines[2] &&
		"Not refuted after 1 tries with maximum depth 10" == lines[3] &&
		result;	
}

test bool shouldThrowExceptionWhenCalledWithInvalidFunction(){
	try quickcheck("test");
	catch IllegalArgument( str s, str e): {
		return s == "test" && e == "Argument should be function.";
	}
	return false;
}

test bool shouldThrowExceptionWhenReturnTypeNotBool(){
	int( str ) property = int ( str a){ return 1; }; 
	try quickcheck(property);
	catch IllegalArgument( int(str) p, str e): {
		return (p == property && e == "Return type should be bool.");
	}
	return false;
}

data MyData = myData(int n);
test bool shouldThrowExceptionWhenNoDepth(){
	try quickcheck(bool(MyData d){ return true;}, 0, 10);
	catch IllegalArgument( int d, str e): {
		return (d == 0 && e == "No construction possible at this depth or less.");
	}
	return false;
}


