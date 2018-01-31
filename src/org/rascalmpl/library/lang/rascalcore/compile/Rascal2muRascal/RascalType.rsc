@bootstrapParser
module lang::rascalcore::compile::Rascal2muRascal::RascalType

//import Type;
//import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;
////import lang::rascalcore::compile::Rascal2muRascal::TypeReifier;
//
//import lang::rascal::\syntax::Rascal;
//import lang::rascal::grammar::definition::Symbols;
//import lang::rascal::types::TestChecker;
//import lang::rascal::types::CheckTypes;
//import lang::rascal::types::AbstractName;
//import lang::rascal::types::CheckerConfig;
//
///*
// * translateType: translate a concrete (textual) type description to a Symbol
// */
//
//Symbol translateType((BasicType) `value`) 		= Symbol::\value();
//Symbol translateType(t: (BasicType) `loc`) 		= Symbol::\loc();
//Symbol translateType(t: (BasicType) `node`) 	= Symbol::\node();
//Symbol translateType(t: (BasicType) `num`) 		= Symbol::\num();
//Symbol translateType(t: (BasicType) `int`) 		= Symbol::\int();
//Symbol translateType(t: (BasicType) `real`) 	= Symbol::\real();
//Symbol translateType(t: (BasicType) `rat`)      = Symbol::\rat();
//Symbol translateType(t: (BasicType) `str`) 		= Symbol::\str();
//Symbol translateType(t: (BasicType) `bool`) 	= Symbol::\bool();
//Symbol translateType(t: (BasicType) `void`) 	= Symbol::\void();
//Symbol translateType(t: (BasicType) `datetime`)	= Symbol::\datetime();
//
//Symbol translateType(t: (StructuredType) `bag [ <TypeArg arg> ]`) 
//												= \bag(translateType(arg)); 
//Symbol translateType(t: (StructuredType) `list [ <TypeArg arg> ]`) 
//												= \list(translateType(arg)); 
//Symbol translateType(t: (StructuredType) `map[ <TypeArg arg1> , <TypeArg arg2> ]`) 
//												= \map(translateType(arg1), translateType(arg2)); 
//Symbol translateType(t: (StructuredType) `set [ <TypeArg arg> ]`)
//												= \set(translateType(arg)); 
//Symbol translateType(t: (StructuredType) `rel [ <{TypeArg ","}+ args> ]`) 
//												= \rel([ translateType(arg) | arg <- args]);
//Symbol translateType(t: (StructuredType) `lrel [ <{TypeArg ","}+ args> ]`) 
//												= \lrel([ translateType(arg) | arg <- args]);
//Symbol translateType(t: (StructuredType) `tuple [ <{TypeArg ","}+ args> ]`)
//												= \tuple([ translateType(arg) | arg <- args]);
//Symbol translateType(t: (StructuredType) `type [ < TypeArg arg> ]`)
//												= \reified(translateType(arg));      
//
//Symbol translateType(t : (Type) `(<Type tp>)`) = translateType(tp);
//Symbol translateType(t : (Type) `<UserType user>`) = translateType(user);
//Symbol translateType(t : (Type) `<FunctionType function>`) = translateType(function);
//Symbol translateType(t : (Type) `<StructuredType structured>`)  = translateType(structured);
//Symbol translateType(t : (Type) `<BasicType basic>`)  = translateType(basic);
//Symbol translateType(t : (Type) `<DataTypeSelector selector>`)  { throw "DataTypeSelector"; }
//Symbol translateType(t : (Type) `<TypeVar typeVar>`) = translateType(typeVar);
//Symbol translateType(t : (Type) `<Sym symbol>`)  = sym2symbol(symbol); // insertLayout(sym2symbol(symbol));	// make sure concrete lists have layout defined
//
//Symbol translateType(t : (TypeArg) `<Type tp>`)  = translateType(tp);
//Symbol translateType(t : (TypeArg) `<Type tp> <Name name>`) = \label(getSimpleName(convertName(name)), translateType(tp));
//
//Symbol translateType(t: (FunctionType) `<Type tp> (<{TypeArg ","}* args>)`) = 
//									\func(translateType(tp), [ translateType(arg) | arg <- args]);
//									
//Symbol translateType(t: (UserType) `<QualifiedName name>`) {
//	// look up the name in the type environment
//	val = getAbstractValueForQualifiedName(name);
//	
//	if(isDataType(val) || isNonTerminalType(val) || isAlias(val)) {
//		return val.rtype;
//	}
//	throw "The name <name> is not resolved to a type: <val>.";
//}
//Symbol translateType(t: (UserType) `<QualifiedName name>[<{Type ","}+ parameters>]`) {
//	// look up the name in the type environment
//	val = getAbstractValueForQualifiedName(name);
//	
//	if(isDataType(val) || isNonTerminalType(val) || isAlias(val)) {
//		// instantiate type parameters
//		val.rtype.parameters = [ translateType(param) | param <- parameters];
//		return val.rtype;
//	}
//	throw "The name <name> is not resolved to a type: <val>.";
//}  
//									
//Symbol translateType(t: (TypeVar) `& <Name name>`) = \parameter(getSimpleName(convertName(name)), Symbol::\value());  
//Symbol translateType(t: (TypeVar) `& <Name name> \<: <Type bound>`) = \parameter(getSimpleName(convertName(name)), translateType(bound));  
//
//default Symbol translateType(Type t) {
//	throw "Cannot translate type <t>";
//}
