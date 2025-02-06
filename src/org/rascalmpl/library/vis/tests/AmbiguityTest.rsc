module vis::tests::AmbiguityTest

import vis::Dot;
import ParseTree;
import Content;

syntax Ambiguous = A? | B?;
syntax A = "a";
syntax B = "a";

Content showAmb() {
    Tree t = parse(#Ambiguous, "a", allowAmbiguity=true);
    return content("Ambiguity Test", dotServer(valueToDot(t, name="ambtest")));
}
