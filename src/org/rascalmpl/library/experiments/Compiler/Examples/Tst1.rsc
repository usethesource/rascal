module experiments::Compiler::Examples::Tst1


int f(int n) {
    if(n > 0){
        int z = n + 3;
        return n * 10;
    } else {
        return n * 20;
    }
}

int g (int m){
    z = m + f(m);
    y = z * 2;
    return f(m);
}

bool h(){
    return [1, *n, 10] := [1,2,3,10];

}