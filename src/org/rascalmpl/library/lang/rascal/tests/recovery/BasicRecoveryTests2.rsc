module lang::rascal::tests::recovery::BasicRecoveryTests2

import ParseTree;
import IO;

layout Layout = [\ ]* !>> [\ ];

syntax S = T;

syntax T = ABC End;
syntax ABC = "a" "b" "c";
syntax End = "$";

test bool ok() {
    return !hasErrors(parse(#S, "a b c $", allowRecovery=true));
}

test bool abx() {
    Tree t = parse(#S, "a b x $", allowRecovery=true);
    return getErrorText(findFirstError(t)) == "x";    
}

test bool axc() {
    Tree t = parse(#S, "a x c $", allowRecovery=true);
    iprintln(t);
    return getErrorText(findFirstError(t)) == "x c";    
}

test bool ax() {
    Tree t = parse(#S, "a x $", allowRecovery=true);
    return getErrorText(findFirstError(t)) == "x";    
}

/*
test bool missingEnd() {
    Tree t = parse(#S, "a b c", allowRecovery=true);
    println("Error text: <getErrorText(findFirstError(t))>");
    return hasErrors(t) && size(findAllErrors(t)) == 1;
}
*/
