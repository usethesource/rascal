module lang::rascalcore::compile::Benchmarks::BFib

int fib(int n) = (n == 0) ? 0 : (n == 1) ? 1 : (fib(n-1) + fib(n-2));

value main(){
    for(i <- [1 .. 100]){
       x= fib(20);
    }
    return 0;
}