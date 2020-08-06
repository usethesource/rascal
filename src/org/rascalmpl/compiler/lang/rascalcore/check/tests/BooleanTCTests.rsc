module lang::rascalcore::check::tests::BooleanTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool orOK1() = checkOK("set[int] f(list[int] ns) = { n | x \<- ns, n := 3 || n := 4};");
test bool orOK2() = checkOK("set[int] f(list[int] ns) = { n | x \<- ns, int n := 3 || n := 4};");
test bool orOK3() = checkOK("set[int] f(list[int] ns) = { n | x \<- ns, n := 3 || int n := 4};");
test bool orOK4() = checkOK("set[int] f(list[int] ns) = { n | x \<- ns, int n := 3 || int n := 4};");

test bool orNOTOK1() = undeclaredVariable("set[int] f(list[int] ns) = { n | x \<- ns, n := 3 || m := 4};");
test bool orNOTOK2() = undeclaredVariable("set[int] f(list[int] ns) = { n | x \<- ns, int n := 3 || m := 4};");
test bool orNOTOK3() = undeclaredVariable("set[int] f(list[int] ns) = { n | x \<- ns, n := 3 || int m := 4};");
test bool orNOTOK4() = undeclaredVariable("set[int] f(list[int] ns) = { n | x \<- ns, int n := 3 || int m := 4};");

test bool orOK5() = checkOK("set[int] f(list[D] ds) = { n | d \<- ds, d1(n) := d || d2(n, _) := d };",
                            initialDecls=["data D = d1(int n) | d2(int n, int m);"]);
test bool orOK6() = checkOK("set[int] f(list[D] ds) = { n | d \<- ds, d1(int n) := d || d2(n, _) := d };",
                            initialDecls=["data D = d1(int n) | d2(int n, int m);"]);                            
test bool orOK7() = checkOK("set[int] f(list[D] ds) = { n | d \<- ds, d1(n) := d || d2(int n, _) := d };",
                            initialDecls=["data D = d1(int n) | d2(int n, int m);"]);
test bool orOK8() = checkOK("set[int] f(list[D] ds) = { n | d \<- ds, d1(int n) := d || d2(int n, _) := d };",
                            initialDecls=["data D = d1(int n) | d2(int n, int m);"]);
                            
                            
test bool orNOTOK5() = undeclaredVariable("set[int] f(list[D] ds) = { n | d \<- ds, d1(n) := d || d2(m, _) := d };",
                            initialDecls=["data D = d1(int n) | d2(int n, int m);"]);
test bool orNOTOK6() = undeclaredVariable("set[int] f(list[D] ds) = { n | d \<- ds, d1(int n) := d || d2(m, _) := d };",
                            initialDecls=["data D = d1(int n) | d2(int n, int m);"]);                            
test bool orNOTOK7() = undeclaredVariable("set[int] f(list[D] ds) = { n | d \<- ds, d1(n) := d || d2(int m, _) := d };",
                            initialDecls=["data D = d1(int n) | d2(int n, int m);"]);
test bool orNOTOK8() = undeclaredVariable("set[int] f(list[D] ds) = { n | d \<- ds, d1(int n) := d || d2(int m, _) := d };",
                            initialDecls=["data D = d1(int n) | d2(int n, int m);"]);
                        