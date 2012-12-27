package org.rascalmpl.test.syntax;

import org.junit.Test;
import org.rascalmpl.test.infrastructure.TestFramework;


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
		testDecl("bool y =  (Sym) `<CaseInsensitiveStringConstant l>` :=  (Sym) `'hello'`;");
	}
	
	
	@Test
	public void nonterminal() {
		testDecl("bool y =  (Sym) `<Nonterminal l>` :=  (Sym) `X`;");
	}
	
	@Test
	public void param() {
		testDecl("bool y =  (Sym) `<Nonterminal n>[<{Sym \",\"}+ syms>]` :=  (Sym) `List[String,String]`;");
	}
	
	@Test
	public void stringlit() {
		testDecl("bool y =  (Sym) `<StringConstant l>` :=  (Sym) `\"hello\"`;");
	}

	
	@Test
	public void match1() {
		testDecl("bool y = (Expression) `<Expression e>` := (Expression) `1`;");
	}
}
