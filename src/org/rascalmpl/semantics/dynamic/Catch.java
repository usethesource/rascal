package org.rascalmpl.semantics.dynamic;

public abstract class Catch extends org.rascalmpl.ast.Catch {


public Catch (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Binding extends org.rascalmpl.ast.Catch.Binding {


public Binding (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,org.rascalmpl.ast.Statement __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Default extends org.rascalmpl.ast.Catch.Default {


public Default (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Statement __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.Catch.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Catch> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}