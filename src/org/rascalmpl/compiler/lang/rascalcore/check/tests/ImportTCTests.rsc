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

// Diamond example suggested by Jurgen Vinju

str  MBottom =	"module Bottom
    			'data Exp;
				'data Bool = \true() | \false();";

str MLeft =		"module Left
				'extend Bottom;
				'data Exp = or(Exp lhs, Exp rhs)| maybe() | \true() | \false();";

str MRight = 	"module Right
				'extend Bottom;
				'data Exp = and(Bool lhs, Bool rhs);
				'data Exp2 = or(Exp lhs, Exp rhs);";

test bool ImportsWithConflictingConstructorsAllowed(){
	writeModule(MBottom); writeModule(MLeft); writeModule(MRight);
	return checkModuleOK("
		module ImportsWithConflictingConstructorsAllowed
			import Left; import Right;	// ok: Both imports declare constructor `or`
		");
}

test bool OverloadedTrueIsOk(){
	writeModule(MBottom); writeModule(MLeft); writeModule(MRight);
	return checkModuleOK("
		module OverloadedTrueIsOk
			import Left; import Right;
			Exp main(){
				return \true(); // ok: resolved to Exp::\true() by declared return type Exp of main
			}
		");
}

test bool OverloadedTrueIsResolved(){
	writeModule(MBottom); writeModule(MLeft); writeModule(MRight);
	return checkModuleOK("
		module OverloadedTrueIsResolved
			import Left; import Right;
			Exp main(){
				return Left::\true(); // ok: explicit disambiguation is not needed here
			}
		");
}

test bool OverloadedTrueIsNotResolved(){
	writeModule(MBottom); writeModule(MLeft); writeModule(MRight);
	return unexpectedTypeInModule("
		module OverloadedTrueIsNotResolved
			import Left; import Right;
			value main(){
				return \true(); // not ok: explicit disambiguation is needed here due to value return type of main
			}
		");
}

test bool OverloadedTrueIsResolvedIncorrectly(){
	writeModule(MBottom); writeModule(MLeft); writeModule(MRight);
	return unexpectedTypeInModule("
		module OverloadedTrueIsResolvedIncorrectly
			import Left; import Right;
			Exp main(){
				return Bool::\true(); // not ok: wrong explicit disambiguation
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
				return x.lhs; // ok: x has already been resolved to Bool in previous statement
			}
		");
}

test bool OverloadedOrOk(){
	writeModule(MBottom); writeModule(MLeft); writeModule(MRight);
	return checkModuleOK("
		module OverloadedOrNotOk
			import Left; import Right;
			Exp main(){
				return or(maybe(), maybe()); // ok: or is resolved via return type Exp of main
			}
		");
}

test bool OverloadedOrIsResolvedOk(){
	writeModule(MBottom); writeModule(MLeft); writeModule(MRight);
	return checkModuleOK("
		module OverloadedOrIsResolvedOk
			import Left; import Right;
			Exp main(){
				return Exp::or(maybe(), maybe()); // ok: redundant disambiguation of or
			}
		");
}

test bool OverloadedFieldLhsOk1(){
	writeModule(MBottom); writeModule(MLeft); writeModule(MRight);
	return checkModuleOK("
		module OverloadedFieldLhsOk1
			import Left; import Right;
			Exp main(){
				Exp2 x = or(maybe(), maybe());	// ok: resolved to Exp::or thanks to declared type Exp of x
				Exp y = x.lhs;					// ok: lhs is overloaded, but resolved by inferred type Exp of x
				return y;
			}
		");
}

test bool OverloadedFieldLhsNotOk1(){
	writeModule(MBottom); writeModule(MLeft); writeModule(MRight);
	return unexpectedTypeInModule("
		module OverloadedFieldLhsNotOk1
			import Left; import Right;
			Exp main(){
				x = or(maybe(), maybe());	// or cannot be resolved
				y = x.lhs;					// not ok: x is overloaded, no field selection possible
				return y;
			}
		");
}

test bool OverloadedFieldLhsNotOk2(){
	writeModule(MBottom); writeModule(MLeft); writeModule(MRight);
	return unexpectedTypeInModule("
		module OverloadedFieldLhsNotOk2
			import Left; import Right;
			Exp main(){
				x = or(maybe(), maybe());	// or cannot be resolved
				return or(x.lhs, x.lhs);	// not ok: x is overloaded, no field selection possible
			}
		");
}

test bool OverloadedModuleVarNotOk(){
	writeModule("module Left public str global = \"World\";");
	writeModule("module Right public str global = \"Hello\";");
	return redeclaredVariableInModule("
		module OverloadedModuleVarNotOk
			import Left; import Right;
			str main() = global;	// not ok: global is ambiguous, disambiguation required
		");
}

test bool QualifiedOverloadedModuleVarOk(){
	writeModule("module Left public str global = \"World\";");
	writeModule("module Right public str global = \"Hello\";");
	return checkModuleOK("
		module OverloadedModuleVarNotOk
			import Left; import Right;
			str main() = Left::global;	// ok: global is explicitly resolved
		");
}

test bool cyclicImportOk(){
	writeModule("module A import B;");
	return checkModuleOK("module B import A;");
}

@ignore{TODO}
test bool cyclicExtendNotOk(){
	writeModule("module A extend B;");
	return unexpectedDeclarationInModule("module B extend A;");
}