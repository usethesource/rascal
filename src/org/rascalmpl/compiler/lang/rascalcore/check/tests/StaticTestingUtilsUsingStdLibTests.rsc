@bootstrapParser
module lang::rascalcore::check::tests::StaticTestingUtilsUsingStdLibTests

import lang::rascalcore::check::tests::StaticTestingUtils;

// Sanity check on the testing utilities themselves

test bool Utils1() = checkModuleOK("
    module Utils10
        impor util::Math;
        int main() = max(3, 4);
    ");

test bool Utils2() = checkModuleOK("
    module Utils2
        import List;
        int main() = size([1,2,3]);
    ");