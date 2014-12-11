@license{
  Copyright (c) 2009-2014 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Vadim Zaytsev - vadim@grammarware.net - UvA}
module lang::rascal::tests::basic::Maps

import Map;
import Set;
import List;
import ListRelation;
import util::Math;

// composition
test bool composition1(map[&K,&V] kvs) = kvs o () == ();
test bool composition2(map[&K,&V] kvs) = () o kvs == ();
test bool composition3(map[&K,&V] kvs) = kvs o (v:v | &K k <- kvs, &V v := kvs[k]) == kvs;
test bool composition4(map[&K,&V] kvs) = (k:k | &K k <- kvs) o kvs == kvs;

// comprehension
test bool comprehension1() = (k:k*k | k <- []) == ();
test bool comprehension2() = (k:k*k | k <- [1..5]) == (1:1,2:4,3:9,4:16);
test bool comprehension3(set[&K] xs) = size((k:k | &K k <- xs)) == size(xs);

// difference
test bool difference1(map[&K,&V] kvs) = kvs - () == kvs;
test bool difference2(map[&K,&V] kvs) = () - kvs == ();
test bool difference3() = (1:10) - (2:20) == (1:10);
test bool difference4() = (1:10) - (1:10) == ();
test bool difference5() = (1:10) - (1:20) == ();

// equal
test bool equal1() = () == ();
test bool equal2(map[&K,&V] kvs) = kvs == kvs;
test bool equal3(map[&K,&V] kvs1, map[&K,&V] kvs2)
	= (kvs1 == kvs2) ==>
	( domain(kvs1) == domain(kvs2)
	&& range(kvs1) == range(kvs2)
	&& (isEmpty(kvs1) || all(x <- kvs1, kvs1[x] == kvs2[x])));
test bool equal4()
{
	map[int,int] iie = ();
	map[str,str] sse = ();
	return iie == sse;
}
test bool equal5() = (2:20,1:10) == (1:10,2:20);
test bool equal6()
{
	map[int,int] iit = (1:10);
	map[num,value] nvt = (1:10);
	return iit == nvt;
}

// in
test bool in1(map[&K,&V] kvs) = isEmpty(kvs) || all(&K k <- kvs, k in kvs);
test bool in2(&K k) = k in (k:k); 

// intersection
test bool intersection1() = (1:10, 2:20) & (3:30) == ();
test bool intersection2() = (1:10, 2:20) & (2:30) == ();
test bool intersection3() = (1:10, 2:20) & (1:20) == ();
test bool intersection4() = (1:10, 2:20) & (1:10) == (1:10);
test bool intersection5() = (1:10, 2:20) & (2:20) == (2:20);
test bool intersection6(map[&K,&V] kvs) = kvs & () == ();
test bool intersection7(map[&K,&V] kvs) = () & kvs == ();

// notequal
test bool notequal1() = !(() != ());
test bool notequal2(map[&K,&V] kvs) = !(kvs != kvs);
test bool notequal3(map[&K,&V] kvs1, map[&K,&V] kvs2)
	= (kvs1 != kvs2) ==>
	(domain(kvs1) != domain(kvs2)
	|| range(kvs1) != range(kvs2)
	|| isEmpty(kvs1) 
	|| any(x <- kvs1, kvs1[x] != kvs2[x]));
test bool notequal4()
{
	map[int,int] iie = ();
	map[str,str] sse = ();
	return !(iie != sse);
}
test bool notequal5() = !((2:20,1:10) != (1:10,2:20));
test bool notequal6()
{
	map[int,int] iit = (1:10);
	map[num,value] nvt = (1:10);
	return !(iit != nvt);
}
test bool notequal7() = (1:10) != (1.0:10);
test bool notequal8(map[&K,&V] kvs) = isEmpty(kvs) || kvs != ();

// notin
test bool notin1(&K k) = k notin ();
test bool notin2(&K k, map[&K,&V] kvs) = k notin (kvs - (k:k));
 
// strictsubmap
test bool strictsubmap1(map[&K,&V] kvs) = isEmpty(kvs) || () < kvs;
test bool strictsubmap2(map[&K,&V] kvs) = isEmpty(kvs) || delete(kvs,getOneFrom(kvs)) < kvs;

// strictsupermap
test bool strictsupermap1(map[&K,&V] kvs) = isEmpty(kvs) || kvs > ();
test bool strictsupermap2(map[&K,&V] kvs) = isEmpty(kvs) || kvs > delete(kvs,getOneFrom(kvs));

// submap
test bool submap1(map[&K,&V] kvs) = () <= kvs;
test bool submap2(map[&K,&V] kvs) = kvs <= kvs;
test bool submap3(map[&K,&V] kvs) = isEmpty(kvs) || delete(kvs,getOneFrom(kvs)) <= kvs;
test bool submap1(map[&K,&V] kvs1, map[&K,&V] kvs2) = kvs1 < kvs2 ==> kvs1 <= kvs2;

// subscription
@expected{NoSuchKey} test bool subscription1(&K k) {map[&K,bool] kvs = (); return kvs[k];}
@expected{NoSuchKey} test bool subscription2() = (1:false)[2];
test bool subscription3() = (1:10)[1] == 10;
test bool subscription4() = (1:10,2:20)[1] == 10;

// supermap
test bool supermap1(map[&K,&V] kvs) = kvs >= ();
test bool supermap2(map[&K,&V] kvs) = kvs >= kvs;
test bool supermap3(map[&K,&V] kvs) = isEmpty(kvs) || kvs >= delete(kvs,getOneFrom(kvs));
test bool supermap4(map[&K,&V] kvs1, map[&K,&V] kvs2) = kvs1 > kvs2 ==> kvs1 >= kvs2;

// union
test bool union1(map[&K,&V] kvs1, map[&K,&V] kvs2) = size(kvs1 + kvs2) >= size(kvs1);
test bool union2(map[&K,&V] kvs) = () + kvs == kvs;
test bool union3(map[&K,&V] kvs) = kvs + () == kvs;
test bool union4() = (1:10) + (2:20) == (1:10,2:20);

////////////////////////////////////////////////////////////////////////////////
// legacy

// is A + B == C?
bool isUnion(map[&K, &V] A, map[&K, &V] B, map[&K, &V] C) =
     isEmpty(A) ==> C == B ||
     isEmpty(B) ==> C == A ||
     ( domain(A) + domain(B) == domain(C) &&
       range(C) <= range(A) + range(B) &&
       all(x <- C,  x in domain(B) && C[x] == B[x] || x in domain(A) && C[x] == A[x]) &&
       all(x <- C,  (x in domain(B) && x in domain(A)) ==> C[x] == B[x])
     );
    
test bool union(map[&K, &V] A, map[&K, &V] B) = isUnion(A,   B,  A + B);

// is A - B == C?
bool isDiff(map[&K, &V] A, map[&K, &V] B, map[&K, &V] C) =
     isEmpty(A) ==> C == B ||
     isEmpty(B) ==> C == A ||
     all(x <- C, x in domain(A) && x notin domain(B));
     
test bool diff(map[&K, &V] A, map[&K, &V] B) = isDiff(A, B, A - B);
 
test bool intersection(map[&K, &V] A, map[&K, &V] B) = isEmpty(A & B) || all(x <- A & B, x in A, x in B, A[x] == B[x]);


test bool lesseq(map[&K, &V] A, map[&K, &V] B)  = A <= (B + A); // right overwrites left

test bool less(map[&K, &V] A, map[&K, &V] B) = (A != B + A) ==> A < (B + A);

test bool greatereq(map[&K, &V] A, map[&K, &V] B)  = (B + A) >= A;
test bool greater(map[int, str] A, map[int, str] B)  = B <= A || (B + A) > A;

test bool tst_in(&K key, &V val, map[&K, &V] M) = key in M || (key in (M + (key : val)) && val == (M + (key : val))[key] &&
														              key in ((key : val) + M) && val == ((key : val) + M)[key]);

test bool intKeyHandling(){
    N = 10000;
    m = (i : i | int i <- [0..N]);
    return size(m) == N && size(domain(m)) == N && size(range(m)) == N;
}

// Library functions

test bool tst_domain(map[str, int] X) = 
   isEmpty(X) || 
   {k | k <- X} == domain(X);

private set[int] sample(map[int, int] X) {
   c = domain(X) + range(X);
   if(size(c) <= 2)
   	  return {};
   <r1, c> = takeOneFrom(c);
   <r2, c> = takeOneFrom(c);
  return {r1, r2};
}

test bool tst_domainR(map[int, int] X) {
   s = sample(X);
   XR = domainR(X, s);
   return isEmpty(XR) || all(k <- XR, k in s);
}

test bool tst_domainX(map[int, int] X) {
   s = sample(X);
   XR = domainX(X, s);
   return isEmpty(XR) || all(k <- XR, k notin s);
}

test bool tst_invert(map[int, int] X) = isEmpty(X) || domain(invert(X)) == range(X) && domain(X) == {*invert(X)[k] | k <- invert(X)};

test bool tst_invertUnique(set[int] D, set[int] R) {
 if(isEmpty(D) || isEmpty(R)) return true;
 dList = toList(D);
 rList = toList(R);
 S = (dList[i] : rList[i] | i <- [0 .. min(size(D) -1 , size(R) -1) + 1]);
 return domain(S) == range(invertUnique(S)) && range(S) == domain(invertUnique(S));
}

test bool tst_range(map[str, int] X) = 
   isEmpty(X) || 
   {X[k] | k <- X} == range(X);

test bool tst_rangeR(map[int, int] X) {
   s = sample(X);
   XR = rangeR(X, s);
   return isEmpty(XR) || all(k <- XR, X[k] in s);
}

test bool tst_rangeX(map[int, int] X) {
   s = sample(X);
   XR = rangeX(X, s);
   return isEmpty(XR) || all(k <- XR, X[k] notin s);
}

test bool tst_toList(map[int,int] S) = isEmpty(S) || size(S) == size(toList(S)) && all(k <- S, <k, S[k]> in toList(S));

test bool tst_toRel(map[int,int] S) = isEmpty(S) || size(S) == size(toRel(S)) && all(k <- S, <k, S[k]> in toRel(S));


