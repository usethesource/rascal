module lang::logic::syntax::Booleans

import lang::std::Whitespace;
import lang::std::Layout;
import lang::std::Comment;

syntax Formula
  = \true :  "true"
  | \false: "false"
  | \not  : "!" Formula arg
  > left ( \and  : Formula lhs "&" Formula rhs
         | \or   : Formula lhs "|" Formula rhs
         )
  > non-assoc ( right \if   : Formula lhs "=\>" Formula rhs
              | left  \fi   : Formula lhs "\<=" Formula rhs
  )
  > non-assoc \iff  : Formula lhs "\<=\>" Formula rhs
  ;  
