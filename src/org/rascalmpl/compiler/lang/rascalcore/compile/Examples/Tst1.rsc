module lang::rascalcore::compile::Examples::Tst1


//import  lang::rascalcore::compile::Examples::Tst0;
@javaClass{org.rascalmpl.library.Prelude}
public java bool arbBool();
value main() = true;

//data B = and(B lhs, B rhs) | or(B lhs, B rhs) | t() | f();