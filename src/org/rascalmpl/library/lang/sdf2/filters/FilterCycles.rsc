module lang::sdf2::filters::FilterCycles

import ParseTree;

&T<:Tree cycleFilter(amb(set[&T<:Tree] alts)) {
  new = { a | a <- alts, /cycle(_,_) !:= a};
  
  if (new == alts) {
    fail cycleFilter;
  }
  else {
    return amb(new);
  }
}