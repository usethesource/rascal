@bootstrapParser
module lang::rascalcore::check::Pattern

//extend analysis::typepal::TypePal;

extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::ConvertType;
extend lang::rascalcore::check::Expression;

import analysis::typepal::FailMessage;

import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::ATypeExceptions;
import lang::rascalcore::check::TypePalConfig;

import lang::rascal::\syntax::Rascal;

import String;
import Set;
import IO;

public str patternContainer = "patternContainer";
public str patternNames     = "patternNames";

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

// ---- bracketed pattern

//AType getPatternType(current: (Pattern) `(<Pattern p>)`, AType subjectType, loc scope, Solver s){
//    return getPatternType(p, subjectType, scope, Solver);
//}

// ---- literal patterns

default AType getPatternType(Pattern p, AType subjectType, loc scope, Solver s){
    return s.getType(p);
}

//void collect(Pattern current:(Literal)`<IntegerLiteral il>`, Collector c){
//    c.fact(current, aint());
//}
//
//void collect(Pattern current:(Literal)`<RealLiteral rl>`, Collector c){
//    c.fact(current, areal());
//}
//
//void collect(Pattern current:(Literal)`<BooleanLiteral bl>`, Collector c){
//    c.fact(current, abool());
// }
//
//void collect(Pattern current:(Literal)`<DateTimeLiteral dtl>`, Collector c){
//    c.fact(current, adatetime());
//}
//
//void collect(Pattern current:(Literal)`<RationalLiteral rl>`, Collector c){
//    c.fact(current, arat());
//}

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
   // don't collect further down
}

//void collect(current: (RegExp)`\<<Name name>\>`, Collector c) {
//    c.use(name, {variableId()});
//    c.fact(current, astr());
//}
//
//void collect(current: (RegExp)`\<<Name name>:<NamedRegExp* regexps>\>`, Collector c) {
//    c.define("<name>", variableId(), name, defType(astr()));
//    c.fact(current, astr());
//    collect(name, regexps, c);
//}

//void collect(Regexp regExp, Collector c){
//    // ignore other RegExp cases
//}

//void collect(Pattern current:(Literal)`<StringLiteral sl>`, Collector c){
//    c.fact(current, astr());
//    collectParts(current, c);
//}

//void collect(Pattern current:(Literal)`<LocationLiteral ll>`, Collector c){
//    c.fact(current, aloc());
//    collectParts(current, c);
//}

// ---- set pattern

void collect(current: (Pattern) `{ <{Pattern ","}* elements0> }`, Collector c){
    if(size([e | e <- elements0]) == 0){    // TODO rewrite
       c.fact(current, aset(avoid()));
    }
    c.push(patternContainer, "set");
    collect(elements0, c);
    c.pop(patternContainer);
}

AType getPatternType(current: (Pattern) `{ <{Pattern ","}* elements0> }`, AType subjectType, loc scope, Solver s){
    elmType = isSetType(subjectType) ? getSetElementType(subjectType) : avalue();
    setType = aset(s.lubList([getPatternType(p, elmType, scope,s) | p <- elements0]));
    s.fact(current, setType);
    return setType;
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

AType getPatternType(current: (Pattern) `[ <{Pattern ","}* elements0> ]`, AType subjectType, loc scope, Solver s){
    elmType = isListType(subjectType) ? getListElementType(subjectType) : avalue();
    res = alist(s.lubList([getPatternType(p, elmType, scope, s) | p <- elements0]));
    s.fact(current, res);
    return res;
}

// ---- typed variable pattern
            
void collect(current: (Pattern) `<Type tp> <Name name>`, Collector c){
    uname = unescape("<name>");
    c.calculate("typed variable pattern", current, [tp], AType(Solver s){  return s.getType(tp)[label=uname]; });
    if(uname != "_"){
       c.push(patternNames, <uname, getLoc(name)>);
       c.define(uname, formalOrPatternFormal(c), name, defType([tp], AType(Solver s){ return s.getType(tp)[label=uname]; }));
    }
    c.enterScope(current);
        collect(tp, c);
    c.leaveScope(current);
}

void collectAsVarArg(current: (Pattern) `<Type tp> <Name name>`, Collector c){
    uname = unescape("<name>");
    
    if(uname != "_"){
       if(inPatternNames(uname, c)){
          c.use(name, {formalId()});
          c.require("typed variable pattern", current, [tp, name], 
            void (Solver s){
                nameType = alist(s.getType(tp));
                s.requireEqual(name, nameType, error(name, "Expected %t for %q, found %q", nameType, uname, name));
            });
       } else {
          c.push(patternNames, <uname, getLoc(name)>);
          c.define(uname, formalId(), name, defType([tp], AType(Solver s){ return alist(s.getType(tp))[label=uname]; }));
       }
    }
 
   c.fact(current, tp);
   c.enterScope(current);
        collect(tp, c);
   c.leaveScope(current);
}

// ---- qualifiedName pattern: QualifiedName

void collect(current: (Pattern) `<QualifiedName name>`,  Collector c){
    <qualifier, base> = splitQualifiedName(name);
    if(base != "_"){
       if(inPatternNames(base, c)){
          //println("qualifiedName: <name>, useLub, <getLoc(current)>");
          c.useLub(name, {formalId(), nestedFormalId(), patternVariableId()});
          return;
       }
       c.push(patternNames, <base, getLoc(current)>);

       if(isTopLevelParameter(c)){
          c.fact(current, avalue());
          if(!isEmpty(qualifier)) c.report(error(name, "Qualifier not allowed"));
          //println("qualifiedName: <name>, parameter defLub, <getLoc(current)>");
          c.define(base, formalId(), name, defLub([], AType(Solver s) { return avalue(); }));
       } else {
          tau = c.newTypeVar(name);
          c.fact(name, tau); //<====
          if(!isEmpty(qualifier)) c.report(error(name, "Qualifier not allowed"));
          //println("qualifiedName: <name>, defLub, <tau>, <getLoc(current)>");
          c.define(base, formalOrPatternFormal(c), name, defLub([], AType(Solver s) { return s.getType(tau); }));
       }
    } else {
       c.fact(name, avalue());
    }
}

void collectAsVarArg(current: (Pattern) `<QualifiedName name>`,  Collector c){
    <qualifier, base> = splitQualifiedName(name);
    if(base != "_"){
       if(inPatternNames(base, c)){
          //println("qualifiedName: <name>, useLub, <getLoc(current)>");
          c.useLub(name, {formalId()});
          return;
       }
       c.push(patternNames, <base, getLoc(current)>);

       if(isTopLevelParameter(c)){
          c.fact(current, alist(avalue()));
          if(!isEmpty(qualifier)) c.report(error(name, "Qualifier not allowed"));
          //println("qualifiedName: <name>, parameter defLub, <getLoc(current)>");
          c.define(base, formalId(), name, defLub([], AType(Solver s) { return avalue(); }));
       } else {
          tau = c.newTypeVar(name);
          c.fact(name, tau);     //<====
          if(!isEmpty(qualifier)) c.report(error(name, "Qualifier not allowed"));
          //println("qualifiedName: <name>, defLub, <tau>, <getLoc(current)>");
          c.define(base, formalOrPatternFormal(c), name, defLub([], AType(Solver s) { return s.getType(tau); }));
       }
    } else {
       c.fact(name, alist(avalue()));
    }
}

default void collectAsVarArg(Pattern current,  Collector c){
    throw rascalCheckerInternalError(getLoc(current), "<current> not supported in varargs");
}

AType getPatternType(current: (Pattern) `<QualifiedName name>`, AType subjectType, loc scope, Solver s){
    base = prettyPrintBaseName(name);
    if(base != "_"){
       nameType = s.getType(name);
       if(!s.isFullyInstantiated(nameType) || !s.isFullyInstantiated(subjectType)){
          s.requireUnify(nameType, subjectType, error(current, "Type of pattern could not be computed"));
          s.fact(name, nameType); // <====
          nameType = s.instantiate(nameType);
          s.fact(name, nameType);
          subjectType = s.instantiate(subjectType);
          //clearBindings();
       }
       s.requireComparable(nameType, subjectType, error(current, "Pattern should be comparable with %t, found %t", subjectType, nameType));
       return nameType;
    } else
       return subjectType;
}

// ---- multiVariable pattern: QualifiedName*

void collect(current: (Pattern) `<QualifiedName name>*`,  Collector c){
    Pattern pat = (Pattern) `<QualifiedName name>`;
    collectSplicePattern(current, pat, c);
}

AType getPatternType(current: (Pattern) `<QualifiedName name>*`,  AType subjectType, loc scope, Solver s){
    Pattern pat = (Pattern) `<QualifiedName name>`;
    return getSplicePatternType(current, pat, subjectType, scope, s);
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

AType getPatternType(current: (Pattern) `* <Pattern argument>`, AType subjectType, loc scope, Solver s){
    return  getSplicePatternType(current, argument, subjectType, scope, s);
}
    
void collectSplicePattern(Pattern current, Pattern argument,  Collector c){
    inSet = inSetPattern(current, c);
    scope = c.getScope();
    if(argument is typedVariable){
       tp = argument.\type;
       argName = argument.name;
       uname = unescape("<argName>");
       
       if(uname != "_"){
          if(inPatternNames(uname, c)){
             c.use(argName, {formalOrPatternFormal(c)});
             c.require("typed variable in splice pattern", current, [tp, argName], 
                void (Solver s){ 
                    nameType = inSet ? aset(s.getType(tp)) : alist(s.getType(tp));
                    s.requireEqual(argName, nameType, error(argName, "Expected %t for %q, found %q", nameType, uname, argName));
               });
          } else {
            c.push(patternNames, <uname, getLoc(argName)>);
            c.define(uname, formalOrPatternFormal(c), argName, defType([tp], 
               AType(Solver s){ return inSet ? aset(s.getType(tp)) : alist(s.getType(tp)); }));
          }          
       }
       c.calculate("typed variable in splice pattern", current, [tp], AType(Solver s){ return s.getType(tp); });
       collect(tp, c);
    } else if(argument is qualifiedName){
        argName = argument.qualifiedName;
        <qualifier, base> = splitQualifiedName(argName);
        if(base != "_"){
           if(inPatternNames(base, c)){
              //println("qualifiedName: <name>, useLub, <getLoc(current)>");
              c.useLub(argName, variableRoles);
              return;
           }
           c.push(patternNames, <base, getLoc(argument)>);
    
           if(isTopLevelParameter(c)){
              c.fact(current, avalue());
              if(!isEmpty(qualifier)) c.report(error(argName, "Qualifier not allowed"));
              //println("qualifiedName: <name>, parameter defLub, <getLoc(current)>");
              c.define(base, formalId(), argName, defLub([], AType(Solver s) { return avalue(); }));
           } else {
              tau = c.newTypeVar(current); // <== argName;
              c.fact(current, tau);    // <===
              if(!isEmpty(qualifier)) c.report(error(argName, "Qualifier not allowed"));
              c.define(base, formalOrPatternFormal(c), argName, 
                        defLub([], AType(Solver s) { 
                        return inSet ? makeSetType(tau) : makeListType(tau);}));
              //c.define(qname.name, variableId(), argName, 
              //          defLub([], AType(Solver s) { 
              //          tp = getType(tau); return inSet ? makeSetType(expandUserTypes(tp, scope, s) : makeListType(expandUserTypes(tp, scope, s));}));
           }
        } else {
           c.fact(current, avoid());
        }
    } else {
        throw rascalCheckerInternalError(getLoc(current), "collectSplicePattern: not implemented");
        //println("current: <current>");
        //println("argument: <argument>");
        //tp = collectSplicePattern(argument, argument, c);
    }
}

AType getSplicePatternType(Pattern current, Pattern argument,  AType subjectType, loc scope, Solver s){
    if(argument is typedVariable){
       uname = unescape("<argument.name>");
       if(uname == "_"){
          return subjectType;
       } else {
          inameType = s.getType(argument.name);
          s.fact(argument, inameType);
          if(isListType(inameType)) {
                elmType = getListElementType(inameType);
                s.fact(current, elmType);
                return elmType;
          }
          if(isSetType(inameType)){
                elmType =  getSetElementType(inameType);
                s.fact(current, elmType);
                return elmType;
          }
          s.report(error(current, "Cannot get element type for %t", inameType)); 
       }
    } if(argument is qualifiedName){
         argName = argument.qualifiedName;
         base = prettyPrintBaseName(argName);
         if(base != "_"){
           elmType = subjectType;
           //try {
               inameType = s.getType(argument.qualifiedName);
           	   elmType = avoid();
           	   if(isListType(inameType)){
           	        elmType = getListElementType(inameType);
           	   } else if(isSetType(inameType)){
           	        elmType = getSetElementType(inameType);
           	   } else {
           	    s.report(error(argument, "List or set type expected, found %t", inameType));
           	   }
                 
               if(!s.isFullyInstantiated(elmType) || !s.isFullyInstantiated(subjectType)){
                  s.requireUnify(elmType, subjectType, error(current, "Type of pattern could not be computed"));
                  elmType = s.instantiate(elmType);
                  //s.fact(argName, nameElementType);//<<
                  s.fact(current, elmType); // <<
                  subjectType = s.instantiate(elmType);
               }
           //} catch TypeUnavailable(): {
           //     nameElementType = subjectType;
           //    //s.fact(argName, nameElementType); //<<
           //    s.fact(current, nameElementType); //<<
           //}
           //nameElementType = isListType(nameType) ? getListElementType(nameType) : getSetElementType(nameType);
           s.requireComparable(elmType, subjectType, error(current, "Pattern should be comparable with %t, found %t", subjectType, elmType));
           return elmType;
        } else
           return subjectType;
    } else {
        throw rascalCheckerInternalError(getLoc(current), "Not implemented");
    }
}

// ---- splicePlus pattern: +Pattern ------------------------------------------

void collect(current: (Pattern) `+<Pattern argument>`, Collector c){
    collectSplicePattern(current, argument, c);
}

AType getPatternType(current: (Pattern) `+<Pattern argument>`, AType subjectType, loc scope, Solver s)
    = getSplicePatternType(current, argument, subjectType, scope, s);

// ---- tuple pattern ---------------------------------------------------------

void collect(current: (Pattern) `\< <{Pattern ","}+ elements1> \>`, Collector c){
    c.push(patternContainer, "tuple");
    collect(elements1, c);
    c.pop(patternContainer);
}

AType getPatternType(current: (Pattern) `\< <{Pattern ","}* elements1> \>`, AType subjectType, loc scope, Solver s){
    pats = [ p | Pattern p <- elements1 ];
    if(isTupleType(subjectType)){
        elmTypes = getTupleFieldTypes(subjectType);
        if(size(pats) == size(elmTypes)){
           if(tupleHasFieldNames(subjectType)){
             res = atuple(atypeList([getPatternType(pats[i], elmTypes[i], scope, s)[label= elmTypes[i].label] | int i <- index(pats)]));
             return res;
           } else {
             return atuple(atypeList([getPatternType(pats[i], elmTypes[i], scope, s) | int i <- index(pats)]));
           }
        } else {
            s.report(error(current, "Expected tuple pattern with %v elements, found %v",size(elmTypes), size(pats)));
        }
    }
    return atuple(atypeList([getPatternType(pats[i], avalue(), scope, s) | int i <- index(pats)]));
}

void collect(current: (KeywordArgument[Pattern]) `<Name name> = <Pattern expression>`,  Collector c){
    //scope = c.getScope();
    //c.calculate("default expression in pattern", expression, [], AType(Solver s){ return getPatternType(expression, avalue(), scope, s); });
    collect(expression, c);
}

AType getPatternType(current: (KeywordArgument[Pattern]) `<Name name> = <Pattern expression>`, AType subjectType, loc scope, Solver s){
    return getPatternType(expression, subjectType, scope, s);
}

// ---- call or tree pattern --------------------------------------------------

void collect(current: (Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`, Collector c){
    c.push(patternContainer, "constructor");
    if(namePat: (Pattern) `<QualifiedName name>` := expression){
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

AType getPatternType(current: (Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`, AType subjectType, loc scope, Solver s){
   //println("getPatternType: <current>");
    pats = [ p | Pattern p <- arguments ];
    
    texp = getPatternType(expression, subjectType, scope, s);
    //println("bindings: <bindings>");
    //clearBindings();    // <====
    subjectType = s.instantiate(subjectType);
    
    if(isStartNonTerminalType(subjectType)){
        subjectType = getStartNonTerminalType(subjectType);
    }
    
    if(isStrType(texp)){
        return computePatternNodeType(current, scope, pats, keywordArguments, s, subjectType);
    }       
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := texp){
       <filteredOverloads, identicalFields> = filterOverloadedConstructors(overloads, size(pats), subjectType);
       if({<key, idr, tp>} := filteredOverloads){
          texp = tp;
       } else {
         overloads = filteredOverloads;
         validReturnTypeOverloads = {};
         validOverloads = {};
         next_cons:
         for(ovl: <key, idr, tp> <- overloads){
            if(acons(adtType:aadt(adtName, list[AType] parameters, _), list[AType] fields, list[Keyword] kwFields) := tp){
               try {
                     validReturnTypeOverloads += <key, idr, computeADTType(current, adtName, scope, adtType, fields, kwFields, pats, keywordArguments, identicalFields, s)>;
                     validOverloads += ovl;
                    } catch checkFailed(list[FailMessage] fms):
                            continue next_cons;
                      catch NoBinding():
                            continue next_cons;
             }
        }
        if({<key, idr, tp>} := validOverloads){
           texp = tp; 
            // TODO check identicalFields to see whether this can make sense
           // unique overload, fall through to non-overloaded case to potentially bind more type variables
        } else if(isEmpty(validReturnTypeOverloads)) s.report(error(current, "No pattern constructor found for %q of expected type %t", expression, subjectType));
        else return overloadedAType(validReturnTypeOverloads);
      }
    }

    if(acons(adtType:aadt(adtName, list[AType] parameters, _), list[AType] fields, list[Keyword] kwFields) := texp){
       return computeADTType(current, adtName, scope, adtType, fields, kwFields, pats, keywordArguments, [true | int i <- index(fields)], s);
    }
    s.report(error(current, "No pattern constructor found for %q of expected type %t", expression, subjectType));
}

tuple[rel[loc, IdRole, AType], list[bool]] filterOverloadedConstructors(rel[loc, IdRole, AType] overloads, int arity, AType subjectType){
    filteredOverloads = {};
    prevFields = [];
    identicalFields = [true | int i <- [0 .. arity]];
    
    for(ovl:<key, idr, tp> <- overloads){                       
        if(acons(ret:aadt(adtName, list[AType] parameters, _), list[AType] fields, list[Keyword] kwFields) := tp, comparable(ret, subjectType)){
           if(size(fields) == arity){
              filteredOverloads += ovl;
              if(isEmpty(prevFields)){
                 prevFields = fields;
              } else {
                 for(int i <- index(fields)) identicalFields[i] = identicalFields[i] && (comparable(prevFields[i], fields[i]));
              }
            }
        }
    }
    return <filteredOverloads, identicalFields>;
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

AType getPatternType(current: (Pattern) `<Name name> : <Pattern pattern>`,  AType subjectType, loc scope, Solver s){
    return getPatternType(pattern, subjectType, scope, s);
}

// ---- typed variable becomes

void collect(current: (Pattern) `<Type tp> <Name name> : <Pattern pattern>`, Collector c){
    uname = unescape("<name>");
    c.push(patternNames, <uname, name>);
    c.define(uname, formalOrPatternFormal(c), name, defType([tp], AType(Solver s){ return s.getType(tp); }));
    c.fact(current, tp);
    collect(tp, pattern, c);
}

AType getPatternType(current: (Pattern) `<Type tp> <Name name> : <Pattern pattern>`, AType subjectType, loc scope, Solver s){
    declaredType = s.getType(name);
    patType = getPatternType(pattern, subjectType, scope, s);
    s.requireComparable(patType, declaredType, error(current, "Incompatible type in assignment to variable %q, expected %t, found %t", name, declaredType, patType));
    return declaredType;
}

// ---- descendant pattern

void collect(current: (Pattern) `/ <Pattern pattern>`, Collector c){
    c.fact(current, avoid());
    collect(pattern, c);
}

AType getPatternType(current: (Pattern) `/ <Pattern pattern>`, AType subjectType, loc scope, Solver s){
    getPatternType(pattern, avalue(), scope, s);
    return avoid();
}

// ---- negative 
void collect(current: (Pattern) `- <Pattern pattern>`, Collector c){
    collect(pattern, c);
}

AType getPatternType(current: (Pattern) `- <Pattern pattern>`,  AType subjectType, loc scope, Solver s){
    return getPatternType(pattern, subjectType, scope, s);
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

AType getPatternType(current: (Pattern) `( <{Mapping[Pattern] ","}* mps> )`, AType subjectType, loc scope, Solver s){
    return amap(avoid(),avoid()); // TODO
}

//TODO: reifiedType

void collect(current: (Pattern) `type ( <Pattern s>, <Pattern d> )`, Collector c){
    collect(s, d, c);
}

// ---- asType
void collect(current: (Pattern) `[ <Type tp> ] <Pattern p>`, Collector c){
    c.calculate("pattern as type", current, [tp], AType(Solver s){ return s.getType(tp); });
    // TODO:
    //c.require("pattern as type", current, [p],
    //    void (Solver s){ expandedType =  expandUserTypes(declaredType, scope, s); ;
    //        subtype(getType(p), expandedType, onError(p, "Pattern should be subtype of <fmt(expandedType)>, found <fmt(getType(p))>"));
    //    });
    
    collect(tp, p, c);
}

// ---- anti

void collect(current: (Pattern) `! <Pattern pattern>`, Collector c){
    c.fact(current, avoid());
    collect(pattern, c);
}

AType getPatternType(current: (Pattern) `! <Pattern pattern>`, AType subjectType, loc scope, Solver s){
    getPatternType(pattern, avalue(), scope, s);
    return avoid();
}