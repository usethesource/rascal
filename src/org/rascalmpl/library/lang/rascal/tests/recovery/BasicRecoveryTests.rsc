module lang::rascal::tests::recovery::BasicRecoveryTests


import ParseTree;
import IO;

layout Layout = [\ ]* !>> [\ ];

syntax S = A End;
syntax A = "1" "2" "3";
syntax End = "$";

test bool parseOk() {
    return !hasErrors(parse(#S, "1 2 3 $", allowRecovery=true));
}

test bool simpleRecovery() {
    Tree t = parse(#S, "1 2 x $", allowRecovery=true);
    return hasErrors(t) && size(findAllErrors(t)) == 1;
}
