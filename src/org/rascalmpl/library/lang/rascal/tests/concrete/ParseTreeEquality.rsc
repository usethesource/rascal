module lang::rascal::tests::concrete::ParseTreeEquality

import ParseTree;

bool testCycleEquality() {
    Tree cycle1 = cycle(sort("X"), 3);
    Tree cycle2 = cycle(sort("X"), 3);

    return cycle1 == cycle2;
}
