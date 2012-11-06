@bootstrapParser
@doc{
In this test module we collect test cases that are associated with bugs from the past.
This is just to make sure the bugs are not re-introduced accidentally.
}
module lang::rascal::tests::SolvedIssues

public test bool emptySetEquals(set[value] x, set[value] y) = x - x == y - y;

public test bool emptySetEquals(map[value,value] x, map[value,value] y) = x - x == y - y;