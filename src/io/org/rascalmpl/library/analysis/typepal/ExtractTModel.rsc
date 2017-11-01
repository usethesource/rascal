@license{
Copyright (c) 2017, Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
}
module analysis::typepal::ExtractTModel

import Node;
import ParseTree;
import String;
extend analysis::typepal::ScopeGraph;
extend analysis::typepal::AType;
//import rascal::ATypeUtils;

data RuntimeException
    = TypePalUsage(str reason)
    | TypePalInternalError(str reason)
    | TypeUnavailable()
    ;

// ScopeRole: the various (language-specific) roles scopes can play.
// Initially ScopeRole only provides the rootScope but is extended in a language-specific module

data ScopeRole
    = anonymousScope()
    ;

loc getLoc(Tree t) = t@\loc ? t.args[0]@\loc;

RuntimeException checkFailed(Tree where, str msg) = checkFailed({ error(msg, getLoc(where)) });
RuntimeException checkFailed(loc where, str msg) = checkFailed({ error(msg, where) });

// Extract (nested) tree locations and type variables from a list of dependencies
list[Key] dependenciesAsKeyList(list[value] dependencies){
    return 
        for(d <- dependencies){
            if(Tree t := d){
                append getLoc(t);
            } else if(tvar(tv) := d){
                append tv;
            } else {
                throw TypePalUsage("Dependency should be a tree or type variable, found <d>");
            }
        };
} 

set[Key] dependenciesAsKeys(list[value] dependencies)
    = toSet(dependenciesAsKeyList(dependencies));

// Definition info used during type checking
data DefInfo
    = defType(AType atype)                                                    // Explicitly given AType
    | defType(set[Key] dependsOn, AType() getAType)                           // AType given as callback.
    | defLub(list[AType] atypes)                                              // redefine previous definition
    | defLub(set[Key] dependsOn, set[Key] defines, list[AType()] getATypes)   // redefine previous definition
    ;

DefInfo defType(list[value] dependsOn, AType() getAType)
    = defType(dependenciesAsKeys(dependsOn), getAType);
    
DefInfo defLub(list[value] dependsOn, AType() getAType)
    = defLub(dependenciesAsKeys(dependsOn), {}, [getAType]);
    
// Errors found during type checking  
data ErrorHandler
    = onError(loc where, str msg)
    | noError()
    ;
   
ErrorHandler onError(Tree t, str msg) = onError(getLoc(t), msg);

str fmt(AType t, bool quoted = true)            = quoted ? "`<prettyPrintAType(t)>`" : prettyPrintAType(t);
str fmt(str s, bool quoted = true)              = quoted ? "`<s>`" : s;
str fmt(int n, bool quoted = true)              = "<n>";
str fmt(list[value] vals, bool quoted = true)   = intercalateAnd([fmt(vl) | vl <- vals]);
str fmt(set[value] vals, bool quoted = true)    = intercalateAnd([fmt(vl) | vl <- vals]);
str fmt(int n, str descr, bool quoted = true)   = n == 1 ? "<n> <descr>" : "<n> <descr>s";
default str fmt(value v, bool quoted = true)    = quoted ? "`<v>`" : "<v>";

void reportError(Tree t, str msg){
    throw checkFailed({error(msg, getLoc(t))});
}

void reportWarning(Tree t, str msg){
    throw checkFailed({warning(msg, getLoc(t))});
}

void reportInfo(Tree t, str msg){
    throw checkFailed({info(msg, getLoc(t))});
}

// The basic ingredients for type checking: facts, requirements and overloads

// Facts about location src, given dependencies and an AType callback
data Fact
    = openFact(loc src, set[loc] dependsOn, AType() getAType)
    | openFact(set[loc] srcs, set[loc] dependsOn, list[AType()] getATypes)
    ;

// A named requirement for location src, given dependencies and a callback predicate
// Eager requirements are tried when not all dependencies are known.
data Requirement
    = openReq(str name, loc src, set[loc] dependsOn, bool eager, void() preds);

// Named type calculator for location src, given args, and resolve callback 
// Eager calculators are tried when not all dependencies are known.   
data Calculator
    = calculate(str name, loc src, set[loc] dependsOn, bool eager, AType() calculator);

// The basic Fact & Requirement Model; can be extended in specific type checkers
data TModel (
        map[loc,Calculator] calculators = (),
        map[loc,AType] facts = (), 
        set[Fact] openFacts = {},
        set[Requirement] openReqs = {},
        map[loc,loc] tvScopes = (),
        set[Message] messages = {},
        rel[str,value] store = {},
        map[Key, Define] definitions = ()
        );

alias Key = loc;

TModel extractTModel(Tree root, TBuilder(Tree t) tBuilder = defaultTBuilder, set[Key] (TModel, Use) lookupFun = lookup){
    tb = tBuilder(root);
    collect(root, tb);
    tm = tb.build();
    tm = resolvePath(tm, lookupFun=lookupFun);
    return tm;
}

// Default definition for collect; to be overridden in a specific type checker
// for handling syntax-constructs-of-interest
default void collect(Tree currentTree, TBuilder tb){
   //println("default collect: <typeOf(currentTree)>: <currentTree>");
   collectParts(currentTree, tb);
}

private  set[str] skipSymbols = {/*"lex",*/ "layouts", "keywords", "lit", "cilit", "char-class"};

int delta = 2;
void collectParts(Tree currentTree, TBuilder tb){
   //println("collectParts: <typeOf(currentTree)>: <currentTree>");
   if(currentTree has prod && getName(currentTree.prod.def) notin skipSymbols){
       args = currentTree.args;
       int n = size(args);
       int i = 0;
       while(i < n){
        collect(args[i], tb);
        i += delta;
       }
   } 
   //else {
   // println("collectParts, skipping: <typeOf(currentTree)>: <currentTree>");
   //}
}
void collectLexicalParts(Tree currentTree, TBuilder tb){
   //println("collectParts: <typeOf(currentTree)>: <currentTree>");
   delta =1 ;
   if(currentTree has prod && getName(currentTree.prod.def) notin skipSymbols){
       args = currentTree.args;
       int n = size(args);
       int i = 0;
       while(i < n){
        collect(args[i], tb);
        i += 1;
       }
   }
   delta = 2;
}

TModel resolvePath(TModel tm, set[Key] (TModel, Use) lookupFun = lookup){
    msgs = {};
    int n = 0;

    while(!isEmpty(tm.referPaths) && n < 3){    // explain this iteration count
        n += 1;
        for(c <- tm.referPaths){
            try {
                foundDefs = lookupFun(tm, c.use);
                if({def} := foundDefs){
                   //println("resolvePath: resolve <c.use> to <def>");
                   tm.paths += {<c.use.scope, c.pathRole, def>};  
                } else {
                   msgs += error("Name <fmt(c.use.id)> is ambiguous <fmt(foundDefs)>", c.use.occ);
                }
                tm.referPaths -= {c}; 
            }
            catch:{
                println("Lookup for <c> fails"); 
                msgs += error("Name <fmt(c.use.id)> not found", c.use.occ);
            }
        }
    }
    for(c <- tm.referPaths){
        msgs += error("Reference to name <fmt(c.use.id)> cannot be resolved", c.use.occ);
    }
    tm.messages += msgs;
    return tm;
}

data TBuilder 
    = tbuilder(
        void (str id, IdRole idRole, value def, DefInfo info) define,
        void (Tree occ, set[IdRole] idRoles) use,
        void (Tree occ, set[IdRole] idRoles) useLub,
        void (Tree occ, set[IdRole] idRoles, PathRole pathRole) useViaPath,
        void (list[str] ids, Tree occ, set[IdRole] idRoles, set[IdRole] qualifierRoles) useQualified,
        void (list[str] ids, Tree occ, set[IdRole] idRoles, set[IdRole] qualifierRoles, PathRole pathRole) useQualifiedViaPath,   
        void (Tree inner) enterScope,
        void (Tree inner) leaveScope,
        void (Key scope, ScopeRole scopeRole, value info) setScopeInfo,
        lrel[Key scope, value scopeInfo] (ScopeRole scopeRole) getScopeInfo,
        Key () getScope,
       
        void (str name, Tree src, list[value] dependencies, void() preds) require,
        void (str name, Tree src, list[value] dependencies, void() preds) requireEager,
        void (Tree src, AType tp) fact,
        void (str name, Tree src, list[value] dependencies, AType() calculator) calculate,
        void (str name, Tree src, list[value] dependencies, AType() calculator) calculateEager,
        void (Tree src, str msg) reportError,
        void (Tree src, str msg) reportWarning,
        void (Tree src, str msg) reportInfo,
        AType () newTypeVar,
        void (str key, value val) store,
        set[value] (str key) getStored,
        void (TModel tm) addTModel,
        TModel () build
      ); 

AType() makeClos1(AType tp) = AType (){ return tp; };                   // TODO: workaround for compiler glitch
void() makeClosError(Tree src, str msg) = void(){ throw checkFailed(src, msg); };
void() makeClosWarning(Tree src, str msg) = void(){ throw checkFailed({ warning(msg, getLoc(src)) }); };
void() makeClosInfo(Tree src, str msg) = void(){ checkFailed({ info(msg, getLoc(src)) }); };
             
TBuilder defaultTBuilder(Tree t) = newTBuilder(t);    
         
TBuilder newTBuilder(Tree t, bool debug = false){
        
    Defines defines = {};
    Defines lubDefines = {};
    
    rel[loc scopeScope, str id, loc idScope, set[IdRole] idRoles, loc occ] lubUses = {};
    set[Key] lubScopes = {};
    //rel[loc scope, str id, IdRole idRole] lubKeys = {};
    Scopes scopes = ();
    Paths paths = {};
    ReferPaths referPaths = {};
    Uses uses = [];
    rel[str,value] storeVals = {};
    
    map[loc,Calculator] calculators = ();
    map[loc,AType] facts = ();
    set[Fact] openFacts = {};
    set[Requirement] openReqs = {};
    int ntypevar = -1;
    map[loc,loc] tvScopes = ();
    luDebug = debug;
    set[Message] messages = {};
   
    Key globalScope = |global-scope:///|;
     
    Key currentScope = globalScope; //getLoc(t);
    Key rootScope = globalScope; //currentScope;
  
    scopes[getLoc(t)] = globalScope;
    lrel[Key scope, bool lubScope, map[ScopeRole, value] scopeInfo] scopeStack = [<globalScope, false, (anonymousScope(): false)>];
    
    bool building = true;
    
    Key getCurrentLubScope(){
        for(int i <- index(scopeStack), <scope, true, map[ScopeRole,value] scopeInfo2> := scopeStack[i]){     
            return scope;
        }
        throw TypePalUsage("`getCurrentLubScope` scope cannot be found");
    }
    
    // TODO This is language dependent!
    str stripEscapes(str s) = replaceAll(s, "\\", "");
    
    void _define(str id, IdRole idRole, value def, DefInfo info){
        if(building){
            loc l;
            if(Tree tdef := def) l = getLoc(tdef);
            else if(loc ldef := def) l = ldef;
            else throw TypePalUsage("Argument `def` of `define` should be `Tree` or `loc`, found <typeOf(def)>");
            
            if(info is defLub){
                lubDefines += {<getCurrentLubScope(), id, idRole, l, info>};
                //lubKeys += <currentScope, id, idRole>;
            } else {
                defines += {<currentScope, id, idRole, l, info>};
            }
        } else {
            throw TypePalUsage("Cannot call `define` on TBuilder after `build`");
        }
    }
       
    void _use(Tree occ, set[IdRole] idRoles) {
        if(building){
           uses += use(stripEscapes("<occ>"), getLoc(occ), currentScope, idRoles);
        } else {
            throw TypePalUsage("Cannot call `use` on TBuilder after `build`");
        }
    }
    
    void _useLub(Tree occ, set[IdRole] idRoles) {
        if(building){
           lubUses += { <getCurrentLubScope(), stripEscapes("<occ>"), currentScope, idRoles, getLoc(occ)> };
        } else {
            throw TypePalUsage("Cannot call `useLub` on TBuilder after `build`");
        }
    }
    
    void _useViaPath(Tree occ, set[IdRole] idRoles, PathRole pathRole) {
        if(building){
            u = use(stripEscapes("<occ>"), getLoc(occ), currentScope, idRoles);
            uses += u;
            referPaths += {refer(u, pathRole)};
        } else {
            throw TypePalUsage("Cannot call `useViaPath` on TBuilder after `build`");
        }
    }
    
    void _useQualified(list[str] ids, Tree occ, set[IdRole] idRoles, set[IdRole] qualifierRoles){
        if(building){
           uses += useq([stripEscapes(id) | id <- ids], getLoc(occ), currentScope, idRoles, qualifierRoles);
        } else {
            throw TypePalUsage("Cannot call `useQualified` on TBuilder after `build`");
        }  
     }
     void _useQualifiedViaPath(list[str] ids, Tree occ, set[IdRole] idRoles, set[IdRole] qualifierRoles, PathRole pathRole){
        if(building){
            u = useq([stripEscapes(id) | id <- ids], getLoc(occ), currentScope, idRoles, qualifierRoles);
            uses += [u];
            referPaths += {refer(u, pathRole)};
        } else {
            throw TypePalUsage("Cannot call `useQualifiedViaPath` on TBuilder after `build`");
        } 
    }
    
    void _enterScope(Tree inner, bool lubScope=false){
        if(building){
           innerLoc = getLoc(inner);
           if(innerLoc != currentScope){
              scopes[innerLoc] = currentScope; 
              currentScope = innerLoc;
              scopeStack = push(<innerLoc, lubScope, ()>, scopeStack);
              if(lubScope) lubScopes += innerLoc;
           } else 
           if(innerLoc == rootScope){
              currentScope = innerLoc;
              scopeStack = push(<innerLoc, lubScope, ()>, scopeStack);
           } else {
              throw TypePalUsage("Cannot call `enterScope` with inner scope that is equal to currentScope");
           }
        } else {
          throw TypePalUsage("Cannot call `enterScope` on TBuilder after `build`");
        }
    }
    
    void _leaveScope(Tree inner){
        if(building){
           innerLoc = getLoc(inner);
           if(innerLoc == currentScope){
              scopeStack = tail(scopeStack);
              if(isEmpty(scopeStack)){
                 throw TypePalUsage("Cannot call `leaveScope` beyond the root scope"); 
              }
              currentScope = scopeStack[0].scope;
           } else {
              throw TypePalUsage("Cannot call `leaveScope` with a scope that is not the current scope"); 
           }
        } else {
          throw TypePalUsage("Cannot call `leaveScope` on TBuilder after `build`");
        }
    }
    
    void _setScopeInfo(Key scope, ScopeRole scopeRole, value scopeInfo){
        if(building){           
           for(int i <- index(scopeStack), <scope, lubScope, map[ScopeRole,value] scopeInfo2> := scopeStack[i]){
               scopeInfo2[scopeRole] = scopeInfo;
               //scopeStack[i] = <scope, lubScope, scopeInfo2>; TODO: why does this not work?
               if(i == 0){
                  scopeStack =  <scope, lubScope, scopeInfo2> + tail(scopeStack);
               } else {
                 scopeStack =  scopeStack[0..i] + <scope, lubScope, scopeInfo2> + scopeStack[i+1..];
               }
               //println("setScopeInfo: <i>, <scope>, <scopeRole>");
               //if(scopeRole == loopScope()){
               //  iprintln(scopeStack[i]);
               //  println("loop scope");
               //  }
               return;
           }
           throw TypePalUsage("`setScopeInfo` scope cannot be found");
        } else {
           throw TypePalUsage("Cannot call `setScopeInfo` on TBuilder after `build`");
        }
    }
    
    lrel[Key scope, value scopeInfo] _getScopeInfo(ScopeRole scopeRole){
        if(building){
            res =
                for(<Key scope, lubScope, map[ScopeRole,value] scopeInfo> <- scopeStack, scopeRole in scopeInfo){
                    append <scope, scopeInfo[scopeRole]>;
                }
            //println("getScopeInfo: <scopeRole>, <size(res)>");
            //iprintln(res);
            return res;
        } else {
           throw TypePalUsage("Cannot call `getScopeInfo` on TBuilder after `build`");
        }
    }
    
    Key _getScope(){
        if(building){
            return currentScope;
        } else {
            throw TypePalUsage("Cannot call `getScope` on TBuilder after `build`");
        }
    }
   
    void _require(str name, Tree src, list[value] dependencies, void() preds){ 
        if(building){
           openReqs += { openReq(name, getLoc(src), dependenciesAsKeys(dependencies), false, preds) };
        } else {
            throw TypePalUsage("Cannot call `require` on TBuilder after `build`");
        }
    } 
    
    void _requireEager(str name, Tree src, list[value] dependencies, void() preds){ 
        if(building){
           openReqs += { openReq(name, getLoc(src), dependenciesAsKeys(dependencies), true, preds) };
        } else {
            throw TypePalUsage("Cannot call `require` on TBuilder after `build`");
        }
    } 
    
    void _fact(Tree tree, AType tp){  
        if(building){
           openFacts += { openFact(getLoc(tree), {}, makeClos1(tp)) };
        } else {
            throw TypePalUsage("Cannot call `atomicFact` on TBuilder after `build`");
        }
    }
    
    void _calculate(str name, Tree src, list[value] dependencies, AType() calculator){
        if(building){
           calculators[getLoc(src)] = calculate(name, getLoc(src), dependenciesAsKeys(dependencies),  false, calculator);
        } else {
            throw TypePalUsage("Cannot call `calculate` on TBuilder after `build`");
        }
    }
    void _calculateEager(str name, Tree src, list[value] dependencies, AType() calculator){
        if(building){
           calculators[getLoc(src)] = calculate(name, getLoc(src), dependenciesAsKeys(dependencies),  true, calculator);
        } else {
            throw TypePalUsage("Cannot call `calculateOpen` on TBuilder after `build`");
        }
    }
    
    void _reportError(Tree src, str msg){
       if(building){
          openReqs += { openReq("error", getLoc(src), {}, true, makeClosError(src, msg)) };
       } else {
            throw TypePalUsage("Cannot call `reportError` on TBuilder after `build`");
       }
    }
    
    void _reportWarning(Tree src, str msg){
        if(building){
           openReqs += { openReq("warning", getLoc(src), {}, true, makeClosWarning(src, msg)) };
        } else {
            throw TypePalUsage("Cannot call `reportWarning` on TBuilder after `build`");
        }
    }
    
    void _reportInfo(Tree src, str msg){
        if(building){
           openReqs += { openReq("info", getLoc(src), {}, true, makeClosInfo(src, msg)) };
        } else {
            throw TypePalUsage("Cannot call `reportInfo` on TBuilder after `build`");
        }
    }
    
    AType _newTypeVar(){
        if(building){
            ntypevar += 1;
            s = right("<ntypevar>", 10, "0");
            tv = |typevar:///<s>|;
            tvScopes[tv] = currentScope;
            return tvar(tv);
        } else {
            throw TypePalUsage("Cannot call `newTypeVar` on TBuilder after `build`");
        }
    }
    
    void _store(str key, value val){
        storeVals += <key, val>;
    }
    
    set[value] _getStored(str key){
        return storeVals[key];
    }
    
    void finalizeDefines(){
    // Recall:
        // alias Define = tuple[Key scope, str id, IdRole idRole, Key defined, DefInfo defInfo];
        //rel[loc lubScope, str id, loc scope, loc idScope, set[IdRole] idRoles, loc occ] lubUses = {};
        set[Define] extra_defines = {};
        
        // Process all lubbables in lubScopes
        for(lubScope <- lubScopes){   
            lubDefsInScope = lubDefines[lubScope];
            lubUsesInScope = lubUses[lubScope];
 
            for(<str id, IdRole role> <- lubDefsInScope<0,1>){
                if({fixedDef} := defines[lubScope, id, role]){
                    // There exists a definition with fixed type, just use it instead of the lubDefines
                    for(<Key defined, DefInfo defInfo> <- lubDefsInScope[id, role]){
                        uses += use(id, defined, lubScope, {role});
                    }
                    //for(u: <id, idScope, set[IdRole] idRoles, loc occ> <- lubUsesInScope){
                    //    uses += use(id, occ, idScope, idRoles);
                    //    lubUsesInScope -= u;
                    //}
                } else {  // No definition with fixed type, collect all lubDefs and appoint a first definition
                    deps = {}; getATypes = [];
                    defineds = {};
                    loc firstDefined;
                    for(tuple[Key defined, DefInfo defInfo] info <- lubDefsInScope[id, role]){
                        defineds += info.defined;
                        if(!firstDefined? || info.defined.offset < firstDefined.offset){
                            firstDefined = info.defined;
                        }
                        deps += info.defInfo.dependsOn;
                        getATypes += info.defInfo.getATypes;
                    }
                    
                    //for(u: <id, loc idScope, set[IdRole] idRoles, loc occ> <- lubUsesInScope){
                    //    defineds += occ; 
                    //    //uses += [use(id, occ, idScope, idRoles)];
                    //    lubUsesInScope -= u;
                    //}
                  
                    res = <lubScope, id, role, firstDefined, defLub(deps - defineds, defineds, getATypes)>;
                    //println("finalizeDefines: add define:");  iprintln(res);
                    extra_defines += res;
                }
            }
            
            // Transform uncovered lubUses into ordinary uses
        
            for(u: <str id, loc idScope, set[IdRole] idRoles, loc occ> <- lubUsesInScope){
                uses += use(id, occ, idScope, idRoles);
                lubUsesInScope -= u;
            }
        }
        defines += extra_defines;
    }
    
    void _addTModel(TModel tm){
        scopes += tm.scopes;
        defines += tm.defines;
        facts += tm.facts;
    }
    
    TModel _build(){
        if(building){
           building = false;
           tm = tmodel();
           finalizeDefines();
           tm.defines = defines;
           tm.scopes = scopes;
           tm.paths = paths;
           tm.referPaths = referPaths;
           tm.uses = uses;
           
           tm.calculators = calculators;
           tm.facts = facts;
           tm.openFacts = openFacts;
           tm.openReqs = openReqs;
           tm.tvScopes = tvScopes;
           tm.store = storeVals;
           tm.definitions = ( def.defined : def | Define def <- defines);
           definesMap = ();
           for(<Key scope, str id, IdRole idRole, Key defined, DefInfo defInfo> <- defines){
                definesMap[<scope, id>] = definesMap[<scope, id>]? ?  definesMap[<scope, id>] + {<idRole, defined>} : {<idRole, defined>};
           }
           tm.definesMap = definesMap;
           tm.messages = messages;
           return tm; 
        } else {
           throw TypePalUsage("Cannot call `build` on TBuilder after `build`");
        }
    }
    
    return tbuilder(_define, 
                     _use, 
                     _useLub,
                     _useViaPath, 
                     _useQualified, 
                     _useQualifiedViaPath, 
                     _enterScope, 
                     _leaveScope,
                     _setScopeInfo,
                     _getScopeInfo,
                     _getScope,
                     _require, 
                     _requireEager,
                     _fact, 
                     //_fact, 
                     _calculate, 
                     _calculateEager,
                     _reportError, 
                     _reportWarning, 
                     _reportInfo, 
                     _newTypeVar, 
                     _store,
                     _getStored,
                     _addTModel,
                     _build); 
}
