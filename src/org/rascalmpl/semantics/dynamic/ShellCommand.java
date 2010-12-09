package org.rascalmpl.semantics.dynamic;

public abstract class ShellCommand extends org.rascalmpl.ast.ShellCommand {


public ShellCommand (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class History extends org.rascalmpl.ast.ShellCommand.History {


public History (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class SetOption extends org.rascalmpl.ast.ShellCommand.SetOption {


public SetOption (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.QualifiedName __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		java.lang.String name = "rascal.config."+this.getName().toString();
		java.lang.String value = this.getExpression().__evaluate(__eval).getValue().toString();

		java.lang.System.setProperty(name, value);

		__eval.updateProperties();

		return org.rascalmpl.interpreter.result.ResultFactory.nothing();
	
}

}
static public class ListDeclarations extends org.rascalmpl.ast.ShellCommand.ListDeclarations {


public ListDeclarations (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		__eval.printVisibleDeclaredObjects(__eval.getStdOut());
		return org.rascalmpl.interpreter.result.ResultFactory.nothing();
	
}

}
static public class Help extends org.rascalmpl.ast.ShellCommand.Help {


public Help (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		__eval.setCurrentAST(this);
		__eval.printHelpMessage(__eval.getStdOut());
		return org.rascalmpl.interpreter.result.ResultFactory.nothing();
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Undeclare extends org.rascalmpl.ast.ShellCommand.Undeclare {


public Undeclare (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.QualifiedName __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Test extends org.rascalmpl.ast.ShellCommand.Test {


public Test (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return org.rascalmpl.interpreter.result.ResultFactory.bool(__eval.runTests(), __eval);
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.ShellCommand.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.ShellCommand> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Quit extends org.rascalmpl.ast.ShellCommand.Quit {


public Quit (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		throw new org.rascalmpl.interpreter.control_exceptions.QuitException();
	
}

}
static public class Unimport extends org.rascalmpl.ast.ShellCommand.Unimport {


public Unimport (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.QualifiedName __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		((org.rascalmpl.interpreter.env.ModuleEnvironment) __eval.getCurrentEnvt().getRoot()).unImport(this.getName().toString());
		return org.rascalmpl.interpreter.result.ResultFactory.nothing();
	
}

}
static public class ListModules extends org.rascalmpl.ast.ShellCommand.ListModules {


public ListModules (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Edit extends org.rascalmpl.ast.ShellCommand.Edit {


public Edit (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.QualifiedName __param2) {
	super(__param1,__param2);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		return org.rascalmpl.interpreter.result.ResultFactory.nothing();
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}