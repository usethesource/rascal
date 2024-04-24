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

public /*const*/ str currentAdt = "currentAdt";       // used to mark data declarations
public /*const*/ str inAlternative = "inAlternative"; // used to mark top-level alternative in syntax declaration
public /*const*/ str typeContainer = "typeContainer";
public /*const*/ str inConcreteLiteral = "concreteLiteral"; // used to mark that we are inside a concrete literal

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
// Case in point: a function type that is part of a return type.

//FUN = resultType inParameters BODY;
//BODY = (useBoundedTP | FUN)*


data TypeParamHandler
    = defineOrReuseTP(bool closed)
    | useTP(bool closed)
    | useBoundedTP(rel[str, Type] tpbounds)
    ;
    
private /*const*/ str key_TypeParameterHandling = "typeParameterHandling";
bool debugTP = false;

void beginDefineOrReuseTypeParameters(Collector c, bool closed=false){
    if(debugTP)println("beginDefineOrReuseTypeParameters, closed = <closed>, <c.getStack(key_TypeParameterHandling)>");
    c.push(key_TypeParameterHandling, defineOrReuseTP(closed));
}

void endDefineOrReuseTypeParameters(Collector c){
    if(debugTP)println("endDefineOrReuseTypeParameters");
    handler = c.pop(key_TypeParameterHandling);
    if(defineOrReuseTP(_) !:= handler){
        throw "beginDefineOrReuseTypeParameters/endDefineOrReuseTypeParameters not properly nested";
    }
}

tuple[bool yes, bool closed] defineOrReuseTypeParameters(Collector c){
    stck = c.getStack(key_TypeParameterHandling);
     if(debugTP)println("defineOrReuseTypeParameters: <stck>");
    switch(stck){
        case [defineOrReuseTP(bool closed), *_]:
            return <true, closed>;
       //case [useTP(bool closed), defineOrReuseTP(closed), *_]:
       //             return <true, closed>;
       //case [defineOrReuseTP(bool closed), defineOrReuseTP(closed), *_]:
       //             return <true, closed>;
       //case [defineOrReuseTP(bool closed), useBoundedTP(_)]:
       //             return <false, closed>;
       // case [defineOrReuseTP(bool closed), useTP(_)]:
       //             return <true, closed>;
       //case [defineOrReuseTP(bool closed)]:
       //             return <true, closed>;
    }
    return <false, false>;
}

void beginUseTypeParameters(Collector c, bool closed = false){
    if(debugTP)println("beginUseTypeParameters, closed=<closed>, <c.getStack(key_TypeParameterHandling)>");
    c.push(key_TypeParameterHandling, useTP(closed));
}

void endUseTypeParameters(Collector c){
    if(debugTP)println("endUseTypeParameters");
    handler = c.pop(key_TypeParameterHandling);
    if(useTP(_) !:= handler)
        throw "beginUseTypeParameters/endUseTypeParameters not properly nested";
}

tuple[bool yes, bool closed] useTypeParameters(Collector c){
    stck = c.getStack(key_TypeParameterHandling);
    if(debugTP)println("useTypeParameters: <stck>");
    switch(stck){ 
        case [useTP(bool closed), *_]:
            return <true, closed>;
        //case [useTP(bool closed), useTP(_), *_]:
        //            return <true, closed>;
        //case [useTP(bool closed), defineOrReuseTP(_), *_]:
        //            return <true, closed>;
        //case [useTP(bool closed), useBoundedTP(_)]:
        //            return <true, true>;
        //case [useTP(bool closed)]:
        //            return <true, closed>;
        //case [defineOrReuseTP(bool closed), useBoundedTP(_)]:
        //            return <true, true>;
    }
    return <false, false>;
}

void beginUseBoundedTypeParameters(rel[str, Type] tpbounds, Collector c){
    if(debugTP)println("beginUseBoundedTypeParameters, <c.getStack(key_TypeParameterHandling)>");
    c.push(key_TypeParameterHandling, useBoundedTP(tpbounds));
}

tuple[bool yes, rel[str, Type] tpbounds] useBoundedTypeParameters(Collector c){
    stck = c.getStack(key_TypeParameterHandling);
    if(debugTP)println("useBoundedTypeParameters: <stck>");
    switch(stck){
        case [useBoundedTP(rel[str, Type] tpbounds), *_]:
                    return <true, tpbounds>;
    }
    return <false, {}>;
}


void endUseBoundedTypeParameters(Collector c){
    if(debugTP) println("endUseBoundedTypeParameters");
    handler = c.pop(key_TypeParameterHandling);
    if(useBoundedTP(_) !:= handler)
        throw "beginUseBoundedTypeParameters/endUseBoundedTypeParameters not properly nested";
}
 