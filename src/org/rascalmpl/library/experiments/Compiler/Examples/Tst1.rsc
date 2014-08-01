module experiments::Compiler::Examples::Tst1

import List;
import ListRelation;

public test bool tstToMapUnique(list[tuple[&A, &B]] L) =
  (toSet(domain(L)) == toSet(L<0>)) ==> (toMapUnique(L) == toMapUnique(toSet(L)));

//value main(list[value] args) = subscriptionWrapped([<{},"">,<{[],[false]},"">]);