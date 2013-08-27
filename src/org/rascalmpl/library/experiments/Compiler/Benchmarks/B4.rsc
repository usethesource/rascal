module experiments::Compiler::Benchmarks::B4

int fib(int n) = (n == 0) ? 0 : (n == 1) ? 1 : (fib(n-1) + fib(n-2));

value main(list[value] args){
    for(i <- [1 .. 100]){
       x= fib(15);
    }
    return 0;
}