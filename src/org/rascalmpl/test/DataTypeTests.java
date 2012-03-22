/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredFieldError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredVariableError;
import org.rascalmpl.interpreter.staticErrors.UninitializedVariableError;


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
	public void valueEquals() {
		assertTrue(runTest("{value x = 1.0; value y = 2; x != y; }"));
	}
	
	@Test
	public void real()
	{		
		assertTrue(runTest("1.0 == 1.0;"));
		assertTrue(runTest("1.0 != 2.0;"));
		
		assertTrue(runTest("-1.0 == -1.0;"));
		assertTrue(runTest("-1.0 != 1.0;"));
		
		assertTrue(runTest("1.0 == 1;"));
		assertTrue(runTest("1.00 == 1.0;"));
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
		
		assertTrue(runTest("2.0 * 3.0 == 6.00;"));	
		assertTrue(runTest("-2.0 * 3.0 == -6.00;"));	
		assertTrue(runTest("2.0 * (-3.0) == -6.00;"));
		assertTrue(runTest("-2.0 * (-3.0) == 6.00;"));	
		
		assertTrue(runTest("2.0 * 3 == 6.0;"));	
		assertTrue(runTest("-2 * 3.0 == -6.0;"));	
		assertTrue(runTest("2.0 * (-3) == -6.0;"));
		assertTrue(runTest("-2 * (-3.0) == 6.0;"));	
		
		assertTrue(runTest("8.0 / 4.0 == 2e0;"));	
		assertTrue(runTest("-8.0 / 4.0 == -2e0;"));
		assertTrue(runTest("8.0 / -4.0 == -2e0;"));	
		assertTrue(runTest("-8.0 / -4.0 == 2e0;"));
		
		// TODO, I don't get it, why does the previous have 1 digit precision and this
		// one two digits
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
		
		assertTrue(runTest("((3.5 > 2.5) ? 3.5 : 2.5) == 3.5;"));
		
		assertTrue(runTest("((3.5 > 2) ? 3.5 : 2) == 3.5;"));
		assertTrue(runTest("((3.5 > 4) ? 3.5 : 2) == 2;"));
	}
	
	@Test
	public void testNumber(){
		assertTrue(runTest("{num n = 1; n == 1;}"));
		assertTrue(runTest("{num n = 1; 1 == n;}"));
		
		assertTrue(runTest("{num n = 1; n != 2;}"));
		assertTrue(runTest("{num n = 1; 2 != n;}"));
		
		
		assertTrue(runTest("{num n = 1; n + 1 == 2;}"));
		assertTrue(runTest("{num n = 1; 1 + n == 2;}"));
		
		assertTrue(runTest("{num n = 2; n - 1 == 1;}"));	
		assertTrue(runTest("{num n = 2; 1 - n == -1;}"));	
		
		
		assertTrue(runTest("{num n = 2; n * 3 == 6;}"));
		assertTrue(runTest("{num n = 2; 3 * n == 6;}"));
		
		assertTrue(runTest("{num n = 8; n / 4 == 2;}"));	
		assertTrue(runTest("{num n = 4; 8 / n == 2;}"));	
		
//		assertTrue(runTest("{num n = 5; n % 2 == 1;}"));	
//		assertTrue(runTest("{num n = 2; 5 % n == 1;}"));	
		
	
		assertTrue(runTest("{num n = 1; n <= 2;}"));
		assertTrue(runTest("{num n = 1; 0 <= n;}"));
		
		assertTrue(runTest("{num n = 1; n < 2;}"));
		assertTrue(runTest("{num n = 1; 0 < n;}"));
		
		assertTrue(runTest("{num n = 2; n >= 1;}"));
		assertTrue(runTest("{num n = 1; 2 >= n;}"));
		
		assertTrue(runTest("{num n = 2; n > 1;}"));
		assertTrue(runTest("{num n = 1; 2 > n;}"));
		
		assertTrue(runTest("{num n = 1; 2 > n;}"));
		
		assertTrue(runTest("{num n35 = 3.5; num n2 = 2; ((n35 > n2) ? 3.5 : 2) == 3.5;}"));
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
	
	@Test
	public void stringEscapes() {
		assertTrue(runTest("\"\\b\" == \"\\b\";"));
		assertTrue(runTest("\"\\t\" == \"\\t\";"));
		assertTrue(runTest("\"\\n\" == \"\\n\";"));
		assertTrue(runTest("\"\\f\" == \"\\f\";"));
		assertTrue(runTest("\"\\r\" == \"\\r\";"));
		
		assertTrue(runTest("\"\\\"\" == \"\\\"\";"));
		assertTrue(runTest("\"\\\'\" == \"\\\'\";"));
		assertTrue(runTest("\"\\\\\" == \"\\\\\";"));
		assertTrue(runTest("\"\\<\" == \"\\<\";"));
		assertTrue(runTest("\"\\>\" == \"\\>\";"));
		
		assertTrue(runTest("\"\\a20\" == \" \";"));
		assertTrue(runTest("\"\\U01F35D\" == \"🍝\";"));
		assertTrue(runTest("\"\\U01F35D\" == \"🍝\";"));
		assertTrue(runTest("\"\\u2713\" == \"✓\";"));
	}
	
	@Test
	public void stringInterpolation(){
		assertTrue(runTest("{str a = \"abc\"; \"1<a>2\" == \"1abc2\";}"));
		assertTrue(runTest("{int a = 789; \"1<a>2\" == \"17892\";}"));
		
		assertTrue(runTest("{str a = \"a\\bc\"; \"1<a>2\" == \"1a\\bc2\";}"));
		assertTrue(runTest("{str a = \"a\\tc\"; \"1<a>2\" == \"1a\\tc2\";}"));
		assertTrue(runTest("{str a = \"a\\nc\"; \"1<a>2\" == \"1a\\nc2\";}"));
		assertTrue(runTest("{str a = \"a\\fc\"; \"1<a>2\" == \"1a\\fc2\";}"));
		assertTrue(runTest("{str a = \"a\\rc\"; \"1<a>2\" == \"1a\\rc2\";}"));
		
		assertTrue(runTest("{str a = \"a\\\"c\"; \"1<a>2\" == \"1a\\\"c2\";}"));
		assertTrue(runTest("{str a = \"a\\\'c\"; \"1<a>2\" == \"1a\\\'c2\";}"));
		assertTrue(runTest("{str a = \"a\\\\c\"; \"1<a>2\" == \"1a\\\\c2\";}"));
		
		assertTrue(runTest("{str a = \"a\\<c\"; \"1<a>2\" == \"1a\\<c2\";}"));
		assertTrue(runTest("{str a = \"a\\>c\"; \"1<a>2\" == \"1a\\>c2\";}"));
	
	}

	@Test(expected=StaticError.class)
	public void orError() {
		runTest("3 || true;");
	}
	
	@Test
	public void testLocation() {
		String Loc = "|file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>)";
		String Loc2 = "|file:///home/paulk/pico2.trm|(0,1,<2,3>,<4,5>)";
		
		assertTrue(runTest("{" + Loc + "; true;}"));
		assertTrue(runTest(Loc + " == " + Loc + ";"));
		assertFalse(runTest(Loc + " == " + Loc2 + ";"));
		
		assertTrue(runTest(Loc + ".uri == \"file:///home/paulk/pico.trm\";"));
		assertTrue(runTest(Loc + ".offset == 0;"));
		assertTrue(runTest(Loc + ".length == 1;"));
		assertTrue(runTest(Loc + ".begin.line == 2;"));
		assertTrue(runTest(Loc + ".begin.column == 3;"));
		assertTrue(runTest(Loc + ".end.line == 4;"));
		assertTrue(runTest(Loc + ".end.column == 5;"));
		assertTrue(runTest(Loc + ".path == \"/home/paulk/pico.trm\";"));
		
		
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.uri == \"file:///home/paulk/pico.trm\";}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.offset == 0;}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.length == 1;}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.begin.line == 2;}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.begin.column == 3;}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.end.line == 4;}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.end.column == 5;}"));
		
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.uri = \"file:///home/paulk/pico2.trm\"; Loc.uri == \"file:///home/paulk/pico2.trm\";}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.offset = 10; Loc.offset == 10;}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.length = 11; Loc.length == 11;}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.end.line = 14; Loc.end.line == 14;}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.begin.line = 1; Loc.begin.line == 1;}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.begin.column = 13; Loc.begin.column == 13;}"));
		assertTrue(runTest("{ loc Loc = " + Loc + "; Loc.end.column = 15; Loc.end.column == 15;}"));
		
		assertTrue(runTest("{loc Loc = " + Loc + "; Loc = Loc[uri= \"file:///home/paulk/pico.trm\"]; Loc == |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>);}"));
		assertTrue(runTest("{loc Loc = " + Loc + "; Loc = Loc[offset = 10]; Loc == |file:///home/paulk/pico.trm|(10,1,<2,3>,<4,5>);}"));
		assertTrue(runTest("{loc Loc = " + Loc + "; Loc = Loc[length = 11]; Loc ==  |file:///home/paulk/pico.trm|(0,11,<2,3>,<4,5>);}"));
		assertTrue(runTest("{loc Loc = " + Loc + "; Loc = Loc[begin = <1,4>]; Loc == |file:///home/paulk/pico.trm|(0,1,<1,4>,<4,5>);}"));
		assertTrue(runTest("{loc Loc = " + Loc + "; Loc = Loc[end = <14,38>]; Loc ==  |file:///home/paulk/pico.trm|(0,1,<2,3>,<14,38>);}"));
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
		String Loc = "|file:///home/paulk/pico2.trm|(0,1,<1,4>,<4,5>)";
		runTest("{loc Loc = " + Loc + "; Loc.bla;}");
	}
	
	@Test(expected=StaticError.class)
	public void WrongLocFieldError2(){
		String Loc = "|file:///home/paulk/pico2.trm|(0,1,<1,4>,<4,5>)";
		runTest(Loc + "[bla=3];");
	}
	
	@Test(expected=StaticError.class)
	public void URLFieldError1(){
		String Loc = "|file:///home/paulk/pico2.trm|(0,1,<1,4>,<4,5>)";
		runTest("{loc Loc = " + Loc + "; Loc.uri=true;}");
	}
	
	@Test(expected=Throw.class)
	public void URLFieldError2(){
		String Loc = "|file:///home/paulk/pico2.trm|(0,1,<1,4>,<4,5>)";
		runTest("{loc Loc = " + Loc + "; Loc.uri=\"? ??\";}");
	}
	
	@Test(expected=StaticError.class)
	public void LengthFieldError(){
		String Loc = "|file:///home/paulk/pico2.trm|(0,1,<1,4>,<4,5>)";
		runTest("{loc Loc = " + Loc + "; Loc.length=true;}");
	}
	
	@Test(expected=StaticError.class)
	public void OffsetFieldError(){
		String Loc = "|file:///home/paulk/pico2.trm|(0,1,<1,4>,<4,5>)";
		runTest("{loc Loc = " + Loc + "; Loc.offset=true;}");
	}
	
	@Test(expected=StaticError.class)
	public void BeginLineFieldError(){
		String Loc = "|file:///home/paulk/pico2.trm|(0,1,<1,4>,<4,5>)";
		runTest("{loc Loc = " + Loc + "; Loc.beginLine=true;}");
	}
	@Test(expected=StaticError.class)
	public void EndLineFieldError(){
		String Loc = "|file:///home/paulk/pico2.trm|(0,1,<1,4>,<4,5>)";
		runTest("{loc Loc = " + Loc + "; Loc.end=<true,12>;}");
	}
	
	@Test(expected=StaticError.class)
	public void BeginColumnFieldError(){
		String Loc = "|file:///home/paulk/pico2.trm|(0,1,<1,4>,<4,5>)";
		runTest("{loc Loc = " + Loc + "; Loc.begin=<true,1>;}");
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
		
		assertTrue(runTest("[1,2,1,2,3,4,3,4,5] - 1 == [2,1,2,3,4,3,4,5];"));
		assertTrue(runTest("[1,2,1,2,3,4,3,4,5] - 2 == [1,1,2,3,4,3,4,5];"));
		assertTrue(runTest("[1,2,1,2,3,4,3,4,5] - 5 == [1,2,1,2,3,4,3,4];"));
		
		assertTrue(runTest("[1,2,1,2,3,4,3,4,5] - [1] == [2,1,2,3,4,3,4,5];"));
		assertTrue(runTest("[1,2,1,2,3,4,3,4,5] - [2] == [1,1,2,3,4,3,4,5];"));
		assertTrue(runTest("[1,2,1,2,3,4,3,4,5] - [5] == [1,2,1,2,3,4,3,4];"));
		
		assertTrue(runTest("[1,2,1,2,3,4,3,4,5] - [1,1] == [2,2,3,4,3,4,5];"));
		assertTrue(runTest("[1,2,1,2,3,4,3,4,5] - [1,1,1] == [2,2,3,4,3,4,5];"));
		
		assertTrue(runTest("[1,2,1,2,3,4,3,4,5] - [1,2] == [1,2,3,4,3,4,5];"));
		assertTrue(runTest("[1,2,1,2,3,4,3,4,5] - [2,3] == [1,1,2,4,3,4,5];"));
		
		assertTrue(runTest("[] & [1,2,4] == [];"));
		assertTrue(runTest("[1,2,3] & [] == [];"));
		assertTrue(runTest("[1,2,3,4,5,4,3,2,1] & [1,2,4] == [1,2,4,4,2,1];"));
		
		
		assertTrue(runTest("[] <= [];"));
		assertTrue(runTest("[] <= [1];"));
// These commented out tests assume that <= etc. are ("half") ordering operations
// Currently they are strictly subset implementations.
//		assertTrue(runTest("[2, 1, 0] <= [2, 3];"));
//		assertTrue(runTest("[2, 1] <= [2, 3, 0];"));
		assertTrue(runTest("[2, 1] <= [2, 1];"));
		assertTrue(runTest("[2, 1] <= [2, 1, 0];"));
		
		assertTrue(runTest("[] < [1];"));
//		assertTrue(runTest("[2, 1, 0] < [2, 3];"));
//		assertTrue(runTest("[2, 1] < [2, 3, 0];"));
		assertTrue(runTest("[2, 1] < [2, 1, 0];"));
		
		assertTrue(runTest("[] >= [];"));
//		assertTrue(runTest("[1] >= [];"));
//		assertTrue(runTest("[2, 3] >= [2, 1, 0];"));
//		assertTrue(runTest("[2, 3, 0] >= [2, 1];"));
		assertTrue(runTest("[2, 1] >= [2, 1];"));
		assertTrue(runTest("[2, 1, 0] >= [2, 1];"));
		
		assertTrue(runTest("[1] > [];"));
//		assertTrue(runTest("[2, 3] > [2, 1, 0];"));
//		assertTrue(runTest("[2, 3, 0] > [2, 1];"));
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
	
	@Test(expected=UndeclaredVariableError.class)
	public void SubscriptError2() {
		runTest("L[5];");
	}
	
	@Test
	public void listSplicing(){
		assertTrue(runTest("{[1,2,3] == [1,2,3];}"));
		assertTrue(runTest("{[*1,2,3] == [1,2,3];}"));
		assertTrue(runTest("{[1,*2,3] == [1,2,3];}"));
		assertTrue(runTest("{[1,2,*3] == [1,2,3];}"));
		assertTrue(runTest("{[*1,*2,3] == [1,2,3];}"));
		
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [L1] == [[1,2]];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [*L1] == [1,2];}"));
		
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [L1,3] == [[1,2],3];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [*L1,3] == [1,2,3];}"));
		
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [L1,L2] == [[1,2],[3,4]];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [*L1,L2] == [1,2,[3,4]];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [L1,*L2] == [[1,2],3,4];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [*L1,*L2] == [1,2,3,4];}"));
		
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [L1,L2,5] == [[1,2],[3,4],5];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [*L1,L2,5] == [1,2,[3,4],5];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [L1,*L2,5] == [[1,2],3,4,5];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [*L1,*L2,5] == [1,2,3,4,5];}"));
		
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [[L1]] == [[[1,2]]];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [[*L1]] == [[1,2]];}"));
		
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [[L1],3] == [[[1,2]],3];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [[*L1],3] == [[1,2],3];}"));	
		
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [[L1],[L2]] == [[[1,2]],[[3,4]]];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [[*L1],[L2]] == [[1,2],[[3,4]]];}"));
		
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [[L1],[*L2]] == [[[1,2]],[3,4]];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [[*L1],[*L2]] == [[1,2],[3,4]];}"));
		
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [*[*L1],[*L2]] == [1,2,[3,4]];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [[*L1],*[*L2]] == [[1,2],3,4];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [*[*L1],*[*L2]] == [1,2,3,4];}"));
	
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [L1,[L2]] == [[1,2],[[3,4]]];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [*L1,[L2]] == [1,2,[[3,4]]];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [L1,[*L2]] == [[1,2],[3,4]];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [*L1,[*L2]] == [1,2,[3,4]];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [*L1,*[*L2]] == [1,2,3,4];}"));

		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [L1,[L2],5] == [[1,2],[[3,4]],5];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [*L1,[L2],5] == [1,2,[[3,4]],5];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; [L1,[*L2],5] == [[1,2],[3,4],5];}"));
		
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; list[list[list[int]]] L3 = [[L1]]; L3 == [[[1,2]]];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; list[value] L3 = [[L1],3]; L3 == [[[1,2]],3];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; list[list[list[int]]] L3 = [[L1],[L2]]; L3 == [[[1,2]],[[3,4]]];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; list[value] L3 = [L1,[L2]]; L3 == [[1,2],[[3,4]]];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; list[int] L2 = [3,4]; list[value] L3 = [L1,[L2],5]; L3 == [[1,2],[[3,4]],5];}"));
	}
	
	@Test 
	public void testSetInListSplicing() {
		assertTrue(runTest("{list[int] L1 = [1,2]; set[int] S2 = {3,4}; [L1,S2,5] == [[1,2],{3,4},5];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; set[int] S2 = {3,4}; [*L1,S2,5] == [1,2,{3,4},5];}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; set[int] S2 = {3,4}; ([L1,*S2,5] == [[1,2],3,4,5]) || ([L1,*S2,5] == [[1,2],4,3,5]);}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; set[int] S2 = {3,4}; ([*L1,*S2,5] == [1,2,3,4,5]) || ([*L1,*S2,5] == [1,2,4,3,5]) ;}"));
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
	public void testSet1()  {
		
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
		
		assertTrue(runTest("((3 > 2) ? {1,2} : {1,2,3}) == {1,2};"));
		
		assertTrue(runTest("{<\"a\", [1,2]>, <\"b\", []>, <\"c\", [4,5,6]>} != {};"));
	}
	
	private boolean auxTest(String S1, String S2){
		return runTestInSameEvaluator(S1 + "==" + S2);
	}
	
	/*
	 * Some nested set patterns to test backtracking behaviour.
	 */
	
	@Test
	public void testSet2(){
		prepare("data TYPESET = SET(str name) | SUBTYPES(TYPESET tset) | INTERSECT(set[TYPESET] tsets);");
		
		assertTrue(runTestInSameEvaluator("{INTERSECT({TYPESET t1, set[TYPESET] rest}), TYPESET t2} :=  {INTERSECT({SET(\"a\"), SET(\"b\")}), SET(\"c\")}"));
		assertTrue(runTestInSameEvaluator("{INTERSECT({TYPESET t1, set[TYPESET] rest}),  t1} :=  {INTERSECT({SET(\"a\"), SET(\"b\")}), SET(\"a\")}"));
		assertTrue(runTestInSameEvaluator("{INTERSECT({TYPESET t1, set[TYPESET] rest}),  t1} :=  {INTERSECT({SET(\"b\"), SET(\"a\")}), SET(\"a\")}"));

		assertTrue(auxTest("{ <t1, t2> | INTERSECT({TYPESET t1, set[TYPESET] t2}) :=  INTERSECT({SET(\"b\"), SET(\"a\")})}",
						   "{ <SET(\"b\"),{SET(\"a\")}>, <SET(\"a\"),{SET(\"b\")}>	}"));
		
		assertTrue(auxTest("{<t1, rest, t2> | {INTERSECT({TYPESET t1, set[TYPESET] rest}),  t2} :=  {INTERSECT({SET(\"a\"), SET(\"b\")}), SET(\"b\")}}",
				           "{ <SET(\"a\"),{SET(\"b\")},SET(\"b\")>, <SET(\"b\"),{SET(\"a\")},SET(\"b\")>}"));

		assertTrue(auxTest("{<t1, rest> | {INTERSECT({TYPESET t1, set[TYPESET] rest}),  t1} :=  {INTERSECT({SET(\"a\"), SET(\"b\")}), SET(\"b\")}}",
				           "{<SET(\"b\"),{SET(\"a\")}>}"));	
	}
	
	/*
	 * Anastassija's type constraint examples
	 * Tests for "simp" version
	 */
	
	private void simpTests(){
	
	assertTrue(auxTest("simp(INTERSECT({ SUBTYPES(INTERSECT({  })), SET(\"s1\") }))",
							"INTERSECT({ SUBTYPES(INTERSECT({  })), SET(\"s1\") })"));

	assertTrue(auxTest("simp(INTERSECT({ SUBTYPES(INTERSECT({SET(\"s1\")  })), SET(\"s2\") }))",
		   	    			"INTERSECT({ SUBTYPES(INTERSECT({SET(\"s1\") })), SET(\"s2\") })"));

	assertTrue(auxTest("simp(INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\") })), SET(\"s1\") }))",
		        			"INTERSECT({ SUBTYPES(INTERSECT({             })), SET(\"s1\") });"));
	
	assertTrue(auxTest("simp(INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\") })), SET(\"s1\"), SET(\"s2\")  }))",
							"INTERSECT({ SUBTYPES(INTERSECT({             })), SET(\"s1\"), SET(\"s2\")  });"));
	
	assertTrue(auxTest("simp(INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\") })), SET(\"s2\"), SET(\"s1\")  }))",
							"INTERSECT({ SUBTYPES(INTERSECT({             })), SET(\"s2\"), SET(\"s1\")  });"));



	assertTrue(auxTest("simp(INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\"), SET(\"s2\") })), SET(\"s3\") }))",
               				"INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\"), SET(\"s2\") })), SET(\"s3\") });"));
	
	assertTrue(auxTest("simp(INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\"), SET(\"s2\") })), SET(\"s3\"), SET(\"s4\") }))",
   		    		  		"INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\"), SET(\"s2\") })), SET(\"s3\"), SET(\"s4\")  });"));


	assertTrue(auxTest("simp(INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\"), SET(\"s3\") })), SET(\"s1\") }))",
							"INTERSECT({ SUBTYPES(INTERSECT({              SET(\"s3\") })), SET(\"s1\") });"));

	assertTrue(auxTest("simp(INTERSECT({ SUBTYPES(INTERSECT({ SUBTYPES(SET(\"s3\"))})), SET(\"s3\") }))",
            				"INTERSECT({ SUBTYPES(INTERSECT({ SUBTYPES(SET(\"s3\"))})), SET(\"s3\") });"));

	assertTrue(auxTest("simp(INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s70\"), SET(\"s4\")})), SET(\"s70\") }))",
            				"INTERSECT({ SUBTYPES(INTERSECT({               SET(\"s4\")})), SET(\"s70\") });"));

	assertTrue(auxTest("simp(INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\"), SUBTYPES(SET(\"s3\")) })), SET(\"s1\") }))",
             				"INTERSECT({ SUBTYPES(INTERSECT({              SUBTYPES(SET(\"s3\")) })), SET(\"s1\") });"));

	assertTrue(auxTest("simp(INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\"), SUBTYPES(SET(\"s3\")) })), SUBTYPES(SET(\"s2\")), SET(\"s1\") }))",   
            				"INTERSECT({ SUBTYPES(INTERSECT({              SUBTYPES(SET(\"s3\")) })), SUBTYPES(SET(\"s2\")), SET(\"s1\") });"));

	assertTrue(auxTest("simp(INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\"), SUBTYPES(SET(\"s2\")) })), SUBTYPES(SET(\"s2\")), SET(\"s1\") }))",
            				"INTERSECT({ SUBTYPES(INTERSECT({                                    })), SUBTYPES(SET(\"s2\")), SET(\"s1\") });"));	

	assertTrue(auxTest("simp(INTERSECT({ SUBTYPES(INTERSECT({ SUBTYPES(SET(\"s3\")), SUBTYPES(SET(\"s2\")) })), SUBTYPES(SET(\"s2\")) }))",
            				"INTERSECT({ SUBTYPES(INTERSECT({ SUBTYPES(SET(\"s3\"))                        })), SUBTYPES(SET(\"s2\")) });"));

	assertTrue(auxTest("simp(INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\"), SUBTYPES(SET(\"s2\")), SUBTYPES(SET(\"s3\")) })), SUBTYPES(SET(\"s2\")), SET(\"s1\") }))",
             				"INTERSECT({ SUBTYPES(INTERSECT({                                     SUBTYPES(SET(\"s3\")) })), SUBTYPES(SET(\"s2\")), SET(\"s1\") });"));

	assertTrue(auxTest("simp(INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\"), SET(\"s2\"), SET(\"s3\"), SET(\"s5\") })), SET(\"s6\"), SET(\"s2\"), SET(\"s7\"), SET(\"s1\") }))",
            				"INTERSECT({ SUBTYPES(INTERSECT({                           SET(\"s3\"), SET(\"s5\") })), SET(\"s6\"), SET(\"s2\"), SET(\"s7\"), SET(\"s1\") });"));

	assertTrue(auxTest("simp(INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\"), SUBTYPES(SET(\"s2\")), SET(\"s3\"), SET(\"s5\") })), SET(\"s6\"), SUBTYPES(SET(\"s2\")), SET(\"s7\"), SET(\"s1\"), SET(\"s3\") }))",
							"INTERSECT({ SUBTYPES(INTERSECT({                                                  SET(\"s5\") })), SET(\"s6\"), SUBTYPES(SET(\"s2\")), SET(\"s7\"), SET(\"s1\"), SET(\"s3\") });"));

	}
	
	private void funTests(){
		
		assertTrue(auxTest("INTERSECT({ SUBTYPES(INTERSECT({  })), SET(\"s1\") })",
						   "INTERSECT({ SUBTYPES(INTERSECT({  })), SET(\"s1\") })"));

		assertTrue(auxTest("INTERSECT({ SUBTYPES(INTERSECT({SET(\"s1\")  })), SET(\"s2\") })",
			   	    	   "INTERSECT({ SUBTYPES(INTERSECT({SET(\"s1\") })),  SET(\"s2\") })"));

		assertTrue(auxTest("INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\") })), SET(\"s1\") })",
			        	   "INTERSECT({ SUBTYPES(INTERSECT({             })), SET(\"s1\") });"));
		
		assertTrue(auxTest("INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\") })), SET(\"s1\"), SET(\"s2\")  })",
						   "INTERSECT({ SUBTYPES(INTERSECT({             })), SET(\"s1\"), SET(\"s2\")  });"));

		assertTrue(auxTest("INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\") })), SET(\"s2\"), SET(\"s1\")  })",
						   "INTERSECT({ SUBTYPES(INTERSECT({             })), SET(\"s2\"), SET(\"s1\")  });"));

		assertTrue(auxTest("INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\"), SET(\"s2\") })), SET(\"s3\") })",
	               		   "INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\"), SET(\"s2\") })), SET(\"s3\") });"));
		
		assertTrue(auxTest("INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\"), SET(\"s2\") })), SET(\"s3\"), SET(\"s4\") })",
        		   		    "INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\"), SET(\"s2\") })), SET(\"s3\"), SET(\"s4\")  });"));

		assertTrue(auxTest("INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\"), SET(\"s3\") })), SET(\"s1\") })",
						   "INTERSECT({ SUBTYPES(INTERSECT({              SET(\"s3\") })), SET(\"s1\") });"));

		assertTrue(auxTest("INTERSECT({ SUBTYPES(INTERSECT({ SUBTYPES(SET(\"s3\"))})), SET(\"s3\") })",
	            		   "INTERSECT({ SUBTYPES(INTERSECT({ SUBTYPES(SET(\"s3\"))})), SET(\"s3\") });"));

		assertTrue(auxTest("INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s70\"), SET(\"s4\")})), SET(\"s70\") })",
	            		   "INTERSECT({ SUBTYPES(INTERSECT({               SET(\"s4\")})), SET(\"s70\") });"));

		assertTrue(auxTest("INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\"), SUBTYPES(SET(\"s3\")) })), SET(\"s1\") })",
	             		   "INTERSECT({ SUBTYPES(INTERSECT({              SUBTYPES(SET(\"s3\")) })), SET(\"s1\") });"));

		assertTrue(auxTest("INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\"), SUBTYPES(SET(\"s3\")) })), SUBTYPES(SET(\"s2\")), SET(\"s1\") })",   
	            		   "INTERSECT({ SUBTYPES(INTERSECT({              SUBTYPES(SET(\"s3\")) })), SUBTYPES(SET(\"s2\")), SET(\"s1\") });"));

		assertTrue(auxTest("INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\"), SUBTYPES(SET(\"s2\")) })), SUBTYPES(SET(\"s2\")), SET(\"s1\") })",
	            		   "INTERSECT({ SUBTYPES(INTERSECT({                                    })), SUBTYPES(SET(\"s2\")), SET(\"s1\") });"));	

		assertTrue(auxTest("INTERSECT({ SUBTYPES(INTERSECT({ SUBTYPES(SET(\"s3\")), SUBTYPES(SET(\"s2\")) })), SUBTYPES(SET(\"s2\")) })",
	            		   "INTERSECT({ SUBTYPES(INTERSECT({ SUBTYPES(SET(\"s3\"))                        })), SUBTYPES(SET(\"s2\")) });"));

		assertTrue(auxTest("INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\"), SUBTYPES(SET(\"s2\")), SUBTYPES(SET(\"s3\")) })), SUBTYPES(SET(\"s2\")), SET(\"s1\") })",
	             		   "INTERSECT({ SUBTYPES(INTERSECT({                                     SUBTYPES(SET(\"s3\")) })), SUBTYPES(SET(\"s2\")), SET(\"s1\") });"));

		assertTrue(auxTest("INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\"), SET(\"s2\"), SET(\"s3\"), SET(\"s5\") })), SET(\"s6\"), SET(\"s2\"), SET(\"s7\"), SET(\"s1\") })",
	            		   "INTERSECT({ SUBTYPES(INTERSECT({                           SET(\"s3\"), SET(\"s5\") })), SET(\"s6\"), SET(\"s2\"), SET(\"s7\"), SET(\"s1\") });"));

		assertTrue(auxTest("INTERSECT({ SUBTYPES(INTERSECT({ SET(\"s1\"), SUBTYPES(SET(\"s2\")), SET(\"s3\"), SET(\"s5\") })), SET(\"s6\"), SUBTYPES(SET(\"s2\")), SET(\"s7\"), SET(\"s1\"), SET(\"s3\") })",
						   "INTERSECT({ SUBTYPES(INTERSECT({                                                  SET(\"s5\") })), SET(\"s6\"), SUBTYPES(SET(\"s2\")), SET(\"s7\"), SET(\"s1\"), SET(\"s3\") });"));

		}
	
	/*
	 * Anastassija's type constraint examples
	 * Version 1; with explicit simplification function, no non-linear constraints, fail to explore alternative matches
	 */
	
	@Test
	public void testSet3()  {
		prepare("data TYPESET = SET(str name) | SUBTYPES(TYPESET tset) | INTERSECT(set[TYPESET] tsets);");
		
		prepareMore("public TYPESET simp(TYPESET ts){" +
			           "for(INTERSECT({ SUBTYPES(INTERSECT({ TYPESET tset, set[TYPESET] rest})), TYPESET tset1, set[TYPESET] rest1 }) := ts){" +
			                "if(tset == tset1) return simp(INTERSECT({ SUBTYPES(INTERSECT(rest)), tset1, *rest1 }));" +
			                "else  fail;" +
			           "}" +
			           "return ts;" +
		            "}");
		simpTests();
		}
	
	/*
	 * Anastassija's type constraint examples
	 * Version 2; with explicit simplification function, and non-linear constraints (tset)
	 */
	
	@Test
	public void testSet4()  {
		prepare("data TYPESET = SET(str name) | SUBTYPES(TYPESET tset) | INTERSECT(set[TYPESET] tsets);");
		
		prepareMore("public TYPESET simp(TYPESET ts){" +
			           "if(INTERSECT({ SUBTYPES(INTERSECT({ TYPESET tset, set[TYPESET] rest})), tset, set[TYPESET] rest1 }) := ts){" +
			                "return simp(INTERSECT({ SUBTYPES(INTERSECT(rest)), tset, *rest1 }));" +
			           "}" +
			           "return ts;" +
		            "}");
		
		simpTests();
	}
	
	/*
	 * Anastassija's type constraint examples
	 * Version 3; with explicit simplification function, non-linear constraints (tset) and nested simp call
	 */
	
	@Test
	public void testSet5()  {
		prepare("data TYPESET = SET(str name) | SUBTYPES(TYPESET tset) | INTERSECT(set[TYPESET] tsets);");
		
		prepareMore("public TYPESET simp(TYPESET ts){" +
			           "if(INTERSECT({ SUBTYPES(INTERSECT({ TYPESET tset, set[TYPESET] rest})), tset, set[TYPESET] rest1 }) := ts){" +
			                "return simp(INTERSECT({ SUBTYPES(simp(INTERSECT(rest))), tset, *rest1 }));" +
			           "}" +
			           "return ts;" +
		            "}");
		
		simpTests();
	}


	/*
	 * Anastassija's type constraint examples
	 * Version 4; with overloaded constructor INTERSECT , and non-linear constraints (tset)
	 */
	@Test
	public void testSet6()  {
		prepare("data TYPESET = SET(str name) | SUBTYPES(TYPESET tset) | INTERSECT(set[TYPESET] tsets);");
		
		prepareMore("public TYPESET INTERSECT({ SUBTYPES(INTERSECT({ TYPESET tset, set[TYPESET] rest})), tset, set[TYPESET] rest1 }) {" +
		               " return INTERSECT({ SUBTYPES(INTERSECT(rest)), tset, *rest1 });" +
				    "}");

		funTests();
	}
	
	@Test
	public void testSetMultiVariable()  {
		assertTrue(runTest("{set[value] S1, set[value] S2} := {} && (S1 == {}) && (S2 == {});"));
		assertTrue(runTest("{S1*, S2*} := {} && (S1 == {}) && (S2 == {});"));
		
		assertTrue(runTest("{set[int] S1, set[int] S2} := {100} && ((S1 == {100} && S2 == {}) || (S1 == {} && S2 == {100}));"));
		assertTrue(runTest("{S1*, S2*} := {100} && ((S1 == {100} && S2 == {}) || (S1 == {} && S2 == {100}));"));
		
		assertTrue(runTest("{R = for({set[int] S1, set[int] S2} := {100}) append <S1, S2>; R == [<{100}, {}>, <{}, {100}> ];}"));
		assertTrue(runTest("{R = for({S1*, S2*} := {100}) append <S1, S2>; R == [<{100}, {}>, <{}, {100}> ];}"));

		assertTrue(runTest("{R = for({S1*, S2*} := {100}) append <S1, S2>; R == [<{100}, {}>, <{}, {100}> ];}"));
		assertTrue(runTest("{R = for({S1*, S2*} := {100, 200}) append <S1, S2>; R == [<{200,100}, {}>, <{200}, {100}>, <{100}, {200}>, <{}, {200,100}>];}"));
		assertTrue(runTest("{R = for({set[int] S1, S2*} := {100, \"a\"})  append <S1, S2>; R == [<{100}, {\"a\"}>, <{},{100,\"a\"}>];}"));
		assertTrue(runTest("{R = for({set[int] S1, set[str] S2} := {100, \"a\"}) append <S1, S2>; R == [<{100}, {\"a\"}>];}"));
		
		assertTrue(runTest("{R = for({set[str] S1, S2*} := {100, \"a\"})  append <S1, S2>; R == [<{\"a\"},{100}>, <{},{100,\"a\"}>];}"));
		assertTrue(runTest("{R = for({set[str] S1, set[int] S2} := {100, \"a\"})  append <S1, S2>; R == [<{\"a\"},{100}>];}"));
		
		assertFalse(runTest("{set[str] S1, set[str] S2} := {100, \"a\"};"));
		assertFalse(runTest("{set[int] S1, set[int] S2} := {100, \"a\"};"));

	}
	
    @Test(expected=UndeclaredVariableError.class)
    public void UndefinedSetElementError(){
    	runTest("{X};");
    }
    
    @Test(expected=StaticError.class)
	public void inError() {
		runTest("1 in 3;");
	}
    
    @Ignore @Test(expected=StaticError.class)
	public void addSetError() {
		runTest("{1,2,3} + true;");
	}
    
	@Test(expected=StaticError.class)
	public void productError() {
		runTest("{1,2,3} * true;");
	}
	
	@Test
	public void setSplicing(){
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {S1} == {{1,2}};}"));
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {*S1} == {1,2};}"));
		
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {S1,3} == {{1,2},3};}"));
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {*S1,3} == {1,2,3};}"));
		
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {S1,S2} == {{1,2},{3,4}};}"));
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {*S1,S2} == {1,2,{3,4}};}"));
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {S1,*S2} == {{1,2},3,4};}"));
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {*S1,*S2} == {1,2,3,4};}"));
	
		
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {S1,S2,5} == {{1,2},{3,4},5};}"));
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {*S1,S2,5} == {1,2,{3,4},5};}"));
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {S1,*S2,5} == {{1,2},3,4,5};}"));
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {*S1,*S2,5} == {1,2,3,4,5};}"));
		
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {{S1}} == {{{1,2}}};}"));
		
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {{*S1}} == {{1,2}};}"));
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {*{*S1}} == {1,2};}"));
		
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {{S1},3} == {{{1,2}},3};}"));
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {*{S1},3} == {{1,2},3};}"));
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {*{*S1},3} == {1,2,3};}"));
		
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {*{*S1},2} == {1,2};}"));
	
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {{S1},{S2}} == {{{1,2}},{{3,4}}};}"));
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {*{S1},{S2}} == {{1,2},{{3,4}}};}"));
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {{S1},*{S2}} == {{{1,2}},{3,4}};}"));
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {*{S1},*{S2}} == {{1,2},{3,4}};}"));
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {*{*S1},*{*S2}} == {1,2,3,4};}"));
		
		
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {S1,{S2}} == {{1,2},{{3,4}}};}"));
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {S1,{*S2}} == {{1,2},{3,4}};}"));
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {S1,*{*S2}} == {{1,2},3,4};}"));
		
		
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {S1,{S2},5} == {{1,2},{{3,4}},5};}"));
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {S1,{*S2},5} == {{1,2},{3,4},5};}"));
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {S1,*{*S2},5} == {{1,2},3,4,5};}"));
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; {*S1,*{*S2},5} == {1,2,3,4,5};}"));
		
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; set[set[set[int]]] S3 = {{S1}}; S3 == {{{1,2}}};}"));
		
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; set[value] S3 = {{S1},3}; S3 == {{{1,2}},3};}"));
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; set[set[set[int]]] S3 = {{S1},{S2}}; S3 == {{{1,2}},{{3,4}}};}"));
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; set[value] S3 = {S1,{S2}}; S3 == {{1,2},{{3,4}}};}"));
		assertTrue(runTest("{set[int] S1 = {1,2}; set[int] S2 = {3,4}; set[value] S3 = {S1,{S2},5}; S3 == {{1,2},{{3,4}},5};}"));
	}
	
	@Test 
	public void testListInSetSplicing() {
		assertTrue(runTest("{list[int] L1 = [1,2]; set[int] S2 = {3,4}; {L1,S2,5} == {[1,2],{3,4},5};}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; set[int] S2 = {3,4}; {*L1,S2,5} == {1,2,{3,4},5};}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; set[int] S2 = {3,4}; {L1,*S2,5} == {[1,2],3,4,5};}"));
		assertTrue(runTest("{list[int] L1 = [1,2]; set[int] S2 = {3,4}; {*L1,*S2,5} == {1,2,3,4,5};}"));
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
		
		
		assertTrue(runTest("1 in (1:10, 2:20);"));
		assertFalse(runTest("3 in (1:10, 2:20);"));
		
		assertTrue(runTest("3 notin (1:10, 2:20);"));
		assertFalse(runTest("2 notin (1:10, 2:20);"));
		
		assertTrue(runTest("{map[str,list[int]] m = (\"a\": [1,2], \"b\": [], \"c\": [4,5,6]); m[\"a\"] == [1,2];}"));
	}
	
	 @Test(expected=UndeclaredVariableError.class)
	    public void UndefinedMapElementError1(){
	    	runTest("(X:2);");
	    }
	 
	 @Test(expected=UndeclaredVariableError.class)
	    public void UndefinedMapElementError2(){
	    	runTest("(1:Y);");
	    }
	 
	 @Test(expected=Throw.class)
	 public void NoKeyError(){
		 runTest("(1:10, 2:20)[3];");
	 }
	 
	 @Test(expected=Throw.class)  // MultipleKey
	 public void MultipleKeyError(){
		 runTest("(1:10, 1:10);");
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
	
	 @Test(expected=UndeclaredVariableError.class)
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
		
		assertTrue(runTest("{<1, \"a\">, <2, \"b\">} * {<false, 0>, <true, 1>} == {<<1,\"a\">,<false,0>>,<<2,\"b\">,<false,0>>,<<1,\"a\">,<true,1>>,<<2,\"b\">,<true,1>>};"));

		assertTrue(runTest("{<1,2>} join {<2,3>} == {<1,2,2,3>};"));
		assertTrue(runTest("{<1,2>} join {} == {};"));
		assertTrue(runTest("{} join {<2,3>} == {};"));
		assertTrue(runTest("{} join {} == {};"));
		assertTrue(runTest("{<1,2>} join {3} == {<1,2,3>};"));
		assertTrue(runTest("{<1,2>} join {3, 4} == {<1,2,3>, <1,2,4>};"));
		assertTrue(runTest("{1} join {2} == {<1,2>};"));
		assertTrue(runTest("{1,2,3} join {2} == {<1,2>, <2,2>, <3,2>};"));
		

		assertTrue(runTest("{} + == {};"));
		assertTrue(runTest("{} * == {};"));
		
		assertTrue(runTest("{<1,2>, <2,3>, <3,4>} + == {<1,2>, <2,3>, <3,4>, <1, 3>, <2, 4>, <1, 4>};"));
		
		assertTrue(runTest("{<1,2>, <2,3>, <3,4>} * == {<1,2>, <2,3>, <3,4>, <1, 3>, <2, 4>, <1, 4>, <1, 1>, <2, 2>, <3, 3>, <4, 4>};"));
		
		assertTrue(runTest("{<1,2>, <2,3>, <3,4>, <4,2>, <4,5>}+ ==	{<1,2>, <2,3>, <3,4>, <4,2>, <4,5>, <1, 3>, <2, 4>, <3, 2>, <3, 5>, <4, 3>, <1, 4>, <2, 2>, <2, 5>, <3, 3>, <4, 4>, <1, 5>};"));
		
		assertTrue(runTest("{<1,2>, <2,3>, <3,4>, <4,2>, <4,5>}* == {<1,2>, <2,3>, <3,4>, <4,2>, <4,5>, <1, 3>, <2, 4>, <3, 2>, <3, 5>, <4, 3>, <1, 4>, <2, 2>, <2, 5>, <3, 3>, <4, 4>, <1, 5>, <1, 1>, <5, 5>};"));
	}
	
	@Test(expected=UndeclaredVariableError.class)
	public void UndeRelationElementError1(){
		runTest("{<1,10>, <X,20>};");
	}
	
	@Test(expected=UndeclaredVariableError.class)
	public void UndefinedRelationElementError2(){
		runTest("{<1,10>, <10, Y>};");
	}
	
	@Test(expected=UndeclaredVariableError.class)
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
		prepare("data NODE = val(value V) | f() | f(NODE a);");
		
		assertTrue(runTestInSameEvaluator("f(val(1)) == f(val(1));"));
	}
	
	@Test
	public void node()  {
		prepare("data NODE = i(int I) | s(str x)  | st(set[NODE] s) | l(list[NODE]) | m(map[NODE,NODE] m) | f() | f(NODE a) | f(NODE a, NODE b) | g() | g(NODE a) | g(NODE a,NODE b);");
		
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
		assertTrue(runTestInSameEvaluator("{NODE n = f(i(1),g(i(2),m((i(3):i(3),i(4):i(4),i(5):i(5))))); NODE x = f(i(1),g(i(2),m((i(3):i(3),i(4):i(4),i(5):i(0))))); n != x;}"));
		
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
		
		assertTrue(runTest("{int T; (T ? 13) == 13;}"));
		assertTrue(runTest("{T = (1:10); (T[1] ? 13) == 10;}"));
		assertTrue(runTest("{T = (1:10); (T[2] ? 13) == 13;}"));
		
		assertTrue(runTest("{T = (1:10); T[1] ? == true;}"));
		assertTrue(runTest("{T = (1:10); T[2] ? == false;}"));
	
	}
}
