package org.rascalmpl.semantics.dynamic;

public abstract class Body extends org.rascalmpl.ast.Body {


public Body (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Toplevels extends org.rascalmpl.ast.Body.Toplevels {


public Toplevels (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Toplevel> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.Body.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Body> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}