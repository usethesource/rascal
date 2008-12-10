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
	private Parser parser = Parser.getInstance();
	private ASTFactory factory = new ASTFactory();
    private ASTBuilder builder = new ASTBuilder(factory);
	private Evaluator evaluator = new Evaluator(ValueFactory.getInstance(), factory, new PrintWriter(System.err));
	
	private boolean eval(Evaluator evaluator, INode tree){
		if (tree.getTreeNodeType() ==  Factory.ParseTree_Summary) {
			System.err.println(tree);
			return false;
		} else {
			Command cmd = builder.buildCommand(tree);
			
			if(cmd.isStatement()){
				IValue value = evaluator.eval(cmd.getStatement());
				if (value == null || ! value.getType().isBoolType())
					return false;
				return value.equals(ValueFactory.getInstance().bool(true)) ? true : false;
			}
			else if(cmd.isImport()){
				IValue value = evaluator.eval(cmd.getImported());
				return true;
			} else {
				throw new RascalBug("unexpected case in test: " + cmd);
			}
			
		}
	}
    
	private boolean runTest(String module, String command) throws IOException {
		INode tree1 = parser.parse(new ByteArrayInputStream(("import " + module + ";").getBytes()));
		INode tree2 = parser.parse(new ByteArrayInputStream(command.getBytes()));

		evaluator.clean();
		eval(evaluator, tree1);
		return eval(evaluator,tree2);
	}
	
	public void testBoolean() throws IOException {
		
		System.err.println("Boolean::arb");
		
		assertTrue(runTest("Boolean", "{bool B = Boolean::arb(); (B == true) || (B == false);};"));
		assertTrue(runTest("Boolean", "{bool B = arb(); (B == true) || (B == false);};"));
		
		System.err.println("Boolean::toInt");
		
		assertTrue(runTest("Boolean", "Boolean::toInt(false) == 0;"));
		assertTrue(runTest("Boolean", "Boolean::toInt(true) == 1;"));
		
		assertTrue(runTest("Boolean", "toInt(false) == 0;"));
		assertTrue(runTest("Boolean", "toInt(true) == 1;"));
		
		System.err.println("Boolean::toDouble");
		
		assertTrue(runTest("Boolean", "Boolean::toDouble(false) == 0.0;"));
		assertTrue(runTest("Boolean", "Boolean::toDouble(true) == 1.0;"));
		
		assertTrue(runTest("Boolean", "toDouble(false) == 0.0;"));
		assertTrue(runTest("Boolean", "toDouble(true) == 1.0;"));
		
		System.err.println("Boolean::toString");
		
		assertTrue(runTest("Boolean", "Boolean::toString(false) == \"false\";"));
		assertTrue(runTest("Boolean", "Boolean::toString(true) == \"true\";"));
		
		assertTrue(runTest("Boolean", "toString(false) == \"false\";"));
		assertTrue(runTest("Boolean", "toString(true) == \"true\";"));
	}
	
	public void testInteger() throws IOException {
		
		System.err.println("Integer::arb");
		
		assertTrue(runTest("Integer", "{int N = Integer::arb(10); (N >= 0) && (N < 10);};"));
		assertTrue(runTest("Integer", "{int N = arb(10); (N >= 0) && (N < 10);};"));
		
		System.err.println("Integer::max");
		
		assertTrue(runTest("Integer", "Integer::max(3, 10) == 10;"));
		assertTrue(runTest("Integer", "max(3, 10) == 10;"));
		assertTrue(runTest("Integer", "Integer::max(10, 10) == 10;"));
		
		System.err.println("Integer::min");
		
		assertTrue(runTest("Integer", "Integer::min(3, 10) == 3;"));
		assertTrue(runTest("Integer", "min(3, 10) == 3;"));
		assertTrue(runTest("Integer", "Integer::min(10, 10) == 10;"));
		
		System.err.println("Integer::toDouble");
		
		assertTrue(runTest("Integer", "Integer::toDouble(3) == 3.0;"));
		assertTrue(runTest("Integer", "toDouble(3) == 3.0;"));
		
		System.err.println("Integer::toString");
		
		assertTrue(runTest("Integer", "Integer::toString(314) == \"314\";"));
		assertTrue(runTest("Integer", "toString(314) == \"314\";"));
		
	}
	
	public void testDouble() throws IOException {
		
		System.err.println("Double::arb");
		
		assertTrue(runTest("Double", "{double D = Double::arb(); (D >= 0.0) && (D <= 1.0);};"));
		assertTrue(runTest("Double", "{double D = arb(); (D >= 0.0) && (D <= 1.0);};"));
		
		System.err.println("Double::max");
		
		assertTrue(runTest("Double", "Double::max(3.0, 10.0) == 10.0;"));
		assertTrue(runTest("Double", "max(3.0, 10.0) == 10.0;"));
		assertTrue(runTest("Double", "Double::max(10.0, 10.0) == 10.0;"));
		
		System.err.println("Double::min");
		
		assertTrue(runTest("Double", "Double::min(3.0, 10.0) == 3.0;"));
		assertTrue(runTest("Double", "min(3.0, 10.0) == 3.0;"));
		assertTrue(runTest("Double", "Double::min(10.0, 10.0) == 10.0;"));
		
		System.err.println("Double::toInteger");
		
		assertTrue(runTest("Double", "Double::toInteger(3.14) == 3;"));
		assertTrue(runTest("Double", "toInteger(3.14) == 3;"));
		
		System.err.println("Double::toString");
		
		assertTrue(runTest("Double", "Double::toString(3.14) == \"3.14\";"));
		assertTrue(runTest("Double", "toString(3.14) == \"3.14\";"));
		
	}
	
	public void testString() throws IOException {
		
		System.err.println("String::charAt");
		
		assertTrue(runTest("String", "String::charAt(\"abc\", 0) == 97;"));
		assertTrue(runTest("String", "String::charAt(\"abc\", 1) == 98;"));
		assertTrue(runTest("String", "String::charAt(\"abc\", 2) == 99;"));
		assertTrue(runTest("String", "charAt(\"abc\", 0) == 97;"));
		
		System.err.println("String::endsWith");
		
		assertTrue(runTest("String", "String::endsWith(\"abc\", \"abc\");"));
		assertTrue(runTest("String", "endsWith(\"abc\", \"abc\");"));
		assertTrue(runTest("String", "String::endsWith(\"abcdef\", \"def\");"));
		assertFalse(runTest("String", "String::endsWith(\"abcdef\", \"abc\");"));
		
		System.err.println("String::reverse");
		
		assertTrue(runTest("String", "String::reverse(\"\") == \"\";"));
		assertTrue(runTest("String", "reverse(\"\") == \"\";"));
		assertTrue(runTest("String", "String::reverse(\"abc\") == \"cba\";"));
		
		System.err.println("String::size");
		
		assertTrue(runTest("String", "String::size(\"\") == 0;"));
		assertTrue(runTest("String", "size(\"\") == 0;"));
		assertTrue(runTest("String", "String::size(\"abc\") == 3;"));
		
		System.err.println("String::startsWith");
		
		assertTrue(runTest("String", "String::startsWith(\"abc\", \"abc\");"));
		assertTrue(runTest("String", "startsWith(\"abc\", \"abc\");"));
		assertTrue(runTest("String", "String::startsWith(\"abcdef\", \"abc\");"));
		assertFalse(runTest("String", "String::startsWith(\"abcdef\", \"def\");"));
		
		System.err.println("String::toLowerCase");
		
		assertTrue(runTest("String", "String::toLowerCase(\"\") == \"\";"));
		assertTrue(runTest("String", "toLowerCase(\"\") ==  \"\";"));
		assertTrue(runTest("String", "String::toLowerCase(\"ABC\") == \"abc\";"));
		assertTrue(runTest("String", "String::toLowerCase(\"ABC123\") == \"abc123\";"));
		
		System.err.println("String::toUpperCase");
		
		assertTrue(runTest("String", "String::toUpperCase(\"\") == \"\";"));
		assertTrue(runTest("String", "toUpperCase(\"\") == \"\";"));
		assertTrue(runTest("String", "String::toUpperCase(\"abc\") == \"ABC\";"));
		assertTrue(runTest("String", "String::toUpperCase(\"abc123\") == \"ABC123\";"));
	}
	
	public void testList() throws IOException {
		
		//arb
		System.err.println("List::arb");
		
		assertTrue(runTest("List", "{int N = List::arb([1]); N == 1;};"));
		assertTrue(runTest("List", "{int N = arb([1]); N == 1;};"));
		assertTrue(runTest("List", "{int N = List::arb([1,2]); (N == 1) || (N == 2);};"));
		assertTrue(runTest("List", "{int N = List::arb([1,2,3]); (N == 1) || (N == 2) || (N == 3);};"));
		assertTrue(runTest("List", "{double D = List::arb([1.0,2.0]); (D == 1.0) || (D == 2.0);};"));
		assertTrue(runTest("List", "{str S = List::arb([\"abc\",\"def\"]); (S == \"abc\") || (S == \"def\");};"));
		
		//average
		System.err.println("List::average");
		
		//assertTrue(runTest("List", "{value N = List::average([],0); N == 0;};"));
		//assertTrue(runTest("List", "{value N = average([],0); N == 0;};"));
		//assertTrue(runTest("List", "{value N = List::average([1],0); N == 1;};"));
		//assertTrue(runTest("List", "{value N = List::average([1, 3],0); N == 4;};"));
		
		//first
		System.err.println("List::first");
		
		assertTrue(runTest("List", "{List::first([1]) == 1;};"));
		assertTrue(runTest("List", "{first([1]) == 1;};"));
		assertTrue(runTest("List", "{List::first([1, 2]) == 1;};"));
		
		//mapper
		System.err.println("List::mapper");
		//assertTrue(runTest("List", "{int inc(int n) {return n + 1;} mapper([1, 2, 3], #inc) == [2, 4, 6];};"));
		//assertTrue(runTest("List", "{int inc(int n) {return n + 1;} List::mapper([1, 2, 3], #inc) == [2, 4, 6];};"));
		
		//max
		System.err.println("List::max");
		
		assertTrue(runTest("List", "{List::max([1, 2, 3, 2, 1]) == 3;};"));
		assertTrue(runTest("List", "{max([1, 2, 3, 2, 1]) == 3;};"));
		
		//min
		System.err.println("List::min");
		assertTrue(runTest("List", "{List::min([1, 2, 3, 2, 1]) == 1;};"));
		assertTrue(runTest("List", "{min([1, 2, 3, 2, 1]) == 1;};"));
		
		//multiply
		System.err.println("List::multiply");
		assertTrue(runTest("List", "{multiply([1, 2, 3, 4]) == 24;};"));
		assertTrue(runTest("List", "{List::multiply([1, 2, 3, 4]) == 24;};"));
		
		//reducer
		System.err.println("List::reducer");
		assertTrue(runTest("List", "{reducer([1, 2, 3, 4], #1, 0) == 10;};"));
		assertTrue(runTest("List", "{List::reducer([1, 2, 3, 4], #1, 0) == 10;};"));
		
		//rest
		System.err.println("rest");
		assertTrue(runTest("List", "{List::rest([1]) == [];};"));
		assertTrue(runTest("List", "{rest([1]) == [];};"));
		assertTrue(runTest("List", "{List::rest([1, 2]) == [2];};"));
		
		//reverse
		System.err.println("List::reverse");
		assertTrue(runTest("List", "{List::reverse([]) == [];};"));
		assertTrue(runTest("List", "{reverse([]) == [];};"));
		assertTrue(runTest("List", "{List::reverse([1]) == [1];};"));
		assertTrue(runTest("List", "{List::reverse([1,2,3]) == [3,2,1];};"));
		
		//size
		System.err.println("List::size");
		assertTrue(runTest("List", "{List::size([]) == 0;};"));
		assertTrue(runTest("List", "{size([]) == 0;};"));
		assertTrue(runTest("List", "{List::size([1]) == 1;};"));
		assertTrue(runTest("List", "{List::size([1,2,3]) == 3;};"));
		
		//sort
		
		//sum
		System.err.println("List::sum");
		assertTrue(runTest("List", "{sum([1,2,3]) == 6;};"));
		assertTrue(runTest("List", "{List::sum([1,2,3]) == 6;};"));
		
		assertTrue(runTest("List", "{List::sum([], 0) == 0;};"));
		assertTrue(runTest("List", "{List::sum([], 0) == 0;};"));
		assertTrue(runTest("List", "{List::sum([1], 0) == 1;};"));
		assertTrue(runTest("List", "{List::sum([1, 2], 0) == 3;};"));
		assertTrue(runTest("List", "{List::sum([1, 2, 3], 0) == 6;};"));
		assertTrue(runTest("List", "{List::sum([1, -2, 3], 0) == 2;};"));
		assertTrue(runTest("List", "{List::sum([1, 1, 1], 0) == 3;};"));
		
		//toSet
		System.err.println("List::toSet");
		assertTrue(runTest("List", "{List::toSet([]) == {};};"));
		assertTrue(runTest("List", "{toSet([]) == {};};"));
		assertTrue(runTest("List", "{List::toSet([1]) == {1};};"));
		assertTrue(runTest("List", "{List::toSet([1, 2, 1]) == {1, 2};};"));
		
		//toMap
		System.err.println("List::toMap");
		assertTrue(runTest("List", "{List::toMap([]) == ();};"));
		assertTrue(runTest("List", "{toMap([]) == ();};"));
		assertTrue(runTest("List", "{List::toMap([<1,10>, <2,20>]) == (1:10, 2:20);};"));
		
		//toString
		System.err.println("List::toString");
		assertTrue(runTest("List", "{List::toString([]) == \"[]\";};"));
		assertTrue(runTest("List", "{toString([]) == \"[]\";};"));
		assertTrue(runTest("List", "{List::toString([1]) == \"[1]\";};"));
		assertTrue(runTest("List", "{List::toString([1, 2]) == \"[1,2]\";};"));
		
	}
	
	public void testSet() throws IOException {
		
		//arb
		
		assertTrue(runTest("Set", "{value N = Set::arb({1}); N == 1;};"));
		assertTrue(runTest("Set", "{value N = arb({1}); N == 1;};"));
		assertTrue(runTest("Set", "{value N = Set::arb({1, 2}); (N == 1) || (N == 2);};"));
		assertTrue(runTest("Set", "{value N = Set::arb({1, 2, 3}); (N == 1) || (N == 2) || (N == 3);};"));
		assertTrue(runTest("Set", "{double D = List::arb({1.0,2.0}); (D == 1.0) || (D == 2.0);};"));
		assertTrue(runTest("Set", "{str S = List::arb({\"abc\",\"def\"}); (S == \"abc\") || (S == \"def\");};"));
		
		//average
		
		assertTrue(runTest("Set", "{value N = Set::average({},0); N == 0;};"));
		assertTrue(runTest("Set", "{value N = average({},0); N == 0;};"));
		assertTrue(runTest("Set", "{value N = Set::average({1},0); N == 1;};"));
		assertTrue(runTest("Set", "{value N = Set::average({1, 3},0); N == 4;};"));
		
		//mapper
		
		//min
		
		assertTrue(runTest("Set", "{Set::min({1, 2, 3, 2, 1}) == 1;};"));
		//assertTrue(runTest("Set", "{min({1, 2, 3, 2, 1}) == 1;};"));

		
		//max
		
		//multiply
		
		//reducer
		
		//size
		
		assertTrue(runTest("Set", "Set::size({}) == 0;"));
		assertTrue(runTest("Set", "size({}) == 0;"));
		assertTrue(runTest("Set", "Set::size({1}) == 1;"));
		assertTrue(runTest("Set", "Set::size({1,2,3}) == 3;"));
		
		//sum
		
		//toList
		
		assertTrue(runTest("Set", "{Set::toList({}) == [];};"));
		assertTrue(runTest("Set", "{toList({}) == [];};"));
		assertTrue(runTest("Set", "{Set::toList({1}) == [1];};"));
		assertTrue(runTest("Set", "{Set::toList({1, 2, 1}) == [1, 2];};"));
		
		
		//toMap
		
		//toRel
		
		//toString
		
		assertTrue(runTest("Set", "Set::toString({}) == \"{}\";"));
		assertTrue(runTest("Set", "toString({}) == \"{}\";"));
		assertTrue(runTest("Set", "Set::toString({1}) == \"{1}\";"));
		assertTrue(runTest("Set", "Set::toString({1, 2, 3}) == \"{1,2,3}\";"));
		
	}
}
