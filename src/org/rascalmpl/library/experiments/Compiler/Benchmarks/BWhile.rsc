module experiments::Compiler::Benchmarks::BWhile



value main(list[value] args){
    i = 2000000;
    while(i > 0)
    	i = i - 1;
    return 0;
}