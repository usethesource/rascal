module lang::rascal::types::tests::TypeInstantiationTests

import lang::rascal::types::TypeInstantiation;
import Type;
import lang::rascal::types::AbstractType;
import lang::rascal::types::TypeExceptions;

test bool match1() = match(\int(), \int(), ()) == ();
test bool match2() = match(\int(), \int(), (), bindIdenticalVars=true) == ();
test bool match3() = match(\int(), \int(), (), bindIdenticalVars=false) == ();

test bool match4() = match(\list(\parameter("T", \value())), \list(\int()), (), bindIdenticalVars=false) == ("T": \int());
test bool match5() = match(\list(\parameter("T", \value())), label("X", \list(\int())), (), bindIdenticalVars=false) == ("T": \int());

@expected{invalidMatch}
test bool match6() = match(\list(\parameter("T", \value())), \set(\int()), (), bindIdenticalVars=false) == ();