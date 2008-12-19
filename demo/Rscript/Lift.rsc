module Lift

/*
 * TODO: remove qualifier
 */

// Component structure/lifting example from tutorial

type str proc;
type str comp;

rel[proc,proc] Calls = {<"main", "a">, <"main", "b">, <"a", "b">, <"a", "c">, <"a", "d">, <"b", "d">};        

set[comp] Components = {"Appl", "DB", "Lib"};

rel[proc, comp] PartOf = {<"main", "Appl">, <"a", "Appl">, <"b", "DB">, 
			  <"c", "Lib">, <"d", "Lib">};

rel[comp,comp] lift(rel[proc,proc] aCalls, rel[proc,comp] aPartOf){
	return { <C1, C2> | <proc P1, proc P2> : aCalls, <comp C1, comp C2> : aPartOf[P1] * aPartOf[P2]};
}

rel[comp,comp] ComponentCalls = lift(Calls, PartOf);

public bool checkResult(){
	return Lift::ComponentCalls == { < "DB" , "Lib" > , < "Appl" , "Lib" > , 
			                   < "Appl" , "DB" > , < "Appl" , "Appl" > };
}



