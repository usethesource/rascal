@license{
  Copyright (c) 2009-2015 CWI
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
import util::Math;
import Type;
import Node;
import Exception;
import IO;

private map[&K,&V] emptyMap(type[map[&K,&V]] _) = ();
private list[&T] emptyList(type[&T] _) = [];
private map[value,value] up(map[&K,&V] m) = m;

// composition
test bool composition1(map[&K,&V] M) = M o emptyMap(#map[value,value]) == ();
test bool composition2(map[&K,&V] M) = () o M == ();
test bool composition3(map[&K,&V] M) = M o (v:v | &K k <- M, &V v := M[k]) == M;
test bool composition4(map[&K,&V] M) = (k:k | &K k <- M) o M == M;

// comprehension
test bool comprehension1() = (k:k*k | k <- emptyList(#num)) == ();
test bool comprehension2() = (k:k*k | k <- [1..5]) == (1:1,2:4,3:9,4:16);
test bool comprehension3(set[&K] xs) = size((k:k | &K k <- delAnnotationsRec(xs))) == size(delAnnotationsRec(xs));

// difference
test bool difference1(map[&K,&V] M) = M - () == M;
test bool difference2(map[&K,&V] M) = () - M == ();
test bool difference3() = (1:10) - (2:20) == (1:10);
test bool difference4() = (1:10) - (1:10) == ();
test bool difference5() = (1:10) - (1:20) == ();

// equal
test bool equal1() = () == ();
test bool equal2(map[&K,&V] M) = M == M;
test bool equal3(map[&K,&V] M1, map[&K,&V] M2)
	= (M1 == M2) ==>
	( domain(M1) == domain(M2)
	&& range(M1) == range(M2)
	&& (isEmpty(M1) || all(x <- M1, eq(M1[x],M2[x]))));
	
test bool equal4()
{
	map[int,int] iie = ();
	map[str,str] sse = ();
	map[value,value] iiev = iie;
	map[value,value] ssev = sse;
	return iiev == ssev;
}
test bool equal5() = (2:20,1:10) == (1:10,2:20);
test bool equal6()
{
	map[int,int] iit = (1:10);
	map[num,value] nvt = (1:10);
	return iit == nvt;
}

// in
test bool in1(map[&K,&V] M) = isEmpty(M) || all(&K k <- M, k in M);
test bool in2(&K k) = k in (k:k); 
test bool in3(&K k, &V v, map[&K,&V] M) = k in (M + (k:v)); 
test bool in4(&K k, &V v, map[&K,&V] M) = eq(v, (M + (k:v))[k]); 
test bool in5(&K k, &V v, map[&K,&V] M) = k in ((k:v) + M); 
test bool in6(&K k, &V v, map[&K,&V] M) = k in M || eq(v, ((k:v) + M)[k]); 

// intersection
test bool intersection1() = (1:10, 2:20) & (3:30) == ();
test bool intersection2() = (1:10, 2:20) & (2:30) == ();
test bool intersection3() = (1:10, 2:20) & (1:20) == ();
test bool intersection4() = (1:10, 2:20) & (1:10) == (1:10);
test bool intersection5() = (1:10, 2:20) & (2:20) == (2:20);
test bool intersection6(map[&K,&V] M) = M & () == ();
test bool intersection7(map[&K,&V] M) = () & M == ();

// notequal
test bool notequal1() = !(() != ());
test bool notequal2(map[&K,&V] M) = !(M != M);
test bool notequal3(map[&K,&V] M1, map[&K,&V] M2)
	= (M1 != M2) ==>
	(domain(M1) != domain(M2)
	|| range(M1) != range(M2)
	|| isEmpty(M1) 
	|| any(x <- M1, !eq(M1[x],M2[x])));
	
test bool notequal4() {
	map[value,value] iie = (1:1) - (1:1);
	map[value,value] sse = ("x":"x") - ("x":"x");
	return !(iie != sse);
}

test bool notequal5() = !((2:20,1:10) != (1:10,2:20));
test bool notequal6()
{
	map[int,int] iit = (1:10);
	map[num,value] nvt = (1:10);
	return !(iit != nvt);
}
test bool notequal7() = up((1:10)) != up((1.0:10));
test bool notequal8(map[&K,&V] M) = isEmpty(M) || M != ();

// notin
test bool notin1(&K k) = k notin ();
test bool notin2(&K k, map[&K,&V] M) = k notin (M - (k:k));

// pattern matching
test bool pm1() { value n = 1; return map[int, int] _ := ( n : n ); }
test bool pm2() { value n = 1; value s = "string"; return map[str, int] _ := ( s : n ); }
test bool pm3() { value n = 1; value s = "string"; return map[int, str] _ := ( n : s ); }
test bool pm4() { value s = "string"; return map[str, str] _ := ( s : s ); }

// strictsubmap
test bool strictsubmap1(map[&K,&V] M) = isEmpty(M) || () < M;
test bool strictsubmap2(map[&K,&V] M) = isEmpty(M) || delete(M,getOneFrom(M)) < M;

// strictsupermap
test bool strictsupermap1(map[&K,&V] M) = isEmpty(M) || M > ();
test bool strictsupermap2(map[&K,&V] M) = isEmpty(M) || M > delete(M,getOneFrom(M));

// submap
test bool submap1(map[&K,&V] M) = () <= M;
test bool submap2(map[&K,&V] M) = M <= M;
test bool submap3(map[&K,&V] M) = isEmpty(M) || delete(M,getOneFrom(M)) <= M;
test bool submap4(map[&K,&V] M1, map[&K,&V] M2) = M1 < M2 ==> M1 <= M2;

// subscription
@expected{NoSuchKey} test bool subscription1(&K k) {map[&K,bool] M = (); return M[k];}
@expected{NoSuchKey} test bool subscription2() = (1:false)[2];
test bool subscription3() = (1:10)[1] == 10;
test bool subscription4() = (1:10,2:20)[1] == 10;

// supermap
test bool supermap1(map[&K,&V] M) = M >= ();
test bool supermap2(map[&K,&V] M) = M >= M;
test bool supermap3(map[&K,&V] M) = isEmpty(M) || M >= delete(M,getOneFrom(M));
test bool supermap4(map[&K,&V] M1, map[&K,&V] M2) = M1 > M2 ==> M1 >= M2;

// union
test bool union1(map[&K,&V] M1, map[&K,&V] M2) = size(M1 + M2) >= size(M1);
test bool union2(map[&K,&V] M) = () + M == M;
test bool union3(map[&K,&V] M) = M + () == M;
test bool union4() = (1:10) + (2:20) == (1:10,2:20);

// +=
test bool increment1(){
    map[int,rel[int,int]] M = (0 : {<1,10>});
    M[0] += <10,20>;
    return M ==  (0 : {<1,10>, <10,20>});
}

////////////////////////////////////////////////////////////////////////////////
// legacy

bool keyIsInRange(&K x, map[&K, &V] A, map[&K, &V] B, map[&K, &V] C)
    = (x in domain(B) && eq(C[x],B[x])) || (x in domain(A) && eq(C[x],A[x]));

bool rightValIsUsedForKey(&K x, map[&K, &V] A, map[&K, &V] B, map[&K, &V] C)
    =  (x in domain(B) && x in domain(A)) ==> eq(C[x],B[x]);
    
// is A + B == C?
bool isUnion(map[&K, &V] A, map[&K, &V] B, map[&K, &V] C) =
     isEmpty(A) ? C == B
                : (isEmpty(B) ? C == A
                              : ( domain(A) + domain(B) == domain(C) &&
                                  range(C) <= range(A) + range(B) &&
                                  all(x <- C, keyIsInRange(x,A,B,C)) &&
                                  all(x <- C, rightValIsUsedForKey(x, A, B, C))
     ));
  
test bool union(map[&K, &V] A, map[&K, &V] B) = isUnion(A,   B,  A + B);

// is A - B == C?
bool isDiff(map[&K, &V] A, map[&K, &V] B, map[&K, &V] C) =
     isEmpty(A) ? isEmpty(C)
                : ( isEmpty(B) ? C == A
                               : (isEmpty(C) ? domain(A) <= domain(B)
                                             : all(x <- C, x in domain(A) && x notin domain(B))));
     
test bool diff(map[&K, &V] A, map[&K, &V] B) = isDiff(A, B, A - B);
 
test bool intersection(map[&K, &V] A, map[&K, &V] B) = isEmpty(A & B) || all(x <- A & B, x in A, x in B, eq(A[x], B[x]));


test bool lesseq(map[&K, &V] A, map[&K, &V] B)  = A <= (B + A); // right overwrites left

test bool less(map[&K, &V] A, map[&K, &V] B) = (A != B + A) ==> A < (B + A);

test bool greatereq(map[&K, &V] A, map[&K, &V] B)  = (B + A) >= A;
test bool greater(map[int, str] A, map[int, str] B)  = A<0> & B<0> == {} ==> B <= A || (B + A) > A;

test bool intKeyHandling1(){
    N = 10000;
    m = (i : i | int i <- [0..N]);
    return size(m) == N && size(domain(m)) == N && size(range(m)) == N && domain(m) == range(m);
}

test bool intKeyHandling2(){
    N = 10000;
    m = (i : i | int i <- [0..N]);
    return all(int i <- [0..N], i in m, m[i] == i);
}

test bool intKeyHandling3(){
    N = 10000;
    m = (i : i | int i <- [0..N]);
    for(int i <- [0..N]){
       m[i] = 2*i;
    }
    return size(m) == N && size(domain(m)) == N && size(range(m)) == N && all(int i <- [0..N], i in m, m[i] == 2*i);
}

test bool intKeyHandling4(){
    N = 10000;
    m = ("X<i>Y" : "X<i>Y" | int i <- [0..N]);
    return size(m) == N && size(domain(m)) == N && size(range(m)) == N && domain(m) == range(m);
}

test bool intKeyHandling5(){
    N = 10000;
    m = ("X<i>Y" : "X<i>Y" | int i <- [0..N]);
    return  all(int i <- [0..N], "X<i>Y" in m, m["X<i>Y"] == "X<i>Y");
}

test bool intKeyHandling6(){
    N = 10000;
    m = ("X<i>Y" : "X<i>Y" | int i <- [0..N]);
    for(int i <- [0..N]){
       m["X<i>Y"] = "XX<i>YY";
    }
    return size(m) == N && size(domain(m)) == N && size(range(m)) == N && all(int i <- [0..N], "X<i>Y" in m, m["X<i>Y"] == "XX<i>YY");
}


