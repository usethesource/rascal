@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
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
module lang::rascalcore::check::ScopeInfo

/*
    Manage information related to specific scopes
*/

import lang::rascalcore::check::BasicRascalConfig;
import lang::rascalcore::check::NameUtils;
import lang::rascal::\syntax::Rascal;

public /*const*/ str patternContainer = "patternContainer";
public /*const*/ str patternNames     = "patternNames";

public /*const*/ str currentAdt = "currentAdt";             // used to mark data declarations
public /*const*/ str currentFunction = "currentFunction";   // used to mark function declarations
public /*const*/ str inAlternative = "inAlternative";       // used to mark top-level alternative in syntax declaration
public /*const*/ str typeContainer = "typeContainer";
public /*const*/ str inConcreteLiteral = "concreteLiteral"; // used to mark that we are inside a concrete literal
public /*const*/ str currentAlternative = "currentAlternative";
                                                            // used to mark that we are inside an alternative of syntax declaration

// Some utilities on patterns
    
void beginPatternScope(str name, Collector c){
    c.clearStack(patternNames);
    c.push(patternContainer, name);
}

void endPatternScope(Collector c){
    c.pop(patternContainer);
    c.clearStack(patternNames);
}

bool inPatternNames(str name, Collector c){
    for(lrel[str,loc] pnames :=  c.getStack(patternNames), <str n, loc _> <-pnames) if(n == name) return true;
    return false;
}

bool inPatternScope(Collector c){
    return !isEmpty(c.getStack(patternContainer));
}

IdRole formalOrPatternFormal(Collector c){
    return isTopLevelParameter(c) ? formalId() :  ("parameter" in c.getStack(patternContainer) ? nestedFormalId() : patternVariableId());
}

bool isTopLevelParameter(Collector c){
    return "parameter" := c.top(patternContainer);
}

data VisitOrSwitchInfo = visitOrSwitchInfo(Expression expression, bool isVisit);

// Information needed for checking return statement
data SignatureInfo
    = signatureInfo(Type returnType)
    ;

// Determine how type parameters (TypeVar in Rascal grammar) will be treated:
// defineOrReuseTypeParameters:
//  - define type parameters unless they are already defined (= reused in same scope)
//  - used for parameter lists
// useTypeParameters:
// - all type parameters should have been declared and each occurrence is treated as a use.
// - used for return types of functions
// useBoundedTypeParameters:
// - given a table of computed bounds, turn each use in a use with the given bound.
// - used for the body of functions
//
//Note neasting of types, e.g., a function type that is part of a return type.

data TypeParamHandler
    = useTP(bool closed)
    | defineOrReuseTP(bool closed)
    | useBoundedTP(rel[str, Type] tpbounds)
    ;
    
private /*const*/ str key_TypeParameterHandling = "typeParameterHandling";

void beginDefineOrReuseTypeParameters(Collector c, bool closed=false){
    c.push(key_TypeParameterHandling, defineOrReuseTP(closed));
}

void endDefineOrReuseTypeParameters(Collector c){
    handler = c.pop(key_TypeParameterHandling);
    if(defineOrReuseTP(_) !:= handler){
        throw "beginDefineOrReuseTypeParameters/endDefineOrReuseTypeParameters not properly nested";
    }
}

tuple[bool yes, bool closed] defineOrReuseTypeParameters(Collector c){
    stck = c.getStack(key_TypeParameterHandling);
    if([defineOrReuseTP(bool closed), *_] := stck){
        return <true, closed>;
    }
    return <false, false>;
}

void beginUseTypeParameters(Collector c, bool closed = false){
    //if(debugTP)println("beginUseTypeParameters, closed=<closed>, <c.getStack(key_TypeParameterHandling)>");
    c.push(key_TypeParameterHandling, useTP(closed));
}

void endUseTypeParameters(Collector c){
    handler = c.pop(key_TypeParameterHandling);
    if(useTP(_) !:= handler)
        throw "beginUseTypeParameters/endUseTypeParameters not properly nested";
}

tuple[bool yes, bool closed] useTypeParameters(Collector c){
    stck = c.getStack(key_TypeParameterHandling);
    if([useTP(bool closed), *_] := stck){
        return <true, closed>;
    }
    return <false, false>;
}

void beginUseBoundedTypeParameters(rel[str, Type] tpbounds, Collector c){
    c.push(key_TypeParameterHandling, useBoundedTP(tpbounds));
}

tuple[bool yes, rel[str, Type] tpbounds] useBoundedTypeParameters(Collector c){
    stck = c.getStack(key_TypeParameterHandling);
    if([useBoundedTP(rel[str, Type] tpbounds), *_] := stck){
        return <true, tpbounds>;
    }
    return <false, {}>;
}

void endUseBoundedTypeParameters(Collector c){
    handler = c.pop(key_TypeParameterHandling);
    if(useBoundedTP(_) !:= handler)
        throw "beginUseBoundedTypeParameters/endUseBoundedTypeParameters not properly nested";
}
 
 data OrInfo = orInfo(set[str] vars);