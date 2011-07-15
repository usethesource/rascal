@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module demo::StateMachine::Syntax

// Concrete syntax for a simple state machine as suggested by Gorel Hedin at GTTSE09
// A State machine consists of a series of declarations for states and transitions

layout Whitespace = [\ \t\n]*;

syntax IdCon = [A-Za-z] [A-Za-z\-0-9]*
				!>> [A-Za-z\-0-9]
				;

syntax State = "state" IdCon;
syntax Trans = "trans" IdCon ":" IdCon "-\>" IdCon;
syntax Decl = State | Trans;
start syntax FSM = "finite-state" "machine" {Decl ";"}+;
