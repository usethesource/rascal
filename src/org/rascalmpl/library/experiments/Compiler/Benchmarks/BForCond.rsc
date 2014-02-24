module experiments::Compiler::Benchmarks::BForCond

value main(list[value] args){
    for(i <- [1 .. 500000], i % 2 == 1){
       x=0;
    }
    return 0;
}