@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module demo::Rules::ConcreteBool

import demo::Rules::BoolSyntax;

// An atypical Rascal example that reminds us of algebraic specifications
// with concrete syntax in the style of ASF+SDF.

// We import the syntax for concrete Boolean expression of type Bool with 
// constants btrue and bfalse and operators & and |.

// Also see AbstractBool.rsc for a, abstract version of Booleans.

// Rewrite rules are used to simplify & and | terms.

Bool b = `btrue`;

rule a1 `btrue & <Bool B2>`   => B2;
rule a2 `bfalse & <Bool B2>`  => `bfalse`;

rule o1 `btrue | btrue`       => `btrue`;
rule o2 `btrue | bfalse`      => `btrue`;
rule o3 `bfalse | btrue`      => `btrue`;
rule o4 `bfalse | bfalse`     => `bfalse`;

public test bool t1() = `btrue` == `btrue`;
public test bool t2() = `btrue | btrue` == `btrue`; 
public test bool t3() = `bfalse | btrue` == `btrue`;
public test bool t4() = `bfalse & bfalse` == `bfalse`;
