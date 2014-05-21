module lang::rascal::tests::functionality::SetMatchTests2

import lang::rascal::tests::functionality::SetMatchTests1;

// Anastassija's type constraint examples

// Version 4; with overloaded constructor INTERSECT , and non-linear constraints (tset)
        
public TYPESET INTERSECT({ SUBTYPES(INTERSECT({ TYPESET tset, *TYPESET rest})), tset, *TYPESET rest1 }) {
    return INTERSECT({ SUBTYPES(INTERSECT(rest)), tset, *rest1 });
}

        
test bool testSimplifyA() = 
   INTERSECT({ SUBTYPES(INTERSECT({  })), SET("s1") }) ==
   INTERSECT({ SUBTYPES(INTERSECT({  })), SET("s1") });

test bool testSimplifyB() = 
    INTERSECT({ SUBTYPES(INTERSECT({SET("s1") })), SET("s2") }) ==
    INTERSECT({ SUBTYPES(INTERSECT({SET("s1") })),  SET("s2") });

test bool testSimplifyC() = 
    INTERSECT({ SUBTYPES(INTERSECT({ SET("s1") })), SET("s1") }) ==
    INTERSECT({ SUBTYPES(INTERSECT({           })), SET("s1") });
      
test bool testSimplifyD() = 
    INTERSECT({ SUBTYPES(INTERSECT({ SET("s1") })), SET("s1"), SET("s2")  }) ==
    INTERSECT({ SUBTYPES(INTERSECT({           })), SET("s1"), SET("s2")  });

test bool testSimplifyE() = 
    INTERSECT({ SUBTYPES(INTERSECT({ SET("s1") })), SET("s2"), SET("s1")  }) ==
    INTERSECT({ SUBTYPES(INTERSECT({           })), SET("s2"), SET("s1")  });

test bool testSimplifyF() = 
    INTERSECT({ SUBTYPES(INTERSECT({ SET("s1"), SET("s2") })), SET("s3") }) ==
             INTERSECT({ SUBTYPES(INTERSECT({ SET("s1"), SET("s2") })), SET("s3") });
  
test bool testSimplifyG() = 
    INTERSECT({ SUBTYPES(INTERSECT({ SET("s1"), SET("s2") })), SET("s3"), SET("s4") }) ==
    INTERSECT({ SUBTYPES(INTERSECT({ SET("s1"), SET("s2") })), SET("s3"), SET("s4")  });

test bool testSimplifyH() = 
    INTERSECT({ SUBTYPES(INTERSECT({ SET("s1"), SET("s3") })), SET("s1") }) ==
    INTERSECT({ SUBTYPES(INTERSECT({            SET("s3") })), SET("s1") });

test bool testSimplifyI() = 
    INTERSECT({ SUBTYPES(INTERSECT({ SUBTYPES(SET("s3"))})), SET("s3") }) ==
    INTERSECT({ SUBTYPES(INTERSECT({ SUBTYPES(SET("s3"))})), SET("s3") });

test bool testSimplifyJ() = 
    INTERSECT({ SUBTYPES(INTERSECT({ SET("s70"), SET("s4")})), SET("s70") }) ==
    INTERSECT({ SUBTYPES(INTERSECT({             SET("s4")})), SET("s70") });

test bool testSimplifyK() = 
    INTERSECT({ SUBTYPES(INTERSECT({ SET("s1"), SUBTYPES(SET("s3")) })), SET("s1") }) ==
    INTERSECT({ SUBTYPES(INTERSECT({            SUBTYPES(SET("s3")) })), SET("s1") });

test bool testSimplifyL() = 
    INTERSECT({ SUBTYPES(INTERSECT({ SET("s1"), SUBTYPES(SET("s3")) })), SUBTYPES(SET("s2")), SET("s1") }) == 
    INTERSECT({ SUBTYPES(INTERSECT({            SUBTYPES(SET("s3")) })), SUBTYPES(SET("s2")), SET("s1") });

test bool testSimplifyM() = 
    INTERSECT({ SUBTYPES(INTERSECT({ SET("s1"), SUBTYPES(SET("s2")) })), SUBTYPES(SET("s2")), SET("s1") }) ==
    INTERSECT({ SUBTYPES(INTERSECT({                                })), SUBTYPES(SET("s2")), SET("s1") });  

test bool testSimplifyN() = 
    INTERSECT({ SUBTYPES(INTERSECT({ SUBTYPES(SET("s3")), SUBTYPES(SET("s2")) })), SUBTYPES(SET("s2")) }) ==
    INTERSECT({ SUBTYPES(INTERSECT({ SUBTYPES(SET("s3"))                      })), SUBTYPES(SET("s2")) });

test bool testSimplifyO() = 
    INTERSECT({ SUBTYPES(INTERSECT({ SET("s1"), SUBTYPES(SET("s2")), SUBTYPES(SET("s3")) })), SUBTYPES(SET("s2")), SET("s1") }) ==
    INTERSECT({ SUBTYPES(INTERSECT({                                 SUBTYPES(SET("s3")) })), SUBTYPES(SET("s2")), SET("s1") });

test bool testSimplifyP() = 
    INTERSECT({ SUBTYPES(INTERSECT({ SET("s1"), SET("s2"), SET("s3"), SET("s5") })), SET("s6"), SET("s2"), SET("s7"), SET("s1") }) ==
    INTERSECT({ SUBTYPES(INTERSECT({                       SET("s3"), SET("s5") })), SET("s6"), SET("s2"), SET("s7"), SET("s1") });

test bool testSimplifyQ() = 
    INTERSECT({ SUBTYPES(INTERSECT({ SET("s1"), SUBTYPES(SET("s2")), SET("s3"), SET("s5") })), SET("s6"), SUBTYPES(SET("s2")), SET("s7"), SET("s1"), SET("s3") }) ==
    INTERSECT({ SUBTYPES(INTERSECT({                                            SET("s5") })), SET("s6"), SUBTYPES(SET("s2")), SET("s7"), SET("s1"), SET("s3") });
