module lang::sdf2::filters::CountPreferAvoid

import ParseTree;
import Set;
import List;

@doc{
Import his module if you want prefer/avoid filtering with counting enabled for your grammar. Use @prefer and @avoid to
label alternatives.
}
&T <:Tree countPreferAvoidFilter(amb(set[&T <:Tree] alternatives)) {
  alts = [*alternatives];
  counts = [( 0 | it + 1 | /appl(prod(_,_,{\tag("prefer"()),*_}),_) := alt) | Tree alt <- alts];
  
  new = [alts[i] | int i <- index(alts), counts[i] == max(counts)];
  
  counts = [( 0 | it + 1 | /appl(prod(_,_,{\tag("avoid"()),*_}),_) := alt) | Tree alt <- new];
  
  result = {new[i] | int i <- index(new), counts[i] == min(counts)};
  
  if (result == alternatives) {
    fail countPreferAvoidFilter;
  }
  else {
    return amb(result);
  }
}