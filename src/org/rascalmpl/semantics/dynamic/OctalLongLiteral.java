package org.rascalmpl.semantics.dynamic;

public abstract class OctalLongLiteral extends org.rascalmpl.ast.OctalLongLiteral {


public OctalLongLiteral (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Lexical extends org.rascalmpl.ast.OctalLongLiteral.Lexical {


public Lexical (org.eclipse.imp.pdb.facts.INode __param1,java.lang.String __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.OctalLongLiteral.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.OctalLongLiteral> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}