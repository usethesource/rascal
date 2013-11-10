module experiments::Compiler::Benchmarks::BFor

value main(list[value] args){
    for(i <- [1 .. 5000000]){
       x=0;
    }
    return 0;
}