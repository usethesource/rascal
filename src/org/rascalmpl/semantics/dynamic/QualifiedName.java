package org.rascalmpl.semantics.dynamic;

public abstract class QualifiedName extends org.rascalmpl.ast.QualifiedName {


public QualifiedName (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Ambiguity extends org.rascalmpl.ast.QualifiedName.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.QualifiedName> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Default extends org.rascalmpl.ast.QualifiedName.Default {


public Default (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Name> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}