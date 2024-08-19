module lang::rascal::tests::recovery::ListRecoveryTests

import ParseTree;
import IO;

layout Layout = [\ ]* !>> [\ ];

syntax S = T End;

syntax T = { AB "," }*;
syntax AB = "a" "b";
syntax End = "$";

Tree parseList(str s, bool visualize=false) {
    return parser(#S, allowRecovery=true, allowAmbiguity=true)(s, |unknown:///?visualize=<"<visualize>">|);
}

test bool listOk() {
    return !hasErrors(parseList("a b , a b , a b $", visualize=true));
}

test bool listTypo() {
    Tree t = parseList("a b, a x, ab $", visualize=true);
    iprintln(t);
    return hasErrors(t);
}

test bool listTypoWs() {
    Tree t = parseList("a b , a x , a b $", visualize=true);
    iprintln(t);
    return hasErrors(t);
}
