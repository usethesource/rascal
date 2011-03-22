module lang::logic::syntax::Truth

import lang::std::Whitespace;
import lang::std::Layout;
import lang::std::Comment;
  
// True only accepts true Boolean formulas              
syntax True 
  = "true"
  | bracket "(" True ")"
  | "not" False f
  > left ( True lt "and" True rt
         | True lt "or" True rt
         | False lf "or" True rt
         | True lt "or" False rf
         )
  ;
  
// False only accepts false Boolean formulas         
syntax False 
  = "false"
  | bracket "(" False ")"
  | "not" True t
  > left ( False lf "and" False rf
         | True  lt "and" False rf
         | False lf "and" True  rt
         | False lf "or" False  rf
         )
  ;
  