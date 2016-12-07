module experiments::Compiler::Examples::Tst1

int f(int n) = g(n);

int g(int n) = ()[1];

value main(){
    return f(10);
}