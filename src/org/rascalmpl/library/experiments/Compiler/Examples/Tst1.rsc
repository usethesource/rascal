module experiments::Compiler::Examples::Tst1


int f(int n) {
    if(n > 0){
        return n * 10;
    } else {
        return n * 20;
    }
}

int g (int m){
    return f(m);
}

bool h(){
    return [1, *n, 10] := [1,2,3,10];

}