module lang::locic::ast::Booleans

data Formula 
  = \true()
  | \false()
  | \not(Formula arg)
  | \and(Formula lhs, Formula rhs)
  | \or(Formula lhs, Formula rhs)
  | \if(Formula lhs, Formula rhs)
  | \fi(Formula lhs, Formula rhs)
  | \iff(Formula lhs, Formula rhs)
  ;
