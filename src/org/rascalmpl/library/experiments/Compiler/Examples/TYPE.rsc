module experiments::Compiler::Examples::TYPE

data SYM = A() | B();

public bool comparable(SYM s, SYM t) = subtype(s,t) || subtype(t,s);

bool subtype(A(), B()) = true;

//default bool subtype(SYM s, SYM t) = false;