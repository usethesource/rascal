@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module demo::Slicing

import Set;
import Relation;
import demo::ReachingDefs;
import demo::Dominators;

// A relational definition of program slicing
//
// Based on the definitions given in 
//	D. Jackson & E.J. Rollins,
//  A new model of program dependences for reverse engineering,
//  Proceedings of the 2nd ACM SIGSOFT symposium on Foundations of software engineering
//  Pages: 2 - 10, 1994


set[use] BackwardSlice(
	set[stat] CONTROLSTATEMENT, 
	rel[stat,stat] PRED,
	rel[stat,var] USES,
	rel[stat,var] DEFS,	
	use Criterion) {

	rel[stat, def] REACH = reachingDefinitions(DEFS, PRED);

	// Compute the relation between each use and corresponding definitions: ud

	rel[use,def] use_def = 
 	 {<<S1,V>, <S2,V>> | <stat S1, var V> <- USES, <stat S2, V> <- REACH[S1]};

	// Internal dependencies per statement

	rel[def,use] def_use_per_stat  = 
     		{<<S,V1>, <S,V2>> | <stat S, var V1> <- DEFS, <S, var V2> <- USES}
    		 +
    		 {<<S,V>, <S,"EXEC">> | <stat S, var V> <- DEFS}
    		 +
    		 {<<S,"TEST">,<S,V>> | stat S <- CONTROLSTATEMENT, 
			    	 	  <S, var V> <- domainR(USES, {S})};

	// Control dependence: control-dependence

	rel[stat, set[stat]] CONTROLDOMINATOR = domainR(dominators(PRED, 1), CONTROLSTATEMENT); //TODO 1 is ad hoc

	rel[def,use] control_dependence  =
	   { <<S2, "EXEC">,<S1,"TEST">> | <stat S1, set[stat] S2s> <- CONTROLDOMINATOR, stat S2 <- S2s};

	// Control and data dependence: use-control-def

	rel[use,def] use_control_def = use_def + control_dependence;
	rel[use,use] USE_USE = (use_control_def o def_use_per_stat)*;
//	rel[def,def] DD = (du o ucd)*
//	rel[use,def] UD = ucd o (du o ucd)*
//	rel[def,use] DU = du o (ucd o du)*

	return USE_USE[Criterion];
}

	// Example from Frank Tip's slicing survey

	//       1: read(n)
	//	 2: i := 1
	//	 3: sum := 0
	//	 4: product := 1
	//	 5: while i<= n do
	//	    begin
	//	 6:	sum := sum + i
	//	 7:	product := product * i;
	//	 8:	i := i + 1
	//	    end
	//       9: write(sum)
	//	 10: write(product)

private	rel[stat,stat] PRED	= {<1,2>, <2,3>, <3,4>, <4,5>, <5,6>, <5,9>, <6,7>,
			    <7,8>,<8,5>, <8,9>, <9,10>};

private	rel[stat,var] DEFS	= {<1, "n">, <2, "i">, <3, "sum">, <4,"product">,
			  <6, "sum">, <7, "product">, <8, "i">};

private	rel[stat,var] USES	= {<5, "i">, <5, "n">, <6, "sum">, <6,"i">,
			   <7, "product">, <7, "i">, <8, "i">, <9, "sum">, 
			   <10, "product">};

private	set[int] CONTROLSTATEMENT = { 5 };

private	set[use] Example1 = 
		BackwardSlice(CONTROLSTATEMENT, PRED, USES, DEFS, <9, "sum">);

private	set[use] Example2 =
		BackwardSlice(CONTROLSTATEMENT, PRED, USES, DEFS,<10, "product">);

public test bool t1() =
  Example1 ==  
	     { <1, "EXEC">, <2, "EXEC">,  <3, "EXEC">, <5, "i">, <5, "n">,  
	        <6, "sum">, <6, "i">, <6, "EXEC">, <8, "i">, <8, "EXEC">, 
	       <9, "sum"> };

public test bool t2() =
  Example2 ==
            { <1, "EXEC">,  <2, "EXEC">, <4, "EXEC">, <5, "i">, <5, "n">, 
	      <7, "i">, <7, "product">, <7, "EXEC">, 
	       <8, "i">, <8, "EXEC">, <10,  "product">  };

/*

DOMINATES == {<1, 2>, <1, 3>, <1, 4>, <1, 5>, <1, 6>, 
 <1, 9>, <1, 7>, <1, 8>, <1, 10>, <2, 3>, <2, 4>, <2, 5>, <2, 6>, <2, 9>,
 <2, 7>, <2, 8>, <2, 10>, <3, 4>, <3, 5>, <3, 6>, <3, 9>, <3, 7>,
 <3, 8>, <3, 10>, <4, 5>, <4, 6>, <4, 9>, <4, 7>, <4, 8>, <4, 10>, <5, 6>,
 <5, 9>, <5, 7>, <5, 8>, <5, 10>, <6, 7>, <6, 8>, <9, 10>, <7, 8>}

POST-DOMINATOR == {<5, 6>, <5, 9>, <5, 7>, <5, 8>,
 <5, 10>}

-------------------------------------------------------------------------

*/
