package test;

import java.io.IOException;

import junit.framework.TestCase;

public class StandardLibraryListTests extends TestCase {
	
	private static TestFramework tf = new TestFramework("import List;");
	
	public void testListAverage() throws IOException {
		
		tf = new TestFramework("import List;");
		
		assertTrue(tf.runTestInSameEvaluator("{int N = List::average([],0); N == 0;}"));
		assertTrue(tf.runTestInSameEvaluator("{int N = average([],0); N == 0;}"));
		assertTrue(tf.runTestInSameEvaluator("{int N = List::average([1],0); N == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{int N = List::average([1, 3],0); N == 2;}"));
	}
	
	public void testListFirst() throws IOException {
		
		tf = new TestFramework("import List;");
		
		assertTrue(tf.runTestInSameEvaluator("{List::first([1]) == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{first([1]) == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{List::first([1, 2]) == 1;}"));
	}
	
	public void testListGetOneFrom() throws IOException {
		
		tf = new TestFramework("import List;");
		
		assertTrue(tf.runTestInSameEvaluator("{int N = List::getOneFrom([1]); N == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{int N = getOneFrom([1]); N == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{int N = List::getOneFrom([1,2]); (N == 1) || (N == 2);}"));
		assertTrue(tf.runTestInSameEvaluator("{int N = List::getOneFrom([1,2,3]); (N == 1) || (N == 2) || (N == 3);}"));
		assertTrue(tf.runTestInSameEvaluator("{double D = List::getOneFrom([1.0,2.0]); (D == 1.0) || (D == 2.0);}"));
		assertTrue(tf.runTestInSameEvaluator("{str S = List::getOneFrom([\"abc\",\"def\"]); (S == \"abc\") || (S == \"def\");}"));
	}
	
public void testListinsertAt() throws IOException {
	
		tf = new TestFramework("import List;");
		
		assertTrue(tf.runTestInSameEvaluator("List::insertAt(1, 0, []) == [1];"));
		assertTrue(tf.runTestInSameEvaluator("insertAt(1, 0, []) == [1];"));
		assertTrue(tf.runTestInSameEvaluator("List::insertAt(1, 1, [2,3]) == [2,1, 3];"));
		assertTrue(tf.runTestInSameEvaluator("insertAt(1, 1, [2,3]) == [2, 1, 3];"));
		assertTrue(tf.runTestInSameEvaluator("List::insertAt(1, 2, [2,3]) == [2,3,1];"));
		assertTrue(tf.runTestInSameEvaluator("insertAt(1, 2, [2,3]) == [2, 3, 1];"));
	}
	
	public void testListMapper() throws IOException {	
		
		tf = new TestFramework("import List;");
		
		assertTrue(tf.runTestInSameEvaluator("{int inc(int n) {return n + 1;} mapper([1, 2, 3], #inc) == [2, 3, 4];}"));
	}
	
	public void testListMax() throws IOException {	
		
		tf = new TestFramework("import List;");
		
		assertTrue(tf.runTestInSameEvaluator("{List::max([1, 2, 3, 2, 1]) == 3;}"));
		assertTrue(tf.runTestInSameEvaluator("{max([1, 2, 3, 2, 1]) == 3;}"));
	}
	
	public void testListMin() throws IOException {	
		
		tf = new TestFramework("import List;");
		
		assertTrue(tf.runTestInSameEvaluator("{List::min([1, 2, 3, 2, 1]) == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{min([1, 2, 3, 2, 1]) == 1;}"));
	}
	
	public void testListMultiply() throws IOException {	
		
		tf = new TestFramework("import List;");
		
		assertTrue(tf.runTestInSameEvaluator("{multiply([1, 2, 3, 4], 1) == 24;}"));
		assertTrue(tf.runTestInSameEvaluator("{List::multiply([1, 2, 3, 4], 1) == 24;}"));
		
	}
	
	public void testListReducer() throws IOException {	
		
		tf = new TestFramework("import List;");
		String add = "int add(int x, int y){return x + y;}";
		
		assertTrue(tf.runTestInSameEvaluator("{" + add + "reducer([1, 2, 3, 4], #add, 0) == 10;}"));
	}
	
	public void testListRest() throws IOException {	
		
		tf = new TestFramework("import List;");
		
		assertTrue(tf.runTestInSameEvaluator("{List::rest([1]) == [];}"));
		assertTrue(tf.runTestInSameEvaluator("{rest([1]) == [];}"));
		assertTrue(tf.runTestInSameEvaluator("{List::rest([1, 2]) == [2];}"));
	}
	
	public void testListReverse() throws IOException {	
		
		tf = new TestFramework("import List;");
		
		assertTrue(tf.runTestInSameEvaluator("{List::reverse([]) == [];}"));
		assertTrue(tf.runTestInSameEvaluator("{reverse([]) == [];}"));
		assertTrue(tf.runTestInSameEvaluator("{List::reverse([1]) == [1];}"));
		assertTrue(tf.runTestInSameEvaluator("{List::reverse([1,2,3]) == [3,2,1];}"));
	}
	
	public void testListSize() throws IOException {	
		
		tf = new TestFramework("import List;");
		
		assertTrue(tf.runTestInSameEvaluator("{List::size([]) == 0;}"));
		assertTrue(tf.runTestInSameEvaluator("{size([]) == 0;}"));
		assertTrue(tf.runTestInSameEvaluator("{List::size([1]) == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{List::size([1,2,3]) == 3;}"));
	}
	
	public void testListSort() throws IOException {	
		
		tf = new TestFramework("import List;");
		
		assertTrue(tf.runTestInSameEvaluator("{List::sort([]) == [];}"));
		assertTrue(tf.runTestInSameEvaluator("{sort([]) == [];}"));
		assertTrue(tf.runTestInSameEvaluator("{List::sort([1]) == [1];}"));
		assertTrue(tf.runTestInSameEvaluator("{sort([1]) == [1];}"));
		assertTrue(tf.runTestInSameEvaluator("{List::sort([2, 1]) == [1,2];}"));
		assertTrue(tf.runTestInSameEvaluator("{sort([2, 1]) == [1,2];}"));
		assertTrue(tf.runTestInSameEvaluator("{List::sort([2,-1,4,-2,3]) == [-2,-1,2,3, 4];}"));
		assertTrue(tf.runTestInSameEvaluator("{sort([2,-1,4,-2,3]) == [-2,-1,2,3, 4];}"));
	}
	
	public void testListSum() throws IOException {	
		
		tf = new TestFramework("import List;");
		
		assertTrue(tf.runTestInSameEvaluator("{sum([1,2,3],0) == 6;}"));
		assertTrue(tf.runTestInSameEvaluator("{List::sum([1,2,3], 0) == 6;}"));
		
		assertTrue(tf.runTestInSameEvaluator("{List::sum([], 0) == 0;}"));
		assertTrue(tf.runTestInSameEvaluator("{List::sum([], 0) == 0;}"));
		assertTrue(tf.runTestInSameEvaluator("{List::sum([1], 0) == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{List::sum([1, 2], 0) == 3;}"));
		assertTrue(tf.runTestInSameEvaluator("{List::sum([1, 2, 3], 0) == 6;}"));
		assertTrue(tf.runTestInSameEvaluator("{List::sum([1, -2, 3], 0) == 2;}"));
		assertTrue(tf.runTestInSameEvaluator("{List::sum([1, 1, 1], 0) == 3;}"));
	}
	
	public void testListTakeOneFrom() throws IOException {	
		
		tf = new TestFramework("import List;");
		
		assertTrue(tf.runTestInSameEvaluator("{<E, L> = takeOneFrom([1]); (E == 1) && (L == []);}"));
		assertTrue(tf.runTestInSameEvaluator("{<E, L> = List::takeOneFrom([1,2]); ((E == 1) && (L == [2])) || ((E == 2) && (L == [1]));}"));
	}
	
	public void testListToMap() throws IOException {	
		
		tf = new TestFramework("import List;");
		
		assertTrue(tf.runTestInSameEvaluator("{List::toMap([]) == ();}"));
		assertTrue(tf.runTestInSameEvaluator("{toMap([]) == ();}"));
		assertTrue(tf.runTestInSameEvaluator("{List::toMap([<1,10>, <2,20>]) == (1:10, 2:20);}"));
	}
	
	public void testListToSet() throws IOException {
		
		tf = new TestFramework("import List;");
		
		assertTrue(tf.runTestInSameEvaluator("{List::toSet([]) == {};}"));
		assertTrue(tf.runTestInSameEvaluator("{toSet([]) == {};}"));
		assertTrue(tf.runTestInSameEvaluator("{List::toSet([1]) == {1};}"));
		assertTrue(tf.runTestInSameEvaluator("{toSet([1]) == {1};}"));
		assertTrue(tf.runTestInSameEvaluator("{List::toSet([1, 2, 1]) == {1, 2};}"));
	}
	
	public void testListToString() throws IOException {	
		
		tf = new TestFramework("import List;");
		
		assertTrue(tf.runTestInSameEvaluator("{List::toString([]) == \"[]\";}"));
		assertTrue(tf.runTestInSameEvaluator("{toString([]) == \"[]\";}"));
		assertTrue(tf.runTestInSameEvaluator("{List::toString([1]) == \"[1]\";}"));
		assertTrue(tf.runTestInSameEvaluator("{List::toString([1, 2]) == \"[1,2]\";}"));
	}
}
