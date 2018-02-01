module lang::rascalcore::compile::Benchmarks::BFac

int fac(int n) = (n <= 0) ? 01 : n * fac(n-1);

value main(){
    for(i <- [1 .. 50000]){
       x = fac(20);
    }
    return 0;
}