package org.rascalmpl.semantics.dynamic;

public abstract class ProdModifier extends org.rascalmpl.ast.ProdModifier {


public ProdModifier (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Ambiguity extends org.rascalmpl.ast.ProdModifier.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.ProdModifier> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Associativity extends org.rascalmpl.ast.ProdModifier.Associativity {


public Associativity (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Assoc __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Tag extends org.rascalmpl.ast.ProdModifier.Tag {


public Tag (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.Tag __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Bracket extends org.rascalmpl.ast.ProdModifier.Bracket {


public Bracket (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Lexical extends org.rascalmpl.ast.ProdModifier.Lexical {


public Lexical (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}