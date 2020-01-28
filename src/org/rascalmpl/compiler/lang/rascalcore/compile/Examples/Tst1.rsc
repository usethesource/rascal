module lang::rascalcore::compile::Examples::Tst1



void foo(node n) {
   if (/s:""() := n) return;
}
//import List;
//bool isEven(int a) = a mod 2 == 0;
//
//list[int] takeEven(list[int] L) = (isEmpty(L) || isEven(elementAt(L,0))) ? [] : [1,2,3];
                                                                
//   value main() {
//    n = 0;
//    if( int p := 3 || ([*int x,*int y] := [4,5,6]) )  {
//        n = n + 1;
//        fail;
//    }
//    return n == 5;
//}                                   
    
//value main() = all(int x <- [1..10], x > 0 && x < 10);

//value main() = [ X | int X <- [] ] == [];

//value main() = any(<int X, int Y> <- {<1,10>,<30,3>,<2,20>}, X > Y);

//public rel[&T0, &T1, &T2] complement(rel[&T0, &T1, &T2] R)
//{
//  return {<V0, V1, V2> | &T0 V0 <- R<0>, &T1 V1 <- R<1>,  &T2 V2 <- R<2>, <V0, V1, V2> notin R};
//}

//data TREE = i(int N) | f(TREE a,TREE b) | g(TREE a, TREE b);
        
//value main() = [ X | /int X <- f(i(1),g(i(2),i(3))) ] == [1,2,3];

//data PAIR = a1() | b1() | c1() | d1() | pair(PAIR q1, PAIR q2) | s1(set[PAIR] S) | l1(list[PAIR] L);

//value main() = {pair(a1(), b1())} := {pair(a1(), b1())};

//value main() = {PAIR D, pair(D, b1())} := {pair(a1(),b1()), a1()} && D == a1();

      
//value main()  = {PAIR D, pair(D, b1())} := {pair(a1(),b1()), a1()};// && D == a1();

//value main() = {*int X, *int Y} := {1} && ((X == {} && Y == {1}) || (X == {1} && Y == {}));   

//value main() = [*int X] := [1] && (X == [] || 1 == 2);

//value main() = {*int X, int Y} := {1};// && X == {} && Y == 1;

//value main() {
// ten = 10;
//    b = [*int x] := [1] && 9 > ten;
//    return !b;
//}