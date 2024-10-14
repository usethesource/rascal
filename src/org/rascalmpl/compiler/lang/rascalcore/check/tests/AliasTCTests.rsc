@bootstrapParser
module lang::rascalcore::check::tests::AliasTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool DoubleDeclarationError() = unexpectedDeclarationInModule("
    module DoubleDeclarationError
        alias A = str;
        alias A = int;
        A x = 0;
    ");

test bool CircularDeclarationError() = unexpectedDeclarationInModule("
    module CircularDeclarationError
        alias A = A;
        A x = 0;
    ");

test bool UndeclaredTypeError() = undeclaredTypeInModule("
    module UndeclaredTypeError
        alias A = B;
        A x = 0;
    ");

test bool IncompatibleInitError() = unexpectedDeclarationInModule("
    module IncompatibleInitError
        alias B = str;
        alias A = B;            
        A x = 0;
    ");

test bool CircularAliasError1() = unexpectedDeclarationInModule("
    module CircularAliasError1
        alias INTEGER0 = INTEGER1;
        alias INTEGER1 = INTEGER2;
        alias INTEGER2 = INTEGER0;
        INTEGER0 x = 0;
    ");

test bool CircularAliasError2() = unexpectedDeclarationInModule("
    module CircularAliasError2
        alias INTEGER0 = INTEGER1;
        alias INTEGER1 = INTEGER2;
        alias INTEGER2 = INTEGER0;
        INTEGER1 x = 0;
    ");

test bool CircularAliasError3() = unexpectedDeclarationInModule("
    module CircularAliasError3
        alias INTEGER0 = INTEGER1;
        alias INTEGER1 = INTEGER2;
        alias INTEGER2 = INTEGER0;
        INTEGER2 x = 0;
    ");

test bool CircularAliasError4() = unexpectedDeclarationInModule("
    module CircularAliasError4
        alias INTEGER0 = INTEGER1;
        alias INTEGER1 = INTEGER0;  

        value main() {
            INTEGER0 x = 0; 
            return x == 0;
        }
    ");
