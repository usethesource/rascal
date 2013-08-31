@bootstrapParser
module experiments::Compiler::Rascal2muRascal::TypeUtils

import Prelude;

import lang::rascal::\syntax::Rascal;

import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;
import lang::rascal::types::AbstractName;
import experiments::Compiler::Rascal2muRascal::RascalType;

import experiments::Compiler::muRascal::AST;

public Configuration config = newConfiguration();

public map[int,tuple[int,int]] uid2addr = ();
public map[loc,int] loc2uid = ();

public set[int] functionScopes = {};
public set[int] constructorScopes = {};
public set[int] variableScopes = {};

public void resetScopeExtraction() {
	uid2addr = ();
	loc2uid = ();
	functionScopes = {};
	constructorScopes = {};
	variableScopes = {};
}

// Get the type of an expression
Symbol getType(loc l) = config.locationTypes[l];

str getType(e) = "<getType(e@\loc)>";

// Get the outermost type constructor of an expression
str getOuterType(e) {
 tp = "<getName(getType(e@\loc))>";
// if(tp in {"int", "real", "rat"})
// 	tp = "num";
 return tp;
}

/* 
* CHANGED: 
* Getting a function type by name is problematic in case of nested functions,
* as fcvEnv does not contain nested functions;
* Additionally, it does not allow getting types of functions that build an overloaded function;   
*/
// Get the type of a declared function
set[Symbol] getFunctionType(str name) { 
   r = config.store[config.fcvEnv[RSimpleName(name)]].rtype; 
   return overloaded(alts) := r ? alts : {r};
}

// Alternatively, the type of a function can be looked up by @loc
Symbol getFunctionType(loc l) { 
   int uid = loc2uid[l];
   fun = config.store[uid];
   if(function(_,Symbol rtype,_,_,_,_) := fun) {
       return rtype;
   } else {
       throw "Looked up a function, but got: <fun> instead";
   }
}

Symbol getClosureType(loc l) {
   int uid = loc2uid[l];
   cls = config.store[uid];
   if(closure(Symbol rtype,_,_) := cls) {
       return rtype;
   } else {
       throw "Looked up a closure, but got: <cls> instead";
   }
}

int getFunctionScope(str name) = config.fcvEnv[RSimpleName(name)];

int getScopeSize(int scope){
  int n = 0;
  for(<scope, int pos> <- range(uid2addr))
    n += 1;
  return n;
}

// Get the type of a declared function
//tuple[int,int] getVariableScope(str name) = uid2addr[config.fcvEnv[RSimpleName(name)]];

MuExp mkVar(str name, loc l) {
  //println("mkVar: <name>");
  //println("l = <l>,\nloc2uid = <loc2uid>");
  
  tuple[int scope, int pos] addr = uid2addr[loc2uid[l]];
  
  res = "<name>::<addr.scope>::<addr.pos>";
  //println("mkVar: <name> =\> <res>; isFun: <loc2uid[l] in functionScopes>; isConstr: <loc2uid[l] in constructorScopes>");
  
  if(loc2uid[l] in functionScopes) {
  	// distinguishes between root and nested scopes
  	return (addr.scope == 0) ? muFun(name) : muFun(name, addr.scope);
  }
  if(loc2uid[l] in constructorScopes) {
  	return muConstr(name);
  }
  return muVar(name, addr.scope, addr.pos);
}

tuple[int,int] getVariableScope(str name, loc l) {
  return uid2addr[loc2uid[l]];
}


/* */

MuExp mkAssign(str name, loc l, MuExp exp) {
  //println("mkAssign: <name>");
  //println("l = <l>,\nloc2uid = <loc2uid>");
  addr = uid2addr[loc2uid[l]];
  res = "<name>::<addr[0]>::<addr[1]>";
  //println("mkVar: <name> =\> <res>");
  return muAssign(name, addr[0], addr[1], exp);
}

void extractScopes(){
   rel[int,int] containment = {};
   rel[int,int] declares = {};
   uid2addr = ();
   loc2uid = ();
   for(uid <- config.store){
      item = config.store[uid];
      switch(item){
        case function(_,_,_,inScope,_,src): { 
        									  functionScopes += {uid}; 
                                              declares += {<inScope, uid>}; 
                                              // containment += {<inScope, uid>}; 
                                              loc2uid[src] = uid;
                                              for(l <- config.uses[uid])
                                                  loc2uid[l] = uid;
                                            }
        case variable(_,_,_,inScope,src):   { 
        									  variableScopes += {uid};
        									  declares += {<inScope, uid>}; 
        									  loc2uid[src] = uid;
                                              for(l <- config.uses[uid])
                                                  loc2uid[l] = uid;
                                            }
        case constructor(_,_,inScope,src):  { 
        									  constructorScopes += {uid};
        									  declares += {<inScope, uid>};
        									  loc2uid[src] = uid;
        									  for(l <- config.uses[uid])
        									      loc2uid[l] = uid;
        									}
        case blockScope(containedIn,src):   { containment += {<containedIn, uid>}; loc2uid[src] = uid;}
        case booleanScope(containedIn,src): { containment += {<containedIn, uid>}; loc2uid[src] = uid;}
        
        case closure(_,inScope,src):        {
                                              functionScopes += {uid};
                                              declares += {<inScope, uid>};
        									  loc2uid[src] = uid;
        									}
      }
    }
    //println("containment = <containment>");
    //println("functionScopes = <functionScopes>");
    //println("declares = <declares>");
   
    containmentPlus = containment+;
    //println("containmentPlus = <containmentPlus>");
    
    topdecls = toList(declares[0]);
    //println("topdecls = <topdecls>");
    for(i <- index(topdecls)){
            uid2addr[topdecls[i]] = <0, i>;
    }
    for(fuid <- functionScopes){
        innerScopes = {fuid} + containmentPlus[fuid];
        decls = toList(declares[innerScopes]);
        //println("Scope <fuid> has inner scopes = <innerScopes>");
        //println("Scope <fuid> declares <decls>");
        for(i <- index(decls)){
            uid2addr[decls[i]] = <fuid, i>;
        }
    }
    //println("uid2addr:");
    //for(uid <- uid2addr){
    //  println("<config.store[uid]> :  <uid2addr[uid]>");
    //}
   
   //println("loc2uid:");
   //for(l <- loc2uid)
   //    println("<l> : <loc2uid[l]>");
}

public bool isDataType(AbstractValue::datatype(_,_,_,_)) = true;
public default bool isDataType(AbstractValue _) = false;

public bool isNonTerminalType(AbstractValue::sorttype(_,_,_,_)) = true;
public default bool isNonTerminalType(AbstractValue _) = false;

public bool isAlias(AbstractValue::\alias(_,_,_,_)) = true;
public default bool isAlias(AbstractValue _) = false;
