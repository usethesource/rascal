module lang::rascal::tests::concrete::AtColumnRequirement

import ParseTree;
import Exception;

lexical C = C C C | "a"@2 | "b";

bool testParse(str input, bool shouldParse) {
    try {
        parse(#C, input);
    } catch ParseError(_): {
        return !shouldParse;
    }

    return shouldParse;
}

test bool testA() = testParse("a", false);

test bool testBab() = testParse("bab", false);

test bool testBba() = testParse("bba", true);
