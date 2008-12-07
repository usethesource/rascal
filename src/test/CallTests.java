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

public class CallTests extends TestCase{
	private Parser parser = Parser.getInstance();
	private ASTFactory factory = new ASTFactory();
    private ASTBuilder builder = new ASTBuilder(factory);
	
	
	private boolean runTest(String statement) throws IOException {
		INode tree = parser.parse(new ByteArrayInputStream(statement.getBytes()));
		Evaluator evaluator = new Evaluator(ValueFactory.getInstance(), factory, new PrintWriter(System.err));

		if (tree.getTreeNodeType() ==  Factory.ParseTree_Summary) {
			System.err.println(tree);
			return false;
		} else {
			Command stat = builder.buildCommand(tree);
			IValue value = evaluator.eval(stat.getStatement());
			
			if (value == null || ! value.getType().isBoolType())
				return false;
			return value.equals(ValueFactory.getInstance().bool(true)) ? true : false;
		}
	}
	
	public void testFac() throws IOException {
		String fac = "int fac(int n){ return (n <= 0) ? 1 : (n * fac(n - 1));}";
		
		assertTrue(runTest("{" + fac + "; fac(0) == 1;}"));
		assertTrue(runTest("{" +  fac + "; fac(1) == 1;}"));
		assertTrue(runTest("{" + fac + "; fac(2) == 2;}"));
		assertTrue(runTest("{" + fac + "; fac(3) == 6;}"));
		assertTrue(runTest("{" + fac + "; fac(4) == 24;}"));
	}
	
	public void testMax() throws IOException {
		String maxInt = "int max(int a, int b) { return a > b ? a : b; }";
		String maxDouble = "double max(double a, double b) { return a > b ? a : b; }";
		assertTrue(runTest("{" + maxInt + "; max(3,4) == 4;}"));
	}
	
	
}

