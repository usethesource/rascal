package org.rascalmpl.semantics.dynamic;

public abstract class TypeVar extends org.rascalmpl.ast.TypeVar {


public TypeVar (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Ambiguity extends org.rascalmpl.ast.TypeVar.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.TypeVar> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Bounded extends org.rascalmpl.ast.TypeVar.Bounded {


public Bounded (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Name __param2,org.rascalmpl.ast.Type __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Free extends org.rascalmpl.ast.TypeVar.Free {


public Free (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Name __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}