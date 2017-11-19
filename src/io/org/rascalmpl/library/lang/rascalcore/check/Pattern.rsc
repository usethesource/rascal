module lang::rascalcore::check::Pattern

extend analysis::typepal::TypePal;

import lang::rascal::\syntax::Rascal;
extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::Scope;
import lang::rascalcore::check::ConvertType;

import lang::rascalcore::check::Expression;

public str patternContainer = "patternContainer";
public str patternNames     = "patternNames";

// Some utilities on patterns

set[str] getAllNames(Pattern p)
    = { "<name>" | /(Pattern) `<Type tp> <Name name>` := p } + { "<name>" | /QualifiedName name := p } - {"_"};
    
void beginPatternScope(str name, TBuilder tb){
    tb.clearStack(patternNames);
    tb.push(patternContainer, name);
}

void endPatternScope(TBuilder tb){
    tb.pop(patternContainer);
    tb.clearStack(patternNames);
}

// ---- literal patterns

default AType getPatternType(Pattern p, AType subjectType, Key scope){
    return getType(p);
}

void collect(Pattern current:(Literal)`<IntegerLiteral il>`, TBuilder tb){
    tb.fact(current, aint());
}

void collect(Pattern current:(Literal)`<RealLiteral rl>`, TBuilder tb){
    tb.fact(current, areal());
}

void collect(Pattern current:(Literal)`<BooleanLiteral bl>`, TBuilder tb){
    tb.fact(current, abool());
 }

void collect(Pattern current:(Literal)`<DateTimeLiteral dtl>`, TBuilder tb){
    tb.fact(current, adatetime());
}

void collect(Pattern current:(Literal)`<RationalLiteral rl>`, TBuilder tb){
    tb.fact(current, arat());
}

void collect(current: (RegExpLiteral)`/<RegExp* regexps>/<RegExpModifier modifier>`,TBuilder tb) {
    tb.fact(current, astr());
    collectLexicalParts(current, tb);
}

void collect(current:(RegExp)`\<<Name name>\>`, TBuilder tb) {
    tb.use(name, {variableId()});
    tb.fact(current, astr());
}

void collect(current: (RegExp)`\<<Name name>:<NamedRegExp* regexps>\>`, TBuilder tb) {
    tb.define(unescape("<name>"), variableId(), name, defType(astr()));
    tb.fact(current, astr());
    collectLexicalParts(current, tb);
}

void collect(Pattern current:(Literal)`<StringLiteral sl>`, TBuilder tb){
    tb.fact(current, astr());
    collectParts(current, tb);
}

void collect(Pattern current:(Literal)`<LocationLiteral ll>`, TBuilder tb){
    tb.fact(current, aloc());
    collectParts(current, tb);
}

// ---- set pattern

void collect(current: (Pattern) `{ <{Pattern ","}* elements0> }`, TBuilder tb){
    tb.push(patternContainer, "set");
    collectParts(current, tb);
    tb.pop(patternContainer);
}

AType getPatternType(current: (Pattern) `{ <{Pattern ","}* elements0> }`, AType subjectType, Key scope){
    elmType = isSetType(subjectType) ? getSetElementType(subjectType) : avalue();
    return aset(lub([getPatternType(p, elmType, scope) | p <- elements0]));
}

// ---- list pattern

void collect(current: (Pattern) `[ <{Pattern ","}* elements0> ]`, TBuilder tb){
    tb.push(patternContainer, "list");
    collectParts(current, tb);
    tb.pop(patternContainer);
}

AType getPatternType(current: (Pattern) `[ <{Pattern ","}* elements0> ]`, AType subjectType, Key scope){
    elmType = isListType(subjectType) ? getListElementType(subjectType) : avalue();
    return alist(lub([getPatternType(p, elmType, scope) | p <- elements0]));
}

// ---- typed variable pattern
            
void collect(current: (Pattern) `<Type tp> <Name name>`, TBuilder tb){
    declaredType = convertType(tp, tb);
    scope = tb.getScope();
       tb.calculate("typed variable pattern", current, [], 
        AType(){ 
            return expandUserTypes(declaredType, scope); });
    uname = unescape("<name>");
    if(uname != "_"){
       tb.push(patternNames, uname);
       tb.define(uname, variableId(), name, defType([], AType(){ return expandUserTypes(declaredType, scope); }));
    }
}

// ---- QualifiedName

void collect(current: (Pattern) `<QualifiedName name>`,  TBuilder tb){
    qname = convertName(name);
       if(qname.name != "_"){
       if(qname.name in tb.getStack(patternNames)){
          //println("qualifiedName: <name>, useLub, <getLoc(current)>");
          tb.useLub(name, {variableId()});
          return;
       }
       tb.push(patternNames, qname.name);

       if("parameter" := tb.top(patternContainer)){
          tb.fact(current, avalue());
          if(isQualified(qname)) tb.reportError(name, "Qualifier not allowed");
          //println("qualifiedName: <name>, parameter defLub, <getLoc(current)>");
          tb.define(qname.name, formalId(), name, defLub([], AType() { return avalue(); }));
       } else {
          tau = tb.newTypeVar();
          tb.fact(name, tau);
          if(isQualified(qname)) tb.reportError(name, "Qualifier not allowed");
          //println("qualifiedName: <name>, defLub, <tau>, <getLoc(current)>");
          tb.define(qname.name, variableId(), name, defLub([], AType() { return getType(tau); }));
       }
    } else {
       tb.fact(name, avalue());
    }
}

AType getPatternType(current: (Pattern) `<QualifiedName name>`, AType subjectType, Key scope){
    qname = convertName(name);
    if(qname.name != "_"){
       //nameType = expandUserTypes(getType(name), scope);
       nameType = getType(name);
       if(!isFullyInstantiated(nameType) || !isFullyInstantiated(subjectType)){
          unify(nameType, subjectType) || reportError(current, "Type of pattern could not be computed");
          nameType = instantiate(nameType);
          fact(name, nameType);
          subjectType = instantiate(subjectType);
          //clearBindings();
       }
       comparable(nameType, subjectType) || reportError(current, "Pattern should be comparable with <fmt(subjectType)>, found <fmt(nameType)>");
       return nameType;
    } else
       return subjectType;
}

// ---- QualifiedName*

void collect(current: (Pattern) `<QualifiedName name>*`,  TBuilder tb){
    collectSplicePattern(current, name, tb);
}

AType getPatternType(current: (Pattern) `<QualifiedName name>*`,  AType subjectType, Key scope){
    return getSplicePatternType(current, name, subjectType, scope);
}

bool inSetPattern(Pattern current, TBuilder tb){
    container = tb.top(patternContainer);
    switch(container){
        case "list": return false;
        case "set":  return true;
        default:
            reportError(current, "Splice operator not allowed inside a <fmt(container)> pattern");
    }
}

void collectSplicePattern(Pattern current, QualifiedName name,  TBuilder tb){ 
   qname = convertName(name);
   if(qname.name != "_"){
      if(qname.name in tb.getStack(patternNames)){
         tb.useLub(name, {variableId()});
         return;
      }
      tb.push(patternNames, qname.name);
      if("parameter" := tb.top(patternContainer)){
         tb.fact(current, avalue());
         if(isQualified(qname)) tb.reportError(name, "Qualifier not allowed");
         tb.define(qname.name, formalId(), name, defLub([], AType() { return alist(avalue()); }));
      } else {
         tau = tb.newTypeVar();
         inSet = inSetPattern(current, tb);
         tb.fact(current, tau); 
         if(isQualified(qname)) tb.reportError(name, "Qualifier not allowed");
         tb.define(qname.name, variableId(), name, defLub([], AType() { tp = getType(tau); return inSet ? aset(tp) : alist(tp); }));
      }
   } else {
         tb.fact(current, avalue());
   } 
}

AType getSplicePatternType(Pattern current, QualifiedName name,  AType subjectType, Key scope){
    qname = convertName(name);
    if(qname.name != "_"){
       patType = getType(current);
       if(!isFullyInstantiated(patType)){
            unify(patType, subjectType) || reportError(current, "Cannot unify <fmt(nameType)> with <fmt(subjectType)>");
            patType = instantiate(patType);
            //clearBindings();
       }
       comparable(patType, subjectType) || reportError(current, "Pattern should be comparable with <fmt(subjectType)>, found <fmt(patType)>");
       //inameType = instantiate(nameType);
       return patType;
    } else
        return subjectType;
}

// ---- *QualifiedName

void collect(current: (Pattern) `*<QualifiedName name>`, TBuilder tb){
    collectSplicePattern(current, name, tb);
}

AType getPatternType(current: (Pattern) `*<QualifiedName name>`, AType subjectType, Key scope)
    = getSplicePatternType(current, name, subjectType, scope);
    
// ---- +QualifiedName

void collect(current: (Pattern) `+<QualifiedName name>`, TBuilder tb){
    collectSplicePattern(current, name, tb);
}

AType getPatternType(current: (Pattern) `+<QualifiedName name>`, AType subjectType, Key scope)
    = getSplicePatternType(current, name, subjectType, scope);
 
 // ---- splice pattern

void collect(current: (Pattern) `* <Pattern argument>`, TBuilder tb){
    collectSplicePattern(current, argument, tb);
}

AType getPatternType(current: (Pattern) `* <Pattern argument>`, AType subjectType, Key scope)
    = getSplicePatternType(current, argument, subjectType, scope);
    
void collectSplicePattern(Pattern current, Pattern argument,  TBuilder tb){
    if(argument is typedVariable){
       tp = argument.\type;
       name = argument.name;
       uname = unescape("<name>");
       declaredType = convertType(tp, tb);
       scope = tb.getScope();
       inSet = inSetPattern(current, tb);
       if(uname != "_"){
          tb.push(patternNames, uname);
          tb.define(uname, variableId(), name, defType([], 
                    AType(){ return inSet ? aset(expandUserTypes(declaredType, scope)) : alist(expandUserTypes(declaredType, scope)); }));
       }
    } if(argument is QualifiedName || argument is typedVariable){
        ;   // handled by QualfiedName rules
    } else {
        throw "Not implemented: <current>";
    }
}

AType getSplicePatternType(Pattern current, Pattern argument,  AType subjectType, Key scope){
    if(argument is typedVariable){
       inameType = getType(argument.name);
       if(isListType(inameType)) return getListElementType(inameType);
       if(isSetType(inameType)) return getSetElementType(inameType);
       reportError(current, "Cannot get element type for <fmt(inameType)>"); 
    } if(argument is QualifiedName){
        ;   // handled by QualfiedName rules
    } else {
        throw "Not implemented: <current>";
    }
}

// ---- splice plus pattern

void collect(current: (Pattern) `+<Pattern argument>`, TBuilder tb){
    collectSplicePattern(current, argument, tb);
}

AType getPatternType(current: (Pattern) `+<Pattern argument>`, AType subjectType, Key scope)
    = getSplicePatternType(current, argument, subjectType, scope);

// ---- tuple pattern

void collect(current: (Pattern) `\< <{Pattern ","}+ elements1> \>`, TBuilder tb){
    tb.push(patternContainer, "tuple");
    collectParts(current, tb);
    tb.pop(patternContainer);
}

AType getPatternType(current: (Pattern) `\< <{Pattern ","}* elements1> \>`, AType subjectType, Key scope){
    pats = [ p | Pattern p <- elements1 ];
    if(isTupleType(subjectType)){
        elmTypes = getTupleFieldTypes(subjectType);
        if(size(pats) == size(elmTypes)){
           return atuple(atypeList([getPatternType(pats[i], elmTypes[i], scope) | int i <- index(pats)]));
        } else {
            reportError(current, "Expected tuple pattern with <fmt(size(elmTypes))> elements, found <size(pats)>");
        }
    }
    return atuple(atypeList([getPatternType(pats[i], avalue(), scope) | int i <- index(pats)]));
}

void collect(current: (KeywordArgument[Pattern]) `<Name name> = <Pattern expression>`,  TBuilder tb){
    scope = tb.getScope();
    tb.calculate("default expression in pattern", expression, [], AType(){ return getPatternType(expression, avalue(), scope); });
    collect(expression, tb);
}

// ---- call or tree pattern

void collect(current: (Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`, TBuilder tb){
    tb.push(patternContainer, "constructor");
    if(namePat: (Pattern) `<QualifiedName name>` := expression){
        qname = convertName(name);
        if(isQualified(qname)){     
           tb.useQualified([qname.qualifier, qname.name], name, {constructorId()}, {dataId(), nonterminalId()} );
        } else {
            tb.useLub(name, {constructorId()});
        }
    } else {
      collect(expression, tb);
    }
    collect(arguments, tb);
    collect(keywordArguments, tb);
    tb.pop(patternContainer);
}

AType getPatternType(current: (Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`, AType subjectType, Key scope){
    pats = [ p | Pattern p <- arguments ];
    
    texp = getPatternType(expression, subjectType, scope);
    clearBindings();
    subjectType = instantiate(subjectType);
    
    if(isStrType(texp) && comparable(anode(),subjectType)){
        return anode(); // TODO force processing of arguments/keywords
    }       
    if(overloadedAType(rel[Key, IdRole, AType] overloads) := texp){
       <filteredOverloads, identicalFields> = filterOverloadedConstructors(overloads, size(pats), subjectType);
       if({<key, idr, tp>} := filteredOverloads){
          texp = tp;
       } else {
         overloads = filteredOverloads;
         validReturnTypeOverloads = {};
         validOverloads = {};
         next_cons:
         for(ovl: <key, idr, tp> <- overloads){
            if(acons(adtType:aadt(adtName, list[AType] parameters), str consName, list[NamedField] fields, list[Keyword] kwFields) := tp){
               try {
                     validReturnTypeOverloads += <key, dataId(), computeADTType(current, adtName, scope, adtType, fields<1>, kwFields, pats, keywordArguments, identicalFields)>;
                     validOverloads += ovl;
                    } catch checkFailed(set[Message] msgs):
                            continue next_cons;
             }
        }
        if({<key, idr, tp>} := validOverloads){
           texp = tp; 
            // TODO check identicalFields to see whether this can make sense
           // unique overload, fall through to non-overloaded case to potentially bind more type variables
        } else if(isEmpty(validReturnTypeOverloads)) reportError(current, "No pattern constructor found for `<"<expression>">` of expected type <fmt(subjectType)>");
        else return overloadedAType(validReturnTypeOverloads);
      }
    }

    if(acons(adtType:aadt(adtName, list[AType] parameters), str consName, list[NamedField] fields, list[Keyword] kwFields) := texp){
       return computeADTType(current, adtName, scope, adtType, fields<1>, kwFields, pats, keywordArguments, [true | int i <- index(fields)]);
    }
    reportError(current, "No pattern constructor found for <"<expression>"> of expected type <fmt(subjectType)>");
}

tuple[rel[Key, IdRole, AType], list[bool]] filterOverloadedConstructors(rel[Key, IdRole, AType] overloads, int arity, AType subjectType){
    filteredOverloads = {};
    prevFields = [];
    identicalFields = [true | int i <- [0 .. arity]];
    
    for(ovl:<key, idr, tp> <- overloads){                       
        if(acons(ret:aadt(adtName, list[AType] parameters), str consName, list[NamedField] fields, list[Keyword] kwFields) := tp, comparable(ret, subjectType)){
           if(size(fields) == arity){
              filteredOverloads += ovl;
              if(isEmpty(prevFields)){
                 prevFields = fields<1>;
              } else {
                 for(int i <- index(fields)) identicalFields[i] = identicalFields[i] && (comparable(prevFields[i], fields[i].fieldType));
              }
            }
        }
    }
    return <filteredOverloads, identicalFields>;
}

// ---- variable becomes pattern

void collect(current: (Pattern) `<Name name> : <Pattern pattern>`, TBuilder tb){
    uname = unescape("<name>");
    if(uname in tb.getStack(patternNames)){
        tb.useLub(name, {variableId()});
    } else {
        tb.push(patternNames, uname);
        scope = tb.getScope();
        tb.define(uname, variableId(), name, defLub([pattern], AType() { return getPatternType(pattern, avalue(), scope); }));
    }
    collect(pattern, tb);
}

AType getPatternType(current: (Pattern) `<Name name> : <Pattern pattern>`,  AType subjectType, Key scope){
    return getPatternType(pattern, subjectType, scope);
}

// ---- typed variable becomes

void collect(current: (Pattern) `<Type tp> <Name name> : <Pattern pattern>`, TBuilder tb){
    declaredType = convertType(tp, tb);
    scope = tb.getScope();
    uname = unescape("<name>");
    tb.push(patternNames, uname);
    tb.define(uname, variableId(), name, defType([], AType(){ return expandUserTypes(declaredType, scope); }));
    collectParts(current, tb);
}

AType getPatternType(current: (Pattern) `<Type tp> <Name name> : <Pattern pattern>`, AType subjectType, Key scope){
    declaredType = getType(name);
    patType = getPatternType(pattern, subjectType, scope);
    subtype(patType, declaredType) || reportError(current, "Incompatible type in assignment to variable `<name>`, expected <fmt(declaredType)>, found <patType>");
    return declaredType;
}

// ---- descendant pattern

void collect(current: (Pattern) `/ <Pattern pattern>`, TBuilder tb){
    tb.fact(current, avoid());
    collectParts(current, tb);
}

AType getPatternType(current: (Pattern) `/ <Pattern pattern>`, AType subjectType, Key scope){
    getPatternType(pattern, avalue(), scope);
    return avoid();
}

// ---- negative 
void collect(current: (Pattern) `- <Pattern pattern>`, TBuilder tb){
    collectParts(current, tb);
}

AType getPatternType(current: (Pattern) `- <Pattern pattern>`,  AType subjectType, Key scope){
    return getPatternType(pattern, subjectType, scope);
}

//TODO: map

void collect(current: (Pattern) `( <{Mapping[Pattern] ","}* mps> )`, TBuilder tb){
    collectParts(current, tb);
}
//TODO: reifiedType

void collect(current: (Pattern) `type ( <Pattern s>, <Pattern d> )`, TBuilder tb){
    collectParts(current, tb);
}

// ---- asType
void collect(current: (Pattern) `[ <Type tp> ] <Pattern p>`, TBuilder tb){
    declaredType = convertType(tp, tb);
    scope = tb.getScope();
    tb.calculate("pattern as type", current, [], AType(){ return expandUserTypes(declaredType, scope); });
    // TODO:
    //tb.require("pattern as type", current, [p],
    //    (){ expandedType =  expandUserTypes(declaredType, scope); ;
    //        subtype(getType(p), expandedType, onError(p, "Pattern should be subtype of <fmt(expandedType)>, found <fmt(getType(p))>"));
    //    });
    
    collectParts(current, tb);
}

//TODO: anti

void collect(current: (Pattern) `! <Pattern pattern>`, TBuilder tb){
    collectParts(current, tb);
}
