package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.control_exceptions.Throw;
import org.meta_environment.rascal.interpreter.staticErrors.StaticError;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredFieldError;
import org.meta_environment.rascal.interpreter.staticErrors.UninitializedVariableError;


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
	

	@Test(expected=StaticError.class)
	public void andError() {
		runTest("3 && true;");
	}
	
	@Test(expected=StaticError.class)
	public void impError() {
		runTest("3 ==> true;");
	}
	

	@Test(expected=StaticError.class)
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
	

	@Test(expected=StaticError.class)
	public void addError() {
		runTest("3 + true;");
	}
	

	@Test(expected=StaticError.class)
	public void subError() {
		runTest("3 - true;");
	}
	
	@Test(expected=StaticError.class)
	public void uMinusError() {
		runTest("- true;");
	}
	
	@Test(expected=StaticError.class)
	public void timesError() {
		runTest("3 * true;");
	}
	
	@Test(expected=StaticError.class)
	public void divError() {
		runTest("3 / true;");
	}
	
	@Test(expected=StaticError.class)
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
	

	@Test(expected=StaticError.class)
	public void orError() {
		runTest("3 || true;");
	}
	
	@Test
	public void testLocation() {
		String Loc = "loc(file:/home/paulk/pico.trm?offset=0&length=1&begin=2,3&end=4,5)";
		String Loc2 = "loc(file:/home/paulk/pico2.trm?offset=0&length=1&begin=2,3&end=4,5)";
		
		assertTrue(runTest("{" + Loc + "; true;}"));
		assertTrue(runTest(Loc + " == " + Loc + ";"));
		assertFalse(runTest(Loc + " == " + Loc2 + ";"));
		
		assertTrue(runTest(Loc + ".url == \"file:/home/paulk/pico.trm\";"));
		assertTrue(runTest(Loc + ".offset == 0;"));
		assertTrue(runTest(Loc + ".length == 1;"));
		assertTrue(runTest(Loc + ".beginLine == 2;"));
		assertTrue(runTest(Loc + ".beginColumn == 3;"));
		assertTrue(runTest(Loc + ".endLine == 4;"));
		assertTrue(runTest(Loc + ".endColumn == 5;"));
		
		
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.url == \"file:/home/paulk/pico.trm\";}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.offset == 0;}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.length == 1;}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.beginLine == 2;}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.beginColumn == 3;}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.endLine == 4;}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.endColumn == 5;}"));
		
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.url = \"file:/home/paulk/pico2.trm\"; Loc.url == \"file:/home/paulk/pico2.trm\";}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.offset = 10; Loc.offset == 10;}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.length = 11; Loc.length == 11;}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.endLine = 14; Loc.endLine == 14;}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.beginLine = 1; Loc.beginLine == 1;}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.beginColumn = 13; Loc.beginColumn == 13;}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.endColumn = 15; Loc.endColumn == 15;}"));
		
		assertTrue(runTest("{loc Loc = " + Loc + "; Loc = Loc[url= \"file:/home/paulk/pico2.trm\"]; Loc == loc(file:/home/paulk/pico2.trm?offset=0&length=1&begin=2,3&end=4,5);}"));
		assertTrue(runTest("{loc Loc = " + Loc + "; Loc = Loc[offset = 10]; Loc == loc(file:/home/paulk/pico.trm?offset=10&length=1&begin=2,3&end=4,5);}"));
		assertTrue(runTest("{loc Loc = " + Loc + "; Loc = Loc[length = 11]; Loc ==  loc(file:/home/paulk/pico.trm?offset=0&length=11&begin=2,3&end=4,5);}"));
		assertTrue(runTest("{loc Loc = " + Loc + "; Loc = Loc[beginLine = 1]; Loc == loc(file:/home/paulk/pico.trm?offset=0&length=1&begin=1,3&end=4,5);}"));
		assertTrue(runTest("{loc Loc = " + Loc + "; Loc = Loc[beginColumn = 13]; Loc  == loc(file:/home/paulk/pico.trm?offset=0&length=1&begin=2,13&end=4,5);}"));
		assertTrue(runTest("{loc Loc = " + Loc + "; Loc = Loc[endLine = 14]; Loc ==  loc(file:/home/paulk/pico.trm?offset=0&length=1&begin=2,3&end=14,5);}"));
		assertTrue(runTest("{loc Loc = " + Loc + "; Loc = Loc[endColumn = 15]; Loc == loc(file:/home/paulk/pico.trm?offset=0&length=1&begin=2,3&end=4,15);}"));
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void UndefinedLocationError1(){
		runTest("{ loc Loc; Loc.url;}");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void UndefinedLocationError2(){
		runTest("{ loc Loc; Loc.url = \"abc\";}");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void UndefinedLocationError3(){
		runTest("{ loc Loc; Loc[url = \"abc\"];}");
	}
	
	@Test(expected=StaticError.class)
	public void WrongLocFieldError1(){
		String Loc = "loc(file:/home/paulk/pico.trm?offset=0&length=1&begin=2,3&end=4,5)";
		runTest("{loc Loc = " + Loc + "; Loc.bla;}");
	}
	
	@Test(expected=StaticError.class)
	public void WrongLocFieldError2(){
		String Loc = "loc(file:/home/paulk/pico.trm?offset=0&length=1&begin=2,3&end=4,5)";
		runTest(Loc + "[bla=3];");
	}
	
	@Test(expected=StaticError.class)
	public void URLFieldError1(){
		String Loc = "loc(file:/home/paulk/pico.trm?offset=0&length=1&begin=2,3&end=4,5)";
		runTest("{loc Loc = " + Loc + "; Loc.url=true;}");
	}
	
	@Test(expected=StaticError.class)
	public void URLFieldError2(){
		String Loc = "loc(file:/home/paulk/pico.trm?offset=0&length=1&begin=2,3&end=4,5)";
		runTest("{loc Loc = " + Loc + "; Loc.url=\"???\";}");
	}
	
	@Test(expected=StaticError.class)
	public void LengthFieldError(){
		String Loc = "loc(file:/home/paulk/pico.trm?offset=0&length=1&begin=2,3&end=4,5)";
		runTest("{loc Loc = " + Loc + "; Loc.length=true;}");
	}
	
	@Test(expected=StaticError.class)
	public void OffsetFieldError(){
		String Loc = "loc(file:/home/paulk/pico.trm?offset=0&length=1&begin=2,3&end=4,5)";
		runTest("{loc Loc = " + Loc + "; Loc.offset=true;}");
	}
	
	@Test(expected=StaticError.class)
	public void BeginLineFieldError(){
		String Loc = "loc(file:/home/paulk/pico.trm?offset=0&length=1&begin=2,3&end=4,5)";
		runTest("{loc Loc = " + Loc + "; Loc.beginLine=true;}");
	}
	@Test(expected=StaticError.class)
	public void EndLineFieldError(){
		String Loc = "loc(file:/home/paulk/pico.trm?offset=0&length=1&begin=2,3&end=4,5)";
		runTest("{loc Loc = " + Loc + "; Loc.endLine=true;}");
	}
	
	@Test(expected=StaticError.class)
	public void BeginColumnFieldError(){
		String Loc = "loc(file:/home/paulk/pico.trm?offset=0&length=1&begin=2,3&end=4,5)";
		runTest("{loc Loc = " + Loc + "; Loc.beginColumn=true;}");
	}
	@Test(expected=StaticError.class)
	public void EndColumnFieldError(){
		String Loc = "loc(file:/home/paulk/pico.trm?offset=0&length=1&begin=2,3&end=4,5)";
		runTest("{loc Loc = " + Loc + "; Loc.endColumn=true;}");
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

	@Test(expected=Throw.class)
	public void SubscriptError1() {
		runTest("[1,2][5];");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void SubscriptError2() {
		runTest("L[5];");
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
	
    @Test(expected=UninitializedVariableError.class)
    public void UndefinedSetElementError(){
    	runTest("{X};");
    }
    
    @Test(expected=StaticError.class)
	public void inError() {
		runTest("1 in 3;");
	}
    
    @Test(expected=StaticError.class)
	public void addSetError() {
		runTest("{1,2,3} + true;");
	}
    
	@Test(expected=StaticError.class)
	public void productError() {
		runTest("{1,2,3} * true;");
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
	
	 @Test(expected=UninitializedVariableError.class)
	    public void UndefinedMapElementError1(){
	    	runTest("(X:2);");
	    }
	 
	 @Test(expected=UninitializedVariableError.class)
	    public void UndefinedMapElementError2(){
	    	runTest("(1:Y);");
	    }
	 
	 @Test(expected=Throw.class)
	 public void NoKeyError(){
		 runTest("(1:10, 2:20)[3];");
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
	
	 @Test(expected=UninitializedVariableError.class)
	    public void UndefinedTupleElementError1(){
	    	runTest("<1,X,3>;");
	    }
	
	@Test
	public void namedTuple()  {
		
		assertTrue(runTest("{tuple[int key, str val] T = <1, \"abc\">; T.key == 1;}"));
		assertTrue(runTest("{tuple[int key, str val] T = <1, \"abc\">; T.val == \"abc\";}"));
		
		
	}
	
	@Test(expected=UndeclaredFieldError.class)
	public void tupleError1(){
		runTest("{tuple[int key, str val] T = <1, \"abc\">; T.zip == \"abc\";}");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void tupleError2(){
		runTest("{tuple[int key, str val] T; T.key;}");
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
		
		assertTrue(runTest("{} + == {};"));
		assertTrue(runTest("{} * == {};"));
		
		assertTrue(runTest("{<1,2>, <2,3>, <3,4>} + == {<1,2>, <2,3>, <3,4>, <1, 3>, <2, 4>, <1, 4>};"));
		
		assertTrue(runTest("{<1,2>, <2,3>, <3,4>} * == {<1,2>, <2,3>, <3,4>, <1, 3>, <2, 4>, <1, 4>, <1, 1>, <2, 2>, <3, 3>, <4, 4>};"));
		
		assertTrue(runTest("{<1,2>, <2,3>, <3,4>, <4,2>, <4,5>}+ ==	{<1,2>, <2,3>, <3,4>, <4,2>, <4,5>, <1, 3>, <2, 4>, <3, 2>, <3, 5>, <4, 3>, <1, 4>, <2, 2>, <2, 5>, <3, 3>, <4, 4>, <1, 5>};"));
		
		assertTrue(runTest("{<1,2>, <2,3>, <3,4>, <4,2>, <4,5>}* == {<1,2>, <2,3>, <3,4>, <4,2>, <4,5>, <1, 3>, <2, 4>, <3, 2>, <3, 5>, <4, 3>, <1, 4>, <2, 2>, <2, 5>, <3, 3>, <4, 4>, <1, 5>, <1, 1>, <5, 5>};"));
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void UndeRelationElementError1(){
		runTest("{<1,10>, <X,20>};");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void UndefinedRelationElementError2(){
		runTest("{<1,10>, <10, Y>};");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void UndefinedRelationElementError3(){
		runTest("{<1,10>, T, <3,30>};");
	}

	
	@Test(expected=StaticError.class)
	public void compError() {
		runTest("1 o 3;");
	}

	
	@Test(expected=StaticError.class)
	public void closError1() {
		runTest("1*;");
	}
	
	@Test(expected=StaticError.class)
	public void closError2() {
		runTest("1+;");
	}
	
	@Test
	public void namedRelation1() {
		
		assertTrue(runTest("{rel[int from, int to] R = {<1,10>, <2,20>}; R.from == {1,2};}"));
		assertTrue(runTest("{rel[int from, int to] R = {<1,10>, <2,20>}; R.to == {10,20};}"));
	}
	@Test(expected=UndeclaredFieldError.class)
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
	
	@Test(expected=UninitializedVariableError.class)
	public void UndefinedDataTypeAccess1(){
		prepare("data D = d(int ival);");
		runTestInSameEvaluator("{D someD; someD.ival;}");
	}
	
	@Test(expected=UninitializedVariableError.class)
	public void UndefinedDataTypeAccess2(){
		prepare("data D = d(int ival);");
		runTestInSameEvaluator("{D someD; someD.ival = 3;}");
	}
	
	@Test
	public void undefined()  {
		
		assertTrue(runTest("{T = (1:10); T[1] ? 13 == 10;}"));
		assertTrue(runTest("{T = (1:10); T[2] ? 13 == 13;}"));
		
		assertTrue(runTest("{T = (1:10); T[1] ? == true;}"));
		assertTrue(runTest("{T = (1:10); T[2] ? == false;}"));
	
	}
}
