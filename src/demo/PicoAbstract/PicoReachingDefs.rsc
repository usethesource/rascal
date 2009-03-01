module demo::PicoAbstract::PicoReachingDefs

alias Def = tuple[Stat theStat, Var theVar];
alias Use = tuple[Stat theStat, Var theVar];

set[Stat] predecessor(rel[Stat,Stat] P, Stat S) { 
    return invert(P)[S]; 
}

set[Stat] successor(rel[Stat,Stat] P, Stat S) { 
    return P(S);
}

rel[Stat, Def] reaching_definitions(rel[Stat,Var] DEFS, 
                                        rel[Stat,Stat] PRED) {

    set[Stat] STATEMENT = carrier(PRED);

    rel[Stat,Def] DEF  = {<S,<S,V>> | <Stat S, Var V>: DEFS};

    rel[Stat,Def] KILL = {<S1, <S2, V>> | <Stat S1, Var V> : DEFS, 
                                           <Stat S2, V> : DEFS, 
                                           S1 != S2
                         },
     with {
		IN = {},
		OUT = DEF;
     } solve {
		IN  =  {<S, D> | int S : STATEMENT, 
                         Stat P : predecessor(PRED,S), 
                         Def D : OUT[P]};
		OUT =  {<S, D> | int S : STATEMENT, 
                         Def D : DEF[S] | (IN[S] - KILL[S])}
    }
    return IN;
}