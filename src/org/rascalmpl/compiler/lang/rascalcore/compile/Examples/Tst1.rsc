module lang::rascalcore::compile::Examples::Tst1

data AnotherAndData = a();

anno list[int] AnotherAndData@l;

value main(){ //test bool anotherAnd() {
    v = a()[@l = [1,2,3]];
    return v;
//    
//    //list[list[int]] res = [];
//    //if(v@l? && [*int x,*int y] := v@l) {
//    //   res = res + [ x, y ];
//    //   fail;
//    //}
//    //return res ==  [[],
//    //                [1,2,3],
//    //                [1],
//    //                [2,3],
//    //                [1,2],
//    //                [3],
//    //                [1,2,3],
//    //                []
//    //                ];
}

//value main() //test bool testList6() 
//    = [1, [*int _, int N, *int _], 3] := [1, [10,20], 3] && N > 10;

// test bool functionTypeArgumentVariance3() {
//  value f = int (str x ) { return 1; };
//  return int (int _) _ !:= f;
//} 
