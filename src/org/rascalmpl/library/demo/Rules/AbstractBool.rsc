module demo::Rules::AbstractBool

// An atypical Rascal example that reminds us of algebraic specifications.
// We define the data type Bool with constants btrue and bfalse and constructors
// band and bor.

// Also see ConcreteBool.rsc for a concrete syntax version

// Rewrite rules are used to simplify band and bor terms.
// Also see AbstractBoolVisit for a similar definition using a visit expression.

data Bool = btrue();
data Bool = bfalse();
data Bool = band(Bool left, Bool right);
data Bool = bor(Bool left, Bool right);  

rule a1 band(btrue(), Bool B)     => B;
rule a2 band(bfalse(), Bool B)    => bfalse();

rule o1 bor(btrue(), btrue())     => btrue();
rule o2 bor(btrue(), bfalse())    => btrue();
rule o3 bor(bfalse(), btrue())    => btrue();
rule o4 bor(bfalse(), bfalse())   => bfalse();

// Tests

test bor(band(btrue(),btrue()),band(btrue(), bfalse())) ==  btrue();
test btrue() == btrue();
test bfalse() == bfalse();
test btrue() != bfalse();
test band(btrue(),bfalse()) == bfalse();
test band(band(btrue(),btrue()),band(btrue(), bfalse())) == bfalse();
test bor(btrue(),bfalse()) == btrue();
test bor(bor(btrue(),btrue()),bor(btrue(), bfalse())) == btrue();
test bor(bor(bfalse(),bfalse()),bor(bfalse(), bfalse())) == bfalse();
test bor(band(btrue(),btrue()),band(btrue(), bfalse())) == btrue();
test band(bor(btrue(),btrue()),band(btrue(), bfalse())) == bfalse();
