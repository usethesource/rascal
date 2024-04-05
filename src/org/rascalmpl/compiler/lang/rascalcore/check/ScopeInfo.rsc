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
// IMPORTANT: the above states should be checked in the above order.
// Case in point: a function type that is part of a return type.

private /*const*/ str key_defineOrReuseTypeParameters = "defineOrReuseTypeParameters";

void beginDefineOrReuseTypeParameters(Collector c, bool closed=false){
    c.push(key_defineOrReuseTypeParameters, closed);
}

void endDefineOrReuseTypeParameters(Collector c){
    c.pop(key_defineOrReuseTypeParameters);
}

tuple[bool yes, bool closed] defineOrReuseTypeParameters(Collector c){
    if(!isEmpty(c.getStack(key_defineOrReuseTypeParameters)) && bool closed := c.top(key_defineOrReuseTypeParameters)){
        return <true, closed>;
    } else {
        return <false, false>;
    }
}

private /*const*/ str key_useTypeParameters = "useTypeParameters";

void beginUseTypeParameters(Collector c, bool closed = false){
    c.push(key_useTypeParameters, closed);
}

void endUseTypeParameters(Collector c){
    c.pop(key_useTypeParameters);
}

tuple[bool yes, bool closed] useTypeParameters(Collector c){
    if(!isEmpty(c.getStack(key_useTypeParameters)) && bool closed := c.top(key_useTypeParameters)){
        return <true, closed>;
    } else {
        return <false, false>;
    }
}

private /*const*/ str key_useBoundedTypeParameters = "useBoundedTypeParameters";

void beginUseBoundedTypeParameters(rel[str, Type] tpbounds, Collector c){
    c.push(key_useBoundedTypeParameters, tpbounds);
}

tuple[bool yes, rel[str, Type] tpbounds] useBoundedTypeParameters(Collector c){
      tbl = c.getStack(key_useBoundedTypeParameters);
      if([rel[str,Type] tpbounds,*_] := tbl){
        return <true, tpbounds>;
      } else {
        return <false, {}>;
      }
}

void endUseBoundedTypeParameters(Collector c){
    c.pop(key_useBoundedTypeParameters);
}

 