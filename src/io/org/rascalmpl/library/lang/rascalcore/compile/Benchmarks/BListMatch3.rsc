module lang::rascalcore::compile::Benchmarks::BListMatch3

value main(){
    for(i <- [1 .. 10000]){
      for([*int a, 10, *int b, 15, *int c, 20] := [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20]) x = 0;
    }
    return 0;
}