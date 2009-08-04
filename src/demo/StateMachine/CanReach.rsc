module demo::StateMachine::CanReach

// A simple state machine FSM as suggested by Gorel Hedin at GTTSE09

// First import its concrete syntax

import demo::StateMachine::Syntax;
import Relation;

// Next declare and initialize example. Note that 
// (1) This variable has the syntactic type {Decl ";"}+
// (2) Concrete State Machine syntax occurs in the initialization value

{Decl ";"}+ example = 
    	    state S1;
    	    state S2;
	    state S3;
	    trans a: S1 -> S2;
	    trans b: S2 -> S1;
	    trans a: S1 -> S3;

// Extract from a give FSM all transitions as a relation

public rel[State, State] getTransitions({Decl ";"}+ fsm){
   return { <from, to> | trans <IdCon a>: <IdCon from> -> <IdCon to> <- fsm };
}

// Finally print all states that can be reached

public map[IdCon, set[IdCon]] printCanReach(){
  return ( s: trTransitions[State] | IdCon s <- carrier(getTransitions(fileName)+) );
}

