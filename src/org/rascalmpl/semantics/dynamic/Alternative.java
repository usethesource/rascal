package org.rascalmpl.semantics.dynamic;

public abstract class Alternative extends org.rascalmpl.ast.Alternative {


public Alternative (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Ambiguity extends org.rascalmpl.ast.Alternative.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Alternative> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class NamedType extends org.rascalmpl.ast.Alternative.NamedType {


public NamedType (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Name __param2,org.rascalmpl.ast.Type __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}