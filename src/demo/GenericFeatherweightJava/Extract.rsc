module Extract
import FGJ;
import TypeConstraints;

set[Constraint] extract(C class) {
  set[Constraint] result = { };
   
  visit (CT[class]) {
     case access(e,n) : if(!isLibraryClass(decl(n))) { // TODO: lookup F for n before calling decl
       result += eq(\type(x),decl(n).fields[n]);
       result += subtype(e, decl(n));  
     } 
     case new(n,fields) : {
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

