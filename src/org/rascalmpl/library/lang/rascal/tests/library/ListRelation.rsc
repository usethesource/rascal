@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Vadim Zaytsev - vadim@grammarware.net - UvA}
module lang::rascal::tests::library::ListRelation

import ListRelation;
import List; // needed for slices used for dynamic type checks

// carrier

test bool carrier1() = carrier([<1,10>,<2,20>]) == [1,10,2,20];
test bool carrier2() = carrier([<1,10,100>,<2,20,200>]) == [1,10,100,2,20,200];
test bool carrier3() = carrier([<1,10,100,1000>,<2,20,200,2000>]) == [1,10,100,1000,2,20,200,2000];
test bool carrier4() = carrier([<1,10,100,1000,10000>,<2,20,200,2000,20000>]) == [1,10,100,1000,10000,2,20,200,2000,20000];
// TODO: duplicates?
// The current implementation is: carrier([<1,1>,<1,1>]) == [1,1,1,1]

// carrierR (set)

test bool carrierRs01() = carrierR([<1,10>,<2,20>], {} ) == [];
test bool carrierRs02() = carrierR([<1,10>,<2,20>], {1,2} ) == [];
test bool carrierRs03() = carrierR([<1,10>,<2,20>], {2,20} ) == [<2,20>];
test bool carrierRs04() = carrierR([<1,10>,<2,20>], {1,2,10,20} ) == [<1,10>,<2,20>];

test bool carrierRs05() = carrierR([<1,10,100>,<2,20,200>], {} ) == [];
test bool carrierRs06() = carrierR([<1,10,100>,<2,20,200>], {1,2} ) == [];
test bool carrierRs07() = carrierR([<1,10,100>,<2,20,200>], {1,2,10,20} ) == [];
test bool carrierRs08() = carrierR([<1,10,100>,<2,20,200>], {2,20,200} ) == [<2,20,200>];
test bool carrierRs09() = carrierR([<1,10,100>,<2,20,200>], {1,2,10,20,100,200} ) == [<1,10,100>,<2,20,200>];

test bool carrierRs10() = carrierR([<1,10,100,1000>,<2,20,200,2000>], {} ) == [];
test bool carrierRs11() = carrierR([<1,10,100,1000>,<2,20,200,2000>], {1,2} ) == [];
test bool carrierRs12() = carrierR([<1,10,100,1000>,<2,20,200,2000>], {1,2,10,20} ) == [];
test bool carrierRs13() = carrierR([<1,10,100,1000>,<2,20,200,2000>], {1,2,10,20,100,200} ) == [];
test bool carrierRs14() = carrierR([<1,10,100,1000>,<2,20,200,2000>], {2,20,200,2000} ) == [<2,20,200,2000>];
test bool carrierRs15() = carrierR([<1,10,100,1000>,<2,20,200,2000>], {1,2,10,20,100,200,1000,2000} ) == [<1,10,100,1000>,<2,20,200,2000>];

test bool carrierRs16() = carrierR([<1,10,100,1000,10000>,<2,20,200,2000,20000>], {} ) == [];
test bool carrierRs17() = carrierR([<1,10,100,1000,10000>,<2,20,200,2000,20000>], {1,2} ) == [];
test bool carrierRs18() = carrierR([<1,10,100,1000,10000>,<2,20,200,2000,20000>], {1,2,10,20} ) == [];
test bool carrierRs19() = carrierR([<1,10,100,1000,10000>,<2,20,200,2000,20000>], {1,2,10,20,100,200} ) == [];
test bool carrierRs20() = carrierR([<1,10,100,1000,10000>,<2,20,200,2000,20000>], {1,2,10,20,100,200,1000,2000} ) == [];
test bool carrierRs21() = carrierR([<1,10,100,1000,10000>,<2,20,200,2000,20000>], {2,20,200,2000,20000} ) == [<2,20,200,2000,20000>];
test bool carrierRs22() = carrierR([<1,10,100,1000,10000>,<2,20,200,2000,20000>], {1,2,10,20,100,200,1000,2000,10000,20000} ) == [<1,10,100,1000,10000>,<2,20,200,2000,20000>];

// carrierX (set)

test bool carrierXs01() = carrierX([<1,10>,<2,20>], {} ) == [<1,10>,<2,20>];
test bool carrierXs02() = carrierX([<1,10>,<2,20>], {3,30} ) == [<1,10>,<2,20>];
test bool carrierXs03() = carrierX([<1,10>,<2,20>], {2} ) == [<1,10>];
test bool carrierXs04() = carrierX([<1,10>,<2,20>], {10} ) == [<2,20>];
test bool carrierXs05() = carrierX([<1,10>,<2,20>], {1,2} ) == [];

test bool carrierXs06() = carrierX([<1,10,100>,<2,20,200>], {} ) == [<1,10,100>,<2,20,200>];
test bool carrierXs07() = carrierX([<1,10,100>,<2,20,200>], {3,30,300} ) == [<1,10,100>,<2,20,200>];
test bool carrierXs08() = carrierX([<1,10,100>,<2,20,200>], {2} ) == [<1,10,100>];
test bool carrierXs09() = carrierX([<1,10,100>,<2,20,200>], {10} ) == [<2,20,200>];
test bool carrierXs10() = carrierX([<1,10,100>,<2,20,200>], {200} ) == [<1,10,100>];
test bool carrierXs11() = carrierX([<1,10,100>,<2,20,200>], {1,2} ) == [];

test bool carrierXs12() = carrierX([<1,10,100,1000>,<2,20,200,2000>], {} ) == [<1,10,100,1000>,<2,20,200,2000>];
test bool carrierXs13() = carrierX([<1,10,100,1000>,<2,20,200,2000>], {3,30,300,3000} ) == [<1,10,100,1000>,<2,20,200,2000>];
test bool carrierXs14() = carrierX([<1,10,100,1000>,<2,20,200,2000>], {2} ) == [<1,10,100,1000>];
test bool carrierXs15() = carrierX([<1,10,100,1000>,<2,20,200,2000>], {10} ) == [<2,20,200,2000>];
test bool carrierXs16() = carrierX([<1,10,100,1000>,<2,20,200,2000>], {200} ) == [<1,10,100,1000>];
test bool carrierXs17() = carrierX([<1,10,100,1000>,<2,20,200,2000>], {1000} ) == [<2,20,200,2000>];
test bool carrierXs18() = carrierX([<1,10,100,1000>,<2,20,200,2000>], {1,2} ) == [];

test bool carrierXs19() = carrierX([<1,10,100,1000,10000>,<2,20,200,2000,20000>], {} ) == [<1,10,100,1000,10000>,<2,20,200,2000,20000>];
test bool carrierXs20() = carrierX([<1,10,100,1000,10000>,<2,20,200,2000,20000>], {3,30,300,3000,30000} ) == [<1,10,100,1000,10000>,<2,20,200,2000,20000>];
test bool carrierXs21() = carrierX([<1,10,100,1000,10000>,<2,20,200,2000,20000>], {2} ) == [<1,10,100,1000,10000>];
test bool carrierXs22() = carrierX([<1,10,100,1000,10000>,<2,20,200,2000,20000>], {10} ) == [<2,20,200,2000,20000>];
test bool carrierXs23() = carrierX([<1,10,100,1000,10000>,<2,20,200,2000,20000>], {200} ) == [<1,10,100,1000,10000>];
test bool carrierXs24() = carrierX([<1,10,100,1000,10000>,<2,20,200,2000,20000>], {1000} ) == [<2,20,200,2000,20000>];
test bool carrierXs25() = carrierX([<1,10,100,1000,10000>,<2,20,200,2000,20000>], {20000} ) == [<1,10,100,1000,10000>];
test bool carrierXs26() = carrierX([<1,10,100,1000,10000>,<2,20,200,2000,20000>], {1,2} ) == [];

// complement
  
test bool complement1() = complement([<1,10>,<2,20>]) == [<1,20>,<2,10>];
test bool complement2() = complement([<1,1>,<2,2>]) == [<1,2>,<2,1>];
test bool complement3() = complement([<1,10,100>,<2,20,100>]) == [<1,20,100>,<2,10,100>];
test bool complement4() = complement([<1,10,100>,<2,20,200>]) == [<1,10,200>,<1,20,100>,<1,20,200>,<2,10,100>,<2,10,200>,<2,20,100>];
test bool complement5() = complement([<1,10,100,1000>,<2,20,200,2000>]) ==
[<1,10,100,2000>,<1,10,200,1000>,<1,10,200,2000>,<1,20,100,1000>,<1,20,100,2000>,<1,20,200,1000>,
 <1,20,200,2000>,<2,10,100,1000>,<2,10,100,2000>,<2,10,200,1000>,<2,10,200,2000>,<2,20,100,1000>,
 <2,20,100,2000>,<2,20,200,1000>];
test bool complement6() = complement([<1,2,2,2,2>,<2,2,2,2,1>]) == [<1,2,2,2,1>, <2,2,2,2,2>];

// domain 
  
test bool domain1() = domain([<1,10>,<2,20>]) == [1,2];
test bool domain2() = domain([<1,10>,<1,20>]) == [1];
test bool domain3() = domain([<1,10,100>,<2,20,200>]) == [1,2];
test bool domain4() = domain([<1,10,100>,<1,20,200>]) == [1];
test bool domain5() = domain([<1,10,100,1000>,<2,20,200,2000>]) == [1,2];
test bool domain6() = domain([<1,10,100,1000>,<1,20,200,2000>]) == [1];
test bool domain7() = domain([<1,10,100,1000,10000>,<2,20,200,2000,20000>]) == [1,2];
test bool domain8() = domain([<1,10,100,1000,10000>,<1,20,200,2000,20000>]) == [1];

// domainR (list)
  
test bool domainRl01() = domainR([<1,10>,<2,20>], []) == [];
test bool domainRl02() = domainR([<1,10>,<2,20>], [2]) == [<2,20>];
test bool domainRl03() = domainR([<2,10>,<2,20>], [2]) == [<2,10>,<2,20>];
test bool domainRl04() = domainR([<1,10>,<2,20>], [1,2]) == [<1,10>,<2,20>];
test bool domainRl05() = domainR([<1,10>,<2,20>], [2,1]) == [<2,20>,<1,10>];
test bool domainRl06() = domainR([<1,10,100>,<2,20,200>], []) == [];
test bool domainRl07() = domainR([<1,10,100>,<2,20,200>], [2,5]) == [<2,20,200>];
test bool domainRl08() = domainR([<1,10,100>,<1,20,200>], [0,1]) == [<1,10,100>,<1,20,200>];
test bool domainRl09() = domainR([<1,10,100>,<2,20,200>], [2,1,10]) == [<2,20,200>,<1,10,100>];
test bool domainRl10() = domainR([<1,10,100>,<2,20,200>], [1,2,5]) == [<1,10,100>,<2,20,200>];
test bool domainRl11() = domainR([<1,10,100,1000>,<2,20,200,2000>], []) == [];
test bool domainRl12() = domainR([<1,10,100,1000>,<2,20,200,2000>], [2,5]) == [<2,20,200,2000>];
test bool domainRl13() = domainR([<-1,10,100,1000>,<-1,20,200,2000>], [0,-1]) == [<-1,10,100,1000>,<-1,20,200,2000>];
test bool domainRl14() = domainR([<1,10,100,1000>,<2,20,200,2000>], [2,3,1]) == [<2,20,200,2000>,<1,10,100,1000>];
test bool domainRl15() = domainR([<1,10,100,1000>,<2,20,200,2000>], [1,7,2]) == [<1,10,100,1000>,<2,20,200,2000>];
test bool domainRl16() = domainR([<1,10,100,1000,10000>,<2,20,200,2000,20000>], []) == [];
test bool domainRl17() = domainR([<1,10,100,1000,10000>,<2,20,200,2000,20000>], [2,5]) == [<2,20,200,2000,20000>];
test bool domainRl18() = domainR([<10,10,100,1000,10000>,<10,20,200,2000,20000>], [5,10,15]) == [<10,10,100,1000,10000>,<10,20,200,2000,20000>];
test bool domainRl19() = domainR([<1,10,100,1000,10000>,<2,20,200,2000,20000>], [2,5,1]) == [<2,20,200,2000,20000>,<1,10,100,1000,10000>];
test bool domainRl20() = domainR([<1,10,100,1000,10000>,<2,20,200,2000,20000>], [1,2,100,200]) == [<1,10,100,1000,10000>,<2,20,200,2000,20000>];

// domainX 
  
test bool domainX1() = domainX([<1,10>,<2,20>], {}) == [<1,10>,<2,20>];
test bool domainX2() = domainX([<1,10>,<2,20>], {2}) == [<1,10>];
test bool domainX3() = domainX([<1,10,100>,<2,20,200>], {2,5}) == [<1,10,100>];
test bool domainX4() = domainX([<1,10,100,1000>,<2,20,200,2000>], {1,3}) == [<2,20,200,2000>];
test bool domainX5() = domainX([<1,10,100,1000,10000>,<2,20,200,2000,20000>],{2,5}) == [<1,10,100,1000,10000>];

// groupDomainByRange

test bool groupDomainByRange1() = groupDomainByRange([<1,1>]) == [[1]];
test bool groupDomainByRange2() = groupDomainByRange([<1,2>,<1,1>]) == [[1]];
test bool groupDomainByRange3() = groupDomainByRange([<1,1>,<2,2>]) == [[1],[2]];
test bool groupDomainByRange4() = groupDomainByRange([<1,2>,<2,2>]) == [[1,2]];
test bool groupDomainByRange5() = groupDomainByRange([<2,2>,<1,2>]) == [[2,1]];
test bool groupDomainByRange6() = groupDomainByRange([<1,3>,<2,1>,<2,3>]) == [[1,2],[2]];

// groupRangeByDomain

test bool groupRangeByDomain1() = groupRangeByDomain([<1,1>]) == [[1]];
test bool groupRangeByDomain2() = groupRangeByDomain([<1,2>,<1,1>]) == [[2,1]];
test bool groupRangeByDomain3() = groupRangeByDomain([<1,1>,<2,2>]) == [[1],[2]];
test bool groupRangeByDomain4() = groupRangeByDomain([<2,1>,<2,2>]) == [[1,2]];
test bool groupRangeByDomain5() = groupRangeByDomain([<2,2>,<2,1>]) == [[2,1]];
test bool groupRangeByDomain6() = groupRangeByDomain([<2,1>,<1,3>,<2,3>]) == [[1,3],[3]];

// ident

test bool ident1() = ident([]) == [];
test bool ident2() = ident([1]) == [<1,1>];
test bool ident3() = ident([1,2]) == [<1,1>,<2,2>];
test bool ident4() = ident([1,2,3]) == [<1,1>,<2,2>,<3,3>];

// invert

test bool invert1() = invert([<1,10>,<2,20>]) == [<10,1>,<20,2>];
test bool invert2() = invert([<1,10,100>,<2,20,200>]) == [<100,10,1>,<200,20,2>];
test bool invert3() = invert([<1,10,100,1000>,<2,20,200,2000>]) == [<1000,100,10,1>,<2000,200,20,2>];
test bool invert4() = invert([<1,10,100,1000,10000>,<2,20,200,2000,20000>]) == [<10000,1000,100,10,1>,<20000,2000,200,20,2>];

// range

test bool range1() = range([<1,10>,<2,20>]) == [10,20];
test bool range2() = range([<1,10,100>,<2,20,200>]) == [<10,100>,<20,200>];
test bool range3() = range([<1,10,100,1000>,<2,20,200,2000>]) == [<10,100,1000>,<20,200,2000>];
test bool range4() = range([<1,10,100,1000,10000>,<2,20,200,2000,20000>]) == [<10,100,1000,10000>,<20,200,2000,20000>];

// rangeR

test bool rangeRs1() = rangeR([<1,10>,<2,20>], {}) == [];
test bool rangeRs2() = rangeR([<1,10>,<2,20>], {20}) == [<2,20>];
test bool rangeRl1() = rangeR([<1,10>,<2,20>], []) == [];
test bool rangeRl2() = rangeR([<1,10>,<2,20>], [20]) == [<2,20>];
test bool rangeRl3() = rangeR([<1,10>,<2,20>], [10,20]) == [<1,10>,<2,20>];
test bool rangeRl4() = rangeR([<1,10>,<2,20>], [20,10]) == [<2,20>,<1,10>];

// rangeX

test bool rangeXs1() = rangeX([<1,10>,<2,20>], {}) == [<1,10>,<2,20>];
test bool rangeXs2() = rangeX([<1,10>,<2,20>], {20}) == [<1,10>];
test bool rangeXs3() = rangeX([<1,10>,<2,20>], {10,20}) == [];
test bool rangeXl4() = rangeX([<1,10>,<2,20>], []) == [<1,10>,<2,20>];
test bool rangeXl5() = rangeX([<1,10>,<2,20>], [20]) == [<1,10>];
test bool rangeXl6() = rangeX([<1,10>,<2,20>], [10,20]) == [];
test bool rangeXl7() = rangeX([<1,10>,<2,20>], [20,10]) == [];

// toMap

test bool toMap1() = toMap([]) == ();
test bool toMap2() = toMap([<1,1>]) == (1:[1]);
test bool toMap3() = toMap([<1,1>,<1,2>]) == (1:[1,2]);
test bool toMap4() = toMap([<1,1>,<2,2>]) == (1:[1],2:[2]);
test bool toMap5() = toMap([<2,1>,<2,2>]) == (2:[1,2]);
test bool toMap6() = toMap([<2,2>,<2,1>]) == (2:[2,1]);

// Tests related to the correctness of the dynamic types of list relations produced by the library functions;
// incorrect dynamic types make pattern matching fail;


test bool dynamicTypes1(){ lrel[value, value] lr = [<"1","1">,<2,2>,<3,3>]; return lrel[int, int] _ := slice(lr, 1, 2);}

test bool dynamicTypes2(){ lrel[value, value] lr = [<"1","1">,<2,2>,<3,3>];return lrel[int, int] _ := lr - <"1","1">; }

test bool dynamicTypes3(){ lrel[value a, value b] lr = [<"1","1">,<2,2>,<3,3>]; return lrel[int, int] _ := lr - [<"1","1">] && (lr - [<"1","1">]).a == [2,3] && (lr - [<"1","1">]).b == [2,3]; }

test bool dynamicTypes4(){ lrel[value, value] lr = [<"1","1">,<2,2>,<3,3>]; return lrel[int, int] _ := delete(lr, 0); }

test bool dynamicTypes5(){ lrel[value, value] lr = [<"1","1">,<2,2>,<3,3>]; return lrel[int, int] _ := drop(1, lr); }

test bool dynamicTypes6(){ lrel[value, value] lr = [<1,1>,<2,2>,<"3","3">]; return lrel[int, int] _ := head(lr, 2); }

test bool dynamicTypes7(){ lrel[value, value] lr = [<1,1>,<2,2>,<"3","3">]; return lrel[int, int] _ := prefix(lr); }

test bool dynamicTypes8(){ lrel[value, value] lr = [<"1","1">,<2,2>,<3,3>]; return lrel[int, int] _ := tail(lr); }

test bool dynamicTypes9(){ lrel[value, value] lr = [<1,1>,<2,2>,<"3","3">]; return lrel[int, int] _ := take(2, lr); }	

test bool dynamicTypes10(){ return [tuple[str,str] _, *tuple[int,int] _] := [<"1","1">,<2,2>,<3,3>]; }

test bool dynamicTypes11(){ 
    lrel[value a, value b] lr1 = [<"1","1">,<2,2>,<3,3>]; lrel[value a, value b] lr2 = [<2,2>,<3,3>]; 
    return lrel[int, int] _ := lr1 & lr2 && (lr1 & lr2).a == [2,3] && (lr2 & lr1).b == [2,3]; 
}
       
test bool dynamicTypes12(){ 
    lrel[value, value] lr = [<"1","1">,<2,2>,<3,3>]; 
    return lrel[int, int] _ := delete(lr, 0); 
}
