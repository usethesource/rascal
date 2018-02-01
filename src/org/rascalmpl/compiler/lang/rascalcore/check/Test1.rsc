module lang::rascalcore::check::Test1




start syntax A = "a";

data Tree;

@javaClass{org.rascalmpl.library.Prelude}
@reflect{Uses information about syntax definitions at call site}
public java &T<:Tree parse(type[&T<:Tree] begin, str input, bool allowAmbiguity=false);

public A parse(str src) = parse(#start[A], src).top;