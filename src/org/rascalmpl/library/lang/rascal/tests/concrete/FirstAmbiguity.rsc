module lang::rascal::tests::concrete::FirstAmbiguity

syntax P = E;
syntax E = "e" | E "+" E;

import ParseTree;

@issue{1868}
test bool firstAmbDoesNotThrowStaticErrors() {
    return amb({E _,E _}) := firstAmbiguity(#P, "e+e+e");
}