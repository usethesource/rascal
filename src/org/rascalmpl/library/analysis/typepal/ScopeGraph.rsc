@license{
Copyright (c) 2017, Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIWideT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
}
module analysis::typepal::ScopeGraph

// ScopeGraphs inspired by Kastens & Waite, Name analysis for modern languages: a general solution, SP&E, 2017

import IO;
import Set;
import List;

private bool luDebug = true;

alias Key = loc;    // a syntactic range in the source code

data Exception
    = NoKey()
    | AmbiguousDefinition(set[Key] definitions)
    ;

// IdRole: the various (language-specific) roles identifiers can play.
// Initially IdRole is empty but is extended in a language-specific module

data IdRole;

// PathRole: the various (language-specific) labelled semantic paths
// between program parts
// Initially PathRole is empty but may be extended in a language-specific module

data PathRole;
    
// Applied occurrence (use) of id for given IdRoles
// IdRoles are used to fold multiple scopeGraphs into one 
// (e.g., one for class and package names, one for variable names etc.)
data Use
    = use(str id, Key occ, Key scope, set[IdRole] idRoles)
    | useq(list[str] ids, Key occ, Key scope, set[IdRole] idRoles, set[IdRole] qualifierRoles)
    ;
alias Uses = list[Use];

str getId(Use u) = u has id ? u.id : intercalate(".", u.ids);

data ReferPath
    = refer(Use use, PathRole pathRole)
    ;

alias ReferPaths = set[ReferPath];

// Language-specific auxiliary associated with a name definition
// Extended in a language-specific module

data DefInfo
    = noDefInfo()
    ;

default FRModel finalizeFRModel(FRModel frm) = frm;

// A single definition: in scope, id is bound in a IdRole to defined, with DefInfo attached
alias Define  = tuple[Key scope, str id, IdRole idRole, Key defined, DefInfo defInfo];
alias Defines = set[Define];                                 // All definitions
alias Scopes  = map[Key inner, Key outer];                   // Syntactic containment
alias Paths   = rel[Key from, PathRole pathRole, Key to];    // Semantic containment path

data FRModel (
    Defines defines = {},
    Scopes scopes = (),
    Paths paths = {}, 
    ReferPaths referPaths = {},
    Uses uses = [],
    map[tuple[Key, str], rel[IdRole idRole, Key defined]] definesMap = ()
)   = frModel()
    ;

void printFRModel(FRModel frm){
    println("FRModel(");
    println("  defines = {");
    for(Define d <- frm.defines){
        println("    \<<d.scope>, <d.id>, <d.idRole>, <d.defined>\>"); 
    }
    println("  },");
    println("  scopes = (");
    for(Key inner <- frm.scopes){
        println("    <inner>: <frm.scopes[inner]>");
    }
    println("  ),");
    println("  paths = {");
    for(<Key from, PathRole pathRole, Key to> <- frm.paths){
        println("    \<<from>, <pathRole>, <to>\>");
    }
    println("  },");
    println("  referPath = {");
    for(c <- frm.referPaths){
        println("    <c>");
    }
    println("  },");
    //iprintln(frm.uses);
    println("  uses = [");
    for(Use u <- frm.uses){
        println("    use(<u.ids? ? u.ids : u.id>, <u.occ>, <u.scope>, <u.idRoles>, <u.qualifierRoles? ? u.qualifierRoles : "">)");
    }
    println("  ]");
    println(");");
}

// Retrieve a unique binding for use in given syntactic scope
private Key bind(FRModel frm, Key scope, str id, set[IdRole] idRoles){
    //throw "Cannot be called";
    defs = frm.defines[scope, id, idRoles];
    
    if(luDebug) println("\tbind: <scope>, <id>, <idRoles>
                       '\tbind: <defs>");
    
    if({<Key res, DefInfo dinfo>} := defs){
        if(luDebug) println("\tbind: <scope>, <id>, <idRoles> =\> <res>");
        return res;
    }
    if(size(defs) > 1){
       throw AmbiguousDefinition(defs<0>);
    }
    
    if(luDebug) println("\t---- bind, NoKey: <scope>, <id>");
    throw NoKey();
}


// Lookup use in given syntactic scope
private Key lookupScope(FRModel frm, Key scope, Use use){
    if(luDebug) println("\tlookupScope: <scope>, <use>");
    def = bind(frm, scope, use.id, use.idRoles);
    if(isAcceptableSimple(frm, def, use) == acceptBinding()){
       if(luDebug) println("\tlookupScope, <scope>. <use> ==\> <def>");
       return def;
    }
    if(luDebug) println("\tlookupScope, NoKey: <use>");
    throw NoKey();
}



// Find all (semantics induced) bindings for use in given syntactic scope via PathRole
private list[Key] lookupPaths(FRModel frm, Key scope, Use use, PathRole pathRole){
    //println("\tlookupPaths: <use.id> in scope <scope>, pathRole <pathRole>");
    res = 
      for(<scope, pathRole, Key parent> <- frm.paths){
        try {
            def = lookupScope(frm, parent, use);
            switch(isAcceptablePath(frm, parent, def, use, pathRole)){
            case acceptBinding():
               append def;
             case ignoreContinue():
                  continue; 
             case ignoreSkipPath():
                  break; 
            }
        } catch NoKey():
            scope = parent;
    }
    if(luDebug)println("\t---- lookupPaths: <scope>, <use>, <pathRole> ==\> <res>");
    return res;
}

// Get all pathRoles and remember them
@memo 
private set[PathRole] pathRoles(FRModel frm){
    //return {pl | /PathRole pl := frm};
    return frm.paths.pathRole;
}

// Lookup use in syntactic scope and via all semantic paths
private Key lookupQual(FRModel frm, Key scope, Use u){
     try 
        return lookupScope(frm, scope, u);
    catch NoKey(): {
        
        if(luDebug) println("\tlookupQual: loop over <pathRoles(frm)>");
        nextPath:
        for(PathRole pathRole <- pathRoles(frm)){
           candidates = lookupPaths(frm, scope, u, pathRole);
           if(size(candidates) == 1){
              return candidates[0];
           }
           for(Key candidate <- candidates){
               switch(isAcceptableSimple(frm, candidate, u)){
               case acceptBinding():
                  return candidate;
               case ignoreContinue():
                  continue;
               case ignoreSkipPath():
                  continue nextPath;
               }
            }
        }
    }
    if(luDebug) println("\t---- lookupQual, NoKey: <u>");
    throw NoKey();
}



// Lookup use in syntactic scope and via all semantic paths,
// recur to syntactic parent until found
private Key lookupNest(FRModel frm, Key scope, Use u){
    if(luDebug)println("\tlookupNest: <scope>, <u>");
    try 
        return lookupQual(frm, scope, u);
    catch NoKey(): {
        if(frm.scopes[scope] ?){
           parent = frm.scopes[scope];
           if(luDebug)println("\tlookupNest: <scope>, <u> move up to <parent>");
           return lookupNest(frm, parent, u);
        }
        if(luDebug) println("\t---- lookupNest, NoKey: <u>");
        throw NoKey();
    }
}

public Key lookup1(FRModel frm, Use u){
    scope = u.scope;
    if(luDebug) println("lookup: <u>");
    if(!(u has qualifierRoles)){
       res = lookupNest(frm, scope, u);
       if(isAcceptableSimple(frm, res, u) == acceptBinding()){
          if(luDebug) println("lookup: <u> ==\> <res>");
          return res;
       }
    } else {
       startScope = scope;
       while(true){
          scope = startScope;
           for(id <- u.ids[0..-1]){ 
               if(luDebug)println("lookup, search for <id>");
               scope = lookupNest(frm, scope, use(id, u.occ, scope, u.qualifierRoles));
            }
       
            try {
                res = lookupNest(frm, scope, use(u.ids[-1], u.occ, scope, u.idRoles));
                if(isAcceptableQualified(frm, res, u) == acceptBinding()){
                   if(luDebug) println("lookup: <u> ==\> <res>");
                   return res;
                }
            } catch NoKey(): {
                  if(frm.scopes[startScope]?){
                     startScope = frm.scopes[startScope];
                     if(luDebug)println("^^^^ lookup move to scope <startScope>");
                  } else {
                     throw NoKey();
                  }
            }
        }
     }
     if(luDebug) println("---- lookup, NoKey: <u>");
     throw NoKey();
}

public set[Key] lookup(FRModel frm, Use u){
    try {
        return {lookup1(frm, u)};
    } catch AmbiguousDefinition(set[Key] definitions):
        return definitions;
}

/************************************************************************************/
/* "wide" scopes were designed to suit Rascal's scope model where names from        */
/* imported modules co-exist with names declared in the current module.             */
/* lookupWide returns all definitions in the current syntactic scope (or its        */
/* parents) and definitions that can be reached in a single step via semantic links */                             
/************************************************************************************/

bool wdebug = true;

// Retrieve all bindings for use in given syntactic scope
private set[Key] bindWide(FRModel frm, Key scope, str id, set[IdRole] idRoles){
    try {
        preDefs = frm.definesMap[<scope, id>];
        if(preDefs<0> <= idRoles){
            res = preDefs<1>;
            if(isEmpty(res)){
               if(wdebug) println("\tbindWide, <id> in scope <scope> ==\> NoKey");
               throw NoKey();
            }
            if(wdebug) println("\tbindWide: <id> in scope <scope> returns:\n<for(r <- res){>\t===\> <r><}>");
            return res;
         } else {
            throw NoKey();
         }
    }  catch NoSuchKey(k): {
        if(wdebug) println("\tbindWide, <id> in scope <scope> ==\> NoKey");
        throw NoKey();
       }
}

// Lookup use in the given syntactic scope
private set[Key] lookupScopeWide(FRModel frm, Key scope, Use use){
    if(wdebug) println("\tlookupScopeWide: <use.id> in scope <scope>");
    defs = {};
    try {
        defs = {def | def <- bindWide(frm, scope, use.id, use.idRoles), isAcceptableSimple(frm, def, use) == acceptBinding()}; 
    } catch NoKey():{
        if(wdebug) println("\tlookupScopeWide: <use.id> in scope <scope> ==\> NoKey");
        throw NoKey();
    }
    if(isEmpty(defs)){
        if(wdebug) println("\tlookupScopeWide: <use.id> in scope <scope> ==\> NoKey");
        throw NoKey();
    }
    if(wdebug) println("\tlookupScopeWide, <use.id> in scope <scope> returns:\n<for(d <- defs){>\t===\> <d><}>"); 
    return defs;  
}

// Find all (semantics induced, one-level) bindings for use in given syntactic scope via PathRole
private set[Key] lookupPathsWide(FRModel frm, Key scope, Use use, PathRole pathRole){
    if(wdebug) println("\tlookupPathsWide: <use.id> in scope <scope>, role <pathRole>\n<for(p <- frm.paths){>\t---- <p>\n<}>");
    res = {};
    
    seenParents = {};
    solve(res, scope) {
    next_path:
        for(<scope, pathRole, Key parent> <- frm.paths, parent notin seenParents){
            seenParents += parent;
            if(wdebug) println("\tlookupPathsWide: scope: <scope>, trying semantic path to: <parent>");
            try {
                defs = lookupScopeWide(frm, parent, use);
                for(def <- defs){
                    switch(isAcceptablePath(frm, parent, def, use, pathRole)){
                    case acceptBinding():
                       res += def;
                     case ignoreContinue():
                          continue; 
                     case ignoreSkipPath():
                          continue next_path; 
                    }
                }
            } catch NoKey(): {
                ;if(wdebug) println("\tlookupPathsWide: <use.id> in scope <scope>, NoKey, move to other semantic path");
                //break; //scope = parent;
            }
            //scope = parent; // <<<<
        }       
    }
    if(wdebug) println("\tlookupPathsWide: <use.id> in scope <scope>, <pathRole> ==\> <res>");
    return res;
}

// Lookup use in given syntactic scope and via all semantic paths
private set[Key] lookupQualWide(FRModel frm, Key scope, Use u){
    if(wdebug) println("\tlookupQualWide: <u.id> in scope <scope>");
    res = {};
    try {
        res = lookupScopeWide(frm, scope, u);
        if(wdebug) println("\tlookupQualWide: <u.id> in scope <scope>, after lookupScopeWide:\n<for(r <- res){>\t--\> <r><}>");
        //return res; //<<<
    } catch NoKey(): { /* nothing found */; }

   
    if(wdebug) println("\tlookupQualWide: <res>, loop over <pathRoles(frm)>");
    nextPath:
    for(PathRole pathRole <- pathRoles(frm)){
       try {
           candidates = lookupPathsWide(frm, scope, u, pathRole);
           if(wdebug) println("\tlookupQualWide: candidates: <candidates>");
           for(Key candidate <- candidates){
               switch(isAcceptableSimple(frm, candidate, u)){
               case acceptBinding():
                  res += candidate;
               case ignoreContinue():
                  continue;
               case ignoreSkipPath():
                  continue nextPath;
               }
            }
        } catch NoKey(): { /* nothing found */; }
    }
    
    if(isEmpty(res)){
        if(wdebug) println("\tlookupQualWide, <u.id> in scope <scope> ==\> NoKey");
        throw NoKey();
    } else {
        if(wdebug) println("\tlookupQualWide: <u.id> in scope <scope> returns:\n<for(r <- res){>\t==\> <r><}>");
        return res;
    }
}

// Lookup use in syntactic scope and via all semantic paths,
// recur to syntactic parent until found
private set[Key] lookupNestWide(FRModel frm, Key scope, Use u){
    if(wdebug) println("\tlookupNestWide: <u.id> in scope <scope>");
    res = {};
    try {
        res = lookupQualWide(frm, scope, u);
        if(wdebug) println("\tlookupNestWide: <u.id> in scope <scope> found:\n<for(r <- res){>\t==\> <r><}>");
        return res; // <<<
    } catch NoKey(): { /* nothing found */; }
    
    try {
        if(frm.scopes[scope] ?){
           parent = frm.scopes[scope];
           if(wdebug) println("\tlookupNestWide: <u.id> in scope <scope> move up to <parent>");
           res += lookupNestWide(frm, parent, u);
        }
    } catch NoKey():
        /* do nothing */;
    if(isEmpty(res)){
        if(wdebug) println("\tlookupNestWide: <u.id> in scope <scope> ==\> NoKey");
        throw NoKey();
    } else {
        if(wdebug) println("\tlookupNestWide: <u.id> in scope <scope> returns:\n<for(r <- res){>\t==\> <r><}>");
        return res;
    }
}

public set[Key] lookupWide(FRModel frm, Use u){
    scope = u.scope;
    if(wdebug) println("lookupWide: <u>");
    if(!(u has qualifierRoles)){
       defs = {def | def <- lookupNestWide(frm, scope, u), isAcceptableSimple(frm, def, u) == acceptBinding()};
       if(wdebug) println("lookupWide: <u> returns:\n<for(d <- defs){>\t==\> <d><}>");
       wdebug = false;
       return defs;
    } else {
       startScope = scope;
       while(true){
           qscopes = {};
           for(id <- u.ids[0..-1]){ 
               if(wdebug) println("lookup, search for <id>"); 
               qscopes = lookupNestWide(frm, scope, use(id, u.occ, scope, u.qualifierRoles));
            }
            try {
                defs = {};
                for(scope <- qscopes){
                    defs += { def | def <- lookupNestWide(frm, scope, use(u.ids[-1], u.occ, scope, u.idRoles)), isAcceptableQualified(frm, def, u) == acceptBinding()};              
                }
                
                if(wdebug) println("lookupWide: <u> returns:\n<for(d <- defs){>\t==\> <d><}>");
                return defs;
            } catch NoKey(): {
                  if(frm.scopes[startScope]?){
                     startScope = frm.scopes[startScope];
                     if(wdebug) println("^^^^ lookup move to scope <startScope>");
                  } else {
                     throw NoKey();
                  }
            }
        }
     }
}

// Language-specific acceptance in case of multiple outcomes
data Accept 
    = acceptBinding()
    | ignoreContinue()
    | ignoreSkipPath()
    ;

default Accept isAcceptableSimple(FRModel frm, Key candidate, Use use) {
    if(wdebug) println("default isAcceptableSimple: <use.id> candidate: <candidate>");
    return acceptBinding();
}

default Accept isAcceptablePath(FRModel frm, Key defScope, Key def, Use use, PathRole pathRole) {
    if(wdebug) println("default isAcceptablePath: <use.id>, defScope: <defScope>, def <def>");
    return acceptBinding();
}

default Accept isAcceptableQualified(FRModel frm, Key candidate, Use use) = acceptBinding();

default bool checkPaths(FRModel frm, Key from, Key to, PathRole pathRole, bool(FRModel,Key) pred) {
    current = from;
    path = [from];
    do {
        if({def} := frm.paths[current, pathRole]){
           path += [def];
           current = def; 
        } else {
            throw "isAcceptablePath: <current>, <use>";
        }
    } while(current != to);
    return all(p <- path, pred(frm, p));
}

bool existsPath(FRModel frm, Key from, Key to, PathRole pathRole){
    return <from, to> in frm.paths<1,0,2>[pathRole]*;
}
