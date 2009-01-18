module Lambda-abstract


type str Var;

data Exp fnc(Var name, Exp exp) | Var Name | apply(Exp exp1, Exp exp2);

public set[Var] allVars(Exp E) {
    return {Name | Var Name : E};
}

public set[Var] boundVars(Exp E) {
    return {Name | fnc(Var Name, Exp E1) : E};
} 

public set[Var] freeVars(Exp E) {
    return allVars(E) - boundVars(E);
}

// Generate a fresh variable if V does not occur in 
// given set of variables.

public Var fresh(Var V, set[Var] S) {
    if (V in S){ return prime(V); } else {return V;}
}

// Substitution: replace all occurrences of V in  expression Org (E2) by expression Repl (E1)

public Exp subst(Var V1, Exp Repl, Exp Org) {

    return visit (Org) { 
      case Var V2: insert (V1==V2) ? Repl : V2;

      case apply(Exp Ea, Exp Eb):
        insert apply(subst(V, E, Ea), subst(V, E, Eb));
/*
      case fnc <Var V2> => <Exp Ea>:
        if(V1 != V2 && !(V1 in freeVars(E2) && 
           V2 in freeVars(E1))){
           insert [| fn <V2> =\> <subst(V1, E1, Ea)> |];
        }              
 
      case fnc <Var V2> => <Exp Ea>: 
        if(V1 != V2 && V1 in freeVars(Ea) &&
           V2 in freeVars(E1)){
           Var V3 = fresh(V2, freeVars(Ea) + freeVars(E1));
           Exp EaS = subst(V1, E1, subst(V2, V3, E2));
           insert [| fn <V3> =\> <EaS> |];
        }
*/
      case fnc(Var V2, Exp Body):
        if(V1 == V2)
        	insert fnc(V2, subst(V1, V2, Body));
        	
        else if(V2 in freeVars(Repl)){
           Var V2f = fresh(V2, freeVar(Body) + freeVars(Repl));
           insert fnc(V2f, subst(V1, Repl, subst(V2, V2f, Body)));        
        } else
        	insert fnc(V2, Body);
    };
}

public Exp test1(){
 return rename(let("a", intcon(3), "a"), ());
}

public Exp test2(){
 return rename(let("a", intcon(3), "b"), ());
}

public Exp test3(){
 return rename(let("a", intcon(1),let("b", intcon(2), "b")), ());
}

public Exp test4(){
 return rename(let("a", intcon(1), op(let("b", intcon(2), "b"), "a")), ());
}

public Exp test5(){
 return rename(let("a", intcon(1), op(let("a", intcon(2), "a"), "a")), ());
}