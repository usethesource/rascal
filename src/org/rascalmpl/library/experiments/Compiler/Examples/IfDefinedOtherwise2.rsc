module experiments::Compiler::Examples::IfDefinedOtherwise2

data F = f() | f(int n) | g(int n) | deep(F f);
anno int F @ pos;

value main(list[value] args){
    X = f(); 
    X @ pos ?= 3;
    if(X @ pos != 3) return false;
    return true;
}