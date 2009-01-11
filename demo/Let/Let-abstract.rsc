module Let-abstract

import Integer;

data Exp let(str name, Exp exp1, Exp exp2) | var(str  name) | intCon(int val);

// Rename all bound variables in an Exp
// Version 1: purely functional
// Exp: given expression to be renamed
// rel[Var,Var]: renaming table
// Int: counter to generate unique variables

public Exp rename(Exp E, map[str,str] Rn, int Cnt) {
    switch (E) {
    case let(var(str Name), Exp E1, Exp E2): {
    	 	str Y = "x" + toString(Cnt);
         	return let(Y, rename(E1, Rn, Cnt), rename(E2, Rn + (V: Y), Cnt+1));
         }
    case var(str Name) : return var(Rn[Name]);

    default: return E;
    }
}

public Exp test(){
 return rename(let("a", intCon(3), var("b")), (), 0);
}