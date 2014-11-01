@bootstrapParser
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}

@license{

  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}

module lang::rascal::checker::TTL::PatternGenerator

import Prelude;
import Type;
import cobra::arbitrary;
import cobra::quickcheck;
import lang::rascal::checker::TTL::Library;
 
// ---- Generating patterns
alias Binding = tuple[Symbol symbol, value val];

alias VarEnv = map[str, Binding];
alias PV = tuple[str pat, value val, int nvars, VarEnv env];

list[str] getVars(str txt){
   return for(/<pat:P[0-9]+>/ := txt){
      append pat;
   }
}

str buildExpr(str txt, map[str, tuple[str pat, value val]] env){
   //println("buildExpr: <txt>, <env>");
   for(id <- env){
      txt = replaceAll(txt, "_P<id>", env[id].pat);
      txt = replaceAll(txt, "_V<id>", "<escape(env[id].val)>");
   }
   return txt;              
}

public tuple[map[str, tuple[str pat, value va]] patterns, VarEnv env] generatePatterns(list[type[&T]] types){
  nvars = 0;
  env = ();
  res = ();
  for(int i <- index(types)){
     <pat, val, nvars, env> = generatePattern(types[i], nvars, env);
     res["<i>"] = <pat, val>;
  }
  //println("generatePatterns: <res>, <env>");
  return <res, env>;
}

public PV generatePattern(type[&T] t){
     return generatePattern(t, 0, ());
}

public PV generatePattern(type[&T] t, int nvars, VarEnv env){
 if(isIntType(t.symbol)) // Avoid ambiguous pattern -1 := 1
        return generatePrimitive(arbInt(0, 10000), 0, ());
     if(isRealType(t.symbol)) // Avoid ambiguous pattern -1.5 := 1.5
        return generatePrimitive(arbReal(0.0, 10000.0), 0, ());
     if(isRatType(t.symbol)) // Avoid ambiguous pattern -1r2 := 1r2
        return generatePrimitive(arbPosRat(), 0, ());
     return generatePattern(t, nvars, env,  true);
}

public PV generatePattern(type[&T] t, int nvars, VarEnv env, bool allowVars){
     //println("generatePattern(<t>, <nvars>, <env>, <allowVars>)");
     if(allowVars){
	     if(arbInt(0,3) == 0){ // Use existing variable
	        for(var <- env){
	            if(typeOf(env[var].symbol) == t){
	               return <var, env[var].val, nvars, env>;
	             }
	        } 
	     }
	     if(arbInt(0,3) == 0){  // Introduce new variable;
	        var = "X<nvars>";
	        <pat, val, nvars, env> = generateValue(t, nvars + 1, env, false);
	        env[var] = <t.symbol, val>;
	        return <arbBool() || !isAssignable(t) ? "<var>" : "<t> <var>", val, nvars, env>;
	     }
	     
	     if(arbInt(0,3) == 0){  // Introduce named pattern;
	        var = "N<nvars>";
	        <pat, val, nvars, env> = generateValue(t, nvars + 1, env, true);
	        env[var] = <t.symbol, val>;
	        return <arbBool() || !isAssignable(t) ? "<var> : <pat>" : "<t> <var> : <pat>", val, nvars, env>;
	     }
     }
     
     return generateValue(t, nvars, env, allowVars);
}

bool isAssignable(type[&T] t){
  switch(t.symbol){
       case \list(\void()): 		return false;
       case \set(\void()):			return false;
       case \map(\void(), vt):		return false;
       case \map(kt, \void()):		return false;
       case \tuple([\void()]):		return false;
       case \rel([\void()]):		return false;
       case \lrel([\void()]):		return false;
  }
  return true;
}
     
public PV generateValue(type[&T] t, int nvars, VarEnv env, bool allowVars){     
     //println("generateValue(<t>, <nvars>, <env>)");
     switch(t.symbol){
       case \bool(): 		return generatePrimitive(arbBool(), nvars, env);
       case \int(): 		return generatePrimitive(arbInt(), nvars, env);
       case \real():		return generatePrimitive(arbReal(), nvars, env);
       case \rat():			return generatePrimitive(arbRat(), nvars, env);
       case \num():			return generatePrimitive(arbNumber(), nvars, env);
       case \str(): 		return generatePrimitive(arbString(10), nvars, env);
       case \datetime():	return generatePrimitive(arbDateTime(), nvars, env);
       
       case \loc():			return generatePrimitive(arbLoc(), nvars, env);

       case \list(et): 		return arbList(type(et, ()), nvars, env, allowVars);
       case \set(et):		return arbSet(type(et, ()), nvars, env, allowVars);
       case \map(kt, vt):	return arbMap(type(kt, ()), type(vt, ()), nvars, env, allowVars);
       
       case \tuple(ets):	return arbTuple(ets, nvars, env, allowVars);
       case \rel(ets):		return arbSet(type(\tuple(ets), ()), nvars, env, allowVars);
       case \lrel(ets):		return arbList(type(\tuple(ets), ()), nvars, env, allowVars);
       case \node():   		return generatePrimitive(arbNode(), nvars, env);
       case \value():		return generateArb(0, baseTypes, env);
     }
     throw "Unknown type: <t>";
}

public PV generatePrimitive(value v, int nvars, VarEnv env) = <str _ := v ? "<escape(v)>" : "<v>", v, nvars, env>;
     
public num arbNumber(){
   return (arbBool()) ? arbInt() : arbReal();
}

public loc  arbLoc(){
 g = getGenerator(#loc);
 return g(5);
}

public rat arbRat(){
  g = getGenerator(#rat);
 return g(5);
}

public rat arbPosRat(){
  g = getGenerator(#rat);
  r = g(5);
  return r >= 0 ? r :  -r;
}

public node arbNode(){
  g = getGenerator(#node);
 return g(5);
}

public PV arbList(type[&T] et, int nvars, VarEnv env, bool allowVars){
   if(isVoidType(et.symbol))
      return <"[]", [], nvars, env>;
   n = arbInt(0, 5);
   pelms = [];
   velms = [];
   listType = type(\list(et.symbol), ());
   while(size(pelms) < n){
    	if(allowVars && arbInt(0,3) == 0){  // Introduce new variable;
	        var = "L<nvars>";
	        <pat, val, nvars, env> = generateValue(listType, nvars + 1, env, false);
	        env[var] = <listType.symbol, val>;
	        pelms += "*<var>";
	        if(list[value] lv := val){
	        	velms += lv;
	        } else {
	        	throw "Generated value should be list: <val>";
	        }
	    } else {
            <pelm, velm, nvars, env> = generatePattern(et, nvars, env, allowVars);
            pelms += pelm;
            velms += velm;
        }
   }
   return <"[<intercalate(", ", pelms)>]", velms, nvars, env>;
}

public PV arbSet(type[&T] et, int nvars, VarEnv env, bool allowVars){
   if(isVoidType(et.symbol))
      return <"{}", {}, nvars, env>;
   n = arbInt(0, 5);
   pelms = [];
   velms = {};
   attempt = 0;
   setType = type(\set(et.symbol), ());
   while(size(pelms) < n && attempt < 100){
   	 attempt += 1;
    	if(allowVars && arbInt(0,3) == 0){  // Introduce new variable;
	        var = "S<nvars>";
	        <pat, val, nvars, env> = generateValue(setType, nvars + 1, env, false);
	        env[var] = <setType.symbol, val>;
	        pelms += "*<var>";
	        if(set[value] sv := val){
	        	velms += sv;
	        } else {
	        	throw "Generated value should be set: <val>";
	        }
	    } else {
   	      <pelm, velm, nvars1, env1> = generatePattern(et, nvars, env, allowVars);
          if(velm notin velms){
             pelms += pelm;
             velms += velm;
             nvars = nvars1;
             env = env;
          }
     }   
   }
   return <"{<intercalate(", ", pelms)>}", velms, nvars, env>;
}

public PV arbMap(type[&K] kt, type[&V] vt, int nvars, VarEnv env, bool allowVars){
   if(isVoidType(kt.symbol) || isVoidType(vt.symbol))
      return <"()", (), nvars, env>;
   keys = ();
   for(int i <- [0 .. arbInt(0, 5)]){ // ensures unique keys
       <pelm, velm, nvars1, env1> = generatePattern(kt, nvars, env, allowVars);
       if(!keys[pelm]?){
          keys[pelm] = velm;
          nvars = nvars1;
          env = env1;
       }   
   }
   pres = "";
   sep = "";
   mres = ();
   for(key <- keys){
       <pelm, velm, nvars, env> = generatePattern(vt, nvars, env, allowVars);
       pres += "<sep> <key> : <pelm>";
       sep = ", ";
       mres[keys[key]] = velm;
   }
   return <"( <pres> )", mres, nvars, env>;
}

public PV arbTuple([Symbol t1], int nvars, VarEnv env, bool allowVars){
   <p1, v1, nvars1, env1> = generatePattern(type(t1, ()), nvars, env, allowVars);
   return <"\<<p1>\>", <v1>, nvars1, env1>;
}

public PV arbTuple([Symbol t1, Symbol t2], int nvars, VarEnv env, bool allowVars){
   <p1, v1, nvars1, env1> = generatePattern(type(t1, ()), nvars, env, allowVars);
   <p2, v2, nvars2, env2> = generatePattern(type(t2, ()), nvars1, env1, allowVars);
   return <"\<<p1>, <p2>\>", <v1, v2>, nvars2, env2>;
}

public PV arbTuple([Symbol t1, Symbol t2, Symbol t3], int nvars, VarEnv env, bool allowVars){
   <p1, v1, nvars1, env1> = generatePattern(type(t1, ()), nvars, env, allowVars);
   <p2, v2, nvars2, env2> = generatePattern(type(t2, ()), nvars1, env1, allowVars);
   <p3, v3, nvars3, env3> = generatePattern(type(t3, ()), nvars2, env2, allowVars);
   return <"\<<p1>, <p2>, <p3>\>", <v1, v2, v3>, nvars3, env3>;
}
public PV arbTuple([Symbol t1, Symbol t2, Symbol t3, Symbol t4], int nvars, VarEnv env, bool allowVars){
   <p1, v1, nvars1, env1> = generatePattern(type(t1, ()), nvars, env, allowVars);
   <p2, v2, nvars2, env2> = generatePattern(type(t2, ()), nvars1, env1, allowVars);
   <p3, v3, nvars3, env3> = generatePattern(type(t3, ()), nvars2, env2, allowVars);
   <p4, v4, nvars4, env4> = generatePattern(type(t4, ()), nvars3, env3, allowVars);
   return <"\<<p1>, <p2>, <p3>, <p4>\>", <v1, v2, v3, v4>, nvars4, env4>;
}

public PV arbTuple([Symbol t1, Symbol t2, Symbol t3, Symbol t4, Symbol t5], int nvars, VarEnv env, bool allowVars){
   <p1, v1, nvars1, env1> = generatePattern(type(t1, ()), nvars, env, allowVars);
   <p2, v2, nvars2, env2> = generatePattern(type(t2, ()), nvars1, env1, allowVars);
   <p3, v3, nvars3, env3> = generatePattern(type(t3, ()), nvars2, env2, allowVars);
   <p4, v4, nvars4, env4> = generatePattern(type(t4, ()), nvars3, env3, allowVars);
   <p5, v5, nvars5, env5> = generatePattern(type(t5, ()), nvars4, env4, allowVars);
   return <"\<<p1>, <p2>, <p3>, <p4>, <p5>\>", <v1, v2, v3, v4, v5>, nvars5, env5>;
}