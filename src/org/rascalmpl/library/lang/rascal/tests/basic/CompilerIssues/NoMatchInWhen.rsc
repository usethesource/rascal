module lang::rascal::tests::basic::CompilerIssues::NoMatchInWhen

data AType = aparameter(str name, AType bound)
           | a()
           ;
bool asubtype(AType x, x) = true;

bool asubtype(aparameter(str pname1, AType bound1), AType r) =
    true
    when aparameter(pname1,_) !:= r ;

@ignoreCompiler{Control flow for !:= is not handled properly}
test bool aparam1() = asubtype(aparameter("A", a()), a());
