package test;

import org.junit.Test;
import static org.junit.Assert.*;

public class SubscriptTests extends TestFramework {

	@Test
	public void testList() {

		assertTrue(runTest("[0,1,2,3][0] == 0;"));
		assertTrue(runTest("[0,1,2,3][1] == 1;"));
		assertTrue(runTest("[0,1,2,3][2] == 2;"));
		assertTrue(runTest("[0,1,2,3][3] == 3;"));

		assertTrue(runWithError("[0,1,2,3][4] == 3;", "out of bounds"));

		assertTrue(runTest("{list[int] L = [0,1,2,3]; L[0] = 10; L == [10,1,2,3];}"));
		assertTrue(runTest("{list[int] L = [0,1,2,3]; L[1] = 11; L == [0,11,2,3];}"));
		assertTrue(runTest("{list[int] L = [0,1,2,3]; L[2] = 22; L == [0,1,22,3];}"));
		assertTrue(runTest("{list[int] L = [0,1,2,3]; L[3] = 33; L == [0,1,2,33];}"));

		assertTrue(runWithError(
				"{list[int] L = [0,1,2,3]; L[4] = 44; L == [0,1,2,3,44];}",
				"out of bounds"));
	}

	@Test
	public void testMap() {
		assertTrue(runTest("(1:10, 2:20, 3:30)[1] == 10;"));
		assertTrue(runTest("(1:10, 2:20, 3:30)[2] == 20;"));
		assertTrue(runTest("(1:10, 2:20, 3:30)[3] == 30;"));

		// assertTruerunWithError("(1:10, 2:20, 3:30)[4] == 30;", "xxx"));

		assertTrue(runTest("{map[int,int] M = (1:10, 2:20, 3:30); M[1] = 100; M == (1:100, 2:20, 3:30);}"));
		assertTrue(runTest("{map[int,int] M = (1:10, 2:20, 3:30); M[2] = 200; M == (1:10, 2:200, 3:30);}"));
		assertTrue(runTest("{map[int,int] M = (1:10, 2:20, 3:30); M[3] = 300; M == (1:10, 2:20, 3:300);}"));
		assertTrue(runTest("{map[int,int] M = (1:10, 2:20, 3:30); M[4] = 400; M == (1:10, 2:20, 3:30, 4:400);}"));
	}

	@Test
	public void testTuple() {
		assertTrue(runTest("<0, \"a\", 3.5>[0] == 0;"));
		assertTrue(runTest("<0, \"a\", 3.5>[1] == \"a\";"));
		assertTrue(runTest("<0, \"a\", 3.5>[2] == 3.5;"));

		assertTrue(runWithError("<0, \"a\", 3.5>[3] == 3.5;", "out of bounds"));
	}

	@Test
	public void testRelation() {
		assertTrue(runTest("{<1, \"a\">, <2, \"b\">}[0] == {};"));
		assertTrue(runTest("{<1, \"a\">, <2, \"b\">}[1] == {\"a\"};"));
		assertTrue(runTest("{<1, \"a\">, <2, \"b\">}[2] == {\"b\"};"));

		assertTrue(runTest("{<1, \"a\">, <2, \"b\">, <1, \"abc\">}[1] == {\"a\", \"abc\"};"));

		assertTrue(runTest("{<1, \"a\", 10>, <2, \"b\", 20>, <1, \"abc\", 100>}[0] == {};"));
		assertTrue(runTest("{<1, \"a\", 10>, <2, \"b\", 20>, <1, \"abc\", 100>}[1] == {<\"a\", 10>, <\"abc\", 100>};"));
		assertTrue(runTest("{<1, \"a\", 10>, <2, \"b\", 20>, <1, \"abc\", 100>}[2] == {<\"b\", 20>};"));
		assertTrue(runTest("{<1, \"a\", 10>, <2, \"b\", 20>, <1, \"abc\", 100>}[{1,2}] == {<\"a\", 10>, <\"b\", 20>, <\"abc\", 100>};"));
	}

	@Test
	public void testRelationMultiIndex() {
		assertTrue(runTest("{<1,\"a\",1.0>,<2,\"b\",2.0>,<3,\"c\",3.0>}[0] == {};"));
		assertTrue(runTest("{<1,\"a\",1.0>,<2,\"b\",2.0>,<3,\"c\",3.0>}[1] == {<\"a\",1.0>};"));
		assertTrue(runTest("{<1,\"a\",1.0>,<2,\"b\",2.0>,<3,\"c\",3.0>}[2, \"b\"] == {2.0};"));

		assertTrue(runTest("{<1,10,10.5>, <2,20,20.5>, <3,20,30.5>, <2,10,100.5>}[{1},{10,20}] == {10.5};"));
	}

	@Test
	public void testNode() {

		prepare("data NODE = f(int a, str b, real c);");

		assertTrue(runTestInSameEvaluator("f(0, \"a\", 3.5)[0] == 0;"));
		assertTrue(runTestInSameEvaluator("f(0, \"a\", 3.5)[1] == \"a\";"));
		assertTrue(runTestInSameEvaluator("f(0, \"a\", 3.5)[2] == 3.5;"));

		assertTrue(runWithErrorInSameEvaluator("f(0, \"a\", 3.5)[3] == 3.5;",
				"out of bounds"));
		assertTrue(runTestInSameEvaluator("{NODE T = f(0, \"a\", 3.5); T[0] = 10; T == f(10, \"a\", 3.5);}"));

	}
}
