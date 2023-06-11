module lang::rascalcore::compile::Examples::Tst3

import List;
//data QueryResult;
//alias Corpus = list[str];
//
//public void showUsageCounts(Corpus corpus, lrel[str p, str v, QueryResult qr] res) {
//    mr = ( p : size([ e | <p,_,e> <- res ]) | p <- corpus );
//    //for (p <- sort([p | str p <- mr<0>])) println("<p>:<mr[p]>");
//}

//value main(){
//    res = [<1,2>, <2,4>, <2,40>, <3, 9>,<3,90>];
//    corpus = {1,2,3};
//    mr = (p : size([ e | <p,e> <- res ]) | int p <- corpus);
//    return mr;
//   
//
//}

value main(){
    return [x | x <- [<1,2>,<3,4>,<5,6>], /2 := x, /3:= x];
    
    println("<h@\loc?>, <(args[0])@\loc?>,  <(args[0].args[4])@\loc?>, <(args[0].args[4].args[0])@\loc?>");
}

loc moduleScope = |unknown:///|;

void setModuleScope(loc l){
    moduleScope = l;
}  

loc getModuleScope()
    = moduleScope;


//value main(){
//    x = {<1, true>};
//    x += {<2, false, "b">};
//    return  x;
//}
//syntax A = "a";
////import ParseTree;
//
//data Tree;
//data Symbol;
//
//data Production;
//
//@javaClass{org.rascalmpl.library.Prelude}
//public java &T (value input, loc origin) parser(type[&T] grammar, bool allowAmbiguity=false, bool hasSideEffects=false, bool firstAmbiguity=false, set[Tree(Tree)] filters={}); 
//
//public &T<:Tree parse(type[&T<:Tree] begin, str input, loc origin, bool allowAmbiguity=false, bool hasSideEffects=false, set[Tree(Tree)] filters={})
//  = parser(begin, allowAmbiguity=allowAmbiguity, hasSideEffects=hasSideEffects, filters=filters)(input, origin);
//
//
//Tree doParseFragment(Symbol sym, map[Symbol, Production] rules) {
//   return parse(type(sym, rules),  "a", |todo:///|);
//}
//
//value main(){ 
//    return doParseFragment(#A.symbol, #A.definitions);
//}

//data x = x() | x(int x);
//
////x x(int n) { return x(2 * n); }
////
//x x = x(3);
//
//value main(){
//    return x.x;
//}


//test bool comprehension2() 
//    = (k:k*k | k <- [1..5]) == (1:1,2:4,3:9,4:16);

//test bool matchNestedList14() {
//     if([*list[int] L] := [[1,2]] && L == [[1,2,3]]){
//         return false;
//      } else {
//         return true;
//     }
//}

//void main(){
//    pcfgVal = [1];
//    if([int pcfg] := pcfgVal){ 
//        try {   
//            x =10;
//        } catch _: {
//            throw "abc";
//        }
//    } else if(pcfgVal == []){
//        return;
//    } else {
//        throw "inconsistent";
//    }
//}
//int main(){ //test bool matchListTuples8() {
//    if([int n] := [2]){
//        return 0;
//    } else {
//        return 1;
//    }
//}

//test bool matchSetLists8() {
//    if({[1, int n, 3]} := {[1, 2, 3]} && n == -2){
//        return false;
//    } else {
//        return true;
//    }
//}

// Same as interpreted
//data D = d(str s) | d(int n) | d();
//
//@doc{triggers issue #1234}
//test bool constructorDynamicMatch() {
//  value x = 1;
//  
//  // Due to issue #1234, `d(x)` would throw a MatchFailed() here */
//  return d(int i) := d(x) && i == 1;
//}


//data D = d(int n) | d(str s) | dl(list[D] ds);
//
//bool containsNoDstr(D t)
//    =  !(/d(str _) := t);
//    
////bool containsNoDstr(D t)
////    =  (/d(str _) := t) ? false : true;
//
//value main() = containsNoDstr(dl([d(1), d("a")]));
