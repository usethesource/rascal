module lang::sdf2::filters::GeneralInjectionCount

import ParseTree;
import List;

private default bool injection(Tree _) = false;
private bool injection(appl(prod(Symbol _,[Symbol _],set[Attr] _), [Tree _])) = true;

&T <: Tree generalInjectionCountFilter(amb(set[&T <: Tree] alts)) {
  as = [*alts];
  counts = [(0 | it + 1 | /Tree t := a, injection(t)) | a <- as];
  minimum = min(counts);
  new = {as[i] | i <- index(as), counts[i] == minimum};
  
  if (new == alts) {
    fail generalInjectionCountFilter;
  }
  else {
    return amb(new);
  } 
} 
