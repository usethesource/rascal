@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Wietse Venema - wietsevenema@gmail.com - CWI}
module cobra::tests::quickcheck::defaultgenerator
import cobra::quickcheck;
import Prelude;
import Type;
import IO;
import util::Math;

private bool checkArbitraryAndTeardown(type[&T] reified, &T expected){
	&T val = arbitrary(reified, 999);
	resetGenerator(reified);
	return val == expected;
}

data Con = cons(int n);
test bool dynamicGeneratorWithDatatype(){
	setGenerator( int(int d){return 7;} );
	result = cons(7) == arbitrary(#Con, 999);
	resetGenerator(#int);
	return result;
}

test bool defaultGeneratorIsDynamicFunction(){ 
	str(int) generator = getGenerator(#str);
	return typeOf(generator(1)) == \str();
}

data customStr = String(str s);
test bool defaultGeneratorExistsForString(){
	return String(_) := arbitrary(#customStr, 10); 
}

data _Exp = _con(int n)
		| _add( _Exp lhs, _Exp rhs );
public int height( _con( int a )) = 1;
public int height( _add( _Exp lhs, _Exp rhs)) = max([height(lhs), height(rhs)])+1;	
test bool defaultGeneratorWithDatatypeRespectsDepthParameter(){
	dTest = bool(int d){ return height(arbitrary(#_Exp, d)) <= d;}; 
	for( int n <- [1..200]){
		if(!dTest(n)) return false;
	}
	return true;
}

test bool expectedLengthOfListIsOne(){
	int sample = 500;
	list[int] a; 
	a = for( int n <- [1..sample] ) append size(arbitrary(#list[int],1000));
	num avg = sum(a)/sample;
	return avg > 0.4 && avg < 2.0;
}

test bool expectedLengthOfSetIsOne(){
	int sample = 500;
	list[int] a; 
	a = for( int n <- [1..sample] ) append size(arbitrary(#set[int],1000));
	num avg = sum(a)/sample;
	return avg > 0.4 && avg < 2.0;
}

test bool setGeneratorShouldWorkWithSmallElementDomains(){
	set[bool] a = arbitrary(#set[bool],10);
	return a in power({true,false}); 
}

