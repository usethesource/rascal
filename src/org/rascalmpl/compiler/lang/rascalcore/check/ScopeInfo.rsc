@bootstrapParser
module lang::rascalcore::check::ScopeInfo

import lang::rascalcore::check::BasicRascalConfig;
import lang::rascalcore::check::NameUtils;
import lang::rascal::\syntax::Rascal;

public /*const*/ str patternContainer = "patternContainer";
public /*const*/ str patternNames     = "patternNames";


public /*const*/ str currentAdt = "currentAdt";       // used to mark data declarations
public /*const*/ str inAlternative = "inAlternative"; // used to mark top-level alternative in syntax declaration
public /*const*/ str typeContainer = "typeContainer";
public /*const*/ str inConcreteLiteral = "concreteLiteral"; // used to mark that we are inside a concrete literal

public /*const*/ str inFormals = "inFormals";

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

data ReturnInfo
    = returnInfo(Type returnType)
    ;

bool insideFormals(Solver s){
    return !isEmpty(s.getStack(inFormals));
}
 