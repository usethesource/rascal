package test;

import junit.framework.TestCase;
import java.io.IOException;

public class StatementTests extends TestCase {
	
	private static TestFramework tf = new TestFramework();

    public void testAssert() throws IOException {
    	assertTrue(tf.runTest("assert \"1\": 3 > 2;"));
	}
	
	public void testAssignment() throws IOException {
		assertTrue(tf.runTest("{int x = 3; assert \"a\": x == 3;}"));
		assertTrue(tf.runTest("{int x = 3; x = 4; assert \"a\": x == 4;}"));
		assertTrue(tf.runTest("{<x, y> = <3, 4>; assert \"a\": (x == 3) && (y == 4);}"));
		assertTrue(tf.runTest("{<x, y, z> = <3, 4, 5>; assert \"a\": (x == 3) && (y == 4) && (z == 5);}"));
		assertTrue(tf.runTest("{<x, y> = <3, 4>; x = 5; assert \"a\": (x == 5) && (y == 4);}"));
		
		
//		assertTrue(tf.runTest("{int x = 3; x += 2; assert \"a\": x == 5;}"));	
//		assertTrue(tf.runTest("{int x = 3; x -= 2; assert \"a\": x == 1;}"));	
//		assertTrue(tf.runTest("{int x = 3; x *= 2; assert \"a\": x == 6;}"));	
//		assertTrue(tf.runTest("{int x = 3; x /= 2; assert \"a\": x == 1;}"));	
//		assertTrue(tf.runTest("{int x = 3; x %= 2; assert \"a\": x == 1;}"));	
		
		assertTrue(tf.runTest("{list[int] x = [0,1,2]; assert \"a\": x == [0,1,2];}"));
		assertTrue(tf.runTest("{list[int] x = [0,1,2]; assert \"a\": x[0] == 0;}"));
		assertTrue(tf.runTest("{list[int] x = [0,1,2]; assert \"a\": x[1] == 1;}"));
		assertTrue(tf.runTest("{list[int] x = [0,1,2]; assert \"a\": x[2] == 2;}"));
		assertTrue(tf.runTest("{list[int] x = [0,1,2]; x[1] = 10; assert \"a\": (x[0] == 0) && (x[1] == 10) && (x[2] == 2);}"));
		
		assertTrue(tf.runTest("{map[int,int] x = (0:0,1:10,2:20); assert \"a\": x == (0:0,1:10,2:20);}"));
//		assertTrue(tf.runTest("{map[int,int] x = (0:0,1:10,2:20); x[1] = 15; assert \"a\": (x[0] == 0) && (x[1] == 15) && (x[2] == 20);}"));
		
		assertTrue(tf.runTest("{set[int] x = {0,1,2}; assert \"a\": x == {0,1,2};}"));
		assertTrue(tf.runTest("{set[int] x = {0,1,2}; x = x + {3,4}; assert \"a\": x == {0,1,2, 3,4};}"));
		
		assertTrue(tf.runTest("{rel[str,list[int]] s = {<\"a\", [1,2]>, <\"b\", []>, <\"c\", [4,5,6]>}; assert \"a\": s != {};}"));
		assertTrue(tf.runTest("{rel[str,list[int]] s = {<\"a\", [1,2]>, <\"b\", []>, <\"c\", [4,5,6]>}; assert \"a\": s != {};}"));
	}
	
	public void testBlock() throws IOException {
	}
	
	public void testBreak() throws IOException {
//		assertTrue(tf.runTest("{int n = 0; while(n < 10){ n = n + 1; break;}; assert \"a\": n == 1;};"));
	}
	
	public void testContinue() throws IOException {
	}
	
	public void testDoWhile()throws IOException {
		assertTrue(tf.runTest("{int n = 0; m = 2; do {m = m * m; n = n + 1;} while (n < 1); assert \"a\": (n == 1) && (m == 4);}"));
		assertTrue(tf.runTest("{int n = 0; m = 2; do {m = m * m; n = n + 1;} while (n < 3); assert \"a\": m == 256;}"));
	}
	
	public void testFail() throws IOException {
	}
	
	public void testFirst() throws IOException {
	}
	
	public void testFor() throws IOException {
		assertTrue(tf.runTest("{int n = 0; for(int i:[1,2,3,4]){ n = n + i;} assert \"a\": n == 10;}"));
		assertTrue(tf.runTest("{int n = 0; for(int i:[1,2,3,4], n <= 3){ n = n + i;} assert \"a\": n == 6;}"));
	}
	public void testIfThen() throws IOException {
		assertTrue(tf.runTest("{int n = 10; if(n < 10){n = n - 4;} assert \"a\": n == 10;}"));
		assertTrue(tf.runTest("{int n = 10; if(n < 15){n = n - 4;} assert \"a\": n == 6;}"));
	}
	
	public void testIfThenElse() throws IOException {
		assertTrue(tf.runTest("{int n = 10; if(n < 10){n = n - 4;} else { n = n + 4;} assert \"a\": n == 14;}"));
		assertTrue(tf.runTest("{int n = 12; if(n < 10){n = n - 4;} else { n = n + 4;} assert \"a\": n == 16;}"));
	}
	
	public void testSwitch() throws IOException {
		assertTrue(tf.runTest("{int n = 0; switch(2){ case 2: n = 2; case 4: n = 4; case 6: n = 6; default: n = 10;} assert \"a\": n == 2;}"));
		assertTrue(tf.runTest("{int n = 0; switch(4){ case 2: n = 2; case 4: n = 4; case 6: n = 6; default: n = 10;} assert \"a\": n == 4;}"));
		assertTrue(tf.runTest("{int n = 0; switch(6){ case 2: n = 2; case 4: n = 4; case 6: n = 6; default: n = 10;} assert \"a\": n == 6;}"));
		assertTrue(tf.runTest("{int n = 0; switch(8){ case 2: n = 2; case 4: n = 4; case 6: n = 6; default: n = 10;} assert \"a\": n == 10;}"));
		
	}
//TODO: currently loops :-(
	public void xxxtestWhile() throws IOException {
		assertTrue(tf.runTest("{int n = 0; int m = 2; while(n != 0){ m = m * m;}; assert \"a\": (n == 0)&& (m == 2);}"));
		assertTrue(tf.runTest("{int n = 0; int m = 2; while(n < 3){ m = m * m; n = n + 1;}; assert \"a\": (n ==3) && (m == 256);}"));
	}
	
}
