 module lang::rascal::tests::library::Relation
  /*******************************************************************************
     * Copyright (c) 2009-2015 CWI
     * All rights reserved. This program and the accompanying materials
     * are made available under the terms of the Eclipse Public License v1.0
     * which accompanies this distribution, and is available at
     * http://www.eclipse.org/legal/epl-v10.html
     *
     * Contributors:
    
     *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
     *   * Paul Klint - Paul.Klint@cwi.nl - CWI
     *   * Bert Lisser - Bert.Lisser@cwi.nl - CWI
    */
import Relation; 
    
// carrier
   
test bool carrier1() = carrier({<1,10>,<2,20>}) == {1,2,10,20};
test bool carrier2() = carrier({<1,10,100>,<2,20,200>}) == {1,2,10,20,100,200};
test bool carrier3() = carrier({<1,10,100,1000>,<2,20,200,2000>}) == {1,2,10,20,100,200,1000,2000};
test bool carrier4() = carrier({<1,10,100,1000,10000>,<2,20,200,2000,20000>}) == {1,2,10,20,100,200,1000,2000,10000,20000};
  
// carrierR
  
test bool carrierR1() = carrierR({<1,10>,<2,20>}, {} ) == {};
test bool carrierR2() = carrierR({<1,10>,<2,20>}, {2,3} ) == {};
test bool carrierR3() = carrierR({<1,10>,<2,20>}, {2,20} ) == {<2,20>};
test bool carrierR4() = carrierR({<1,10,100>,<2,20,200>}, {2, 20,200}) == {<2,20,200>};
test bool carrierR5() = carrierR({<1,10,100>,<2,20,200>}, {1,2,10,20,100,200}) == {<1,10,100>,<2,20,200>};
test bool carrierR6() = carrierR({<1,10,100,1000>,<2,20,200,2000>}, {1,10,100,1000}) == {<1,10,100,1000>};
test bool carrierR7() = carrierR({<1,10,100,1000>,<2,20,200,2000>}, {2,20,200,2000}) == {<2,20,200,2000>};
  
// carrierX
  
test bool carrierX1() = carrierX({<1,10>,<2,20>}, {} ) == {<1,10>,<2,20>};
test bool carrierX2() = carrierX({<1,10>,<2,20>}, {2,3} ) == {<1,10>};
test bool carrierX3() = carrierX({<1,10,100>,<2,20,200>}, {20}) == {<1,10,100>};
test bool carrierX4() = carrierX({<1,10,100>,<2,20,200>}, {20,100}) == {};
test bool carrierX5() = carrierX({<1,10,100,1000>,<2,20,200,2000>}, {1000}) == {<2,20,200,2000>};
test bool carrierX6() = carrierX({<1,10,100,1000>,<2,20,200,2000>}, {2}) == {<1,10,100,1000>};
  
// complement
  
test bool complement1() = complement({<1,10>,<2,20>}) == {<2,10>,<1,20>};
test bool complement2() = complement({<1,10,100>,<2,20,200>}) == {<2,20,100>,<2,10,200>,<2,10,100>,<1,20,200>,<1,20,100>,<1,10,200>};
test bool complement3() = complement({<1,10,100,1000>,<2,20,200,2000>}) == {<2,20,200,1000>,<1,10,100,2000>,<1,10,200,1000>,<1,10,200,2000>,<1,20,100,1000>,<1,20,100,2000>,<1,20,200,1000>,<1,20,200,2000>,<2,10,100,1000>,<2,10,100,2000>,<2,10,200,1000>,<2,10,200,2000>,<2,20,100,1000>,<2,20,100,2000>};
  
// domain 
  
test bool domain1() = domain({<1,10>,<2,20>}) == {1,2};
test bool domain2() = domain({<1,10,100>,<2,20,200>}) == {1,2};
test bool domain3() = domain({<1,10,100,1000>,<2,20,200,2000>}) == {1,2};
test bool domain4() = domain({<1,10,100,1000,10000>,<2,20,200,2000,20000>}) == {1,2};
  
// domainR
  
test bool domainR1() = domainR({<1,10>,<2,20>}, {}) == {};
test bool domainR2() = domainR({<1,10>,<2,20>}, {2}) == {<2,20>};
test bool domainR3() = domainR({<1,10,100>,<2,20,200>}, {2,5}) == {<2,20,200>};
test bool domainR4() = domainR({<1,10,100,1000>,<2,20,200,2000>}, {1,3}) == {<1,10,100,1000>};
test bool domainR5() = domainR({<1,10,100,1000,10000>,<2,20,200,2000,20000>},{2,5}) == {<2,20,200,2000,20000>};
  
// domainX 
  
test bool domainX1() = domainX({<1,10>,<2,20>}, {}) == {<1,10>,<2,20>};
test bool domainX2() = domainX({<1,10>,<2,20>}, {2}) == {<1,10>};
test bool domainX3() = domainX({<1,10,100>,<2,20,200>}, {2,5}) == {<1,10,100>};
test bool domainX4() = domainX({<1,10,100,1000>,<2,20,200,2000>}, {1,3}) == {<2,20,200,2000>};
test bool domainX5() = domainX({<1,10,100,1000,10000>,<2,20,200,2000,20000>},{2,5}) == {<1,10,100,1000,10000>};
  	
// ident
  
test bool ident1() = ident({}) == {};
test bool ident2() = ident({1}) == {<1,1>};
test bool ident3() = ident({1,2,3}) == {<1,1>,<2,2>,<3,3>};
  
// invert
  
test bool invert1() = invert({<1,10>,<2,20>}) == {<10,1>,<20,2>};
test bool invert2() = invert({<1,10,100>,<2,20,200>}) == {<100,10,1>,<200,20,2>};
test bool invert3() = invert({<1,10,100,1000>,<2,20,200,2000>}) == {<1000,100,10,1>,<2000,200,20,2>};
test bool invert4() = invert({<1,10,100,1000,10000>,<2,20,200,2000,20000>}) == {<10000,1000,100,10,1>,<20000,2000,200,20,2>};
  
// range
  
test bool range1() = range({<1,10>,<2,20>}) == {10,20};
test bool range2() = range({<1,10,100>,<2,20,200>}) == {<10,100>,<20,200>};
test bool range3() = range({<1,10,100,1000>,<2,20,200,2000>}) == {<10,100,1000>,<20,200,2000>};
test bool range4() = range({<1,10,100,1000,10000>,<2,20,200,2000,20000>}) == {<10,100,1000,10000>,<20,200,2000,20000>};
 
  
// rangeR
  
test bool rangeR1() = rangeR({<1,10>,<2,20>}, {}) == {};
test bool rangeR2() = rangeR({<1,10>,<2,20>}, {20}) == {<2,20>};
  
// rangeX
  
test bool rangeX() = rangeX({<1,10>,<2,20>}, {}) == {<1,10>,<2,20>};
test bool rangeX1() = rangeX({<1,10>,<2,20>}, {20}) == {<1,10>};
  
// Tests related to the correctness of the dynamic types of relations produced by the library functions;
// incorrect dynamic types make pattern matching fail;
  
// testDynamicTypes
  
test bool testDynamicTypes1(){ rel[value, value] sr = {<"1","1">,<2,2>,<3,3>}; return rel[int, int] _ := sr - <"1","1">; }
test bool testDynamicTypes2(){ rel[value a, value b] sr = {<"1","1">,<2,2>,<3,3>}; return rel[int, int] _ := sr - {<"1","1">} && (sr - {<"1","1">}).a == {2,3} && (sr - {<"1","1">}).b == {2,3}; }
test bool testDynamicTypes3(){ return {<"1","1">, *tuple[int,int] _} := {<"1","1">,<2,2>,<3,3>}; }
  		
test bool testDynamicTypes4(){ rel[value a, value b] sr1 = {<"1","1">,<2,2>,<3,3>}; rel[value a, value b] sr2 = {<2,2>,<3,3>}; return rel[int, int] _ := sr1 & sr2 && (sr1 & sr2).a == {2,3} && (sr2 & sr1).b == {2,3}; }
  
  
    
 
