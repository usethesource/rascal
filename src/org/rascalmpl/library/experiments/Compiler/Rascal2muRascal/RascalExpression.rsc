@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalExpression
import Prelude;

import lang::rascal::\syntax::Rascal;

import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;
import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractType;
import lang::rascal::types::TypeInstantiation;
import lang::rascal::types::TypeExceptions;

import experiments::Compiler::Rascal2muRascal::TmpAndLabel;
import experiments::Compiler::Rascal2muRascal::RascalModule;
import experiments::Compiler::Rascal2muRascal::RascalPattern;
import experiments::Compiler::Rascal2muRascal::RascalStatement;
import experiments::Compiler::Rascal2muRascal::RascalType;
import experiments::Compiler::Rascal2muRascal::TypeReifier;

import experiments::Compiler::muRascal::AST;

import experiments::Compiler::Rascal2muRascal::TypeUtils;
import experiments::Compiler::RVM::Interpreter::ParsingTools;


int size_exps({Expression ","}* es) = size([e | e <- es]);		     // TODO: should become library function
int size_exps({Expression ","}+ es) = size([e | e <- es]);		     // TODO: should become library function
int size_assignables({Assignable ","}+ es) = size([e | e <- es]);	 // TODO: should become library function

MuExp generateMu("ALL", list[MuExp] exps, list[bool] backtrackfree) {
    str fuid = topFunctionScope();
    str all_uid = "Library/<fuid>/ALL_<getNextAll()>(0)";
    localvars = [ muVar("c_<i>", all_uid, i)| int i <- index(exps) ];
    list[MuExp] body = [ muYield() ];
    for(int i <- index(exps)) {
        int j = size(exps) - 1 - i;
        if(backtrackfree[j]) {
            body = [ muIfelse(nextLabel(), exps[j], body, [ muCon(222) ]) ];
        } else {
            body = [ muAssign("c_<j>", all_uid, j, muInit(exps[j])), muWhile(nextLabel(), muNext(localvars[j]), body), muCon(222) ];
        }
    }
    body = [ muGuard(muCon(true)) ] + body + [ muExhaust() ];
    functions_in_module += muCoroutine(all_uid, fuid, 0, size(localvars), [], muBlock(body));
    return muMulti(muCreate(muFun(all_uid)));
}

MuExp generateMu("OR", list[MuExp] exps, list[bool] backtrackfree) {
    str fuid = topFunctionScope();
    str or_uid = "Library/<fuid>/Or_<getNextOr()>(0)";
    localvars = [ muVar("c_<i>", or_uid, i)| int i <- index(exps) ];
    list[MuExp] body = [];
    for(int i <- index(exps)) {
        if(backtrackfree[i]) {
            body += muIfelse(nextLabel(), exps[i], [ muYield() ], [ muCon(222) ]);
        } else {
            body = body + [ muAssign("c_<i>", or_uid, i, muInit(exps[i])), muWhile(nextLabel(), muNext(localvars[i]), [ muYield() ]), muCon(222) ];
        }
    }
    body = [ muGuard(muCon(true)) ] + body + [ muExhaust() ];
    functions_in_module += muCoroutine(or_uid, fuid, 0, size(localvars), [], muBlock(body));
    return muMulti(muCreate(muFun(or_uid)));
}

// Produces multi- or backtrack-free expressions
/*
 * Reference implementation that uses the generic definitions of ALL and OR  
 */
/*
MuExp makeMu(str muAllOrMuOr, [ e:muMulti(_) ]) = e;
MuExp makeMu(str muAllOrMuOr, [ e:muOne(MuExp exp) ]) = makeMuMulti(e);
MuExp makeMu(str muAllOrMuOr, [ MuExp e ]) = e when !(muMulti(_) := e || muOne(_) := e);
default MuExp makeMu(str muAllOrMuOr, list[MuExp] exps) {
    assert(size(exps) >= 1);
    if(MuExp exp <- exps, muMulti(_) := exp) { // Multi expression
        return muMulti(muCreate(mkCallToLibFun("Library",muAllOrMuOr,1),
                                [ muCallMuPrim("make_array",[ { str fuid = topFunctionScope();
    															str gen_uid = "<fuid>/LAZY_EVAL_GEN_<nextLabel()>(0)";
                                                                functions_in_module += muFunction(gen_uid, Symbol::\func(Symbol::\value(),[]), fuid, 0, 0, false, |rascal:///|, [], (), muReturn(makeMuMulti(exp).exp));
                                                                muFun(gen_uid,fuid);
                                                              } | MuExp exp <- exps ]) ]));
    }
    if(muAllOrMuOr == "ALL") {
        return ( exps[0] | muIfelse(nextLabel(), it, [ exps[i] is muOne ? muNext(muInit(exps[i])) : exps[i] ], [ muCon(false) ]) | int i <- [ 1..size(exps) ] );
    } 
    if(muAllOrMuOr == "OR"){
        return ( exps[0] | muIfelse(nextLabel(), it, [ muCon(true) ], [ exps[i] is muOne ? muNext(muInit(exps[i])) : exps[i] ]) | int i <- [ 1..size(exps) ] );
    }
}
*/
/*
 * Alternative, fast implementation that generates a specialized definitions of ALL and OR
 */
MuExp makeMu(str muAllOrMuOr, [ e:muMulti(_) ]) = e;
MuExp makeMu(str muAllOrMuOr, [ e:muOne(MuExp exp) ]) = makeMuMulti(e);
MuExp makeMu(str muAllOrMuOr, [ MuExp e ]) = e when !(muMulti(_) := e || muOne(_) := e);
default MuExp makeMu(str muAllOrMuOr, list[MuExp] exps) {
    assert(size(exps) >= 1);
    if(MuExp exp <- exps, muMulti(_) := exp) { // Multi expression
        list[MuExp] expressions = [];
        list[bool] backtrackfree = [];
        for(MuExp e <- exps) {
            if(muMulti(_) := e) {
                expressions += e.exp;
                backtrackfree += false;
            } else if(muOne(_) := e) {
                expressions += muNext(muInit(e.exp));
                backtrackfree += true;
            } else {
                expressions += e;
                backtrackfree += true;
            }
        }
        return generateMu(muAllOrMuOr, expressions, backtrackfree);
    }
    if(muAllOrMuOr == "ALL") {
        return ( exps[0] | muIfelse(nextLabel(), it, [ exps[i] is muOne ? muNext(muInit(exps[i])) : exps[i] ], [ muCon(false) ]) | int i <- [ 1..size(exps) ] );
    } 
    if(muAllOrMuOr == "OR"){
        return ( exps[0] | muIfelse(nextLabel(), it, [ muCon(true) ], [ exps[i] is muOne ? muNext(muInit(exps[i])) : exps[i] ]) | int i <- [ 1..size(exps) ] );
    }
}

MuExp makeMuMulti(e:muMulti(_)) = e;
// TODO: should it (ONE) be also passed a closure? I would say yes
MuExp makeMuMulti(e:muOne(MuExp exp)) = muMulti(muCreate(mkCallToLibFun("Library","ONE",1),[ exp ])); // ***Note: multi expression that produces at most one solution
default MuExp makeMuMulti(MuExp exp) {
    // Works because mkVar and mkAssign produce muVar and muAssign, i.e., specify explicitly function scopes computed by the type checker
    str fuid = topFunctionScope();
    str gen_uid = "<fuid>/GEN_<nextLabel()>(0)";
    functions_in_module += muCoroutine(gen_uid, fuid, 0, 0, [], muBlock([ muGuard(muCon(true)), muIfelse(nextLabel(), exp, [ muReturn() ], [ muExhaust() ]) ]));
    return muMulti(muCreate(muFun(gen_uid)));
}

MuExp makeMuOne(str muAllOrMuOr, [ e:muMulti(MuExp exp) ]) = muOne(exp);
MuExp makeMuOne(str muAllOrMuOr, [ e:muOne(MuExp exp) ]) = e;
MuExp makeMuOne(str muAllOrMuOr, [ MuExp e ]) = e when !(muMulti(_) := e || muOne(_) := e);
default MuExp makeMuOne(str muAllOrMuOr, list[MuExp] exps) {
    MuExp e = makeMu(muAllOrMuOr,exps);
    if(muMulti(exp) := e) {
        return muOne(exp);
    }
    return e;
}

bool isMulti(muMulti(_)) = true;
default bool isMulti(MuExp _) = false;

// Generate code for completely type-resolved operators

bool isContainerType(str t) = t in {"list", "map", "set", "rel", "lrel"};

bool areCompatibleContainerTypes({"list", "lrel"}) = true;
bool areCompatibleContainerTypes({"set", "rel"}) = true;
bool areCompatibleContainerTypes({str c}) = true;
default bool areCompatibleContainerTypes(set[str] s) = false;

str reduceContainerType("lrel") = "list";
str reduceContainerType("rel") = "set";
default str reduceContainerType(str c) = c;


str typedBinaryOp(str lot, str op, str rot) {
  //lot = reduceContainerType(lot);
  //rot = reduceContainerType(rot);
  if(lot == "value" || rot == "value" || lot == "parameter" || rot == "parameter"){
     return op;
  }
  if(isContainerType(lot))
     return areCompatibleContainerTypes({lot, rot}) ? "<lot>_<op>_<rot>" : "<lot>_<op>_elm";
  else
     return isContainerType(rot) ? "elm_<op>_<rot>" : "<lot>_<op>_<rot>";
}

MuExp infix(str op, Expression e) = 
  muCallPrim(typedBinaryOp(getOuterType(e.lhs), op, getOuterType(e.rhs)), 
             [*translate(e.lhs), *translate(e.rhs)]);

MuExp infix_elm_left(str op, Expression e){
   rot = getOuterType(e.rhs);
   //rot = reduceContainerType(rot);
   return muCallPrim("elm_<op>_<rot>", [*translate(e.lhs), *translate(e.rhs)]);
}

MuExp infix_rel_lrel(str op, Expression e){
  lot = getOuterType(e.lhs);
  if(lot == "set") lot = "rel"; else if (lot == "list") lot = "lrel";
  rot = getOuterType(e.rhs);
  if(rot == "set") rot = "rel"; else if (rot == "list") rot = "lrel";
  return muCallPrim("<lot>_<op>_<rot>", [*translate(e.lhs), *translate(e.rhs)]);
}

str typedUnaryOp(str ot, str op) = (ot == "value" || ot == "parameter") ? op : "<op>_<ot>";
 
MuExp prefix(str op, Expression arg) {
  return muCallPrim(typedUnaryOp(getOuterType(arg), op), [translate(arg)]);
}

MuExp postfix(str op, Expression arg) = muCallPrim(typedUnaryOp(getOuterType(arg), op), [translate(arg)]);

MuExp postfix_rel_lrel(str op, Expression arg) {
  ot = getOuterType(arg);
  if(ot == "set" ) ot = "rel"; else if(ot == "list") ot = "lrel";
  return muCallPrim("<ot>_<op>", [translate(arg)]);
}

set[str] numeric = {"int", "real", "rat", "num"};

MuExp comparison(str op, Expression e) {
  lot = reduceContainerType(getOuterType(e.lhs));
  rot = reduceContainerType(getOuterType(e.rhs));
  //println("comparison: op = <op>, lot = <lot>, rot = <rot>");
  if(lot == "value" || rot == "value"){
     lot = ""; rot = "";
  } else {
    if(lot in numeric) lot += "_"; else lot = "";
 
    if(rot in numeric) rot = "_" + rot; else rot = "";
  }
  lot = reduceContainerType(lot);
  rot = reduceContainerType(rot);
  return muCallPrim("<lot><op><rot>", [*translate(e.lhs), *translate(e.rhs)]);
}

// Determine constant expressions

bool isConstantLiteral((Literal) `<LocationLiteral src>`) = src.protocolPart is nonInterpolated;
bool isConstantLiteral((Literal) `<StringLiteral n>`) = n is nonInterpolated;
default bool isConstantLiteral(Literal l) = true;

// TODO Add map constants

bool isConstant(Expression e:(Expression)`{ <{Expression ","}* es> }`) = size_exps(es) == 0 || all(elm <- es, isConstant(elm));
bool isConstant(Expression e:(Expression)`[ <{Expression ","}* es> ]`)  = size_exps(es) == 0 ||  all(elm <- es, isConstant(elm));
bool isConstant(e:(Expression) `\< <{Expression ","}+ elements> \>`) = size_exps(elements) == 0 ||  all(elm <- elements, isConstant(elm));
bool isConstant((Expression) `<Literal s>`) = isConstantLiteral(s);
default bool isConstant(Expression e) = false;

value getConstantValue(Expression e) {
  //println("getConstant: <e>");
  return readTextValueString("<e>");
}


/*********************************************************************/
/*                  Expressions                                       */
/*********************************************************************/

// literals

MuExp translate((Literal) `<BooleanLiteral b>`) = "<b>" == "true" ? muCon(true) : muCon(false);
 
MuExp translate((Literal) `<IntegerLiteral n>`) = muCon(toInt("<n>"));

MuExp translate((Literal) `<RegExpLiteral r>`) { throw "RexExpLiteral cannot occur in expression"; }

MuExp translate((Literal) `<StringLiteral n>`) = translateStringLiteral(n);

MuExp translate((Literal) `<LocationLiteral src>`) = translateLocationLiteral(src);

default MuExp translate((Literal) `<Literal s>`) =  muCon(readTextValueString("<s>"));

MuExp translate(e:(Expression)  `<Literal s>`) = translate(s);

// Other expressions

// Concrete
MuExp translate(e:(Expression) `<Concrete concrete>`) {
  return translateConcrete(concrete);
}

MuExp getConstructor(str cons) {
   uid = -1;
   for(c <- constructors){
     //println("c = <c>, uid2name = <uid2name[c]>, uid2str = <uid2str(c)>");
     if(cons == getSimpleName(config.store[c].name)){
        //println("c = <c>, <config.store[c]>,  <uid2addr[c]>");
        uid = c;
        break;
     }
   }
   if(uid < 0)
      throw("No definition for constructor: <cons>");
   return muConstr(fuid2str[uid]);
}
/*
data Tree 
     = appl(Production prod, list[Tree] args)
     | cycle(Symbol symbol, int cycleLength) 
     | amb(set[Tree] alternatives)  
     | char(int character)
     ;
     
lexical Concrete 
  = typed: "(" LAYOUTLIST l1 Sym symbol LAYOUTLIST l2 ")" LAYOUTLIST l3 "`" ConcretePart* parts "`";

lexical ConcretePart
  = @category="MetaSkipped" text   : ![`\<\>\\\n]+ !>> ![`\<\>\\\n]
  | newline: "\n" [\ \t \u00A0 \u1680 \u2000-\u200A \u202F \u205F \u3000]* "\'"
  | @category="MetaVariable" hole : ConcreteHole hole
  | @category="MetaSkipped" lt: "\\\<"
  | @category="MetaSkipped" gt: "\\\>"
  | @category="MetaSkipped" bq: "\\`"
  | @category="MetaSkipped" bs: "\\\\"
  ;
  
syntax ConcreteHole 
  = \one: "\<" Sym symbol Name name "\>"
  ;
*/

//MuExp translateConcrete(e:(ConcreteHole) `\< <Sym symbol> <Name name> \>`){
//  println("***** ConcreteHole, name: <name>");
//  iprint(e);
//  return muCallPrim("list_subscript_int", [muCallPrim("adt_field_access", [mkVar("<name>", name@\loc), muCon("args")]), muCon(7)]);
//}

default MuExp translateConcrete(e: appl(Production cprod, list[Tree] cargs)){   
    Tree parsedFragment = parseFragment(getModuleName(), e, e@\loc, getGrammar(config));
    return translateConcreteParsed(parsedFragment);
}

default MuExp translateConcrete(t) = muCon(t);

MuExp translateConcreteParsed(e: appl(Production prod, list[Tree] args)){
   if(prod.def == label("hole", lex("ConcretePart"))){
       varloc = args[0].args[4].args[0]@\loc;		// TODO: refactor (see concrete patterns)
       <fuid, pos> = getVariableScope("ConcreteVar", varloc);
       return muVar("ConcreteVar", fuid, pos);
    }    
    return muCall(muConstr("ParseTree/adt(\"Tree\",[])::appl(adt(\"Production\",[]) prod;list(adt(\"Tree\",[])) args;)"), 
                   [muCon(prod), muCallPrim("list_create", [translateConcreteParsed(arg) | arg <- args])]);
}

default MuExp translateConcreteParsed(Tree t) = muCon(t);

// Block
MuExp translate(e:(Expression) `{ <Statement+ statements> }`) = muBlock([translate(stat) | stat <- statements]);

// Parenthesized expression
MuExp translate(e:(Expression) `(<Expression expression>)`)   = translate(expression);

// Closure
MuExp translate (e:(Expression) `<Type \type> <Parameters parameters> { <Statement+ statements> }`) = translateClosure(e, parameters, statements);

MuExp translate (e:(Expression) `<Parameters parameters> { <Statement* statements> }`) = translateClosure(e, parameters, statements);

// Enumerator with range

MuExp translate (e:(Expression) `<Pattern pat> \<- [ <Expression first> .. <Expression last> ]`) {
    kind = getOuterType(first) == "int" && getOuterType(last) == "int" ? "_INT" : "";
    return muMulti(muCreate(mkCallToLibFun("Library", "RANGE<kind>", 3), [ translatePat(pat), translate(first), translate(last)]));
 }
    
MuExp translate (e:(Expression) `<Pattern pat> \<- [ <Expression first> , <Expression second> .. <Expression last> ]`) {
     kind = getOuterType(first) == "int" && getOuterType(second) == "int" && getOuterType(last) == "int" ? "_INT" : "";
     return muMulti(muCreate(mkCallToLibFun("Library", "RANGE_STEP<kind>", 4), [ translatePat(pat), translate(first), translate(second), translate(last)]));
}

// Range
MuExp translate (e:(Expression) `[ <Expression first> .. <Expression last> ]`) {
  str fuid = topFunctionScope();
  loopname = nextLabel(); 
  writer = asTmp(loopname);
  var = nextTmp();
  patcode = muCreate(mkCallToLibFun("Library","MATCH_VAR",2), [muTmpRef(var,fuid)]);

  kind = getOuterType(first) == "int" && getOuterType(last) == "int" ? "_INT" : "";
  rangecode = muMulti(muCreate(mkCallToLibFun("Library", "RANGE<kind>", 3), [ patcode, translate(first), translate(last)]));
  
  return
    muBlock(
    [ muAssignTmp(writer, fuid, muCallPrim("listwriter_open", [])),
      muWhile(loopname, makeMu("ALL", [ rangecode ]), [ muCallPrim("listwriter_add", [muTmp(writer,fuid), muTmp(var,fuid)])]),
      muCallPrim("listwriter_close", [muTmp(writer,fuid)]) 
    ]);
    
}

MuExp translate (e:(Expression) `[ <Expression first> , <Expression second> .. <Expression last> ]`) {
  str fuid = topFunctionScope();
  loopname = nextLabel(); 
  writer = asTmp(loopname);
  var = nextTmp();
  patcode = muCreate(mkCallToLibFun("Library","MATCH_VAR",2), [muTmpRef(var,fuid)]);

  kind = getOuterType(first) == "int" && getOuterType(second) == "int" && getOuterType(last) == "int" ? "_INT" : "";
  rangecode = muMulti(muCreate(mkCallToLibFun("Library", "RANGE_STEP<kind>", 4), [ patcode, translate(first), translate(second), translate(last)]));
  
  return
    muBlock(
    [ muAssignTmp(writer, fuid, muCallPrim("listwriter_open", [])),
      muWhile(loopname, makeMu("ALL", [ rangecode ]), [ muCallPrim("listwriter_add", [muTmp(writer,fuid), muTmp(var,fuid)])]),
      muCallPrim("listwriter_close", [muTmp(writer,fuid)]) 
    ]);
}

// Visit
MuExp translate (e:(Expression) `<Label label> <Visit visitItself>`) = translateVisit(label, visitItself);

// Reducer
MuExp translate (e:(Expression) `( <Expression init> | <Expression result> | <{Expression ","}+ generators> )`) = translateReducer(init, result, generators);

// Reified type
MuExp translate (e:(Expression) `type ( <Expression symbol> , <Expression definitions >)`) { throw("reifiedType"); }
//  muCon(symbolToValue(symbol, config));

// Call
MuExp translate(e:(Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments keywordArguments>)`){

   MuExp kwargs = translateKeywordArguments(keywordArguments);
      
   MuExp receiver = translate(expression);
   list[MuExp] args = [ translate(a) | a <- arguments ];
   if(getOuterType(expression) == "str") {
       return muCallPrim("node_create", [receiver, *args]);
   }
   
   if(getOuterType(expression) == "loc"){
       return muCallPrim("loc_with_offset_create", [receiver, *args]);
   }
   
   if(muFun(str _) := receiver || muFun(str _, str _) := receiver || muConstr(str _) := receiver) {
       return muCall(receiver, args + [ kwargs ]);
   }
   
   // Now overloading resolution...
   ftype = getType(expression@\loc); // Get the type of a receiver
   if(isOverloadedFunction(receiver) && receiver.fuid in overloadingResolver) {
       // Get the types of arguments
       list[Symbol] targs = [ getType(arg@\loc) | arg <- arguments ];
       // Generate a unique name for an overloaded function resolved for this specific use 
       str ofqname = receiver.fuid + "(<for(targ<-targs){><targ>;<}>)";
       // Resolve alternatives for this specific call
       int i = overloadingResolver[receiver.fuid];
       tuple[str scopeIn,set[int] alts] of = overloadedFunctions[i];
       set[int] resolved = {};
       
       bool matches(Symbol t) {
           if(isFunctionType(ftype) || isConstructorType(ftype)) {
               if(/parameter(_,_) := t) { // In case of polymorphic function types
                   try {
                       if(isConstructorType(t) && isConstructorType(ftype)) {
                           bindings = match(\tuple([ a | Symbol arg <- getConstructorArgumentTypes(t),     label(_,Symbol a) := arg || Symbol a := arg ]),
                                            \tuple([ a | Symbol arg <- getConstructorArgumentTypes(ftype), label(_,Symbol a) := arg || Symbol a := arg ]),());
                           bindings = bindings + ( name : \void() | /parameter(str name,_) := t, name notin bindings );
                           return instantiate(t.\adt,bindings) == ftype.\adt;
                       }
                       if(isFunctionType(t) && isFunctionType(ftype)) {
                           bindings = match(getFunctionArgumentTypesAsTuple(t),getFunctionArgumentTypesAsTuple(ftype),());
                           bindings = bindings + ( name : \void() | /parameter(str name,_) := t, name notin bindings );
                           return instantiate(t.ret,bindings) == ftype.ret;
                       }
                       return false;
                   } catch invalidMatch(_,_,_): {
                       return false;
                   } catch invalidMatch(_,_): {
                       return false; 
                   } catch err: {
                       println("WARNING: Cannot match <ftype> against <t> for location: <expression@\loc>! <err>");
                   }
               }
               return t == ftype;
           }           
           if(isOverloadedType(ftype)) {
               if(/parameter(_,_) := t) { // In case of polymorphic function types
                   for(Symbol alt <- (getNonDefaultOverloadOptions(ftype) + getDefaultOverloadOptions(ftype))) {
                       try {
           	               if(isConstructorType(t) && isConstructorType(alt)) {
           	                   bindings = match(\tuple([ a | Symbol arg <- getConstructorArgumentTypes(t),   label(_,Symbol a) := arg || Symbol a := arg ]),
           	                                    \tuple([ a | Symbol arg <- getConstructorArgumentTypes(alt), label(_,Symbol a) := arg || Symbol a := arg ]),());
           	                   bindings = bindings + ( name : \void() | /parameter(str name,_) := t, name notin bindings );
           	                   return instantiate(t.\adt,bindings) == alt.\adt;
           	               }
           	               if(isFunctionType(t) && isFunctionType(alt)) {
           	                   bindings = match(getFunctionArgumentTypesAsTuple(t),getFunctionArgumentTypesAsTuple(alt),());
           	                   bindings = bindings + ( name : \void() | /parameter(str name,_) := t, name notin bindings );
           	                   return instantiate(t.ret,bindings) == alt.ret;
           	               }
           	               return false;
           	           } catch invalidMatch(_,_,_): {
           	               ;
                       } catch invalidMatch(_,_): {
                           ;
                       } catch err: {
                           println("WARNING: Cannot match <alt> against <t> for location: <expression@\loc>! <err>");
                       }
                   }
                   return false;
           	   }
               return t in (getNonDefaultOverloadOptions(ftype) + getDefaultOverloadOptions(ftype));
           }
           throw "Ups, unexpected type of the call receiver expression!";
       }
       
       for(int alt <- of.alts) {
           t = fuid2type[alt];
           if(matches(t)) {
               resolved += alt;
           }
       }
       if(isEmpty(resolved)) {
           for(int alt <- of.alts) {
               t = fuid2type[alt];
               matches(t);
               println("ALT: <t> ftype: <ftype>");
           }
           throw "ERROR in overloading resolution: <ftype>; <expression@\loc>";
       }
       bool exists = <of.scopeIn,resolved> in overloadedFunctions;
       if(!exists) {
           i = size(overloadedFunctions);
           overloadedFunctions += <of.scopeIn,resolved>;
       } else {
           i = indexOf(overloadedFunctions, <of.scopeIn,resolved>);
       }
       
       overloadingResolver[ofqname] = i;
       return muOCall(muOFun(ofqname), args + [ kwargs ]);
   }
   if(isOverloadedFunction(receiver) && receiver.fuid notin overloadingResolver) {
      throw "The use of a function has to be managed via overloading resolver!";
   }
   // Push down additional information if the overloading resolution needs to be done at runtime
   return muOCall(receiver, 
   				  isFunctionType(ftype) ? Symbol::\tuple([ ftype ]) : Symbol::\tuple([ t | Symbol t <- getNonDefaultOverloadOptions(ftype) + getDefaultOverloadOptions(ftype) ]), 
   				  args + [ kwargs ]);
}

// Any
MuExp translate (e:(Expression) `any ( <{Expression ","}+ generators> )`) = makeMuOne("ALL",[ translate(g) | g <- generators ]);

// All

MuExp translate (e:(Expression) `all ( <{Expression ","}+ generators> )`) {
  // First split generators with a top-level && operator
  generators1 = [*(((Expression) `<Expression e1> && <Expression e2>` := g) ? [e1, e2] : [g]) | g <- generators];
  isGen = [!backtrackFree(g) | g <- generators1];
  tgens = [];
  for(i <- index(generators1)) {
     gen = generators1[i];
     //println("all <i>: <gen>");
     if(isGen[i]){
	 	tgen = translate(gen);
	 	if(muMulti(exp) := tgen){ // Unwraps muMulti, if any
	 	   tgen = exp;
	 	}
	 	tgens += tgen;
	 } else {
	    tgens += translateBoolClosure(gen);
	 }
  }
  //gens = [isGen[i] ? translate(generators2[i]).exp // Unwraps muMulti 
  //                 : translateBoolClosure(generators2[i]) | i <- index(generators1)];
  return muCall(mkCallToLibFun("Library", "RASCAL_ALL", 2), [ muCallMuPrim("make_array", tgens), muCallMuPrim("make_array", [ muBool(b) | bool b <- isGen ]) ]);
}

// Comprehension
MuExp translate (e:(Expression) `<Comprehension comprehension>`) = translateComprehension(comprehension);

// Set
MuExp translate(Expression e:(Expression)`{ <{Expression ","}* es> }`) = translateSetOrList(es, "set");

// List
MuExp translate(Expression e:(Expression)`[ <{Expression ","}* es> ]`)  = translateSetOrList(es, "list");

// Reified type
MuExp translate (e:(Expression) `# <Type tp>`) = muCon(symbolToValue(translateType(tp),config));

// Tuple
MuExp translate (e:(Expression) `\< <{Expression ","}+ elements> \>`) {
    if(all(elem <-elements, isConstant(elem))){
      return muCon(readTextValueString("<e>"));
    } else
        return muCallPrim("tuple_create", [ translate(elem) | elem <- elements ]);
}

// Map
// TODO: map constants
MuExp translate (e:(Expression) `( <{Mapping[Expression] ","}* mappings> )`) =
   muCallPrim("map_create", [ translate(m.from), translate(m.to) | m <- mappings ]);

// It in reducer
MuExp translate (e:(Expression) `it`) = muTmp(topIt().name,topIt().fuid);
 
 // Qualified name
MuExp translate(q:(QualifiedName) `<QualifiedName v>`) = mkVar("<v>", v@\loc);

MuExp translate((Expression) `<QualifiedName v>`) = translate(v);

// For the benefit of names in regular expressions

MuExp translate((Name) `<Name name>`) = mkVar("<name>", name@\loc);

// Subscript
MuExp translate(Expression e:(Expression) `<Expression exp> [ <{Expression ","}+ subscripts> ]`){
    ot = getOuterType(exp);
    op = "<ot>_subscript";
    if(ot notin {"map", "rel", "lrel"}) {
    	   op = "<getOuterType(exp)>_subscript_<intercalate("-", [getOuterType(s) | s <- subscripts])>";
    }
    return muCallPrim(op, translate(exp) + ["<s>" == "_" ? muCon("_") : translate(s) | s <- subscripts]);
}

// Slice
MuExp translate (e:(Expression) `<Expression expression> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`) =
	translateSlice(expression, optFirst, optLast);

MuExp translate (e:(Expression) `<Expression expression> [ <OptionalExpression optFirst> , <Expression second> .. <OptionalExpression optLast> ]`) =
	translateSlice(expression, optFirst, second, optLast);

// Field access
MuExp translate (e:(Expression) `<Expression expression> . <Name field>`) {
   tp = getType(expression@\loc);
   if(isTupleType(tp) || isRelType(tp) || isListRelType(tp) || isMapType(tp)) {
       return translate((Expression)`<Expression expression> \< <Name field> \>`);
   }
   op = isNonTerminalType(tp) ? "nonterminal" : getOuterType(expression);
   return muCallPrim("<op>_field_access", [ translate(expression), muCon("<field>") ]);
}

// Field update
MuExp translate (e:(Expression) `<Expression expression> [ <Name key> = <Expression replacement> ]`) {
    tp = getType(expression@\loc);  
 
    list[str] fieldNames = [];
    if(isRelType(tp)){
       tp = getSetElementType(tp);
    } else if(isListType(tp)){
       tp = getListElementType(tp);
    } else if(isMapType(tp)){
       tp = getMapFieldsAsTuple(tp);
    } else if(isADTType(tp)){
       println("tp = <tp>"); 
        return muCallPrim("adt_field_update", [ translate(expression), muCon("<key>"), translate(replacement) ]);
    }
    if(tupleHasFieldNames(tp)){
    	  fieldNames = getTupleFieldNames(tp);
    }	
    return muCallPrim("<getOuterType(expression)>_update", [ translate(expression), muCon(indexOf(fieldNames, "<key>")), translate(replacement) ]);
}

// Field project
MuExp translate (e:(Expression) `<Expression expression> \< <{Field ","}+ fields> \>`) {
    tp = getType(expression@\loc);   
    list[str] fieldNames = [];
    if(isRelType(tp)){
       tp = getSetElementType(tp);
    } else if(isListType(tp)){
       tp = getListElementType(tp);
    } else if(isMapType(tp)){
       tp = getMapFieldsAsTuple(tp);
    }
    if(tupleHasFieldNames(tp)){
       	fieldNames = getTupleFieldNames(tp);
    }	
    fcode = [(f is index) ? muCon(toInt("<f>")) : muCon(indexOf(fieldNames, "<f>")) | f <- fields];
    //fcode = [(f is index) ? muCon(toInt("<f>")) : muCon("<f>") | f <- fields];
    return muCallPrim("<getOuterType(expression)>_field_project", [ translate(expression), *fcode]);
}

// setAnnotation
MuExp translate (e:(Expression) `<Expression expression> [ @ <Name name> = <Expression val> ]`) =
    muCallPrim("annotation_set", [translate(expression), muCon("<name>"), translate(val)]);

// getAnnotation
MuExp translate (e:(Expression) `<Expression expression> @ <Name name>`) {
	println("getAnnotation: <e>");
    return muCallPrim("annotation_get", [translate(expression), muCon("<name>")]);
    }

// Is
MuExp translate (e:(Expression) `<Expression expression> is <Name name>`) =
    muCallPrim("is", [translate(expression), muCon("<name>")]);

// Has
MuExp translate (e:(Expression) `<Expression expression> has <Name name>`) = 
    muCon(hasField(getType(expression@\loc), "<name>"));   

// Transitive closure
MuExp translate(e:(Expression) `<Expression argument> +`)   = postfix_rel_lrel("transitive_closure", argument);

// Transitive reflexive closure
MuExp translate(e:(Expression) `<Expression argument> *`)   = postfix_rel_lrel("transitive_reflexive_closure", argument);

// isDefined?
MuExp translate(e:(Expression) `<Expression argument> ?`)  = generateIfDefinedOtherwise(muBlock([ translate(argument), muCon(true) ]),  muCon(false));

// IfDefinedOtherwise
MuExp translate(e:(Expression) `<Expression lhs> ? <Expression rhs>`)  = generateIfDefinedOtherwise(translate(lhs), translate(rhs));

MuExp generateIfDefinedOtherwise(MuExp muLHS, MuExp muRHS) {
    str fuid = topFunctionScope();
    str varname = asTmp(nextLabel());
	// Check if evaluation of the expression throws a 'NoSuchKey' or 'NoSuchAnnotation' exception;
	// do this by checking equality of the value constructor names
	cond1 = muCallMuPrim("equal", [ muCon("UninitializedVariable"),
									muCallMuPrim("subscript_array_mint", [ muCallMuPrim("get_name_and_children", [ muTmp(asUnwrapedThrown(varname),fuid) ]), muInt(0) ] ) ]);
	cond3 = muCallMuPrim("equal", [ muCon("NoSuchKey"),
									muCallMuPrim("subscript_array_mint", [ muCallMuPrim("get_name_and_children", [ muTmp(asUnwrapedThrown(varname),fuid) ]), muInt(0) ] ) ]);
	cond2 = muCallMuPrim("equal", [ muCon("NoSuchAnnotation"),
									muCallMuPrim("subscript_array_mint", [ muCallMuPrim("get_name_and_children", [ muTmp(asUnwrapedThrown(varname),fuid) ]), muInt(0) ] ) ]);
	
	elsePart3 = muIfelse(nextLabel(), cond3, [ muRHS ], [ muThrow(muTmp(varname,fuid)) ]);
	elsePart2 = muIfelse(nextLabel(), cond2, [ muRHS ], [ elsePart3 ]);
	catchBody = muIfelse(nextLabel(), cond1, [ muRHS ], [ elsePart2 ]);
	return muTry(muLHS, muCatch(varname, fuid, Symbol::\adt("RuntimeException",[]), catchBody), 
			  		 	muBlock([]));
}

// Not
MuExp translate(e:(Expression) `!<Expression argument>`)    = translateBool(e);

// Negate
MuExp translate(e:(Expression) `-<Expression argument>`)    = prefix("negative", argument);

// Splice
MuExp translate(e:(Expression) `*<Expression argument>`) {
    throw "Splice cannot occur outside set or list";
}
   
// AsType
MuExp translate(e:(Expression) `[ <Type typ> ] <Expression argument>`)  =
   muCallPrim("parse", [muCon(getModuleName()), muCon(type(symbolToValue(translateType(typ), config).symbol,getGrammar(config))), translate(argument)]);
   

// Composition
MuExp translate(e:(Expression) `<Expression lhs> o <Expression rhs>`)   = infix_rel_lrel("compose", e);

// Product
MuExp translate(e:(Expression) `<Expression lhs> * <Expression rhs>`)   = infix("product", e);

// Join
MuExp translate(e:(Expression) `<Expression lhs> join <Expression rhs>`)   = infix("join", e);

// Remainder
MuExp translate(e:(Expression) `<Expression lhs> % <Expression rhs>`)   = infix("remainder", e);

// Division
MuExp translate(e:(Expression) `<Expression lhs> / <Expression rhs>`)   = infix("divide", e);

// Intersection
MuExp translate(e:(Expression) `<Expression lhs> & <Expression rhs>`)   = infix("intersect", e);

//Addition
MuExp translate(e:(Expression) `<Expression lhs> + <Expression rhs>`)   = infix("add", e);

// Subtraction
MuExp translate(e:(Expression) `<Expression lhs> - <Expression rhs>`)   = infix("subtract", e);

// Insert Before
MuExp translate(e:(Expression) `<Expression lhs> \>\> <Expression rhs>`)   = infix("add", e);

// Append After
MuExp translate(e:(Expression) `<Expression lhs> \<\< <Expression rhs>`)   = infix("add", e);

// Modulo
MuExp translate(e:(Expression) `<Expression lhs> mod <Expression rhs>`)   = infix("mod", e);

// Notin
MuExp translate(e:(Expression) `<Expression lhs> notin <Expression rhs>`)   = infix_elm_left("notin", e);

// In
MuExp translate(e:(Expression) `<Expression lhs> in <Expression rhs>`)   = infix_elm_left("in", e);

// Greater Equal
MuExp translate(e:(Expression) `<Expression lhs> \>= <Expression rhs>`) = infix("greaterequal", e);

// Less Equal
MuExp translate(e:(Expression) `<Expression lhs> \<= <Expression rhs>`) = infix("lessequal", e);

// Less
MuExp translate(e:(Expression) `<Expression lhs> \< <Expression rhs>`)  = infix("less", e);

// Greater
MuExp translate(e:(Expression) `<Expression lhs> \> <Expression rhs>`)  = infix("greater", e);

// Equal
MuExp translate(e:(Expression) `<Expression lhs> == <Expression rhs>`)  = comparison("equal", e);

// NotEqual
MuExp translate(e:(Expression) `<Expression lhs> != <Expression rhs>`)  = comparison("notequal", e);



// NoMatch
MuExp translate(e:(Expression) `<Pattern pat> !:= <Expression rhs>`)  = translateMatch(e);

// Match
MuExp translate(e:(Expression) `<Pattern pat> := <Expression exp>`)     = translateMatch(e);

// Enumerate

MuExp translate(e:(Expression) `<QualifiedName name> \<- <Expression exp>`) {
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return muMulti(muCreate(mkCallToLibFun("Library", "ENUMERATE_AND_ASSIGN", 2), [muVarRef("<name>", fuid, pos), translate(exp)]));
}

MuExp translate(e:(Expression) `<Type tp> <Name name> \<- <Expression exp>`) {
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return muMulti(muCreate(mkCallToLibFun("Library", "ENUMERATE_CHECK_AND_ASSIGN", 3), [muTypeCon(translateType(tp)), muVarRef("<name>", fuid, pos), translate(exp)]));
}

MuExp translate(e:(Expression) `<Pattern pat> \<- <Expression exp>`) =
    muMulti(muCreate(mkCallToLibFun("Library", "ENUMERATE_AND_MATCH", 2), [translatePat(pat), translate(exp)]));

// Implies
MuExp translate(e:(Expression) `<Expression lhs> ==\> <Expression rhs>`)  = translateBool(e);

// Equivalent
MuExp translate(e:(Expression) `<Expression lhs> \<==\> <Expression rhs>`)  = translateBool(e);

// And
MuExp translate(e:(Expression) `<Expression lhs> && <Expression rhs>`)  = translateBool(e);

// Or
MuExp translate(e:(Expression) `<Expression lhs> || <Expression rhs>`)  = translateBool(e);
 
// Conditional Expression
MuExp translate(e:(Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>`) =
	// ***Note that the label (used to backtrack) here is not important (no backtracking scope is pushed) 
	// as it is not allowed to have 'fail' in conditional expressions
	muIfelse(nextLabel(),translate(condition), [translate(thenExp)],  [translate(elseExp)]);

// Default: should not happen
default MuExp translate(Expression e) {
	throw "MISSING CASE FOR EXPRESSION: <e>";
}

/*********************************************************************/
/*                  End of Ordinary Expessions                       */
/*********************************************************************/

/*********************************************************************/
/*                  BooleanExpessions                                */
/*********************************************************************/
 
// Is an expression free of backtracking? 

bool backtrackFree(Expression e){
    top-down visit(e){
    //case (Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments keywordArguments>)`:
    //	return true;
    case (Expression) `all ( <{Expression ","}+ generators> )`: 
    	return true;
    case (Expression) `any ( <{Expression ","}+ generators> )`: 
    	return true;
    case (Expression) `<Pattern pat> \<- <Expression exp>`: 
    	return false;
    case (Expression) `<Pattern pat> \<- [ <Expression first> .. <Expression last> ]`: 
    	return false;
    case (Expression) `<Pattern pat> \<- [ <Expression first> , <Expression second> .. <Expression last> ]`: 
    	return false;
    case (Expression) `<Pattern pat> := <Expression exp>`:
    	return false;
    	case (Expression) `<Pattern pat> !:= <Expression exp>`:
    	return false;
    }
    return true;
}

// Boolean expressions

MuExp translateBool((Expression) `<Expression lhs> && <Expression rhs>`) = translateBoolBinaryOp("and", lhs, rhs);

MuExp translateBool((Expression) `<Expression lhs> || <Expression rhs>`) = translateBoolBinaryOp("or", lhs, rhs);

MuExp translateBool((Expression) `<Expression lhs> ==\> <Expression rhs>`) = translateBoolBinaryOp("implies", lhs, rhs);

MuExp translateBool((Expression) `<Expression lhs> \<==\> <Expression rhs>`) = translateBoolBinaryOp("equivalent", lhs, rhs);

MuExp translateBool((Expression) `! <Expression lhs>`) = translateBoolNot(lhs);
 
MuExp translateBool(e: (Expression) `<Pattern pat> := <Expression exp>`)  = translateMatch(e);
   
MuExp translateBool(e: (Expression) `<Pattern pat> !:= <Expression exp>`) = translateMatch(e);

// All other expressions are translated as ordinary expression

default MuExp translateBool(Expression e) {
   //println("translateBool, default: <e>");
   return translate(e);
}
   
// Translate Boolean operators

// TODO: WORK IN PROGRESS HERE!

MuExp translateBoolBinaryOp(str fun, Expression lhs, Expression rhs){
  if(backtrackFree(lhs) && backtrackFree(rhs)) {
     switch(fun){
     	case "and": 		return muIfelse(nextLabel("L_AND"), translateBool(lhs), [translateBool(rhs)], [muCon(false)]);
     	case "or":			return muIfelse(nextLabel("L_OR"), translateBool(lhs), [muCon(true)], [translateBool(rhs)]);
     	case "implies":		return muIfelse(nextLabel("L_IMPLIES"), translateBool(lhs), [translateBool(rhs)], [muCon(true)]);
     	case "equivalent":	return muIfelse(nextLabel("L_EQUIVALENT"), translateBool(lhs), [translateBool(rhs)], [muCallMuPrim("not_mbool", [translateBool(rhs)])]);
     	default:
    		throw "translateBoolBinary: unknown operator <fun>";
     }
  } else {
    switch(fun){
    // TODO: Review short-cut semantics
    	case "and": return makeMu("ALL",[translate(lhs), translate(rhs)]);
    	case "or":  // a or b == !(!a and !b)
    	            return makeMu("OR",[translate(lhs), translate(rhs)]);
    	case "implies":
    				// a ==> b
    	            return makeMu("ALL",[muIfelse(nextLabel("L_IMPLIES"), translate(lhs), [translate(rhs)], [muCon(true)])]);
    	case "equivalent":
    				// a <==> b
    				return makeMu("ALL",[muIfelse(nextLabel("L_EQUIVALENCE"), translate(lhs), [translate(rhs)], [muCallMuPrim("not_mbool", [translate(rhs)])])]);
    	default:
    		throw "translateBoolBinary: unknown operator <fun>";
    }
  }
}

MuExp translateBoolNot(Expression lhs){
  if(backtrackFree(lhs)){
  	  return muCallMuPrim("not_mbool", [translateBool(lhs)]);
  	} else {
  	  return muCallMuPrim("not_mbool", [ makeMu("ALL",[translate(lhs)]) ]);
  	}
}

/*********************************************************************/
/*      Auxiliary functions for translating various constructs       */
/*********************************************************************


// Translate a string literals and string templates

/*
syntax StringLiteral
	= template: PreStringChars pre StringTemplate template StringTail tail 
	| interpolated: PreStringChars pre Expression expression StringTail tail 
	| nonInterpolated: StringConstant constant ;
	
lexical PreStringChars
	= [\"] StringCharacter* [\<] ;
	
lexical MidStringChars
	=  [\>] StringCharacter* [\<] ;
	
lexical PostStringChars
	= @category="Constant" [\>] StringCharacter* [\"] ;
*/	

MuExp translateStringLiteral(s: (StringLiteral) `<PreStringChars pre> <StringTemplate template> <StringTail tail>`) {
    str fuid = topFunctionScope();
	preResult = nextTmp();
	return muBlock( [ muAssignTmp(preResult, fuid, translateChars("<pre>")),
                      muCallPrim("template_addunindented", [ translateTemplate(template, computeIndent(pre), preResult, fuid), *translateTail(tail)])
                    ]);
}
    
MuExp translateStringLiteral((StringLiteral) `<PreStringChars pre> <Expression expression> <StringTail tail>`) {
    str fuid = topFunctionScope();
    preResult = nextTmp();
    return muBlock( [ muAssignTmp(preResult, fuid, translateChars("<pre>")),
					  muCallPrim("template_addunindented", [ translateTemplate(expression, computeIndent(pre), preResult, fuid), *translateTail(tail)])
					]   );
}
                    
MuExp translateStringLiteral((StringLiteral)`<StringConstant constant>`) = muCon(readTextValueString("<constant>"));

str removeMargins(str s)  = visit(s) { case /^[ \t]*'/m => "" };

str computeIndent(str s) {
   lines = split("\n", removeMargins(s)); 
   return isEmpty(lines) ? "" : left("", size(lines[-1]));
} 

str computeIndent(PreStringChars pre) = computeIndent(removeMargins("<pre>"[1..-1]));
str computeIndent(MidStringChars mid) = computeIndent(removeMargins("<mid>"[1..-1]));

MuExp translateChars(str s) = muCon(removeMargins(s[1..-1]));

/*
syntax StringTemplate
	= ifThen    : "if"    "(" {Expression ","}+ conditions ")" "{" Statement* preStats StringMiddle body Statement* postStats "}" 
	| ifThenElse: "if"    "(" {Expression ","}+ conditions ")" "{" Statement* preStatsThen StringMiddle thenString Statement* postStatsThen "}" "else" "{" Statement* preStatsElse StringMiddle elseString Statement* postStatsElse "}" 
	| \for       : "for"   "(" {Expression ","}+ generators ")" "{" Statement* preStats StringMiddle body Statement* postStats "}" 
	| doWhile   : "do"    "{" Statement* preStats StringMiddle body Statement* postStats "}" "while" "(" Expression condition ")" 
	| \while     : "while" "(" Expression condition ")" "{" Statement* preStats StringMiddle body Statement* postStats "}" ;
*/
	

/*
  syntax StringMiddle
	= mid: MidStringChars mid 
	| template: MidStringChars mid StringTemplate template StringMiddle tail 
	| interpolated: MidStringChars mid Expression expression StringMiddle tail ;
*/

MuExp translateMiddle((StringMiddle) `<MidStringChars mid>`) = muCon(removeMargins("<mid>"[1..-1]));

MuExp translateMiddle((StringMiddle) `<MidStringChars mid> <StringTemplate template> <StringMiddle tail>`) {
    str fuid = topFunctionScope();
    midResult = nextTmp();
    return muBlock( [ muAssignTmp(midResult, fuid, translateChars("<mid>")),
   			          muCallPrim("template_addunindented", [ translateTemplate(template, computeIndent(mid), midResult, fuid), translateMiddle(tail) ])
   			        ]);
   	}

MuExp translateMiddle((StringMiddle) `<MidStringChars mid> <Expression expression> <StringMiddle tail>`) {
    str fuid = topFunctionScope();
    midResult = nextTmp();
    return muBlock( [ muAssignTmp(midResult, fuid, translateChars("<mid>")),
                      muCallPrim("template_addunindented", [ translateTemplate(expression, computeIndent(mid), midResult, fuid), translateMiddle(tail) ])
                    ]);
}
  
/*
syntax StringTail
	= midInterpolated: MidStringChars mid Expression expression StringTail tail 
	| post: PostStringChars post 
	| midTemplate: MidStringChars mid StringTemplate template StringTail tail ;
*/

list[MuExp] translateTail((StringTail) `<MidStringChars mid> <Expression expression> <StringTail tail>`) {
    str fuid = topFunctionScope();
    midResult = nextTmp();
    return [ muBlock( [ muAssignTmp(midResult, fuid, translateChars("<mid>")),
                      muCallPrim("template_addunindented", [ translateTemplate(expression, computeIndent(mid), midResult, fuid), *translateTail(tail)])
                    ])
           ];
}
	
list[MuExp] translateTail((StringTail) `<PostStringChars post>`) {
  content = removeMargins("<post>"[1..-1]);
  return size(content) == 0 ? [] : [muCon(content)];
}

list[MuExp] translateTail((StringTail) `<MidStringChars mid> <StringTemplate template> <StringTail tail>`) {
    str fuid = topFunctionScope();
    midResult = nextTmp();
    return [ muBlock( [ muAssignTmp(midResult, fuid, translateChars("<mid>")),
                        muCallPrim("template_addunindented", [ translateTemplate(template, computeIndent(mid), midResult, fuid), *translateTail(tail) ])
                    ])
           ];
 }  
 
 MuExp translateTemplate(Expression e, str indent, str preResult, str prefuid){
    str fuid = topFunctionScope();
    result = nextTmp();
    return muBlock([ muAssignTmp(result, fuid, muCallPrim("template_open", [muCon(indent), muTmp(preResult,prefuid)])),
    				 muAssignTmp(result, fuid, muCallPrim("template_add", [ muTmp(result,fuid), muCallPrim("value_to_string", [translate(e)]) ])),
                     muCallPrim("template_close", [muTmp(result,fuid)])
                   ]);
 }
 
 // Translate location templates
 
 /*
 syntax LocationLiteral
	= \default: ProtocolPart protocolPart PathPart pathPart ;
 */
 
 MuExp translateLocationLiteral((LocationLiteral) `<ProtocolPart protocolPart> <PathPart pathPart>`) =
     muCallPrim("loc_create", [muCallPrim("str_add_str", [translateProtocolPart(protocolPart), translatePathPart(pathPart)])]);
 
 /*
 syntax ProtocolPart
	= nonInterpolated: ProtocolChars protocolChars 
	| interpolated: PreProtocolChars pre Expression expression ProtocolTail tail ;
	
lexical PreProtocolChars
	= "|" URLChars "\<" ;
	
 lexical MidProtocolChars
	= "\>" URLChars "\<" ;
	
lexical ProtocolChars
	= [|] URLChars "://" !>> [\t-\n \r \ \u00A0 \u1680 \u2000-\u200A \u202F \u205F \u3000];

syntax ProtocolTail
	= mid: MidProtocolChars mid Expression expression ProtocolTail tail 
	| post: PostProtocolChars post ;

lexical PostProtocolChars
	= "\>" URLChars "://" ;	
*/

 MuExp translateProtocolPart((ProtocolPart) `<ProtocolChars protocolChars>`) = muCon("<protocolChars>"[1..]);
 
 MuExp translateProtocolPart((ProtocolPart) `<PreProtocolChars pre> <Expression expression> <ProtocolTail tail>`) =
    muCallPrim("str_add_str", [muCon("<pre>"[1..-1]), translate(expression), translateProtocolTail(tail)]);
 
 // ProtocolTail
 MuExp  translateProtocolTail((ProtocolTail) `<MidProtocolChars mid> <Expression expression> <ProtocolTail tail>`) =
   muCallPrim("str_add_str", [muCon("<mid>"[1..-1]), translate(expression), translateProtocolTail(tail)]);
   
MuExp translateProtocolTail((ProtocolTail) `<PostProtocolChars post>`) = muCon("<post>"[1 ..]);

/*
syntax PathPart
	= nonInterpolated: PathChars pathChars 
	| interpolated: PrePathChars pre Expression expression PathTail tail ;

lexical PathChars
	= URLChars [|] ;
		
 syntax PathTail
	= mid: MidPathChars mid Expression expression PathTail tail 
	| post: PostPathChars post ;

lexical PrePathChars
	= URLChars "\<" ;

lexical MidPathChars
	= "\>" URLChars "\<" ;
	
lexical PostPathChars
	=  "\>" URLChars "|" ;
*/

MuExp translatePathPart((PathPart) `<PathChars pathChars>`) = muCon("<pathChars>"[..-1]);
MuExp translatePathPart((PathPart) `<PrePathChars pre> <Expression expression> <PathTail tail>`) =
   muCallPrim("str_add_str", [ muCon("<pre>"[..-1]), translate(expression), translatePathTail(tail)]);

// PathTail
MuExp translatePathTail((PathTail) `<MidPathChars mid> <Expression expression> <PathTail tail>`) =
   muCallPrim("str_add_str", [ muCon("<mid>"[1..-1]), translate(expression), translatePathTail(tail)]);
   
MuExp translatePathTail((PathTail) `<PostPathChars post>`) = muCon("<post>"[1..-1]);

 
// Translate a closure   
 
 MuExp translateClosure(Expression e, Parameters parameters, Tree cbody) {
 	uid = loc2uid[e@\loc];
	fuid = uid2str(uid);
	
	enterFunctionScope(fuid);
	
    ftype = getClosureType(e@\loc);
	nformals = size(ftype.parameters);
	bool isVarArgs = (varArgs(_,_) := parameters);
  	
  	// Keyword parameters
    list[MuExp] kwps = translateKeywordParameters(parameters, fuid, getFormals(uid), e@\loc);
    
    // TODO: we plan to introduce keyword patterns as formal parameters
    MuExp body = translateFunction(parameters.formals.formals, isVarArgs, kwps, cbody, []);
    
    tuple[str fuid,int pos] addr = uid2addr[uid];
    functions_in_module += muFunction(fuid, ftype, (addr.fuid in moduleNames) ? "" : addr.fuid, 
  									  getFormals(uid), getScopeSize(fuid), 
  									  isVarArgs, e@\loc, [], (), 
  									  body);
  	
  	leaveFunctionScope();								  
  	
	return (addr.fuid == uid2str(0)) ? muFun(fuid) : muFun(fuid, addr.fuid); // closures are not overloaded
}

MuExp translateBoolClosure(Expression e){
    tuple[str fuid,int pos] addr = <topFunctionScope(),-1>;
	fuid = addr.fuid + "/non_gen_at_<e@\loc>()";
	
	enterFunctionScope(fuid);
	
    ftype = Symbol::func(Symbol::\bool(),[]);
	nformals = 0;
	nlocals = 0;
	bool isVarArgs = false;
  	
    MuExp body = muReturn(translate(e));
    functions_in_module += muFunction(fuid, ftype, addr.fuid, 
  									  nformals, nlocals, isVarArgs, e@\loc, [], (), body);
  	
  	leaveFunctionScope();								  
  	
	return muFun(fuid, addr.fuid); // closures are not overloaded

}

// Translate comprehensions

MuExp translateGenerators({Expression ","}+ generators){
   if(all(gen <- generators, backtrackFree(gen))){
      return makeMu("ALL",[translate(g) | g <-generators]);
   } else {
     return makeMu("ALL",[muCallPrim("rbool", [translate(g)]) | g <-generators]);
   }
}

list[MuExp] translateComprehensionContribution(str kind, str tmp, str fuid, list[Expression] results){
  return 
	  for( r <- results){
	    if((Expression) `* <Expression exp>` := r){
	       append muCallPrim("<kind>writer_splice", [muTmp(tmp,fuid), translate(exp)]);
	    } else {
	      append muCallPrim("<kind>writer_add", [muTmp(tmp,fuid), translate(r)]);
	    }
	  }
} 

MuExp translateComprehension(c: (Comprehension) `[ <{Expression ","}+ results> | <{Expression ","}+ generators> ]`) {
    str fuid = topFunctionScope();
    loopname = nextLabel(); 
    tmp = asTmp(loopname);
    return
    muBlock(
    [ muAssignTmp(tmp, fuid, muCallPrim("listwriter_open", [])),
      muWhile(loopname, makeMu("ALL",[ translate(g) | g <- generators ]), translateComprehensionContribution("list", tmp, fuid, [r | r <- results])),
      muCallPrim("listwriter_close", [muTmp(tmp,fuid)]) 
    ]);
}

MuExp translateComprehension(c: (Comprehension) `{ <{Expression ","}+ results> | <{Expression ","}+ generators> }`) {
    str fuid = topFunctionScope();
    loopname = nextLabel(); 
    tmp = asTmp(loopname); 
    return
    muBlock(
    [ muAssignTmp(tmp, fuid, muCallPrim("setwriter_open", [])),
      muWhile(loopname, makeMu("ALL",[ translate(g) | g <- generators ]), translateComprehensionContribution("set", tmp, fuid, [r | r <- results])),
      muCallPrim("setwriter_close", [muTmp(tmp,fuid)]) 
    ]);
}

MuExp translateComprehension(c: (Comprehension) `(<Expression from> : <Expression to> | <{Expression ","}+ generators> )`) {
    str fuid = topFunctionScope();
    loopname = nextLabel(); 
    tmp = asTmp(loopname); 
    return
    muBlock(
    [ muAssignTmp(tmp, fuid, muCallPrim("mapwriter_open", [])),
      muWhile(loopname, makeMu("ALL",[ translate(g) | g <- generators ]), [muCallPrim("mapwriter_add", [muTmp(tmp,fuid)] + [ translate(from), translate(to)])]), 
      muCallPrim("mapwriter_close", [muTmp(tmp,fuid)]) 
    ]);
}

// Translate Reducer

MuExp translateReducer(init, result, generators){
    str fuid = topFunctionScope();
    loopname = nextLabel(); 
    tmp = asTmp(loopname); 
    pushIt(tmp,fuid);
    code = [ muAssignTmp(tmp, fuid, translate(init)), muWhile(loopname, makeMu("ALL", [ translate(g) | g <- generators ]), [muAssignTmp(tmp,fuid,translate(result))]), muTmp(tmp,fuid)];
    popIt();
    return muBlock(code);
}

// Translate SetOrList including spliced elements

private bool containSplices(es) = any(e <- es, e is splice);

MuExp translateSetOrList(es, str kind){
 if(containSplices(es)){
       str fuid = topFunctionScope();
       writer = nextTmp();
       enterWriter(writer);
       code = [ muAssignTmp(writer, fuid, muCallPrim("<kind>writer_open", [])) ];
       for(elem <- es){
           if(elem is splice){
              code += muCallPrim("<kind>writer_splice", [muTmp(writer,fuid), translate(elem.argument)]);
            } else {
              code += muCallPrim("<kind>writer_add", [muTmp(writer,fuid), translate(elem)]);
           }
       }
       code += [ muCallPrim("<kind>writer_close", [ muTmp(writer,fuid) ]) ];
       leaveWriter();
       return muBlock(code);
    } else {
      if(all(elm <- es, isConstant(elm))){
         return kind == "list" ? muCon([getConstantValue(elm) | elm <- es]) : muCon({getConstantValue(elm) | elm <- es});
      } else 
        return muCallPrim("<kind>_create", [ translate(elem) | elem <- es ]);
    }
}

// Translate Slice

MuExp translateSlice(Expression expression, OptionalExpression optFirst, OptionalExpression optLast) =
    muCallPrim("<getOuterType(expression)>_slice", [ translate(expression), translateOpt(optFirst), muCon("false"), translateOpt(optLast) ]);

MuExp translateOpt(OptionalExpression optExp) =
    optExp is noExpression ? muCon("false") : translate(optExp.expression);

MuExp translateSlice(Expression expression, OptionalExpression optFirst, Expression second, OptionalExpression optLast) =
    muCallPrim("<getOuterType(expression)>_slice", [  translate(expression), translateOpt(optFirst), translate(second), translateOpt(optLast) ]);

// Translate Visit
MuExp translateVisit(label,\visit) {
	MuExp traverse_fun;
	bool fixpoint = false;
	
	if(\visit is defaultStrategy) {
		traverse_fun = mkCallToLibFun("Library","TRAVERSE_BOTTOM_UP",5);
	} else {
		switch("<\visit.strategy>") {
			case "bottom-up"      :   traverse_fun = mkCallToLibFun("Library","TRAVERSE_BOTTOM_UP",      5);
			case "top-down"       :   traverse_fun = mkCallToLibFun("Library","TRAVERSE_TOP_DOWN",       5);
			case "bottom-up-break":   traverse_fun = mkCallToLibFun("Library","TRAVERSE_BOTTOM_UP_BREAK",5);
			case "top-down-break" :   traverse_fun = mkCallToLibFun("Library","TRAVERSE_TOP_DOWN_BREAK", 5);
			case "innermost"      : { traverse_fun = mkCallToLibFun("Library","TRAVERSE_BOTTOM_UP",      5); fixpoint = true; }
			case "outermost"      : { traverse_fun = mkCallToLibFun("Library","TRAVERSE_TOP_DOWN",       5); fixpoint = true; }
		}
	}
	
	bool rebuild = false;
	if( Case c <- \visit.cases, (c is patternWithAction && c.patternWithAction is replacing 
									|| hasTopLevelInsert(c)) ) {
		println("Rebuilding visit!");
		rebuild = true;
	}
	
	// Unique 'id' of a visit in the function body
	int i = nextVisit();
	
	// Generate and add a nested function 'phi'
	str scopeId = topFunctionScope();
	str phi_fuid = scopeId + "/" + "PHI_<i>";
	Symbol phi_ftype = Symbol::func(Symbol::\value(), [Symbol::\value(),Symbol::\value()]);
	
	enterVisit();
	enterFunctionScope(phi_fuid);
	
	functions_in_module += muFunction(phi_fuid, phi_ftype, scopeId, 3, 3, false, \visit@\loc, [], (), 
										translateVisitCases([ c | Case c <- \visit.cases ],phi_fuid));
	
	leaveFunctionScope();
	leaveVisit();
	
	if(fixpoint) {
		str phi_fixpoint_fuid = scopeId + "/" + "PHI_FIXPOINT_<i>";
		
		enterFunctionScope(phi_fixpoint_fuid);
		
		// Local variables of 'phi_fixpoint_fuid': 'iSubject', 'matched', 'hasInsert', 'changed', 'val'
		list[MuExp] body = [];
		body += muAssign("changed", phi_fixpoint_fuid, 3, muBool(true));
		body += muWhile(nextLabel(), muVar("changed",phi_fixpoint_fuid,3), 
						[ muAssign("val", phi_fixpoint_fuid, 4, muCall(muFun(phi_fuid,scopeId), [ muVar("iSubject",phi_fixpoint_fuid,0), muVar("matched",phi_fixpoint_fuid,1), muVar("hasInsert",phi_fixpoint_fuid,2) ])),
						  muIfelse(nextLabel(), makeMu("ALL", [ muCallPrim("equal",[ muVar("val",phi_fixpoint_fuid,4), muVar("iSubject",phi_fixpoint_fuid,0) ]) ]),
						  						[ muAssign("changed",phi_fixpoint_fuid,3,muBool(false)) ], 
						  						[ muAssign("iSubject",phi_fixpoint_fuid,0,muVar("val",phi_fixpoint_fuid,4)) ] )]);
		body += muReturn(muVar("iSubject",phi_fixpoint_fuid,0));
		
		leaveFunctionScope();
		
		functions_in_module += muFunction(phi_fixpoint_fuid, phi_ftype, scopeId, 3, 5, false, \visit@\loc, [], (), muBlock(body));
	
	    // Local variables of the surrounding function
		str hasMatch = asTmp(nextLabel());
		str beenChanged = asTmp(nextLabel());
		return muBlock([ muAssignTmp(hasMatch,scopeId,muBool(false)),
						 muAssignTmp(beenChanged,scopeId,muBool(false)),
					 	 muCall(traverse_fun, [ muFun(phi_fixpoint_fuid,scopeId), translate(\visit.subject), muTmpRef(hasMatch,scopeId), muTmpRef(beenChanged,scopeId), muBool(rebuild) ]) 
				   	   ]);
	}
	
	// Local variables of the surrounding function
	str hasMatch = asTmp(nextLabel());
	str beenChanged = asTmp(nextLabel());
	return muBlock([ muAssignTmp(hasMatch,scopeId,muBool(false)), 
	                 muAssignTmp(beenChanged,scopeId,muBool(false)),
					 muCall(traverse_fun, [ muFun(phi_fuid,scopeId), translate(\visit.subject), muTmpRef(hasMatch,scopeId), muTmpRef(beenChanged,scopeId), muBool(rebuild) ]) 
				   ]);
}

@doc{Generates the body of a phi function}
MuExp translateVisitCases(list[Case] cases,str fuid) {
	// TODO: conditional
	if(size(cases) == 0) {
		return muReturn(muVar("subject",fuid,0));
	}
	
	c = head(cases);
	
	if(c is patternWithAction) {
		pattern = c.patternWithAction.pattern;
		typePat = getType(pattern@\loc);
		cond = muMulti(muCreate(mkCallToLibFun("Library","MATCH",2), [ translatePat(pattern), muVar("subject",fuid,0) ]));
		ifname = nextLabel();
		enterBacktrackingScope(ifname);
		if(c.patternWithAction is replacing) {
			replacement = translate(c.patternWithAction.replacement.replacementExpression);
			list[MuExp] conditions = [];
			if(c.patternWithAction.replacement is conditional) {
				conditions = [ translate(e) | Expression e <- c.patternWithAction.replacement.conditions ];
			}
			replacementType = getType(c.patternWithAction.replacement.replacementExpression@\loc);
			tcond = muCallPrim("subtype", [ muTypeCon(replacementType), muCallPrim("typeOf", [ muVar("iSubject",fuid,0) ]) ]);
			list[MuExp] cbody = [ muAssignVarDeref("matched",fuid,1,muBool(true)), muAssignVarDeref("hasInsert",fuid,2,muBool(true)), replacement ];
        	exp = muIfelse(ifname, makeMu("ALL",[ cond,tcond,*conditions ]), [ muReturn(muBlock(cbody)) ], [ translateVisitCases(tail(cases),fuid) ]);
        	leaveBacktrackingScope();
        	return exp;
		} else {
			// Arbitrary
			statement = c.patternWithAction.statement;
			\case = translate(statement);
			insertType = topCaseType();
			clearCaseType();
			tcond = muCallPrim("subtype", [ muTypeCon(insertType), muCallPrim("typeOf", [ muVar("iSubject",fuid,0) ]) ]);
			list[MuExp] cbody = [ muAssignVarDeref("matched",fuid,1,muBool(true)) ];
			if(!(muBlock([]) := \case)) {
				cbody += \case;
			}
			cbody += muReturn(muVar("subject",fuid,0));
			exp = muIfelse(ifname, makeMu("ALL",[ cond,tcond ]), cbody, [ translateVisitCases(tail(cases),fuid) ]);
        	leaveBacktrackingScope();
			return exp;
		}
	} else {
		// Default
		return muBlock([ muAssignVarDeref("matched",fuid,1,muBool(true)), translate(c.statement), muReturn(muVar("iSubject",fuid,0)) ]);
	}
}

private bool hasTopLevelInsert(Case c) {
	println("Look for an insert...");
	top-down-break visit(c) {
		case (Statement) `insert <DataTarget dt> <Statement stat>`: return true;
		case Visit v: ;
	}
	println("Insert has not been found, non-rebuilding visit!");
	return false;
}

MuExp translateKeywordArguments(KeywordArguments keywordArguments) {
   // Keyword arguments
   str fuid = topFunctionScope();
   list[MuExp] kwargs = [ muAssignTmp("map_of_keyword_arguments", fuid, muCallPrim("mapwriter_open",[])) ];
   if(keywordArguments is \default) {
       for(KeywordArgument kwarg <- keywordArguments.keywordArgumentList) {
           kwargs += muCallPrim("mapwriter_add",[ muTmp("map_of_keyword_arguments",fuid), muCon("<kwarg.name>"), translate(kwarg.expression) ]);           
       }
   }
   return muBlock([ *kwargs, muCallPrim("mapwriter_close", [ muTmp("map_of_keyword_arguments",fuid) ]) ]);
}
