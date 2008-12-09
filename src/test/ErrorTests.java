package test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import junit.framework.TestCase;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Command;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.RascalException;
import org.meta_environment.rascal.interpreter.RascalTypeError;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;
import org.meta_environment.uptr.Factory;

public class ErrorTests extends TestCase{
	private Parser parser = Parser.getInstance();
	private ASTFactory factory = new ASTFactory();
    private ASTBuilder builder = new ASTBuilder(factory);
	private Evaluator evaluator = new Evaluator(ValueFactory.getInstance(), factory, new PrintWriter(System.err));
	
	private boolean runTest(String statement, String msg) throws IOException {
		INode tree = parser.parse(new ByteArrayInputStream(statement.getBytes()));
		evaluator.clean();

		if (tree.getTreeNodeType() ==  Factory.ParseTree_Summary) {
			System.err.println(tree);
			return false;
		} else {
			try {
				Command stat = builder.buildCommand(tree);
				IValue value = evaluator.eval(stat.getStatement());
				return false;
			}
			catch (RascalTypeError e){
				if(e.getMessage().indexOf(msg) >= 0){
					return true;
				}
				return false;
			}
		}
	}
	
	public void testErrors() throws IOException{
		assertTrue(runTest("int i = true;", "declared type integer incompatible with initialization type bool"));
		assertTrue(runTest("assert \"a\": 3.5;", "expression in assertion should be bool instead of double"));
		//assertTrue(runTest("{int n = 3; n = true;}", "cannot assign value of type bool"));
		assertTrue(runTest("[1,2][5];", "Subscript out of bounds"));
		//assertTrue(runTest("x.a;", "has no field named a"));
		assertTrue(runTest("if(3){n = 4;}", "has type integer but should be bool"));
		assertTrue(runTest("while(3){n = 4;}", "has type integer but should be bool"));
		assertTrue(runTest("do {n = 4;} while(3);", "has type integer but should be bool"));
		assertTrue(runTest("n;", "Uninitialized variable"));
		assertTrue(runTest("3 + true;", "Operands of + have illegal types"));
		assertTrue(runTest("3 - true;", "Operands of - have illegal types"));
		assertTrue(runTest("- true;", "Operand of unary - should be"));
		assertTrue(runTest("3 * true;", "Operands of * have illegal types"));
		assertTrue(runTest("3 / true;", "Operands of / have illegal types"));
		assertTrue(runTest("3 % true;", "Operands of % have illegal types"));
		assertTrue(runTest("3 || true;", "Operands of || should be boolean instead of"));
		assertTrue(runTest("3 && true;", "Operands of && should be boolean instead of"));
		assertTrue(runTest("3 ==> true;", "Operands of ==> should be boolean instead of"));
		assertTrue(runTest("3 == true;", "Operands of == should have equal types instead of"));
		assertTrue(runTest("3 < true;", "Operands of comparison have different types"));
		assertTrue(runTest("3 <= true;", "Operands of comparison have different types"));
		assertTrue(runTest("3 > true;", "Operands of comparison have different types"));
		assertTrue(runTest("3 >= true;", "Operands of comparison have different types"));
		assertTrue(runTest("1 ? 2 : 3;", "but should be bool"));
		assertTrue(runTest("1 in 3;", "Operands of in have wrong types"));
		assertTrue(runTest("1 o 3;", "Operands of o have wrong types"));
		assertTrue(runTest("1*;", "Operand of + or * closure has wrong type"));
		assertTrue(runTest("1+;", "Operand of + or * closure has wrong type"));
		assertTrue(runTest("{x | x : 3};", "expression in generator should be of type list/set"));
		assertTrue(runTest("{x | 5};", "Expression as generator should have type bool"));
		assertTrue(runTest("exists(x : [1,2,3] | \"abc\");", "expression in exists should yield bool"));
		assertTrue(runTest("forall(x : [1,2,3] | \"abc\");", "expression in forall should yield bool"));
	}
}
