@bootstrapParser
module lang::rascalcore::check::CollectPattern

/*
    Check patterns
*/

extend lang::rascalcore::check::CheckerCommon;
extend lang::rascalcore::check::CollectLiteral;

import lang::rascal::\syntax::Rascal;
import String;

void collect(current: (Literal)`<RegExpLiteral regExpLiteral>`, Collector c){
    c.fact(current, regExpLiteral);
    collect(regExpLiteral, c);
}

void collect(current: (RegExpLiteral)`/<RegExp* regexps>/<RegExpModifier modifier>`,Collector c) {
    c.fact(current, astr());
    collect(regexps, modifier, c);
}

void collect(RegExp regExp, Collector c){
    if( (RegExp)`\<<Name name>\>` := regExp){
        c.use(name, variableRoles);
    } else if ((RegExp)`\<<Name name>:<NamedRegExp* regexps>\>` := regExp){
        c.define("<name>", formalOrPatternFormal(c), name, defType(astr()));
        collect(name, regexps, c);
    }
    c.fact(regExp, astr());
    // ignore other RegExp cases
}

void collect(NamedRegExp namedRegExp, Collector c){
   if((NamedRegExp)`\<<Name name>\>` := namedRegExp){
        c.use(name, variableRoles);
   }
}

// ---- set pattern

void collect(current: (Pattern) `{ <{Pattern ","}* elements0> }`, Collector c){
    if(size([e | e <- elements0]) == 0){    // TODO rewrite
       c.fact(current, aset(avoid()));
    }
    c.push(patternContainer, "set");
    collect(elements0, c);
    c.pop(patternContainer);
}

// ---- list pattern

void collect(current: (Pattern) `[ <{Pattern ","}* elements0> ]`, Collector c){
    if(size([e | e <- elements0]) == 0){    // TODO rewrite
       c.fact(current, alist(avoid()));
    }
    c.push(patternContainer, "list");
        collect(elements0, c);
    c.pop(patternContainer);
}

// ---- typed variable pattern
            
void collect(current: (Pattern) `<Type tp> <Name name>`, Collector c){
    uname = unescape("<name>");
    if(tp is function) c.enterScope(current);
        collect(tp, c);
    if(tp is function) c.leaveScope(current);
    
    c.calculate("typed variable pattern", current, [tp], AType(Solver s){  return s.getType(tp)[alabel=uname]; });
    if(!isWildCard(uname)){
       c.push(patternNames, <uname, getLoc(name)>);
       orScopes = c.getScopeInfo(orScope());
       for(<_, orInfo(vars)> <- orScopes){
            for(str id <- vars, id == uname){
                if(c.isAlreadyDefined("<name>", name)){
                    c.use(name, {variableId(), formalId(), nestedFormalId(), patternVariableId()});
                    return;
                }
            }
       }
       c.define(uname, formalOrPatternFormal(c), name, defType(tp));
    } else {
       c.fact(name, tp);
    }
}

void collectAsVarArg(current: (Pattern) `<Type tp> <Name name>`, Collector c){
    uname = unescape("<name>");
    
    if(!isWildCard(uname)){
       if(inPatternNames(uname, c)){
          c.use(name, {formalId()});
          c.require("typed variable pattern", current, [tp, name], 
            void (Solver s){
                nameType = alist(s.getType(tp), alabel=uname);
                s.requireEqual(name, nameType, error(name, "Expected %t for %q, found %q", nameType, uname, name));
            });
       } else {
          c.push(patternNames, <uname, getLoc(name)>);
          c.define(uname, formalOrPatternFormal(c), name, defType([tp], AType(Solver s){ 
            res = alist(s.getType(tp))[alabel=uname];
            return res;
             }));
       }
    }
   c.calculate("var arg", current, [tp], AType(Solver s) { return s.getType(tp)[alabel=uname]; });
   c.enterScope(current);
        collect(tp, c);
   c.leaveScope(current);
}

default void collectAsVarArg(Pattern current,  Collector c){
    c.report(error(current, "Unsupported construct in varargs"));
}

// ---- qualifiedName pattern: QualifiedName

void collect(current: (Pattern) `<QualifiedName name>`,  Collector c){
    <qualifier, base> = splitQualifiedName(name);
    if(!isWildCard(base)){
       if(inPatternNames(base, c)){
          c.useLub(name, {variableId(), moduleVariableId(), formalId(), nestedFormalId(), patternVariableId()});
          return;
       }
       c.push(patternNames, <base, getLoc(current)>);
       if(!isEmpty(qualifier)) c.report(error(name, "Qualifier not allowed"));
       if(isTopLevelParameter(c)){
          c.fact(current, avalue(alabel=unescape(prettyPrintBaseName(name))));  
          c.define(base, formalId(), name, defLub([], AType(Solver _) { return avalue(alabel=unescape(prettyPrintBaseName(name))); }));
       } else {
          if(c.isAlreadyDefined("<name>", name)){
            c.use(name, {variableId(), moduleVariableId(), formalId(), nestedFormalId(), patternVariableId()});
            c.report(info(name, "Pattern variable %q has been declared outside pattern and its value will be used, add explicit declaration here if you want a new variable", name));
          } else {
            tau = c.newTypeVar(name);
            c.fact(name, tau); //<====
            c.define(base, formalOrPatternFormal(c), name, defLub([], AType(Solver s) { 
                return s.getType(tau)[alabel=unescape(prettyPrintBaseName(name))]; 
            }));
          }
       }
    } else {
       c.fact(name, avalue(alabel=unescape(prettyPrintBaseName(name))));
    }
}

// ---- multiVariable pattern: QualifiedName*

void collect(current: (Pattern) `<QualifiedName name>*`,  Collector c){
    Pattern pat = (Pattern) `<QualifiedName name>`;
    c.report(warning(current, "`<name>*` is deprecated, use `*<name>` instead"));
    collectSplicePattern(current, pat, c);
}

bool inSetPattern(Pattern current, Collector c){
    container = c.top(patternContainer);
    switch(container){
        case "list": return false;
        case "set":  return true;
        default:
            c.report(error(current, "Splice operator not allowed inside a %v pattern", container));
    }
    return false;
}

// ---- splice pattern: *Pattern

void collect(current: (Pattern) `* <Pattern argument>`, Collector c){
    collectSplicePattern(current, argument, c);
}
    
void collectSplicePattern(Pattern current, Pattern argument,  Collector c){
    inSet = inSetPattern(current, c);
    scope = c.getScope();
    if(argument is typedVariable){
       tp = argument.\type;
       argName = argument.name;
       uname = unescape("<argName>");
       
       if(!isWildCard(uname)){
          if(!inPatternNames(uname, c)){
             c.push(patternNames, <uname, getLoc(argName)>);
          }    
          
          c.define(uname, formalOrPatternFormal(c), argName, defType([tp], 
               AType(Solver s){ return inSet ? aset(s.getType(tp)) : alist(s.getType(tp)); }));     
       } else {
          c.calculate("typed anonymous variable in splice pattern", argName, [tp], 
                AType(Solver s){ 
                    return inSet ? aset(s.getType(tp)) : alist(s.getType(tp));
                });
       }
       c.calculate("typed variable in splice pattern", current, [tp], AType(Solver s){ return s.getType(tp); });
       collect(tp, c);
    } else if(argument is qualifiedName){
        argName = argument.qualifiedName;
        <qualifier, base> = splitQualifiedName(argName);
        if(!isWildCard(base)){
           if(inPatternNames(base, c)){
              c.useLub(argName, variableRoles);
              return;
           }
           c.push(patternNames, <base, getLoc(argument)>);
    
           if(isTopLevelParameter(c)){
              c.fact(current, avalue());
              if(!isEmpty(qualifier)) c.report(error(argName, "Qualifier not allowed"));
              c.define(base, formalId(), argName, defLub([], AType(Solver _) { return avalue(); }));
           } else {
              if(c.isAlreadyDefined("<argName>", argName)) {
                  c.use(argName, {variableId(), moduleVariableId(), formalId(), nestedFormalId(), patternVariableId()});
                  c.report(info(argName, "Pattern variable %q has been declared outside pattern and its value will be used, add explicit declaration here if you want a new variable", argName));
              } else {
                  tau = c.newTypeVar(current); // <== argName;
                  c.fact(current, tau);    // <===
                  if(!isEmpty(qualifier)) c.report(error(argName, "Qualifier not allowed"));
                  c.define(base, formalOrPatternFormal(c), argName, 
                            defLub([], AType(Solver s) { 
                            return inSet ? makeSetType(s.getType(tau)) : makeListType(s.getType(tau));}));
              }
             }
        } else {
           c.fact(current, avoid());
        }
    } else {
        c.report(error(current, "Unsupported variant of splice pattern"));
    }
}

// ---- splicePlus pattern: +Pattern ------------------------------------------

void collect(current: (Pattern) `+<Pattern argument>`, Collector c){
    collectSplicePattern(current, argument, c);
}

// ---- tuple pattern ---------------------------------------------------------

void collect(current: (Pattern) `\< <{Pattern ","}+ elements1> \>`, Collector c){
    c.push(patternContainer, "tuple");
    collect(elements1, c);
    c.pop(patternContainer);
}

void collect(current: (KeywordArgument[Pattern]) `<Name name> = <Pattern expression>`,  Collector c){
    //scope = c.getScope();
    //c.calculate("default expression in pattern", expression, [], AType(Solver s){ return getPatternType(expression, avalue(), scope, s); });
    collect(expression, c);
}

//AType getPatternType0(current: (KeywordArgument[Pattern]) `<Name name> = <Pattern expression>`, AType subjectType, loc scope, Solver s){
//    return getPatternType(expression, subjectType, scope, s);
//}

// ---- call or tree pattern --------------------------------------------------

void collect(current: (Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`, Collector c){
    c.push(patternContainer, "constructor");
    if((Pattern) `<QualifiedName name>` := expression){
        <qualifier, base> = splitQualifiedName(name);
        if(!isEmpty(qualifier)){     
           c.useQualified([qualifier, base], name, {constructorId()}, dataOrSyntaxRoles + {moduleId()} );
        } else {
            c.use(name, {constructorId()});  // <==
        }
    } else {
      collect(expression, c);
    }
    collect(arguments, c);
    collect(keywordArguments, c);
    c.pop(patternContainer);
}

// ---- variable becomes pattern

void collect(current: (Pattern) `<Name name> : <Pattern pattern>`, Collector c){
    uname = unescape("<name>");
    if(inPatternNames(uname, c)){
        c.useLub(name, variableRoles);
    } else {
        c.push(patternNames, <uname, getLoc(name)>);
        scope = c.getScope();
        c.define(uname, formalOrPatternFormal(c), name, defLub([pattern], AType(Solver s) { return getPatternType(pattern, avalue(), scope, s); }));
    }
    collect(pattern, c);
}

// ---- typed variable becomes

void collect(current: (Pattern) `<Type tp> <Name name> : <Pattern pattern>`, Collector c){
    uname = unescape("<name>");
    c.push(patternNames, <uname, name>);
    c.define(uname, formalOrPatternFormal(c), name, defType([tp], AType(Solver s){ return s.getType(tp); }));
    c.fact(current, tp);
    collect(tp, pattern, c);
}

// ---- descendant pattern

void collect(current: (Pattern) `/ <Pattern pattern>`, Collector c){
    c.push(patternContainer, "descendant");
        collect(pattern, c);
    c.pop(patternContainer);
    c.fact(current, avalue());
}

// ---- negative 
void collect(current: (Pattern) `- <Pattern pattern>`, Collector c){
    collect(pattern, c);
}

//TODO: map

void collect(current: (Pattern) `( <{Mapping[Pattern] ","}* mps> )`, Collector c){
     if(size([e | e <- mps]) == 0){    // TODO rewrite
       c.fact(current, amap(avoid(), avoid()));
    }
    c.push(patternContainer, "map");
        collect(mps, c);
    c.pop(patternContainer);
}

// ---- reifiedType

void collect(current: (Pattern) `type ( <Pattern symbol>, <Pattern definitions> )`, Collector c){
    c.fact(current, areified(avalue()));
    c.push(patternContainer, "reified type constructor");
        collect(symbol, definitions, c);
    c.pop(patternContainer);
}

// ---- asType
void collect(current: (Pattern) `[ <Type tp> ] <Pattern p>`, Collector c){
    c.fact(current, tp);

    // If pattern is of type string, the string will be parsed using the non-terminal tp.
    // If the pattern has another type, then it must be a subtype of tp 
    
    c.require("asType", current, [tp, p], void(Solver s){
        if(!isStrAType(s.getType(p))){
            s.requireComparable(p, tp, error(p, "Pattern should be subtype of %t, found %t", tp, p)); 
        }
    });
    collect(tp, c);
    c.push(patternContainer, "asType");
    	collect(p, c);
    c.pop(patternContainer);
}

// ---- anti

void collect(current: (Pattern) `! <Pattern pattern>`, Collector c){
    c.fact(current, avalue());
    c.enterLubScope(current); // wrap in extra scope to avoid that variables in pattern leak to surroundings
        collect(pattern, c);
    c.leaveScope(current);    
}