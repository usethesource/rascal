package test;

import java.io.IOException;

import junit.framework.TestCase;

public class StandardLibraryStringTests extends TestCase {
	
	private TestFramework tf = new TestFramework();
	
	public void testString() throws IOException {
		
		tf.prepare("import String;");
		
		System.err.println("String::charAt");
		
		assertTrue(tf.runTestInSameEvaluator("String::charAt(\"abc\", 0) == 97;"));
		assertTrue(tf.runTestInSameEvaluator("String::charAt(\"abc\", 1) == 98;"));
		assertTrue(tf.runTestInSameEvaluator("String::charAt(\"abc\", 2) == 99;"));
		assertTrue(tf.runTestInSameEvaluator("charAt(\"abc\", 0) == 97;"));
		
		System.err.println("String::endsWith");
		
		assertTrue(tf.runTestInSameEvaluator("String::endsWith(\"abc\", \"abc\");"));
		assertTrue(tf.runTestInSameEvaluator("endsWith(\"abc\", \"abc\");"));
		assertTrue(tf.runTestInSameEvaluator("String::endsWith(\"abcdef\", \"def\");"));
		assertFalse(tf.runTestInSameEvaluator("String::endsWith(\"abcdef\", \"abc\");"));
		
		System.err.println("String::reverse");
		
		assertTrue(tf.runTestInSameEvaluator("String::reverse(\"\") == \"\";"));
		assertTrue(tf.runTestInSameEvaluator("reverse(\"\") == \"\";"));
		assertTrue(tf.runTestInSameEvaluator("String::reverse(\"abc\") == \"cba\";"));
		
		System.err.println("String::size");
		
		assertTrue(tf.runTestInSameEvaluator("String::size(\"\") == 0;"));
		assertTrue(tf.runTestInSameEvaluator("size(\"\") == 0;"));
		assertTrue(tf.runTestInSameEvaluator("String::size(\"abc\") == 3;"));
		
		System.err.println("String::startsWith");
		
		assertTrue(tf.runTestInSameEvaluator("String::startsWith(\"abc\", \"abc\");"));
		assertTrue(tf.runTestInSameEvaluator("startsWith(\"abc\", \"abc\");"));
		assertTrue(tf.runTestInSameEvaluator("String::startsWith(\"abcdef\", \"abc\");"));
		assertFalse(tf.runTestInSameEvaluator("String::startsWith(\"abcdef\", \"def\");"));
		
		System.err.println("String::toLowerCase");
		
		assertTrue(tf.runTestInSameEvaluator("String::toLowerCase(\"\") == \"\";"));
		assertTrue(tf.runTestInSameEvaluator("toLowerCase(\"\") ==  \"\";"));
		assertTrue(tf.runTestInSameEvaluator("String::toLowerCase(\"ABC\") == \"abc\";"));
		assertTrue(tf.runTestInSameEvaluator("String::toLowerCase(\"ABC123\") == \"abc123\";"));
		
		System.err.println("String::toUpperCase");
		
		assertTrue(tf.runTestInSameEvaluator("String::toUpperCase(\"\") == \"\";"));
		assertTrue(tf.runTestInSameEvaluator("toUpperCase(\"\") == \"\";"));
		assertTrue(tf.runTestInSameEvaluator("String::toUpperCase(\"abc\") == \"ABC\";"));
		assertTrue(tf.runTestInSameEvaluator("String::toUpperCase(\"abc123\") == \"ABC123\";"));
	}
}
