module lang::rascalcore::check::Test5

data D[&T] = d1(&T n, &T kw = n);
    
int f() { D[int] x = d1(10); str m = x.kw; }
    