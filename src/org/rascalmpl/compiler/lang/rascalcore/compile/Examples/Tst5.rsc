module lang::rascalcore::compile::Examples::Tst5

import util::Maybe;

 &T testFunction(Maybe[&T] _, &T x) = x;
 
 value main() = testFunction(just(3), 5);
 
//value edits(value x: ![]) {return x;}
//
//value main(){
//    return ![] := 
//            [1];
//}


//MH
//public rel[str s, int n] filterRelBroken(rel[str s, int n] inRel, set[str] relFilter) {
//    return { t | t:< s, n > <- inRel, s in relFilter };
//}