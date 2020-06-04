module lang::rascalcore::compile::Examples::Tst1

data F = f(F left, F right) | g(int N);

value main() //test bool descendant33() 
    = [1, [F] /f(/g(2), F _), 3] := [1, f(g(1),f(g(2),g(3))), 3];

//value main() // test bool descendant36() 
//    = [1, /g(int N1), 3] := [1, f(g(1),f(g(2),g(3))), 3] && N1 == 1;

//value main() //test bool descendant5() 
//    = /int N := [1,2,3,2] && N > 2;

//value main() // test bool descendant2() 
//    =  !/int x := true;
    
//value main() // test bool testSimple1() 
//    = int i <- [1,4] && int k := i && k >= 4;
    
//bool f(int n) = n == 1;
//bool f("a") = true;
//default bool f(value v) = true;
//
//value main() = f("b");

//data DATA = a() | b() | c() | d() |
//            e(int N) | f(list[DATA] L) | f(set[DATA] S)| s(set[DATA] S)
//          | g(int N) |h(int N)| f(DATA left, DATA right);
//
//value main() // test bool matchListSet7() 
//    = ([a(), f({a(), DATA X6})] := [a(), f({a(), c()})]);// && (X6 == c());


//// int f (int n) { 
////  switch(n) { 
////      case 0: return 1; 
////      case 1: return 1; 
////      case int m: return m*(m-1); 
////      default: return -1;
////  } 
////}
//
//
////value main() // test bool testSet13() 
////= {1, {*int _, int _, *int _}, 3} := {1, {1,2,3}, 3};
//
////value main() //test bool testList6() 
////    = [1, [*int A, int N, *int B], 3] := [1, [10,20], 3] && N > 10;
//
////value main() //test bool testList5() 
////    = [*int X, int N, *int Y] := [1,2,3,2] && N > 1;
//
////value main() //test bool testList3() 
////    = ([*int _, int N, *int _] := [1,2,3,4]) && 
////                        ([*int _, int M, *int _] := [3,4]) && (N > M) &&  (N == 4);
//
//
////value main(){ //test bool compositeAndBTLast() {
////    ten = 10;
////    return ten > 9 && [*int _, int  _, *int _] := [1,2,3];
////}
//
////value main() //test bool testList3() 
////    = ([*int _, int N, *int _] := [1,2,3,4]) && 
////      ([*int _, int M, *int _] := [3,4]) && (N > M) &&  (N == 4);
//
//
////value main() //test bool testList5() 
////    = [*int _, int N, *int _]:= [1,2,3,2] && N > 1;
////
//
////value main() //test bool testSet12() 
////    = {*int _, int N}:= {1,2,3,2} && N == 3;
//
////value main(){ //test bool simpler() {
////   bool pred(int a, int b) = !(a == 0 || b == 0);
////   l = [1,1,1];
////   while ([*pre,a,b,*pst] := l, pred(a, b)) {
////      l = [*pre,a,0,b,*pst];
////   }
////   return l;// == [1,0,1,0,1];
////}
//
//
////----------------------------
////int fvarargs(bool _, int _, str _, value _...) = 2;
//
////public int ModVar42 = 42;
////
////test bool matchModuleVar1() = ModVar42 := 42;
////
////public bool hasDuplicateElement(list[int] L)
////{
////    switch(L){
////    
////    case [*int _, int I, *int _, int J, *int _]:
////        if(I == J){
////            return true;
////        } else {
////            fail;
////        }
////    default:
////        return false;
////      }
////}
////
////test bool transEq2(value x, value y, value z) = (x := y && y := z);
////
////test bool allEqualValuesMatch(node a, node b) = a == b ==> a := b;
//
////public loc toLocation(str s) = (/<car:.*>\:\/\/<cdr:.*>/ := s) ? |<car>://<cdr>| : |cwd:///<s>|;
//
////@javaClass{org.rascalmpl.library.Prelude}
////public java str toLowerCase(str s);
////test bool tstToLowerCase(str S) = /[A-Z]/ !:= toLowerCase(S);
//
////import util::Math;
////import List;
////import Set;
//
////public set[set[&T]] power(set[&T] st)
////{
////  // the power set of a set of size n has 2^n-1 elements 
////  // so we enumerate the numbers 0..2^n-1
////  // if the nth bit of a number i is 1 then
////  // the nth element of the set should be in the
////  // ith subset 
////  //stl = [*st];
////  i = 0;
////  res = while(i < 10) {
////    //j = i;
////    //elIndex = 0;
////    sub = [];/*while(j > 0) {;
////      if(j mod 2 == 1) {
////        append stl[elIndex];
////      }
////    }*/
////    append {};//{*sub};
////    //i+=1;
////  }
////  return {}; //{*res};
////}
//
////public bool isDuo1(list[int] L)
////{
////    switch(L){
////    case [*int L1, *int L2]:
////        if(L1 == L2){
////            return true;
////        } else {
////            fail;
////        }
////    default:
////        return false;
////      }
////}
////
////value main() = isDuo1([1,1]);
////
//
//
//// Lists:
////value main(){
////      bool flag = false;
////      list[int] lst = [1,2,3,4];
////      //el = 3;
////      //x = ((el > 3 && !flag) ? {flag=true; [el];} : [5]);
////      //return x;
////      return [ ((el > 3 && !flag) ? {flag=true; [el];} : [5]) | int el <- lst ];
////      //return [ ((el > 3 && !flag) ? {flag=true; [el];} : [5]) | int el <- lst ];
////}
//
////test bool dtstDifference(list[int] lst) {
////      
////    for(int elem <- lst) {
////        bool deleted = false;
////        lhs = lst - [elem];
////        rhs = [ *( (elem == el && !deleted) ? { deleted = true; []; } : [ el ]) | int el <- lst ];
////    }
////    return true;
////}
////test bool compositeImplCntBTLast2() {
////    n = 0;
////    ten = 10;
////    if( ten > 9 ==> [*int _, int  _, *int _] := [1,2,3] )  {
////        n = n + 1;
////        fail;
////    }
////    return n == 3;
////}
////
////test bool compositeImplBothBTCnt() {
////    n = 0;
////    if( [*int _, int  _, *int _] := [1,2,3] ==> [*int _, int  _, *int _] := [4, 5, 6] )  {
////        n = n + 1;
////        fail;
////    }
////    return n == 3;
////}
////
////test bool compositeEquivCnt() {
////    n = 0;
////    ten = 10;
////    if( [*int _, int  _, *int _] := [1,2,3] <==> ten > 9 )  {
////        n = n + 1;
////        fail;
////    }
////    return n == 3;
////}
//
////value main(){ //test bool compositeImplCnt() {
////    n = 0;
////    ten = 10;
////    if( [*int _, int  _, *int _] := [1,2,3] ==> ten > 9 )  {
////        n = n + 1;
////        fail;
////    }
////    return n;// == 1;
////}
//
//
//
//
//
////////////
////value main(){ //test bool compositeEquivBTLast1(){
////  ten = 10;
////  b = 9 > ten <==> [*int _, int  _, *int _] := [1,2,3];
////  return !b;
////}
//
//
////import String;
//
////test bool top2(loc x) = toLocation(x.uri) == x.top;
//
////value main() = toLocation(|project://rascal/src/org/rascalmpl/library/List.rsc|.uri);
//
////str toLowerCase(str s) = s;
////test bool tstToLowerCase(str S) = /[A-Z]/ !:= toLowerCase(S);
//
//
////data B = and(B lhs, B rhs) | or(B lhs, B rhs) | t() | f();
////
////B and(B b1, and(B b2, B b3)) = and(and(b1,b2),b3);
////
////value main() = //test bool normalizedVisit() =
////  /and(_, and(_, _)) !:= visit (or(or(t(),t()),or(t(),t()))) { case or(a,b) => and(a,b) };
//
//
////value main(){
////    return /and(_,_) := t();
////}
//////////////////////////////////
////void compositeAndCnt() {
////    if( [*int _, *int _] := [1] )  {
////        fail;
////    }
////}
//
////void compositeOrBTLast() {
////
////    if( int _ := 3 || [*int _] := [4] )  {
////        fail;
////    }
////} 
//
////void compositeOrBTFirst() {
////    if( [*int _] := [4] || int _ := 3)  {
////        fail;
////    }
////}  
//
////value main(){ //test bool compositeImplFalse(){
////  ten = 10;
////  b = [*int _, int  _, *int _] := [1,2,3] ==> 9 > ten;
////  return !b;
////}
//
////value main(){ //test bool compositeImplBTLast1(){
////  ten = 10;
////  b = 9 > ten ==> [*int _, *int _] := [1,2,3];
////  return b;
////}
////
//
//
////test bool compositeImplBTLast2(){
////  ten = 10;
////  b = ten > 9 ==> [*int x, int  y, *int z] := [1,2,3];
////  return b;
////}
//
////
////value main(){ //test bool compositeImplBothBT(){
////  ten = 10;
////  b = [*int _, int  _, *int _] := [1,2,3] ==> ([*int _, *int _] := [4,5,6] && int _ := 4);
////  return b;
////}
//
//
////value main() = [*int _,*int _] := [1,2,3] && 7 > 2;
//
////value main(){ // compositeAndOr() {
////    n = 0;
////    if( ([*int _,*int _] := [1,2,3] && int _ := 3) || ([*int _,*int _] := [4,5,6] && int _ := 4) )  {
////        n = n + 1;
////        fail;
////    }
////    return n; // n == 8;
////}
//
//
////bool f1() = {{ig()}} := {{ig()}};
////
////data T1 = \int() | \void() | string(str s);
////data T2 = \int() | \void() | string(str s);
////
////test bool tstQNameInPatternInt(){
////    T1 t1 = T1::\int();
////    //T2 t2 = T2::\int();
////    return T1::\int() := t1 ;//&& T2::\int() := t2;
////}
//
////data IG = ig();
//
////test bool ignoreKeywordParameter5() = {{ig()}} := {{ig()}};
////
////data IG = ig(int x = 1);
////
////test bool ignoreKeywordParameter7() = [ig(),_] := [ig(), 2];
//
//    
////test  bool lessequal1(int f1, int t1, int f2, int t2){
////    l1 = |noscheme:///|; l2 = |noscheme:///|;
////    return l1 == l2 ||
////           l1.offset >  l2.offset && l1.offset + l1.length <= l2.offset + l2.length ||
////           l1.offset >= l2.offset && l1.offset + l1.length <  l2.offset + l2.length
////           ? l1 <= l2 : !(l1 <= l2);
////}
//
////
////test bool compositeImplBothBT(){
////  ten = 10;
////  b = [*int x, *int y] := [1] ==> ([*int p] := [4] && 2>3);
////  return b;
////}
//
////test bool compositeEquivTrue(){
////  ten = 10;
////  b = [*int _, int  _, *int _] := [1,2,3] <==> ten > 9;
////  return b;
////}
//
////test bool compositeEquivFalse(){
////  ten = 10;
////  b = [*int _, int  _, *int _] := [1,2,3]<==> 9 > ten;
////  return !b;
////}
//
////test bool compositeEquivBTLast1(){
////  ten = 10;
////  b = 9 > ten <==> [*int _, int  _, *int _] := [1,2,3];
////  return !b;
////}
////
////test bool compositeEquivBTLast2(){
////  ten = 10;
////  b = ten > 9 <==> [*int _, int  _, *int _] := [1,2,3];
////  return b;
////}
////
////test bool compositeEquivBothBT(){
////  ten = 10;
////  b = [*int _, int  _, *int _] := [1,2,3] <==> ([*int _, *int _] := [4,5,6] && int _ := 4);
////  return b;
////}
////
////data D = d(int i, int j = 0);
////node n1 = d(3);
////
////value main() = !(n1 has j);
//
//
////=============
////set[int] g(set[set[int]] ns)
////    = { *x | st <- ns, {1, *int x} := st || {2, *int x} := st };