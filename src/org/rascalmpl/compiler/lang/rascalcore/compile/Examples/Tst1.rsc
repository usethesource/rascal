module lang::rascalcore::compile::Examples::Tst1

test bool exceptionHandling1(){
    value f() { throw "Try to catch me!"; }
    
   
    f();
   
    return true; //n == "0 1 2 8";
}