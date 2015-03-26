module experiments::Compiler::Examples::Tst1

data SYM = A() | B();

public bool comparable(Symbol s, Symbol t) = subtype(s,t) || subtype(t,s);

bool subtype(A(), B()) = true;

bool subtype(SYM s, SYM t) = false;