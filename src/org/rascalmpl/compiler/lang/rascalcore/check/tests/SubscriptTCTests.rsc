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
module lang::rascalcore::check::tests::SubscriptTCTests

import lang::rascalcore::check::tests::StaticTestingUtils; 

test bool wrongMapIndex1() = unexpectedType("map[int,int] M = (1:10,2:20); M[\"abc\"];");

test bool wrongMapIndex2() = unexpectedType("map[int,int] M = (1:10,2:20); M[\"abc\"] = 3;");    //TODO: getMapFieldsAsTuple called with unexpected type fail

test bool wrongMapAssignment() = unexpectedType("map[int,int] M = (1:10,2:20); M[2] = \"abc\";");

test bool tupleIndexError1() = unexpectedType("\<0, \"a\", 3.5\>[\"abc\"];");

test bool tupleIndexError2() = unexpectedType("T = \<0, \"a\", 3.5\>[\"abc\"]; T[1] = 3;");

test bool NodeIndexError() = unexpectedTypeInModule("
    module NodeIndexError
        data NODE = f(int a, str b, real c);
        void main() { f(0, \"a\", 3.5)[\"abc\"]; }
    ");

test bool NodeAssignmentError() = unexpectedTypeInModule("
    module NodeAssignmentError
        data NODE = f(int a, str b, real c);
        void main(){
            NODE N = f(0, \"a\", 3.5); 
            N.b = 3;
        }
    ");

test bool wrongListIndex1() = unexpectedType("list[int] L = [0,1,2,3]; L[\"abc\"];");
	
test bool wrongListIndex2() = unexpectedType("list[int] L = [0,1,2,3]; L[\"abc\"] = 44;");

test bool wrongListAssignment() = unexpectedType("list[int] L = [0,1,2,3]; L[2] = \"abc\";");

test bool uninitializedTupleVariable1() = uninitialized("tuple[int,int] T; T[1];");

test bool uninitializedTupleVariable2() = uninitialized("tuple[int,int] T; T[1] = 10;");

test bool tupleBoundsError() = unexpectedType("\<0, \"a\", 3.5\>[3] == 3.5;");
	
test bool uninitializedRelVariable() = uninitialized("rel[int,int] R; R[1,2];");
	
test bool uninitializedListVariable1() = uninitialized("list[int] L; L[4];");
	
test bool uninitializedListVariable2() = uninitialized("list[int] L; L[4] = 44;");

test bool uninitializedMapVariable1() = uninitialized("map[int,int] M; M[4];");
	
test bool uninitializedMapVariable2() = uninitialized("map[int,int] M; M[4] = 44;");