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
module lang::rascalcore::check::tests::DataDeclarationTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool UndefinedValue1() = uninitializedInModule("
    module lang::rascalcore::check::tests::StaticTestingUtilsTests
        data Bool = btrue() | bfalse() | band(Bool left, Bool right) | bor(Bool left, Bool right);  
        void main() { Bool b; b.left; }
    ");
 
test bool UnitializedVariable1() = uninitializedInModule("
    module UnitializedVariable1
        data Bool = btrue() | bfalse() | band(Bool left, Bool right) | bor(Bool left, Bool right); 
        void main() { Bool b; b[left = btrue()]; }
    ");
				  
test bool UnitializedVariable2() = uninitializedInModule("
    module UnitializedVariable2
        data Exp = let(str name, Exp exp1, Exp exp2) | var(str name) | \\int(int intVal);
		data Bool = btrue() | bfalse() | band(Bool left, Bool right) | bor(Bool left, Bool right);
		alias Var2 = str;
        void main() { Bool b; b[left = btrue()]; }
    ");
																  
test bool LetWrongTypeViaAlias() = unexpectedTypeInModule("
    module LetWrongTypeViaAlias
        alias Var2 = str;
	    data Exp2 = let(Var2 var, Exp2 exp1, Exp2 exp2) | var(Var2 var) | \\int(int intVal);

	    void main() { Var2 varx !:= let(\"a\",\\int(1),var(\"a\")); }
    ");
				    
test bool DoubleFieldError2() = unexpectedDeclarationInModule("
    module DoubleFieldError2
	    data D = d(int n) | d(value v);
    ");  
  	
test bool DoubleFieldError3() = unexpectedDeclarationInModule("
    module DoubleFieldError3
        data D = d(int n) | d(int v);
    ");

test bool DoubleFieldError4() = unexpectedDeclarationInModule("
    module DoubleFieldError4
        alias INTEGER = int;
        data D = d(int n) | d(INTEGER v);
    ");

test bool ExactDoubleDataDeclarationIsNotAllowed() = unexpectedDeclarationInModule("
    module ExactDoubleDataDeclarationIsNotAllowed
	    data D = d(int n) | e();
        data D = d(int n);
    ");

test bool UndeclaredTypeError1() = unexpectedDeclarationInModule("
    module UndeclaredTypeError1
	    data D = anE(E e);
    ");
	
test bool SharedKeywordsWork1() = checkModuleOK("
    module SharedKeywordsWork1
        data Y(int ll = 0) = xx();
        bool main() = xx().ll == 0;
    ");
  	
test bool SharedKeywordsWork2() = checkModuleOK("
    module SharedKeywordsWork2
        data Y(int ll = 0);
        data Y = xx();
        bool main() = xx().ll == 0;
    ");

test bool D1() = unexpectedType("D x;");

test bool D2() = checkModuleOK("
    module D2
        data D;
        D x;
    ");

test bool D3() = checkModuleOK("
    module D3
        data D = d1();
        D x;
    ");

test bool D4() = checkModuleOK("
    module D4
        data D = d1();
        void main() { D x = d1(); }
    ");

test bool D5() = checkModuleOK("
    module D5
        data D[&T] = d1(&T fld);
        D[int] x = d1(3);
    ");

test bool D6() = unexpectedTypeInModule("
    module D6
        data D[&T] = d1(&T fld);
        D x = d1(3);
    ");

test bool D7() = unexpectedTypeInModule("
    module D7
        data D[&T] = d1(&T fld);
        void main() { D[int,str] x = d1(3); }
    ");

test bool D8() = unexpectedTypeInModule("
    module D8
        data D = d1();
        void main() { D[&T] x = d1(); }
    ");

test bool D9() = unexpectedTypeInModule("
    module D9
        data D[&T, &U] = d1(&T fld1, &U fld2);
        void main(){ D[int] x = d1(3, \"a\"); }
    ");

test bool D10() {
	writeModule("module A
                    data D[&T] = d1(&T fld);");
	writeModule("module B
                    import A;
                    data D[&T, &U] = d1(&T fld1, &U fld2);
                    void main(){ D[int] x = d1(3, \"a\"); }");
	return unexpectedTypeInModule("
        module D10
            import A;
            import B;
            void main() { D[int] x = d1(3, \"a\"); }
        ");
}

test bool K1() = checkModuleOK("
    module K1
        data D[&T] = d1(&T n, list[&T] l = [n]);
        void main() { d1(1, l=[2]); }
    ");

test bool K2() = argumentMismatchInModule("
    module K2
        data D[&T] = d1(&T n, list[&T] l = [n]);
        void main() { d1(1, l=[\"a\"]); }
    ");

test bool K3() = checkModuleOK("
    module K3
        data D[&T] = d1(&T n, list[&T] l = [n]) | d1(str s, &T x);
        void main() { d1(1, l=[2]); }
    ");

test bool K4() = checkModuleOK("
    module K4
        data D[&T] = d1(&T n, list[&T] l = [n]) | d1(str s, &T x);
        void main() { d1(\"a\", \"b\"); }
    ");

test bool K5() = checkModuleOK("
    module K5
        data D[&T] = d1(&T n, list[&T] l = [n]) | d1(str s, &T x);
        void main() { D[str] x = d1(\"a\", \"b\"); }
    ");

test bool K6() = checkModuleOK("
    module K6
        data D[&T] = d1(&T n, list[&T] l = [n]) | d1(str s, &T x);
        void main() { d1(\"a\", 3); }
    ");

test bool K7() = checkModuleOK("
    module K7
        data D[&T] = d1(&T n, list[&T] l = [n]) | d1(str s, &T x);
        void main() { D[int] x = d1(\"a\", 3); }
    ");

test bool P1() = cannotMatchInModule("
    module P1
        data D[&T] = d1(str s, &T x) | d1(&T n, list[&T] l = [n]);
        void main() { d1(1) := d1(1); }
    ");

test bool P2() = unexpectedTypeInModule("
    module P2
        data D[&T] = d1(str s, &T x) | d1(&T n, list[&T] l = [n]);
        void main() { d1(1, l=[\"a\"]) := d1(1); }
    ");

test bool P3() = unexpectedTypeInModule("
    module P3
        data D[&T] = d1(str s, &T x) | d1(&T n, list[&T] l = [n]);
        void main() { d1(\"a\", 3) := d1(1); }
    ");

test bool P4() = cannotMatchInModule("
    module P4
        data D[&T] = d1(str s, &T x) | d1(&T n, list[&T] l = [n]);
        void main() { d1(\"a\", \"b\") := d1(1); }
    ");

test bool A1() = checkModuleOK("
    module A1
        data D = d1(int n, bool b = false);
        data D = d2(str s, int m = 0);
        data D = d3(bool f, str t = \"a\");

        D X1 = d1(3);       D X2 = d1(3, b=false);
        D Y1 = d2(\"z\");   D Y2 = d2(\"z\", m=1);
        D Z1 = d3(true);    D Z2 = d3(true, t =\"z\");
    ");

test bool A2a() {
	writeModule("module A
                    data D = d1(int n, bool b = false);
                    data D = d2(str s, int m = 0);
                    data D = d3(bool f, str t = \"a\");");
    writeModule("module B import A;");    
	writeModule("module C import B;");
    
	return checkModuleOK("
        module A2a
            import A;
            import B;
            import C;
            void main() { D X1 = d1(3); }
        ");
}

test bool A2b() {
	writeModule("module A
                    data D = d1(int n, bool b = false);
                    data D = d2(str s, int m = 0);
                    data D = d3(bool f, str t = \"a\");");
    writeModule("module B extend A;");    
	writeModule("module C import B;");
    
	return checkModuleOK("
        module A2b
            import A; import B; import C;
			D X1 = d1(3);       D X2 = d1(3, b=false);
            D Y1 = d2(\"z\");   D Y2 = d2(\"z\", m=1);
            D Z1 = d3(true);    D Z2 = d3(true, t =\"z\");
        ");
}

test bool A3a() {
	writeModule("module A
                    data D = d1(int n, bool b = false);
                    data D = d2(str s, int m = 0);");
    writeModule("module B 
                    import A;
					data D = d3(bool f, str t = \"a\");");    
	writeModule("module C
                    import B;");
    
	return checkModuleOK("
        module A3a
            import A; import B; import C;
            void main() { D X1 = d1(3); }
        ");
}

test bool A3b() {
	writeModule("module A
                    data D = d1(int n, bool b = false);
                    data D = d2(str s, int m = 0);");
    writeModule("module B
                    import A;
					data D = d3(bool f, str t = \"a\");");    
	writeModule("module C
                    import B;
					D Z1 = d3(true); 
					D Z2 = d3(true, t =\"z\");");
	return checkModuleOK("
        module A3b
            import A; import B; import C;
        ");
}

test bool A4() {
	writeModule("module A
                    data D = d1(int n, bool b = false);
                    data D = d2(str s, int m = 0);");
    writeModule("module B
                    import A;"); 
	writeModule("module C
                    import B;
					data D = d3(bool f, str t = \"a\");
					D Z1 = d3(true); 
					D Z2 = d3(true, t =\"z\");");
	return checkModuleOK("
        module A4
            import A; import B; import C;
        ");
}

test bool A5() {
	writeModule("module A
                    data D = d1(int n, bool b = false);");
                     
    writeModule("module B
                    import A;
					data D = d2(str s, int m = 0);");
	writeModule("module C
                    import B;
					data D = d3(bool f, str t = \"a\");
					D Z1 = d3(true); 
					D Z2 = d3(true, t =\"z\");");
	return checkModuleOK("
        module A5
            import A; import B; import C;
        ");
}

test bool C1() = checkModuleOK("
    module C1
        data D(int N = 0)      = d1(int n, bool b = false);
        data D(str S = \"a\")  = d2(str s, int m = 0);
        data D(bool B = false) = d3(bool f, str t = \"a\");
         
        D X1 = d1(3);       D X2 = d1(3, b=false);    D X3 = d1(3, b=false, N=1, S=\"z\",B=true);
        D Y1 = d2(\"z\");   D Y2 = d2(\"z\", m=1);    D Y3 = d2(\"z\", m=1, N=1, S=\"z\",B=true);
        D Z1 = d3(true);   D Z2 = d3(true, t =\"z\");D Z3 = d3(true, t =\"z\", N=1, S=\"z\",B=true);
    ");

test bool C2() {
	writeModule("module A
                    data D(int N = 0)      = d1(int n, bool b = false);
                    data D(str S = \"a\")  = d2(str s, int m = 0);
                    data D(bool B = false) = d3(bool f, str t = \"a\");");
    writeModule("module B
                    extend A;");
        
	return checkModuleOK("
        module C2
            import B;
            
            D X1 = d1(3);      D X2 = d1(3, b=false);    D X3 = d1(3, b=false, N=1, S=\"z\",B=true);
            D Y1 = d2(\"z\");  D Y2 = d2(\"z\", m=1);    D Y3 = d2(\"z\", m=1, N=1, S=\"z\",B=true);
            D Z1 = d3(true);   D Z2 = d3(true, t =\"z\");D Z3 = d3(true, t =\"z\", N=1, S=\"z\",B=true);
		");
}

 test bool C3() {
	writeModule("module A
                    data D(int N = 0)      = d1(int n, bool b = false);
                    data D(str S = \"a\")  = d2(str s, int m = 0);");
    writeModule("module B 
                    import A;
                data D(bool B = false) = d3(bool f, str t = \"a\");");
    return unexpectedDeclarationInModule("
        module C3   
            import B;
            
            D Z1 = d3(true);   D Z2 = d3(true, t =\"z\");D Z3 = d3(true, t =\"z\", N=1, S=\"z\",B=true);
        ");
 }

 test bool C4() {
	writeModule("module A
                    data D(int N = 0)      = d1(int n, bool b = false);
                    data D(str S = \"a\")  = d2(str s, int m = 0);");
    writeModule("module B  
                    import A;");
    return checkModuleOK("
        module C4
            import B;
            data D(bool B = false) = d3(bool f, str t = \"a\");
            
            D Z1 = d3(true);   D Z2 = d3(true, t =\"z\");D Z3 = d3(true, t =\"z\",B=true);
        ");
}

test bool C5() {
	writeModule("module A
                    data D(int N = 0)      = d1(int n, bool b = false);");
    writeModule("module B
                    import A;
                    data D(str S = \"a\")   = d2(str s, int m = 0);");
    return checkModuleOK("
        module C5  
            import B;
            data D(bool B = false) = d3(bool f, str t = \"a\");
            
            D Y1 = d2(\"z\");  D Y2 = d2(\"z\", m=1);    D Y3 = d2(\"z\", m=1, S=\"z\");
            D Z1 = d3(true);   D Z2 = d3(true, t =\"z\");D Z3 = d3(true, t =\"z\", B=true);
        ");
}

test bool Escapes1() = checkModuleOK("
    module Escapes1
        data \\D = \\d1(int \\n, bool \\b = false);
        data \\D = \\d2(str \\s, int \\m = 0);
        data \\D = \\d3(bool \\f, str \\t = \"a\");
        
        D X1 = d1(3);       D X2 = d1(3, b=false);
        D Y1 = d2(\"z\");   D Y2 = d2(\"z\", m=1);
        D Z1 = d3(true);    D Z2 = d3(true, t =\"z\");
    ");

test bool Escapes2() = checkModuleOK("
	module Escapes2
        data D = d1(int n, bool b = false);
        data D = d2(str s, int m = 0);
        data D = d3(bool f, str t = \"a\");
            
        \\D X1 = \\d1(3);       \\D X2 = \\d1(3, \\b=false);
        \\D Y1 = \\d2(\"z\");   \\D Y2 = \\d2(\"z\", \\m=1);
        \\D Z1 = \\d3(true);    \\D Z2 = \\d3(true, \\t =\"z\");
    ");
	
// ---- type parameters -------------------------------------------------------

test bool ADTWithTypeParameter() = checkModuleOK("
    module ADTWithTypeParameter
        data D[&T] = d1(&T n);
    ");

test bool UndefinedParameter() = unexpectedTypeInModule("
    module  UndefinedParameter
        data D[&T] = d1(&U n);
    ");

test bool UndefinedBound1() = unexpectedTypeInModule("
    module UndefinedBound1
        data D[&T] = d1(&U \<: &S n);
    ");

test bool UndefinedBound2() = unexpectedTypeInModule("
    module UndefinedBound2
        data D[&T] = d1(&U \<: &S v = 1);
    ");

test bool MissingTypeParameter() = unexpectedTypeInModule("
    module MissingTypeParameter
        data D[&T] = d1(&T n);
        void f(){ D x = d1(10); return x.n; }
    ");

test bool MultipleInstances() = checkModuleOK("
    module MultipleInstances
        data D[&T] = d1(&T n);
        void f() { D[int] x = d1(10); D[str] y = d1(\"abc\"); int m = x.n; str s = y.n; }
    ");

test bool ADTWithTypeParameterAndKW1() = checkModuleOK("
    module ADTWithTypeParameterAndKW1
        data D[&T] = d1(&T n, &T kw = n);
    ");

test bool ADTWithTypeParameterAndKW2() = checkModuleOK("
    module ADTWithTypeParameterAndKW2
        data D[&T] = d1(&T n, &T kw = n);
        void f() { D[int] x = d1(10); int m = x.kw; }
    ");

test bool ADTWithTypeParameterAndKW3() = checkModuleOK("
    module ADTWithTypeParameterAndKW3
        data D[&T] = d1(&T n, &T kw = n);
        void f() { D[int] x = d1(10); str m = x.kw; }
    ");

// https://github.com/cwi-swat/rascal/issues/430

test bool Issue430() = checkModuleOK("
    module Issue430
        data T1 = \\int() | \\void() | string(str s);
		data T2 = \\int() | \\void() | string(str s);
		bool fT1(T1::\\int()) = true;
		bool fT2(T2::\\int()) = true;
    ");


// https://github.com/cwi-swat/rascal/issues/456

test bool Issue456() = checkModuleOK("
    module Issue456
	    data POINT1 = point1(int x, int y, int z = 3, list[str] colors = []);
	    value my_main() =  point1(1,2);
    ");


// https://github.com/cwi-swat/rascal/issues/457

test bool Issue457() = checkModuleOK("
    module Issue457
	    data Exp1[&T] = tval(&T tval) | tval2(&T tval1, &T tval2) | ival(int x);
		value my_main() {m = tval2(\"abc\", \"def\"); str s2 = m.tval2; return s2 == \"def\";}
    "); 

// https://github.com/cwi-swat/rascal/issues/480

test bool Issue480() = checkModuleOK("
	module Issue480
        data Figure (real shrink = 1.0, str fillColor = \"white\", str lineColor = \"black\") 
            =  emptyFigure() 
  		    | ellipse(Figure inner = emptyFigure()) 
  		    | box(Figure inner = emptyFigure());

 		value my_main() = (!(ellipse(inner=emptyFigure(fillColor=\"red\")).fillColor == \"white\"));
	");