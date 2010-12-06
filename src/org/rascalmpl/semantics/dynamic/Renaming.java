package org.rascalmpl.semantics.dynamic;

public abstract class Renaming extends org.rascalmpl.ast.Renaming {


public Renaming (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Default extends org.rascalmpl.ast.Renaming.Default {


public Default (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Name __param2,org.rascalmpl.ast.Name __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.Renaming.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Renaming> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}