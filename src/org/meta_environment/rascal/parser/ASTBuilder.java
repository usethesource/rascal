package org.meta_environment.rascal.parser;

import java.lang.reflect.Method;

import org.eclipse.imp.pdb.facts.ITree;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Expression;
import org.meta_environment.rascal.ast.Module;
import org.meta_environment.rascal.ast.Statement;
import org.meta_environment.uptr.TreeWrapper;

/**
 * Uses reflection to construct an AST hierarchy from a 
 * UPTR parse tree of a rascal program.
 *
 */
public class ASTBuilder {
	private ASTFactory factory;
    private Class<? extends ASTFactory> clazz;
    
	public ASTBuilder(ASTFactory factory) {
		this.factory = factory;
		this.clazz = factory.getClass();
	}
	
	public Module buildModule(ITree parseTree) {

	}
	
	public Expression buildExpression(ITree expression) {

	}
	
	public Statement buildStatement(ITree statement) {
		return null;
	}

	private <T> T buildNode(ITree in) {
		TreeWrapper tree = new TreeWrapper(in);
		
		String cons = tree.getConstructorName();
		String sort = tree.getProduction().getSortName();
	}

}
