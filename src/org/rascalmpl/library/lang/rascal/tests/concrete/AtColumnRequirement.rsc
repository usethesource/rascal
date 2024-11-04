module lang::rascal::tests::concrete::AtColumnRequirement

import ParseTree;
import Exception;

lexical C = C C C | "a"@2 | "b";

test bool testA() {
    try {
        parse(#C, "a");
    } catch ParseError(e): {
        return true;
    }

    return false;
}

test bool testBab() {
    try {
        parse(#C, "bab");
    } catch ParseError(e): {
        return true;
    }

    return false;
}

test bool testBba() {
    try {
        parse(#C, "bba");
    } catch ParseError(e): {
        return false;
    }

    return true;
}