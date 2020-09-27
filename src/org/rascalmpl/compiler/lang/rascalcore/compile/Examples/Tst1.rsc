module lang::rascalcore::compile::Examples::Tst1

//import Type;

import  lang::rascalcore::compile::Examples::Tst2;
//   
//value main() // test bool domainRl02() 
//    = domainR([<1,10>,<2,20>], [2]) == [<2,20>];
    
 
//test bool nonComparabilityImpliesNonEq(value x, value y) = !comparable(typeOf(x),typeOf(y)) ==> !eq(x,y);    


//// Make a triple of lists from a list of triples.
//public tuple[list[&T],list[&U],list[&V]] unzip(list[tuple[&T,&U,&V]] lst) =
//    <[t | <t,_,_> <- lst], [u | <_,u,_> <- lst], [w | <_,_,w> <- lst]>;
//    
//public tuple[list[&T],list[&U]] unzip(list[tuple[&T,&U]] lst) =
//    <[t | <t,_> <- lst], [u | <_,u> <- lst]>;
    
test bool tstUnzip2(list[tuple[&A, &B]] L) = unzip(L) == <[a | <a,_> <- L], [b | <_,b> <- L]>;

value main() = tstUnzip2([]);