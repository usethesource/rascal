module experiments::Compiler::Examples::Fib

int fib(int n) = (n == 0) ? 0 : (n == 1) ? 1 : (fib(n-1) + fib(n-2));

int main(list[value] args){
    return fib(33);
}

bool testFib() = fib(25) == 75025;