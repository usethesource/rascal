package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.meta_environment.rascal.interpreter.errors.IndexOutOfBoundsError;
import org.meta_environment.rascal.interpreter.errors.NoSuchFieldError;
import org.meta_environment.rascal.interpreter.errors.TypeError;

public class DataTypeTests extends TestFramework {
	
	@Test
	public void bool()
	{
		
		assertTrue(runTest("true == true;"));
		assertFalse(runTest("true == false;"));
		assertTrue(runTest("true != false;"));	
		
		assertTrue(runTest("(!true) == false;"));
		assertTrue(runTest("(!false) == true;"));
		
		assertTrue(runTest("(true && true) == true;"));	
		assertTrue(runTest("(true && false) == false;"));	
		assertTrue(runTest("(false && true) == false;"));	 
		assertTrue(runTest("(false && false) == false;"));	
		
		assertTrue(runTest("(true || true) == true;"));	
		assertTrue(runTest("(true || false) == true;"));	
		assertTrue(runTest("(false || true) == true;"));	
		assertTrue(runTest("(false || false) == false;"));	
		
		assertTrue(runTest("(true ==> true) == true;"));	
		assertTrue(runTest("(true ==> false) == false;"));	
		assertTrue(runTest("(false ==> true)  == true;"));	
		assertTrue(runTest("(false ==> false) == true;"));
		
		assertTrue(runTest("(true <==> true) == true;"));	
		assertTrue(runTest("(true <==> false) == false;"));	
		assertTrue(runTest("(false <==> true) == false;"));	
		assertTrue(runTest("(false <==> false) == true;"));
		
		assertTrue(runTest("false  <= false;"));
		assertTrue(runTest("false  <= true;"));
		assertFalse(runTest("true  <= false;"));
		assertTrue(runTest("true   <= true;"));
		
		assertFalse(runTest("false < false;"));
		assertTrue(runTest("false  < true;"));
		assertFalse(runTest("true  < false;"));
		assertFalse(runTest("true  < true;"));
		
		assertTrue(runTest("false  >= false;"));
		assertTrue(runTest("true   >= false;"));
		assertFalse(runTest("false >= true;"));
		assertTrue(runTest("true   >= true;"));
		
		assertFalse(runTest("false > false;"));
		assertTrue(runTest("true   > false;"));
		assertFalse(runTest("false > true;"));
		assertFalse(runTest("true   > true;"));
	}
	

	@Test(expected=TypeError.class)
	public void andError() {
		runTest("3 && true;");
	}
	
	@Test(expected=TypeError.class)
	public void impError() {
		runTest("3 ==> true;");
	}
	

	@Test(expected=TypeError.class)
	public void condExpError() {
		runTest("1 ? 2 : 3;");
	}
	
	@Test
	public void testInt()
	{		
		assertTrue(runTest("1 == 1;"));
		assertTrue(runTest("1 != 2;"));
		
		assertTrue(runTest("-1 == -1;"));
		assertTrue(runTest("-1 != 1;"));
		
		assertTrue(runTest("1 + 1 == 2;"));
		assertTrue(runTest("-1 + 2 == 1;"));
		assertTrue(runTest("1 + (-2) == -1;"));
		
		assertTrue(runTest("2 - 1 == 1;"));	
		assertTrue(runTest("2 - 3 == -1;"));	
		assertTrue(runTest("2 - -1 == 3;"));	
		assertTrue(runTest("-2 - 1 == -3;"));	
		
		assertTrue(runTest("2 * 3 == 6;"));	
		assertTrue(runTest("-2 * 3 == -6;"));	
		assertTrue(runTest("2 * (-3) == -6;"));
		assertTrue(runTest("-2 * (-3) == 6;"));	
		
		assertTrue(runTest("8 / 4 == 2;"));	
		assertTrue(runTest("-8 / 4 == -2;"));
		assertTrue(runTest("8 / -4 == -2;"));	
		assertTrue(runTest("-8 / -4 == 2;"));
		
		assertTrue(runTest("7 / 2 == 3;"));	
		assertTrue(runTest("-7 / 2 == -3;"));
		assertTrue(runTest("7 / -2 == -3;"));	
		assertTrue(runTest("-7 / -2 == 3;"));	
		
		assertTrue(runTest("0 / 5 == 0;"));	
		assertTrue(runTest("5 / 1 == 5;"));	
		
		assertTrue(runTest("5 % 2 == 1;"));	
		assertTrue(runTest("-5 % 2 == -1;"));
		assertTrue(runTest("5 % -2 == 1;"));		
		
		assertTrue(runTest("-2 <= -1;"));
		assertTrue(runTest("-2 <= 1;"));
		assertTrue(runTest("1 <= 2;"));
		assertTrue(runTest("2 <= 2;"));
		assertFalse(runTest("2 <= 1;"));
		
		assertTrue(runTest("-2 < -1;"));
		assertTrue(runTest("-2 < 1;"));
		assertTrue(runTest("1 < 2;"));
		assertFalse(runTest("2 < 2;"));
		
		assertTrue(runTest("-1 >= -2;"));
		assertTrue(runTest("1 >= -1;"));
		assertTrue(runTest("2 >= 1;"));
		assertTrue(runTest("2 >= 2;"));
		assertFalse(runTest("1 >= 2;"));
		
		assertTrue(runTest("-1 > -2;"));
		assertTrue(runTest("1 > -1;"));
		assertTrue(runTest("2 > 1;"));
		assertFalse(runTest("2 > 2;"));
		assertFalse(runTest("1 > 2;"));
		
		assertTrue(runTest("(3 > 2 ? 3 : 2) == 3;"));
		
	}
	

	@Test(expected=TypeError.class)
	public void addError() {
		runTest("3 + true;");
	}
	

	@Test(expected=TypeError.class)
	public void subError() {
		runTest("3 - true;");
	}
	
	@Test(expected=TypeError.class)
	public void uMinusError() {
		runTest("- true;");
	}
	
	@Test(expected=TypeError.class)
	public void timesError() {
		runTest("3 * true;");
	}
	
	@Test(expected=TypeError.class)
	public void divError() {
		runTest("3 / true;");
	}
	
	@Test(expected=TypeError.class)
	public void modError() {
		runTest("3 % true;");
	}
	
	
	@Test
	public void real()
	{		
		assertTrue(runTest("1.0 == 1.0;"));
		assertTrue(runTest("1.0 != 2.0;"));
		
		assertTrue(runTest("-1.0 == -1.0;"));
		assertTrue(runTest("-1.0 != 1.0;"));
		
		assertTrue(runTest("1.0 == 1;"));
		assertTrue(runTest("1 == 1.0;"));
		
		assertTrue(runTest("{value x = 1.0; value y = 1; x == y; }"));
		assertTrue(runTest("{value x = 1.0; value y = 2; x != y; }"));
		
		assertTrue(runTest("1.0 + 1.0 == 2.0;"));
		assertTrue(runTest("-1.0 + 2.0 == 1.0;"));
		assertTrue(runTest("1.0 + (-2.0) == -1.0;"));
		
		assertTrue(runTest("1.0 + 1 == 2.0;"));
		assertTrue(runTest("-1 + 2.0 == 1.0;"));
		assertTrue(runTest("1.0 + (-2) == -1.0;"));
		
		assertTrue(runTest("2.0 - 1.0 == 1.0;"));	
		assertTrue(runTest("2.0 - 3.0 == -1.0;"));	
		assertTrue(runTest("2.0 - -1.0 == 3.0;"));	
		assertTrue(runTest("-2.0 - 1.0 == -3.0;"));
		
		assertTrue(runTest("2.0 - 1 == 1.0;"));	
		assertTrue(runTest("2 - 3.0 == -1.0;"));	
		assertTrue(runTest("2.0 - -1 == 3.0;"));	
		assertTrue(runTest("-2 - 1.0 == -3.0;"));
		
		assertTrue(runTest("2.0 * 3.0 == 6.0;"));	
		assertTrue(runTest("-2.0 * 3.0 == -6.0;"));	
		assertTrue(runTest("2.0 * (-3.0) == -6.0;"));
		assertTrue(runTest("-2.0 * (-3.0) == 6.0;"));	
		
		assertTrue(runTest("2.0 * 3 == 6.0;"));	
		assertTrue(runTest("-2 * 3.0 == -6.0;"));	
		assertTrue(runTest("2.0 * (-3) == -6.0;"));
		assertTrue(runTest("-2 * (-3.0) == 6.0;"));	
		
		assertTrue(runTest("8.0 / 4.0 == 2.0;"));	
		assertTrue(runTest("-8.0 / 4.0 == -2.0;"));
		assertTrue(runTest("8.0 / -4.0 == -2.0;"));	
		assertTrue(runTest("-8.0 / -4.0 == 2.0;"));
		
		assertTrue(runTest("7.0 / 2.0 == 3.5;"));	
		assertTrue(runTest("-7.0 / 2.0 == -3.5;"));
		assertTrue(runTest("7.0 / -2.0 == -3.5;"));	
		assertTrue(runTest("-7.0 / -2.0 == 3.5;"));	
		
		assertTrue(runTest("0.0 / 5.0 == 0.0;"));	
		assertTrue(runTest("5.0 / 1.0 == 5.0;"));	
		
		assertTrue(runTest("7 / 2.0 == 3.5;"));	
		assertTrue(runTest("-7.0 / 2 == -3.5;"));
		assertTrue(runTest("7 / -2.0 == -3.5;"));	
		assertTrue(runTest("-7.0 / -2 == 3.5;"));	
		
		assertTrue(runTest("-2.0 <= -1.0;"));
		assertTrue(runTest("-2.0 <= 1.0;"));
		assertTrue(runTest("1.0 <= 2.0;"));
		assertTrue(runTest("2.0 <= 2.0;"));
		assertFalse(runTest("2.0 <= 1.0;"));
		
		assertTrue(runTest("-2 <= -1.0;"));
		assertTrue(runTest("-2.0 <= 1;"));
		assertTrue(runTest("1 <= 2.0;"));
		assertTrue(runTest("2.0 <= 2;"));
		assertFalse(runTest("2 <= 1.0;"));
		
		assertTrue(runTest("-2.0 < -1.0;"));
		assertTrue(runTest("-2.0 < 1.0;"));
		assertTrue(runTest("1.0 < 2.0;"));
		assertFalse(runTest("2.0 < 2.0;"));
		
		assertTrue(runTest("-2 < -1.0;"));
		assertTrue(runTest("-2.0 < 1;"));
		assertTrue(runTest("1 < 2.0;"));
		assertFalse(runTest("2.0 < 2;"));
		
		assertTrue(runTest("-1.0 >= -2.0;"));
		assertTrue(runTest("1.0 >= -1.0;"));
		assertTrue(runTest("2.0 >= 1.0;"));
		assertTrue(runTest("2.0 >= 2.0;"));
		assertFalse(runTest("1.0 >= 2.0;"));
		
		assertTrue(runTest("-1 >= -2.0;"));
		assertTrue(runTest("1.0 >= -1;"));
		assertTrue(runTest("2 >= 1.0;"));
		assertTrue(runTest("2.0 >= 2;"));
		assertFalse(runTest("1 >= 2.0;"));
		
		assertTrue(runTest("-1.0 > -2.0;"));
		assertTrue(runTest("1.0 > -1.0;"));
		assertTrue(runTest("2.0 > 1.0;"));
		assertFalse(runTest("2.0 > 2.0;"));
		assertFalse(runTest("1.0 > 2.0;"));
		
		assertTrue(runTest("-1 > -2.0;"));
		assertTrue(runTest("1.0 > -1;"));
		assertTrue(runTest("2 > 1.0;"));
		assertFalse(runTest("2.0 > 2;"));
		assertFalse(runTest("1 > 2.0;"));
		
		assertTrue(runTest("3.5 > 2.5 ? 3.5 : 2.5 == 3.5;"));
		
		assertTrue(runTest("3.5 > 2 ? 3.5 : 2 == 3.5;"));
		assertTrue(runTest("3.5 > 4 ? 3.5 : 2 == 2;"));
	}
	
	@Test
	public void testString() {
		
		assertTrue(runTest("\"\" == \"\";"));
		assertTrue(runTest("\"abc\" != \"\";"));
		assertTrue(runTest("\"abc\" == \"abc\";"));
		assertTrue(runTest("\"abc\" != \"def\";"));
		
		assertTrue(runTest("\"abc\" + \"\" == \"abc\";"));
		assertTrue(runTest("\"abc\" + \"def\" == \"abcdef\";"));
		
		assertTrue(runTest("\"\" <= \"\";"));
		assertTrue(runTest("\"\" <= \"abc\";"));
		assertTrue(runTest("\"abc\" <= \"abc\";"));
		assertTrue(runTest("\"abc\" <= \"def\";"));
		
		assertFalse(runTest("\"\" < \"\";"));
		assertTrue(runTest("\"\" < \"abc\";"));
		assertFalse(runTest("\"abc\" < \"abc\";"));
		assertTrue(runTest("\"abc\" < \"def\";"));
		
		assertTrue(runTest("\"\" >= \"\";"));
		assertTrue(runTest("\"abc\" >= \"\";"));
		assertTrue(runTest("\"abc\" >= \"abc\";"));
		assertTrue(runTest("\"def\" >= \"abc\";"));
		
		assertFalse(runTest("\"\" > \"\";"));
		assertTrue(runTest("\"abc\" > \"\";"));
		assertFalse(runTest("\"abc\" > \"abc\";"));
		assertTrue(runTest("\"def\" > \"abc\";"));
	}
	

	@Test(expected=TypeError.class)
	public void orError() {
		runTest("3 || true;");
	}
	
	@Test
	public void testLocation() {
		
		//assertTrue(runTest("{area(5,2,6,8,0,0); true;}"));
		//assertTrue(runTest("{file(\"pico1.trm\"); true;}"));
		assertTrue(runTest("{area-in-file(\"pico1.trm\",area(5,2,6,8,0,0)); true;}"));
	}
	
	@Test
	public void testList() 
	{		
		assertTrue(runTest("[] == [];"));
		assertTrue(runTest("[] != [1];"));
		assertTrue(runTest("[1] == [1];"));
		assertTrue(runTest("[1] != [2];"));
		assertTrue(runTest("[1, 2] == [1, 2];"));
		assertTrue(runTest("[1, 2] != [2, 1];"));
		
		assertTrue(runTest("[] + [] == [];"));
		assertTrue(runTest("[1, 2, 3] + [] == [1, 2, 3];"));
		assertTrue(runTest("[] + [1, 2, 3] == [1, 2, 3];"));
		assertTrue(runTest("[1, 2] + [3, 4, 5] == [1, 2, 3, 4, 5];"));	
		
		assertTrue(runTest("([1, 2] + [3, 4]) + [5] == [1, 2, 3, 4, 5];"));	
		assertTrue(runTest("[1, 2] + ([3, 4] + [5]) == [1, 2, 3, 4, 5];"));	
		assertTrue(runTest("[1, 2] + [3, 4] + [5] == [1, 2, 3, 4, 5];"));
		
		assertTrue(runTest("[1, 2] + 3 == [1, 2, 3];"));
		assertTrue(runTest("1 +  [2, 3] == [1, 2, 3];"));
		
		assertTrue(runTest("[1,1,2,2,3,3,4,4,5] - [1,2,4] == [3,3,5];"));
		assertTrue(runTest("[1,2,3,4,5,4,3,2,1] - [1,2,4] == [3,5,3];"));
		
		assertTrue(runTest("[] <= [];"));
		assertTrue(runTest("[] <= [1];"));
		assertTrue(runTest("[2, 1, 0] <= [2, 3];"));
		assertTrue(runTest("[2, 1] <= [2, 3, 0];"));
		assertTrue(runTest("[2, 1] <= [2, 1];"));
		assertTrue(runTest("[2, 1] <= [2, 1, 0];"));
		
		assertTrue(runTest("[] < [1];"));
		assertTrue(runTest("[2, 1, 0] < [2, 3];"));
		assertTrue(runTest("[2, 1] < [2, 3, 0];"));
		assertTrue(runTest("[2, 1] < [2, 1, 0];"));
		
		assertTrue(runTest("[] >= [];"));
		assertTrue(runTest("[1] >= [];"));
		assertTrue(runTest("[2, 3] >= [2, 1, 0];"));
		assertTrue(runTest("[2, 3, 0] >= [2, 1];"));
		assertTrue(runTest("[2, 1] >= [2, 1];"));
		assertTrue(runTest("[2, 1, 0] >= [2, 1];"));
		
		assertTrue(runTest("[1] > [];"));
		assertTrue(runTest("[2, 3] > [2, 1, 0];"));
		assertTrue(runTest("[2, 3, 0] > [2, 1];"));
		assertTrue(runTest("[2, 1, 0] > [2, 1];"));
		
		assertTrue(runTest("[] * [] == [];"));
		assertTrue(runTest("[1] * [9] == [<1,9>];"));
		assertTrue(runTest("[1, 2] * [9] == [<1,9>, <2,9>];"));
		assertTrue(runTest("[1, 2, 3] * [9] == [<1,9>, <2,9>, <3,9>];"));
		assertTrue(runTest("[1, 2, 3] * [9, 10] == [<1,9>, <1,10>, <2,9>, <2,10>, <3,9>, <3,10>];"));
		
		assertTrue(runTest("2 in [1, 2, 3];"));
		assertTrue(runTest("3 notin [2, 4, 6];"));
		
		assertTrue(runTest("2 > 3 ? [1,2] : [1,2,3] == [1,2,3];"));
	}

	@Test(expected=IndexOutOfBoundsError.class)
	public void SubscriptError1() {
		runTest("[1,2][5];");
	}
	
	@Test
	public void testRange() {
		
		assertTrue(runTest("[1 .. 1] == [1];"));
		assertTrue(runTest("[1 .. 2] == [1, 2];"));
		assertTrue(runTest("[1 .. -1] == [1, 0, -1];"));
		assertTrue(runTest("[1, 2 .. 10] == [1,2,3,4,5,6,7,8,9,10];"));
		assertTrue(runTest("[1, 3 .. 10] == [1,3,5,7,9];"));
		assertTrue(runTest("[1, -2 .. 10] == [];"));
		assertTrue(runTest("[1, -3 .. -10] == [1,-3,-7];"));
	}
	
	@Test
	public void testSet()  {
		
		assertTrue(runTest("{} == {};"));
		assertTrue(runTest("{} != {1};"));
		assertTrue(runTest("{1} == {1};"));
		assertTrue(runTest("{1} != {2};"));
		assertTrue(runTest("{1, 2} == {1, 2};"));
		assertTrue(runTest("{1, 2} == {2, 1};"));
		assertTrue(runTest("{1, 2, 3, 1, 2, 3} == {3, 2, 1};"));	
		
		assertTrue(runTest("{1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};"));
		assertTrue(runTest("{1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {10, 2, 3, 4, 5, 6, 7, 8, 9, 1};"));
		assertTrue(runTest("{1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {10, 9, 3, 4, 5, 6, 7, 8, 2, 1};"));
		assertTrue(runTest("{1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {10, 9, 7, 4, 5, 6, 3, 8, 2, 1};"));
		assertTrue(runTest("{1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {10, 9, 7, 6, 5, 4, 3, 8, 2, 1};"));
		
		assertTrue(runTest("{{1}, {2}} == {{2}, {1}};"));
		assertTrue(runTest("{{}} == {{}};"));
		assertTrue(runTest("{{}, {}} == {{}};"));
		assertTrue(runTest("{{}, {}, {}} == {{}};"));
		
		assertTrue(runTest("{{1, 2}, {3,4}} == {{2,1}, {4,3}};"));	
	
		assertTrue(runTest("{} + {} == {};"));
		assertTrue(runTest("{1, 2, 3} + {} == {1, 2, 3};"));
		assertTrue(runTest("{} + {1, 2, 3} == {1, 2, 3};"));
		assertTrue(runTest("{1, 2} + {3, 4, 5} == {1, 2, 3, 4, 5};"));	
		assertTrue(runTest("{1, 2, 3, 4} + {3, 4, 5} == {1, 2, 3, 4, 5};"));
		assertTrue(runTest("{{1, 2}, {3,4}} + {{5,6}} == {{1,2},{3,4},{5,6}};"));
		assertTrue(runTest("1 + {2,3} == {1,2,3};"));
		assertTrue(runTest("{1,2} + 3 == {1,2,3};"));
		
		assertTrue(runTest("{} - {} == {};"));
		assertTrue(runTest("{1, 2, 3} - {} == {1, 2, 3};"));
		assertTrue(runTest("{} - {1, 2, 3} == {};"));
		assertTrue(runTest("{1, 2, 3} - {3, 4, 5} == {1, 2};"));	
		assertTrue(runTest("{1, 2, 3, 4} - {1, 2, 3, 4, 5} == {};"));
		assertTrue(runTest("{{1, 2}, {3,4}, {5,6}} - {{3,4}} == {{1,2}, {5,6}};"));
		assertTrue(runTest("{1,2,3} - 3 == {1,2};"));
		
		assertTrue(runTest("{} & {} == {};"));
		assertTrue(runTest("{1, 2, 3} & {} == {};"));
		assertTrue(runTest("{} & {1, 2, 3} == {};"));
		assertTrue(runTest("{1, 2, 3} & {3, 4, 5} == {3};"));	
		assertTrue(runTest("{1, 2, 3, 4} & {3, 4, 5} == {3, 4};"));	
		assertTrue(runTest("{{1,2},{3,4},{5,6}} & {{2,1}, {8,7}, {6,5}} == {{1,2},{5,6}};"));
		
		assertTrue(runTest("{} <= {};"));
		assertTrue(runTest("{} <= {1};"));
		assertTrue(runTest("{2, 1} <= {1, 2};"));
		assertTrue(runTest("{2, 1} <= {1, 2, 3};"));
		assertTrue(runTest("{2, 1} <= {2, 1, 0};"));
	
		assertTrue(runTest("{} < {1};"));
		assertTrue(runTest("{2, 1} < {2, 1, 3};"));
	
		assertTrue(runTest("{} >= {};"));
		assertTrue(runTest("{1} >= {};"));
		assertTrue(runTest("{2, 3} >= {2};"));
	
		assertTrue(runTest("{1} > {};"));
		assertTrue(runTest("{2, 1, 3} > {2, 3};"));
		
		assertTrue(runTest("{} * {} == {};"));
		assertTrue(runTest("{1} * {9} == {<1,9>};"));
		assertTrue(runTest("{1, 2} * {9} == {<1,9>, <2,9>};"));
		assertTrue(runTest("{1, 2, 3} * {9} == {<1,9>, <2,9>, <3,9>};"));
		assertTrue(runTest("{1, 2, 3} * {9, 10} == {<1,9>, <1,10>, <2,9>, <2,10>, <3,9>, <3,10>};"));
		
		assertTrue(runTest("2 in {1, 2, 3};"));
		assertTrue(runTest("{4,3} in {{1, 2}, {3,4}, {5,6}};"));
		
		assertTrue(runTest("5 notin {1, 2, 3};"));
		assertTrue(runTest("{7,8} notin {{1, 2}, {3,4}, {5,6}};"));
		
		assertTrue(runTest("3 > 2 ? {1,2} : {1,2,3} == {1,2};"));
		
		assertTrue(runTest("{<\"a\", [1,2]>, <\"b\", []>, <\"c\", [4,5,6]>} != {};"));
		
	}
	

	@Test(expected=TypeError.class)
	public void inError() {
		runTest("1 in 3;");
	}
	
	@Test
	public void testMap()  {
		
		assertTrue(runTest("() == ();"));
		assertTrue(runTest("(1:10) != ();"));
		assertTrue(runTest("(1:10) == (1:10);"));
		assertTrue(runTest("(1:10) != (2:20);"));
		
		assertTrue(runTest("() + () == ();"));
		assertTrue(runTest("(1:10) + () == (1:10);"));
		assertTrue(runTest("(1:10) + (2:20) == (1:10, 2:20);"));
		assertTrue(runTest("(1:10, 2:20) + (2:25) == (1:10, 2:25);"));
		
		assertTrue(runTest("() - () == ();"));
		assertTrue(runTest("(1:10, 2:20) - () == (1:10,2:20);"));
		assertTrue(runTest("(1:10, 2:20) - (2:20) == (1:10);"));
		assertTrue(runTest("(1:10, 2:20) - (2:25) == (1:10);")); // This is current behaviour; is this ok?
	
		assertTrue(runTest("() & () == ();"));
		assertTrue(runTest("(1:10) & () == ();"));
		assertTrue(runTest("(1:10, 2:20, 3:30, 4:40) & (2:20, 4:40, 5:50) == (2:20, 4:40);"));
		assertTrue(runTest("(1:10, 2:20, 3:30, 4:40) & (5:50, 6:60) == ();"));
		
		assertTrue(runTest("() <= ();"));
		assertTrue(runTest("() <= (1:10);"));
		assertTrue(runTest("(1:10) <= (1:10);"));
		assertTrue(runTest("(1:10) <= (1:10, 2:20);"));
		
		assertFalse(runTest("() < ();"));
		assertTrue(runTest("() < (1:10);"));
		assertFalse(runTest("(1:10) < (1:10);"));
		assertTrue(runTest("(1:10) < (1:10, 2:20);"));
		
		assertTrue(runTest("() >= ();"));
		assertTrue(runTest("(1:10) >= ();"));
		assertTrue(runTest("(1:10) >= (1:10);"));
		assertTrue(runTest("(1:10, 2:20) >= (1:10);"));
		
		assertFalse(runTest("() > ();"));
		assertTrue(runTest("(1:10) > ();"));
		assertFalse(runTest("(1:10) > (1:10);"));
		assertTrue(runTest("(1:10, 2:20) > (1:10);"));
		
		
		assertTrue(runTest("20 in (1:10, 2:20);"));
		assertFalse(runTest("15 in (1:10, 2:20);"));
		
		assertTrue(runTest("15 notin (1:10, 2:20);"));
		assertFalse(runTest("20 notin (1:10, 2:20);"));
		
		assertTrue(runTest("{map[str,list[int]] m = (\"a\": [1,2], \"b\": [], \"c\": [4,5,6]); m[\"a\"] == [1,2];}"));
	}
	
	@Test
	public void testTuple() {
		
		assertTrue(runTest("<1, 2.5, true> == <1, 2.5, true>;"));
		assertTrue(runTest("<1, 2.5, true> != <0, 2.5, true>;"));
		assertTrue(runTest("<{1,2}, 3> == <{2,1}, 3>;"));
		assertTrue(runTest("<1, {2,3}> == <1, {3,2}>;"));
		assertTrue(runTest("<{1,2}, {3,4}> == <{2,1},{4,3}>;"));
		
		assertTrue(runTest("<1>           >= <1>;"));
		assertTrue(runTest("<2>           >= <1>;"));
		assertTrue(runTest("<1,2>         >= <1>;"));
		assertTrue(runTest("<1,2>         >= <1,2>;"));
		assertTrue(runTest("<1,2>         >= <1, 1>;"));
		assertTrue(runTest("<1,\"def\">   >= <1, \"abc\">;"));
		assertTrue(runTest("<1, [2,3,4]>  >= <1, [2,3]>;"));
		assertTrue(runTest("<1, [2,3]>    >= <1, [2,3]>;"));
		
		assertFalse(runTest("<1>          > <1>;"));
		assertTrue(runTest("<2>           > <1>;"));
		assertTrue(runTest("<1,2>         > <1>;"));
		assertFalse(runTest("<1,2>        > <1,2>;"));
		assertTrue(runTest("<1,2>         > <1, 1>;"));
		assertTrue(runTest("<1,\"def\">   > <1, \"abc\">;"));
		assertTrue(runTest("<1, [2,3,4]>  > <1, [2,3]>;"));
		assertFalse(runTest("<1, [2,3]>   > <1, [2,3]>;"));
		
		assertTrue(runTest("<1>           <= <1>;"));
		assertTrue(runTest("<1>           <= <2>;"));
		assertTrue(runTest("<1>           <= <1,2>;"));
		assertTrue(runTest("<1,2>         <= <1,2>;"));
		assertTrue(runTest("<1,1>         <= <1, 2>;"));
		assertTrue(runTest("<1,\"abc\">   <= <1, \"def\">;"));
		assertTrue(runTest("<1, [2,3]>    <= <1, [2,3,4]>;"));
		assertTrue(runTest("<1, [2,3]>    <= <1, [2,3]>;"));
		
		assertFalse(runTest("<1>          < <1>;"));
		assertTrue(runTest("<1>           < <2>;"));
		assertTrue(runTest("<1>           < <1,2>;"));
		assertFalse(runTest("<1,2>        < <1,2>;"));
		assertTrue(runTest("<1,1>         < <1, 2>;"));
		assertTrue(runTest("<1,\"abc\">   < <1, \"def\">;"));
		assertTrue(runTest("<1, [2,3]>    < <1, [2,3,4]>;"));
		assertFalse(runTest("<1, [2,3]>   < <1, [2,3]>;"));
		
		assertTrue(runTest("<1, \"a\", true> + <1.5, \"def\"> == <1, \"a\", true> + <1.5, \"def\">;"));
	}
	
	@Test
	public void namedTuple()  {
		
		assertTrue(runTest("{tuple[int key, str val] T = <1, \"abc\">; T.key == 1;}"));
		assertTrue(runTest("{tuple[int key, str val] T = <1, \"abc\">; T.val == \"abc\";}"));
		
		
	}
	
	@Ignore @Test(expected=NoSuchFieldError.class)
	public void tupleError1(){
		runTest("{tuple[int key, str val] T = <1, \"abc\">; T.zip == \"abc\";}");
	}
	

	@Test
	public void testRelation()  {
		
		assertTrue(runTest("{} == {};"));
		assertTrue(runTest("{<1,10>} == {<1,10>};"));
		assertTrue(runTest("{<1,2,3>} == {<1,2,3>};"));
		assertTrue(runTest("{<1,10>, <2,20>} == {<1,10>, <2,20>};"));
		assertTrue(runTest("{<1,10>, <2,20>, <3,30>} == {<1,10>, <2,20>, <3,30>};"));
		assertTrue(runTest("{<1,2,3>, <4,5,6>} == {<4,5,6>, <1,2,3>};"));
		assertTrue(runTest("{<1,2,3,4>, <4,5,6,7>} == {<4,5,6,7>, <1,2,3,4>};"));
		
		assertTrue(runTest("{} != {<1,2>, <3,4>};"));
		assertFalse(runTest("{<1,2>, <3,4>} == {};"));
		
		assertTrue(runTest("{<1, {1,2,3}>, <2, {2,3,4}>} ==  {<1, {1,2,3}>, <2, {2,3,4}>};"));
		assertTrue(runTest("{<1, {1,2,3}>, <2, {2,3,4}>} ==  {<2, {2,3,4}>, <1, {1,2,3}>};"));
		assertTrue(runTest("{<1, {1,2,3}>, <2, {2,3,4}>} ==  {<2, {4,3,2}>, <1, {2,1,3}>};"));
		
		assertTrue(runTest("{<1,10>} + {} == {<1,10>};"));
		assertTrue(runTest("{} + {<1,10>}  == {<1,10>};"));
		assertTrue(runTest("{<1,10>} + {<2,20>} == {<1,10>, <2,20>};"));
		assertTrue(runTest("{<1,10>, <2,20>} + {<3,30>} == {<1,10>, <2,20>, <3,30>};"));
		assertTrue(runTest("{<1,10>, <2,20>} + {<2,20>, <3,30>} == {<1,10>, <2,20>, <3,30>};"));
		
		assertTrue(runTest("{<1,10>} - {} == {<1,10>};"));
		assertTrue(runTest("{} - {<1,10>}  == {};"));
		assertTrue(runTest("{<1,10>, <2,20>} - {<2,20>, <3,30>} == {<1,10>};"));
		
		assertTrue(runTest("{<1,10>} & {} == {};"));
		assertTrue(runTest("{} & {<1,10>}  == {};"));
		assertTrue(runTest("{<1,10>, <2,20>} & {<2,20>, <3,30>} == {<2,20>};"));
		assertTrue(runTest("{<1,2,3,4>, <2,3,4,5>} & {<2,3,4,5>,<3,4,5,6>} == {<2,3,4,5>};"));
		
		assertTrue(runTest("<2,20> in {<1,10>, <2,20>, <3,30>};"));
		assertTrue(runTest("<1,2,3> in {<1,2,3>, <4,5,6>};"));
		
		assertTrue(runTest("<4,40> notin {<1,10>, <2,20>, <3,30>};"));
		assertTrue(runTest("<1,2,4> notin {<1,2,3>, <4,5,6>};"));
		
		assertTrue(runTest("{} o {} == {};"));
		assertTrue(runTest("{<1,10>,<2,20>} o {} == {};"));
		assertTrue(runTest("{} o {<10,100>, <20,200>} == {};"));
		assertTrue(runTest("{<1,10>,<2,20>} o {<10,100>, <20,200>} == {<1,100>, <2,200>};"));
		
		assertTrue(runTest("{<1, \"a\">, <2, \"b\">} * {<false, 0>, <true, 1>} == {<1,\"a\",false,0>,<2,\"b\",false,0>,<1,\"a\",true,1>,<2,\"b\",true,1>};"));
		
		assertTrue(runTest("{<1,2>, <2,3>, <3,4>} + == {<1,2>, <2,3>, <3,4>, <1, 3>, <2, 4>, <1, 4>};"));
		
		assertTrue(runTest("{<1,2>, <2,3>, <3,4>} * == {<1,2>, <2,3>, <3,4>, <1, 3>, <2, 4>, <1, 4>, <1, 1>, <2, 2>, <3, 3>, <4, 4>};"));
		
		assertTrue(runTest("{<1,2>, <2,3>, <3,4>, <4,2>, <4,5>}+ ==	{<1,2>, <2,3>, <3,4>, <4,2>, <4,5>, <1, 3>, <2, 4>, <3, 2>, <3, 5>, <4, 3>, <1, 4>, <2, 2>, <2, 5>, <3, 3>, <4, 4>, <1, 5>};"));
		
		assertTrue(runTest("{<1,2>, <2,3>, <3,4>, <4,2>, <4,5>}* == {<1,2>, <2,3>, <3,4>, <4,2>, <4,5>, <1, 3>, <2, 4>, <3, 2>, <3, 5>, <4, 3>, <1, 4>, <2, 2>, <2, 5>, <3, 3>, <4, 4>, <1, 5>, <1, 1>, <5, 5>};"));
	}
	

	
	@Test(expected=TypeError.class)
	public void compError() {
		runTest("1 o 3;");
	}

	
	@Test(expected=TypeError.class)
	public void closError1() {
		runTest("1*;");
	}
	
	@Test(expected=TypeError.class)
	public void closError2() {
		runTest("1+;");
	}
	
	@Test
	public void namedRelation1() {
		
		assertTrue(runTest("{rel[int from, int to] R = {<1,10>, <2,20>}; R.from == {1,2};}"));
		assertTrue(runTest("{rel[int from, int to] R = {<1,10>, <2,20>}; R.to == {10,20};}"));
	}
	@Test(expected=NoSuchFieldError.class)
	public void namedRelationError(){
		runTest("{rel[int from, int to] R = {<1,10>, <2,20>}; R.zip == {10,20};}");
	}
	
	@Test
	public void good()  {
		prepare("data NODE = val(value V) | f | f(NODE a);");
		
		assertTrue(runTestInSameEvaluator("f(val(1)) == f(val(1));"));
	}
	
	@Test
	public void node()  {
		prepare("data NODE = i(int I) | s(str x)  | st(set[NODE] s) | l(list[NODE]) | m(map[NODE,NODE] m) | f | f(NODE a) | f(NODE a, NODE b) | g | g(NODE a) | g(NODE a,NODE b);");
		
		assertTrue(runTestInSameEvaluator("f() == f();"));
		assertTrue(runTestInSameEvaluator("f() != g();"));
		assertTrue(runTestInSameEvaluator("{NODE n = f(); NODE m = g(); n != m;}"));
		assertTrue(runTestInSameEvaluator("f(i(1)) == f(i(1));"));
		assertTrue(runTestInSameEvaluator("f(i(1)) != g(i(1));"));
		assertTrue(runTestInSameEvaluator("{NODE n = f(i(1)); NODE m = g(i(1)); n != m;}"));
		assertTrue(runTestInSameEvaluator("f(i(1),i(2)) == f(i(1),i(2));"));
		assertTrue(runTestInSameEvaluator("f(i(1),i(2)) != f(i(1),i(3));"));
		assertTrue(runTestInSameEvaluator("{ NODE n = f(i(1),i(2)); NODE m = f(i(1),i(3)); n != m;}"));
		assertTrue(runTestInSameEvaluator("f(i(1),g(i(2),i(3))) == f(i(1),g(i(2),i(3)));"));
		assertTrue(runTestInSameEvaluator("f(i(1),g(i(2),i(3))) != f(i(1),g(i(2),i(4)));"));
		assertTrue(runTestInSameEvaluator("{NODE n = f(i(1),g(i(2),i(3))); NODE m = f(i(1),g(i(2),i(4))); n != m;}"));
		assertTrue(runTestInSameEvaluator("f(i(1),g(i(2),st({i(3),i(4),i(5)}))) == f(i(1),g(i(2),st({i(3),i(4),i(5)})));"));
		assertTrue(runTestInSameEvaluator("{ NODE n = f(i(1),g(i(2),st({i(3),i(4),i(5)}))); NODE m = f(i(1),g(i(2),st({i(3),i(4),i(5),i(6)}))); n != m;}"));
		assertTrue(runTestInSameEvaluator("f(i(1),g(i(2),l([i(3),i(4),i(5)]))) == f(i(1),g(i(2),l([i(3),i(4),i(5)])));"));
		assertTrue(runTestInSameEvaluator("{ NODE n = f(i(1),g(i(2),l([i(3),i(4),i(5)]))); NODE m = f(i(1),g(i(2),l([i(3),i(4),i(5),i(6)]))); n != m;}"));
		assertTrue(runTestInSameEvaluator("f(i(1),g(i(2),m((i(3):i(3),i(4):i(4),i(5):i(5))))) == f(i(1),g(i(2),m((i(3):i(3),i(4):i(4),i(5):i(5)))));"));
		assertTrue(runTestInSameEvaluator("{NODE n = f(i(1),g(i(2),m((i(3):i(3),i(4):i(4),i(5):i(5))))); NODE m = f(i(1),g(i(2),m((i(3):i(3),i(4):i(4),i(5):i(0))))); n != m;}"));
		
		assertTrue(runTestInSameEvaluator("f()                       <= f();"));
		assertTrue(runTestInSameEvaluator("f()                       <= g();"));
		assertTrue(runTestInSameEvaluator("f()                       <= f(i(1));"));
		assertTrue(runTestInSameEvaluator("f(i(1))                   <= f(i(1));"));
		assertTrue(runTestInSameEvaluator("f(i(1), i(2))             <= f(i(1), i(3));"));
		assertTrue(runTestInSameEvaluator("f(i(1), i(2))             <= g(i(1), i(3));"));
		assertTrue(runTestInSameEvaluator("f(i(1), s(\"abc\"))       <= f(i(1), s(\"def\"));"));
		assertTrue(runTestInSameEvaluator("f(i(1), l([i(2), i(3)]))  <= f(i(1), l([i(2),i(3),i(4)]));"));
		assertTrue(runTestInSameEvaluator("f(i(1), l([i(2), i(3)]))  <= f(i(1), l([i(2),i(3)]));"));
		
		assertFalse(runTestInSameEvaluator("f()                      < f();"));
		assertTrue(runTestInSameEvaluator("f()                       < g();"));
		assertTrue(runTestInSameEvaluator("f()                       < f(i(1));"));
		assertFalse(runTestInSameEvaluator("f(i(1))                  < f(i(1));"));
		assertTrue(runTestInSameEvaluator("f(i(1), i(2))             < f(i(1), i(3));"));
		assertTrue(runTestInSameEvaluator("f(i(1), i(2))             < g(i(1), i(3));"));
		assertTrue(runTestInSameEvaluator("f(i(1), s(\"abc\"))       < f(i(1), s(\"def\"));"));
		assertTrue(runTestInSameEvaluator("f(i(1), l([i(2), i(3)]))  < f(i(1), l([i(2),i(3),i(4)]));"));
		assertFalse(runTestInSameEvaluator("f(i(1), l([i(2), i(3)])) < f(i(1), l([i(2),i(3)]));"));
		
		assertTrue(runTestInSameEvaluator("f()                          >= f();"));
		assertTrue(runTestInSameEvaluator("g()                          >= f();"));
		assertTrue(runTestInSameEvaluator("f(i(1))                      >= f();"));
		assertTrue(runTestInSameEvaluator("f(i(1))                      >= f(i(1));"));
		assertTrue(runTestInSameEvaluator("f(i(1), i(3))                >= f(i(1), i(2));"));
		assertTrue(runTestInSameEvaluator("g(i(1), i(2))                >= f(i(1), i(3));"));
		assertTrue(runTestInSameEvaluator("f(i(1), s(\"def\"))          >= f(i(1), s(\"abc\"));"));
		assertTrue(runTestInSameEvaluator("f(i(1), l([i(2),i(3),i(4)])) >= f(i(1), l([i(2),i(3)]));"));
		assertTrue(runTestInSameEvaluator("f(i(1), l([i(2), i(3)]))     >= f(i(1), l([i(2),i(3)]));"));
		
		assertFalse(runTestInSameEvaluator("f()                         > f();"));
		assertTrue(runTestInSameEvaluator("g()                          > f();"));
		assertTrue(runTestInSameEvaluator("f(i(1))                      > f();"));
		assertFalse(runTestInSameEvaluator("f(i(1))                     > f(i(1));"));
		assertTrue(runTestInSameEvaluator("f(i(1), i(3))                > f(i(1), i(2));"));
		assertTrue(runTestInSameEvaluator("g(i(1), i(2))                > f(i(1), i(3));"));
		assertTrue(runTestInSameEvaluator("f(i(1), s(\"def\"))          > f(i(1), s(\"abc\"));"));
		assertTrue(runTestInSameEvaluator("f(i(1), l([i(2),i(3),i(4)])) > f(i(1), l([i(2),i(3)]));"));
		assertFalse(runTestInSameEvaluator("f(i(1), l([i(2), i(3)]))    > f(i(1), l([i(2),i(3)]));"));
	}
	
	@Test
	public void undefined()  {
		
		assertTrue(runTest("1 =? 13 == 1;"));
		assertTrue(runTest("x =? 13 == 13;"));
		assertTrue(runTest("{ x = 3; x =? 13 == 3; }"));
	}
}
