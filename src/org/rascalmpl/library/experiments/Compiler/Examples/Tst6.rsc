module experiments::Compiler::Examples::Tst6

data D = d(int n);

anno int D@a;

value main6(){
    return d(1)[@a=12] == d(1)[@a=13];
}