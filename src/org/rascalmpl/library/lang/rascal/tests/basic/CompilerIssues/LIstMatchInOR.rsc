module lang::rascal::tests::basic::CompilerIssues::LIstMatchInOR

data Symbol
     = strt()
     | par(list[Symbol] parameters) // <6>>
     ;
    
 @ignoreCompiler{Generates incorrect code}
test bool listMatchInOR() {
    bool unquotable(Symbol x) 
     =    par([*Symbol _]) := x
       || strt() := x
       ;
    return unquotable(\strt());
}