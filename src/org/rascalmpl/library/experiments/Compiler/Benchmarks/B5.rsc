module experiments::Compiler::Benchmarks::B5

value main(list[value] args){
    for(i <- [1 .. 1000]){
      res = [];
      for([*int a, *int b, *int c] := [0,1,2,3,4,5,6,7,8,9]) res = res + [[a,b,c]];
    }
    return 0;
}