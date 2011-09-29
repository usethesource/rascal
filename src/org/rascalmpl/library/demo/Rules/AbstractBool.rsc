@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
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

public test bool t1() =  bor(band(btrue(),btrue()),band(btrue(), bfalse())) ==  btrue();
public test bool t2() =  btrue() == btrue();
public test bool t3() =  bfalse() == bfalse();
public test bool t4() =  btrue() != bfalse();
public test bool t5() =  band(btrue(),bfalse()) == bfalse();
public test bool t6() =  band(band(btrue(),btrue()),band(btrue(), bfalse())) == bfalse();
public test bool t7() =  bor(btrue(),bfalse()) == btrue();
public test bool t8() =  bor(bor(btrue(),btrue()),bor(btrue(), bfalse())) == btrue();
public test bool t9() =  bor(bor(bfalse(),bfalse()),bor(bfalse(), bfalse())) == bfalse();
public test bool t10() =  bor(band(btrue(),btrue()),band(btrue(), bfalse())) == btrue();
public test bool t11() =  band(bor(btrue(),btrue()),band(btrue(), bfalse())) == bfalse();
