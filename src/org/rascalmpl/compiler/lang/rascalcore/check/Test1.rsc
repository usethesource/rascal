module lang::rascalcore::check::Test1 
 
//value main(){
//    n1 = "f"(1,kw=3);
//    b1 = "f"(1,kw=3) := "f"(1,kw=3);
//}

data F = f() | g();
    
int func(f()) = 1;
int func(g()) = 2;

int apply(int (F) theFun, F theArg) = theFun(theArg);

int main2() = apply(func, f());