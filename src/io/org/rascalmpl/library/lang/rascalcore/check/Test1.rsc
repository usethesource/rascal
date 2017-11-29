module lang::rascalcore::check::Test1

data F = f3();// | f3(int n) | g(int n) | deep(F f);
anno int F@pos;

test bool isDefinedAnno5(){
    X = f3(); 
    X@pos ? 0 += 1;
    return X@pos == 1;
}

test bool isDefinedAnno7(){
    Y = f3(); 
    Y@pos ?= 3;
    return Y@pos == 3;
}