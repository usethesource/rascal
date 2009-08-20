package test.StandardLibraryTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import test.TestFramework;

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
}
