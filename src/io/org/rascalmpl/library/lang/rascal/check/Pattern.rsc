module lang::rascal::check::Pattern

extend analysis::typepal::TypePal;

import lang::rascal::\syntax::Rascal;
extend lang::rascal::check::AType;
extend lang::rascal::check::ATypeUtils;
import lang::rascal::check::Scope;
import lang::rascal::check::ConvertType;

import lang::rascal::check::Expression;

// ---- literal patterns

void collect(current:(Literal)`<IntegerLiteral il>`, FRBuilder frb){
    frb.fact(current, aint());
}

void collect(current:(Literal)`<RealLiteral rl>`, FRBuilder frb){
    frb.fact(current, areal());
}

void collect(current:(Literal)`<BooleanLiteral bl>`, FRBuilder frb){
    frb.fact(current, abool());
 }

void collect(current:(Literal)`<DateTimeLiteral dtl>`, FRBuilder frb){
    frb.fact(current, adatetime());
}

void collect(current:(Literal)`<RationalLiteral rl>`, FRBuilder frb){
    frb.fact(current, arat());
}

// TODO
//void collect(Literalpat:(Literal)`<RegExpLiteral rl>`, Configuration c) {

void collect(current:(Literal)`<StringLiteral sl>`, FRBuilder frb){
    frb.fact(current, astr());
}

void collect(current:(Literal)`<LocationLiteral ll>`, FRBuilder frb){
    frb.fact(current, aloc());
}

// ---- set pattern

void collect(current: (Pattern) `{ <{Pattern ","}* elements0> }`, FRBuilder frb){
    pats = [ p | Pattern p <- elements0 ];
   
    handleSingleVariablePatterns(pats, frb);
    
    tvElm = frb.newTypeVar();
    frb.calculateEager("set pattern", current, pats, 
        AType() { 
            elmType = lub([typeof(p) | p <- pats]);
            setPatType = aset(lub([typeof(p) | p <- pats]));
            unify(aset(tvElm), setPatType); // bind type variable to elmType
            return aset(tvElm);
        });
     collectParts(current, frb);
}

// ---- list pattern

void collect(current: (Pattern) `[ <{Pattern ","}* elements0> ]`, FRBuilder frb){
    pats = [ p | Pattern p <- elements0 ];
   
    handleSingleVariablePatterns(pats, frb);
    
    tvElm = frb.newTypeVar();
    frb.calculateEager("list pattern", current, pats, 
        AType() { 
            elmType = lub([typeof(p) | p <- pats]);
            listPatType = alist(elmType);
            unify(alist(tvElm), listPatType); // bind type variable to elmType
            return alist(tvElm);
        });
    collectParts(current, frb);
}

// ---- splice pattern

void collect(current: (Pattern) `* <Pattern argument>`, FRBuilder frb){
    checkSplicePattern(current, argument, frb);
}

void checkSplicePattern(Pattern current, Pattern argument, FRBuilder frb){
    if(argument is typedVariable){
        tp = argument.\type;
        name = argument.name;
        declaredType = convertType(tp, frb);
        scope = frb.getScope();
        frb.fact(current, declaredType);
        frb.define("<name>", variableId(), name, defType([], AType(){ return alist(expandUserTypes(declaredType, scope)); }));
    } else
    if(!(argument is qualifiedName)){
        frb.calculateEager("splice pattern", current, [argument], AType(){ return computeSpliceType(typeof(argument)); });
        collectParts(current, frb);
    }
}        

// ---- splice plus pattern

void collect(current: (Pattern) `+<Pattern argument>`, FRBuilder frb){
    checkSplicePattern(current, argument, frb);
}

// ---- typed variable pattern
            
void collect(current: (Pattern) `<Type tp> <Name name>`, FRBuilder frb){
    declaredType = convertType(tp, frb);
    scope = frb.getScope();
    frb.fact(current, declaredType);
    frb.define("<name>", variableId(), name, defType([], AType(){ return expandUserTypes(declaredType, scope); }));
    collectParts(current, frb);
}

void handleSingleVariablePatterns(list[Pattern] pats, FRBuilder frb){
    for(pat <- pats){
       
        if(namePat: (Pattern) `<QualifiedName name>` := pat){
            tau = frb.newTypeVar();
            frb.fact(pat, tau);
            qname = convertName(name);
            if(isQualified(qname)) frb.reportError(name, "Qualifier not allowed");
            frb.define(qname.name, variableId(), name, defLub([], AType() { return typeof(tau); }));
        }
        
        if(splicePat: (Pattern) `<QualifiedName name>*` := pat || // deprecated
           splicePat: (Pattern) `*<QualifiedName name>` := pat){
            tau = frb.newTypeVar();
            frb.fact(pat, tau);
            qname = convertName(name);
            if(isQualified(qname)) frb.reportError(name, "Qualifier not allowed");
            frb.define(qname.name, variableId(), name, defLub([], AType() { return alist(typeof(tau)); }));
        }
      
        if(splicePlusPat: (Pattern) `+<QualifiedName name>` := pat){
            tau = frb.newTypeVar();
            frb.fact(pat, tau);
            qname = convertName(name);
            if(isQualified(qname)) frb.reportError(name, "Qualifier not allowed");
            frb.define(qname.name, variableId(), name, defLub([], AType() { return alist(typeof(tau)); }));
        }
    }
}

// ---- tuple pattern

void collect(current: (Pattern) `\< <{Pattern ","}+ elements1> \>`, FRBuilder frb){
    pats = [ p | Pattern p <- elements1 ];
    for(p <- pats){
        if(namePat: (Pattern) `<QualifiedName name>` := p){
            tau = frb.newTypeVar();
            frb.fact(p, tau);
            qname = convertName(name);
            if(isQualified(qname)) reportError(name, "Qualifier not allowed");
            frb.define(qname.name, variableId(), name, defLub([], AType() { return typeof(tau); }));
        }
    }
    
    frb.calculateEager("tuple pattern", current, pats, 
        AType() { return atuple(atypeList([typeof(p) | p <- pats])); });
    collectParts(current, frb);
}

// ---- call or tree pattern

void collect(current: (Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`, FRBuilder frb){
    
    if(namePat: (Pattern) `<QualifiedName name>` := expression){
        qname = convertName(name);
        if(isQualified(qname)){     
            frb.use_qual([qname.qualifier, qname.name], name, {variableId(), constructorId()}, {dataId()} );
        } else {
            frb.use(name, {variableId(), formalId(), constructorId()});
        }
    }
    
    pats = [ p | Pattern p <- arguments ];
    for(p <- pats){
        if(namePat: (Pattern) `<QualifiedName name>` := p){
            tau = frb.newTypeVar();
            frb.fact(p, tau);
            qname = convertName(name);
            if(isQualified(qname)) {
                frb.define(qname.name, variableId(), name, defLub([], AType() { return typeof(tau); }));
            } else {
                frb.define(qname.name, variableId(), name, defLub([], AType() { return typeof(tau); }));
            }
        }
    }
    
    frb.calculateEager("call or tree pattern", current, pats,
        AType(){        
            if(overloadedAType(rel[Key, AType] overloads) := typeof(expression)){
              
               next_cons:
                 for(<key, tp> <- overloads){
                    if(acons(adtType:aadt(adtName, list[AType] parameters, list[Keyword] common), str consName, lrel[AType fieldType, str fieldName] fields, lrel[AType fieldType, str fieldName, Expression defaultExp] kwFields) := tp){
                        nactuals = size(pats); nformals = size(fields);
                        if(nactuals != nformals) continue next_cons;
                        Bindings bindings = ();
                        for(int i <- index(fields)){
                            if(isFullyInstantiated(typeof(pats[i]))){
                                try {
                                     bindings = matchRascalTypeParams(fields[i].fieldType, typeof(pats[i]), bindings, bindIdenticalVars=true);
                                } catch invalidMatch(str reason): {
                                     continue next_cons;
                                }
                                ifieldType = fields[i].fieldType;
                                try {
                                    println("<i>: <fields[i].fieldType>");
                                    ifieldType = instantiateRascalTypeParams(fields[i].fieldType, bindings);
                                    println("instantiation gives: <ifieldType>");
                                } catch invalidInstantiation(str msg): {
                                    println("instantiation fails");
                                    continue next_cons;
                                }
                                if(!comparable(typeof(pats[i]), ifieldType)) continue next_cons;
                            } else {
                               if(!unify(typeof(pats[i]), fields[i].fieldType)) continue next_cons;
                            }  
                        }
                        try { 
                            checkKwArgs(kwFields, keywordArguments, bindings);
                        } catch checkFailed(set[Message] msgs):
                            continue next_cons;
                        
                        try {
                            return adtType = instantiateRascalTypeParams(adtType, bindings);
                        } catch invalidInstantiation(str msg): {
                            continue next_cons;
                        }
                    }
                }
                reportError(current, "No function or constructor <"<expression>"> for arguments <fmt(pats)>");
            }
        
            if(acons(adtType:aadt(adtName, list[AType] parameters, list[Keyword] common), str consName, lrel[AType fieldType, str fieldName] fields, lrel[AType fieldType, str fieldName, Expression defaultExp] kwFields) := typeof(expression)){
                nactuals = size(pats); nformals = size(fields);
                if(nactuals != nformals){
                    reportError(callOrTreePat, "Expected <nformals> argument<nformals != 1 ? "s" : ""> for `<expression>`, found <nactuals>");
                }
                Bindings bindings = ();
                for(int i <- index(pats)){
                    if(isFullyInstantiated(typeof(pats[i]))){
                       try {
                         bindings = matchRascalTypeParams(fields[i].fieldType, typeof(pats[i]), bindings, bindIdenticalVars=true);
                       } catch invalidMatch(str reason): {
                         reportError(actuals[i], reason);
                       }
                       ifieldType = fields[i].fieldType;
                       try {
                        ifieldType = instantiateRascalTypeParams(fields[i].fieldType, bindings);
                       } catch invalidInstantiation(str msg): {
                            reportError(pats[i], msg);
                       }
                       comparable(typeof(pats[i]), ifieldType, onError(pats[i], "Field <fmt(fields[i].fieldName)> should have type <fmt(ifieldType)>, found <fmt(pats[i])>"));
                    } else {
                        unify(typeof(pats[i]), fields[i].fieldType, onError(pats[i], "Field <fmt(fields[i].fieldName)> should have type <fmt(fields[i].fieldType)>, found <fmt(pats[i])>"));
                    }
                }
              
                checkKwArgs(kwFields, keywordArguments, bindings);
                return adtType;
            }
            reportError(current, "Function or constructor type required for <"<expression>">, found <fmt(expression)>");
        });
     collectParts(current, frb);
}

// ---- variable becomes pattern

void collect(current: (Pattern) `<Name name> : <Pattern pattern>`, FRBuilder frb){
    frb.define("<name>", variableId(), name, defLub([pattern], AType() { return typeof(pattern); }));
    frb.calculateEager("variable becomes", current, [pattern], AType(){ return typeof(pattern); });
    collectParts(current, frb);
}

// ---- typed variable becomes

void collect(current: (Pattern) `<Type tp> <Name name> : <Pattern pattern>`, FRBuilder frb){
    declaredType = convertType(tp, frb);
    scope = frb.getScope();
    frb.define("<name>", variableId(), name, defType([], AType(){ return expandUserTypes(declaredType, scope); }));
    frb.fact(current, declaredType);
    tau = frb.newTypeVar();
    frb.fact(pattern, tau);
    frb.requireEager("typed variable becomes", current, [pattern],
        () { subtype(typeof(pattern), declaredType, onError(pattern, "Incompatible type in assignment to variable `<name>`, expected <fmt(declaredType)>, found <fmt(pattern)>"));
        });
    collectParts(current, frb);
}

// ---- descendant pattern

void collect(current: (Pattern) `/ <Pattern pattern>`, FRBuilder frb){
    frb.fact(current, avalue());
    collectParts(current, frb);
}

//TODO: negative 
//TODO: map
//TODO: reifiedType
//TODO: asType
//TODO: anti
