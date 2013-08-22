@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalType

import Prelude;
import lang::rascal::\syntax::Rascal;

Symbol translateType((BasicType) `value`) 		= \value();
Symbol translateType(t: (BasicType) `loc`) 		= \loc();
Symbol translateType(t: (BasicType) `node`) 	= \node();
Symbol translateType(t: (BasicType) `num`) 		= \num();
Symbol translateType(t: (BasicType) `type`) 	= \type();
Symbol translateType(t: (BasicType) `bag`) 		= \bag();
Symbol translateType(t: (BasicType) `int`) 		= \int();
Symbol translateType(t: (BasicType) `rel`) 		= \rel();
Symbol translateType(t: (BasicType) `lrel`) 	= \lrel();
Symbol translateType(t: (BasicType) `real`) 	= \real();
Symbol translateType(t: (BasicType) `tuple`) 	= \tuple();
Symbol translateType(t: (BasicType) `str`) 		= \str();
Symbol translateType(t: (BasicType) `bool`) 	= \bool();
Symbol translateType(t: (BasicType) `void`) 	= \void();
Symbol translateType(t: (BasicType) `datetime`)	= \datetime();
Symbol translateType(t: (BasicType) `set`) 		= \set();
Symbol translateType(t: (BasicType) `map`) 		= \map();
Symbol translateType(t: (BasicType) `list`) 	= \list();

Symbol translateType(t: (StructuredType) `bag [ <TypeArg arg> ]`) 
												= \bag(translateType(arg)); 
Symbol translateType(t: (StructuredType) `list [ <TypeArg arg> ]`) 
												= \list(translateType(arg)); 
Symbol translateType(t: (StructuredType) `map[ <TypeArg arg1> , <TypeArg arg2> ]`) 
												= \map(translateType(arg1), translateType(arg2)); 
Symbol translateType(t: (StructuredType) `set [ <TypeArg arg> ]`)
												= \set(translateType(arg)); 
Symbol translateType(t: (StructuredType) `rel [ <{TypeArg ","}+ args> ]`) 
												= \rel([ translateType(arg) | arg <- args]);
Symbol translateType(t: (StructuredType) `lrel [ <{TypeArg ","}+ args> ]`) 
												= \lrel([ translateType(arg) | arg <- args]);
Symbol translateType(t: (StructuredType) `tuple [ <{TypeArg ","}+ args> ]`)
												= \tuple([ translateType(arg) | arg <- args]);   

Symbol translateType(t : (Type) `<UserType user>`) = translateType(userType);
Symbol translateType(t : (Type) `<FunctionType function>`) = translateType(function);
Symbol translateType(t : (Type) `<StructuredType structured>`)  = translateType(structured);
Symbol translateType(t : (Type) `<BasicType basic>`)  = translateType(basic);
Symbol translateType(t : (Type) `<DataTypeSelector selector>`)  { throw "DataTypeSelector"; }
Symbol translateType(t : (Type) `<TypeVar typeVar>`) = translateType(typeVar);
Symbol translateType(t : (Type) `<Sym symbol>`)  = symbol;

Symbol translateType(t : (TypeArg) `<Type \type>`)  { throw "Sym"; }
Symbol translateType(t : (TypeArg) `<Type \type> <Name name>`) { throw "Sym"; }

Symbol translateType(t: (FunctionType) `<Type \type> (<{TypeArg ","}* args>)`) = 
									\func(translateType(ret), [ translateType(arg) | arg <- args]);
									
Symbol translateType(t: (UserType) `<QualifiedName name>`) = \adt("<name>", []);  	
Symbol translateType(t: (UserType) `<QualifiedName name>[<{Type ","}+ parameters>]`) = 
									\adt("<name>", [ translateType(arg) | arg <- args]);  
									
Symbol translateType(t: (TypeVar) `& <Name name>`) = \parameter("<name>");  
Symbol translateType(t: (TypeVar) `& <Name name> \<: <Type bound>`) = \parameter("<name>", translateType(bound));  


default Symbol translateType(Type t) {
	throw "Cannot translate type <t>";
}
