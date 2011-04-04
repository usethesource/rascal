@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module demo::GenericFeatherweightJava::Extract

import demo::GenericFeatherweightJava::AbstractSyntax;
import demo::GenericFeatherweightJava::TypeConstraints;
import demo::GenericFeatherweightJava::Types;
import List;
import IO;

set[Name] libraries = { };

public void registerLibraries(set[Name] libs) {
  libraries += libs;
}

public bool isLibraryClass(Name className) {
  return className in libraries;
}  

public bool isLibraryClass(Type t) {
  return typeLit(name, _) := t && isLibraryClass(name);
}

public set[Constraint] extract(set[Name] classes) {
  // compute initial constraints, Fig 5 and 7
  result = { constraint | class <- classes, constraint <- extract(class) };
  // then compute closures from Fig 8.
  vars = carrier(result);
  // rule 17
  result += {eq(typeof(T,a0),typeof(T,a1)) | 
             subtype(typeof(a0),typeof(a1)) <- result,  a <- {a0,a1}, typeof(T, a) in vars};
  // TODO rule 18 
  result += {c | typeof(T1, a) <- vars, /* ??? TODO */ c <- cGen(T2, a, T,a, makeEq) };  
}
  
public set[Constraint] extract(Name class) {
  def    = ClassTable[class];
  bounds = ( def.formals.vars[i]:def.formals.bounds[i] | i <- domain(def.formals.vars));
  
  // first extract basic constraints
  result = { c | Method method <- def, c <- extract(bounds, def, method) };

  // constraints from method overloading
  // result += { c | 
                  // Method methodP <- def, Method method <- ClassTable[def.extends.className],
                  // methodP.name == method.name,
                  // MethodType TmP := mtype(methodP.name, typeLit(def.className,def.formals.bounds)),
                  // MethodType Tm := mtype(method.name, typeLit(ClassTable[def.extends.className].className,def.formals.bounds)),
                  // overrides(bounds, TmP, Tm), i <- domain(method.formals),
                  // TODO inline these two elements in the lhs of the comprehension
                  // c <- { eq(typeof(methodP.params.types[i]), typeof(methodP.params.types[i])), 
                         // subtype(TmP.returnType, Tm.returnType) }
            // };   

  return result;
}

bool overrides(Bounds b, MethodType m1, MethodType m2) {
  return m1.name == m2.name &&
         !(i <- domain(m1.formals.types) && !subtype(b, m1.formals.types[i],m2.formals.types[i]));
}

public set[Constraint] extract(Bounds bounds, Class def, Method method) { // [Fuhrer et al., Fig 5]
  println("EXTRACT");
  set[Constraint] result = { };
  bounds += (method.formalTypes.vars[i]:method.formalTypes.bounds[i] | i <- domain(method.formalTypes.vars));
  env = ("this": typeLit(def.className, [])); // TODO check if this is a good env

  visit (method.expr) {  
     case x:access(Expr erec, Name fieldName) : {
println("extract ACCESS");
       Trec = etype(env, bounds, erec);
       fieldType = ftype(Trec, fieldName);
       if (!isLibraryClass(def.className))
         result += {eq(typeof(method), typeof(fieldType)), subtype(typeof(erec), typeof(fdecl(Trec, fieldName)))};  
     }   
     case x:new(Type new, list[Expr] args) : {
println("extract NEW");
       result += {eq(typeof(x), typeof(new))};
       println("new: ", eq(typeof(x), typeof(new)));
       if (!isLibraryClass(new)) {
         result += { subtype(typeof(args[i]), typeof(constructorTypes(new)[i])) | int i <- domain(args) }; 
       }
     }
     case x:call(Expr rec, Name methodName, list[Type] actuals, list[Expr] args)  : {
println("extract CALL");
        Trec = etype(env, bounds, rec);
        println("call receiver is: ", Trec);
        result += {subtype(typeof(x), typeof(Trec))};
        if (!isLibraryClass(Trec)) {
           println("\tis not a library");
           methodType = mtype(methodeName, Trec);
           result += eq(typeof(x),typeof(methodType.resultType));
           result += { subtype(typeof(args[i]),typeof(methodType.formals[i])) | int i <- domain(args) };
        }
        else { // [Fuhrer et al.,Fig 7] should this be in an else branch or not???
           println("\tis a library");
           methodType = mtype(methodName, Trec);
           set[Constraint] gen = cGen(etype(env, bounds, x), methodType.returnType, rec, makeEq);
           result += gen;
           result += { c | i <- domain(args), Ei := args[i], 
                           c <- cGen(Ei, methodType.formals[i], rec, makeSub)}; 
        }
     }
     case x:cast(Type to, Expr expr) :
       result += {eq(typeof(x), typeof(to)), subtype(typeof(expr), typeof(to))};
     case x:var("this") : 
       result += {eq(typeof(x), typeof(typeLit(def.className,def.formals.bounds)))};
  }  

  return result;
}

set[Constraint] cGen(Type a, Type T, Expr E, Constraint (TypeOf t1, TypeOf t2) op) {
  println("cGen(<a>,<T>,<E>,<op>");

  // TODO: bounds and env for etype are bogus
  if (T in etype((),(),E).actuals) {
    return {op(typeof(a), typeof(T, E))};
  }
  else if (typelit(name, actuals) := T) { 
    Wi = ClassTable[name].formals.vars;
    return { c | i <- domain(Wi), Wia := a.actuals[i], c <- cGen(Wia, Wi[i], E, makeEq)}
         + { op(typeof(a), typeof(T)) };
  }
}

Constraint makeEq(TypeOf t1, TypeOf t2)       { return eq(t1, t2);      }
Constraint makeSub(TypeOf t1, TypeOf t2)      { return subtype(t1, t2); }
Constraint makeSub(TypeOf t1, set[TypeOf] t2) { return subtype(t1, t2); }
