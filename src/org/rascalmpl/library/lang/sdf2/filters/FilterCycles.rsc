module lang::sdf2::filters::FilterCycles

import ParseTree;

&T<:Tree amb(set[&T<:Tree] alts) {
  new = { a | a <- alts, /t:cycle(_,_) !:= a};
  
  if (new == alts) {
    fail amb;
  }
  else {
    return amb(new);
  }
}