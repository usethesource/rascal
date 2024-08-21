module lang::rascal::tests::recovery::BasicRecoveryTests

import ParseTree;
import IO;

layout Layout = [\ ]* !>> [\ ];

syntax S = T;

syntax T = ABC End;
syntax ABC = 'a' 'b' 'c';
syntax End = "$";

private Tree parseS(str input, bool visualize=false) 
    = parser(#S, allowRecovery=true, allowAmbiguity=true)(input, |unknown:///?visualize=<"<visualize>">|);

test bool basicOk() {
    return !hasErrors(parseS("a b c $", visualize=true));
}

test bool abx() {
    Tree t = parseS("a b x $", visualize=true);
    return getErrorText(findFirstError(t)) == "x";    
}

test bool axc() {
    Tree t = parseS("a x c $", visualize=true);
    iprintln(getSkipped(findFirstError(t)).src);
    return getErrorText(findFirstError(t)) == "x c";    
}

/* We need to look through non-terminals to make this test work
test bool ax() {
    Tree t = parseS("a x $", visualize=true);
    return getErrorText(findFirstError(t)) = "x";    
}
*/
