module tests::functionality::SetMatchTests1

data TYPESET = SET(str name) | SUBTYPES(TYPESET tset) | INTERSECT(set[TYPESET] tsets);
            
// Anastassija's type constraint examples
 
        
bool testSimplifyA(TYPESET (TYPESET ts) simplify) = 
    simplify(INTERSECT({ SUBTYPES(INTERSECT({  })), SET("s1") })) ==
             INTERSECT({ SUBTYPES(INTERSECT({  })), SET("s1") });

bool testSimplifyB(TYPESET (TYPESET ts) simplify) = 
    simplify(INTERSECT({ SUBTYPES(INTERSECT({SET("s1") })), SET("s2") })) ==
             INTERSECT({ SUBTYPES(INTERSECT({SET("s1") })),  SET("s2") });

bool testSimplifyC(TYPESET (TYPESET ts) simplify) = 
    simplify(INTERSECT({ SUBTYPES(INTERSECT({ SET("s1") })), SET("s1") })) ==
             INTERSECT({ SUBTYPES(INTERSECT({           })), SET("s1") });
      
bool testSimplifyD(TYPESET (TYPESET ts) simplify) = 
    simplify(INTERSECT({ SUBTYPES(INTERSECT({ SET("s1") })), SET("s1"), SET("s2")  })) ==
             INTERSECT({ SUBTYPES(INTERSECT({           })), SET("s1"), SET("s2")  });

bool testSimplifyE(TYPESET (TYPESET ts) simplify) = 
    simplify(INTERSECT({ SUBTYPES(INTERSECT({ SET("s1") })), SET("s2"), SET("s1")  })) ==
             INTERSECT({ SUBTYPES(INTERSECT({           })), SET("s2"), SET("s1")  });

bool testSimplifyF(TYPESET (TYPESET ts) simplify) = 
    simplify(INTERSECT({ SUBTYPES(INTERSECT({ SET("s1"), SET("s2") })), SET("s3") })) ==
             INTERSECT({ SUBTYPES(INTERSECT({ SET("s1"), SET("s2") })), SET("s3") });
  
bool testSimplifyG(TYPESET (TYPESET ts) simplify) = 
    simplify(INTERSECT({ SUBTYPES(INTERSECT({ SET("s1"), SET("s2") })), SET("s3"), SET("s4") }))==
             INTERSECT({ SUBTYPES(INTERSECT({ SET("s1"), SET("s2") })), SET("s3"), SET("s4")  });

bool testSimplifyH(TYPESET (TYPESET ts) simplify) = 
    simplify(INTERSECT({ SUBTYPES(INTERSECT({ SET("s1"), SET("s3") })), SET("s1") }))==
             INTERSECT({ SUBTYPES(INTERSECT({            SET("s3") })), SET("s1") });

bool testSimplifyI(TYPESET (TYPESET ts) simplify) = 
    simplify(INTERSECT({ SUBTYPES(INTERSECT({ SUBTYPES(SET("s3"))})), SET("s3") }))==
             INTERSECT({ SUBTYPES(INTERSECT({ SUBTYPES(SET("s3"))})), SET("s3") });

bool testSimplifyJ(TYPESET (TYPESET ts) simplify) = 
    simplify(INTERSECT({ SUBTYPES(INTERSECT({ SET("s70"), SET("s4")})), SET("s70") })) ==
             INTERSECT({ SUBTYPES(INTERSECT({             SET("s4")})), SET("s70") });

bool testSimplifyK(TYPESET (TYPESET ts) simplify) = 
    simplify(INTERSECT({ SUBTYPES(INTERSECT({ SET("s1"), SUBTYPES(SET("s3")) })), SET("s1") })) ==
             INTERSECT({ SUBTYPES(INTERSECT({            SUBTYPES(SET("s3")) })), SET("s1") });

bool testSimplifyL(TYPESET (TYPESET ts) simplify) = 
    simplify(INTERSECT({ SUBTYPES(INTERSECT({ SET("s1"), SUBTYPES(SET("s3")) })), SUBTYPES(SET("s2")), SET("s1") })) == 
             INTERSECT({ SUBTYPES(INTERSECT({            SUBTYPES(SET("s3")) })), SUBTYPES(SET("s2")), SET("s1") });

bool testSimplifyM(TYPESET (TYPESET ts) simplify) = 
    simplify(INTERSECT({ SUBTYPES(INTERSECT({ SET("s1"), SUBTYPES(SET("s2")) })), SUBTYPES(SET("s2")), SET("s1") })) ==
             INTERSECT({ SUBTYPES(INTERSECT({                                })), SUBTYPES(SET("s2")), SET("s1") });  

bool testSimplifyN(TYPESET (TYPESET ts) simplify) = 
    simplify(INTERSECT({ SUBTYPES(INTERSECT({ SUBTYPES(SET("s3")), SUBTYPES(SET("s2")) })), SUBTYPES(SET("s2")) })) ==
             INTERSECT({ SUBTYPES(INTERSECT({ SUBTYPES(SET("s3"))                      })), SUBTYPES(SET("s2")) });

bool testSimplifyO(TYPESET (TYPESET ts) simplify) = 
    simplify(INTERSECT({ SUBTYPES(INTERSECT({ SET("s1"), SUBTYPES(SET("s2")), SUBTYPES(SET("s3")) })), SUBTYPES(SET("s2")), SET("s1") })) ==
             INTERSECT({ SUBTYPES(INTERSECT({                                 SUBTYPES(SET("s3")) })), SUBTYPES(SET("s2")), SET("s1") });

bool testSimplifyP(TYPESET (TYPESET ts) simplify) = 
    simplify(INTERSECT({ SUBTYPES(INTERSECT({ SET("s1"), SET("s2"), SET("s3"), SET("s5") })), SET("s6"), SET("s2"), SET("s7"), SET("s1") })) ==
             INTERSECT({ SUBTYPES(INTERSECT({                       SET("s3"), SET("s5") })), SET("s6"), SET("s2"), SET("s7"), SET("s1") });

bool testSimplifyQ(TYPESET (TYPESET ts) simplify) = 
    simplify(INTERSECT({ SUBTYPES(INTERSECT({ SET("s1"), SUBTYPES(SET("s2")), SET("s3"), SET("s5") })), SET("s6"), SUBTYPES(SET("s2")), SET("s7"), SET("s1"), SET("s3") })) ==
             INTERSECT({ SUBTYPES(INTERSECT({                                            SET("s5") })), SET("s6"), SUBTYPES(SET("s2")), SET("s7"), SET("s1"), SET("s3") });
    
     
// Version 1; with explicit simplification function, no non-linear constraints, fail to explore alternative matches
    
 TYPESET simp1(TYPESET ts){
   for(INTERSECT({ SUBTYPES(INTERSECT({ TYPESET tset, *TYPESET rest})), TYPESET tset1, *TYPESET rest1 }) := ts){
        if(tset == tset1) return simp1(INTERSECT({ SUBTYPES(INTERSECT(rest)), tset1, *rest1 }));
        else  fail;
   }
   return ts;
}
           
test bool testSimp1A() = testSimplifyA(simp1); 
test bool testSimp1B() = testSimplifyB(simp1); 
test bool testSimp1C() = testSimplifyC(simp1); 
test bool testSimp1D() = testSimplifyD(simp1); 
test bool testSimp1E() = testSimplifyE(simp1); 
test bool testSimp1F() = testSimplifyF(simp1); 
test bool testSimp1G() = testSimplifyG(simp1); 
test bool testSimp1H() = testSimplifyH(simp1); 
test bool testSimp1I() = testSimplifyI(simp1); 
test bool testSimp1J() = testSimplifyJ(simp1); 
test bool testSimp1K() = testSimplifyK(simp1); 
test bool testSimp1L() = testSimplifyL(simp1); 
test bool testSimp1M() = testSimplifyM(simp1); 
test bool testSimp1N() = testSimplifyN(simp1); 
test bool testSimp1O() = testSimplifyO(simp1); 
test bool testSimp1P() = testSimplifyP(simp1); 
test bool testSimp1Q() = testSimplifyQ(simp1); 
        
// Version 2; with explicit simplification function, and non-linear constraints (tset)

public TYPESET simp2(TYPESET ts){
     if(INTERSECT({ SUBTYPES(INTERSECT({ TYPESET tset, *TYPESET rest})), tset, *TYPESET rest1 }) := ts){
          return simp2(INTERSECT({ SUBTYPES(INTERSECT(rest)), tset, *rest1 }));
     }
     return ts;
}
          
test bool testSimp2A() = testSimplifyA(simp2); 
test bool testSimp2B() = testSimplifyB(simp2); 
test bool testSimp2C() = testSimplifyC(simp2); 
test bool testSimp2D() = testSimplifyD(simp2); 
test bool testSimp2E() = testSimplifyE(simp2); 
test bool testSimp2F() = testSimplifyF(simp2); 
test bool testSimp2G() = testSimplifyG(simp2); 
test bool testSimp2H() = testSimplifyH(simp2); 
test bool testSimp2I() = testSimplifyI(simp2); 
test bool testSimp2J() = testSimplifyJ(simp2); 
test bool testSimp2K() = testSimplifyK(simp2); 
test bool testSimp2L() = testSimplifyL(simp2); 
test bool testSimp2M() = testSimplifyM(simp2); 
test bool testSimp2N() = testSimplifyN(simp2); 
test bool testSimp2O() = testSimplifyO(simp2); 
test bool testSimp2P() = testSimplifyP(simp2); 
test bool testSimp2Q() = testSimplifyQ(simp2); 
 
// Version 3; with explicit simplification function, non-linear constraints (tset) and nested simp call
        
public TYPESET simp3(TYPESET ts){
     if(INTERSECT({ SUBTYPES(INTERSECT({ TYPESET tset, *TYPESET rest})), tset, *TYPESET rest1 }) := ts){
          return simp3(INTERSECT({ SUBTYPES(simp3(INTERSECT(rest))), tset, *rest1 }));
     }
     return ts;
}
      
test bool testSimp3A() = testSimplifyA(simp3); 
test bool testSimp3B() = testSimplifyB(simp3); 
test bool testSimp3C() = testSimplifyC(simp3); 
test bool testSimp3D() = testSimplifyD(simp3); 
test bool testSimp3E() = testSimplifyE(simp3); 
test bool testSimp3F() = testSimplifyF(simp3); 
test bool testSimp3G() = testSimplifyG(simp3); 
test bool testSimp3H() = testSimplifyH(simp3); 
test bool testSimp3I() = testSimplifyI(simp3); 
test bool testSimp3J() = testSimplifyJ(simp3); 
test bool testSimp3K() = testSimplifyK(simp3); 
test bool testSimp3L() = testSimplifyL(simp3); 
test bool testSimp3M() = testSimplifyM(simp3); 
test bool testSimp3N() = testSimplifyN(simp3); 
test bool testSimp3O() = testSimplifyO(simp3); 
test bool testSimp3P() = testSimplifyP(simp3); 
test bool testSimp3Q() = testSimplifyQ(simp3);     
    
        //
        // Anastassija's type constraint examples
        // Version 4; with overloaded constructor INTERSECT , and non-linear constraints (tset)
        //
    //  @Test
    //  public void testSet()  {
    //      prepare("data TYPESET = SET(str name) | SUBTYPES(TYPESET tset) | INTERSECT(set[TYPESET] tsets);");
    //      
    //      prepareMore("public TYPESET INTERSECT({ SUBTYPES(INTERSECT({ TYPESET tset, *TYPESET rest})), tset, *TYPESET rest1 }) {" +
    //                     " return INTERSECT({ SUBTYPES(INTERSECT(rest)), tset, *rest1 });" +
    //                  "}");
    //
    //      funTests();
    //  }
    