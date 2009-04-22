module demo::GenericFeatherweightJava::Extract

import demo::GenericFeatherweightJava::FGJ;
import demo::GenericFeatherweightJava::TypeConstraints;

bool isLibraryClass(T t) {
  return false;
}

set[Constraint] extract(set[C] classes) {
  result = { };
  for (C c <- classes) result += extract(c);
  return result;
}
  
set[Constraint] extract(C class) {
  set[Constraint] result = { };
   
  visit (CT[class]) { case e x: switch(x) {
     case access(e erec, f f)  : {
       Trec = etype((), erec);
       C = ftype(Trec.N, f);
       if (!isLibraryClass(C)) {
         result += eq(typeof(x), typeof(C));
         result += subtype(typeof(erec), typeof(C));  
       } 
     } 
     case new(T C, list[e] es) : {
       result += eq(typeof(x), typeof(C));
       if (!isLibraryClass(C)) {
         result += { subtype(typeof(es[i]), typeof(constructorTypes(C)[i])) | int i <- domain(es) }; 
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
        else { // pending answer of Bob, I think this should implement Fig 7 of the ECOOP paper
           M = mtype(m, C.N);
           E = etype(x);
            
           Return = M.U;
           if (Return.N.Ts == []) { // unparameterized
             result += eq(typeof(x), typeof(M.U));
           }
           else if (int i <- domain(E.N.XsNs.Xs) && E.N.XsNs.Xs[i] == Return) { // same as one of the class parameters
             result += eq(typeof(x), typeof(Return));
           }
           else if (Return.N.Ts != []) { // parameterized class
             result += eq(typeof(x), typeof(M.U));
             // TODO got stuck here
             result += { eq(typeof(params[i],TODO)) | list[T] params := E.N.XsNs.Xs, int i <- domain(params) };
           }      
        }
     }
     case cast(T C, e e0) : {
       result += eq(typeof(x), typeof(C));
       result += subtype(typeof(e0), typeof(C));
     }
     case this : 
       result += eq(typeof(x), typeof(\type(lit(C,[]))));
  }  
  }  
  
  return result;
}

set[Constraint] CGEN() {
  result = { };
  
  
}