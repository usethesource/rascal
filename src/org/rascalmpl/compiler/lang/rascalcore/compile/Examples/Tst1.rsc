module lang::rascalcore::compile::Examples::Tst1

data TYPESET = SET(str name) | SUBTYPES(TYPESET tset) | INTERSECT(set[TYPESET] tsets);
TYPESET simp(TYPESET  ts) = ts;
bool testSimp(TYPESET ats, TYPESET (TYPESET  ts) aSimp) = ats == aSimp(ats);

value main() = testSimp(SET("a"), simp);