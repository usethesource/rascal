@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
@contributor{Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl - CWI}
@contributor{Vadim Zaytsev - vadim@grammarware.net - UvA}
module lang::rascal::tests::library::Map

import Map;
import Set;
import Relation;
import List;
import IO;
import Exception;
import Type;

// delete
test bool delete1(&K k) = isEmpty(delete((),k));
test bool delete2() = delete((1:10, 2:20), 0)  == (1:10, 2:20);
test bool delete3() = delete((1:10, 2:20), 10) == (1:10, 2:20);
test bool delete4() = delete((1:10, 2:20), 1)  == (2:20);
test bool delete5(map[&K, &V] M) {
  X = (M | delete(it,k) | &K k <- M);
  if (X != ()) {
    println(X);
    println(typeOf(X));
    println(M);
    return false;
  }
  else {
    return true;
  }
}
test bool delete6(map[&K, &V] M) = isEmpty(M) || size(M) == size(delete(M,getOneFrom(M))) +1;
test bool delete7(map[str, &V] M) = size(M) == size(delete(M,1));
test bool delete8(map[int, &V] M) = size(M) == size(delete(M,"1"));

// domain
test bool domain1() = domain(()) == {};
test bool domain2() = domain((1:10)) == {1};
test bool domain3() = domain((1:10, 2:20)) == {1,2};
test bool domain4(map[&K, &V] M) = domain(M) == M<0>;
test bool domain5(map[&K, &V] M) = domain(M) == {k | k <- M};
test bool domain6(map[&K k, &V v] M) = domain(M) == M.k;

// domainR
test bool domainR1(map[&K,&V] M) = isEmpty(domainR(M,{}));
test bool domainR2(set[&K] D) = isEmpty(domainR((),D));
test bool domainR3(map[&K,&V] M)
{
	if(isEmpty(M)) return true;
	S = {getOneFrom(M),getOneFrom(M)};
	return all(&K k <- domainR(M,S), k in S);
}

// domainX
test bool domainX1(map[&K,&V] M) = domainX(M,{}) == M;
test bool domainX2(set[&K] D) = isEmpty(domainX((),D));
test bool domainX3(map[&K,&V] M)
{
	if(isEmpty(M)) return true;
	S = {getOneFrom(M),getOneFrom(M)};
	XM = domainX(M,S);
	return isEmpty(XM) || all(&K k <- XM, k notin S);
}

// getOneFrom
@expected{EmptyMap} 
test bool getOneFrom1() { map[int,int] m = (); v = getOneFrom(m); return true; }
test bool getOneFrom2() = getOneFrom((1:10)) == 1;
test bool getOneFrom3() = getOneFrom((1:10, 2:20)) in {1,2};
test bool getOneFrom4(map[&K,&V] M) = isEmpty(M) || getOneFrom(M) in domain(M);

// invert
test bool invert1() = invert(()) == ();
test bool invert2() = invert((1:10)) == (10:{1});
test bool invert3() = invert((1:10, 2:20)) == (10:{1}, 20:{2});
test bool invert4() = invert((1:10, 2:10, 3:30, 4:30)) == (10: {1,2}, 30:{3,4});
test bool invert5(map[&K,&V] M) = range(M) == domain(invert(M));	
test bool invert6(map[&K,&V] M) = domain(M) == {*invert(M)[v] | v <- invert(M)};	

// invertUnique
test bool invertUnique1() = invertUnique(()) == ();
test bool invertUnique2() = invertUnique((1:10)) == (10:1);
test bool invertUnique3() = invertUnique((1:10, 2:20)) == (10:1, 20:2);
test bool invertUnique4() = invertUnique(([[]]:0,[[2]]:2,[[1,2],[2,1]]:1,[[1]]:3)) == (0:[[]],2:[[2]],1:[[1,2],[2,1]],3:[[1]]);
@expected{MultipleKey} test bool invertUnique5() { invertUnique((1:10, 2:10)); return true; }

test bool invertUnique6(map[&K,&V] M) {
	try	{ 
		map[&V,&K] RM = invertUnique(M);
		return range(M) == domain(RM);
	} 
	catch MultipleKey(_,_,_): 
	  return true;
}

test bool invertUnique7(map[&K,&V] M) {
	try	{
		map[&V,&K] RM = invertUnique(M);
		return range(RM) == domain(M);
	} 
	catch MultipleKey(_,_,_): 
	  return true;
}

test bool invertUnique8(set[int] D, set[int] R) {
	if (isEmpty(D) || isEmpty(R)) {
	  return true;
	}
	
	dList = toList(D);
	rList = toList(R);
	M = (dList[i] : rList[i] | i <- [0.. size(D) > size(R) ? Set::size(R) : Set::size(D) ]);
	
	return domain(M) == range(invertUnique(M)) && range(M) == domain(invertUnique(M));
}

// isEmpty
test bool isEmpty1() = isEmpty(());
test bool isEmpty2() = !isEmpty((1:10));

// mapper
private int inc(int n) = n + 1;
private int dec(int n) = n - 1;
test bool mapper1() = mapper((), inc, inc) == ();
test bool mapper2() = mapper((1:10,2:20), inc, inc) == (2:11,3:21);
test bool mapper3() = mapper((), inc, dec) == ();
test bool mapper4() = mapper((1:10,2:20), inc, dec) == (2:9,3:19);

// range
test bool range1() = range(()) == {};
test bool range2() = range((1:10)) == {10};
test bool range3() = range((1:10, 2:20)) == {10,20};
test bool range4(map[&K, &V] M) = range(M) == M<1>;
test bool range5(map[&K, &V] M) = range(M) == {M[k] | k <- M};
test bool range6(map[&K k, &V v] M) = range(M) == M.v;

// rangeR
test bool rangeR1(map[&K,&V] M) = isEmpty(rangeR(M,{}));
test bool rangeR2(set[&K] D) = isEmpty(rangeR((),D));
test bool rangeR3(map[&K,&V] M)
{
	if(isEmpty(M)) return true;
	S = {M[getOneFrom(M)],M[getOneFrom(M)]};
	return all(&K k <- rangeR(M,S), M[k] in S);
}

// rangeX
test bool rangeX1(map[&K,&V] M) = rangeX(M,{}) == M;
test bool rangeX2(set[&K] D) = isEmpty(rangeX((),D));
test bool rangeX3(map[&K,&V] M)
{
	if(isEmpty(M)) return true;
	S = {M[getOneFrom(M)],M[getOneFrom(M)]};
	XM = rangeX(M,S);
	return isEmpty(XM) || all(&K k <- XM, M[k] notin S);
}

// size
test bool size1() = size(()) == 0;
test bool size2() = size((1:10)) == 1;
test bool size3() = size((1:10,2:20)) == 2;
test bool size4(map[&K,&V] M) = size(M) == Set::size(domain(M));
test bool size5(map[&K,&V] M) = size(M) >= Set::size(range(M));

// toList
test bool toList1() = toList(()) == [];
test bool toList2() = toList((1:10)) == [<1,10>];
test bool toList3() = toList((1:10, 2:20)) in [ [<1,10>,<2,20>], [<2,20>,<1,10>] ];
test bool toList4(map[&K,&V] M) = size(M) == List::size(toList(M));
test bool toList5(map[&K,&V] M) = isEmpty(M) || all(k <- M, <k, M[k]> in toList(M));

// toRel (on plain maps)
@ignoreCompiler{FIX: Typechecker says: Multiple functions found which could be applied} 
test bool toRel_g1() = toRel(()) == {};
test bool toRel_g2() = toRel((1:10)) == {<1,10>};
test bool toRel_g3() = toRel((1:10, 2:20)) == {<1,10>,<2,20>};
// NB: basically could be &V, but not list[&V] or set[&V]
test bool toRel_g4(map[&K,int] M)  = isEmpty(M) || all(k <- M, <k, M[k]> in toRel(M));
test bool toRel_g5(map[&K,str] M)  = isEmpty(M) || all(k <- M, <k, M[k]> in toRel(M));
test bool toRel_g6(map[&K,bool] M) = isEmpty(M) || all(k <- M, <k, M[k]> in toRel(M));

// NB: basically could be &V, but not void; void screws up comprehensions 
test bool toRel_v1(map[&K,int] M)  = Relation::domain(toRel(M)) == domain(M);
test bool toRel_v2(map[&K,int] M)  = Relation::range(toRel(M))  == range(M);
test bool toRel_v3(map[&K,str] M)  = Relation::domain(toRel(M)) == domain(M);
test bool toRel_v4(map[&K,str] M)  = Relation::range(toRel(M))  == range(M);
test bool toRel_v5(map[&K,bool] M) = Relation::domain(toRel(M)) == domain(M);
test bool toRel_v6(map[&K,bool] M) = Relation::range(toRel(M))  == range(M);

// toRel (on maps to sets)
test bool toRel_s1() = toRel((1:{10})) == {<1,10>};
test bool toRel_s2() = toRel((1:{10}, 2:{20})) == {<1,10>,<2,20>};
test bool toRel_s3() = toRel((1:{10,20}, 2:{20})) == {<1,10>,<1,20>,<2,20>};
test bool toRel_s4(map[&K,set[int]] M) = any(v <- M<1>, Set::isEmpty(v)) || Relation::domain(toRel(M)) == domain(M);
test bool toRel_s5(map[&K,set[int]] M) = any(v <- M<1>, Set::isEmpty(v)) || Relation::range(toRel(M)) == {*R | R <- range(M)};
test bool toRel_s6(map[&K,set[str]] M) = any(v <- M<1>, Set::isEmpty(v)) || Relation::domain(toRel(M)) == domain(M);
test bool toRel_s7(map[&K,set[str]] M) = any(v <- M<1>, Set::isEmpty(v)) || Relation::range(toRel(M)) == {*R | R <- range(M)};

// toRel (on maps to lists)
test bool toRel_l1() = toRel((1:[10])) == {<1,10>};
test bool toRel_l2() = toRel((1:[10], 2:[20])) == {<1,10>,<2,20>};
test bool toRel_l3() = toRel((1:[10,20], 2:[20])) == {<1,10>,<1,20>,<2,20>};
test bool toRel_l4(map[&K,list[int]] M) = any(v <- M<1>, List::isEmpty(v)) || Relation::domain(toRel(M)) == domain(M);
test bool toRel_l5(map[&K,list[int]] M) = any(v <- M<1>, List::isEmpty(v)) || Relation::range(toRel(M)) == {*R | R <- range(M)};
test bool toRel_l6(map[&K,list[str]] M) = any(v <- M<1>, List::isEmpty(v)) || Relation::domain(toRel(M)) == domain(M);
test bool toRel_l7(map[&K,list[str]] M) = any(v <- M<1>, List::isEmpty(v)) || Relation::range(toRel(M)) == {*R | R <- range(M)};
test bool toRel_l8(map[&K,list[bool]] M) = any(v <- M<1>, List::isEmpty(v)) || Relation::domain(toRel(M)) == domain(M);
test bool toRel_l9(map[&K,list[bool]] M) = any(v <- M<1>, List::isEmpty(v)) || Relation::range(toRel(M)) == {*R | R <- range(M)};

// toString
test bool toString1() = toString(()) == "()";
test bool toString2() = toString((1:10)) == "(1:10)";
test bool toString3() = toString( (1:[], 2: []) ) == "(1:[],2:[])";

// itoString
test bool itoString1() = itoString(()) == "()";
test bool itoString2() = itoString((1:10)) == "(1:10)";
test bool itoString3() = itoString( (1:[], 2: []) ) == "(\n  1:[],\n  2:[]\n)";

// toString and itoString should produce the same for flat types
test bool toStrings1(map[int,int] M)   = toString(M) == itoString(M);
test bool toStrings2(map[bool,bool] M) = toString(M) == itoString(M);
test bool toStrings3(map[str,str] M)   = toString(M) == itoString(M);

// Tests related to the correctness of the dynamic types of maps produced by the library functions;
// incorrect dynamic types make pattern matching fail;
// testDynamicTypes
test bool testDynamicTypes1() { map[value a, value b] m = ("1":"1",2:2,3:3); return map[int, int] _ := m - ("1":"1") && (m - ("1":"1")).a == {2,3} && (m - ("1":"1")).b == {2,3}; }
test bool testDynamicTypes2() { map[value a, value b] m1 = ("1":"1",2:2,3:3); map[value a, value b] m2 = (2:2,3:3); return map[int, int] _ := m1 & m2 && (m1 & m2).a == {2,3} && (m2 & m1).b == {2,3}; }
