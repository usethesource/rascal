@bootstrapParser
module lang::rascalcore::compile::Rascal2muRascal::Common

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::check::AType;
import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;
import lang::rascalcore::check::NameUtils;

// get the types and constructor names from a pattern

tuple[set[AType] types, set[str] constructors] getTypesAndConstructorNames(p:(Pattern) `<QualifiedName qualifiedName>`) =
    <{getType(p)}, {}>;
    
tuple[set[AType] types, set[str] constructors] getTypesAndConstructorNames(p:(Pattern) `<Type tp> <Name name>`) =
    <{getType(p)}, {}>; 
    
tuple[set[AType] types, set[str] constructors] getTypesAndConstructorNames(p:(Pattern) `<Name name> : <Pattern pattern>`) =
    getTypesAndConstructorNames(pattern);   

tuple[set[AType] types, set[str] constructors]  getTypesAndConstructorNames(p:(Pattern) `<Type tp> <Name name> : <Pattern pattern>`) =
    getTypesAndConstructorNames(pattern);
        
tuple[set[AType] types, set[str] constructors] getTypesAndConstructorNames(p:(Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`) =
    (expression is qualifiedName) ? <{}, {prettyPrintName("<expression>")}> : <{getType(p)}, {}>;

tuple[set[AType] types, set[str] constructors] getTypesAndConstructorNames(Pattern p) = <{getType(p)}, {}>;