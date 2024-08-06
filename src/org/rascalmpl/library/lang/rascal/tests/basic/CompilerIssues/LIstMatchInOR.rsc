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


data Symbol
    = sym(str name) 
    | label(str name, Symbol sym)
    ;

// Simplified version from lang::rascal::grammar::analyze::Dependency
set[Symbol] symbolDependenciesOld(set[Symbol] sses) =
  { from | s <- sses, bprintln(s), (label(_,Symbol from) := s || Symbol from := s)};

// Test for original version (with probably unintended result)
@ignoreCompiler{Generates incorrect code}
test bool symbolDependenciesOld1()
= symbolDependenciesOld({sym("a"), label("x", sym("b"))}) == {sym("a"), sym("b"), label("x", sym("b"))};

// Rewritten version with intended output, compiler behaves well on it
set[Symbol] symbolDependenciesNew(set[Symbol] sses) =
  { from | s <- sses, bprintln(s),  Symbol from := ((label(_,Symbol f) := s) ? f : s) };

test bool symbolDependenciesNew1()
= symbolDependenciesNew({sym("a"), label("x", sym("b"))}) == {sym("a"), sym("b")};
