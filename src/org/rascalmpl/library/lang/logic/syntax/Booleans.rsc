module lang::logic::syntax::Booleans

import lang::basic::Whitespace;

syntax Formula
  = \true :  "true"
  | \false: "false"
  | \not  : "!" Formula arg
  > left \and  : Formula lhs "&" Formula rhs
  > left \or   : Formula lhs "|" Formula rhs
  > non-assoc ( right \if   : Formula lhs "=\>" Formula rhs
              | left  \fi   : Formula lhs "\<=" Formula rhs
  )
  > non-assoc \iff  : Formula lhs "\<=\>" Formula rhs
  ;  
  