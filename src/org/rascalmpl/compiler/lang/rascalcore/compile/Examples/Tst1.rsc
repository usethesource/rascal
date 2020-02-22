module lang::rascalcore::compile::Examples::Tst1

//import String;
//import ParseTree;



lexical Example = ([A-Z] head [a-z]* tail)+ words;

test bool staticFieldProjectType() = list[[A-Z]] _ := [ w.head |  w <- t.words ]
  when Example t := [Example] "CamelCaseBaseFeest";
  
list[![]] f() = [ w.head |  w <- t.words ]
   when Example t := [Example] "CamelCaseBaseFeest";

//
//test bool characterClassSubType() {
//  [A-Za-z] tmp = (Example) `A`.head; // assignment into bigger class: always allowed
//  
//  if ([A-Z] _ := tmp) { // binding to smaller class should match if it fits
//    return true;
//  }
//  
//  return false;
//}

//list[![]] characters(str x) = [char(i) | i <- chars(x)]; 