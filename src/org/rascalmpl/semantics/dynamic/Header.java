package org.rascalmpl.semantics.dynamic;

public abstract class Header extends org.rascalmpl.ast.Header {


public Header (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Parameters extends org.rascalmpl.ast.Header.Parameters {


public Parameters (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Tags __param2,org.rascalmpl.ast.QualifiedName __param3,org.rascalmpl.ast.ModuleParameters __param4,java.util.List<org.rascalmpl.ast.Import> __param5) {
	super(__param1,__param2,__param3,__param4,__param5);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		__eval.visitImports(this.getImports());
		return org.rascalmpl.interpreter.result.ResultFactory.nothing();
	
}

}
static public class Ambiguity extends org.rascalmpl.ast.Header.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Header> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Default extends org.rascalmpl.ast.Header.Default {


public Default (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Tags __param2,org.rascalmpl.ast.QualifiedName __param3,java.util.List<org.rascalmpl.ast.Import> __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __evaluate(org.rascalmpl.interpreter.Evaluator __eval) {
	
		__eval.visitImports(this.getImports());
		return org.rascalmpl.interpreter.result.ResultFactory.nothing();
	
}

@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}