package org.rascalmpl.semantics.dynamic;

public abstract class Label extends org.rascalmpl.ast.Label {


public Label (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Empty extends org.rascalmpl.ast.Label.Empty {


public Empty (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.Label.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Label> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Default extends org.rascalmpl.ast.Label.Default {


public Default (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Name __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}