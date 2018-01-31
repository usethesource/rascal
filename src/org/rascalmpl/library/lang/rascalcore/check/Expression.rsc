module lang::rascalcore::check::Expression

extend analysis::typepal::TypePal;
extend analysis::typepal::ExtractTModel;

import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::ATypeExceptions;
import lang::rascalcore::check::TypePalConfig;
import lang::rascalcore::check::ATypeInstantiation;
import lang::rascalcore::grammar::definition::Symbols;

import lang::rascalcore::check::Declaration;
import lang::rascalcore::check::Operators;
import lang::rascalcore::check::Pattern;
import lang::rascalcore::check::Statement;
import lang::rascalcore::check::ConvertType;

import lang::rascal::\syntax::Rascal;
import String;
import ListRelation;
import Set;
import Map;
import Node;

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
    collect(sl, tb);
}

void collect(current: (StringTemplate) `if(<{Expression ","}+ conditions>){ <Statement* preStats> <StringMiddle body> <Statement* postStats> }`, TBuilder tb){
    tb.enterScope(conditions);  // thenPart may refer to variables defined in conditions
        condList = [cond | Expression cond <- conditions];
        tb.fact(current, avalue());
        tb.requireEager("if then template", current, condList, (){ checkConditions(condList); });
        beginPatternScope("conditions", tb);
        collect(conditions, tb);
        endPatternScope(tb);
        collect(preStats, body, postStats, tb);
    tb.leaveScope(conditions);
}

void collect(current: (StringTemplate) `if( <{Expression ","}+ conditions> ){ <Statement* preStatsThen> <StringMiddle thenString> <Statement* postStatsThen> } else { <Statement* preStatsElse> <StringMiddle elseString> <Statement* postStatsElse> }`, TBuilder tb){
    tb.enterScope(conditions);   // thenPart may refer to variables defined in conditions; elsePart may not
    
        condList = [cond | Expression cond <- conditions];
        // TODO scoping in else does not yet work
        if(!isEmpty([s | s <- preStatsElse]))
           storeExcludeUse(conditions, preStatsElse, tb); // variable occurrences in elsePart may not refer to variables defined in conditions
        if(!isEmpty("<elseString>"))
           storeExcludeUse(conditions, elseString, tb); 
        if(!isEmpty([s | s <- postStatsElse]))
           storeExcludeUse(conditions, postStatsElse, tb);
        
        tb.calculate("if then else template", current, condList/* + [postStatsThen + postStatsElse]*/,
            AType (){ checkConditions(condList); 
                      return avalue();
            });
        beginPatternScope("conditions", tb);
        collect(condList, tb);
        endPatternScope(tb);
        collect(preStatsThen, thenString, postStatsThen, preStatsElse, elseString, postStatsElse, tb);    
    tb.leaveScope(conditions);
} 

void collect(current: (StringTemplate) `for( <{Expression ","}+ generators> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`, TBuilder tb){
    tb.enterScope(generators);   // body may refer to variables defined in conditions
        condList = [cond | Expression cond <- generators];
        tb.requireEager("for statement  template", current, condList, (){ checkConditions(condList); });
        beginPatternScope("conditions", tb);
        collect(condList, tb);
        endPatternScope(tb);
        collect(preStats, body, postStats, tb);
    tb.leaveScope(generators);
}

void collect(current: (StringTemplate) `do { <Statement* preStats> <StringMiddle body> <Statement* postStats> } while( <Expression condition> )`, TBuilder tb){
    tb.enterScope(current);   // condition may refer to variables defined in body
        condList = [condition];
        tb.requireEager("do statement template", current, condList, (){ checkConditions(condList); });
        collect(preStats, body, postStats, tb);
        beginPatternScope("conditions", tb);
        collect(condition, tb);
        endPatternScope(tb);
    tb.leaveScope(current); 
}

void collect(current: (StringTemplate) `while( <Expression condition> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`, TBuilder tb){
    tb.enterScope(condition);   // body may refer to variables defined in conditions
        condList = [condition];
        tb.requireEager("while statement  template", current, condList, (){ checkConditions(condList); });
        beginPatternScope("conditions", tb);
        collect(condList, tb);
        endPatternScope(tb);
        collect(preStats, body, postStats, tb);
    tb.leaveScope(condition);
} 


void collect(Literal l:(Literal)`<LocationLiteral ll>`, TBuilder tb){
    tb.fact(l, aloc());
    collect(ll, tb);
}

// ---- Concrete literals

void collect(Concrete concrete, TBuilder tb){
//println("Concrete: <concrete>");
    tb.fact(concrete, sym2AType(concrete.symbol));
    collectLexical(concrete.parts, tb);
}

void collect(current: (ConcreteHole) `\< <Sym symbol> <Name name> \>`, TBuilder tb){
    varType = sym2AType(symbol);
    scope = tb.getScope();
    //println("patternContainer: <tb.getStack(patternContainer)>");
    if(size(tb.getStack(patternContainer)) == 1){    // An expression
        //println("ConcreteHole exp: <current>");
        tb.use(name, {variableId()});
        tb.calculate("concrete hole", current, [], AType() { return expandUserTypes(varType, scope); });
    } else {                                        //A pattern
        //println("ConcreteHole pat: <current>");
        uname = unescape("<name>");
        if(uname != "_"){
           if(uname in tb.getStack(patternNames)){
              tb.useLub(name, {variableId()});
           } else {
               tb.push(patternNames, uname);
               dt = defType([], AType(){ return expandUserTypes(varType, scope); });
               tb.define(unescape("<name>"), variableId(), name, dt); 
           }
        }
    }
    tb.calculateEager("concrete hole", current, [], AType() { return expandUserTypes(varType, scope); });
}

// Rascal expressions

// ---- non-empty block

void collect(current: (Expression) `{ <Statement+ statements> }`, TBuilder tb){
    stats = [ stat | Statement stat <- statements ];
    tb.calculate("non-empty block expression", current, [stats[-1]],  AType() { return getType(stats[-1]); } );
    collect(statements, tb);
}

// ---- brackets

void collect(current: (Expression) `( <Expression expression> )`, TBuilder tb){
    tb.calculate("brackets", current, [expression],  AType() { return getType(expression); } );
    collect(expression, tb);
}

// ---- closure

void collect(current: (Expression) `<Type tp> <Parameters parameters> { <Statement+ statements> }`, TBuilder tb){
    tb.enterScope(current);
        retType = convertType(tp, tb);
        scope = tb.getScope();
        <formals, kwTypeVars, kwFormals> = checkFunctionType(scope, retType, parameters, false, tb);
        tb.calculate("type of closure", current, formals,
            AType (){ return afunc(expandUserTypes(retType, scope), atypeList([getType(f) | f <- formals]), kwFormals); });
        collect(statements, tb);
    tb.leaveScope(current);
}

// ---- void closure

void collect(current: (Expression) `<Parameters parameters> { <Statement* statements0> }`, TBuilder tb){
    tb.enterScope(current);
        scope = tb.getScope();
        <formals, kwTypeVars, kwFormals> = checkFunctionType(scope, avoid(), parameters, false, tb);
        tb.calculate("type of void closure", current, formals,
            AType (){ return afunc(avoid(), atypeList([getType(f) | f <- formals]), kwFormals); });
        collect(statements0, tb);
    tb.leaveScope(current);
}

// ---- step range

void collect(current: (Expression) `[ <Expression first> , <Expression second> .. <Expression last> ]`, TBuilder tb){
    tb.calculate("step range", current, [first, second, last],
        AType(){ t1 = getType(first); t2 = getType(second); t3 = getType(last);
                 subtype(t1,anum()) || reportError(first, "Invalid type: expected numeric type, found <fmt(t1)>");
                 subtype(t2,anum()) || reportError(second, "Invalid type: expected numeric type, found <fmt(t2)>");
                 subtype(t3,anum()) || reportError(last, "Invalid type: expected numeric type, found <fmt(t3)>");
                 return alist(lub([t1, t2, t3]));
        
        });
    collect(first, second, last, tb);    
}

// ---- range

void collect(current: (Expression) `[ <Expression first> .. <Expression last> ]`, TBuilder tb){
    tb.calculate("step range", current, [first, last],
        AType(){ t1 = getType(first); t2 = getType(last);
                 subtype(t1,anum()) || reportError(first, "Invalid type: expected numeric type, found <fmt(t1)>");
                 subtype(t2,anum()) || reportError(last, "Invalid type: expected numeric type, found <fmt(t2)>");
                 return alist(lub([t1, t2]));
        });
    collect(first, last, tb);    
}

// ---- visit

void collect(current: (Expression) `<Label label> <Visit vst>`, TBuilder tb){
    tb.enterScope(current);
        scope = tb.getScope();
        tb.setScopeInfo(scope, visitOrSwitchScope(), visitOrSwitchInfo(vst.subject, true));
        if(label is \default){
            tb.define("<label.name>", labelId(), label.name, noDefInfo());
        }
        tb.calculate("visit", current, [vst.subject], AType(){ return getType(vst.subject); });
        collect(vst, tb);
    tb.leaveScope(current);
}

// ---- reifyType

void collect(current: (Expression) `# <Type tp>`, TBuilder tb){
    rt = convertType(tp, tb);
    scope = tb.getScope();
    tb.calculate("reified type", current, [], AType() { return areified(expandUserTypes(rt, scope)); });
    //collectParts(current, tb);
}

// ---- reifiedType

void collect(current: (Expression) `type ( <Expression es> , <Expression ed> )`, TBuilder tb) {
    // TODO: Is there anything we can do statically to make the result type more accurate?
    tb.fact(current, areified(avalue()));
    tb.require("reified type", current, [es, ed],
        (){ 
            subtype(getType(es), aadt("Symbol",[], dataSyntax())) || reportError(es, "Expected subtype of Symbol, instead found <fmt(getType(es))>");
            subtype(getType(ed), amap(aadt("Symbol",[],dataSyntax()),aadt("Production",[],dataSyntax()))) || 
                reportError(ed, "Expected subtype of map[Symbol,Production], instead found <fmt(getType(ed))>");
          });
    collect(es, ed, tb);
}

// ---- any

void collect(current: (Expression)`any ( <{Expression ","}+ generators> )`, TBuilder tb){
    gens = [gen | gen <- generators];
    tb.fact(current, abool());
    
    tb.enterScope(generators);
    beginPatternScope("any", tb);
        tb.require("any", current, gens,
            () { for(gen <- gens) if(getType(gen) != abool()) reportError(gen, "Type of generator should be `bool`, found <fmt(getType(gen))>");
            });
        collect(generators, tb);
    endPatternScope(tb);
    tb.leaveScope(generators);
}

// ---- all

void collect(current: (Expression)`all ( <{Expression ","}+ generators> )`, TBuilder tb){
    gens = [gen | gen <- generators];
    tb.fact(current, abool());
    
    //newScope = tb.getScope() != getLoc(current);
    //if(newScope) tb.enterScope(current);
    tb.enterScope(generators);
    beginPatternScope("all", tb);
        tb.require("all", current, gens,
            () { for(gen <- gens) if(getType(gen) != abool()) reportError(gen, "Type of generator should be `bool`, found <fmt(getType(gen))>");
            });
        collect(generators, tb);
    endPatternScope(tb);
    //if(newScope) tb.leaveScope(current);
    tb.leaveScope(generators);
}

// ---- comprehensions and reducer

// set comprehension

void collect(current: (Expression)`{ <{Expression ","}+ results> | <{Expression ","}+ generators> }`, TBuilder tb){
    gens = [gen | gen <- generators];
    res  = [r | r <- results];
    storeAllowUseBeforeDef(current, results, tb); // variable occurrences in results may refer to variables defined in generators
    tb.enterScope(current);
    beginPatternScope("set-comprehension", tb);
        tb.require("set comprehension", current, gens,
            () { for(gen <- gens) if(getType(gen) != abool()) reportError(gen, "Type of generator should be `bool`, found <fmt(getType(gen))>");
            });
        tb.calculate("set comprehension results", current, res,
            AType(){
                return makeSetType(lubList([ getType(r) | r <- res]));
            });
         
        collect(results, generators, tb);
    endPatternScope(tb);
    tb.leaveScope(current);
}

// list comprehension

void collect(current: (Expression) `[ <{Expression ","}+ results> | <{Expression ","}+ generators> ]`, TBuilder tb){
    gens = [gen | gen <- generators];
    res  = [r | r <- results];
    storeAllowUseBeforeDef(current, results, tb); // variable occurrences in results may refer to variables defined in generators
    tb.enterScope(current);
    beginPatternScope("list-comprehension", tb);
        tb.require("list comprehension", current, gens,
            () { for(gen <- gens) if(getType(gen) != abool()) reportError(gen, "Type of generator should be `bool`, found <fmt(getType(gen))>");
            });
        tb.calculate("list comprehension results", current, res,
            AType(){
                return makeListType(lubList([ getType(r) | r <- res]));
            });
         
        collect(results, generators, tb);
    endPatternScope(tb);
    tb.leaveScope(current);
}

// map comprehension

void collect(current: (Expression) `(<Expression from> : <Expression to> | <{Expression ","}+ generators> )`, TBuilder tb){
    gens = [gen | gen <- generators];
    storeAllowUseBeforeDef(current, from, tb); // variable occurrences in from may refer to variables defined in generators
    storeAllowUseBeforeDef(current, to, tb); // variable occurrences in to may refer to variables defined in generators
    tb.enterScope(current);
    beginPatternScope("map-comprehension", tb);
        tb.require("map comprehension", current, gens,
            () { for(gen <- gens) if(getType(gen) != abool()) reportError(gen, "Type of generator should be `bool`, found <fmt(getType(gen))>");
            });
        tb.calculate("list comprehension results", current, [from, to],
            AType(){
                return makeMapType(unset(getType(from), "label"), unset(getType(to), "label"));
            });
         
        collect(from, to, generators, tb);
    endPatternScope(tb);
    tb.leaveScope(current);
}

// ---- reducer

void collect(current: (Expression) `( <Expression init> | <Expression result> | <{Expression ","}+ generators> )`, TBuilder tb){
    gens = [gen | gen <- generators];
    storeAllowUseBeforeDef(current, result, tb); // variable occurrences in result may refer to variables defined in generators
    tb.enterScope(current);
    beginPatternScope("reducer", tb);
        //tau = tb.newTypeVar();
        tb.define("it", variableId(), init, defLub([init, result], AType() { return lub(getType(init), getType(result)); }));
        tb.require("reducer", current, gens,
            () { for(gen <- gens) if(getType(gen) != abool()) reportError(gen, "Type of generator should be `bool`, found <fmt(getType(gen))>");
            });
        tb.calculate("reducer result", current, [result], AType () { return getType(result); });
        //tb.requireEager("reducer it", current, [init, result], (){ unify(tau, lub(getType(init), getType(result)), onError(current, "Can determine it")); });
         
        collect(init, result, generators, tb);
    endPatternScope(tb);
    tb.leaveScope(current);
}

void collect(current: (Expression) `it`, TBuilder tb){
    tb.use(current, {variableId()});
}

// ---- set

void collect(current: (Expression) `{ <{Expression ","}* elements0> }`, TBuilder tb){
    elms = [ e | Expression e <- elements0 ];
    if(isEmpty(elms)){
        tb.fact(current, aset(avoid()));
    } else {
        tb.calculate("set expression", current, elms, AType() { return aset(lub([getType(elm) | elm <- elms])); });
        collect(elements0, tb);
    }
}

// ---- list

void collect(current: (Expression) `[ <{Expression ","}* elements0> ]`, TBuilder tb){
    elms = [ e | Expression e <- elements0 ];
    if(isEmpty(elms)){
        tb.fact(current, alist(avoid()));
    } else {
        tb.calculate("list expression", current, elms, AType() { return alist(lub([getType(elm) | elm <- elms])); });
        collect(elements0, tb);
    }
}

// ---- call or tree
           
void collect(current: (Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments[Expression] keywordArguments>)`, TBuilder tb){
    actuals = [a | Expression a <- arguments];  
    kwactuals = keywordArguments is \default ? [ kwa.expression | kwa <- keywordArguments.keywordArgumentList] : [];
 
    scope = tb.getScope();
    
    tb.calculate("call of function/constructor <fmt("<expression>")>", current, expression + actuals + kwactuals,
        AType(){
            if(any(x <- expression + actuals + kwactuals, !isFullyInstantiated(getType(x)))) throw TypeUnavailable();
            
            texp = getType(expression);
            try {
                texp = expandUserTypes(texp, scope);
            } catch TypeUnavailable(): {
                missing = getUndefinedADTs(texp, scope);
                if(size(missing) > 0){
                    reportError(current, "Missing import, <intercalateAnd(getUndefinedADTs(texp, scope))> not available here but used in type of <fmt("<expression>")>: <fmt(expression)>");
                } else {
                    reportError(current, "Cannot expand all types in type of <fmt("<expression>")>: <fmt(expression)>");
                }
            }
            if(isStrType(texp)){
                return computeNodeType(current, scope, actuals, keywordArguments);
            } 
            if(isLocType(texp)){
                nactuals = size(actuals);
                if(!(nactuals == 2 || nactuals == 4)) reportError(current, "Source locations requires 2 or 4 arguments, found <nactuals>");
                equal(getType(actuals[0]), aint()) || reportError(actuals[0], "Offset should be of type `int`, found <fmt(actuals[0])>");
                equal(getType(actuals[1]), aint()) || reportError(actuals[1], "Length should be of type `int`, found <fmt(actuals[1])>");

                if(nactuals == 4){
                    equal(getType(actuals[2]), atuple(atypeList([aint(),aint()]))) || reportError(actuals[2], "Begin should be of type `tuple[int,int]`, found <fmt(actuals[2])>");
                    equal(getType(actuals[3]), atuple(atypeList([aint(),aint()]))) || reportError(actuals[3], "End should be of type `tuple[int,int]`, found <fmt(actuals[3])>"); 
                }
                return aloc();
            }
             
            if(overloadedAType(rel[Key, IdRole, AType] overloads) := texp){
              <filteredOverloads, identicalFormals> = filterOverloads(overloads, size(actuals));
              if({<key, idr, tp>} := filteredOverloads){
                texp = tp;
              } else {
                overloads = filteredOverloads;
                validReturnTypeOverloads = {};
                validOverloads = {};
                next_fun:
                for(ovl: <key, idr, tp> <- overloads){                       
                    if(ft:afunc(AType ret, atypeList(list[AType] formals), list[Keyword] kwFormals) := tp){
                       try {
                            //if(tp.deprecationMessage?){
                            //    reportWarning(expression, "Deprecated function <fmt(tp)><isEmpty(tp.deprecationMessage) ? "" : ": " + tp.deprecationMessage>");
                            //}
                            validReturnTypeOverloads += <key, dataId(), checkArgsAndComputeReturnType(current, scope, ret, formals, kwFormals, ft.varArgs ? false, actuals, keywordArguments, identicalFormals)>;
                            validOverloads += ovl;
                       } catch checkFailed(set[Message] msgs):
                             continue next_fun;
                    }
                 }
                 next_cons:
                 for(ovl: <key, idr, tp> <- overloads){
                    if(acons(ret:aadt(adtName, list[AType] parameters, _), str consName, list[NamedField] fields, list[Keyword] kwFields) := tp){
                       try {
                            validReturnTypeOverloads += <key, dataId(), computeADTType(current, adtName, scope, ret, fields<1>, kwFields, actuals, keywordArguments, identicalFormals)>;
                            validOverloads += ovl;
                       } catch checkFailed(set[Message] msgs):
                             continue next_cons;
                    }
                 }
                 if({<key, idr, tp>} := validOverloads){
                    texp = tp;  
                    // TODO check identicalFields to see whether this can make sense
                    // unique overload, fall through to non-overloaded case to potentially bind more type variables
                 } else if(isEmpty(validReturnTypeOverloads)) { 
                        reportError(current, "<fmt("<expression>")> is defined as <fmt(expression)> and cannot be applied to argument(s) <fmt(actuals)>");}
                 else return overloadedAType(validReturnTypeOverloads);
               }
            }
            
            if(ft:afunc(AType ret, atypeList(list[AType] formals), list[Keyword] kwFormals) := texp){
               //if(texp.deprecationMessage?){
               //     reportWarning(expression, "Deprecated function <fmt(texp)><isEmpty(texp.deprecationMessage) ? "": ": " + texp.deprecationMessage>");
               //}
                return checkArgsAndComputeReturnType(current, scope, ret, formals, kwFormals, ft.varArgs, actuals, keywordArguments, [true | int i <- index(formals)]);
            }
            if(acons(ret:aadt(adtName, list[AType] parameters,_), str consName, list[NamedField] fields, list[Keyword] kwFields) := texp){
               return computeADTType(current, adtName, scope, ret, fields<1>, kwFields, actuals, keywordArguments, [true | int i <- index(fields)]);
            }
            reportError(current, "<fmt("<expression>")> is defined as <fmt(expression)> and cannot be applied to argument(s) <fmt(actuals)>");
        });
      collect(expression, arguments, keywordArguments, tb);
}

tuple[rel[Key, IdRole, AType], list[bool]] filterOverloads(rel[Key, IdRole, AType] overloads, int arity){
    filteredOverloads = {};
    prevFormals = [];
    identicalFormals = [true | int i <- [0 .. arity]];
    for(ovl:<key, idr, tp> <- overloads){                       
        if(ft:afunc(AType ret, atypeList(list[AType] formals), list[Keyword] kwFormals) := tp){
           if(ft.varArgs ? (arity >= size(formals)) : (arity == size(formals))) {
              filteredOverloads += ovl;
              if(isEmpty(prevFormals)){
                 prevFormals = formals;
              } else {
                 for(int i <- index(formals)) identicalFormals[i] = identicalFormals[i] && (comparable(prevFormals[i], formals[i]));
              }
           }
        } else
        if(acons(aadt(adtName, list[AType] parameters,_), str consName, list[NamedField] fields, list[Keyword] kwFields) := tp){
           if(size(fields) == arity){
              filteredOverloads += ovl;
              if(isEmpty(prevFormals)){
                 prevFormals = fields<1>;
              } else {
                 for(int i <- index(fields)) identicalFormals[i] = identicalFormals[i] && (comparable(prevFormals[i], fields[i].fieldType));
              }
            }
        }
    }
    return <filteredOverloads, identicalFormals>;
}

// TODO: in order to reuse the function below `keywordArguments` is passed as where `eywordArguments[&T] keywordArguments` would make more sense.
// The interpreter does not handle this well, so revisit this later

AType checkArgsAndComputeReturnType(Expression current, Key scope, AType retType, list[AType] formals, list[Keyword] kwFormals, bool isVarArgs, list[Expression] actuals, keywordArguments, list[bool] identicalFormals){
    nactuals = size(actuals); nformals = size(formals);
   
    list[AType] actualTypes = [];
   
    if(isVarArgs){
       if(nactuals < nformals - 1) reportError(current, "Expected at least <fmt(nformals-1, "argument")>, found <nactuals>");
       varArgsType = (avoid() | lub(it, getType(actuals[i])) | int i <- [nformals-1 .. nactuals]);
       actualTypes = [getType(actuals[i]) | int i <- [0 .. nformals-1]] + (isListType(varArgsType) ? varArgsType : alist(varArgsType));
    } else {
        if(nactuals != nformals) reportError(current, "Expected <fmt(nformals, "argument")>, found <nactuals>");
        actualTypes = [getType(a) | a <- actuals];
    }
    
    index_formals = index(formals);
    
    list[AType] formalTypes =  [ expandUserTypes(formals[i], scope) | i <- index_formals ];
    
    for(int i <- index_formals){
        if(overloadedAType(rel[Key, IdRole, AType] overloads) := actualTypes[i]){   // TODO only handles a single overloaded actual
            //println("checkArgsAndComputeReturnType: <current>");
            //iprintln(overloads);
            returnTypeForOverloadedActuals = {};
            for(ovl: <key, idr, tp> <- overloads){   
                try {
                    actualTypes[i] = tp;
                    returnTypeForOverloadedActuals += <key, idr, computeReturnType(current, scope, retType, formalTypes, actuals, actualTypes, kwFormals, keywordArguments, identicalFormals)>;
                    //println("succeeds: <ovl>");
                } catch checkFailed(set[Message] msgs): {
                    //println("fails: <ovl>");
                    ; // continue with next overload
                }
             }
             if(isEmpty(returnTypeForOverloadedActuals)) { reportError(current, "Call with arguments <fmt(actuals)> cannot be resolved");}
             else return overloadedAType(returnTypeForOverloadedActuals);
        }
    }
    //No overloaded actual
    return computeReturnType(current, scope, retType, formalTypes, actuals, actualTypes, kwFormals, keywordArguments, identicalFormals);
}

AType computeReturnType(Expression current, Key scope, AType retType, list[AType] formalTypes, list[Expression] actuals, list[AType] actualTypes, list[Keyword] kwFormals, keywordArguments, list[bool] identicalFormals){
    index_formals = index(formalTypes);
    Bindings bindings = ();
    for(int i <- index_formals){
        try   bindings = matchRascalTypeParams(formalTypes[i], actualTypes[i], bindings, bindIdenticalVars=true);
        catch invalidMatch(str reason):
              reportError(actuals[i], reason);
    }
  
    iformalTypes = formalTypes;
    if(!isEmpty(bindings)){
        try {
          iformalTypes = [instantiateRascalTypeParams(formalTypes[i], bindings) | int i <- index_formals];
        } catch invalidInstantiation(str msg): {
            reportError(current, msg);
        }
    }
    
    for(int i <- index_formals){
        ai = actualTypes[i];
        ai = instantiate(ai);
        if(tvar(loc l) := ai || !isFullyInstantiated(ai)){
           if(identicalFormals[i]){
              unify(ai, iformalTypes[i]) || reportError(current, "Cannot unify <fmt(ai)> with <fmt(iformalTypes[i])>");
              ai = instantiate(ai);
              //clearBindings();
           } else
              continue;
        }
        comparable(ai, iformalTypes[i]) ||
           reportError(actuals[i], "Argument should have type <fmt(iformalTypes[i])>, found <fmt(ai)>");       
    }
    
    checkKwArgs(kwFormals, keywordArguments, bindings, scope, isExpression=true);
    
    // Artificially bind unbound type parameters in the return type
    for(rparam <- collectAndUnlabelRascalTypeParams(retType)){
        pname = rparam.pname;
        if(!bindings[pname]?) bindings[pname] = rparam;
    }
    if(isEmpty(bindings))
       return retType;
    try   return instantiateRascalTypeParams(retType, bindings);
    catch invalidInstantiation(str msg):
          reportError(current, msg);
}

AType computeADTType(Tree current, str adtName, Key scope, AType retType, list[AType] formals, list[Keyword] kwFormals, actuals, keywordArguments, list[bool] identicalFormals){                     
    //println("---- <current>, identicalFormals: <identicalFormals>");
    nactuals = size(actuals); nformals = size(formals);
    if(nactuals != nformals){
        reportError(current, "Expected <fmt(nformals, "argument")>, found <nactuals>");
    }
    formalTypes = [ expandUserTypes(formals[i], scope) | i <- index(formals) ];
    index_formals = index(formals);
    //println("formals: <formals>");
    list[AType] actualTypes = [];
    list[bool] dontCare = [];
    bool isExpression = true;
    switch(actuals){
        case list[Expression] expList: {
                dontCare = [ false | i <- index(expList) ];
                actualTypes = [ getType(expList[i]) | i <- index(expList) ];
                //print("expList: [ "); for(i <- index(expList)) print("<expList[i]> "); println(" ]");
            }
        case list[Pattern] patList: {
                dontCare = [ "<patList[i]>" == "_" | i <- index(patList) ];
                actualTypes = [ dontCare[i] ? formals[i] : expandUserTypes(getPatternType(patList[i], formals[i], scope), scope) | i <- index(patList) ];
                //print("patList: [ "); for(i <- index(patList)) print("<patList[i]> "); println(" ]");
                isExpression = false;
            }
        default:
            throw "Illegal argument `actuals`";
    }
    
    for(int i <- index_formals){
        if(overloadedAType(rel[Key, IdRole, AType] overloads) := actualTypes[i]){   // TODO only handles a single overloaded actual
            //println("computeADTType: <current>");
            //iprintln(overloads);
            returnTypeForOverloadedActuals = {};
            for(ovl: <key, idr, tp> <- overloads){   
                try {
                    actualTypes[i] = tp;
                    returnTypeForOverloadedActuals += <key, idr, computeADTReturnType(current, adtName, scope, retType, formalTypes, actuals, actualTypes, kwFormals, keywordArguments, identicalFormals, dontCare, isExpression)>;
                    //println("succeeds: <ovl>");
                } catch checkFailed(set[Message] msgs): {
                    //println("fails: <ovl>");
                    ; // continue with next overload
                }
             }
             if(isEmpty(returnTypeForOverloadedActuals)) { reportError(current, "Constructor with arguments <fmt(actuals)> cannot be resolved");}
             else return overloadedAType(returnTypeForOverloadedActuals);
        }
    }
    
    return computeADTReturnType(current, adtName, scope, retType, formalTypes, actuals, actualTypes, kwFormals, keywordArguments, identicalFormals, dontCare, isExpression);
}

AType computeADTReturnType(Tree current, str adtName, Key scope, AType retType, list[AType] formalTypes, actuals, list[AType] actualTypes, list[Keyword] kwFormals, keywordArguments, list[bool] identicalFormals, list[bool] dontCare, bool isExpression){
    Bindings bindings = ();
    index_formals = index(formalTypes);
    for(int i <- index_formals, !dontCare[i]){
        try   bindings = matchRascalTypeParams(formalTypes[i], actualTypes[i], bindings, bindIdenticalVars=true);
        catch invalidMatch(str reason): 
              reportError(current, reason);   
    }
    iformals = formalTypes;
    if(!isEmpty(bindings)){
        try   iformals = [instantiateRascalTypeParams(formalTypes[i], bindings) | int i <- index_formals];
        catch invalidInstantiation(str msg):
              reportError(current, msg);
    }
    for(int i <- index_formals, !dontCare[i]){
        ai = actualTypes[i];
       //println("<i>: <ai>");
        if(!isFullyInstantiated(ai) && isExpression){
            if(identicalFormals[i]){
               unify(ai, iformals[i]) || reportError(current, "Cannot unify <fmt(ai)> with <fmt(iformals[i])>");
               ai = instantiate(ai);
               //println("instantiated <actuals[i]>: <ai>");
            } else
                continue;
        }
        //println("comparable?: <ai>, <iformals[i]>");
        //actuals_i = (list[Expression] actualsExp := actuals) ? actualsExp[i] : ((list[Pattern] actualsPat := actuals) ? actualsPat[i] : current);
        comparable(ai, iformals[i]) ||
            reportError(current, "Argument <i> should have type <fmt(formalTypes[i])>, found <fmt(ai)>");
    }
    adtType = expandUserTypes(getType(adtName, scope, {dataId(), nonterminalId()}), scope);
    checkKwArgs(kwFormals + getCommonKeywords(adtType, scope), keywordArguments, bindings, scope, isExpression=isExpression);
   
    if(!isEmpty(bindings)){
        try    return instantiateRascalTypeParams(expandUserTypes(getType(adtName, scope, {dataId(), nonterminalId()}), scope), bindings);
        catch invalidInstantiation(str msg):
               reportError(current, msg);
    }
   
    return adtType;
}

void checkKwArgs(list[Keyword] kwFormals, keywordArguments, Bindings bindings, Key scope, bool isExpression=true){
    switch(keywordArguments){
    case (KeywordArguments[Expression]) `<KeywordArguments[Expression] keywordArgumentsExp>`: {
        if(keywordArgumentsExp is none) return;
     
        next_arg:
        for(kwa <- keywordArgumentsExp.keywordArgumentList){ 
            kwName = "<kwa.name>";
            
            for(<fn, ft, de> <- kwFormals){
               if(kwName == fn){
                  ift = expandUserTypes(ft, scope);
                  if(!isEmpty(bindings)){
                      try   ift = instantiateRascalTypeParams(ft, bindings);
                      catch invalidInstantiation(str msg):
                            reportError(kwa, msg);
                  }
                  kwType = isExpression ? getType(kwa.expression) : getPatternType(kwa.expression, ift, scope);
                  comparable(kwType, ift) || reportError(kwa, "Keyword argument <fmt(kwName)> has type <fmt(kwType)>, expected <fmt(ift)>");
                  continue next_arg;
               } 
            }
            availableKws = intercalateOr(["`<prettyPrintAType(ft)> <fn>`" | <str fn, AType ft, Expression de> <- kwFormals]);
            switch(size(kwFormals)){
            case 0: availableKws ="; no other keyword parameters available";
            case 1: availableKws = "; available keyword parameter: <availableKws>";
            default:
                availableKws = "; available keyword parameters: <availableKws>";
            }
            
            reportError(kwa, "Undefined keyword argument <fmt(kwName)><availableKws>");
        }
        }
     case (KeywordArguments[Pattern]) `<KeywordArguments[Pattern] keywordArgumentsPat>`: {
        if(keywordArgumentsPat is none) return;
     
        next_arg:
        for(kwa <- keywordArgumentsPat.keywordArgumentList){ 
            kwName = "<kwa.name>";
            
            for(<fn, ft, de> <- kwFormals){
               if(kwName == fn){
                  ift = expandUserTypes(ft, scope);
                  if(!isEmpty(bindings)){
                      try   ift = instantiateRascalTypeParams(ft, bindings);
                      catch invalidInstantiation(str msg):
                            reportError(kwa, msg);
                  }
                  kwType = isExpression ? getType(kwa.expression) : getPatternType(kwa.expression, ift, scope);
                  comparable(kwType, ift) || reportError(kwa, "Keyword argument <fmt(kwName)> has type <fmt(kwType)>, expected <fmt(ift)>");
                  continue next_arg;
               } 
            }
            availableKws = intercalateOr(["`<prettyPrintAType(ft)> <fn>`" | <str fn, AType ft, Expression de> <- kwFormals]);
            switch(size(kwFormals)){
            case 0: availableKws ="; no other keyword parameters available";
            case 1: availableKws = "; available keyword parameter: <availableKws>";
            default:
                availableKws = "; available keyword parameters: <availableKws>";
            }
            
            reportError(kwa, "Undefined keyword argument <fmt(kwName)><availableKws>");
        }
     } 
    default: 
      throw "checkKwArgs: illegal keywordArguments";
    }
 } 
 
 AType computeNodeType(Tree current, Key scope, actuals, keywordArguments  , AType subjectType=avalue(), bool isExpression = true){                     
    nactuals = size(actuals);

    switch(actuals){
        case list[Expression] expList: {
                actualType = [ getType(expList[i]) | i <- index(expList) ];
                return anode(computeKwArgs(keywordArguments, scope, isExpression=isExpression));
            }
        case list[Pattern] patList: {
                dontCare = [ "<patList[i]>" == "_" | i <- index(patList) ];
                actualType = [ dontCare[i] ? avalue() : expandUserTypes(getPatternType(patList[i], avalue(), scope), scope) | i <- index(patList) ];
                
                if(adtType:aadt(adtName, list[AType] parameters,_) := subjectType){
                   declaredInfo = getDefinitions(adtName, scope, {dataId(), nonterminalId()});
                   declaredType = getType(adtName, scope, {dataId(), nonterminalId()});
                   checkKwArgs(getCommonKeywords(adtType, scope), keywordArguments, (), scope, isExpression=isExpression);
                   return subjectType;
                } else if(acons(adtType:aadt(adtName, list[AType] parameters, _), str consName, list[NamedField] fields, list[Keyword] kwFields) := subjectType){
                   kwFormals = kwFields;
                   checkKwArgs(kwFormals + getCommonKeywords(adtType, scope), keywordArguments, (), scope, isExpression=isExpression);
                   return anode([]);
                } else if(anode(list[NamedField] fields) := subjectType){
                    return computeNodeTypeWithKwArgs(current, keywordArguments, fields, scope);
                } else if(avalue() := subjectType){
                    return anode([]);
                }
                reportError(current, "Node pattern does not match <fmt(subjectType)>");
            }
        default:
            throw "Illegal argument `actuals`";
    }
}

AType computeNodeTypeWithKwArgs(Tree current, keywordArguments, list[NamedField] fields, Key scope){
    switch(keywordArguments){
        case (KeywordArguments[Expression]) `<KeywordArguments[Expression] keywordArgumentsExp>`: {
               if(keywordArgumentsExp is none) return anode([]);
                               
               nodeFieldTypes = [];
               nextKW:
                   for(<fn, ft> <- fields){
                       for(kwa <- keywordArgumentsExp.keywordArgumentList){ 
                           kwName = "<kwa.name>";
                           if(kwName == fn){
                              kwType = getPatternType(kwa.expression,ft, scope);
                              unify(ft, kwType) || reportError(current, "Cannot determine type of field <fmt(fn)>");
                              nodeFieldTypes += <fn, ft>;
                              continue nextKW;
                           }
                       }    
                   }
               return anode(nodeFieldTypes); 
        }
        case (KeywordArguments[Pattern]) `<KeywordArguments[Pattern] keywordArgumentsPat>`: {
               if(keywordArgumentsPat is none) return anode([]);
                               
               nodeFieldTypes = [];
               nextKW:
                   for(<fn, ft> <- fields){
                       for(kwa <- keywordArgumentsPat.keywordArgumentList){ 
                           kwName = "<kwa.name>";
                           if(kwName == fn){
                              kwType = getPatternType(kwa.expression,ft, scope);
                              unify(ft, kwType) || reportError(current, "Cannot determine type of field <fmt(fn)>");
                              nodeFieldTypes += <fn, ft>;
                              continue nextKW;
                           }
                       }    
                   }
               return anode(nodeFieldTypes); 
        }
        
        default:
             throw "computeNodeTypeWithKwArgs: illegal keywordArguments";
    }
}

list[NamedField] computeKwArgs(keywordArguments, Key scope, bool isExpression=true){
    switch(keywordArguments){
        case (KeywordArguments[Expression]) `<KeywordArguments[Expression] keywordArgumentsExp>`: {
            if(keywordArgumentsExp is none) return [];
 
            return for(kwa <- keywordArgumentsExp.keywordArgumentList){ 
                kwName = "<kwa.name>";
                kwType = isExpression ? getType(kwa.expression) : getPatternType(kwa.expression, avalue(), scope);
                append <kwName, kwType>;
            }
        }
        
        case (KeywordArguments[Pattern]) `<KeywordArguments[Pattern] keywordArgumentsPat>`: {
            if(keywordArgumentsPat is none) return [];
 
            return for(kwa <- keywordArgumentsPat.keywordArgumentList){ 
                kwName = "<kwa.name>";
                kwType = isExpression ? getType(kwa.expression) : getPatternType(kwa.expression, avalue(), scope);
                append <kwName, kwType>;
            }
        }
        
        default:
             throw "computeKwArgs: illegal keywordArguments";
    }
}
 
list[Keyword] getCommonKeywords(aadt(str adtName, list[AType] parameters, _), loc scope) = [ *d.defInfo.commonKeywordFields | d <- getDefinitions(adtName, scope, {dataId(), nonterminalId()}) ];
list[Keyword] getCommonKeywords(overloadedAType(rel[Key, IdRole, AType] overloads), loc scope) = [ *getCommonKeywords(adt, scope) | <def, idr, adt> <- overloads ];
default list[Keyword] getCommonKeywords(AType atype, loc scope) = [];

// ---- tuple

void collect(current: (Expression) `\< <{Expression ","}+ elements1> \>`, TBuilder tb){
    elms = [ e | Expression e <- elements1 ];
    tb.calculateEager("tuple expression", current, elms,
        AType() {
                return atuple(atypeList([ getType(elm) | elm <- elms ]));
        });
    collect(elements1, tb);
}

// ---- map

void collect(current: (Expression) `( <{Mapping[Expression] ","}* mappings>)`, TBuilder tb){
    froms = [ m.from | m <- mappings ];
    tos =  [ m.to | m <- mappings ];
    if(isEmpty(froms)){
        tb.fact(current, amap(avoid(), avoid()));
    } else {
        tb.calculateEager("map expression", current, froms + tos,
            AType() {
                return amap(lub([ getType(f) | f <- froms ]), lub([ getType(t) | t <- tos ]));
            });
        collect(mappings, tb);
    }
}

// ---- it

// ---- qualified name
 
void collect(current: (Expression) `<QualifiedName name>`, TBuilder tb){
    qname = convertName(name);
    if(isQualified(qname)){     
       tb.useQualified([qname.qualifier, qname.name], name, {variableId(), functionId(), constructorId()}, {dataId(), nonterminalId(), moduleId()} );
    } else {
       if(qname.name != "_"){
          tb.useLub(name, {variableId(), fieldId(), functionId(), constructorId()});
       } else {
          tb.fact(current, avalue());
       }
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
                  AType(){ return computeSubscriptionType(current, getType(expression), [getType(e) | e <- indexList], indexList);  });
    collect(expression, indices, tb);
}

AType computeSubscriptionType(Tree current, AType t1, list[AType] tl, list[Expression] indexList){
    if(!isFullyInstantiated(t1) || any(tp <- tl, !isFullyInstantiated(tp))){
        throw TypeUnavailable();
    }

    if(overloadedAType(rel[Key, IdRole, AType] overloads) := t1){
        //println("computeSubscriptionType: <current>, <t1>");
        subscript_overloads = {};
        for(<key, role, tp> <- overloads){
            try {
                subscript_overloads += <key, role, computeSubscriptionType(current, tp, tl, indexList)>;
            } catch checkFailed(set[Message] msgs): {
                ; // do nothing and try next overload
            } catch e: {
                ; // do nothing and try next overload
            }  
        }
        //println("computeSubscriptionType: <current>, <t1>, <tl> ==\> <overloadedAType(subscript_overloads)>");
        if(isEmpty(subscript_overloads)) reportError(current, "Expressions of type <fmt(t1)> cannot be subscripted");
        return overloadedAType(subscript_overloads);
    } else if (isListType(t1) && (!isListRelType(t1) || (isListRelType(t1) && size(tl) == 1 && isIntType(tl[0])))) {
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
            failures = { error("At subscript <idx+1>, subscript type <fmt(tl[idx])> must be comparable to relation field type <fmt(relFields[idx])>", getLoc(indexList[idx])) | idx <- index(tl), ! (comparable(tl[idx],relFields[idx]) || comparable(tl[idx],makeSetType(relFields[idx]))) };
            if (size(failures) > 0) {
                reportErrors(failures);
            } else if ((size(relFields) - size(tl)) == 1) {
                rftype = last(relFields);
                //if (alabel(_,rft) := rftype) rftype = rft; 
                return makeSetType(rftype);
            } else {
                return arel(atypeList(tail(relFields,size(relFields)-size(tl))));
            }
        }
    } else if (isListRelType(t1)) {
        if (size(tl) >= size(getListRelFields(t1)))
            reportError(current, "For a list relation with arity <size(getListRelFields(t1))> you can have at most <size(getListRelFields(t1))-1> subscripts");
        else {
            relFields = getListRelFields(t1);
            failures = { error("At subscript <idx+1>, subscript type <fmt(tl[idx])> must be comparable to relation field type <fmt(relFields[idx])>", getLoc(indexList[idx])) | idx <- index(tl), ! (comparable(tl[idx],relFields[idx]) || comparable(tl[idx],makeSetType(relFields[idx]))) };
            if (size(failures) > 0) {
                reportErrors(failures);
            } else if ((size(relFields) - size(tl)) == 1) {
                rftype = last(relFields);
                //if (alabel(_,rft) := rftype) rftype = rft; 
                return makeListType(rftype);
            } else {
                return alrel(atypeList(tail(relFields,size(relFields)-size(tl))));
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
        } else if ((Expression)`<DecimalIntegerLiteral dil>` := head(indexList)) {
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
    } else if (isNonTerminalType(t1)) {
        if (size(tl) != 1)
            reportError(current, "Expected only 1 subscript for a nonterminal subscript expression, not <size(tl)>");
        else if (!isIntType(tl[0]))
            reportError(current, "Expected subscript of type int, not <fmt(tl[0])>");
        else if (isNonTerminalIterType(t1))
            return getNonTerminalIterElement(t1);
        else
            return makeADTType("Tree");    
    } else {
        reportError(current, "Expressions of type <fmt(t1)> cannot be subscripted");
    }
}

// ---- slice

void collect(current: (Expression) `<Expression e> [ <OptionalExpression ofirst> .. <OptionalExpression olast> ]`, TBuilder tb){
    if(ofirst is noExpression) tb.fact(ofirst, aint());
    if(olast is noExpression) tb.fact(olast, aint());

    tb.calculate("slice", current, [e, ofirst, olast],
        AType(){ return computeSliceType(current, getType(e), getType(ofirst), aint(), getType(olast)); });
    collect(e, ofirst, olast, tb);
}

AType computeSliceType(Tree current, AType base, AType first, AType step, AType last){

    if(!isFullyInstantiated(base) || !isFullyInstantiated(first) || !isFullyInstantiated(step) || !isFullyInstantiated(last)) throw TypeUnavailable();
    
     if(overloadedAType(rel[Key, IdRole, AType] overloads) := base){
        slice_overloads = {};
        for(<key, role, tp> <- overloads){
            try {
                slice_overloads += <key, role, computeSliceType(current, tp, first, step, last)>;
            } catch checkFailed(set[Message] msgs): {
                ; // do nothing and try next overload
            } catch e:;  
        }
        if(isEmpty(slice_overloads)) reportError(current, "Slice cannot be computed for <fmt(base)>");
        return overloadedAType(slice_overloads);
    }
    
    failures = {};
    if(!isIntType(first)) failures += error("The first slice index must be of type `int`, found <fmt(first)>", getLoc(current));
    if(!isIntType(step)) failures  += error("The slice step must be of type `int`, found <fmt(step)>", getLoc(current));
    if(!isIntType(last)) failures  += error("The last slice index must be of type `int`, found <fmt(last)>", getLoc(current));
    
    if(!isEmpty(failures)) throw reportErrors(failures);
    
    if (isListType(base) || isStrType(base) || isNonTerminalIterType(base)) {
        return base;
    } else if (isNodeType(base)) {
        return makeListType(avalue());
    }
    
    reportErrors(failures + error("Slices can only be used on (concrete) lists, strings, and nodes, found <fmt(base)>", getLoc(current)));
}

// ---- sliceStep

void collect(current: (Expression) `<Expression e> [ <OptionalExpression ofirst>, <Expression second> .. <OptionalExpression olast> ]`, TBuilder tb){
    if(ofirst is noExpression) tb.fact(ofirst, aint());
    if(olast is noExpression) tb.fact(olast, aint());

    tb.calculate("slice step", current, [e, ofirst, second, olast],
        AType(){ return computeSliceType(current, getType(e), getType(ofirst), getType(second), getType(olast)); });
    collect(e, ofirst, second, olast, tb);
}

// ---- fieldAccess

void collect(current: (Expression) `<Expression expression> . <Name field>`, TBuilder tb){
    scope = tb.getScope();
    
    tb.calculate("field access", current, [expression],
        AType(){ 
            return computeFieldType(current, getType(expression), prettyPrintQName(convertName(field)), scope, tb); });
    collect(expression, tb);
}

@doc{Field names and types for built-ins}
public map[AType,map[str,AType]] fieldMap =
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
public AType computeFieldType(Tree current, AType t1, str fieldName, Key scope, TBuilder tb) {
   //println("computeFieldType: <current>, <t1>, <fieldName>");
    if(!isFullyInstantiated(t1)) throw TypeUnavailable();
    
    t1 = expandUserTypes(t1, scope);
    if(overloadedAType(rel[Key, IdRole, AType] overloads) := t1){
        field_overloads = {};
        for(<key, role, tp> <- overloads){
            try {
                field_overloads += <key, role, computeFieldType(current, tp, fieldName, scope, tb)>;
            } catch checkFailed(set[Message] msgs): {
                ; // do nothing and try next overload
            } catch e:;  
        }
        if(isEmpty(field_overloads))  reportError(current, "Cannot access fields on type <fmt(t1)>");
        return overloadedAType(field_overloads);
        
     } else if (isReifiedType(t1)) {
        if(tb.getConfig().classicReifier){
            if (fieldName == "symbol") {
                return getType("Symbol", scope, {dataId()});
            } else if (fieldName == "definitions") {
               getType("Symbol", scope, {dataId()});
                      
               getType("Production", scope, {dataId()});
               return makeMapType(makeADTType("Symbol"), makeADTType("Production"));
                    
            } else {
               reportError(current, "Field <fmt(fieldName)> does not exist on type `type` (classic reifier)  ");
            }
         } else {
            if (fieldName == "symbol") {
                return getType("AType", scope, {dataId()});
            } else if (fieldName == "definitions") {
               getType("AType", scope, {dataId()});
               getType("AProduction", scope, {dataId()});
               return makeMapType(makeADTType("AType"), makeADTType("AProduction"));
                    
            } else {
               reportError(current, "Field <fmt(fieldName)> does not exist on type `type`  (non-classic reifier) ");
            }
         }
    } else if (aadt(adtName, list[AType] actualTypeParams,_) := t1){
        try {
            if ((getADTName(t1) == "Tree" || isNonTerminalType(t1)) && fieldName == "top") {
                return t1;
            }
            //fieldType = expandUserTypes(getType(fieldName, scope, {formalId(), fieldId()}), scope);
            
            fieldType = getType(fieldName, scope, {fieldId()});
            
            try {
                fieldType = expandUserTypes(fieldType, scope);
            } catch TypeUnavailable():
                reportError(current, "Cannot expand type of field <fmt(fieldType)>, missing import of field type?");
           
            declaredInfo = getDefinitions(adtName, scope, {dataId(), nonterminalId()});
            declaredType = getType(adtName, scope, {dataId(), nonterminalId()});
      
            return computeADTFieldType(current, declaredType, fieldType, fieldName, actualTypeParams, declaredInfo, scope, tb);             
                
        } catch TypeUnavailable(): { // TODO Remove try
            throw TypeUnavailable(); //reportError(current, "Cannot compute type of field <fmt(fieldName)>, user type <fmt(t1)> has not been declared or is out of scope"); 
        }
     } else if(isStartNonTerminalType(t1)){
       return computeFieldType(current, getStartNonTerminalType(t1), fieldName, scope, tb);
    } else if (isNonTerminalType(t1)){
       return computeFieldType(current, aadt("Tree", [], contextFreeSyntax()), fieldName, scope, tb);
    } else if (isTupleType(t1)) {
        if(tupleHasFieldNames(t1)){
            idx = indexOf(getTupleFieldNames(t1), fieldName);
            if(idx >= 0)
                return getTupleFieldTypes(t1)[idx];
            else
                reportError(current, "Field <fmt(fieldName)> does not exist on type <fmt(t1)>");
        } else {
            reportError(current, "Field <fmt(fieldName)> does not exist on type <fmt(t1)>");
        }
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
    
    } else if (isNodeType(t1)) {
        return avalue();
    } 
    reportError(current, "Cannot access fields on type <fmt(t1)>");
}


AType computeADTFieldType(Tree current, AType declaredType, AType fieldType, str fieldName, list[AType] actualTypeParams, set[Define] declaredInfo, Key scope, TBuilder tb){
        //println("getADTTypeParameters: <declaredType>");
        declaredTypeParams = getADTTypeParameters(declaredType);
        //println("getADTTypeParameters: <declaredType> ==\> <declaredTypeParams>");
                
        if (size(declaredTypeParams) > 0) {
            if (size(declaredTypeParams) != size(actualTypeParams)) {
                reportError(current, "Invalid ADT type, the number of type parameters is inconsistent");
            } else {
                map[str, AType] bindings = ( getRascalTypeParamName(declaredTypeParams[idx]) : actualTypeParams[idx] | idx <- index(declaredTypeParams));
                if(!isEmpty(bindings)){
                    try {
                        fieldType = instantiateRascalTypeParams(fieldType, bindings);
                    } catch invalidInstantiation(str msg): {
                        reportError(current, "Failed to instantiate type parameters in field type <fmt(fieldType)>");
                    } 
                }                      
            }
        } 
        
        fieldType = filterFieldType(fieldName, fieldType, declaredInfo, scope);
        if(overloadedAType({}) !:= fieldType){
           return expandUserTypes(fieldType, scope);
        } else if(isConcreteSyntaxRole(declaredType.syntaxRole)) {
           return computeFieldType(current, aadt("Tree", [], contextFreeSyntax()), fieldName, scope, tb);
        }            
        
        reportError(current, "Field <fmt(fieldName)> does not exist on type <fmt(declaredType)>");
}

AType filterFieldType(str fieldName, AType fieldType, set[Define] declaredInfo, Key scope){
    if(overloadedAType(rel[Key, IdRole, AType] overloads) := fieldType){
       filteredOverloads = {};
       for(<Key key, fieldId(), AType tp> <- overloads){
           for(Define def <- declaredInfo){     
               filteredOverloads += { <key, fieldId(), unset(expandUserTypes(ft, scope), "label")> | <fieldName, ft> <- def.defInfo.constructorFields, comparable(fieldType, tp)};
               filteredOverloads += { <key, fieldId(), unset(expandUserTypes(ft, scope), "label")> | <fieldName, ft, de> <- def.defInfo.commonKeywordFields, comparable(fieldType, tp)};
           }
       }
       return ({<Key key, fieldId(), AType tp>} := filteredOverloads) ? tp : overloadedAType(filteredOverloads);
    } else {
        return fieldType;
    } 
}

// ---- fieldUpdate

void collect(current:(Expression) `<Expression expression> [ <Name field> = <Expression repl> ]`, TBuilder tb){
    scope = tb.getScope();
    //tb.use(field, {fieldId()});
    tb.calculate("field update", current, [expression, repl],
        AType(){ fieldType = computeFieldType(current, getType(expression), prettyPrintQName(convertName(field)), scope, tb);
                 replType = getType(repl);
                 subtype(replType, fieldType) || reportError(current, "Cannot assign type <fmt(replType)> into field of type <fmt(fieldType)>");
                 return getType(expression);
        });
    collect(expression, repl, tb);
}

// ---- fieldProjection

void collect(current:(Expression) `<Expression expression> \< <{Field ","}+ fields> \>`, TBuilder tb){

    flds = [f | f <- fields];
    tb.calculate("field projection", current, [expression],
        AType(){ return computeFieldProjectionType(current, getType(expression), flds); });
    collectParts(current, tb);
}

AType computeFieldProjectionType(Expression current, AType base, list[lang::rascal::\syntax::Rascal::Field] fields){

    if(!isFullyInstantiated(base)) throw TypeUnavailable();
    
    if(overloadedAType(rel[Key, IdRole, AType] overloads) := base){
        projection_overloads = {};
        for(<key, role, tp> <- overloads){
            try {
                projection_overloads += <key, role, computeFieldProjectionType(current, tp, fields)>;
            } catch checkFailed(set[Message] msgs): {
                ; // do nothing and try next overload
            } catch e:;
        }
        if(isEmpty(projection_overloads))  reportError(current, "Illegal projection <fmt(base)>");
        return overloadedAType(projection_overloads);
    }
    
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
            fnAsString = prettyPrintName(fn);
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
    
    reportError(current, "Illegal projection <fmt(base)>");  
}

// ---- setAnnotation

void collect(current:(Expression) `<Expression e> [ @ <Name n> = <Expression er> ]`, TBuilder tb) {
    tb.use(n, {annoId()});
    scope = tb.getScope();
    tb.calculate("set annotation", current, [e, n, er],
        AType(){ t1 = expandUserTypes(getType(e), scope); tn = expandUserTypes(getType(n), scope); t2 = expandUserTypes(getType(er), scope);
                 return computeSetAnnotationType(current, t1, tn, t2);
               });
    collect(e, er, tb);
}

AType computeSetAnnotationType(Tree current, AType t1, AType tn, AType t2)
    = ternaryOp("set annotation", _computeSetAnnotationType, current, t1, tn, t2);

private AType _computeSetAnnotationType(Tree current, AType t1, AType tn, AType t2){
    if (isNodeType(t1) || isADTType(t1) || isNonTerminalType(t1)) {
        if(aanno(_, onType, annoType) := tn){
           subtype(t2, annoType) || reportError(current, "Cannot assign value of type <fmt(t2)> to annotation of type <fmt(annoType)>");
           return t1;
        } else
            reportError(current, "Invalid annotation type: <fmt(tn)>");
    } else
        reportError(current, "Invalid type: expected node, ADT, or concrete syntax types, found <fmt(t1)>");
}

// ---- getAnnotation

void collect(current:(Expression) `<Expression e>@<Name n>`, TBuilder tb) {
    tb.use(n, {annoId()});
    scope = tb.getScope();
    tb.calculate("get annotation", current, [e, n],
        AType(){ 
                 t1 = expandUserTypes(getType(e), scope);
                 tn = getType(n);
                 return computeGetAnnotationType(current, t1, tn);
               });
    collect(e, tb);
}
AType computeGetAnnotationType(Tree current, AType t1, AType t2)
    = binaryOp("get annotation", _computeGetAnnotationType, current, t1, t2);
    
private AType _computeGetAnnotationType(Tree current, AType t1, AType tn){
    if (isNodeType(t1) || isADTType(t1) || isNonTerminalType(t1)) {
        if(aanno(_, onType, annoType) := tn){
           return annoType;
        } else
            reportError(current, "Invalid annotation type: <fmt(tn)>");
    } else
        reportError(current, "Invalid type: expected node, ADT, or concrete syntax types, found <fmt(t1)>");
}
