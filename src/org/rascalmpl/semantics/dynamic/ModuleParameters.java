package org.rascalmpl.semantics.dynamic;

public abstract class ModuleParameters extends org.rascalmpl.ast.ModuleParameters {


public ModuleParameters (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Ambiguity extends org.rascalmpl.ast.ModuleParameters.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.ModuleParameters> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Default extends org.rascalmpl.ast.ModuleParameters.Default {


public Default (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.TypeVar> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}