package test;

import java.io.IOException;

import junit.framework.TestCase;

public class StandardLibraryListTests extends TestCase {
	
	private TestFramework tf = new TestFramework();
	
	public void testList() throws IOException {
		
		tf.prepare("import List;");
				
		System.err.println("List::addAt");
		
		assertTrue(tf.runTestInSameEvaluator("List::addAt(1, 0, []) == [1];"));
		assertTrue(tf.runTestInSameEvaluator("add(1, 0, []) == [1];"));
		assertTrue(tf.runTestInSameEvaluator("List::addAt(1, 1, [2,3]) == [2,1, 3];"));
		assertTrue(tf.runTestInSameEvaluator("addAt(1, 1, [2,3]) == [2, 1, 3];"));
		assertTrue(tf.runTestInSameEvaluator("List::addAt(1, 2, [2,3]) == [2,3,1];"));
		assertTrue(tf.runTestInSameEvaluator("addAt(1, 2, [2,3]) == [2, 3, 1];"));
				
		System.err.println("List::average");
		
		//assertTrue(tf.runTestInSameEvaluator("{int N = List::average([],0); N == 0;};"));
		//assertTrue(tf.runTestInSameEvaluator("{int N = average([],0); N == 0;};"));
		//assertTrue(tf.runTestInSameEvaluator("{int N = List::average([1],0); N == 1;};"));
		//assertTrue(tf.runTestInSameEvaluator("{int N = List::average([1, 3],0); N == 4;};"));

		System.err.println("List::first");
		
		assertTrue(tf.runTestInSameEvaluator("{List::first([1]) == 1;};"));
		assertTrue(tf.runTestInSameEvaluator("{first([1]) == 1;};"));
		assertTrue(tf.runTestInSameEvaluator("{List::first([1, 2]) == 1;};"));	
		
		System.err.println("List::getOneFrom");
		
		assertTrue(tf.runTestInSameEvaluator("{int N = List::getOneFrom([1]); N == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{int N = getOneFrom([1]); N == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{int N = List::getOneFrom([1,2]); (N == 1) || (N == 2);}"));
		assertTrue(tf.runTestInSameEvaluator("{int N = List::getOneFrom([1,2,3]); (N == 1) || (N == 2) || (N == 3);}"));
		assertTrue(tf.runTestInSameEvaluator("{double D = List::getOneFrom([1.0,2.0]); (D == 1.0) || (D == 2.0);}"));
		assertTrue(tf.runTestInSameEvaluator("{str S = List::getOneFrom([\"abc\",\"def\"]); (S == \"abc\") || (S == \"def\");}"));
		
		System.err.println("List::mapper");
		
		//assertTrue(tf.runTestNoClean("{int inc(int n) {return n + 1;} mapper([1, 2, 3], #inc) == [2, 4, 6];};"));
		//assertTrue(tf.runTestNoClean("{int inc(int n) {return n + 1;} List::mapper([1, 2, 3], #inc) == [2, 4, 6];};"));
		
		System.err.println("List::max");
		
		assertTrue(tf.runTestInSameEvaluator("{List::max([1, 2, 3, 2, 1]) == 3;};"));
		assertTrue(tf.runTestInSameEvaluator("{max([1, 2, 3, 2, 1]) == 3;};"));
		
		System.err.println("List::min");
		
		assertTrue(tf.runTestInSameEvaluator("{List::min([1, 2, 3, 2, 1]) == 1;};"));
		assertTrue(tf.runTestInSameEvaluator("{min([1, 2, 3, 2, 1]) == 1;};"));
		
		System.err.println("List::multiply");
		
		//assertTrue(tf.runTestInSameEvaluator("{multiply([1, 2, 3, 4], 1) == 24;};"));
		//assertTrue(tf.runTestInSameEvaluator("{List::multiply([1, 2, 3, 4], 1) == 24;};"));
		
		System.err.println("List::reducer");
		
		//assertTrue(tf.runTestInSameEvaluator("{reducer([1, 2, 3, 4], #1, 0) == 10;};"));
		//assertTrue(tf.runTestInSameEvaluator("{List::reducer([1, 2, 3, 4], #1, 0) == 10;};"));
		
		System.err.println("List::rest");
		
		assertTrue(tf.runTestInSameEvaluator("{List::rest([1]) == [];};"));
		assertTrue(tf.runTestInSameEvaluator("{rest([1]) == [];};"));
		assertTrue(tf.runTestInSameEvaluator("{List::rest([1, 2]) == [2];};"));
		
		System.err.println("List::reverse");
		
		assertTrue(tf.runTestInSameEvaluator("{List::reverse([]) == [];};"));
		assertTrue(tf.runTestInSameEvaluator("{reverse([]) == [];};"));
		assertTrue(tf.runTestInSameEvaluator("{List::reverse([1]) == [1];};"));
		assertTrue(tf.runTestInSameEvaluator("{List::reverse([1,2,3]) == [3,2,1];};"));
		
		System.err.println("List::size");
		
		assertTrue(tf.runTestInSameEvaluator("{List::size([]) == 0;};"));
		assertTrue(tf.runTestInSameEvaluator("{size([]) == 0;};"));
		assertTrue(tf.runTestInSameEvaluator("{List::size([1]) == 1;};"));
		assertTrue(tf.runTestInSameEvaluator("{List::size([1,2,3]) == 3;};"));
		
		System.err.println("List::sort");
		
		assertTrue(tf.runTestInSameEvaluator("{List::sort([]) == [];};"));
		assertTrue(tf.runTestInSameEvaluator("{sort([]) == [];};"));
		assertTrue(tf.runTestInSameEvaluator("{List::sort([1]) == [1];};"));
		assertTrue(tf.runTestInSameEvaluator("{sort([1]) == [1];};"));
		assertTrue(tf.runTestInSameEvaluator("{List::sort([2, 1]) == [1,2];};"));
		assertTrue(tf.runTestInSameEvaluator("{sort([2, 1]) == [1,2];};"));
		assertTrue(tf.runTestInSameEvaluator("{List::sort([2,-1,4,-2,3]) == [-1,-2,2,3, 4];};"));
		assertTrue(tf.runTestInSameEvaluator("{sort([2,-1,4,-2,3]) == [-1,-2,2,3, 4];};"));
		
		System.err.println("List::sum");
		
		//assertTrue(tf.runTestInSameEvaluator("{sum([1,2,3],0) == 6;};"));
		//assertTrue(tf.runTestInSameEvaluator("{List::sum([1,2,3], 0) == 6;};"));
		
		//assertTrue(tf.runTestInSameEvaluator("{List::sum([], 0) == 0;};"));
		//assertTrue(tf.runTestInSameEvaluator("{List::sum([], 0) == 0;};"));
		//assertTrue(tf.runTestInSameEvaluator("{List::sum([1], 0) == 1;};"));
		//assertTrue(tf.runTestInSameEvaluator("{List::sum([1, 2], 0) == 3;};"));
		//assertTrue(tf.runTestInSameEvaluator("{List::sum([1, 2, 3], 0) == 6;};"));
		//assertTrue(tf.runTestInSameEvaluator("{List::sum([1, -2, 3], 0) == 2;};"));
		//assertTrue(tf.runTestInSameEvaluator("{List::sum([1, 1, 1], 0) == 3;};"));
		
		System.err.println("List::takeOneFrom");
		
		assertTrue(tf.runTestInSameEvaluator("{<E, L> = List::takeOneFrom([1]}; (E == 1) && (L == []);}"));
		assertTrue(tf.runTestInSameEvaluator("{<E, L> = List::takeOneFrom([1,2]}; ((E == 1) && (L == [2])) || ((E == 2) && (L == [1]);}"));
	
		System.err.println("List::toMap");
		
		assertTrue(tf.runTestInSameEvaluator("{List::toMap([]) == ();};"));
		assertTrue(tf.runTestInSameEvaluator("{toMap([]) == ();};"));
		assertTrue(tf.runTestInSameEvaluator("{List::toMap([<1,10>, <2,20>]) == (1:10, 2:20);};"));
		
		System.err.println("List::toSet");
		
		assertTrue(tf.runTestInSameEvaluator("{List::toSet([]) == {};};"));
		assertTrue(tf.runTestInSameEvaluator("{toSet([]) == {};};"));
		assertTrue(tf.runTestInSameEvaluator("{List::toSet([1]) == {1};};"));
		assertTrue(tf.runTestInSameEvaluator("{List::toSet([1, 2, 1]) == {1, 2};};"));
		
		System.err.println("List::toString");
		
		assertTrue(tf.runTestInSameEvaluator("{List::toString([]) == \"[]\";};"));
		assertTrue(tf.runTestInSameEvaluator("{toString([]) == \"[]\";};"));
		assertTrue(tf.runTestInSameEvaluator("{List::toString([1]) == \"[1]\";};"));
		assertTrue(tf.runTestInSameEvaluator("{List::toString([1, 2]) == \"[1,2]\";};"));
		
	}
}
