package org.rascalmpl.semantics.dynamic;

public abstract class FunctionModifiers extends org.rascalmpl.ast.FunctionModifiers {


public FunctionModifiers (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class List extends org.rascalmpl.ast.FunctionModifiers.List {


public List (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.FunctionModifier> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.FunctionModifiers.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.FunctionModifiers> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}