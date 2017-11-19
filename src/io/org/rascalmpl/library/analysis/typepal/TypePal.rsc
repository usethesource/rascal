@license{
Copyright (c) 2017, Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
}
module analysis::typepal::TypePal

import Set; 
import Node;
import Map;
import IO;
import List; 
import ParseTree;
import String;
import Message;
import Exception;

import util::Benchmark;

extend analysis::typepal::ScopeGraph;
extend analysis::typepal::AType;
extend analysis::typepal::ExtractTModel;

syntax ANONYMOUS_OCCURRENCE = "anonymous_occurence";
private loc anonymousOccurrence = ([ANONYMOUS_OCCURRENCE] "anonymous_occurence")@\loc;

bool cdebug = false;

// ---- defaults for all my* functions

default bool myIsSubType(AType atype1, AType atype2) {
    throw TypePalUsage("`subtype(<atype1>, <atype2>)` called but `myIsSubType` is not specified");
}

default AType myLUB(AType atype1, AType atype2){
    throw TypePalUsage("`lub(<atype1>, <atype2>)` called but `myLUB` is not specified");
}

default AType myATypeMin(){
    throw TypePalUsage("`myATypeMin()` called but `myATypeMin` is not specified");
}

default AType myATypeMax(){
    throw TypePalUsage("`myATypeMax()` called but `myATypeMax` is not specified");
}

default bool myMayOverload(set[Key] defs, map[Key, Define] defines) = false;

// --- Error handling

str fmt(Tree t)  = "`<prettyPrintAType(getType(t))>`";

Message error(Tree t, str msg) = error(msg, getLoc(t));

Message warning(Tree t, str msg) = warning(msg, getLoc(t));

Message info(Tree t, str msg) = info(msg, getLoc(t));

bool reportError(Tree t, str msg){
    throw checkFailed({error(msg, getLoc(t))});
}

bool reportErrors(set[Message] msgs){
    throw checkFailed(msgs);
}

set[Message] filterMostPrecise(set[Message] messages){
//  = { msg | msg <- messages, !any(msg2 <- messages, surrounds(msg, msg2)) };
    tbl = ();
    for(msg <- messages){
        line = msg.at.begin.line;
        if(tbl[line]?) tbl[line] += msg; else tbl[line] = {msg};
    }
    result = {}; 
    for(line <- tbl){
        alts = tbl[line];
        result += { msg | msg <- alts, !any(msg2 <- alts, surrounds(msg, msg2)) };
    };
    return result;
}

set[Message] filterMostGlobal(set[Message] messages) = messages;
// = { msg | msg <- messages, !any(msg2 <- messages, surrounds(msg2, msg)) };
    
bool surrounds (Message msg1, Message msg2){
    // TODO: return msg1.at > msg2.at should also work but does not.
    return msg1.at.offset <= msg2.at.offset && msg1.at.offset + msg1.at.length > msg2.at.offset + msg2.at.length;
}

str intercalateAnd(list[str] strs){
    switch(size(strs)){
      case 0: return "";
      case 1: return strs[0];
      default: 
              return intercalate(", ", strs[0..-1]) + " and " + strs[-1];
      };
}

str intercalateOr(list[str] strs){
    switch(size(strs)){
      case 0: return "";
      case 1: return strs[0];
      default: 
              return intercalate(", ", strs[0..-1]) + " or " + strs[-1];
      };
}

// Global variables, used by validate and callbacks (define, require, etc.)

// Used outside validate
TModel extractedTModel;

map[loc, AType] facts = ();

set[Fact] openFacts = {};
set[Requirement] openReqs = {};
map[loc, AType] bindings = ();
map[loc, set[Requirement]] triggersRequirement = ();
map[loc, set[Fact]] triggersFact = ();

set[Requirement] requirementJobs = {};

set[Message] messages = {};

set[Key] (TModel, Use) lookupFun = lookup;

TModel getTModel(){
    return extractedTModel[facts=facts];
}

void clearBindings() { bindings = (); }

AType lub(AType t1, AType t2) = lub([t1, t2]);

AType lub(list[AType] atypes) {
    atypes = toList(toSet(atypes));  // remove duplicates
    minType = myATypeMin();
    lubbedType = (minType | myLUB(it, t) | t <- atypes, isFullyInstantiated(t));
    tvs =  [ t | t <- atypes, tvar(v) := t ];
    other = [t | t <- atypes - tvs, !isFullyInstantiated(t) ];
    lubArgs = (lubbedType == minType ? [] : [lubbedType]) + [ t | t <- atypes, !isFullyInstantiated(t) ];
    if(size(tvs) == 1 && size(other) == 0 && lubbedType == minType){
        //println("lub <atypes> ==\> <tvs[0]>");
        return tvs[0];
    }
    if(size(tvs) >= 1 && size(other) == 0 && lubbedType != minType){
        for(tvar(v) <- tvs){
            addFact(v, lubbedType);  
        }
        //println("lub: <atypes> ==\> <lubbedType>");
        return lubbedType;
    }
    lubArgs = lubbedType + tvs + other;
    res = avalue();
    switch(size(lubArgs)){
        case 0: res = minType;
        case 1: res = lubArgs[0];
        default:
                res = lazyLub(lubArgs);
    }
    //println("lub: <atypes> ==\> <res>");
    return res;
}

void printState(){
    println("Derived facts:");
        for(Key fact <- facts){
            println("\t<fact>: <facts[fact]>");
        }
    if(size(openFacts) > 0){
        println("Unresolved facts:");
        for(Fact fact <- openFacts){
            if(fact has src){
                println("\t<fact.src>"); 
            } else {
                println("\t<fact.srcs>");
            }
            if(isEmpty(fact.dependsOn)){
                println("\t  dependsOn: nothing");
            } else {
                for(dep <- fact.dependsOn){
                    println("\t  dependsOn: <dep><facts[dep]? ? "" : " ** unavailable **">");
                }
            }
            println("\t<fact>");
        }
    }
    if(size(openReqs) > 0){
        println("Unresolved requirements:");
        for(rq <- openReqs){
            println("\t<rq.name> at <rq.src>:");
            for(atype <- rq.dependsOn){
                println("\t  dependsOn: <atype><facts[atype]? ? "" : " ** unavailable **">");
            }
        }
    }
}

bool allDependenciesKnown(set[loc] deps, bool eager)
    = isEmpty(deps) || (eager ? all(dep <- deps, facts[dep]?)
                             : all(dep <- deps, facts[dep]?, isFullyInstantiated(facts[dep])));

bool isFullyInstantiated(AType atype){
    visit(atype){
        case tvar(name): return facts[name]?;
        case lazyLub(atypes): return isEmpty(atypes) || all(AType tp <- atype, isFullyInstantiated(tp));
        case overloadedAType(overloads): all(<k, tp> <- overloads, isFullyInstantiated(tp));
    }
    return true;
}
// Find a (possibly indirect) binding
AType find(loc src){
    //println("find: <src>");
    if(bindings[src]?){
        v = bindings[src];
        if(tvar(loc src1) := v && src1 != src) return find(src1);
        return v;
    }
    if(facts[src]?){
        v = facts[src];
        if(tvar(loc src1) := v && src1 != src) return find(src1);
        return v;
    }
    if(isTypeVariable(src)) return tvar(src);
    throw NoSuchKey(src);
}

// Substitute a type variable first using bindings, then facts; return as is when there is no binding
AType substitute(tv: tvar(loc src)){
    //println("substitute: <tv>
    //        'bindings:   <bindings[src]? ? bindings[src] : "unbound">
    //        'facts:      <facts[src]? ? facts[src] : "unbound">");
    if(bindings[src]?) { b = bindings[src]; return b == tv ? tv : substitute(b); }
    if(facts[src]?) { b = facts[src]; return b == tv ? tv : substitute(b); }
    return tv;
}

default AType substitute(AType atype){
        return atype;
}

// Recursively instantiate all type variables and lazyLubs in a type
AType instantiate(AType atype){
  return
      visit(atype){
        case tv: tvar(loc src) => substitute(tv)
        case lazyLub(list[AType] atypes) : {
            sbs = [substitute(tp) | tp <- atypes];
            insert lub(sbs);
            }
      };
}

// Unification of two types, for now, without checks on variables
tuple[bool, map[loc, AType]] unify(AType t1, AType t2, map[loc, AType] bindings){
    //println("unify: <t1>, <t2>");
    if(t1 == t2) return <true, bindings>;
   
    if(tvar(loc tv1) := t1){
       if(bindings[tv1]?){
          return unify(bindings[tv1], t2, bindings);
       } else {
            return <true, (tv1 : t2) + bindings>;
       }
    }
      
    if(tvar(loc tv2) := t2){
       if(bindings[tv2]?){
          return unify(bindings[tv2], t1, bindings); 
       } else {
        return <true, (tv2 : t1) + bindings>;
      }
    }
    
    if(atypeList(atypes1) := t1){
       if(atypeList(atypes2) := t2){
          if(size(atypes1) == size(atypes2)){
            for(int i <- index(atypes1)){
                <res, bindings1> = unify(atypes1[i], atypes2[i], bindings);
                if(!res) return <res, bindings>;
                bindings += bindings1;
            }
            return <true, bindings>;
          }
       }
       return <false, ()>;
    }
    
    // TODO:introducing lazyLub in unify is an interesting idea but is it correct?
    if(lazyLub(lubbables1) := t1 && lazyLub(lubbables2) !:= t2){
        for(lb <- toSet(lubbables1)){
            if(tvar(loc tv) := lb){
               bindings += (tv : t2) + bindings;
            }
        }
        return <true, bindings>;
    }
    
    if(lazyLub(lubbables1) !:= t1 && lazyLub(lubbables2) := t2){
        for(lb <- toSet(lubbables2)){
            if(tvar(loc tv) := lb){
               bindings += (tv : t1) + bindings;
            }
        }
        return <true, bindings>;
    }
    c1 = getName(t1); c2 = getName(t2);
    a1 = arity(t1); a2 = arity(t2);
    if(c1 != c2 || a1 != a2) return <false, bindings>;
   
    kids1 = getChildren(t1); kids2 = getChildren(t2);
  
    for(int i <- [0 .. a1]){
        if(AType k1 := kids1[i], AType k2 := kids2[i]){
            <res, bindings1> = unify(k1, k2, bindings);
            if(!res) return <res, bindings>;
            bindings += bindings1;
        } else {
            if( kids1[i] != kids2[i] ){
                return <false, bindings>;
            }
        }
    }
    return <true, bindings>;
}
    
bool addFact(<Key scope, str id, IdRole idRole, Key defined, noDefInfo()>) 
    = true;
 
bool addFact(<Key scope, str id, IdRole idRole, Key defined, defType(AType atype)>) 
    = isFullyInstantiated(atype) ? addFact(defined, atype) : addFact(openFact(defined, atype));
    
bool addFact(<Key scope, str id, IdRole idRole, Key defined, defType(set[Key] dependsOn, AType() getAType)>) 
    = addFact(openFact(defined, dependsOn, getAType));

bool addFact(<Key scope, str id, IdRole idRole, Key defined, defLub(set[Key] dependsOn, set[Key] defines, list[AType()] getATypes)>) 
    = addFact(openFact(defines, dependsOn, getATypes));

default bool addFact(Define d) {  throw TypePalInternalError("Cannot handle <d>"); }


bool addFact(loc l, AType atype){
    iatype = instantiate(atype);
    facts[l] = iatype;
    if(cdebug)println(" fact <l> ==\> <iatype>");
    fireTriggers(l);
    return true;
}

set[loc] getDependencies(AType atype){
    deps = {};
    visit(atype){
        case tv: tvar(loc src) : deps += src;
    };
    return deps;
}

bool addFact(fct:openFact(loc src, AType uninstantiated)){
    try {
        facts[src] = getType(uninstantiated);
        fireTriggers(src);
        return true;
    } catch TypeUnavailable(): /* cannot yet compute type */;
    openFacts += fct;
    dependsOn = getDependencies(uninstantiated);
    for(d <- dependsOn) triggersFact[d] = (triggersFact[d] ? {}) + {fct};
    fireTriggers(src);
    return false;
}

bool addFact(fct:openFact(loc src, set[loc] dependsOn,  AType() getAType)){
    //if(cdebug)println("addFact2: <fct>");
    if(allDependenciesKnown(dependsOn, true)){
        try {
            facts[src] = getAType();
            if(cdebug)println(" fact <src> ==\> <facts[src]>");
            fireTriggers(src);
            return true;
        } catch TypeUnavailable(): /* cannot yet compute type */;
    }
    openFacts += fct;
    for(d <- dependsOn) triggersFact[d] = (triggersFact[d] ? {}) + {fct};
    fireTriggers(src);
    return false;
}

bool addFact(fct:openFact(set[loc] defines, set[loc] dependsOn, list[AType()] getATypes)){
    //if(cdebug)println("addFact: <fct>");
    if(allDependenciesKnown(dependsOn, true)){
        try {    
            tp =  (getATypes[0]() | myLUB(it, getAType()) | getAType <- getATypes[1..]);    
            for(def <- defines){ facts[def] = tp;  if(cdebug)println(" fact3 <def> ==\> <tp>");}
            for(def <- defines) { fireTriggers(def); }
            if(cdebug)println("\taddFact3: lub computed: <tp> for <defines>");
            return true;
        } catch TypeUnavailable(): /* cannot yet compute type */;
    }
    
    // try to partially compute the lub;
    //if(cdebug) println("try to partially compute the lub");
    knownTypes = ();
    solve(knownTypes){
        AType currentLub;
        for(int i <- index(getATypes)){
            try {
                knownTypes[i] = getATypes[i]();
                currentLub = currentLub? ? myLUB(currentLub, knownTypes[i]) : knownTypes[i];
            } catch TypeUnavailable(): /*println("unavailable: <i>")*/;
        }
        
        if(currentLub?){
            for(def <- defines){ facts[def] = currentLub;  }
            for(def <- defines) { 
                try fireTriggers(def, protected=false); 
                catch TypeUnavailable():
                    facts = delete(facts, def);
            }
        }
    }
    if(size(knownTypes) == size(getATypes))
        return true;
    
    if(cdebug) println("last resort");
    // last resort
    openFacts += fct;
    //if(cdebug)println("\taddFact: adding dependencies: <dependsOn>");
    for(d <- dependsOn) triggersFact[d] = (triggersFact[d] ? {}) + {fct};
    for(def <- defines) fireTriggers(def);
    return false;
}

default void addFact(Fact fct) {
    throw TypePalInternalError("Cannot handle <fct>");
}

void fireTriggers(loc l, bool protected=true){
    //if(cdebug) println("\tfireTriggers: <l>");
    
    for(fct <- triggersFact[l] ? {}){        
        if(fct has uninstantiated || allDependenciesKnown(fct.dependsOn, true)){
           try {
              //if(cdebug) println("\tfireTriggers: adding fact: <fct>");
              openFacts -= fct;
              addFact(fct);
           } catch TypeUnavailable(): {
                  /* cannot yet compute type */;
                  if(!protected){
                     throw TypeUnavailable();
                  }
              }
        }
    }
    
    for(req <- triggersRequirement[l] ? {}){
        if(allDependenciesKnown(req.dependsOn, true)){
           requirementJobs += req;
           //if(cdebug)println("\tfireTriggers: adding requirementJob: <req.name>, <req.src>");
        }
    }
}

// The binding of a type variable that occurs inside the scope of that type variable can be turned into a fact
void bindings2facts(map[loc, AType] bindings, loc occ){
   
    for(b <- bindings){
        if(cdebug) println("bindings2facts: <b>, <facts[b]?>");
        if(isTypeVariable(b) && !facts[b]? /*&& (!extractedTModel.tvScopes[b]? || occ <= extractedTModel.tvScopes[b])*/){
           addFact(b, bindings[b]);
           if(cdebug) println("bindings2facts, added: <b> : <bindings[b]>");
        } else {
           if(cdebug) println("bindings2facts, not added: <b> : !facts: <!facts[b]?>, occ: <(!extractedTModel.tvScopes[b]? || occ <= extractedTModel.tvScopes[b])>");
        }
    }
}
   
// Check whether a requirement is satisfied
tuple[bool ok, set[Message] messages, map[loc, AType] bindings] satisfies(Requirement req){
    bindings = ();
    try {
        req.preds();
        bindings2facts(bindings, req.src);
        return <true, {}, bindings>;
    } catch checkFailed(set[Message] msgs):
        return <false, msgs, bindings>;
}

// The "run-time" functions that can be called from requirements and calculators

@doc{
.Synopsis
Get type of a tree as inferred by specified type checker

.Description
xxx
}    
AType getType(Tree tree) {
    try {
        return  instantiate(find(tree@\loc));
    } catch NoSuchKey(l): {
        //println("getType: <tree@\loc> unavailable");
        throw TypeUnavailable();
    }
}

AType getType(tvar(loc l)){
    try {
        return facts[l];
    } catch NoSuchKey(k): {
        throw TypeUnavailable();
    }
}

AType getType(loc l){
    try {
        return facts[l];
    } catch NoSuchKey(k): {
        throw TypeUnavailable();
    }
}

AType getType(str id, Key scope, set[IdRole] idRoles){
    try {
        foundDefs = lookupFun(extractedTModel, use(id, anonymousOccurrence, scope, idRoles));
        if({def} := foundDefs){
           return instantiate(facts[def]);
        } else {
          if(myMayOverload(foundDefs, extractedTModel.definitions)){
                  return overloadedAType({<d, extractedTModel.definitions[d].idRole, instantiate(facts[d])> | d <- foundDefs});
          } else {
               throw AmbiguousDefinition(foundDefs);
          }
        }
     } catch NoSuchKey(k):
            throw TypeUnavailable();
       catch NoKey(): {
            println("getType: <id> in scope <scope> ==\> TypeUnavailable1");
            throw TypeUnavailable();
       }
}

Define getDefinition(Tree tree){
    try {
        return extractedTModel.definitions[getLoc(tree)];
     } catch NoSuchKey(k):
            throw TypeUnavailable();
       catch NoKey(): {
            println("getDefinition: <id> in scope <scope> ==\> TypeUnavailable1");
            throw TypeUnavailable();
       }
}

set[Define] getDefinitions(str id, Key scope, set[IdRole] idRoles){
    try {
        foundDefs = lookupFun(extractedTModel, use(id, anonymousOccurrence, scope, idRoles));
        if({def} := foundDefs){
           return {extractedTModel.definitions[def]};
        } else {
          if(myMayOverload(foundDefs, extractedTModel.definitions)){
            return {extractedTModel.definitions[def] | def <- foundDefs};
          } else {
               throw AmbiguousDefinition(foundDefs);
          }
        }
     } catch NoSuchKey(k):
            throw TypeUnavailable();
       catch NoKey(): {
            println("getDefinitions: <id> in scope <scope> ==\> TypeUnavailable1");
            throw TypeUnavailable();
       }
}

//// The "equal" predicate that succeeds or gives error
//void equal(AType given, AType expected, ErrorHandler onError){
//    if(given != expected){
//        throw checkFailed(onError.where, onError.msg);
//    }
//}

// Check the "equal" predicate
bool equal(AType given, AType expected){
    return given == expected;
}

//// The "unify" predicate that succeeds or gives error
//void unify(AType given, AType expected, ErrorHandler onError){
//    <ok, bindings1> = unify(instantiate(given), instantiate(expected), bindings);
//    if(cdebug)println("unify(<given>, <expected>) =\> <ok>, <bindings1>");
//    if(ok){
//        bindings += bindings1;
//    } else {
//        throw checkFailed(onError.where, onError.msg);
//    }
//}

// Check the "unify" predicate
bool unify(AType given, AType expected){
    if(tvar(name) := given){
        bindings[name] = expected;
            return true;
    }
    <ok, bindings1> = unify(instantiate(given), instantiate(expected), bindings);
    if(cdebug)println("unify(<given>, <expected>) ==\> <ok>, <bindings1>");
    if(ok){
        bindings += bindings1;
        return true;
    } else {
        return false;
    }
}

//// The "subtype" predicate
//void subtype(AType small, AType large, ErrorHandler onError){
//    extractedTModel.facts = facts;
//    r = myIsSubType(small, large);
//    //println("subtype: <small>, <large> ==\> <r>");
//    if(!r){
//        throw checkFailed(onError.where, onError.msg);
//    }
//}

bool subtype(AType small, AType large){
    extractedTModel.facts = facts;
    if(isFullyInstantiated(small) && isFullyInstantiated(large)){
       r = myIsSubType(small, large);
       //println("subtype: <small>, <large> ==\> <r>");
       return r;
    } else {
      throw TypeUnavailable();
    }
}

//// The "comparable" predicate
//void comparable(AType atype1, AType atype2, ErrorHandler onError){
//    extractedTModel.facts = facts;
//    if(isFullyInstantiated(atype1) && isFullyInstantiated(atype2)){
//        if(!(myIsSubType(atype1, atype2) || myIsSubType(atype2, atype1))){
//            throw checkFailed(onError.where, onError.msg);
//        }
//    } else {
//        throw TypeUnavailable();
//    }
//}

default bool comparable(AType atype1, AType atype2){
    extractedTModel.facts = facts;
    if(isFullyInstantiated(atype1) && isFullyInstantiated(atype2)){
        return myIsSubType(atype1, atype2) || myIsSubType(atype2, atype1);
    } else {
        throw TypeUnavailable();
    }
}

// The "fact" assertion
void fact(Tree t, AType atype){
        addFact(t@\loc, atype);
}

void fact(loc src, AType atype){
        addFact(src, atype);
}

// The "reportError" assertion 
void reportError(loc src, str msg){
    throw checkFailed({Message::error(msg, src)});
}

// The "reportWarning" assertion 
void reportWarning(loc src, str msg){
    throw {Message::warning(msg, src)}; // TODO FIXME
}

/*
 *  validate: validates an extracted TModel via constraint solving
 *  
 */

TModel validate(TModel er,  set[Key] (TModel, Use) lookupFun = lookup, bool debug = false){
    // Initialize global state
    extractedTModel = er;
      
    facts = extractedTModel.facts;
    openFacts = extractedTModel.openFacts;
    bindings = ();
    openReqs = extractedTModel.openReqs;
    messages = extractedTModel.messages;
    triggersRequirement = ();
    triggersFact = ();
  
    requirementJobs = {};
    lookupFun = lookupFun;
    
    //luDebug = debug;
    cdebug = debug;
    
    // Initialize local state
    map[Key, set[Key]] definedBy = ();
    map[loc, Calculator] calculators = extractedTModel.calculators;
    set[Use] openUses = {};
    
    int iterations = 0;
   
    if(cdebug){
       println("calculators: <size(calculators)>; facts: <size(facts)>; openFacts: <size(openFacts)>; openReqs: <size(openReqs)>");
       printTModel(extractedTModel);
    }
    
    if(cdebug) println("..... filter double declarations");
 
    // Check for illegal overloading in the same scope
    for(<scope, id> <- extractedTModel.defines<0,1>){
        foundDefines = extractedTModel.defines[scope, id];
        if(size(foundDefines) > 1){
           ds = {defined | <IdRole idRole, Key defined, DefInfo defInfo> <- foundDefines};
           if(!myMayOverload(ds, extractedTModel.definitions)){
              messages += {error("Double declaration of `<id>`", defined) | <IdRole idRole, Key defined, DefInfo defInfo>  <- foundDefines};
           }
        }
    }
   
    if(cdebug) println("..... lookup uses");
    
    // Check that all uses have a definition and that all overloading is allowed
    for(Use u <- extractedTModel.uses){
        try {
           foundDefs = lookupFun(extractedTModel, u);
           
           if(isEmpty(foundDefs)){
              roles = size(u.idRoles) > 3 ? "name" : intercalateOr([replaceAll(getName(idRole), "Id", "") | idRole <-u.idRoles]);
              messages += error("Undefined <roles> `<getId(u)>`", u.occ);
           } else 
           if(size(foundDefs) == 1 || myMayOverload(foundDefs, extractedTModel.definitions)){
              definedBy[u.occ] = foundDefs;
              openUses += u;
              if(cdebug) println("  use of \"<u has id ? u.id : u.ids>\" at <u.occ> ==\> <foundDefs>");
            } else {
                messages += {error("Double declaration", d) | d <- foundDefs} + error("Undefined `<getId(u)>` due to double declaration", u.occ);
                if(cdebug) println("  use of \"<u has id ? u.id : u.ids>\" at <u.occ> ==\> ** double declaration **");
            }
        }
        catch NoKey(): {
            roles = size(u.idRoles) > 5 ? "" : intercalateOr([replaceAll(getName(idRole), "Id", "") | idRole <-u.idRoles]);
            messages += error("Undefined <roles> `<getId(u)>`", u.occ);
            if(cdebug) println("  use of \"<u has id ? u.id : u.ids>\" at <u.occ> ==\> ** undefined **");
        }
    }
    
    if(cdebug) println("..... handle defines");

    for(Define d <- extractedTModel.defines){
        try addFact(d);
        catch checkFailed(set[Message] msgs):
            messages += msgs;
    }
 
    if(cdebug) println("..... handle open facts");
    for(Fact f <- openFacts){
        try {
            if(addFact(f)){
                openFacts -= f;
            }
        } catch checkFailed(set[Message] msgs): 
            messages += msgs;
    } 
    
    if(cdebug) println("..... handle open requirements");
    for(Requirement oreq <- openReqs){
       for(dep <- oreq.dependsOn){
           triggersRequirement[dep] = (triggersRequirement[dep] ? {}) + {oreq};
       }
    }

    for(Requirement oreq <- openReqs){
        if(allDependenciesKnown(oreq.dependsOn, oreq.eager)){
           requirementJobs += oreq;
        }
    }
  
    /****************** main solve loop *********************************/
    
    if(cdebug) println("..... start solving");
    int nfacts = size(facts);
    int nopenReqs = size(openReqs);
    int nopenFacts = size(openFacts);
    int nopenUses = size(openUses);
    int nrequirementJobs = size(requirementJobs);
    int ncalculators = size(calculators);
    
    solve(nfacts, nopenReqs, nopenFacts, nopenUses, nrequirementJobs, ncalculators){   
    //while(iterations < 30){
    
        iterations += 1;
        
        println("iteration: <iterations>; calculators: <size(calculators)>; facts: <size(facts)>; openFacts: <size(openFacts)>; openReqs: <size(openReqs)>");
        
       // ---- openUses
       
       openUsesToBeRemoved = {};
       
       handleOpenUses:
       for(Use u <- openUses){
           foundDefs = definedBy[u.occ];
           if (cdebug) println("Consider unresolved use: <u>, foundDefs=<foundDefs>");
           try {
               if({def} := foundDefs){  // unique definition found for use u
                   if(facts[def]?){     // has type of its definition become available?
                      addFact(u.occ, facts[def]);
                      openUsesToBeRemoved += u;
                      if(cdebug) println("  use of \"<u has id ? u.id : u.ids>\" at <u.occ> ==\> <facts[u.occ]>");
                   } else {
                      if(cdebug) println("  use of \"<u has id ? u.id : u.ids>\" at <u.occ> ==\> <facts[u.occ] ? "** unavailable **">");
                   }
                } else {                // Multiple definitions found
                    foundDefs1 = {d | d <- foundDefs, extractedTModel.definitions[d].idRole in u.idRoles}; 
                    for(dkey <- foundDefs1){
                        try  addFact(extractedTModel.definitions[dkey]);
                         catch TypeUnavailable():
                            continue handleFacts;
                    }
                    if(all(d <- foundDefs1, facts[d]?)){ 
                       addFact(u.occ, overloadedAType({<d, extractedTModel.definitions[d].idRole, instantiate(facts[d])> | d <- foundDefs1}));
                       openUsesToBeRemoved += u;
                       if(cdebug) println("  use of \"<u has id ? u.id : u.ids>\" at <u.occ> ==\> <facts[u.occ]>");
                    }
                }
            } catch checkFailed(set[Message] msgs):
                messages += msgs;
       }
       
       openUses -= openUsesToBeRemoved;
      
       // ---- calculators
      
       calculatorsToBeRemoved = {};
       for(Key calcKey <- calculators){
          calc = calculators[calcKey];
          if(allDependenciesKnown(calc.dependsOn, calc.eager)){
              try {
                t = calc.calculator();
                addFact(calcKey, t);
                bindings2facts(bindings, calc.src);
                if(cdebug) println("+calc [<calc.name>] at <calc.src> ==\> <t>"); 
              } catch TypeUnavailable(): {
                continue;
              } catch checkFailed(set[Message] msgs): {
                messages += msgs;
                if(cdebug) println("-calc [<calc.name>] at <calc.src> ==\> <msgs>");
              }
              calculatorsToBeRemoved += calcKey;
          }
       }
       calculators = domainX(calculators, calculatorsToBeRemoved);
       
       // ---- open requirements
       
       openFactsToBeRemoved = {};
       openReqsToBeRemoved = {};
       requirementJobsToBeRemoved = {};
       for(Requirement oreq <- requirementJobs){
          if(allDependenciesKnown(oreq.dependsOn, oreq.eager)){  
             try {       
                 <ok, messages1, bindings1> = satisfies(oreq); 
                 messages += messages1;
                 if(ok){
                    for(tv <- domain(bindings1), f <- triggersFact[tv] ? {}){
                        if(f has uninstantiated){
                         try {
                                if(addFact(f.src, instantiate(f.uninstantiated)))
                                   openFactsToBeRemoved += f;
                            } catch TypeUnavailable(): /* cannot yet compute type */;
                        }
                        else if(allDependenciesKnown(f.dependsOn, true)){
                            try {
                                if(addFact(f.src, f.getAType()))
                                   openFactsToBeRemoved += f;
                            } catch TypeUnavailable(): /* cannot yet compute type */;
                        }
                    }
                 }
                 if(cdebug)println("<ok ? "+" : "-">requ [<oreq.name>] at <oreq.src>");
                 openReqsToBeRemoved += oreq;
                 requirementJobsToBeRemoved += oreq;
             } catch TypeUnavailable():/* cannot yet compute type */;
           }
         }
         
         openFacts -= openFactsToBeRemoved;
         openReqs -= openReqsToBeRemoved;
         requirementJobs -= requirementJobsToBeRemoved;
         
         // ---- open facts
         
         if(cdebug) println("..... handle openFacts");
    
         openFactsToBeRemoved = {};
         for(Fact fct <- openFacts){
             try {
             	if(addFact(fct))
                    openFactsToBeRemoved += fct;
             } catch checkFailed(set[Message] msgs):
            		messages += msgs;
         }
         openFacts -= openFactsToBeRemoved;
         
        nfacts = size(facts);
        nopenReqs = size(openReqs);
        nopenFacts = size(openFacts);
        nopenUses = size(openUses);
        nrequirementJobs = size(requirementJobs);
        ncalculators = size(calculators);
       }
       
       /****************** end of main solve loop *****************************/
       
       if(cdebug) println("..... solving complete");
    
       for (Use u <- openUses) {
          foundDefs = definedBy[u.occ];
          for(def <- foundDefs){
              if (facts[def]?) {
                messages += { error("Unresolved type for `<u.id>`", u.occ)};
              }
          }
       }
    
       for(Key l <- calculators){
           calc = calculators[l];
           deps = toList(calculators[l].dependsOn);
           forDeps = isEmpty(deps) ? "" : " for <for(int i <- index(deps)){><facts[deps[i]]? ? "`<prettyPrintAType(facts[deps[i]])>`" : "`unknown type`"><i < size(deps)-1 ? "," : ""> <}>";
           messages += error("Type of <calc.name> could not be computed<forDeps>", calc.src );
       }
  
       messages += { error("Invalid <req.name>; type of one or more subparts could not be inferred", req.src) | req <- openReqs};
   
       if(cdebug){
           //println("----");
           println("iterations: <iterations>; calculators: <size(calculators)>; facts: <size(facts)>; openFacts: <size(openFacts)>; openReqs: <size(openReqs)>");
           printState();
           if(size(calculators) > 0){
               println("Unresolved calculators:");
               for(c <- calculators){
                    calc = calculators[c];
                    println("\t<calc.name> at <calc.src>:");
                    for(atype <- calc.dependsOn){
                        println("\t  dependsOn: <atype><facts[atype]? ? "" : " ** unavailable **">");
                    }
               }
           }
           
           //println("----");
           if(isEmpty(messages) && isEmpty(openReqs) && isEmpty(openFacts)){
              println("No type errors found");
           } else {
              println("Errors:");
              for(msg <- messages){
                  println(msg);
              }
              if(!isEmpty(openReqs)) println("*** <size(openReqs)> unresolved requirements ***");
              if(!isEmpty(openFacts)) println("*** <size(openFacts)> open facts ***");
           }
       }
       er.facts = facts;
       er.messages = filterMostPrecise(messages);
       
       if(cdebug) println("Derived facts: <size(er.facts)>");
       return er;
}

rel[loc, loc] getUseDef(TModel tm){
    res = {};
    for(Use u <- tm.uses){
        try {
           foundDefs =  lookup(tm, u);
           res += { <u.occ, def> | def <- foundDefs };
        } catch NoKey(): {
            ;// ignore it
        } catch AmbiguousDefinition(_):{
            ;// ignore it
        }
    };
    return res;
}

set[str] getVocabulary(TModel tm)
    = {d.id | Define d <- tm.defines};

map[loc, AType] getFacts(TModel tm)
    = tm.facts;

set[Message] getMessages(TModel tm)
    = tm.messages;
