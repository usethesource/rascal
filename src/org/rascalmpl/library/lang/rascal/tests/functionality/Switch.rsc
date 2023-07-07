module lang::rascal::tests::functionality::Switch

// testSwitch
  
test bool testSwitch1a() {int n = 0; switch(2){ case 2: n = 2; case 4: n = 4; case 6: n = 6; default: n = 10;} return n == 2;}
test bool testSwitch1b() {int n = 0; switch(4){ case 2: n = 2; case 4: n = 4; case 6: n = 6; default: n = 10;} return n == 4;}
test bool testSwitch1c() {int n = 0; switch(6){ case 2: n = 2; case 4: n = 4; case 6: n = 6; default: n = 10;} return n == 6;}
test bool testSwitch1d() {int n = 0; switch(8){ case 2: n = 2; case 4: n = 4; case 6: n = 6; default: n = 10;} return n == 10;}
test bool testSwitch1e() {int n = 0; switch(8){ default: ;} return n == 0;}
test bool testSwitch1f() {int n = 0; switch(8){ default: n = 10;} return n == 10;}
 
 int sw2(int e){    
    int n = 0;
    switch(e){
        case 1 :        n = 1;
        case _: 2:      n = 2;
        case int _: 3:  n = 3;
        default:        n = 4;
    }
    return n;
 }  
 
 test bool testSwitch2a() = sw2(1) == 1;
 test bool testSwitch2b() = sw2(2) == 2;
 test bool testSwitch2c() = sw2(3) == 3;
 test bool testSwitch2d() = sw2(4) == 4;
 
 int sw3(str e){    
    int n = 0;
    switch(e){
        case "abc": n = 1;
        case /A/: n = 2;
        case str _: "def": n = 3;
        default: n = 4;
    }
    return n;
 }
 
 test bool testSwitch3a() = sw3("abc") == 1;
 test bool testSwitch3b() = sw3("AAA") == 2;
 test bool testSwitch3c() = sw3("def") == 3;
 test bool testSwitch3d() = sw3("zzz") == 4;
 
data D = d(int i) | d();
    
 int sw4(value e){  
    int n = 0;
    switch(e){
        case "abc":         n = 1;
        case str _: /def/:  n = 2;
        case 3:             n = 3;
        case d():           n = 4;
        case d(_):          n = 5;
        case str _(3):      n = 6;
        case [1,2,3]:       n = 7;
        case [1,2,3,4]:     n = 8;
        default:            n = 9;
    }
    return n;
 }
 
 test bool testSwitch4a() = sw4("abc")      == 1;
 test bool testSwitch4b() = sw4("def")      == 2;
 test bool testSwitch4c() = sw4(3)          == 3;
 test bool testSwitch4d() = sw4(d())        == 4;
 test bool testSwitch4e() = sw4(d(2))       == 5;
 test bool testSwitch4f() = sw4("abc"(3))   == 6;
 test bool testSwitch4g() = sw4([1,2,3])    == 7;
 test bool testSwitch4h() = sw4([1,2,3,4])  == 8;
 test bool testSwitch4i() = sw4(<-1,-1>)    == 9;
 
 data E = e() | e(int n) | e(str s, int m);
 
 int sw5(value v){
    int n = 0;
    switch(v){
        case "abc":                             n = 1;
        case e(/<s:^[A-Za-z0-9\-\_]+$>/, 2):     { n = 2; if (str _ := s /* just use the s to avoid warning */) true; }
        case e(/<s:^[A-Za-z0-9\-\_]+$>/, 3):     { n = 3; if (str _ := s) true; }
        case 4:                                 n = 4;
        case e():                               n = 5;
        case e(int _):                          n = 6;
        case str _(7):                          n = 7;
        case [1,2,3]:                           n = 8;
        case [1,2,3,4]:                         n = 9;
        case e("abc", 10):                      n = 10;
        case e("abc", int _):                   n = 11;
        case node _:                            n = 12;
        default:                                n = 13;
    }
    return n;
}

test bool testSwitch5a() = sw5("abc")       == 1;
test bool testSwitch5b() = sw5(e("abc",2))  == 2;
test bool testSwitchdc() = sw5(e("abc",3))  == 3;
test bool testSwitch5e() = sw5(4)           == 4;
test bool testSwitch5f() = sw5(e())         == 5;
test bool testSwitch5g() = sw5(e(6))        == 6;
test bool testSwitch5h() = sw5(e(7))        == 6;
test bool testSwitch5i() = sw5("f"(7))      == 7;
test bool testSwitch5j() = sw5([1,2,3])     == 8;
test bool testSwitch5k() = sw5([1,2,3,4])   == 9;
test bool testSwitch5l() = sw5(e("abc",10)) == 10;
test bool testSwitch5m() = sw5(e("abc",11)) == 11;
test bool testSwitch5n() = sw5("f"(12))     == 12;
test bool testSwitch5o() = sw5(13)          == 13;
    
int sw6(value v){
    int n = 0;
    switch(v){
        case true:                              n = 1;
        case 2:                                 n = 2;
        case 3.0:                               n = 3;
        case 4r3:                               n = 4;
        case |home:///|:                        n = 5;  
        case $2015-02-11T20:09:01.317+00:00$:   n = 6;
        case "abc":                             n = 7;
        case [1,2,3]:                           n = 8;
        case [<1,2,3>]:                         n = 9;
        case {1,2,3}:                           n = 10;
        case {<1,2,3>}:                         n = 11;
        //case ("a" : 1):                       n = 12;
        default:                                n = 13;
    }
    return n;
}

@ignoreInterpreter{
Location, datetime and map pattern not supported
}
test bool testSwitch6a() = sw6(true)        == 1;

@ignoreInterpreter{
Location, datetime and map pattern not supported
}
test bool testSwitch6b() = sw6(2)           == 2;
@ignoreInterpreter{
Location, datetime and map pattern not supported
}
test bool testSwitch6c() = sw6(3.0)         == 3;
@ignoreInterpreter{
Location, datetime and map pattern not supported
}
test bool testSwitch6d() = sw6(4r3)         == 4;
@ignoreInterpreter{
Location, datetime and map pattern not supported
}
test bool testSwitch6e() = sw6(|home:///|)  == 5;
@ignoreInterpreter{
Location, datetime and map pattern not supported
}
test bool testSwitch6f() = sw6($2015-02-11T20:09:01.317+00:00$)     
                                            == 6;
@ignoreInterpreter{
Location, datetime and map pattern not supported
}
test bool testSwitch6g() = sw6("abc")       == 7;
@ignoreInterpreter{
Location, datetime and map pattern not supported
}
test bool testSwitch6h() = sw6([1,2,3])     == 8;
@ignoreInterpreter{
Location, datetime and map pattern not supported
}
test bool testSwitch6i() = sw6([<1,2,3>])   == 9;
@ignoreInterpreter{
Location, datetime and map pattern not supported
}
test bool testSwitch6j() = sw6({1,2,3})     == 10;
@ignoreInterpreter{
Location, datetime and map pattern not supported
}
test bool testSwitch6k() = sw6({<1,2,3>})   == 11;
@ignore{
map pattern not supported
}
test bool testSwitch6l() = sw6(("a" : 1))   == 12;
@ignoreInterpreter{
Location, datetime and map pattern not supported
}
test bool testSwitch6m() = sw6(13)          == 13;

