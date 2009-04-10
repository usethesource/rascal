module demo::GenericFeatherweightJava::Extract

import demo::GenericFeatherweightJava::FGJ;
import demo::GenericFeatherweightJava::TypeConstraints;

bool isLibraryClass(T t) {
  return false;
}

set[Constraint] extract(set[C] classes) {
  result = { };
  for (C c <- classes) result += extract(c);
}  
  
set[Constraint] extract(C class) {
  set[Constraint] result = { };
   
  visit (CT[class]) { case e x: switch(x) {
     case access(e e0, f f)  : {
       Te = etype((), e0);
       C = ftype(Te.N, f);
       if(!isLibraryClass(C)) {
         result += eq(typeof(x), typeof(C));
         result += subtype(typeof(e0), typeof(C));  
       } 
     } 
     case new(T C, list[e] es) : {
       result += eq(typeof(x), typeof(C));
       if (!isLibraryClass(C)) {
         result += { subtype(typeof(es[i]),typeof(constructorTypes(C)[i])) | int i <- domain(es) }; 
       }
     }
     case call(e e0, m m, list[T] Ts, list[e] es)  : {
        C = etype((), e0);
        result += subtype(typeof(x), typeof(C));
        if (!isLibraryClass(C)) {
           M = mtype(m, C.N);
           result += eq(typeof(x),typeof(M.U));
           result += { subtype(typeof(es[i]),typeof(M.Us[i])) | int i <- domain(es) };
        }
     }
  }  }  
  
  return result;
}