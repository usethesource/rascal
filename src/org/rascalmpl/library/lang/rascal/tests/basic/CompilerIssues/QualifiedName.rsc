module lang::rascal::tests::basic::CompilerIssues::QualifiedName

data G = g(int n) | g1();

@ignore{Defeats the compiler's strategy in lang::rascalcore::compile::Rascal2muRascal::RascalPattern for qualified name pattern}
test bool nestedfunctionInTrueBranch(){
    int f(int x) = 2 * x;
    return [ g(n) := g1() ? f(n) : -1 ] == [ -1 ];
}