@license{
Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
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