module lang::rascalcore::compile::Examples::Tst2

data D = d(int n) | d(str s);

value main(){
    x = d(3);
    return x;
}