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
		
		assertTrue(runTest("Boolean", "{bool B = Boolean::arb(); (B == true) || (B == false);};"));
		//assertTrue(runTest("Boolean", "{bool B = arb(); (B == true) || (B == false);};"));
		
		assertTrue(runTest("Boolean", "Boolean::toInt(false) == 0;"));
		assertTrue(runTest("Boolean", "Boolean::toInt(true) == 1;"));
		
		//assertTrue(runTest("Boolean", "toInt(false) == 0;"));
		//assertTrue(runTest("Boolean", "toInt(true) == 1;"));
		
		assertTrue(runTest("Boolean", "Boolean::toDouble(false) == 0.0;"));
		assertTrue(runTest("Boolean", "Boolean::toDouble(true) == 1.0;"));
		
		//assertTrue(runTest("Boolean", "toDouble(false) == 0.0;"));
		//assertTrue(runTest("Boolean", "toDouble(true) == 1.0;"));
		
		assertTrue(runTest("Boolean", "Boolean::toString(false) == \"false\";"));
		assertTrue(runTest("Boolean", "Boolean::toString(true) == \"true\";"));
		
		//assertTrue(runTest("Boolean", "toString(false) == \"false\";"));
		//assertTrue(runTest("Boolean", "toString(true) == \"true\";"));
	}
	
	public void testInteger() throws IOException {
		assertTrue(runTest("Integer", "{int N = Integer::arb(10); (N >= 0) && (N < 10);};"));
		//assertTrue(runTest("Integer", "{int N = arb(10); (N >= 0) && (N < 10);};"));
		
		assertTrue(runTest("Integer", "Integer::toDouble(3) == 3.0;"));
		//assertTrue(runTest("Integer", "toDouble(3) == 3.0;"));
		
		assertTrue(runTest("Integer", "Integer::toString(314) == \"314\";"));
		//assertTrue(runTest("Integer", "toString(314) == \"314\";"));
		
	}
	public void testDouble() throws IOException {
		assertTrue(runTest("Double", "{double D = Double::arb(); (D >= 0.0) && (D <= 1.0);};"));
		//assertTrue(runTest("Double", "{double D = arb(10); (D >= 0.0) && (D <= 1.0);};"));
		
		assertTrue(runTest("Double", "Double::toInteger(3.14) == 3;"));
		//assertTrue(runTest("Double", "toInteger(3.14) == 3;"));
		
		
		assertTrue(runTest("Double", "Double::toString(3.14) == \"3.14\";"));
		//assertTrue(runTest("Double", "toString(3.14) == \"3.14\";"));
		
	}
}
