package org.rascalmpl.semantics.dynamic;

public abstract class Field extends org.rascalmpl.ast.Field {


public Field (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Index extends org.rascalmpl.ast.Field.Index {


public Index (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.IntegerLiteral __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.Field.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Field> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Name extends org.rascalmpl.ast.Field.Name {


public Name (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Name __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}