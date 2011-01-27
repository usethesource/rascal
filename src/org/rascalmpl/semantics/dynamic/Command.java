package org.rascalmpl.semantics.dynamic;

public abstract class Command extends org.rascalmpl.ast.Command {


public Command (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Import extends org.rascalmpl.ast.Command.Import {


public Import (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Import __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		__eval.setCurrentAST(this);
		return this.getImported().interpret(__eval);
	
}

}
static public class Shell extends org.rascalmpl.ast.Command.Shell {


public Shell (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.ShellCommand __param2) {
	super(__param1,__param2);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		__eval.setCurrentAST(this);
		return this.getCommand().interpret(__eval);
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.Command.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Command> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		throw new org.rascalmpl.interpreter.asserts.Ambiguous((org.eclipse.imp.pdb.facts.IConstructor) this.getTree());
	
}

}
static public class Statement extends org.rascalmpl.ast.Command.Statement {


public Statement (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Statement __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		__eval.setCurrentAST(this.getStatement());
		return __eval.eval(this.getStatement());
	
}

}
static public class Expression extends org.rascalmpl.ast.Command.Expression {


public Expression (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		org.rascalmpl.interpreter.env.Environment old = __eval.getCurrentEnvt();

		try {
			__eval.pushEnv();
			__eval.setCurrentAST(this.getExpression());
			return this.getExpression().interpret(__eval);
		}
		finally {
			__eval.unwind(old);
		}
	
}

}
static public class Declaration extends org.rascalmpl.ast.Command.Declaration {


public Declaration (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Declaration __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		__eval.setCurrentAST(this);
		return this.getDeclaration().interpret(__eval);
	
}

}
}