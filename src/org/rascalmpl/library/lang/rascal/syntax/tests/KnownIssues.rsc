@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@doc{
This module documents known issues in Rascal that still need solving.
Perhaps they are hard to solve, but one day the inversion of these tests will
end up in SolvedIssues
}
@bootstrapParser
module lang::rascal::\syntax::tests::KnownIssues

import lang::rascal::\syntax::Rascal;
import ParseTree;


public bool isAmb(Tree t) = /amb(_) := t;

public test bool literalAmb() = isAmb(parse(#Command,"\"a\"+ b;", allowAmbiguity=true));

public test bool basicAmb() = amb(_) := amb({});
