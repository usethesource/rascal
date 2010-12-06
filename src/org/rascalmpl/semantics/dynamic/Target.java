package org.rascalmpl.semantics.dynamic;

public abstract class Target extends org.rascalmpl.ast.Target {


public Target (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Labeled extends org.rascalmpl.ast.Target.Labeled {


public Labeled (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Name __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Empty extends org.rascalmpl.ast.Target.Empty {


public Empty (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.Target.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Target> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}