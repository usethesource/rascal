package test;

import junit.framework.TestCase;
import java.io.IOException;

public class DataTypeTests extends TestCase{
	
	private static TestFramework tf = new TestFramework();
	
	public void testBool() throws IOException
	{
		assertTrue(tf.runTest("true == true;"));
		assertFalse(tf.runTest("true == false;"));
		assertTrue(tf.runTest("true != false;"));	
		
		assertTrue(tf.runTest("(!true) == false;"));
		assertTrue(tf.runTest("(!false) == true;"));
		
		assertTrue(tf.runTest("(true && true) == true;"));	
		assertTrue(tf.runTest("(true && false) == false;"));	
		assertTrue(tf.runTest("(false && true) == false;"));	 
		assertTrue(tf.runTest("(false && false) == false;"));	
		
		assertTrue(tf.runTest("(true || true) == true;"));	
		assertTrue(tf.runTest("(true || false) == true;"));	
		assertTrue(tf.runTest("(false || true) == true;"));	
		assertTrue(tf.runTest("(false || false) == false;"));	
		
		assertTrue(tf.runTest("(true ==> true) == true;"));	
		assertTrue(tf.runTest("(true ==> false) == false;"));	
		assertTrue(tf.runTest("(false ==> true)  == true;"));	
		assertTrue(tf.runTest("(false ==> false) == true;"));
		
		assertTrue(tf.runTest("(true <==> true) == true;"));	
		assertTrue(tf.runTest("(true <==> false) == false;"));	
		assertTrue(tf.runTest("(false <==> true) == false;"));	
		assertTrue(tf.runTest("(false <==> false) == true;"));
		
		assertTrue(tf.runTest("false  <= false;"));
		assertTrue(tf.runTest("false  <= true;"));
		assertFalse(tf.runTest("true  <= false;"));
		assertTrue(tf.runTest("true   <= true;"));
		
		assertFalse(tf.runTest("false < false;"));
		assertTrue(tf.runTest("false  < true;"));
		assertFalse(tf.runTest("true  < false;"));
		assertFalse(tf.runTest("true  < true;"));
		
		assertTrue(tf.runTest("false  >= false;"));
		assertTrue(tf.runTest("true   >= false;"));
		assertFalse(tf.runTest("false >= true;"));
		assertTrue(tf.runTest("true   >= true;"));
		
		assertFalse(tf.runTest("false > false;"));
		assertTrue(tf.runTest("true   > false;"));
		assertFalse(tf.runTest("false > true;"));
		assertFalse(tf.runTest("true   > true;"));
	}
	
	public void testInt() throws IOException 
	{
		assertTrue(tf.runTest("1 == 1;"));
		assertTrue(tf.runTest("1 != 2;"));
		
		assertTrue(tf.runTest("-1 == -1;"));
		assertTrue(tf.runTest("-1 != 1;"));
		
		assertTrue(tf.runTest("1 + 1 == 2;"));
		assertTrue(tf.runTest("-1 + 2 == 1;"));
		assertTrue(tf.runTest("1 + (-2) == -1;"));
		
		assertTrue(tf.runTest("2 - 1 == 1;"));	
		assertTrue(tf.runTest("2 - 3 == -1;"));	
		assertTrue(tf.runTest("2 - -1 == 3;"));	
		assertTrue(tf.runTest("-2 - 1 == -3;"));	
		
		assertTrue(tf.runTest("2 * 3 == 6;"));	
		assertTrue(tf.runTest("-2 * 3 == -6;"));	
		assertTrue(tf.runTest("2 * (-3) == -6;"));
		assertTrue(tf.runTest("-2 * (-3) == 6;"));	
		
		assertTrue(tf.runTest("8 / 4 == 2;"));	
		assertTrue(tf.runTest("-8 / 4 == -2;"));
		assertTrue(tf.runTest("8 / -4 == -2;"));	
		assertTrue(tf.runTest("-8 / -4 == 2;"));
		
		assertTrue(tf.runTest("7 / 2 == 3;"));	
		assertTrue(tf.runTest("-7 / 2 == -3;"));
		assertTrue(tf.runTest("7 / -2 == -3;"));	
		assertTrue(tf.runTest("-7 / -2 == 3;"));	
		
		assertTrue(tf.runTest("0 / 5 == 0;"));	
		assertTrue(tf.runTest("5 / 1 == 5;"));	
		
		assertTrue(tf.runTest("5 % 2 == 1;"));	
		assertTrue(tf.runTest("-5 % 2 == -1;"));
		assertTrue(tf.runTest("5 % -2 == 1;"));		
		
		assertTrue(tf.runTest("-2 <= -1;"));
		assertTrue(tf.runTest("-2 <= 1;"));
		assertTrue(tf.runTest("1 <= 2;"));
		assertTrue(tf.runTest("2 <= 2;"));
		assertFalse(tf.runTest("2 <= 1;"));
		
		assertTrue(tf.runTest("-2 < -1;"));
		assertTrue(tf.runTest("-2 < 1;"));
		assertTrue(tf.runTest("1 < 2;"));
		assertFalse(tf.runTest("2 < 2;"));
		
		assertTrue(tf.runTest("-1 >= -2;"));
		assertTrue(tf.runTest("1 >= -1;"));
		assertTrue(tf.runTest("2 >= 1;"));
		assertTrue(tf.runTest("2 >= 2;"));
		assertFalse(tf.runTest("1 >= 2;"));
		
		assertTrue(tf.runTest("-1 > -2;"));
		assertTrue(tf.runTest("1 > -1;"));
		assertTrue(tf.runTest("2 > 1;"));
		assertFalse(tf.runTest("2 > 2;"));
		assertFalse(tf.runTest("1 > 2;"));
		
		assertTrue(tf.runTest("(3 > 2 ? 3 : 2) == 3;"));
		
	}
	
	public void testReal() throws IOException 
	{
		assertTrue(tf.runTest("1.0 == 1.0;"));
		assertTrue(tf.runTest("1.0 != 2.0;"));
		
		assertTrue(tf.runTest("-1.0 == -1.0;"));
		assertTrue(tf.runTest("-1.0 != 1.0;"));
		
		assertTrue(tf.runTest("1.0 == 1;"));
		assertTrue(tf.runTest("1 == 1.0;"));
		
		assertTrue(tf.runTest("{value x = 1.0; value y = 1; x == y; }"));
		assertTrue(tf.runTest("{value x = 1.0; value y = 2; x != y; }"));
		
		assertTrue(tf.runTest("1.0 + 1.0 == 2.0;"));
		assertTrue(tf.runTest("-1.0 + 2.0 == 1.0;"));
		assertTrue(tf.runTest("1.0 + (-2.0) == -1.0;"));
		
		assertTrue(tf.runTest("1.0 + 1 == 2.0;"));
		assertTrue(tf.runTest("-1 + 2.0 == 1.0;"));
		assertTrue(tf.runTest("1.0 + (-2) == -1.0;"));
		
		assertTrue(tf.runTest("2.0 - 1.0 == 1.0;"));	
		assertTrue(tf.runTest("2.0 - 3.0 == -1.0;"));	
		assertTrue(tf.runTest("2.0 - -1.0 == 3.0;"));	
		assertTrue(tf.runTest("-2.0 - 1.0 == -3.0;"));
		
		assertTrue(tf.runTest("2.0 - 1 == 1.0;"));	
		assertTrue(tf.runTest("2 - 3.0 == -1.0;"));	
		assertTrue(tf.runTest("2.0 - -1 == 3.0;"));	
		assertTrue(tf.runTest("-2 - 1.0 == -3.0;"));
		
		assertTrue(tf.runTest("2.0 * 3.0 == 6.0;"));	
		assertTrue(tf.runTest("-2.0 * 3.0 == -6.0;"));	
		assertTrue(tf.runTest("2.0 * (-3.0) == -6.0;"));
		assertTrue(tf.runTest("-2.0 * (-3.0) == 6.0;"));	
		
		assertTrue(tf.runTest("2.0 * 3 == 6.0;"));	
		assertTrue(tf.runTest("-2 * 3.0 == -6.0;"));	
		assertTrue(tf.runTest("2.0 * (-3) == -6.0;"));
		assertTrue(tf.runTest("-2 * (-3.0) == 6.0;"));	
		
		assertTrue(tf.runTest("8.0 / 4.0 == 2.0;"));	
		assertTrue(tf.runTest("-8.0 / 4.0 == -2.0;"));
		assertTrue(tf.runTest("8.0 / -4.0 == -2.0;"));	
		assertTrue(tf.runTest("-8.0 / -4.0 == 2.0;"));
		
		assertTrue(tf.runTest("7.0 / 2.0 == 3.5;"));	
		assertTrue(tf.runTest("-7.0 / 2.0 == -3.5;"));
		assertTrue(tf.runTest("7.0 / -2.0 == -3.5;"));	
		assertTrue(tf.runTest("-7.0 / -2.0 == 3.5;"));	
		
		assertTrue(tf.runTest("0.0 / 5.0 == 0.0;"));	
		assertTrue(tf.runTest("5.0 / 1.0 == 5.0;"));	
		
		assertTrue(tf.runTest("7 / 2.0 == 3.5;"));	
		assertTrue(tf.runTest("-7.0 / 2 == -3.5;"));
		assertTrue(tf.runTest("7 / -2.0 == -3.5;"));	
		assertTrue(tf.runTest("-7.0 / -2 == 3.5;"));	
		
		assertTrue(tf.runTest("-2.0 <= -1.0;"));
		assertTrue(tf.runTest("-2.0 <= 1.0;"));
		assertTrue(tf.runTest("1.0 <= 2.0;"));
		assertTrue(tf.runTest("2.0 <= 2.0;"));
		assertFalse(tf.runTest("2.0 <= 1.0;"));
		
		assertTrue(tf.runTest("-2 <= -1.0;"));
		assertTrue(tf.runTest("-2.0 <= 1;"));
		assertTrue(tf.runTest("1 <= 2.0;"));
		assertTrue(tf.runTest("2.0 <= 2;"));
		assertFalse(tf.runTest("2 <= 1.0;"));
		
		assertTrue(tf.runTest("-2.0 < -1.0;"));
		assertTrue(tf.runTest("-2.0 < 1.0;"));
		assertTrue(tf.runTest("1.0 < 2.0;"));
		assertFalse(tf.runTest("2.0 < 2.0;"));
		
		assertTrue(tf.runTest("-2 < -1.0;"));
		assertTrue(tf.runTest("-2.0 < 1;"));
		assertTrue(tf.runTest("1 < 2.0;"));
		assertFalse(tf.runTest("2.0 < 2;"));
		
		assertTrue(tf.runTest("-1.0 >= -2.0;"));
		assertTrue(tf.runTest("1.0 >= -1.0;"));
		assertTrue(tf.runTest("2.0 >= 1.0;"));
		assertTrue(tf.runTest("2.0 >= 2.0;"));
		assertFalse(tf.runTest("1.0 >= 2.0;"));
		
		assertTrue(tf.runTest("-1 >= -2.0;"));
		assertTrue(tf.runTest("1.0 >= -1;"));
		assertTrue(tf.runTest("2 >= 1.0;"));
		assertTrue(tf.runTest("2.0 >= 2;"));
		assertFalse(tf.runTest("1 >= 2.0;"));
		
		assertTrue(tf.runTest("-1.0 > -2.0;"));
		assertTrue(tf.runTest("1.0 > -1.0;"));
		assertTrue(tf.runTest("2.0 > 1.0;"));
		assertFalse(tf.runTest("2.0 > 2.0;"));
		assertFalse(tf.runTest("1.0 > 2.0;"));
		
		assertTrue(tf.runTest("-1 > -2.0;"));
		assertTrue(tf.runTest("1.0 > -1;"));
		assertTrue(tf.runTest("2 > 1.0;"));
		assertFalse(tf.runTest("2.0 > 2;"));
		assertFalse(tf.runTest("1 > 2.0;"));
		
		assertTrue(tf.runTest("3.5 > 2.5 ? 3.5 : 2.5 == 3.5;"));
		
		assertTrue(tf.runTest("3.5 > 2 ? 3.5 : 2 == 3.5;"));
		assertTrue(tf.runTest("3.5 > 4 ? 3.5 : 2 == 2;"));
	}
	
	public void testString() throws IOException {
		assertTrue(tf.runTest("\"\" == \"\";"));
		assertTrue(tf.runTest("\"abc\" != \"\";"));
		assertTrue(tf.runTest("\"abc\" == \"abc\";"));
		assertTrue(tf.runTest("\"abc\" != \"def\";"));
		
		assertTrue(tf.runTest("\"abc\" + \"\" == \"abc\";"));
		assertTrue(tf.runTest("\"abc\" + \"def\" == \"abcdef\";"));
		
		assertTrue(tf.runTest("\"\" <= \"\";"));
		assertTrue(tf.runTest("\"\" <= \"abc\";"));
		assertTrue(tf.runTest("\"abc\" <= \"abc\";"));
		assertTrue(tf.runTest("\"abc\" <= \"def\";"));
		
		assertFalse(tf.runTest("\"\" < \"\";"));
		assertTrue(tf.runTest("\"\" < \"abc\";"));
		assertFalse(tf.runTest("\"abc\" < \"abc\";"));
		assertTrue(tf.runTest("\"abc\" < \"def\";"));
		
		assertTrue(tf.runTest("\"\" >= \"\";"));
		assertTrue(tf.runTest("\"abc\" >= \"\";"));
		assertTrue(tf.runTest("\"abc\" >= \"abc\";"));
		assertTrue(tf.runTest("\"def\" >= \"abc\";"));
		
		assertFalse(tf.runTest("\"\" > \"\";"));
		assertTrue(tf.runTest("\"abc\" > \"\";"));
		assertFalse(tf.runTest("\"abc\" > \"abc\";"));
		assertTrue(tf.runTest("\"def\" > \"abc\";"));
	}
	
	public void testLocation() throws IOException {
		//assertTrue(tf.runTest("{area(5,2,6,8,0,0); true;}"));
		//assertTrue(tf.runTest("{file(\"pico1.trm\"); true;}"));
		assertTrue(tf.runTest("{area-in-file(\"pico1.trm\",area(5,2,6,8,0,0)); true;}"));
	}
	
	public void testList() throws IOException 
	{
		assertTrue(tf.runTest("[] == [];"));
		assertTrue(tf.runTest("[] != [1];"));
		assertTrue(tf.runTest("[1] == [1];"));
		assertTrue(tf.runTest("[1] != [2];"));
		assertTrue(tf.runTest("[1, 2] == [1, 2];"));
		assertTrue(tf.runTest("[1, 2] != [2, 1];"));
		
		assertTrue(tf.runTest("[] + [] == [];"));
		assertTrue(tf.runTest("[1, 2, 3] + [] == [1, 2, 3];"));
		assertTrue(tf.runTest("[] + [1, 2, 3] == [1, 2, 3];"));
		assertTrue(tf.runTest("[1, 2] + [3, 4, 5] == [1, 2, 3, 4, 5];"));	
		
		assertTrue(tf.runTest("([1, 2] + [3, 4]) + [5] == [1, 2, 3, 4, 5];"));	
		assertTrue(tf.runTest("[1, 2] + ([3, 4] + [5]) == [1, 2, 3, 4, 5];"));	
		assertTrue(tf.runTest("[1, 2] + [3, 4] + [5] == [1, 2, 3, 4, 5];"));
		
		assertTrue(tf.runTest("[1, 2] + 3 == [1, 2, 3];"));
		assertTrue(tf.runTest("1 +  [2, 3] == [1, 2, 3];"));
		
		assertTrue(tf.runTest("[1,1,2,2,3,3,4,4,5] - [1,2,4] == [3,3,5];"));
		assertTrue(tf.runTest("[1,2,3,4,5,4,3,2,1] - [1,2,4] == [3,5,3];"));
		
		assertTrue(tf.runTest("[] <= [];"));
		assertTrue(tf.runTest("[] <= [1];"));
		assertTrue(tf.runTest("[2, 1, 0] <= [2, 3];"));
		assertTrue(tf.runTest("[2, 1] <= [2, 3, 0];"));
		assertTrue(tf.runTest("[2, 1] <= [2, 1];"));
		assertTrue(tf.runTest("[2, 1] <= [2, 1, 0];"));
		
		assertTrue(tf.runTest("[] < [1];"));
		assertTrue(tf.runTest("[2, 1, 0] < [2, 3];"));
		assertTrue(tf.runTest("[2, 1] < [2, 3, 0];"));
		assertTrue(tf.runTest("[2, 1] < [2, 1, 0];"));
		
		assertTrue(tf.runTest("[] >= [];"));
		assertTrue(tf.runTest("[1] >= [];"));
		assertTrue(tf.runTest("[2, 3] >= [2, 1, 0];"));
		assertTrue(tf.runTest("[2, 3, 0] >= [2, 1];"));
		assertTrue(tf.runTest("[2, 1] >= [2, 1];"));
		assertTrue(tf.runTest("[2, 1, 0] >= [2, 1];"));
		
		assertTrue(tf.runTest("[1] > [];"));
		assertTrue(tf.runTest("[2, 3] > [2, 1, 0];"));
		assertTrue(tf.runTest("[2, 3, 0] > [2, 1];"));
		assertTrue(tf.runTest("[2, 1, 0] > [2, 1];"));
		
		assertTrue(tf.runTest("[] * [] == [];"));
		assertTrue(tf.runTest("[1] * [9] == [<1,9>];"));
		assertTrue(tf.runTest("[1, 2] * [9] == [<1,9>, <2,9>];"));
		assertTrue(tf.runTest("[1, 2, 3] * [9] == [<1,9>, <2,9>, <3,9>];"));
		assertTrue(tf.runTest("[1, 2, 3] * [9, 10] == [<1,9>, <1,10>, <2,9>, <2,10>, <3,9>, <3,10>];"));
		
		assertTrue(tf.runTest("2 in [1, 2, 3];"));
		assertTrue(tf.runTest("3 notin [2, 4, 6];"));
		
		assertTrue(tf.runTest("2 > 3 ? [1,2] : [1,2,3] == [1,2,3];"));
	}
	
	public void testRange() throws IOException {
		assertTrue(tf.runTest("[1 .. 1] == [1];"));
		assertTrue(tf.runTest("[1 .. 2] == [1, 2];"));
		assertTrue(tf.runTest("[1 .. -1] == [1, 0, -1];"));
		assertTrue(tf.runTest("[1, 2 .. 10] == [1,2,3,4,5,6,7,8,9,10];"));
		assertTrue(tf.runTest("[1, 3 .. 10] == [1,3,5,7,9];"));
		assertTrue(tf.runTest("[1, -2 .. 10] == [1];"));
		assertTrue(tf.runTest("[1, -3 .. -10] == [1,-3,-7];"));
	}
	
	public void testSet() throws IOException {
		assertTrue(tf.runTest("{} == {};"));
		assertTrue(tf.runTest("{} != {1};"));
		assertTrue(tf.runTest("{1} == {1};"));
		assertTrue(tf.runTest("{1} != {2};"));
		assertTrue(tf.runTest("{1, 2} == {1, 2};"));
		assertTrue(tf.runTest("{1, 2} == {2, 1};"));
		assertTrue(tf.runTest("{1, 2, 3, 1, 2, 3} == {3, 2, 1};"));	
		
		assertTrue(tf.runTest("{1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};"));
		assertTrue(tf.runTest("{1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {10, 2, 3, 4, 5, 6, 7, 8, 9, 1};"));
		assertTrue(tf.runTest("{1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {10, 9, 3, 4, 5, 6, 7, 8, 2, 1};"));
		assertTrue(tf.runTest("{1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {10, 9, 7, 4, 5, 6, 3, 8, 2, 1};"));
		assertTrue(tf.runTest("{1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {10, 9, 7, 6, 5, 4, 3, 8, 2, 1};"));
		
		assertTrue(tf.runTest("{{1}, {2}} == {{2}, {1}};"));
		assertTrue(tf.runTest("{{}} == {{}};"));
		assertTrue(tf.runTest("{{}, {}} == {{}};"));
		assertTrue(tf.runTest("{{}, {}, {}} == {{}};"));
		
		assertTrue(tf.runTest("{{1, 2}, {3,4}} == {{2,1}, {4,3}};"));	
	
		assertTrue(tf.runTest("{} + {} == {};"));
		assertTrue(tf.runTest("{1, 2, 3} + {} == {1, 2, 3};"));
		assertTrue(tf.runTest("{} + {1, 2, 3} == {1, 2, 3};"));
		assertTrue(tf.runTest("{1, 2} + {3, 4, 5} == {1, 2, 3, 4, 5};"));	
		assertTrue(tf.runTest("{1, 2, 3, 4} + {3, 4, 5} == {1, 2, 3, 4, 5};"));
		assertTrue(tf.runTest("{{1, 2}, {3,4}} + {{5,6}} == {{1,2},{3,4},{5,6}};"));
		assertTrue(tf.runTest("1 + {2,3} == {1,2,3};"));
		assertTrue(tf.runTest("{1,2} + 3 == {1,2,3};"));
		
		assertTrue(tf.runTest("{} - {} == {};"));
		assertTrue(tf.runTest("{1, 2, 3} - {} == {1, 2, 3};"));
		assertTrue(tf.runTest("{} - {1, 2, 3} == {};"));
		assertTrue(tf.runTest("{1, 2, 3} - {3, 4, 5} == {1, 2};"));	
		assertTrue(tf.runTest("{1, 2, 3, 4} - {1, 2, 3, 4, 5} == {};"));
		assertTrue(tf.runTest("{{1, 2}, {3,4}, {5,6}} - {{3,4}} == {{1,2}, {5,6}};"));
		assertTrue(tf.runTest("{1,2,3} - 3 == {1,2};"));
		
		assertTrue(tf.runTest("{} & {} == {};"));
		assertTrue(tf.runTest("{1, 2, 3} & {} == {};"));
		assertTrue(tf.runTest("{} & {1, 2, 3} == {};"));
		assertTrue(tf.runTest("{1, 2, 3} & {3, 4, 5} == {3};"));	
		assertTrue(tf.runTest("{1, 2, 3, 4} & {3, 4, 5} == {3, 4};"));	
		assertTrue(tf.runTest("{{1,2},{3,4},{5,6}} & {{2,1}, {8,7}, {6,5}} == {{1,2},{5,6}};"));
		
		assertTrue(tf.runTest("{} <= {};"));
		assertTrue(tf.runTest("{} <= {1};"));
		assertTrue(tf.runTest("{2, 1} <= {1, 2};"));
		assertTrue(tf.runTest("{2, 1} <= {1, 2, 3};"));
		assertTrue(tf.runTest("{2, 1} <= {2, 1, 0};"));
	
		assertTrue(tf.runTest("{} < {1};"));
		assertTrue(tf.runTest("{2, 1} < {2, 1, 3};"));
	
		assertTrue(tf.runTest("{} >= {};"));
		assertTrue(tf.runTest("{1} >= {};"));
		assertTrue(tf.runTest("{2, 3} >= {2};"));
	
		assertTrue(tf.runTest("{1} > {};"));
		assertTrue(tf.runTest("{2, 1, 3} > {2, 3};"));
		
		assertTrue(tf.runTest("{} * {} == {};"));
		assertTrue(tf.runTest("{1} * {9} == {<1,9>};"));
		assertTrue(tf.runTest("{1, 2} * {9} == {<1,9>, <2,9>};"));
		assertTrue(tf.runTest("{1, 2, 3} * {9} == {<1,9>, <2,9>, <3,9>};"));
		assertTrue(tf.runTest("{1, 2, 3} * {9, 10} == {<1,9>, <1,10>, <2,9>, <2,10>, <3,9>, <3,10>};"));
		
		assertTrue(tf.runTest("2 in {1, 2, 3};"));
		assertTrue(tf.runTest("{4,3} in {{1, 2}, {3,4}, {5,6}};"));
		
		assertTrue(tf.runTest("5 notin {1, 2, 3};"));
		assertTrue(tf.runTest("{7,8} notin {{1, 2}, {3,4}, {5,6}};"));
		
		assertTrue(tf.runTest("3 > 2 ? {1,2} : {1,2,3} == {1,2};"));
		
		assertTrue(tf.runTest("{<\"a\", [1,2]>, <\"b\", []>, <\"c\", [4,5,6]>} != {};"));
		
	}
	
	public void testMap() throws IOException {
		assertTrue(tf.runTest("() == ();"));
		assertTrue(tf.runTest("(1:10) != ();"));
		assertTrue(tf.runTest("(1:10) == (1:10);"));
		assertTrue(tf.runTest("(1:10) != (2:20);"));
		
		assertTrue(tf.runTest("() + () == ();"));
		assertTrue(tf.runTest("(1:10) + () == (1:10);"));
		assertTrue(tf.runTest("(1:10) + (2:20) == (1:10, 2:20);"));
		assertTrue(tf.runTest("(1:10, 2:20) + (2:25) == (1:10, 2:25);"));
		
		assertTrue(tf.runTest("() - () == ();"));
		assertTrue(tf.runTest("(1:10, 2:20) - () == (1:10,2:20);"));
		assertTrue(tf.runTest("(1:10, 2:20) - (2:20) == (1:10);"));
		assertTrue(tf.runTest("(1:10, 2:20) - (2:25) == (1:10);")); // This is current behaviour; is this ok?
	
		assertTrue(tf.runTest("() & () == ();"));
		assertTrue(tf.runTest("(1:10) & () == ();"));
		assertTrue(tf.runTest("(1:10, 2:20, 3:30, 4:40) & (2:20, 4:40, 5:50) == (2:20, 4:40);"));
		assertTrue(tf.runTest("(1:10, 2:20, 3:30, 4:40) & (5:50, 6:60) == ();"));
		
		assertTrue(tf.runTest("() <= ();"));
		assertTrue(tf.runTest("() <= (1:10);"));
		assertTrue(tf.runTest("(1:10) <= (1:10);"));
		assertTrue(tf.runTest("(1:10) <= (1:10, 2:20);"));
		
		assertFalse(tf.runTest("() < ();"));
		assertTrue(tf.runTest("() < (1:10);"));
		assertFalse(tf.runTest("(1:10) < (1:10);"));
		assertTrue(tf.runTest("(1:10) < (1:10, 2:20);"));
		
		assertTrue(tf.runTest("() >= ();"));
		assertTrue(tf.runTest("(1:10) >= ();"));
		assertTrue(tf.runTest("(1:10) >= (1:10);"));
		assertTrue(tf.runTest("(1:10, 2:20) >= (1:10);"));
		
		assertFalse(tf.runTest("() > ();"));
		assertTrue(tf.runTest("(1:10) > ();"));
		assertFalse(tf.runTest("(1:10) > (1:10);"));
		assertTrue(tf.runTest("(1:10, 2:20) > (1:10);"));
		
		
		assertTrue(tf.runTest("20 in (1:10, 2:20);"));
		assertFalse(tf.runTest("15 in (1:10, 2:20);"));
		
		assertTrue(tf.runTest("15 notin (1:10, 2:20);"));
		assertFalse(tf.runTest("20 notin (1:10, 2:20);"));
		
		assertTrue(tf.runTest("{map[str,list[int]] m = (\"a\": [1,2], \"b\": [], \"c\": [4,5,6]); m[\"a\"] == [1,2];}"));
	}
	
	public void testTuple() throws IOException {
		assertTrue(tf.runTest("<1, 2.5, true> == <1, 2.5, true>;"));
		assertTrue(tf.runTest("<1, 2.5, true> != <0, 2.5, true>;"));
		assertTrue(tf.runTest("<{1,2}, 3> == <{2,1}, 3>;"));
		assertTrue(tf.runTest("<1, {2,3}> == <1, {3,2}>;"));
		assertTrue(tf.runTest("<{1,2}, {3,4}> == <{2,1},{4,3}>;"));
		
		assertTrue(tf.runTest("<1>           >= <1>;"));
		assertTrue(tf.runTest("<2>           >= <1>;"));
		assertTrue(tf.runTest("<1,2>         >= <1>;"));
		assertTrue(tf.runTest("<1,2>         >= <1,2>;"));
		assertTrue(tf.runTest("<1,2>         >= <1, 1>;"));
		assertTrue(tf.runTest("<1,\"def\">   >= <1, \"abc\">;"));
		assertTrue(tf.runTest("<1, [2,3,4]>  >= <1, [2,3]>;"));
		assertTrue(tf.runTest("<1, [2,3]>    >= <1, [2,3]>;"));
		
		assertFalse(tf.runTest("<1>          > <1>;"));
		assertTrue(tf.runTest("<2>           > <1>;"));
		assertTrue(tf.runTest("<1,2>         > <1>;"));
		assertFalse(tf.runTest("<1,2>        > <1,2>;"));
		assertTrue(tf.runTest("<1,2>         > <1, 1>;"));
		assertTrue(tf.runTest("<1,\"def\">   > <1, \"abc\">;"));
		assertTrue(tf.runTest("<1, [2,3,4]>  > <1, [2,3]>;"));
		assertFalse(tf.runTest("<1, [2,3]>   > <1, [2,3]>;"));
		
		assertTrue(tf.runTest("<1>           <= <1>;"));
		assertTrue(tf.runTest("<1>           <= <2>;"));
		assertTrue(tf.runTest("<1>           <= <1,2>;"));
		assertTrue(tf.runTest("<1,2>         <= <1,2>;"));
		assertTrue(tf.runTest("<1,1>         <= <1, 2>;"));
		assertTrue(tf.runTest("<1,\"abc\">   <= <1, \"def\">;"));
		assertTrue(tf.runTest("<1, [2,3]>    <= <1, [2,3,4]>;"));
		assertTrue(tf.runTest("<1, [2,3]>    <= <1, [2,3]>;"));
		
		assertFalse(tf.runTest("<1>          < <1>;"));
		assertTrue(tf.runTest("<1>           < <2>;"));
		assertTrue(tf.runTest("<1>           < <1,2>;"));
		assertFalse(tf.runTest("<1,2>        < <1,2>;"));
		assertTrue(tf.runTest("<1,1>         < <1, 2>;"));
		assertTrue(tf.runTest("<1,\"abc\">   < <1, \"def\">;"));
		assertTrue(tf.runTest("<1, [2,3]>    < <1, [2,3,4]>;"));
		assertFalse(tf.runTest("<1, [2,3]>   < <1, [2,3]>;"));
		
		assertTrue(tf.runTest("<1, \"a\", true> + <1.5, \"def\"> == <1, \"a\", true> + <1.5, \"def\">;"));
	}
	
	public void testNamedTuple()  throws IOException {
		assertTrue(tf.runTest("{tuple[int key, str val] T = <1, \"abc\">; T.key == 1;}"));
		assertTrue(tf.runTest("{tuple[int key, str val] T = <1, \"abc\">; T.val == \"abc\";}"));
		
		assertTrue(tf.runWithError("{tuple[int key, str val] T = <1, \"abc\">; T.zip == \"abc\";}", "no field exists"));
	}
	
	public void testRelation() throws IOException {
		assertTrue(tf.runTest("{} == {};"));
		assertTrue(tf.runTest("{<1,10>} == {<1,10>};"));
		assertTrue(tf.runTest("{<1,2,3>} == {<1,2,3>};"));
		assertTrue(tf.runTest("{<1,10>, <2,20>} == {<1,10>, <2,20>};"));
		assertTrue(tf.runTest("{<1,10>, <2,20>, <3,30>} == {<1,10>, <2,20>, <3,30>};"));
		assertTrue(tf.runTest("{<1,2,3>, <4,5,6>} == {<4,5,6>, <1,2,3>};"));
		assertTrue(tf.runTest("{<1,2,3,4>, <4,5,6,7>} == {<4,5,6,7>, <1,2,3,4>};"));
		
		assertTrue(tf.runTest("{} != {<1,2>, <3,4>};"));
		assertFalse(tf.runTest("{<1,2>, <3,4>} == {};"));
		
		assertTrue(tf.runTest("{<1, {1,2,3}>, <2, {2,3,4}>} ==  {<1, {1,2,3}>, <2, {2,3,4}>};"));
		assertTrue(tf.runTest("{<1, {1,2,3}>, <2, {2,3,4}>} ==  {<2, {2,3,4}>, <1, {1,2,3}>};"));
		assertTrue(tf.runTest("{<1, {1,2,3}>, <2, {2,3,4}>} ==  {<2, {4,3,2}>, <1, {2,1,3}>};"));
		
		assertTrue(tf.runTest("{<1,10>} + {} == {<1,10>};"));
		assertTrue(tf.runTest("{} + {<1,10>}  == {<1,10>};"));
		assertTrue(tf.runTest("{<1,10>} + {<2,20>} == {<1,10>, <2,20>};"));
		assertTrue(tf.runTest("{<1,10>, <2,20>} + {<3,30>} == {<1,10>, <2,20>, <3,30>};"));
		assertTrue(tf.runTest("{<1,10>, <2,20>} + {<2,20>, <3,30>} == {<1,10>, <2,20>, <3,30>};"));
		
		assertTrue(tf.runTest("{<1,10>} - {} == {<1,10>};"));
		assertTrue(tf.runTest("{} - {<1,10>}  == {};"));
		assertTrue(tf.runTest("{<1,10>, <2,20>} - {<2,20>, <3,30>} == {<1,10>};"));
		
		assertTrue(tf.runTest("{<1,10>} & {} == {};"));
		assertTrue(tf.runTest("{} & {<1,10>}  == {};"));
		assertTrue(tf.runTest("{<1,10>, <2,20>} & {<2,20>, <3,30>} == {<2,20>};"));
		assertTrue(tf.runTest("{<1,2,3,4>, <2,3,4,5>} & {<2,3,4,5>,<3,4,5,6>} == {<2,3,4,5>};"));
		
		assertTrue(tf.runTest("<2,20> in {<1,10>, <2,20>, <3,30>};"));
		assertTrue(tf.runTest("<1,2,3> in {<1,2,3>, <4,5,6>};"));
		
		assertTrue(tf.runTest("<4,40> notin {<1,10>, <2,20>, <3,30>};"));
		assertTrue(tf.runTest("<1,2,4> notin {<1,2,3>, <4,5,6>};"));
		
		assertTrue(tf.runTest("{} o {} == {};"));
		assertTrue(tf.runTest("{<1,10>,<2,20>} o {} == {};"));
		assertTrue(tf.runTest("{} o {<10,100>, <20,200>} == {};"));
		assertTrue(tf.runTest("{<1,10>,<2,20>} o {<10,100>, <20,200>} == {<1,100>, <2,200>};"));
		
		assertTrue(tf.runTest("{<1, \"a\">, <2, \"b\">} * {<false, 0>, <true, 1>} == {<1,\"a\",false,0>,<2,\"b\",false,0>,<1,\"a\",true,1>,<2,\"b\",true,1>};"));
		
		assertTrue(tf.runTest("{<1,2>, <2,3>, <3,4>} + == {<1,2>, <2,3>, <3,4>, <1, 3>, <2, 4>, <1, 4>};"));
		
		assertTrue(tf.runTest("{<1,2>, <2,3>, <3,4>} * == {<1,2>, <2,3>, <3,4>, <1, 3>, <2, 4>, <1, 4>, <1, 1>, <2, 2>, <3, 3>, <4, 4>};"));
		
		assertTrue(tf.runTest("{<1,2>, <2,3>, <3,4>, <4,2>, <4,5>}+ ==	{<1,2>, <2,3>, <3,4>, <4,2>, <4,5>, <1, 3>, <2, 4>, <3, 2>, <3, 5>, <4, 3>, <1, 4>, <2, 2>, <2, 5>, <3, 3>, <4, 4>, <1, 5>};"));
		
		assertTrue(tf.runTest("{<1,2>, <2,3>, <3,4>, <4,2>, <4,5>}* == {<1,2>, <2,3>, <3,4>, <4,2>, <4,5>, <1, 3>, <2, 4>, <3, 2>, <3, 5>, <4, 3>, <1, 4>, <2, 2>, <2, 5>, <3, 3>, <4, 4>, <1, 5>, <1, 1>, <5, 5>};"));
	}
	
	public void testNamedRelation() throws IOException {
		assertTrue(tf.runTest("{rel[int from, int to] R = {<1,10>, <2,20>}; R.from == {1,2};}"));
		assertTrue(tf.runTest("{rel[int from, int to] R = {<1,10>, <2,20>}; R.to == {10,20};}"));
		assertTrue(tf.runWithError("{rel[int from, int to] R = {<1,10>, <2,20>}; R.zip == {10,20};}", "no field exists"));
	}
	
	public void testGood() throws IOException {
		tf = new TestFramework("data NODE val(value V) | f | f(NODE a);");
		assertTrue(tf.runTestInSameEvaluator("f(val(1)) == f(val(1));"));
	}
	
	public void testTree() throws IOException {
		tf = new TestFramework("data NODE int V | string(str x)  | s(set[NODE] s) | l(list[NODE]) | m(map[NODE,NODE] m) | f | f(NODE a) | f(NODE a, NODE b) | g | g(NODE a) | g(NODE a,NODE b);");
		
		assertTrue(tf.runTestInSameEvaluator("f() == f();"));
		assertTrue(tf.runTestInSameEvaluator("f() != g();"));
		assertTrue(tf.runTestInSameEvaluator("f(1) == f(1);"));
		assertTrue(tf.runTestInSameEvaluator("f(1) != g(1);"));
		assertTrue(tf.runTestInSameEvaluator("f(1,2) == f(1,2);"));
		assertTrue(tf.runTestInSameEvaluator("f(1,2) != f(1,3);"));
		assertTrue(tf.runTestInSameEvaluator("f(1,g(2,3)) == f(1,g(2,3));"));
		assertTrue(tf.runTestInSameEvaluator("f(1,g(2,3)) != f(1,g(2,4));"));
		assertTrue(tf.runTestInSameEvaluator("f(1,g(2,s({3,4,5}))) == f(1,g(2,s({3,4,5})));"));
		assertTrue(tf.runTestInSameEvaluator("f(1,g(2,s({3,4,5}))) != f(1,g(2,s({3,4,5,6})));"));
		assertTrue(tf.runTestInSameEvaluator("f(1,g(2,l([3,4,5]))) == f(1,g(2,l([3,4,5])));"));
		assertTrue(tf.runTestInSameEvaluator("f(1,g(2,l([3,4,5]))) != f(1,g(2,l([3,4,5,6])));"));
		assertTrue(tf.runTestInSameEvaluator("f(1,g(2,m((3:30,4:40,5:50)))) == f(1,g(2,m((3:30,4:40,5:50))));"));
		assertTrue(tf.runTestInSameEvaluator("f(1,g(2,m((3:30,4:40,5:50)))) != f(1,g(2,m((3:30,4:40,5:55))));"));
		
		assertTrue(tf.runTestInSameEvaluator("f()           <= f();"));
		assertTrue(tf.runTestInSameEvaluator("f()           <= g();"));
		assertTrue(tf.runTestInSameEvaluator("f()           <= f(1);"));
		assertTrue(tf.runTestInSameEvaluator("f(1)          <= f(1);"));
		assertTrue(tf.runTestInSameEvaluator("f(1, 2)       <= f(1, 3);"));
		assertTrue(tf.runTestInSameEvaluator("f(1, 2)       <= g(1, 3);"));
		assertTrue(tf.runTestInSameEvaluator("f(1, string(\"abc\")) <= f(1, string(\"def\"));"));
		assertTrue(tf.runTestInSameEvaluator("f(1, l([2, 3]))  <= f(1, l([2,3,4]));"));
		assertTrue(tf.runTestInSameEvaluator("f(1, l([2, 3]))  <= f(1, l([2,3]));"));
		
		assertFalse(tf.runTestInSameEvaluator("f()          < f();"));
		assertTrue(tf.runTestInSameEvaluator("f()           < g();"));
		assertTrue(tf.runTestInSameEvaluator("f()           < f(1);"));
		assertFalse(tf.runTestInSameEvaluator("f(1)         < f(1);"));
		assertTrue(tf.runTestInSameEvaluator("f(1, 2)       < f(1, 3);"));
		assertTrue(tf.runTestInSameEvaluator("f(1, 2)       < g(1, 3);"));
		assertTrue(tf.runTestInSameEvaluator("f(1, string(\"abc\")) < f(1, string(\"def\"));"));
		assertTrue(tf.runTestInSameEvaluator("f(1, l([2, 3]))  < f(1, l([2,3,4]));"));
		assertFalse(tf.runTestInSameEvaluator("f(1, l([2, 3])) < f(1, l([2,3]));"));
		
		assertTrue(tf.runTestInSameEvaluator("f()           >= f();"));
		assertTrue(tf.runTestInSameEvaluator("g()           >= f();"));
		assertTrue(tf.runTestInSameEvaluator("f(1)          >= f();"));
		assertTrue(tf.runTestInSameEvaluator("f(1)          >= f(1);"));
		assertTrue(tf.runTestInSameEvaluator("f(1, 3)       >= f(1, 2);"));
		assertTrue(tf.runTestInSameEvaluator("g(1, 2)       >= f(1, 3);"));
		assertTrue(tf.runTestInSameEvaluator("f(1, string(\"def\")) >= f(1, string(\"abc\"));"));
		assertTrue(tf.runTestInSameEvaluator("f(1, l([2,3,4])) >= f(1, l([2,3]));"));
		assertTrue(tf.runTestInSameEvaluator("f(1, l([2, 3]))  >= f(1, l([2,3]));"));
		
		assertFalse(tf.runTestInSameEvaluator("f()          > f();"));
		assertTrue(tf.runTestInSameEvaluator("g()           > f();"));
		assertTrue(tf.runTestInSameEvaluator("f(1)          > f();"));
		assertFalse(tf.runTestInSameEvaluator("f(1)         > f(1);"));
		assertTrue(tf.runTestInSameEvaluator("f(1, 3)       > f(1, 2);"));
		assertTrue(tf.runTestInSameEvaluator("g(1, 2)       > f(1, 3);"));
		assertTrue(tf.runTestInSameEvaluator("f(1, string(\"def\")) > f(1, string(\"abc\"));"));
		assertTrue(tf.runTestInSameEvaluator("f(1, l([2,3,4])) > f(1, l([2,3]));"));
		assertFalse(tf.runTestInSameEvaluator("f(1, l([2, 3])) > f(1, l([2,3]));"));
	}
	
	public void testUndefined() throws IOException {
		assertTrue(tf.runTest("1 =? 13 == 1;"));
		assertTrue(tf.runTest("x =? 13 == 13;"));
		assertTrue(tf.runTest("{ x = 3; x =? 13 == 3; }"));
	}
}
