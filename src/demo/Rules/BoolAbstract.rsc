module demo::Rules::BoolAbstract

import UnitTest;

// An atypical Rascal example that reminds us of algebraic specifications.
// We define the data type Bool with constants btrue and bfalse and constructors
// band and bor.

// Rewrite rules are used to simplify band and bor terms.

data Bool = btrue();
data Bool = bfalse();
data Bool = band(Bool left, Bool right);
data Bool = bor(Bool left, Bool right);  

rule a1 band(btrue(), Bool B)    => B;
rule a2 band(bfalse(), Bool B)   => bfalse();

rule o1 bor(btrue(), btrue())     => btrue();
rule o2 bor(btrue(), bfalse())    => btrue();
rule o3 bor(bfalse(), btrue())    => btrue();
rule o4 bor(bfalse(), bfalse())   => bfalse();

public bool test()
{
		assertEqual(bor(band(btrue(),btrue()),band(btrue(), bfalse())), btrue());
		assertEqual(btrue(), btrue());
		assertEqual(bfalse(), bfalse());
		assertEqual(btrue() != bfalse());
		assertEqual(band(btrue(),bfalse()), bfalse());	
		assertEqual(band(band(btrue(),btrue()),band(btrue(), bfalse())), bfalse());
		assertEqual(bor(btrue(),bfalse()), btrue());
		assertEqual(bor(bor(btrue(),btrue()),bor(btrue(), bfalse())), btrue());
		assertEqual(bor(bor(bfalse(),bfalse()),bor(bfalse(), bfalse())), bfalse());
		assertEqual(bor(band(btrue(),btrue()),band(btrue(), bfalse())), btrue());
		assertEqual(band(bor(btrue(),btrue()),band(btrue(), bfalse())), bfalse());
		return report();
}