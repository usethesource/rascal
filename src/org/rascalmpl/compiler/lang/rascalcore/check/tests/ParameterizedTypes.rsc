module lang::rascalcore::check::tests::ParameterizedTypes

import lang::rascalcore::check::tests::StaticTestingUtils;


test bool typeParamOK() = checkOK("&T f(&T x) = x;");
test bool newTypeParamInReturnNotOK() = unexpectedType("&T f(&S x) = x;");

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
            
test bool maybeOK2() = checkOK("Maybe[value] nn() = none();",          
            initialDecls = ["data Maybe[&T] = none() | just(&T arg);"]    );   

test bool maybeNotK2() = unexpectedType("Maybe[&S] nn() = none();",          
            initialDecls = ["data Maybe[&T] = none() | just(&T arg);"]    );   
            
test bool maybeOK3() = checkOK("Maybe[value] mb() { if(3 \> 2) return just(3); return just(\"Abc\"); }",         
            initialDecls = ["data Maybe[&T] = none() | just(&T arg);"]    ); 

test bool maybeNotOK3() = unexpectedType("Maybe[&S] mb() { if(3 \> 2) return just(3); return just(\"Abc\"); }",         
            initialDecls = ["data Maybe[&T] = none() | just(&T arg);"]    );   

test bool maybeBoundOK() = checkOK("Maybe[num] mb() { if(3 \> 2) return just(3); return just(1.5); }",         
            initialDecls = ["data Maybe[&T] = none() | just(&T arg);"]    ); 

test bool maybeBoundNotOK() = unexpectedType("Maybe[&S \<: num] mb() { if(3 \> 2) return just(3); return just(1.5); }",         
            initialDecls = ["data Maybe[&T] = none() | just(&T arg);"]    ); 
                        
test bool maybeBoundViolated() = unexpectedType("Maybe[&S \<: num] mb() { if(3 \> 2) return just(3); return just(\"Abc\"); }",         
            initialDecls = ["data Maybe[&T] = none() | just(&T arg);"]    );                 

test bool boundViolatedInCall()
    = unexpectedType("value main() = strange(3, \"abc\");",
        initialDecls = ["bool strange(&L \<: num _, &R \<: &L _) = false;"] );
        
test bool boundOKInCall()
    = checkOK("value main() = strange(3, 1.5);",
        initialDecls = ["bool strange(&L \<: num _, &R \<: &L _) = false;"] );
       
test bool boundViolatedInFormals()
    =  unexpectedType("bool strange(&L \<: num _, &R \<: &L \<: str _) = false;");

test bool boundViolatedInFormalsIndirect1()  
    = unexpectedType("bool strange(&A \<: num _, &B \<: &A _, &B \<: str _) = false;");
    
test bool boundViolatedInFormalsIndirect2()      
    = unexpectedType("bool strange(&A \<: num _, &B \<: &A _, &S \<: str _, &B \<: &S _) = false;");

test bool circular()   
    = checkOK("bool strange(&A \<: &B _, &B \<: &A _) = false;"); 
 
 test bool circularBoundsViolated()
    = unexpectedType("bool strange(&A \<: &B \<: str _, &B \<: &A \<: num _) = false;");