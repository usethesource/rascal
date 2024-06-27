module lang::rascal::tests::recovery::BasicRecoveryTests


import ParseTree;
import IO;

layout Layout = [\ ]* !>> [\ ];

syntax S = A B C;
syntax A = "a";
syntax B = "b" "b";
syntax C = "c";

test bool parseTest() {
    Tree t = parse(#S, "a b x c", allowRecovery=true);
    iprintln(t);
    return true;
}
