package org.rascalmpl.semantics.dynamic;

public abstract class Renamings extends org.rascalmpl.ast.Renamings {


public Renamings (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Default extends org.rascalmpl.ast.Renamings.Default {


public Default (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Renaming> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.Renamings.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Renamings> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}