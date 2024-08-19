module lang::rascal::tests::recovery::NestedRecoveryTests

import ParseTree;
import IO;

layout Layout = [\ ]* !>> [\ ];

syntax S = T;

syntax T = A B "c";

syntax A = "a";
syntax B = "b" "b";
//syntax C = "c";

private Tree parseS(str input, bool visualize=false) 
    = parser(#S, allowRecovery=true, allowAmbiguity=true)(input, |unknown:///?visualize=<"<visualize>">|);

test bool nestedOk() {
    return !hasErrors(parseS("a b b c", visualize=true));
}

test bool nestedTypo() {
    Tree t = parseS("a b x c", visualize=true);
    iprintln(t);
    println("Error text: \'<getErrorText(findFirstError(t))>\'");
    return getErrorText(findFirstError(t)) == "x ";    
}
