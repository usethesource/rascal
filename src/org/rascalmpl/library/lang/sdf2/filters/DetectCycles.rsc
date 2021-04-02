module lang::sdf2::filters::DetectCycles

import ParseTree;

&T<:Tree cycleDetectionFilter(amb(set[&T<:Tree] alts)) {
  if (/t:cycle(_,_) <- alts) {
    throw "Cycle detected at <t@\loc>";
  }
  else {
    fail cycleDetectionFilter;
  }
}