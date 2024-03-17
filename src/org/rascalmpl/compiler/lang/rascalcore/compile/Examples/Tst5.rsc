module lang::rascalcore::compile::Examples::Tst5


value main(){
    if (int j := 1  || int j := 2) {
        return j;
    }
    return -1;
}


//&T avoidEmpty(list[&T] l) { return  1; }
//&T avoidEmpty(list[&T] _) { throw "this should happen"; }
//
//test bool voidReturnIsNotAllowed() {
//   try {
//     avoidEmpty([]); 
//     return false;
//   } catch "this should happen":
//     return true;
//}

//data Wrapper[&SAME] = something(&SAME wrapped);
//
//@synopsis{it matters for testing that '&SAME' is the same name as in the definition of Wrapper}
//&XXXSAME getIt(Wrapper[&XXXSAME] x) = x.wrapped;
//
//value main() { //test bool hygienicGenericADT() {
//  // in the same context, we bind the same type parameter in
//  // different ways to see if nothing is leaking.
//  int i = something(1).wrapped;
//  int j = getIt(something(2));
//  int k = getIt(something(3));
//  str x = something("x").wrapped;
//  str y = getIt(something("y"));
//  
//  return i == 1 && j == 2 && k == 3
//      && x == "x" && y == "y";
//}

//import List;
//
//value main(){
//    myList = [<1,2>,<2,2>];
//    return sort(myList, bool (<int i, _>, <int j, _>) { return i < j; });
//}

//value main(){
//    if([1, int x] !:= [1]) return x;
//    return -1;
//}


