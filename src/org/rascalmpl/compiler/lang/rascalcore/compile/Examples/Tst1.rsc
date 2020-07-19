module lang::rascalcore::compile::Examples::Tst1
//
//data D[&T] = d1(&T fld);
//
//value main() = #type[D[int]];




syntax Sym
    =   
     empty: "(" ")"
    ;

syntax Type
    = bracket \bracket: "(" Type type ")" 
    | symbol: Sym symbol
    ;
    
value main() = true;


 
data Symbol     // <2>
     = \label(str name, Symbol symbol)
     ;
  
//data Symbol      // <3>
//     = \set(Symbol symbol)
//     | \rel(list[Symbol] symbols)
//     | \lrel(list[Symbol] symbols)
//     | \tuple(list[Symbol] symbols)
//     | \list(Symbol symbol)
//     | \map(Symbol from, Symbol to)
//     | \bag(Symbol symbol)
//     | \adt(str name, list[Symbol] parameters)
//     | \cons(Symbol \adt, str name, list[Symbol] parameters)
//     | \alias(str name, list[Symbol] parameters, Symbol aliased)
//     | \func(Symbol ret, list[Symbol] parameters, list[Symbol] kwTypes)
//     | \func(Symbol ret, list[Symbol] parameters) // deprecated
//     | \overloaded(set[Symbol] alternatives)
//     | \var-func(Symbol ret, list[Symbol] parameters, Symbol varArg)
//     | \reified(Symbol symbol)
//     ;
//
//data Symbol // <4>
//     = \parameter(str name, Symbol bound) 
//     ;
//
//@doc{
//.Synopsis
//A production in a grammar or constructor in a data type.
//
//.Description
//Productions represent abstract (recursive) definitions of abstract data type constructors and functions:
//
//* `cons`: a constructor for an abstract data type.
//* `func`: a function.
//* `choice`: the choice between various alternatives.
//* `composition`: composition of two productions.
//
//In ParseTree, see <<ParseTree-Production>>, 
//Productions will be further extended and will be used to represent productions in syntax rules.
//}  
//data Production
//     = \cons(Symbol def, list[Symbol] symbols, list[Symbol] kwTypes, set[Attr] attributes)
//     | \func(Symbol def, list[Symbol] symbols, list[Symbol] kwTypes, set[Attr] attributes /*, str code = "", map[str,value] bindings = (), loc cpe = |unknown:///|*/)
//     | \choice(Symbol def, set[Production] alternatives)
//     | \composition(Production lhs, Production rhs)
//     ;
//
//@doc{
//.Synopsis
//Attributes register additional semantics annotations of a definition. 
//}
//data Attr 
//     = \tag(value \tag) 
//     ;
  
  

