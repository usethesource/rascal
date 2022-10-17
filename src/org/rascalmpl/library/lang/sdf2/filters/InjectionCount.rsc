module lang::sdf2::filters::InjectionCount

import ParseTree;

private default int count(Tree _) = 0;
private int count(appl(prod(Symbol _,[Symbol _],set[Attr] _), [Tree arg])) = 1 + count(arg);

&T <: Tree injectionCountFilter(amb(set[&T <: Tree] alts)) {
  as = [*alts];
  counts = [count(a) | a <- as];
  new = {as[i] | i <- index(as), counts[i] == min(counts)};
  
  if (new == alts) {
    fail injectionCountFilter;
  }
  else {
    return amb(new);
  } 
} 
