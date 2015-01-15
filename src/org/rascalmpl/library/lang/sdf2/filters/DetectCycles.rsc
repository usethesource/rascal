module lang::sdf2::filters::DetectCycles

import ParseTree;

&T<:Tree amb(set[&T<:Tree] alts) {
  if (/t:cycle(_,_) <- alts) {
    throw "Cycle detected at <t.origin>";
  }
  else {
    fail amb;
  }
}