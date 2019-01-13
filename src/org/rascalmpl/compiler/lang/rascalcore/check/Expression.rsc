@bootstrapParser
module lang::rascalcore::check::Expression
 
extend analysis::typepal::TypePal;

extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::ConvertType;
extend lang::rascalcore::check::Declaration;
extend lang::rascalcore::check::Operators;
extend lang::rascalcore::check::Pattern;
extend lang::rascalcore::check::Statement;

import lang::rascalcore::check::ATypeExceptions;
import lang::rascalcore::check::ATypeInstantiation;
import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::TypePalConfig;

import lang::rascalcore::grammar::definition::Symbols;

import lang::rascal::\syntax::Rascal;
import String;
//import ListRelation;
import Set;
import Map;
import Node;
import ValueIO;
import IO;
//import Exception;

// ---- Rascal literals

void collect(IntegerLiteral il, Collector c){
    c.fact(il, aint());
}

void collect(RealLiteral current, Collector c){
    c.fact(current, areal());
}

void collect(BooleanLiteral current, Collector c){
    c.fact(current, abool());
 }

void collect(DateTimeLiteral current, Collector c){
    c.fact(current, adatetime());
    try {
        readTextValueString("<current>");   // ensure that the datetime literal is valid
    } catch IO(msg): {
        c.report(error(current, "Malformed datetime literal %q", current));
    }
}

void collect(RationalLiteral current, Collector c){
    c.fact(current, arat());
}

//void collect(Literal l:(Literal)`<RegExpLiteral rl>`, Collector c) {
//    println("collectA: <l>");
//    c.fact(l, astr());
//    collectParts(l, c);
//}
//
//void collect(current:(RegExp)`\<<Name name>\>`, Collector c) {
//    println("collectB: <current>");
//    c.use(name, variableId());
//    c.fact(current, astr());
//}
//
//void collect(current:(RegExp)`\<<Name name>:<NamedRegExp* regexps>\>`, Collector c) {
//    println("collectC: <current>");
//    c.define("<name>", variableId(), name, defType(astr()));
//    c.fact(current, astr());
//    collectParts(regexps, c);
//}

// ---- string literals and templates
void collect(current:(Literal)`<StringLiteral sl>`, Collector c){
    c.fact(current, astr());
    collect(sl, c);
}

void collect(StringCharacter current, Collector c){ }

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
        c.requireEager("if then template", current, condList, void (Solver s){ checkConditions(condList, s); });
        beginPatternScope("conditions", c);
        collect(conditions, c);
        endPatternScope(c);
        collect(preStats, body, postStats, c);
    c.leaveScope(conditions);
}

void collect(current: (StringTemplate) `if( <{Expression ","}+ conditions> ){ <Statement* preStatsThen> <StringMiddle thenString> <Statement* postStatsThen> } else { <Statement* preStatsElse> <StringMiddle elseString> <Statement* postStatsElse> }`, Collector c){
    //c.enterScope(current);
    c.enterScope(conditions);   // thenPart may refer to variables defined in conditions; elsePart may not
    
        condList = [cond | Expression cond <- conditions];
        // TODO scoping in else does not yet work
        if(!isEmpty([s | s <- preStatsElse]))
           storeExcludeUse(conditions, preStatsElse, c); // variable occurrences in elsePart may not refer to variables defined in conditions
        if(!isEmpty("<elseString>"))
           storeExcludeUse(conditions, elseString, c); 
        if(!isEmpty([s | s <- postStatsElse]))
           storeExcludeUse(conditions, postStatsElse, c);
        
        c.calculate("if then else template", current, condList/* + [postStatsThen + postStatsElse]*/,
            AType (Solver s){ checkConditions(condList, s); 
                      return avalue();
            });
        beginPatternScope("conditions", c);
        collect(condList, c);
        endPatternScope(c);
        collect(preStatsThen, thenString, postStatsThen, preStatsElse, elseString, postStatsElse, c);    
    c.leaveScope(conditions);
    //c.leaveScope(current);
} 

void collect(current: (StringTemplate) `for( <{Expression ","}+ generators> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`, Collector c){
    c.enterScope(generators);   // body may refer to variables defined in conditions
        condList = [cond | Expression cond <- generators];
        c.requireEager("for statement  template", current, condList, void (Solver s){ checkConditions(condList, s); });
        beginPatternScope("conditions", c);
        collect(condList, c);
        endPatternScope(c);
        collect(preStats, body, postStats, c);
    c.leaveScope(generators);
}

void collect(current: (StringTemplate) `do { <Statement* preStats> <StringMiddle body> <Statement* postStats> } while( <Expression condition> )`, Collector c){
    c.enterScope(current);   // condition may refer to variables defined in body
        condList = [condition];
        c.requireEager("do statement template", current, condList, void (Solver s){ checkConditions(condList, s); });
        collect(preStats, body, postStats, c);
        beginPatternScope("conditions", c);
        collect(condition, c);
        endPatternScope(c);
    c.leaveScope(current); 
}

void collect(current: (StringTemplate) `while( <Expression condition> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> }`, Collector c){
    c.enterScope(condition);   // body may refer to variables defined in conditions
        condList = [condition];
        c.requireEager("while statement  template", current, condList, void (Solver s){ checkConditions(condList, s); });
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

// ---- Concrete literals

void collect(Concrete concrete, Collector c){
    c.fact(concrete, concrete.symbol);
    collect(concrete.symbol, c);
    collect(concrete.parts, c);
}

void collect(current: (ConcreteHole) `\< <Sym symbol> <Name name> \>`, Collector c){
    varType = symbol;
    uname = prettyPrintName(name);
    //println("patternContainer: <c.getStack(patternContainer)>");
    if(size(c.getStack(patternContainer)) == 1){    // An expression
        //println("ConcreteHole exp: <current>");
        //c.use(name, {variableId()});
        c.define(uname, formalOrPatternFormal(c), name, defLub([symbol], AType(Solver s) { return s.getType(symbol); }));
        //c.calculate("concrete hole", current, [varType], AType(Solver s) { return s.getType(varType); });
    } else {                                        //A pattern
        //println("ConcreteHole pat: <current>");
      
        if(uname != "_"){
           if(uname in c.getStack(patternNames)){
              c.useLub(name, {formalOrPatternFormal(c)});
           } else {
               c.push(patternNames, uname);
               c.define(uname, formalOrPatternFormal(c), name, defLub([symbol], AType(Solver s) { return s.getType(symbol); }));
           }
        }
    }
   // c.calculateEager("concrete hole", current, [varType], AType(Solver s) { return s.getType(varType); });
    c.fact(current, symbol);
    collect(symbol, c);
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
}

// ---- closure

str closureName(Expression closure){
    l = getLoc(closure);
    return "$CLOSURE<l.offset>";
}

void collect(current: (Expression) `<Type returnType> <Parameters parameters> { <Statement+ statements> }`, Collector c){
    parentScope = c.getScope();
    c.enterLubScope(current);
        scope = c.getScope();
        c.setScopeInfo(scope, functionScope(), returnInfo(returnType));
        
        formals = getFormals(parameters);
        kwFormals = getKwFormals(parameters);
        stats = [stat | stat <- statements];
        
        c.calculate("type of closure", current, returnType + formals,
            AType(Solver s){ return afunc(s.getType(returnType), [s.getType(f) | f <- formals], computeKwFormals(kwFormals, s)); });
            
        dt = defType(returnType + formals, AType(Solver s){
                return afunc(s.getType(returnType), [s.getType(f) | f <- formals], computeKwFormals(kwFormals, s)); 
             });
        c.defineInScope(parentScope, closureName(current), functionId(), current, dt); 
        collect(returnType + formals + kwFormals + stats, c);
    c.leaveScope(current);
}

// ---- void closure

void collect(current: (Expression) `<Parameters parameters> { <Statement* statements0> }`, Collector c){
    parentScope = c.getScope(); 
    c.enterLubScope(current);
        scope = c.getScope();
        
        //TODO: setScope Info, where comes void from?
        formals = getFormals(parameters);
        kwFormals = getKwFormals(parameters);
        stats = [stat | stat <- statements0];
        
        c.calculate("type of void closure", current, formals,
            AType(Solver s){ return afunc(avoid(), [s.getType(f) | f <- formals], computeKwFormals(kwFormals, s)); });
        dt = defType(formals, AType(Solver s){
                return afunc(avoid(), [s.getType(f) | f <- formals], computeKwFormals(kwFormals, s)); 
             });
        c.defineInScope(parentScope,  closureName(current), functionId(), current, dt); 
        c.use(current, {functionId()});
        collect(formals + kwFormals + stats, c);
    c.leaveScope(current);
}

// ---- step range

void collect(current: (Expression) `[ <Expression first> , <Expression second> .. <Expression last> ]`, Collector c){
    c.calculate("step range", current, [first, second, last],
        AType(Solver s){ t1 = s.getType(first); t2 = s.getType(second); t3 = s.getType(last);
                 s.requireSubType(t1,anum(), error(first, "Invalid type: expected numeric type, found %t", t1));
                 s.requireSubType(t2,anum(), error(second, "Invalid type: expected numeric type, found %t", t2));
                 s.requireSubType(t3,anum(), error(last, "Invalid type: expected numeric type, found %t", t3));
                 return alist(s.lubList([t1, t2, t3]));
        
        });
    collect(first, second, last, c);    
}

// ---- range

void collect(current: (Expression) `[ <Expression first> .. <Expression last> ]`, Collector c){
    c.calculate("step range", current, [first, last],
        AType(Solver s){ t1 = s.getType(first); t2 = s.getType(last);
                 s.requireSubType(t1,anum(), error(first, "Invalid type: expected numeric type, found %t", t1));
                 s.requireSubType(t2,anum(), error(last, "Invalid type: expected numeric type, found %t", t2));
                 return alist(s.lub(t1, t2));
        });
    collect(first, last, c);    
}

// ---- visit

void collect(current: (Expression) `<Label label> <Visit vst>`, Collector c){
    c.enterScope(current);
        scope = c.getScope();
        c.setScopeInfo(scope, visitOrSwitchScope(), visitOrSwitchInfo(vst.subject, true));
        if(label is \default){
            c.define(prettyPrintName(label.name), labelId(), label.name, defType(avoid()));
        }
        c.calculate("visit", current, [vst.subject], AType(Solver s){ return s.getType(vst.subject); });
        collect(vst, c);
    c.leaveScope(current);
}

// ---- reifyType

void collect(current: (Expression) `# <Type tp>`, Collector c){
    c.calculate("reified type", current, [tp], AType(Solver s) { return areified(s.getType(tp)); });
    collect(tp, c);
}

// ---- reifiedType

void collect(current: (Expression) `type ( <Expression es> , <Expression ed> )`, Collector c) {
    // TODO: Is there anything we can do statically to make the result type more accurate?
    c.fact(current, areified(avalue()));
    c.require("reified type", current, [es, ed],
        void (Solver s){ 
            s.requireSubType(es, aadt("Symbol",[], dataSyntax()), error(es, "Expected subtype of Symbol, instead found %t", es));
            s.requireSubType(ed, amap(aadt("Symbol",[],dataSyntax()),aadt("Production",[],dataSyntax())), 
                error(ed, "Expected subtype of map[Symbol,Production], instead found %t", ed));
          });
    collect(es, ed, c);
}

// ---- any

void collect(current: (Expression)`any ( <{Expression ","}+ generators> )`, Collector c){
    gens = [gen | gen <- generators];
    c.fact(current, abool());
    
    c.enterScope(generators);
    beginPatternScope("any", c);
        c.require("any", current, gens,
            void (Solver s) { for(gen <- gens) if(!isBoolType(s.getType(gen))) s.report(error(gen, "Type of generator should be `bool`, found %t", gen));
            });
        collect(generators, c);
    endPatternScope(c);
    c.leaveScope(generators);
}

// ---- all

void collect(current: (Expression)`all ( <{Expression ","}+ generators> )`, Collector c){
    gens = [gen | gen <- generators];
    c.fact(current, abool());
    
    //newScope = c.getScope() != getLoc(current);
    //if(newScope) c.enterScope(current);
    c.enterScope(generators);
    beginPatternScope("all", c);
        c.require("all", current, gens,
            void (Solver s) { for(gen <- gens) if(!isBoolType(s.getType(gen))) s.report(error(gen, "Type of generator should be `bool`, found %t", gen));
            });
        collect(generators, c);
    endPatternScope(c);
    //if(newScope) c.leaveScope(current);
    c.leaveScope(generators);
}

// ---- comprehensions and reducer

// set comprehension

void collect(current: (Comprehension)`{ <{Expression ","}+ results> | <{Expression ","}+ generators> }`, Collector c){
    gens = [gen | gen <- generators];
    res  = [r | r <- results];
    storeAllowUseBeforeDef(current, results, c); // variable occurrences in results may refer to variables defined in generators
    c.enterScope(current);
    beginPatternScope("set-comprehension", c);
        c.require("set comprehension", current, gens,
            void (Solver s) { for(gen <- gens) if(!isBoolType(s.getType(gen))) s.report(error(gen, "Type of generator should be `bool`, found %t", gen));
            });
        c.calculate("set comprehension results", current, res,
            AType(Solver s){
                return makeSetType(lubList([ s.getType(r) | r <- res]));
            });
         
        collect(results, generators, c);
    endPatternScope(c);
    c.leaveScope(current);
}

// list comprehension

void collect(current: (Comprehension) `[ <{Expression ","}+ results> | <{Expression ","}+ generators> ]`, Collector c){
    gens = [gen | gen <- generators];
    res  = [r | r <- results];
    storeAllowUseBeforeDef(current, results, c); // variable occurrences in results may refer to variables defined in generators
    c.enterScope(current);
    beginPatternScope("list-comprehension", c);
        c.require("list comprehension", current, gens,
            void (Solver s) { for(gen <- gens) if(!isBoolType(s.getType(gen))) s.report(error(gen, "Type of generator should be `bool`, found %t", gen));
            });
        c.calculate("list comprehension results", current, res,
            AType(Solver s){
                return makeListType(lubList([ s.getType(r) | r <- res]));
            });
         
        collect(results, generators, c);
    endPatternScope(c);
    c.leaveScope(current);
}

// map comprehension

void collect(current: (Comprehension) `(<Expression from> : <Expression to> | <{Expression ","}+ generators> )`, Collector c){
    gens = [gen | gen <- generators];
    storeAllowUseBeforeDef(current, from, c); // variable occurrences in from may refer to variables defined in generators
    storeAllowUseBeforeDef(current, to, c); // variable occurrences in to may refer to variables defined in generators
    c.enterScope(current);
    beginPatternScope("map-comprehension", c);
        c.require("map comprehension", current, gens,
            void (Solver s) { for(gen <- gens) if(!isBoolType(s.getType(gen))) s.report(error(gen, "Type of generator should be `bool`, found %t", gen));
            });
        c.calculate("list comprehension results", current, [from, to],
            AType(Solver s){
                return makeMapType(unset(s.getType(from), "label"), unset(s.getType(to), "label"));
            });
         
        collect(from, to, generators, c);
    endPatternScope(c);
    c.leaveScope(current);
}

// ---- reducer

void collect(current: (Expression) `( <Expression init> | <Expression result> | <{Expression ","}+ generators> )`, Collector c){
    gens = [gen | gen <- generators];
    storeAllowUseBeforeDef(current, result, c); // variable occurrences in result may refer to variables defined in generators
    c.enterScope(current);
    beginPatternScope("reducer", c);
        //tau = c.newTypeVar();
        c.define("it", variableId(), init, defLub([init, result], AType(Solver s) { return s.lub(init, result); }));
        c.require("reducer", current, gens,
            void (Solver s) { for(gen <- gens) if(!isBoolType(s.getType(gen))) s.report(error(gen, "Type of generator should be `bool`, found %t", gen));
            });
        //c.calculate("reducer result", current, [result], AType(Solver s) { return s.getType(result); });
        
        c.fact(current, result);
        //c.requireEager("reducer it", current, [init, result], void (Solver s){ unify(tau, lub(getType(init), getType(result)), onError(current, "Can determine it")); });
         
        collect(init, result, generators, c);
    endPatternScope(c);
    c.leaveScope(current);
}

void collect(current: (Expression) `it`, Collector c){
    c.use(current, {variableId()});
}

// ---- set

void collect(current: (Expression) `{ <{Expression ","}* elements0> }`, Collector c){
    elms = [ e | Expression e <- elements0 ];
    if(isEmpty(elms)){
        c.fact(current, aset(avoid()));
    } else {
        c.calculateEager("set expression", current, elms, AType(Solver s) { return aset(s.lubList([s.getType(elm) | elm <- elms])); });
        collect(elms, c);
    }
}

// ---- list

void collect(current: (Expression) `[ <{Expression ","}* elements0> ]`, Collector c){
    elms = [ e | Expression e <- elements0 ];
    if(isEmpty(elms)){
        c.fact(current, alist(avoid()));
    } else {
        c.calculateEager("list expression", current, elms, AType(Solver s) { return alist(s.lubList([s.getType(elm) | elm <- elms])); });
        collect(elms, c);
    }
}

// ---- call or tree
           
void collect(current: (Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments[Expression] keywordArguments>)`, Collector c){
    actuals = [a | Expression a <- arguments];  
    kwactuals = keywordArguments is \default ? [ kwa.expression | kwa <- keywordArguments.keywordArgumentList] : [];
  
    scope = c.getScope();
    
    c.calculate("call of function/constructor `<expression>`", current, expression + actuals + kwactuals,
        AType(Solver s){
            for(x <- expression + actuals + kwactuals){
                 tp = s.getType(x);
                 if(!s.isFullyInstantiated(tp)) throw TypeUnavailable();
            }
            
            texp = s.getType(expression);
            if(isStrType(texp)){
                return computeExpressionNodeType(current, scope, actuals, keywordArguments, s);
            } 
            if(isLocType(texp)){
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
             
            if(overloadedAType(rel[loc, IdRole, AType] overloads) := texp){
              <filteredOverloads, identicalFormals> = filterOverloads(overloads, size(actuals));
              if({<key, idr, tp>} := filteredOverloads){
                texp = tp;
                s.specializedFact(expression, tp);
              } else {
                overloads = filteredOverloads;
                validReturnTypeOverloads = {};
                validOverloads = {};
                next_fun:
                for(ovl: <key, idr, tp> <- overloads){                       
                    if(ft:afunc(AType ret, list[AType] formals, list[Keyword] kwFormals) := tp){
                       try {
                            // TODO: turn this on after review of all @deprecated uses in the Rascal library library
                            if(tp.deprecationMessage? && c.getConfig().warnDeprecated){
                                s.report(warning(expression, "Deprecated function%v", isEmpty(tp.deprecationMessage) ? "" : ": " + tp.deprecationMessage));
                            }
                            validReturnTypeOverloads += <key, dataId(), checkArgsAndComputeReturnType(expression, scope, ret, formals, kwFormals, ft.varArgs ? false, actuals, keywordArguments, identicalFormals, s)>;
                            validOverloads += ovl;
                       } catch checkFailed(list[FailMessage] fms):
                             continue next_fun;
                         catch NoBinding():
                            continue next_fun;
                    }
                 }
                 next_cons:
                 for(ovl: <key, idr, tp> <- overloads){
                    if(acons(ret:aadt(adtName, list[AType] parameters, _),  list[AType] fields, list[Keyword] kwFields) := tp){
                       try {
                            validReturnTypeOverloads += <key, dataId(), computeADTType(expression, adtName, scope, ret, fields, kwFields, actuals, keywordArguments, identicalFormals, s)>;
                            validOverloads += ovl;
                       } catch checkFailed(list[FailMessage] fms):
                             continue next_cons;
                         catch NoBinding():
                             continue next_cons;
                    }
                 }
                 if({<key, idr, tp>} := validOverloads){
                    texp = tp;  
                    s.specializedFact(expression, tp);
                    // TODO check identicalFields to see whether this can make sense
                    // unique overload, fall through to non-overloaded case to potentially bind more type variables
                 } else if(isEmpty(validReturnTypeOverloads)) { 
                        s.report(error(current, "%q is defined as %t and cannot be applied to argument(s) %v", "<expression>", expression, actuals));}
                 else {
                    stexp = overloadedAType(validOverloads);
                    if(texp != stexp) s.specializedFact(expression, stexp);
                    return overloadedAType(validReturnTypeOverloads);
                 }
               }
            }
            
            if(ft:afunc(AType ret, list[AType] formals, list[Keyword] kwFormals) := texp){
               if(texp.deprecationMessage? && c.getConfig().warnDeprecated){
                    s.report(warning(expression, "Deprecated function%v", isEmpty(texp.deprecationMessage) ? "": ": " + texp.deprecationMessage));
               }
                return checkArgsAndComputeReturnType(expression, scope, ret, formals, kwFormals, ft.varArgs, actuals, keywordArguments, [true | int i <- index(formals)], s);
            }
            if(acons(ret:aadt(adtName, list[AType] parameters,_), list[AType] fields, list[Keyword] kwFields) := texp){
               return computeADTType(expression, adtName, scope, ret, fields/*<1>*/, kwFields, actuals, keywordArguments, [true | int i <- index(fields)], s);
            }
            s.report(error(current, "%q is defined as %t and cannot be applied to argument(s) %v", "<expression>", expression, actuals));
        });
      collect(expression, arguments, keywordArguments, c);
}

tuple[rel[loc, IdRole, AType], list[bool]] filterOverloads(rel[loc, IdRole, AType] overloads, int arity){
    filteredOverloads = {};
    prevFormals = [];
    identicalFormals = [true | int i <- [0 .. arity]];
    
    for(ovl:<key, idr, tp> <- overloads){                       
        if(ft:afunc(AType ret, formalTypes: list[AType] formals, list[Keyword] kwFormals) := tp){
           if(ft.varArgs ? (arity >= size(formals) - 1) : (arity == size(formals))) {
              filteredOverloads += ovl;
              if(isEmpty(prevFormals)){
                 prevFormals = formals;
              } else {
                 relevantFormals = [0 .. size(formals) - (ft.varArgs ? 1 : 0)];
                 for(int i <- relevantFormals) identicalFormals[i] = identicalFormals[i] && (comparable(prevFormals[i], formals[i]));
              }
           }
        } else
        if(acons(aadt(adtName, list[AType] parameters,_), list[AType] fields, list[Keyword] kwFields) := tp){
           if(size(fields) == arity){
              filteredOverloads += ovl;
              if(isEmpty(prevFormals)){
                 prevFormals = fields; //<1>;
              } else {
                 for(int i <- index(fields)) identicalFormals[i] = identicalFormals[i] && (comparable(prevFormals[i], fields[i]/*.fieldType*/));
              }
            }
        }
    }
    return <filteredOverloads, identicalFormals>;
}

// TODO: in order to reuse the function below `keywordArguments` is passed as where `eywordArguments[&T] keywordArguments` would make more sense.
// The interpreter does not handle this well, so revisit this later

// TODO: maybe check that all upperbounds of type parameters are identical?

AType checkArgsAndComputeReturnType(Expression current, loc scope, AType retType, list[AType] formals, list[Keyword] kwFormals, bool isVarArgs, list[Expression] actuals, keywordArguments, list[bool] identicalFormals, Solver s){
    nactuals = size(actuals); nformals = size(formals);
   
    list[AType] actualTypes = [];
   
    if(isVarArgs){
       if(nactuals < nformals - 1) s.report(error(current, "Expected at least %v argument(s) found %v", nformals-1, nactuals));
       varArgsType = (avoid() | s.lub(it, s.getType(actuals[i])) | int i <- [nformals-1 .. nactuals]);
       actualTypes = [s.getType(actuals[i]) | int i <- [0 .. nformals-1]] + (isListType(varArgsType) ? varArgsType : alist(varArgsType));
    } else {
        if(nactuals != nformals) s.report(error(current, "Expected %v argument(s), found %v", nformals, nactuals));
        actualTypes = [s.getType(a) | a <- actuals];
    }
    
    index_formals = index(formals);
    
    list[AType] formalTypes =  formals;
    
    for(int i <- index_formals){
        if(overloadedAType(rel[loc, IdRole, AType] overloads) := actualTypes[i]){   // TODO only handles a single overloaded actual
            //println("checkArgsAndComputeReturnType: <current>");
            //iprintln(overloads);
            returnTypeForOverloadedActuals = {};
            for(ovl: <key, idr, tp> <- overloads){   
                try {
                    actualTypes[i] = tp;
                    returnTypeForOverloadedActuals += <key, idr, computeReturnType(current, scope, retType, formalTypes, actuals, actualTypes, kwFormals, keywordArguments, identicalFormals, s)>;
                    //println("succeeds: <ovl>");
                } catch checkFailed(list[FailMessage] fms): /* continue with next overload */;
                  catch NoBinding():/* continue with next overload */;
             }
             if(isEmpty(returnTypeForOverloadedActuals)) { s.report(error(current, "Call with %v arguments cannot be resolved", size(actuals)));}
             else return overloadedAType(returnTypeForOverloadedActuals);
        }
    }
    //No overloaded actual
    return computeReturnType(current, scope, retType, formalTypes, actuals, actualTypes, kwFormals, keywordArguments, identicalFormals, s);
}

AType computeReturnType(Expression current, loc scope, AType retType, list[AType] formalTypes, list[Expression] actuals, list[AType] actualTypes, list[Keyword] kwFormals, keywordArguments, list[bool] identicalFormals, Solver s){
    //println("computeReturnType: retType=<retType>, formalTypes=<formalTypes>, actualTypes=<actualTypes>");
    index_formals = index(formalTypes);
    Bindings bindings = ();
    for(int i <- index_formals){
        try   bindings = matchRascalTypeParams(formalTypes[i], actualTypes[i], bindings, bindIdenticalVars=true);
        catch invalidMatch(str reason):
              s.report(error(i < size(actuals)  ? actuals[i] : current, reason));
    }
  
    iformalTypes = formalTypes;
    if(!isEmpty(bindings)){
        try {
          iformalTypes = [instantiateRascalTypeParams(formalTypes[i], bindings) | int i <- index_formals];
        } catch invalidInstantiation(str msg): {
            s.report(error(current, msg));
        }
    }
    
    for(int i <- index_formals){
        ai = actualTypes[i];
        ai = s.instantiate(ai);
        if(tvar(loc l) := ai || !s.isFullyInstantiated(ai)){
           if(identicalFormals[i]){
              s.requireUnify(ai, iformalTypes[i], error(current, "Cannot unify %t with %t", ai, iformalTypes[i]));
              ai = s.instantiate(ai);
              //clearBindings();
           } else
              continue;
        }
        s.requireComparable(ai, iformalTypes[i], error(i < size(actuals)  ? actuals[i] : current, "Argument %v should have type %t, found %t", i, iformalTypes[i], ai));       
    }
    
    checkExpressionKwArgs(kwFormals, keywordArguments, bindings, scope, s);
    
    //// Artificially bind unbound type parameters in the return type
    //for(rparam <- collectAndUnlabelRascalTypeParams(retType)){
    //    pname = rparam.pname;
    //    if(!bindings[pname]?) bindings[pname] = rparam;
    //}
    if(isEmpty(bindings))
       return retType;
    try   return instantiateRascalTypeParams(retType, bindings);
    catch invalidInstantiation(str msg):
          s.report(error(current, msg));
}

AType computeADTType(Tree current, str adtName, loc scope, AType retType, list[AType] formals, list[Keyword] kwFormals, actuals, keywordArguments, list[bool] identicalFormals, Solver s){                     
    //println("---- <current>, identicalFormals: <identicalFormals>");
    nactuals = size(actuals); nformals = size(formals);
    if(nactuals != nformals){
        s.report(error(current, "Expected %v arguments(s), found %v", nformals, nactuals));
    }
    formalTypes = formals; // [ expandUserTypes(formals[i], scope, s) | i <- index(formals) ];
    index_formals = index(formals);
    //println("formals: <formals>");
    list[AType] actualTypes = [];
    list[bool] dontCare = [];
    bool isExpression = true;
    switch(actuals){
        case list[Expression] expList: {
                dontCare = [ false | i <- index(expList) ];
                actualTypes = [ s.getType(expList[i]) | i <- index(expList) ];
                //print("expList: [ "); for(i <- index(expList)) print("<expList[i]> "); println(" ]");
            }
        case list[Pattern] patList: {
                dontCare = [ "<patList[i]>" == "_" | i <- index(patList) ];
                actualTypes = [ dontCare[i] ? formals[i] : getPatternType(patList[i], formals[i], scope, s) | i <- index(patList) ];
                //print("patList: [ "); for(i <- index(patList)) print("<patList[i]> "); println(" ]");
                isExpression = false;
            }
        default:
            throw rascalCheckerInternalError(getLoc(current), "Illegal argument `actuals`");
    }
    
    for(int i <- index_formals){
        if(overloadedAType(rel[loc, IdRole, AType] overloads) := actualTypes[i]){   // TODO only handles a single overloaded actual
            //println("computeADTType: <current>");
            //iprintln(overloads);
            returnTypeForOverloadedActuals = {};
            for(ovl: <key, idr, tp> <- overloads){   
                try {
                    actualTypes[i] = tp;
                    returnTypeForOverloadedActuals += <key, idr, computeADTReturnType(current, adtName, scope, retType, formalTypes, actuals, actualTypes, kwFormals, keywordArguments, identicalFormals, dontCare, isExpression, s)>;
                    //println("succeeds: <ovl>");
                } catch checkFailed(list[FailMessage] fms): /* continue with next overload */;
                  catch NoBinding(): /* continue with next overload */;
             }
             if(isEmpty(returnTypeForOverloadedActuals)) { s.report(error(current, "Constructor for %q with arguments %v cannot be resolved", adtName, actuals));}
             else return overloadedAType(returnTypeForOverloadedActuals);
        }
    }
    
    return computeADTReturnType(current, adtName, scope, retType, formalTypes, actuals, actualTypes, kwFormals, keywordArguments, identicalFormals, dontCare, isExpression, s);
}

AType computeADTReturnType(Tree current, str adtName, loc scope, AType retType, list[AType] formalTypes, actuals, list[AType] actualTypes, list[Keyword] kwFormals, keywordArguments, list[bool] identicalFormals, list[bool] dontCare, bool isExpression, Solver s){
    Bindings bindings = ();
    index_formals = index(formalTypes);
    for(int i <- index_formals, !dontCare[i]){
        try   bindings = matchRascalTypeParams(formalTypes[i], actualTypes[i], bindings, bindIdenticalVars=true);
        catch invalidMatch(str reason): 
              s.report(error(current, reason));   
    }
    iformals = formalTypes;
    if(!isEmpty(bindings)){
        try   iformals = [instantiateRascalTypeParams(formalTypes[i], bindings) | int i <- index_formals];
        catch invalidInstantiation(str msg):
              s.report(error(current, msg));
    }
    for(int i <- index_formals, !dontCare[i]){
        ai = actualTypes[i];
       //println("<i>: <ai>");
        if(!s.isFullyInstantiated(ai) && isExpression){
            if(identicalFormals[i]){
               s.requireUnify(ai, iformals[i], error(current, "Cannot unify %t with %t", ai, iformals[i]));
               ai = s.instantiate(ai);
               //println("instantiated <actuals[i]>: <ai>");
            } else
                continue;
        }
        //println("comparable?: <ai>, <iformals[i]>");
        //actuals_i = (list[Expression] actualsExp := actuals) ? actualsExp[i] : ((list[Pattern] actualsPat := actuals) ? actualsPat[i] : current);
        s.requireComparable(ai, iformals[i], error(current, "Argument %v should have type %t, found %t", i, formalTypes[i], ai));
    }
    adtType = s.getTypeInScopeFromName(adtName, scope, dataOrSyntaxRoles);
    
    switch(keywordArguments){
    case (KeywordArguments[Expression]) `<KeywordArguments[Expression] keywordArgumentsExp>`:
        checkExpressionKwArgs(kwFormals + getCommonKeywords(adtType, scope, s), keywordArgumentsExp, bindings, scope, s);
    case (KeywordArguments[Pattern]) `<KeywordArguments[Pattern] keywordArgumentsPat>`:
        checkPatternKwArgs(kwFormals + getCommonKeywords(adtType, scope, s), keywordArguments, bindings, scope, s);
    default:
        throw rascalCheckerInternalError("computeADTReturnType: illegal keywordArguments: <keywordArguments>");
    }
    
    list[AType] parameters = [];
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := adtType){
       params = {};
       for(<key, idr, tp> <- overloads){  
        if(isADTType(tp)) params += toSet(getADTTypeParameters(tp));
       }
       parameters = toList(params);
    } else {
        parameters = getADTTypeParameters(adtType);
    }
    for(p <- parameters){
        if(!bindings[p.pname]?) bindings[p.pname] = avoid();
    }
    if(!isEmpty(bindings)){
        try    return instantiateRascalTypeParams(s.getTypeInScopeFromName(adtName, scope, dataOrSyntaxRoles), bindings);
        catch invalidInstantiation(str msg):
               s.report(error(current, msg));
    }
   
    return adtType;
}

void checkExpressionKwArgs(list[Keyword] kwFormals, (KeywordArguments[Expression]) `<KeywordArguments[Expression] keywordArgumentsExp>`, Bindings bindings, loc scope, Solver s){
    if(keywordArgumentsExp is none) return;
 
   // if([,\ (\t\n] << {KeywordArgument[Expression] ","}+ keywordArgumentList := keywordArgumentsExp.keywordArgumentList){
        next_arg:
        for(kwa <- keywordArgumentsExp.keywordArgumentList){ 
            kwName = prettyPrintName(kwa.name);
            
            for(<ft, de> <- kwFormals){
               fn = ft.label;
               if(kwName == fn){
                  ift = ft;
                  if(!isEmpty(bindings)){
                      try   ift = instantiateRascalTypeParams(ft, bindings);
                      catch invalidInstantiation(str msg):
                            s.report(error(kwa, msg));
                  }
                  kwType = s.getType(kwa.expression);
                  s.requireComparable(kwType, ift, error(kwa, "Keyword argument %q has type %t, expected %t", kwName, kwType, ift));
                  continue next_arg;
               } 
            }
            availableKws = intercalateOr(["`<prettyAType(ft)> <ft.label>`" | < AType ft, Expression de> <- kwFormals]);
            switch(size(kwFormals)){
            case 0: availableKws ="; no other keyword parameters available";
            case 1: availableKws = "; available keyword parameter: <availableKws>";
            default:
                availableKws = "; available keyword parameters: <availableKws>";
            }
            
           s.report(error(kwa, "Undefined keyword argument %q%v", kwName, availableKws));
        }
   // }
} 
 
 void checkPatternKwArgs(list[Keyword] kwFormals, (KeywordArguments[Pattern]) `<KeywordArguments[Pattern] keywordArgumentsPat>`, Bindings bindings, loc scope, Solver s){
    if(keywordArgumentsPat is none) return;
    //if([,\ (\t\n] << {KeywordArgument[Pattern] ","}+ keywordArgumentList := keywordArgumentsPat.keywordArgumentList){
        next_arg:
        for(kwa <- keywordArgumentsPat.keywordArgumentList){ 
            kwName = prettyPrintName(kwa.name);
            
            for(<ft, de> <- kwFormals){
               fn = ft.label;
               if(kwName == fn){
                  ift = ft;
                  if(!isEmpty(bindings)){
                      try   ift = instantiateRascalTypeParams(ft, bindings);
                      catch invalidInstantiation(str msg):
                            s.report(error(kwa, msg));
                  }
                  kwType = getPatternType(kwa.expression, ift, scope, s);
                s.requireComparable(kwType, ift, error(kwa, "Keyword argument %q has type %t, expected %t", kwName, kwType, ift));
                  continue next_arg;
               } 
            }
            availableKws = intercalateOr(["`<prettyAType(ft)> <ft.label>`" | </*str fn,*/ AType ft, Expression de> <- kwFormals]);
            switch(size(kwFormals)){
            case 0: availableKws ="; no other keyword parameters available";
            case 1: availableKws = "; available keyword parameter: <availableKws>";
            default:
                availableKws = "; available keyword parameters: <availableKws>";
            }
            
            s.report(error(kwa, "Undefined keyword argument %q%v", kwName, availableKws));
        }
   // }
}
 
 AType computeExpressionNodeType(Tree current, loc scope, list[Expression]  actuals, (KeywordArguments[Expression]) `<KeywordArguments[Expression] keywordArgumentsExp>`, Solver s, AType subjectType=avalue()){                     
    actualType = [ s.getType(actuals[i]) | i <- index(actuals) ];
    return anode(computeExpressionKwArgs(keywordArgumentsExp, scope, s));
}

AType computePatternNodeType(Tree current, loc scope, list[Pattern] patList, (KeywordArguments[Pattern]) `<KeywordArguments[Pattern] keywordArgumentsPat>`, Solver s, AType subjectType){                     
    dontCare = [ "<patList[i]>" == "_" | i <- index(patList) ];
    actualType = [ dontCare[i] ? avalue() : getPatternType(patList[i], avalue(), scope, s) | i <- index(patList) ];
    
    if(adtType:aadt(adtName, list[AType] parameters,_) := subjectType){
       declaredInfo = s.getDefinitions(adtName, scope, dataOrSyntaxRoles);
       declaredType = s.getTypeInScopeFromName(adtName, scope, dataOrSyntaxRoles);
       checkPatternKwArgs(getCommonKeywords(adtType, scope, s), keywordArgumentsPat, (), scope, s);
       return subjectType;
    } else if(acons(adtType:aadt(adtName, list[AType] parameters, _), list[AType] fields, list[Keyword] kwFields) := subjectType){
       kwFormals = kwFields;
       checkPatternKwArgs(kwFormals + getCommonKeywords(adtType, scope, s), keywordArgumentsPat, (), scope, s);
       return anode([]);
    } else if(anode(list[AType] fields) := subjectType){
        return computePatternNodeTypeWithKwArgs(current, keywordArgumentsPat, fields, scope, s);
    } else if(avalue() := subjectType){
        return anode([]);
    }
    s.report(error(current, "Node pattern does not match %t", subjectType));
}

AType computeExpressionNodeTypeWithKwArgs(Tree current, (KeywordArguments[Expression]) `<KeywordArguments[Expression] keywordArgumentsExp>`, list[AType] fields, loc scope, Solver s){
    if(keywordArgumentsExp is none) return anode([]);
                       
    nodeFieldTypes = [];
    nextKW:
       for(ft <- fields){
           fn = ft.label;
           for(kwa <- keywordArgumentsExp.keywordArgumentList){ 
               kwName = prettyPrintName(kwa.name);
               if(kwName == fn){
                  kwType = s.getType(kwa.expression); //getPatternType(kwa.expression,ft, scope, s);
                  s.requireUnify(ft, kwType, error(current, "Cannot determine type of field %q", fn));
                  nodeFieldTypes += ft;
                  continue nextKW;
               }
           }    
       }
       return anode(nodeFieldTypes); 
}

AType computePatternNodeTypeWithKwArgs(Tree current, (KeywordArguments[Pattern]) `<KeywordArguments[Pattern] keywordArgumentsPat>`, list[AType] fields, loc scope, Solver s){
    if(keywordArgumentsPat is none) return anode([]);
                       
    nodeFieldTypes = [];
    nextKW:
       for(ft <- fields){
           fn = ft.label;
           for(kwa <- keywordArgumentsPat.keywordArgumentList){ 
               kwName = prettyPrintName(kwa.name);
               if(kwName == fn){
                  kwType = getPatternType(kwa.expression,ft, scope, s);
                  s.requireUnify(ft, kwType, error(current, "Cannot determine type of field %q", fn));
                  nodeFieldTypes += ft;
                  continue nextKW;
               }
           }    
       }
       return anode(nodeFieldTypes); 
 }


list[AType] computeExpressionKwArgs((KeywordArguments[Expression]) `<KeywordArguments[Expression] keywordArgumentsExp>`, loc scope, Solver s){
    if(keywordArgumentsExp is none) return [];
 
    return for(kwa <- keywordArgumentsExp.keywordArgumentList){ 
                kwName = prettyPrintName(kwa.name);
                kwType = s.getType(kwa.expression);
                append kwType[label=kwName];
            }  
}

list[AType] computePatternKwArgs((KeywordArguments[Pattern]) `<KeywordArguments[Pattern] keywordArgumentsPat>`, loc scope, Solver s){
    if(keywordArgumentsPat is none) return [];
 
    return for(kwa <- keywordArgumentsPat.keywordArgumentList){ 
                kwName = prettyPrintName(kwa.name);
                kwType = getPatternType(kwa.expression, avalue(), scope, s);
                append kwType[label=kwName];
            }
}
 
list[Keyword] getCommonKeywords(aadt(str adtName, list[AType] parameters, _), loc scope, Solver s) =
     [ <s.getType(kwf.\type)[label=prettyPrintName(kwf.name)], kwf.expression> | d <- s.getDefinitions(adtName, scope, dataOrSyntaxRoles), kwf <- d.defInfo.commonKeywordFields ];
     
list[Keyword] getCommonKeywords(overloadedAType(rel[loc, IdRole, AType] overloads), loc scope, Solver s) = [ *getCommonKeywords(adt, scope, s) | <def, idr, adt> <- overloads ];
default list[Keyword] getCommonKeywords(AType atype, loc scope, Solver s) = [];

// ---- tuple

void collect(current: (Expression) `\< <{Expression ","}+ elements1> \>`, Collector c){
    elms = [ e | Expression e <- elements1 ];
    c.calculateEager("tuple expression", current, elms,
        AType(Solver s) {
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
                return amap(s.lubList([ s.getType(f) | f <- froms ]), lubList([ s.getType(t) | t <- tos ]));
            });
        collect(mappings, c);
    }
}

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
       c.useQualified([qualifier, base], name, {variableId(), functionId(), constructorId()}, dataOrSyntaxRoles + {moduleId()} );
    } else {
       if(base != "_"){
          if(inPatternScope(c)){
            c.use(name, {variableId(), formalId(), nestedFormalId(), patternVariableId(), keywordFormalId(), fieldId(), keywordFieldId(), functionId(), constructorId()});
          } else {
            c.useLub(name, {variableId(), formalId(), nestedFormalId(), patternVariableId(), keywordFormalId(), fieldId(), keywordFieldId(), functionId(), constructorId()});
          }
       } else {
          c.fact(current, avalue());
       }
    }
}

//void collect(current: (Expression) `*<QualifiedName name>`, Collector c){
//    c.calculateEager("*<name>", current, [name], AType(Solver s){
//        nameType = s.getType(name);
//        return isSetType(nameType) ? getSetElementType(nameType) : getListElementType(nameType);
//    });
//    collect(name, c);
//}

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
                  AType(Solver s){ return computeSubscriptionType(current, s.getType(expression), [s.getType(e) | e <- indexList], indexList, s);  });
    collect(expression, indices, c);
}

AType computeSubscriptionType(Tree current, AType t1, list[AType] tl, list[Expression] indexList, Solver s){
    if(!s.isFullyInstantiated(t1)) throw TypeUnavailable();
    for(tp <- tl){
        if(!s.isFullyInstantiated(tp)) throw TypeUnavailable();
    }

    if(overloadedAType(rel[loc, IdRole, AType] overloads) := t1){
        //println("computeSubscriptionType: <current>, <t1>");
        subscript_overloads = {};
        for(<key, role, tp> <- overloads){
            try {
                subscript_overloads += <key, role, computeSubscriptionType(current, tp, tl, indexList, s)>;
            } catch checkFailed(list[FailMessage] fms):/* continue with next overload */;
              catch NoBinding(): /* continue with next overload */;
 //>>>        catch e: /* continue with next overload */;
        }
        //println("computeSubscriptionType: <current>, <t1>, <tl> ==\> <overloadedAType(subscript_overloads)>");
        if(isEmpty(subscript_overloads)) s.report(error(current, "Expressions of type %t cannot be subscripted", t1));
        return overloadedAType(subscript_overloads);
    } else if (isListType(t1) && (!isListRelType(t1) || (isListRelType(t1) && size(tl) == 1 && isIntType(tl[0])))) {
        // TODO: At some point we should have separate notation for this, but this final condition treats list
        // relations indexed by one int value as lists, making this an index versus a projection
        if (size(tl) != 1)
            s.report(error(current, "Expected only 1 subscript for a list expression, not %v", size(tl)));
        else if (!isIntType(tl[0]))
            s.report(error(current, "Expected subscript of type int, not %t", tl[0]));
        else
            return getListElementType(t1);
    } else if (isRelType(t1)) {
        if (size(tl) >= size(getRelFields(t1)))
            s.report(error(current, "For a relation with arity %v you can have at most %v subscripts", size(getRelFields(t1)), size(getRelFields(t1))-1));
        else {
            relFields = getRelFields(t1);
            failures = [ error(indexList[idx], "At subscript %v, subscript type %t must be comparable to relation field type %t",  idx+1, tl[idx], relFields[idx])
                       | idx <- index(tl), ! (comparable(tl[idx],relFields[idx]) || comparable(tl[idx],makeSetType(relFields[idx]))) 
                       ];
            if (size(failures) > 0) {
                s.reports(failures);
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
            s.report(error(current, "For a list relation with arity %v you can have at most %v subscripts", size(getListRelFields(t1)), size(getListRelFields(t1))-1));
        else {
            relFields = getListRelFields(t1);
            failures = [ error(indexList[idx], "At subscript %v, subscript type %t must be comparable to relation field type %t", idx+1, tl[idx], relFields[idx]) 
                       | idx <- index(tl), ! (comparable(tl[idx],relFields[idx]) || comparable(tl[idx],makeSetType(relFields[idx]))) 
                       ];
            if (size(failures) > 0) {
                s.reports(failures);
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
            s.report(error(current, "Expected only 1 subscript for a map expression, not %v", size(tl)));
        else if (!comparable(tl[0],getMapDomainType(t1)))
            s.report(error(current, "Expected subscript of type %t, not %t", getMapDomainType(t1),tl[0]));
        else
            return getMapRangeType(t1);
    } else if (isNodeType(t1)) {
        if (size(tl) != 1)
            s.report(error(current, "Expected only 1 subscript for a node expression, not %v", size(tl)));
        else if (!isIntType(tl[0]))
            s.report(error(current, "Expected subscript of type `int`, not %t", tl[0]));
        else
            return avalue();
    } else if (isTupleType(t1)) {
        if (size(tl) != 1) {
            s.report(error(current, "Expected only 1 subscript for a tuple expression, not %v", size(tl)));
        } else if (!isIntType(tl[0])) {
            s.report(error(current, "Expected subscript of type `int`, not %v", tl[0]));
        } else if ((Expression)`<DecimalIntegerLiteral dil>` := head(indexList)) {
            tupleIndex = toInt("<dil>");
            if (tupleIndex < 0 || tupleIndex >= size(getTupleFields(t1))) {
               s.report(error(current, "Tuple index must be between 0 and %v", size(getTupleFields(t1))-1));
            } else {
                return getTupleFields(t1)[tupleIndex];
            }
        } else {
            return lubList(getTupleFields(t1));
        }
    } else if (isStrType(t1)) {
        if (size(tl) != 1)
            s.report(error(current, "Expected only 1 subscript for a string expression, not %v", size(tl)));
        else if (!isIntType(tl[0]))
            s.report(error(current, "Expected subscript of type `int`, not %t", tl[0]));
        else
            return astr();
    } else if (isNonTerminalType(t1)) {
        if (size(tl) != 1)
            s.report(error(current, "Expected only 1 subscript for a nonterminal subscript expression, not %v", size(tl)));
        else if (!isIntType(tl[0]))
            s.report(error(current, "Expected subscript of type `int`, not %t", tl[0]));
        else if (isNonTerminalIterType(t1))
            return getNonTerminalIterElement(t1);
        else
            return makeADTType("Tree");    
    } else {
        s.report(error(current, "Expressions of type %t cannot be subscripted", t1));
    }
}

// ---- slice

void collect(current: (Expression) `<Expression e> [ <OptionalExpression ofirst> .. <OptionalExpression olast> ]`, Collector c){
    if(ofirst is noExpression) c.fact(ofirst, aint());
    if(olast is noExpression) c.fact(olast, aint());

    c.calculate("slice", current, [e, ofirst, olast],
        AType(Solver s){ return computeSliceType(current, s.getType(e), s.getType(ofirst), aint(), s.getType(olast), s); });
    collect(e, ofirst, olast, c);
}

AType computeSliceType(Tree current, AType base, AType first, AType step, AType last, Solver s){

    if(!s.isFullyInstantiated(base)) throw TypeUnavailable();
    if(!s.isFullyInstantiated(first)) throw TypeUnavailable(); 
    if(!s.isFullyInstantiated(step)) throw TypeUnavailable();
    if(!s.isFullyInstantiated(last)) throw TypeUnavailable();
    
     if(overloadedAType(rel[loc, IdRole, AType] overloads) := base){
        slice_overloads = {};
        for(<key, role, tp> <- overloads){
            try {
                slice_overloads += <key, role, computeSliceType(current, tp, first, step, last, s)>;
            } catch checkFailed(list[FailMessage] fms): /* continue with next overload */;
              catch NoBinding(): /* continue with next overload */;
 //>>         catch e: /* continue with next overload */; 
        }
        if(isEmpty(slice_overloads)) s.report(error(current, "Slice cannot be computed for %t", base));
        return overloadedAType(slice_overloads);
    }
    
    failures = [];
    if(!isIntType(first)) failures += error(current, "The first slice index must be of type `int`, found %t", first);
    if(!isIntType(step)) failures  += error(current, "The slice step must be of type `int`, found %t", step);
    if(!isIntType(last)) failures  += error(current, "The last slice index must be of type `int`, found %t", last);
    
    if(!isEmpty(failures)) throw s.reports(failures);
    
    if (isListType(base) || isStrType(base) || isNonTerminalIterType(base)) {
        return base;
    } else if (isNodeType(base)) {
        return makeListType(avalue());
    }
    
    s.reports(failures + error(current, "Slices can only be used on (concrete) lists, strings, and nodes, found %t", base));
}

// ---- sliceStep

void collect(current: (Expression) `<Expression e> [ <OptionalExpression ofirst>, <Expression second> .. <OptionalExpression olast> ]`, Collector c){
    if(ofirst is noExpression) c.fact(ofirst, aint());
    if(olast is noExpression) c.fact(olast, aint());

    c.calculate("slice step", current, [e, ofirst, second, olast],
        AType(Solver s){ return computeSliceType(current, s.getType(e), s.getType(ofirst), s.getType(second), s.getType(olast), s); });
    collect(e, ofirst, second, olast, c);
}

// ---- fieldAccess

void collect(current: (Expression) `<Expression expression> . <Name field>`, Collector c){
    c.useViaType(expression, field, {fieldId(), keywordFieldId()});
    c.fact(current, field);
    collect(expression, c);
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
    
public AType computeFieldTypeWithADT(AType containerType, Tree field, loc scope, Solver s) {
    //println("computeFieldTypeWithADT: <containerType>, <field>");
    fieldName = unescape("<field>");
    if(isNonTerminalType(containerType) && fieldName == "top"){
        return containerType;
    }
    return s.getTypeInType(containerType, field, {fieldId(), keywordFieldId()}, scope);
}
    
@doc{Compute the type of field fn on type containerType. A checkFailed is thrown if the field is not defined on the given type.}
public AType computeFieldType(AType containerType, Tree field, loc scope, Solver s) {
    //println("computeFieldType: <containerType>, <field>");
   
    if(!s.isFullyInstantiated(containerType)) throw TypeUnavailable();
    fieldName = unescape("<field>");
    
   if (isReifiedType(containerType)) {
        if(s.getConfig().classicReifier){
            if (fieldName == "symbol") {
                return s.getTypeInScopeFromName("Symbol", scope, {dataId()});
            } else if (fieldName == "definitions") {
               s.getTypeInScopeFromName("Symbol", scope, {dataId()});
                      
               s.getTypeInScopeFromName("Production", scope, {dataId()});
               return makeMapType(makeADTType("Symbol"), makeADTType("Production"));
                    
            } else {
               s.report(error(field, "Field %q does not exist on type `type` (classic reifier)", fieldName));
            }
         } else {
            if (fieldName == "symbol") {
                return s.getTypeInScopeFromName("AType", scope, {dataId()});
            } else if (fieldName == "definitions") {
               s.getTypeInScopeFromName("AType", scope, {dataId()});
               s.getTypeInScopeFromName("AProduction", scope, {dataId()});
               return makeMapType(makeADTType("AType"), makeADTType("AProduction"));
                    
            } else {
               s.report(error(field, "Field %q does not exist on type `type`  (non-classic reifier)", fieldName));
            }
         }
    } else if(isNonTerminalType(containerType)){
        if(isStartNonTerminalType(containerType)){
           return computeFieldTypeWithADT(getStartNonTerminalType(containerType), field, scope, s);
        } else if(isNonTerminalIterType(containerType)){
            if(containerType.label == fieldName){
                return makeListType(getNonTerminalIterElement(containerType));
            }
        } else if(isNonTerminalSeqType(containerType)){
            for(tp <- getNonTerminalSeqTypes(containerType)){
                if(tp.label == fieldName){
                    return tp;
                }
            }
        } else if(isNonTerminalAltType(containerType)){
            for(tp <- getNonTerminalAltTypes(containerType)){
                if(tp.label == fieldName){
                    return tp;
                }
            }
        }
    } else if (isTupleType(containerType)) {
        if(tupleHasFieldNames(containerType)){
            idx = indexOf(getTupleFieldNames(containerType), fieldName);
            if(idx >= 0)
                return getTupleFieldTypes(containerType)[idx];
            else
                s.report(error(field, "Field %q does not exist on type %t", fieldName, containerType));
        } else {
            s.report(error(field, "Field %q does not exist on type %t", fieldName, containerType));
        }
    } else if (isLocType(containerType)) {
        if (fieldName in fieldMap[aloc()])
            return fieldMap[aloc()][fieldName];
        else
            s.report(error(field, "Field %q does not exist on type %t", fieldName, containerType));
    } else if (isDateTimeType(containerType)) {
        if (fieldName in fieldMap[adatetime()])
            return fieldMap[adatetime()][fieldName];
        else
           s.report(error(field, "Field %q does not exist on type %t", fieldName, containerType));
    } else if (isRelType(containerType)) {
        idx = indexOf(getRelFieldNames(containerType), fieldName);
        if(idx >= 0){
            return makeSetType(getRelFields(containerType)[idx]); 
        }
        else
           s.report(error(field, "Field %q does not exist on type %t", fieldName, containerType));
    } else if (isListRelType(containerType)) {
        idx = indexOf(getListRelFieldNames(containerType), fieldName);
        if(idx >= 0){
            return makeListType(getListRelFields(containerType)[idx]); 
        }
        else
           s.report(error(field, "Field %q does not exist on type %t", fieldName, containerType));
    } else if (isMapType(containerType)) {
           idx = indexOf(getMapFieldNames(containerType), fieldName);
        if(idx >= 0){
            return idx == 0 ? makeSetType(getMapDomainType(containerType)) : makeSetType(getMapRangeType(containerType));
        }
        else
            s.report(error(field, "Field %q does not exist on type %t", fieldName, containerType));
    
    } else if (isNodeType(containerType)) {
        return avalue();
    } 
    s.report(error(field, "computeFieldType: Cannot access fields on type %t", containerType));
}

// ---- fieldUpdate

void collect(current:(Expression) `<Expression expression> [ <Name field> = <Expression repl> ]`, Collector c){
    scope = c.getScope();
    //c.use(field, {fieldId(), keywordFieldId()});
    c.calculate("field update of `<field>`", current, [expression, repl],
        AType(Solver s){ 
                 fieldType = computeFieldTypeWithADT(s.getType(expression), field, scope, s);
                 replType = s.getType(repl);
                 s.requireSubType(replType, fieldType, error(current, "Cannot assign type %t to field %q of type %t", replType, field, fieldType));
                 return s.getType(expression);
        });
    collect(expression, repl, c);
}

// ---- fieldProjection

void collect(current:(Expression) `<Expression expression> \< <{Field ","}+ fields> \>`, Collector c){

    flds = [f | f <- fields];
    c.calculate("field projection", current, [expression],
        AType(Solver s){ return computeFieldProjectionType(current, s.getType(expression), flds, s); });
    //collectParts(current, c);
    collect(expression, fields, c);
}

void collect(current:(Field) `<IntegerLiteral il>`, Collector c){
    c.fact(current, aint());
}

void collect(current:(Field) `<Name fieldName>`, Collector c){

}

AType computeFieldProjectionType(Expression current, AType base, list[lang::rascal::\syntax::Rascal::Field] fields, Solver s){

    if(!s.isFullyInstantiated(base)) throw TypeUnavailable();
    
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := base){
        projection_overloads = {};
        for(<key, role, tp> <- overloads){
            try {
                projection_overloads += <key, role, computeFieldProjectionType(current, tp, fields, s)>;
            } catch checkFailed(list[FailMessage] fms): /* continue with next overload */;
              catch NoBinding(): /* continue with next overload */;
//>>>         catch e: /* continue with next overload */;
        }
        if(isEmpty(projection_overloads))  s.report(error(current, "Illegal projection %t", base));
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
    
    s.report(error(current, "Illegal projection %t", base));  
}

// ---- setAnnotation

void collect(current:(Expression) `<Expression e> [ @ <Name n> = <Expression er> ]`, Collector c) {
    c.use(n, {annoId()});
    scope = c.getScope();
    c.calculate("set annotation", current, [e, n, er],
        AType(Solver s){ t1 = s.getType(e); tn = s.getType(n); t2 = s.getType(er);
                 return computeSetAnnotationType(current, t1, tn, t2, s);
               });
    collect(e, er, c);
}

AType computeSetAnnotationType(Tree current, AType t1, AType tn, AType t2, Solver s)
    = ternaryOp("set annotation", _computeSetAnnotationType, current, t1, tn, t2, s);

private AType _computeSetAnnotationType(Tree current, AType t1, AType tn, AType t2, Solver s){
    if (isNodeType(t1) || isADTType(t1) || isNonTerminalType(t1)) {
        if(aanno(_, onType, annoType) := tn){
          s.requireSubType(t2, annoType, error(current, "Cannot assign value of type %t to annotation of type %t", t2, annoType));
           return t1;
        } else
            s.report(error(current, "Invalid annotation type: %t", tn));
    } else
        s.report(error(current, "Invalid type: expected node, ADT, or concrete syntax types, found %t", t1));
}

// ---- getAnnotation

void collect(current:(Expression) `<Expression e>@<Name n>`, Collector c) {
    c.use(n, {annoId()});
    scope = c.getScope();
    c.calculate("get annotation", current, [e, n],
        AType(Solver s){ 
                 t1 = s.getType(e);
                 tn = s.getType(n);
                 return computeGetAnnotationType(current, t1, tn, s);
               });
    collect(e, c);
}
AType computeGetAnnotationType(Tree current, AType t1, AType t2, Solver s)
    = binaryOp("get annotation", _computeGetAnnotationType, current, t1, t2, s);
    
private AType _computeGetAnnotationType(Tree current, AType t1, AType tn, Solver s){
    if (isNodeType(t1) || isADTType(t1) || isNonTerminalType(t1)) {
        if(aanno(_, onType, annoType) := tn){
           return annoType;
        } else
            s.report(error(current, "Invalid annotation type: %t", tn));
    } else
        s.report(error(current, "Invalid type: expected node, ADT, or concrete syntax types, found %t", t1));
}
