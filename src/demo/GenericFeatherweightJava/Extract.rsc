module demo::GenericFeatherweightJava::Extract

import demo::GenericFeatherweightJava::FGJ;
import demo::GenericFeatherweightJava::TypeConstraints;

bool isLibraryClass(T t) {
  return false;
}

set[Constraint] extract(set[C] classes) {
  set[Constraint] result = { };
  for (C c <- classes) result += extract(c);
}  
  
set[Constraint] extract(C class) {
  set[Constraint] result = { };
   
  visit (CT[class]) {
     case access(e,n) : {
       T te = etype((), e);
       T tf = ftype(te.N, n);
       if(!isLibraryClass(tf)) {
         result += eq(typeof(access(e,n)), tf);
         result += subtype(e, tf);  
       } 
     } 
     case new(n,_) : {
       result += eq(\type(x), decl(n));
       if (!isLibraryClass(decl(n))) {
         result += { subtype(e1,e2) | e1 <- fields, e2 := decl(n).fields[n] }; 
       }
     }
     case call(\on,n,bindings,actuals) : {
        result += subtype(\type(\on), rootDecl(n));
        if (!isLibraryClass(decl(n))) {
           result += eq(\type(x),\type(decl(n)));
           result += { subtype(e1,e2) | e1 <- fields, e2 := decl(n).params[n] };
        }
     }
  }
  
  return result;
}