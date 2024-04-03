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

private /*const*/ str key_useOrDeclareTypeParameters = "useOrDeclareTypeParameters";
private /*const*/ str key_useTypeParameters = "useTypeParameters";
private /*const*/ str key_useBoundedTypeParameters = "useBoundedTypeParameters";


// Some utilities on patterns

set[str] getAllNames(Pattern p)
    = { "<name>" | /(Pattern) `<Type _><Name name>` := p, !isWildCard("<name>") } + { "<name>" | /QualifiedName name := p, !isWildCard("<name>") };
    
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

data SignatureInfo
    = signatureInfo(Type returnType)
    ;
    
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

void beginUseOrDeclareTypeParameters(Collector c, bool closed=false){
    c.push(key_useOrDeclareTypeParameters, closed);
}

void endUseOrDeclareTypeParameters(Collector c){
    c.pop(key_useOrDeclareTypeParameters);
}

tuple[bool yes, bool closed] useOrDeclareTypeParameters(Collector c){
    if(!isEmpty(c.getStack(key_useOrDeclareTypeParameters)) && bool closed := c.top(key_useOrDeclareTypeParameters)){
        return <true, closed>;
    } else {
        return <false, false>;
    }
}

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

 