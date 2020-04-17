module lang::rascalcore::check::tests::ParameterizedTypes

import lang::rascalcore::check::tests::StaticTestingUtils;


test bool typeParamOK() = checkOK("&T f(&T x) = x;");
test bool newTypeParamInReturnOK() = checkOK("&T f(&S x) = x;");

test bool issue1300a() =
    unexpectedType("&T f(&T param) {
                   '  Wrap[&T] x = wrap(param);
                   '
                   ' return id(x);
                   '}",
                   initialDecls = ["data Wrap[&T] = wrap(&T val);
                                   '&T id(&T arg) = arg;"]);
                                   
test bool issue1300b() =
    unexpectedType("&S f(&S param) {
                   '  Wrap[&S] x = wrap(param);
                   '
                   ' return id(x);
                   '}",
                   initialDecls = ["data Wrap[&T] = wrap(&T val);
                                   '&T id(&T arg) = arg;"]);                                   
                                   
test bool issue1300c() =
    unexpectedType("&T f(&T param) {
                   '  Wrap[&T] x = wrap(param);
                   '
                   ' return x;
                   '}",
                   initialDecls = ["data Wrap[&T] = wrap(&T val);
                                   '&T id(&T arg) = arg;"]);
test bool issue1300d() =
           checkOK("&T f(&T param) {
                   '  Wrap[&T] x = wrap(param);
                   '
                   ' return x.val;
                   '}",
                   initialDecls = ["data Wrap[&T] = wrap(&T val);
                                   '&T id(&T arg) = arg;"]);
                                   
test bool issue1386a() = checkOK("bool f(type(symbol,definitions)) = true;");
                                   
test bool issue1386b() = checkOK("bool f(type[&T] _: type(symbol,definitions)) = true;");

test bool issue1386c() = checkOK("bool f(type[&T] x: type(symbol,definitions)) = true;");
 
test bool maybeOK1() = checkOK("1;",          
            initialDecls = ["data Maybe[&T] = none() | just(&T arg);"]    );  
            
test bool maybeOK2() = checkOK("Maybe[&S] nn() = none();",          
            initialDecls = ["data Maybe[&T] = none() | just(&T arg);"]    );   
            
test bool maybeOK3() = checkOK("Maybe[&S] mb() { if(3 \> 2) return just(3); return just(\"Abc\"); }",         
            initialDecls = ["data Maybe[&T] = none() | just(&T arg);"]    );  

test bool maybeBoundOK() = checkOK("Maybe[&S \<: num] mb() { if(3 \> 2) return just(3); return just(1.5); }",         
            initialDecls = ["data Maybe[&T] = none() | just(&T arg);"]    ); 
                        
test bool maybeBoundViolated() = unexpectedType("Maybe[&S \<: num] mb() { if(3 \> 2) return just(3); return just(\"Abc\"); }",         
            initialDecls = ["data Maybe[&T] = none() | just(&T arg);"]    );                 
