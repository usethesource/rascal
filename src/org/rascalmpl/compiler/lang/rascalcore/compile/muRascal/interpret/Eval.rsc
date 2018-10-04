module lang::rascalcore::compile::muRascal::interpret::Eval

import lang::rascalcore::compile::muRascal::AST;
import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;
import List;
import Set;
import Map;
import Node;
import IO;
import Type;
import util::Reflective;

data DoControl
     = doReturn(RValue v, Env env)
     | doFailReturn()
     | doBreak(str label, Env env)
     | doContinue(str label, Env env)
     | doFail(str label, Env env)
     ;
    
data RValue
     = undefined()
     | rvalue(value val)
     | rtype(AType tp)
     ;
 
 RValue rvalue(rvalue(v)) = rvalue(v);
         
// ---- Environments and variables --------------------------------------------

alias Frame  = tuple[str frameName, list[RValue] stack, map[str,RValue] temporaries];
alias Env    = list[Frame];
alias Result = tuple[RValue val, Env env];

bool debug = false;

// get/assign variables 

RValue getVariable(str name, str fuid, int pos, Env env){
    for(<str frameName, list[RValue] stack, map[str,RValue] temporaries> <- env){
        if(fuid == frameName){
            return stack[pos];
        }
    }
    throw "getVariable: <name>";
}

Result assignVariable(str name, str fuid, int pos, RValue v, Env env){
    for(int i <- index(env)){
        <frameName, stack, temporaries> = env[i];
        if(fuid == frameName){
            stack[pos] = v;
            res = <v, env[0 .. i] + <frameName, stack, temporaries> + env[i+1 ..]>;
            return res;
        }
    }
    throw "assignVariable: <name>";
}

// get/assign temporaries

RValue getTmp(str name, str fuid, Env env){
    for(<str frameName, list[RValue] stack, map[str,RValue] temporaries> <- env){
        if(fuid == frameName){
            return temporaries[name];
        }
    }
    throw "getTmp: <name>";
}

Result assignTmp(str name, str fuid, RValue v, Env env){
    for(int i <- index(env)){
        <frameName, stack, temporaries> = env[i];
        if(fuid == frameName){
            temporaries[name] = v;
            res = <v, env[0 .. i] + <frameName, stack, temporaries> + env[i+1 ..]>;
            return res;
        }
    }
    throw "assignTmp: <name>";
}

// get/Assign keyword parameters

Result getKwp(str name, str fuid, Env env){
    for(<str frameName, list[RValue] stack, map[str,RValue] temporaries> <- env){
        if(fuid == frameName){
            nargs = muFunctions[fuid].nlocals;
            ikwactuals = nargs - 2;
            ikwdefaults = nargs - 1;
            if(rvalue(map[str, RValue] kwactuals) := stack[ikwactuals] &&
               rvalue(map[str, tuple[AType, RValue]] kwdefaults) := stack[ikwdefaults]){
               if(kwactuals[name]?) return <kwactuals[name], env>;
               return <kwdefaults[name]<1>, env>;
            } else {
                throw "getKwp, wrong stack values: <stack>";
            }
        }
    }
    throw "getKwp: <name>, <fuid>";
}

Result assignKwp(str name, str fuid, RValue v, Env env){
    for(int i <- index(env)){
        <frameName, stack, temporaries> = env[i];
        if(fuid == frameName){
            nargs = muFunctions[fuid].nlocals;
            ikwactuals = nargs - 2;
            if(rvalue(map[str,RValue] mp) := stack[ikwactuals]){
                mp[name] = v;
                stack[ikwactuals] = rvalue(mp);
                res = <v, env[0 .. i] + <frameName, stack, temporaries> + env[i+1 ..]>;
                return res;
            } else {
                throw "assignKwp: illegal kwactuals: <stack>";
            }
        }
    }
    throw "assignVariable: <name>";
}



// ---- eval ------------------------------------------------------------------

map[str, MuFunction] muFunctions = ();
lrel[str name, AType funType, str scope, list[str] ofunctions, list[str] oconstructors] overloadedFunctions = [];


RValue eval(MuModule m){
    muFunctions = (f.qname : f | f <- m.functions);
    overloadedFunctions = m.overloaded_functions;
    main_fun = "";
    for(f <- m.functions){
        if(f.uqname == "main") {
            env = [];
            <result, env> = call(f, [], env);
            return result;
        }
    }
    throw "No function `main` found";
}

Result call(MuFunction fun, list[RValue] actuals, Env env){
    stack = [undefined() | i <- [0..fun.nlocals+1]];
    for(int i <- index(actuals)) stack[i] = actuals[i];
    try {
        if(debug) println("call <fun.qname>, <stack>");
        <result, env1> = eval(fun.body, <fun.qname, stack, ()> + env);
        return <result, tail(env)>;
    } catch doReturn(RValue v, Env env1):
        return <v, tail(env1)>;
}

Result eval(muBool(b), Env env)                                 // muRascal Boolean constant
    = <rvalue(b), env>;        
                                      
Result eval(muInt(int n), Env env)                              // muRascal integer constant
    = <rvalue(n), env>;                                          

Result eval(muCon(value v), Env env)                           // Rascal Constant: an arbitrary IRValue
    = <rvalue(v), env>;                                          
                    
          //| muFun1(str fuid)                                    // *muRascal* function constant: functions at the root
          //| muFun2(str fuid, str scopeIn)                       // *muRascal* function constant: nested functions and closures
          //
          //| muOFun(str fuid)                                    // *Rascal* function, i.e., overloaded function at the root
          //
          //| muConstr(str fuid)                                  // Constructor
          
// Variables

Result eval(muLoc(str name, int pos), Env env)                   // Local variable, with position in current scope
    =  <env[0].stack[pos], env>;                                
   
Result eval(muVar(str name, str fuid, int pos), Env env)        // Variable: retrieve its value
    =   <getVariable(name, fuid, pos, env), env>;               

Result eval(muTmp(str name, str fuid) , Env env)             // Temporary variable introduced by front-end
    = < getTmp(name, fuid, env), env>;

// Keyword parameters

         // muLocKwp(str name)                                  // Local keyword parameter

Result eval(muVarKwp(str fuid, str name), Env env){                        // Keyword parameter
    return getKwp(name, fuid, env);
} 

Result eval(muAssignKwp(str name, str fuid, MuExp exp), Env env){
    <v, env> = eval(exp, env);
    return assignKwp(name, fuid, v, env);
}

Result eval(muTypeCon(AType tp), Env env)                            // Type constant
    = <rtype(tp), env>;


// Call/Apply/return      
    
//          | muCall(MuExp fun, list[MuExp] largs)                 // Call a *muRascal function
//          | muApply(MuExp fun, list[MuExp] largs)                // Partial *muRascal function application
//          

Result eval(muOCall3(MuExp fun, list[MuExp] largs, loc src), Env env){       // Call a declared *Rascal function
    actuals = for(arg <- largs){
                <v, env> = eval(arg, env);
                append v;
            }
    if(muOFun(str fname) := fun){
        for(<str name, AType funType, str oname, list[str] ofunctions, list[str] oconstructors> <- overloadedFunctions){
            if(fname == oname){
                for(of <- ofunctions){
                    try {
                        return call(muFunctions[of], actuals, env);
                    } catch doFailReturn(): /* try next alternative */;
                }
                throw "No applicable alternative for overloaded function <fname>";
            }
        }
        throw throw "No overloading alternatives found for <fname>";
    }
    if(muCon(str s) := fun){
        if(rvalue(map[str,RValue] kwmap) := actuals[-1]){
            return <rvalue(makeNode(s, [v | rvalue(v) <- actuals[0..-1]], keywordParameters = (k : v | k <- kwmap, rvalue(v) := kwmap[k]))), env>;
        }
        throw "mOCall3: kwmap, <actuals>";
    }
}


//          | muOCall4(MuExp fun, AType types,                    // Call a dynamic *Rascal function
//                               list[MuExp] largs, loc src)

  
Result eval(muCallPrim2(str name, loc src), Env env){                // Call a Rascal primitive function (with empty list of arguments)
    return evalPrim(name, env);
}
 
Result eval(muCallPrim3(str name, list[MuExp] exps, loc src), Env env){     // Call a Rascal primitive function
    actuals = for(exp <- exps){
                <result, env> = eval(exp, env);
                append result;
            }
    return evalPrim(name, actuals, env);
}
 
Result eval(muCallMuPrim(str name, list[MuExp] exps) , Env env){           // Call a muRascal primitive function
    actuals = for(exp <- exps){
                <result, env> = eval(exp, env);
                append result;
            }
    if(debug) println("muCallMuPrim: <name>, <actuals>");
    return evalMuPrim(name, actuals, env);
}
 
//          | muCallJava(str name, str class, 
//                       AType parameterTypes,
//                       AType keywordTypes,
//                       int reflect,
//                       list[MuExp] largs)                       // Call a Java method in given class

 
Result eval(muReturn0(), Env env){                                 // Return from a function without value
    throw doReturn(undefined(), env);
}
    
Result eval(muReturn1(MuExp exp), Env env){                       // Return from a function with value
    <result, env1> = eval(exp, env);
    throw doReturn(result, env1);
}
          
//          | muFilterReturn()                                    // Return for filer statement

Result eval(muKwpDefaults(lrel[str name, AType tp, MuExp defaultExp] kwpMap), Env env){
   resMap = ();
   for(<str name, AType tp, MuExp defaultExp> <- kwpMap){
        <v, env> = eval(defaultExp, env);
        resMap[name] = <tp, v>;
   }
   return <rvalue(resMap), env>;
}

Result eval(muKwpActuals(lrel[str name, MuExp exp] kwpActuals), Env env){
    resMap = ();
    for(<str name, MuExp exp> <- kwpActuals){
        <v, env> = eval(exp, env);
        resMap[name] = v;
    }
    return <rvalue(resMap), env>;
}
      
//          | muInsert(MuExp exp)                                 // Insert statement           

// Assignment, If and While
             
// muAssignLoc(str name, int pos, MuExp exp)           // Assign a value to a local variable

Result eval(muAssign(str name, str fuid, int pos, MuExp exp), Env env){    // Assign a value to a variable
    <v, env> = eval(exp, env);
    return assignVariable(name, fuid, pos, v, env);
}

Result eval(muAssignTmp(str name, str fuid, MuExp exp) , Env env){         // Assign to temporary variable introduced by front-end         
    <v, env> = eval(exp, env);
    return assignTmp(name, fuid, v, env);
}   
                                                             
Result eval(muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart), Env env){// If-then-else expression
    <b, env> = eval(cond, env);
    return rvalue(true) := b ? eval(thenPart, env) : eval(elsePart, env);
}

Result eval(muIfelse(str flabel, MuExp cond, MuExp thenPart, MuExp elsePart), Env env){// If-then-else expression
    <b, env> = eval(cond, env);
    if(rvalue(true) := b){
        try {
            return eval(thenPart, env);
        } catch doFail(str flabel1, Env env1): {
            if(flabel1 == flabel){
                return eval(elsePart, env1);
            } else {
                throw doFail(flabel1, env1);
            }
        }
    } else {
        return eval(elsePart, env);
    }
}

Result eval(muIf(MuExp cond,  MuExp thenPart), Env env){ // If-then expression
    <b, env> = eval(cond, env);
    return rvalue(true) := b ? eval(thenPart, env) : <rvalue(undefined()), env>;
}
                                
Result eval(mw: muWhile(str bclabel, MuExp cond, MuExp body), Env env){    // While-Do expression
    <b, env> = eval(cond, env);
    while(rvalue(true) := b) {
        try {
            <v, env> = eval(body, env);
            <b, env> = eval(cond, env);
       } catch doBreak(bclabel): return <undefined(), env>;
         catch doContinue(bclabel): return eval(mw, env);
    }
    return <undefined(), env>;
}

Result eval(mw: muForEach(str btscope, str varName, str fuid, MuExp iterable, MuExp body/*, MuExp thenPart, MuExp elsePart*/), Env env){    // for-each expression
    <rvalue(iterable_val), env> = eval(iterable, env);
    the_iterator = makeIterator(iterable_val);
    while(the_iterator.hasNext()){
        v = the_iterator.getNext();
        <v, env> = assignTmp(varName, fuid, v, env);
        try {
            <v, env> = eval(body, env);
       } catch doBreak(btscope, env1): return <undefined(), env1>;
         catch doContinue(btscope, env1): return eval(mw, env1);
         catch doFail(btscope, env1): ;
    }
    return <undefined(), env>;
}

Result eval(mw: muFor(str label, str varName, str fuid, MuExp fromExp, MuExp byExp, MuExp toExp, list[MuExp] body), Env env){    // for  expression
    <rvalue(from), env> = eval(fromExp, env);
    <rvalue(by), env> = eval(byExp, env);
    <rvalue(to), env> = eval(toExp, env);
    result = undefined();
    while(i < to){
        <v, env> = assignTmp(varName, fuid, rvalue(i), env);
        try {
            <result, env> = eval(body, env);
            i += by;
       } catch doBreak(label): return <undefined(), env>;
         catch doContinue(label): return eval(mw, env);
         catch doFail(label): ;
    }
    return <result, env>;
}

Result eval(muForRange(str label, str varName, str fuid, MuExp first, MuExp second, MuExp last, MuExp exp), Env env){
    <rvalue(vfirst), env> = eval(first, env);
    <rvalue(vsecond), env> = eval(second, env);
    <rvalue(vlast), env> = eval(last, env);
    result = undefined();
    if(num nfirst := vfirst, num nsecond := vsecond, num nlast := vlast){
        if(nsecond == 0){
            nsecond = nfirst < nlast ? nfirst + 1 : nfirst - 1;
        }
        for(x <- [nfirst, nsecond .. nlast]){
            <v, env> = assignTmp(varName, fuid, rvalue(x), env);
            try {
                <result, env> = eval(exp, env);
            } catch doBreak(label): return <undefined(), env>;
              catch doContinue(label): return eval(mw, env);
              catch doFail(label): ;
        }
        return <result, env>;
    } else 
        throw "muForRange: <vfirst>, <vsecond>, <vlast>";
}
         
//          | muTypeSwitch(MuExp exp, list[MuTypeCase] type_cases, MuExp \default)        // switch over cases for specific type   

Result eval(muSwitch(MuExp exp, bool useConcreteFingerprint, list[MuCase] cases, MuExp defaultExp), Env env){      // switch over cases for specific value
    <rvalue(swval), env> = eval(exp, env);
    fp = getFingerprint(swval, useConcreteFingerprint);
    for(muCase(int fingerprint, MuExp exp) <- cases){
        println("fp = <fp>; case <fingerprint>, <exp>");
        if(fingerprint == fp){
            println("found");
            return eval(exp, env);
        }
    }
    return eval(defaultExp, env);
} 

Result eval(muEndCase(), Env env){                                        // Marks the exit point of a case
    throw "muEndCase cannot be executed";
}

data MuCatch = muCatch(str id, str fuid, AType \type, MuExp body);    

data MuTypeCase = muTypeCase(str name, MuExp exp);
data MuCase = muCase(int fingerprint, MuExp exp);     


Result eval(muBreak(str label), Env env){                                  // Break statement
    throw doBreak(label, env);
}

Result eval(muContinue(str label), Env env){                              // Continue statement
    throw doContinue(labe, env);
}

Result eval(muFail(str label), Env env){                                   // Fail statement
    throw doFail(label, env);
}

Result eval(muFailReturn(), Env env){                                      // Failure from function body
    throw doFailReturn();
}
          
// Multi-expressions
         
Result eval(muBlock(list[MuExp] exps), Env env)                   // A list of expressions, only last value remains
    = eval(exps, env);
    
Result eval(muBlockWithTmps(lrel[str name, str fuid] tmps, lrel[str name, str fuid] tmpRefs, list[MuExp] exps), Env env) // A block with scoped temporary variables and temporaries used as reference
    = eval(exps, env); // TODO: implement fully

//          | muMulti(MuExp exp)                                  // Expression that can produce multiple values
//          | muOne1(MuExp exp)                                   // Expression that always produces only the first value
//          
//          // Exceptions
//          
//          | muThrow(MuExp exp, loc src)
//          
//          // Exception handling try/catch
//          
//          | muTry(MuExp exp, MuCatch \catch, MuExp \finally)
//          
//          | muVisit(bool direction, bool fixedpoint, bool progress, bool rebuild, MuExp descriptor, MuExp phi, MuExp subject, MuExp refHasMatch, MuExp refBeenChanged, MuExp refLeaveVisit, MuExp refBegin, MuExp refEnd)
//    

Result eval(list[MuExp] exps, env){ 
    result = undefined();
    for(exp <- exps){
        if(debug) println("eval: <exp>");
        <result, env> = eval(exp, env);
    }
    return <result, env>;
}

//          

Result eval(muEqual(MuExp exp1, MuExp exp2), Env env){
    <rvalue(v1), env> = eval(exp1, env);
    <rvalue(v2), emv> = eval(exp2, env);
    return <v1 == v2 ? rvalue(true) : rvalue(false), env>;
 }
    
Result eval(muHasType(str typeName, MuExp exp), Env env){
    <rvalue(v), env> = eval(exp, env);
    return <getName(typeof(v)) == typeName ? rvalue(true) : rvalue(false), env>;
}

Result eval(muValueIsSubType(MuExp exp, AType tp), Env env){
    <rvalue(v), env> = eval(exp, env);
    return <subtype(typeOf(v), atype2symbol(tp)) ? rvalue(true) : rvalue(false), env>;
}

Result eval(muHasTypeAndArity(str typeName, int arity, MuExp exp), Env env){
    <rvalue(v), env> = eval(exp, env);
    if(getName(typeOf(v)) != typeName) return <rvalue(false), env>;
    return <getSize(v) == arity ? rvalue(true) : rvalue(false), env>;
}

Result eval(muHasNameAndArity(str name, int arity, MuExp exp), Env env){
    <rvalue(v), env> = eval(exp, env);
    if(node nd := v){
        if(getName(nd) != name) return <rvalue(false), env>;
        return <getSize(nd) == arity ? rvalue(true) : rvalue(false), env>;
    }
    throw "muHasNameAndArity: <name>, <arity>, <v>";
}

int getSize(value v){
    int n = 0;
    switch(v){
        case list[&T] lst: n = size(lst);
        case set[&T] st: n = size(st);
        case map[&K,&V] mp: n = size(mp);
        case node nd: n = arity(nd);
        case tuple[&A] tup: n = 1;
        case tuple[&A,&B] tup: n = 2;
        case tuple[&A,&B,&C] tup: n = 3;
        case tuple[&A,&B,&C,&D] tup: n = 4;
        case tuple[&A,&B,&C,&D,&E] tup: n = 5;
        case tuple[&A,&B,&C,&D,&E,&F] tup: n = 6;
        case tuple[&A,&B,&C,&D,&E,&F,&G] tup: n = 7;
        case tuple[&A,&B,&C,&D,&E,&F,&G,&H] tup: n = 8;
        case tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I] tup: n = 9;
        case tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I,&J] tup: n = 10;
        default: throw "muHasTypeAndArity: <typeName>, <arity>, <v>";    
    }
    return n;
}

Result eval(muSize(MuExp exp), Env env){
    <rvalue(v), env> = eval(exp, env);    
    return <rvalue(getSize(v)), env>;
}

Result eval(muSubscript(MuExp exp, MuExp idx), Env env){
    <rvalue(v), env> = eval(exp, env);
    <rvalue(vidx), env> = eval(idx, env);
    if(int n := vidx){
       switch(v){
            case list[&T] lst: return <rvalue(lst[n]), env>;
            case node nd: return <rvalue(getChildren(nd)[n]), env>;
            case tuple[&A] tup: return <rvalue(tup[n]), env>;
            case tuple[&A,&B] tup: return <rvalue(tup[n]), env>;
            case tuple[&A,&B,&C] tup: return <rvalue(tup[n]), env>;
            case tuple[&A,&B,&C,&D] tup: return <rvalue(tup[n]), env>;
            case tuple[&A,&B,&C,&D,&E] tup: return <rvalue(tup[n]), env>;
            case tuple[&A,&B,&C,&D,&E,&F] tup: return <rvalue(tup[n]), env>;
            case tuple[&A,&B,&C,&D,&E,&F,&G] tup: return <rvalue(tup[n]), env>;
            case tuple[&A,&B,&C,&D,&E,&F,&G,&H] tup: return <rvalue(tup[n]), env>;
            case tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I] tup: return <rvalue(tup[n]), env>;
            case tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I,&J] tup:return <rvalue(tup[n]), env>;
            default: throw "muSubscript: <v>, <n>";    
        }
    }
    throw "muSubscript: non-integer index <vidx>";    
}

Result eval(muInc(str tmpName, str fuid, MuExp exp), Env env){
    <v, env> = eval(exp, env);
    vtmp = getValue(tmpName, fuid, env);
    if(rvalue(int n) := vtmp, int inc := v){
        return assignTmp(tmpName, fuid, rvalue(n + inc), env);
    }
    throw "muInc: <tmpName>, <vtmp>, <v>";
}

Result eval(muSub(MuExp exp1, MuExp exp2), Env env){
    <rvalue(v1), env> = eval(exp1, env);
    <rvalue(v2), env> = eval(exp2, env);
    if(int n1 := v1, int n2 := v2){
        return <rvalue(n1 - n2), env>;
    }
    throw "muSub: <v1>, <v2>";
}

Result eval(muAdd(MuExp exp1, MuExp exp2), Env env){
    <rvalue(v1), env> = eval(exp1, env);
    <rvalue(v2), env> = eval(exp2, env);
    if(int n1 := v1, int n2 := v2){
        return <rvalue(n1 + n2), env>;
    }
    throw "muAdd: <v1>, <v2>";
}

Result eval(muGreaterEq(MuExp exp1, MuExp exp2), Env env){
    <rvalue(v1), env> = eval(exp1, env);
    <rvalue(v2), env> = eval(exp2, env);
    if(int n1 := v1, int n2 := v2){
        return <rvalue(n1 >= n2), env>;
    }
    throw "muGreaterEq: <v1>, <v2>";
}

Result eval(muAnd(MuExp exp1, MuExp exp2), Env env){
    <rvalue(v1), env> = eval(exp1, env);
    <rvalue(v2), env> = eval(exp2, env);
    if(bool b1 := v1, bool b2 := v2){
        return <rvalue(b1 && b2), env>;
    }
    throw "muAnd: <v1>, <v2>";
}

Result eval(muSubList(MuExp exp1, MuExp exp2), Env env){
    <rvalue(vlst), env> = eval(exp1, env);
    <rvalue(vlen), env> = eval(exp2, env);
    if(list[&T] lst := vlst, int len := vlen){
        return <rvalue(lst[0 .. len+1]), env>;
    }
    throw "muSubList: <vlist>, <vlen>";
}

Result eval(muPrintln(str s), Env env){
    println(s);
    return <undefined(), env>;
}

Result eval(muHasKeywordArg(MuExp exp, str kwname), Env env){
    <rvalue(v), env> = eval(exp, env);
    if(node nd := v){
        if(map[str, RValue] kwmap := getChildren(nd)[-1]){
            return <kwmap[kwname]? ? rvalue(true) : rvalue(false) , env>;
        }
    }
    return <rvalue(false), env>;
}

Result eval(muGetKeywordArg(MuExp exp, str kwname), Env env){
    <rvalue(v), env> = eval(exp, env);
    if(node nd := v){
        if(map[str, RValue] kwmap := getChildren(nd)[-1]){
            return <kwmap[kwname], env>;
        }
    }
}

// MuPrimitives

Result evalMuPrim("check_arg_type_and_copy", [rvalue(int from), rtype(AType tp), rvalue(int to)], Env env){
    arg = env[0].stack[from];
    if(getType(arg) != tp) return <rvalue(false), env>;
    env[0].stack[to] = arg;
    return <rvalue(true), env>;
}

AType getType(rvalue(v)) = getType(v);

AType getType(int n) = aint();
AType getType(list[&T] lst) = isEmpty(lst) ? alist(avoid()) : alist(getType(typeof(lst[0]))); // TODO

// Rascal primitives

Result evalPrim("aint_add_aint", [rvalue(int x), rvalue(int y)], Env env)           = <rvalue(x + y), env>;
Result evalPrim("aint_product_aint", [rvalue(int x), rvalue(int y)], Env env)       = <rvalue(x * y), env>;
Result evalPrim("aint_greater_aint", [rvalue(int x), rvalue(int y)], Env env)       = <rvalue(x > y), env>;
Result evalPrim("aint_greaterequal_aint", [rvalue(int x), rvalue(int y)], Env env)  = <rvalue(x >= y), env>;
Result evalPrim("aint_less_aint", [rvalue(int x), rvalue(int y)], Env env)          = <rvalue(x < y), env>;
Result evalPrim("aint_lessequal_aint", [rvalue(int x), rvalue(int y)], Env env)     = <rvalue(x <= y), env>;
Result evalPrim("equal", [rvalue(value x), rvalue(value y)], Env env)               = <rvalue(x == y), env>;

Result evalPrim("list_create", list[RValue] elms, Env env)
    = <rvalue([ v | rvalue(v) <- elms]), env>;
    
Result evalPrim("set_create", list[RValue] elms, Env env)
    = <rvalue({ v | rvalue(v) <- elms}), env>;
    
Result evalPrim("tuple_create", [rvalue(v1)], Env env)
    = <rvalue(<v1>), env>;
    
Result evalPrim("tuple_create", [rvalue(v1), rvalue(v2)], Env env)
    = <rvalue(<v1, v2>), env>;
    
Result evalPrim("tuple_create", [rvalue(v1), rvalue(v2), rvalue(v3)], Env env)
    = <rvalue(<v1, v2, v3>), env>;
    
Result evalPrim("tuple_create", [rvalue(v1), rvalue(v2), rvalue(v3), rvalue(v4)], Env env)
    = <rvalue(<v1, v2, v3, v4>), env>;
    
Result evalPrim("tuple_create", [rvalue(v1), rvalue(v2), rvalue(v3), rvalue(v4), rvalue(v5)], Env env)
    = <rvalue(<v1, v2, v3, v4, v5>), env>;

Result evalPrim("tuple_create", [rvalue(v1), rvalue(v2), rvalue(v3), rvalue(v4), rvalue(v5), rvalue(v6)], Env env)
    = <rvalue(<v1, v2, v3, v4, v5, v6>), env>;

Result evalPrim("tuple_create", [rvalue(v1), rvalue(v2), rvalue(v3), rvalue(v4), rvalue(v5), rvalue(v6), rvalue(v7)], Env env)
    = <rvalue(<v1, v2, v3, v4, v5, v6, v7>), env>;
    
Result evalPrim("tuple_create", [rvalue(v1), rvalue(v2), rvalue(v3), rvalue(v4), rvalue(v5), rvalue(v6), rvalue(v7), rvalue(v8)], Env env)
    = <rvalue(<v1, v2, v3, v4, v5, v6, v7, v8>), env>;

Result evalPrim("tuple_create", [rvalue(v1), rvalue(v2), rvalue(v3), rvalue(v4), rvalue(v5), rvalue(v6), rvalue(v7), rvalue(v8), rvalue(v9)], Env env)
    = <rvalue(<v1, v2, v3, v4, v5, v6, v7, v8, v9>), env>;
    
Result evalPrim("tuple_create", [rvalue(v1), rvalue(v2), rvalue(v3), rvalue(v4), rvalue(v5), rvalue(v6), rvalue(v7), rvalue(v8), rvalue(v9), rvalue(v10)], Env env)
    = <rvalue(<v1, v2, v3, v4, v5, v6, v7, v8, v, v10>), env>;   
    
Result evalPrim("node_create", [rvalue(str name), *RValue args, rvalue(map[str, RValue] kwmap)], Env env)
    = makeNode(name, [v | rvalue(v) <- args], keywordParameters = (k : v | k <- kwmap, bprint(v), rvalue(v) := kwmap[k]));

// ListWriter

data Writer
    = writer(
        void (value v) add,
        void (value v) splice,
        RValue() close
      );
      
Writer makeListWriter(){
    list[value] lst = [];
    void add(value v) { lst += [v]; }
    void splice(value elms) { if(list[value] lelms:= elms) lst += lelms; else throw "ListWriter.splice: <elms>"; }
    RValue close() = rvalue(lst);
    
    return writer(add, splice, close);
}

Result evalPrim("listwriter_open", [], Env env)
    = <rvalue(makeListWriter()), env>;
   
Result evalPrim("listwriter_add", [rvalue(Writer w), rvalue(v)], Env env){
    w.add(v);
    return <rvalue(w), env>;
}

Result evalPrim("listwriter_splice",  [rvalue(Writer w), rvalue(v)], Env env){
    w.splice(v);
    return <rvalue(w), env>;
}
    
Result evalPrim("listwriter_close",  [rvalue(Writer w)], Env env)
    = <rvalue(w.close()), env>;

// SetWriter

Writer makeSetWriter(){
    set[value] st = {};
    void add(value v) { st += {v}; }
    void splice(value elms) { if(set[value] selms := elms) st += elms; else throw "SetWriter.splice: <elms>"; }
    RValue close() = rvalue(st);
    
    return writer(add, splice, close);
}

Result evalPrim("setwriter_open", [], Env env)
    = <rvalue(makeSetWriter()), env>;
   
Result evalPrim("setwriter_add", [rvalue(Writer w), rvalue(v)], Env env){
    w.add(v);
    return <rvalue(w), env>;
}

Result evalPrim("setwriter_splice",  [rvalue(Writer w), rvalue(v)], Env env){
    w.splice(v);
    return <rvalue(w), env>;
}
    
Result evalPrim("setwriter_close",  [rvalue(Writer w)], Env env)
    = <rvalue(w.close()), env>;
    
// MapWriter

Writer makeMapWriter(){
    map[value, value] mp = ();
    void add(value v) { if(<key, val> := v) mp[key] = val; else throw "MapWriter.add: <v>"; }
    void splice(value elms) { if(set[value] selms := elms) st += elms; else throw "MapWriter.splice: <elms>"; }
    RValue close() = rvalue(mp);
    
    return writer(add, splice, close);
}

Result evalPrim("mapwriter_open", [], Env env)
    = <rvalue(makeMapWriter()), env>;
   
Result evalPrim("mapwriter_add", [rvalue(Writer w), rvalue(key), rvalue(val)], Env env){
    w.add(<key,val>);
    return <rvalue(w), env>;
}

Result evalPrim("mapwriter_splice",  [rvalue(Writer w), rvalue(v)], Env env){
    w.splice(v);
    return <rvalue(w), env>;
}
    
Result evalPrim("mapwriter_close",  [rvalue(Writer w)], Env env)
    = <rvalue(w.close()), env>;

    
// Iterators for all Rascal data types

data Iterator
    = iterator(
        bool () hasNext,
        RValue () getNext
    );

Iterator makeIterator(list[&T] elems){
    int i = 0;
    bool hasNext() = i < size(elems);
    RValue getNext() { result = elems[i]; i += 1; return rvalue(result); }
    
    return iterator(hasNext, getNext);
}

Iterator makeIterator(set[&T] elems) 
    = makeIterator(toList(elems));

Iterator makeIterator(map[&K,&V] m) 
    = makeIterator(toList(domain(m)));

Iterator makeIterator(node nd) 
    = makeIterator([x | x <- nd]);

Iterator makeIterator(tuple[&A,&B] tup) 
    = makeIterator([tup[0], tup[1]]);

Iterator makeIterator(tuple[&A,&B,&C] tup)
    = makeIterator([tup[0], tup[1], tup[2]]);

Iterator makeIterator(tuple[&A,&B,&C,&D] tup)
    = makeIterator([tup[0], tup[1], tup[2], tup[3]]);

Iterator makeIterator(tuple[&A,&B,&C,&D,&E] tup)
    = makeIterator([tup[0], tup[1], tup[2], tup[3], tup[4]]);

Iterator makeIterator(tuple[&A,&B,&C,&D,&E,&F] tup)
    = makeIterator([tup[0], tup[1], tup[2], tup[3], tup[4], tup[5]]);
    
Iterator makeIterator(tuple[&A,&B,&C,&D,&E,&F,&G] tup)
    = makeIterator([tup[0], tup[1], tup[2], tup[3], tup[4], tup[5], tup[6]]);

Iterator makeIterator(tuple[&A,&B,&C,&D,&E,&F,&G,&H] tup)
    = makeIterator([tup[0], tup[1], tup[2], tup[3], tup[4], tup[5], tup[6], tup[7]]);

Iterator makeIterator(tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I] tup)
    = makeIterator([tup[0], tup[1], tup[2], tup[3], tup[4], tup[5], tup[6], tup[7], tup[8]]);

Iterator makeIterator(tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I,&J] tup)
    = makeIterator([tup[0], tup[1], tup[2], tup[3], tup[4], tup[5], tup[6], tup[7], tup[8], tup[9]]);
