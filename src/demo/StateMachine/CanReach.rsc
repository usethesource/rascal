module demo::StateMachine::CanReach

import demo::StateMachine::Syntax;
import Relation;

{Decl ";"}+ example = 
    	    state S1;
    	    state S2;
	    state S3;
	    trans a: S1 -> S2;
	    trans b: S2 -> S1;
	    trans a: S1 -> S3;

public rel[State, State] getTransitions({Decl ";"}+ fsm){
   return { <from, to> | trans <IdCon a>: <IdCon from> -> <IdCon to> <- fsm };
}

public map[IdCon, set[IdCon]] printCanReach(){
  return ( s: trTransitions[State] | IdCon s <- carrier(getTransitions(fileName)+) );
}

