module lang::rascalcore::compile::Examples::Tst5

void main(){
    previous = "non empty";
     msg = "<for (x <- ["a", "b", "c"]) { continue;><x>
           '<}>";
}

//MH
//public void showUsageCounts(Corpus corpus, lrel[str p, str v, QueryResult qr] res) {
//    mr = ( p : size([ e | <p,_,e> <- res ]) | p <- corpus );
//    for (p <- sort([p | str p <- mr<0>])) println("<p>:<mr[p]>");
//}

//value main(){
//    if (int j := 1  || int j := 2) {
//        return j;
//    }
//    return -1;
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


