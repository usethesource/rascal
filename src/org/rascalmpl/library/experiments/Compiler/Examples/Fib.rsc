module experiments::Compiler::Examples::Fib
   
@memo
int fib(int n) = (n == 0) ? 0 : (n == 1) ? 1 : (fib(n-1) + fib(n-2));

int main(){
    return fib(30);
}

bool testFib() = fib(25) == 75025;