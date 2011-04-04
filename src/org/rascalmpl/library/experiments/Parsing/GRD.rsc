@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module experiments::Parsing::GRD


// Experiment: Generalized Recursive Descent Parsing.
// See A. Johnstone & Elisabeth Scott, 
// Generalised Recursive Descent Parsing and Follow Determinism,
// Proceedings of Compiler Construction 98,
// LNCS 1383, pp. 16-30, 1998

import List;
import Set;
import IO;

list[str] input = [];   // The global input of the parser
int current = 0;   // Current position in input
bool debug = false;


// Definine new input

public bool defInput(list[str] inp){
  input = inp;
  current = 0;
  return true;
}

// Match a token and advance current when it matches.

public bool match(str token){

  if(current < size(input)){
     if(input[current] == token){
	if(debug)println("match <token> at <current> yields true");
        current += 1;
	return true;
     }
  }
  if(debug)println("match <token> at <current> yields false");
  return false;
}

// Example grammar:
// A ::= BA | C | d
// B ::= abb | ab
// C ::= c | cd

// Each parse function starts at the current input position and returns a
// (possibly empty) set of end position describing the input segment that could
// be parsed.

public set[int] A(){
  int entry_current = current;
  set[int] return_set = {};
  
  // BA
  for(int c <- B()){
      current = c;
      set[int] A_set = A();
      if(size(A_set) > 0)
         return_set += A_set;
  }
  // C
  current = entry_current;
  set[int] C_set = C();
  if(size(C_set) > 0){
     return_set += C_set;
  }
  // d
  current = entry_current;
  if(match("d"))
     return_set += {current};
     
  return return_set;
}

public set[int] B (){
  int entry_current = current;
  set[int] return_set = {};
  
  if(match("a")){
     if(match("b"))
        return_set += {current}; 
     if(match("b"))
        return_set += {current};
  }
  return return_set;
}

public set[int] C (){
  int entry_current = current;
  set[int] return_set = {};
  
  if(match("c")){
     return_set += {current};
     
     if(match("d")){
        return_set += {current};
     }
  }
 
  return return_set;
}

// Tests

test defInput(["d"]) &&  (A() == {1});
test defInput(["c"]) && (A() == {1});
test defInput(["c", "d"]) && (A() == {1, 2});
test defInput(["a", "b", "b", "c", "d"]) && (A() == {4, 5});
