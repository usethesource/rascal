@license{
Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
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
module lang::rascalcore::check::tests::DeclarationTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;
 
test bool localTypeInferenceNoEscape2() = undeclaredVariable("{ x = 1; x == 1; } x;");

test bool undeclaredType1() = undeclaredType("X N;");

test bool doubleDeclaration3() = redeclaredVariable("int f(int N){int N = 1; return N;}");

test bool doubleDeclaration4() = redeclaredVariable("void main() {
    'int bar = 8;
    'bool bar() = true;
    'bar();
}");

test bool shadowingDeclaration1() = checkOK("int N = 1; {int N = 2;}; N == 1;");

test bool shadowingDeclaration2() = checkOK("N = 1; {int N = 2; N == 2;}; N == 1;");

test bool shadowingDeclaration3() = checkOK("int N = 3; int N := 3;");

// Variable declaration in imported module

test bool PrivateVarDeclarationNotVisible(){ 
	writeModule("module MMM private int x = 3;"); 
	return undeclaredVariableInModule("
		module PrivateVarDeclarationNotVisible
			import MMM;
			int main() = x;
		");
}

test bool PublicVarDeclarationVisible(){ 
	writeModule("module MMM public int x = 3;"); 
	return checkModuleOK("
		module PublicVarDeclarationVisible
			import MMM;
			int main() = x;
		");
}

test bool PublicVarDeclarationVisibleViaQualifiedName(){ 
	writeModule("module MMM public int x = 3;"); 
	return checkModuleOK("
		module PublicVarDeclarationVisibleViaQualifiedName
			import MMM;
			int main() = MMM::x;
		");
}

test bool DefaultVarDeclarationNotVisible(){ 
	writeModule("module MMM int x = 3;"); 
	return undeclaredVariableInModule("
		module DefaultVarDeclarationNotVisible
			import MMM;
			int main() = x;
		");
}

test bool RedeclaredVarDeclaration(){
	writeModule("module MMM public int x = 3;"); 
	return redeclaredVariableInModule("
		module RedeclaredVarDeclaration
			import MMM;
			int x = 4;
		");
}

test bool ModuleVarReDeclaration(){ 
	writeModule("module MMM
					public int n = 1; 
					public int n = 2;"); 
	return redeclaredVariableInModule("
		module ModuleVarReDeclaration
			import MMM;
			bool main() = n == 1;
		");
}

test bool QualifiedScopeTest(){ 
	writeModule("module MMM public int n = 1;"); 
	return checkModuleOK("
		module QualifiedScopeTest
			import MMM;
			bool main() = MMM::n == 1;
		");
}

// Function declaration in imported module

test bool PrivateFunDeclarationNotVisible(){ 
	writeModule("module MMM private int f() = 3;"); 
	return undeclaredVariableInModule("
		module PrivateFunDeclarationNotVisible
			import MMM;
			int main() = f();
		");
}

test bool PublicFunDeclarationVisible(){ 
	writeModule("module MMM public int f() = 3;"); 
	return checkModuleOK("
		module PublicFunDeclarationVisible
			import MMM;
			int main() = f();
		");
}

test bool PublicFunDeclarationVisibleViaQualifiedName(){ 
	writeModule("module MMM public int f() = 3;"); 
	return checkModuleOK("
		module PublicFunDeclarationVisibleViaQualifiedName
			import MMM;
			int main() = f();
		");
}

test bool DefaultFunDeclarationVisible(){ 
	writeModule("module MMM int f() = 3;"); 
	return checkModuleOK("
		module DefaultFunDeclarationVisible
			import MMM;
			int main() = f();
		");
}

// Non-terminal declaration in imported module

test bool NonTerminalVisible(){ 
	writeModule("module MMM syntax A = \"a\";"); 
	return checkModuleOK("
		module NonTerminalVisible
			import MMM;
			A a;
		");
}

test bool QualifiedNonTerminalVisible(){ 
	writeModule("module MMM syntax A = \"a\";"); 
	return checkModuleOK("
		module QualifiedNonTerminalVisible
			import MMM;
			MMM::A a;
		");
}

test bool UseNonTerminal1(){ 
	writeModule("module MMM syntax A = \"a\";"); 
	return checkModuleOK("
		module UseNonTerminal1
			import MMM;
			A main() = [A]\"a\";
		");
}

test bool UseNonTerminal2(){ 
	writeModule("module MMM syntax A = \"a\";"); 
	return checkModuleOK("
		module UseNonTerminal2
			import MMM;
			A anA = [A]\"a\";
		");
}

test bool UseNonTerminal3(){ 
	writeModule("module MMM syntax A = \"a\";"); 
	return checkModuleOK("
		module UseNonTerminal3
			import MMM;
			A main() = (A)`a`;
	");
}

test bool UseNonTerminal4(){ 
	writeModule("module MMM syntax A = \"a\";"); 
	return checkModuleOK("
		module UseNonTerminal4
			import MMM;
			A anA = (A)`a`;
		");
}

test bool ExtendNonTerminal(){
	writeModule("module MMM syntax A = \"a\";"); 
	return checkModuleOK("
		module ExtendNonTerminal
			import MMM;
			syntax A = \"b\";
	
			A a;
		");
}

test bool UseExtendedNonTerminal(){
	writeModule("module MMM syntax A = \"a\";"); 
	return checkModuleOK("
		module UseExtendedNonTerminal
			import MMM;
			syntax A = \"b\";
			A x = [A] \"b\";
	");
}

// Data declaration in imported module

test bool ADTVisible(){ 
	writeModule("module MMM data DATA = d();"); 
	return checkModuleOK("
		module ADTVisible
			import MMM;
			DATA x;
		");
}

test bool QualifiedADTVisible(){ 
	writeModule("module MMM data DATA = d();"); 
	return checkModuleOK("
		module QualifiedADTVisible
			import MMM;
			MMM::DATA x;
		");
}

test bool ExtendADT(){ 
	writeModule("module MMM data DATA = d();"); 
	return checkModuleOK("
		module ExtendADT
			import MMM;
			data DATA = d2(int n);
			DATA x = d2(3);
		");
}

test bool UseVariableInConcreteSyntax() {
    writeModule("module MMM
					syntax A = a:\"a\"; 
                      A hello() {
                        A given_a = (A) `a`;
                        return (A) `\<A given_a\> \<A given_a\>`;
                      }");
                      
    return checkModuleOK("
		module UseVariableInConcreteSyntax
			import MMM;
			A main() = hello();
		");
}


test bool RedeclareConstructorError(){ 
	writeModule("module MMM data DATA = d(int n);"); 
	return unexpectedDeclarationInModule("
		module RedeclareConstructorError
			import MMM;
			data DATA = d(int m);
			DATA x = d(3);
		");
}

// Alias declaration in imported module

test bool UseImportedAlias(){ 
	writeModule("module MMM alias INT = int;"); 
	return checkModuleOK("
		module UseImportedAlias
			import MMM;
			INT x = 3;
		");
}

test bool ClosureInTuple() {
    writeModule("module MMM
					tuple[int(int)] f() = \<int(int i) { return 2 * i; }\>;");
    return checkModuleOK("
		module ClosureInTuple
			import MMM;
			value main() = f();
		");
}
test bool VoidClosureInTuple1() {
    writeModule("module MMM
					tuple[void(int)] f() = \<void(int i) { return; }\>;");
    return checkModuleOK("
		module VoidClosureInTuple1
			import MMM;
			value main() = f();
		");
}

test bool VoidClosureInTuple2() {
    writeModule("module MMM 
					tuple[void(int)] f() = \<(int i) { return; }\>;");
    return checkModuleOK("
		module VoidClosureInTuple2
			import MMM;
			value main() = f();
		");
}

test bool VoidClosureInTuple3() {
    writeModule("module MMM
					tuple[void(int)] f() = \<void(int i) { return; }\>;");
    return checkModuleOK("
		module VoidClosureInTuple3
			import MMM;
			value main() = f();
		");
}

test bool VoidClosureInTuple4() {
    writeModule("module MMM
					tuple[void(int)] f() = \<(int i) { return; }\>;");
    return checkModuleOK("
		module VoidClosureInTuple4
			import MMM;
			value main() = f();
		");
}

test bool NonAmbiguousParameter1() =
    checkModuleOK("
		module NonAmbiguousParameter1
			data F = f(str s, int n) | f(int n, str s);
			int getN(f(str s, n)) = n;
		");
        
test bool NonAmbiguousParameter2() =
    checkModuleOK("
		module NonAmbiguousParameter2
			data F = f(str s, int n) | f(int n, str s);
			int getN(f(\"a\", n)) = n;
		");
    
test bool NonAmbiguousParameter3() =
    checkModuleOK("
		module NonAmbiguousParameter3
			data F = f(str s, int n) | f(int n, str s);
			int getN(f(s, int n)) = n;
		");	

test bool NonAmbiguousParameter4() =
    checkModuleOK("
		module NonAmbiguousParameter4
			data F = f(int n) | f(int n, str s);
			int getN(f(n)) = n;
		");

test bool NonAmbiguousParameter5() =
    checkModuleOK("
		module NonAmbiguousParameter5
			data F = f(int n) | f(int n, str s);
			int getN(f(n, s)) = n;
		");               
            
test bool AmbiguousParameter1() =
    unexpectedDeclarationInModule("
		module AmbiguousParameter1
			data F = f(str s, int n) | f(int n, str s);
			int getN(f(s, n)) = n;
		");
            
test bool listWithWrongArity() =
    unexpectedDeclaration("list[int,str] x = [];");

test bool F1() = checkModuleOK("
	module F1
		int f(int n, int k = 0) { return n; }
		int x = f(1);
	");

test bool F2() = argumentMismatchInModule("
	module F2
		int f(int n, int k = 0) { return n; }
		int x = f(true);
	");

test bool F3() = argumentMismatchInModule("
	module F3
		int f(int n, int k = 0) { return n; }
		int x = f();
	");

test bool F4() = argumentMismatchInModule("
	module F4
		int f(int n, int k = 0) { return n; }
		int x = f(1, \"a\");
	");

test bool F5() = checkModuleOK("
	module F5
		int f(int n, int k = 0) { return n; }
		int x = f(1, k=3);
	");

test bool F6() = argumentMismatchInModule("
	module F6
		int f(int n, int k = 0) { return n; }
		int x = f(1, k=\"a\");
	");

test bool F7() = argumentMismatchInModule("
	module F7
		int f(int n, int k = 0) { return n; }
		int x = f(1, kkk=\"a\");
	");

test bool F8() = unexpectedTypeInModule("
	module F8
		int f(int n, int k = 0) { return n; }
		int x = g(1, kkk=\"a\");
	");

test bool O1() = checkModuleOK("
	module O1
		int f(int n, int k = 0) { return n; }
	    str f(str s, bool b = false) { return s; }
	
		int x = f(1, k=1); str y = f(\"a\");
	");

test bool O2() = checkModuleOK("
	module O2
		data D = f(bool n, str s, int z = 0)
			     | f(str q, bool b);
		int f(int n, int k = 0) { return n; }
	    str f(str s, bool b = false) { return s; }
		
		void main(){ int x = f(1, k=1); str y = f(\"a\"); z = f(true, \"a\"); z = f(\"a\", false); }
	");
	
test bool K1() = checkModuleOK("
	module K1
		data D(int n = 0) = d1();
		void main() { D x = d1(); }
	");

test bool K2() = checkModuleOK("
	module K2
		data D(int n = 0) = d1();
		void main() { D x = d1(n=3); }
	");

test bool K3() = argumentMismatchInModule("
	module K3
		data D(int n = 0) = d1();
		void main() { D x = d1(m=3); }
	");

test bool K4() = argumentMismatchInModule("
	module K4
		data D(int n = 0) = d1();
		void main() { D x = d1(n=true); }
	");

test bool K5() = checkModuleOK("
	module K5
		data D(int n = 0) = d1();
	 	data D(bool b = true) = d2();
		void main() { D x = d2(b=true); }
	");

test bool K6() = checkModuleOK("
	module K6
		data D(int n = 0) = d1();
	 	data D(bool b = true) = d2();
		void main() { D x = d2(n=3); }
	");

test bool R1() = checkOK("int f() { return 10; }");

test bool R2() = unexpectedType("int f() { return \"a\"; }");

test bool R3() = unexpectedType("int f(n) { return n; }");

test bool R4() = unexpectedType("list[int] f([n]) { return [n]; }");

test bool R5() = checkOK("value f(b) { return b; }");

test bool R6() = checkOK("value f(b, bool c) { if(c) 10; return b; }");

test bool R7() = unexpectedType("value f(b) {  b || true; return b; }");

test bool R8() = unexpectedType("value f(b) { n = b || true; return b; }");

test bool R9() = unexpectedType("alue f(b) { n = b || true; if(b) 10; return b; }");

test bool R10() = unexpectedType(" value f(b) { n = b || true; if(b) 10; return b; b && false; }");

test bool Issue1051() = checkModuleOK("
	module Issue1051
		data F = f() | g();
    	int func(f()) = 1;
    	int func(g()) = 2;
    	int apply(int (F) theFun, F theArg) = theFun(theArg);
		int main() = apply(func, f());
	");

test bool Var1() = checkModuleOK("
	module Var1
		int f(str s, int n...) = n[0];
		int z = f(\"a\");
	");

// ---- alias -----------------------------------------------------------------

test bool OkAlias1() = checkModuleOK("
	module OkAlias1
		alias INT = int;
		INT z = 13;
	");

test bool UnknownAlias() = unexpectedType("INT z = 13;");

test bool IllegalParameter() = unexpectedTypeInModule("
	module IllegalParameter
		alias INT = int;
		INT[str] z = 13;
	");

test bool OkParameter() = checkModuleOK("
	module OkParameter
		alias LIST[&T] = list[&T];
		LIST[int] z = [1,2];
	");

test bool WrongTypeParameter() = unexpectedTypeInModule("
	module WrongTypeParameter
		alias LIST[&T] = list[&T];
		LIST[str] z = [1,2];
	");

test bool OkIndirectAlias1() = checkModuleOK("
	module OkIndirectAlias1
		alias LIST[&T] = LIST1[&T];
		alias LIST1[&T] = list[&T];

		LIST[int] z = [1,2];
	");

test bool OkIndirectAlias2() = checkModuleOK("
	module OkIndirectAlias2
		alias LIST1[&T] = list[&T];
		alias LIST[&T] = LIST1[&T];

		LIST[int] z = [1,2];
	");

test bool CircularAlias() = unexpectedDeclarationInModule("
	module CircularAlias
		alias X = Y;
		alias Y = X;
	");

test bool LF1() = unexpectedDeclaration("list[int n] l = [];");

test bool SF1() = unexpectedDeclaration("set[int n] l = {};");

test bool MF1() = checkOK("map[str key, int val] x; set[str] y = x.key;");

test bool MF2() = unexpectedType("map[str key, int val] x; set[int] y = x.key;");

test bool MF3() = checkOK("map[str key, int val] x; set[int] y = x.val;");

test bool MF4() = undefinedField("map[str key, int val] x; set[int] y = x.vals;");

test bool MF5() = unexpectedDeclaration("map[str key, int] x;");

test bool MF6() = unexpectedDeclaration("map[str key, int key] x;");

test bool RF1() = checkOK("rel[str a, int b, real r] x; set[str] y = x.a;");

test bool RF2() = checkOK("rel[str a, int b, real r] x; set[int] y = x.b;");

test bool RF3() = checkOK("rel[str a, int b, real r] x; set[real] y = x.r;");

test bool RF4() = undefinedField("rel[str a, int b, real r] x; set[real] y = x.c;");

test bool RF5() = unexpectedDeclaration("rel[str a, int b, real] x;");

test bool RF6() = unexpectedDeclaration("rel[str a, int b, real a] x;");

test bool LRF1() = checkOK("lrel[str a, int b, real r] x; list[str] y = x.a;");

test bool LRF2() = checkOK("lrel[str a, int b, real r] x; list[int] y = x.b;");

test bool LRF3() = checkOK("lrel[str a, int b, real r] x; list[real] y = x.r;");

test bool LRF4() = undefinedField("lrel[str a, int b, real r] x; list[real] y = x.c;");

test bool LRF5() = unexpectedDeclaration("lrel[str a, int b, real] x;");

test bool LRF6() = unexpectedDeclaration("lrel[str a, int b, real a] x;");

test bool TF1() = checkOK("tuple[str a, int b, real r] x; str y = x.a;");

test bool RT2() = checkOK("tuple[str a, int b, real r] x; int y = x.b;");

test bool RT3() = checkOK("tuple[str a, int b, real r] x; real y = x.r;");

test bool TF4() = undefinedField("tuple[str a, int b, real r] x; real y = x.c;");

test bool TF5() = unexpectedDeclaration("tuple[str a, int b, real] x;");

test bool TF6() = unexpectedDeclaration("tuple[str a, int b, real a] x;");

test bool LU1() = checkModuleOK("
	module LU1
        int twice(int n) = 2 * n;    
        int M = twice(3);
");

test bool LU2(){
	writeModule("module A int twice(int n) = 2 * n;");
    return checkModuleOK("
            module LU2
            	import A;
            	int M = twice(3);
	");
}

test bool LU3() {
	writeModule("module A int twice(int n) = 2 * n;");
    writeModule("module B import A");
            
	return checkModuleOK("
        module LU3
            import A;
            int M = twice(3);
	");
}

test bool LU4(){
	writeModule("module A int twice(int n) = 2 * n;");
    writeModule("module B import A;");

	return checkModuleOK("    
        module LU4
            import A;
            import B;
            int M = twice(3);
	");
}

test bool LU5() {
	writeModule("module A int twice(int n) = 2 * n;");
    writeModule("module B
            		import A;
            		import B;");
    return checkModuleOK("        
        module LU5
           	import A;
           	import B;
           	int M = twice(3);
	");
}

test bool LU6(){
	writeModule("module A");
	return checkModuleOK("
		module LU6
           	import A;
           	int twice(int n) = 2 * n;   
            int M = twice(3);
	");
}

test bool LU7(){
	writeModule("module A");
	writeModule("module B
            		import A;
            		int twice(int n) = 2 * n;");
    return checkModuleOK("        
        module LU7
            import B;
            int M = twice(3);
	");
}

test bool LU8(){
	writeModule("module A");
	writeModule("module B
            		import A;
            		int twice(int n) = 2 * n;");
   return checkModuleOK("        
        module LU8
           	import A;
            import B;
            int M = twice(3);
	");
}

test bool I1(){
	writeModule("module A public int N = 0;");

	return unexpectedDeclarationInModule("
        module I1     // -- missing import
           	int M = N;
	");
}
test bool I2(){
	writeModule("module A public int N = 0;");

	return checkModuleOK("
        module I2
           	import A;
        	int M = N;
	");
}

test bool I3(){
	writeModule("module A int N = 0;");  // <-- not public
    
	return unexpectedTypeInModule("
        module I3
           import A;
           int M = N;
	");
}

test bool I4(){
	writeModule("module A int N = 0;");

	return checkModuleOK("  
           module I4
           		extend A;
    			int M = N;
	"); 
}

test bool I5(){
	writeModule("module A public int N = 0;");
    writeModule("module B
           			import A;");    //  <-- import not transitive for decls
    return unexpectedTypeInModule("    
        module I5
           import B;
           int X = N;
	");
}
test bool I6(){
	writeModule("module A public int N = 0;");
	writeModule("module B extend A;");

	return checkModuleOK("
        module I6
      		import B;
       		int X = N;
	");
}

test bool I7(){
	writeModule("module A public int N = 0;");
	writeModule("module B extend A;");
    
	return checkModuleOK("
        module I7
           	extend B;
           	int X = N;
	");
}

test bool I8(){
	writeModule("module A public int N = 0;");
	writeModule("module B import A;");

	return unexpectedTypeInModule("
        module I8
           	extend B;
           	int X = N;
	");
}

test bool I9() = missingModuleInModule("module A import Z;");

test bool C1(){
	writeModule("module A
           			import B;
           			public int N = 0;");
    writeModule("module B
           			import A;");

	return checkModuleOK("  
        module C1
           	import A;
           	int X = N;
	");
}

test bool EXT1(){
	writeModule("module A
             		data D = d1(int n);
             
             		D d1(int n) { if(n \< 0) return d1(-n); else fail; }");
	writeModule("module B
             		extend A;");
	writeModule("module C
             		extend B;");
    
	return checkModuleOK("
        module EXT1
            import C;
            D ddd = d1(10);          
	");
}

test bool EXT2(){
	writeModule("module A
             		data D = d1(int n);");
	writeModule("module B extend A;");
    writeModule("module C
             		extend B;
             
             		D d1(int n) { if(n \< 0) return d1(-n); else fail; }");
	return checkModuleOK("
            module EXT2
             	import C;
             	D ddd = d1(10);          
	");
}

test bool WrongExtend1(){
	writeModule("module A
            		extend B;
            		extend F;");
	writeModule("module B
            		extend C;");
	writeModule("module C 
					import D;");
    return missingModuleInModule("        
        module WrongExtend1
            extend F;
	");
}

// https://github.com/cwi-swat/rascal/issues/435
@ignore{The forward references to `called` are not handled properly}
test bool Issue435() {
	writeModule("module MMM
					bool sideEffect1() {
             			void One() { called = called + 1; return; }
             			int called = 0;  
             			One(); 
             			One(); 
            			One(); 
             			return called == 3;
             		}");
	return checkModuleOK("
		module 	Issue435
			import MMM;
		");
}