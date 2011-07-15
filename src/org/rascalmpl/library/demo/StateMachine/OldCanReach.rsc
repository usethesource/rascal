@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module demo::StateMachine::OldCanReach

// A simple state machine FSM as suggested by Gorel Hedin at GTTSE09

import demo::StateMachine::Syntax;
import Relation;
import Map;
import IO;
import Node;

@doc{Extract from a give FSM all transitions as a relation}
public rel[IdCon, IdCon] getTransitions(FSM fsm){
   return { <delAnnotationsRec(from), delAnnotationsRec(to)> | /`trans <IdCon a>: <IdCon from> -> <IdCon to>` <- fsm };
}

@doc{Compute all states that can be reached}
public map[IdCon, set[IdCon]] canReach(FSM fsm){
  transitions = getTransitions(fsm);
  return ( s: (transitions+)[s] | IdCon s <- carrier(transitions) );
}

// Examples and tests

public FSM example0 =
	   `finite-state machine
	      state S1;
	      state S2;
	      trans a: S1 -> S2`;

public FSM example = 
       `finite-state machine
          state S1;
    	  state S2;
	      state S3;
	      trans a: S1 -> S2;
	      trans b: S2 -> S1;
	      trans a: S1 -> S3`;

IdCon S1 = (IdCon) `S1`;
IdCon S2 = (IdCon) `S2`;
IdCon S3 = (IdCon) `S3`;

public set[str] readable(set[IdCon] i) {
  return { "<a>" | a <- i };
}

public rel[str,str] readable(rel[IdCon,IdCon] r) {
  return { <"<from>","<to>"> | <from, to> <- r };
}

test getTransitions(example0) == {<S1, S2>};

test getTransitions(example) == {<S1, S2>, <S2, S1>, <S1, S3>};
 
test canReach(example) == (S1 : {S1, S2, S3}, 
                           S2 : {S1, S2, S3},
                           S3 : {});

