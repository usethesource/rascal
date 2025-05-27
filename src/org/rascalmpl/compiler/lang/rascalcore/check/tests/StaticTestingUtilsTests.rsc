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
module lang::rascalcore::check::tests::StaticTestingUtilsTests

import lang::rascalcore::check::tests::StaticTestingUtils;

// Sanity check on the testing utilities themselves

test bool TestUtils01() = checkOK("13;");

test bool TestUtils02() = checkModuleOK("
	module TestUtils02
		int main(){
			int x = 5;
			return x;
		}
	");

test bool TestUtils03() = checkModuleOK("
	module TestUtils03
		data D = d();
		D main() = d();
	");

test bool TestUtils04() = checkModuleOK("
	module TestUtils04
		data D = d() | d(int n);
		D main() = d();
	");

test bool TestUtils05() = checkModuleOK("
	module TestUtils05
		data D = d() | d(int n);
		D main() = d(3);
	");

test bool TestUtils06() = checkModuleOK("
	module TestUtils06
		data Bool = and(Bool, Bool) | t();
		Bool main() = t();
	");
	
test bool TestUtils07() = checkModuleOK("
	module TestUtils07
		data Bool = and(Bool, Bool) | t();
		Bool main() = and(t(),t());
	");

test bool TestUtils08() = checkModuleOK("
	module TestUtils08
		data Bool = and(Bool, Bool) | t();
		data Prop = or(Prop, Prop) | f();
		Prop main() {
			and(t(),t());
			return f();
		}
	");

test bool TestUtils09() = checkModuleOK("
	module TestUtils09
		data NODE = f(int a, str b, real c);
		NODE N = f(0, \"a\", 3.5);
	");

test bool TestUtils10(){
	writeModule("module MMM int x = 3;"); 
	return checkModuleOK("
		module TestUtils13
			import MMM;
			int main() = 13;
	");
}

test bool TestUtils11(){
	writeModule("module MMM int x = 3;"); 
	return undeclaredVariableInModule("
		module TestUtils14
			import MMM;
			int main() = x;
	");
}	

test bool TestUtils12() = checkModuleOK("
    module TestUtils12
        import List;
        int main() = size([1,2,3]);
    ");

