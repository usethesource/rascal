module demo::GenericFeatherweightJava::Extract

import demo::GenericFeatherweightJava::FGJ;
import demo::GenericFeatherweightJava::TypeConstraints;

bool isLibraryClass(Name className) {
  return false;
}

set[Constraint] extract(set[Name] classes) {
  return { constraint | class <- classes, constraint <- extract(class) }; 
}
  
set[Constraint] extract(Name class) {
  def    = ClassTable[class];
  bounds = ( def.formals.vars[i]:def.formals.bounds[i] | i <- domain(def.formals.vars));
  
  // first extract basic constraints
  result = { c | Method method <- def, c <- extract(bounds, def, method) };

  // constraints from method overloading
  result += { c | 
                  Method methodP <- def, Method method <- ClassTable[def.extends],
                  methodP.name == method.name,
                  MethodType TmP := mtype(methodP.name, typeLit(def.className,def.formals.bounds)),
                  MethodType Tm := mtype(method.name, typeLit(ClassTable[def.extends].className,def.formals.bounds)),
                  overrides(bounds, TmP, Tm), i <- domain(method.formals),
                  // TODO inline these two elements in the lhs of the comprehension
                  c <- { eq(typeof(methodP.params.types[i]), typeof(methodP.params.types[i])), 
                         subtype(TmP.returnType, Tm.returnType) }
            };   

  return result;
}

bool overrides(Bounds b, MethodType m1, MethodType m2) {
  return m1.name == m2.name &&
         !(i <- domain(m1.formals.types) && !subtype(b, m1.formals.types[i],m2.formals.types[i]));
}

set[Constraint] extract(Bounds bounds, Class def, Method method) { // [Fuhrer et al., Fig 5]

  set[Constraint] result = { };
  bounds += (method.formalTypes.vars[i]:method.formalTypes.bounds[i] | i <- domain(method.formalTypes.vars));
      
  visit (method.expr) {  
     case x:access(Expr erec, Name fieldName) : {
       Trec = etype(bounds, erec);
       fieldType = ftype(Trec, fieldName);
       if (!isLibraryClass(def.className))
         result += {eq(typeof(method), typeof(fieldType)), subtype(typeof(erec), fdecl(Trec, fieldName))};  
     }   
     case x:new(Type new, list[Expr] args) : {
       result += {eq(typeof(x), typeof(new))};
       if (!isLibraryClass(new)) {
         result += { subtype(typeof(args[i]), typeof(constructorTypes(new)[i])) | int i <- domain(args) }; 
       }
     }
     case x:call(Expr rec, Method methodName, list[Type] actuals, list[Expr] args)  : {
        Trec = etype(bounds, rec);
        result += {subtype(typeof(x), typeof(Trec))};
        if (!isLibraryClass(Trec)) {
           methodType = mtype(methodeName, Trec);
           result += eq(typeof(x),typeof(methodType.resultType));
           result += { subtype(typeof(args[i]),typeof(methodType.formals[i])) | int i <- domain(args) };
        }
        else { // [Fuhrer et al.,Fig 7] should this be in an else branch or not???
           methodType = mtype(methodName, Trec);
           result += cGen(typeof(etype(x)), methodType.returnType, rec, #makeEq);
           result += { c | i <- domain(args), Ei := args[i], 
                           c <- cGen(Ei, methodType.formals[i], rec, #makeSub)};      
        }
     }
     case x:cast(Type to, Expr expr) :
       result += {eq(typeof(x), typeof(to)), subtype(typeof(expr), typeof(to))};
     case x:var("this") : 
       result += {eq(typeof(x), typeof(typelit(def.className,def.formals.bounds)))};
  }  

  return result;
}

set[Constraint] cGen(Type a, Type T, Expr E, Constraint (TypeOf t1, TypeOf t2) op) {
  if (T in etype(E).actuals) {
    return {#op(typeof(a), typeof(T, E))};
  }
  else if (typelit(name, actuals) := T) { 
    Wi = ClassTable[name].formals.vars;
    return { c | int i <- domain(Wi), Wia := a.actuals[i], c <- cGen(Wia, Wi[i], E, #makeEq)}
         + { #op(typeof(a), typeof(T)) };
  }
}

Constraint makeEq(TypeOf t1, TypeOf t2)       { return eq(t1, t2);      }
Constraint makeSub(TypeOf t1, TypeOf t2)      { return subtype(t1, t2); }
Constraint makeSub(TypeOf t1, set[TypeOf] t2) { return subtype(t1, t2); }