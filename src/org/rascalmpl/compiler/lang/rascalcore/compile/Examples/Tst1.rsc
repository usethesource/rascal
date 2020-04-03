module lang::rascalcore::compile::Examples::Tst1

import Node;
import ParseTree;

syntax A = "a";
syntax B = "b" | [b]; // ambiguous on purpose

// we only allow declarations on Tree for now, for lack of a syntax to declare them on non-terminals.
data Tree(str y = "y");
// to be able to access the kw param feature, you have to remove the loc annotation first (until we remove annotations):
&T<:Tree get(&T<:Tree e) = delAnnotation(e, "loc");


test bool charTest() = get(char(32)).y == "y";

//test bool charTest2() = get(char(32))[y="z"].y == "z";
//
//test bool charTest3() = get(char(32))[y="z"].character == 32;
//
//test bool charTest4() {
//   t = get(char(32));
//   t.y = "z";
//   return t.y == "z";
//}


//import ParseTree;
//import String;


// 
//&L strange(&L <: num arg1, &R <: &L arg2){
//  return arg2;
//}
//
//value main() = strange(3, "abc");


//set[str] f(){
//    res = {};
//    res += 1;
//   
//    return res;
//}

//test bool compositeAndBothBTCnt() {
//    n = 0;
//    if( [*int _, int  _, *int _] := [1,2,3] && [*int _, int  _, *int _] := [4, 5, 6] )  {
//        n = n + 1;
//        fail;
//    }
//    return n == 9;
//}
//
//int g(n) { n = 10; return n; }
//
//int h(int x = 1, int y = x + x) = 0;
//
//data D(int nn = -3, int m = nn + nn);
//
//tuple[int,int] tp() = <1,2>;
//
//value k(){
//    <x, y> = tp();
//    y += 1;
//    return y;
//}
//
//
//
//void l(){
//    M += 1;
//}
//
//int M = 3;
//
//data E = e(int n) | f(str s);
//value m(E x){
//    switch(x){
//        case f(n): return n;
//        case e(n): return n + 1;
//    }
//    return 0;
//}
//
//value main(){
//    for(s:str x <- {"a", "b", "c"}){
//    ;}
//    s += 1;
//    return s + 1;
//
//}

//void removeIdPairs(rel[int,int] inp){
//   res = inp;
//   if ( { < a, b >, < b, b >, *c } := res ) 
//        res = { *c, < a, b > };
//}
//
//public list[&T] dup(list[&T] lst) {
//  done = {};
//  return for (e <- lst, e notin done) {
//    done = done + {e};
//    append e;
//  }
//}
//
//int eval1(int main, list[int] args) {
//  penv = [ f | f <-args ];
//  f = penv[main];
//  return 0; 
//}
//
//void testAppend3() {
//    res1 = 1;
//    res1 = res1 + 1;
//}
//
//void f(){
//    kind = "syntax";
//    if(true)
//        kind = "start" + kind;
//}

//void g(){
//    X = "abc";
//}
//
//int X = 0;

//
//void h(){
//    result = 0.5;
//    result = (result | r + it | r <- [1.5..10.5]);
//}
