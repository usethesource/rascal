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