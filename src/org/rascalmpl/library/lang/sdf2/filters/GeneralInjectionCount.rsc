module lang::sdf2::filters::GeneralInjectionCount

import ParseTree;
import List;

private default bool injection(Tree _) = false;
private bool injection(appl(prod(Symbol _,[Symbol _],set[Attr] _), [Tree _])) = true;

&T <: Tree generalInjectionCountFilter(amb(set[&T <: Tree] alts)) {
  as = [*alts];
  counts = [(0 | it + 1 | a <- as, /Tree t := a, injection(t)) | a <- alts];
  new = {as[i] | i <- index(as), counts[i] == min(counts)};
  
  if (new == alts) {
    fail generalInjectionCountFilter;
  }
  else {
    return amb(new);
  } 
} 
