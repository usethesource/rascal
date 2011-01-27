package org.rascalmpl.semantics.dynamic;

public abstract class Test extends org.rascalmpl.ast.Test {


public Test (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Labeled extends org.rascalmpl.ast.Test.Labeled {


public Labeled (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Tags __param2,org.rascalmpl.ast.Expression __param3,org.rascalmpl.ast.StringLiteral __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		__eval.getCurrentModuleEnvironment().addTest(this);
		return org.rascalmpl.interpreter.result.ResultFactory.nothing();
	
}

}
static public class Ambiguity extends org.rascalmpl.ast.Test.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Test> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Unlabeled extends org.rascalmpl.ast.Test.Unlabeled {


public Unlabeled (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Tags __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> interpret(org.rascalmpl.interpreter.Evaluator __eval) {
	
		__eval.getCurrentModuleEnvironment().addTest(this);
		return org.rascalmpl.interpreter.result.ResultFactory.nothing();
	
}

}
}