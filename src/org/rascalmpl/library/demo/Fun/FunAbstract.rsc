@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module demo::Fun::FunAbstract

import Integer;
import IO;
import List;
import Relation;

/*
 * The Expression language EXP
 */
 
data Exp =
       fnc(str name, Exp exp)             // Function definition
     | var(str name)                      // Variable occurrence
     | intcon(int ival)                   // Integer constant
     | strcon(str sval)                   // String constant
     | apply(Exp exp1, Exp exp2)          // Function application
     | op (str name, Exp exp1, Exp exp2); // Named binary operator


// Find all the variables in an expression

public set[str] allVars(Exp E) {
    return {Name | / var(str Name) := E};
}

// Find all bound variables in an expression

public set[str] boundVars(Exp E) {
    return {Name | / fnc(str Name, Exp E1) := E};
} 

// Find all free variables in an expression

public set[str] freeVars(Exp E) {
    return allVars(E) - boundVars(E);
}

int Cnt = 0;

@doc{a unique name generator}
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
    case fnc(str Name, Exp E1): {
    	 	str Y = genSym();
         	return fnc(Y, rename(E1, Rn + (Name: Y)));
         }
         
    case apply(Exp E1, Exp E2):
    	 return apply(rename(E1, Rn), rename(E2, Rn));
    
    case var(str Name):
    	 return var(Rn[Name] ? Name);
   
    case op(str Name, Exp E1, Exp E2):
         return op(Name, rename(E1, Rn), rename(E2, Rn));

    default:
    	 return E;
    }
}

public Exp rename(Exp E){
	return rename(E, ());
}


// Substitution: replace all occurrences of V1 by expression Repl in expression Org

public Exp subst(str V1, Exp Repl, Exp Org) {

    return top-down-break visit (Org) { 
      case var(str V2):
      	if( V1 == V2) insert Repl;

      case apply(Exp Ea, Exp Eb):
        insert apply(subst(V1, Repl, Ea), subst(V1, Repl, Eb));
        
      case fnc(str V2, Exp Body):
        if(V1 != V2)
           if(V2 in freeVars(Repl)){
              str V2f = fresh(V2, freeVars(Body) + freeVars(Repl));
              insert fnc(V2f, subst(V1, Repl, subst(V2, var(V2f), Body)));        
           } else
             insert fnc(V2, subst(V1, Repl, Body)); 
       case op(str Name, Exp E1, Exp E2):
           insert op(Name, subst(V1, Repl, E1), subst(V2, Repl, E2));     
    };
}

data Type = intType() | strType();

data Error = constant(Exp E) | varuse(str Nm1, str Nm2);

data Cons = c(Exp E, Type T) | error(Error e);

data Constraints = constraints(list[Cons] cons);

public Constraints collect(Exp E)
{
   list[Cons] cons = [];
   
   visit(rename(E)){
   
   case op("add", Exp E1, Exp E2):
   		cons = cons  + [c(E1, intType()), c(E2, intType())];
   	
   case op("conc", Exp E1, Exp E2):
   	    cons = cons + [c(E1, strType()), c(E2, strType())];
   }   /* Put here a ";" and you get an ambiguous list */
   
   println("constraints: <cons>");
   
   return constraints(cons);
}

// Simplify a list of constraints
      

rule a1 constraints([list[Cons] C1, c(intcon(int N), Type T), list[Cons]C2]) =>
        	(T == intType()) ? constraints([C1, C2]) : constraints([C1, error(constant(intcon(N))), C2]);
        	
rule a2 constraints([list[Cons] C1, c(strcon(str S), Type T), list[Cons]C2]) =>
        	(T == strType()) ? constraints([C1, C2]) : constraints([C1, error(constant(strcon(S))), C2]);
       
rule a3 constraints([list[Cons] C1, c(var(str Nm1), Type T1), list[Cons]C2,  c(var(str Nm2), Type T2), list[Cons]C3]):{
        if(Nm1 == Nm2){
           if(T1 == T2) {
              insert constraints([C1, c(var(Nm1), T1), C2,  C3]);
           } else {
              insert constraints([C1, error(varuse(Nm1, Nm2)), C2,  C3]);
           }
        } else {
           fail;
           }
        };      
                  

public bool solveConstraints(Constraints constraints)
{
   int nError = 0;
   for(error(Error e) <- constraints.cons){
      println("Error: <e>");
      nError = nError + 1;
   }   
   return nError == 0;
}

public bool typecheck(Exp E)
{
   return solveConstraints(collect(E));
}

// Tests

test !typecheck(op("seq", op("add", var("a"), var("b")),
                         op("conc", var("a"), op("conc", var("b"), strcon("abc")))));


test rename(apply(fnc("a", var("a")),  intcon(3))) == 
            apply(fnc("_x1",var("_x1")),intcon(3));
         
test rename(apply(fnc("a", var("b")),  intcon(3))) ==
            apply(fnc("_x2",var("b")),intcon(3));
         
test rename(apply(fnc("a", apply(fnc("b", var("b")), intcon(2))), intcon(1))) ==
            apply(fnc("_x3",apply(fnc("_x4",var("_x4")),intcon(2))),intcon(1));
          
test rename(apply(fnc("a", apply(fnc("b", var("a")), intcon(2))), intcon(1))) ==
            apply(fnc("_x5",apply(fnc("_x6",var("_x5")),intcon(2))),intcon(1));
         
test rename(apply(fnc("a", apply(fnc("b", var("a")), intcon(2))), var("c"))) ==
            apply(fnc("_x7",apply(fnc("_x8",var("_x7")),intcon(2))),var("c"));
 /*                     
   test subst("a", intcon(1), apply(fnc("a", var("a")),  intcon(3))) ==
                                     apply(fnc("a", intcon(1)),intcon(3));
                                     
   test subst("a", var("b"), apply(fnc("a", var("b")),  var("a"))) ==
                                    apply(fnc("a",var("b")),var("b"));
                                    
   test subst("a", op("add", intcon(3),var("b")), apply(fnc("b", var("b")),  var("a"))) ==
                                                  apply(fnc("_x1",var("_x1")),op("add", intcon(3),var("b")));
  */
