module demo::StateMachine::CanReach

// A simple state machine FSM as suggested by Gorel Hedin at GTTSE09

// First import its concrete syntax

import demo::StateMachine::Syntax;
import Relation;
import Map;
import UnitTest;
import IO;


// Extract from a give FSM all transitions as a relation

public rel[IdCon, IdCon] getTransitions(FSM fsm){
   return { <from, to> | `trans <IdCon a>: <IdCon from> -> <IdCon to>` <- fsm };
}

// Compute all states that can be reached

public map[IdCon, set[IdCon]] canReach(FSM fsm){
  transitions = getTransitions(fsm);
  return ( s: (transitions+)[s] | IdCon s <- carrier(transitions) );
}

// Examples and tests

public FSM example0 =
	   finite-state machine
	      state S1;
    	  state S2;
	      trans a: S1 -> S2;

public FSM example = 
       finite-state machine
          state S1;
    	  state S2;
	      state S3;
	      trans a: S1 -> S2;
	      trans b: S2 -> S1;
	      trans a: S1 -> S3;

IdCon S1 = (IdCon) `S1`;
IdCon S2 = (IdCon) `S2`;
IdCon S3 = (IdCon) `S3`;

public bool test(){
  assertEqual(getTransitions(example0), {<S1, S2>});
  
  assertEqual(getTransitions(example), {<S1, S2>, <S2, S1>, <S1, S3>});
  
  assertEqual(canReach(example), (S1 : {S1, S2, S3}, 
                                  S2 : {S1, S2, S3},
                                  S3 : {}));
  return report();
}

