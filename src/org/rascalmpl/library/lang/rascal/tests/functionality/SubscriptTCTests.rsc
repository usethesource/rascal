module tests::functionality::SubscriptTCTests

import StaticTestingUtils; 

public test bool WrongMapIndex1() = unexpectedType("map[int,int] M = (1:10,2:20); M[\"abc\"];");

public test bool WrongMapIndex2() = unexpectedType("map[int,int] M = (1:10,2:20); M[\"abc\"] = 3;");    //TODO: getMapFieldsAsTuple called with unexpected type fail

public test bool WrongMapAssignment() = unexpectedType("map[int,int] M = (1:10,2:20); M[2] = \"abc\";");

public test bool tupleIndexError1() = unexpectedType("\<0, \"a\", 3.5\>[\"abc\"];");

public test bool tupleIndexError2() = unexpectedType("T = \<0, \"a\", 3.5\>[\"abc\"]; T[1] = 3;");

public test bool nodeIndexError() = unexpectedType("f(0, \"a\", 3.5)[\"abc\"];", initialDecls = ["data NODE = f(int a, str b, real c);"]);
	
public test bool nodeAssignmentError() = unexpectedType("NODE N = f(0, \"a\", 3.5); N.b = 3;", initialDecls = ["data NODE = f(int a, str b, real c);"]);

public test bool WrongListIndex1() = unexpectedType("list[int] L = [0,1,2,3]; L[\"abc\"];");
	
public test bool WrongListIndex2() = unexpectedType("list[int] L = [0,1,2,3]; L[\"abc\"] = 44;");

public test bool WrongListAssignment() = unexpectedType("list[int] L = [0,1,2,3]; L[2] = \"abc\";");

public test bool UninitializedTupleVariable1() = uninitialized("tuple[int,int] T; T[1];");

public test bool UninitializedTupleVariable2() = uninitialized("tuple[int,int] T; T[1] = 10;");

public test bool tupleBoundsError() = unexpectedType("\<0, \"a\", 3.5\>[3] == 3.5;");
	
public test bool UninitializedRelVariable() = uninitialized("rel[int,int] R; R[1,2];");
	
public test bool UninitializedListVariable1() = uninitialized("list[int] L; L[4];");
	
public test bool UninitializedListVariable2() = uninitialized("list[int] L; L[4] = 44;");

public test bool UninitializedMapVariable1() = uninitialized("map[int,int] M; M[4];");
	
public test bool UninitializedMapVariable1() = uninitialized("map[int,int] M; M[4] = 44;");
		
	
	// Changed: no support for relation updates
	//@expected{UninitializedVariable}
	//public test bool UninitializedRelVariable(){
	//	rel[int,int] R; R[1] = 10; return false;
	//}
	