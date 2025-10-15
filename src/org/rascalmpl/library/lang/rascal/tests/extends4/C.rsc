module lang::rascal::tests::extends4::C

import lang::rascal::tests::extends4::B;
import IO;

// issue 2513
test bool privateExtendedGlobalShouldNotHidePublicConstructor() {
    println(glob);
    return X _ := glob();
}
