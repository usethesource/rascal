package org.rascalmpl.test.functionality;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.rascalmpl.test.infrastructure.TestFramework;


public class MemoizationTests extends TestFramework {
	
	//@Test
    // too flaky, depends on memory available to the tester, only enable when you want to change the memo functionality
	public void memoryIsReleased() throws InterruptedException {
	    prepare("int n = 0;");
		prepareMore("@memo list[int] OneMB(int w) { n += 1; return [ n | i <- [0..(1024*1024*2)]];}");
		assertTrue("Let's fill up the memory a bit", runTestInSameEvaluator("( true | it && OneMB(i)[0] == i + 1 | i <- [0..3])"));
		// Force an OoM
		// use all memory to cause the memoization to cleanup
		for (int i =0; i< 5; i++) {
            try {
                final ArrayList<Object[]> allocations = new ArrayList<Object[]>();
                while(true)
                    allocations.add( new Object[(int) Math.min(Integer.MAX_VALUE, Runtime.getRuntime().maxMemory())] );
            } catch( OutOfMemoryError e ) {
                // great!
            }
            System.gc();
            Thread.sleep(10);
		}
		TimeUnit.SECONDS.sleep(6); // note should be more than the frequency of the cleanup thread
        System.gc();
		TimeUnit.SECONDS.sleep(6); // note should be more than the frequency of the cleanup thread
        System.gc();
		// actually hit the cache to cause a cleanup
		assertTrue("Should be run again, since GC happened", runTestInSameEvaluator("OneMB(1)[0] != 2"));
	}

	@Test
	public void memoryIsReleasedTimeout() throws InterruptedException {
	    prepare("int n = 0;");
	    prepareMore("import util::Memo;");
		prepareMore("@memo=expireAfter(seconds=1) int calc(int w) { n +=1; return n; }");
		assertTrue("Memo works", runTestInSameEvaluator("( true | it && calc(1) == 1 | i <- [0..100])"));
		assertTrue("Memo works", runTestInSameEvaluator("calc(1) == 1"));
		TimeUnit.SECONDS.sleep(2); 
		assertTrue("Entry should be cleared by now", runTestInSameEvaluator("calc(1) == 2"));
	}

	@Test
	public void memoryIsReleasedEntries() throws InterruptedException {
	    prepare("int n = 0;");
	    prepareMore("import util::Memo;");
		prepareMore("@memo=maximumSize(100) int calc(int w) { n +=1; return n; }");
		assertTrue("Just storing something in range", runTestInSameEvaluator("( true | it && calc(i) == i + 1 | i <- [0..100])"));
		assertTrue("Memo works", runTestInSameEvaluator("calc(1) == 2"));
		assertTrue("Memo works", runTestInSameEvaluator("calc(2) == 3"));
		// now we run a lot more calcs, so that the memo of old cases might be cleared
		prepareMore("for (i <- [0..300]) { calc(100 + i); }");
		TimeUnit.SECONDS.sleep(6); // note should be more than the frequency of the cleanup thread
		// note that since the Caffeine cache is smart, it might pin 1 and 2 longer, so we ask for another result that should have been cleared by now
		assertTrue("Memo should have been cleared", runTestInSameEvaluator("calc(3) != 4"));
	}

	@Test
	public void memoryIsReleasedCombination() throws InterruptedException {
	    prepare("int n = 0;");
	    prepareMore("import util::Memo;");
		prepareMore("@memo={expireAfter(seconds=1),maximumSize(200)} int calc(int w) { n +=1; return n; }");
		assertTrue("Memo works", runTestInSameEvaluator("( true | it && calc(i) == i + 1 | i <- [0..100])"));
		assertTrue("Memo works", runTestInSameEvaluator("calc(1) == 2"));
		prepareMore("for (i <- [0..100]) { calc(100 + i); }");
		TimeUnit.SECONDS.sleep(2); // note should be more than the frequency of the cleanup thread
		prepareMore("for (i <- [0..100]) { calc(200 + i); }");
		TimeUnit.SECONDS.sleep(2); // note should be more than the frequency of the cleanup thread
		assertTrue("Entry should be cleared by now", runTestInSameEvaluator("calc(1) != 2"));
	}

	@Test
	public void manyEntries() throws InterruptedException {
		prepare("import String;");
		prepareMore("@memo str dup(str s) = s + s;");
		assertTrue(runTestInSameEvaluator("(true | it && dup(s) == s + s | i <- [0..30000], str s := stringChar(i))"));  
	}

}
