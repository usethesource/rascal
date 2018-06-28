module lang::rascalcore::check::Test3
  
data Symbol      // <3>
     = \set(Symbol symbol)
     | \rel(list[Symbol] symbols)
     | \lrel(list[Symbol] symbols)
     | \tuple(list[Symbol] symbols)
     | \list(Symbol symbol)
     | \map(Symbol from, Symbol to)
     | \bag(Symbol symbol)
     | \adt(str name, list[Symbol] parameters)
     | \cons(Symbol \adt, str name, list[Symbol] parameters)
     | \alias(str name, list[Symbol] parameters, Symbol aliased)
     | \func(Symbol ret, list[Symbol] parameters, list[Symbol] kwTypes)
     | \func(Symbol ret, list[Symbol] parameters) // deprecated
     | \overloaded(set[Symbol] alternatives)
     | \var-func(Symbol ret, list[Symbol] parameters, Symbol varArg)
     | \reified(Symbol symbol)
     ;

Symbol main(){
    Symbol ftype;
    selected = ftype;
    return selected.ret;
}