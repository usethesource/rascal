package test;

import java.io.IOException;

import junit.framework.TestCase;

public class StandardLibraryStringTests extends TestCase {

	private static TestFramework tf = new TestFramework("import String;");
	
	public void testStringCenter() throws IOException {
		tf = new TestFramework("import String;");
		
		assertTrue(tf.runTestInSameEvaluator("center(\"a\", 0) == \"a\";"));
		assertTrue(tf.runTestInSameEvaluator("center(\"a\", 1) == \"a\";"));
		assertTrue(tf.runTestInSameEvaluator("center(\"a\", 2) == \"a \";"));
		assertTrue(tf.runTestInSameEvaluator("center(\"a\", 3) == \" a \";"));
		
		assertTrue(tf.runTestInSameEvaluator("center(\"ab\", 0, \"-\") == \"ab\";"));
		assertTrue(tf.runTestInSameEvaluator("center(\"ab\", 1, \"-\") == \"ab\";"));
		assertTrue(tf.runTestInSameEvaluator("center(\"ab\", 2, \"-\") == \"ab\";"));
		assertTrue(tf.runTestInSameEvaluator("center(\"ab\", 3, \"-\") == \"ab-\";"));
		assertTrue(tf.runTestInSameEvaluator("center(\"ab\", 4, \"-\") == \"-ab-\";"));
		
		assertTrue(tf.runTestInSameEvaluator("center(\"ab\", 3, \"-+\") == \"ab-\";"));
		assertTrue(tf.runTestInSameEvaluator("center(\"ab\", 4, \"-+\") == \"-ab-\";"));
		assertTrue(tf.runTestInSameEvaluator("center(\"ab\", 5, \"-+\") == \"-ab-+\";"));
		assertTrue(tf.runTestInSameEvaluator("center(\"ab\", 6, \"-+\") == \"-+ab-+\";"));
	}
	
	public void testStringCharAt() throws IOException {
		
		tf = new TestFramework("import String;");

		assertTrue(tf
				.runTestInSameEvaluator("String::charAt(\"abc\", 0) == 97;"));
		assertTrue(tf
				.runTestInSameEvaluator("String::charAt(\"abc\", 1) == 98;"));
		assertTrue(tf
				.runTestInSameEvaluator("String::charAt(\"abc\", 2) == 99;"));
		assertTrue(tf.runTestInSameEvaluator("charAt(\"abc\", 0) == 97;"));
	}
	
	public void testStringEndsWith() throws IOException {
		
		tf = new TestFramework("import String;");

		assertTrue(tf
				.runTestInSameEvaluator("String::endsWith(\"abc\", \"abc\");"));
		assertTrue(tf.runTestInSameEvaluator("endsWith(\"abc\", \"abc\");"));
		assertTrue(tf
				.runTestInSameEvaluator("String::endsWith(\"abcdef\", \"def\");"));
		assertFalse(tf
				.runTestInSameEvaluator("String::endsWith(\"abcdef\", \"abc\");"));
	}

	public void testStringLeft() throws IOException {
		tf = new TestFramework("import String;");
		
		assertTrue(tf.runTestInSameEvaluator("left(\"a\", 0) == \"a\";"));
		assertTrue(tf.runTestInSameEvaluator("left(\"a\", 1) == \"a\";"));
		assertTrue(tf.runTestInSameEvaluator("left(\"a\", 2) == \"a \";"));
		
		assertTrue(tf.runTestInSameEvaluator("left(\"ab\", 0, \"-\") == \"ab\";"));
		assertTrue(tf.runTestInSameEvaluator("left(\"ab\", 1, \"-\") == \"ab\";"));
		assertTrue(tf.runTestInSameEvaluator("left(\"ab\", 2, \"-\") == \"ab\";"));
		assertTrue(tf.runTestInSameEvaluator("left(\"ab\", 3, \"-\") == \"ab-\";"));
		assertTrue(tf.runTestInSameEvaluator("left(\"ab\", 4, \"-\") == \"ab--\";"));
		
		assertTrue(tf.runTestInSameEvaluator("left(\"ab\", 3, \"-+\") == \"ab-\";"));
		assertTrue(tf.runTestInSameEvaluator("left(\"ab\", 4, \"-+\") == \"ab-+\";"));
		assertTrue(tf.runTestInSameEvaluator("left(\"ab\", 5, \"-+\") == \"ab-+-\";"));
		assertTrue(tf.runTestInSameEvaluator("left(\"ab\", 6, \"-+\") == \"ab-+-+\";"));
	}

	public void testStringReverse() throws IOException {
		
		tf = new TestFramework("import String;");

		assertTrue(tf.runTestInSameEvaluator("String::reverse(\"\") == \"\";"));
		assertTrue(tf.runTestInSameEvaluator("reverse(\"\") == \"\";"));
		assertTrue(tf
				.runTestInSameEvaluator("String::reverse(\"abc\") == \"cba\";"));
	}

	public void testStringRight() throws IOException {
		tf = new TestFramework("import String;");
		
		assertTrue(tf.runTestInSameEvaluator("right(\"a\", 0) == \"a\";"));
		assertTrue(tf.runTestInSameEvaluator("right(\"a\", 1) == \"a\";"));
		assertTrue(tf.runTestInSameEvaluator("right(\"a\", 2) == \" a\";"));
		
		assertTrue(tf.runTestInSameEvaluator("right(\"ab\", 0, \"-\") == \"ab\";"));
		assertTrue(tf.runTestInSameEvaluator("right(\"ab\", 1, \"-\") == \"ab\";"));
		assertTrue(tf.runTestInSameEvaluator("right(\"ab\", 2, \"-\") == \"ab\";"));
		assertTrue(tf.runTestInSameEvaluator("right(\"ab\", 3, \"-\") == \"-ab\";"));
		assertTrue(tf.runTestInSameEvaluator("right(\"ab\", 4, \"-\") == \"--ab\";"));
		
		assertTrue(tf.runTestInSameEvaluator("right(\"ab\", 3, \"-+\") == \"-ab\";"));
		assertTrue(tf.runTestInSameEvaluator("right(\"ab\", 4, \"-+\") == \"-+ab\";"));
		assertTrue(tf.runTestInSameEvaluator("right(\"ab\", 5, \"-+\") == \"-+-ab\";"));
		assertTrue(tf.runTestInSameEvaluator("right(\"ab\", 6, \"-+\") == \"-+-+ab\";"));
	}

	public void testStringSize() throws IOException {
		
		tf = new TestFramework("import String;");

		assertTrue(tf.runTestInSameEvaluator("String::size(\"\") == 0;"));
		assertTrue(tf.runTestInSameEvaluator("size(\"\") == 0;"));
		assertTrue(tf.runTestInSameEvaluator("String::size(\"abc\") == 3;"));
	}

	public void testStringStartsWith() throws IOException {
		
		tf = new TestFramework("import String;");

		assertTrue(tf
				.runTestInSameEvaluator("String::startsWith(\"abc\", \"abc\");"));
		assertTrue(tf.runTestInSameEvaluator("startsWith(\"abc\", \"abc\");"));
		assertTrue(tf
				.runTestInSameEvaluator("String::startsWith(\"abcdef\", \"abc\");"));
		assertFalse(tf
				.runTestInSameEvaluator("String::startsWith(\"abcdef\", \"def\");"));
	}

	public void testStringToLowerCase() throws IOException {
		
		tf = new TestFramework("import String;");

		assertTrue(tf
				.runTestInSameEvaluator("String::toLowerCase(\"\") == \"\";"));
		assertTrue(tf.runTestInSameEvaluator("toLowerCase(\"\") ==  \"\";"));
		assertTrue(tf
				.runTestInSameEvaluator("String::toLowerCase(\"ABC\") == \"abc\";"));
		assertTrue(tf
				.runTestInSameEvaluator("String::toLowerCase(\"ABC123\") == \"abc123\";"));
	}

	public void testStringToUpperCase() throws IOException {
		
		tf = new TestFramework("import String;");

		assertTrue(tf
				.runTestInSameEvaluator("String::toUpperCase(\"\") == \"\";"));
		assertTrue(tf.runTestInSameEvaluator("toUpperCase(\"\") == \"\";"));
		assertTrue(tf
				.runTestInSameEvaluator("String::toUpperCase(\"abc\") == \"ABC\";"));
		assertTrue(tf
				.runTestInSameEvaluator("String::toUpperCase(\"abc123\") == \"ABC123\";"));
	}
}
