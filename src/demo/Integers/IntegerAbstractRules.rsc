module demo::Integers::IntegerAbstractRules

import demo::Booleans::BoolAbstractRules;

data Integer = z | s(Integer arg);
/*
data Integer = add(Integer L,Integer R);
rule a1 add(z, Integer N)              => N;
rule a2 add(s(Integer N), Integer M)   => s(add(N, M));

data Integer = mul(Integer L, Integer R);
rule m1 mul(z, Integer N)              => z;
rule m2 mul(s(Integer N), Integer M)   => add(M, mul(N, M));

data Integer = exp(Integer L, Integer R);
rule m1 exp(Integer N, z)              => s(z);
rule m2 exp(Integer N, s(Integer M))   => mul(N, exp(N, M));
*/
data Bool = eq(Integer L, Integer R);
rule e1 eq(z,z)                        => btrue;
rule e2 eq(s(Integer N),z)             => bfalse;
rule e2 eq(z,s(Integer N))             => bfalse;
rule e3 eq(s(Integer N), s(Integer M)) => eq(N,M);

public bool testInt(){
		  
//		assert add(s(s(z)), s(s(s(z)))) == s(s(s(s(s(z)))));
//		assert mul(s(s(z)), s(s(s(z)))) == s(s(s(s(s(s(z))))));
//		assert exp(s(s(z)), s(s(s(z)))) == s(s(s(s(s(s(s(s(z))))))));
		assert eq(s(s(z)),  s(s(s(z)))) == bfalse;
//		assert eq(s(s(s(z))), s(s(s(z)))) == btrue;
		return true;
}