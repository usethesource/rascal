@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module \Fun-concrete

// OUTDATED, see Fun-abstract

import \Fun-syntax;

set[Var] allVars(Exp E) {
    return {V | Var V <- E};
}

set[Var] boundVars(Exp E) {
    return {V | `fn <Var V>` <- E};
} 

set[Var] freeVars(Exp E) {
    return allVars(E) - boundVars(E);
}

// Generate a fresh variable if V does not occur in 
// given set of variables.

Var fresh(Var V, set[Var] S) {
    if (V in S){ return prime(V); } else {return V;}
}

// Substitution: replace all occurrences of V in E2 by E1

Exp subst(Var V1, Exp E1, Exp E2) {

    return visit (E2) { 
      case <Var V2>: insert (V1==V2) ? E1 : V2;

      case <Exp Ea> <Exp Eb>:
        insert ` <subst(V, E, Ea)> <subst(V, E, Eb)> `;

      case fn <Var V2> => <Var Ea>:
        if (V1 == V2) { insert ` fn <V2> =\> <Ea> `; }

      case fn <Var V2> => <Exp Ea>:
        if(V1 != V2 && !(V1 in freeVars(E2) && 
           V2 in freeVars(E1))){
           insert ` fn <V2> =\> <subst(V1, E1, Ea)> `;
        }              
 
      case fn <Var V2> => <Exp Ea>: 
        if(V1 != V2 && V1 in freeVars(Ea) &&
           V2 in freeVars(E1)){
           Var V3 = fresh(V2, freeVars(Ea) + freeVars(E1));
           Exp EaS = subst(V1, E1, subst(V2, V3, E2));
           insert ` fn <V3> =\> <EaS> `;
        }
    };
}
