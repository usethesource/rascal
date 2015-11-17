module experiments::Compiler::Examples::Fac

import String;
  
int fac(int n) = (n <= 1) ? 1 : n * fac(n-1);
 
int main(str n = "24"){
    return fac(toInt(n));
}  

test bool tfac() = fac(24) == 620448401733239439360000;
