package org.rascalmpl.semantics.dynamic;

public abstract class Variable extends org.rascalmpl.ast.Variable {


public Variable (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class UnInitialized extends org.rascalmpl.ast.Variable.UnInitialized {


public UnInitialized (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Name __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.Variable.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Variable> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Initialized extends org.rascalmpl.ast.Variable.Initialized {


public Initialized (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Name __param2,org.rascalmpl.ast.Expression __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}