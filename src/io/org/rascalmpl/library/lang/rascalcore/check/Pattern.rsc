module lang::rascalcore::check::Pattern

extend analysis::typepal::TypePal;

import lang::rascal::\syntax::Rascal;
extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::Scope;
import lang::rascalcore::check::ConvertType;

import lang::rascalcore::check::Expression;

// Some utilities on patterns

set[str] getAllNames(Pattern p)
    = { "<name>" | /(Pattern) `<Type tp> <Name name>` := p } + { "<name>" | /QualifiedName name := p } - {"_"};

// ---- literal patterns

void collect(Pattern current:(Literal)`<IntegerLiteral il>`, TBuilder tb){
    tb.fact(current, aint());
}

AType getPatternType(Pattern current:(Literal)`<IntegerLiteral il>`, AType subjectType)
    = getType(current);

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
    tb.use(name, variableId());
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
    pats = [ p | Pattern p <- elements0 ];
    collectPatternElements(pats, tb, inSet=true);
    
    tb.calculateEager("set pattern", current, pats, 
        AType() { 
            return aset(lub([getType(p) | p <- pats]));
        });
}

// ---- list pattern

void collect(current: (Pattern) `[ <{Pattern ","}* elements0> ]`, TBuilder tb){
    pats = [ p | Pattern p <- elements0 ];
   
    collectPatternElements(pats, tb);
    
    tb.calculateEager("list pattern", current, pats, 
        AType() {
            return alist(lub([getType(p) | p <- pats]));
        });
}

AType getPatternType(current: (Pattern) `[ <{Pattern ","}* elements0> ]`, AType subjectType){
    if(isListType(subjectType)){
        elmType = getListElementType(subjectType);
        return alist(lub([getPatternType(p, elmType) | p <- elements0]));
    }
    return subjectType;
}

// ---- splice pattern

//void collect(current: (Pattern) `* <Pattern argument>`, TBuilder tb){
//    checkSplicePattern(current, argument, tb);
//}

void checkSplicePattern(Pattern current, Pattern argument, TBuilder tb, bool inSet = false){
    if(argument is typedVariable){
        tp = argument.\type;
        name = argument.name;
        declaredType = convertType(tp, tb);
        scope = tb.getScope();
        tb.calculate("splice pattern", current, [], AType(){ return expandUserTypes(declaredType, scope); });
        tb.define(unescape("<name>"), variableId(), name, defType([], 
            AType(){ return inSet ? aset(expandUserTypes(declaredType, scope)) : alist(expandUserTypes(declaredType, scope)); }));
    } else
    if(!(argument is qualifiedName)){
        tb.calculateEager("splice pattern", current, [argument], AType(){ return computeSpliceType(getType(argument)); });
        collectParts(current, tb);
    }
}        

// ---- splice plus pattern

//void collect(current: (Pattern) `+<Pattern argument>`, TBuilder tb){
//    checkSplicePattern(current, argument, tb);
//}

// ---- typed variable pattern
            
void collect(current: (Pattern) `<Type tp> <Name name>`, TBuilder tb){
    declaredType = convertType(tp, tb);
    scope = tb.getScope();
    tb.calculate("typed variable pattern", current, [], 
        AType(){ 
            return expandUserTypes(declaredType, scope); });
    uname = unescape("<name>");
    if(uname != "_") tb.define(uname, variableId(), name, defType([], 
        AType(){ 
            return expandUserTypes(declaredType, scope); }));
    collectParts(current, tb);
}

void collectPatternElements(list[Pattern] pats, TBuilder tb, bool inSet = false){
    previousNames = {};
    for(pat <- pats){
       
        if(namePat: (Pattern) `<QualifiedName name>` := pat){
           qname = convertName(name);
           if(qname.name != "_"){
              if("<name>" in previousNames){
                tb.use(name, {variableId()});
              } else {
                previousNames += "<name>";
                tau = tb.newTypeVar();
                tb.fact(pat, tau);
                if(isQualified(qname)) tb.reportError(name, "Qualifier not allowed");
                tb.define(qname.name, variableId(), name, defLub([], AType() { return getType(tau); }));
              }
           } else {
             tb.fact(pat, avalue());
           }
        } else
        
        if(splicePat: (Pattern) `<QualifiedName name>*` := pat || // deprecated
           splicePat: (Pattern) `*<QualifiedName name>` := pat){
           qname = convertName(name);
           if(qname.name != "_"){
              if("<name>" in previousNames){
                 tb.use(name, {variableId()});
              } else {
                 previousNames += "<name>";
                 tau = tb.newTypeVar();
                 tb.fact(pat, tau); 
                 if(isQualified(qname)) tb.reportError(name, "Qualifier not allowed");
                 tb.define(qname.name, variableId(), name, defLub([], 
                    AType() { 
                        tp = getType(tau);
                        return inSet ? aset(tp) : alist(tp); 
                    }));
              }
           } else {
             tb.fact(pat, avalue());
           } 
        } else
      
        if(splicePlusPat: (Pattern) `+<QualifiedName name>` := pat){
           qname = convertName(name);
           if(qname.name != "_"){
              if("<name>" in previousNames){
                 tb.use(name, {variableId()});
              } else {
                 previousNames += "<name>";
                 tau = tb.newTypeVar();
                 tb.fact(pat, tau);
            
                 if(isQualified(qname)) tb.reportError(name, "Qualifier not allowed");
                 tb.define(qname.name, variableId(), name, defLub([], AType() { return inSet ? aset(getType(tau)) : alist(getType(tau)); }));
              }
           } else {
             tb.fact(pat, avalue());
           }
        } else if((Pattern) `* <Pattern argument>` := pat ||  (Pattern) `+<Pattern argument>` := pat){
            checkSplicePattern(pat, argument, tb, inSet=inSet);
        } else {
           collect(pat, tb);
           previousNames += getAllNames(pat);
        }
    }
}

// ---- tuple pattern

void collect(current: (Pattern) `\< <{Pattern ","}+ elements1> \>`, TBuilder tb){
    pats = [ p | Pattern p <- elements1 ];
    for(p <- pats){
        if(namePat: (Pattern) `<QualifiedName name>` := p){
           qname = convertName(name);
           //if(qname.name != "_"){
              tau = tb.newTypeVar();
              tb.fact(p, tau);
              if(isQualified(qname)) reportError(name, "Qualifier not allowed");
              tb.define(qname.name, variableId(), name, defLub([], AType() { return getType(tau); }));
           //} 
           //else {
           //     tb.fact(name, avalue());
           //}
        }
    }
    
    //collectPatternElements(pats, tb);
      
    tb.calculateEager("tuple pattern", current, pats, 
        AType() { return atuple(atypeList([getType(p) | p <- pats])); });
    collectParts(current, tb);
}

// ---- call or tree pattern

void collect(current: (Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`, TBuilder tb){
    
    if(namePat: (Pattern) `<QualifiedName name>` := expression){
        qname = convertName(name);
        //if(qname.name != "_"){
            if(isQualified(qname)){     
               tb.useQualified([qname.qualifier, qname.name], name, {constructorId()}, {dataId(), nonterminalId()} );
            } else {
                tb.use(name, {constructorId()});
            }
        //} else {
        //        tb.fact(name, avalue());
        //   }
    }
    
    previousNames = {};
    pats = [ p | Pattern p <- arguments ];
    scope = tb.getScope();
    collectPatternElements(pats, tb);
    collect(keywordArguments, tb);
    
    tb.calculateEager("call or tree pattern", current, expression + pats,
        AType(){  
            texp = getType(expression);
            if(isStrType(texp)){
                return anode();
            }         
            if(overloadedAType(rel[Key, IdRole, AType] overloads) := texp){
               validOverloads = {};
               next_cons:
                 for(<key, idr, tp> <- overloads){
                    if(acons(adtType:aadt(adtName, list[AType] parameters), str consName, lrel[AType fieldType, str fieldName] fields, lrel[AType fieldType, str fieldName, Expression defaultExp] kwFields) := tp){
                        nactuals = size(pats); nformals = size(fields);
                        if(nactuals != nformals) continue next_cons;
                        Bindings bindings = ();
                        for(int i <- index(fields)){
                            if("<pats[i]>" != "_"){
                                type_expanded_pat_i =  expandUserTypes(getType(pats[i]), scope);
                                if(isFullyInstantiated(type_expanded_pat_i)){
                                    try {
                                         bindings = matchRascalTypeParams(fields[i].fieldType, type_expanded_pat_i, bindings, bindIdenticalVars=true);
                                    } catch invalidMatch(str reason): {
                                         continue next_cons;
                                    }
                                    ifieldType = fields[i].fieldType;
                                    
                                    try {
                                       // println("<i>: <fields[i].fieldType>");
                                        ifieldType = instantiateRascalTypeParams(fields[i].fieldType, bindings);
                                        //println("instantiation gives: <ifieldType>");
                                    } catch invalidInstantiation(str msg): {
                                        //println("instantiation fails");
                                        continue next_cons;
                                    }
                                    ifieldType = expandUserTypes(ifieldType, scope);
                                    if(!comparable(type_expanded_pat_i, ifieldType)) continue next_cons;
                                } else {
                                   //unify(type_expanded_pat_i, fields[i].fieldType); // Too Agressive
                                   validOverloads += <key, idr, tp>;
                                } 
                            } 
                        }
                        try { 
                            checkKwArgs(kwFields, keywordArguments, bindings, scope);
                        } catch checkFailed(set[Message] msgs):
                            continue next_cons;
                        
                        try {
                            validOverloads += <key, idr, /*acons(*/instantiateRascalTypeParams(adtType, bindings)/*, consName, fields, kwFields)*/>;
                        } catch invalidInstantiation(str msg): {
                            continue next_cons;
                        }
                    }
                }
                if(isEmpty(validOverloads)) reportError(current, "No function or constructor <"<expression>"> for arguments <fmt(pats)>");
                return overloadedAType(validOverloads);
            }
        
            if(acons(adtType:aadt(adtName, list[AType] parameters), str consName, lrel[AType fieldType, str fieldName] fields, lrel[AType fieldType, str fieldName, Expression defaultExp] kwFields) := getType(expression)){
                nactuals = size(pats); nformals = size(fields);
                if(nactuals != nformals){
                    reportError(current, "Expected <nformals> argument<nformals != 1 ? "s" : ""> for `<expression>`, found <nactuals>");
                }
                Bindings bindings = ();
                for(int i <- index(pats)){
                    if("<pats[i]>" != "_"){
                        type_expanded_pat_i =  expandUserTypes(getType(pats[i]), scope);
                        if(isFullyInstantiated(type_expanded_pat_i)){
                           try {
                             bindings = matchRascalTypeParams(fields[i].fieldType, type_expanded_pat_i, bindings, bindIdenticalVars=true);
                           } catch invalidMatch(str reason): {
                             reportError(actuals[i], reason);
                           }
                           ifieldType = fields[i].fieldType;
                           try {
                            ifieldType = instantiateRascalTypeParams(fields[i].fieldType, bindings);
                           } catch invalidInstantiation(str msg): {
                                reportError(pats[i], msg);
                           }
                           ifieldType = expandUserTypes(ifieldType, scope);
                           comparable(type_expanded_pat_i, ifieldType, onError(pats[i], "Field <fmt(fields[i].fieldName)> should have type <fmt(ifieldType)>, found <fmt(type_expanded_pat_i)>"));
                        } else {
                            unify(type_expanded_pat_i, fields[i].fieldType, onError(pats[i], "Field <fmt(fields[i].fieldName)> should have type <fmt(fields[i].fieldType)>, found <fmt(type_expanded_pat_i)>"));
                            //instantiate(type_expanded_pat_i);
                        }
                    }
                }
              
                checkKwArgs(kwFields, keywordArguments, bindings, scope);
                return adtType;
            }
            reportError(current, "Function or constructor type required for <"<expression>">, found <fmt(expression)>");
        });
}

// ---- variable becomes pattern

void collect(current: (Pattern) `<Name name> : <Pattern pattern>`, TBuilder tb){
    tb.define(unescape("<name>"), variableId(), name, defLub([pattern], AType() { return getType(pattern); }));
    tb.calculateEager("variable becomes", current, [pattern], AType(){ return getType(pattern); });
    collectParts(current, tb);
}

// ---- typed variable becomes

void collect(current: (Pattern) `<Type tp> <Name name> : <Pattern pattern>`, TBuilder tb){
    declaredType = convertType(tp, tb);
    scope = tb.getScope();
    tb.define(unescape("<name>"), variableId(), name, defType([], AType(){ return expandUserTypes(declaredType, scope); }));
    tb.calculate("typed variable becomes", current, [], AType(){ return expandUserTypes(declaredType, scope); });
    tau = tb.newTypeVar();
    tb.fact(pattern, tau);
    tb.require("typed variable becomes", current, [pattern],
        () { expandedType = expandUserTypes(declaredType, scope);
             expandedPatType = expandUserTypes(getType(pattern), scope);
             subtype(expandedPatType, expandedType, onError(pattern, "Incompatible type in assignment to variable `<name>`, expected <fmt(expandedType)>, found <fmt(pattern)>"));
        });
    collectParts(current, tb);
}

// ---- descendant pattern

void collect(current: (Pattern) `/ <Pattern pattern>`, TBuilder tb){
    tb.fact(current, avoid());
    //tb.calculate("descandant pattern", current, [pattern], AType() { return getType(pattern); } );
    collectParts(current, tb);
}

// ---- negative 
void collect(current: (Pattern) `- <Pattern pattern>`, TBuilder tb){
    collectParts(current, tb);
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
