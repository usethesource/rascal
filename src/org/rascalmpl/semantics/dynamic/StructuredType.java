package org.rascalmpl.semantics.dynamic;

public abstract class StructuredType extends org.rascalmpl.ast.StructuredType {


public StructuredType (org.eclipse.imp.pdb.facts.INode __param1) {
	super(__param1);
}
static public class Default extends org.rascalmpl.ast.StructuredType.Default {


public Default (org.eclipse.imp.pdb.facts.INode __param1,org.rascalmpl.ast.BasicType __param2,java.util.List<org.rascalmpl.ast.TypeArg> __param3) {
	super(__param1,__param2,__param3);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

@Override
public org.eclipse.imp.pdb.facts.type.Type __evaluate(org.rascalmpl.interpreter.TypeEvaluator.Visitor __eval) {
	
		   return this.getBasicType().__evaluate(new org.rascalmpl.interpreter.BasicTypeEvaluator(__eval.__getEnv(), __eval.getArgumentTypes(this.getArguments()), null));
		
}

}
static public class Ambiguity extends org.rascalmpl.ast.StructuredType.Ambiguity {


public Ambiguity (org.eclipse.imp.pdb.facts.INode __param1,java.util.List<org.rascalmpl.ast.StructuredType> __param2) {
	super(__param1,__param2);
}
@Override
public <T>  T __evaluate(org.rascalmpl.ast.NullASTVisitor<T> __eval) {
	 return null; 
}

}
}