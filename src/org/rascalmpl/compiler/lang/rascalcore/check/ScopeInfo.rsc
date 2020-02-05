@bootstrapParser
module lang::rascalcore::check::ScopeInfo

import lang::rascalcore::check::BasicRascalConfig;
import lang::rascal::\syntax::Rascal;

public str patternContainer = "patternContainer";
public str patternNames     = "patternNames";

public str currentAdt = "currentAdt";       // used to mark data declarations
public str inAlternative = "inAlternative"; // used to mark top-level alternative in syntax declaration
public str typeContainer = "typeContainer";


// Some utilities on patterns

set[str] getAllNames(Pattern p)
    = { "<name>" | /(Pattern) `<Type tp> <Name name>` := p } + { "<name>" | /QualifiedName name := p } - {"_"};
    
void beginPatternScope(str name, Collector c){
    c.clearStack(patternNames);
    c.push(patternContainer, name);
}

void endPatternScope(Collector c){
    c.pop(patternContainer);
    c.clearStack(patternNames);
}

bool inPatternNames(str name, Collector c){
    for(lrel[str,loc] pnames :=  c.getStack(patternNames), <str n, loc l> <-pnames) if(n == name) return true;
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
    | returnInfo(AType returnAType)
    ;
