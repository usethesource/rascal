module lang::sdf2::filters::DetectCycles

&T<:Tree amb(set[&T<:Tree] alts) {
  if (/t:cycle(_,_) <- alts) {
    // throw "Cycle detected at <t@\loc>";
    x;
  }
  else {
    fail amb;
  }
}