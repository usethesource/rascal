module lang::rascal::tests::basic::CompilerIssues::TypedQualifiedName


data G = g(int n) | g1();

@ignoreCompiler{Defeats the strategy in lang::rascalcore::compile::Rascal2muRascal::RascalPattern fortyped qualified name pattern}
test bool nestedfunctionInTrueBranch(){
    int f(int x) = 2 * x;
    return [ g(int n) := g1() ? f(n) : -1 ] == [ -1 ];
}

data D = d(int n) | d();

@ignoreCompiler{Defeats the strategy in lang::rascalcore::compile::Rascal2muRascal::RascalPattern for typed qualified name pattern}
test bool nestedfunctionInTrueBranch2() {
    int f(int n) = n;
    for(((d(int t) := d()) ? f(t) : 4) == 1){
        return 10;
    }
    return -1;
}

