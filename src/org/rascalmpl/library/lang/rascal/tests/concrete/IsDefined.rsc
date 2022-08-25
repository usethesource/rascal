module lang::rascal::tests::concrete::IsDefined

syntax As = "a"* as;

syntax Bs = {"b" ","}* bs;

// Concrete lists

test bool isDefinedConcrete1() = (([As] "aaa")[0])?;

test bool isDefinedConcrete2() = !(([As] "aaa")[5])?;

test bool isDefinedConcrete3() = (([Bs] "b,b,b")[0])?;

test bool isDefinedConcrete4() = !(([Bs] "b,b,b")[5])?;

test bool hasConcrete1() = ([As] "aaa") has as;

test bool hasConcrete2() = !(([As] "aaa") has bs);

test bool hasConcrete3() = ([Bs] "b,b,b") has bs;

test bool hasConcrete4() = !(([Bs] "b,b,b") has as);
