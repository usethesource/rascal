module experiments::Compiler::Examples::Tst2

import IO;
import ParseTree;

layout Whitespace = [\ \t\n]*;

start syntax D = "d";
start syntax DS = D+;


value main(list[value] args) { if( (DS)`d <D+ Xs>` := (DS)`d d`) { return  (DS)`d <D+ Xs>` == (DS)`d d`;}}


//syntax D = "d";
//syntax Ds = "(" D* ")";
//
//public list[D] build((Ds) `(<D* ds>)`)  {
//    //iprintln(ds);    
//    return [d | D d <- ds];
//}
//
//value main(list[value] args) = build([Ds] "(ddddd)");


   
    
 