module tests::functionality::SetMatchTests2

import tests::functionality::SetMatchTests1;

// Anastassija's type constraint examples

// Version 4; with overloaded constructor INTERSECT , and non-linear constraints (tset)
        
public TYPESET INTERSECT({ SUBTYPES(INTERSECT({ TYPESET tset, *TYPESET rest})), tset, *TYPESET rest1 }) {
    return INTERSECT({ SUBTYPES(INTERSECT(rest)), tset, *rest1 });
}

public TYPESET ident(TYPESET ts) = ts;
    
test bool testSimp4A() = testSimplifyA(ident); 
test bool testSimp4B() = testSimplifyB(ident); 
test bool testSimp4C() = testSimplifyC(ident); 
test bool testSimp4D() = testSimplifyD(ident); 
test bool testSimp4E() = testSimplifyE(ident); 
test bool testSimp4F() = testSimplifyF(ident); 
test bool testSimp4G() = testSimplifyG(ident); 
test bool testSimp4H() = testSimplifyH(ident); 
test bool testSimp4I() = testSimplifyI(ident); 
test bool testSimp4J() = testSimplifyJ(ident); 
test bool testSimp4K() = testSimplifyK(ident); 
test bool testSimp4L() = testSimplifyL(ident); 
test bool testSimp4M() = testSimplifyM(ident); 
test bool testSimp4N() = testSimplifyN(ident); 
test bool testSimp4O() = testSimplifyO(ident); 
test bool testSimp4P() = testSimplifyP(ident); 
test bool testSimp4Q() = testSimplifyQ(ident);  