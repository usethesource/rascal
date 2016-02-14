@bootstrapParser
module experiments::Compiler::Examples::Tst1


data D = d1(int n);
 
value main() = d1(10);

//data ColoredTree = leaf(int N)      /*1*/
//                 | red(ColoredTree left, ColoredTree right) 
//                 | black(ColoredTree left, ColoredTree right);
//
//public ColoredTree  rb = red(black(leaf(1), red(leaf(2),leaf(3))), black(leaf(3), leaf(4)));
//          
//// Count the number of red nodes
//          
//int cntRed(ColoredTree t){
//   int c = 0;
//   visit(t) {
//     case red(_,_): c = c + 1;      /*2*/
//   };
//   return c;
//}
//
//value main() = rb; // cntRed(rb); //== 2;

//value main() = [a] := [123];

//value main(){
//    //for(i <- [1 .. 2]){
//      for([*int a] := [0,1,2,3,4,5,6,7,8,9]) x = 0;
//   // }
//    return 0;
//}


//data D = d1(int n, str s) | d2(str s, bool b) | d3(list[int] l, list[int] r); 
//
//value main() =  /2 := [10, d1(2, "a"), 11];

//value main() = [0..3];

//value main() { int n = 0; if([*int x,*int y] := [1,2,3,4,5]) { n += 1; } n; }


//import List;
//import Set;

//int g(int n) = 2 * n;

//int f(int n) = g(n) + 1;

//value main() = f(13);


//value main() = sort([1]);

//value main(){
//    bool similar(int a, int b) = a % 5 == b % 5;
//
//    return group({1,2,3}, similar) == {{1}, {2}, {3}};
//}   