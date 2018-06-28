module lang::rascalcore::check::Test1 

syntax A = small_a: "a";
  
syntax Expression
    =  A!small_a from ":" A to 
    ;

A g(Expression e) {
    return e.from;
}