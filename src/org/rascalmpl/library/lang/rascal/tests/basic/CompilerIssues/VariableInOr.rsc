module lang::rascal::tests::basic::CompilerIssues::VariableInOr

data D = d1(int n) | label(D d);

@ignoreCompiler{Generates erroneous Java code}
int f(D d) = e.n
    when label(D e) := d || e := d;
    
@ignoreCompiler{Generates erroneous Java code}
test bool testF() = f(label(d1(10))) == 10;