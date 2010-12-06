package org.rascalmpl.semantics.dynamic;

public abstract class Variant extends org.rascalmpl.ast.Variant {


public Variant (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Ambiguity extends org.rascalmpl.ast.Variant.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Variant> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class NAryConstructor extends org.rascalmpl.ast.Variant.NAryConstructor {


public NAryConstructor (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Name __param2,java.util.List<org.rascalmpl.ast.TypeArg> __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}