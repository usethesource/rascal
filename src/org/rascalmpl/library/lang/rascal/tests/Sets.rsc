module lang::rascal::tests::Sets

import Set;

// is A + B == C?
bool isUnion(set[&T] A, set[&T] B, set[&T] C) =
     isEmpty(A) ==> C == B ||
     isEmpty(B) ==> C == A ||
     all(x <- C, x in A || x in B);

public test bool union(set[&T] A, set[&T] B) = isUnion(A,   B,  A + B);
public test bool union(     &T A, set[&T] B) = isUnion({A}, B,  {A} + B);
public test bool union(set[&T] A,      &T B) = isUnion(A,   {B}, A +{B});

// is A - B == C?
bool isDiff(set[&T] A, set[&T] B, set[&T] C) =
     isEmpty(A) ==> C == B ||
     isEmpty(B) ==> C == A ||
     all(x <- C, x in A && x notin B);
     
public test bool diff(set[&T] A, set[&T] B) = isDiff(A, B, A - B);

// A == B?
public bool isEqual(set[&T] A, set[&T] B) = 
     size(A) == size(B) ? (size(A) == 0 || all(x <- A, x in B) && all(x <- B, x in A))
                        : false;

public test bool equal(set[&T] A) = A == A;
public test bool equal(set[int] A, set[int] B) = (A == B) ? isEqual(A,B) : !isEqual(A, B);

public test bool notEqual(set[&T] A) = !(A != A);
public test bool notEqual(set[int] A, set[int] B) = (A != B) ? !isEqual(A,B) : isEqual(A, B);
 
public test bool intersection(set[&T] A, set[&T] B) = isEmpty(A & B) || all(x <- A & B, x in A, x in B);

public test bool lesseq(set[int] A, set[int] B)  = A <= (A + B);
public test bool less(set[int] A, set[int] B) = isEmpty(B) || A < (A + B);

public test bool greatereq(set[int] A, set[int] B)  = (A + B) >= A;
public test bool greater(set[int] A, set[int] B)  = isEmpty(B) || (A + B) > A;

public test bool tst_in(int A, set[int] B) = A in (A + B) && A in (B + A);
public test bool tst_notin(int A, set[int] B) = A notin (B - A);

public test bool splicing(set[&T] A, set[&T] B) = {*A, *B} == A + B && {A, *B} == {A} + B && {*A, B} == A + {B};

