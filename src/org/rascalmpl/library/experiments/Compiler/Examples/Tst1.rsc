module experiments::Compiler::Examples::Tst1

int fac(int n) = n <= 1 ? 1 : n * fac(n-1);


value work() { 
    i = 0;
    
    while(i < 100){
        fac(50);
        i += 1;
     }
     return i;
}

value main() = work();