module lang::rascal::tests::basic::CompilerIssues::TemplateInConditional

data D = d1(int nfield) | d2(str sfield);

@ignoreCompiler{x.nfield if moved before loop when x may not satisfy x is d1}
test bool templateInConditional()
    =  {x is d1 ? "<x.nfield>" : "x" | x <- [d2("abc")]} == {};
