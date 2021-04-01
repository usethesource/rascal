module lang::sdf2::filters::PreferAvoid

import ParseTree;
import Set;

@doc{
Import his module if you want prefer/avoid filtering enabled for your grammar. Use @prefer and @avoid to
label alternatives.
}
&T <:Tree preferAvoidFilter(amb(set[&T <:Tree] alternatives)) {
  prefers = { t | t:appl(prod(_,_,{\tag("prefer"()),*_}),_) <- alternatives};
  
  if (prefers != {}, size(alternatives) != size(prefers)) {
    return amb(prefers);
  }
  
  avoids = { t | t:appl(prod(_,_,{\tag("avoid"()),*_}),_) <- alternatives};
  
  if (avoids != {}, size(alternatives) != size(avoids)) {
    return amb(alternatives - avoids);
  }
  
  fail;
}