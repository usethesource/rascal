module demo::Parsing::Test

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

// Functions omitted ...

public set[int] C (){
  int entry_current = current;
  set[int] return_set = {};
  
  println("C1 -- <return_set>");
  if(match("c")){
     return_set += {current};
     println("C2 -- <return_set>");
     
     if(match("d")){                       <-- verdachte if
        return_set += {current};
     }
     println("C3 -- <return_set>");
  }
  println("C4 -- <return_set>");
  return return_set;
}

public bool test(){
  
  defInput(["c", "d"]);
  res = C();
  println("parse cd = <res>");
  
  return true;
}
