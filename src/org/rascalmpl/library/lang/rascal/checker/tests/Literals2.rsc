module lang::rascal::checker::tests::Literals2

// All of these should generate type errors...
public int testfun1() {
    int x = "1";
    str y = false;
    bool b = 3.5;
    real r = |file:///dev/null|;
    loc l = $2010-12-01;
    datetime dt = 1;
    
    return "whatever";
}
