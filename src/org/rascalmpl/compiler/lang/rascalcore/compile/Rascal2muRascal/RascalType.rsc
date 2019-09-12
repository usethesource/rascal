@bootstrapParser
module lang::rascalcore::compile::Rascal2muRascal::RascalType

//import Type;
import lang::rascalcore::check::AType;
import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;
//import lang::rascalcore::compile::Rascal2muRascal::TypeReifier;

import lang::rascal::\syntax::Rascal;

/*
 * translateType: translate a concrete (textual) type description to a Symbol
 */
 
 AType translateType(Type tp) = getType(tp@\loc);

//AType translateType((BasicType) `value`) 		= avalue();
//AType translateType(t: (BasicType) `loc`) 		= aloc();
//AType translateType(t: (BasicType) `node`) 	    = anode();
//AType translateType(t: (BasicType) `num`) 		= anum();
//AType translateType(t: (BasicType) `int`) 		= aint();
//AType translateType(t: (BasicType) `real`) 	    = areal();
//AType translateType(t: (BasicType) `rat`)       = arat();
//AType translateType(t: (BasicType) `str`) 		= astr();
//AType translateType(t: (BasicType) `bool`) 	    = abool();
//AType translateType(t: (BasicType) `void`) 	    = avoid();
//AType translateType(t: (BasicType) `datetime`)	= adatetime();
//
//AType translateType(t: (StructuredType) `bag [ <TypeArg arg> ]`) 
//												= abag(translateType(arg)); 
//AType translateType(t: (StructuredType) `list [ <TypeArg arg> ]`) 
//												= alist(translateType(arg)); 
//AType translateType(t: (StructuredType) `map[ <TypeArg arg1> , <TypeArg arg2> ]`) 
//												= amap(translateType(arg1), translateType(arg2)); 
//AType translateType(t: (StructuredType) `set [ <TypeArg arg> ]`)
//												= aset(translateType(arg)); 
//AType translateType(t: (StructuredType) `rel [ <{TypeArg ","}+ args> ]`) 
//												= arel(atypeList([ translateType(arg) | arg <- args]));
//AType translateType(t: (StructuredType) `lrel [ <{TypeArg ","}+ args> ]`) 
//												= alrel(atypeList([ translateType(arg) | arg <- args]);
//AType translateType(t: (StructuredType) `tuple [ <{TypeArg ","}+ args> ]`)
//												= atuple(atypeList([ translateType(arg) | arg <- args]));
//AType translateType(t: (StructuredType) `type [ < TypeArg arg> ]`)
//												= areified(translateType(arg));      
//
//AType translateType(t : (Type) `(<Type tp>)`) = translateType(tp);
//AType translateType(t : (Type) `<UserType user>`) = translateType(user);
//AType translateType(t : (Type) `<FunctionType function>`) = translateType(function);
//AType translateType(t : (Type) `<StructuredType structured>`)  = translateType(structured);
//AType translateType(t : (Type) `<BasicType basic>`)  = translateType(basic);
//AType translateType(t : (Type) `<DataTypeSelector selector>`)  { throw "DataTypeSelector"; }
//AType translateType(t : (Type) `<TypeVar typeVar>`) = translateType(typeVar);
//AType translateType(t : (Type) `<Sym symbol>`)  = sym2symbol(symbol); // insertLayout(sym2symbol(symbol));	// make sure concrete lists have layout defined
//
//AType translateType(t : (TypeArg) `<Type tp>`)  = translateType(tp);
//AType translateType(t : (TypeArg) `<Type tp> <Name name>`) = \label(getSimpleName(convertName(name)), translateType(tp));
//
//AType translateType(t: (FunctionType) `<Type tp> (<{TypeArg ","}* args>)`) = 
//									afunc(translateType(tp), [ translateType(arg) | arg <- args]);
//									
//AType translateType(t: (UserType) `<QualifiedName name>`) {
//	// look up the name in the type environment
//	val = getAbstractValueForQualifiedName(name);
//	
//	if(isDataType(val) || isNonTerminalType(val) || isAlias(val)) {
//		return val.rtype;
//	}
//	throw "The name <name> is not resolved to a type: <val>.";
//}
//AType translateType(t: (UserType) `<QualifiedName name>[<{Type ","}+ parameters>]`) {
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
//AType translateType(t: (TypeVar) `& <Name name>`) = \parameter(getSimpleName(convertName(name)), Symbol::\value());  
//AType translateType(t: (TypeVar) `& <Name name> \<: <Type bound>`) = \parameter(getSimpleName(convertName(name)), translateType(bound));  
//
//default AType translateType(Type t) {
//	throw "Cannot translate type <t>";
//}
