module lang::dot::tests::DotTests

import lang::dot::\syntax::Dot;

import ParseTree;

test bool parseExample1() {
    parse(#DOT, |std:///lang/dot/examples/example1.dot|);
    parse(#DOT, |std:///lang/dot/examples/example2.dot|);
    return true;
}