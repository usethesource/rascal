module demo::Lift

// Given:
// - a call relation between procedures
// - a partOf relation between procedures and components
// lift the call structure to the component level.
// In other words: a call between two procedures will be lifted to
// a call between the components to which each procedure belongs

alias proc = str;
alias comp = str ;

public rel[comp,comp] lift(rel[proc,proc] aCalls, rel[proc,comp] aPartOf){
	return { <C1, C2> | <proc P1, proc P2> <- aCalls, <comp C1, comp C2> <- aPartOf[P1] * aPartOf[P2]};
}

// The following only for testing

rel[proc,proc] Calls = {<"main", "a">, <"main", "b">, <"a", "b">, <"a", "c">, <"a", "d">, <"b", "d">};        

set[comp] Components = {"Appl", "DB", "Lib"};

rel[proc, comp] PartOf = {<"main", "Appl">, <"a", "Appl">, <"b", "DB">, 
			  			  <"c", "Lib">, <"d", "Lib">};

rel[comp,comp] ComponentCalls = lift(Calls, PartOf);

	test ComponentCalls ==
	            { < "DB" , "Lib" > , < "Appl" , "Lib" > , 
			      < "Appl" , "DB" > , < "Appl" , "Appl" > }
			   ;



