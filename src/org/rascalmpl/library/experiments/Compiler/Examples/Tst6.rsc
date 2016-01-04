module experiments::Compiler::Examples::Tst6

data D = d1() | d2();

int f("abc", int n) = 1;

default int f(str name, value arg) = -1;

int wrapF(str name, value arg) = f(name, arg);

//value main() = f("abc", 5);
value main(){
   
    int f(d1(), int i) = i;
     default int f(D d, int i) = -i;

    return f(d2(), 5);
}



//test bool tcc3() = translateConstantCall("value", [1, 2]) == 2;

//test bool tcc1() = translateConstantCall("value", []) == 0;
//test bool tcc2() = translateConstantCall("value", [1]) == 1;
//test bool tcc3() = translateConstantCall("value", [1, 2]) == 2;
//test bool tcc4() = translateConstantCall("xxx", []) == -1;
//test bool tcc4() = translateConstantCall("xxx", [1]) == -1;
