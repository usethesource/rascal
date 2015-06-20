module experiments::Compiler::Examples::Fac

int fac(int n) = (n <= 1) ? 1 : n * fac(n-1);
 
int main(list[value] args){
    return fac(24);
}  

test bool tfac() = fac(24) == 620448401733239439360000;