@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module demo::Rules::ConcreteBoolVisit

import demo::Rules::BoolSyntax;

Bool reduce(Bool B) {
    Bool B2;
    return innermost visit(B) {
      case `btrue & <B2>`   => B2
      case `bfalse & <B3>`  => `bfalse`

      case `btrue | btrue`   => `btrue`
      case `btrue | bfalse`  => `btrue`
      case `bfalse | btrue`  => `btrue`
      case `bfalse | bfalse` => `bfalse`
    };
}

public test bool t1() = reduce(`btrue`) == `btrue`;
public test bool t2() = reduce(`btrue | btrue`) == `btrue`;
public test bool t3() = reduce(`bfalse | btrue`) ==  `btrue`;
public test bool t4() = reduce(`bfalse & bfalse`) == `bfalse`;
