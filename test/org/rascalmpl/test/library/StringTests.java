/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.test.library;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.test.infrastructure.TestFramework;


public class StringTests extends TestFramework {

	@Test
	public void center() {
		prepare("import String;");

		assertTrue(runTestInSameEvaluator("center(\"a\", 0) == \"a\";"));
		assertTrue(runTestInSameEvaluator("center(\"a\", 1) == \"a\";"));
		assertTrue(runTestInSameEvaluator("center(\"a\", 2) == \"a \";"));
		assertTrue(runTestInSameEvaluator("center(\"a\", 3) == \" a \";"));

		assertTrue(runTestInSameEvaluator("center(\"ab\", 0, \"-\") == \"ab\";"));
		assertTrue(runTestInSameEvaluator("center(\"ab\", 1, \"-\") == \"ab\";"));
		assertTrue(runTestInSameEvaluator("center(\"ab\", 2, \"-\") == \"ab\";"));
		assertTrue(runTestInSameEvaluator("center(\"ab\", 3, \"-\") == \"ab-\";"));
		assertTrue(runTestInSameEvaluator("center(\"ab\", 4, \"-\") == \"-ab-\";"));

		assertTrue(runTestInSameEvaluator("center(\"ab\", 3, \"-+\") == \"ab-\";"));
		assertTrue(runTestInSameEvaluator("center(\"ab\", 4, \"-+\") == \"-ab-\";"));
		assertTrue(runTestInSameEvaluator("center(\"ab\", 5, \"-+\") == \"-ab-+\";"));
		assertTrue(runTestInSameEvaluator("center(\"ab\", 6, \"-+\") == \"-+ab-+\";"));
	}

	@Test
	public void charAt() {

		prepare("import String;");

		assertTrue(runTestInSameEvaluator("String::charAt(\"abc\", 0) == 97;"));
		assertTrue(runTestInSameEvaluator("String::charAt(\"abc\", 1) == 98;"));
		assertTrue(runTestInSameEvaluator("String::charAt(\"abc\", 2) == 99;"));
		assertTrue(runTestInSameEvaluator("charAt(\"abc\", 0) == 97;"));
	}
	
	@Test(expected=RuntimeException.class)
	public void chartAtError(){
		prepare("import String;");
		assertTrue(runTestInSameEvaluator("String::charAt(\"abc\", 3) == 99;"));
	}

	@Test
	public void endsWith() {

		prepare("import String;");

		assertTrue(runTestInSameEvaluator("String::endsWith(\"abc\", \"abc\");"));
		assertTrue(runTestInSameEvaluator("endsWith(\"abc\", \"abc\");"));
		assertTrue(runTestInSameEvaluator("String::endsWith(\"abcdef\", \"def\");"));
		assertFalse(runTestInSameEvaluator("String::endsWith(\"abcdef\", \"abc\");"));
	}

	@Test
	public void left() {
		prepare("import String;");

		assertTrue(runTestInSameEvaluator("left(\"a\", 0) == \"a\";"));
		assertTrue(runTestInSameEvaluator("left(\"a\", 1) == \"a\";"));
		assertTrue(runTestInSameEvaluator("left(\"a\", 2) == \"a \";"));

		assertTrue(runTestInSameEvaluator("left(\"ab\", 0, \"-\") == \"ab\";"));
		assertTrue(runTestInSameEvaluator("left(\"ab\", 1, \"-\") == \"ab\";"));
		assertTrue(runTestInSameEvaluator("left(\"ab\", 2, \"-\") == \"ab\";"));
		assertTrue(runTestInSameEvaluator("left(\"ab\", 3, \"-\") == \"ab-\";"));
		assertTrue(runTestInSameEvaluator("left(\"ab\", 4, \"-\") == \"ab--\";"));

		assertTrue(runTestInSameEvaluator("left(\"ab\", 3, \"-+\") == \"ab-\";"));
		assertTrue(runTestInSameEvaluator("left(\"ab\", 4, \"-+\") == \"ab-+\";"));
		assertTrue(runTestInSameEvaluator("left(\"ab\", 5, \"-+\") == \"ab-+-\";"));
		assertTrue(runTestInSameEvaluator("left(\"ab\", 6, \"-+\") == \"ab-+-+\";"));
	}
	
	@Test
	public void isEmpty(){
		prepare("import String;");
		
		assertTrue(runTestInSameEvaluator("isEmpty(\"\");"));
		assertTrue(runTestInSameEvaluator("isEmpty(\"abc\") == false;"));
	}

	@Test
	public void reverse() {

		prepare("import String;");

		assertTrue(runTestInSameEvaluator("String::reverse(\"\") == \"\";"));
		assertTrue(runTestInSameEvaluator("reverse(\"\") == \"\";"));
		assertTrue(runTestInSameEvaluator("String::reverse(\"abc\") == \"cba\";"));
	}

	@Test
	public void right() {
		prepare("import String;");

		assertTrue(runTestInSameEvaluator("right(\"a\", 0) == \"a\";"));
		assertTrue(runTestInSameEvaluator("right(\"a\", 1) == \"a\";"));
		assertTrue(runTestInSameEvaluator("right(\"a\", 2) == \" a\";"));

		assertTrue(runTestInSameEvaluator("right(\"ab\", 0, \"-\") == \"ab\";"));
		assertTrue(runTestInSameEvaluator("right(\"ab\", 1, \"-\") == \"ab\";"));
		assertTrue(runTestInSameEvaluator("right(\"ab\", 2, \"-\") == \"ab\";"));
		assertTrue(runTestInSameEvaluator("right(\"ab\", 3, \"-\") == \"-ab\";"));
		assertTrue(runTestInSameEvaluator("right(\"ab\", 4, \"-\") == \"--ab\";"));

		assertTrue(runTestInSameEvaluator("right(\"ab\", 3, \"-+\") == \"-ab\";"));
		assertTrue(runTestInSameEvaluator("right(\"ab\", 4, \"-+\") == \"-+ab\";"));
		assertTrue(runTestInSameEvaluator("right(\"ab\", 5, \"-+\") == \"-+-ab\";"));
		assertTrue(runTestInSameEvaluator("right(\"ab\", 6, \"-+\") == \"-+-+ab\";"));
	}

	@Test
	public void size() {

		prepare("import String;");

		assertTrue(runTestInSameEvaluator("String::size(\"\") == 0;"));
		assertTrue(runTestInSameEvaluator("size(\"\") == 0;"));
		assertTrue(runTestInSameEvaluator("String::size(\"abc\") == 3;"));
	}

	@Test
	public void startsWith() {

		prepare("import String;");

		assertTrue(runTestInSameEvaluator("String::startsWith(\"abc\", \"abc\");"));
		assertTrue(runTestInSameEvaluator("startsWith(\"abc\", \"abc\");"));
		assertTrue(runTestInSameEvaluator("String::startsWith(\"abcdef\", \"abc\");"));
		assertFalse(runTestInSameEvaluator("String::startsWith(\"abcdef\", \"def\");"));
	}
	
	@Test
	public void substring(){
		prepare("import String;");
		assertTrue(runTestInSameEvaluator("substring(\"abc\", 0) == \"abc\";"));
		assertTrue(runTestInSameEvaluator("substring(\"abc\", 1) == \"bc\";"));
		assertTrue(runTestInSameEvaluator("substring(\"abc\", 2) == \"c\";"));
		assertTrue(runTestInSameEvaluator("substring(\"abc\", 3) == \"\";"));
		assertTrue(runTestInSameEvaluator("substring(\"abc\", 1, 2) == \"b\";"));
		assertTrue(runTestInSameEvaluator("substring(\"abc\", 1, 3) == \"bc\";"));
	}
	
	@Test(expected=RuntimeException.class)
	public void substringWrongIndex1(){
		prepare("import String;");
		assertTrue(runTestInSameEvaluator("substring(\"abc\", 4) == \"abc\";"));
	}
	
	@Test(expected=RuntimeException.class)
	public void substringWrongIndex2(){
		prepare("import String;");
		assertTrue(runTestInSameEvaluator("substring(\"abc\", 1, 4) == \"abc\";"));
	}

	@Test
	public void toLowerCase() {

		prepare("import String;");

		assertTrue(runTestInSameEvaluator("String::toLowerCase(\"\") == \"\";"));
		assertTrue(runTestInSameEvaluator("toLowerCase(\"\") ==  \"\";"));
		assertTrue(runTestInSameEvaluator("String::toLowerCase(\"ABC\") == \"abc\";"));
		assertTrue(runTestInSameEvaluator("String::toLowerCase(\"ABC123\") == \"abc123\";"));
	}

	@Test
	public void toUpperCase() {

		prepare("import String;");

		assertTrue(runTestInSameEvaluator("String::toUpperCase(\"\") == \"\";"));
		assertTrue(runTestInSameEvaluator("toUpperCase(\"\") == \"\";"));
		assertTrue(runTestInSameEvaluator("String::toUpperCase(\"abc\") == \"ABC\";"));
		assertTrue(runTestInSameEvaluator("String::toUpperCase(\"abc123\") == \"ABC123\";"));
	}
	
	@Test
	public void toInt(){
		prepare("import String;");
		
		assertTrue(runTestInSameEvaluator("toInt(\"0\") == 0;"));
		assertTrue(runTestInSameEvaluator("toInt(\"1\") == 1;"));
		assertTrue(runTestInSameEvaluator("toInt(\"0001\") == 1;"));
		assertTrue(runTestInSameEvaluator("toInt(\"-1\") == -1;"));
		assertTrue(runTestInSameEvaluator("toInt(\"12345\") == 12345;"));
	}
	
	@Test(expected=RuntimeException.class)
	public void toIntError(){
		prepare("import String;");
		assertTrue(runTestInSameEvaluator("toInt(\"abc\") == 0;"));
	}
	
	@Test
	public void toReal(){
		prepare("import String;");
		
		assertTrue(runTestInSameEvaluator("toReal(\"0.0\") == 0.0;"));
		assertTrue(runTestInSameEvaluator("toReal(\"1.0\") == 1.0;"));
		assertTrue(runTestInSameEvaluator("toReal(\"0001.0\") == 1.0;"));
		assertTrue(runTestInSameEvaluator("toReal(\"-1.0\") == -1.0;"));
		assertTrue(runTestInSameEvaluator("toReal(\"1.2345\") == 1.2345;"));
	}
	
	@Test(expected=RuntimeException.class)
	public void toRealError(){
		prepare("import String;");
		assertTrue(runTestInSameEvaluator("toReal(\"abc\") == 0;"));
	}
	
	@Test
	public void replaceAll(){
		prepare("import String;");
		assertTrue(runTestInSameEvaluator("replaceAll(\"a\", \"a\", \"A\") == \"A\";"));
		assertTrue(runTestInSameEvaluator("replaceAll(\"a\", \"x\", \"X\") == \"a\";"));
		assertTrue(runTestInSameEvaluator("replaceAll(\"a\", \"aa\", \"A\") == \"a\";"));
		
		assertTrue(runTestInSameEvaluator("replaceAll(\"abracadabra\", \"a\", \"A\") == \"AbrAcAdAbrA\";"));
		assertTrue(runTestInSameEvaluator("replaceAll(\"abracadabra\", \"a\", \"A\") == \"AbrAcAdAbrA\";"));
		assertTrue(runTestInSameEvaluator("replaceAll(\"abracadabra\", \"a\", \"AA\") == \"AAbrAAcAAdAAbrAA\";"));
		assertTrue(runTestInSameEvaluator("replaceAll(\"abracadabra\", \"ab\", \"AB\") == \"ABracadABra\";"));
	}
	
	@Test
	public void replaceFirst(){
		prepare("import String;");
		assertTrue(runTestInSameEvaluator("replaceFirst(\"a\", \"a\", \"A\") == \"A\";"));
		assertTrue(runTestInSameEvaluator("replaceFirst(\"a\", \"x\", \"X\") == \"a\";"));
		assertTrue(runTestInSameEvaluator("replaceFirst(\"a\", \"aa\", \"A\") == \"a\";"));
		assertTrue(runTestInSameEvaluator("replaceFirst(\"abracadabra\", \"a\", \"A\") == \"Abracadabra\";"));
		assertTrue(runTestInSameEvaluator("replaceFirst(\"abracadabra\", \"a\", \"AA\") == \"AAbracadabra\";"));
		assertTrue(runTestInSameEvaluator("replaceFirst(\"abracadabra\", \"ab\", \"AB\") == \"ABracadabra\";"));
	}
	
	@Test
	public void replaceLast(){
		prepare("import String;");
		assertTrue(runTestInSameEvaluator("replaceLast(\"a\", \"a\", \"A\") == \"A\";"));
		assertTrue(runTestInSameEvaluator("replaceLast(\"a\", \"x\", \"X\") == \"a\";"));
		assertTrue(runTestInSameEvaluator("replaceLast(\"a\", \"aa\", \"A\") == \"a\";"));
		assertTrue(runTestInSameEvaluator("replaceLast(\"abracadabra\", \"a\", \"A\") == \"abracadabrA\";"));
		assertTrue(runTestInSameEvaluator("replaceLast(\"abracadabra\", \"a\", \"AA\") == \"abracadabrAA\";"));
		assertTrue(runTestInSameEvaluator("replaceLast(\"abracadabra\", \"ab\", \"AB\") == \"abracadABra\";"));
	}
	
	@Test
	public void contains(){
		prepare("import String;");
		assertTrue(runTestInSameEvaluator("contains(\"abc\", \"a\");"));
		assertTrue(runTestInSameEvaluator("contains(\"abc\", \"c\");"));
		assertFalse(runTestInSameEvaluator("contains(\"abc\", \"x\");"));
		assertFalse(runTestInSameEvaluator("contains(\"abc\", \"xyzpqr\");"));
		assertTrue(runTestInSameEvaluator("contains(\"abracadabra\", \"bra\");"));
	}
	
	@Test
	public void findAll(){
		prepare("import String;");
		assertTrue(runTestInSameEvaluator("findAll(\"abc\", \"a\") == [0];"));
		assertTrue(runTestInSameEvaluator("findAll(\"abc\", \"c\") == [2];"));
		assertTrue(runTestInSameEvaluator("findAll(\"abc\", \"x\") == [];"));
		assertTrue(runTestInSameEvaluator("findAll(\"abc\", \"xyzpqr\") == [];"));
		assertTrue(runTestInSameEvaluator("findAll(\"abracadabra\", \"bra\") == [1, 8];"));
	}
	
	@Test
	public void findFirst(){
		prepare("import String;");
		assertTrue(runTestInSameEvaluator("findFirst(\"abc\", \"a\") == 0;"));
		assertTrue(runTestInSameEvaluator("findFirst(\"abc\", \"c\") == 2;"));
		assertTrue(runTestInSameEvaluator("findFirst(\"abc\", \"x\") == -1;"));
		assertTrue(runTestInSameEvaluator("findFirst(\"abc\", \"xyzpqr\") == -1;"));
		assertTrue(runTestInSameEvaluator("findFirst(\"abracadabra\", \"bra\") == 1;"));
	}
	
	@Test
	public void findLast(){
		prepare("import String;");
		assertTrue(runTestInSameEvaluator("findLast(\"abc\", \"a\") == 0;"));
		assertTrue(runTestInSameEvaluator("findLast(\"abc\", \"c\") == 2;"));
		assertTrue(runTestInSameEvaluator("findLast(\"abc\", \"x\") == -1;"));
		assertTrue(runTestInSameEvaluator("findLast(\"abc\", \"xyzpqr\") == -1;"));
		assertTrue(runTestInSameEvaluator("findLast(\"abracadabra\", \"bra\") == 8;"));
	}
}
