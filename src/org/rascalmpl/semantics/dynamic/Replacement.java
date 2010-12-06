package org.rascalmpl.semantics.dynamic;

public abstract class Replacement extends org.rascalmpl.ast.Replacement {


public Replacement (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Conditional extends org.rascalmpl.ast.Replacement.Conditional {


public Conditional (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2,java.util.List<org.rascalmpl.ast.Expression> __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Unconditional extends org.rascalmpl.ast.Replacement.Unconditional {


public Unconditional (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.Replacement.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Replacement> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}