package test;

import junit.framework.TestCase;
import java.io.IOException;

public class PatternTests extends TestCase {
	
	private TestFramework tf = new TestFramework();
	
	public void testMatchLiteral() throws IOException {

		assertTrue(tf.runTest("true     ~= true;"));
		assertFalse(tf.runTest("true    ~= false;"));
		assertTrue(tf.runTest("true     ~! false;"));
		assertFalse(tf.runTest("true    ~= 1;"));
		assertTrue(tf.runTest("true     ~! 1;"));
		assertFalse(tf.runTest("\"abc\" ~= true;"));
		assertTrue(tf.runTest("\"abc\"  ~! true;"));
		
		assertTrue(tf.runTest("1        ~= 1;"));
		assertFalse(tf.runTest("2       ~= 1;"));
		assertTrue(tf.runTest("2        ~! 1;"));
		assertFalse(tf.runTest("true    ~= 1;"));
		assertTrue(tf.runTest("true     ~! 1;"));
		assertFalse(tf.runTest("1.0     ~= 1;"));
		assertTrue(tf.runTest("1.0      ~! 1;"));
		assertFalse(tf.runTest("\"abc\" ~= 1;"));
		assertTrue(tf.runTest("\"abc\"  ~! 1;"));
		
		assertTrue(tf.runTest("1.5      ~= 1.5;"));
		assertFalse(tf.runTest("2.5     ~= 1.5;"));
		assertTrue(tf.runTest("2.5      ~! 1.5;"));
		assertFalse(tf.runTest("true    ~= 1.5;"));
		assertTrue(tf.runTest("true     ~! 1.5;"));
		assertFalse(tf.runTest("2       ~= 1.5;"));
		assertTrue(tf.runTest("2        ~! 1.5;"));
		assertFalse(tf.runTest("1.0     ~= 1.5;"));
		assertTrue(tf.runTest("1.0      ~! 1.5;"));
		assertFalse(tf.runTest("\"abc\" ~= 1.5;"));
		assertTrue(tf.runTest("\"abc\"  ~! 1.5;"));
		
		assertTrue(tf.runTest("\"abc\"  ~= \"abc\";"));
		assertFalse(tf.runTest("\"def\" ~= \"abc\";"));
		assertTrue(tf.runTest("\"def\"  ~! \"abc\";"));
		assertFalse(tf.runTest("true    ~= \"abc\";"));
		assertTrue(tf.runTest("true     ~! \"abc\";"));
		assertFalse(tf.runTest("1       ~= \"abc\";"));
		assertTrue(tf.runTest("1        ~! \"abc\";"));
		assertFalse(tf.runTest("1.5     ~= \"abc\";"));
		assertTrue(tf.runTest("1.5      ~! \"abc\";"));
	}
	
	public void testMatchTuple() throws IOException {
		assertTrue(tf.runTest("<1>           ~= <1>;"));
		assertTrue(tf.runTest("<1, \"abc\">  ~= <1, \"abc\">;"));
		assertFalse(tf.runTest("<2>          ~= <1>;"));
		assertTrue(tf.runTest("<2>           ~! <1>;"));
		assertFalse(tf.runTest("<1,2>        ~= <1>;"));
		assertTrue(tf.runTest("<1,2>         ~! <1>;"));
		assertFalse(tf.runTest("<1, \"abc\"> ~= <1, \"def\">;"));
		assertTrue(tf.runTest("<1, \"abc\">  ~! <1, \"def\">;"));
	}
	
	public void testMatchTree() throws IOException {
		assertTrue(tf.runTest("f(1)                   ~= f(1);"));
		assertTrue(tf.runTest("f(1, g(\"abc\"), true) ~= f(1, g(\"abc\"), true);"));
		assertFalse(tf.runTest("1                     ~= f(1);"));
		assertTrue(tf.runTest("1                      ~! f(1);"));
		assertFalse(tf.runTest("1.5                   ~= f(1);"));
		assertTrue(tf.runTest("1.5                    ~! f(1);"));
		assertFalse(tf.runTest("\"abc\"               ~= f(1);"));
		assertTrue(tf.runTest("\"abc\"                ~! f(1);"));
		assertFalse(tf.runTest("g(1)                  ~= f(1);"));
		assertTrue(tf.runTest("g(1)                   ~! f(1);"));
		assertFalse(tf.runTest("f(1, 2)               ~= f(1);"));
		assertTrue(tf.runTest("f(1, 2)                ~! f(1);"));
	}
	
	public void testMatchVariable() throws IOException {
		assertTrue(tf.runTest("(n ~= 1) && (n == 1);"));
		assertTrue(tf.runTest("{int n = 1; (n ~= 1) && (n == 1);};"));
		assertTrue(tf.runTest("{int n = 1; (n ~! 2) && (n == 1);};"));
		assertTrue(tf.runTest("{int n = 1; (n ~! \"abc\") && (n == 1);};"));
		
		assertTrue(tf.runTest("(f(n) ~= f(1)) && (n == 1);"));
		assertTrue(tf.runTest("{int n = 1; (f(n) ~= f(1)) && (n == 1);};"));
	}
	
}
