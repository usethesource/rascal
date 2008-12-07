package test;

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
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;
import org.meta_environment.uptr.Factory;

public class PatternTests extends TestCase {
	private Parser parser = Parser.getInstance();
	private ASTFactory factory = new ASTFactory();
	private ASTBuilder builder = new ASTBuilder(factory);

	private boolean runTest(String statement) throws IOException {
		INode tree = parser
				.parse(new ByteArrayInputStream(statement.getBytes()));
		Evaluator evaluator = new Evaluator(ValueFactory.getInstance(),
				factory, new PrintWriter(System.err));

		if (tree.getTreeNodeType() == Factory.ParseTree_Summary) {
			System.err.println(tree);
			return false;
		} else {
			Command stat = builder.buildCommand(tree);
			IValue value = evaluator.eval(stat.getStatement());

			if (value == null || !value.getType().isBoolType())
				return false;
			return value.equals(ValueFactory.getInstance().bool(true)) ? true
					: false;
		}
	}
	
	public void testMatchLiteral() throws IOException {

		assertTrue(runTest("true := true;"));
		assertFalse(runTest("true := false;"));
		assertFalse(runTest("true := 1;"));
		assertFalse(runTest("true := \"abc\";"));
		
		assertTrue(runTest("1 := 1;"));
		assertFalse(runTest("1 := 2;"));
		assertFalse(runTest("1 := true;"));
		assertFalse(runTest("1 := 2;"));
		assertFalse(runTest("1 := 1.0;"));
		assertFalse(runTest("1 := \"abc\";"));
		
		assertTrue(runTest("1.5 := 1.5;"));
		assertFalse(runTest("1.5 := 2.5;"));
		assertFalse(runTest("1.5 := true;"));
		assertFalse(runTest("1.5 := 2;"));
		assertFalse(runTest("1.5 := 1.0;"));
		assertFalse(runTest("1.5 := \"abc\";"));
		
		assertTrue(runTest("\"abc\" := \"abc\";"));
		assertFalse(runTest("\"abc\" := \"def\";"));
		assertFalse(runTest("\"abc\" := true;"));
		assertFalse(runTest("\"abc\" := 1;"));
		assertFalse(runTest("\"abc\" := 1.5;"));
		assertFalse(runTest("\"abc\" := 1.5;"));
	}
	
	public void testMatchTuple() throws IOException {
		assertTrue(runTest("<1> := <1>;"));
		assertTrue(runTest("<1, \"abc\"> := <1, \"abc\">;"));
		assertFalse(runTest("<1> := <2>;"));
		assertFalse(runTest("<1> := <1, 2>;"));
		assertFalse(runTest("<1, \"abc\"> := <1, \"def\">;"));
	}
	
	public void testMatchTree() throws IOException {
		assertTrue(runTest("f(1) := f(1);"));
		assertTrue(runTest("f(1, g(\"abc\"), true) := f(1, g(\"abc\"), true);"));
		assertFalse(runTest("f(1) := 1;"));
		assertFalse(runTest("f(1) := 1.5;"));
		assertFalse(runTest("f(1) := \"abc\";"));
		assertFalse(runTest("f(1) := g(1);"));
		assertFalse(runTest("f(1) := f(1, 2);"));
	}
	
	public void testMatchVariable() throws IOException {
		assertTrue(runTest("(n := 1) && (n == 1);"));
		assertTrue(runTest("{int n = 1; (n := 1) && (n == 1);};"));
		assertTrue(runTest("{int n = 1; (n !:= 2) && (n == 1);};"));
		assertTrue(runTest("{int n = 1; (n !:= \"abc\") && (n == 1);};"));
		
		assertTrue(runTest("(f(n) := f(1)) && (n == 1);"));
		assertTrue(runTest("{int n = 1; (f(n) := f(1)) && (n == 1);};"));
	}
	
}
