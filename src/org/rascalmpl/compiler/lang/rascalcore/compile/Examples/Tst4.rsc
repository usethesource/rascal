module lang::rascalcore::compile::Examples::Tst4
data Symbol    // <1>
     = \int()
     //| \bool()
     //| \real()
     //| \rat()
     //| \str()
     //| \num()
     //| \node()
     //| \void()
     //| \value()
     //| \loc()
     //| \datetime()
     ;
 
data Symbol     // <2>
     = \label(str name, Symbol symbol)
     ;
  
data Symbol      // <3>
     = \set(Symbol symbol)
     //| \rel(list[Symbol] symbols)
     //| \lrel(list[Symbol] symbols)
     //| \tuple(list[Symbol] symbols)
     //| \list(Symbol symbol)
     //| \map(Symbol from, Symbol to)
     //| \bag(Symbol symbol)
     //| \adt(str name, list[Symbol] parameters)
     //| \cons(Symbol \adt, str name, list[Symbol] parameters)
     //| \alias(str name, list[Symbol] parameters, Symbol aliased)
     | \func(Symbol ret, list[Symbol] parameters, list[Symbol] kwTypes)
     | \func(Symbol ret, list[Symbol] parameters) // deprecated
     //| \overloaded(set[Symbol] alternatives)
     //| \var-func(Symbol ret, list[Symbol] parameters, Symbol varArg)
     //| \reified(Symbol symbol)
     ;

data Symbol // <4>
     = \parameter(str name, Symbol bound) 
     ;

data Production
     = \cons(Symbol def, list[Symbol] symbols, list[Symbol] kwTypes, set[Attr] attributes)
     | \func(Symbol def, list[Symbol] symbols, list[Symbol] kwTypes, set[Attr] attributes)
     //| \choice(Symbol def, set[Production] alternatives)
     //| \composition(Production lhs, Production rhs)
     ;
     
data Attr 
     = \tag(value \tag) 
     ;

