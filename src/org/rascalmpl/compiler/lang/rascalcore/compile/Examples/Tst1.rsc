module lang::rascalcore::compile::Examples::Tst1

data B = and(B lhs, B rhs) | or(B lhs, B rhs) | t() | f();

B and(B b1, and(B b2, B b3)) = and(and(b1,b2),b3);

test bool normalizedVisit() =
  /and(_, and(_, _)) !:= visit (or(or(t(),t()),or(t(),t()))) { case or(a,b) => and(a,b) };

////////////////////////////////
//void compositeAndCnt() {
//    if( [*int _, *int _] := [1] )  {
//        fail;
//    }
//}

//void compositeOrBTLast() {
//
//    if( int _ := 3 || [*int _] := [4] )  {
//        fail;
//    }
//} 

//void compositeOrBTFirst() {
//    if( [*int _] := [4] || int _ := 3)  {
//        fail;
//    }
//}  

//value main(){ //test bool compositeImplFalse(){
//  ten = 10;
//  b = [*int _, int  _, *int _] := [1,2,3] ==> 9 > ten;
//  return !b;
//}

//value main(){ //test bool compositeImplBTLast1(){
//  ten = 10;
//  b = 9 > ten ==> [*int _, *int _] := [1,2,3];
//  return b;
//}
//
//test bool compositeImplBTLast2(){
//  ten = 10;
//  b = ten > 9 ==> [*int _, int  _, *int _] := [1,2,3];
//  return b;
//}
//
//value main(){ //test bool compositeImplBothBT(){
//  ten = 10;
//  b = [*int _, int  _, *int _] := [1,2,3] ==> ([*int _, *int _] := [4,5,6] && int _ := 4);
//  return b;
//}


//value main() = [*int _,*int _] := [1,2,3] && 7 > 2;

//value main(){ // compositeAndOr() {
//    n = 0;
//    if( ([*int _,*int _] := [1,2,3] && int _ := 3) || ([*int _,*int _] := [4,5,6] && int _ := 4) )  {
//        n = n + 1;
//        fail;
//    }
//    return n; // n == 8;
//}


//bool f1() = {{ig()}} := {{ig()}};
//
//data T1 = \int() | \void() | string(str s);
//data T2 = \int() | \void() | string(str s);
//
//test bool tstQNameInPatternInt(){
//    T1 t1 = T1::\int();
//    //T2 t2 = T2::\int();
//    return T1::\int() := t1 ;//&& T2::\int() := t2;
//}

//data IG = ig();

//test bool ignoreKeywordParameter5() = {{ig()}} := {{ig()}};
//
//data IG = ig(int x = 1);
//
//test bool ignoreKeywordParameter7() = [ig(),_] := [ig(), 2];

    
//test  bool lessequal1(int f1, int t1, int f2, int t2){
//    l1 = |noscheme:///|; l2 = |noscheme:///|;
//    return l1 == l2 ||
//           l1.offset >  l2.offset && l1.offset + l1.length <= l2.offset + l2.length ||
//           l1.offset >= l2.offset && l1.offset + l1.length <  l2.offset + l2.length
//           ? l1 <= l2 : !(l1 <= l2);
//}

//
//test bool compositeImplBothBT(){
//  ten = 10;
//  return  [*int a, int  b, *int c] := [1,2,3] ==> ([*int d, *int e] := [4,5,6] && int f := 4);
//  //return x;
//}

//test bool compositeEquivTrue(){
//  ten = 10;
//  b = [*int _, int  _, *int _] := [1,2,3] <==> ten > 9;
//  return b;
//}

//test bool compositeEquivFalse(){
//  ten = 10;
//  b = [*int _, int  _, *int _] := [1,2,3]<==> 9 > ten;
//  return !b;
//}

//test bool compositeEquivBTLast1(){
//  ten = 10;
//  b = 9 > ten <==> [*int _, int  _, *int _] := [1,2,3];
//  return !b;
//}
//
//test bool compositeEquivBTLast2(){
//  ten = 10;
//  b = ten > 9 <==> [*int _, int  _, *int _] := [1,2,3];
//  return b;
//}
//
//test bool compositeEquivBothBT(){
//  ten = 10;
//  b = [*int _, int  _, *int _] := [1,2,3] <==> ([*int _, *int _] := [4,5,6] && int _ := 4);
//  return b;
//}
//
//data D = d(int i, int j = 0);
//node n1 = d(3);
//
//value main() = !(n1 has j);


//=============
//set[int] g(set[set[int]] ns)
//    = { *x | st <- ns, {1, *int x} := st || {2, *int x} := st };