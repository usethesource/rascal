package org.rascalmpl.semantics.dynamic;

public abstract class ModuleActuals extends org.rascalmpl.ast.ModuleActuals {


public ModuleActuals (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Default extends org.rascalmpl.ast.ModuleActuals.Default {


public Default (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Type> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.ModuleActuals.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.ModuleActuals> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}