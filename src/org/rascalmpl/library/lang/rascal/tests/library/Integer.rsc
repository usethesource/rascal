@license{
   Copyright (c) 2009-2015 CWI
   All rights reserved. This program and the accompanying materials
   are made available under the terms of the Eclipse Public License v1.0
   which accompanies this distribution, and is available at
   http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl - CWI}
module lang::rascal::tests::library::Integer
  
import util::Math;
  
// abs
  
test bool abs1() = abs(0) == 0;
test bool abs2() = abs(-1) == 1;
test bool abs3() = abs(1) == 1;
  
// arbInt
  
test bool arbInt1() {
	int N = arbInt(10); 
  	return (N >= 0) && (N < 10);
}

test bool arbInt2() {
	arbInt(); 
	return true;
}
  
// max
  
test bool max1() = max(3, 10) == 10;
test bool max2() = max(10, 10) == 10;
  
// min
  
test bool min1() = min(3, 10) == 3;
test bool min2() = min(10, 10) == 10;
  
// toReal
  
test bool toReal1() =  toReal(3) == 3.0;
  
// testToString
  
test bool testToString1() = toString(314) == "314";
 
