module demo::StateMachine::CanReach

// A simple state machine FSM as suggested by Gorel Hedin at GTTSE09

// First import its concrete syntax

import demo::StateMachine::Syntax;
import Relation;
import Map;
import UnitTest;
import IO;

// Next declare and initialize example. Note that 
// (1) This variable has the syntactic type {Decl ";"}+
// (2) Concrete State Machine syntax occurs in the initialization value


//TODO All types {Decl ";"}+ should actually be FSM

public {Decl ";"}+ example = 
    	 [| state S1;
    	    state S2;
	        state S3;
	        trans a: S1 -> S2;
	        trans b: S2 -> S1;
	        trans a: S1 -> S3 |];

// Extract from a give FSM all transitions as a relation

public rel[IdCon, IdCon] getTransitions({Decl ";"}+ fsm){
   r = { <from, to> | [| trans <IdCon a>: <IdCon from> -> <IdCon to> |] <- fsm };
   return r;
}

// Finally compute all states that can be reached

public map[IdCon, set[IdCon]] canReach({Decl ";"}+ fsm){
  transitions = getTransitions(fsm);
  return ( s: transitions[s] | IdCon s <- carrier(transitions+) );
}

public bool test(){
  //assertEqual(getTransitions(example), {<IdCon[|S1|], IdCon[|S2|]>, <IdCon[|S2|], IdCon[|S1|]>, <IdCon[|S1|], IdCon[|S3|]>});
 
  map[IdCon, set[IdCon]] cr = canReach(example);
  
  for(IdCon a <- domain(cr)){
      println(a, cr[a]);
  }
  assertEqual(canReach(example), ([|S1|] :{[|S1|], [|S2|], [|S3|]}, [|S2|] : {[|S1|], [|S2|], [|S3|]}, [|S3|] : {}));
  return report();
}

