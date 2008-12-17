package test;

import junit.framework.TestCase;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import junit.framework.TestCase;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Command;
import org.meta_environment.rascal.ast.Statement;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.RascalBug;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;
import org.meta_environment.uptr.Factory;

public class StandardLibrarySetTests extends TestCase {
	
	private TestFramework tf = new TestFramework();
	
	public void testSetAverage() throws IOException {
		
		tf.prepare("import Set;");
		
		//assertTrue(tf.runTestInSameEvaluator("{int N = Set::average({},0); N == 0;};"));
		//assertTrue(tf.runTestInSameEvaluator("{int N = average({},0); N == 0;};"));
		//assertTrue(tf.runTestInSameEvaluator("{int N = Set::average({1},0); N == 1;};"));
		//assertTrue(tf.runTestInSameEvaluator("{int N = Set::average({1, 3},0); N == 4;};"));
	}
	
	public void testSetgetOneFrom() throws IOException {
		tf.prepare("import Set;");
		
		assertTrue(tf.runTestInSameEvaluator("{int N = Set::getOneFrom({1}); N == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{int N = Set::getOneFrom({1}); N == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{int N = getOneFrom({1}); N == 1;}"));
		assertTrue(tf.runTestInSameEvaluator("{int N = Set::getOneFrom({1, 2}); (N == 1) || (N == 2);}"));
		assertTrue(tf.runTestInSameEvaluator("{int N = Set::getOneFrom({1, 2, 3}); (N == 1) || (N == 2) || (N == 3);}"));
		assertTrue(tf.runTestInSameEvaluator("{double D = Set::getOneFrom({1.0,2.0}); (D == 1.0) || (D == 2.0);}"));
		assertTrue(tf.runTestInSameEvaluator("{str S = Set::getOneFrom({\"abc\",\"def\"}); (S == \"abc\") || (S == \"def\");}"));
	}
		
	//mapper
	
	public void testSetMax() throws IOException {
		tf.prepare("import Set;");
		
		assertTrue(tf.runTestInSameEvaluator("{Set::max({1, 2, 3, 2, 1}) == 3;};"));
		assertTrue(tf.runTestInSameEvaluator("{max({1, 2, 3, 2, 1}) == 3;};"));
	}
	
	public void testSetMin() throws IOException {
		tf.prepare("import Set;");		
		
		assertTrue(tf.runTestInSameEvaluator("{Set::min({1, 2, 3, 2, 1}) == 1;};"));
		assertTrue(tf.runTestInSameEvaluator("{min({1, 2, 3, 2, 1}) == 1;};"));
	}	
	
	//multiply
	
	public void testSetPower() throws IOException {
		tf.prepare("import Set;");		
		
		assertTrue(tf.runTestInSameEvaluator("{Set::power({}) == {{}};};"));
		assertTrue(tf.runTestInSameEvaluator("{Set::power({1}) == {{}, {1}};};"));
		assertTrue(tf.runTestInSameEvaluator("{Set::power({1, 2}) == {{}, {1}, {2}, {1,2}};};"));
		assertTrue(tf.runTestInSameEvaluator("{Set::power({1, 2, 3}) == {{}, {1}, {2}, {3}, {1,2}, {1,3}, {2,3}, {1,2,3}};};"));
		assertTrue(tf.runTestInSameEvaluator("{Set::power({1, 2, 3, 4}) == { {}, {1}, {2}, {3}, {4}, {1,2}, {1,3}, {1,4}, {2,3}, {2,4}, {3,4}, {1,2,3}, {1,2,4}, {1,3,4}, {2,3,4}, {1,2,3,4}};};"));
	}
	
	//reducer
	
	public void testSetSize() throws IOException {
		tf.prepare("import Set;");		
		
		assertTrue(tf.runTestInSameEvaluator("Set::size({}) == 0;"));
		assertTrue(tf.runTestInSameEvaluator("size({}) == 0;"));
		assertTrue(tf.runTestInSameEvaluator("Set::size({1}) == 1;"));
		assertTrue(tf.runTestInSameEvaluator("Set::size({1,2,3}) == 3;"));
	}
	
	public void testSetSum() throws IOException {
		tf.prepare("import Set;");	
		
		//assertTrue(tf.runTestInSameEvaluator("{sum({1,2,3},0) == 6;};"));
		//assertTrue(tf.runTestInSameEvaluator("{Set::sum({1,2,3}, 0) == 6;};"));
		
		//assertTrue(tf.runTestInSameEvaluator("{Set::sum({}, 0) == 0;};"));
		//assertTrue(tf.runTestInSameEvaluator("{Set::sum({}, 0) == 0;};"));
		//assertTrue(tf.runTestInSameEvaluator("{Set::sum({1}, 0) == 1;};"));
		//assertTrue(tf.runTestInSameEvaluator("{Set::sum({1, 2}, 0) == 3;};"));
		//assertTrue(tf.runTestInSameEvaluator("{Set::sum({1, 2, 3}, 0) == 6;};"));
		//assertTrue(tf.runTestInSameEvaluator("{Set::sum({1, -2, 3}, 0) == 2;};"));
		//assertTrue(tf.runTestInSameEvaluator("{Set::sum({1, 1, 1}, 0) == 1;};"));

	}
	
	public void testSetTakeOneFrom() throws IOException {
		tf.prepare("import Set;");	
	
		System.err.println("Set::takeOneFrom");
		assertTrue(tf.runTestInSameEvaluator("{<E, S> = Set::takeOneFrom({1}}; (E == 1) && (S == {}) ;}"));
		assertTrue(tf.runTestInSameEvaluator("{<E, S> = Set::takeOneFrom({1,2}}; ((E == 1) && (S == {2})) || ((E == 2) && (L == {1});}"));
	}
	
	public void testSetToList() throws IOException {
		tf.prepare("import Set;");	
		
		assertTrue(tf.runTestInSameEvaluator("{Set::toList({}) == [];};"));
		assertTrue(tf.runTestInSameEvaluator("{toList({}) == [];};"));
		assertTrue(tf.runTestInSameEvaluator("{Set::toList({1}) == [1];};"));
		assertTrue(tf.runTestInSameEvaluator("{(Set::toList({1, 2, 1}) == [1, 2]) || (Set::toList({1, 2, 1}) == [2, 1]);};"));
	}
	
	public void testSetToMap() throws IOException {
		tf.prepare("import Set;");	
		
		assertTrue(tf.runTestInSameEvaluator("{Set::toMap({}) == ();};"));
		assertTrue(tf.runTestInSameEvaluator("{toMap({}) == ();};"));
		assertTrue(tf.runTestInSameEvaluator("{Set::toMap({<1, \"a\">}) == (1 : \"a\");};"));
		assertTrue(tf.runTestInSameEvaluator("{Set::toMap({<1, \"a\">, <2, \"b\">}) == (1 : \"a\", 2 : \"b\");};"));
	}
	
	public void testSetToString() throws IOException {
		tf.prepare("import Set;");
		
		assertTrue(tf.runTestInSameEvaluator("Set::toString({}) == \"{}\";"));
		assertTrue(tf.runTestInSameEvaluator("toString({}) == \"{}\";"));
		assertTrue(tf.runTestInSameEvaluator("Set::toString({1}) == \"{1}\";"));
		assertTrue(tf.runTestInSameEvaluator("Set::toString({1, 2, 3}) == \"{1,2,3}\";"));
	}
}
