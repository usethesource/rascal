@license{
Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
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
                          
                        