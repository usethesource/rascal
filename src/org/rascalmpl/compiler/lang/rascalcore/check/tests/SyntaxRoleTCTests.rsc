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
module lang::rascalcore::check::tests::SyntaxRoleTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

private str exampleGrammar
   = "lexical Z = [a-z]+;
     'syntax A = \"a\" | \"b\" \"b\";
     'layout L = [\\ ]*;
     'syntax E = \"e\"; 
     'data A = a();
     'data E = e();
     ";

test bool TestSimpleModificationOfRoles() = checkModuleOK(
   "module TestSimpleModificationOfRoles
   '  <exampleGrammar>
   '  data[E] exData = e();
   '  syntax[E] exSyntax = (E) `e`;
   "
);

test bool NoGenericUseOfModifiers()         = checkModuleOK(
   "module NoGenericUseOfModifiers
   '  syntax E = \"e\";
   '  data E = e();
   '  data[E] implode(type[data[E]] grammar, syntax[E] tree) = e();
   '  data[E] example = implode(#data[E], [E] \"e\");
   ");

test bool GenericUseOfModifiers()         = checkModuleOK(
   "module GenericUseOfModifiers
   '  syntax E = \"e\";
   '  data E = e();
   '  data[&T] id(data[&T] a, syntax[&T] b) = a;
   '  data[E] example = implode(e(), (E) `e`);
   ");

test bool WrongGenericUseOfModifiers()         = unexpectedTypeInModule(
   "module GenericUseOfModifiers
   '  syntax E = \"e\";
   '  data E = e();
   '  &T id(data[&T] a, syntax[&T] b) = a; // return type is unmodified
   '  data[E] example = implode(e(), (E) `e`);
   ");
   