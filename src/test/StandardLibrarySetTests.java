package test;

import java.io.IOException;

import junit.framework.TestCase;

public class StandardLibrarySetTests extends TestCase {
	
	private static TestFramework tf = new TestFramework("import Set;");
	
	public void testSetAverage() throws IOException {
		
		new TestFramework("import Set;");
		
		assertTrue(tf.runTestInSameEvaluator("{int N = Set::average({},0); N == 0;}"));
		assertTrue(tf.runTestInSameEvaluator("{int N = average({},0); N == 0;}"));
		assertTrue(tf.runTestInSameEvaluator("{int N = Set::average({1},0); N == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{int N = Set::average({1, 3},0); N == 2;}"));
	}
	
	public void testSetgetOneFrom() throws IOException {
		
		new TestFramework("import Set;");
		
		assertTrue(tf.runTestInSameEvaluator("{int N = Set::getOneFrom({1}); N == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{int N = Set::getOneFrom({1}); N == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{int N = getOneFrom({1}); N == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{int N = Set::getOneFrom({1, 2}); (N == 1) || (N == 2);}"));
		assertTrue(tf.runTestInSameEvaluator("{int N = Set::getOneFrom({1, 2, 3}); (N == 1) || (N == 2) || (N == 3);}"));
		assertTrue(tf.runTestInSameEvaluator("{double D = Set::getOneFrom({1.0,2.0}); (D == 1.0) || (D == 2.0);}"));
		assertTrue(tf.runTestInSameEvaluator("{str S = Set::getOneFrom({\"abc\",\"def\"}); (S == \"abc\") || (S == \"def\");}"));
	}
		
	public void testSetMapper() throws IOException {
		
		new TestFramework("import Set;");
		
		assertTrue(tf.runTestInSameEvaluator("{int inc(int n) {return n + 1;} mapper({1, 2, 3}, #inc) == {2, 3, 4};}"));
	
	}
	
	public void testSetMax() throws IOException {
		
		new TestFramework("import Set;");
		
		assertTrue(tf.runTestInSameEvaluator("{Set::max({1, 2, 3, 2, 1}) == 3;}"));
		assertTrue(tf.runTestInSameEvaluator("{max({1, 2, 3, 2, 1}) == 3;}"));
	}
	
	public void testSetMin() throws IOException {
		
		new TestFramework("import Set;");
		
		assertTrue(tf.runTestInSameEvaluator("{Set::min({1, 2, 3, 2, 1}) == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{min({1, 2, 3, 2, 1}) == 1;}"));
	}	
	
	public void testSetMultiply() throws IOException {
		
		new TestFramework("import Set;");
		
		assertTrue(tf.runTestInSameEvaluator("{multiply({1, 2, 3, 4}, 1) == 24;}"));
		assertTrue(tf.runTestInSameEvaluator("{Set::multiply({1, 2, 3, 4}, 1) == 24;}"));
	}
	
	public void testSetPower() throws IOException {	
		
		new TestFramework("import Set;");
		
		assertTrue(tf.runTestInSameEvaluator("{Set::power({}) == {{}};}"));
		assertTrue(tf.runTestInSameEvaluator("{Set::power({1}) == {{}, {1}};}"));
		assertTrue(tf.runTestInSameEvaluator("{Set::power({1, 2}) == {{}, {1}, {2}, {1,2}};}"));
		assertTrue(tf.runTestInSameEvaluator("{Set::power({1, 2, 3}) == {{}, {1}, {2}, {3}, {1,2}, {1,3}, {2,3}, {1,2,3}};}"));
		assertTrue(tf.runTestInSameEvaluator("{Set::power({1, 2, 3, 4}) == { {}, {1}, {2}, {3}, {4}, {1,2}, {1,3}, {1,4}, {2,3}, {2,4}, {3,4}, {1,2,3}, {1,2,4}, {1,3,4}, {2,3,4}, {1,2,3,4}};}"));
	}
	
	public void testSetReducer() throws IOException {
		
		new TestFramework("import Set;");
		String add = "int add(int x, int y){return x + y;}";
		assertTrue(tf.runTestInSameEvaluator("{" + add + "reducer({1, 2, 3, 4}, #add, 0) == 10;}"));
	}
	
	public void testSetSize() throws IOException {	
		
		new TestFramework("import Set;");
		
		assertTrue(tf.runTestInSameEvaluator("Set::size({}) == 0;"));
		assertTrue(tf.runTestInSameEvaluator("size({}) == 0;"));
		assertTrue(tf.runTestInSameEvaluator("Set::size({1}) == 1;"));
		assertTrue(tf.runTestInSameEvaluator("Set::size({1,2,3}) == 3;"));
	}
	
	public void testSetSum() throws IOException {
		
		new TestFramework("import Set;");
		
		assertTrue(tf.runTestInSameEvaluator("{sum({1,2,3},0) == 6;}"));
		assertTrue(tf.runTestInSameEvaluator("{Set::sum({1,2,3}, 0) == 6;}"));
		
		assertTrue(tf.runTestInSameEvaluator("{Set::sum({}, 0) == 0;}"));
		assertTrue(tf.runTestInSameEvaluator("{Set::sum({}, 0) == 0;}"));
		assertTrue(tf.runTestInSameEvaluator("{Set::sum({1}, 0) == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{Set::sum({1, 2}, 0) == 3;}"));
		assertTrue(tf.runTestInSameEvaluator("{Set::sum({1, 2, 3}, 0) == 6;}"));
		assertTrue(tf.runTestInSameEvaluator("{Set::sum({1, -2, 3}, 0) == 2;}"));
		assertTrue(tf.runTestInSameEvaluator("{Set::sum({1, 1, 1}, 0) == 1;}"));
	}
	
	public void testSetTakeOneFrom() throws IOException {
		
		new TestFramework("import Set;");
	
		assertTrue(tf.runTestInSameEvaluator("{<E, SI> = Set::takeOneFrom({1}); (E == 1) && (SI == {}) ;}"));
		assertTrue(tf.runTestInSameEvaluator("{<E, SI> = Set::takeOneFrom({1,2}); ((E == 1) && (SI == {2})) || ((E == 2) && (SI == {1}));}"));
	}
	
	public void testSetToList() throws IOException {	
		
		new TestFramework("import Set;");
		
		assertTrue(tf.runTestInSameEvaluator("{Set::toList({}) == [];}"));
		assertTrue(tf.runTestInSameEvaluator("{toList({}) == [];}"));
		assertTrue(tf.runTestInSameEvaluator("{Set::toList({1}) == [1];}"));
		assertTrue(tf.runTestInSameEvaluator("{(Set::toList({1, 2, 1}) == [1, 2]) || (Set::toList({1, 2, 1}) == [2, 1]);}"));
	}
	
	public void testSetToMap() throws IOException {
		
		new TestFramework("import Set;");
		assertTrue(tf.runTestInSameEvaluator("{Set::toMap({}) == ();}"));

		assertTrue(tf.runTestInSameEvaluator("{toMap({}) == ();}"));
		assertTrue(tf.runTestInSameEvaluator("{Set::toMap({<1, \"a\">}) == (1 : \"a\");}"));
		assertTrue(tf.runTestInSameEvaluator("{Set::toMap({<1, \"a\">, <2, \"b\">}) == (1 : \"a\", 2 : \"b\");}"));
	}
	
	public void testSetToString() throws IOException {
		
		new TestFramework("import Set;");
		
		assertTrue(tf.runTestInSameEvaluator("Set::toString({}) == \"{}\";"));
		assertTrue(tf.runTestInSameEvaluator("toString({}) == \"{}\";"));
		assertTrue(tf.runTestInSameEvaluator("Set::toString({1}) == \"{1}\";"));
		assertTrue(tf.runTestInSameEvaluator("{ S = Set::toString({1, 2}); (S == \"{1,2}\") || (S == \"{2,1}\");}"));
	}
}
