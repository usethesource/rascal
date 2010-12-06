package org.rascalmpl.semantics.dynamic;

public abstract class DataTarget extends org.rascalmpl.ast.DataTarget {


public DataTarget (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Labeled extends org.rascalmpl.ast.DataTarget.Labeled {


public Labeled (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Name __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.DataTarget.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.DataTarget> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Empty extends org.rascalmpl.ast.DataTarget.Empty {


public Empty (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}