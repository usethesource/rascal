package test;

import junit.framework.TestCase;
import java.io.IOException;

public class RegExpTests extends TestCase {
	
	private TestFramework tf = new TestFramework();
	
	public void testMatch() throws IOException {
		assertTrue(tf.runTest("/abc/ ~= \"abc\";"));
		assertFalse(tf.runTest("/def/ ~= \"abc\";"));
		assertTrue(tf.runTest("/def/ ~! \"abc\";"));
		assertTrue(tf.runTest("/[a-z]+/ ~= \"abc\";"));
		assertTrue(tf.runTest("/.*is.*/ ~= \"Rascal is marvelous\";"));
		assertTrue(tf.runTest("/@.*@/ ~= \"@ abc @\";"));
		
		assertTrue(tf.runTest("(/<x:[a-z]+>/ ~= \"abc\") && (x == \"abc\");"));
		assertTrue(tf.runTest("(/if<tst:.*>then<th:.*>fi/ ~= \"if a > b then c fi\") " +
				           "&& (tst == \" a > b \") && (th == \" c \");"));

		assertTrue(tf.runTest("(/<l:.*>[Rr][Aa][Ss][Cc][Aa][Ll]<r:.*>/ ~= \"RASCAL is marvelous\")" +
				            "&& (l == \"\") && (r == \" is marvelous\");"));

	}
}
