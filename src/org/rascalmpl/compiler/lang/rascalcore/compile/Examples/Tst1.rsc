module lang::rascalcore::compile::Examples::Tst1

import lang::rascalcore::compile::Examples::Tst0;


data Message 
    = error(str msg, loc at)
    | error(str msg)
    ;

//data Grammar 
//  = \grammar(int starts)
//  ;
//  
//  public void compose(Grammar g1) {
//  
//  g1.starts == 1;
//}    