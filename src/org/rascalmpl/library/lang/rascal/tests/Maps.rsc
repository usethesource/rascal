module lang::rascal::tests::Maps

import Map;

// is A + B == C?
bool isUnion(map[&K, &V] A, map[&K, &V] B, map[&K, &V] C) =
     isEmpty(A) ==> C == B ||
     isEmpty(B) ==> C == A ||
     all(x <- C,  x in domain(B) && C[x] == B[x] || x in domain(A) && C[x] == A[x]);

public test bool union(map[&K, &V] A, map[&K, &V] B) = isUnion(A,   B,  A + B);

// is A - B == C?
bool isDiff(map[&K, &V] A, map[&K, &V] B, map[&K, &V] C) =
     isEmpty(A) ==> C == B ||
     isEmpty(B) ==> C == A ||
     all(x <- C, x in domain(A) && x notin domain(B));
     
public test bool diff(map[&K, &V] A, map[&K, &V] B) = isDiff(A, B, A - B);

// A == B?
public bool isEqual(map[&K, &V] A, map[&K, &V] B) = 
    domain(A) == domain(B) && range(A) == range(B) &&
    (size(A) == 0 || all(x <- A, A[x] == B[x]));

public test bool equal(map[&K, &V] A) = A == A;
public test bool equal(map[str, int] A, map[str, int] B) = (A == B) ? isEqual(A,B) : !isEqual(A, B);

public test bool notEqual(map[&K, &V] A) = !(A != A);
public test bool notEqual(map[&K, &V] A, map[&K, &V] B) = (A != B) ? !isEqual(A,B) : isEqual(A, B);
 
public test bool intersection(map[&K, &V] A, map[&K, &V] B) = isEmpty(A & B) || all(x <- A & B, x in A, x in B, A[x] == B[x]);

public test bool lesseq(map[&K, &V] A, map[&K, &V] B)  = A <= (A + B);
public test bool less(map[&K, &V] A, map[&K, &V] B) = isEmpty(B) || A < (A + B);

public test bool greatereq(map[&K, &V] A, map[&K, &V] B)  = (A + B) >= A;
public test bool greater(map[&K, &V] A, map[&K, &V] B)  = isEmpty(B) || (A + B) > A;

public test bool tst_in(&K key, &V val, map[&K, &V] M) = key in M || (key in (M + (key : val)) && val == (M + (key : val))[key] &&
														              key in ((key : val) + M) && val == ((key : val) + M)[key]);
public test bool tst_notin(int A, set[int] B) = A notin (B - A);

public test bool splicing(set[&T] A, set[&T] B) = {*A, *B} == A + B && {A, *B} == {A} + B && {*A, B} == A + {B};

