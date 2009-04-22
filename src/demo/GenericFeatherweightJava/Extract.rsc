module demo::GenericFeatherweightJava::Extract

import demo::GenericFeatherweightJava::FGJ;
import demo::GenericFeatherweightJava::TypeConstraints;

bool isLibraryClass(Name className) {
  return false;
}

set[Constraint] extract(set[Name] classes) {
  result = { };
  for (Name c <- classes) {
    result += extract(c);
  }
  return result;
}
  
set[Constraint] extract(Name class) {
  set[Constraint] result = { };
  
  def = ClassTable[class];
  bounds = ( def.formals.vars[i]:def.formals.bounds[i] | i <- domain(def.formals.vars));
  
  result += { c | Method method <- def, c <- extractMethod(bounds, def, method) };
  
  return result;
}
set[Constraint] extract(Bounds bounds, Class def, Method method) { // [Fuhrer et al., Fig 5]

  set[Constraint] result = { };
  bounds += (method.formalTypes.vars[i]:method.formalTypes.bounds[i] | i <- domain(method.formalTypes.vars));
      
  visit (method.expr) {  
     case x:access(Expr erec, Name fieldName) : {
       Trec = etype(bounds, erec);
       fieldType = ftype(Trec, fieldName);
       if (!isLibraryClass(def.className)) {
         result += eq(typeof(method), typeof(fieldType));
         result += subtype(typeof(erec), fdecl(Trec, fieldName));  
       } 
     }   
     case x:new(Type new, list[Expr] args) : {
       result += eq(typeof(x), typeof(new));
       if (!isLibraryClass(new)) {
         result += { subtype(typeof(args[i]), typeof(constructorTypes(new)[i])) | int i <- domain(args) }; 
       }
     }
     case x:call(Expr rec, Method methodName, list[Type] actuals, list[Expr] args)  : {
        Trec = etype(bounds, rec);
        result += subtype(typeof(x), typeof(Trec));
        if (!isLibraryClass(Trec)) {
           methodType = mtype(methodeName, Trec);
           result += eq(typeof(x),typeof(methodType.resultType));
           result += { subtype(typeof(args[i]),typeof(methodType.formals[i])) | int i <- domain(args) };
        }
        else { // [Fuhrer et al.,Fig 7]
           methodType = mtype(methodName, Trec);
           result += cGen(typeof(etype(x)), methodType.returnType, rec, #makeEq);
           result += { c | i <- domain(args), Ei := args[i], 
                           c <- cGen(Ei, methodType.formals[i], rec, #makeSub)};      
        }
     }
     case x:cast(Type to, Expr expr) : {
       result += eq(typeof(x), typeof(to));
       result += subtype(typeof(expr), typeof(to));
     }
     case x:var("this") : 
        result += eq(typeof(x), typeof(typelit(def.className,def.formals.bounds)));
  }  

  return result;
}


set[Constraint] cGen(Type a, Type T, Expr E, Constraint (TypeOf t1, TypeOf t2) op) {
  result = { };
 
  if (t in etype(E).actuals) {
    result += eq(typeof(x), typeof(methodType.returnType));
  }
  else if (typelit(name, actuals) := T) { 
    result += #op(typeof(a), typeof(T));
    
    Wi = ClassTable[name].formals.vars;
    result += { c | int i <- domain(Wi), Wia := a.actuals[i], c <- cGen(Wia, Wi[i], E, #makeEq)};
  }
  
  return result;
}

Constraint makeEq(TypeOf t1, TypeOf t2)       { return eq(t1, t2);      }
Constraint makeSub(TypeOf t1, TypeOf t2)      { return subtype(t1, t2); }
Constraint makeSub(TypeOf t1, set[TypeOf] t2) { return subtype(t1, t2); }