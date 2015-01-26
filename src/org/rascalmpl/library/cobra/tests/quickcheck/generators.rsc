@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Wietse Venema - wietsevenema@gmail.com - CWI}
module cobra::tests::quickcheck::generators

import cobra::quickcheck;
import cobra::monadicgenerators;
import cobra::arbitrary;

public str(int) genStringAlphanumeric = bind( iszero, Gen[str] (bool a) {
		if(a){
			return unit("");
		} else {
			return arbStringAlphanumeric;
		}
	}
); 

//Test for regression (below zero exception with custom generator)
test bool belowZeroWithCustomGenerator(){
	setGenerator(genStringAlphanumeric);
	return quickcheck( bool( rel[str, int] a) { return true; });	
}


private bool checkArbitraryAndTeardown(type[&T] reified, &T expected){
	&T val = arbitrary(reified, 999);
	resetGenerator(reified);
	return val == expected;
}

/*TESTS*/
public int myInt(int d){
	return 1;
}

test bool setGeneratorWithPublicFunction(){
	setGenerator(myInt);
	return checkArbitraryAndTeardown(#int, 1);
}

private int _myInt(int d){
	return 2;
}

test bool setGeneratorWithPrivateFunction(){
	setGenerator(_myInt);
	return checkArbitraryAndTeardown(#int, 2);	
}

test bool setGeneratorWithClosure(){
	setGenerator(int(int d){return 3;});
	return checkArbitraryAndTeardown(#int, 3);
}


test bool secondSetGeneratorOverridesFirst(){
	setGenerator(int(int d){return 5;});
	setGenerator(int(int d){return 6;});
	return checkArbitraryAndTeardown(#int, 6);
}

test bool resetGeneratorShouldReset(){
	setGenerator(int(int d){return 4;});
	resetGenerator(#int);
	return (arbitrary(#int, 999) != 4 || arbitrary(#int, 999) != 4 || arbitrary(#int, 999) != 4);
}


		

test bool testGetGeneratorReturnsRightGenerator(){
	int(int) gen = int(int d){ return 7; };
	setGenerator( gen );
	bool result = ( gen == getGenerator(#int) );
	resetGenerator(#int); 
	return result;	
}


