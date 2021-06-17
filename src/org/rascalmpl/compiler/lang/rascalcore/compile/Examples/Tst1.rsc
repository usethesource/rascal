module lang::rascalcore::compile::Examples::Tst1

 
syntax X7 = "x"? x7;
test bool tX7b() ="<([X7] "").x7>" == "";

//lexical Example = ([A-Z] head [a-z]* tail)+ words;
//          
//value main(){ //test bool fieldsFromLexicals2() = 
//    
//    Example t = [Example] "CamelCaseBaseFeest"; 
//    return /*["amel", "ase", "ase", "eest"] == */[ w.tail |  w <- t.words ];
//}