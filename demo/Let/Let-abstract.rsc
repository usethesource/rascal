module Let-abstract

import Integer;

type str Var;

data Exp let(Var var, Exp exp1, Exp exp2) | Var  varVal | int intVal;

// Rename all bound variables in an Exp
// Version 1: purely functional
// Exp: given expression to be renamed
// rel[Var,Var]: renaming table
// Int: counter to generate unique variables

public Exp rename(Exp E, map[Var,Var] Rn, int Cnt) {
    switch (E) {
    case let(Var V, Exp E1, Exp E2): {
    	 	Var Y = "x" + toString(Cnt);
         	return let(Y, rename(E1, Rn, Cnt), rename(E2, Rn + (V: Y), Cnt+1));
         }
    case Var V: return Rn[V];

    default: return E;
    }
}

public Exp test(){
 return rename(let("a", 3, "b"), (), 0);
}