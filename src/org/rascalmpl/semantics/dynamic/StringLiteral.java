package org.rascalmpl.semantics.dynamic;

public abstract class StringLiteral extends org.rascalmpl.ast.StringLiteral {


public StringLiteral (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Ambiguity extends org.rascalmpl.ast.StringLiteral.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.StringLiteral> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Template extends org.rascalmpl.ast.StringLiteral.Template {


public Template (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.PreStringChars __param2,org.rascalmpl.ast.StringTemplate __param3,org.rascalmpl.ast.StringTail __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class Interpolated extends org.rascalmpl.ast.StringLiteral.Interpolated {


public Interpolated (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.PreStringChars __param2,org.rascalmpl.ast.Expression __param3,org.rascalmpl.ast.StringTail __param4) {
	super(__param1,__param2,__param3,__param4);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
static public class NonInterpolated extends org.rascalmpl.ast.StringLiteral.NonInterpolated {


public NonInterpolated (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.StringConstant __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}