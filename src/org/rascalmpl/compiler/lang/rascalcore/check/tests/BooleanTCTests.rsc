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

test bool orOK5() = checkModuleOK("
    module orOK5
        data D = d1(int n) | d2(int n, int m);
        set[int] f(list[D] ds) = { n | d \<- ds, d1(n) := d || d2(n, _) := d };
    ");
                            
test bool orOK6() = checkModuleOK("
    module orOK6
        data D = d1(int n) | d2(int n, int m);  
        set[int] f(list[D] ds) = { n | d \<- ds, d1(int n) := d || d2(n, _) := d };
    ");
                                                     
test bool orOK7() = checkModuleOK("
    module orOK7
        data D = d1(int n) | d2(int n, int m);
        set[int] f(list[D] ds) = { n | d \<- ds, d1(n) := d || d2(int n, _) := d };
    ");
                           
test bool orOK8() = checkModuleOK("
    module orOK8
        data D = d1(int n) | d2(int n, int m);
        set[int] f(list[D] ds) = { n | d \<- ds, d1(int n) := d || d2(int n, _) := d };
    ");
                                
test bool orNOTOK5() = undeclaredVariableInModule("
    module orNOTOK5
        data D = d1(int n) | d2(int n, int m);
        set[int] f(list[D] ds) = { n | d \<- ds, d1(n) := d || d2(m, _) := d };
    ");
                            
test bool orNOTOK6() = undeclaredVariableInModule("
    module orNOTOK6
        data D = d1(int n) | d2(int n, int m);
        set[int] f(list[D] ds) = { n | d \<- ds, d1(int n) := d || d2(m, _) := d };
    ");
                                                    
test bool orNOTOK7() = undeclaredVariableInModule("
    module orNOTOK7
        data D = d1(int n) | d2(int n, int m);
        set[int] f(list[D] ds) = { n | d \<- ds, d1(n) := d || d2(int m, _) := d };
    ");
                           
test bool orNOTOK8() = undeclaredVariableInModule("
    module orNOTOK8
        data D = d1(int n) | d2(int n, int m);
        set[int] f(list[D] ds) = { n | d \<- ds, d1(int n) := d || d2(int m, _) := d };
    ");
                          
                        