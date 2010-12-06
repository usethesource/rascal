package org.rascalmpl.ast;

public class ASTFactoryFactory {
	public static org.rascalmpl.ast.ASTFactory getASTFactory() {
		return new org.rascalmpl.semantics.dynamic.DynamicSemanticsASTFactory();
	}
}
