package org.rascalmpl.semantics.dynamic;

public abstract class LocalVariableDeclaration extends org.rascalmpl.ast.LocalVariableDeclaration {


public LocalVariableDeclaration (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Default extends org.rascalmpl.ast.LocalVariableDeclaration.Default {


public Default (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Declarator __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		// TODO deal with dynamic variables
		return this.getDeclarator().__evaluate(__eval);
	
}

}
static public class Ambiguity extends org.rascalmpl.ast.LocalVariableDeclaration.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.LocalVariableDeclaration> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Dynamic extends org.rascalmpl.ast.LocalVariableDeclaration.Dynamic {


public Dynamic (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Declarator __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}