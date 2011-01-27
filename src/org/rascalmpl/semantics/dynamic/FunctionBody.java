package org.rascalmpl.semantics.dynamic;

public abstract class FunctionBody extends org.rascalmpl.ast.FunctionBody {


public FunctionBody (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Default extends org.rascalmpl.ast.FunctionBody.Default {


public Default (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Statement> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> result = org.rascalmpl.interpreter.result.ResultFactory.nothing();

		for (org.rascalmpl.ast.Statement statement : this.getStatements()) {
			__eval.setCurrentAST(statement);
			result = statement.interpret(__eval);
		}

		return result;
	
}

}
static public class Ambiguity extends org.rascalmpl.ast.FunctionBody.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.FunctionBody> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}