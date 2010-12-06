package org.rascalmpl.semantics.dynamic;

public abstract class SyntaxDefinition extends org.rascalmpl.ast.SyntaxDefinition {


public SyntaxDefinition (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Ambiguity extends org.rascalmpl.ast.SyntaxDefinition.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.SyntaxDefinition> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Layout extends org.rascalmpl.ast.SyntaxDefinition.Layout {


public Layout (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Sym __param2,org.rascalmpl.ast.Prod __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Language extends org.rascalmpl.ast.SyntaxDefinition.Language {


public Language (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Start __param2,org.rascalmpl.ast.Sym __param3,org.rascalmpl.ast.Prod __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}