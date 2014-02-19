@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalType

import experiments::Compiler::Rascal2muRascal::TypeUtils;

import Prelude;
import lang::rascal::\syntax::Rascal;
import lang::rascal::grammar::definition::Symbols;

import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;
import lang::rascal::types::AbstractName;

Symbol translateType((BasicType) `value`) 		= \value();
Symbol translateType(t: (BasicType) `loc`) 		= \loc();
Symbol translateType(t: (BasicType) `node`) 	= \node();
Symbol translateType(t: (BasicType) `num`) 		= \num();
Symbol translateType(t: (BasicType) `int`) 		= \int();
Symbol translateType(t: (BasicType) `real`) 	= \real();
Symbol translateType(t: (BasicType) `rat`)      = \rat();
Symbol translateType(t: (BasicType) `str`) 		= \str();
Symbol translateType(t: (BasicType) `bool`) 	= \bool();
Symbol translateType(t: (BasicType) `void`) 	= \void();
Symbol translateType(t: (BasicType) `datetime`)	= \datetime();

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
Symbol translateType(t: (StructuredType) `type [ < TypeArg arg> ]`)
												= \reified(translateType(arg));      

Symbol translateType(t : (Type) `(<Type tp>)`) = translateType(tp);
Symbol translateType(t : (Type) `<UserType user>`) = translateType(user);
Symbol translateType(t : (Type) `<FunctionType function>`) = translateType(function);
Symbol translateType(t : (Type) `<StructuredType structured>`)  = translateType(structured);
Symbol translateType(t : (Type) `<BasicType basic>`)  = translateType(basic);
Symbol translateType(t : (Type) `<DataTypeSelector selector>`)  { throw "DataTypeSelector"; }
Symbol translateType(t : (Type) `<TypeVar typeVar>`) = translateType(typeVar);
Symbol translateType(t : (Type) `<Sym symbol>`)  = sym2symbol(symbol);

Symbol translateType(t : (TypeArg) `<Type tp>`)  = translateType(tp);
Symbol translateType(t : (TypeArg) `<Type tp> <Name name>`) = \label(getSimpleName(convertName(name)), translateType(tp));

Symbol translateType(t: (FunctionType) `<Type tp> (<{TypeArg ","}* args>)`) = 
									\func(translateType(tp), [ translateType(arg) | arg <- args]);
									
Symbol translateType(t: (UserType) `<QualifiedName name>`) {
	rn = convertName(name);
	// look up the name in the type environment
	val = config.store[config.typeEnv[rn]];
	if(isDataType(val) || isNonTerminalType(val) || isAlias(val)) {
		return val.rtype;
	}
	throw "The name <name> is not resolved to a type: <val>.";
}
Symbol translateType(t: (UserType) `<QualifiedName name>[<{Type ","}+ parameters>]`) {
	rn = convertName(name);
	// look up the name in the type environment
	val = config.store[config.typeEnv[rn]];
	if(isDataType(val) || isNonTerminalType(val) || isAlias(val)) {
		// instantiate type parameters
		val.rtype.parameters = [ translateType(param) | param <- parameters];
		return val.rtype;
	}
	throw "The name <name> is not resolved to a type: <val>.";
}  
									
Symbol translateType(t: (TypeVar) `& <Name name>`) = \parameter(getSimpleName(convertName(name)), \value());  
Symbol translateType(t: (TypeVar) `& <Name name> \<: <Type bound>`) = \parameter(getSimpleName(convertName(name)), translateType(bound));  

default Symbol translateType(Type t) {
	throw "Cannot translate type <t>";
}
