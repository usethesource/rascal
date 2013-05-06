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

alias VarEnv = map[str, value];
alias PV = tuple[str pat, value val, int nvars, VarEnv env];

public PV generatePattern(type[&T] t){
     if(isIntType(t.symbol)) // Avoid ambiguous pattern -1 := 1
        return generatePrimitive(arbInt(0, 10000), 0, ());
     if(isRealType(t.symbol)) // Avoid ambiguous pattern -1.5 := 1.5
        return generatePrimitive(arbReal(0.0, 10000.0), 0, ());
     if(isRatType(t.symbol)) // Avoid ambiguous pattern -1r2 := 1r2
        return generatePrimitive(arbPosRat(), 0, ());
     return generatePattern(t, 0, ());
}

public PV generatePattern(type[&T] t, int nvars, VarEnv env){
     if(arbInt(0,3) == 0){ // Use existing variable
        for(var <- env){
            if(typeOf(env[var]) == t){
                <pat, val, nvars, env> = generateValue(t, nvars, env);
               return <var, val, nvars, env>;
             }
        } 
     }
     if(arbInt(0,3) == 0){  // Introduce new variable;
        var = "X<nvars>";
        <pat, val, nvars, env> = generateValue(t, nvars, env);
        env[var] = val;
        return <arbBool() ? var : "<t> <var>", val, nvars + 1, env>;
     }
     
     return generateValue(t, nvars, env);
}
     
public PV generateValue(type[&T] t, int nvars, VarEnv env){     
     //println("generatePattern(<t>, <nvars>, <env>)");
     switch(t.symbol){
       case \bool(): 		return generatePrimitive(arbBool(), nvars, env);
       case \int(): 		return generatePrimitive(arbInt(), nvars, env);
       case \real():		return generatePrimitive(arbReal(), nvars, env);
       case \rat():			return generatePrimitive(arbRat(), nvars, env);
       case \num():			return generatePrimitive(arbNumber(), nvars, env);
       case \str(): 		return generatePrimitive(arbString(10), nvars, env);
       case \datetime():	return generatePrimitive(arbDateTime(), nvars, env);
       
       case \loc():			return generatePrimitive(arbLoc(), nvars, env);

       case \list(et): 		return arbList(type(et, ()), nvars, env);
       case \set(et):		return arbSet(type(et, ()), nvars, env);
       case \map(kt, vt):	return arbMap(type(kt, ()), type(vt, ()), nvars, env);
       
       case \tuple(ets):	return arbTuple(ets, nvars, env);
       case \rel(ets):		return arbSet(type(\tuple(ets), ()), nvars, env);
       case \lrel(ets):		return arbList(type(\tuple(ets), ()), nvars, env);
       case \node():   		return generatePrimitive(arbNode(), nvars, env);
       case \value():		return generateArb(0, baseTypes, env);
     }
     throw "Unknown type: <t>";
}

public PV generatePrimitive(value v, nvars, env) = <str _ := v ? "<escape(v)>" : "<v>", v, nvars, env>;
     
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

public PV arbList(type[&T] et, int nvars, VarEnv env){
   if(isVoidType(et.symbol))
      return <"[]", [], nvars, env>;
   n = arbInt(0, 5);
   pelms = [];
   velms = [];
   while(size(pelms) < n){
        <pelm, velm, nvars, env> = generatePattern(et, nvars, env);
        pelms += pelm;
        velms += velm;
   }
   return <"[<intercalate(", ", pelms)>]", velms, nvars, env>;
}

public PV arbSet(type[&T] et, int nvars, VarEnv env){
   if(isVoidType(et.symbol))
      return <"{}", {}, nvars, env>;
   n = arbInt(0, 5);
   pelms = [];
   velms = {};
   attempt = 0;
   while(size(pelms) < n && attempt < 100){
   	 attempt += 1;
   	 <pelm, velm, nvars, env> = generatePattern(et, nvars, env);
     if(velm notin velms){
        pelms += pelm;
        velms += velm;
     }   
   }
   return <"{<intercalate(", ", pelms)>}", velms, nvars, env>;
}

public PV arbMap(type[&K] kt, type[&V] vt, int nvars, VarEnv env){
   if(isVoidType(kt.symbol) || isVoidType(vt.symbol))
      return <"()", (), nvars, env>;
   keys = ();
   for(int i <- [0 .. arbInt(0, 5)]){ // ensures unique keys
       <pelm, velm, nvars, env> = generatePattern(kt, nvars, env);
       if(!keys[pelm]?){
          keys[pelm] = velm;
       }   
   }
   pres = "";
   sep = "";
   mres = ();
   for(key <- keys){
       <pelm, velm, nvars, env> = generatePattern(vt, nvars, env);
       pres += "<sep> <key> : <pelm>";
       sep = ", ";
       mres[keys[key]] = velm;
   }
   return <pres, mres, nvars, env>;
}

public PV arbTuple([Symbol t1], int nvars, VarEnv env){
   <p1, v1, nvars1, env1> = generatePattern(type(t1, ()), nvars, env);
   return <"\<<p1>\>", <v1>, nvars1, env1>;
}

public PV arbTuple([Symbol t1, Symbol t2], int nvars, VarEnv env){
   <p1, v1, nvars1, env1> = generatePattern(type(t1, ()), nvars, env);
   <p2, v2, nvars2, env2> = generatePattern(type(t2, ()), nvars1, env1);
   return <"\<<p1>, <p2>\>", <v1, v2>, nvars2, env2>;
}

public PV arbTuple([Symbol t1, Symbol t2, Symbol t3], int nvars, VarEnv env){
   <p1, v1, nvars1, env1> = generatePattern(type(t1, ()), nvars, env);
   <p2, v2, nvars2, env2> = generatePattern(type(t2, ()), nvars1, env1);
   <p3, v3, nvars3, env3> = generatePattern(type(t3, ()), nvars2, env2);
   return <"\<<p1>, <p2>, <p3>\>", <v1, v2, v3>, nvars3, env3>;
}
public PV arbTuple([Symbol t1, Symbol t2, Symbol t3, Symbol t4], int nvars, VarEnv env){
   <p1, v1, nvars1, env1> = generatePattern(type(t1, ()), nvars, env);
   <p2, v2, nvars2, env2> = generatePattern(type(t2, ()), nvars1, env1);
   <p3, v3, nvars3, env3> = generatePattern(type(t3, ()), nvars2, env2);
   <p4, v4, nvars4, env4> = generatePattern(type(t4, ()), nvars3, env3);
   return <"\<<p1>, <p2>, <p3>, <p4>\>", <v1, v2, v3, v4>, nvars4, env4>;
}

public PV arbTuple([Symbol t1, Symbol t2, Symbol t3, Symbol t4, Symbol t5], int nvars, VarEnv env){
   <p1, v1, nvars1, env1> = generatePattern(type(t1, ()), nvars, env);
   <p2, v2, nvars2, env2> = generatePattern(type(t2, ()), nvars1, env1);
   <p3, v3, nvars3, env3> = generatePattern(type(t3, ()), nvars2, env2);
   <p4, v4, nvars4, env4> = generatePattern(type(t4, ()), nvars3, env3);
   <p5, v5, nvars5, env5> = generatePattern(type(t5, ()), nvars4, env4);
   return <"\<<p1>, <p2>, <p3>, <p4>, <p5>\>", <v1, v2, v3, v4, v5>, nvars5, env5>;
}


