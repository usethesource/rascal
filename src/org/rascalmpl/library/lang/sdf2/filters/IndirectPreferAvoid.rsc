module lang::sdf2::filters::IndirectPreferAvoid

import ParseTree;
import Set;

private default bool isPreferred(Tree _) = false;
private bool isPreferred(appl(prod(_,_,{\tag("prefer"()),*_}),_)) = true;
private bool isPreferred(appl(prod(Symbol _,[Symbol _],set[Attr] _), [Tree arg])) = isPreferred(arg);

private default bool isAvoided(Tree _) = false;
private bool isAvoided(appl(prod(_,_,{\tag("avoid"()),*_}),_)) = true;
private bool isAvoided(appl(prod(Symbol _,[Symbol _],set[Attr] _), [Tree arg])) = isAvoided(arg);

@doc{
Import his module if you want prefer/avoid filtering enabled for your grammar. Use @prefer and @avoid to
label alternatives.
}
&T <:Tree indirectPreferAvoidFilter(amb(set[&T <:Tree] alternatives)) {
  prefers = { t | t <- alternatives, isPreferred(t)};
  
  if (prefers != {}, size(alternatives) != size(prefers)) {
    return amb(prefers);
  }
  
  avoids = { t | t <- alternatives, isAvoided(t)};
  
  if (avoids != {}, size(alternatives) != size(avoids)) {
    return amb(alternatives - avoids);
  }
  
  fail;
}