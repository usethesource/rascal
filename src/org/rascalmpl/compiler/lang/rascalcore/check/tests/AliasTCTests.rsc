@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
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

test bool Issue504() = redeclaredVariableInModule("
    module Issue504
	    alias INT = int;
        alias INT = int;
    ");

