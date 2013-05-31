module lang::sdf2::filters::CountPreferAvoid

import ParseTree;
import Set;
import List;

@doc{
Import his module if you want prefer/avoid filtering with counting enabled for your grammar. Use @prefer and @avoid to
label alternatives.
}
&T <:Tree amb(set[&T <:Tree] alternatives) {
  alts = [*alternatives];
  counts = [( 0 | it + 1 | /appl(prod(_,_,{\tag("prefer"()),*_}),_) := alt) | alt <- alts];
  
  new = [alts[i] | i <- index(alts), counts[i] == max(counts)];
  
  counts = [( 0 | it + 1 | /appl(prod(_,_,{\tag("avoid"()),*_}),_) := alt) | alt <- new];
  
  result = {alts[i] | i <- index(alts), counts[i] == min(counts)};
  
  if (result == alternatives) {
    fail amb;
  }
  else {
    return amb(result);
  }
}