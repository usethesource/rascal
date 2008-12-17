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

public class StandardLibraryTests extends TestCase {
	
	private TestFramework tf = new TestFramework();

	public void testBoolean() throws IOException {
		
		tf.prepare("import Boolean;");
		
		System.err.println("Boolean::arbBool");
		
		assertTrue(tf.runTestInSameEvaluator("{bool B = Boolean::arbBool(); (B == true) || (B == false);};"));
		assertTrue(tf.runTestInSameEvaluator("{bool B = arbBool(); (B == true) || (B == false);};"));
		
		System.err.println("Boolean::toInt");
		
		assertTrue(tf.runTestInSameEvaluator("Boolean::toInt(false) == 0;"));
		assertTrue(tf.runTestInSameEvaluator("Boolean::toInt(true) == 1;"));
		
		assertTrue(tf.runTestInSameEvaluator("toInt(false) == 0;"));
		assertTrue(tf.runTestInSameEvaluator("toInt(true) == 1;"));
		
		System.err.println("Boolean::toDouble");
		
		assertTrue(tf.runTestInSameEvaluator("Boolean::toDouble(false) == 0.0;"));
		assertTrue(tf.runTestInSameEvaluator("Boolean::toDouble(true) == 1.0;"));
		
		assertTrue(tf.runTestInSameEvaluator("toDouble(false) == 0.0;"));
		assertTrue(tf.runTestInSameEvaluator("toDouble(true) == 1.0;"));
		
		System.err.println("Boolean::toString");
		
		assertTrue(tf.runTestInSameEvaluator("Boolean::toString(false) == \"false\";"));
		assertTrue(tf.runTestInSameEvaluator("Boolean::toString(true) == \"true\";"));
		
		assertTrue(tf.runTestInSameEvaluator("toString(false) == \"false\";"));
		assertTrue(tf.runTestInSameEvaluator("toString(true) == \"true\";"));
	}
	
	public void testInteger() throws IOException {
		
		tf.prepare("import Integer;");
		
		System.err.println("Integer::arb");
		
		assertTrue(tf.runTestInSameEvaluator("{int N = Integer::arb(10); (N >= 0) && (N < 10);};"));
		assertTrue(tf.runTestInSameEvaluator("{int N = arb(10); (N >= 0) && (N < 10);};"));
		
		System.err.println("Integer::max");
		
		assertTrue(tf.runTestInSameEvaluator("Integer::max(3, 10) == 10;"));
		assertTrue(tf.runTestInSameEvaluator("max(3, 10) == 10;"));
		assertTrue(tf.runTestInSameEvaluator("Integer::max(10, 10) == 10;"));
		
		System.err.println("Integer::min");
		
		assertTrue(tf.runTestInSameEvaluator("Integer::min(3, 10) == 3;"));
		assertTrue(tf.runTestInSameEvaluator("min(3, 10) == 3;"));
		assertTrue(tf.runTestInSameEvaluator("Integer::min(10, 10) == 10;"));
		
		System.err.println("Integer::toDouble");
		
		assertTrue(tf.runTestInSameEvaluator("Integer::toDouble(3) == 3.0;"));
		assertTrue(tf.runTestInSameEvaluator("toDouble(3) == 3.0;"));
		
		System.err.println("Integer::toString");
		
		assertTrue(tf.runTestInSameEvaluator("Integer::toString(314) == \"314\";"));
		assertTrue(tf.runTestInSameEvaluator("toString(314) == \"314\";"));
		
	}
	
	public void testDouble() throws IOException {
		
		tf.prepare("import Double;");
		
		System.err.println("Double::arb");
		
		assertTrue(tf.runTestInSameEvaluator("{double D = Double::arbDouble(); (D >= 0.0) && (D <= 1.0);};"));
		assertTrue(tf.runTestInSameEvaluator("{double D = arbDouble(); (D >= 0.0) && (D <= 1.0);};"));
		
		System.err.println("Double::max");
		
		assertTrue(tf.runTestInSameEvaluator("Double::max(3.0, 10.0) == 10.0;"));
		assertTrue(tf.runTestInSameEvaluator("max(3.0, 10.0) == 10.0;"));
		assertTrue(tf.runTestInSameEvaluator("Double::max(10.0, 10.0) == 10.0;"));
		
		System.err.println("Double::min");
		
		assertTrue(tf.runTestInSameEvaluator("Double::min(3.0, 10.0) == 3.0;"));
		assertTrue(tf.runTestInSameEvaluator("min(3.0, 10.0) == 3.0;"));
		assertTrue(tf.runTestInSameEvaluator("Double::min(10.0, 10.0) == 10.0;"));
		
		System.err.println("Double::toInteger");
		
		assertTrue(tf.runTestInSameEvaluator("Double::toInteger(3.14) == 3;"));
		assertTrue(tf.runTestInSameEvaluator("toInteger(3.14) == 3;"));
		
		System.err.println("Double::toString");
		
		assertTrue(tf.runTestInSameEvaluator("Double::toString(3.14) == \"3.14\";"));
		assertTrue(tf.runTestInSameEvaluator("toString(3.14) == \"3.14\";"));
		
	}
	
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
