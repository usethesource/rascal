package org.rascalmpl.semantics.dynamic;

public abstract class LongLiteral extends org.rascalmpl.ast.LongLiteral {


public LongLiteral (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class DecimalLongLiteral extends org.rascalmpl.ast.LongLiteral.DecimalLongLiteral {


public DecimalLongLiteral (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.DecimalLongLiteral __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Ambiguity extends org.rascalmpl.ast.LongLiteral.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.LongLiteral> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class HexLongLiteral extends org.rascalmpl.ast.LongLiteral.HexLongLiteral {


public HexLongLiteral (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.HexLongLiteral __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class OctalLongLiteral extends org.rascalmpl.ast.LongLiteral.OctalLongLiteral {


public OctalLongLiteral (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.OctalLongLiteral __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}