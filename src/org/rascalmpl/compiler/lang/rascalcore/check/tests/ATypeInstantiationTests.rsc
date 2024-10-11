module lang::rascalcore::check::tests::ATypeInstantiationTests

import lang::rascalcore::check::ATypeInstantiation;
import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;

test bool matchIntInt() = matchRascalTypeParams(aint(), aint(), ()) == ();

test bool matchIntStr() = matchRascalTypeParams(aint(), astr(), ()) == ();

test bool matchPar1() = matchRascalTypeParams(aparameter("T", avalue()), aint(), ()) == ("T": aint());
test bool matchPar2() = matchRascalTypeParams(aparameter("T", avalue()), aparameter("U", avalue()), ()) == ("T": aparameter("U", avalue()));

test bool matchPar3() = matchRascalTypeParams(alist(aparameter("T", avalue())), alist(aint()), ()) == ("T": aint());
test bool matchPar4() = matchRascalTypeParams(alist(aparameter("T", avalue())), alist(aint())[alabel="X"], ()) == ("T": aint());

@expected{invalidMatch}
test bool nomatchPar() = matchRascalTypeParams(alist(aparameter("T", avalue())), aset(aint()), ()) == ();