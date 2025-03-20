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
module lang::rascalcore::check::tests::ImportTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool ImportError() = missingModuleInModule("
	module ImportError
		import zap;
	");

test bool UndefinedPrivateVar1(){
	writeModule("module M private int m = 3;");
	return undeclaredVariableInModule("
		module UndefinedPrivateVar1
			import M;
			value main() = m;
		");
}

test bool UndefinedPrivateVar2(){
	writeModule("module M private int m = 3;");
	return undeclaredVariableInModule("
		module UndefinedPrivateVar2
			import M;
			int n = m;
		");
}

test bool UndefinedPrivateFunction(){
	writeModule("module M private int f() {return 3;}");
	return undeclaredVariableInModule("
		module UndefinedPrivateFunction
			import M;
			value main() = f();
		");
}