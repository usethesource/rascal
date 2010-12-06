package org.rascalmpl.semantics.dynamic;

public abstract class TimePartNoTZ extends org.rascalmpl.ast.TimePartNoTZ {


public TimePartNoTZ (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Ambiguity extends org.rascalmpl.ast.TimePartNoTZ.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.TimePartNoTZ> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Lexical extends org.rascalmpl.ast.TimePartNoTZ.Lexical {


public Lexical (org.eclipse.imp.pdb.facts.INode __param1,java.lang.String __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}