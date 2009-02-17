package test.StandardLibraryTests;

import org.junit.Test;

import test.TestFramework;
import static org.junit.Assert.*;

public class StringTests extends TestFramework {

	@Test
	public void testStringCenter() {
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
	public void testStringCharAt() {

		prepare("import String;");

		assertTrue(runTestInSameEvaluator("String::charAt(\"abc\", 0) == 97;"));
		assertTrue(runTestInSameEvaluator("String::charAt(\"abc\", 1) == 98;"));
		assertTrue(runTestInSameEvaluator("String::charAt(\"abc\", 2) == 99;"));
		assertTrue(runTestInSameEvaluator("charAt(\"abc\", 0) == 97;"));
	}

	@Test
	public void testStringEndsWith() {

		prepare("import String;");

		assertTrue(runTestInSameEvaluator("String::endsWith(\"abc\", \"abc\");"));
		assertTrue(runTestInSameEvaluator("endsWith(\"abc\", \"abc\");"));
		assertTrue(runTestInSameEvaluator("String::endsWith(\"abcdef\", \"def\");"));
		assertFalse(runTestInSameEvaluator("String::endsWith(\"abcdef\", \"abc\");"));
	}

	@Test
	public void testStringLeft() {
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
	public void testStringReverse() {

		prepare("import String;");

		assertTrue(runTestInSameEvaluator("String::reverse(\"\") == \"\";"));
		assertTrue(runTestInSameEvaluator("reverse(\"\") == \"\";"));
		assertTrue(runTestInSameEvaluator("String::reverse(\"abc\") == \"cba\";"));
	}

	@Test
	public void testStringRight() {
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
	public void testStringSize() {

		prepare("import String;");

		assertTrue(runTestInSameEvaluator("String::size(\"\") == 0;"));
		assertTrue(runTestInSameEvaluator("size(\"\") == 0;"));
		assertTrue(runTestInSameEvaluator("String::size(\"abc\") == 3;"));
	}

	@Test
	public void testStringStartsWith() {

		prepare("import String;");

		assertTrue(runTestInSameEvaluator("String::startsWith(\"abc\", \"abc\");"));
		assertTrue(runTestInSameEvaluator("startsWith(\"abc\", \"abc\");"));
		assertTrue(runTestInSameEvaluator("String::startsWith(\"abcdef\", \"abc\");"));
		assertFalse(runTestInSameEvaluator("String::startsWith(\"abcdef\", \"def\");"));
	}

	@Test
	public void testStringToLowerCase() {

		prepare("import String;");

		assertTrue(runTestInSameEvaluator("String::toLowerCase(\"\") == \"\";"));
		assertTrue(runTestInSameEvaluator("toLowerCase(\"\") ==  \"\";"));
		assertTrue(runTestInSameEvaluator("String::toLowerCase(\"ABC\") == \"abc\";"));
		assertTrue(runTestInSameEvaluator("String::toLowerCase(\"ABC123\") == \"abc123\";"));
	}

	@Test
	public void testStringToUpperCase() {

		prepare("import String;");

		assertTrue(runTestInSameEvaluator("String::toUpperCase(\"\") == \"\";"));
		assertTrue(runTestInSameEvaluator("toUpperCase(\"\") == \"\";"));
		assertTrue(runTestInSameEvaluator("String::toUpperCase(\"abc\") == \"ABC\";"));
		assertTrue(runTestInSameEvaluator("String::toUpperCase(\"abc123\") == \"ABC123\";"));
	}
}
