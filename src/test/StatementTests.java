package test;

import junit.framework.TestCase;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Command;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;
import org.meta_environment.uptr.Factory;

public class StatementTests extends TestCase {
	private Parser parser = Parser.getInstance();
	private ASTFactory factory = new ASTFactory();
    private ASTBuilder builder = new ASTBuilder(factory);
	
	
	private boolean runTest(String statement) throws IOException {
		INode tree = parser.parse(new ByteArrayInputStream(statement.getBytes()));
		Evaluator evaluator = new Evaluator(ValueFactory.getInstance(), factory);

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
	
    public class All extends TestCase {
    	
	}
    
    public void testAssert() throws IOException {
    	assertTrue(runTest("assert \"1\": 3 > 2;"));
	}
	
	public void testAssignmnment() throws IOException {
		
	}
	
	public void testBlock() throws IOException {
	}
	
	public void testBreak() throws IOException {
	}
	
	public void testContinue() throws IOException {
	}
	
	public void testDoWhile()throws IOException {
	}
	
	public void testFail() throws IOException {
	}
	
	public void testFirst() throws IOException {
	}
	public void testFor() throws IOException {
	}
	public void testIfThen() throws IOException {
	}
	public void testIfThenElse() throws IOException {
	}
	public void testInsert() throws IOException {
	}
	public void testReturn() throws IOException {
	}
	public void testSolve() throws IOException {
	}
	public void testSwitch() throws IOException {
	}
	public void testThrow() throws IOException {
	}
	public void testTryFinally() throws IOException {
	}
	public void testVisit() throws IOException {
	}
	public void testWhile() throws IOException {
	}
	
	public void testTry() throws IOException {
	}
}
