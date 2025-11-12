module lang::rascal::tests::extends4::C

import lang::rascal::tests::extends4::B

// issue 2513
test bool privateExtendedGlobalShouldNotHidePublicConstructor() {
    return X _ := glob();
}
