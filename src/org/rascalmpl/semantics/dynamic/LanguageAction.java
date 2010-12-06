package org.rascalmpl.semantics.dynamic;

public abstract class LanguageAction extends org.rascalmpl.ast.LanguageAction {


public LanguageAction (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Build extends org.rascalmpl.ast.LanguageAction.Build {


public Build (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Expression __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.LanguageAction.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.LanguageAction> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Action extends org.rascalmpl.ast.LanguageAction.Action {


public Action (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.Statement> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}