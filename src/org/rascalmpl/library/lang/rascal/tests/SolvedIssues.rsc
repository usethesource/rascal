@bootstrapParser
@doc{
In this test module we collect test cases that are associated with bugs from the past.
This is just to make sure the bugs are not re-introduced accidentally.
}
module lang::rascal::tests::SolvedIssues

import lang::rascal::syntax::RascalRascal;
import ParseTree;

public bool notAmb(Tree t) = /amb(_) !:= t;

public test bool amb1() = notAmb(parse(#Expression, "1 + -1"));

public test bool amb2() = notAmb(parse(#Command,"\"1\" + 2"));