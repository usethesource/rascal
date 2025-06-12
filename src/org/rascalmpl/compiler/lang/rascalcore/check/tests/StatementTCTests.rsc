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
module lang::rascalcore::check::tests::StatementTCTests
/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Bert Lisser - Bert.Lisser@cwi.nl - CWI
*******************************************************************************/

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool assertOK1() = checkOK("assert true;");

test bool assertError2() = unexpectedType("assert 1;");

test bool assertOK2() = checkOK("assert true: \"msg\";");

test bool assertError3() = unexpectedType("assert 1: \"msg\";");

test bool assertError4() = unexpectedType("assert true: 5;");

test bool assertError5() = unexpectedType("assert 3.5;");

test bool assertError6() = unexpectedType("assert 3.5: \"Wrong expression type\";");

test bool assertError7() = undeclaredVariable("assert X;");
 
test bool assertError8() = undeclaredVariable("assert X: \"Wrong expression type\";");

test bool ifThenOK1() = checkOK("if(true) 1;");
test bool ifThenOK2() = checkOK("if(true,true) 1;");

test bool ifThenError1() = unexpectedType("if(true,\"a\") 1;");
test bool ifThenError2() = unexpectedType("if(3){n = 4;};");

test bool ifThenElseOK1() = checkOK("if(true) 1; else 2;");
test bool ifThenElseOK2() = checkOK("if(true,true) 1; else 2;");

test bool ifThenElseError1() = unexpectedType("if(true,\"a\") 1; else 2;");
test bool ifThenElseError2() = unexpectedType("if(\"abc\") {n = 4;} else {n=5;}");
  
test bool WhileOK1() = checkOK("void main(){ while(true) 1; }");
test bool WhileError1() = unexpectedType("void main(){ while(13) 1; }");

test bool solveError1() = unexpectedType("rel[int,int] R1 = {\<1,2\>, \<2,3\>, \<3,4\>}; rel[int,int] T = R1; solve (T; true)  T = T + (T o R1);");

test bool doWhileError1() = unexpectedType("do {n = 4;} while(3);");

test bool whileError1() = unexpectedType("while(3){n = 4;}");	

test bool doOK1() = checkOK("do 1; while(true);");
test bool doError1() = unexpectedType("do 1; while(13);");

test bool forOK1() = checkOK("for(true) 1;");
test bool forError1() = unexpectedType("for(13) 1;");

test bool VisitOK1() = checkOK("visit(1) { case 1 =\> 2 } ");
test bool VisitError1() = unexpectedType("visit(1) { case 1 =\> \"a\" };");
test bool VisitOK2() = checkOK("visit(1) { case 1: insert 2; }");
test bool VisitError2() = unexpectedType("visit(1) { case 1: insert \"a\"; }");
test bool VisitError3() = unexpectedType("visit(1) { case int x: insert \"a\"; }");
test bool VisitError4() = unexpectedType("void main(){ visit(1) { case int x: insert 1; }; x; }");
test bool VisitError5() = unexpectedType("insert 2;");

test bool WrongInsert() = unexpectedType("String vs = visit ([1,2,3]) {case 1: insert \"abc\";} == [\"abc\", 2, 3];;");

// https://github.com/cwi-swat/rascal/issues/416

test bool Issue416() = checkModuleOK("
    module Issue416
        data D = d(int i) | d();
        D d(int i) { if (i % 2 == 0) fail d; else return d();}
    ");

// https://github.com/cwi-swat/rascal/issues/432

test bool Issue432() = unexpectedType("set[value] s := {} && s\<0\> == {};"); 