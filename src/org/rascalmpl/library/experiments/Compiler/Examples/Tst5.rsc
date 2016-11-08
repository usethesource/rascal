module experiments::Compiler::Examples::Tst5

import List;
import Set;    
import Type;   
   
public bool subtypeXXX(type[&T] t, type[&U] u) = subtypeXXX(t.symbol, u.symbol);

@doc{
.Synopsis
This function documents and implements the subtype relation of Rascal's type system. 
}
public bool subtypeXXX(Symbol s, s) = true;
public default bool subtypeXXX(Symbol s, Symbol t) = false;

public bool subtypeXXX(Symbol _, Symbol::\value()) = true;

//{func(bool(),[label("_",adt("Symbol",[])),label("arg1",adt("Symbol",[]))])[@isVarArgs=false],
// func(bool(),[label("s",adt("Symbol",[])),label("s",adt("Symbol",[]))])[@isVarArgs=false]}; 
//{func(bool(),[label("s",adt("Symbol",[])),label("t",adt("Symbol",[]))])[@isVarArgs=false]} 

//public bool subtypeXXX(Symbol::\void(), Symbol _) = true;
//public bool subtypeXXX(Symbol::\cons(Symbol a, _, list[Symbol] _), a) = true;
//public bool subtypeXXX(Symbol::\cons(Symbol a, str name, list[Symbol] ap), Symbol::\cons(a,name,list[Symbol] bp)) = subtypeXXX(ap,bp);
//public bool subtypeXXX(Symbol::\adt(str _, list[Symbol] _), Symbol::\node()) = true;
//public bool subtypeXXX(Symbol::\adt(str n, list[Symbol] l), Symbol::\adt(n, list[Symbol] r)) = subtypeXXX(l, r);
//public bool subtypeXXX(Symbol::\alias(str _, list[Symbol] _, Symbol aliased), Symbol r) = subtypeXXX(aliased, r);
//public bool subtypeXXX(Symbol l, \alias(str _, list[Symbol] _, Symbol aliased)) = subtypeXXX(l, aliased);
//public bool subtypeXXX(Symbol::\int(), Symbol::\num()) = true;
//public bool subtypeXXX(Symbol::\rat(), Symbol::\num()) = true;
//public bool subtypeXXX(Symbol::\real(), Symbol::\num()) = true;
//public bool subtypeXXX(Symbol::\tuple(list[Symbol] l), \tuple(list[Symbol] r)) = subtypeXXX(l, r);
//
//// list and lrel
//public bool subtypeXXX(Symbol::\list(Symbol s), Symbol::\list(Symbol t)) = subtypeXXX(s, t); 
//public bool subtypeXXX(Symbol::\lrel(list[Symbol] l), Symbol::\lrel(list[Symbol] r)) = subtypeXXX(l, r);
//
//// Potential alternative rules:
////public bool subtypeXXX(\list(Symbol s), \lrel(list[Symbol] r)) = subtypeXXX(s, (size(r) == 1) ? r[0] : \tuple(r));
////public bool subtypeXXX(\lrel(list[Symbol] l), \list(Symbol r)) = subtypeXXX((size(l) == 1) ? l[0] : \tuple(l), r);
//
//public bool subtypeXXX(Symbol::\list(Symbol s), Symbol::\lrel(list[Symbol] r)) = subtypeXXX(s, Symbol::\tuple(r));
//public bool subtypeXXX(Symbol::\lrel(list[Symbol] l), \list(Symbol r)) = subtypeXXX(Symbol::\tuple(l), r);
//
//// set and rel
//public bool subtypeXXX(Symbol::\set(Symbol s), Symbol::\set(Symbol t)) = subtypeXXX(s, t);
//public bool subtypeXXX(Symbol::\rel(list[Symbol] l), Symbol::\rel(list[Symbol] r)) = subtypeXXX(l, r);
//
////Potential alternative rules:
////public bool subtypeXXX(\set(Symbol s), \rel(list[Symbol] r)) = subtypeXXX(s, (size(r) == 1) ? r[0] : \tuple(r));
////public bool subtypeXXX(\rel(list[Symbol] l), \set(Symbol r)) = subtypeXXX((size(l) == 1) ? l[0] : \tuple(l), r);
//
//public bool subtypeXXX(Symbol::\set(Symbol s), Symbol::\rel(list[Symbol] r)) = subtypeXXX(s, Symbol::\tuple(r));
//public bool subtypeXXX(Symbol::\rel(list[Symbol] l), Symbol::\set(Symbol r)) = subtypeXXX(Symbol::\tuple(l), r);
//
//public bool subtypeXXX(Symbol::\bag(Symbol s), Symbol::\bag(Symbol t)) = subtypeXXX(s, t);  
//public bool subtypeXXX(Symbol::\map(Symbol from1, Symbol to1), Symbol::\map(Symbol from2, Symbol to2)) = subtypeXXX(from1, from2) && subtypeXXX(to1, to2);
//public bool subtypeXXX(Symbol::\func(Symbol r1, list[Symbol] p1), Symbol::\func(Symbol r2, list[Symbol] p2)) = subtypeXXX(r1, r2) && subtypeXXX(p2, p1); // note the contra-variance of the argument types
//public bool subtypeXXX(Symbol::\parameter(str _, Symbol bound), Symbol r) = subtypeXXX(bound, r);
//public bool subtypeXXX(Symbol l, Symbol::\parameter(str _, Symbol bound)) = subtypeXXX(l, bound);
//public bool subtypeXXX(Symbol::\label(str _, Symbol s), Symbol t) = subtypeXXX(s,t);
//public bool subtypeXXX(Symbol s, Symbol::\label(str _, Symbol t)) = subtypeXXX(s,t);
//public bool subtypeXXX(Symbol::\reified(Symbol s), Symbol::\reified(Symbol t)) = subtypeXXX(s,t);
//public bool subtypeXXX(Symbol::\reified(Symbol s), Symbol::\node()) = true;
//public bool subtypeXXX(list[Symbol] l, list[Symbol] r) = all(i <- index(l), subtypeXXX(l[i], r[i])) when size(l) == size(r) && size(l) > 0;
//public default bool subtypeXXX(list[Symbol] l, list[Symbol] r) = size(l) == 0 && size(r) == 0;
