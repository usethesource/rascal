package org.rascalmpl.test.functionality;

import java.util.ArrayList;

import org.junit.Test;
import org.rascalmpl.test.infrastructure.TestFramework;


public class MemoizationTests extends TestFramework {
	@Test
	public void memoryIsReleased() throws InterruptedException {
		prepare("@memo list[int] OneMB(int w) = [ w | i <- [0..(1024*1024/8)]];");
		runTestInSameEvaluator("( true | it && OneMB(i)[0] == i | i <- [0..10])");
		// Force an OoM
		// use all memory to cause the memoization to cleanup
	    try {
	        final ArrayList<Object[]> allocations = new ArrayList<Object[]>();
	        while(true)
	            allocations.add( new Object[(int) Runtime.getRuntime().maxMemory()] );
	    } catch( OutOfMemoryError e ) {
	        // great!
	    }
		// actually hit the cache to cause a cleanup
		runTestInSameEvaluator("OneMB(1)[0]==1");
	}

	@Test
	public void manyEntries() throws InterruptedException {
		prepare("import String;");
		prepareMore("@memo str dup(str s) = s + s;");
		runTestInSameEvaluator("(true | it && dup(s) == s + s | i <- [0..30000], str s := stringChar(i))");   ;
	}

}
