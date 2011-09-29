@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module demo::Rules::AbstractInteger

// We continue our exploration of algebraic specification by
// first importing the Bool data type and then introducing
// zero/successor integer with various arithmetic operations

import demo::Rules::AbstractBool;

// Define zero/successor integers, examples are:
// z() corresponds to 0
// s(z()) corresponds to 1
// s(s(z())) corresponds to 2, and so on

data Integer = z() | s(Integer arg);

// Define addition

data Integer = add(Integer L,Integer R);
rule a1 add(z(), Integer N)              => N;
rule a2 add(s(Integer N), Integer M)     => s(add(N, M));

// Define multiplication

data Integer = mul(Integer L, Integer R);
rule m1 mul(z(), Integer N)              => z();
rule m2 mul(s(Integer N), Integer M)     => add(M, mul(N, M));

// Define exponentiation

data Integer = exp(Integer L, Integer R);
rule m1 exp(Integer N, z())              => s(z());
rule m2 exp(Integer N, s(Integer M))     => mul(N, exp(N, M));

// Define equality

data Bool = eq(Integer L, Integer R);
rule e1 eq(z(),z())                      => btrue();
rule e2 eq(s(Integer N),z())             => bfalse();
rule e2 eq(z(),s(Integer N))             => bfalse();
rule e3 eq(s(Integer N), s(Integer M))   => eq(N,M);

// Tests
		  
public test bool t1() = add(s(s(z())), s(s(s(z())))) == s(s(s(s(s(z())))));
public test bool t2() = mul(s(s(z())), s(s(s(z())))) == s(s(s(s(s(s(z()))))));
public test bool t3() = exp(s(s(z())), s(s(s(z())))) == s(s(s(s(s(s(s(s(z()))))))));
public test bool t4() = eq(s(s(z())),  s(s(s(z())))) == bfalse();
public test bool t5() = eq(s(s(s(z()))), s(s(s(z())))) == btrue();
