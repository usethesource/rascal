@doc{
This module documents known issues in Rascal that still need solving.
Perhaps they are hard to solve, but one day the inversion of these tests will
end up in SolvedIssues
}
@bootstrapParser
module lang::rascal::tests::KnownIssues

import lang::rascal::syntax::RascalRascal;
import ParseTree;

public bool isAmb(Tree t) = /amb(_) := t;

public test bool literalAmb() = isAmb(parse(#Command,"\"a\"+ b;"));
