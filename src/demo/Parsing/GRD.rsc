module demo::Parsing::GRD


// Experiment: Generalized Recursive Descent Parsing.
// See A. Johnstone & Elisabeth Scott, Generalised Recursive Descent Parsing and Follow Determinism,
//

import Set;
import IO;

list[str] input;   // The global input of the parser
int current = 0;   // Current position in input


// Definine new input

public void defInput(list[str] inp){
  input = inp;
  current = 0;
}

// Match a token and advance current when it matches.

public bool match(str token){
  if(input[current] == token){
     println("match <token> at <current> yields true");
     current += 1;
     return true;
  } 
  println("match <token> at <current> yields false");
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
  
  println("C1 -- <return_set>, <current>");
  if(match("c")){
     return_set += {current};
     println("C2 -- <return_set>, <current>");
     
     if(match("d")){
        a = 1; //return_set += {current};
     }
     println("C3 -- <return_set>, <current>");
  }
  println("C4 -- <return_set>, <current>");
  return return_set;
}

public bool test(){
//  defInput(["d"]);
//  res = A();
//  println("parse d = <res>");
  
  defInput(["c"]);
  res = C();
  println("parse c = <res>");
  
   defInput(["c", "d"]);
  res = A();
  println("parse cd = <res>");
  
  return true;
}
