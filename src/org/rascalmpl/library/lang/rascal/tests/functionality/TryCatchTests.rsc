 module lang::rascal::tests::functionality::TryCatchTests
  /*******************************************************************************
   * Copyright (c) 2009-2015 CWI
   * All rights reserved. This program and the accompanying materials
   * are made available under the terms of the Eclipse Public License v1.0
   * which accompanies this distribution, and is available at
   * http://www.eclipse.org/legal/epl-v10.html
   *
   * Contributors:
  
   *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
   *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
   *   * Paul Klint - Paul.Klint@cwi.nl - CWI
   *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
   *   * Bert Lisser - Bert.Lisser@cwi.nl - CWI
  *******************************************************************************/
import Exception;
import List;
import Set;
import Map;
import IO;
import util::Math;
  
data NODEA = fA(int N);
  
data NODEB = fB(int N) | dB(NODEB a, NODEB b);
  
data NODEC = fC(int N) | fin(value V) | dC(NODEC a) | dC(NODEC a, NODEC b);
  
data Exception = divide_by_zero();
  
int classify(value v){
	try { 
		throw v; 
	} catch int x:{ 
		return 1; 
	} 
	catch node x: { 
		return 2; 
 	}
	catch str s: { 
		return 3; 
	} 
	catch: { 
		return 4; 
	}
	return 0; 
}
  		
value duplicate(value v){
	try { 
		throw v; 
	} catch int x: {
		return x + x;
	} 
	catch NODEB x: { 
		return dB(x,x); 
	} 
	catch str s: { 
		return s + s; 
	} 
	catch: { 
		return v; 
	} 
}
  			
value dfin(value v){
	value res = 0;
	try { 
		throw v; 
	} catch int x: { 
		res = x + x; 
	} 
	catch NODEC x: { 
		res = dC(x,x); 
	} 
	catch str s: { 
 		res = s + s; 
	} 
	catch: { 
		res = v; 
	} 
	finally { 
		return fin(res); 
	} 
}
  		
int divide(int x, int y) throws divide_by_zero { 
	if(y == 0){ 
		throw divide_by_zero(); 
	} else { 
		return x / y; 
	} 
} 
  	
int safeDivide(int x, int y){ 
	try 
		return divide(x,y); 
	catch:  
 		return 0; 
}

// testClassify
  
test bool testClassify1() = classify(3) == 1;
test bool testClassify2() = classify(fA(3)) == 2;
test bool testClassify3() = classify("abc") == 3;
test bool testClassify4() = classify([1,2,3]) == 4;
  	
// testDuplicate
  	
test bool testDuplicate1() = duplicate(3) == 6;
test bool testDuplicate2() = duplicate(fB(3)) == dB(fB(3),fB(3));
test bool testDuplicate3() = duplicate("abc") == "abcabc";
test bool testDuplicate4() = duplicate(3.5) == 3.5;
  	
// testDFin
  
test bool testDFin1() = dfin(3) == fin(6);
test bool testDFin2() = dfin(fC(3)) == fin(dC(fC(3),fC(3)));
test bool testDFin3() = dfin("abc") == fin("abcabc");
test bool testDFin4() = dfin(3.5) == fin(3.5);
  	
// testDivide

test bool testDivide1() = divide(3, 2) == 1;
test bool testDivide2() = safeDivide(3, 2) == 1;
test bool testDivide3() = safeDivide(3, 0) == 0;
  
// emptyListException
  
test bool emptyListException1() {
	try { 
		head([]); 
  	} catch EmptyList(): 
  		return true; 
  	return false; 
}
  	
// emptyMapException
  
test bool emptyMapException1() { 
	try { 
		getOneFrom(()); 
	} catch EmptyMap(): 
  		return true; 
	return false; 
}
  	
// emptySetException
  
test bool emptySetException1() { 
	try { 
		getOneFrom({}); 
	} catch EmptySet(): 
		return true; 
	return false; 
}
  	
// indexOutOfBoundsException
  
test bool indexOutOfBoundsException1() {
	try { 
		[0,1,2][3]; 
	} catch IndexOutOfBounds(int i): 
		return true; 
	return false; 
}
  
// pathNotFoundException
  
test bool pathNotFoundException1() {
	try {
		S = readFile(|file:///DoesNotExist|);
	} catch PathNotFound(loc location):
		return true;
	return false;
}

  
  
  	
 
 