module lang::rascal::tests::concrete::Syntax8

layout Layout = " "*;

syntax AB = "a" | "b";
syntax ABPlus = AB+ abs;
syntax ABStar = AB* abs;
syntax ABPlusSep = {AB ","}+ abs;
syntax ABStarSep = {AB ","}* abs;

test bool cntABPStar0() { n = 0; for(_ <- ([ABStar]"").abs) n += 1; return n == 0; }
test bool cntABPStar3() { n = 0; for(_ <- ([ABStar]"a b a").abs) n += 1; return n == 3; }

test bool cntABPlus1() { n = 0; for(_ <- ([ABPlus]"a").abs) n += 1; return n == 1; }
test bool cntABPlus3() { n = 0; for(_ <- ([ABPlus]"a b a").abs) n += 1; return n == 3; }

test bool cntABStarSep0() { n = 0; for(_ <- ([ABStarSep]"").abs) n += 1; return n == 0; }
test bool cntABStarSep3() { n = 0; for(_ <- ([ABStarSep]"a, b, a").abs) n += 1; return n == 3; }

test bool cntABPlusSep1() { n = 0; for(_ <- ([ABPlusSep]"a").abs) n += 1; return n == 1; }
test bool cntABPlusSep3() { n = 0; for(_ <- ([ABPlusSep]"a, b, a").abs) n += 1; return n == 3; }

test bool all1() = all(x <- ([ABStar]"a a a").abs, "a" := "<x>");
test bool all2() = !all(x <- ([ABStar]"a b a").abs, "a" := "<x>");
test bool all3() = all(x <- ([ABStarSep]"a, a, a").abs, "a" := "<x>");
test bool all4() = !all(x <- ([ABStarSep]"a, b, a").abs, "a" := "<x>");

test bool any1() = any(x <- ([ABStar]"a b a").abs, "b" := "<x>");
test bool any2() = !any(x <- ([ABStar]"a a a").abs, "b" := "<x>");
test bool any3() = any(x <- ([ABStarSep]"a, a, a").abs, "a" := "<x>");
test bool any4() = !any(x <- ([ABStarSep]"a, a, a").abs, "b" := "<x>");

int size(&E* l) = (0 | it + 1 | _ <- l);

@ignoreInterpreter{Not implemented}
test bool sizeABStar0() = size(([ABStar]"").abs) == 0;
@ignoreInterpreter{Not implemented}
test bool sizeABStar1() = size(([ABStar]"a").abs) == 1;
@ignoreInterpreter{Not implemented}
test bool sizeABStar2() = size(([ABStar]"a a").abs) == 2;

@ignoreInterpreter{Not implemented}
test bool sizeABPlus1() = size(([ABPlus]"a").abs) == 1;
@ignoreInterpreter{Not implemented}
test bool sizeABPlus2() = size(([ABPlus]"a b").abs) == 2;
@ignoreInterpreter{Not implemented}
test bool sizeABPlus3() = size(([ABPlus]"a b a").abs) == 3;

int size({&E &S}* l) = (0 | it + 1 | _ <- l);

@ignoreInterpreter{Not implemented}
test bool sizeABStarSep0() = size(([ABStarSep]"").abs) == 0;
@ignoreInterpreter{Not implemented}
test bool sizeABStarSep1() = size(([ABStarSep]"a").abs) == 1;
@ignoreInterpreter{Not implemented}
test bool sizeABStarSep2() = size(([ABStarSep]"a, b").abs) == 2;

@ignoreInterpreter{Not implemented}
test bool sizeABPlusSep1() = size(([ABPlusSep]"a").abs) == 1;
@ignoreInterpreter{Not implemented}
test bool sizeABPlusSep2() = size(([ABPlusSep]"a, b").abs) == 2;
@ignoreInterpreter{Not implemented}
test bool sizeABPlusSep3() = size(([ABPlusSep]"a, b, a").abs) == 3;