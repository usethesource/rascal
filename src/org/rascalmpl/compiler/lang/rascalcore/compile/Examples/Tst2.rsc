module lang::rascalcore::compile::Examples::Tst2

int f13(int n, str s = "") = n when s == "";
int f13(int n, str s = "") = -n when s != "";

value main() //test bool when1() 
    = f13(10);// == 10;
    
//test bool when2() = f13(10, s="a") == -10;
