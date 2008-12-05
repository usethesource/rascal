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
		Evaluator evaluator = new Evaluator(ValueFactory.getInstance(), factory, new PrintWriter(System.err));

		eval(evaluator, tree1);
		return eval(evaluator,tree2);
	}
	
	public void testBoolean() throws IOException {
		//arb
		
		assertTrue(runTest("Boolean", "{bool B = Boolean::arb(); (B == true) || (B == false);};"));
		assertTrue(runTest("Boolean", "{bool B = arb(); (B == true) || (B == false);};"));
		
		//toInt
		
		assertTrue(runTest("Boolean", "Boolean::toInt(false) == 0;"));
		assertTrue(runTest("Boolean", "Boolean::toInt(true) == 1;"));
		
		assertTrue(runTest("Boolean", "toInt(false) == 0;"));
		assertTrue(runTest("Boolean", "toInt(true) == 1;"));
		
		//toDouble
		
		assertTrue(runTest("Boolean", "Boolean::toDouble(false) == 0.0;"));
		assertTrue(runTest("Boolean", "Boolean::toDouble(true) == 1.0;"));
		
		assertTrue(runTest("Boolean", "toDouble(false) == 0.0;"));
		assertTrue(runTest("Boolean", "toDouble(true) == 1.0;"));
		
		//toString
		
		assertTrue(runTest("Boolean", "Boolean::toString(false) == \"false\";"));
		assertTrue(runTest("Boolean", "Boolean::toString(true) == \"true\";"));
		
		assertTrue(runTest("Boolean", "toString(false) == \"false\";"));
		assertTrue(runTest("Boolean", "toString(true) == \"true\";"));
	}
	
	public void testInteger() throws IOException {
		
		//arb
		
		assertTrue(runTest("Integer", "{int N = Integer::arb(10); (N >= 0) && (N < 10);};"));
		assertTrue(runTest("Integer", "{int N = arb(10); (N >= 0) && (N < 10);};"));
		
		// max
		
		assertTrue(runTest("Integer", "Integer::max(3, 10) == 10;"));
		assertTrue(runTest("Integer", "max(3, 10) == 10;"));
		assertTrue(runTest("Integer", "Integer::max(10, 10) == 10;"));
		
		// min
		
		assertTrue(runTest("Integer", "Integer::min(3, 10) == 1;"));
		assertTrue(runTest("Integer", "min(3, 10) == 13;"));
		assertTrue(runTest("Integer", "Integer::min(10, 10) == 10;"));
		
		//toDouble
		
		assertTrue(runTest("Integer", "Integer::toDouble(3) == 3.0;"));
		assertTrue(runTest("Integer", "toDouble(3) == 3.0;"));
		
		//toString
		
		assertTrue(runTest("Integer", "Integer::toString(314) == \"314\";"));
		assertTrue(runTest("Integer", "toString(314) == \"314\";"));
		
	}
	
	public void testDouble() throws IOException {
		
		// arb
		
		assertTrue(runTest("Double", "{double D = Double::arb(); (D >= 0.0) && (D <= 1.0);};"));
		//assertTrue(runTest("Double", "{double D = arb(10); (D >= 0.0) && (D <= 1.0);};"));
		
		// max
		
		assertTrue(runTest("Double", "Double::max(3.0, 10.0) == 10.0;"));
		assertTrue(runTest("Double", "max(3.0, 10.0) == 10.0;"));
		assertTrue(runTest("Double", "Double::max(10.0, 10.0) == 10.0;"));
		
		// min
		
		assertTrue(runTest("Double", "Double::min(3.0, 10.0) == 3.0;"));
		assertTrue(runTest("Double", "min(3.0, 10.0) == 3.0;"));
		assertTrue(runTest("Double", "Double::min(10.0, 10.0) == 10.0;"));
		
		//toInteger
		
		assertTrue(runTest("Double", "Double::toInteger(3.14) == 3;"));
		assertTrue(runTest("Double", "toInteger(3.14) == 3;"));
		
		//toString
		
		assertTrue(runTest("Double", "Double::toString(3.14) == \"3.14\";"));
		assertTrue(runTest("Double", "toString(3.14) == \"3.14\";"));
		
	}
	
	public void testString() throws IOException {
		
		//charAT
		
		assertTrue(runTest("String", "String::charAt(\"abc\", 0) == 97;"));
		assertTrue(runTest("String", "String::charAt(\"abc\", 1) == 98;"));
		assertTrue(runTest("String", "String::charAt(\"abc\", 2) == 99;"));
		assertTrue(runTest("String", "charAt(\"abc\", 0) == 97;"));
		
		//endsWith
		
		assertTrue(runTest("String", "String::endsWith(\"abc\", \"abc\");"));
		assertTrue(runTest("String", "endsWith(\"abc\", \"abc\");"));
		assertTrue(runTest("String", "String::endsWith(\"abcdef\", \"def\");"));
		assertFalse(runTest("String", "String::endsWith(\"abcdef\", \"abc\");"));
		
		//reverse
		
		assertTrue(runTest("String", "String::reverse(\"\") == \"\";"));
		assertTrue(runTest("String", "reverse(\"\") == \"\";"));
		assertTrue(runTest("String", "String::reverse(\"abc\") == \"cba\";"));
		
		//size
		
		assertTrue(runTest("String", "String::size(\"\") == 0;"));
		assertTrue(runTest("String", "size(\"\") == 0;"));
		assertTrue(runTest("String", "String::size(\"abc\") == 3;"));
		
		//startsWith
		
		assertTrue(runTest("String", "String::startsWith(\"abc\", \"abc\");"));
		assertTrue(runTest("String", "startsWith(\"abc\", \"abc\");"));
		assertTrue(runTest("String", "String::startsWith(\"abcdef\", \"abc\");"));
		assertFalse(runTest("String", "String::startsWith(\"abcdef\", \"def\");"));
		
		//toLowerCase
		
		assertTrue(runTest("String", "String::toLowerCase(\"\") == \"\";"));
		assertTrue(runTest("String", "toLowerCase(\"\") ==  \"\";"));
		assertTrue(runTest("String", "String::toLowerCase(\"ABC\") == \"abc\";"));
		assertTrue(runTest("String", "String::toLowerCase(\"ABC123\") == \"abc123\";"));
		
		//toUpperCase
		
		assertTrue(runTest("String", "String::toUpperCase(\"\") == \"\";"));
		assertTrue(runTest("String", "toUpperCase(\"\") == \"\";"));
		assertTrue(runTest("String", "String::toUpperCase(\"abc\") == \"ABC\";"));
		assertTrue(runTest("String", "String::toUpperCase(\"abc123\") == \"ABC123\";"));
	}
	
	public void testList() throws IOException {
		
		// TODO: Replace value N by int N;
		
		//arb
		
		assertTrue(runTest("List", "{value N = List::arb([1]); N == 1;};"));
		assertTrue(runTest("List", "{value N = arb([1]); N == 1;};"));
		assertTrue(runTest("List", "{value N = List::arb([1,2]); (N == 1) || (N == 2);};"));
		assertTrue(runTest("List", "{value N = List::arb([1,2,3]); (N == 1) || (N == 2) || (N == 3);};"));
		
		//average
		
		//assertTrue(runTest("List", "{value N = List::average([]); N == 0;};"));
		//assertTrue(runTest("List", "{value N = average([]); N == 0;};"));
		//assertTrue(runTest("List", "{value N = List::average([1]); N == 1;};"));
		//assertTrue(runTest("List", "{value N = List::average([1, 3]); N == 4;};"));
		//first
		
		assertTrue(runTest("List", "{List::first([1]) == 1;};"));
		assertTrue(runTest("List", "{first([1]) == 1;};"));
		assertTrue(runTest("List", "{List::first([1, 2]) == 1;};"));
		
		//mapper
		
		//max
		
		//assertTrue(runTest("List", "{List::max([1, 2, 3, 2, 1]) == 3;};"));
		//assertTrue(runTest("List", "{max([1, 2, 3, 2, 1]) == 3;};"));
		
		//min
		
		//assertTrue(runTest("List", "{List::min([1, 2, 3, 2, 1]) == 1;};"));
		//assertTrue(runTest("List", "{min([1, 2, 3, 2, 1]) == 1;};"));
		
		//multiply
		
		//reducer
		
		//rest
		
		assertTrue(runTest("List", "{List::rest([1]) == [];};"));
		assertTrue(runTest("List", "{rest([1]) == [];};"));
		assertTrue(runTest("List", "{List::rest([1, 2]) == [2];};"));
		
		//reverse
		assertTrue(runTest("List", "{List::reverse([]) == [];};"));
		assertTrue(runTest("List", "{reverse([]) == [];};"));
		assertTrue(runTest("List", "{List::reverse([1]) == [1];};"));
		assertTrue(runTest("List", "{List::reverse([1,2,3]) == [3,2,1];};"));
		
		//size
		
		assertTrue(runTest("List", "{List::size([]) == 0;};"));
		assertTrue(runTest("List", "{size([]) == 0;};"));
		assertTrue(runTest("List", "{List::size([1]) == 1;};"));
		assertTrue(runTest("List", "{List::size([1,2,3]) == 3;};"));
		
		//sort
		
		//sum
		
		//assertTrue(runTest("List", "{List::sum([], 0) == 0;};"));
		//assertTrue(runTest("List", "{List::sum([], 0) == 0;};"));
		//assertTrue(runTest("List", "{List::sum([1], 0) == 1;};"));
		//assertTrue(runTest("List", "{List::sum([1, 2], 0) == 3;};"));
		//assertTrue(runTest("List", "{List::sum([1, 2, 3], 0) == 6;};"));
		//assertTrue(runTest("List", "{List::sum([1, -2, 3], 0) == 2;};"));
		//assertTrue(runTest("List", "{List::sum([1, 1, 1], 0) == 3;};"));
		
		//toSet
		
		assertTrue(runTest("List", "{List::toSet([]) == {};};"));
		assertTrue(runTest("List", "{toSet([]) == {};};"));
		assertTrue(runTest("List", "{List::toSet([1]) == {1};};"));
		assertTrue(runTest("List", "{List::toSet([1, 2, 1]) == {1, 2};};"));
		
		//toMap
		
		//assertTrue(runTest("List", "{List::toMap([]) == ();};"));
		//assertTrue(runTest("List", "{toMap([]) == ();};"));
		assertTrue(runTest("List", "{List::toMap([<1,10>, <2,20>]) == (1:10, 2:20);};"));
		
		//toString
		
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
		
		//average
		
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
