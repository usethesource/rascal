module lang::rascalcore::check::tests::BooleanTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool OrOK1() = checkOK("set[int] f(list[int] ns) = { n | x \<- ns, n := 3 || n := 4};");
test bool OrOK2() = checkOK("set[int] f(list[int] ns) = { n | x \<- ns, int n := 3 || n := 4};");
test bool OrOK3() = checkOK("set[int] f(list[int] ns) = { n | x \<- ns, n := 3 || int n := 4};");
test bool OrOK4() = checkOK("set[int] f(list[int] ns) = { n | x \<- ns, int n := 3 || int n := 4};");

test bool OrNOTOK1() = undeclaredVariable("set[int] f(list[int] ns) = { n | x \<- ns, n := 3 || m := 4};");
test bool OrNOTOK2() = undeclaredVariable("set[int] f(list[int] ns) = { n | x \<- ns, int n := 3 || m := 4};");
test bool OrNOTOK3() = undeclaredVariable("set[int] f(list[int] ns) = { n | x \<- ns, n := 3 || int m := 4};");
test bool OrNOTOK4() = undeclaredVariable("set[int] f(list[int] ns) = { n | x \<- ns, int n := 3 || int m := 4};");

test bool OrOK5() = checkModuleOK("
    module OrOK5
        data D = d1(int n) | d2(int n, int m);
        set[int] f(list[D] ds) = { n | d \<- ds, d1(n) := d || d2(n, _) := d };
    ");
                            
test bool OrOK6() = checkModuleOK("
    module OrOK6
        data D = d1(int n) | d2(int n, int m);  
        set[int] f(list[D] ds) = { n | d \<- ds, d1(int n) := d || d2(n, _) := d };
    ");
                                                     
test bool OrOK7() = checkModuleOK("
    module OrOK7
        data D = d1(int n) | d2(int n, int m);
        set[int] f(list[D] ds) = { n | d \<- ds, d1(n) := d || d2(int n, _) := d };
    ");
                           
test bool OrOK8() = checkModuleOK("
    module OrOK8
        data D = d1(int n) | d2(int n, int m);
        set[int] f(list[D] ds) = { n | d \<- ds, d1(int n) := d || d2(int n, _) := d };
    ");
                                
test bool OrNOTOK5() = undeclaredVariableInModule("
    module OrNOTOK5
        data D = d1(int n) | d2(int n, int m);
        set[int] f(list[D] ds) = { n | d \<- ds, d1(n) := d || d2(m, _) := d };
    ");
                            
test bool OrNOTOK6() = undeclaredVariableInModule("
    module OrNOTOK6
        data D = d1(int n) | d2(int n, int m);
        set[int] f(list[D] ds) = { n | d \<- ds, d1(int n) := d || d2(m, _) := d };
    ");
                                                    
test bool OrNOTOK7() = undeclaredVariableInModule("
    module OrNOTOK7
        data D = d1(int n) | d2(int n, int m);
        set[int] f(list[D] ds) = { n | d \<- ds, d1(n) := d || d2(int m, _) := d };
    ");
                           
test bool OrNOTOK8() = undeclaredVariableInModule("
    module OrNOTOK8
        data D = d1(int n) | d2(int n, int m);
        set[int] f(list[D] ds) = { n | d \<- ds, d1(int n) := d || d2(int m, _) := d };
    ");
                          
                        