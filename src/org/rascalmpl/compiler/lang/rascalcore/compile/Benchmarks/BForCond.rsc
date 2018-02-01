module lang::rascalcore::compile::Benchmarks::BForCond

value main(){
    for(i <- [1 .. 500000], i % 2 == 1){
       x=0;
    }
    return 0;
}