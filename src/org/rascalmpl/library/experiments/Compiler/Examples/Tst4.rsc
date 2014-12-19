module experiments::Compiler::Examples::Tst4

extend ParseTree;
public data Symbol = deferred(Symbol givenType);
public bool subtype(Symbol t, Symbol::deferred(Symbol s)) = subtype(t,s); 
