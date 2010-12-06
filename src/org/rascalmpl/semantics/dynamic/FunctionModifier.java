package org.rascalmpl.semantics.dynamic;

public abstract class FunctionModifier extends org.rascalmpl.ast.FunctionModifier {


public FunctionModifier (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Ambiguity extends org.rascalmpl.ast.FunctionModifier.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.FunctionModifier> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Java extends org.rascalmpl.ast.FunctionModifier.Java {


public Java (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}