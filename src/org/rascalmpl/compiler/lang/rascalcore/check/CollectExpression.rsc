@bootstrapParser
module lang::rascalcore::check::CollectExpression

/*
    Check all expressions
*/
 
extend lang::rascalcore::check::CheckerCommon;
extend lang::rascalcore::check::PathAnalysis;
extend lang::rascalcore::check::CollectLiteral;

import lang::rascalcore::check::CollectOperators;
import lang::rascalcore::check::CollectStatement;

import lang::rascal::\syntax::Rascal;

import Map;
import Node;
import Set;
import String;
import util::Math;
import IO;

// ---- Rascal literals, also see CollectLiteral

void collect(current: (StringLiteral) `<PreStringChars pre><StringTemplate template><StringTail tail>`, Collector c){
    c.fact(current, astr());
    collect(template, tail, c);
} 

void collect(current: (StringLiteral) `<PreStringChars pre><Expression expression><StringTail tail>`, Collector c){
    c.fact(current, astr());
    collect(expression, tail, c);
}

void collect(current: (StringConstant) `"<StringCharacter* chars>"`, Collector c){
    c.fact(current, astr());
}

void collect(current: (StringMiddle) `<MidStringChars mid><StringTemplate template><StringMiddle tail>`, Collector c){
    collect(template, tail, c);
} 

void collect(current: (StringMiddle) `<MidStringChars mi><Expression expression><StringMiddle tail>`, Collector c){
    collect(expression, tail, c);
}

void collect(current: (MidStringChars) `\><StringCharacter* chars>\<`, Collector c){

}
void collect(current: (StringTail) `<MidStringChars mid> <Expression expression> <StringTail tail>`, Collector c){
    collect(expression, tail, c);
}

void collect(current: (StringTail) `<MidStringChars mid> <StringTemplate template> <StringTail tail>`, Collector c){
    collect(template, tail, c);
}

void collect(current: (StringTemplate) `if(<{Expression ","}+ conditions>){ <Statement* preStats> <StringMiddle body> <Statement* postStats> }`, Collector c){
    c.enterScope(conditions);  // thenPart may refer to variables defined in conditions
        condList = [cond | Expression cond <- conditions];
        c.fact(current, avalue());
        c.require("if then template", current, condList, void (Solver s){ checkConditions(condList, s); });
        beginPatternScope("conditions", c);
        collect(conditions, c);
        endPatternScope(c);
        collect(preStats, body, postStats, c);
    c.leaveScope(conditions);
}

void collect(current: (StringTemplate) `if( <{Expression ","}+ conditions> ){ <Statement* preStatsThen> <StringMiddle thenString> <Statement* postStatsThen> } else { <Statement* preStatsElse> <StringMiddle elseString> <Statement* postStatsElse> }`, Collector c){
    compScope = [conditions] 
                + (size([s | s <- preStatsThen]) > 0 ? [preStatsThen] : [])
                + thenString
                + (size([s | s <- postStatsThen]) > 0 ? [postStatsThen] : []);
    c.enterCompositeScope(compScope);   // thenPart may refer to variables defined in conditions; elsePart may not
    
        condList = [cond | Expression cond <- conditions];
        
        c.calculate("if then else template", current, condList/* + [postStatsThen + postStatsElse]*/,
            AType (Solver s){ checkConditions(condList, s); 
                      return avalue();
            });
        beginPatternScope("conditions", c);
        collect(condList, c);
        endPatternScope(c);
        collect(preStatsThen, thenString, postStatsThen, c);
    c.leaveCompositeScope(compScope);
    collect(preStatsElse, elseString, postStatsElse, c);    
} 

void collect(current: (StringTemplate) `for( <{Expression ","}+ generators> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`, Collector c){
    c.enterScope(generators);   // body may refer to variables defined in conditions
        loopName = "";
        c.setScopeInfo(c.getScope(), loopScope(), loopInfo(loopName, [])); // appends in body
        c.fact(current, alist(avoid()));
        
        condList = [cond | Expression cond <- generators];
        c.require("for statement  template", current, condList, void (Solver s){ checkConditions(condList, s); });
        
        beginPatternScope("conditions", c);
            collect(condList, c);
        endPatternScope(c);
        
        collect(preStats, body, postStats, c);
    c.leaveScope(generators);
}

void collect(current: (StringTemplate) `do { <Statement* preStats> <StringMiddle body> <Statement* postStats> } while( <Expression condition> )`, Collector c){
    c.enterScope(current);   // condition may refer to variables defined in body
        loopName = "";
        c.setScopeInfo(c.getScope(), loopScope(), loopInfo(loopName, [])); // appends in body
        c.fact(current, alist(avoid()));
        
        condList = [condition];
        c.require("do statement template", current, condList, void (Solver s){ checkConditions(condList, s); });
       
        beginPatternScope("conditions", c);
            collect(condition, c);
        endPatternScope(c);
        
        collect(preStats, body, postStats, c);
    c.leaveScope(current); 
}

void collect(current: (StringTemplate) `while( <Expression condition> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`, Collector c){
    c.enterScope(condition);   // body may refer to variables defined in conditions
        loopName = "";
        c.setScopeInfo(c.getScope(), loopScope(), loopInfo(loopName, [])); // appends in body
        c.fact(current, alist(avoid()));
        
        condList = [condition];
        c.require("while statement  template", current, condList, void (Solver s){ checkConditions(condList, s); });
        
        beginPatternScope("conditions", c);
            collect(condList, c);
        endPatternScope(c);
        
        collect(preStats, body, postStats, c);
    c.leaveScope(condition);
} 

void collect(Literal l:(Literal)`<LocationLiteral ll>`, Collector c){
    c.fact(l, aloc());
    collect(ll.protocolPart, ll.pathPart, c);
}

void collect(current: (ProtocolPart) `<ProtocolChars protocolChars>`, Collector c){

}

void collect(current: (ProtocolPart) `<PreProtocolChars pre> <Expression expression> <ProtocolTail tail>`, Collector c){
    collect(expression, c);
}

void collect(current: (PathPart) `<PathChars pathChars >`, Collector c){

}

void collect(current: (PathPart) `<PrePathChars pre> <Expression expression> <PathTail tail>`, Collector c){
    collect(expression, tail, c);
}

void collect(current: (PathTail) `<MidPathChars mid> <Expression expression> <PathTail tail>`, Collector c){
    collect(expression, tail, c);
}

 void collect(current: (PathTail) `<PostPathChars post>`, Collector c){
 }

// Rascal expressions

// ---- non-empty block

void collect(current: (Expression) `{ <Statement+ statements> }`, Collector c){
    stats = [ stat | Statement stat <- statements ];
    c.calculate("non-empty block expression", current, [stats[-1]],  AType(Solver s) { return s.getType(stats[-1]); } );
    collect(statements, c);
}

// ---- brackets

void collect(current: (Expression) `( <Expression expression> )`, Collector c){
   //c.calculate("brackets", current, [expression],  AType(Solver s) { return s.getType(expression); } );
    c.fact(current, expression);
    collect(expression, c);
    c.require("non-void expression", current, [expression], void(Solver s){ checkNonVoid(expression, s, "Parenthesized expression"); });
}

// ---- closure

str closureName(Expression closure){
    l = getLoc(closure);
    return "$CLOSURE_<l.begin.line>A<l.offset>";
}

void collect(current: (Expression) `<Type returnType> <Parameters parameters> { <Statement+ statements> }`, Collector c){
    // TODO: experimental check
    //if(!isEmpty(c.getScopeInfo(loopScope())) || inPatternScope(c)){
    //    c.report(warning(current, "Function closure inside loop or backtracking scope, be aware of interactions with current function context"));
    //}
    collectClosure(current, returnType, parameters, [stat | stat <- statements], c);
}

void collectClosure(Expression current, Type returnType, Parameters parameters, list[Statement] stats, Collector c){
    parentScope = c.getScope();
    c.enterLubScope(current);
        scope = c.getScope();
        c.setScopeInfo(scope, functionScope(), returnInfo(returnType));
        collect([returnType, parameters] + stats, c);
        
        clos_name = closureName(current);
        bool returnsViaAll = returnsViaAllPath(stats, clos_name, c);
        formals = getFormals(parameters);
        kwFormals = getKwFormals(parameters);
                
        dt = defType(returnType + formals + kwFormals, AType(Solver s){
                res = afunc(s.getType(returnType), [s.getType(f) | f <- formals], computeKwFormals(kwFormals, s), returnsViaAllPath=returnsViaAll)[alabel=clos_name]; 
                return res;
             });
        
        alwaysSucceeds = all(pat <- formals, pat is typedVariable && /(Statement) `fail <Target _>;` := stats);
        if(!alwaysSucceeds) dt.canFail = true;
        
        c.defineInScope(parentScope, clos_name, functionId(), current, dt); 
        
        if(!returnsViaAll && "<returnType>" != "void"){
                c.report(error(current, "Missing return statement"));
        }
    c.leaveScope(current);
}

// ---- void closure

private Type voidReturnType = (Type) `void`;

void collect(current: (Expression) `<Parameters parameters> { <Statement* statements0> }`, Collector c){
    //if(!isEmpty(c.getScopeInfo(loopScope())) || inPatternScope(c)){
    //    c.report(warning(current, "Function closure inside loop or backtracking scope, be aware of interactions with current function context"));
    //}
    c.fact(voidReturnType, avoid());
    
    collectClosure(current, voidReturnType, parameters, [stat | stat <- statements0], c);
}

// ---- step range

void collect(current: (Expression) `[ <Expression first> , <Expression second> .. <Expression last> ]`, Collector c){
    c.calculate("step range", current, [first, second, last],
        AType(Solver s){ 
                 t1 = s.getType(first);
                 checkNonVoid(first, t1, s, "First in range");
                 s.requireSubType(t1,anum(), error(first, "First in range: expected numeric type, found %t", t1));
                 
                 t2 = s.getType(second); 
                 checkNonVoid(second, t2, s, "Second in range");
                 s.requireSubType(t2,anum(), error(second, "Second in range: expected numeric type, found %t", t2));
                 
                 t3 = s.getType(last);
                 checkNonVoid(last, t3, s, "Last in range");
                 s.requireSubType(t3,anum(), error(last, "Last in range: expected numeric type, found %t", t3));
                 return alist(s.lubList([t1, t2, t3]));
        
        });
    collect(first, second, last, c);    
}

// ---- range

void collect(current: (Expression) `[ <Expression first> .. <Expression last> ]`, Collector c){
    c.calculate("step range", current, [first, last],
        AType(Solver s){ 
                 t1 = s.getType(first); 
                 checkNonVoid(first, t1, s, "First in range");
                 s.requireSubType(t1,anum(), error(first, "First in range: expected numeric type, found %t", t1));
                 
                 t2 = s.getType(last);
                 checkNonVoid(last, t2, s, "Last in range");
                 s.requireSubType(t2,anum(), error(last, "Last in range: expected numeric type, found %t", t2));
                 return alist(s.lub(t1, t2));
        });
    collect(first, last, c);    
}

// ---- visit -- handled in CollectStatement


// ---- reifyType - type literals

void collect(current: (Expression) `# <Type tp>`, Collector c) {
    // A type literal expression like `#int` guarantees that the dynamic type of the value it produces
    // is equal to its static type (ignoring the effect to type parameter instantiation).
    // so, `#int` of static type `type[int]` will produce a value `type(\int(),())` also of 
    // type `type[int]`.
    // 
    // this simple property is the core assumption under the type-safety of generic functions that
    // take a reified type value as parameter, like `&T cast(type[&T] _, value x)`, and the pattern
    // matches in their bodies: `&T _ := x` also float on that property.
    //
    // Compare this to Java's class literals, `Object.class`, `Integer.class` which also produce
    // `Class<Object>` and `Class<Integer>`; except that Rascal does not have type erasure and
    // it does have co-variance, also for type literals. 

    // All of this is implied by the following inocuous type calculation:
    c.calculate("reified type", current, [tp], AType(Solver s) { 
        // notice how the type _literal_ here is lifted to a type _instance_
        return areified(s.getType(tp)); 
    });

    collect(tp, c);
}

// ---- reifiedType - type values

void collect(current: (Expression) `type ( <Expression es> , <Expression ed> )`, Collector c) {
    // This is where the dynamic type system and the static type system touch, but
    // they can not have the _same_ type. For the other reified type expression, `#Type`,
    // the story is different. There the static type coincides with the static type, by design.
    
    // Here, the type() expression has much more dynamic behavior, also by design. The first 
    // parameter is a computed value, and that value will
    // be "unreified" to a specific type at run-time. At compile-time we do not know yet
    // what that type will be, so we must assume it will be `value`.

    // A type value is never statically more precise than value.
    // with constant propagation of the symbol parameter, we could compute
    // more precise types, but `type[value]` is always a proper type for all
    // possible instances.
   
    c.fact(current, areified(\avalue()));

    c.require("reified type", current, [es, ed],
        void (Solver s) {
            checkNonVoid(es, s, "First element of reified type");
            s.requireSubType(es, aadt("Symbol", [], dataSyntax()), error(es, "Expected a Symbol, instead found %t", es));
            
            checkNonVoid(ed, s, "Second element of reified type");
            s.requireSubType(ed, amap(aadt("Symbol", [], dataSyntax()), aadt("Production", [], dataSyntax())), 
                error(ed, "Expected a map[Symbol, Production], instead found %t", ed));
        }
    );
    
    collect(es, ed, c);
}

// ---- any

void collect(current: (Expression)`any ( <{Expression ","}+ generators> )`, Collector c){
    gens = [gen | gen <- generators];
    c.fact(current, abool());
    beginPatternScope("any", c);
        c.require("any", current, gens,
            void (Solver s) { for(gen <- gens) if(!isBoolAType(s.getType(gen))) s.report(error(gen, "Type of generator should be `bool`, found %t", gen));
            });
        collectGenerators([], gens, c);
    endPatternScope(c);
}

// ---- all

void collect(current: (Expression)`all ( <{Expression ","}+ generators> )`, Collector c){
    gens = [gen | gen <- generators];
    c.fact(current, abool());
    beginPatternScope("all", c);
        c.require("all", current, gens,
            void (Solver s) { for(gen <- gens) if(!isBoolAType(s.getType(gen))) s.report(error(gen, "Type of generator should be `bool`, found %t", gen));
            });
        collectGenerators([], gens, c);
    endPatternScope(c);
}

// ---- comprehensions and reducer

void collectGenerators(list[Tree] results, list[Expression] generators, Collector c){
    n = size(generators);
    assert n > 0;
    c.enterScope(generators[0]);
        for(res <- results){
            storeAllowUseBeforeDef(generators[0], res, c); // variable occurrences in results may refer to variables defined in generators
        }
        collect(generators[0], c);
        if(n > 1){
            collectGenerators(results, generators[1..], c);
        } else {
            collect(results, c);
        }
    c.leaveScope(generators[0]);
}

// set comprehension

void collect(current: (Comprehension)`{ <{Expression ","}+ results> | <{Expression ","}+ generators> }`, Collector c){
    gens = [gen | gen <- generators];
    res  = [r | r <- results];
    c.enterLubScope(current);
    beginPatternScope("set-comprehension", c);
        c.require("set comprehension", current, gens,
            void (Solver s) { 
                for(g <- gens) if(!isBoolAType(s.getType(g))) s.report(error(g, "Type of generator should be `bool`, found %t", g));
                for(r <- results) checkNonVoidOrSplice(r, s, "Contribution to set comprehension");
            });
        c.calculate("set comprehension results", current, res,
            AType(Solver s){
                return makeSetType(lubList([ s.getType(r) | r <- res]));
            });
         
        collectGenerators(res, gens, c);
    endPatternScope(c);
    c.leaveScope(current);
}

// list comprehension

void collect(current: (Comprehension) `[ <{Expression ","}+ results> | <{Expression ","}+ generators> ]`, Collector c){
    gens = [gen | gen <- generators];
    res  = [r | r <- results];
    
    c.enterLubScope(current);
    beginPatternScope("list-comprehension", c);
        collectGenerators(res, gens, c);
        try {
            c.fact(current, makeListType(lubList([ c.getType(r) | r <- res])));
        } catch TypeUnavailable(): {
            c.calculate("list comprehension results", current, res,
                AType(Solver s){
                    return makeListType(lubList([ s.getType(r) | r <- res]));
                });
          }
        c.require("list comprehension", current, gens,
            void (Solver s) { 
                for(g <- gens) if(!isBoolAType(s.getType(g))) s.report(error(g, "Type of generator should be `bool`, found %t", g));
                for(r <- results) checkNonVoidOrSplice(r, s, "Contribution to list comprehension");
            });
        
        
    endPatternScope(c);
    c.leaveScope(current);
}

// map comprehension

void collect(current: (Comprehension) `(<Expression from> : <Expression to> | <{Expression ","}+ generators> )`, Collector c){
    gens = [gen | gen <- generators];
    res = [from, to];
  
    c.enterLubScope(current);
    beginPatternScope("map-comprehension", c);
        c.require("map comprehension", current, gens,
            void (Solver s) { for(gen <- gens) if(!isBoolAType(s.getType(gen))) s.report(error(gen, "Type of generator should be `bool`, found %t", gen));
            });
        c.calculate("list comprehension results", current, [from, to],
            AType(Solver s){
                checkNonVoid(from, s, "Key of map comprehension");
                checkNonVoid(to, s, "Value of map comprehension");
                return makeMapType(unset(s.getType(from), "alabel"), unset(s.getType(to), "alabel"));
            });
         
        collectGenerators(res, gens, c);
    endPatternScope(c);
    c.leaveScope(current);
}

// ---- reducer

void collect(current: (Expression) `( <Expression init> | <Expression result> | <{Expression ","}+ generators> )`, Collector c){
    gens = [gen | gen <- generators];
    res = [result];
    c.enterLubScope(current);
    beginPatternScope("reducer", c);
        c.define("it", variableId(), init, defType(current));
        c.require("reducer", current, gens,
            void (Solver s) { 
                checkNonVoid(init, s, "Initialization expression of reducer");
                checkNonVoid(result, s, "Result expession of reducer");
                for(gen <- gens) if(!isBoolAType(s.getType(gen))) s.report(error(gen, "Type of generator should be `bool`, found %t", gen));
            });
        
        c.fact(current, result);
         
        collect(init, c);
        collectGenerators(res, gens, c);
    endPatternScope(c);
    c.leaveScope(current);
}

void collect(current: (Expression) `it`, Collector c){
    c.useLub(current, {variableId()});
}

// ---- set

void checkNonVoidOrSplice(Expression e, Solver s, str msg){
    if(isVoidAType(s.getType(e)) && !e is splice){
        s.report(error(e, msg + " should not have type `void`"));
    }
}

void collect(current: (Expression) `{ <{Expression ","}* elements0> }`, Collector c){
    elms = [ e | Expression e <- elements0 ];
    if(isEmpty(elms)){
        c.fact(current, aset(avoid()));
    } else {
        c.calculate("set expression", current, elms, AType(Solver s) { 
            for(elm <- elms) checkNonVoidOrSplice(elm, s, "Element of set");
            return aset(s.lubList([s.getType(elm) | elm <- elms]));
        });
        collect(elms, c);
    }
}

// ---- list

void collect(current: (Expression) `[ <{Expression ","}* elements0> ]`, Collector c){
    elms = [ e | Expression e <- elements0 ];
    if(isEmpty(elms)){
        c.fact(current, alist(avoid()));
    } else {
        c.calculate("list expression", current, elms, AType(Solver s) { 
            for(elm <- elms) checkNonVoidOrSplice(elm, s, "Element of list");
            return alist(s.lubList([s.getType(elm) | elm <- elms])); 
        });
        collect(elms, c);
    }
}

// ---- call or tree
           
void collect(current: (Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments[Expression] keywordArguments>)`, Collector c){
//println("<current>, <getLoc(current)>");
    list[Expression] actuals = [a | Expression a <- arguments];  
    kwactuals = keywordArguments is \default ? [ kwa.expression | kwa <- keywordArguments.keywordArgumentList] : [];
  
    scope = c.getScope();
    
    c.calculate("call of function/constructor `<expression>`", current, expression + actuals + kwactuals,
        AType(Solver s){
            for(x <- expression + actuals + kwactuals){
                 tp = s.getType(x);
                 if(!s.isFullyInstantiated(tp)) throw TypeUnavailable();
                 checkNonVoid(x, s, "Argument");
            }
            
            texp = s.getType(expression);
            if(isStrAType(texp)){
                return computeExpressionNodeType(scope, actuals, keywordArguments, s);
            } 
            if(isLocAType(texp)){
                nactuals = size(actuals);
                if(!(nactuals == 2 || nactuals == 4)) s.report(error(current, "Source locations requires 2 or 4 arguments, found %v", nactuals));
                s.requireEqual(actuals[0], aint(), error(actuals[0], "Offset should be of type `int`, found %t", actuals[0]));
                s.requireEqual(actuals[1], aint(), error(actuals[1], "Length should be of type `int`, found %t", actuals[1]));

                if(nactuals == 4){
                    s.requireEqual(actuals[2], atuple(atypeList([aint(),aint()])), error(actuals[2], "Begin should be of type `tuple[int,int]`, found %t", actuals[2]));
                    s.requireEqual(actuals[3], atuple(atypeList([aint(),aint()])), error(actuals[3], "End should be of type `tuple[int,int]`, found %t", actuals[3])); 
                }
                return aloc();
            }
            if(isConstructorAType(texp) && getConstructorResultType(texp).adtName == "Tree" && "<expression>" == "char"){
                nactuals = size(actuals);
                if(nactuals != 1){
                    s.report(error(current, "`char` requires 1 argument, found %v", nactuals));
                }
                s.requireEqual(actuals[0], aint(), error(actuals[0], "Argument should be of type `int`, found %t", actuals[0]));
                if(actuals[0] is literal){
                    chr = toInt("<actuals[0]>");
                    return \achar-class([arange(chr, chr)]);
                } else
                    return anyCharType;
            }
             
            if(overloadedAType(rel[loc, IdRole, AType] overloads) := texp){
              <filteredOverloads, identicalFormals> = filterOverloads(overloads, size(actuals));
              if({<_, _, tp>} := filteredOverloads){
                texp = tp;
                s.specializedFact(expression, tp);
              } else {
                overloads = filteredOverloads;
                validReturnTypeOverloads = {};
                validOverloads = {};
                next_fun:
                for(ovl: <key, idRole, tp> <- overloads){                       
                    if(ft:afunc(AType ret, list[AType] formals, list[Keyword] kwFormals) := tp){
                       try {
                            // TODO: turn this on after review of all @deprecated uses in the Rascal library library
                            if(ft.deprecationMessage? && c.getConfig().warnDeprecated){
                                s.report(warning(expression, "Deprecated function%v", isEmpty(ft.deprecationMessage) ? "" : ": " + ft.deprecationMessage));
                            }
                           
                            validReturnTypeOverloads += <key, idRole, checkArgsAndComputeReturnType(expression, scope, ret, formals, kwFormals, ft.varArgs ? false, actuals, keywordArguments, identicalFormals, s)>;
                            validOverloads += ovl;
                       } catch checkFailed(list[FailMessage] _):
                            continue next_fun;
                         catch NoBinding():
                            continue next_fun;
                    }
                 }
                 reportMissingNonTerminalCases(current, overloads, validOverloads, actuals, s);
                 next_cons:
                 for(ovl: <key,idRole, tp> <- overloads){
                    if(acons(ret:aadt(adtName, list[AType] _, _),  list[AType] fields, list[Keyword] kwFields) := tp){
                       try {
                            validReturnTypeOverloads += <key, idRole, computeADTType(expression, adtName, scope, ret, fields, kwFields, actuals, keywordArguments, identicalFormals, s)>;
                            validOverloads += ovl;
                       } catch checkFailed(list[FailMessage] _):
                             continue next_cons;
                         catch NoBinding():
                             continue next_cons;
                    }
                 }
                 if({<_, _, tp>} := validOverloads){
                    texp = tp;  
                    s.specializedFact(expression, tp);
                    // TODO check identicalFields to see whether this can make sense
                    // unique overload, fall through to non-overloaded case to potentially bind more type variables
                 } else if(isEmpty(validReturnTypeOverloads)) {
                        reportCallError(current, expression, actuals, keywordArguments, s);
                        return avalue();
                 } else {
                    checkOverloadedConstructors(expression, validOverloads, s);
                    stexp = overloadedAType(validOverloads);
                    if(texp != stexp) s.specializedFact(expression, stexp);
                    //s.report(error(current, "Unresolved call to overloaded function defined as %t",  expression));
                    return overloadedAType(validReturnTypeOverloads);
                 }
               }
            }
            
            if(ft:afunc(AType ret, list[AType] formals, list[Keyword] kwFormals) := texp){
               // TODO; texp can get type value and then texp.deprecationMessage does not exist
               //if(texp.deprecationMessage? && c.getConfig().warnDeprecated){
               //     s.report(warning(expression, "Deprecated function%v", isEmpty(texp.deprecationMessage) ? "": ": " + texp.deprecationMessage));
               //}
                return checkArgsAndComputeReturnType(expression, scope, ret, formals, kwFormals, ft.varArgs, actuals, keywordArguments, [true | int _ <- index(formals)], s);
            }
            if(acons(ret:aadt(adtName, list[AType] _,_), list[AType] fields, list[Keyword] kwFields) := texp){
               res =  computeADTType(expression, adtName, scope, ret, fields, kwFields, actuals, keywordArguments, [true | int _ <- index(fields)], s);
               return res;
            }
            reportCallError(current, expression, actuals, keywordArguments, s);
            return avalue();
        });
      collect(expression, arguments, keywordArguments, c);
}

void reportCallError(Expression current, Expression callee, list[Expression] actuals, (KeywordArguments[Expression]) `<KeywordArguments[Expression] keywordArguments>`, Solver s){
    kwactuals = keywordArguments is \default ? [ kwa.expression | kwa <- keywordArguments.keywordArgumentList] : [];
    
    arguments = size(actuals) > 1 ? "arguments of types" : "argument of type";
    kwarguments = size(kwactuals) > 1 ? "keyword arguments" : "keyword argument";
    if(isEmpty(kwactuals)){
        s.report(error(current, "%q is defined as %t and cannot be applied to %v %v",  "<callee>", callee, arguments, actuals));
    } else {
        kwargs = keywordArguments is \default ? [ kwa | kwa <- keywordArguments.keywordArgumentList] : [];
        kws = [ "`<kwa.name>` of type `<prettyAType(s.getType(kwa.expression))>`" | kwa <- kwargs ];
        s.report(error(current, "%q is defined as %t and cannot be applied to %v `%v` and %v %v", 
                                "<callee>", callee, arguments, actuals, kwarguments, 
                                kws));
    }
}

void reportMissingNonTerminalCases(Expression current, rel[loc def, IdRole idRole, AType atype] overloads, rel[loc def, IdRole idRole, AType atype] validOverloads, list[Expression] actuals, Solver s){
    for(int i <- index(actuals)){
        actual_type = s.getType(actuals[i]);
        if(isNonTerminalAType(actual_type)){
            if(!isEmpty(validOverloads) && all(ovl <- validOverloads, 
                                               arg_types := getFunctionOrConstructorArgumentTypes(ovl.atype),       
                                               size(arg_types) == size(actuals), isADTAType(arg_types[i]), getADTName(arg_types[i]) == "Tree")){                
                nont = [ovl.atype | ovl <- overloads, arg_types := getFunctionOrConstructorArgumentTypes(ovl.atype), size(arg_types) == size(actuals), isNonTerminalAType(arg_types[i]) ];
                if(!isEmpty(nont)){
                    s.report(info(current, "Actual #%v has nonterminal type %t, but only comparable overloads with `Tree` argument exist, maybe missing import/extend?", i, actual_type));
                }
            }       
        }
    }
}

private void checkOverloadedConstructors(Expression current, rel[loc defined, IdRole role, AType atype] overloads, Solver s){  
    coverloads = [  ovl  | ovl <- overloads, isConstructorAType(ovl.atype) ];
    if(size(coverloads) > 1){
        ovl1 = coverloads[0];
        adtNames = { adtName | <key, idRole, tp>  <- overloads, acons(ret:aadt(adtName, list[AType] _, _),  list[AType] fields, list[Keyword] kwFields) := tp };
        qualifyHint = size(adtNames) > 1 ? "you may use <intercalateOr(sort(adtNames))> as qualifier" : "";
        argHint = "<isEmpty(qualifyHint) ? "" : " or ">make argument type(s) more precise";
        s.report(error(current, "Constructor %q is overloaded, to resolve it %v%v", 
                             ovl1.atype.alabel, 
                             qualifyHint,
                             argHint));
                             }
}

private tuple[rel[loc, IdRole, AType], list[bool]] filterOverloads(rel[loc, IdRole, AType] overloads, int arity){
    rel[loc, IdRole, AType] filteredOverloads = {};
    prevFormals = [];
    list[bool] identicalFormals = [true | int _ <- [0 .. arity]];
    
    for(ovl:<_, _, tp> <- overloads){                       
        if(ft:afunc(AType _, list[AType] formals, list[Keyword] _) := tp){
           if(ft.varArgs ? (arity >= size(formals) - 1) : (arity == size(formals))) {
              filteredOverloads += ovl;
              if(isEmpty(prevFormals)){
                 prevFormals = formals;
              } else {
                 nRelevantFormals = size(formals) - (ft.varArgs ? 1 : 0);
                 nPrevFormals =  size(prevFormals);
                 for(int i <- [0 .. min(nRelevantFormals, nPrevFormals)]) {
                   identicalFormals[i] = identicalFormals[i] && (comparable(prevFormals[i], formals[i]));
                 }
              }
           }
        } 
        else if(acons(aadt(_, list[AType] _,_), list[AType] fields, list[Keyword] _) := tp){
           if(size(fields) == arity){
              filteredOverloads += ovl;
              if(isEmpty(prevFormals)){
                 prevFormals = fields; //<1>;
              } else {
                 for(int i <- index(fields)) {
                   identicalFormals[i] = identicalFormals[i] && (comparable(prevFormals[i], fields[i]/*.fieldType*/));
                 }
              }
            }
        }
    }
    return <filteredOverloads, identicalFormals>;
}



// TODO: in order to reuse the function below `keywordArguments` is passed as where `keywordArguments[&T] keywordArguments` would make more sense.
// The interpreter does not handle this well, so revisit this later

// TODO: maybe check that all upperbounds of type parameters are identical?

private AType checkArgsAndComputeReturnType(Expression current, loc scope, AType retType, list[AType] formals, list[Keyword] kwFormals, bool isVarArgs, list[Expression] actuals, keywordArguments, list[bool] identicalFormals, Solver s){
    nactuals = size(actuals); nformals = size(formals);
   
    list[AType] actualTypes = [];
   
    if(isVarArgs){
       if(nactuals < nformals - 1) s.report(error(current, "Expected at least %v argument(s) found %v", nformals-1, nactuals));
       varArgsType = (avoid() | s.lub(it, s.getType(actuals[i])) | int i <- [nformals-1 .. nactuals]);
       actualTypes = [s.getType(actuals[i]) | int i <- [0 .. nformals-1]] + (isListAType(varArgsType) ? varArgsType : alist(varArgsType));
    } else {
        if(nactuals != nformals) s.report(error(current, "Expected %v argument(s), found %v", nformals, nactuals));
        actualTypes = [s.getType(a) | a <- actuals];
    }
    
    index_formals = index(formals);
    
    list[AType] formalTypes =  formals;
    
    int noverloaded = 0;
    for(int i <- index_formals){
        if(overloadedAType(rel[loc, IdRole, AType] overloads) := actualTypes[i]){   // TODO only handles a single overloaded actual
            noverloaded += 1;
            if(noverloaded > 1){
                s.report(error(current, "Cannot yet handle calls with multiple overloaded arguments"));
            }
            //println("checkArgsAndComputeReturnType: <current>");
            //iprintln(overloads);
            returnTypeForOverloadedActuals = {};
            for(<key, idr, tp> <- overloads){   
                try {
                    actualTypes[i] = tp;
                    returnTypeForOverloadedActuals += <key, idr, computeReturnType(current, scope, retType, formalTypes, actuals, actualTypes, kwFormals, keywordArguments, identicalFormals, s)>;
                    //println("succeeds: <ovl>");
                } catch checkFailed(list[FailMessage] _): /* continue with next overload */;
                  catch NoBinding():/* continue with next overload */;
             }
             if(isEmpty(returnTypeForOverloadedActuals)) { s.report(error(current, "Call with %v arguments cannot be resolved", size(actuals)));}
             else /*retType = */ return overloadedAType(returnTypeForOverloadedActuals);
        }
    }
    //No overloaded actual
    return computeReturnType(current, scope, retType, formalTypes, actuals, actualTypes, kwFormals, keywordArguments, identicalFormals, s);
}

private AType computeReturnType(Expression current, loc _src, AType retType, list[AType] formalTypes, list[Expression] actuals, list[AType] actualTypes, list[Keyword] kwFormals, keywordArguments, list[bool] identicalFormals, Solver s){
    //println("computeReturnType: retType=<retType>, formalTypes=<formalTypes>, actualTypes=<actualTypes>");
    index_formals = index(formalTypes);
    Bindings bindings = ();
    for(int i <- index_formals){
        try   bindings = matchRascalTypeParams(formalTypes[i], actualTypes[i], bindings);
        catch invalidMatch(str reason):
              s.report(error(i < size(actuals)  ? actuals[i] : current, reason));
    }
  
    iformalTypes = formalTypes;
    if(!isEmpty(bindings)){
       iformalTypes =
            for(int i <- index_formals){
                try {
                    append instantiateRascalTypeParameters(current, formalTypes[i], bindings, s); // changed
                } catch invalidInstantiation(str msg): {
                    s.report(error(current, "Cannot instantiate formal parameter type `<prettyAType(formalTypes[i])>`: " + msg));
                }
            };
    }
    
    for(int i <- index_formals){
        ai = actualTypes[i];
        ai = s.instantiate(ai);
        if(tvar(loc _) := ai || !s.isFullyInstantiated(ai)){
           if(identicalFormals[i]){
              s.requireUnify(ai, iformalTypes[i], error(current, "Cannot unify %t with %t", ai, iformalTypes[i]));
              ai = s.instantiate(ai);
              //clearBindings();
           } else
              continue;
        }
        s.requireComparable(ai, iformalTypes[i], error(i < size(actuals)  ? actuals[i] : current, "Argument %v should have type %t, found %t", i, iformalTypes[i], ai));     
    }
    
    checkExpressionKwArgs(kwFormals, keywordArguments, bindings, s);
    
    //// Artificially bind unbound type parameters in the return type
    //for(rparam <- collectAndUnlabelRascalTypeParams(retType)){
    //    pname = rparam.pname;
    //    if(!bindings[pname]?) bindings[pname] = rparam;
    //}
    if(isEmpty(bindings))
       return retType;
       
    try   return instantiateRascalTypeParameters(current, retType, bindings, s); // changed
    catch invalidInstantiation(str msg):
          s.report(error(current, msg));

    return avalue();
}
 
 private AType computeExpressionNodeType(loc scope, list[Expression]  actuals, (KeywordArguments[Expression]) `<KeywordArguments[Expression] keywordArgumentsExp>`, Solver s){                     
    actualType = [ s.getType(actuals[i]) | i <- index(actuals) ];
    return anode(computeExpressionKwArgs(keywordArgumentsExp, scope, s));
}

private list[AType] computeExpressionKwArgs((KeywordArguments[Expression]) `<KeywordArguments[Expression] keywordArgumentsExp>`, loc _, Solver s){
    if(keywordArgumentsExp is none) return [];
 
    return for(kwa <- keywordArgumentsExp.keywordArgumentList){ 
                kwName = prettyPrintName(kwa.name);
                kwType = s.getType(kwa.expression);
                append kwType[alabel=kwName];
            }  
}

// ---- tuple

void collect(current: (Expression) `\< <{Expression ","}+ elements1> \>`, Collector c){
    elms = [ e | Expression e <- elements1 ];
    c.calculate("tuple expression", current, elms,
        AType(Solver s) {
                for(elm <- elms) checkNonVoid(elm, s, "Element of tuple");
                return atuple(atypeList([ s.getType(elm) | elm <- elms ]));
        });
    collect(elements1, c);
}

// ---- map

void collect(current: (Expression) `( <{Mapping[Expression] ","}* mappings>)`, Collector c){
    froms = [ m.from | m <- mappings ];
    tos =  [ m.to | m <- mappings ];
    if(isEmpty(froms)){
        c.fact(current, amap(avoid(), avoid()));
    } else {
        c.calculate("map expression", current, froms + tos,
            AType(Solver s) {
                for(f <- froms) checkNonVoid(f, s, "Key element of map");
                for(t <- tos) checkNonVoid(t, s, "Value element of map");
                return amap(s.lubList([ s.getType(f) | f <- froms ]), lubList([ s.getType(t) | t <- tos ]));
            });
        collect(mappings, c);
    }
}

//void collect(current: (Expression) `( <{Mapping[Expression] ","}* mappings> )`, Collector c){
//    froms = [ m.from | m <- mappings ];
//    tos =  [ m.to | m <- mappings ];
//    if(isEmpty(froms)){
//        c.fact(current, amap(avoid(), avoid()));
//    } else {
//        c.calculate("map expression", current, mappings,
//            AType(Solver s) {
//                return s.lubList([ s.getType(m) | m <- mappings ]);
//            });
//        collect(mappings, c);
//    }
//}
//
//void collect((Mapping[Expression]) `<Mapping[Expression] mapping>`, collector c){
//    c.calculate("map expression", mapping,[mapping.from, mapping.to],
//            AType(Solver s) {
//                checkNonVoid(mapping.from, s, "Key element of map");
//                checkNonVoid(mapping.to, s, "Value element of map");
//                return amap(s.getType(mapping.from), ms.getType(mapping.to));
//            });
//    collect(mapping.from, mapping.to, c);
//}

// TODO: does not work in interpreter
//void collect(Mapping[&T] mappings, collector c){
//    collect(mappings.from, mappings.to, c);
//}

// ---- it

// ---- qualified name

//void collect(current: (Name) `<Name name>`, Collector c){
//    base = unescape("<name>");
//    if(base != "_"){
//      if(inPatternScope(c)){
//        c.use(name, {variableId(), fieldId(), functionId(), constructorId()});
//      } else {
//        c.useLub(name, {variableId(), fieldId(), functionId(), constructorId()});
//      }
//    } else {
//      c.fact(current, avalue());
//    }
//}
 
void collect(current: (QualifiedName) `<QualifiedName name>`, Collector c){
    <qualifier, base> = splitQualifiedName(name);
    if(!isEmpty(qualifier)){     
       c.useQualified([qualifier, base], name, {variableId(), moduleVariableId(), functionId(), constructorId()}, dataOrSyntaxRoles + {moduleId()} );
    } else {
       if(!isWildCard(base)){
          //if(inPatternScope(c)){
            if(!isEmpty(c.getStack(currentAdt))){
                c.use(name, {variableId(), moduleVariableId(), formalId(), nestedFormalId(), patternVariableId(), keywordFormalId(), fieldId(), keywordFieldId(), functionId(), constructorId()});
            } else {
                c.useLub(name, {variableId(), moduleVariableId(), formalId(), nestedFormalId(), patternVariableId(), keywordFormalId(), fieldId(), keywordFieldId(), functionId(), constructorId()});
            }
          //} else {
          //  c.useLub(name, {variableId(), formalId(), nestedFormalId(), patternVariableId(), keywordFormalId(), fieldId(), keywordFieldId(), functionId(), constructorId()});
          //}
       } else {
          c.fact(current, avalue());
       }
    }
}

// ---- subscript

void collect(current:(Expression)`<Expression expression> [ <{Expression ","}+ indices> ]`, Collector c){
    indexList = [e | e <- indices];
    // Subscripts can also use the "_" character, which means to ignore that position; we do
    // that here by treating it as avalue(), which is comparable to all other types and will
    // thus work when calculating the type below.
    
    for(e <- indexList, (Expression)`_` := e){
        c.fact(e, avalue());
    }
    
    c.calculate("subscription", current, expression + indexList,
                  AType(Solver s){ 
                    checkNonVoid(expression, s, "Base expression of subscription");
                    for(e <- indexList) checkNonVoid(e, s, "Subscript");
                    return computeSubscriptionType(current, s.getType(expression), [s.getType(e) | e <- indexList], indexList, s);  
                  });
    collect(expression, indices, c);
}

// ---- slice

void collect(current: (Expression) `<Expression e> [ <OptionalExpression ofirst> .. <OptionalExpression olast> ]`, Collector c){
    if(ofirst is noExpression) c.fact(ofirst, aint());
    if(olast is noExpression) c.fact(olast, aint());

    c.calculate("slice", current, [e, ofirst, olast],
        AType(Solver s){ 
            checkNonVoid(e, s, "Base expression of slice");
            checkNonVoid(ofirst, s, "First expression of slice");
            checkNonVoid(olast, s, "Last expression of slice");
            return computeSliceType(current, s.getType(e), s.getType(ofirst), aint(), s.getType(olast), s); 
        });
    collect(e, ofirst, olast, c);
}

// ---- sliceStep

void collect(current: (Expression) `<Expression e> [ <OptionalExpression ofirst>, <Expression second> .. <OptionalExpression olast> ]`, Collector c){
    if(ofirst is noExpression) c.fact(ofirst, aint());
    if(olast is noExpression) c.fact(olast, aint());

    c.calculate("slice step", current, [e, ofirst, second, olast],
        AType(Solver s){ 
            checkNonVoid(e, s, "Base expression of slice");
            checkNonVoid(ofirst, s, "First expression of slice");
            checkNonVoid(second, s, "Second expression of slice");
            checkNonVoid(olast, s, "Last expression of slice");
            return computeSliceType(current, s.getType(e), s.getType(ofirst), s.getType(second), s.getType(olast), s); 
        });
    collect(e, ofirst, second, olast, c);
}

// ---- fieldAccess

void collect(current: (Expression) `<Expression expression> . <Name field>`, Collector c){
    c.useViaType(expression, field, {fieldId(), keywordFieldId()/*, annoId()*/}); // DURING TRANSITION: allow annoIds
    c.require("non void", expression, [], makeNonVoidRequirement(expression, "Base expression of field selection"));
    c.fact(current, field);
    collect(expression, c);
}

// ---- fieldUpdate

void collect(current:(Expression) `<Expression expression> [ <Name field> = <Expression repl> ]`, Collector c){
    scope = c.getScope();
    //c.use(field, {fieldId(), keywordFieldId()});
    c.calculate("field update of `<field>`", current, [expression, repl],
        AType(Solver s){ 
                 fieldType = computeFieldTypeWithADT(s.getType(expression), field, scope, s);
                 replType = s.getType(repl);
                 checkNonVoid(expression, s, "Base expression of field update`");
                 checkNonVoid(repl, s, "Replacement expression of field update`");
                 s.requireSubType(replType, fieldType, error(current, "Cannot assign value of type %t to field %q of type %t", replType, field, fieldType));
                 return s.getType(expression);
        });
    collect(expression, repl, c);
}

// ---- fieldProjection

void collect(current:(Expression) `<Expression expression> \< <{Field ","}+ fields> \>`, Collector c){

    flds = [f | f <- fields];
    c.calculate("field projection", current, [expression],
        AType(Solver s){ 
            checkNonVoid(expression, s, "Base expression of field projection");
            return computeFieldProjectionType(current, s.getType(expression), flds, s); 
        });
    collect(expression, fields, c);
}

void collect(current:(Field) `<IntegerLiteral il>`, Collector c){
    c.fact(current, aint());
}

void collect(current:(Field) `<Name fieldName>`, Collector c){

}

private AType computeFieldProjectionType(Expression current, AType base, list[lang::rascal::\syntax::Rascal::Field] fields, Solver s){

    if(!s.isFullyInstantiated(base)) throw TypeUnavailable();
    
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := base){
        projection_overloads = {};
        for(<key, role, tp> <- overloads){
            try {
                projection_overloads += <key, role, computeFieldProjectionType(current, tp, fields, s)>;
            } catch checkFailed(list[FailMessage] _): /* continue with next overload */;
              catch NoBinding(): /* continue with next overload */;
//>>>         catch e: /* continue with next overload */;
        }
        if(isEmpty(projection_overloads))  s.report(error(current, "Illegal projection %t", base));
        return overloadedAType(projection_overloads);
    }
    
    // Get back the fields as a tuple, if this is one of the allowed subscripting types.
    AType rt = avoid();

    if (isRelAType(base)) {
        rt = getRelElementType(base);
    } else if (isListRelAType(base)) {
        rt = getListRelElementType(base);
    } else if (isMapAType(base)) {
        rt = getMapFieldsAsTuple(base);
    } else if (isTupleAType(base)) {
        rt = base;
    } else {
        s.report(error(current, "Type %t does not allow fields", base));
    }
    
    // Find the field type and name for each index
    list[FailMessage] failures = [];
    list[AType] subscripts = [ ];
    list[str] fieldNames = [ ];
    bool maintainFieldNames = tupleHasFieldNames(rt);
    
    for (f <- fields) {
        if ((Field)`<IntegerLiteral il>` := f) {
            int offset = toInt("<il>");
            if (!tupleHasField(rt, offset))
                failures += error(il, "Field subscript %q out of range", il);
            else {
                subscripts += getTupleFieldType(rt, offset);
                if (maintainFieldNames) fieldNames += getTupleFieldName(rt, offset);
            }
        } else if ((Field)`<Name fn>` := f) {
            fnAsString = prettyPrintName(fn);
            if (!tupleHasField(rt, fnAsString)) {
                failures += error(fn, "Field %q does not exist", fn);
            } else {
                subscripts += getTupleFieldType(rt, fnAsString);
                if (maintainFieldNames) fieldNames += fnAsString;
            }
        } else {
            throw rascalCheckerInternalError("computeFieldProjectionType; Unhandled field case: <f>");
        }
    }
    
    if (size(failures) > 0) s.reports(failures);

    // Keep the field names if all fields are named and if we have unique names
    if (!(size(subscripts) > 1 && size(subscripts) == size(fieldNames) && size(fieldNames) == size(toSet(fieldNames)))) {
        subscripts = [ unset(tp, "alabel") | tp <- subscripts ];
    }
    
    if (isRelAType(base)) {
        if (size(subscripts) > 1) return arel(atypeList(subscripts));
        return makeSetType(head(subscripts));
    } else if (isListRelAType(base)) {
        if (size(subscripts) > 1) return alrel(atypeList(subscripts));
        return makeListType(head(subscripts));
    } else if (isMapAType(base)) {
        if (size(subscripts) > 1) return arel(atypeList(subscripts));
        return makeSetType(head(subscripts));
    } else if (isTupleAType(base)) {
        if (size(subscripts) > 1) return atuple(atypeList(subscripts));
        return head(subscripts);
    } 
    
    s.report(error(current, "Illegal projection %t", base)); 
    return avalue(); 
}

// ---- setAnnotation

//TODO: Deprecated
private AType computeSetAnnotationType(Tree current, AType t1, AType tn, AType t2, Solver s)
    = ternaryOp("set annotation", _computeSetAnnotationType, current, t1, tn, t2, s);

private AType _computeSetAnnotationType(Tree current, AType t1, AType tn, AType t2, Solver s){
    if (isNodeAType(t1) || isADTAType(t1) || isNonTerminalAType(t1)) {
        if(aanno(_, onType, annoType) := tn){
          s.requireSubType(t2, annoType, error(current, "Cannot assign value of type %t to annotation of type %t", t2, annoType));
           return t1;
        } else
            s.report(error(current, "Invalid annotation type: %t", tn));
    } else {
        s.report(error(current, "Invalid type: expected node, ADT, or concrete syntax types, found %t", t1));
    }
    return avalue();
}

// TODO: Deprecated
void collect(current:(Expression) `<Expression expression> [ @ <Name name> = <Expression repl> ]`, Collector c) {
    c.report(warning(current, "Annotations are deprecated, use keyword parameters instead"));
    
    c.use(name, {annoId()});
    c.calculate("set annotation", current, [expression, name, repl],
        AType(Solver s){ 
                t1 = s.getType(expression); tn = s.getType(name); t2 = s.getType(repl);
                checkNonVoid(expression, s, "Base expression of set annotation");
                checkNonVoid(repl, s, "Replacement expression of set annotation");
               return computeSetAnnotationType(current, t1, tn, t2, s);
               });
    collect(expression, repl, c);
}

// ---- getAnnotation

AType computeGetAnnotationType(Tree current, AType t1, AType tn, Solver s)
    = binaryOp("get annotation", _computeGetAnnotationType, current, t1, tn, s);

private AType _computeGetAnnotationType(Tree current, AType t1, AType tn, Solver s){
    if (isNodeAType(t1) || isADTAType(t1) || isNonTerminalAType(t1)) {
        if(aanno(_, onType, annoType) := tn){
           return annoType;
        } else
            s.report(error(current, "Invalid annotation type: %t", tn));
    } else {
        s.report(error(current, "Invalid type: expected node, ADT, or concrete syntax types, found %t", t1));
    }
    return avalue();
}

// TODO: Deprecated
void collect(current:(Expression) `<Expression expression>@<Name name>`, Collector c) {
    c.report(warning(current, "Annotations are deprecated, use keyword parameters instead"));
    
    c.use(name, {annoId()});
    c.calculate("get annotation", current, [expression, name],
        AType(Solver s){ 
                 t1 = s.getType(expression);
                 tn = s.getType(name);
                 checkNonVoid(expression, s, "Base expression of get annotation`");
                 return computeGetAnnotationType(current, t1, tn, s);
                 });
   collect(expression, c);
}