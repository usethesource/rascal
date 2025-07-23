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

test bool FunctionNotVisibleViaIndirectImport(){
	writeModule("module A int twice(int n) = 2 * n;");
	writeModule("module B import A;");
	return undeclaredTypeInModule("
		module FunctionNotVisibleViaIndirectImport
			import B;
			int t = twice(3);
		");
}

test bool FunctionVisibleViaExtend(){
	writeModule("module A int twice(int n) = 2 * n;");
	writeModule("module B extend A;");
	return checkModuleOK("
		module FunctionVisibleViaExtend
			import B;
			int t = twice(3);
		");
}

test bool ADTNotVisibleViaIndirectImport(){
	writeModule("module A data D = d1();");
	writeModule("module B import A;");
	return undeclaredTypeInModule("
		module ADTNotVisibleViaIndirectImport
			import B;
			D x = d1();
		");
}

test bool ADTVisibleViaExtend(){
	writeModule("module A data D = d1();");
	writeModule("module B extend A;");
	return checkModuleOK("
		module ADTVisibleViaExtend
			import B;
			D x = d1();
		");
}

test bool SyntaxNotVisibleViaIndirectImport(){
	writeModule("module A syntax A = \"a\";");
	writeModule("module B import A;");
	return undeclaredTypeInModule("
		module SyntaxNotVisibleViaIndirectImport
			import B;
			A x = [A] \"a\";
		");
}

test bool SyntaxVisibleViaExtend(){
	writeModule("module A syntax A = \"a\";");
	writeModule("module B extend A;");
	return checkModuleOK("
		module SyntaxVisibleViaExtend
			import B;
			A x = [A] \"a\";
		");
}

str  MBottom =
	"module Bottom
    'data Exp;
	'data Bool = \true() | \false();";

str MLeft =
	"module Left
	'extend Bottom;
	'data Exp = or(Exp lhs, Exp rhs)| maybe() | \true() | \false();";

str MRight = 
	"module Right
	'extend Bottom;
	'data Exp = and(Bool lhs, Bool rhs);
	'data Exp2 = or(Exp lhs, Exp rhs);";

test bool ImportsWithConflictingConstructorsAllowed(){
	writeModule(MBottom); writeModule(MLeft); writeModule(MRight);
	return checkModuleOK("
		module ImportsWithConflictingConstructorsAllowed
			import Left; import Right;
		");
}

test bool TrueIsOverloaded(){
	writeModule(MBottom); writeModule(MLeft); writeModule(MRight);
	return unexpectedTypeInModule("
		module OverloadedFieldOk
			import Left; import Right;
			Exp main(){
				return \true();
			}
		");
}

test bool TrueIsOverloadedButResolved(){
	writeModule(MBottom); writeModule(MLeft); writeModule(MRight);
	return checkModuleOK("
		module OverloadedFieldOk
			import Left; import Right;
			Exp main(){
				return Left::\true();
			}
		");
}

test bool OverloadedFieldOk(){
	writeModule(MBottom); writeModule(MLeft); writeModule(MRight);
	return checkModuleOK("
		module OverloadedFieldOk
			import Left; import Right;
			Exp main(){
				x = and(Bool::\true(), Bool::\true());
				return x.lhs;
			}
		");
}

test bool OverloadedOrNotOk(){
	writeModule(MBottom); writeModule(MLeft); writeModule(MRight);
	return unexpectedTypeInModule("
		module OverloadedOrNotOk
			import Left; import Right;
			Exp main(){
				return or(maybe(), maybe());
			}
		");
}

test bool OrIsOverloadedButResolved(){
	writeModule(MBottom); writeModule(MLeft); writeModule(MRight);
	return checkModuleOK("
		module OrIsOverloadedButResolved
			import Left; import Right;
			Exp main(){
				return Exp::or(maybe(), maybe());
			}
		");
}

test bool FieldLhsIsOverloaded1(){
	writeModule(MBottom); writeModule(MLeft); writeModule(MRight);
	return unexpectedTypeInModule("
		module FieldLhsIsOverloaded1
			import Left; import Right;
			Exp main(){
				x = Exp::or(maybe(), maybe());
				Exp y = x.lhs;
				return y;
			}
		");
}

test bool FieldLhsIsOverloaded2(){
	writeModule(MBottom); writeModule(MLeft); writeModule(MRight);
	return unexpectedTypeInModule("
		module FieldLhsIsOverloaded2
			import Left; import Right;
			Exp main(){
				x = Exp::or(maybe(), maybe());
				return or(x.lhs, x.lhs);
			}
		");
}

test bool OverloadedModuleVarNotOk(){
	writeModule("module Left public str global = \"World\";");
	writeModule("module Right public str global = \"Hello\";");
	return redeclaredVariableInModule("
		module OverloadedModuleVarNotOk
			import Left; import Right;
			str main() = global;
		");
}

test bool QualifiedOverloadedModuleVarOk(){
	writeModule("module Left public str global = \"World\";");
	writeModule("module Right public str global = \"Hello\";");
	return checkModuleOK("
		module OverloadedModuleVarNotOk
			import Left; import Right;
			str main() = Left::global;
		");
}