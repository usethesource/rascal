module Fun-abstract

import Integer;
import IO;

/*
 * The Expression language EXP
 */
 
data Exp =
       fun(str name, Exp exp)      // Function application
     | var(str Name)               // Variable occurrence
     | intcon(int ival)            // Integer constant
     | apply(Exp exp1, Exp exp2)   // Function application
     | op (Exp exp1, Exp exp2);    // Arbitrary binary operator


// Find all the variables in an expression

public set[str] allVars(Exp E) {
    return {Name | var(str Name) : E};
}

// Find all bound variables in an expression

public set[str] boundVars(Exp E) {
    return {Name | fun(str Name, Exp E1) : E};
} 

// Find all free variables in an expression

public set[str] freeVars(Exp E) {
    return allVars(E) - boundVars(E);
}

/*
 * Gensym, a unique name generator.
 */
 
int Cnt = 0;

public str genSym()
{
	Cnt = Cnt + 1;
    return "_x" + toString(Cnt);
}

// Generate a fresh variable if V does not occur in 
// given set of variables.

public str fresh(str V, set[str] S) {
    return V in S ? genSym() : V;
}

// Rename all bound variables in an Exp
// Exp E: given expression to be renamed
// map[str,str]: renaming table

public Exp rename(Exp E, map[str,str] Rn) {
    switch (E) {
    case fun(str Name, Exp E1): {
    	 	str Y = genSym();
         	return fun(Y, rename(E1, Rn + (Name: Y)));
         }
         
    case apply(Exp E1, Exp E2):
    	 return apply(rename(E1, Rn), rename(E2, Rn));
    
    case var(str Name):
    	 return var(Rn[Name] =? Name);

    default:
    	 return E;
    }
}

public Exp rename(Exp E){
	return rename(E, ());
}


// Substitution: replace all occurrences of V1 in expression Org by expression Repl

public Exp subst(str V1, Exp Repl, Exp Org) {

    return top-down-break visit (Org) { 
      case var(str V2): insert (V1==V2) ? Repl : var(V2);

      case apply(Exp Ea, Exp Eb):
        insert apply(subst(V1, Repl, Ea), subst(V1, Repl, Eb));
        
      case fun(str V2, Exp Body):
        if(V1 == V2)
           insert fun(V2, Body);
        else if(V2 in freeVars(Repl)){
           str V2f = fresh(V2, freeVars(Body) + freeVars(Repl));
           insert fun(V2f, subst(V1, Repl, subst(V2, var(V2f), Body)));        
        } else
           insert fun(V2, subst(V1, Repl, Body));      
    };
}

int nError = 0;
public bool assertEqual(value E1, value E2)
{
  Cnt = 0;
  if(E1 != E2){
  	println("test fails: <E1> != <E2>");
  	nError = nError + 1;
  	return false;
  }
  return true;
}

public bool test(){
   nError = 0;
   assertEqual(rename(apply(fun("a", var("a")),  intcon(3))),
                      apply(fun("_x1",var("_x1")),intcon(3)));
          
   assertEqual(rename(apply(fun("a", var("b")),  intcon(3))),
                      apply(fun("_x1",var("b")),intcon(3)));
          
   assertEqual(rename(apply(fun("a", apply(fun("b", var("b")), intcon(2))), intcon(1))),
                      apply(fun("_x1",apply(fun("_x2",var("_x2")),intcon(2))),intcon(1)));
          
   assertEqual(rename(apply(fun("a", apply(fun("b", var("a")), intcon(2))), intcon(1))),
                      apply(fun("_x1",apply(fun("_x2",var("_x1")),intcon(2))),intcon(1)));
          
   assertEqual(rename(apply(fun("a", apply(fun("b", var("a")), intcon(2))), var("c"))),
                      apply(fun("_x1",apply(fun("_x2",var("_x1")),intcon(2))),var("c")));
                      
   assertEqual(subst("a", intcon(1), apply(fun("a", var("a")),  intcon(3))),
                                     apply(fun("a",var("a")),intcon(3)));
                                     
   assertEqual(subst("a", var("b"), apply(fun("a", var("b")),  var("a"))),
                                    apply(fun("a",var("b")),var("b")));
                                    
   assertEqual(subst("a", op(intcon(3),var("b")), apply(fun("b", var("b")),  var("a"))),
                                                  apply(fun("_x1",var("_x1")),op(intcon(3),var("b"))));
   return nError == 0;
}


