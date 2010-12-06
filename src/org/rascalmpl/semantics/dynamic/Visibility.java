package org.rascalmpl.semantics.dynamic;

public abstract class Visibility extends org.rascalmpl.ast.Visibility {


public Visibility (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Private extends org.rascalmpl.ast.Visibility.Private {


public Private (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Default extends org.rascalmpl.ast.Visibility.Default {


public Default (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.Visibility.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Visibility> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Public extends org.rascalmpl.ast.Visibility.Public {


public Public (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}