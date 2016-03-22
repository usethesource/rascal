module experiments::Compiler::Examples::Fac

syntax A = "a";
syntax As = A*;

import String;
   
int fac(int n) = (n <= 1) ? 1 : n * fac(n-1);
 
//int main(str n = "24"){
//    return fac(toInt(n));
//} 

int main() = fac(10); 

//test bool tfac() = fac(24) == 620448401733239439360000;

data D = d1(int n) | d2(str s);

A getA() = [A] "a";

As getAs(int n) = [As] "<for(int i <-[0..n]){>a<}>";

int size(As as) = size("<as>");

int mulKW(int n, int kw = 10) = n * kw;
