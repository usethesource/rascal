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
import Map;
import ParseTree;
import String;
import Relation;
extend analysis::typepal::ScopeGraph;
extend analysis::typepal::AType;
//import rascal::ATypeUtils;
import util::Benchmark;
import IO;

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

//loc getLoc(Tree t) = t@\loc ? t.args[0]@\loc;

loc getLoc(Tree t)
    = t@\loc ? { fst = t.args[0]@\loc; 
                 lst = t.args[-1]@\loc;
                 fst[length=lst.offset - fst.offset + lst.length][end=lst.end]; };

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

str fmt(AType t, bool quoted = true)            = quoted ? "`<prettyPrintAType(t)>`" : prettyPrintAType(t);
str fmt(str s, bool quoted = true)              = quoted ? "`<s>`" : s;
str fmt(int n, bool quoted = true)              = "<n>";
str fmt(list[value] vals, bool quoted = true)   = isEmpty(vals) ? "nothing" : intercalateAnd([fmt(vl) | vl <- vals]);
str fmt(set[value] vals, bool quoted = true)    = isEmpty(vals) ? "nothing" :intercalateAnd([fmt(vl) | vl <- vals]);
str fmt(int n, str descr, bool quoted = true)   = n == 1 ? "<n> <descr>" : "<n> <descr>s";
default str fmt(value v, bool quoted = true)    = quoted ? "`<v>`" : "<v>";

bool reportError(Tree t, str msg){
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
    = openFact(loc src, AType uninstantiated)
    | openFact(loc src, set[loc] dependsOn, AType() getAType)
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
        map[str,value] store = (),
        map[Key, Define] definitions = ()
        );

void printTModel(TModel tm){
    println("TModel(");
    println("  defines = {");
    for(Define d <- tm.defines){
        println("    \<<d.scope>, <d.id>, <d.idRole>, <d.defined>\>"); 
    }
    println("  },");
    println("  scopes = (");
    for(Key inner <- tm.scopes){
        println("    <inner>: <tm.scopes[inner]>");
    }
    println("  ),");
    println("  paths = {");
    for(<Key from, PathRole pathRole, Key to> <- tm.paths){
        println("    \<<from>, <pathRole>, <to>\>");
    }
    println("  },");
    println("  referPath = {");
    for(c <- tm.referPaths){
        println("    <c>");
    }
    println("  },");
    //iprintln(tm.uses);
    println("  uses = [");
    for(Use u <- tm.uses){
        println("    use(<u.ids? ? u.ids : u.id>, <u.occ>, <u.scope>, <u.idRoles>, <u.qualifierRoles? ? u.qualifierRoles : "">)");
    }
    println("  ]");
    println(");");
}

alias Key = loc;

TModel extractTModel(Tree root, TBuilder(Tree t) tBuilder = defaultTBuilder, set[Key] (TModel, Use) lookupFun = lookup){
    tb = tBuilder(root);
    collect(root, tb);
    tm = tb.build();
    tm = resolvePath(tm, lookupFun=lookup);
    return tm;
}

void collect(Tree t1, Tree t2, TBuilder tb){
    collect(t1, tb);
    collect(t2, tb);
}

void collect(Tree t1, Tree t2, Tree t3, TBuilder tb){
    collect(t1, tb);
    collect(t2, tb);
    collect(t3, tb);
}

void collect(Tree t1, Tree t2, Tree t3, Tree t4, TBuilder tb){
    collect(t1, tb);
    collect(t2, tb);
    collect(t3, tb);
    collect(t4, tb);
}

void collect(Tree t1, Tree t2, Tree t3, Tree t4, Tree t5, TBuilder tb){
    collect(t1, tb);
    collect(t2, tb);
    collect(t3, tb);
    collect(t4, tb);
    collect(t5, tb);
}

void collect(Tree t1, Tree t2, Tree t3, Tree t4, Tree t5, Tree t6, TBuilder tb){
    collect(t1, tb);
    collect(t2, tb);
    collect(t3, tb);
    collect(t4, tb);
    collect(t5, tb);
    collect(t6, tb);
}

void collect(Tree t1, Tree t2, Tree t3, Tree t4, Tree t5, Tree t6, Tree t7, TBuilder tb){
    collect(t1, tb);
    collect(t2, tb);
    collect(t3, tb);
    collect(t4, tb);
    collect(t5, tb);
    collect(t6, tb);
    collect(t7, tb);
}

void collect(Tree t1, Tree t2, Tree t3, Tree t4, Tree t5, Tree t6, Tree t7, Tree t8, TBuilder tb){
    collect(t1, tb);
    collect(t2, tb);
    collect(t3, tb);
    collect(t4, tb);
    collect(t5, tb);
    collect(t6, tb);
    collect(t7, tb);
    collect(t8, tb);
}

void collect(Tree t1, Tree t2, Tree t3, Tree t4, Tree t5, Tree t6, Tree t7, Tree t8, Tree t9, TBuilder tb){
    collect(t1, tb);
    collect(t2, tb);
    collect(t3, tb);
    collect(t4, tb);
    collect(t5, tb);
    collect(t6, tb);
    collect(t7, tb);
    collect(t8, tb);
    collect(t9, tb);
}

void collect(list[Tree] currentTrees, TBuilder tb){
    for(t <- currentTrees) collect(t, tb);
}

// Default definition for collect; to be overridden in a specific type checker
// for handling syntax-constructs-of-interest
default void collect(Tree currentTree, TBuilder tb){
   //println("default collect: <typeOf(currentTree)>: <currentTree>");
   if(nlexical == 0)  collectParts(currentTree, tb); else collectLexicalParts(currentTree, tb); 
}

private  set[str] skipSymbols = {"lex", "layouts", "keywords", "lit", "cilit", "char-class"};

int delta = 2;
void collectParts(Tree currentTree, TBuilder tb){
   //println("collectParts: <typeOf(currentTree)>: <currentTree>");
   if(currentTree has prod /*&& getName(currentTree.prod.def) notin skipSymbols*/){
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

int nlexical = 0;

void collectLexical(Tree currentTree, TBuilder tb){
    //println("collectLexical: <typeOf(currentTree)>: <currentTree>");
    nlexical += 1;
    collect(currentTree, tb);
    collectLexicalParts(currentTree, tb);
    nlexical -= 1;
}

void collectLexicalParts(Tree currentTree, TBuilder tb){
   //println("collectLexicalParts: <typeOf(currentTree)>: <currentTree>"); 
   delta =1 ;
   if(currentTree has prod /*&& getName(currentTree.prod.def) notin skipSymbols*/){
       args = currentTree.args;
       int n = size(args);
       int i = 0;
       while(i < n){
        collectLexical(args[i], tb);
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
        //void (str key, value val) store,
        //set[value] (str key) getStored,
        void(str key, value val) push,
        value (str key) pop,
        value (str key) top,
        list[value] (str key) getStack,
        void (str key) clearStack,
        void (TModel tm) addTModel,
        TModel () build
      ); 

AType() makeClos1(AType tp) = AType (){ return tp; };                   // TODO: workaround for compiler glitch
void() makeClosError(Tree src, str msg) = void(){ throw checkFailed(src, msg); };
void() makeClosWarning(Tree src, str msg) = void(){ throw checkFailed({ warning(msg, getLoc(src)) }); };
void() makeClosInfo(Tree src, str msg) = void(){ checkFailed({ info(msg, getLoc(src)) }); };
             
TBuilder defaultTBuilder(Tree t) = newTBuilder(t);    
 
alias LubDefine = tuple[Key lubScope, str id, Key scope, IdRole idRole, Key defined, DefInfo defInfo]; 
alias LubDefine2 = tuple[str id, Key scope, IdRole idRole, Key defined, DefInfo defInfo];       

TBuilder newTBuilder(Tree t, bool debug = false){
    str rootPath = getLoc(t).path;
    Key globalScope = |global-scope:///|;
    Defines defines = {};
    //set[LubDefine] lubDefines = {};
    
    //rel[loc lubScope, str id, loc idScope, set[IdRole] idRoles, loc occ] lubUses = {};
    
    map[Key, set[Define]] definesPerLubScope = (globalScope: {});
    map[Key, set[LubDefine2]] lubDefinesPerLubScope = (globalScope: {});
    map[Key, rel[str id, loc idScope, set[IdRole] idRoles, loc occ]] lubUsesPerLubScope = (globalScope: {});
    map[Key, rel[Key,Key]]  scopesPerLubScope = (globalScope: {});
 
    Scopes scopes = ();
    
    Paths paths = {};
    ReferPaths referPaths = {};
    Uses uses = [];
    map[str,value] storeVals = ();
    
    map[loc,Calculator] calculators = ();
    map[loc,AType] facts = ();
    set[Fact] openFacts = {};
    set[Requirement] openReqs = {};
    int ntypevar = -1;
    map[loc,loc] tvScopes = ();
    luDebug = debug;
    set[Message] messages = {};
   
    Key currentScope = globalScope; //getLoc(t);
    Key rootScope = globalScope; //currentScope;
  
    scopes[getLoc(t)] = globalScope;
    lrel[Key scope, bool lubScope, map[ScopeRole, value] scopeInfo] scopeStack = [<globalScope, false, (anonymousScope(): false)>];
    list[Key] lubScopeStack = [];
    Key currentLubScope = globalScope;
    
    bool building = true;
    
    // TODO This is language dependent!
    str stripEscapes(str s) = replaceAll(s, "\\", "");
    
    void _define(str id, IdRole idRole, value def, DefInfo info){
        if(building){
            //if(currentScope == globalScope) throw TypePalUsage("`define` requires a user-defined scope; missing `enterScope`");
            loc l;
            if(Tree tdef := def) l = getLoc(tdef);
            else if(loc ldef := def) l = ldef;
            else throw TypePalUsage("Argument `def` of `define` should be `Tree` or `loc`, found <typeOf(def)>");
            
            if(info is defLub){
            if(id == "Y")
                println("**** defLub Y, <getLoc(def)>");
                //lubDefines += {<currentLubScope, id, currentScope, idRole, l, info>};                
                lubDefinesPerLubScope[currentLubScope] += {<id, currentScope, idRole, l, info>};
            } else {
                //defines += {<currentScope, id, idRole, l, info>};
                definesPerLubScope[currentLubScope] += <currentScope, id, idRole, l, info>;
            }
        } else {
            throw TypePalUsage("Cannot call `define` on TBuilder after `build`");
        }
    }
       
    void _use(Tree occ, set[IdRole] idRoles) {
        if(building){
          //if(currentScope == globalScope) throw TypePalUsage("`use` requires a user-defined scope; missing `enterScope`");
           uses += use(stripEscapes("<occ>"), getLoc(occ), currentScope, idRoles);
        } else {
            throw TypePalUsage("Cannot call `use` on TBuilder after `build`");
        }
    }
    
    void _useLub(Tree occ, set[IdRole] idRoles) {
        if(building){
           //if(currentScope == globalScope) throw TypePalUsage("`use` requires a user-defined scope; missing `enterScope`");
           //lubUses += { <currentLubScope, stripEscapes("<occ>"), currentScope, idRoles, getLoc(occ)> };
           //println("*** useLub: <occ>, <getLoc(occ)>");
           lubUsesPerLubScope[currentLubScope] += <stripEscapes("<occ>"), currentScope, idRoles, getLoc(occ)>;
        } else {
            throw TypePalUsage("Cannot call `useLub` on TBuilder after `build`");
        }
    }
    
    void _useViaPath(Tree occ, set[IdRole] idRoles, PathRole pathRole) {
        if(building){
            //if(currentScope == globalScope) throw TypePalUsage("`useViaPath` requires a user-defined scope; missing `enterScope`");
            u = use(stripEscapes("<occ>"), getLoc(occ), currentScope, idRoles);
            uses += u;
            referPaths += {refer(u, pathRole)};
        } else {
            throw TypePalUsage("Cannot call `useViaPath` on TBuilder after `build`");
        }
    }
    
    void _useQualified(list[str] ids, Tree occ, set[IdRole] idRoles, set[IdRole] qualifierRoles){
        if(building){
          //if(currentScope == globalScope) throw TypePalUsage("`useQualified` requires a user-defined scope; missing `enterScope`");
           uses += useq([stripEscapes(id) | id <- ids], getLoc(occ), currentScope, idRoles, qualifierRoles);
        } else {
            throw TypePalUsage("Cannot call `useQualified` on TBuilder after `build`");
        }  
     }
     void _useQualifiedViaPath(list[str] ids, Tree occ, set[IdRole] idRoles, set[IdRole] qualifierRoles, PathRole pathRole){
        if(building){
           //if(currentScope == globalScope) throw TypePalUsage("`useQualifiedViaPath` requires a user-defined scope; missing `enterScope`");
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
            if(innerLoc == rootScope){
              currentScope = innerLoc;
              scopeStack = push(<innerLoc, lubScope, ()>, scopeStack);
           } else { //if(innerLoc != currentScope){
              scopes[innerLoc] = currentScope; 
              scopesPerLubScope[currentLubScope] += <currentScope, innerLoc>;
              currentScope = innerLoc;
              scopeStack = push(<innerLoc, lubScope, ()>, scopeStack);
              if(lubScope){
                lubScopeStack = push(innerLoc, lubScopeStack);
                currentLubScope = innerLoc;
                definesPerLubScope[currentLubScope] = {};
                lubDefinesPerLubScope[currentLubScope] = {};
                lubUsesPerLubScope[currentLubScope] = {};
                scopesPerLubScope[currentLubScope] = {};
              }
           } 
           //else 
           //if(innerLoc == rootScope){
           //   currentScope = innerLoc;
           //   scopeStack = push(<innerLoc, lubScope, ()>, scopeStack);
           //} else {
           //   ;// do nothing throw TypePalUsage("Cannot call `enterScope` with inner scope <innerLoc> that is equal to currentScope");
           //}
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
              if(!isEmpty(lubScopeStack) && innerLoc == lubScopeStack[0]){
                defines += finalizeDefines(currentLubScope);
                lubScopeStack = tail(lubScopeStack);
                if(isEmpty(lubScopeStack)){
                   currentLubScope = globalScope;
                } else {
                   currentLubScope = lubScopeStack[0];
                }
              }
           } else {
              throw TypePalUsage("Cannot call `leaveScope` with scope <inner> that is not the current scope"); 
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
            return res;
        } else {
           throw TypePalUsage("Cannot call `getScopeInfo` on TBuilder after `build`");
        }
    }
    
    Key _getScope(){
        if(building){
           if(currentScope == globalScope) throw TypePalUsage("`getScope` requires a user-defined scope; missing `enterScope`");
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
            tv = |typevar://<rootPath>/<s>|;
            tvScopes[tv] = currentScope;
            //println("newTypeVar: <tv> in scope <currentScope>");
            return tvar(tv);
        } else {
            throw TypePalUsage("Cannot call `newTypeVar` on TBuilder after `build`");
        }
    }
    
    void _push(str key, value val){
        if(storeVals[key]? && list[value] old := storeVals[key]){
           storeVals[key] = val + old;
        } else {
           storeVals[key] = [val];
        }
    }
    
    value _pop(str key){
        if(storeVals[key]? && list[value] old := storeVals[key], size(old) > 0){
           pval = old[0];
           storeVals[key] = tail(old);
           return pval;
        } else {
           throw TypePalUsage("Cannot pop from empty stack for key <fmt(key)>");
        }
    }
    
    value _top(str key){
        if(storeVals[key]? && list[value] old := storeVals[key], size(old) > 0){
           return old[0];
        } else {
           throw TypePalUsage("Cannot get top from empty stack for key <fmt(key)>");
        }
    }
    
    list[value] _getStack(str key){
        if(storeVals[key]? && list[value] old := storeVals[key]){
            return old;
        }
        return [];
    }
    
    void _clearStack(str key){
        storeVals[key] = [];
    }
    
   // Merge all lubDefs and appoint a definition to refer to
    
    Define mergeLubDefs(str id, Key scope, rel[IdRole role, Key defined, DefInfo defInfo] lubDefs){
        if(id == "Y")
            println("mergeLubDefs");
        deps = {}; getATypes = [];
        defineds = {};
        loc firstDefined = |undef:///|;
        roles = {};
        for(tuple[IdRole role, Key defined, DefInfo defInfo] info <- lubDefs){
            roles += info.role;
            defineds += info.defined;
            if(firstDefined == |undef:///| || info.defined.offset < firstDefined.offset){
                firstDefined = info.defined;
            }
            deps += info.defInfo.dependsOn;
            getATypes += info.defInfo.getATypes;
        }
        if({role} := roles){
            //try {
            //    if({globalDef} := lookupFun(tm, use(id, roles, scope, firstDefined))){
            //        firstDefined = globalDef;
            //    }
            //} catch NoKey(): /* there iis no outer definition */;
            res = <scope, id, role, firstDefined, defLub(deps - defineds, defineds, getATypes)>;
            //println("finalizeDefines: add define:");  iprintln(res);
            return res;
        } else 
             throw TypePalUsage("LubDefs should use a single role, found <fmt(roles)>");
    }
    
    // Finalize all defines
    
     set[Define] finalizeDefines(){
          return defines + definesPerLubScope[globalScope];
        //return definesPerLubScope[globalScope] + {*finalizeDefines(lubScope) | lubScope <- lubScopes};
     }
     
    //  Finalize all defLubs and useLubs in one lubScope:
    //  1. A definition with a fixed type in the lubScope itself:
    //     - change all defLubs into uses of that definition
    //  2. A definition (but without a fixed type) in the lubScope itself:
    //     - merge all defLubs (also in nested scopes)
    //  3. Definitions with a fixed type in one or more subscopes:
    //     - change all defLubs per subscope (and its subscopes) to use the definition in the subscope
    //  4. Definitions (but without a fixed type) in one or more subscopes:
    //     - merge all defLubs per subscope (and its subscopes)
    // Recall:
    // alias Define - tuple[Key scope, str id, IdRole idRole, Key defined, DefInfo defInfo];
    // alias LubDefine2 = tuple[str id, Key scope, IdRole idRole, Key defined, DefInfo defInfo]; 
                
    
     set[Define] finalizeDefines(Key lubScope){
        
        set[Define] extra_defines = {};
       
        rel[Key,Key] containment = scopesPerLubScope[lubScope]* + <lubScope,lubScope>;
        set[Key] allScopes = carrier(containment);
        
        set[LubDefine2] deflubs_in_lubscope = lubDefinesPerLubScope[lubScope];
        set[str] deflub_names = deflubs_in_lubscope<0>;
        rel[str id, loc idScope, set[IdRole] idRoles, loc occ] uselubs_in_lubscope = lubUsesPerLubScope[lubScope];  
         
        
        set[Define] local_fixed_defines = definesPerLubScope[lubScope];
        extra_defines += local_fixed_defines;
        
        rel[str, Key] local_fixed_defines_scope = local_fixed_defines<1,0>;
        set[str] ids_with_fixed_def = domain(local_fixed_defines_scope);
        
        for(str id <- deflub_names){
            if(id == "Y"){
                println("finalizeDefines");
            }
            set[Key] id_defined_in_scopes = deflubs_in_lubscope[id]<0>;
            id_defined_in_scopes = { sc1 | sc1 <- id_defined_in_scopes, isEmpty(containment) || !any(sc2 <- id_defined_in_scopes, sc1 != sc2, <sc2, sc1> in containment)};
            
            //println("Consider <id>, defined in scopes <id_defined_in_scopes>");
            
            if({fixedDef} := local_fixed_defines[lubScope, id]){  // Definition exists with fixed type in the lubScope, use it instead of the lubDefines          
               //println("---top level fixedDef: <fixedDef> in <lubScope>");
               for(<IdRole role, Key defined, DefInfo defInfo> <- deflubs_in_lubscope[id, allScopes]){
                   u = use(id, defined, lubScope, {role});
                   //println("add: <u>");
                   uses += u;
               }
            } else if(id in ids_with_fixed_def){ // Definition(s) with fixed type exist in one or more subscopes, use them instead of the lubDefines 
                //println("---fixed def(s) in subscopes: <local_fixed_defines_scope[id]>");
                for(scope <- allScopes){
                    if(scope in local_fixed_defines_scope[id]){
                        for(<IdRole role, Key defined, DefInfo defInfo> <- deflubs_in_lubscope[id, containment[scope]]){
                            u = use(id, defined, scope, {role});
                            //println("add: <u>");
                            uses += u;
                        }
                    } else {
                        id_dfs = deflubs_in_lubscope[id, scope];
                        if(!isEmpty(id_dfs)) {
                            extra_defines += mergeLubDefs(id, scope, id_dfs);
                        }
                    }
                }
            } else if(lubScope in id_defined_in_scopes){   // Definition exists in the lubScope without fixed type, merge all lubDefs
                //println("---toplevel lubDef");
                 id_dfs = deflubs_in_lubscope[id, containment[id_defined_in_scopes]];
                 if(!isEmpty(id_dfs)){
                    extra_defines += mergeLubDefs(id, lubScope, id_dfs);
                 }
           } else {                                     // Same id defined in one or more disjoint subscopes
             for(scope <- id_defined_in_scopes){
                if({fixedDef} := local_fixed_defines[scope, id]){ // defined in outer scope with fixed type
                   //println("fixedDef: <fixedDef> in inner scope <scope>");
                   // There exists a definition with fixed type in the inner scope, just use it instead of the lubDefines
                   for(<IdRole role, Key defined, DefInfo defInfo> <- deflubs_in_lubscope[id, containment[id_defined_in_scopes]]){
                       u = use(id, defined, scope, {role});
                      //println("add: <u>");
                       uses += u;
                   }
               } else {
                 extra_defines += mergeLubDefs(id, scope, deflubs_in_lubscope[id, containment[scope]]);
               }
             }
           }
        }
        
        // Transform uncovered lubUses into ordinary uses
    
        for(u: <str id, loc idScope, set[IdRole] idRoles, loc occ> <- uselubs_in_lubscope){
            //println("replace lubUse by <use(id, occ, idScope, idRoles)>");
            uses += use(id, occ, idScope, idRoles);
            uselubs_in_lubscope -= u;
        }
        
        Map::delete(definesPerLubScope, lubScope);   // Remove all data recorded for this lubScope
        Map::delete(lubDefinesPerLubScope, lubScope);
        Map::delete(lubUsesPerLubScope, lubScope);
        Map::delete(scopesPerLubScope, lubScope);
        
        return extra_defines;
    }
    
    void _addTModel(TModel tm){
        messages += tm.messages;
        scopes += tm.scopes;
        defines += tm.defines;
        facts += tm.facts;
    }
    
    TModel _build(){
        if(building){
           building = false;
           
           if(size(scopeStack) == 0){
                throw TypePalUsage("Bottom entry missing from scopeStack: more leaveScopes than enterScopes");
           }
           
           if(size(scopeStack) > 1){
                unclosed = [scope | <Key scope, bool lubScope, map[ScopeRole, value] scopeInfo> <- scopeStack];
                throw TypePalUsage("Missing `leaveScope`(s): unclosed scopes <unclosed>");
           }
           
           tm = tmodel();
           defines = finalizeDefines();
           tm.defines = defines;
           tm.scopes = scopes;  scopes = ();
           tm.paths = paths;
           tm.referPaths = referPaths;
           tm.uses = uses;      uses = [];
           
           tm.calculators = calculators;
           tm.facts = facts;            facts = ();
           tm.openFacts = openFacts;    openFacts = {};    
           tm.openReqs = openReqs;  
           tm.tvScopes = tvScopes;      tvScopes = ();
           tm.store = storeVals;        storeVals = ();
           tm.definitions = ( def.defined : def | Define def <- defines);
           definesMap = ();
           for(<Key scope, str id, IdRole idRole, Key defined, DefInfo defInfo> <- defines){
                dm = ();
                if(definesMap[scope]?) dm = definesMap[scope];
                dm[id] =  (dm[id] ? {}) + {<idRole, defined>};
                definesMap[scope] = dm;
           }
           //for(<Key scope, str id, IdRole idRole, Key defined, DefInfo defInfo> <- defines){
           //     definesMap[<scope, id>] = definesMap[<scope, id>]? ?  definesMap[<scope, id>] + {<idRole, defined>} : {<idRole, defined>};
           //}
           tm.definesMap = definesMap;
           defines = {};
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
                     _calculate, 
                     _calculateEager,
                     _reportError, 
                     _reportWarning, 
                     _reportInfo, 
                     _newTypeVar, 
                     _push,
                     _pop,
                     _top,
                     _getStack,
                     _clearStack,
                     _addTModel,
                     _build); 
}
