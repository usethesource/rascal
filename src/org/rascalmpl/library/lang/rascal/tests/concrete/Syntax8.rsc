module lang::rascal::tests::concrete::Syntax8

layout Layout = " "*;

syntax AB = "a" | "b";
syntax ABPlus = AB+ abs;
syntax ABStar = AB* abs;
syntax ABPlusSep = {AB ","}+ abs;
syntax ABStarSep = {AB ","}* abs;

test bool cntABPStar0() { n = 0; for(x <- ([ABStar]"").abs) n += 1; return n == 0; }
test bool cntABPStar3() { n = 0; for(x <- ([ABStar]"a b a").abs) n += 1; return n == 3; }

test bool cntABPlus1() { n = 0; for(x <- ([ABPlus]"a").abs) n += 1; return n == 1; }
test bool cntABPlus3() { n = 0; for(x <- ([ABPlus]"a b a").abs) n += 1; return n == 3; }

test bool cntABStarSep0() { n = 0; for(x <- ([ABStarSep]"").abs) n += 1; return n == 0; }
test bool cntABStarSep3() { n = 0; for(x <- ([ABStarSep]"a, b, a").abs) n += 1; return n == 3; }

test bool cntABPlusSep1() { n = 0; for(x <- ([ABPlusSep]"a").abs) n += 1; return n == 1; }
test bool cntABPlusSep3() { n = 0; for(x <- ([ABPlusSep]"a, b, a").abs) n += 1; return n == 3; }

test bool all1() = all(x <- ([ABStar]"a a a").abs, "a" := "<x>");
test bool all2() = !all(x <- ([ABStar]"a b a").abs, "a" := "<x>");
test bool all3() = all(x <- ([ABStarSep]"a, a, a").abs, "a" := "<x>");
test bool all4() = !all(x <- ([ABStarSep]"a, b, a").abs, "a" := "<x>");

test bool any1() = any(x <- ([ABStar]"a b a").abs, "b" := "<x>");
test bool any2() = !any(x <- ([ABStar]"a a a").abs, "b" := "<x>");
test bool any3() = any(x <- ([ABStarSep]"a, a, a").abs, "a" := "<x>");
test bool any4() = !any(x <- ([ABStarSep]"a, a, a").abs, "b" := "<x>");