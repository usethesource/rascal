@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module demo::GenericFeatherweightJava::Types

import demo::GenericFeatherweightJava::AbstractSyntax;
import List;
import IO;  

public Type Object = typeLit("Object",[]);
public Class ObjectClass = class("Object", <[],[]>, Object, <[],[]>, cons(<[],[]>, super([]), []), []);

private Expr this   = var("this");  

public map[Name,Class] ClassTable = ("Object":ObjectClass); 
  
alias MethodType = tuple[FormalTypes forall, Type returnType, list[Type] formals];
alias Bounds     = map[Type var, Type bound];
alias Env        = map[Name var, Type varType];  
  
data Error = NoSuchMethod(Name methodName) | NoSuchField(Name fieldType) | NoType(Expr expr);
       
public rel[Name sub, Name sup] subclasses() { 
  return { <c, ClassTable[c].extends.className> | Name c <- ClassTable }*;
}

public bool subclass(Name c1, Name c2) {
  return <c1,c2> in subclasses();
}  

public Type bound(Bounds bounds, Type t) {
  if(typeVar(name) := t) return bounds[t];
  return t;
}  

public bool subtype(Bounds bounds, Type sub, Type sup) {
  if (sub == sup || sup == Object) {
    return true;
  }
  if (sub == Object) {
    return false;
  }        
  if (typeVar(name) := sub) {
    return subtype(bounds, bounds[sub]?Object, sup);
  }
  if (typeLit(name, actuals) := sub) {
    Class def = ClassTable[name];
    return subtype(bounds, inst(def.extends, def.formals.vars, actuals), sup);  
  }    
}

public list[Type] constructorTypes(Type t) {
  return ClassTable[t.className].constr.args.types;
}

public bool subtypes(Bounds env, list[Type] t1, list[Type] t2) {
  println("SUBTYPES: ", t1, " and ", t2);
  println(" bounds: ", env);
  if (size(t2) > size(t1)) throw "subtypes: <t2> is longer than <t1>";
  if ((int i <- domain(t1) + domain(t2)) && !subtype(env, t1[i], t2[i])) 
    return false;
  return true;
}      
      
public FormalVars fields(Type t) {
  if (t == Object) return <[],[]>;
  
  Class def = ClassTable[t.className];
      
  <sT,sf> = fields(inst(def.extends, def.formals.vars, t.actuals));
  <tT,tf> = inst(def.fields, def.formals.vars, t.actuals);
  
  return <sT + tT, sf + tf>;
}

public Type ftype(Type t, Name fieldName) {
  fields = fields(t);
  if (int i <- domain(fields.names) && fields.names[i] == fieldName) 
    return fields.types[i];
}

public Type fdecl(Type t, Name fieldName) {
  if (t == Object) throw NoSuchField(fieldName);
  
  Class def = ClassTable[t.className];
  if (fieldName in def.fields.names) return t;
  
  return fdecl(inst(def.extends, def.formals.vars, t.actuals), fieldName);
}

public map[Type,Type] bindings(list[Type] formals, list[Type] actuals ) {
  return (formals[i] : actuals[i] ? Object | int i <- domain(formals) + domain(actuals));  
}  

public &T inst(&T arg, list[Type] formals, list[Type] actuals) {
  map[Type,Type] subs = bindings(formals, actuals);
  return visit (arg) { case Type t => subs[t] ? t };
}

public MethodType mtype(Name methodName, Type t) {
   if (t == Object) throw NoSuchMethod(methodName);

   Class def = ClassTable[t.className];

   if (int i <- domain(def.methods) && def.methods[i].name == methodName) {
     return inst(<def.methods[i].formalTypes, def.methods[i].returnType, def.methods[i].formals.types>, def.formals.vars, t.actuals);
   }
   else { // if not found, go to super class
     return mtype(methodName, inst(def.extends, def.formals.vars, t.actuals));    
   } 
}   

public Expr mbody(Name methodName, list[Type] bindings, Type t) {
   if (n == Object) throw NoSuchMethod(methodName);

   Class def = ClassTable[t.className];

   if (int i <- domain(def.methods) && def.methods[i].name == methodName) { 
     return inst(inst(expr, def.formals.types, t.actuals), def.methods[i].formalTypes, bindings);
   }
   else {
     return mtype(methodName, inst(def.extends, def.formals.vars, t.actuals));
   }     
}

public Type etype(Env env, Bounds bounds, Expr expr) {
  println("ETYPE", expr);
  switch (expr) {
    case this : return env["this"];
    case var(Name v) : return env[v];
    case access(Expr rec, Name field) : {
      Type Trec = etype(env, bounds, rec);
      <types,fields> = fields(bound(bounds, Trec));
      if (int i <- domain(types) && fields[i] == field) return types[i];
    }
    case call(Expr rec, Name methodName, list[Type] actualTypes, list[Expr] params) : {
      Type Trec = etype(env, bounds, rec);
      <<vars,varBounds>, returnType, formals> = mtype(methodName, bound(bounds, Trec)); 
      
      if (subtypes(bounds, actualTypes, inst(varBounds, vars, actualTypes))) {
        paramTypes = [ etype(env, bounds, param) | param <- params];
        if (subtypes(bounds, paramTypes, inst(formals,vars,actualTypes))) { 
          return inst(returnType, vars, actualTypes);  
        }
      }
    } 
    case new(Type t, list[Expr] params) : {
       <types,fields> = fields(t);
       println("etype(new), field types is : ", types, " params is ", params);
       paramTypes = [ etype(env, bounds, param) | param <- params];
       println("etype(new), param types is : ", paramTypes);
       if (subtypes(bounds, paramTypes, types)) {
         return t;
       }
    }
    case cast(Type t, Expr sup) : {
      Tsup = etype(env, bounds, sup);
      Bsup = bound(bounds, Tsup);
      
      if (subtype(Bsup, t)) return t;
      if (subtype(t, Bsup) && dcast(t, Bsup)) return t;
    }
  }
    
  throw NoType(expr);
}  

public bool dcast(Name C, Name D) {
  if (C == Object || C == D) {
    return true;
  }
  // all vars must contribute    
  return typeVars(D.actuals) == typeVars(ClassTable[C].formalTypes.vars);
}

public set[Type] typeVars(&T x) {
  set[Type] result = { };
  visit (x) { case typeVar(Name v) : result += { v }; };
  return result;
}  
 
