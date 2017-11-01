module lang::rascalcore::check::Expression

extend analysis::typepal::TypePal;

import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::Scope;
import lang::rascalcore::check::ATypeInstantiation;

import lang::rascal::\syntax::Rascal;
import ListRelation;

// ---- Rascal literals
void collect(Literal l:(Literal)`<IntegerLiteral il>`, TBuilder tb){
    tb.fact(l, aint());
}

void collect(Literal l:(Literal)`<RealLiteral rl>`, TBuilder tb){
    tb.fact(l, areal());
}

void collect(Literal l:(Literal)`<BooleanLiteral bl>`, TBuilder tb){
    tb.fact(l, abool());
 }

void collect(Literal l:(Literal)`<DateTimeLiteral dtl>`, TBuilder tb){
    tb.fact(l, adatetime());
}

void collect(Literal l:(Literal)`<RationalLiteral rl>`, TBuilder tb){
    tb.fact(l, arat());
}

//void collect(Literal l:(Literal)`<RegExpLiteral rl>`, TBuilder tb) {
//    println("collectA: <l>");
//    tb.fact(l, astr());
//    collectParts(l, tb);
//}
//
//void collect(current:(RegExp)`\<<Name name>\>`, TBuilder tb) {
//    println("collectB: <current>");
//    tb.use(name, variableId());
//    tb.fact(current, astr());
//}
//
//void collect(current:(RegExp)`\<<Name name>:<NamedRegExp* regexps>\>`, TBuilder tb) {
//    println("collectC: <current>");
//    tb.define("<name>", variableId(), name, defType(astr()));
//    tb.fact(current, astr());
//    collectParts(regexps, tb);
//}

// ---- string literals and templates
void collect(current:(Literal)`<StringLiteral sl>`, TBuilder tb){
    tb.fact(current, astr());
    collectParts(current, tb);
}

void collect(template: (StringTemplate) `if(<{Expression ","}+ conditions>){ <Statement* preStats> <StringMiddle body> <Statement* postStats> }`, TBuilder tb){
    condList = [cond | Expression cond <- conditions];
    tb.fact(template, avalue());
    tb.requireEager("if then template", template, condList, (){ checkConditions(condList); });
    tb.enterScope(template);// thenPart may refer to variables defined in conditions
        collectParts(templae, tb);
    tb.leaveScope(template);
}

Tree define(template: (StringTemplate) `if( <{Expression ","}+ conditions> ){ <Statement* preStatsThen> <StringMiddle thenString> <Statement* postStatsThen> } else { <Statement* preStatsElse> <StringMiddle elseString> <Statement* postStatsElse> }`, TBuilder tb){
    condList = [cond | Expression cond <- conditions];
    // TODO scoping in else does not yet work
    if(!isEmpty([s | s <- preStatsElse]))
       storeExcludeUse(conditions, preStatsElse, tb); // variable occurrences in elsePart may not refer to variables defined in conditions
    if(!isEmpty("<elseString>"))
       storeExcludeUse(conditions, elseString, tb); 
    if(!isEmpty([s | s <- postStatsElse]))
       storeExcludeUse(conditions, postStatsElse, tb);
    
    tb.calculate("if then else template", template, condList/* + [postStatsThen + postStatsElse]*/,
        AType (){ checkConditions(condList); 
                  return avalue();
        });
    return conditions; // thenPart may refer to variables defined in conditions
} 

void collect(Literal l:(Literal)`<LocationLiteral ll>`, TBuilder tb){
    tb.fact(l, aloc());
}

// Rascal expressions

// ---- non-empty block

void collect(current: (Expression) `{ <Statement+ statements> }`, TBuilder tb){
    stats = [ stat | Statement stat <- statements ];
    tb.calculate("non-empty block expression", current, [stats[-1]],  AType() { return getType(stats[-1]); } );
    collectParts(current, tb);
}

// ---- brackets

void collect(current: (Expression) `( <Expression expression> )`, TBuilder tb){
    tb.calculate("brackets", current, [expression],  AType() { return getType(expression); } );
    collectParts(current, tb);
}

// ---- closure

void collect(current: (Expression) `<Type tp> <Parameters parameters> { <Statement+ statements> }`, TBuilder tb){
    tb.enterScope(current);
        retType = convertType(tp, tb);
        scope = tb.getScope();
        <formals, kwTypeVars, kwFormals> = checkFunctionType(scope, retType, parameters, tb);
        tb.calculate("type of closure", current, formals,
            AType (){ return afunc(expandUserTypes(retType, scope), atypeList([getType(f) | f <- formals]), kwFormals); });
        collect(statements, tb);
    tb.leaveScope(current);
}

// ---- void closure

void collect(current: (Expression) `<Parameters parameters> { <Statement* statements0> }`, TBuilder tb){
    tb.enterScope(current);
        scope = tb.getScope();
        <formals, kwTypeVars, kwFormals> = checkFunctionType(scope, avoid(), parameters, tb);
        tb.calculate("type of void closure", current, parameters,
            AType (){ return afunc(avoid(), atypeList([getType(f) | f <- formals]), kwFormals); });
        collect(statements0, tb);
    tb.leaveScope(current);
}

// ---- step range

void collect(current: (Expression) `[ <Expression first> , <Expression second> .. <Expression last> ]`, TBuilder tb){
    tb.calculate("step range", current, [first, second, last],
        AType(){ t1 = getType(first); t2 = getType(second); t3 = getType(last);
                 if(!subtype(t1,anum())) reportError(first, "Invalid type: expected numeric type, found <fmt(t1)>");
                 if(!subtype(t2,anum())) reportError(second, "Invalid type: expected numeric type, found <fmt(t2)>");
                 if(!subtype(t3,anum())) reportError(last, "Invalid type: expected numeric type, found <fmt(t3)>");
                 return alist(lub([t1, t2, t3]));
        
        });
    collectParts(current, tb);    
}

// ---- range

void collect(current: (Expression) `[ <Expression first> .. <Expression last> ]`, TBuilder tb){
    tb.calculate("step range", current, [first, last],
        AType(){ t1 = getType(first); t2 = getType(last);
                 if(!subtype(t1,anum())) reportError(first, "Invalid type: expected numeric type, found <fmt(t1)>");
                 if(!subtype(t2,anum())) reportError(last, "Invalid type: expected numeric type, found <fmt(t2)>");
                 return alist(lub([t1, t2]));
        });
    collectParts(current, tb);    
}

// ---- visit

void collect(current: (Expression) `<Label label> <Visit vst>`, TBuilder tb){
    tb.enterScope(current);
        scope = tb.getScope();
        tb.setScopeInfo(scope, visitOrSwitchScope(), visitOrSwitchInfo(vst.subject, true));
        if(label is \default){
            tb.define("<label.name>", labelId(), label.name, noDefInfo());
        }
        tb.calculate("visit subject", vst, [vst.subject], AType(){ return getType(vst.subject); });
        collectParts(current, tb);
    tb.leaveScope(current);
}

// ---- reifyType

void collect(current: (Expression) `# <Type tp>`, TBuilder tb){
    rt = convertType(tp, tb);
    scope = tb.getScope();
    tb.calculate("reified type", current, [], AType() { return areified(expandUserTypes(rt, scope)); });
    collectParts(current, tb);
}

// ---- reifiedType

void collect(current: (Expression) `type ( <Expression es> , <Expression ed> )`, TBuilder tb) {
    // TODO: Is there anything we can do statically to make the result type more accurate?
    tb.fact(current, areified(avalue()));
    tb.require("reified type", current, [es, ed],
        (){ subtype(getType(es), aadt("Symbol",[]), 
                onError(es, "Expected subtype of Symbol, instead found <fmt(getType(es))>"));
            subtype(getType(ed), amap(aadt("Symbol",[]),aadt("Production",[])), 
                onError(ed, "Expected subtype of map[Symbol,Production], instead found <fmt(getType(ed))>"));
          });
    collectParts(current, tb);
}

// ---- any

void collect(current: (Expression)`any ( <{Expression ","}+ generators> )`, TBuilder tb){
    gens = [gen | gen <- generators];
    tb.fact(current, abool());
    
    tb.enterScope(current);
        tb.require("any", current, gens,
            () { for(gen <- gens) if(getType(gen) != abool()) reportError(gen, "Type of generator should be `bool`, found <fmt(getType(gen))>");
            });
        collectParts(current, tb);
    tb.leaveScope(current);
}

// ---- all

void collect(current: (Expression)`all ( <{Expression ","}+ generators> )`, TBuilder tb){
    gens = [gen | gen <- generators];
    tb.fact(current, abool());
    
    tb.enterScope(current);
        tb.require("all", current, gens,
            () { for(gen <- gens) if(getType(gen) != abool()) reportError(gen, "Type of generator should be `bool`, found <fmt(getType(gen))>");
            });
        collectParts(current, tb);
    tb.leaveScope(current);
}

// ---- comprehensions and reducer

// set comprehension

void collect(current: (Expression)`{ <{Expression ","}+ results> | <{Expression ","}+ generators> }`, TBuilder tb){
    gens = [gen | gen <- generators];
    res  = [r | r <- results];
    storeAllowUseBeforeDef(current, results, tb); // variable occurrences in results may refer to variables defined in generators
    tb.enterScope(current);
        tb.require("set comprehension", current, gens,
            () { for(gen <- gens) if(getType(gen) != abool()) reportError(gen, "Type of generator should be `bool`, found <fmt(getType(gen))>");
            });
        tb.calculate("set comprehension results", current, res,
            AType(){
                return makeSetType(lubList([ getType(r) | r <- res]));
            });
         
        collectParts(current, tb);
    tb.leaveScope(current);
}

// list comprehension

void collect(current: (Expression) `[ <{Expression ","}+ results> | <{Expression ","}+ generators> ]`, TBuilder tb){
    gens = [gen | gen <- generators];
    res  = [r | r <- results];
    storeAllowUseBeforeDef(current, results, tb); // variable occurrences in results may refer to variables defined in generators
    tb.enterScope(current);
        tb.require("list comprehension", current, gens,
            () { for(gen <- gens) if(getType(gen) != abool()) reportError(gen, "Type of generator should be `bool`, found <fmt(getType(gen))>");
            });
        tb.calculate("list comprehension results", current, res,
            AType(){
                return makeListType(lubList([ getType(r) | r <- res]));
            });
         
        collectParts(current, tb);
    tb.leaveScope(current);
}

// map comprehension

void collect(current: (Expression) `(<Expression from> : <Expression to> | <{Expression ","}+ generators> )`, TBuilder tb){
    gens = [gen | gen <- generators];
    storeAllowUseBeforeDef(current, from, tb); // variable occurrences in from may refer to variables defined in generators
    storeAllowUseBeforeDef(current, to, tb); // variable occurrences in to may refer to variables defined in generators
    tb.enterScope(current);
        tb.require("map comprehension", current, gens,
            () { for(gen <- gens) if(getType(gen) != abool()) reportError(gen, "Type of generator should be `bool`, found <fmt(getType(gen))>");
            });
        tb.calculate("list comprehension results", current, [from, to],
            AType(){
                return makeMapType(getType(from), getType(to));
            });
         
        collectParts(current, tb);
    tb.leaveScope(current);
}

// ---- reducer

void collect(current: (Expression) `( <Expression init> | <Expression result> | <{Expression ","}+ generators> )`, TBuilder tb){
    gens = [gen | gen <- generators];
    storeAllowUseBeforeDef(current, result, tb); // variable occurrences in result may refer to variables defined in generators
    tb.enterScope(current);
        //tau = tb.newTypeVar();
        tb.define("it", variableId(), init, defLub([init, result], AType() { return lub(getType(init), getType(result)); }));
        tb.require("reducer", current, gens,
            () { for(gen <- gens) if(getType(gen) != abool()) reportError(gen, "Type of generator should be `bool`, found <fmt(getType(gen))>");
            });
        tb.calculate("reducer result", current, [result], AType () { return getType(result); });
        //tb.requireEager("reducer it", current, [init, result], (){ unify(tau, lub(getType(init), getType(result)), onError(current, "Can determine it")); });
         
        collectParts(current, tb);
    tb.leaveScope(current);
}

void collect(current: (Expression) `it`, TBuilder tb){
    tb.use(current, {variableId()});
}


// ---- set

void collect(current: (Expression) `{ <{Expression ","}* elements0> }`, TBuilder tb){
    elms = [ e | Expression e <- elements0 ];
    tb.calculateEager("set expression", current, elms,
        AType() { return aset(lub([getType(elm) | elm <- elms])); });
    collectParts(current, tb);
}

// ---- list

void collect(current: (Expression) `[ <{Expression ","}* elements0> ]`, TBuilder tb){
    elms = [ e | Expression e <- elements0 ];
    tb.calculateEager("list expression", current, elms,
        AType() { return alist(lub([getType(elm) | elm <- elms])); });
    collectParts(current, tb);
}

// ---- call or tree
           
void collect(current: (Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments[Expression] keywordArguments>)`, TBuilder tb){
    actuals = [a | Expression a <- arguments];
    scope = tb.getScope();
    
    tb.calculate("call of function/constructor", current, expression + actuals,
        AType(){   
            texp = getType(expression);
            if(isStrType(texp)){
                return anode();
            }     
            if(overloadedAType(rel[Key, IdRole, AType] overloads) := texp){
              //validateOverloading(expression, texp);
              next_fun:
                for(<key, idr, tp> <- overloads){                       
                    if(ft:afunc(AType ret, atypeList(list[AType] formals), list[Keyword] kwFormals) := tp){
                       try return checkArgsAndComputeReturnType(current, scope, ret, formals, kwFormals, ft.varArgs, actuals, keywordArguments);
                       catch checkFailed(set[Message] msgs):
                             continue next_fun;
                    }
                 }
               next_cons:
                 for(<key, idr, tp> <- overloads){
                    if(acons(ret:aadt(adtName, list[AType] parameters), str consName, list[NamedField] fields, list[Keyword] kwFields) := tp){
                       try return computeADTType(current, adtName, scope, ret, fields<0>, kwFields, actuals, keywordArguments);
                       catch checkFailed(set[Message] msgs):
                             continue next_cons;
                    }
                }
                reportError(current, "No function or constructor <fmt(expression)> for arguments <fmt(actuals)>");
            }
          
            if(ft:afunc(AType ret, atypeList(list[AType] formals), list[Keyword] kwFormals) := texp){
                return checkArgsAndComputeReturnType(current, scope, ret, formals, kwFormals, ft.varArgs, actuals, keywordArguments);
            }
            if(acons(ret:aadt(adtName, list[AType] parameters), str consName, list[NamedField] fields, list[Keyword] kwFields) := texp){
               return computeADTType(current, adtName, scope, ret, fields<0>, kwFields, actuals, keywordArguments);
            }
            reportError(current, "Function or constructor type required for <fmt(expression)>, found <fmt(expression)>");
        });
      collectParts(current, tb);
}

void validateOverloading(Expression current, overloadedAType(rel[Key, IdRole, AType] overloads)){
    tm = getTModel();
    fs = [<tm.definitions[k].id, tp> | <k, functionId(), tp> <- overloads];
    for(id <- domain(fs)){
        if([*_, afunc(AType ret1, AType formals1, _), *_, afunc(AType ret2, AType formals2, _), *_] := fs[id],
           subtype(formals1, formals2) || subtype(formals2, formals1))
           reportError(current, "Ambiguous function <fmt(id)>");
    }
    fs = [<tm.definitions[k].id, tp> | <k, constructorId(), tp> <- overloads];
    for(id <- domain(fs)){
        if([*_, acons(AType adt1, id, list[NamedField] fields1, _), *_, acons(AType adt2, id, list[NamedField] fields2, _), *_] := fs[id],
            fieldTypes1 := [tp | <tp, nm> <- fields1], fieldTypes1 := [tp | <tp, nm> <- fields2],
           subtype(fieldTypes1, fieldTypes2) || subtype(fieldTypes2, fieldTypes1))
           reportError(current, "Ambiguous constructor <fmt(id)>");   
    }
}

AType checkArgsAndComputeReturnType(Expression current, Key scope, AType retType, list[AType] formals, list[Keyword] kwFormals, bool isVarArgs, list[Expression] actuals, keywordArguments){
    nactuals = size(actuals); nformals = size(formals);
   
    list[AType] actualTypes;
    list[AType] formalTypes;
    if(isVarArgs){
       if(nactuals < nformals - 1) reportError(current, "Expected at least <fmt(nformals-1, "argument")>, found <nactuals>");
       varArgsType = (avoid() | lub(it, getType(actuals[i])) | int i <- [nformals-1 .. nactuals]);
       actualTypes = [getType(actuals[i]) | int i <- [0 .. nformals-1]] + alist(varArgsType);
       formalTypes = formals[0.. nformals-1] + alist(formals[nformals-1]);
    } else {
        if(nactuals != nformals) reportError(current, "Expected <fmt(nformals, "argument")>, found <nactuals>");
        actualTypes = [getType(a) | a <- actuals];
        formalTypes = formals;
    }
    
    formalTypes = [ expandUserTypes(formalTypes[i], scope) | i <- index(formalTypes) ];
    
    Bindings bindings = ();
    for(int i <- index(formals)){
        try   bindings = matchRascalTypeParams(formalTypes[i], actualTypes[i], bindings, bindIdenticalVars=true);
        catch invalidMatch(str reason):
              reportError(actuals[i], reason);
    }
  
    iformalTypes = [];
    try {
      iformalTypes = [instantiateRascalTypeParams(formalTypes[i], bindings) | int i <- index(formals)];
    } catch invalidInstantiation(str msg): {
        reportError(current, msg);
    }
    
    for(int i <- index(formals)){
        if(!comparable(actualTypes[i], iformalTypes[i]))
           reportError(actuals[i], "Argument should have type <fmt(iformalTypes[i])>, found <fmt(actualTypes[i])>");       
    }
    
    checkKwArgs(kwFormals, keywordArguments, bindings, scope);
    
    // Artificially bind unbound type parameters in the return type
    for( rparam <- collectRascalTypeParams(retType)){
        pname = rparam.pname;
        if(!bindings[pname]?) bindings[pname] = rparam;
    }
        
    try   return instantiateRascalTypeParams(retType, bindings);
    catch invalidInstantiation(str msg):
          reportError(current, msg);
}

AType computeADTType(Expression current, str adtName, Key scope, AType retType, list[AType] formals, list[Keyword] kwFormals, list[Expression] actuals, keywordArguments){
    nactuals = size(actuals); nformals = size(formals);
    if(nactuals != nformals){
        reportError(current, "Expected <fmt(nformals, "argument")>, found <nactuals>");
    }
    formals = [ expandUserTypes(formals[i], scope) | i <- index(formals) ];
    Bindings bindings = ();
    for(int i <- index(actuals)){
        try   bindings = matchRascalTypeParams(formals[i], getType(actuals[i]), bindings, bindIdenticalVars=true);
        catch invalidMatch(str reason): 
              reportError(actuals[i], reason);   
    }
    iformals = [];
    try   iformals = [instantiateRascalTypeParams(formals[i], bindings) | int i <- index(formals)];
    catch invalidInstantiation(str msg):
          reportError(current, msg);
      
    
    for(int i <- index(actuals)){
        if(!comparable(getType(actuals[i]), iformals[i]))
            reportError(actuals[i], "Argument <actuals[i]> should have type <fmt(formals[i])>, found <fmt(actuals[i])>");
    }
    adtType = avalue();
    try    adtType = instantiateRascalTypeParams(expandUserTypes(getType(adtName, scope, {dataId(), nonterminalId()}), scope), bindings);
    catch invalidInstantiation(str msg):
           reportError(current, msg);
      
    checkKwArgs(kwFormals + getCommonKeywords(adtType, scope), keywordArguments, bindings, scope);
    return adtType;
}

void checkKwArgs(list[Keyword] kwFormals, keywordArguments, Bindings bindings, Key scope){
    if(keywordArguments is none) return;
 
    next_arg:
    for(kwa <- keywordArguments.keywordArgumentList){ 
        kwName = "<kwa.name>";
        kwType = getType(kwa.expression);
        
        for(<ft, fn, de> <- kwFormals){
           if(kwName == fn){
              ift = expandUserTypes(ft, scope);
              try   ift = instantiateRascalTypeParams(ft, bindings);
              catch invalidInstantiation(str msg):
                    reportError(kwa, msg);

              if(!comparable(kwType, ift)){
                 reportError(kwa, "Keyword argument <fmt(kwName)> has type <fmt(kwType)>, expected <fmt(ift)>");
              }
              continue next_arg;
           } 
        }
        reportError(kwa, "Undefined keyword argument <fmt(kwName)>; <kwFormals<0,1>>");
    }
 } 

list[Keyword] getCommonKeywords(aadt(str adtName, list[AType] parameters), loc scope) = [ *d.defInfo.commonKeywordFields | d <- getDefinitions(adtName, scope, {dataId(), nonterminalId()}) ];
list[Keyword] getCommonKeywords(overloadedAType(rel[Key, IdRole, AType] overloads), loc scope) = [ *getCommonKeywords(adt, scope) | <def, idr, adt> <- overloads ];

// ---- tuple

void collect(current: (Expression) `\< <{Expression ","}+ elements1> \>`, TBuilder tb){
    elms = [ e | Expression e <- elements1 ];
    tb.calculateEager("tuple expression", current, elms,
        AType() {
                return atuple(atypeList([ getType(elm) | elm <- elms ]));
        });
    collectParts(current, tb);
}

// ---- map

void collect(current: (Expression) `( <{Mapping[Expression] ","}* mappings>)`, TBuilder tb){
    froms = [ m.from | m <- mappings ];
    tos =  [ m.to | m <- mappings ];
    tb.calculateEager("map expression", current, froms + tos,
        AType() {
                return amap(lub([ getType(f) | f <- froms ]), lub([ getType(t) | t <- tos ]));
        });
    collectParts(current, tb);
}

// ---- it

// ---- qualified name
 
void collect(current: (Expression) `<QualifiedName name>`, TBuilder tb){
    qname = convertName(name);
    if(isQualified(qname)){     
       tb.useQualified([qname.qualifier, qname.name], name, {variableId(), functionId(), constructorId()}, {dataId(), nonterminalId(), moduleId()} );
    } else {
       tb.useLub(name, {variableId(), formalId(), functionId(), constructorId()});
    }
}

// ---- subscript

void collect(current:(Expression)`<Expression expression> [ <{Expression ","}+ indices> ]`, TBuilder tb){
    indexList = [e | e <- indices];
    // Subscripts can also use the "_" character, which means to ignore that position; we do
    // that here by treating it as avalue(), which is comparable to all other types and will
    // thus work when calculating the type below.
    
    for(e <- indexList, (Expression)`_` := e){
        tb.fact(e, avalue());
    }
    
    tb.calculate("subscription", current, expression + indexList,
                  AType(){ return computeSubscriptionType(current, getType(expression), [getType(e) | e <- indexList]);  });
    collectParts(current, tb);
}

AType computeSubscriptionType(Tree current, AType t1, list[AType] tl){

    if (isListType(t1) && (!isListRelType(t1) || (isListRelType(t1) && size(tl) == 1 && isIntType(tl[0])))) {
        // TODO: At some point we should have separate notation for this, but this final condition treats list
        // relations indexed by one int value as lists, making this an index versus a projection
        if (size(tl) != 1)
            reportError(current, "Expected only 1 subscript for a list expression, not <size(tl)>");
        else if (!isIntType(tl[0]))
            reportError(current, "Expected subscript of type int, not <fmt(tl[0])>");
        else
            return getListElementType(t1);
    } else if (isRelType(t1)) {
        if (size(tl) >= size(getRelFields(t1)))
            reportError(current, "For a relation with arity <size(getRelFields(t1))> you can have at most <size(getRelFields(t1))-1> subscripts");
        else {
            relFields = getRelFields(t1);
            failures = { "At subscript <idx+1>, subscript type <fmt(tl[idx])> must be comparable to relation field type <fmt(relFields[idx])>" | idx <- index(tl), ! (comparable(tl[idx],relFields[idx]) || comparable(tl[idx],makeSetType(relFields[idx]))) };
            if (size(failures) > 0) {
                reportError(failures);
            } else if ((size(relFields) - size(tl)) == 1) {
                rftype = last(relFields);
                if (alabel(_,rft) := rftype) rftype = rft; 
                return makeSetType(rftype);
            } else {
                return arel(tail(relFields,size(relFields)-size(tl)));
            }
        }
    } else if (isListRelType(t1)) {
        if (size(tl) >= size(getListRelFields(t1)))
            reportError(current, "For a list relation with arity <size(getListRelFields(t1))> you can have at most <size(getListRelFields(t1))-1> subscripts");
        else {
            relFields = getListRelFields(t1);
            failures = { "At subscript <idx+1>, subscript type <fmt(tl[idx])> must be comparable to relation field type <fmt(relFields[idx])>" | idx <- index(tl), ! (comparable(tl[idx],relFields[idx]) || comparable(tl[idx],makeSetType(relFields[idx]))) };
            if (size(failures) > 0) {
                reportError(failures);
            } else if ((size(relFields) - size(tl)) == 1) {
                rftype = last(relFields);
                if (alabel(_,rft) := rftype) rftype = rft; 
                return makeListType(rftype);
            } else {
                return alrel(tail(relFields,size(relFields)-size(tl)));
            }
        }
    } else if (isMapType(t1)) {
        if (size(tl) != 1)
            reportError(current, "Expected only 1 subscript for a map expression, not <size(tl)>");
        else if (!comparable(tl[0],getMapDomainType(t1)))
            reportError(current, "Expected subscript of type <fmt(getMapDomainType(t1))>, not <fmt(tl[0])>");
        else
            return getMapRangeType(t1);
    } else if (isNodeType(t1)) {
        if (size(tl) != 1)
            reportError(current, "Expected only 1 subscript for a node expression, not <size(tl)>");
        else if (!isIntType(tl[0]))
            reportError(current, "Expected subscript of type int, not <fmt(tl[0])>");
        else
            return avalue();
    } else if (isTupleType(t1)) {
        if (size(tl) != 1) {
            reportError(current, "Expected only 1 subscript for a tuple expression, not <size(tl)>");
        } else if (!isIntType(tl[0])) {
            reportError(current, "Expected subscript of type int, not <fmt(tl[0])>");
        } else if ((Expression)`<DecimalIntegerLiteral dil>` := head(eslist)) {
            tupleIndex = toInt("<dil>");
            if (tupleIndex < 0 || tupleIndex >= size(getTupleFields(t1))) {
                reportError(current, "Tuple index must be between 0 and <size(getTupleFields(t1))-1>");
            } else {
                return getTupleFields(t1)[tupleIndex];
            }
        } else {
            return lubList(getTupleFields(t1));
        }
    } else if (isStrType(t1)) {
        if (size(tl) != 1)
            reportError(current, "Expected only 1 subscript for a string expression, not <size(tl)>");
        else if (!isIntType(tl[0]))
            reportError(current, "Expected subscript of type int, not <fmt(tl[0])>");
        else
            return astr();
    //} else if (isNonTerminalType(t1)) {
    //    if (size(tl) != 1)
    //        reportError("Expected only 1 subscript for a nonterminal subscript expression, not <size(tl)>");
    //    else if (!isIntType(tl[0]))
    //        reportError("Expected subscript of type int, not <fmt(tl[0])>");
    //    else if (isNonTerminalIterType(t1))
    //        return getNonTerminalIterElement(t1);
    //    else
    //        return makeADTType("Tree");    
    } else {
        reportError(current, "Expressions of type <fmt(t1)> cannot be subscripted");
    }
}

// ---- slice

void collect(current: (Expression) `<Expression e> [ <OptionalExpression ofirst> .. <OptionalExpression olast> ]`, TBuilder tb){
    if(ofirst is noExpression) tb.fact(ofirst, aint());
    if(olast is noExpression) tb.fact(olast, aint());

    tb.calculate("slice", current, [e, ofirst, olast],
        AType(){ return computeSliceType(getType(e), getType(ofirst), aint(), getType(olast)); });
    collectParts(current, tb);
}

AType computeSliceType(AType base, AType first, AType step, AType last){
    failures = {};
    if(!isIntType(first)) failures += "The first slice index must be of type `int`, found <fmt(first)>"; //TODO change to error
    if(!isIntType(step)) failures  += "The slice step must be of type `int`, found <fmt(step)>";
    if(!isIntType(last)) failures  += "The last slice index must be of type `int`, found <fmt(last)>";
    
    if(!isEmpty(failures)) throw reportError(failures);
    
    if (isListType(base) || isStrType(base) || isNonTerminalIterType(base)) {
        return base;
    } else if (isNodeType(base)) {
        return makeListType(avalue());
    }
    
    reportError(failures + "Slices can only be used on (concrete) lists, strings, and nodes");
}

// ---- sliceStep

void collect(current: (Expression) `<Expression e> [ <OptionalExpression ofirst>, <Expression second> .. <OptionalExpression olast> ]`, TBuilder tb){
    if(ofirst is noExpression) tb.fact(ofirst, aint());
    if(olast is noExpression) tb.fact(olast, aint());

    tb.calculate("slice step", current, [e, ofirst, second, olast],
        AType(){ return computeSliceType(getType(e), getType(ofirst), getType(second), getType(olast)); });
    collectParts(current, tb);
}

// ---- fieldAccess

void collect(current: (Expression) `<Expression expression> . <Name field>`, TBuilder tb){
    scope = tb.getScope();
    
    tb.calculate("field access", current, [expression],
        AType(){ return computeFieldType(current, getType(expression), prettyPrintQName(convertName(field)), scope); });
    collectParts(current, tb);
}

@doc{Field names and types for built-ins}
private map[AType,map[str,AType]] fieldMap =
    ( aloc() :
        ( "scheme" : astr(), 
          "authority" : astr(), 
          "host" : astr(), 
          "user" : astr(), 
          "port" : aint(), 
          "path" : astr(), 
          "query" : astr(), 
          "fragment" : astr(), 
          "length" : aint(), 
          "offset" : aint(), 
          "begin" : atuple(atypeList([aint()[label="line"],aint()[label="column"]])), 
          "end" :   atuple(atypeList([aint()[label="line"],aint()[label="column"]])), 
          "uri" : astr(), 
          "top" : aloc(),
          "parent" : aloc(),
          "file" : astr(), 
          "ls" : makeListType(aloc()), 
          "extension" : astr(),
          "params" : amap(astr(),astr())
        ),
      adatetime() :
        ( "year" : aint(), "month" : aint(), "day" : aint(), "hour" : aint(), "minute" : aint(), 
          "second" : aint(), "millisecond" : aint(), "timezoneOffsetHours" : aint(), 
          "timezoneOffsetMinutes" : aint(), "century" : aint(), "isDate" : abool(), 
          "isTime" : abool(), "isDateTime" : abool(), "justDate" : adatetime(), "justTime" : adatetime()
        )
    );

@doc{Compute the type of field fn on type t1. A checkFailed is thrown if the field is not defined on the given type.}
public AType computeFieldType(Tree current, AType t1, str fieldName, Key scope) {
 
    if (aadt(adtName, list[AType] actualTypeParams) := t1){
        try {
            //if (getADTName(t1) == "Tree" && fieldName == "top") {
            //    return t1;
            //}
            fieldType = expandUserTypes(getType(fieldName, scope, {fieldId()}), scope);
           
            declaredInfo = getDefinitions(adtName, scope, {dataId(), nonterminalId()});
            declaredType = getType(adtName, scope, {dataId(), nonterminalId()});
            declaredTypeParams = getADTTypeParameters(declaredType);
            
            if (size(declaredTypeParams) > 0) {
                if (size(declaredTypeParams) != size(actualTypeParams)) {
                    reportError(current, "Invalid ADT type, the number of type parameters is inconsistent");
                } else {
                    map[str, AType] bindings = ( getRascalTypeParamName(declaredTypeParams[idx]) : actualTypeParams[idx] | idx <- index(declaredTypeParams));
                    try {
                        fieldType = instantiateRascalTypeParams(fieldType, bindings);
                    } catch invalidInstantiation(str msg): {
                        reportError(current, "Failed to instantiate type parameters in field type <fmt(fieldType)>");
                    }                       
                }
            } 
             
            fieldType = filterFieldType(fieldType, declaredInfo, scope);
            
            for(def <- declaredInfo){
               if(fieldName in domain(def.defInfo.constructorFields)){
                    return fieldType;
               }
            }  
            
            if (declaredType.hasSyntax) {
                return computeFieldType(current, aadt("Tree", []), fieldName, scope);
            }                         
            
            reportError(current, "Field <fmt(fieldName)> does not exist on type <fmt(t1)>");
        } catch TypeUnavailable(): {
            reportError(current, "Cannot compute type of field <fmt(fieldName)>, user type <fmt(t1)> has not been declared or is out of scope"); 
        }
    } else if (isNonTerminalType(t1)){
       return computeFieldType(current, aadt("Tree", []), fieldName, scope);
    } else if (isTupleType(t1)) {
        idx = indexOf(getTupleFieldNames(t1), fieldName);
        if(idx >= 0)
            return getTupleFieldTypes(t1)[idx];
        else
            reportError(current, "Field <fmt(fieldName)> does not exist on type <fmt(t1)>");
    } else if (isLocType(t1)) {
        if (fieldName in fieldMap[aloc()])
            return fieldMap[aloc()][fieldName];
        else
            reportError(current, "Field <fmt(fieldName)> does not exist on type <fmt(t1)>");
    } else if (isDateTimeType(t1)) {
        if (fieldName in fieldMap[adatetime()])
            return fieldMap[adatetime()][fieldName];
        else
           reportError(current, "Field <fmt(fieldName)> does not exist on type <fmt(t1)>");
    } else if (isRelType(t1)) {
        idx = indexOf(getRelFieldNames(t1), fieldName);
        if(idx >= 0){
            return makeSetType(getRelFields(t1)[idx]); 
        }
        else
           reportError(current, "Field <fmt(fieldName)> does not exist on type <fmt(t1)>");
    } else if (isListRelType(t1)) {
        idx = indexOf(getListRelFieldNames(t1), fieldName);
        if(idx >= 0){
            return makeListType(getListRelFields(t1)[idx]); 
        }
        else
           reportError(current, "Field <fmt(fieldName)> does not exist on type <fmt(t1)>");
    } else if (isMapType(t1)) {
    	   idx = indexOf(getMapFieldNames(t1), fieldName);
        if(idx >= 0){
            return idx == 0 ? makeSetType(getMapDomainType(t1)) : makeSetType(getMapRangeType(t1));
        }
        else
            reportError(current, "Field <fmt(fieldName)> does not exist on type <fmt(t1)>");
    } else if (isReifiedType(t1)) {
        if (fieldName == "symbol") {
            try {
                return getType("Symbol", scope, {dataId()});
            } catch TypeUnavailable():{
               reportError(current, "The type `Symbol` of field <fmt(fieldName)> is not in scope");
            }
        } else if (fieldName == "definitions") {
            try {
                getType("Symbol", scope, {dataId()});
                try {   
                    getType("Production", scope, {dataId()});
                    return makeMapType(makeADTType("Symbol"), makeADTType("Production"));
                } catch TypeUnavailable():{
                    reportError(current, "The type `Production` used in field <fmt(fieldName)> is not in scope");
                }
            } catch TypeUnavailable():{
                reportError(current, "The type `Symbol` used in field <fmt(fieldName)> is not in scope");
            }
        } else {
           reportError(current, "Field <fmt(fieldName)> does not exist on type `type`   ");
        }
    } else if (isNodeType(t1)) {
        return avalue();
    }
    reportError(current, "Cannot access fields on type <fmt(t1)>");
}

AType filterFieldType(AType fieldType, set[Define] declaredInfo, Key scope){
    if(overloadedAType(rel[Key, IdRole, AType] overloads) := fieldType){
       filteredOverloads = {};
       for(<key, fieldId(), tp> <- overloads){
           for(def <- declaredInfo){
               expandedTypes = { unset(expandUserTypes(ft, scope), "label") | <fn, ft> <- def.defInfo.constructorFields };
               if(tp in expandedTypes){
                  filteredOverloads += <key, fieldId(), tp>;
               }
           }
       }
       return {<key, fieldId(), tp>} := filteredOverloads ? tp : overloadedAType(filteredOverloads);
    } else {
        return fieldType;
    } 
}
// ---- fieldUpdate

void collect(current:(Expression) `<Expression expression> [ <Name field> = <Expression repl> ]`, TBuilder tb){
    scope = tb.getScope();
    tb.use(field, {fieldId()});
    tb.calculate("field update", current, [expression, field, repl],
        AType(){ fieldType = computeFieldType(current, getType(expression), prettyPrintQName(convertName(field)), scope);
                 replType = getType(repl);
                 subtype(replType, fieldType, onError(current, "Cannot assign type <fmt(replType)> into field of type <fmt(fieldType)>"));
                 return getType(expression);
        });
    collectParts(current, tb);
}

// ---- fieldProjection

void collect(current:(Expression) `<Expression expression> \< <{Field ","}+ fields> \>`, TBuilder tb){

    flds = [f | f <- fields];
    tb.calculate("field projection", current, [expression],
        AType(){ return computeFieldProjectionType(current, getType(expression), flds); });
    collectParts(current, tb);
}

AType computeFieldProjectionType(Expression current, AType base, list[lang::rascal::\syntax::Rascal::Field] fields){
    // Get back the fields as a tuple, if this is one of the allowed subscripting types.
    AType rt = avoid();

    if (isRelType(base)) {
        rt = getRelElementType(base);
    } else if (isListRelType(base)) {
        rt = getListRelElementType(base);
    } else if (isMapType(base)) {
        rt = getMapFieldsAsTuple(base);
    } else if (isTupleType(base)) {
        rt = base;
    } else {
        reportError(current, "Type <fmt(base)> does not allow fields");
    }
    
    // Find the field type and name for each index
    set[Message] failures = { };
    list[AType] subscripts = [ ];
    list[str] fieldNames = [ ];
    bool maintainFieldNames = tupleHasFieldNames(rt);
    
    for (f <- fields) {
        if ((Field)`<IntegerLiteral il>` := f) {
            int offset = toInt("<il>");
            if (!tupleHasField(rt, offset))
                failures += error(il, "Field subscript <il> out of range");
            else {
                subscripts += getTupleFieldType(rt, offset);
                if (maintainFieldNames) fieldNames += getTupleFieldName(rt, offset);
            }
        } else if ((Field)`<Name fn>` := f) {
            fnAsString = prettyPrintQName(convertName(fn));
            if (!tupleHasField(rt, fnAsString)) {
                failures += error(fn, "Field <fn> does not exist");
            } else {
                subscripts += getTupleFieldType(rt, fnAsString);
                if (maintainFieldNames) fieldNames += fnAsString;
            }
        } else {
            throw "Unhandled field case: <f>";
        }
    }
    
    if (size(failures) > 0) reportErrors(failures);

    // Keep the field names if all fields are named and if we have unique names
    if (!(size(subscripts) > 1 && size(subscripts) == size(fieldNames) && size(fieldNames) == size(toSet(fieldNames)))) {
        subscripts = [ unset(tp, "label") | tp <- subscripts ];
    }
    
    if (isRelType(base)) {
        if (size(subscripts) > 1) return arel(atypeList(subscripts));
        return makeSetType(head(subscripts));
    } else if (isListRelType(base)) {
        if (size(subscripts) > 1) return alrel(atypeList(subscripts));
        return makeListType(head(subscripts));
    } else if (isMapType(base)) {
        if (size(subscripts) > 1) return arel(atypeList(subscripts));
        return makeSetType(head(subscripts));
    } else if (isTupleType(base)) {
        if (size(subscripts) > 1) return atuple(atypeList(subscripts));
        return head(subscripts);
    }   
}

// ---- setAnnotation

void collect(current:(Expression) `<Expression e> [ @ <Name n> = <Expression er> ]`, TBuilder tb) {
    tb.use(n, {annoId()});
    tb.calculate("set annotation", current, [e, n, er],
        AType(){ t1 = getType(e); tn = getType(n); t2 = getType(er);
                 return computeSetAnnotation(current, t1, tn, t2, tb);
               });
    collectParts(current, tb);
}

AType computeSetAnnotation(Expression current, AType t1, AType tn, AType t2, TBuilder tb){
    if (isNodeType(t1) || isADTType(t1) || isNonTerminalType(t1)) {
        if(overloadedAType(rel[Key, IdRole, AType] overloads) := tn){
           for(<key, idr, tp> <- overloads, aanno(_, onType, annoType) := tp, subtype(t1, onType)){
               subtype(t2, annoType, onError(current, "Cannot assign value of type <fmt(t2)> to annotation of type <fmt(annoType)>"));
               return onType;
           }
           reportError(current, "Annotation on <fmt(t1)> cannot be resolved from <fmt(tn)>");
        } else
        if(aanno(_, onType, annoType) := tn){
           subtype(t2, annoType, onError(current, "Cannot assign value of type <fmt(t2)> to annotation of type <fmt(annoType)>"));
           return onType;
        } else
            reportError(current, "Invalid annotation type: <fmt(tn)>");
    } else
        reportError(current, "Invalid type: expected node, ADT, or concrete syntax types, found <fmt(t1)>");
}

// ---- getAnnotation

void collect(current:(Expression) `<Expression e>@<Name n>`, TBuilder tb) {
    tb.use(n, {annoId()});
    tb.calculate("get annotation", current, [e, n],
        AType(){ t1 = getType(e); tn = getType(n);
                 return computeGetAnnotation(current, t1, tn, tb);
               });
    collectParts(current, tb);
}

AType computeGetAnnotation(Expression current, AType t1, AType tn, TBuilder tb){
    if (isNodeType(t1) || isADTType(t1) || isNonTerminalType(t1)) {
        if(overloadedAType(rel[Key, IdRole, AType] overloads) := tn){
           for(<key, idr, tp> <- overloads, aanno(_, onType, annoType) := tp, subtype(t1, onType)){
               return annoType;
           }
           println(tn);
           println(t1);
           reportError(current, "Annotation on <fmt(t1)> cannot be resolved from <fmt(tn)>");
        } else
        if(aanno(_, onType, annoType) := tn){
           return annoType;
        } else
            reportError(current, "Invalid annotation type: <fmt(tn)>");
    } else
        reportError(current, "Invalid type: expected node, ADT, or concrete syntax types, found <fmt(t1)>");
}
