module lang::rascal::check::Expression

extend analysis::typepal::TypePal;

import lang::rascal::check::AType;
import lang::rascal::check::ATypeUtils;
import lang::rascal::check::Scope;
import lang::rascal::check::ATypeInstantiation;

import lang::rascal::\syntax::Rascal;

// ---- Rascal literals
void collect(Literal l:(Literal)`<IntegerLiteral il>`, FRBuilder frb){
    frb.fact(l, aint());
}

void collect(Literal l:(Literal)`<RealLiteral rl>`, FRBuilder frb){
    frb.fact(l, areal());
}

void collect(Literal l:(Literal)`<BooleanLiteral bl>`, FRBuilder frb){
    frb.fact(l, abool());
 }

void collect(Literal l:(Literal)`<DateTimeLiteral dtl>`, FRBuilder frb){
    frb.fact(l, adatetime());
}

void collect(Literal l:(Literal)`<RationalLiteral rl>`, FRBuilder frb){
    frb.fact(l, arat());
}

void collect(Literal l:(Literal)`<RegExpLiteral rl>`, FRBuilder frb) {
    frb.fact(l, astr());
    collectParts(l, frb);
}

// ---- string literals and templates
void collect(Literal l:(Literal)`<StringLiteral sl>`, FRBuilder frb){
    frb.fact(l, astr());
    collectParts(l, frb);
}

void collect(template: (StringTemplate) `if(<{Expression ","}+ conditions>){ <Statement* preStats> <StringMiddle body> <Statement* postStats> }`, FRBuilder frb){
    condList = [cond | Expression cond <- conditions];
    frb.fact(template, avalue());
    frb.requireEager("if then template", template, condList, (){ checkConditions(condList); });
    frb.enterScope(template);// thenPart may refer to variables defined in conditions
        collectParts(templae, frb);
    frb.leaveScope(template);
}

Tree define(template: (StringTemplate) `if( <{Expression ","}+ conditions> ){ <Statement* preStatsThen> <StringMiddle thenString> <Statement* postStatsThen> } else { <Statement* preStatsElse> <StringMiddle elseString> <Statement* postStatsElse> }`, FRBuilder frb){
    condList = [cond | Expression cond <- conditions];
    // TODO scoping in else does not yet work
    if(!isEmpty([s | s <- preStatsElse]))
       storeExcludeUse(conditions, preStatsElse, frb); // variable occurrences in elsePart may not refer to variables defined in conditions
    if(!isEmpty("<elseString>"))
       storeExcludeUse(conditions, elseString, frb); 
    if(!isEmpty([s | s <- postStatsElse]))
       storeExcludeUse(conditions, postStatsElse, frb);
    
    frb.calculate("if then else template", template, condList/* + [postStatsThen + postStatsElse]*/,
        AType (){ checkConditions(condList); 
                  return avalue();
        });
    return conditions; // thenPart may refer to variables defined in conditions
} 

void collect(Literal l:(Literal)`<LocationLiteral ll>`, FRBuilder frb){
    frb.fact(l, aloc());
}

// Rascal expressions

// ---- non-empty block

void collect(current: (Expression) `{ <Statement+ statements> }`, FRBuilder frb){
    stats = [ stat | Statement stat <- statements ];
    frb.calculate("non-empty block expression", current, [stats[-1]],  AType() { return typeof(stats[-1]); } );
    collectParts(current, frb);
}

// ---- brackets

void collect(current: (Expression) `( <Expression expression> )`, FRBuilder frb){
    frb.calculate("brackets", current, [expression],  AType() { return typeof(expression); } );
    collectParts(current, frb);
}

// ---- closure

void collect(current: (Expression) `<Type tp> <Parameters parameters> { <Statement+ statements> }`, FRBuilder frb){
    frb.enterScope(current);
        retType = convertType(tp, frb);
        scope = frb.getScope();
        <formals, kwTypeVars, kwFormals> = checkFunctionType(scope, retType, parameters, frb);
        frb.calculate("type of closure", current, formals,
            AType (){ return afunc(expandUserTypes(retType, scope), atypeList([typeof(f) | f <- formals]), kwFormals); });
        collectParts(current, frb);
    frb.leaveScope(current);
}

// ---- void closure

void collect(current: (Expression) `<Parameters parameters> { <Statement* statements0> }`, FRBuilder frb){
    frb.enterScope(current);
        scope = frb.getScope();
        <formals, kwTypeVars, kwFormals> = checkFunctionType(scope, avoid(), parameters, frb);
        frb.calculate("type of void closure", current, parameters,
            AType (){ return afunc(avoid(), atypeList([typeof(f) | f <- formals]), kwFormals); });
        collectParts(current, frb);
    frb.leaveScope(current);
}

// ---- step range

void collect(current: (Expression) `[ <Expression first> , <Expression second> .. <Expression last> ]`, FRBuilder frb){
    frb.calculate("step range", current, [first, second, last],
        AType(){ t1 = typeof(first); t2 = typeof(second); t3 = typeof(last);
                 if(!subtype(t1,anum())) reportError(first, "Invalid type: expected numeric type, found <fmt(t1)>");
                 if(!subtype(t2,anum())) reportError(second, "Invalid type: expected numeric type, found <fmt(t2)>");
                 if(!subtype(t3,anum())) reportError(last, "Invalid type: expected numeric type, found <fmt(t3)>");
                 return alist(lub([t1, t2, t3]));
        
        });
    collectParts(current, frb);    
}

// ---- range

void collect(current: (Expression) `[ <Expression first> .. <Expression last> ]`, FRBuilder frb){
    frb.calculate("step range", current, [first, last],
        AType(){ t1 = typeof(first); t2 = typeof(last);
                 if(!subtype(t1,anum())) reportError(first, "Invalid type: expected numeric type, found <fmt(t1)>");
                 if(!subtype(t2,anum())) reportError(last, "Invalid type: expected numeric type, found <fmt(t2)>");
                 return alist(lub([t1, t2]));
        });
    collectParts(current, frb);    
}

// ---- visit

void collect(current: (Expression) `<Label label> <Visit vst>`, FRBuilder frb){
    frb.enterScope(current);
        if(label is \default){
            frb.define("<label.name>", labelId(), label.name, noDefInfo());
        }
        frb.calculate("visit subject", vst, [vst.subject], AType(){ return typeof(vst.subject); });
        collectParts(current, frb);
    frb.leaveScope(current);
}

// ---- reifyType

void collect(current: (Expression) `# <Type tp>`, FRBuilder frb){
    rt = expandUserTypes(convertType(tp, frb), frb.getScope());
    frb.fact(current, areified(rt));
    collectParts(current, frb);
}

// ---- reifiedType

void collect(current: (Expression) `type ( <Expression es> , <Expression ed> )`, FRBuilder frb) {
    // TODO: Is there anything we can do statically to make the result type more accurate?
    frb.fact(current, areified(avalue()));
    frb.require("reified type", current, [es, ed],
        (){ subtype(typeof(es), aadt("Symbol",[], []), 
                onError(es, "Expected subtype of Symbol, instead found <fmt(typeof(es))>"));
            subtype(typeof(ed), amap(aadt("Symbol",[], []),aadt("Production",[], [])), 
                onError(ed, "Expected subtype of map[Symbol,Production], instead found <fmt(typeof(ed))>"));
          });
    collectParts(current, frb);
}

// ---- any

void collect(current: (Expression)`any ( <{Expression ","}+ generators> )`, FRBuilder frb){
    gens = [gen | gen <- generators];
    frb.fact(current, abool());
    
    frb.enterScope(current);
        frb.require("any", current, gens,
            () { for(gen <- gens) if(typeof(gen) != abool()) reportError(gen, "Type of generator should be `bool`, found <fmt(typeof(gen))>");
            });
        collectParts(current, frb);
    frb.leaveScope(current);
}

// ---- all

void collect(current: (Expression)`all ( <{Expression ","}+ generators> )`, FRBuilder frb){
    gens = [gen | gen <- generators];
    frb.fact(current, abool());
    
    frb.enterScope(current);
        frb.require("all", current, gens,
            () { for(gen <- gens) if(typeof(gen) != abool()) reportError(gen, "Type of generator should be `bool`, found <fmt(typeof(gen))>");
            });
        collectParts(current, frb);
    frb.leaveScope(current);
}

// ---- comprehensions and reducer

// set comprehension

void collect(current: (Expression)`{ <{Expression ","}+ results> | <{Expression ","}+ generators> }`, FRBuilder frb){
    gens = [gen | gen <- generators];
    res  = [r | r <- results];
    storeAllowUseBeforeDef(current, results, frb); // variable occurrences in results may refer to variables defined in generators
    frb.enterScope(current);
        frb.require("set comprehension", current, gens,
            () { for(gen <- gens) if(typeof(gen) != abool()) reportError(gen, "Type of generator should be `bool`, found <fmt(typeof(gen))>");
            });
        frb.calculate("set comprehension results", current, res,
            AType(){
                return makeSetType(lubList([ typeof(r) | r <- res]));
            });
         
        collectParts(current, frb);
    frb.leaveScope(current);
}

// list comprehension

void collect(current: (Expression) `[ <{Expression ","}+ results> | <{Expression ","}+ generators> ]`, FRBuilder frb){
    gens = [gen | gen <- generators];
    res  = [r | r <- results];
    storeAllowUseBeforeDef(current, results, frb); // variable occurrences in results may refer to variables defined in generators
    frb.enterScope(current);
        frb.require("list comprehension", current, gens,
            () { for(gen <- gens) if(typeof(gen) != abool()) reportError(gen, "Type of generator should be `bool`, found <fmt(typeof(gen))>");
            });
        frb.calculate("list comprehension results", current, res,
            AType(){
                return makeListType(lubList([ typeof(r) | r <- res]));
            });
         
        collectParts(current, frb);
    frb.leaveScope(current);
}

// map comprehension

void collect(current: (Expression) `(<Expression from> : <Expression to> | <{Expression ","}+ generators> )`, FRBuilder frb){
    gens = [gen | gen <- generators];
    storeAllowUseBeforeDef(current, from, frb); // variable occurrences in from may refer to variables defined in generators
    storeAllowUseBeforeDef(current, to, frb); // variable occurrences in to may refer to variables defined in generators
    frb.enterScope(current);
        frb.require("map comprehension", current, gens,
            () { for(gen <- gens) if(typeof(gen) != abool()) reportError(gen, "Type of generator should be `bool`, found <fmt(typeof(gen))>");
            });
        frb.calculate("list comprehension results", current, [from, to],
            AType(){
                return makeMapType(typeof(from), typeof(to));
            });
         
        collectParts(current, frb);
    frb.leaveScope(current);
}

// ---- reducer

void collect(current: (Expression) `( <Expression init> | <Expression result> | <{Expression ","}+ generators> )`, FRBuilder frb){
    gens = [gen | gen <- generators];
    storeAllowUseBeforeDef(current, result, frb); // variable occurrences in result may refer to variables defined in generators
    frb.enterScope(current);
        tau = frb.newTypeVar();
        frb.define("it", variableId(), init, defLub([], AType() { return typeof(tau); }));
        frb.require("reducer", current, gens,
            () { for(gen <- gens) if(typeof(gen) != abool()) reportError(gen, "Type of generator should be `bool`, found <fmt(typeof(gen))>");
            });
        frb.calculate("reducer result", current, [result], AType () { return typeof(result); });
        frb.requireEager("reducer it", current, [init, result], (){ unify(tau, lub(typeof(init), typeof(result)), onError(current, "Can determine it")); });
         
        collectParts(current, frb);
    frb.leaveScope(current);
}

void collect(current: (Expression) `it`, FRBuilder frb){
    frb.use(current, {variableId()});
}


// ---- set

void collect(current: (Expression) `{ <{Expression ","}* elements0> }`, FRBuilder frb){
    elms = [ e | Expression e <- elements0 ];
    frb.calculateEager("set expression", current, elms,
        AType() { return aset(lub([typeof(elm) | elm <- elms])); });
    collectParts(current, frb);
}

// ---- list

void collect(current: (Expression) `[ <{Expression ","}* elements0> ]`, FRBuilder frb){
    elms = [ e | Expression e <- elements0 ];
    frb.calculateEager("list expression", current, elms,
        AType() { return alist(lub([typeof(elm) | elm <- elms])); });
    collectParts(current, frb);
}

// ---- call or tree
           
void collect(current: (Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments[Expression] keywordArguments>)`, FRBuilder frb){
    actuals = [a | Expression a <- arguments];
    scope = frb.getScope();
    
    frb.calculate("call of function/constructor", current, expression + actuals,
        AType(){        
            if(overloadedAType(rel[Key, AType] overloads) := typeof(expression)){
              next_fun:
                for(<key, tp> <- overloads){                       
                    if(ft:afunc(AType ret, atypeList(list[AType] formals), list[Keyword] kwFormals) := tp){
                       try return checkArgsAndComputeReturnType(current, ret, formals, kwFormals, ft.varArgs, actuals, keywordArguments);
                       catch checkFailed(set[Message] msgs):
                             continue next_fun;
                    }
                 }
               next_cons:
                 for(<key, tp> <- overloads){
                    if(acons(ret:aadt(adtName, list[AType] parameters, list[Keyword] common), str consName, list[NamedField] fields, list[Keyword] kwFields) := tp){
                       try return computeADTType(current, adtName, scope, ret, fields<0>, kwFields, actuals, keywordArguments);
                       catch checkFailed(set[Message] msgs):
                             continue next_cons;
                    }
                }
                reportError(current, "No function or constructor <fmt(expression)> for arguments <fmt(actuals)>");
            }
          
            if(ft:afunc(AType ret, atypeList(list[AType] formals), list[Keyword] kwFormals) := typeof(expression)){
                return checkArgsAndComputeReturnType(current, ret, formals, kwFormals, ft.varArgs, actuals, keywordArguments);
            }
            if(acons(ret:aadt(adtName, list[AType] parameters, list[Keyword] common), str consName, list[NamedField] fields, list[Keyword] kwFields) := typeof(expression)){
               return computeADTType(current, adtName, scope, ret, fields<0>, kwFields, actuals, keywordArguments);
            }
            reportError(current, "Function or constructor type required for <fmt(expression)>, found <fmt(expression)>");
        });
      collectParts(current, frb);
}

AType checkArgsAndComputeReturnType(Expression current, AType retType, list[AType] formals, list[Keyword] kwFormals, bool isVarArgs, list[Expression] actuals, keywordArguments){
    nactuals = size(actuals); nformals = size(formals);
   
    list[AType] actualTypes;
    list[AType] formalTypes;
    if(isVarArgs){
       if(nactuals < nformals - 1) reportError(current, "Expected at least <fmt(nformals-1, "argument")>, found <nactuals>");
       varArgsType = (avoid() | lub(it, typeof(actuals[i])) | int i <- [nformals-1 .. nactuals]);
       actualTypes = [typeof(actuals[i]) | int i <- [0 .. nformals-1]] + alist(varArgsType);
       formalTypes = formals[0.. nformals-1] + alist(formals[nformals-1]);
    } else {
        if(nactuals != nformals) reportError(current, "Expected <fmt(nformals, "argument")>, found <nactuals>");
        actualTypes = [typeof(a) | a <- actuals];
        formalTypes = formals;
    }
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
    
    checkKwArgs(kwFormals, keywordArguments, bindings);
    
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
    Bindings bindings = ();
    for(int i <- index(actuals)){
        try   bindings = matchRascalTypeParams(formals[i], typeof(actuals[i]), bindings, bindIdenticalVars=true);
        catch invalidMatch(str reason): 
              reportError(actuals[i], reason);   
    }
    iformals = [];
    try   iformals = [instantiateRascalTypeParams(formals[i], bindings) | int i <- index(formals)];
    catch invalidInstantiation(str msg):
          reportError(current, msg);
      
    
    for(int i <- index(actuals)){
        if(!comparable(typeof(actuals[i]), iformals[i]))
            reportError(actuals[i], "Argument <actuals[i]> should have type <fmt(formals[i])>, found <fmt(actuals[i])>");
    }
    adtType = avalue();
    try    adtType = instantiateRascalTypeParams(typeof(adtName, scope, {dataId()}), bindings);
    catch invalidInstantiation(str msg):
           reportError(current, msg);
      
    checkKwArgs(kwFormals + getCommonKeywords(adtType), keywordArguments, bindings);
    return adtType;
}

void checkKwArgs(list[Keyword] kwFormals, keywordArguments, Bindings bindings){
    if(keywordArguments is none) return;
 
    next_arg:
    for(kwa <- keywordArguments.keywordArgumentList){ 
        kwName = "<kwa.name>";
        kwType = typeof(kwa.expression);
        
        for(<ft, fn, de> <- kwFormals){
           if(kwName == fn){
              ift = ft;
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

list[Keyword] getCommonKeywords(aadt(str adtName, list[AType] parameters, list[Keyword] common)) =  common;
list[Keyword] getCommonKeywords(overloadedAType(rel[Key, AType] overloads)) = [ *getCommonKeywords(adt) | <def, adt> <- overloads ];

// ---- tuple

void collect(current: (Expression) `\< <{Expression ","}+ elements1> \>`, FRBuilder frb){
    elms = [ e | Expression e <- elements1 ];
    frb.calculateEager("tuple expression", current, elms,
        AType() {
                return atuple(atypeList([ typeof(elm) | elm <- elms ]));
        });
    collectParts(current, frb);
}

// ---- map

void collect(current: (Expression) `( <{Mapping[Expression] ","}* mappings>)`, FRBuilder frb){
    froms = [ m.from | m <- mappings ];
    tos =  [ m.to | m <- mappings ];
    frb.calculateEager("map expression", current, froms + tos,
        AType() {
                return amap(lub([ typeof(f) | f <- froms ]), lub([ typeof(t) | t <- tos ]));
        });
    collectParts(current, frb);
}

// ---- it

// ---- qualified name
 
void collect(current: (Expression) `<QualifiedName name>`, FRBuilder frb){
    qname = convertName(name);
    if(isQualified(qname)){     
       frb.use_qual([qname.qualifier, qname.name], name, {variableId(), functionId(), constructorId()}, {dataId()} );
    } else {
       frb.use(name, {variableId(), formalId(), functionId(), constructorId()});
    }
}

// ---- subscript

void collect(current:(Expression)`<Expression expression> [ <{Expression ","}+ indices> ]`, FRBuilder frb){
    indexList = [e | e <- indices];
    // Subscripts can also use the "_" character, which means to ignore that position; we do
    // that here by treating it as avalue(), which is comparable to all other types and will
    // thus work when calculating the type below.
    
    for(e <- indexList, (Expression)`_` := e){
        frb.fact(e, avalue());
    }
    
    frb.calculate("subscription", current, expression + indexList,
                  AType(){ return computeSubscriptionType(current, typeof(expression), [typeof(e) | e <- indexList]);  });
    collectParts(current, frb);
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

void collect(current: (Expression) `<Expression e> [ <OptionalExpression ofirst> .. <OptionalExpression olast> ]`, FRBuilder frb){
    if(ofirst is noExpression) frb.fact(ofirst, aint());
    if(olast is noExpression) frb.fact(olast, aint());

    frb.calculate("slice", current, [e, ofirst, olast],
        AType(){ return computeSliceType(typeof(e), typeof(ofirst), aint(), typeof(olast)); });
    collectParts(current, frb);
}

AType computeSliceType(AType base, AType first, AType step, AType last){
    failures = {};
    if(!isIntType(first)) failures += "The first slice index must be of type `int`, found <fmt(first)>";
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

void collect(current: (Expression) `<Expression e> [ <OptionalExpression ofirst>, <Expression second> .. <OptionalExpression olast> ]`, FRBuilder frb){
    if(ofirst is noExpression) frb.fact(ofirst, aint());
    if(olast is noExpression) frb.fact(olast, aint());

    frb.calculate("slice step", current, [e, ofirst, second, olast],
        AType(){ return computeSliceType(typeof(e), typeof(ofirst), typeof(second), typeof(olast)); });
    collectParts(current, frb);
}

// ---- fieldAccess

void collect(current: (Expression) `<Expression expression> . <Name field>`, FRBuilder frb){
    scope = frb.getScope();
    
    frb.calculate("field access", current, [expression],
        AType(){ return computeFieldType(current, typeof(expression), prettyPrintQName(convertName(field)), scope); });
    collectParts(current, frb);
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
 
    if (isLocType(t1)) {
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
                return typeof("Symbol", scope, {dataId()});
            } catch TypeUnavailable():{
               reportError(current, "The type `Symbol` of field <fmt(fieldName)> is not in scope");
            }
        } else if (fieldName == "definitions") {
            try {
                typeof("Symbol", scope, {dataId()});
                try {   
                    typeof("Production", scope, {dataId()});
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
    } else if (aadt(adtName, list[AType] actualTypeParams, list[Keyword] common) := t1){
        try {
            if (getADTName(t1) == "Tree" && fieldName == "top") {
                return t1;
            }
            fieldType = typeof(fieldName, scope, {fieldId()});
            
            if(overloadedAType(rel[Key, AType] overloads) := fieldType){
               reduced = { tp | tp <- range(overloads), isADTType(tp.base) && getADTName(tp.base) == adtName };
               if({commonType} := reduced){
                  fieldType = commonType;
               } else {
                  reportError(current, "Field <fmt(fieldName)> can have several types <fmt(range(overloads))>");
               }
            } 
            
            declaredType = typeof(adtName, scope, {dataId()});
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
            return fieldType;
        } catch TypeUnavailable(): {
            reportError(current, "Cannot compute type of field <fmt(fieldName)>, user type <fmt(t1)> has not been declared or is out of scope"); 
        }
        
    //} else if (isStartNonTerminalType(t1)) {
    //    nonterminalName = RSimpleName("start[<getNonTerminalName(t1)>]");
    //    if (nonterminalName in c.globalSortMap && c.store[c.globalSortMap[nonterminalName]] is sorttype) {
    //        sortId = c.globalSortMap[nonterminalName];
    //        usingTree = false;
    //        if (fieldName == "top") {
    //            return getStartNonTerminalType(t1);
    //        }
    //        if (<sortId,fieldName> notin c.nonterminalFields) {
    //            if (RSimpleName("Tree") in c.globalAdtMap && c.store[c.globalAdtMap[RSimpleName("Tree")]] is datatype) {
    //                treeId = c.globalAdtMap[RSimpleName("Tree")];
    //                if (<treeId,fieldName> notin c.adtFields) {
    //                   reportError(current, "Field <fmt(fieldName)> does not exist on type <fmt(t1)>");
    //                }
    //                sortId = treeId; // The field is not on the nonterminal directly, but is on Tree
    //                usingTree = true;
    //            } else {
    //               reportError(current, "Field <fmt(fieldName)> does not exist on type <fmt(t1)>");
    //            }
    //        } 
    //        originalType = c.store[sortId].rtype;
    //        originalParams = getNonTerminalTypeParameters(originalType);
    //        fieldType = usingTree ? c.adtFields[<sortId,fieldName>] : c.nonterminalFields[<sortId,fieldName>];
    //        if (size(originalParams) > 0) {
    //            actualParams = getNonTerminalTypeParameters(t1);
    //            if (size(originalParams) != size(actualParams)) {
    //               reportError(current, "Invalid nonterminal type, the number of type parameters (<size(originalParams)>,<size(actualParams)>) is inconsistent");
    //            } else {
    //                bindings = ( getRascalTypeParamName(originalParams[idx]) : actualParams[idx] | idx <- index(originalParams));
    //                try {
    //                    fieldType = instantiate(fieldType, bindings);
    //                } catch : {
    //                   reportError(current, "Failed to instantiate type parameters in field type");
    //                }                       
    //            }
    //        }                                               
    //        return fieldType;
    //    } else {
    //       reportError(current, "Cannot compute type of field <fmt(fieldName)>, nonterminal type <fmt(t1)> has not been declared");
    //    } 
    //} else if (isNonTerminalType(t1)) {
    //    nonterminalName = RSimpleName(getNonTerminalName(t1));
    //    if (nonterminalName in c.globalSortMap && c.store[c.globalSortMap[nonterminalName]] is sorttype) {
    //        sortId = c.globalSortMap[nonterminalName];
    //        usingTree = false;
    //        if (<sortId,fieldName> notin c.nonterminalFields) {
    //            if (RSimpleName("Tree") in c.globalAdtMap && c.store[c.globalAdtMap[RSimpleName("Tree")]] is datatype) {
    //                treeId = c.globalAdtMap[RSimpleName("Tree")];
    //                if (<treeId,fieldName> notin c.adtFields) {
    //                   reportError(current, "Field <fmt(fieldName)> does not exist on type <fmt(t1)>");
    //                }
    //                sortId = treeId; // The field is not on the nonterminal directly, but is on Tree
    //                usingTree = true;
    //            } else {
    //               reportError(current, "Field <fmt(fieldName)> does not exist on type <fmt(t1)>");
    //            }
    //        } 
    //        originalType = c.store[sortId].rtype;
    //        originalParams = getNonTerminalTypeParameters(originalType);
    //        fieldType = usingTree ? c.adtFields[<sortId,fieldName>] : c.nonterminalFields[<sortId,fieldName>];
    //        if (size(originalParams) > 0) {
    //            actualParams = getNonTerminalTypeParameters(t1);
    //            if (size(originalParams) != size(actualParams)) {
    //               reportError(current, "Invalid nonterminal type, the number of type parameters (<size(originalParams)>,<size(actualParams)>) is inconsistent");
    //            } else {
    //                bindings = ( getRascalTypeParamName(originalParams[idx]) : actualParams[idx] | idx <- index(originalParams));
    //                try {
    //                    fieldType = instantiate(fieldType, bindings);
    //                } catch : {
    //                   reportError(current, "Failed to instantiate type parameters in field type");
    //                }                       
    //            }
    //        }                                               
    //        return fieldType;
    //    } else {
    //       reportError(current, "Cannot compute type of field <fmt(fieldName)>, nonterminal type <fmt(t1)> has not been declared"); 
    //    }  
    } else if (isTupleType(t1)) {
        idx = indexOf(getTupleFieldNames(t1), fieldName);
        if(idx >= 0)
            return getTupleFieldTypes(t1)[idx];
        else
            reportError(current, "Field <fmt(fieldName)> does not exist on type <fmt(t1)>");
    } else if (isNodeType(t1)) {
        return \value();
    }
     
    reportError(current, "Cannot access fields on type <fmt(t1)>");
}
// ---- fieldUpdate

void collect(current:(Expression) `<Expression expression> [ <Name field> = <Expression repl> ]`, FRBuilder frb){
    scope = frb.getScope();
    frb.use(field, {fieldId()});
    frb.calculate("field update", current, [expression, field, repl],
        AType(){ fieldType = computeFieldType(current, typeof(expression), prettyPrintQName(convertName(field)), typeof(field), scope);
                 replType = typeof(repl);
                 subtype(replType, fieldType, onError(current, "Cannot assign type <fmt(replType)> into field of type <fmt(fieldType)>"));
                 return typeof(expression);
        });
    collectParts(current, frb);
}

// ---- fieldProjection

void collect(current:(Expression) `<Expression expression> \< <{Field ","}+ fields> \>`, FRBuilder frb){

    flds = [f | f <- fields];
    frb.calculate("field projection", current, [expression],
        AType(){ return computeFieldProjectionType(current, typeof(expression), flds); });
    collectParts(current, frb);
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
// ---- getAnnotation
