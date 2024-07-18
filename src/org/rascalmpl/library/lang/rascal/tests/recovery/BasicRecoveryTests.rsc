module lang::rascal::tests::recovery::BasicRecoveryTests

import ParseTree;

layout Layout = [\ ]; //* !>> [\ ];

syntax S = ABC End;
syntax ABC = "a" "b" "c";
syntax End = "$";

test bool ok() {
    return !hasErrors(parse(#S, "a b c $", allowRecovery=true));
}

test bool abx() {
    Tree t = parse(#S, "a b x $", allowRecovery=true);
    return hasErrors(t) && size(findAllErrors(t)) == 1;
}

test bool axc() {
    Tree t = parse(#S, "a x c $", allowRecovery=true);
    return hasErrors(t) && size(findAllErrors(t)) == 1;
}

test bool ax() {
    Tree t = parse(#S, "a x $", allowRecovery=true);
    return hasErrors(t) && size(findAllErrors(t)) == 1;
}
