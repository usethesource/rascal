@ignore{TODO}
module lang::rascalcore::check::tests::TypeInstantiationTests

import lang::rascalcore::check::ATypeInstantiation;
import lang::rascalcore::check::AType;

test bool match1() = match(aint(), aint(), ()) == ();
test bool match2() = match(aint(), aint(), (), bindIdenticalVars=true) == ();
test bool match3() = match(aint(), aint(), (), bindIdenticalVars=false) == ();

test bool match4() = match(alist(aparameter("T", avalue())), alist(aint()), (), bindIdenticalVars=false) == ("T": aint());
test bool match5() = match(alist(aparameter("T", avalue())), label("X", alist(aint())), (), bindIdenticalVars=false) == ("T": aint());

@expected{invalidMatch}
test bool match6() = match(alist(aparameter("T", avalue())), \set(aint()), (), bindIdenticalVars=false) == ();