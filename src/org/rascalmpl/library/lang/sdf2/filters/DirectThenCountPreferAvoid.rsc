module lang::sdf2::filters::DirectThenCountPreferAvoid

import ParseTree;
import Set;
import List;

@doc{
Import his module if you want prefer/avoid filtering with counting enabled for your grammar. Use @prefer and @avoid to
label alternatives.
}
&T <:Tree amb(set[&T <:Tree] alternatives) {
  if (size(alternatives) == 1) {
    fail amb;
  }
  // first check for direct prefers / avoids
  direct = { t | t:appl(prod(_,_,{\tag("prefer"()),*_}),_) <- alternatives};
  if (size(direct) == 1) {
  	return ParseTree::amb(direct);
  }
  if (size(direct) != 0) {
  	// some were filtered
 	alternatives = direct; 
  }
  
  avoids = { t | t:appl(prod(_,_,{\tag("avoid"()),*_}),_) <- alternatives};
  
  if (size(alternatives) == (size(avoids) + 1)) {
    return ParseTree::amb(alternatives - avoids);
  }
  
  alternatives -= avoids;
  
  
  // now filter the nested prefers
  
  alts = [*alternatives];
  counts = [( 0 | it + 1 | /appl(prod(_,_,{\tag("prefer"()),*_}),_) := alt) | alt <- alts];
  
  new = [alts[i] | i <- index(alts), counts[i] == max(counts)];
  
  counts = [( 0 | it + 1 | /appl(prod(_,_,{\tag("avoid"()),*_}),_) := alt) | alt <- new];
  
  result = {new[i] | i <- index(new), counts[i] == min(counts)};
  
  if (result == alternatives) {
    fail amb;
  }
  else {
    return amb(result);
  }
}