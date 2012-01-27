package org.rascalmpl.test;

import org.junit.Test;


public class PreBootstrapTests extends TestFramework {

	private void testDecl(String decl) {
		prepareModule("X", 
				"@bootstrapParser module X import lang::rascal::syntax::RascalRascal;\n" + decl);
		prepareMore("import X;");
	}
	
	@Test
	public void expr1() {
		testDecl("Expression y = (Expression) `1`;");
	}
	
	@Test
	public void caseinsensitivelit() {
		testDecl("bool y =  (Sym) `<CaseInsensitiveStringConstant l>` :=  (Sym) `'hello'`");
	}
	
	@Test
	public void match1() {
		testDecl("bool y = (Expression) `<Expression e>` := (Expression) `1`;");
	}
}
