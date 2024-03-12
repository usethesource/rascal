module lang::rascalcore::compile::Examples::Tst5


//value main(){
//    if([1, int x] !:= [1]) return x;
//    return -1;
//}

//import List;

//void warningExample() {
//    mylist = [1,2,3];
//    myrel = { <1,"a">,<1,"b">,<2,"c">,<2,"d">,<2,"e">,<3,"f"> };
//    mymap = ( p : [e | <p,e> <- myrel] | int p <- mylist );
//}


//import util::Maybe;
//
// &T testFunction(Maybe[&T] _, &T x) = x;
// 
// value main() = testFunction(just(3), 5);
// 
//value edits(value x: ![]) {return x;}
//
//value main(){
//    return ![] := 
//            [1];
//}


//MH
public rel[str s, int n] filterRelBroken(rel[str s, int n] inRel, set[str] relFilter) {
    return { t | t:< s, n > <- inRel, s in relFilter };
}