module experiments::Compiler::Examples::Tst1

data D = d1(int n, str s = "abc");

int f(int n) {
    L = [0,1,2];
    M = ("a": 1, "b" : 2);
    X = d1(10, s="def");
    Y = d1(20);
    
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
    if([1, *n, m, 10] := [1,2,3,10], m > 2) return true;
    return false;

}

int k(int n){
    for(int i <- [0 .. n]){
        f(i);
    }
}