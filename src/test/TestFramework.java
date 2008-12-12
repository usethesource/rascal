package test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Command;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.RascalBug;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;
import org.meta_environment.uptr.Factory;

public class TestFramework {
		private Parser parser = Parser.getInstance();
		private ASTFactory factory = new ASTFactory();
	    private ASTBuilder builder = new ASTBuilder(factory);
		private Evaluator evaluator = new Evaluator(ValueFactory.getInstance(), factory, new PrintWriter(System.err));
		
		boolean runTest(String statement) throws IOException {
			INode tree = parser.parse(new ByteArrayInputStream(statement.getBytes()));
			evaluator.clean();
		
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
					} else if(cmd.isDeclaration()){
						evaluator.eval(cmd.getDeclaration());
						return true;
					} else {
						throw new RascalBug("unexpected case in test: " + cmd);
					}
				}
			}
		    
			boolean runTest(String command1, String command2) throws IOException {
				INode tree1 = parser.parse(new ByteArrayInputStream(command1.getBytes()));
				INode tree2 = parser.parse(new ByteArrayInputStream(command2.getBytes()));
				Evaluator evaluator = new Evaluator(ValueFactory.getInstance(), factory, new PrintWriter(System.err));

				eval(evaluator, tree1);
				return eval(evaluator,tree2);
			}
		
}
