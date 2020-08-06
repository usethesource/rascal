module lang::rascalcore::check::tests::BooleanUsingStdLibTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;
                            
test bool orOK9() = checkOK("bool in1(map[&K,&V] M) = isEmpty(M) || all(&K k \<- M, k in M);", importedModules=["Map"]);